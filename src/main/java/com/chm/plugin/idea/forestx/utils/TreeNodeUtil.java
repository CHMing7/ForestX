package com.chm.plugin.idea.forestx.utils;

import com.chm.plugin.idea.forestx.Icons;
import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.ui.tree.TreeUtil;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-02
 **/
public class TreeNodeUtil {

    private static final Map<String, Icon> METHOD_ICON_MAP = Maps.newConcurrentMap();

    private static final Map<String, Icon> REQUEST_TYPE_ICON_MAP = Maps.newConcurrentMap();

    public static final Comparator NAME_SORTED = (o1, o2) -> {
        String name1 = TreeNodeUtil.getNodeName(o1);
        String name2 = TreeNodeUtil.getNodeName(o2);
        return name1.compareTo(name2);
    };

    static {
        METHOD_ICON_MAP.put(Annotation.GET.getQualifiedName(), Icons.GET);
        METHOD_ICON_MAP.put(Annotation.GET_REQUEST.getQualifiedName(), Icons.GET);
        METHOD_ICON_MAP.put(Annotation.POST.getQualifiedName(), Icons.POST);
        METHOD_ICON_MAP.put(Annotation.POST_REQUEST.getQualifiedName(), Icons.POST);
        METHOD_ICON_MAP.put(Annotation.PUT.getQualifiedName(), Icons.PUT);
        METHOD_ICON_MAP.put(Annotation.PUT_REQUEST.getQualifiedName(), Icons.PUT);
        METHOD_ICON_MAP.put(Annotation.PATCH.getQualifiedName(), Icons.PATCH);
        METHOD_ICON_MAP.put(Annotation.PATCH_REQUEST.getQualifiedName(), Icons.PATCH);
        METHOD_ICON_MAP.put(Annotation.HEAD_REQUEST.getQualifiedName(), Icons.HEAD);
        METHOD_ICON_MAP.put(Annotation.OPTIONS.getQualifiedName(), Icons.OPTIONS);
        METHOD_ICON_MAP.put(Annotation.OPTIONS_REQUEST.getQualifiedName(), Icons.OPTIONS);
        METHOD_ICON_MAP.put(Annotation.DELETE.getQualifiedName(), Icons.DELETE);
        METHOD_ICON_MAP.put(Annotation.DELETE_REQUEST.getQualifiedName(), Icons.DELETE);
        METHOD_ICON_MAP.put(Annotation.TRACE.getQualifiedName(), Icons.TRACE);
        METHOD_ICON_MAP.put(Annotation.TRACE_REQUEST.getQualifiedName(), Icons.TRACE);

        REQUEST_TYPE_ICON_MAP.put("GET", Icons.GET);
        REQUEST_TYPE_ICON_MAP.put("POST", Icons.POST);
        REQUEST_TYPE_ICON_MAP.put("PUT", Icons.PUT);
        REQUEST_TYPE_ICON_MAP.put("HEAD", Icons.HEAD);
        REQUEST_TYPE_ICON_MAP.put("OPTIONS", Icons.OPTIONS);
        REQUEST_TYPE_ICON_MAP.put("DELETE", Icons.DELETE);
        REQUEST_TYPE_ICON_MAP.put("PATCH", Icons.PATCH);
        REQUEST_TYPE_ICON_MAP.put("TRACE", Icons.TRACE);
    }

