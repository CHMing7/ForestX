package com.chm.plugin.idea.forestx.listener

import com.chm.plugin.idea.forestx.startup.getForestCheckTask
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.chm.plugin.idea.forestx.utils.UiUtil
import com.intellij.ide.projectView.ProjectViewPsiTreeChangeListener
import com.intellij.ide.projectView.impl.AbstractProjectTreeStructure
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.ide.util.treeView.AbstractTreeUpdater
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.tree.DefaultMutableTreeNode

/**
 * @author CHMing
 * @date 2022-11-30
 **/
class ProjectViewChangeListener(private val myProject: Project) : ProjectViewPsiTreeChangeListener(myProject) {

    private val viewPane: ProjectViewPane = ProjectViewPane(myProject)

    override fun getUpdater(): AbstractTreeUpdater? {
        return null
    }

    override fun isFlattenPackages(): Boolean {
        val structure = viewPane.treeStructure
        return structure is AbstractProjectTreeStructure && structure.isFlattenPackages
    }

    override fun getRootNode(): DefaultMutableTreeNode? {
        return null
    }


    override fun addSubtreeToUpdateByRoot() {
        val mainForm = myProject.getRightSidebar()
        UiUtil.updateUi {
            mainForm.runCatching {
                mainForm.doRescanClassAndProcess()
            }
        }
    }

    override fun addSubtreeToUpdateByElement(element: PsiElement): Boolean {
        processElement(element)
        return true
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
        val mainForm = myProject.getRightSidebar()
        if (element is PsiClass) {
            UiUtil.updateUi {
                mainForm.runCatching {
                    processClass(element, deleteInOtherModule)
                }
            }
        }
        element.runCatching {
            // 检查节点是否有效
            println("element: $element")
            val psiClassCollection = PsiTreeUtil.findChildrenOfType(this, PsiClass::class.java)
            psiClassCollection.remove(this)
            psiClassCollection.forEach {
                UiUtil.updateUi {
                    mainForm.runCatching {
                        processClass(it, deleteInOtherModule)
                    }
                }
            }
        }
    }

    internal class MyStartupActivity : StartupActivity {

        override fun runActivity(project: Project) {
            val disposable = Disposer.newDisposable()
            PsiManager.getInstance(project)
                .addPsiTreeChangeListener(ProjectViewChangeListener(project), disposable)
        }

    }
}