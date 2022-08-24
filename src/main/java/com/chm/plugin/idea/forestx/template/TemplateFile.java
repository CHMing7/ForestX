package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.chm.plugin.idea.forestx.template.psi.TemplateFileType;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class TemplateFile extends PsiFileBase {

    protected TemplateFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ForestTemplateLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return TemplateFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Forest File";
    }
}
