package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.chm.plugin.idea.forestx.template.utils.SpringBootConfigFileUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.ide.SearchTopHitProvider;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.injected.editor.VirtualFileWindowImpl;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.PropertiesImplUtil;
import com.intellij.lang.properties.PropertiesUtil;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
import com.intellij.spring.el.completion.SpringELKeywordCompletionContributor;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLBlockSequenceImpl;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
        String filePath = virtualFile.getPath();
        if (virtualFile instanceof VirtualFileWindow) {
            VirtualFile delegate = ((VirtualFileWindow) virtualFile).getDelegate();
            filePath = delegate.getPath();
        }
        final String testDir = "/src/test/";
        boolean isTestSourceFile = filePath.contains(testDir);
        final boolean hasSpringBootLib = SpringBootLibraryUtil.hasSpringBootLibrary(module);
        if (hasSpringBootLib) {
            Collection<VirtualFile> virtualFiles = SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);
            for (VirtualFile yamlVirtualFile : virtualFiles) {
                PsiFile psiFile = PsiManager.getInstance(project).findFile(yamlVirtualFile);
                if (psiFile == null) {
                    continue;
                }
                if (psiFile instanceof YAMLFileImpl) {
                    // yaml 配置文件
                    YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                    List<YAMLDocument> documents = yamlFile.getDocuments();
                    List<SearchedYAMLKeyValue> keyValueList = new ArrayList<>();
                    for (YAMLDocument document : documents) {
                        ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                                document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                        List<YAMLKeyValue> allDocKeyValues = accessor.getAllKeys();
                        for (YAMLKeyValue keyValue : allDocKeyValues) {
                            YAMLValue value = keyValue.getValue();
                            if (value instanceof YAMLPlainTextImpl) {
                                String key = YAMLUtil.getConfigFullName(keyValue);
                                keyValueList.add(new SearchedYAMLKeyValue(key, keyValue));
                            } else if (value instanceof YAMLBlockSequenceImpl) {
                                for (YAMLSequenceItem item : ((YAMLBlockSequenceImpl) value).getItems()) {
                                    YAMLValue itemValue = item.getValue();
                                    if (itemValue instanceof YAMLPlainTextImpl) {
                                        String key = YAMLUtil.getConfigFullName(item);
                                        keyValueList.add(new SearchedYAMLKeyValue(key, item));
                                    }
                                }
                            }
                        }
                    }
                    for (SearchedYAMLKeyValue key : keyValueList) {
                        resultSet.addElement(LookupElementBuilder.create(key)
                                .withRenderer(SearchedYAMLKeyValue.YAML_KEY_VALUE_RENDER));
                    }
                } else if (psiFile instanceof PropertiesFileImpl) {
                    // Properties 配置文件
                    PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                    List<IProperty> properties = propertiesFile.getProperties();
                    List<SearchedProperty> resultProperties = new ArrayList<>();
                    for (IProperty property : properties) {
                        String key = property.getKey();
                        resultProperties.add(new SearchedProperty(key, property));
                    }
                    for (SearchedProperty searchedProperty : resultProperties) {
                        resultSet.addElement(LookupElementBuilder.create(searchedProperty)
                                .withRenderer(SearchedProperty.PROPERTY_RENDER));
                    }
                }
            }
        }

    }

}
