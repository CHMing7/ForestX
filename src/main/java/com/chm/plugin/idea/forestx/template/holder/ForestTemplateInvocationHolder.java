package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;

import java.util.List;

public class ForestTemplateInvocationHolder extends ForestTemplatePathElementHolder<PsiMethod> {

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
