// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyBlock;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ForestTemplatePropertyBlockImpl extends ASTWrapperPsiElement implements ForestTemplatePropertyBlock {

    public ForestTemplatePropertyBlockImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ForestTemplateVisitor visitor) {
        visitor.visitPropertyBlock(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor) visitor);
        else super.accept(visitor);
    }

}
