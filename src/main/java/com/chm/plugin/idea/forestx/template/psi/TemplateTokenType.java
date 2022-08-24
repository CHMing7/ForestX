package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TemplateTokenType extends IElementType {

    public TemplateTokenType(@NotNull String debugName) {
        super(debugName, TemplateLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "TemplateTokenType." + super.toString();
    }
}
