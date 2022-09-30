package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.lang.properties.IProperty;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;

public class ForestTemplatePropertyVariableHolder extends ForestTemplateVariableHolder<IProperty> {

    public final static LookupElementRenderer<LookupElement> PROPERTY_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplatePropertyVariableHolder searchedProp = (ForestTemplatePropertyVariableHolder) element.getObject();
            final IProperty prop = searchedProp.getElement();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            presentation.setItemText(searchedProp.getVarName());
            presentation.setTailText("=" + prop.getValue());
        }
    };


    public ForestTemplatePropertyVariableHolder(String insertion, IProperty element, PsiType type, boolean el) {
        super(insertion, element, type, el);
    }

    @Override
    public String getVarName() {
        if (el) {
            return insertion;
        }
        return element.getKey();
    }

}
