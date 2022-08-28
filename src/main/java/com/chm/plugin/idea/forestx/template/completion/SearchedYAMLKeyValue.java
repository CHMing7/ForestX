package com.chm.plugin.idea.forestx.template.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.lang.properties.PropertiesHighlighter;
import com.intellij.microservices.config.yaml.ConfigYamlAccessor;
import com.intellij.microservices.config.yaml.ConfigYamlUtils;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.spring.boot.application.metadata.SpringBootApplicationMetaConfigKeyManager;
import com.intellij.util.PlatformIcons;
import com.intellij.util.SmartList;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLBlockSequenceImpl;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SearchedYAMLKeyValue {

    public final static LookupElementRenderer<LookupElement> YAML_KEY_VALUE_RENDER = new LookupElementRenderer<LookupElement>() {
        @Override
        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
            SearchedYAMLKeyValue searchedYAMLKeyValue = (SearchedYAMLKeyValue) element.getObject();
            YAMLPsiElement elem = searchedYAMLKeyValue.getElement();
            presentation.setIcon(PlatformIcons.PROPERTY_ICON);
            String value = null;
            TextAttributes attrs = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(PropertiesHighlighter.PROPERTY_VALUE);
            presentation.setItemText(YAMLUtil.getConfigFullName(elem));
            if (elem instanceof YAMLKeyValue) {
                YAMLKeyValue yamlKeyValue = (YAMLKeyValue) elem;
                value = ConfigYamlUtils.getValuePresentationText(yamlKeyValue);
            } else if (elem instanceof YAMLSequenceItem) {
                YAMLSequenceItem item = (YAMLSequenceItem) elem;
                value = getSequenceItemText(item);
            }
            presentation.setTailText("=" + value, attrs.getForegroundColor());
        }
    };


    private static @NotNull String getSequenceItemText(YAMLSequenceItem item) {
        List<YAMLKeyValue> keysValues = new SmartList();
        int count = 0;
        String suffix = "";
        Iterator<YAMLKeyValue> iterator = item.getKeysValues().iterator();

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
                keyValue.getKeyText() + ": " + ConfigYamlUtils.getValuePresentationText(keyValue), ", ") +
                suffix;
        return result;
    }


    private final String insertion;

    private final YAMLPsiElement element;

    public SearchedYAMLKeyValue(String insertion, YAMLPsiElement element) {
        this.insertion = insertion;
        this.element = element;
    }

    public String getInsertion() {
        return insertion;
    }

    public YAMLPsiElement getElement() {
        return element;
    }

    @Override
    public String toString() {
        return insertion;
    }

    public static List<SearchedYAMLKeyValue> searchRelatedKeyValues(YAMLDocument document, String prefix) {
        List<SearchedYAMLKeyValue> results = new ArrayList<>();
        ConfigYamlAccessor accessor = new ConfigYamlAccessor(
                document, SpringBootApplicationMetaConfigKeyManager.getInstance());
        List<YAMLKeyValue> rootKeyValues = accessor.getAllKeys();
        for (YAMLKeyValue rootKeyValue : rootKeyValues) {
            YAMLValue rootValue = rootKeyValue.getValue();
            if (rootValue == null) {
                continue;
            }
            if (rootValue instanceof YAMLPlainTextImpl) {
                String keyName = YAMLUtil.getConfigFullName(rootKeyValue);
                String insertion = keyName;
                if (StringUtils.isNotEmpty(prefix)) {
                    if (keyName.startsWith(prefix)) {
                        insertion = keyName.substring(prefix.length() + 1);
                    } else {
                        continue;
                    }
                }
                results.add(new SearchedYAMLKeyValue(insertion, rootKeyValue));
            } else if (rootValue instanceof YAMLBlockSequenceImpl) {
                YAMLBlockSequenceImpl sequenceValue = (YAMLBlockSequenceImpl) rootValue;
                System.out.println(sequenceValue.getTextValue());
                int i = 0;
                for (YAMLSequenceItem item : sequenceValue.getItems()) {
                    String keyName = YAMLUtil.getConfigFullName(item);
                    String insertion = keyName;
                    if (StringUtils.isNotEmpty(prefix)) {
                        if (keyName.startsWith(prefix)) {
                            insertion = keyName.substring(prefix.length() + 1);
                        } else {
                            continue;
                        }
                    }
                    results.add(new SearchedYAMLKeyValue(insertion, item));
                }
            }
        }
        return results;
    }


}
