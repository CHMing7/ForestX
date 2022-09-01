import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea apply true
    id("java")
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.intellij") version "1.7.0"
    id("org.jetbrains.changelog") version "1.3.1"
    id("com.github.ben-manes.versions") version "0.42.0"
}

group = "com.chm.plugin.idea.forestx"
version = "1.0.0"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2020.2.4")
    type.set("IU") // 企业版
    plugins.set(listOf("java", "Spring", "SpringBoot"))

    pluginName.set("ForestX")
    sandboxDir.set("${rootProject.rootDir}/idea-sandbox")

    updateSinceUntilBuild.set(false)
    downloadSources.set(true)
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}