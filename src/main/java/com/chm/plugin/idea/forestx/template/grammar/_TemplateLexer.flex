package com.chm.plugin.idea.forestx.template.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
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

EOL=\R
WHITE_SPACE=\s+

CHARACTER=[a-zA-Z$_] | ~[\u0000-\u007F\uD800-\uDBFF] | [\uD800-\uDBFF] [\uDC00-\uDFFF]
BACK_SLASH=\\
LINE_ESCAPE=BACK_SLASH (\r)? \n
JAVA_STRING_CHARACTER = ~[#${}] | {LINE_ESCAPE} | {BACK_SLASH} [#${}]
IDENTIFIER = {CHARACTER} [:jletterdigit:]*
PROPERTY_NAME_PART = {IDENTIFIER}+ (\- {IDENTIFIER}+)*

%%
<YYINITIAL> {
  {WHITE_SPACE}                  { return WHITE_SPACE; }

  "#{"                           { return FT_PROPERTY_BLOCK_BEGIN; }
  "}"                            { return FT_BLOCK_END; }
  "."                            { return FT_DOT; }

  {JAVA_STRING_CHARACTER}+       { return FT_JAVA_STRING_CHARACTERS; }
  {PROPERTY_NAME_PART}           { return FT_PROPERTY_NAME_PART; }
}

[^] { return BAD_CHARACTER; }
