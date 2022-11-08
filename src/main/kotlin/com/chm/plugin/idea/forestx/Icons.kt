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
        val ICON = IconLoader.getIcon("/icons/forest-logo-20x20.svg")

        val ICON_16 = IconLoader.getIcon("/icons/forest-logo-16x16.svg")

        val INTERFACE_18 = IconLoader.getIcon("/icons/forest-logo-18x18.svg")

        val DELETE = IconLoader.getIcon("/icons/request-DELETE.svg")

        val GET = IconLoader.getIcon("/icons/request-GET.svg")

        val HEAD = IconLoader.getIcon("/icons/request-HEAD.svg")

        val OPTIONS = IconLoader.getIcon("/icons/request-OPTIONS.svg")

        val PATCH = IconLoader.getIcon("/icons/request-PATCH.svg")

        val POST = IconLoader.getIcon("/icons/request-POST.svg")

        val PUT = IconLoader.getIcon("/icons/request-PUT.svg")

        val TRACE = IconLoader.getIcon("/icons/request-TRACE.svg")

        val SPRING_INJECTION_ICON = ICON
    }
}

