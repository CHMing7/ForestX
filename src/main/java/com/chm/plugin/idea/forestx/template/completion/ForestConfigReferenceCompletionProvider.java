package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.extension.TreeNodeExtensionKt;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.chm.plugin.idea.forestx.template.utils.SearchedConfigItem;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestConfigReferenceCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        final Project project = parameters.getEditor().getProject();
        final VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile();
        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        if (virtualFile == null) {
            return;
        }
        final Module module = fileIndex.getModuleForFile(virtualFile);
        if (module == null) {
            return;
        }
        final String filePath = ForestTemplateUtil.getPathOfVirtualFile(virtualFile);
        final boolean isTestSourceFile = ForestTemplateUtil.isTestFile(filePath);
        final boolean hasSpringBootLib = TreeNodeExtensionKt.hasSpringBootLibrary(module);
        if (hasSpringBootLib) {
            final List<SearchedConfigItem> searchedConfigItems = ForestTemplateUtil.searchConfigItems(project, isTestSourceFile, null, false);
            for (SearchedConfigItem item : searchedConfigItems) {
                if (item instanceof SearchedConfigYAMLKeyValue) {
                    resultSet.addElement(LookupElementBuilder.create(item)
                            .withRenderer(SearchedConfigYAMLKeyValue.YAML_KEY_VALUE_CONFIG_RENDER));
                } else if (item instanceof SearchedConfigProperty) {
                    resultSet.addElement(LookupElementBuilder.create(item)
                            .withRenderer(SearchedConfigProperty.PROPERTY_RENDER));
                }
            }
        }

    }

}
