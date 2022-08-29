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
  // EL_BLOCK_BEGIN ElExpress EL_BLOCK_END
  public static boolean ElBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElBlock")) return false;
    if (!nextTokenIs(b, EL_BLOCK_BEGIN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_BLOCK_BEGIN);
    r = r && ElExpress(b, l + 1);
    r = r && consumeToken(b, EL_BLOCK_END);
    exit_section_(b, m, EL_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // Identity | Number
  public static boolean ElExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElExpress")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EL_EXPRESS, "<el express>");
    r = Identity(b, l + 1);
    if (!r) r = Number(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EL_IDENTITY
  public static boolean Identity(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Identity")) return false;
    if (!nextTokenIs(b, EL_IDENTITY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_IDENTITY);
    exit_section_(b, m, IDENTITY, r);
    return r;
  }

  /* ********************************************************** */
  // EL_INT | EL_DECIMAL
  public static boolean Number(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Number")) return false;
    if (!nextTokenIs(b, "<number>", EL_DECIMAL, EL_INT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMBER, "<number>");
    r = consumeToken(b, EL_INT);
    if (!r) r = consumeToken(b, EL_DECIMAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PROP_BLOCK_BEGIN PROP_REFERENCE PROP_BLOCK_END
  public static boolean PropertyBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyBlock")) return false;
    if (!nextTokenIs(b, PROP_BLOCK_BEGIN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PROP_BLOCK_BEGIN, PROP_REFERENCE, PROP_BLOCK_END);
    exit_section_(b, m, PROPERTY_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // (PropertyBlock | ElBlock) FT_JSTRING?
  public static boolean StringBlockContent(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringBlockContent")) return false;
    if (!nextTokenIs(b, "<string block content>", EL_BLOCK_BEGIN, PROP_BLOCK_BEGIN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_BLOCK_CONTENT, "<string block content>");
    r = StringBlockContent_0(b, l + 1);
    r = r && StringBlockContent_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PropertyBlock | ElBlock
  private static boolean StringBlockContent_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringBlockContent_0")) return false;
    boolean r;
    r = PropertyBlock(b, l + 1);
    if (!r) r = ElBlock(b, l + 1);
    return r;
  }

  // FT_JSTRING?
  private static boolean StringBlockContent_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringBlockContent_1")) return false;
    consumeToken(b, FT_JSTRING);
    return true;
  }

  /* ********************************************************** */
  // FT_DQ FT_JSTRING? StringBlockContent* FT_DQ
  static boolean StringTemplate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate")) return false;
    if (!nextTokenIs(b, FT_DQ)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FT_DQ);
    r = r && StringTemplate_1(b, l + 1);
    r = r && StringTemplate_2(b, l + 1);
    r = r && consumeToken(b, FT_DQ);
    exit_section_(b, m, null, r);
    return r;
  }

  // FT_JSTRING?
  private static boolean StringTemplate_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate_1")) return false;
    consumeToken(b, FT_JSTRING);
    return true;
  }

  // StringBlockContent*
  private static boolean StringTemplate_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!StringBlockContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StringTemplate_2", c)) break;
    }
    return true;
  }

}
