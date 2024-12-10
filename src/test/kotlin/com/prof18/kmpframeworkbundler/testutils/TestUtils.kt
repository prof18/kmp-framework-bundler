package com.prof18.kmpframeworkbundler.testutils

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

const val FRAMEWORK_VERSION_NUMBER = "1.0.0"

const val POD_SPEC_VERSION_NUMBER = "s.version       = \"$FRAMEWORK_VERSION_NUMBER\""

val baseFatFrameworkGradleFile = """
        kotlin {
            listOf(
                iosX64(),
                iosArm64(),
            ).forEach {
                it.binaries.framework {
                    baseName = "FrameworkName"
                }
            }            
        }
    """.trimIndent()

val baseXCFrameworkGradleFile = """
        kotlin {
            val xcFramework = XCFramework("LibraryName")
            
            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
            ).forEach {
                it.binaries.framework {
                    baseName = "LibraryName"
                    xcFramework.add(this)
                }
            }  
        }
    """.trimIndent()

val fatFrameworkGradleFile = """
        kotlin {
            listOf(
                iosX64(),
                iosArm64(),
            ).forEach {
                it.binaries.framework {
                    baseName = "FrameworkName"
                }
            }              
        }
        
       frameworkBundlerConfig {
            frameworkName.set("LibraryName")
            outputPath.set("${'$'}rootDir/../../../../../test-dest")
            versionName.set("$FRAMEWORK_VERSION_NUMBER")
            frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.FAT_FRAMEWORK
       }  
    """.trimIndent()

val xcFrameworkGradleFile = """        
        kotlin {
            val xcFramework = XCFramework("LibraryName")
            
            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
            ).forEach {
                it.binaries.framework {
                    baseName = "LibraryName"
                    xcFramework.add(this)
                }
            }             
        }
        
       frameworkBundlerConfig {
            frameworkName.set("LibraryName")
            outputPath.set("${'$'}rootDir/../../../../../test-dest")
            versionName.set("$FRAMEWORK_VERSION_NUMBER")
            frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.XC_FRAMEWORK
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

fun File.buildAndRun(vararg commands: String): BuildResult = GradleRunner.create()
    .withProjectDir(this)
    .withArguments(*commands, "--stacktrace")
    .forwardOutput()
    .build()

fun File.buildAndFail(vararg commands: String): BuildResult = GradleRunner.create()
    .withProjectDir(this)
    .withArguments(*commands, "--stacktrace")
    .forwardOutput()
    .buildAndFail()
