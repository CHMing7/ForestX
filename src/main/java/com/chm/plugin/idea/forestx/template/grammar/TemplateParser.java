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
  // ArgumentListElement (EL_COMMA ArgumentListElement)?
  public static boolean ArgumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, "<argument list>");
    r = ArgumentListElement(b, l + 1);
    r = r && ArgumentList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (EL_COMMA ArgumentListElement)?
  private static boolean ArgumentList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1")) return false;
    ArgumentList_1_0(b, l + 1);
    return true;
  }

  // EL_COMMA ArgumentListElement
  private static boolean ArgumentList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_COMMA);
    r = r && ArgumentListElement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ElExpress
  public static boolean ArgumentListElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentListElement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST_ELEMENT, "<argument list element>");
    r = ElExpress(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EL_LPAREN ArgumentList? EL_RPAREN
  public static boolean Arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Arguments")) return false;
    if (!nextTokenIs(b, EL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_LPAREN);
    r = r && Arguments_1(b, l + 1);
    r = r && consumeToken(b, EL_RPAREN);
    exit_section_(b, m, ARGUMENTS, r);
    return r;
  }

  // ArgumentList?
  private static boolean Arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Arguments_1")) return false;
    ArgumentList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // EL_DECIMAL
  public static boolean Decimal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Decimal")) return false;
    if (!nextTokenIs(b, EL_DECIMAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_DECIMAL);
    exit_section_(b, m, DECIMAL, r);
    return r;
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
  // PathExpress
  public static boolean ElExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElExpress")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EL_EXPRESS, "<el express>");
    r = PathExpress(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EL_IDENTIFIER
  public static boolean Identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Identifier")) return false;
    if (!nextTokenIs(b, EL_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_IDENTIFIER);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // EL_INT
  public static boolean Integer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Integer")) return false;
    if (!nextTokenIs(b, EL_INT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_INT);
    exit_section_(b, m, INTEGER, r);
    return r;
  }

  /* ********************************************************** */
  // Identifier
  public static boolean NamePart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamePart")) return false;
    if (!nextTokenIs(b, EL_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Identifier(b, l + 1);
    exit_section_(b, m, NAME_PART, r);
    return r;
  }

  /* ********************************************************** */
  // EL_DOT NamePart | Arguments
  public static boolean PathElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PathElement")) return false;
    if (!nextTokenIs(b, "<path element>", EL_DOT, EL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATH_ELEMENT, "<path element>");
    r = PathElement_0(b, l + 1);
    if (!r) r = Arguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EL_DOT NamePart
  private static boolean PathElement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PathElement_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EL_DOT);
    r = r && NamePart(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Primary (PathElement)*
  public static boolean PathExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PathExpress")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATH_EXPRESS, "<path express>");
    r = Primary(b, l + 1);
    r = r && PathExpress_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (PathElement)*
  private static boolean PathExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PathExpress_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PathExpress_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PathExpress_1", c)) break;
    }
    return true;
  }

  // (PathElement)
  private static boolean PathExpress_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PathExpress_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = PathElement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Identifier | Integer | Decimal
  public static boolean Primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Primary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY, "<primary>");
    r = Identifier(b, l + 1);
    if (!r) r = Integer(b, l + 1);
    if (!r) r = Decimal(b, l + 1);
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
