package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.holder.ForestTemplateFieldHolder;
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
        final ForestTemplatePathElement pathElement = PsiTreeUtil.getParentOfType(element, ForestTemplatePathElement.class);
        final PsiElement prevElement = pathElement.getPrevSibling();
        if (prevElement == null) {
            return;
        }

        final PsiElement literal = ForestTemplateUtil.getJavaElement(project, element);
        final PsiMethod method = PsiTreeUtil.getParentOfType(literal, PsiMethod.class);
        final ForestTemplatePathElementHolder holder = ForestTemplateUtil.getELHolder(
                isTestSourceFile, prevElement, method);
        if (holder == null) {
            return;
        }
        final PsiType type = holder.getType();
        final PsiClass psiClass = PsiUtil.resolveClassInType(type);
        final PsiMethod[] methods = PsiClassImplUtil.getAllMethods(psiClass);
        final Set<String> nameCache = new HashSet<>();
        for (PsiMethod mtd : methods) {
            final PsiParameterList paramList = mtd.getParameterList();
            if (paramList.getParametersCount() > 1) {
                continue;
            }
            final String methodName = mtd.getName();
            if (nameCache.contains(mtd.getName())) {
                continue;
            }
            nameCache.add(methodName);
            if (methodName.startsWith("get") && methodName.length() > 3) {
                String getter = methodName.substring(3);
                getter = getter.substring(0, 1).toLowerCase() + getter.substring(1);
                ForestTemplateFieldHolder filedHolder = new ForestTemplateFieldHolder(getter, mtd, type);
                resultSet.addElement(LookupElementBuilder.create(filedHolder)
                        .withRenderer(ForestTemplateFieldHolder.FIELD_RENDER));
            }
            final ForestTemplateInvocationHolder invocationHolder = new ForestTemplateInvocationHolder(
                    mtd.getName(), mtd, type, Lists.newArrayList());
            resultSet.addElement(LookupElementBuilder.create(invocationHolder)
                    .withRenderer(ForestTemplateInvocationHolder.INVOCATION_RENDER));
        }
    }
}
