// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateArgumentList;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateArgumentListElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestTemplateArgumentListImpl extends ASTWrapperPsiElement implements ForestTemplateArgumentList {

    public ForestTemplateArgumentListImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ForestTemplateVisitor visitor) {
        visitor.visitArgumentList(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<ForestTemplateArgumentListElement> getArgumentListElementList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, ForestTemplateArgumentListElement.class);
    }

}
