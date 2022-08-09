package com.chm.plugin.idea.forestx.constants;

import com.chm.plugin.idea.forestx.tw.RightSidebarToolWindow;
import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;

import java.util.Map;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-07-29
 **/
public interface Constants {

    Map<Project, RightSidebarToolWindow> PROJECT_RIGHT_SIDEBAR_MAP = Maps.newConcurrentMap();

    String TOOL_WINDOW = "ForestX";

    String DISPLAY_NAME = "ForestX";

    String PLUGIN_ID_STR = "com.chm.plugin.idea.forestx";

    static RightSidebarToolWindow getRightSidebar(Project project) {
        return PROJECT_RIGHT_SIDEBAR_MAP.computeIfAbsent(project, RightSidebarToolWindow::new);
    }
}
