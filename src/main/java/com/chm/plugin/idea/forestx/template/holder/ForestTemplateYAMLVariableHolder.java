package com.chm.plugin.idea.forestx.template.holder;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
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
            presentation.setItemText(yamlVariable.getVarName());
            if (elem instanceof YAMLKeyValue) {
                final YAMLKeyValue yamlKeyValue = (YAMLKeyValue) elem;
                value = yamlKeyValue.getValueText();
            } else if (elem instanceof YAMLSequenceItem) {
                final YAMLSequenceItem item = (YAMLSequenceItem) elem;
                value = getSequenceItemText(item);
            }
            if (yamlVariable.isEl()) {
                presentation.setTypeText("String");
            }
            presentation.setTailText("=" + value);
        }
    };


    public ForestTemplateYAMLVariableHolder(String insertion, YAMLPsiElement element, PsiType type, boolean el) {
        super(null, insertion, element, type, el);
    }

    private static @NotNull String getSequenceItemText(final YAMLSequenceItem item) {
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
                        keyValue.getKeyText() + ": " + keyValue.getValueText(), ", ") +
                suffix;
        return result;
    }

    public boolean isMapping() {
        PsiElement[] children = element.getChildren();
        return children.length > 0 && children[0] instanceof YAMLMapping;
    }

    @Override
    public String getVarName() {
        if (el) {
            return insertion;
        }
        return YAMLUtil.getConfigFullName(element);
    }

}
