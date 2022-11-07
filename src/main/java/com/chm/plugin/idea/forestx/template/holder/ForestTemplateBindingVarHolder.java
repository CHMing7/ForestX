package com.chm.plugin.idea.forestx.template.holder;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.searches.AnnotationTargetsSearch;
import com.intellij.util.PlatformIcons;
import com.intellij.util.Query;

import java.util.Collection;
import java.util.function.Function;

public class ForestTemplateBindingVarHolder extends ForestTemplateVariableHolder {

    public final static LookupElementRenderer<LookupElement> BINDING_VAR_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplateBindingVarHolder forestVariable = (ForestTemplateBindingVarHolder)
                    element.getObject();
            final String varName = forestVariable.getVarName();
            final PsiType varType = forestVariable.getType();
            presentation.setIcon(PlatformIcons.METHOD_ICON);
            presentation.setItemText(varName);
            presentation.setTypeText(varType.getPresentableText());
        }
    };


    public static ForestTemplateBindingVarHolder eachAllVariables(
            Project project,
            Function<ForestTemplateBindingVarHolder, Boolean> func) {
        GlobalSearchScope librariesScope = ProjectScope.getLibrariesScope(project);
        GlobalSearchScope inheritorsScope =
                GlobalSearchScopesCore.projectProductionScope(project).union(librariesScope);
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass bindingVarAnnClass = facade.findClass(
                Annotation.Companion.getBINDING_VAR().getQualifiedName(), librariesScope);
        if (bindingVarAnnClass != null) {
            Query<PsiModifierListOwner> annotationTargets =
                    AnnotationTargetsSearch.search(bindingVarAnnClass, inheritorsScope);
            Collection<PsiModifierListOwner> all = annotationTargets.findAll();
            for (Object psiModifierListOwner : all) {
                if (psiModifierListOwner instanceof PsiMethod) {
                    ForestTemplateBindingVarHolder holder =
                            findVariable((PsiMethod) psiModifierListOwner, null);
                    if (holder != null) {
                        Boolean ret = func.apply(holder);
                        if (ret != null && !ret) {
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static ForestTemplateBindingVarHolder findVariable(Project project, String varName) {
        GlobalSearchScope librariesScope = ProjectScope.getLibrariesScope(project);
        GlobalSearchScope inheritorsScope =
                GlobalSearchScopesCore.projectProductionScope(project).union(librariesScope);
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass bindingVarAnnClass = facade.findClass(
                Annotation.Companion.getBINDING_VAR().getQualifiedName(), librariesScope);
        if (bindingVarAnnClass != null) {
            Query<PsiModifierListOwner> annotationTargets =
                    AnnotationTargetsSearch.search(bindingVarAnnClass, inheritorsScope);
            Collection<PsiModifierListOwner> all = annotationTargets.findAll();
            for (Object psiModifierListOwner : all) {
                if (psiModifierListOwner instanceof PsiMethod) {
                    ForestTemplateBindingVarHolder holder =
                            findVariable((PsiMethod) psiModifierListOwner, varName);
                    if (holder != null) {
                        return holder;
                    }
                }
            }
        }
        return null;
    }

    public static ForestTemplateBindingVarHolder findVariable(PsiMethod method, String varName) {
        PsiAnnotation ann = method.getAnnotation(Annotation.Companion.getBINDING_VAR().getQualifiedName());
        if (ann != null) {
            PsiAnnotationMemberValue value = ann.findAttributeValue("value");
            if (value != null && value instanceof PsiLiteralExpression) {
                String name = String.valueOf(((PsiLiteralExpression) value).getValue());
                if (varName == null || varName.equals(name)) {
                    ForestTemplateBindingVarHolder holder = new ForestTemplateBindingVarHolder(
                            name,
                            method,
                            method.getReturnType(),
                            true);
                    return holder;
                }
            }
        }
        return null;
    }


    public ForestTemplateBindingVarHolder(String insertion, PsiElement element, PsiType type, boolean el) {
        super(null, insertion, element, type, el);
    }

    @Override
    public String getVarName() {
        return insertion;
    }
}
