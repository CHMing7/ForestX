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
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.boot.SpringBootConfigFileConstants;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
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
        Project project = sourceElement.getProject();
        if (!isPropertyKey(sourceElement)) {
            return null;
        }
        List<PsiElement> results = new LinkedList<>();
        String propertyKey = sourceElement.getText();
        final String testDir = "/src/test/";
        VirtualFile sourceVirtualFile = sourceElement.getContainingFile().getVirtualFile();
        String filePath = sourceVirtualFile.getPath();
        if (sourceVirtualFile instanceof VirtualFileWindow) {
            VirtualFile delegate = ((VirtualFileWindow) sourceVirtualFile).getDelegate();
            filePath = delegate.getPath();
        }
        boolean isTestSourceFile = filePath.contains(testDir);
        Collection<VirtualFile> virtualFiles = SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);

        for (VirtualFile virtualFile : virtualFiles) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            if (psiFile instanceof YAMLFileImpl) {
                YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                for (YAMLDocument document : yamlFile.getDocuments()) {
                    ConfigYamlAccessor accessor = new ConfigYamlAccessor(document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    List<YAMLKeyValue> allKeyValues = accessor.getAllKeys();
                    for (YAMLKeyValue keyValue : allKeyValues) {
                        YAMLValue value = keyValue.getValue();
                        if (value instanceof YAMLPlainTextImpl) {
                            String keyName = YAMLUtil.getConfigFullName(keyValue);
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
                PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                for (IProperty property : propertiesFile.getProperties()) {
                    if (property.getKey().equals(propertyKey)) {
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
                    results.add(itemValue);
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
