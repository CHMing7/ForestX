package com.chm.plugin.idea.forestx.utils

import com.chm.plugin.idea.forestx.Icons
import com.chm.plugin.idea.forestx.annotation.Annotation
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.util.ui.tree.TreeUtil
import org.apache.commons.compress.utils.Lists
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreeNode

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
private val METHOD_ICON_MAP: MutableMap<String, Icon> = mutableMapOf(
    Annotation.GET.qualifiedName to Icons.GET,
    Annotation.GET_REQUEST.qualifiedName to Icons.GET,
    Annotation.POST.qualifiedName to Icons.POST,
    Annotation.POST_REQUEST.qualifiedName to Icons.POST,
    Annotation.PUT.qualifiedName to Icons.PUT,
    Annotation.PUT_REQUEST.qualifiedName to Icons.PUT,
    Annotation.PATCH.qualifiedName to Icons.PATCH,
    Annotation.PATCH_REQUEST.qualifiedName to Icons.PATCH,
    Annotation.HEAD_REQUEST.qualifiedName to Icons.HEAD,
    Annotation.OPTIONS.qualifiedName to Icons.OPTIONS,
    Annotation.OPTIONS_REQUEST.qualifiedName to Icons.OPTIONS,
    Annotation.DELETE.qualifiedName to Icons.DELETE,
    Annotation.DELETE_REQUEST.qualifiedName to Icons.DELETE,
    Annotation.TRACE.qualifiedName to Icons.TRACE,
    Annotation.TRACE_REQUEST.qualifiedName to Icons.TRACE
)

private val REQUEST_TYPE_ICON_MAP: MutableMap<String, Icon> = mutableMapOf(
    "GET" to Icons.GET,
    "POST" to Icons.POST,
    "PUT" to Icons.PUT,
    "HEAD" to Icons.HEAD,
    "OPTIONS" to Icons.OPTIONS,
    "DELETE" to Icons.DELETE,
    "PATCH" to Icons.PATCH,
    "TRACE" to Icons.TRACE
)

val NAME_SORTED: Comparator<Any> = Comparator { o1: Any, o2: Any ->
    val name1 = o1.getNodeName()
    val name2 = o2.getNodeName()
    name1.compareTo(name2)
}


fun TreeNode.findNode(o: Any): DefaultMutableTreeNode? {
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i)
        val childName = child.getNodeName()
        val oName = o.getNodeName()
        if (childName == oName) {
            return child as DefaultMutableTreeNode
        }
    }
    return null
}

fun DefaultMutableTreeNode.findNodeOrNew(
    model: DefaultTreeModel?,
    o: Any
): DefaultMutableTreeNode {
    return this.findNodeOrNew(model, o, false)
}

fun DefaultMutableTreeNode.findNodeOrNew(
    model: DefaultTreeModel?,
    o: Any,
    sorted: Boolean
): DefaultMutableTreeNode {
    val node = this.findNode(o)
    if (node != null) {
        return node
    }
    val newNode = DefaultMutableTreeNode(o)
    if (sorted) {
        TreeUtil.insertNode(newNode, this, model, NAME_SORTED)
    } else {
        val insertionPoint = if (this.childCount >= 0) this.childCount else -(this.childCount + 1)
        if (model != null) {
            model.insertNodeInto(newNode, this, insertionPoint)
        } else {
            this.insert(newNode, insertionPoint)
        }
    }
    return newNode
}

fun DefaultMutableTreeNode.findNodeOrNew(
    model: DefaultTreeModel?,
    o: Any,
    index: Int
): DefaultMutableTreeNode {
    val node = this.findNode(o)
    return node ?: this.insertNode(model, o, index)
}

fun DefaultMutableTreeNode.moveOrInsert(
    model: DefaultTreeModel?,
    o: Any,
    index: Int
): DefaultMutableTreeNode {
    val node = this.findNode(o)
    if (node != null && this.childCount < index) {
        val child = this.getChildAt(index)
        if (child === node) {
            return node
        }
        this.removeNode(model, node)
        this.insertNode(model, node, index)
        return node
    }
    return this.insertNode(model, o, index)
}

