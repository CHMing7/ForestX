package com.chm.plugin.idea.forestx.template.completion;

import com.chm.plugin.idea.forestx.template.holder.ForestTemplateFieldHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateInvocationHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePathElementHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplatePropertyVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateVariableHolder;
import com.chm.plugin.idea.forestx.template.holder.ForestTemplateYAMLVariableHolder;
import com.chm.plugin.idea.forestx.template.psi.ForestTemplatePathElement;
import com.chm.plugin.idea.forestx.template.utils.ForestTemplateUtil;
import com.chm.plugin.idea.forestx.utils.TreeNodeUtilKt;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.ProcessingContext;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ForestELPathElementCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        final PsiElement element = parameters.getPosition();
        final Project project = element.getProject();
        final VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile();
        if (virtualFile == null) {
            return;
        }
        TreeNodeUtilKt.resolveElement(project, virtualFile, element,
                (javaVirtualFile, module, isTestSourceFile, hasSpringBootLib, defMethod) -> {
                    final ForestTemplatePathElement pathElement = PsiTreeUtil.getParentOfType(element, ForestTemplatePathElement.class);
                    final PsiElement prevElement = pathElement.getPrevSibling();
                    if (prevElement == null) {
                        return;
                    }
                    final PsiElement literal = ForestTemplateUtil.getJavaElement(element);
                    final PsiMethod method = PsiTreeUtil.getParentOfType(literal, PsiMethod.class);
                    final ForestTemplatePathElementHolder holder = ForestTemplateUtil.getELHolder(
                            isTestSourceFile, prevElement, method);
                    if (holder == null) {
                        return;
                    }
                    if (holder instanceof ForestTemplateYAMLVariableHolder) {
                        ForestTemplateYAMLVariableHolder yamlHolder = (ForestTemplateYAMLVariableHolder) holder;
                        if (yamlHolder.isMapping()) {
                            final String pathExprText = TreeNodeUtilKt.getPathExpressionText(prevElement);
                            final List<ForestTemplateVariableHolder> variableHolders = ForestTemplateUtil.findConfigHolders(
                                    project, isTestSourceFile, ForestTemplateUtil.FOREST_VARIABLES_PREFIX + pathExprText + ".", true);
                            for (final ForestTemplateVariableHolder varHolder : variableHolders) {
                                if (varHolder instanceof ForestTemplateYAMLVariableHolder) {
                                    resultSet.addElement(LookupElementBuilder.create(varHolder)
                                            .withRenderer(ForestTemplateYAMLVariableHolder.YAML_KEY_VALUE_CONFIG_RENDER));
                                } else if (varHolder instanceof ForestTemplatePropertyVariableHolder) {
                                    resultSet.addElement(LookupElementBuilder.create(varHolder)
                                            .withRenderer(ForestTemplatePropertyVariableHolder.PROPERTY_RENDER));
                                }
                            }
                            return;
                        }
                    }
                    final PsiType type = holder.getType();
                    final PsiClass psiClass = PsiUtil.resolveClassInType(type);
                    if (psiClass == null) {
                        return;
                    }
                    final PsiMethod[] methods = PsiClassImplUtil.getAllMethods(psiClass);
                    final Set<String> nameCache = new HashSet<>();
                    for (PsiMethod mtd : methods) {
                        final PsiParameterList paramList = mtd.getParameterList();
                        final int paramCount = paramList.getParametersCount();
                        final PsiType returnType = mtd.getReturnType();
                        if (Objects.equals(returnType, PsiType.VOID)) {
                            continue;
                        }
                        if (paramCount > 0) {
                            continue;
                        }
                        final String methodName = mtd.getName();
                        if (mtd.isConstructor()) {
                            continue;
                        }
                        final String methodFullName = TreeNodeUtilKt.getMethodFullName(mtd);
                        if (nameCache.contains(methodFullName)) {
                            continue;
                        }
                        nameCache.add(methodFullName);
                        if (methodName.startsWith("get") && methodName.length() > 3) {
                            final ForestTemplateFieldHolder fieldHolder = ForestTemplateFieldHolder.getHolder(mtd, type);
                            resultSet.addElement(LookupElementBuilder.create(fieldHolder)
                                            .withLookupString(methodName)
                                            .withRenderer(ForestTemplateFieldHolder.FIELD_RENDER));
                        } else {
                            final ForestTemplateInvocationHolder invocationHolder = new ForestTemplateInvocationHolder(
                                    mtd.getName(), mtd, type, Lists.newArrayList());
                            resultSet.addElement(LookupElementBuilder.create(invocationHolder)
                                    .withRenderer(ForestTemplateInvocationHolder.INVOCATION_RENDER)
                                    .withInsertHandler(ForestTemplateInvocationHolder.INVOCATION_INSERT_HANDLER));
                        }
                    }
                });

    }
}
