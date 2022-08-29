package com.chm.plugin.idea.forestx.template.utils;

import com.chm.plugin.idea.forestx.template.completion.SearchedConfigProperty;
import com.chm.plugin.idea.forestx.template.completion.SearchedConfigYAMLKeyValue;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
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



}
