package com.chm.plugin.idea.forestx.provider

import com.chm.plugin.idea.forestx.Icons
import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.utils.findClazz
import com.chm.plugin.idea.forestx.utils.getPsiAnnotation
import com.chm.plugin.idea.forestx.utils.isAnnotationPresent
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifier
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.apache.commons.lang.StringUtils

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class InjectionLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element !is PsiField) {
            return
        }
        val field = element
        if (!isTargetField(field)) {
            return
        }
        val type = field.type as? PsiClassReferenceType ?: return
        val psiClass = element.getProject().findClazz(type.canonicalText) ?: return
        if (!interfaceFilter(psiClass)) {
            return
        }
        val builder = NavigationGutterIconBuilder.create(Icons.SPRING_INJECTION_ICON)
            .setAlignment(GutterIconRenderer.Alignment.CENTER)
            .setTarget(psiClass)
            .setTooltipTitle("Forest object found - " + psiClass.qualifiedName)
        result.add(builder.createLineMarkerInfo(field.nameIdentifier))
    }

    private fun isTargetField(field: PsiField): Boolean {
        if (field.isAnnotationPresent(Annotation.AUTOWIRED)) {
            return true
        }
        val resourceAnno = field.getPsiAnnotation(Annotation.RESOURCE)
        if (resourceAnno.isPresent) {
            val nameValue = resourceAnno.get().findAttributeValue("name")
            val name = nameValue!!.text.replace("\"".toRegex(), "")
            return StringUtils.isBlank(name) || name == field.name
        }
        return false
    }

    private fun interfaceFilter(psiClass: PsiClass): Boolean {
        if (!psiClass.isInterface ||
            psiClass.hasModifierProperty(PsiModifier.FINAL)
        ) {
            return false
        }
        val superClass = psiClass.interfaces
        var hasSuperForestClient: Boolean
        for (sup in superClass) {
            hasSuperForestClient = interfaceFilter(sup)
            if (hasSuperForestClient) {
                return true
            }
        }
        val annotations = AnnotationUtil.getAllAnnotations(psiClass, false, null)
        for (annotation in annotations) {
            if ("com.dtflys.forest.annotation.ForestClient" == annotation.qualifiedName) {
                return true
            }
            if ("com.dtflys.forest.annotation.BaseRequest" == annotation.qualifiedName) {
                return true
            }
        }
        val methods = psiClass.methods
        for (method in methods) {
            val methodAnnotations = AnnotationUtil.getAllAnnotations(method, false, null)
            for (methodAnnotation in methodAnnotations) {
                for (annotation in Annotation.FOREST_METHOD_ANNOTATION) {
                    if (methodAnnotation.qualifiedName == annotation.qualifiedName) {
                        return true
                    }
                }
            }
        }
        return false
    }
}