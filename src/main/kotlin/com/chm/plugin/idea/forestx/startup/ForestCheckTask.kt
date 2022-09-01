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

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class ForestCheckTask(project: Project) : Backgroundable(project, "Forest check") {

    override fun run(indicator: ProgressIndicator) {
        ReadAction.nonBlocking { runCollectors(ProgressIndicatorProvider.getGlobalProgressIndicator()!!) }
            .inSmartMode(myProject)
            .wrapProgress(indicator)
            .executeSynchronously()
    }

    private fun runCollectors(indicator: ProgressIndicator) {
        val searchedSet: MutableSet<PsiClass> = Sets.newHashSet()
        // 搜索lib包
        val librariesScope = ProjectScope.getLibrariesScope(myProject)
        // 搜索项目里
        val inheritorsScope: SearchScope =
            GlobalSearchScopesCore.projectProductionScope(myProject).union(librariesScope)

        for (annotation in Annotation.FOREST_METHOD_ANNOTATION) {
            // 获取forest注解类
            val annotationClass =
                JavaPsiFacade.getInstance(myProject).findClass(annotation.qualifiedName, librariesScope)
                    ?: continue
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
            val mainForm = myProject.getRightSidebar()
            // 处理
            for ((i, psiClass) in searchedSet.withIndex()) {
                mainForm.processClass(psiClass)
                indicator.isIndeterminate = false
                indicator.fraction = i.toDouble() / searchedSet.size.toDouble()
            }
        }
    }
}