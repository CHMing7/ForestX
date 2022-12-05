// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateArgumentListElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateElExpress;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ForestTemplateArgumentListElementImpl extends ASTWrapperPsiElement implements ForestTemplateArgumentListElement {

    public ForestTemplateArgumentListElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ForestTemplateVisitor visitor) {
        visitor.visitArgumentListElement(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public ForestTemplateElExpress getElExpress() {
        return findNotNullChildByClass(ForestTemplateElExpress.class);
    }

}
