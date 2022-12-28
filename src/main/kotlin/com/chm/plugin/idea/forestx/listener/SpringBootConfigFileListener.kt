package com.chm.plugin.idea.forestx.listener

import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil
import com.chm.plugin.idea.forestx.template.utils.SpringBootConfigFileUtil
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import org.jetbrains.yaml.psi.YAMLFile

/**
 * 监听spring boot项目的yaml、yml、properties文件的创建、删除、移动
 * @author CHMing
 * @date 2022-12-29
 **/
class SpringBootConfigFileListener(private val myProject: Project) : PsiTreeChangeAdapter() {

    override fun childAdded(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    override fun childMoved(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    override fun childrenChanged(event: PsiTreeChangeEvent) {
        onChange(event)
    }

    private fun onChange(event: PsiTreeChangeEvent) {
        event.runCatching {
            val child = this.child
            if (child is YAMLFile) {
                val filePath = child.virtualFile.path
                val isTestSourceFile = ForestTemplateUtil.isTestFile(filePath)
                SpringBootConfigFileUtil.reloadSpringBootConfigFiles(myProject, isTestSourceFile)
            } else if (child is PropertiesFile) {
                val filePath = child.virtualFile.path
                val isTestSourceFile = ForestTemplateUtil.isTestFile(filePath)
                SpringBootConfigFileUtil.reloadSpringBootConfigFiles(myProject, isTestSourceFile)
            }
        }
    }

    internal class MyStartupActivity : StartupActivity {

        override fun runActivity(project: Project) {
            val disposable = Disposer.newDisposable()
            PsiManager.getInstance(project)
                .addPsiTreeChangeListener(SpringBootConfigFileListener(project), disposable)
        }

    }
}