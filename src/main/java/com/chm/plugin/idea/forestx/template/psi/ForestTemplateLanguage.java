package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.lang.Language;

public class ForestTemplateLanguage extends Language {

    public final static ForestTemplateLanguage INSTANCE = new ForestTemplateLanguage();

    protected ForestTemplateLanguage() {
        super("Forest");
    }
}
