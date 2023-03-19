package com.chm.plugin.idea.forestx.startup

import com.chm.plugin.idea.forestx.tw.RightSidebarToolWindow
import com.chm.plugin.idea.forestx.tw.RightSidebarUpdater
import com.google.common.collect.Maps
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class ForestCheckTask(project: Project) : Backgroundable(project, "Forest check") {

    companion object {
        val TaskMap: MutableMap<Project, ForestCheckTask> = Maps.newConcurrentMap()

        @JvmStatic
        fun getInstance(project: Project): ForestCheckTask? {
            return TaskMap[project]
        }
    }

    init {
        TaskMap[project] = this
    }

    private var checkFinish = false

    private val updater = RightSidebarUpdater.getInstance(project)


    override fun run(indicator: ProgressIndicator) {
        ReadAction.nonBlocking { runCollectors(ProgressIndicatorProvider.getGlobalProgressIndicator()!!) }
            .inSmartMode(project)
            .wrapProgress(indicator)
            .executeSynchronously()
    }

    private fun runCollectors(indicator: ProgressIndicator) {
        checkFinish = false
        // 处理过的class
        val pendingProcessClassSet = updater.doScanClass()
        // 处理class
        pendingProcessClassSet.forEachIndexed { i, psiClass ->
            // 处理
            updater.updateFromElement(psiClass)
            indicator.isIndeterminate = false
            indicator.fraction = i.toDouble() / pendingProcessClassSet.size.toDouble()
        }
        checkFinish = true
    }

    fun isCheckFinish() = checkFinish
}

