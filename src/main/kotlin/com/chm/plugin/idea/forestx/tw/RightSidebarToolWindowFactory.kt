package com.chm.plugin.idea.forestx.tw

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class RightSidebarToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val disposable = Disposer.newDisposable()
        val mainForm = project.getRightSidebar()
        val content = ContentFactory.getInstance().createContent(mainForm.getContent(disposable), "", false)
        content.setDisposer(disposable)
        toolWindow.contentManager.addContent(content)
    }
}