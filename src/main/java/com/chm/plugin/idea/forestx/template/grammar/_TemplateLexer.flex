package com.chm.plugin.idea.forestx.template.grammar;

import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.chm.plugin.idea.forestx.template.psi.TemplateTypes.*;

%%

%{
  public _TemplateLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _TemplateLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%states STRING, PROP_BLOCK, EL_BLOCK

EOL = \R
WHITE_SPACE = \s+

CHARACTER = [a-zA-Z$_] | [^\u0000-\u007F\uD800-\uDBFF] | [\uD800-\uDBFF] [\uDC00-\uDFFF]
BACK_SLASH = \\
LINE_ESCAPE = {BACK_SLASH} (\r)? \n
JAVA_STRING_CHARACTER = [^#${}\"] | {LINE_ESCAPE} | {BACK_SLASH} [#${}\"]
PROP_BLOCK_BEGIN = "#{"
EL_BLOCK_BEGIN = "${"
BLOCK_END = "}"
ZERO = 0
DIGIT = [0-9]
NON_ZERO_DIGIT = [1-9]
INT = {ZERO} | {NON_ZERO_DIGIT} {DIGIT}*
DECIMAL = {INT} "." {DIGIT}+
IDENTIFIER = {CHARACTER}+ {DIGIT}*
PROPERTY_NAME_PART = {IDENTIFIER}+ (\- {IDENTIFIER}+)*

%%
<YYINITIAL, STRING> "\""                        { yybegin(STRING); return FT_DQ; }
//<STRING> "."                                    { return FT_DOT; }
<STRING> "#{"                                   { yybegin(PROP_BLOCK); return PROP_BLOCK_BEGIN; }
<STRING> "${"                                   { yybegin(EL_BLOCK); return EL_BLOCK_BEGIN; }
<STRING> "{"                                    { yybegin(EL_BLOCK); return EL_BLOCK_BEGIN; }
<STRING> {JAVA_STRING_CHARACTER}+               { return FT_JSTRING; }
<EL_BLOCK> {
    "}"                                         { yybegin(STRING); return EL_BLOCK_END; }
    {IDENTIFIER}                                { return EL_IDENTITY; }
    {INT}                                       { return EL_INT; }
    {DECIMAL}                                   { return EL_DECIMAL; }
}
<PROP_BLOCK> {
    "}"                                         { yybegin(STRING); return PROP_BLOCK_END; }
    "."                                         { return PROP_DOT; }
    {PROPERTY_NAME_PART}                        { return PROP_NAME_PART; }
}

[^] { return BAD_CHARACTER; }
