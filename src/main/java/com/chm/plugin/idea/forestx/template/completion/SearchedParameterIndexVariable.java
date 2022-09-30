package com.chm.plugin.idea.forestx.template.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

public class SearchedParameterIndexVariable extends SearchedParameterVariable {

    public final static LookupElementRenderer<LookupElement> PARAMETER_INDEX_VAR_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final SearchedParameterIndexVariable forestVariable = (SearchedParameterIndexVariable) element.getObject();
            final PsiParameter psiParameter = forestVariable.getParameter();
            final PsiType varType = forestVariable.getType();
            presentation.setIcon(PlatformIcons.PARAMETER_ICON);
            presentation.setItemText(forestVariable.toString());
            presentation.setTypeText(varType.getPresentableText());
            presentation.setTailText(": " + psiParameter.getName());
        }
    };


    public static SearchedParameterIndexVariable getIndexVariable(PsiParameter parameter, int index) {
        final PsiType type = parameter.getType();
        final String varName = index + "";
        return new SearchedParameterIndexVariable(varName, index, type, parameter);
    }


    private final int index;

    private final boolean hasDollar;

    public SearchedParameterIndexVariable(String varName, int index, PsiType type, PsiParameter parameter) {
        super(varName, type, parameter);
        this.index = index;
        this.hasDollar = true;
    }

    public int getIndex() {
        return index;
    }

    public boolean hasDollar() {
        return hasDollar;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (hasDollar) {
            builder.append('$');
        }
        builder.append(index);
        return builder.toString();
    }
}
