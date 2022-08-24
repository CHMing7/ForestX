package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.lang.Language;

public class TemplateLanguage extends Language {

    public final static TemplateLanguage INSTANCE = new TemplateLanguage();

    protected TemplateLanguage() {
        super("Forest Template");
    }
}
