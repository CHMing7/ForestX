package com.chm.plugin.idea.forestx.startup

import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.chm.plugin.idea.forestx.utils.UiUtil
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
        val mainForm = project.getRightSidebar()
        // 处理过的class
        val pendingProcessClassSet = mainForm.doScanClass()
        // 处理class
        pendingProcessClassSet.forEachIndexed { i, psiClass ->
            // 处理
            UiUtil.updateUi {
                mainForm.processClass(psiClass)
            }
            indicator.isIndeterminate = false
            indicator.fraction = i.toDouble() / pendingProcessClassSet.size.toDouble()
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