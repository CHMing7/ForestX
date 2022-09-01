package com.chm.plugin.idea.forestx.template.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

public class SearchedForestParameterVariable {

    public final static LookupElementRenderer<LookupElement> PARAMETER_VAR_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            SearchedForestParameterVariable forestVariable = (SearchedForestParameterVariable) element.getObject();
            String varName = forestVariable.getVarName();
            PsiType varType = forestVariable.getType();
            presentation.setIcon(PlatformIcons.PARAMETER_ICON);
            presentation.setItemText(varName);
            presentation.setTypeText(varType.getPresentableText());
        }
    };


    private final String varName;

    private final PsiType type;




    public static SearchedForestParameterVariable findVariable(PsiParameter parameter) {
        PsiAnnotation varAnn = parameter.getAnnotation("com.dtflys.forest.annotation.Var");
        PsiAnnotationMemberValue varNameAttrValue = varAnn.findAttributeValue("value");
        String varName = null;
        if (varNameAttrValue instanceof PsiLiteralExpression) {
            varName = String.valueOf(((PsiLiteralExpression) varNameAttrValue).getValue());
        } else {
            return null;
        }
        PsiType type = parameter.getType();
        return new SearchedForestParameterVariable(varName, type);
    }

    public SearchedForestParameterVariable(String varName, PsiType type) {
        this.varName = varName;
        this.type = type;
    }

    public String getVarName() {
        return varName;
    }

    public PsiType getType() {
        return type;
    }

    @Override
    public String toString() {
        return varName;
    }
}
