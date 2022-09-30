package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.template.context.ForestTemplateContextProvider;
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
import com.intellij.spring.el.SpringElInjector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ForestTemplateToJavaInjector implements MultiHostInjector {

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {
        if (host instanceof PsiLiteralExpressionImpl && shouldInject(host)) {
            final PsiLiteralExpressionImpl expr = (PsiLiteralExpressionImpl) host;

            registrar
                    .startInjecting(ForestTemplateLanguage.INSTANCE)
                    .addPlace(null, null, expr, innerRangeStrippingQuotes(expr))
                    .doneInjecting();

            host.putUserData(ForestTemplateContextProvider.TemplateContextKey, new ForestTemplateContextProvider(host));
        }
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(PsiLiteralExpression.class);
    }

    public TextRange innerRangeStrippingQuotes(final PsiLiteralExpression expr) {
        return new TextRange(
                0,
                expr.getTextLength());
    }

    public boolean shouldInject(final PsiElement context) {
        if (!(context instanceof PsiLiteralExpression)) {
            return false;
        }
        final PsiLiteralExpression expr = (PsiLiteralExpression) context;
        PsiElement parent = expr.getParent();
        if (parent instanceof PsiNameValuePair) {
            final PsiNameValuePair nameValuePair = (PsiNameValuePair) parent;
            parent = nameValuePair.getParent();
            if (parent instanceof PsiAnnotationParameterList) {
                final PsiAnnotationParameterList annotationParameterList = (PsiAnnotationParameterList) parent;
                parent = annotationParameterList.getParent();
                if (parent instanceof PsiAnnotation) {
                    final PsiAnnotation annotation = (PsiAnnotation) parent;
                    return Annotation.isForestAnnotation(annotation.getQualifiedName());
                }
            }
        }
        return false;
    }

}
