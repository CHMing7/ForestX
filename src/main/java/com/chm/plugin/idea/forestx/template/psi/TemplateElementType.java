package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class TemplateElementType extends IElementType {

    public TemplateElementType(@NotNull String debugName) {
        super(debugName, TemplateLanguage.INSTANCE);
    }
}
