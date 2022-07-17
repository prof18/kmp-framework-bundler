import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform") version System.getProperty("kotlinVersion")
    id("com.prof18.kmp.framework.bundler") version System.getProperty("pluginVersion")
}
