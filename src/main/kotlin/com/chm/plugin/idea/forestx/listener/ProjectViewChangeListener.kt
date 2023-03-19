package com.chm.plugin.idea.forestx.listener

import com.chm.plugin.idea.forestx.startup.ForestCheckTask
import com.chm.plugin.idea.forestx.tw.RightSidebarUpdater
import com.intellij.ide.projectView.ProjectViewPsiTreeChangeListener
import com.intellij.ide.projectView.impl.AbstractProjectTreeStructure
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.ide.util.treeView.AbstractTreeUpdater
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiUtil
import javax.swing.tree.DefaultMutableTreeNode

/**
 * @author CHMing
 * @date 2022-11-30
 **/
class ProjectViewChangeListener(private val myProject: Project) : ProjectViewPsiTreeChangeListener(myProject) {

    private val viewPane = ProjectViewPane(myProject)

    private val updater = RightSidebarUpdater(myProject)

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
        if (ForestCheckTask.getInstance(myProject)?.isCheckFinish() != true) {
            // 初始化未完成
            return
        }
        updater.updateFromRoot()
    }

    override fun addSubtreeToUpdateByElement(element: PsiElement): Boolean {
        if (ForestCheckTask.getInstance(PsiUtil.getProjectInReadAction(element))?.isCheckFinish() != true) {
            // 初始化未完成
            return true
        }
        updater.updateFromElement(element)
        return true
    }


    internal class MyStartupActivity : StartupActivity {

        override fun runActivity(project: Project) {
            val disposable = Disposer.newDisposable()
            PsiManager.getInstance(project)
                .addPsiTreeChangeListener(ProjectViewChangeListener(project), disposable)
        }
    }
}