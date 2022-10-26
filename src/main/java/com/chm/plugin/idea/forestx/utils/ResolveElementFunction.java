package com.chm.plugin.idea.forestx.utils;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiMethod;

@FunctionalInterface
public interface ResolveElementFunction {

    void resolve(VirtualFile javaVirtualFile, Module module, boolean isTestSourceFile, boolean hasSpringBootLib, PsiMethod defMethod);

}
