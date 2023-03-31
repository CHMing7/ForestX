package com.chm.plugin.idea.forestx.tw

import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.extension.findNode
import com.chm.plugin.idea.forestx.extension.findNodeOrNew
import com.chm.plugin.idea.forestx.extension.getAllChildren
import com.chm.plugin.idea.forestx.extension.removeNode
import com.chm.plugin.idea.forestx.utils.checkExist
import com.google.common.collect.Maps
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScopesCore
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.AnnotationTargetsSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.concurrency.Invoker
import com.intellij.util.ui.tree.TreeUtil
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.compress.utils.Lists
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

/**
 * @author CHMing
 * @date 2023-03-18
 **/
class RightSidebarUpdater(private val project: Project) {

    companion object {
        private val UPDATER_MAP: MutableMap<Project, RightSidebarUpdater> = Maps.newConcurrentMap()

        @JvmStatic
        fun getInstance(project: Project): RightSidebarUpdater {
            return UPDATER_MAP.computeIfAbsent(project, ::RightSidebarUpdater)
        }
    }

    init {
        UPDATER_MAP[project] = this
    }

    private val toolWindow: RightSidebarToolWindow = RightSidebarToolWindow.getInstance(project)

    private val invoker: Invoker = toolWindow.invoker

    private val psiClassCacheMap = mutableMapOf<String, PsiClass>()

    fun updateFromRoot() {
        ApplicationManager.getApplication().invokeLater {
            doRescanClassAndProcess()
        }
    }

    fun updateFromElement(element: PsiElement) {
        if (element is PsiClass) {
            ApplicationManager.getApplication().invokeLater {
                processClass(element)
                element.runCatching {
                    // 检查节点是否有效
                    val psiClassCollection = PsiTreeUtil.findChildrenOfType(this, PsiClass::class.java)
                    psiClassCollection.remove(this)
                    psiClassCollection.forEach {
                        processClass(it, true)
                    }
                }
            }
        }
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
    private fun doScanClassAndProcess() {
        // 处理过的class
        val pendingProcessClassSet = doScanClass()
        // 处理class
        pendingProcessClassSet.forEach { psiClass ->
            // 处理
            this.runCatching {
                processClass(psiClass)
            }
        }
    }

    /**
     * 重新扫描类并处理
     */
    fun doRescanClassAndProcess() {
        val root = toolWindow.treeModel.root as DefaultMutableTreeNode
        // 清空树结构
        val listChildren = TreeUtil.listChildren(root)
        listChildren.forEach {
            TreeUtil.removeLastPathComponent(toolWindow.mainTree, TreeUtil.getPath(root, it))
        }
        // mainTree.removeAll()
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
    private fun processClass(
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
        val currentModule = ModuleUtil.findModuleForPsiElement(psiClass)
        val psiMethodList = methodsFilter(psiClass)
        if (currentModule == null) {
            return
        }
        val treeModel = toolWindow.treeModel
        val root = treeModel.root as DefaultMutableTreeNode

        val module: DefaultMutableTreeNode = if (toolWindow.isOnlyOneModule()) {
            root
        } else {
            root.findNodeOrNew(treeModel, currentModule)
        }
        // 若没有可用forest方法则清除接口类
        if (CollectionUtils.isEmpty(psiMethodList)) {
            val clazz = module.findNode(psiClass)
            clazz?.let {
                TreeUtil.removeLastPathComponent(toolWindow.mainTree, TreePath(clazz.path))
            }
            // 若该模块下没有forest接口类，则清除该模块
            if (!toolWindow.isOnlyOneModule() && module.childCount == 0) {
                TreeUtil.removeLastPathComponent(toolWindow.mainTree, TreePath(module.path))
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
                toolWindow.mainTree.removeNode(child)
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
                    TreeUtil.removeLastPathComponent(toolWindow.mainTree, TreePath(findClass.path))
                }
            }
        }
    }


    private fun deletePsiClass(psiClass: PsiClass) {
        val clazz = toolWindow.mainTree.findNode(psiClass)
        clazz?.let {
            val modulePath = TreePath(clazz.path).parentPath
            val module = clazz.parent as DefaultMutableTreeNode
            TreeUtil.removeLastPathComponent(toolWindow.mainTree, TreePath(clazz.path))
            // 若该模块下没有forest接口类，则清除该模块
            if (!toolWindow.isOnlyOneModule() && module.childCount == 0) {
                TreeUtil.removeLastPathComponent(toolWindow.mainTree, modulePath)
            }
        }
    }

    private fun methodsFilter(psiClass: PsiClass): List<PsiMethod> {
        // 非接口类或者final标记的直接跳过
        if (!psiClass.isInterface ||
            psiClass.hasModifierProperty(PsiModifier.FINAL)
        ) {
            return Lists.newArrayList()
        }
        val psiMethods: MutableList<PsiMethod> = Lists.newArrayList()
        val methods = psiClass.methods
        methodForeach@ for (method in methods) {
            //　静态方法与默认方法跳过
            if (isStaticOrDefault(method)) {
                continue
            }
            for (annotation in Annotation.FOREST_METHOD_ANNOTATION) {
                val methodAnnotation = AnnotationUtil.findAnnotation(method, annotation.qualifiedName)
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
        return method.hasModifierProperty(PsiModifier.STATIC) || method.hasModifierProperty(PsiModifier.DEFAULT)
    }
}