// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.chm.plugin.idea.forestx.template.psi.impl.*;

public interface TemplateTypes {

  IElementType EL_BLOCK = new ForestTemplateElementType("EL_BLOCK");
  IElementType EL_EXPRESS = new ForestTemplateElementType("EL_EXPRESS");
  IElementType IDENTITY = new ForestTemplateElementType("IDENTITY");
  IElementType NUMBER = new ForestTemplateElementType("NUMBER");
  IElementType PROPERTY_ARRAY_INDEX = new ForestTemplateElementType("PROPERTY_ARRAY_INDEX");
  IElementType PROPERTY_ARRAY_REFERENCE = new ForestTemplateElementType("PROPERTY_ARRAY_REFERENCE");
  IElementType PROPERTY_BLOCK = new ForestTemplateElementType("PROPERTY_BLOCK");
  IElementType PROPERTY_EXPRESS = new ForestTemplateElementType("PROPERTY_EXPRESS");
  IElementType PROPERTY_PART = new ForestTemplateElementType("PROPERTY_PART");
  IElementType STRING_BLOCK_CONTENT = new ForestTemplateElementType("STRING_BLOCK_CONTENT");

  IElementType EL_BLOCK_BEGIN = new ForestTemplateTokenType("EL_BLOCK_BEGIN");
  IElementType EL_BLOCK_END = new ForestTemplateTokenType("EL_BLOCK_END");
  IElementType EL_DECIMAL = new ForestTemplateTokenType("EL_DECIMAL");
  IElementType EL_IDENTITY = new ForestTemplateTokenType("EL_IDENTITY");
  IElementType EL_INT = new ForestTemplateTokenType("EL_INT");
  IElementType FT_DQ = new ForestTemplateTokenType("FT_DQ");
  IElementType FT_JSTRING = new ForestTemplateTokenType("FT_JSTRING");
  IElementType PROP_BLOCK_BEGIN = new ForestTemplateTokenType("PROP_BLOCK_BEGIN");
  IElementType PROP_BLOCK_END = new ForestTemplateTokenType("PROP_BLOCK_END");
  IElementType PROP_DOT = new ForestTemplateTokenType("PROP_DOT");
  IElementType PROP_INT = new ForestTemplateTokenType("PROP_INT");
  IElementType PROP_LBRACE = new ForestTemplateTokenType("PROP_LBRACE");
  IElementType PROP_NAME_PART = new ForestTemplateTokenType("PROP_NAME_PART");
  IElementType PROP_RBRACE = new ForestTemplateTokenType("PROP_RBRACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == EL_BLOCK) {
        return new ForestTemplateElBlockImpl(node);
      }
      else if (type == EL_EXPRESS) {
        return new ForestTemplateElExpressImpl(node);
      }
      else if (type == IDENTITY) {
        return new ForestTemplateIdentityImpl(node);
      }
      else if (type == NUMBER) {
        return new ForestTemplateNumberImpl(node);
      }
      else if (type == PROPERTY_ARRAY_INDEX) {
        return new ForestTemplatePropertyArrayIndexImpl(node);
      }
      else if (type == PROPERTY_ARRAY_REFERENCE) {
        return new ForestTemplatePropertyArrayReferenceImpl(node);
      }
      else if (type == PROPERTY_BLOCK) {
        return new ForestTemplatePropertyBlockImpl(node);
      }
      else if (type == PROPERTY_EXPRESS) {
        return new ForestTemplatePropertyExpressImpl(node);
      }
      else if (type == PROPERTY_PART) {
        return new ForestTemplatePropertyPartImpl(node);
      }
      else if (type == STRING_BLOCK_CONTENT) {
        return new ForestTemplateStringBlockContentImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
