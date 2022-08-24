// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.grammar;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.chm.plugin.idea.forestx.template.psi.TemplateTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TemplateParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return StringTemplate(b, l + 1);
  }

  /* ********************************************************** */
  // FT_PROPERTY_BLOCK_BEGIN PropertyReference FT_BLOCK_END
  public static boolean PropertyBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyBlock")) return false;
    if (!nextTokenIs(b, FT_PROPERTY_BLOCK_BEGIN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FT_PROPERTY_BLOCK_BEGIN);
    r = r && PropertyReference(b, l + 1);
    r = r && consumeToken(b, FT_BLOCK_END);
    exit_section_(b, m, PROPERTY_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // PropertyReferencePart (FT_DOT PropertyReferencePart)*
  public static boolean PropertyReference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyReference")) return false;
    if (!nextTokenIs(b, FT_PROPERTY_NAME_PART)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = PropertyReferencePart(b, l + 1);
    r = r && PropertyReference_1(b, l + 1);
    exit_section_(b, m, PROPERTY_REFERENCE, r);
    return r;
  }

  // (FT_DOT PropertyReferencePart)*
  private static boolean PropertyReference_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyReference_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PropertyReference_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PropertyReference_1", c)) break;
    }
    return true;
  }

  // FT_DOT PropertyReferencePart
  private static boolean PropertyReference_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyReference_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FT_DOT);
    r = r && PropertyReferencePart(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FT_PROPERTY_NAME_PART
  public static boolean PropertyReferencePart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyReferencePart")) return false;
    if (!nextTokenIs(b, FT_PROPERTY_NAME_PART)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FT_PROPERTY_NAME_PART);
    exit_section_(b, m, PROPERTY_REFERENCE_PART, r);
    return r;
  }

  /* ********************************************************** */
  // (FT_JAVA_STRING_CHARACTERS | PropertyBlock)*
  static boolean StringTemplate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate")) return false;
    while (true) {
      int c = current_position_(b);
      if (!StringTemplate_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StringTemplate", c)) break;
    }
    return true;
  }

  // FT_JAVA_STRING_CHARACTERS | PropertyBlock
  private static boolean StringTemplate_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate_0")) return false;
    boolean r;
    r = consumeToken(b, FT_JAVA_STRING_CHARACTERS);
    if (!r) r = PropertyBlock(b, l + 1);
    return r;
  }

}
