package com.chm.plugin.idea.forestx.listener

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent


/**
 * @author CHMing
 * @date 2022-11-29
 **/
class MyPsiTreeChangeListener : PsiTreeChangeAdapter() {


    override fun beforeChildRemoval(event: PsiTreeChangeEvent) {

        println(event)
    }
}