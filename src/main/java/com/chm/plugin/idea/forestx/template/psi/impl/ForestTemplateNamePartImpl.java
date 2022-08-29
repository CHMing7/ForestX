// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.chm.plugin.idea.forestx.template.psi.*;

public class ForestTemplateNamePartImpl extends ASTWrapperPsiElement implements ForestTemplateNamePart {

  public ForestTemplateNamePartImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ForestTemplateVisitor visitor) {
    visitor.visitNamePart(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ForestTemplateIdentifier getIdentifier() {
    return findNotNullChildByClass(ForestTemplateIdentifier.class);
  }

}
