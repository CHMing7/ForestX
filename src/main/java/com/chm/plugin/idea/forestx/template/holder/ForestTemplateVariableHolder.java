package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

public abstract class ForestTemplateVariableHolder<T> extends ForestTemplatePathElementHolder<T> {

    public ForestTemplateVariableHolder(String insertion, T element, PsiType type, boolean el) {
        super(insertion, element, type, el);
    }

    public abstract String getVarName();
}
