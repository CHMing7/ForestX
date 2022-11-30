package com.chm.plugin.idea.forestx.listener

import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.psi.*

/**
 * @author CHMing
 * @date 2022-11-30
 **/
class ProjectViewPsiTreeChangeListener(val myProject: Project) : PsiTreeChangeAdapter() {

    override fun childAdded(event: PsiTreeChangeEvent) {
        event.child?.let { processElement(it) }
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        event.child?.let { processElement(it) }
    }

    /**
     * 递归处理新增节点
     */
    fun processElement(element: PsiElement) {
        if (element is PsiClass) {
            val mainForm = myProject.getRightSidebar()
            mainForm.processClass(element)
        }
        element.children?.forEach { processElement(it) }
    }

    internal class MyStartupActivity : StartupActivity {

        override fun runActivity(project: Project) {
            val disposable = Disposer.newDisposable()
            PsiManager.getInstance(project)
                .addPsiTreeChangeListener(ProjectViewPsiTreeChangeListener(project), disposable)
        }

    }
}