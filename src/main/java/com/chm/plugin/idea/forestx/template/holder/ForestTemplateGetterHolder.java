package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class ForestTemplateGetterHolder extends ForestTemplateInvocationHolder {

    public final static LookupElementRenderer<LookupElement> GETTER_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplateGetterHolder getterHolder = (ForestTemplateGetterHolder) element.getObject();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            presentation.setItemText(getterHolder.insertion);
        }
    };


    public ForestTemplateGetterHolder(String varName, PsiMethod method, PsiType type) {
        super(varName, method, type, Lists.newArrayList());
    }

}
