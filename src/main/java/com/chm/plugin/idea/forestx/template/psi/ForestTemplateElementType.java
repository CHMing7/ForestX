package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class ForestTemplateElementType extends IElementType {

    public ForestTemplateElementType(@NotNull String debugName) {
        super(debugName, ForestTemplateLanguage.INSTANCE);
    }
}
