package com.chm.plugin.idea.forestx.startup

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.profile.ProfileChangeAdapter
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import com.intellij.util.concurrency.AppExecutorUtil

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class MyStartupActivity : StartupActivity {

    override fun runActivity(project: Project) {
        val application = ApplicationManager.getApplication()
        if (!application.isUnitTestMode && !application.isHeadlessEnvironment) {
            ReadAction.nonBlocking {
                if (ProjectInspectionProfileManager.getInstance(project).isCurrentProfileInitialized()) {
                    queueTask(project)
                } else {
                    project.messageBus.connect()
                        .subscribe(ProfileChangeAdapter.TOPIC, object : ProfileChangeAdapter {
                            override fun profilesInitialized() {
                                queueTask(project)
                            }
                        })
                }
            }.inSmartMode(project).submit(AppExecutorUtil.getAppExecutorService())
        }
    }

    private fun queueTask(project: Project) {
        ApplicationManager.getApplication().invokeLater({ ForestCheckTask(project).queue() }, project.disposed)
    }
}