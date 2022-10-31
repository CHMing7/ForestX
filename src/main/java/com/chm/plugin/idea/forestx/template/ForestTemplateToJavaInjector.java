package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.template.context.ForestTemplateContextProvider;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplateLanguage;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ForestTemplateToJavaInjector implements MultiHostInjector {

    private final static Set<String> FOREST_METHODS = new HashSet<>();

    static {
        FOREST_METHODS.add("get");
        FOREST_METHODS.add("post");
        FOREST_METHODS.add("put");
        FOREST_METHODS.add("delete");
        FOREST_METHODS.add("head");
        FOREST_METHODS.add("patch");
        FOREST_METHODS.add("options");
        FOREST_METHODS.add("trace");
    }

    private final static Set<String> FOREST_REQUEST_METHODS = new HashSet<>();

    static {
        FOREST_METHODS.add("url");
        FOREST_METHODS.add("setUrl");
    }


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
        final String text = expr.getText();
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        PsiElement parent = expr.getParent();
        if (parent instanceof PsiNameValuePair) {
            final PsiNameValuePair nameValuePair = (PsiNameValuePair) parent;
            parent = nameValuePair.getParent();
            if (parent instanceof PsiAnnotationParameterList) {
                final PsiAnnotationParameterList annotationParameterList = (PsiAnnotationParameterList) parent;
                parent = annotationParameterList.getParent();
                if (parent instanceof PsiAnnotation) {
                    final PsiAnnotation annotation = (PsiAnnotation) parent;
                    return Annotation.Companion.isForestAnnotation(annotation) && containsELScript(text);
                }
            }
        } else if (parent instanceof PsiExpressionList) {
            parent = parent.getParent();
            if (parent instanceof PsiMethodCallExpression) {
                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) parent;
                PsiMethod method = methodCallExpression.resolveMethod();
                PsiClass clazz = PsiTreeUtil.getParentOfType(method, PsiClass.class);
                if (isForestOfConfigurationClass(clazz)) {
                    return isForestInjectionMethods(method) && containsELScript(text);
                }
                if (isForestRequestClass(clazz)) {
                    return isForestRequestInjectionMethods(method) && containsELScript(text);
                }
            }
        }
        return false;
    }


    private static boolean containsELScript(String text) {
        return text.matches(".*(\\{|\\$).*");
    }

    private static boolean isForestOfConfigurationClass(PsiClass clazz) {
        return "com.dtflys.forest.Forest".equals(clazz.getQualifiedName()) ||
                "com.dtflys.forest.config.ForestConfiguration".equals(clazz.getQualifiedName());
    }

    private static boolean isForestRequestClass(PsiClass clazz) {
        return "com.dtflys.forest.http.ForestRequest".equals(clazz.getQualifiedName());
    }


    private static boolean isForestInjectionMethods(PsiMethod method) {
        return FOREST_METHODS.contains(method.getName());
    }

    private static boolean isForestRequestInjectionMethods(PsiMethod method) {
        return FOREST_REQUEST_METHODS.contains(method.getName());
    }

}
