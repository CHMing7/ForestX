package com.chm.plugin.idea.forestx.tw

import com.chm.plugin.idea.forestx.extension.getForestAnnotationUrl
import com.chm.plugin.idea.forestx.extension.getNodeIcon
import com.chm.plugin.idea.forestx.extension.getNodeName
import com.chm.plugin.idea.forestx.utils.checkExist
import com.google.common.collect.Maps
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.tree.TreeVisitor.ByTreePath
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.concurrency.Invoker
import com.intellij.util.ui.tree.TreeModelAdapter
import com.intellij.util.ui.tree.TreeUtil
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
class RightSidebarToolWindow(
    val project: Project,
    disposable: Disposable
) {

    companion object {
        private val RIGHT_SIDEBAR_MAP: MutableMap<Project, RightSidebarToolWindow> = Maps.newConcurrentMap()

        fun getInstance(project: Project, disposable: Disposable = Disposer.newDisposable()): RightSidebarToolWindow {
            return RIGHT_SIDEBAR_MAP.computeIfAbsent(
                project
            ) { RightSidebarToolWindow(project, Invoker.forBackgroundPoolWithReadAction(disposable)) }
        }

    }

    //    val URL_BOLD_ATTRIBUTES = SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, Color(203, 150, 103))
    val TYPE_BOLD_ATTRIBUTES = SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, Color(203, 150, 103))

    val URL_ATTRIBUTES = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, Color(37, 178, 178))

    private var rootPanel: JPanel

    var mainTree: Tree

    private var onlyOneModule = false

    var treeModel: DefaultTreeModel

    val invoker = Invoker.forBackgroundThreadWithReadAction(disposable)

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
                runCatching {
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
        Disposer.register(disposable) { rootPanel.removeAll() }
    }

    /**
     * 增加节点增加事件监听，默认展开模块
     */
    fun addDefaultExpandModuleListener() {
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

    fun getContent(): JPanel {
        return rootPanel
    }

    fun isOnlyOneModule() = onlyOneModule
}

