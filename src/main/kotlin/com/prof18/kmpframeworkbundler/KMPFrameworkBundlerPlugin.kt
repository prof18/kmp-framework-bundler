package com.prof18.kmpframeworkbundler

import com.prof18.kmpframeworkbundler.data.ErrorMessages
import com.prof18.kmpframeworkbundler.data.FrameworkType
import com.prof18.kmpframeworkbundler.data.PluginConfig
import com.prof18.kmpframeworkbundler.data.isXCFramework
import com.prof18.kmpframeworkbundler.task.common.registerPrepareCocoaRepoForDebugTasks
import com.prof18.kmpframeworkbundler.task.common.registerPrepareCocoaRepoForReleaseTasks
import com.prof18.kmpframeworkbundler.task.fatframework.registerBuildDebugFatFrameworkTask
import com.prof18.kmpframeworkbundler.task.fatframework.registerBuildReleaseFatFrameworkTask
import com.prof18.kmpframeworkbundler.task.fatframework.registerPublishDebugFatFrameworkTask
import com.prof18.kmpframeworkbundler.task.fatframework.registerPublishReleaseFatFrameworkTask
import com.prof18.kmpframeworkbundler.task.registerGenerateCocoaPodRepositoryTask
import com.prof18.kmpframeworkbundler.task.xcframework.legacy.registerBuildLegacyDebugXCFrameworkTask
import com.prof18.kmpframeworkbundler.task.xcframework.legacy.registerBuildLegacyReleaseXCFrameworkTask
import com.prof18.kmpframeworkbundler.task.xcframework.registerBuildDebugXCFrameworkTask
import com.prof18.kmpframeworkbundler.task.xcframework.registerBuildReleaseXCFrameworkTask
import com.prof18.kmpframeworkbundler.task.xcframework.registerPublishDebugXCFrameworkTask
import com.prof18.kmpframeworkbundler.task.xcframework.registerPublishReleaseXCFrameworkTask
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask
import org.jetbrains.kotlin.konan.target.Family

abstract class KMPFrameworkBundlerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            KMP_FRAMEWORK_BUNDLER_EXTENSION,
            KMPFrameworkBundlerExtension::class.java,
        )

        project.afterEvaluate {
            extension.validateUserInput()

            val xcFrameworkTasks: List<DefaultTask> = try {
                tasks.withType(XCFrameworkTask::class.java).toList()
            } catch (e: NoClassDefFoundError) {
                // This will happen with Kotlin versions below than 1.5.30
                emptyList()
            }

            extension.xcFrameworkTasks.addAll(xcFrameworkTasks)

            if (extension.frameworkType == FrameworkType.XC_FRAMEWORK && xcFrameworkTasks.isEmpty()) {
                throw InvalidUserDataException(ErrorMessages.EMPTY_XC_FRAMEWORK_TASKS)
            }

            if (extension.frameworkType == FrameworkType.XC_FRAMEWORK_LEGACY_BUILD && xcFrameworkTasks.isNotEmpty()) {
                logger.lifecycle(ErrorMessages.USING_LEGACY_BUILD_SYSTEM)
            }

            project.extensions.findByType<KotlinMultiplatformExtension>()
                ?.let { kmpExtension: KotlinMultiplatformExtension ->
                    val nativeTargetList: List<KotlinNativeTarget> = kmpExtension.targets.toList()
                        .filterIsInstance<KotlinNativeTarget>()
                        .filter {
                            if (extension.frameworkType.isXCFramework()) {
                                it.konanTarget.family.isAppleFamily
                            } else {
                                it.konanTarget.family == Family.IOS
                            }
                        }

                    val debugFrameworks: List<Framework> = nativeTargetList
                        .flatMap {
                            it.binaries
                                .filter { binary ->
                                    binary.buildType == NativeBuildType.DEBUG && binary.outputKind == NativeOutputKind.FRAMEWORK
                                }
                                .mapNotNull { binary ->
                                    // Nullable cast just to be safe
                                    binary as? Framework
                                }
                        }

                    val releaseFrameworks: List<Framework> = nativeTargetList
                        .flatMap {
                            it.binaries
                                .filter { binary ->
                                    binary.buildType == NativeBuildType.RELEASE && binary.outputKind == NativeOutputKind.FRAMEWORK
                                }
                                .mapNotNull { binary ->
                                    // Nullable cast just to be safe
                                    binary as? Framework
                                }
                        }

                    extension.debugFrameworkList.addAll(debugFrameworks)
                    extension.releaseFrameworkList.addAll(releaseFrameworks)

                    val config = PluginConfig.of(extension)

                    // Register Tasks
                    // Cocoa Pod Repo
                    project.registerGenerateCocoaPodRepositoryTask(config)

                    project.registerPrepareCocoaRepoForDebugTasks(config)
                    project.registerPrepareCocoaRepoForReleaseTasks(config)

                    when (extension.frameworkType) {
                        FrameworkType.FAT_FRAMEWORK -> {
                            // Build
                            project.registerBuildDebugFatFrameworkTask(config)
                            project.registerBuildReleaseFatFrameworkTask(config)

                            // Release
                            project.registerPublishDebugFatFrameworkTask(config)
                            project.registerPublishReleaseFatFrameworkTask(config)
                        }
                        FrameworkType.XC_FRAMEWORK_LEGACY_BUILD -> {
                            // Build
                            project.registerBuildLegacyDebugXCFrameworkTask(config)
                            project.registerBuildLegacyReleaseXCFrameworkTask(config)

                            // Release
                            project.registerPublishDebugXCFrameworkTask(config)
                            project.registerPublishReleaseXCFrameworkTask(config)
                        }
                        FrameworkType.XC_FRAMEWORK -> {
                            // Build
                            project.registerBuildDebugXCFrameworkTask(config)
                            project.registerBuildReleaseXCFrameworkTask(config)

                            // Release
                            project.registerPublishDebugXCFrameworkTask(config)
                            project.registerPublishReleaseXCFrameworkTask(config)
                        }
                    }
                }
        }
    }
}



