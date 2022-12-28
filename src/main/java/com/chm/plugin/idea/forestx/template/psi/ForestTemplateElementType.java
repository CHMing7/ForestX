package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author dt_flys
 * @version v1.0
 * @since 2022-08-24
 **/
public class ForestTemplateElementType extends IElementType {

    public ForestTemplateElementType(@NotNull String debugName) {
        super(debugName, ForestTemplateLanguage.INSTANCE);
    }

    public String toString() {
        return "Forest:" + super.toString();
    }
}
