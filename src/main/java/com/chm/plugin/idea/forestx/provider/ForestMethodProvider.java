package com.chm.plugin.idea.forestx.provider;

import com.chm.plugin.idea.forestx.constants.Constants;
import com.chm.plugin.idea.forestx.tw.RightSidebarToolWindow;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-07-29
 **/
public class ForestMethodProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (!(element instanceof PsiClass)) {
            return;
        }
        PsiClass psiClass = (PsiClass) element;
        Project project = element.getProject();
        RightSidebarToolWindow mainForm = Constants.getRightSidebar(project);
        mainForm.processClass(psiClass);
        mainForm.afterProcessClass();
    }
}
