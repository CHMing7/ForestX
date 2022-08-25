package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class ForestTemplateTokenType extends IElementType {

    public ForestTemplateTokenType(@NotNull String debugName) {
        super(debugName, ForestTemplateLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "TemplateTokenType." + super.toString();
    }
}
