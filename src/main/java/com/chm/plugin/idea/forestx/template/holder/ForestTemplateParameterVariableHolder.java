package com.chm.plugin.idea.forestx.template.holder;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.template.completion.SearchedParameterVariable;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

public class ForestTemplateParameterVariableHolder extends ForestTemplateVariableHolder {

    public final static LookupElementRenderer<LookupElement> PARAMETER_VAR_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplateParameterVariableHolder forestVariable = (ForestTemplateParameterVariableHolder)
                    element.getObject();
            final String varName = forestVariable.getVarName();
            final PsiType varType = forestVariable.getType();
            presentation.setIcon(PlatformIcons.PARAMETER_ICON);
            presentation.setItemText(varName);
            presentation.setTypeText(varType.getPresentableText());
        }
    };


    public static ForestTemplateParameterVariableHolder findVariable(PsiParameter parameter) {
        final PsiAnnotation[] annotations = parameter.getAnnotations();
        for (PsiAnnotation ann : annotations) {
            if (ann.getQualifiedName().equals(Annotation.VAR.getQualifiedName())) {
                final PsiAnnotationMemberValue varNameAttrValue = ann.findAttributeValue("value");
                String varName = null;
                if (varNameAttrValue instanceof PsiLiteralExpression) {
                    varName = String.valueOf(((PsiLiteralExpression) varNameAttrValue).getValue());
                } else {
                    return null;
                }
                final PsiType type = parameter.getType();
                return new ForestTemplateParameterVariableHolder(varName, type, parameter);
            }
        }
        return null;
    }


    public ForestTemplateParameterVariableHolder(String varName, PsiType type, PsiParameter parameter) {
        super(varName, parameter, type, false);
    }

    @Override
    public String getVarName() {
        return insertion;
    }
}
