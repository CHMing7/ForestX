// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateArguments;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateNamePart;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForestTemplatePathElementImpl extends ASTWrapperPsiElement implements ForestTemplatePathElement {

    public ForestTemplatePathElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ForestTemplateVisitor visitor) {
        visitor.visitPathElement(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public ForestTemplateArguments getArguments() {
        return findChildByClass(ForestTemplateArguments.class);
    }

    @Override
    @Nullable
    public ForestTemplateNamePart getNamePart() {
        return findChildByClass(ForestTemplateNamePart.class);
    }

}
