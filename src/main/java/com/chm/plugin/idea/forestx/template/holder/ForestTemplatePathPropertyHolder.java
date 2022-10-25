package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

public class ForestTemplatePathPropertyHolder extends ForestTemplatePathElementHolder {
    private final String name;

    public ForestTemplatePathPropertyHolder(ForestTemplatePathElementHolder prevHolder, String insertion, PsiElement element, PsiType type) {
        super(prevHolder, insertion, element, type, false);
        this.name = element.getText();
    }

    public String getName() {
        return name;
    }
}
