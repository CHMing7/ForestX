package com.chm.plugin.idea.forestx.template.utils;

import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyArrayIndex;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyArrayReference;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyExpress;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePropertyPart;
import com.chm.plugin.idea.forestx.template.psi.TemplateTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;

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
                    PsiElement prevSibling = shiftLeftNode(node);
                    if (prevSibling == null) {
                        break;
                    }
                    if (prevSibling instanceof ForestTemplatePropertyPart) {
                        StringBuilder name = new StringBuilder();
                        int i = 0;
                        for (PsiElement child = prevSibling.getFirstChild(); child != null;  child = child.getNextSibling(), i++) {
                            if (child instanceof LeafPsiElement) {
                                LeafPsiElement leafChild = (LeafPsiElement) child;
                                if (i == 0) {
                                    node = leafChild;
                                }
                                if (isPropertyLeafElement(node.getElementType())) {
                                    name.append(child.getText());
                                }
                            } else if (child instanceof ForestTemplatePropertyArrayReference) {
                                for (PsiElement arrayRefChild = child.getFirstChild();
                                     arrayRefChild != null;
                                     arrayRefChild = arrayRefChild.getNextSibling()) {
                                    if (arrayRefChild instanceof LeafPsiElement) {
                                        name.append(arrayRefChild.getText());
                                    } else if (arrayRefChild instanceof ForestTemplatePropertyArrayIndex) {
                                        name.append(arrayRefChild.getFirstChild().getText());
                                    }
                                }
                            }
                        }
                        if (name.length() > 0) {
                            propKeyStack.push(name.toString());
                        }
                    } else if (prevSibling instanceof LeafPsiElement) {
                        node = (LeafPsiElement) prevSibling;
                        if (isPropertyLeafElement(node.getElementType())) {
                            propKeyStack.push(node.getText());
                        }
                    }
                } while (isPropertyLeafElement(node.getElementType()));
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

    public static boolean isPropertyLeafElement(IElementType nodeType) {
        return nodeType.equals(TemplateTypes.PROP_NAME_PART) ||
                nodeType.equals(TemplateTypes.PROP_DOT) ||
                nodeType.equals(TemplateTypes.PROP_LBRACE) ||
                nodeType.equals(TemplateTypes.PROP_RBRACE) ||
                nodeType.equals(TemplateTypes.PROP_INT);
    }

    public static PsiElement shiftLeftNode(LeafPsiElement node) {
        if (node.getElementType().equals(TemplateTypes.PROP_NAME_PART)) {
            PsiElement parent = node.getParent();
            if (!(parent instanceof ForestTemplatePropertyPart)) {
                PsiElement prev = node.getPrevSibling();
                if (prev != null) {
                    return prev;
                }
                return null;
            }
            return parent.getPrevSibling();
        }
        return node.getPrevSibling();
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
