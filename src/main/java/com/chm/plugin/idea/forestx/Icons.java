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


    /**
     * The constant SPRING_INJECTION_ICON.
     * 锤子不好看, 就用代表mapper文件的图标好了
     * Icon SPRING_INJECTION_ICON = IconLoader.findIcon("/icons/forest.ico");
     */
    Icon SPRING_INJECTION_ICON = ICON;

}
