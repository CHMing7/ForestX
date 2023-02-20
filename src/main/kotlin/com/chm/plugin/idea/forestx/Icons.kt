package com.chm.plugin.idea.forestx

import com.intellij.openapi.util.IconLoader

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/

interface Icons {

    companion object {

        /**
         * Forest 图标
         */
        val ICON = IconLoader.getIcon("/icons/forest-logo-20x20.svg", Icons::class.java)

        val ICON_16 = IconLoader.getIcon("/icons/forest-logo-16x16.svg", Icons::class.java)

        val INTERFACE_18 = IconLoader.getIcon("/icons/forest-logo-18x18.svg", Icons::class.java)

        val DELETE = IconLoader.getIcon("/icons/request-DELETE.svg", Icons::class.java)

        val GET = IconLoader.getIcon("/icons/request-GET.svg", Icons::class.java)

        val HEAD = IconLoader.getIcon("/icons/request-HEAD.svg", Icons::class.java)

        val OPTIONS = IconLoader.getIcon("/icons/request-OPTIONS.svg", Icons::class.java)

        val PATCH = IconLoader.getIcon("/icons/request-PATCH.svg", Icons::class.java)

        val POST = IconLoader.getIcon("/icons/request-POST.svg", Icons::class.java)

        val PUT = IconLoader.getIcon("/icons/request-PUT.svg", Icons::class.java)

        val TRACE = IconLoader.getIcon("/icons/request-TRACE.svg", Icons::class.java)

        val SPRING_INJECTION_ICON = ICON
    }
}

