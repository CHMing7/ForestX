package com.chm.plugin.idea.forestx.provider;

import com.chm.plugin.idea.forestx.Icons;
import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.utils.JavaUtil;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-03-11
 **/
public class InjectionLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (!(element instanceof PsiField)) {
            return;
        }
        PsiField field = (PsiField) element;
        if (!isTargetField(field)) {
            return;
        }

        PsiType type = field.getType();
        if (!(type instanceof PsiClassReferenceType)) {
            return;
        }

        Optional<PsiClass> clazz = JavaUtil.findClazz(element.getProject(), type.getCanonicalText());
        if (!clazz.isPresent()) {
            return;
        }

        PsiClass psiClass = clazz.get();

        if (!interfaceFilter(psiClass)) {
            return;
        }

        NavigationGutterIconBuilder<PsiElement> builder =
                NavigationGutterIconBuilder.create(Icons.SPRING_INJECTION_ICON)
                        .setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTarget(psiClass)
                        .setTooltipTitle("Forest object found - " + psiClass.getQualifiedName());
        result.add(builder.createLineMarkerInfo(field.getNameIdentifier()));
    }

    private boolean isTargetField(PsiField field) {
        if (JavaUtil.isAnnotationPresent(field, Annotation.AUTOWIRED)) {
            return true;
        }
        Optional<PsiAnnotation> resourceAnno = JavaUtil.getPsiAnnotation(field, Annotation.RESOURCE);
        if (resourceAnno.isPresent()) {
            PsiAnnotationMemberValue nameValue = resourceAnno.get().findAttributeValue("name");
            String name = nameValue.getText().replaceAll("\"", "");
            return StringUtils.isBlank(name) || name.equals(field.getName());
        }
        return false;
    }

    private boolean interfaceFilter(PsiClass psiClass) {
        if (!psiClass.isInterface() || Objects.requireNonNull(psiClass.getModifierList()).hasModifierProperty(PsiModifier.FINAL)) {
            return false;
        }

        PsiClass[] superClass = psiClass.getInterfaces();
        boolean hasSuperForestClient = false;
        for (PsiClass sup : superClass) {
            hasSuperForestClient = interfaceFilter(sup);
            if (hasSuperForestClient) {
                return true;
            }
        }

        PsiAnnotation[] annotations = AnnotationUtil.getAllAnnotations(psiClass, false, null);
        for (PsiAnnotation annotation : annotations) {
            if ("com.dtflys.forest.annotation.ForestClient".equals(annotation.getQualifiedName())) {
                return true;
            }
            if ("com.dtflys.forest.annotation.BaseRequest".equals(annotation.getQualifiedName())) {
                return true;
            }
        }
        PsiMethod[] methods = psiClass.getMethods();
        for (PsiMethod method : methods) {
            PsiAnnotation[] methodAnnotations = AnnotationUtil.getAllAnnotations(method, false, null);
            for (PsiAnnotation methodAnnotation : methodAnnotations) {
                for (Annotation annotation : Annotation.FOREST_METHOD_ANNOTATION) {
                    if (Objects.equals(methodAnnotation.getQualifiedName(), annotation.getQualifiedName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
