package com.chm.plugin.idea.forestx.template.utils;

import com.chm.plugin.idea.forestx.template.completion.SearchedConfigProperty;
import com.chm.plugin.idea.forestx.template.completion.SearchedConfigYAMLKeyValue;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePathElementHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePropertyVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateYAMLVariableHolder;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.intellij.codeInsight.completion.CompletionContext;
import com.intellij.injected.editor.DocumentWindow;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeMapper;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLBlockSequenceImpl;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ForestTemplateUtil {

    public final static String TEST_DIR = "/src/test/";


    public static VirtualFile getSourceJavaFile(final VirtualFile virtualFile) {
        if (virtualFile instanceof VirtualFileWindow) {
            VirtualFile delegate = ((VirtualFileWindow) virtualFile).getDelegate();
            return delegate;
        }
        return null;
    }

    public static String getPathOfVirtualFile(final VirtualFile virtualFile) {
        String filePath = virtualFile.getPath();
        if (virtualFile instanceof VirtualFileWindow) {
            VirtualFile delegate = ((VirtualFileWindow) virtualFile).getDelegate();
            filePath = delegate.getPath();
        }
        return filePath;
    }

    public static boolean isTestFile(String filePath) {
        return filePath.contains(ForestTemplateUtil.TEST_DIR);
    }


    public static List<ForestTemplateVariableHolder> findConfigHolders(final Project project, final boolean isTestSourceFile, final String prefix, final boolean isEL) {
        List<ForestTemplateVariableHolder> resultItems = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);
        final PsiManager manager = PsiManager.getInstance(project);
        final PsiClassType STRING_TYPE = PsiType.getJavaLangString(manager, GlobalSearchScope.allScope(project));
        for (VirtualFile yamlVirtualFile : virtualFiles) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(yamlVirtualFile);
            if (psiFile == null) {
                continue;
            }
            if (psiFile instanceof YAMLFileImpl) {
                // yaml 配置文件
                YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                List<YAMLDocument> documents = yamlFile.getDocuments();
                for (YAMLDocument document : documents) {
                    ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                            document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    List<YAMLKeyValue> allDocKeyValues = accessor.getAllKeys();
                    for (YAMLKeyValue keyValue : allDocKeyValues) {
                        YAMLValue value = keyValue.getValue();
                        if (value instanceof YAMLPlainTextImpl) {
                            String key = YAMLUtil.getConfigFullName(keyValue);
                            if (prefix != null) {
                                if (key.length() > prefix.length() && key.startsWith(prefix)) {
                                    key = key.substring(prefix.length());
                                    resultItems.add(new ForestTemplateYAMLVariableHolder(key, keyValue, STRING_TYPE, isEL));
                                }
                            } else {
                                resultItems.add(new ForestTemplateYAMLVariableHolder(key, keyValue, STRING_TYPE, isEL));
                            }
                        } else if (value instanceof YAMLBlockSequenceImpl) {
                            for (YAMLSequenceItem item : ((YAMLBlockSequenceImpl) value).getItems()) {
                                YAMLValue itemValue = item.getValue();
                                if (itemValue instanceof YAMLPlainTextImpl) {
                                    String key = YAMLUtil.getConfigFullName(item);
                                    if (prefix != null) {
                                        if (key.length() > prefix.length() && key.startsWith(prefix)) {
                                            key = key.substring(prefix.length());
                                            resultItems.add(new ForestTemplateYAMLVariableHolder(key, item, STRING_TYPE, isEL));
                                        }
                                    } else {
                                        resultItems.add(new ForestTemplateYAMLVariableHolder(key, item, STRING_TYPE, isEL));
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (psiFile instanceof PropertiesFileImpl) {
                // Properties 配置文件
                PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                List<IProperty> properties = propertiesFile.getProperties();
                for (IProperty property : properties) {
                    String key = property.getKey();
                    if (key == null) {
                        continue;
                    }
                    if (prefix != null) {
                        if (key.length() > prefix.length() && key.startsWith(prefix)) {
                            key = key.substring(prefix.length() + 1);
                            resultItems.add(
                                    new ForestTemplatePropertyVariableHolder(key, property, STRING_TYPE, isEL));
                        }
                    } else {
                        resultItems.add(
                                new ForestTemplatePropertyVariableHolder(key, property, STRING_TYPE, isEL));
                    }
                }
            }
        }
        return resultItems;
    }


    public static List<SearchedConfigItem> searchConfigItems(final Project project, final boolean isTestSourceFile, final String prefix, final boolean isEL) {
        List<SearchedConfigItem> resultItems = new ArrayList<>();
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
                for (YAMLDocument document : documents) {
                    ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                            document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    List<YAMLKeyValue> allDocKeyValues = accessor.getAllKeys();
                    for (YAMLKeyValue keyValue : allDocKeyValues) {
                        YAMLValue value = keyValue.getValue();
                        if (value instanceof YAMLPlainTextImpl) {
                            String key = YAMLUtil.getConfigFullName(keyValue);
                            if (prefix != null) {
                                if (key.length() > prefix.length() && key.startsWith(prefix)) {
                                    key = key.substring(prefix.length());
                                    resultItems.add(new SearchedConfigYAMLKeyValue(key, keyValue, isEL));
                                }
                            } else {
                                resultItems.add(new SearchedConfigYAMLKeyValue(key, keyValue, isEL));
                            }
                        } else if (value instanceof YAMLBlockSequenceImpl) {
                            for (YAMLSequenceItem item : ((YAMLBlockSequenceImpl) value).getItems()) {
                                YAMLValue itemValue = item.getValue();
                                if (itemValue instanceof YAMLPlainTextImpl) {
                                    String key = YAMLUtil.getConfigFullName(item);
                                    if (prefix != null) {
                                        if (key.length() > prefix.length() && key.startsWith(prefix)) {
                                            key = key.substring(prefix.length());
                                            resultItems.add(new SearchedConfigYAMLKeyValue(key, item, isEL));
                                        }
                                    } else {
                                        resultItems.add(new SearchedConfigYAMLKeyValue(key, item, isEL));
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (psiFile instanceof PropertiesFileImpl) {
                // Properties 配置文件
                PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                List<IProperty> properties = propertiesFile.getProperties();
                for (IProperty property : properties) {
                    String key = property.getKey();
                    if (key == null) {
                        continue;
                    }
                    if (prefix != null) {
                        if (key.length() > prefix.length() && key.startsWith(prefix)) {
                            key = key.substring(prefix.length() + 1);
                            resultItems.add(new SearchedConfigProperty(key, property, isEL));
                        }
                    } else {
                        resultItems.add(new SearchedConfigProperty(key, property, isEL));
                    }
                }
            }
        }
        return resultItems;
    }

    public static PsiElement getJavaElement(final Project project, final PsiElement element) {
        final CompletionContext completionContext = element.getUserData(CompletionContext.COMPLETION_CONTEXT_KEY);
        if (completionContext == null) {
            return null;
        }
        final DocumentWindow doc = (DocumentWindow) completionContext.getOffsetMap().getDocument();
        final Segment[] textRange = doc.getHostRanges();
        final TextRange range = new TextRange(textRange[0].getStartOffset(), textRange[0].getEndOffset());
        final PsiFile javaFile = PsiDocumentManager.getInstance(project).getPsiFile(doc.getDelegate());
        if (javaFile == null) {
            return null;
        }
        final PsiElement javaFileElement = javaFile.findElementAt(range.getStartOffset());
        return javaFileElement;
    }


    public static ForestTemplatePathElementHolder getHolder(PsiElement element) {
        if (element instanceof ForestTemplatePrimary) {

        }
        return null;
    }

}
