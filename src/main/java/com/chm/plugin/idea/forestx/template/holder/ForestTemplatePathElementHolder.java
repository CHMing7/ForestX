package com.chm.plugin.idea.forestx.template.holder;

import com.chm.plugin.idea.forestx.utils.JavaUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

import java.util.Optional;

public abstract class ForestTemplatePathElementHolder<T> {

    protected final String insertion;

    protected final T element;

    protected final PsiType type;

    protected final boolean el;

    public ForestTemplatePathElementHolder(String insertion, T element, PsiType type, boolean el) {
        this.insertion = insertion;
        this.element = element;
        this.type = type;
        this.el = el;
    }


    public T getElement() {
        return element;
    }

    public PsiType getType() {
        return type;
    }

    public PsiClass getPsiClass() {
        if (element instanceof PsiElement) {
            final Optional<PsiClass> clazz = JavaUtil.findClazz(((PsiElement) element).getProject(), type.getCanonicalText());
            return clazz.get();
        }
        return null;
    }

    public boolean isEl() {
        return el;
    }

    @Override
    public String toString() {
        return insertion;
    }
}
