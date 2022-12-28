// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateDecimal;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateIdentifier;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateInteger;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForestTemplatePrimaryImpl extends ASTWrapperPsiElement implements ForestTemplatePrimary {

    public ForestTemplatePrimaryImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ForestTemplateVisitor visitor) {
        visitor.visitPrimary(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ForestTemplateVisitor) {
            accept((ForestTemplateVisitor) visitor);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    @Nullable
    public ForestTemplateDecimal getDecimal() {
        return findChildByClass(ForestTemplateDecimal.class);
    }

    @Override
    @Nullable
    public ForestTemplateIdentifier getIdentifier() {
        return findChildByClass(ForestTemplateIdentifier.class);
    }

    @Override
    @Nullable
    public ForestTemplateInteger getInteger() {
        return findChildByClass(ForestTemplateInteger.class);
    }

}
