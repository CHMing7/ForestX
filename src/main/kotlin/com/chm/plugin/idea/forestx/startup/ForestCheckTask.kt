package com.chm.plugin.idea.forestx.startup

import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.google.common.collect.Sets
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScopesCore
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.AnnotationTargetsSearch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class ForestCheckTask(project: Project) : Backgroundable(project, "Forest check") {

    companion object {
        val TaskMap = mutableMapOf<Project, ForestCheckTask>()
    }

    init {
        TaskMap[project] = this
    }

    var isCheckFinish = false


    override fun run(indicator: ProgressIndicator) {
        ReadAction.nonBlocking { runCollectors(ProgressIndicatorProvider.getGlobalProgressIndicator()!!) }
            .inSmartMode(project)
            .wrapProgress(indicator)
            .executeSynchronously()
    }

    private fun runCollectors(indicator: ProgressIndicator) {
        val searchedSet: MutableSet<PsiClass> = Sets.newHashSet()
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
                    searchedSet.add(psiModifierListOwner)
                } else if (psiModifierListOwner is PsiMethod) {
                    searchedSet.add(psiModifierListOwner.containingClass!!)
                }
            }
            pendingProcessClassSet.addAll(searchedSet);
        }
        val mainForm = project.getRightSidebar()
        // 处理class
        pendingProcessClassSet.forEachIndexed { i, psiClass ->
            // 处理
            GlobalScope.launch {
                mainForm.channel.send {
                    mainForm.processClass(psiClass)
                }
            }
            indicator.isIndeterminate = false
            indicator.fraction = i.toDouble() / searchedSet.size.toDouble()
        }
//        // 展开所有模块节点
//        mainForm.expandModuleNode()
//        // 添加默认展开模块节点监听器
//        mainForm.addDefaultExpandModuleListener()
        isCheckFinish = true
    }
}

fun Project.getForestCheckTask(): ForestCheckTask? {
    return ForestCheckTask.TaskMap[this]
}