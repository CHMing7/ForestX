package com.chm.plugin.idea.forestx.template.utils;

import com.chm.plugin.idea.forestx.template.completion.SearchedConfigProperty;
import com.chm.plugin.idea.forestx.template.completion.SearchedConfigYAMLKeyValue;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateFieldHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateInvocationHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateParameterVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePathElementHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePropertyVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateYAMLVariableHolder;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateArguments;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.chm.plugin.idea.forestx.utils.TreeNodeUtil;
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
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
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

    public final static String FOREST_VARIABLES_PREFIX = "forest.variables.";


    public static VirtualFile getSourceJavaFile(final VirtualFile virtualFile) {
        if (virtualFile instanceof VirtualFileWindow) {
            final VirtualFile delegate = ((VirtualFileWindow) virtualFile).getDelegate();
            return delegate;
        }
        return null;
    }

    public static String getPathOfVirtualFile(final VirtualFile virtualFile) {
        String filePath = virtualFile.getPath();
        if (virtualFile instanceof VirtualFileWindow) {
            final VirtualFile delegate = ((VirtualFileWindow) virtualFile).getDelegate();
            filePath = delegate.getPath();
        }
        return filePath;
    }

    public static boolean isTestFile(final String filePath) {
        return filePath.contains(ForestTemplateUtil.TEST_DIR);
    }

    public static ForestTemplateVariableHolder getConfigHolder(final Project project, final boolean isTestSourceFile, final String keyName, final boolean isEL) {
        final Collection<VirtualFile> virtualFiles = SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);
        if (virtualFiles == null) {
            return null;
        }
        final PsiManager manager = PsiManager.getInstance(project);
        final PsiClassType STRING_TYPE = PsiType.getJavaLangString(manager, GlobalSearchScope.allScope(project));
        for (VirtualFile yamlVirtualFile : virtualFiles) {
            final PsiFile psiFile = PsiManager.getInstance(project).findFile(yamlVirtualFile);
            if (psiFile == null) {
                continue;
            }
            if (psiFile instanceof YAMLFileImpl) {
                // yaml 配置文件
                final YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                final List<YAMLDocument> documents = yamlFile.getDocuments();
                for (final YAMLDocument document : documents) {
                    final ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                            document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    final YAMLKeyValue keyValue = accessor.findExistingKey(keyName);
                    if (keyValue != null) {
                        return new ForestTemplateYAMLVariableHolder(keyName, keyValue, STRING_TYPE, isEL);
                    }
                }
            } else if (psiFile instanceof PropertiesFileImpl) {
                // Properties 配置文件
                final PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                final IProperty property = propertiesFile.findPropertyByKey(keyName);
                if (property != null) {
                    return new ForestTemplatePropertyVariableHolder(keyName, property, STRING_TYPE, isEL);
                }
            }
        }
        return null;
    }

    public static List<ForestTemplateVariableHolder> findConfigHolders(final Project project, final boolean isTestSourceFile, final String prefix, final boolean isEL) {
        final List<ForestTemplateVariableHolder> resultItems = new ArrayList<>();
        final Collection<VirtualFile> virtualFiles = SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);
        if (virtualFiles == null) {
            return null;
        }
        final PsiManager manager = PsiManager.getInstance(project);
        final PsiClassType STRING_TYPE = PsiType.getJavaLangString(manager, GlobalSearchScope.allScope(project));
        for (VirtualFile yamlVirtualFile : virtualFiles) {
            final PsiFile psiFile = PsiManager.getInstance(project).findFile(yamlVirtualFile);
            if (psiFile == null) {
                continue;
            }
            if (psiFile instanceof YAMLFileImpl) {
                // yaml 配置文件
                final YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                final List<YAMLDocument> documents = yamlFile.getDocuments();
                for (final YAMLDocument document : documents) {
                    final ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                            document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    final List<YAMLKeyValue> allDocKeyValues = accessor.getAllKeys();
                    for (final YAMLKeyValue keyValue : allDocKeyValues) {
                        final YAMLValue value = keyValue.getValue();
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
                            for (final YAMLSequenceItem item : ((YAMLBlockSequenceImpl) value).getItems()) {
                                final YAMLValue itemValue = item.getValue();
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
                final PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                final List<IProperty> properties = propertiesFile.getProperties();
                for (final IProperty property : properties) {
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
        final List<SearchedConfigItem> resultItems = new ArrayList<>();
        final Collection<VirtualFile> virtualFiles = SpringBootConfigFileUtil.findSpringBootConfigFiles(project, isTestSourceFile);
        for (VirtualFile yamlVirtualFile : virtualFiles) {
            final PsiFile psiFile = PsiManager.getInstance(project).findFile(yamlVirtualFile);
            if (psiFile == null) {
                continue;
            }
            if (psiFile instanceof YAMLFileImpl) {
                // yaml 配置文件
                final YAMLFileImpl yamlFile = (YAMLFileImpl) psiFile;
                final List<YAMLDocument> documents = yamlFile.getDocuments();
                for (YAMLDocument document : documents) {
                    final ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                            document, SpringBootApplicationMetaConfigKeyManager.getInstance());
                    final List<YAMLKeyValue> allDocKeyValues = accessor.getAllKeys();
                    for (YAMLKeyValue keyValue : allDocKeyValues) {
                        final YAMLValue value = keyValue.getValue();
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
                                final YAMLValue itemValue = item.getValue();
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
                final PropertiesFileImpl propertiesFile = (PropertiesFileImpl) psiFile;
                final List<IProperty> properties = propertiesFile.getProperties();
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

    public static PsiElement getJavaElement(final PsiElement element) {
        PsiFile psiFile = PsiTreeUtil.getParentOfType(element, PsiFile.class);
        return psiFile.getContext().getFirstChild();

//        CompletionContext completionContext = element.getUserData(CompletionContext.COMPLETION_CONTEXT_KEY);
//        if (completionContext == null) {
//            PsiFile psiFile = PsiTreeUtil.getParentOfType(element, PsiFile.class);
//            return psiFile.getContext();
//        }
//        final DocumentWindow doc = (DocumentWindow) completionContext.getOffsetMap().getDocument();
//        final Segment[] textRange = doc.getHostRanges();
////        final TextRange range = new TextRange(textRange[0].getStartOffset(), textRange[0].getEndOffset());
//        final TextRange range = element.getTextRange();
//        final PsiFile javaFile = PsiDocumentManager.getInstance(project).getPsiFile(doc.getDelegate());
//        if (javaFile == null) {
//            return null;
//        }
//        final PsiElement javaFileElement = javaFile.findElementAt(range.getStartOffset());
//        return javaFileElement;
    }


    public static ForestTemplatePathElementHolder getELHolder(
            final boolean isTestSourceFile, final PsiElement element, final PsiMethod defMethod) {
        final Project project = element.getOriginalElement().getProject();
        final String text = element.getText();
        if (element instanceof ForestTemplatePrimary) {
            if (SpringBootLibraryUtil.hasSpringBootLibrary(project)) {
                final String keyName = FOREST_VARIABLES_PREFIX + text;
                final ForestTemplateVariableHolder holder = getConfigHolder(
                        project,
                        isTestSourceFile,
                        keyName, true);
                if (holder != null) {
                    return holder;
                }
            }

            final PsiParameterList paramList = PsiTreeUtil.getChildOfType(defMethod, PsiParameterList.class);
            if (paramList != null && paramList.getParametersCount() > 0) {
                final PsiParameter[] methodParamArray = paramList.getParameters();
                for (int i = 0; i < methodParamArray.length; i++) {
                    final PsiParameter methodParam = methodParamArray[i];
                    final ForestTemplateVariableHolder holder =
                            ForestTemplateParameterVariableHolder.findVariable(methodParam);
                    if (holder != null && text.equals(holder.getVarName())) {
                        return holder;
                    }
                }
            }
            return null;
        }

        if (element instanceof ForestTemplatePathElement) {
            PsiElement firstChild = element.getFirstChild();
            PsiElement prevElement = element.getPrevSibling();
            if (firstChild == null || prevElement == null) {
                return null;
            }
            if (firstChild instanceof ForestTemplateArguments) {
                ForestTemplateArguments args = (ForestTemplateArguments) firstChild;
                final PsiElement namePart = prevElement.getLastChild();
                if (namePart == null) {
                    return null;
                }
                String methodName = namePart.getText();
                ForestTemplatePathElementHolder invokerHolder = getELHolder(
                        isTestSourceFile, prevElement.getPrevSibling(), defMethod);
                if (invokerHolder == null) {
                    return null;
                }
                PsiClass invokerClass = invokerHolder.getPsiClass();
                PsiMethod[] methods = invokerClass.findMethodsByName(methodName, true);
                for (PsiMethod mtd : methods) {
                    if (mtd.getParameterList().getParametersCount() == args.getChildren().length) {
                        return new ForestTemplateInvocationHolder(
                                methodName, mtd, mtd.getReturnType(), new ArrayList<>());
                    }
                }
                return null;
            } else {
                final PsiElement namePart = element.getLastChild();
                if (namePart == null) {
                    return null;
                }
                final String getterName = namePart.getText();
                final ForestTemplatePathElementHolder invokerHolder =
                        getELHolder(isTestSourceFile, element.getPrevSibling(), defMethod);
                if (invokerHolder == null) {
                    return null;
                }
                if (invokerHolder instanceof ForestTemplateYAMLVariableHolder) {
                    final ForestTemplateYAMLVariableHolder yamlHolder = (ForestTemplateYAMLVariableHolder) invokerHolder;
                    final String keyName = yamlHolder.getVarName() + "." + getterName;
                    return ForestTemplateUtil.getConfigHolder(project, isTestSourceFile, keyName, true);

                }
                PsiClass invokerClass = invokerHolder.getPsiClass();
                String methodName = TreeNodeUtil.getterMethodName(getterName);
                PsiMethod[] methods = invokerClass.findMethodsByName(methodName, true);
                if (methods == null || methods.length == 0) {
                    methods = invokerClass.findMethodsByName(getterName, true);
                }
                for (PsiMethod mtd : methods) {
                    if (mtd.getParameterList().getParametersCount() == 0) {
                        return new ForestTemplateFieldHolder(
                                getterName, mtd, mtd.getReturnType());
                    }
                }
                return null;
            }
        }

        if (element instanceof LeafPsiElement) {
            ForestTemplatePrimary primary = PsiTreeUtil.getParentOfType(element, ForestTemplatePrimary.class);
            if (primary != null) {
                return getELHolder(isTestSourceFile, primary, defMethod);
            }
            ForestTemplatePathElement pathElement = PsiTreeUtil.getParentOfType(element, ForestTemplatePathElement.class);
            if (pathElement != null) {
                return getELHolder(isTestSourceFile, pathElement, defMethod);
            }
            return null;
        }

        return null;
    }

}
