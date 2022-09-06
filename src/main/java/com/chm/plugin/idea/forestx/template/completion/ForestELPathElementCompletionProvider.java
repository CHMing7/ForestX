package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePathElementHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateVariableHolder;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateNamePart;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.spring.boot.library.SpringBootLibraryUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class ForestELPathElementCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
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
        final boolean hasSpringBootLib = SpringBootLibraryUtil.hasSpringBootLibrary(module);

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
    }
}
