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

public class ForestTemplateParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(ForestTemplateLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new ForestTemplateLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new TemplateParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return TemplateTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ForestTemplateFile(viewProvider);
    }
}
