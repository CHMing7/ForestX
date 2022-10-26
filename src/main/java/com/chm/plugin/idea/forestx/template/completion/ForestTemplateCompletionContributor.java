package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionInitializationContextImpl;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.OffsetKey;
import com.intellij.codeInsight.completion.OffsetMap;
import com.intellij.codeInsight.completion.OffsetsInFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestTemplateCompletionContributor extends CompletionContributor {

    public ForestTemplateCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TemplateTypes.PROP_REFERENCE),
                new ForestConfigReferenceCompletionProvider());
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inside(ForestTemplatePrimary.class).afterLeaf("${", "{"),
                new ForestELIdentifierCompletionProvider());
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inside(ForestTemplatePathElement.class).afterLeaf("."),
                new ForestELPathElementCompletionProvider());
    }

}
