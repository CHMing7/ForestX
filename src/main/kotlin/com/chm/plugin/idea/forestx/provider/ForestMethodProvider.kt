package com.chm.plugin.idea.forestx.provider

import com.chm.plugin.idea.forestx.startup.getForestCheckTask
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class ForestMethodProvider : RelatedItemLineMarkerProvider() {

    @OptIn(DelicateCoroutinesApi::class)
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
        GlobalScope.launch {
            mainForm.channel.send {
                mainForm.processClass(element)
            }
        }
    }
}