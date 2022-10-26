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

public class ForestTemplatePathExpressImpl extends ASTWrapperPsiElement implements ForestTemplatePathExpress {

  public ForestTemplatePathExpressImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ForestTemplateVisitor visitor) {
    visitor.visitPathExpress(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ForestTemplateVisitor) accept((ForestTemplateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ForestTemplatePathElement> getPathElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ForestTemplatePathElement.class);
  }

  @Override
  @NotNull
  public ForestTemplatePrimary getPrimary() {
    return findNotNullChildByClass(ForestTemplatePrimary.class);
  }

}