    public static DefaultMutableTreeNode findNode(TreeNode parentNode, Object o) {
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            TreeNode child = parentNode.getChildAt(i);
            String childName = getNodeName(child);
            String oName = getNodeName(o);
            if (Objects.equals(childName, oName)) {
                return (DefaultMutableTreeNode) child;
            }
        }
        return null;
    }

    public static DefaultMutableTreeNode findNodeOrNew(DefaultTreeModel model,
                                                       DefaultMutableTreeNode parentNode,
                                                       Object o) {
        return findNodeOrNew(model, parentNode, o, false);
    }

    public static DefaultMutableTreeNode findNodeOrNew(DefaultTreeModel model,
                                                       DefaultMutableTreeNode parent,
                                                       Object o,
                                                       boolean sorted) {
        DefaultMutableTreeNode node = findNode(parent, o);
        if (node != null) {
            return node;
        }
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(o);
        if (sorted) {
            TreeUtil.insertNode(newNode, parent, model, NAME_SORTED);
        } else {
            int insertionPoint = parent.getChildCount() >= 0 ? parent.getChildCount() : -(parent.getChildCount() + 1);
            if (model != null) {
                model.insertNodeInto(newNode, parent, insertionPoint);
            } else {
                parent.insert(newNode, insertionPoint);
            }
        }
        return newNode;
    }

    public static DefaultMutableTreeNode findNodeOrNew(DefaultTreeModel model,
                                                       DefaultMutableTreeNode parent,
                                                       Object o,
                                                       int index) {
        DefaultMutableTreeNode node = findNode(parent, o);
        if (node != null) {
            return node;
        }
        return insertNode(model, parent, o, index);
    }

    public static DefaultMutableTreeNode moveOrInsert(DefaultTreeModel model,
                                                      DefaultMutableTreeNode parent,
                                                      Object o,
                                                      int index) {
        DefaultMutableTreeNode node = findNode(parent, o);
        if (node != null && parent.getChildCount() < index) {
            TreeNode child = parent.getChildAt(index);
            if (child == node) {
                return node;
            }
            removeNode(model, parent, node);
            insertNode(model, parent, node, index);
            return node;
        }
        return insertNode(model, parent, o, index);
    }

    public static DefaultMutableTreeNode insertNode(DefaultTreeModel model,
                                                    DefaultMutableTreeNode parent,
                                                    Object o,
                                                    int index) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(o);
        int insertionPoint = index >= 0 ? index : -(index + 1);
        if (model != null) {
            model.insertNodeInto(newNode, parent, insertionPoint);
        } else {
            parent.insert(newNode, insertionPoint);
        }
        return newNode;
    }

    public static void insertNode(DefaultTreeModel model,
                                  DefaultMutableTreeNode parent,
                                  DefaultMutableTreeNode child,
                                  int index) {
        int insertionPoint = index >= 0 ? index : -(index + 1);
        if (model != null) {
            model.insertNodeInto(child, parent, insertionPoint);
        } else {
            parent.insert(child, insertionPoint);
        }
    }

    public static DefaultMutableTreeNode addNode(DefaultTreeModel model,
                                                 DefaultMutableTreeNode parent,
                                                 Object o) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(o);
        int insertionPoint = parent.getChildCount() >= 0 ? parent.getChildCount() : -(parent.getChildCount() + 1);
        if (model != null) {
            model.insertNodeInto(newNode, parent, insertionPoint);
        } else {
            parent.insert(newNode, insertionPoint);
        }
        return newNode;
    }

    public static List<MutableTreeNode> getAllChildren(DefaultMutableTreeNode parent) {
        List<MutableTreeNode> childrenList = Lists.newArrayList();
        for (int i = 0; i < parent.getChildCount(); i++) {
            MutableTreeNode child = (MutableTreeNode) parent.getChildAt(i);
            childrenList.add(child);
        }
        return childrenList;
    }


    public static void removeAllChildren(DefaultTreeModel model,
                                         DefaultMutableTreeNode parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            MutableTreeNode child = (MutableTreeNode) parent.getChildAt(i);
            removeNode(model, parent, child);
        }

    }

    public static void removeChildrenList(DefaultTreeModel model,
                                          DefaultMutableTreeNode parent,
                                          List<MutableTreeNode> childrenList) {
        for (MutableTreeNode children : childrenList) {
            if (model != null) {
                model.removeNodeFromParent(children);
            } else {
                parent.remove(children);
            }
        }

    }

    public static void removeNode(DefaultTreeModel model,
                                  DefaultMutableTreeNode parent,
                                  MutableTreeNode child) {
        if (model != null) {
            model.removeNodeFromParent(child);
        } else {
            parent.remove(child);
        }
    }

    public static String getNodeName(Object node) {
        Object o;
        if (node instanceof DefaultMutableTreeNode) {
            o = ((DefaultMutableTreeNode) node).getUserObject();
        } else {
            o = node;
        }
        if (o instanceof Project) {
            return ((Project) o).getName();
        } else if (o instanceof Module) {
            return ((Module) o).getName();
        } else if (o instanceof PsiClass) {
            return ((PsiClass) o).getQualifiedName();
        } else if (o instanceof PsiMethod) {
            return ((PsiMethod) o).getName();
        } else {
            return o != null ? o.toString() : "null";
        }
    }

    public static Icon getNodeIcon(Object node) {
        Object o;
        if (node instanceof DefaultMutableTreeNode) {
            o = ((DefaultMutableTreeNode) node).getUserObject();
        } else {
            o = node;
        }
        if (o instanceof Project) {
            return Icons.PROJECT_16;
        } else if (o instanceof Module) {
            return Icons.MODULE_16;
        } else if (o instanceof PsiClass) {
            return Icons.INTERFACE_18;
        } else if (o instanceof PsiMethod) {
            PsiAnnotation[] methodAnnotations = AnnotationUtil.getAllAnnotations((PsiMethod) o, false, null);
            for (PsiAnnotation methodAnnotation : methodAnnotations) {
                Icon icon = METHOD_ICON_MAP.get(methodAnnotation.getQualifiedName());
                if (icon != null) {
                    return icon;
                }

                if (Objects.equals(methodAnnotation.getQualifiedName(), Annotation.REQUEST.getQualifiedName())) {
                    String type = AnnotationUtil.getStringAttributeValue(methodAnnotation, "type");
                    Icon icon2 = REQUEST_TYPE_ICON_MAP.get(StringUtil.toUpperCase(type));
                    if (icon2 != null) {
                        return icon2;
                    }
                    return Icons.GET;
                }
            }
            return Icons.GET;
        } else {
            return Icons.ICON_16;
        }
    }
}
