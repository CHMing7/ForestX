package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateIdentifier;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateIdentifierImpl;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionInitializationContextImpl;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.OffsetKey;
import com.intellij.codeInsight.completion.OffsetMap;
import com.intellij.codeInsight.completion.OffsetsInFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.spring.groovy.GroovySpringReferenceContributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestTemplateCompletionContributor extends CompletionContributor {

    public ForestTemplateCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TemplateTypes.PROP_REFERENCE),
                new ForestConfigReferenceCompletionProvider());
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TemplateTypes.EL_IDENTIFIER).inside(ForestTemplatePrimary.class).afterLeaf("${", "{"),
                new ForestELIdentifierCompletionProvider());
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TemplateTypes.EL_INT).inside(ForestTemplatePrimary.class).afterLeaf("${", "{"),
                new ForestELIdentifierCompletionProvider());


    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        if (!(context instanceof CompletionInitializationContextImpl)) {
            return;
        }

        final OffsetsInFile offsetsInFile = ((CompletionInitializationContextImpl) context).getHostOffsets();
        final OffsetMap offsetMap = offsetsInFile.getOffsets();
        final List<OffsetKey> allOffsets =  offsetMap.getAllOffsets();
        if (allOffsets.isEmpty()) {
            return;
        }

        final int offset = offsetMap.getOffset(allOffsets.get(0));
        PsiElement javaLiteral = offsetsInFile.getFile().findElementAt(offset);
        if (javaLiteral != null) {
        }
    }
}
