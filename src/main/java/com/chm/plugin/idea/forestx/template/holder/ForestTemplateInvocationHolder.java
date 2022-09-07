package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

import java.util.List;

public class ForestTemplateInvocationHolder extends ForestTemplatePathElementHolder<PsiMethod> {

    public final static LookupElementRenderer<LookupElement> INVOCATION_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            ForestTemplateInvocationHolder invocationHolder = (ForestTemplateInvocationHolder) element.getObject();
            PsiMethod psiMethod = invocationHolder.getMethod();
            StringBuilder paramText = new StringBuilder("(");
            PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                PsiParameter parameter = parameters[i];
                paramText.append(parameter.getType().getPresentableText())
                        .append(" ")
                        .append(parameter.getName());
            }
            paramText.append(")");
            presentation.setIcon(PlatformIcons.METHOD_ICON);
            presentation.setItemText(psiMethod.getName());
            presentation.setTailText(paramText.toString());
//            presentation.setTypeText(psiMethod.getReturnType().getPresentableText());
        }
    };

    private final PsiMethod method;
    private final List<PsiElement> arguments;


    public ForestTemplateInvocationHolder(String insertion, PsiMethod method, PsiType type, List<PsiElement> arguments) {
        super(insertion, method, type, false);
        this.method = method;
        this.arguments = arguments;
    }

    public PsiMethod getMethod() {
        return method;
    }

    public List<PsiElement> getArguments() {
        return arguments;
    }
}
