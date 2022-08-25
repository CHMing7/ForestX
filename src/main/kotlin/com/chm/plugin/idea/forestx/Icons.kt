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
        var ICON = IconLoader.getIcon("/icons/forest-logo-20x20.svg")

        var ICON_16 = IconLoader.getIcon("/icons/forest-logo-16x16.svg")

        var PROJECT_16 = IconLoader.getIcon("/icons/forest-module-16x16.svg")

        var MODULE_16 = IconLoader.getIcon("/icons/forest-module-16x16.svg")

        var INTERFACE_18 = IconLoader.getIcon("/icons/forest-logo-18x18.svg")

        var DELETE = IconLoader.getIcon("/icons/request-DELETE.svg")

        var GET = IconLoader.getIcon("/icons/request-GET.svg")

        var HEAD = IconLoader.getIcon("/icons/request-HEAD.svg")

        var OPTIONS = IconLoader.getIcon("/icons/request-OPTIONS.svg")

        var PATCH = IconLoader.getIcon("/icons/request-PATCH.svg")

        var POST = IconLoader.getIcon("/icons/request-POST.svg")

        var PUT = IconLoader.getIcon("/icons/request-PUT.svg")

        var TRACE = IconLoader.getIcon("/icons/request-TRACE.svg")

        var SPRING_INJECTION_ICON = ICON
    }
}

