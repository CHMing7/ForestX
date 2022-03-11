package com.chm.plugin.idea.forestx.utils;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public final class JavaUtil {

    private JavaUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Find clazz optional.
     *
     * @param project   the project
     * @param clazzName the clazz name
     * @return the optional
     */
    public static Optional<PsiClass> findClazz(@NotNull Project project, @NotNull String clazzName) {
        return Optional.ofNullable(JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project)));
    }

    /**
     * Is annotation present boolean.
     *
     * @param target     the target
     * @param annotation the annotation
     * @return the boolean
     */
    public static boolean isAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return null != modifierList && null != modifierList.findAnnotation(annotation.getQualifiedName());
    }

    /**
     * Gets psi annotation.
     *
     * @param target     the target
     * @param annotation the annotation
     * @return the psi annotation
     */
    public static Optional<PsiAnnotation> getPsiAnnotation(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return null == modifierList ? Optional.empty() : Optional.ofNullable(modifierList.findAnnotation(annotation.getQualifiedName()));
    }

}
