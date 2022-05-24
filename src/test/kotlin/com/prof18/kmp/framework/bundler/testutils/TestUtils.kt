package com.prof18.kmp.framework.bundler.testutils

object TestUtils {

    private const val KOTLIN_VERSION = "1.6.20"

    val baseGradleFile = """
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
    """.trimIndent()
}


/*


frameworkBundlerConfig {
    frameworkName = "LibraryName"
    outputPath = "$rootDir/../test-dest"
    namePrefix = "LibraryName"
    versionName = "1.2-SNAPSHOT"

    cocoaPodRepoInfo {
        summary = "This is a test KMP framework"
        homepage = "https://github.com/prof18/ccoca-repo-test"
        license = "Apache"
        authors = "\"Revelop Team\" => \"team@uniwhere.com\""
        gitUrl = "git@github.com:prof18/ccoca-repo-test.git"
    }

}

 */