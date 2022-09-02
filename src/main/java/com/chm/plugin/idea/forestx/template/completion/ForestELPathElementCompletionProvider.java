package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class ForestELPathElementCompletionProvider extends CompletionProvider<CompletionParameters> {



    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement element = parameters.getPosition();
        ForestTemplatePathElement prevPathElement = PsiTreeUtil.getPrevSiblingOfType(element, ForestTemplatePathElement.class);
        PsiElement prevElement = null;
        if (prevPathElement != null) {
            prevElement = prevPathElement;
        } else {
            prevElement = PsiTreeUtil.getPrevSiblingOfType(element, ForestTemplatePrimary.class);
        }
        if (prevElement == null) {
            return;
        }
    }
}
