package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

public class ForestTemplateFieldHolder extends ForestTemplatePathElementHolder<PsiMethod> {

    public final static LookupElementRenderer<LookupElement> FIELD_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplateFieldHolder invocationHolder = (ForestTemplateFieldHolder) element.getObject();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            presentation.setItemText(invocationHolder.toString());
        }
    };

    private final PsiMethod getter;

    public ForestTemplateFieldHolder(ForestTemplatePathElementHolder prevHolder, String insertion, PsiMethod method, PsiType type) {
        super(prevHolder, insertion, method, type, false);
        this.getter = method;
    }

    public ForestTemplateFieldHolder(String insertion, PsiMethod method, PsiType type) {
        this(null, insertion, method, type);
    }

    public static ForestTemplateFieldHolder getHolder(PsiMethod method, PsiType type) {
        final String methodName = method.getName();
        String getter = methodName.substring(3);
        getter = getter.substring(0, 1).toLowerCase() + getter.substring(1);
        return new ForestTemplateFieldHolder(getter, method, type);
    }

    public PsiMethod getGetter() {
        return getter;
    }

}
