package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.psi.PsiType;

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

    public boolean isEl() {
        return el;
    }

    @Override
    public String toString() {
        return insertion;
    }
}
