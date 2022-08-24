// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.chm.plugin.idea.forestx.template.psi.impl.*;

public interface TemplateTypes {

  IElementType PROPERTY_BLOCK = new TemplateElementType("PROPERTY_BLOCK");
  IElementType PROPERTY_REFERENCE = new TemplateElementType("PROPERTY_REFERENCE");
  IElementType PROPERTY_REFERENCE_PART = new TemplateElementType("PROPERTY_REFERENCE_PART");

  IElementType FT_BLOCK_END = new TemplateTokenType("FT_BLOCK_END");
  IElementType FT_DOT = new TemplateTokenType("FT_DOT");
  IElementType FT_JAVA_STRING_CHARACTERS = new TemplateTokenType("FT_JAVA_STRING_CHARACTERS");
  IElementType FT_PROPERTY_BLOCK_BEGIN = new TemplateTokenType("FT_PROPERTY_BLOCK_BEGIN");
  IElementType FT_PROPERTY_NAME_PART = new TemplateTokenType("FT_PROPERTY_NAME_PART");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY_BLOCK) {
        return new ForestTemplatePropertyBlockImpl(node);
      }
      else if (type == PROPERTY_REFERENCE) {
        return new ForestTemplatePropertyReferenceImpl(node);
      }
      else if (type == PROPERTY_REFERENCE_PART) {
        return new ForestTemplatePropertyReferencePartImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
