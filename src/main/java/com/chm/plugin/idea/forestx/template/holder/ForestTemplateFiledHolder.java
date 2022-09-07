package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

public class ForestTemplateFiledHolder extends ForestTemplatePathElementHolder<PsiMethod> {

    public final static LookupElementRenderer<LookupElement> FIELD_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            ForestTemplateFiledHolder invocationHolder = (ForestTemplateFiledHolder) element.getObject();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            presentation.setItemText(invocationHolder.toString());
//            presentation.setTypeText(psiMethod.getReturnType().getPresentableText());
        }
    };

    private final PsiMethod getter;

    public ForestTemplateFiledHolder(String insertion, PsiMethod method, PsiType type) {
        super(insertion, method, type, false);
        this.getter = method;
    }
    public PsiMethod getGetter() {
        return getter;
    }

}
