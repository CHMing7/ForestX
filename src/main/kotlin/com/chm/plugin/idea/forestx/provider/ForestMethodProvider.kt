package com.chm.plugin.idea.forestx.provider

import com.chm.plugin.idea.forestx.startup.getForestCheckTask
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.chm.plugin.idea.forestx.utils.UiUtil
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement

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
        if (element.project.getForestCheckTask()?.isCheckFinish != true) {
            // 初始化未完成
            return
        }
        val project = element.getProject()
        val mainForm = project.getRightSidebar()
        UiUtil.updateUi {
            mainForm.processClass(element)
        }
    }
}