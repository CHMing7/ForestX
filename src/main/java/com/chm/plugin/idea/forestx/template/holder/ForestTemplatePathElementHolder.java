package com.chm.plugin.idea.forestx.template.holder;

import com.chm.plugin.idea.forestx.utils.KotlinExtensionKt;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

import java.util.Optional;

public abstract class ForestTemplatePathElementHolder<T> {

    protected final ForestTemplatePathElementHolder prevHolder;

    protected final String insertion;

    protected final T element;

    protected final PsiType type;
    protected final boolean el;

    public ForestTemplatePathElementHolder(ForestTemplatePathElementHolder prevHolder, String insertion, T element, PsiType type, boolean el) {
        this.prevHolder = prevHolder;
        this.insertion = insertion;
        this.element = element;
        this.type = type;
        this.el = el;
    }

    public ForestTemplatePathElementHolder(String insertion, T element, PsiType type, boolean el) {
        this(null, insertion, element, type, el);
    }

    public ForestTemplatePathElementHolder getPrevHolder() {
        return prevHolder;
    }

    public T getElement() {
        return element;
    }

    public PsiType getType() {
        return type;
    }

    public PsiClass getPsiClass() {
        if (element instanceof PsiElement) {
            final Optional<PsiClass> clazz = KotlinExtensionKt.findClazz(((PsiElement) element).getProject(), type.getCanonicalText());
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
