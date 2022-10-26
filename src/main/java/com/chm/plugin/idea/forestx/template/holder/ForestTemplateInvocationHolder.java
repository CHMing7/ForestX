package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForestTemplateInvocationHolder extends ForestTemplatePathElementHolder<PsiMethod> {

    public final static LookupElementRenderer<LookupElement> INVOCATION_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplateInvocationHolder invocationHolder = (ForestTemplateInvocationHolder) element.getObject();
            final PsiMethod psiMethod = invocationHolder.getMethod();
            final StringBuilder paramText = new StringBuilder("(");
            final PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                final PsiParameter parameter = parameters[i];
                paramText.append(parameter.getType().getPresentableText())
                        .append(" ")
                        .append(parameter.getName());
            }
            paramText.append(")");
            presentation.setIcon(PlatformIcons.METHOD_ICON);
            presentation.setItemText(psiMethod.getName());
            presentation.setTailText(paramText.toString());
        }
    };

    public final static InsertHandler<LookupElement> INVOCATION_INSERT_HANDLER = (context, item) -> {
        final int offset = context.getTailOffset();
        context.getDocument().insertString(offset, "()");
        final ForestTemplateInvocationHolder invocationHolder = (ForestTemplateInvocationHolder) item.getObject();
        final PsiMethod psiMethod = invocationHolder.getMethod();
        final PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        int toOffset = offset + 2;
        if (parameters.length > 0) {
            toOffset = offset + 1;
        }
        context.getEditor().getCaretModel().moveToOffset(toOffset);
    };


    private final PsiMethod method;
    private final List<PsiElement> arguments;

    public ForestTemplateInvocationHolder(ForestTemplatePathElementHolder prevHolder, String insertion, PsiMethod method, PsiType type, List<PsiElement> arguments) {
        super(prevHolder, insertion, method, type, false);
        this.method = method;
        this.arguments = arguments;
    }

    public ForestTemplateInvocationHolder(String insertion, PsiMethod method, PsiType type, List<PsiElement> arguments) {
        this(null, insertion, method, type, arguments);
    }

    public PsiMethod getMethod() {
        return method;
    }

    public List<PsiElement> getArguments() {
        return arguments;
    }
}
