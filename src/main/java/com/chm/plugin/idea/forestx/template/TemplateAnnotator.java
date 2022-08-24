package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiNameValuePair;
import org.jetbrains.annotations.NotNull;

public class TemplateAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // 仅对Java文件处理
        if (!(element.getContainingFile() instanceof PsiJavaFile)) {
            return;
        }

        if (element instanceof PsiLiteralExpression) {
            PsiLiteralExpression expression = (PsiLiteralExpression) element;
            // 忽略空字符串
            String value =  expression.getValue() instanceof String ? (String) expression.getValue() : null;
            if (value == null || value.length() == 0) {
                return;
            }
            PsiElement parent = expression.getParent();
            // 仅对Forest注解中的字符串进行处理
            if (expression.getParent() instanceof PsiNameValuePair) {
                PsiNameValuePair nameValuePair = (PsiNameValuePair) parent;
                parent = nameValuePair.getParent();
                if (parent instanceof PsiAnnotationParameterList) {
                    PsiAnnotationParameterList annotationParameterList = (PsiAnnotationParameterList) parent;
                    parent = annotationParameterList.getParent();
                    if (parent instanceof PsiAnnotation) {
                        PsiAnnotation annotation = (PsiAnnotation) parent;
                        if (!Annotation.isForestAnnotation(annotation.getQualifiedName())) {
                            return;
                        }
                    }
                }
            } else {
                return;
            }
            int propBeginIdx = value.indexOf("#{");
            int propEndIdx = value.indexOf("}");
            TextRange propRange = null;
            if (propBeginIdx >= 0 && propEndIdx > propBeginIdx) {
                propRange = new TextRange(element.getTextRange().getStartOffset() + propBeginIdx,
                        element.getTextRange().getStartOffset() + propEndIdx + 2);
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(propRange)
                        .textAttributes(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
                        .create();
            } else {
                propRange = new TextRange(element.getTextRange().getStartOffset() + propBeginIdx,
                        element.getTextRange().getEndOffset() - 1);
                holder.newAnnotation(HighlightSeverity.ERROR, "Illegal property block end")
                        .range(propRange)
                        .highlightType(ProblemHighlightType.ERROR)
                        .create();
            }

            String propName = value.substring(propBeginIdx + 2, propEndIdx);
            System.out.println("Annotator处理: " + expression.getText() + " => 属性: " + propName);

        }
    }
}
