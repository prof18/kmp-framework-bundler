package com.prof18.kmp.framework.bundler.testutils

import java.io.BufferedReader
import java.io.File

private const val KOTLIN_VERSION = "1.6.20"

val baseFatFrameworkGradleFile = """
        plugins {
            kotlin("multiplatform") version "$KOTLIN_VERSION"
            id("com.prof18.kmp.framework.bundler")
        }

        repositories {
            mavenCentral()
        }

        kotlin {
            ios() {
                binaries.framework("FrameworkName")
            }
            sourceSets {
                val commonMain by getting
                val iosMain by getting
            }
        }
    """.trimIndent()

val baseXCFrameworkGradleFile = """
        import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

        plugins {
            kotlin("multiplatform") version "$KOTLIN_VERSION"
            id("com.prof18.kmp.framework.bundler")
        }

        repositories {
            mavenCentral()
        }

        kotlin {
            val xcFramework = XCFramework("LibraryName")

            ios() {
                binaries.framework("LibraryName") {
                    xcFramework.add(this)
                }
            }
            sourceSets {
                val commonMain by getting
                val iosMain by getting
            }
        }
    """.trimIndent()

val fatFrameworkGradleFile = """
        plugins {
            kotlin("multiplatform") version "$KOTLIN_VERSION"
            id("com.prof18.kmp.framework.bundler")
        }

        repositories {
            mavenCentral()
        }

        kotlin {
            ios() {
                binaries.framework("FrameworkName")
            }
            sourceSets {
                val commonMain by getting
                val iosMain by getting
            }
        }
        
       frameworkBundlerConfig {
            frameworkName.set("LibraryName")
            outputPath.set("${'$'}rootDir/../test-dest")
            versionName.set("1.0.0")
            frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.FAT_FRAMEWORK
       }  
    """.trimIndent()

val xcFrameworkGradleFile = """
        import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

        plugins {
            kotlin("multiplatform") version "$KOTLIN_VERSION"
            id("com.prof18.kmp.framework.bundler")
        }

        repositories {
            mavenCentral()
        }

        kotlin {
            val xcFramework = XCFramework("LibraryName")

            ios() {
                binaries.framework("LibraryName") {
                    xcFramework.add(this)
                }
            }
            sourceSets {
                val commonMain by getting
                val iosMain by getting
            }
        }
        
       frameworkBundlerConfig {
            frameworkName.set("LibraryName")
            outputPath.set("${'$'}rootDir/../test-dest")
            versionName.set("1.0.0")
            frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK
       }  
    """.trimIndent()

val legacyXCFrameworkGradleFile = """
        plugins {
            kotlin("multiplatform") version "$KOTLIN_VERSION"
            id("com.prof18.kmp.framework.bundler")
        }

        repositories {
            mavenCentral()
        }

        kotlin {
            ios() {
                binaries.framework("LibraryName") 
            }
            sourceSets {
                val commonMain by getting
                val iosMain by getting
            }
        }
        
       frameworkBundlerConfig {
            frameworkName.set("LibraryName")
            outputPath.set("${'$'}rootDir/../test-dest")
            versionName.set("1.0.0")
            frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK_LEGACY_BUILD
       }  
    """.trimIndent()

fun getFileContent(plist: File): String {
    val bufferedReader: BufferedReader = plist.bufferedReader()
    return bufferedReader.use { it.readText() }
}