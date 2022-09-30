package com.chm.plugin.idea.forestx.template.holder;

import com.chm.plugin.idea.forestx.template.completion.SearchedConfigYAMLKeyValue;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.lang.properties.PropertiesHighlighter;
import com.intellij.microservices.config.yaml.ConfigYamlUtils;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ForestTemplateYAMLVariableHolder extends ForestTemplateVariableHolder<YAMLPsiElement> {

    public final static LookupElementRenderer<LookupElement> YAML_KEY_VALUE_CONFIG_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            final ForestTemplateYAMLVariableHolder yamlVariable = (ForestTemplateYAMLVariableHolder) element.getObject();
            final YAMLPsiElement elem = yamlVariable.getElement();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            String value = null;
            final TextAttributes attrs = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(PropertiesHighlighter.PROPERTY_VALUE);
            presentation.setItemText(yamlVariable.getVarName());
            if (elem instanceof YAMLKeyValue) {
                final YAMLKeyValue yamlKeyValue = (YAMLKeyValue) elem;
                value = ConfigYamlUtils.getValuePresentationText(yamlKeyValue);
            } else if (elem instanceof YAMLSequenceItem) {
                final YAMLSequenceItem item = (YAMLSequenceItem) elem;
                value = getSequenceItemText(item);
            }
            if (yamlVariable.isEl()) {
                presentation.setTypeText("String");
            }
            presentation.setTailText("=" + value, attrs.getForegroundColor());
        }
    };


    private static @NotNull String getSequenceItemText(YAMLSequenceItem item) {
        final List<YAMLKeyValue> keysValues = new ArrayList<>();
        int count = 0;
        String suffix = "";
        final Iterator<YAMLKeyValue> iterator = item.getKeysValues().iterator();

        for (final YAMLKeyValue keysValue : item.getKeysValues()) {
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

        final String result = StringUtil.join(
                keysValues, (keyValue) ->
                        keyValue.getKeyText() + ": " + ConfigYamlUtils.getValuePresentationText(keyValue), ", ") +
                suffix;
        return result;
    }


    public ForestTemplateYAMLVariableHolder(String insertion, YAMLPsiElement element, PsiType type, boolean el) {
        super(insertion, element, type, el);
    }

    @Override
    public String getVarName() {
        if (el) {
            return insertion;
        }
        return YAMLUtil.getConfigFullName(element);
    }

}
