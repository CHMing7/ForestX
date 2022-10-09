package com.chm.plugin.idea.forestx.template.highlighter;

import com.chm.plugin.idea.forestx.template.ForestTemplateLexerAdapter;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.HighlighterColors.BAD_CHARACTER;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ForestTemplateSyntaxHighLighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey JAVA_STRING_DQ =
            createTextAttributesKey("JAVA_STRING_DQ", DefaultLanguageHighlighterColors.STRING);

    public static final TextAttributesKey JAVA_STRING =
            createTextAttributesKey("JAVA_STRING", DefaultLanguageHighlighterColors.STRING);

    public static final TextAttributesKey PROP_BLOCKS =
            createTextAttributesKey("PROP_BLOCKS", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey PROP_REFERENCE =
            createTextAttributesKey("PROP_REFERENCE", DefaultLanguageHighlighterColors.IDENTIFIER);

    public static final TextAttributesKey EL_BLOCK_BEGIN =
            createTextAttributesKey("EL_BLOCK_BEGIN", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey EL_BLOCK_END =
            createTextAttributesKey("EL_BLOCK_END", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey EL_IDENTIFIER =
            createTextAttributesKey("EL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    public static final TextAttributesKey EL_NUMBER =
            createTextAttributesKey("EL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

    public static final TextAttributesKey EL_DOT =
            createTextAttributesKey("EL_DOT", DefaultLanguageHighlighterColors.DOT);

    public static final TextAttributesKey EL_PARENS =
            createTextAttributesKey("EL_PARENS", DefaultLanguageHighlighterColors.PARENTHESES);


    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] JAVA_STRING_DQ_KEYS = new TextAttributesKey[]{JAVA_STRING_DQ};
    private static final TextAttributesKey[] JAVA_STRING_KEYS = new TextAttributesKey[]{JAVA_STRING};
    private static final TextAttributesKey[] PROP_BLOCK_KEYS = new TextAttributesKey[]{PROP_BLOCKS};

    private static final TextAttributesKey[] PROP_REFERENCE_KEYS = new TextAttributesKey[]{PROP_REFERENCE};
    private static final TextAttributesKey[] EL_BLOCK_BEGIN_KEYS = new TextAttributesKey[]{EL_BLOCK_BEGIN};
    private static final TextAttributesKey[] EL_BLOCK_END_KEYS = new TextAttributesKey[]{EL_BLOCK_END};

    private static final TextAttributesKey[] EL_IDENTIFIER_KEYS = new TextAttributesKey[]{EL_IDENTIFIER};

    private static final TextAttributesKey[] EL_NUMBER_KEYS = new TextAttributesKey[]{EL_NUMBER};

    private static final TextAttributesKey[] EL_DOT_KEYS = new TextAttributesKey[]{EL_DOT};

    private static final TextAttributesKey[] EL_PARENS_KEYS = new TextAttributesKey[]{EL_PARENS};

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];


    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new ForestTemplateLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(TemplateTypes.FT_DQ)) {
            return JAVA_STRING_DQ_KEYS;
        }
        if (tokenType.equals(TemplateTypes.FT_JSTRING)) {
            return JAVA_STRING_KEYS;
        }
        if (tokenType.equals(TemplateTypes.PROP_BLOCK_BEGIN) || tokenType.equals(TemplateTypes.PROP_BLOCK_END)) {
            return PROP_BLOCK_KEYS;
        }
        if (tokenType.equals(TemplateTypes.PROP_REFERENCE)) {
            return PROP_REFERENCE_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_BLOCK_BEGIN)) {
            return EL_BLOCK_BEGIN_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_BLOCK_END)) {
            return EL_BLOCK_END_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_IDENTIFIER)) {
            return EL_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_INT) || tokenType.equals(TemplateTypes.EL_DECIMAL)) {
            return EL_NUMBER_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_DOT)) {
            return EL_DOT_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_LPAREN) || tokenType.equals(TemplateTypes.EL_RPAREN)) {
            return EL_PARENS_KEYS;
        }
        return EMPTY_KEYS;
    }

}
