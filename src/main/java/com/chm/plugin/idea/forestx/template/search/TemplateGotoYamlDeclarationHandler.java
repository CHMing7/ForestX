package com.chm.plugin.idea.forestx.template.search;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateElementType;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateTokenType;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.chm.plugin.idea.forestx.template.utils.SpringBootConfigFileUtil;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.microservices.config.yaml.ConfigYamlUtils;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLBlockSequenceImpl;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TemplateGotoYamlDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement @Nullable [] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        final Project project = sourceElement.getProject();
        if (!isPropertyKey(sourceElement)) {
            return null;
        }
        final List<PsiElement> results = new LinkedList<>();
        final String propertyKey = sourceElement.getText();
        final VirtualFile sourceVirtualFile = sourceElement.getContainingFile().getVirtualFile();
        final String filePath = ForestTemplateUtil.getPathOfVirtualFile(sourceVirtualFile);
        final boolean isTestSourceFile = ForestTemplateUtil.isTestFile(filePath);
        final Collection<VirtualFile> virtualFiles =
                SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);

        for (final VirtualFile virtualFile : virtualFiles) {
            final PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            if (psiFile instanceof YAMLFileImpl) {
                final YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                for (final YAMLDocument document : yamlFile.getDocuments()) {
                    final ConfigYamlAccessor accessor =
                            new ConfigYamlAccessor(document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    final List<YAMLKeyValue> allKeyValues = accessor.getAllKeys();
                    for (final YAMLKeyValue keyValue : allKeyValues) {
                        final YAMLValue value = keyValue.getValue();
                        if (value instanceof YAMLPlainTextImpl) {
                            final String keyName = YAMLUtil.getConfigFullName(keyValue);
                            if (keyName.equals(propertyKey)) {
                                results.add(keyValue);
                                break;
                            }
                        } else if (value instanceof YAMLBlockSequenceImpl) {
                            processYAMLBlockSequence((YAMLBlockSequenceImpl) value, propertyKey, results);
                        }
                    }
                }
            } else if (psiFile instanceof PropertiesFileImpl) {
                final PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                for (final IProperty property : propertiesFile.getProperties()) {
                    final String key = property.getKey();
                    if (StringUtils.isNotEmpty(key) && key.equals(propertyKey)) {
                        results.add(property.getPsiElement());
                    }
                }
            }
        }
        return results.toArray(new PsiElement[0]);
    }


    private void processYAMLBlockSequence(YAMLBlockSequenceImpl blockSequence, String propertyKey, List<PsiElement> results) {
        for (YAMLSequenceItem item : blockSequence.getItems()) {
            YAMLValue itemValue = item.getValue();
            if (itemValue instanceof YAMLPlainTextImpl) {
                String keyName = YAMLUtil.getConfigFullName(item);
                if (keyName.equals(propertyKey)) {
                    results.add(item);
                    return;
                }
            }
        }
    }

    public boolean isPropertyKey(PsiElement element) {
        if (!(element instanceof LeafPsiElement)) {
            return false;
        }
        if (((LeafPsiElement) element).getElementType().equals(TemplateTypes.PROP_REFERENCE)) {
            return true;
        }
        return false;
    }

}
