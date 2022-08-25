package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ForestTemplateToJavaInjector implements MultiHostInjector {

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
        if (context instanceof PsiLiteralExpressionImpl && shouldInject(context)) {
            PsiLiteralExpressionImpl expr = (PsiLiteralExpressionImpl) context;
            registrar
                    .startInjecting(ForestTemplateLanguage.INSTANCE)
                    .addPlace(null, null, expr, innerRangeStrippingQuotes(expr))
                    .doneInjecting();
        }
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(PsiLiteralExpression.class);
    }

    public TextRange innerRangeStrippingQuotes(PsiLiteralExpression expr) {
        return new TextRange(
                0,
                expr.getTextLength());
    }

    public boolean shouldInject(PsiElement context) {
        if (!(context instanceof PsiLiteralExpression)) {
            return false;
        }
        PsiLiteralExpression expr = (PsiLiteralExpression) context;
        PsiElement parent = expr.getParent();
        if (parent instanceof PsiNameValuePair) {
            PsiNameValuePair nameValuePair = (PsiNameValuePair) parent;
            parent = nameValuePair.getParent();
            if (parent instanceof PsiAnnotationParameterList) {
                PsiAnnotationParameterList annotationParameterList = (PsiAnnotationParameterList) parent;
                parent = annotationParameterList.getParent();
                if (parent instanceof PsiAnnotation) {
                    PsiAnnotation annotation = (PsiAnnotation) parent;
                    return Annotation.isForestAnnotation(annotation.getQualifiedName());
                }
            }
        }
        return false;
    }

}
