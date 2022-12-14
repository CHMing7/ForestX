package com.chm.plugin.idea.forestx.utils

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.util.Computable
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierListOwner

/**
 * @author CHMing
 * @date 2022-12-04
 **/
object ReadActionUtil {

    inline fun <reified T> runReadAction(computation: Computable<T>): T {
        return ApplicationManager.getApplication().runReadAction<T>(computation)
    }

    fun getAllAnnotations(
        owner: PsiModifierListOwner,
        inHierarchy: Boolean,
        visited: MutableSet<in PsiModifierListOwner>?
    ): Array<PsiAnnotation> {
        return runReadAction {
            AnnotationUtil.getAllAnnotations(owner, inHierarchy, visited)
        }
    }

    fun findAnnotation(listOwner: PsiModifierListOwner, vararg annotationNames: String): PsiAnnotation? {
        return runReadAction {
            AnnotationUtil.findAnnotation(listOwner, *annotationNames)
        }
    }

    fun getStringAttributeValue(anno: PsiAnnotation, attributeName: String): String? {
        return runReadAction {
            AnnotationUtil.getStringAttributeValue(anno, attributeName)
        }
    }

    fun findModuleForPsiElement(element: PsiElement): Module? {
        return runReadAction {
            element.runCatching {
                ModuleUtil.findModuleForPsiElement(element)
            }.getOrElse { null }
        }
    }

    fun hasModifierProperty(owner: PsiModifierListOwner, modifier: String): Boolean {
        return runReadAction {
            owner.hasModifierProperty(modifier)
        }
    }

    fun hasAnyModifierProperty(owner: PsiModifierListOwner, vararg modifiers: String): Boolean {
        return runReadAction {
            modifiers.any {
                owner.hasModifierProperty(it)
            }
        }
    }

    fun getMethods(psiClass: PsiClass): Array<PsiMethod> {
        return runReadAction {
            psiClass.methods
        }
    }

    fun isInterface(psiClass: PsiClass): Boolean {
        return runReadAction {
            psiClass.isInterface
        }
    }

}