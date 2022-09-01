package com.chm.plugin.idea.forestx.provider

import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement

/**
 * @author caihongming
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
        val project = element.getProject()
        val mainForm = project.getRightSidebar()
        mainForm.processClass(element)
    }
}