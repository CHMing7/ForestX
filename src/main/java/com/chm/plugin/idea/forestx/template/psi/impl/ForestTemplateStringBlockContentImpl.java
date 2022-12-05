// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateElBlock;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyBlock;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateStringBlockContent;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForestTemplateStringBlockContentImpl extends ASTWrapperPsiElement implements ForestTemplateStringBlockContent {

    public ForestTemplateStringBlockContentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ForestTemplateVisitor visitor) {
        visitor.visitStringBlockContent(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public ForestTemplateElBlock getElBlock() {
        return findChildByClass(ForestTemplateElBlock.class);
    }

    @Override
    @Nullable
    public ForestTemplatePropertyBlock getPropertyBlock() {
        return findChildByClass(ForestTemplatePropertyBlock.class);
    }

}
