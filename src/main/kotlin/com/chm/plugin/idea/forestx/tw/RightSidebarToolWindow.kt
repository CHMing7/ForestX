package com.chm.plugin.idea.forestx.tw

import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.extension.findNode
import com.chm.plugin.idea.forestx.extension.findNodeOrNew
import com.chm.plugin.idea.forestx.extension.getAllChildren
import com.chm.plugin.idea.forestx.extension.getForestAnnotationUrl
import com.chm.plugin.idea.forestx.extension.getNodeIcon
import com.chm.plugin.idea.forestx.extension.getNodeName
import com.chm.plugin.idea.forestx.extension.removeNode
import com.chm.plugin.idea.forestx.utils.ReadActionUtil
import com.chm.plugin.idea.forestx.utils.UiUtil
import com.chm.plugin.idea.forestx.utils.checkExist
import com.google.common.collect.Maps
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScopesCore
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.AnnotationTargetsSearch
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.tree.TreeVisitor.ByTreePath
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeModelAdapter
import com.intellij.util.ui.tree.TreeUtil
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.compress.utils.Lists
import java.awt.BorderLayout
import java.awt.Color
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
class RightSidebarToolWindow(val project: Project) {

    //    val URL_BOLD_ATTRIBUTES = SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, Color(203, 150, 103))
    val TYPE_BOLD_ATTRIBUTES = SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, Color(203, 150, 103))

    val URL_ATTRIBUTES = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, Color(37, 178, 178))


    private var rootPanel: JPanel

    private var mainTree: Tree

    private var onlyOneModule = false

    private var treeModel: DefaultTreeModel

    private val psiClassCacheMap = mutableMapOf<String, PsiClass>()

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
                if (value !is DefaultMutableTreeNode) {
                    return
                }
                val o: Any = value.userObject
                if (o is PsiClass || o is PsiMethod) {
                    o as PsiElement
                    if (!o.checkExist()) {
                        return
                    }
                }
                val name = value.getNodeName()
                append(" ")

                if (o is Project || o is Module || o is PsiClass) {
                    append(name, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)
                } else {
                    append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
                }

                if (o is PsiClass) {
                    // 类增加鼠标悬停显示全路径名
                    toolTipText = o.qualifiedName
                }

                val forestAnnotationUrl = value.getForestAnnotationUrl()
                if (forestAnnotationUrl.isNotBlank()) {
                    // 节点名跟url之间增加空格
                    append("  ")
                    append(forestAnnotationUrl, SimpleTextAttributes.GRAYED_BOLD_ATTRIBUTES)
                }
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

        // 工具栏
        val toolbarDecorator = ToolbarDecorator.createDecorator(mainTree)
        rootPanel = JPanel()
        rootPanel.layout = BorderLayout()
        rootPanel.add(toolbarDecorator.createPanel(), BorderLayout.CENTER)
    }

    /**
     * 增加节点增加事件监听，默认展开模块
     */
    fun addDefaultExpandModuleListener() {
        //
        treeModel.addTreeModelListener(TreeModelAdapter.create { event, type ->
            if (type == TreeModelAdapter.EventType.NodesInserted) {
                event.children.forEach { c ->
                    if (c is DefaultMutableTreeNode) {
                        val o = c.userObject
                        if (o is PsiClass && c.parent.childCount == 1) {
                            val cPath = TreePath(c.path).parentPath
                            TreeUtil.promiseExpand(mainTree, ByTreePath(cPath) { node: Any? -> node })
                        }
                    }
                }
            }
        })
    }

    /**
     * 展开所有模块节点
     */
    fun expandModuleNode() {
        TreeUtil.promiseExpand(mainTree, if (onlyOneModule) 1 else 2)
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

    fun findCachePsiClass(qualifiedName: String): PsiClass? {
        return psiClassCacheMap[qualifiedName]
    }

    fun doScanClass(): Set<PsiClass> {
        // 搜索lib包
        val librariesScope = ProjectScope.getLibrariesScope(project)
        // 搜索项目里
        val inheritorsScope: SearchScope =
            GlobalSearchScopesCore.projectProductionScope(project).union(librariesScope)
        val facade: JavaPsiFacade = JavaPsiFacade.getInstance(project)
        // 处理过的class
        val pendingProcessClassSet = mutableSetOf<PsiClass>()
        for (annotation in Annotation.FOREST_METHOD_ANNOTATION) {
            // 获取forest注解类
            val annotationClass = facade.findClass(annotation.qualifiedName, librariesScope) ?: continue
            // 搜索被forest注解标记的类
            val annotationTargets = AnnotationTargetsSearch.search(annotationClass, inheritorsScope)
            val all = annotationTargets.findAll()
            // 搜集搜索到的类
            for (psiModifierListOwner in all) {
                if (psiModifierListOwner is PsiClass) {
                    pendingProcessClassSet.add(psiModifierListOwner)
                } else if (psiModifierListOwner is PsiMethod) {
                    pendingProcessClassSet.add(psiModifierListOwner.containingClass!!)
                }
            }
        }
        return pendingProcessClassSet
    }

    /**
     * 扫描类并处理
     */
    fun doScanClassAndProcess() {
        // 处理过的class
        val pendingProcessClassSet = doScanClass()
        // 处理class
        pendingProcessClassSet.forEach { psiClass ->
            // 处理
            UiUtil.updateUi {
                this.runCatching {
                    processClass(psiClass)
                }
            }
        }
    }

    /**
     * 重新扫描类并处理
     */
    fun doRescanClassAndProcess() {
        // 清空树结构
        mainTree.removeAll()
        // 扫描并处理
        doScanClassAndProcess()
    }

    /**
     * 处理class
     *
     * @param psiClass 待处理的class
     * @param deleteInOtherModule 是否删除其他module中的此class
     */
    @Synchronized
    fun processClass(
        psiClass: PsiClass,
        deleteInOtherModule: Boolean = false
    ) {
        if (!psiClass.checkExist()) {
            deletePsiClass(psiClass)
            return
        }
        psiClass.qualifiedName?.let {
            psiClassCacheMap[it] = psiClass
        }
        val currentModule = ReadActionUtil.findModuleForPsiElement(psiClass)
        val psiMethodList = methodsFilter(psiClass)
        if (currentModule == null) {
            return
        }
        val treeModel = getTreeModel()
        val root = treeModel.root as DefaultMutableTreeNode

        val module: DefaultMutableTreeNode = if (onlyOneModule) {
            root
        } else {
            root.findNodeOrNew(treeModel, currentModule)
        }
        // 若没有可用forest方法则清除接口类
        if (CollectionUtils.isEmpty(psiMethodList)) {
            val clazz = module.findNode(psiClass)
            clazz?.let {
                TreeUtil.removeLastPathComponent(mainTree, TreePath(clazz.path))
            }
            // 若该模块下没有forest接口类，则清除该模块
            if (!onlyOneModule && module.childCount == 0) {
                TreeUtil.removeLastPathComponent(mainTree, TreePath(module.path))
            }
            return
        }
        val clazz = module.findNodeOrNew(treeModel, psiClass)
        // 清空接口类中原先的method,先增加新子节点，再删除旧子节点
        val methodList: MutableList<DefaultMutableTreeNode> = Lists.newArrayList()
        for (i in psiMethodList.indices) {
            val psiMethod = psiMethodList[i]
            var method = clazz.findNode(psiMethod)
            if (method == null) {
                method = DefaultMutableTreeNode(psiMethod)
                treeModel.insertNodeInto(method, clazz, i)
            }
            methodList.add(method)
        }
        val allChildren = clazz.getAllChildren()
        for (child in allChildren) {
            if (child !in methodList) {
                mainTree.removeNode(child)
            }
        }
        // 可能class在其他module中存在，删除
        if (deleteInOtherModule) {
            // 所有模块
            val modules = ModuleManager.getInstance(project).modules
            modules.filter { it != currentModule }.forEach {
                // 在侧边栏结构中找到此模块所在节点
                val otherModule = root.findNode(it) ?: return@forEach
                // 找到class在这模块中的节点
                val findClass = otherModule.findNode(psiClass)
                // 在这模块中删除class节点
                findClass?.let {
                    TreeUtil.removeLastPathComponent(mainTree, TreePath(findClass.path))
                }
            }
        }
    }

    private fun deletePsiClass(psiClass: PsiClass) {
        val clazz = mainTree.findNode(psiClass)
        clazz?.let {
            val modulePath = TreePath(clazz.path).parentPath
            val module = clazz.parent as DefaultMutableTreeNode
            TreeUtil.removeLastPathComponent(mainTree, TreePath(clazz.path))
            // 若该模块下没有forest接口类，则清除该模块
            if (!onlyOneModule && module.childCount == 0) {
                TreeUtil.removeLastPathComponent(mainTree, modulePath)
            }
        }
    }

    private fun methodsFilter(psiClass: PsiClass): List<PsiMethod> {
        // 非接口类或者final标记的直接跳过
        if (!ReadActionUtil.isInterface(psiClass) ||
            ReadActionUtil.hasModifierProperty(psiClass, PsiModifier.FINAL)
        ) {
            return Lists.newArrayList()
        }
        val psiMethods: MutableList<PsiMethod> = Lists.newArrayList()
        val methods = ReadActionUtil.runReadAction<Array<PsiMethod>> { psiClass.methods }
        methodForeach@ for (method in methods) {
            //　静态方法与默认方法跳过
            if (isStaticOrDefault(method)) {
                continue
            }
            for (annotation in Annotation.FOREST_METHOD_ANNOTATION) {
                val methodAnnotation = ReadActionUtil.findAnnotation(method, annotation.qualifiedName)
                if (methodAnnotation != null) {
                    psiMethods.add(method)
                    //跳出多重循环
                    continue@methodForeach
                }
            }
        }
        return psiMethods
    }

    private fun isStaticOrDefault(method: PsiMethod): Boolean {
        return ReadActionUtil.hasAnyModifierProperty(method, PsiModifier.STATIC, PsiModifier.DEFAULT)
    }

}

var PROJECT_RIGHT_SIDEBAR_MAP: MutableMap<Project, RightSidebarToolWindow> = Maps.newConcurrentMap()

fun Project.getRightSidebar(): RightSidebarToolWindow {
    return PROJECT_RIGHT_SIDEBAR_MAP.computeIfAbsent(this, ::RightSidebarToolWindow)
}
