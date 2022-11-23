import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()


plugins {
    id("java")
    idea
    // Kotlin Support 太高版本的kotlin jvm需要高版本的idea支持，切换到1.6.10 idea2021.2.2也能运行项目
    kotlin("jvm") version "1.6.10"
    // Intellij Support
    id("org.jetbrains.intellij") version "1.9.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"
}


group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    maven {
        url = uri("https://www.jetbrains.com/intellij-repository/releases")
    }
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
//    plugins = properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty)
}


dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
            options.encoding = "UTF-8"
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))
        // 说明文档键值对
        val readmeFileMap = mapOf(
            "README-EN.md" to "English",
            "README.md" to "中文"
        )
        val pluginDescriptionBuildList = mutableListOf<StringBuilder>()
        readmeFileMap.forEach { (fileName, language) ->
            val pluginDescriptionBuild = StringBuilder("")
            pluginDescriptionBuild.append("<h1>").append(language).append(":").append("</h1>").append("\n")
            // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
            pluginDescriptionBuild.append(projectDir.resolve(fileName).readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in$fileName:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run {
                org.jetbrains.changelog.markdownToHTML(this)
            })
            pluginDescriptionBuildList.add(pluginDescriptionBuild)
        }
        // 不同语言说明之间增加两空行
        pluginDescription.set(pluginDescriptionBuildList.joinToString("<br><br>") {
            it.toString()
        })

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider {
            changelog.run {
                getOrNull(properties("pluginVersion")) ?: getLatest()
            }.toHTML()
        })
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

}

tasks.test {
    useJUnit()
}

