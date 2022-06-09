package com.prof18.kmp.framework.bundler.testutils

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

private const val KOTLIN_VERSION = "1.6.20"

const val FRAMEWORK_VERSION_NUMBER = "1.0.0"

const val POD_SPEC_VERSION_NUMBER = "s.version       = \"$FRAMEWORK_VERSION_NUMBER\""

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
            outputPath.set("${'$'}rootDir/../../../../../test-dest")
            versionName.set("$FRAMEWORK_VERSION_NUMBER")
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
            outputPath.set("${'$'}rootDir/../../../../../test-dest")
            versionName.set("$FRAMEWORK_VERSION_NUMBER")
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
            outputPath.set("${'$'}rootDir/../../../../../test-dest")
            versionName.set("$FRAMEWORK_VERSION_NUMBER")
            frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK_LEGACY_BUILD
       }  
    """.trimIndent()

val fatFrameworkPodSpec = """
    Pod::Spec.new do |s|
    s.name          = "LibraryName"
    s.version       = "<version-number>"
    s.summary       = "This is a test KMP framework"
    s.homepage      = "https://www.homepage.com"
    s.license       = "Apache"
    s.author        = { "Author Name" => "mg@me.com" }
    s.vendored_frameworks = 'LibraryName.framework'
    s.source        = { :git => "git@url.com", :tag => "#{s.version}" }
    s.exclude_files = "Classes/Exclude"
    end
""".trimIndent()

val xcFrameworkPodSpec = """
    Pod::Spec.new do |s|
    s.name          = "LibraryName"
    s.version       = "<version-number>"
    s.summary       = "This is a test KMP framework"
    s.homepage      = "https://www.homepage.com"
    s.license       = "Apache"
    s.author        = { "Author Name" => "mg@me.com" }
    s.vendored_frameworks = 'LibraryName.xcframework'
    s.source        = { :git => "git@url.com", :tag => "#{s.version}" }
    s.exclude_files = "Classes/Exclude"
    end
""".trimIndent()

fun File.runBashCommand(vararg arguments: String) =
    ProcessBuilder(*arguments)
        .directory(this)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor(60, TimeUnit.SECONDS)

fun File.runBashCommandAndGetOutput(vararg arguments: String): String {
    val pb = ProcessBuilder(*arguments).directory(this)
    val process = pb.start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val builder = StringBuilder()
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        builder.append(line)
        builder.append(System.lineSeparator())
    }
    return builder.toString()
}

fun File.getPlainText(): String {
    val bufferedReader: BufferedReader = this.bufferedReader()
    return bufferedReader.use { it.readText() }
}