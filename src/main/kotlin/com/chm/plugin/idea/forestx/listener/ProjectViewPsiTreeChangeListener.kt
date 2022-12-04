package com.chm.plugin.idea.forestx.listener

import com.chm.plugin.idea.forestx.startup.getForestCheckTask
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author CHMing
 * @date 2022-11-30
 **/
class ProjectViewPsiTreeChangeListener(val myProject: Project) : PsiTreeChangeAdapter() {

    override fun childAdded(event: PsiTreeChangeEvent) {
        event.child?.let {
            processElement(it)
        }
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        event.child?.let {
            processElement(it)
        }
    }

    /**
     * 递归处理新增节点
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun processElement(element: PsiElement) {
        if (element.project.getForestCheckTask()?.isCheckFinish != true) {
            // 初始化未完成
            return
        }
        if (element is PsiClass) {
            val mainForm = myProject.getRightSidebar()
            GlobalScope.launch {
                mainForm.channel.send {
                    mainForm.processClass(element)
                }
            }
        }
        element.children.forEach { processElement(it) }
    }

    internal class MyStartupActivity : StartupActivity {

        override fun runActivity(project: Project) {
            val disposable = Disposer.newDisposable()
            PsiManager.getInstance(project)
                .addPsiTreeChangeListener(ProjectViewPsiTreeChangeListener(project), disposable)
        }

    }
}