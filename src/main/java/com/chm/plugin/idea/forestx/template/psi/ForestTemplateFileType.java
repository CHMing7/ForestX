package com.chm.plugin.idea.forestx.template.psi;

import com.chm.plugin.idea.forestx.Icons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ForestTemplateFileType extends LanguageFileType {

    public static final ForestTemplateFileType INSTANCE = new ForestTemplateFileType();

    protected ForestTemplateFileType() {
        super(ForestTemplateLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Forest Template File";
    }

    @Override
    public @NotNull @NlsContexts.Label String getDescription() {
        return "Forest template expression language";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "ft";
    }

    @Override
    public @Nullable Icon getIcon() {
        return Icons.ICON;
    }
}
