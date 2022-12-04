package com.chm.plugin.idea.forestx.extension

import com.chm.plugin.idea.forestx.Icons
import com.chm.plugin.idea.forestx.annotation.Annotation
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePrimary
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil
import com.chm.plugin.idea.forestx.utils.ReadActionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ui.tree.TreeUtil
import org.apache.commons.compress.utils.Lists
import org.jetbrains.yaml.YAMLUtil
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl
import org.jetbrains.yaml.psi.impl.YAMLBlockSequenceImpl
import java.util.*
import java.util.function.BiFunction
import javax.swing.Icon
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath

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

val PROJECT_SPRING_BOOT_MAP: MutableMap<String, Boolean> = mutableMapOf()
val MODULE_SPRING_BOOT_MAP: MutableMap<String, Boolean> = mutableMapOf()
val MODULE_SPRING_MAP: MutableMap<String, Boolean> = mutableMapOf()

val BINDING_VAR_MAP: MutableMap<String, PsiElement> = mutableMapOf()

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
    val hasSpringBootLib = hasSpringBootLibrary(module)
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

fun hasSpringLibrary(module: Module): Boolean {
    val name = module.name
    if (!MODULE_SPRING_MAP.contains(name)) {
        val ret = try {
            val type = PsiType.getTypeByName(
                "org.springframework.core.SpringVersion",
                module.project,
                GlobalSearchScope.moduleScope(module)
            )
            type.isValid
        } catch (th: Throwable) {
            false
        }
        MODULE_SPRING_MAP[name] = ret
    }
    return MODULE_SPRING_MAP[name]!!
}

fun hasSpringBootLibrary(module: Module): Boolean {
    val name = module.name
    if (!MODULE_SPRING_BOOT_MAP.contains(name)) {
        val ret = try {
            val type = PsiType.getTypeByName(
                "org.springframework.boot.SpringBootVersion",
                module.project,
                GlobalSearchScope.moduleScope(module)
            )
            type.isValid
        } catch (th: Throwable) {
            false
        }
        MODULE_SPRING_BOOT_MAP[name] = ret
    }
    return MODULE_SPRING_BOOT_MAP[name]!!
}

fun hasSpringBootLibrary(project: Project): Boolean {
    val name = project.name
    if (!PROJECT_SPRING_BOOT_MAP.contains(name)) {
        val ret = try {
            val type = PsiType.getTypeByName(
                "org.springframework.boot.SpringBootVersion",
                project,
                GlobalSearchScope.allScope(project)
            )
            type.isValid
        } catch (th: Throwable) {
            false
        }
        PROJECT_SPRING_BOOT_MAP[name] = ret
    }
    return PROJECT_SPRING_BOOT_MAP[name]!!
}

fun getYAMLKeyValue(yamlFile: YAMLFile, keyName: String): Pair<YAMLPsiElement, YAMLValue?>? {
    var pair: Pair<YAMLPsiElement, YAMLValue?>? = null;
    eachYAMLKeyValues(yamlFile) { keyValue: YAMLPsiElement?, yamlValue: YAMLValue? ->
        val keyValueText = YAMLUtil.getConfigFullName(
            keyValue!!
        )
        if (keyValueText == keyName) {
            pair = Pair(keyValue, yamlValue)
            return@eachYAMLKeyValues false
        }
        true
    }
    return pair
}


fun eachYAMLKeyValues(yamlFile: YAMLFile, func: (YAMLKeyValue?, YAMLValue?) -> Boolean): Boolean {
    val topKeyValues = YAMLUtil.getTopLevelKeys(yamlFile)
    for (keyValue in topKeyValues) {
        val value = keyValue.value
        if (value != null) {
            val ret: Boolean = eachYAMLKeyValues(keyValue, value, func)
            if (!ret) {
                return false
            }
        }
    }
    return true
}

fun eachYAMLKeyValues(keyValue: YAMLKeyValue, value: YAMLValue, func: (YAMLKeyValue?, YAMLValue?) -> Boolean): Boolean {
    if (!func(keyValue, value)) {
        return false
    }
    if (value is YAMLBlockSequenceImpl) {
        val block: YAMLBlockSequenceImpl = value
        val items = block.items
        for (item in items) {
            if (item is YAMLKeyValue) {
                val ret = eachYAMLKeyValues(item, value, func)
                if (!ret) {
                    return false
                }
            }
        }
    } else if (value is YAMLBlockMappingImpl) {
        var mapping: YAMLBlockMappingImpl = value
        var mkvs = mapping.keyValues
        for (mkv in mkvs) {
            var mvalue: YAMLValue? = mkv.value
            if (!eachYAMLKeyValues(mkv, mvalue!!, func)) {
                return false
            }
        }
    }
    return true
}

fun getPathExpressionText(element: PsiElement): String? {
    if (element is ForestTemplatePathElement) {
        val prevElementText = getPathExpressionText(element.prevSibling) ?: return element.getText()
        return prevElementText + element.getText()
    }
    return (element as? ForestTemplatePrimary)?.text
}

fun getterMethodName(name: String): String {
    return "get" + name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
}

fun JTree.findNode(o: Any): DefaultMutableTreeNode? {
    val root = this.model.root as DefaultMutableTreeNode
    return root.findNode(o, true)
}

