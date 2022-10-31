package com.chm.plugin.idea.forestx.utils

import com.chm.plugin.idea.forestx.Icons
import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.spring.boot.library.SpringBootLibraryUtil
import com.intellij.util.ui.tree.TreeUtil
import org.apache.commons.compress.utils.Lists
import java.util.function.BiFunction
import javax.swing.Icon
import javax.swing.tree.*

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

fun getMethodFullName(method: PsiMethod): String {
    val builder = StringBuilder()
    builder.append(method.name).append('(')
    val paramTypes = method.typeParameterList!!.typeParameters
    val count = method.parameterList.parametersCount
    for (i in 0 until count) {
        val param = method.parameterList.getParameter(i)
        builder.append(param!!.type)
        if (i < paramTypes.size - 1) {
            builder.append(',')
        }
    }
    builder.append(')')
    return builder.toString()
}

fun resolveElement(project: Project, virtualFile: VirtualFile, element: PsiElement, func: ResolveElementFunction) {
    val fileIndex = ProjectRootManager.getInstance(project).fileIndex
    val module = fileIndex.getModuleForFile(virtualFile) ?: return
    val javaVirtualFile = ForestTemplateUtil.getSourceJavaFile(virtualFile) ?: return
    val filePath = javaVirtualFile.path
    val isTestSourceFile = ForestTemplateUtil.isTestFile(filePath)
    val hasSpringBootLib = SpringBootLibraryUtil.hasSpringBootLibrary(module)
    val literal = ForestTemplateUtil.getJavaElement(element)
    val method = PsiTreeUtil.getParentOfType(literal, PsiMethod::class.java)
    func.resolve(javaVirtualFile, module, isTestSourceFile, hasSpringBootLib, method)
}

fun findMethodParameter(method: PsiMethod, func: BiFunction<PsiParameter, Int, Boolean>) {
    val paramList = PsiTreeUtil.getChildOfType(method, PsiParameterList::class.java)
    if (paramList != null && paramList.parametersCount > 0) {
        val methodParamArray = paramList.parameters
        for (i in methodParamArray.indices) {
            val methodParam = methodParamArray[i]
            if (!func.apply(methodParam, i)) {
                return
            }
        }
    }
}

fun getPathExpressionText(element: PsiElement): String? {
    if (element is ForestTemplatePathElement) {
        val prevElementText = getPathExpressionText(element.prevSibling) ?: return element.getText()
        return prevElementText + element.getText()
    }
    return (element as? ForestTemplatePrimary)?.text
}

fun getterMethodName(name: String): String {
    return "get" + name.substring(0, 1).toUpperCase() + name.substring(1)
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

fun Any.getNodeIcon(): Icon {
    val o: Any
    o = if (this is DefaultMutableTreeNode) {
        this.userObject
    } else {
        this
    }
    return when (o) {
        is Project -> {
            AllIcons.Nodes.Folder
        }
        is Module -> {
            AllIcons.Nodes.Folder
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

fun interface ResolveElementFunction {

    fun resolve(
        javaVirtualFile: VirtualFile,
        module: Module,
        isTestSourceFile: Boolean,
        hasSpringBootLib: Boolean,
        defMethod: PsiMethod?
    )
}
