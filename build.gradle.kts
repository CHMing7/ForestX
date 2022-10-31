import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.intellij") version "0.7.3"
}

group = "com.chm.plugin.idea.forestx"
version = "1.0.0"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.2.4"
    type = "IU" // 企业版

    setPlugins("java", "Spring", "SpringBoot", "yaml")

    pluginName = "ForestX"
    sandboxDirectory = "${rootProject.rootDir}/idea-sandbox"

    updateSinceUntilBuild = false
    downloadSources = true
}


dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}