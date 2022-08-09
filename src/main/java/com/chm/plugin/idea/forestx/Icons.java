package com.chm.plugin.idea.forestx;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-03-11
 **/
public interface Icons {

    /**
     * Forest 图标
     */
    Icon ICON = IconLoader.getIcon("/icons/forest.png");

    Icon ICON_12 = IconLoader.getIcon("/icons/forest-12x12.png");

    Icon ICON_16 = IconLoader.getIcon("/icons/forest-16x16.png");

    Icon PROJECT_16 = IconLoader.getIcon("/icons/forest-module2.svg");

    Icon MODULE_16 = IconLoader.getIcon("/icons/forest-module2.svg");

    Icon DELETE = IconLoader.getIcon("/icons/request-DELETE.svg");

    Icon GET = IconLoader.getIcon("/icons/request-GET.svg");

    Icon HEAD = IconLoader.getIcon("/icons/request-HEAD.svg");

    Icon OPTIONS = IconLoader.getIcon("/icons/request-OPTIONS.svg");

    Icon PATCH = IconLoader.getIcon("/icons/request-PATCH.svg");

    Icon POST = IconLoader.getIcon("/icons/request-POST.svg");

    Icon PUT = IconLoader.getIcon("/icons/request-PUT.svg");

    Icon TRACE = IconLoader.getIcon("/icons/request-TRACE.svg");

    /**
     * The constant SPRING_INJECTION_ICON.
     * 锤子不好看, 就用代表mapper文件的图标好了
     * Icon SPRING_INJECTION_ICON = IconLoader.findIcon("/icons/forest.ico");
     */
    Icon SPRING_INJECTION_ICON = ICON;
}
