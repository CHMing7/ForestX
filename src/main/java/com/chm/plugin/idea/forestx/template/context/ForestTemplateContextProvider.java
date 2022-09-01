package com.chm.plugin.idea.forestx.template.context;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;

public class ForestTemplateContextProvider {

    public final static Key<ForestTemplateContextProvider> TemplateContextKey = new Key<>("ForestContextKey");

    private final PsiElement host;

    public ForestTemplateContextProvider(PsiElement host) {
        this.host = host;
    }

    public PsiElement getHost() {
        return host;
    }
}
