package com.chm.plugin.idea.forestx.startup;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.constants.Constants;
import com.chm.plugin.idea.forestx.tw.RightSidebarToolWindow;
import com.google.common.collect.Sets;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AnnotationTargetsSearch;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-09
 **/
public class ForestCheckTask extends Backgroundable {

    ForestCheckTask(Project project) {
        super(project, "Forest check");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        ReadAction.nonBlocking(() ->
                        this.runCollectors(ProgressIndicatorProvider.getGlobalProgressIndicator()))
                .inSmartMode(this.myProject)
                .wrapProgress(indicator)
                .executeSynchronously();
    }

    private void runCollectors(ProgressIndicator indicator) {
        Set<PsiClass> searchedSet = Sets.newHashSet();
        GlobalSearchScope librariesScope = ProjectScope.getLibrariesScope(myProject);
        SearchScope inheritorsScope = GlobalSearchScopesCore.projectProductionScope(this.myProject).union(librariesScope);
        for (Annotation annotation : Annotation.FOREST_METHOD_ANNOTATION) {
            PsiClass annotationClass = JavaPsiFacade.getInstance(this.myProject).findClass(annotation.getQualifiedName(), librariesScope);
            if (annotationClass == null) {
                continue;
            }
            Query<PsiModifierListOwner> annotationTargets = AnnotationTargetsSearch.search(annotationClass, inheritorsScope);
            Collection<PsiModifierListOwner> all = annotationTargets.findAll();
            for (PsiModifierListOwner psiModifierListOwner : all) {
                if (psiModifierListOwner instanceof PsiClass) {
                    searchedSet.add((PsiClass) psiModifierListOwner);
                } else if (psiModifierListOwner instanceof PsiMethod) {
                    searchedSet.add(((PsiMethod) psiModifierListOwner).getContainingClass());
                }
            }

            RightSidebarToolWindow mainForm = Constants.getRightSidebar(this.myProject);
            int i = 0;
            for (PsiClass psiClass : searchedSet) {
                mainForm.processClass(psiClass);
                mainForm.afterProcessClass();
                indicator.setFraction((double) (i++) / (double) searchedSet.size());
            }
        }
    }
}
