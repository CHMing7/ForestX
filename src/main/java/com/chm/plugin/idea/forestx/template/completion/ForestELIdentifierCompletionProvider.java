package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.extension.TreeNodeExtensionKt;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateBindingVarHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateParameterIndexVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateParameterVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePropertyVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateYAMLVariableHolder;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestELIdentifierCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        final PsiElement element = parameters.getPosition();
        final Project project = element.getProject();
        final VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile();
        if (virtualFile == null) {
            return;
        }
        TreeNodeExtensionKt.resolveElement(project, virtualFile, element,
                (javaVirtualFile, module, isTestSourceFile, hasSpringBootLib, method) -> {
                    if (method != null) {
                        TreeNodeExtensionKt.findMethodParameter(method, (methodParam, i) -> {
                            final ForestTemplateParameterIndexVariableHolder indexVariableHolder =
                                    ForestTemplateParameterIndexVariableHolder.findIndexVariable(methodParam, i);
                            resultSet.addElement(LookupElementBuilder.create(indexVariableHolder)
                                    .withRenderer(ForestTemplateParameterIndexVariableHolder.PARAMETER_INDEX_VAR_RENDER));
                            final ForestTemplateParameterVariableHolder variableHolder =
                                    ForestTemplateParameterVariableHolder.findVariable(methodParam);
                            if (variableHolder != null) {
                                resultSet.addElement(LookupElementBuilder.create(variableHolder)
                                        .withRenderer(ForestTemplateParameterVariableHolder.PARAMETER_VAR_RENDER));
                            }
                            return true;
                        });
                    }
                    if (hasSpringBootLib) {
                        final List<ForestTemplateVariableHolder> variableHolders = ForestTemplateUtil.findConfigHolders(
                                project, isTestSourceFile, ForestTemplateUtil.FOREST_VARIABLES_PREFIX, true);
                        for (final ForestTemplateVariableHolder holder : variableHolders) {
                            if (holder instanceof ForestTemplateYAMLVariableHolder) {
                                resultSet.addElement(LookupElementBuilder.create(holder)
                                        .withRenderer(ForestTemplateYAMLVariableHolder.YAML_KEY_VALUE_CONFIG_RENDER));
                            } else if (holder instanceof ForestTemplatePropertyVariableHolder) {
                                resultSet.addElement(LookupElementBuilder.create(holder)
                                        .withRenderer(ForestTemplatePropertyVariableHolder.PROPERTY_RENDER));
                            }
                        }
                    }

                    ForestTemplateBindingVarHolder.eachAllVariables(project, holder -> {
                        resultSet.addElement(LookupElementBuilder.create(holder)
                                .withRenderer(ForestTemplateBindingVarHolder.BINDING_VAR_RENDER));
                        return true;
                    });
                });
    }

}
