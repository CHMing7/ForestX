package com.chm.plugin.idea.forestx.template.search;

import a.c.P;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyExpress;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.boot.SpringBootConfigFileConstants;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import com.intellij.spring.boot.application.yaml.SpringBootApplicationYamlKeyCompletionContributor;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.navigation.YAMLScalarKeyDeclarationSearcher;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TemplateGotoYamlDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement @Nullable [] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        Project project = sourceElement.getProject();
        if (!isPropertyKey(sourceElement)) {
            return null;
        }
        List<PsiElement> results = new LinkedList<>();
        String propertyKey = ForestTemplateUtil.getPropertyPrefix(sourceElement, true);
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(YAMLFileType.YML, GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            String[] paths = virtualFile.getName().split("[\\\\/]");
            String fileName = paths[paths.length - 1];
            if (!SpringBootConfigFileConstants.APPLICATION_YML.equals(fileName)) {
                continue;
            }
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            if (!(psiFile instanceof YAMLFileImpl)) {
                continue;
            }
            YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
            List<YAMLDocument> documents = yamlFile.getDocuments();
            for (YAMLDocument document : documents) {
                ConfigYamlAccessor accessor = new ConfigYamlAccessor(document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                YAMLKeyValue keyValue = accessor.findExistingKey(propertyKey);
                if (keyValue == null) {
                    continue;
                }
                results.add(keyValue);
            }
        }
        return results.toArray(new PsiElement[0]);
    }


    public boolean isPropertyKey(PsiElement element) {
        if (!(element instanceof LeafPsiElement)) {
            return false;
        }
        PsiElement parent = element.getParent();
        if (parent instanceof ForestTemplatePropertyExpress) {
            return true;
        }
        return false;
    }

}
