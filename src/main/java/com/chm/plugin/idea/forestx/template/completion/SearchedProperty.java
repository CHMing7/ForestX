package com.chm.plugin.idea.forestx.template.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.lang.properties.IProperty;
import com.intellij.util.PlatformIcons;

public class SearchedProperty {

    public final static LookupElementRenderer<LookupElement> PROPERTY_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            SearchedProperty searchedProp = (SearchedProperty) element.getObject();
            IProperty prop = searchedProp.getProperty();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            presentation.setItemText(prop.getKey());
            presentation.setTailText("=" + prop.getValue());
        }
    };

    private final String insertion;

    private final IProperty property;

    public SearchedProperty(String insertion, IProperty property) {
        this.insertion = insertion;
        this.property = property;
    }

    public String getInsertion() {
        return insertion;
    }

    public IProperty getProperty() {
        return property;
    }

    @Override
    public String toString() {
        return insertion;
    }
}
