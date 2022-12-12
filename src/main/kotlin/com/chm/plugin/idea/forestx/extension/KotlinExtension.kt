package com.chm.plugin.idea.forestx.utils

import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.tw.getRightSidebar
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifierListOwner
import com.intellij.psi.search.GlobalSearchScope
import java.util.*

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/

/**
 * Find clazz optional.
 *
 * @param clazzName the clazz name
 * @return the optional
 */
fun Project.findClazz(clazzName: String): PsiClass? {
    return JavaPsiFacade.getInstance(this).findClass(clazzName, GlobalSearchScope.allScope(this))
        ?: this.getRightSidebar().findCachePsiClass(clazzName)
}

/**
 * Find clazz optional.
 *
 * @param project   the project
 * @return the optional
 */
fun String.findClazz(project: Project): PsiClass? {
    return project.findClazz(this)
}

/**
 * Is annotation present boolean.
 *
 * @param annotation the annotation
 * @return the boolean
 */
fun PsiModifierListOwner.isAnnotationPresent(annotation: Annotation): Boolean {
    val modifierList = this.modifierList
    return modifierList?.findAnnotation(annotation.qualifiedName) != null
}

/**
 * Is annotation present boolean.
 *
 * @param target     the target
 * @return the boolean
 */
fun Annotation.isAnnotationPresent(target: PsiModifierListOwner): Boolean {
    return target.isAnnotationPresent(this)
}

/**
 * Gets psi annotation.
 *
 * @param annotation the annotation
 * @return the psi annotation
 */
fun PsiModifierListOwner.getPsiAnnotation(annotation: Annotation): Optional<PsiAnnotation> {
    val modifierList = this.modifierList
    return if (null == modifierList) Optional.empty() else Optional.ofNullable(modifierList.findAnnotation(annotation.qualifiedName))
}

/**
 * Gets psi annotation.
 *
 * @param target     the target
 * @param annotation the annotation
 * @return the psi annotation
 */
fun Annotation.getPsiAnnotation(target: PsiModifierListOwner): Optional<PsiAnnotation> {
    return target.getPsiAnnotation(this)
}

/**
 * 检查是否可能被删除或转移位置
 */
fun PsiClass.checkExist(): Boolean {
    return ApplicationManager.getApplication().runReadAction<Boolean> { this.containingFile.isValid }
}

/**
 * 检查是否可能被删除
 */
fun PsiElement.checkExist(): Boolean {
    return ApplicationManager.getApplication().runReadAction<Boolean> { this.isValid }
}