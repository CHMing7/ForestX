// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.chm.plugin.idea.forestx.template.psi.impl.*;

public interface TemplateTypes {

  IElementType ARGUMENTS = new ForestTemplateElementType("ARGUMENTS");
  IElementType ARGUMENT_LIST = new ForestTemplateElementType("ARGUMENT_LIST");
  IElementType ARGUMENT_LIST_ELEMENT = new ForestTemplateElementType("ARGUMENT_LIST_ELEMENT");
  IElementType DECIMAL = new ForestTemplateElementType("DECIMAL");
  IElementType EL_BLOCK = new ForestTemplateElementType("EL_BLOCK");
  IElementType EL_EXPRESS = new ForestTemplateElementType("EL_EXPRESS");
  IElementType IDENTITY = new ForestTemplateElementType("IDENTITY");
  IElementType INTEGER = new ForestTemplateElementType("INTEGER");
  IElementType NAME_PART = new ForestTemplateElementType("NAME_PART");
  IElementType PATH_ELEMENT = new ForestTemplateElementType("PATH_ELEMENT");
  IElementType PATH_EXPRESS = new ForestTemplateElementType("PATH_EXPRESS");
  IElementType PRIMARY = new ForestTemplateElementType("PRIMARY");
  IElementType PROPERTY_BLOCK = new ForestTemplateElementType("PROPERTY_BLOCK");
  IElementType STRING_BLOCK_CONTENT = new ForestTemplateElementType("STRING_BLOCK_CONTENT");

  IElementType EL_BLOCK_BEGIN = new ForestTemplateTokenType("EL_BLOCK_BEGIN");
  IElementType EL_BLOCK_END = new ForestTemplateTokenType("EL_BLOCK_END");
  IElementType EL_COMMA = new ForestTemplateTokenType("EL_COMMA");
  IElementType EL_DECIMAL = new ForestTemplateTokenType("EL_DECIMAL");
  IElementType EL_DOT = new ForestTemplateTokenType("EL_DOT");
  IElementType EL_IDENTITY = new ForestTemplateTokenType("EL_IDENTITY");
  IElementType EL_INT = new ForestTemplateTokenType("EL_INT");
  IElementType EL_LPAREN = new ForestTemplateTokenType("EL_LPAREN");
  IElementType EL_RPAREN = new ForestTemplateTokenType("EL_RPAREN");
  IElementType FT_DQ = new ForestTemplateTokenType("FT_DQ");
  IElementType FT_JSTRING = new ForestTemplateTokenType("FT_JSTRING");
  IElementType PROP_BLOCK_BEGIN = new ForestTemplateTokenType("PROP_BLOCK_BEGIN");
  IElementType PROP_BLOCK_END = new ForestTemplateTokenType("PROP_BLOCK_END");
  IElementType PROP_REFERENCE = new ForestTemplateTokenType("PROP_REFERENCE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARGUMENTS) {
        return new ForestTemplateArgumentsImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new ForestTemplateArgumentListImpl(node);
      }
      else if (type == ARGUMENT_LIST_ELEMENT) {
        return new ForestTemplateArgumentListElementImpl(node);
      }
      else if (type == DECIMAL) {
        return new ForestTemplateDecimalImpl(node);
      }
      else if (type == EL_BLOCK) {
        return new ForestTemplateElBlockImpl(node);
      }
      else if (type == EL_EXPRESS) {
        return new ForestTemplateElExpressImpl(node);
      }
      else if (type == IDENTITY) {
        return new ForestTemplateIdentityImpl(node);
      }
      else if (type == INTEGER) {
        return new ForestTemplateIntegerImpl(node);
      }
      else if (type == NAME_PART) {
        return new ForestTemplateNamePartImpl(node);
      }
      else if (type == PATH_ELEMENT) {
        return new ForestTemplatePathElementImpl(node);
      }
      else if (type == PATH_EXPRESS) {
        return new ForestTemplatePathExpressImpl(node);
      }
      else if (type == PRIMARY) {
        return new ForestTemplatePrimaryImpl(node);
      }
      else if (type == PROPERTY_BLOCK) {
        return new ForestTemplatePropertyBlockImpl(node);
      }
      else if (type == STRING_BLOCK_CONTENT) {
        return new ForestTemplateStringBlockContentImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
