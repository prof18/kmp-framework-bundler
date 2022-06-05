package com.prof18.kmp.framework.bundler.data

import com.prof18.kmp.framework.bundler.CocoaPodRepoDSL
import com.prof18.kmp.framework.bundler.KMPFrameworkBundlerExtension
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask
import java.io.File

internal class PluginConfig private constructor(
    val debugFrameworkList: List<Framework>,
    val releaseFrameworkList: List<Framework>,
    val frameworkName: Property<String>,
    val outputPath: Property<String>,
    val versionName: Property<String>,
    val cocoaPodRepoInfo: CocoaPodRepoDSL,
    private val xcFrameworkTasks: List<DefaultTask>,
    val frameworkType: FrameworkType,
) {

    internal fun getPodSpecFile() = File("${outputPath.get()}/${frameworkName.get()}.podspec")

    internal fun getXCFrameworkDebugBuildTask(): XCFrameworkTask {
        val buildTask = xcFrameworkTasks.firstOrNull {
            (it as XCFrameworkTask).buildType == NativeBuildType.DEBUG
        } as? XCFrameworkTask ?: throw ClassNotFoundException(ErrorMessages.XC_FRAMEWORK_DEBUG_TASK_NOT_FOUND)
        return buildTask
    }

    internal fun getXCFrameworkReleaseBuildTask(): XCFrameworkTask {
        val buildTask = xcFrameworkTasks.firstOrNull {
            (it as XCFrameworkTask).buildType == NativeBuildType.RELEASE
        } as? XCFrameworkTask ?: throw IllegalArgumentException(ErrorMessages.XC_FRAMEWORK_RELEASE_TASK_NOT_FOUND)
        return buildTask
    }

    internal companion object {
        fun of(extension: KMPFrameworkBundlerExtension): PluginConfig {
            return PluginConfig(
                debugFrameworkList = extension.debugFrameworkList,
                releaseFrameworkList = extension.releaseFrameworkList,
                frameworkName = extension.frameworkName,
                outputPath = extension.outputPath,
                versionName = extension.versionName,
                cocoaPodRepoInfo = extension.cocoaPodRepoInfo,
                xcFrameworkTasks = extension.xcFrameworkTasks,
                frameworkType = extension.frameworkType,
            )
        }
    }
}