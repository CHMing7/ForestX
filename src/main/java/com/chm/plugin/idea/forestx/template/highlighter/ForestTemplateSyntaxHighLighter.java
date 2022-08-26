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

    public static final TextAttributesKey PROP_NAME_PART =
            createTextAttributesKey("PROP_NAME_PART", DefaultLanguageHighlighterColors.CONSTANT);

    public static final TextAttributesKey PROP_BRACES =
            createTextAttributesKey("PROP_BRACES", DefaultLanguageHighlighterColors.BRACES);

    public static final TextAttributesKey PROP_NUMBER =
            createTextAttributesKey("PROP_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey PROP_DOT =
            createTextAttributesKey("PROP_DOT", DefaultLanguageHighlighterColors.DOT);

    public static final TextAttributesKey EL_BLOCK_BEGIN =
            createTextAttributesKey("EL_BLOCK_BEGIN", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey EL_BLOCK_END =
            createTextAttributesKey("EL_BLOCK_END", DefaultLanguageHighlighterColors.KEYWORD);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] JAVA_STRING_DQ_KEYS = new TextAttributesKey[]{JAVA_STRING_DQ};
    private static final TextAttributesKey[] JAVA_STRING_KEYS = new TextAttributesKey[]{JAVA_STRING};
    private static final TextAttributesKey[] PROP_BLOCK_KEYS = new TextAttributesKey[]{PROP_BLOCKS};
    private static final TextAttributesKey[] PROP_NAME_PART_KEYS = new TextAttributesKey[]{PROP_NAME_PART};

    private static final TextAttributesKey[] PROP_BRACES_KEYS = new TextAttributesKey[]{PROP_BRACES};

    private static final TextAttributesKey[] PROP_NUMBER_KEYS = new TextAttributesKey[]{PROP_NUMBER};
    private static final TextAttributesKey[] PROP_DOT_KEYS = new TextAttributesKey[]{PROP_DOT};
    private static final TextAttributesKey[] EL_BLOCK_BEGIN_KEYS = new TextAttributesKey[]{EL_BLOCK_BEGIN};
    private static final TextAttributesKey[] EL_BLOCK_END_KEYS = new TextAttributesKey[]{EL_BLOCK_END};


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
        if (tokenType.equals(TemplateTypes.PROP_NAME_PART)) {
            return PROP_NAME_PART_KEYS;
        }
        if (tokenType.equals(TemplateTypes.PROP_LBRACE) || tokenType.equals(TemplateTypes.PROP_RBRACE)) {
            return PROP_BRACES_KEYS;
        }
        if (tokenType.equals(TemplateTypes.PROP_INT)) {
            return PROP_NUMBER_KEYS;
        }
        if (tokenType.equals(TemplateTypes.PROP_DOT)) {
            return PROP_DOT_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_BLOCK_BEGIN)) {
            return EL_BLOCK_BEGIN_KEYS;
        }
        if (tokenType.equals(TemplateTypes.EL_BLOCK_END)) {
            return EL_BLOCK_END_KEYS;
        }
        return EMPTY_KEYS;
    }

}
