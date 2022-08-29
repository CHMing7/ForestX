package com.chm.plugin.idea.forestx.template.completion;

import a.c.P;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateIdentifier;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
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
import com.intellij.openapi.vfs.VirtualFileUrlChangeAdapter;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
import com.intellij.spring.el.completion.SpringELKeywordCompletionContributor;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestELIdentifierCompletionProvider extends CompletionProvider<CompletionParameters> {


    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        final PsiElement element = parameters.getPosition();
        if (!isIdentifier(element)) {
            return;
        }
        final Project project = element.getProject();
        final VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile();
        if (virtualFile == null) {
            return;
        }
        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        final Module module = fileIndex.getModuleForFile(virtualFile);
        if (module == null) {
            return;
        }
        final VirtualFile javaVirtualFile = ForestTemplateUtil.getSourceJavaFile(virtualFile);
        if (javaVirtualFile == null) {
            return;
        }
        final String filePath = javaVirtualFile.getPath();
        final boolean isTestSourceFile = ForestTemplateUtil.isTestFile(filePath);
        final boolean hasSpringBootLib = SpringBootLibraryUtil.hasSpringBootLibrary(module);
        ForestTemplateIdentifier identifier = (ForestTemplateIdentifier) element.getParent();
        if (isFirstIdentifier(identifier)) {
            if (hasSpringBootLib) {
                List<SearchedConfigItem> searchedConfigItems = ForestTemplateUtil.searchConfigItems(project, isTestSourceFile, "forest.variables.", true);
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

    private boolean isIdentifier(PsiElement element) {
        if (!(element instanceof LeafPsiElement)) {
            return false;
        }
        PsiElement parent = element.getParent();
        if (parent instanceof ForestTemplateIdentifier) {
            return true;
        }
        return false;
    }

    private boolean isFirstIdentifier(ForestTemplateIdentifier identifier) {
        PsiElement parent = identifier.getParent();
        if (parent instanceof ForestTemplatePrimary) {
            return true;
        }
        return false;
    }

}
