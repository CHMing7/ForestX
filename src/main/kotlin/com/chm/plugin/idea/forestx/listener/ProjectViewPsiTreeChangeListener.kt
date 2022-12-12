package com.chm.plugin.idea.forestx.listener

import com.chm.plugin.idea.forestx.startup.getForestCheckTask
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.chm.plugin.idea.forestx.utils.UiUtil
import com.chm.plugin.idea.forestx.utils.findClazz
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.impl.PsiManagerImpl
import com.intellij.psi.impl.source.PsiJavaFileImpl
import com.intellij.psi.impl.source.tree.java.PsiIdentifierImpl
import com.intellij.psi.util.ClassUtil

/**
 * @author CHMing
 * @date 2022-11-30
 **/
class ProjectViewPsiTreeChangeListener(private val myProject: Project) : PsiTreeChangeAdapter() {

    override fun childAdded(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
        if (event.parent is PsiClass &&
            event.child is PsiIdentifier
        ) {
            val packageName = (event.parent.parent as PsiJavaFileImpl).packageName
            val oldClass = myProject.findClazz("${packageName}.${(event.oldChild as PsiIdentifierImpl).text}")
            val newClass = myProject.findClazz("${packageName}.${(event.newChild as PsiIdentifierImpl).text}")
            oldClass?.let {
                processElement(it)
            }
            newClass?.let {
                processElement(it)
            }
        }
    }

    override fun childMoved(event: PsiTreeChangeEvent) {
        // 清楚新节点
        event.child?.let {
            processElement(it, true)
        }
    }

    private fun onChange(event: PsiTreeChangeEvent) {
        event.child?.let {
            processElement(it)
        }
    }

    /**
     * 递归处理新增节点
     */
    private fun processElement(
        element: PsiElement,
        deleteInOtherModule: Boolean = false
    ) {
        if (myProject.getForestCheckTask()?.isCheckFinish != true) {
            // 初始化未完成
            return
        }
        if (element is PsiClass) {
            val mainForm = myProject.getRightSidebar()
            UiUtil.updateUi {
                mainForm.processClass(element, deleteInOtherModule)
            }
        }
        element.runCatching {
            // 检查节点是否有效
            element.children.forEach { processElement(it, deleteInOtherModule) }
        }
    }

    internal class MyStartupActivity : StartupActivity {

        override fun runActivity(project: Project) {
            val disposable = Disposer.newDisposable()
            PsiManager.getInstance(project)
                .addPsiTreeChangeListener(ProjectViewPsiTreeChangeListener(project), disposable)
        }

    }
}