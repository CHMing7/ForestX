package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.lang.properties.PropertiesHighlighter;
import com.intellij.microservices.config.yaml.ConfigYamlUtils;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.boot.SpringBootConfigFileConstants;
import com.intellij.spring.boot.application.config.SpringBootConfigFileHighlightingUtil;
import com.intellij.spring.boot.application.config.SpringBootConfigurationPropertyReferenceSearcher;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ForestTemplateCompletionContributor extends CompletionContributor {

    public ForestTemplateCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TemplateTypes.PROP_NAME_PART).withLanguage(ForestTemplateLanguage.INSTANCE),
                new ForestTemplateCompletionProvider());
    }


}
