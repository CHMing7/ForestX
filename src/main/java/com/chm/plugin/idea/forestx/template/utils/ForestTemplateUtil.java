package com.chm.plugin.idea.forestx.template.utils;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyExpress;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

import java.util.Stack;

public class ForestTemplateUtil {

    public static String getPropertyKey(ForestTemplatePropertyExpress express) {
        return express.getText();
    }

    public static String getPropertyPrefix(PsiElement element, boolean containFirst) {
        if (element instanceof LeafPsiElement) {
            LeafPsiElement node = (LeafPsiElement) element;
            if (node.getElementType().equals(TemplateTypes.PROP_NAME_PART)) {
                Stack<String> propKeyStack = new Stack<>();
                StringBuilder propName = new StringBuilder();
                if (containFirst) {
                    propKeyStack.add(node.getText());
                }
                do {
                    PsiElement prevSibling = node.getPrevSibling();
                    if (prevSibling == null) {
                        break;
                    }
                    if (prevSibling instanceof LeafPsiElement) {
                        node = (LeafPsiElement) prevSibling;
                        if (node.getElementType().equals(TemplateTypes.PROP_NAME_PART)
                                || node.getElementType().equals(TemplateTypes.PROP_DOT)) {
                            propKeyStack.push(node.getText());
                        }
                    }
                } while (node.getElementType().equals(TemplateTypes.PROP_NAME_PART)
                        || node.getElementType().equals(TemplateTypes.PROP_DOT));
                for (int i = 0, len = propKeyStack.size(); i < len; i++) {
                    String propPart = propKeyStack.pop();
                    if (i < len - 1 || !".".equals(propPart)) {
                        propName.append(propPart);
                    }
                }
                return propName.toString();
            }
        }
        return null;
    }


    public static String getPropertyKeyFromChildElement(PsiElement element) {
        if (!(element instanceof LeafPsiElement)) {
            return null;
        }
        PsiElement parent = element.getParent();
        if (parent instanceof ForestTemplatePropertyExpress) {
            return getPropertyKey((ForestTemplatePropertyExpress) parent);
        }
        return null;
    }
}
