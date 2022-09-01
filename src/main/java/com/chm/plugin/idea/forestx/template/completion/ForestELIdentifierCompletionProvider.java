package com.chm.plugin.idea.forestx.template.completion;

import a.c.P;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateIdentifier;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.chm.plugin.idea.forestx.template.utils.SearchedConfigItem;
import com.intellij.aop.psi.SpringAopCompletionContributor;
import com.intellij.codeInsight.completion.CompletionContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.injected.editor.DocumentWindow;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileUrlChangeAdapter;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
import com.intellij.spring.boot.mvc.SpringBootMvcReferenceContributor;
import com.intellij.spring.boot.mvc.SpringBootMvcSemContributor;
import com.intellij.spring.el.SpringElInjector;
import com.intellij.spring.el.completion.SpringELKeywordCompletionContributor;
import com.intellij.spring.model.highlighting.SpringAopInspectionsRegistryContributor;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestELIdentifierCompletionProvider extends CompletionProvider<CompletionParameters> {


    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        final PsiElement element = parameters.getPosition();
//        if (!isIdentifier(element)) {
//            return;
//        }
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

        CompletionContext completionContext = element.getUserData(CompletionContext.COMPLETION_CONTEXT_KEY);
        if (completionContext == null) {
            return;
        }
        DocumentWindow doc = (DocumentWindow) completionContext.getOffsetMap().getDocument();

        Segment[] textRange = doc.getHostRanges();
        TextRange range = new TextRange(textRange[0].getStartOffset(), textRange[0].getEndOffset());

        PsiFile javaFile = PsiDocumentManager.getInstance(project).getPsiFile(doc.getDelegate());
        PsiElement literal = javaFile.findElementAt(range.getStartOffset());
        PsiMethod method = PsiTreeUtil.getParentOfType(literal, PsiMethod.class);
        if (method != null) {
            PsiParameterList paramList = PsiTreeUtil.getChildOfType(method, PsiParameterList.class);
            if (paramList != null && paramList.getParametersCount() > 0) {
                for (PsiParameter methodParam : paramList.getParameters()) {
                    SearchedForestParameterVariable variable = SearchedForestParameterVariable.findVariable(methodParam);
                    if (variable == null) {
                        continue;
                    }
                    resultSet.addElement(LookupElementBuilder.create(variable)
                            .withRenderer(SearchedForestParameterVariable.PARAMETER_VAR_RENDER));
                }
            }
        }

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
