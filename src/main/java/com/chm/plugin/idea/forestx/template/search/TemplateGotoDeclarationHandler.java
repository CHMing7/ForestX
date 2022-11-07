package com.chm.plugin.idea.forestx.template.search;

import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePathElementHolder;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.chm.plugin.idea.forestx.template.utils.SpringBootConfigFileUtil;
import com.chm.plugin.idea.forestx.utils.TreeNodeUtilKt;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLUtil;
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

public class TemplateGotoDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement @Nullable [] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (isElIdentifier(sourceElement)) {
            return getGotoELIdentifierDeclarationTargets(sourceElement, offset, editor);
        }
        if (!isPropertyKey(sourceElement)) {
            return null;
        }
        final Project project = sourceElement.getProject();
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
                TreeNodeUtilKt.eachYAMLKeyValues(yamlFile, (keyValue, value) -> {
                    final String keyName = YAMLUtil.getConfigFullName(keyValue);
                    if (keyName.equals(propertyKey)) {
                        results.add(keyValue);
                        return false;
                    }
                    return true;
                });
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
            final YAMLValue itemValue = item.getValue();
            if (itemValue instanceof YAMLPlainTextImpl) {
                String keyName = YAMLUtil.getConfigFullName(item);
                if (keyName.equals(propertyKey)) {
                    results.add(item);
                    return;
                }
            }
        }
    }


    private boolean isElIdentifier(final PsiElement element) {
        if (!(element instanceof LeafPsiElement)) {
            return false;
        }
        if (((LeafPsiElement) element).getElementType().equals(TemplateTypes.EL_IDENTIFIER)) {
            return true;
        }
        return false;
    }

    private boolean isPropertyKey(final PsiElement element) {
        if (!(element instanceof LeafPsiElement)) {
            return false;
        }
        if (((LeafPsiElement) element).getElementType().equals(TemplateTypes.PROP_REFERENCE)) {
            return true;
        }
        return false;
    }

    public PsiElement[] getGotoELIdentifierDeclarationTargets(PsiElement sourceElement, int offset, Editor editor) {
        final Project project = sourceElement.getProject();
        final VirtualFile virtualFile = PsiUtil.getVirtualFile(sourceElement);
        if (virtualFile == null) {
            return null;
        }
        final List<PsiElement> results = new LinkedList<>();
        TreeNodeUtilKt.resolveElement(project, virtualFile, sourceElement,
                (javaVirtualFile, module, isTestSourceFile, hasSpringBootLib, defMethod) -> {
                    ForestTemplatePathElementHolder holder =
                            ForestTemplateUtil.getELHolder(isTestSourceFile, sourceElement, defMethod);
                    if (holder == null) {
                        return;
                    }
                    Object elem = holder.getElement();
                    if (elem instanceof PsiElement) {
                        results.add((PsiElement) elem);
                    }
                });
        return results.toArray(new PsiElement[]{});
    }

}
