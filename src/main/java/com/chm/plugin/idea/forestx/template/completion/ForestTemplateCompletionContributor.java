package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.properties.references.PropertiesCompletionContributor;
import com.intellij.lang.properties.references.PropertiesPsiCompletionUtil;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.navigation.DirectNavigationProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.boot.SpringBootConfigFileConstants;
import com.intellij.spring.boot.application.config.SpringBootConfigFileHighlightingUtil;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import com.intellij.spring.boot.application.metadata.additional.SpringBootAdditionalConfigUtils;
import com.intellij.spring.boot.application.properties.SpringBootApplicationPropertiesReplacementTokenCompletionContributor;
import com.intellij.spring.boot.application.yaml.SpringBootApplicationYamlIconProvider;
import com.intellij.spring.boot.application.yaml.SpringBootApplicationYamlKeyCompletionContributor;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
import com.intellij.spring.el.completion.SpringELKeywordCompletionContributor;
import com.intellij.spring.model.converters.SpringCompletionContributor;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class ForestTemplateCompletionContributor extends CompletionContributor {

    public ForestTemplateCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(TemplateTypes.PROP_NAME_PART).withLanguage(ForestTemplateLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
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
                        PsiElement element = parameters.getPosition();
                        String propPrefix = ForestTemplateUtil.getPropertyPrefix(element, false);
                        System.out.println("解析Property Prefix: " + propPrefix);
                        if (SpringBootLibraryUtil.hasSpringBootLibrary(module)) {
                            Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(YAMLFileType.YML, GlobalSearchScope.allScope(project));

                            for (VirtualFile yamlVirtualFile : virtualFiles) {
                                String[] paths = yamlVirtualFile.getName().split("[\\\\/]");
                                String fileName = paths[paths.length - 1];
                                if (!SpringBootConfigFileConstants.APPLICATION_YML.equals(fileName)) {
                                    continue;
                                }
                                PsiFile psiFile = PsiManager.getInstance(project).findFile(yamlVirtualFile);
                                if (!(psiFile instanceof YAMLFileImpl)) {
                                    continue;
                                }
                                YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                                List<YAMLDocument> documents = yamlFile.getDocuments();
                                List<String> keyList = new ArrayList<>();
                                Map<String, YAMLKeyValue> keyValueMap = new HashMap<>();
                                String[] propKeyPaths = propPrefix.split("\\.");
                                int level = 0;
                                for (YAMLDocument document : documents) {
                                    ConfigYamlAccessor accessor = new ConfigYamlAccessor(document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                                    List<YAMLKeyValue> allKeyValues = accessor.getAllKeys();
                                    for (YAMLKeyValue keyValue : allKeyValues) {
                                        keyValueMap.put(keyValue.getKeyText(), keyValue);
                                    }
                                    YAMLKeyValue keyValue = null;
                                    while (level < propKeyPaths.length) {
                                        String path = propKeyPaths[level];
                                        keyValue = keyValueMap.get(path);
                                        if (keyValue != null) {
                                            YAMLValue value = keyValue.getValue();
                                            keyValueMap = new HashMap<>();
                                            PsiElement[] children = Objects.requireNonNull(value).getChildren();
                                            if (children.length > 0) {
                                                for (PsiElement child : children) {
                                                    if (child instanceof YAMLKeyValue) {
                                                        YAMLKeyValue childKeyValue = (YAMLKeyValue) child;
                                                        keyValueMap.put(childKeyValue.getKeyText(), childKeyValue);
                                                        if (level == propKeyPaths.length - 1) {
                                                            for (PsiElement completionKeyValue : Objects.requireNonNull(keyValue.getValue()).getChildren()) {
                                                                keyList.add(((YAMLKeyValue) completionKeyValue).getKeyText());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            level++;
                                        } else {
                                            break;
                                        }
                                    }

                                }

                                for (String key : keyList) {
                                    resultSet.addElement(LookupElementBuilder.create(key)
                                            .withIcon(AllIcons.Nodes.Property));
                                }
                            }

                        }
                    }
                }
        );
    }


}
