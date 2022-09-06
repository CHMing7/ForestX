package com.chm.plugin.idea.forestx.template.utils;

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.boot.SpringBootConfigFileConstants;
import org.jetbrains.yaml.YAMLFileType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SpringBootConfigFileUtil {



    public static List<VirtualFile> findSpringBootConfigFiles(final Project project, final boolean needTestFile) {
        List<VirtualFile> fileList = new ArrayList<>();
        Collection<VirtualFile> virtualYAMLFiles = null;
        try {
            virtualYAMLFiles = FileTypeIndex.getFiles(YAMLFileType.YML, GlobalSearchScope.allScope(project));
        } catch (Throwable th) {
        }
        Collection<VirtualFile> virtualPropertiesFile = null;
        try {
            virtualPropertiesFile = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, GlobalSearchScope.allScope(project));
        } catch (Throwable th) {
        }

        List<VirtualFile> bootstrapYmlFiles = new LinkedList<>();
        List<VirtualFile> bootstrapYamlFiles = new LinkedList<>();
        List<VirtualFile> bootstrapPropertiesFiles = new LinkedList<>();
        List<VirtualFile> applicationYmlFiles = new LinkedList<>();
        List<VirtualFile> applicationYamlFiles = new LinkedList<>();
        List<VirtualFile> applicationPropertiesFiles = new LinkedList<>();
        List<VirtualFile> applicationDefaultYmlFiles = new LinkedList<>();
        List<VirtualFile> applicationDefaultYamlFiles = new LinkedList<>();
        List<VirtualFile> applicationDefaultPropertiesFiles = new LinkedList<>();
        List<VirtualFile> allConfigFiles = new LinkedList<>();
        if (virtualYAMLFiles != null) {
            allConfigFiles.addAll(virtualYAMLFiles);
        }
        if (virtualPropertiesFile != null) {
            allConfigFiles.addAll(virtualPropertiesFile);
        }
        for (VirtualFile virtualFile : allConfigFiles) {
            String configFilePath = virtualFile.getPath();
            final boolean isTestConfigFile = configFilePath.contains("/src/test");
            if (needTestFile && !isTestConfigFile) {
                continue;
            } else if (!needTestFile && isTestConfigFile) {
                continue;
            }
            String[] paths = virtualFile.getName().split("[\\\\/]");
            String fileName = paths[paths.length - 1];
            if ("bootstrap.yml".equals(fileName)) {
                bootstrapYmlFiles.add(virtualFile);
            } else if ("bootstrap.yaml".equals(fileName)) {
                bootstrapYamlFiles.add(virtualFile);
            } else if ("bootstrap.properties".equals(fileName)) {
                bootstrapPropertiesFiles.add(virtualFile);
            } else if (SpringBootConfigFileConstants.APPLICATION_YML.equals(fileName)) {
                applicationYmlFiles.add(virtualFile);
            } else if ("application.yaml".equals(fileName)) {
                applicationYamlFiles.add(virtualFile);
            } else if ("application.properties".equals(fileName)) {
                applicationPropertiesFiles.add(virtualFile);
            } else if ("application-default.yml".equals(fileName)) {
                applicationDefaultYmlFiles.add(virtualFile);
            } else if ("application-default.yaml".equals(fileName)) {
                applicationDefaultYamlFiles.add(virtualFile);
            } else if ("application-default.properties".equals(fileName)) {
                applicationDefaultPropertiesFiles.add(virtualFile);
            }
        }
        if (!bootstrapYmlFiles.isEmpty()) {
            fileList.addAll(bootstrapYmlFiles);
        } else if (!bootstrapYamlFiles.isEmpty()) {
            fileList.addAll(bootstrapYamlFiles);
        } else if (!bootstrapPropertiesFiles.isEmpty()) {
            fileList.addAll(bootstrapPropertiesFiles);
        }

        if (!applicationYmlFiles.isEmpty()) {
            fileList.addAll(applicationYmlFiles);
            return fileList;
        }
        if (!applicationYamlFiles.isEmpty()) {
            fileList.addAll(applicationYamlFiles);
            return fileList;
        }
        if (!applicationPropertiesFiles.isEmpty()) {
            fileList.addAll(applicationPropertiesFiles);
            return fileList;
        }
        if (!applicationDefaultYmlFiles.isEmpty()) {
            fileList.addAll(applicationDefaultYmlFiles);
            return fileList;
        }
        if (!applicationDefaultYamlFiles.isEmpty()) {
            fileList.addAll(applicationDefaultYamlFiles);
            return fileList;
        }
        if (!applicationDefaultPropertiesFiles.isEmpty()) {
            fileList.addAll(applicationDefaultPropertiesFiles);
            return fileList;
        }
        return fileList;
    }

}
