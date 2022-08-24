package com.chm.plugin.idea.forestx.template.psi;

import com.chm.plugin.idea.forestx.Icons;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TemplateFileType extends LanguageFileType {

    public static final TemplateFileType INSTANCE = new TemplateFileType();

    protected TemplateFileType() {
        super(ForestTemplateLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Forest File";
    }

    @Override
    public @NotNull @NlsContexts.Label String getDescription() {
        return "Forest template expression language";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "forest";
    }

    @Override
    public @Nullable Icon getIcon() {
        return Icons.ICON;
    }
}
