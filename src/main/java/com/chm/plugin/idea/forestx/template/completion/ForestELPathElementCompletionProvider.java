package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.holder.ForestTemplateFiledHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateInvocationHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePathElementHolder;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.ProcessingContext;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ForestELPathElementCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        final PsiElement element = parameters.getPosition();
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

        ForestTemplatePathElement pathElement = PsiTreeUtil.getParentOfType(element, ForestTemplatePathElement.class);
        PsiElement prevElement = pathElement.getPrevSibling();
        if (prevElement == null) {
            return;
        }
        ForestTemplatePathElementHolder holder = ForestTemplateUtil.getELHolder(isTestSourceFile, prevElement);
        if (holder == null) {
            return;
        }
        PsiType type = holder.getType();
        System.out.println(type);
        PsiClass psiClass = PsiUtil.resolveClassInType(type);
        PsiMethod[] methods = PsiClassImplUtil.getAllMethods(psiClass);
        Set<String> nameCache = new HashSet<>();
        for (PsiMethod method : methods) {
            PsiParameterList paramList = method.getParameterList();
            if (paramList.getParametersCount() > 0) {
                continue;
            }
            String methodName = method.getName();
            if (nameCache.contains(method.getName())) {
                continue;
            }
            nameCache.add(methodName);
            int getterIndex = methodName.indexOf("get");
            if (getterIndex >= 0 && methodName.length() > 3) {
                String getter = methodName.substring(3);
                getter = getter.substring(0, 1).toLowerCase() + getter.substring(1);
                ForestTemplateFiledHolder filedHolder = new ForestTemplateFiledHolder(getter, method, type);
                resultSet.addElement(LookupElementBuilder.create(filedHolder)
                        .withRenderer(ForestTemplateFiledHolder.FIELD_RENDER));
            }
            ForestTemplateInvocationHolder invocationHolder = new ForestTemplateInvocationHolder(
                    method.getName(), method, type, Lists.newArrayList());
            resultSet.addElement(LookupElementBuilder.create(invocationHolder)
                    .withRenderer(ForestTemplateInvocationHolder.INVOCATION_RENDER));
        }
    }
}
