// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.chm.plugin.idea.forestx.template.psi.TemplateTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.chm.plugin.idea.forestx.template.psi.*;

public class ForestTemplatePropertyBlockImpl extends ASTWrapperPsiElement implements ForestTemplatePropertyBlock {

  public ForestTemplatePropertyBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ForestTemplateVisitor visitor) {
    visitor.visitPropertyBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ForestTemplatePropertyReference getPropertyReference() {
    return findNotNullChildByClass(ForestTemplatePropertyReference.class);
  }

}
