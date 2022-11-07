package com.chm.plugin.idea.forestx.template.utils;

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.yaml.YAMLFileType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SpringBootConfigFileUtil {


    public static List<VirtualFile> findSpringBootConfigFiles(final Project project, final boolean needTestFile) {
        final List<VirtualFile> fileList = new ArrayList<>();
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

        final List<VirtualFile> bootstrapYmlFiles = new LinkedList<>();
        final List<VirtualFile> bootstrapYamlFiles = new LinkedList<>();
        final List<VirtualFile> bootstrapPropertiesFiles = new LinkedList<>();
        final List<VirtualFile> applicationYmlFiles = new LinkedList<>();
        final List<VirtualFile> applicationYamlFiles = new LinkedList<>();
        final List<VirtualFile> applicationPropertiesFiles = new LinkedList<>();
        final List<VirtualFile> applicationDefaultYmlFiles = new LinkedList<>();
        final List<VirtualFile> applicationDefaultYamlFiles = new LinkedList<>();
        final List<VirtualFile> applicationDefaultPropertiesFiles = new LinkedList<>();
        final List<VirtualFile> otherFiles = new LinkedList<>();
        final List<VirtualFile> allConfigFiles = new LinkedList<>();
        if (virtualYAMLFiles != null) {
            allConfigFiles.addAll(virtualYAMLFiles);
        }
        if (virtualPropertiesFile != null) {
            allConfigFiles.addAll(virtualPropertiesFile);
        }
        for (final VirtualFile virtualFile : allConfigFiles) {
            final String configFilePath = virtualFile.getPath();
            final boolean isTestConfigFile = configFilePath.contains("/src/test");
            if (needTestFile && !isTestConfigFile) {
                continue;
            } else if (!needTestFile && isTestConfigFile) {
                continue;
            }
            final String[] paths = virtualFile.getName().split("[\\\\/]");
            final String fileName = paths[paths.length - 1];
            if ("bootstrap.yml".equals(fileName)) {
                bootstrapYmlFiles.add(virtualFile);
            } else if ("bootstrap.yaml".equals(fileName)) {
                bootstrapYamlFiles.add(virtualFile);
            } else if ("bootstrap.properties".equals(fileName)) {
                bootstrapPropertiesFiles.add(virtualFile);
            } else if ("application.yml".equals(fileName)) {
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
            } else if (fileName.endsWith(".yml") || fileName.endsWith("yaml")) {
                otherFiles.add(virtualFile);
            } else if (fileName.endsWith("properties")) {
                otherFiles.add(virtualFile);
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
        if (!otherFiles.isEmpty()) {
            return otherFiles;
        }
        return fileList;
    }

}