fun DefaultMutableTreeNode.findNode(
    o: Any,
    checkChild: Boolean = false
): DefaultMutableTreeNode? {
    val aObject = if (o is DefaultMutableTreeNode) o.userObject else o
    if (checkChild) {
        return TreeUtil.findNodeWithObject(this, aObject)
    }
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i) as DefaultMutableTreeNode
        if (child.userObject == aObject) {
            return child
        }
    }
    return null
}

fun DefaultMutableTreeNode.findNodeOrNew(
    model: DefaultTreeModel?,
    o: Any,
    comparator: Comparator<Any> = NAME_SORTED
): DefaultMutableTreeNode {
    val node = this.findNode(o)
    if (node != null) {
        return node
    }
    val newNode = DefaultMutableTreeNode(o)
    TreeUtil.insertNode(newNode, this, model, comparator)
    return newNode
}

fun DefaultMutableTreeNode.getAllChildren(): List<DefaultMutableTreeNode> {
    val childrenList: MutableList<DefaultMutableTreeNode> = Lists.newArrayList()
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i) as DefaultMutableTreeNode
        childrenList.add(child)
    }
    return childrenList
}

fun JTree.removeNode(
    node: DefaultMutableTreeNode
) {
    TreeUtil.removeLastPathComponent(this, TreePath(node.path))
}

fun Any.getNodeName(): String {
    val o: Any?
    o = if (this is DefaultMutableTreeNode) {
        this.userObject
    } else {
        this
    }
    return ReadActionUtil.runReadAction<String> {
        when (o) {
            is Project -> {
                o.name
            }
            is Module -> {
                o.name
            }
            is PsiClass -> {
                o.name ?: ""
            }
            is PsiMethod -> {
                o.name
            }
            else -> {
                o?.toString() ?: "null"
            }
        }
    }
}

fun Any.getForestAnnotationUrl(): String {
    val o = if (this is DefaultMutableTreeNode) {
        this.userObject
    } else {
        this
    }
    return when (o) {
        is PsiClass -> {
            val baseRequest = ReadActionUtil.findAnnotation(o, Annotation.BASE_REQUEST.qualifiedName)
            baseRequest?.let {
                // 存在@BaseRequest注解，拼接basePath
                val baseURLText = ReadActionUtil.getStringAttributeValue(it, "baseURL")
                if (!baseURLText.isNullOrBlank()) {
                    return baseURLText
                }
            }

            val baseURL = ReadActionUtil.findAnnotation(o, Annotation.BASE_URL.qualifiedName)
            baseURL?.let {
                val valueText = ReadActionUtil.getStringAttributeValue(it, "value")
                if (!valueText.isNullOrBlank()) {
                    return valueText
                }
            }

            val address = ReadActionUtil.findAnnotation(o, Annotation.ADDRESS.qualifiedName)
            address?.let {
                // 存在@Address注解，拼接basePath
                val schemeText = ReadActionUtil.getStringAttributeValue(it, "scheme")
                val hostText = ReadActionUtil.getStringAttributeValue(it, "host")
                val portText = ReadActionUtil.getStringAttributeValue(it, "port")
                if (!schemeText.isNullOrBlank() && !hostText.isNullOrBlank() && !portText.isNullOrBlank()) {
                    return "$schemeText://$hostText:$portText"
                }

                val basePathText = ReadActionUtil.getStringAttributeValue(it, "basePath")
                if (!basePathText.isNullOrBlank()) {
                    return basePathText
                }
            }

            ""
        }

        is PsiMethod -> {
            for ((forestMethodAnnotationName, _) in METHOD_ICON_MAP) {
                val annotation = ReadActionUtil.findAnnotation(o, forestMethodAnnotationName)
                annotation?.let {
                    val urlText = ReadActionUtil.getStringAttributeValue(it, "url")
                    if (!urlText.isNullOrBlank()) {
                        return urlText
                    }

                    val valueText = ReadActionUtil.getStringAttributeValue(it, "value")
                    if (!valueText.isNullOrBlank()) {
                        return valueText
                    }
                }

            }

            val requestAnnotation = ReadActionUtil.findAnnotation(o, Annotation.REQUEST.qualifiedName)
            if (requestAnnotation != null) {
                val urlText = ReadActionUtil.getStringAttributeValue(requestAnnotation, "url")
                if (!urlText.isNullOrBlank()) {
                    return urlText
                }

                val valueText = ReadActionUtil.getStringAttributeValue(requestAnnotation, "value")
                if (!valueText.isNullOrBlank()) {
                    return valueText
                }
            }

            ""
        }

        else -> {
            ""
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
            for ((forestMethodAnnotationName, icon) in METHOD_ICON_MAP) {
                val annotation = ReadActionUtil.findAnnotation(o, forestMethodAnnotationName)
                if (annotation != null) {
                    return icon
                }
            }

            val requestAnnotation = ReadActionUtil.findAnnotation(o, Annotation.REQUEST.qualifiedName)
            if (requestAnnotation != null) {
                val type = ReadActionUtil.getStringAttributeValue(requestAnnotation, "type")
                val icon2 = REQUEST_TYPE_ICON_MAP[StringUtil.toUpperCase(type)]
                return icon2 ?: Icons.GET
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
