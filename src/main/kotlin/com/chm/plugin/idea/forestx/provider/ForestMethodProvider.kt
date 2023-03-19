package com.chm.plugin.idea.forestx.provider

import com.chm.plugin.idea.forestx.startup.ForestCheckTask
import com.chm.plugin.idea.forestx.tw.RightSidebarUpdater
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtil

/**
 * @author CHMing
 * @version v1.0
 * @since 2022-08-25
 **/
class ForestMethodProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element !is PsiClass) {
            return
        }
        if (ForestCheckTask.getInstance(PsiUtil.getProjectInReadAction(element))?.isCheckFinish() != true) {
            // 初始化未完成
            return
        }
        val project = element.getProject()
        val updater = RightSidebarUpdater.getInstance(project)
        updater.updateFromElement(element)
    }
}