fun DefaultMutableTreeNode.insertNode(
    model: DefaultTreeModel?,
    o: Any,
    index: Int
): DefaultMutableTreeNode {
    val newNode = DefaultMutableTreeNode(o)
    val insertionPoint = if (index >= 0) index else -(index + 1)
    if (model != null) {
        model.insertNodeInto(newNode, this, insertionPoint)
    } else {
        this.insert(newNode, insertionPoint)
    }
    return newNode
}

fun DefaultMutableTreeNode.insertNode(
    model: DefaultTreeModel?,
    child: DefaultMutableTreeNode,
    index: Int
) {
    val insertionPoint = if (index >= 0) index else -(index + 1)
    if (model != null) {
        model.insertNodeInto(child, this, insertionPoint)
    } else {
        this.insert(child, insertionPoint)
    }
}

fun DefaultMutableTreeNode.addNode(
    model: DefaultTreeModel?,
    o: Any
): DefaultMutableTreeNode {
    val newNode = DefaultMutableTreeNode(o)
    val insertionPoint = if (this.childCount >= 0) this.childCount else -(this.childCount + 1)
    if (model != null) {
        model.insertNodeInto(newNode, this, insertionPoint)
    } else {
        this.insert(newNode, insertionPoint)
    }
    return newNode
}

fun DefaultMutableTreeNode.getAllChildren(): List<MutableTreeNode> {
    val childrenList: MutableList<MutableTreeNode> = Lists.newArrayList()
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i) as MutableTreeNode
        childrenList.add(child)
    }
    return childrenList
}


fun DefaultMutableTreeNode.removeAllChildren(
    model: DefaultTreeModel?,
) {
    for (i in this.childCount - 1 downTo 0) {
        val child = this.getChildAt(i) as MutableTreeNode
        this.removeNode(model, child)
    }
}

fun DefaultMutableTreeNode.removeChildrenList(
    model: DefaultTreeModel?,
    childrenList: List<MutableTreeNode>
) {
    for (children in childrenList) {
        if (model != null) {
            model.removeNodeFromParent(children)
        } else {
            this.remove(children)
        }
    }
}

fun DefaultMutableTreeNode.removeNode(
    model: DefaultTreeModel?,
    child: MutableTreeNode?
) {
    if (model != null) {
        model.removeNodeFromParent(child)
    } else {
        this.remove(child)
    }
}

fun Any.getNodeName(): String {
    val o: Any?
    o = if (this is DefaultMutableTreeNode) {
        this.userObject
    } else {
        this
    }
    return when (o) {
        is Project -> {
            o.name
        }
        is Module -> {
            o.name
        }
        is PsiClass -> {
            o.qualifiedName ?: ""
        }
        is PsiMethod -> {
            o.name
        }
        else -> {
            o?.toString() ?: "null"
        }
    }
}

fun Any.getNodeIcon(): Icon? {
    val o: Any
    o = if (this is DefaultMutableTreeNode) {
        this.userObject
    } else {
        this
    }
    return when (o) {
        is Project -> {
            Icons.PROJECT_16
        }
        is Module -> {
            Icons.MODULE_16
        }
        is PsiClass -> {
            Icons.INTERFACE_18
        }
        is PsiMethod -> {
            val methodAnnotations = AnnotationUtil.getAllAnnotations(
                o, false, null
            )
            for (methodAnnotation in methodAnnotations) {
                val icon = METHOD_ICON_MAP[methodAnnotation.qualifiedName]
                if (icon != null) {
                    return icon
                }
                if (methodAnnotation.qualifiedName == Annotation.REQUEST.qualifiedName) {
                    val type = AnnotationUtil.getStringAttributeValue(methodAnnotation, "type")
                    val icon2 = REQUEST_TYPE_ICON_MAP[StringUtil.toUpperCase(type)]
                    return icon2 ?: Icons.GET
                }
            }
            Icons.GET
        }
        else -> {
            Icons.ICON_16
        }
    }
}