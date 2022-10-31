package com.chm.plugin.idea.forestx.tw

import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.utils.*
import com.google.common.collect.Maps
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.tree.TreeVisitor
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeModelAdapter
import com.intellij.util.ui.tree.TreeUtil
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.compress.utils.Lists
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class RightSidebarToolWindow(project: Project) {

    private var rootPanel: JPanel

    private var mainTree: Tree

    private var onlyOneModule = false

    private var treeModel: DefaultTreeModel

    init {
        val modules = ModuleManager.getInstance(project).modules
        val root: DefaultMutableTreeNode
        if (modules.size == 1 && modules[0].name == project.name) {
            // 没子模块的时候不显示项目节点
            root = DefaultMutableTreeNode(modules[0])
            onlyOneModule = true
        } else {
            root = DefaultMutableTreeNode(project)
        }
        treeModel = DefaultTreeModel(root)
        mainTree = Tree(treeModel)
        TreeSpeedSearch(mainTree)
        // 自定义样式
        val coloredTreeCellRenderer: ColoredTreeCellRenderer = object : ColoredTreeCellRenderer() {
            override fun customizeCellRenderer(
                tree: JTree,
                value: Any,
                selected: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ) {
                val name = value.getNodeName()
                append(name)
                val icon = value.getNodeIcon()
                setIcon(icon)
            }
        }
        mainTree.cellRenderer = coloredTreeCellRenderer
        mainTree.addMouseListener(
            object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.clickCount == 2 && e.button == MouseEvent.BUTTON1) {
                        val selectionPath = mainTree.selectionPath ?: return
                        val lastPathComponent = selectionPath.lastPathComponent
                        if (lastPathComponent is DefaultMutableTreeNode) {
                            val o = lastPathComponent.userObject
                            if (o is PsiMethod) {
                                val psiFile = o.containingFile
                                // 打开文件
                                FileEditorManager.getInstance(o.project).openFile(psiFile.virtualFile, true)
                                val editor = PsiEditorUtil.findEditor(o) ?: return
                                val selectionModel = editor.selectionModel
                                selectionModel.removeSelection(true)
                                // 移动光标
                                val caretModel = editor.caretModel
                                caretModel.moveToOffset(o.textOffset)
                                // 滚动屏幕
                                val scrollingModel = editor.scrollingModel
                                scrollingModel.scrollTo(caretModel.logicalPosition, ScrollType.CENTER)
                            }
                        }
                    }
                }
            })
        // 增加节点增加事件监听，默认展开模块
        treeModel.addTreeModelListener(TreeModelAdapter.create { event, type ->
            if (type == TreeModelAdapter.EventType.NodesInserted) {
                event.children.forEach { c ->
                    if (c is DefaultMutableTreeNode) {
                        val o = c.userObject
                        if (o is Module || (o is PsiClass && c.parent.childCount == 1)) {
                            val cPath = if (o is Module) TreePath(c.path) else TreePath(c.path).parentPath
                            TreeUtil.expand(
                                mainTree,
                                { path ->
                                    if (cPath == path)
                                        TreeVisitor.Action.INTERRUPT
                                    else
                                        TreeVisitor.Action.CONTINUE
                                },
                                { }
                            )
                        }
                    }
                }
            }
        })

        // 工具栏
        val toolbarDecorator = ToolbarDecorator.createDecorator(mainTree)
        rootPanel = JPanel()
        rootPanel.layout = BorderLayout()
        rootPanel.add(toolbarDecorator.createPanel(), BorderLayout.CENTER)
    }

    fun getContent(disposable: Disposable?): JPanel {
        Disposer.register(disposable!!) { rootPanel.removeAll() }
        return rootPanel
    }

    fun getMainTree(): Tree {
        return mainTree
    }

    private fun getTreeModel(): DefaultTreeModel {
        return treeModel
    }

    fun processClass(psiClass: PsiClass) {
        val currentModule = ModuleUtil.findModuleForPsiElement(psiClass)
        val psiMethodList = methodsFilter(psiClass)
        if (currentModule == null) {
            return
        }
        val rootModel = getTreeModel()
        val root = rootModel.root as DefaultMutableTreeNode

        val module: DefaultMutableTreeNode = if (onlyOneModule) {
            root
        } else {
            root.findNodeOrNew(rootModel, currentModule, true)
        }
        // 若没有可用forest方法则清除接口类
        if (CollectionUtils.isEmpty(psiMethodList)) {
            val clazz = module.findNode(psiClass)
            if (clazz != null) {
                module.removeNode(rootModel, clazz)
            }
            // 若该模块下没有forest接口类，则清除该模块
            if (!onlyOneModule && module.childCount == 0) {
                root.removeNode(rootModel, module)
            }
            return
        }
        val clazz = module.findNodeOrNew(rootModel, psiClass, true)
        // 清空接口类中原先的method,先增加新子节点，再删除旧子节点
        val methodList: MutableList<DefaultMutableTreeNode> = Lists.newArrayList()
        for (i in psiMethodList.indices) {
            val psiMethod = psiMethodList[i]
            val method = clazz.moveOrInsert(rootModel, psiMethod, i)
            methodList.add(method)
        }
        val allChildren = clazz.getAllChildren()
        for (child in allChildren) {
            if (!methodList.contains(child)) {
                clazz.removeNode(rootModel, child)
            }
        }
    }

    private fun methodsFilter(psiClass: PsiClass): List<PsiMethod> {
        if (!psiClass.isInterface || psiClass.modifierList?.hasModifierProperty(PsiModifier.FINAL) == true) {
            return Lists.newArrayList()
        }
        val psiMethods: MutableList<PsiMethod> = Lists.newArrayList()
        val methods = psiClass.methods
        for (method in methods) {
            if (isStaticOrDefault(method)) {
                continue
            }
            val methodAnnotations = AnnotationUtil.getAllAnnotations(method, false, null)
            for (methodAnnotation in methodAnnotations) {
                for (annotation in Annotation.FOREST_METHOD_ANNOTATION) {
                    if (methodAnnotation.qualifiedName == annotation.qualifiedName) {
                        psiMethods.add(method)
                    }
                }
            }
        }
        return psiMethods
    }

    private fun isStaticOrDefault(method: PsiMethod): Boolean {
        return method.hasModifierProperty(PsiModifier.STATIC) || method.hasModifierProperty(PsiModifier.DEFAULT)
    }

}

var PROJECT_RIGHT_SIDEBAR_MAP: MutableMap<Project, RightSidebarToolWindow> = Maps.newConcurrentMap()

fun Project.getRightSidebar(): RightSidebarToolWindow {
    return PROJECT_RIGHT_SIDEBAR_MAP.computeIfAbsent(this, ::RightSidebarToolWindow)
}