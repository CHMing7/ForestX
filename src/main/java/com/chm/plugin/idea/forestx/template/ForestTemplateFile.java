package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateFileType;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class ForestTemplateFile extends PsiFileBase {

    protected ForestTemplateFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ForestTemplateLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ForestTemplateFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Forest File";
    }
}
