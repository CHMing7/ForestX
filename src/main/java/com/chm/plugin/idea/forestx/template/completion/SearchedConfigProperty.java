package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.utils.SearchedConfigItem;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.lang.properties.IProperty;
import com.intellij.util.PlatformIcons;

public class SearchedConfigProperty extends SearchedConfigItem<IProperty> {

    public final static LookupElementRenderer<LookupElement> PROPERTY_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final SearchedConfigProperty searchedProp = (SearchedConfigProperty) element.getObject();
            final IProperty prop = searchedProp.getElement();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            presentation.setItemText(searchedProp.getItemText());
            presentation.setTailText("=" + prop.getValue());
        }
    };


    public SearchedConfigProperty(String insertion, IProperty property, boolean isEL) {
        super(insertion, property, isEL);
    }

    @Override
    public String getItemText() {
        if (isEL) {
            return insertion;
        }
        return element.getKey();
    }
}
