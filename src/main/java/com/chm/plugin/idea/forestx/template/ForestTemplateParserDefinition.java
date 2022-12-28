package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.template.grammar.TemplateParser;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * @author dt_flys
 * @version v1.0
 * @since 2022-08-25
 **/
public class ForestTemplateParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(ForestTemplateLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new ForestTemplateLexerAdapter();
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new TemplateParser();
    }

    @Override
    @NotNull
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    @NotNull
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return TemplateTypes.Factory.createElement(node);
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new ForestTemplateFile(viewProvider);
    }
}
