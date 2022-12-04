package com.chm.plugin.idea.forestx.utils

import com.intellij.openapi.application.ApplicationManager


/**
 * @author CHMing
 * @date 2022-12-02
 **/
object UiUtil {

    fun updateUi(runnable: () -> Unit) {
        val application = ApplicationManager.getApplication()
        if (application.isDispatchThread) {
            application.runWriteAction(runnable)
        } else {
            application.invokeLater { application.runWriteAction(runnable) }
        }
    }

    fun readUi(runnable: () -> Unit) {
        val application = ApplicationManager.getApplication()
        if (application.isDispatchThread) {
            application.runReadAction(runnable)
        } else {
            application.invokeLater { application.runReadAction(runnable) }
        }
    }
}