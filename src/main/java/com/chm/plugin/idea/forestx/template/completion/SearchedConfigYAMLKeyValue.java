package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.utils.SearchedConfigItem;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SearchedConfigYAMLKeyValue extends SearchedConfigItem<YAMLPsiElement> {

    public final static LookupElementRenderer<LookupElement> YAML_KEY_VALUE_CONFIG_RENDER = new LookupElementRenderer<>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final SearchedConfigYAMLKeyValue searchedYAMLKeyValue = (SearchedConfigYAMLKeyValue) element.getObject();
            final YAMLPsiElement elem = searchedYAMLKeyValue.getElement();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            String value = null;
            presentation.setItemText(searchedYAMLKeyValue.getItemText());
            if (elem instanceof YAMLKeyValue) {
                YAMLKeyValue yamlKeyValue = (YAMLKeyValue) elem;
                value = yamlKeyValue.getValueText();
            } else if (elem instanceof YAMLSequenceItem) {
                YAMLSequenceItem item = (YAMLSequenceItem) elem;
                value = getSequenceItemText(item);
            }
            if (searchedYAMLKeyValue.isEL()) {
                presentation.setTypeText("String");
            }
            presentation.setTailText("=" + value);
        }
    };


    public SearchedConfigYAMLKeyValue(String insertion, YAMLPsiElement element, boolean isEL) {
        super(insertion, element, isEL);
    }

    private static @NotNull String getSequenceItemText(YAMLSequenceItem item) {
        final List<YAMLKeyValue> keysValues = new SmartList();
        int count = 0;
        String suffix = "";
        final Iterator<YAMLKeyValue> iterator = item.getKeysValues().iterator();

        for (YAMLKeyValue keysValue : item.getKeysValues()) {
            keysValues.add(keysValue);
            if (count++ == 2) {
                if (iterator.hasNext()) {
                    suffix = ", [...]";
                }
                break;
            }
        }

        if (keysValues.size() == 0) {
            return Objects.requireNonNull(item.getValue()).getText();
        }

        String result = StringUtil.join(
                keysValues, (keyValue) ->
                        keyValue.getKeyText() + ": " + keyValue.getValueText(), ", ") +
                suffix;
        return result;
    }

    @Override
    public String getItemText() {
        if (isEL) {
            return insertion;
        }
        return YAMLUtil.getConfigFullName(element);
    }
}
