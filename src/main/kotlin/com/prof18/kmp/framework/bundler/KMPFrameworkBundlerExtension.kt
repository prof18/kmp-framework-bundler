package com.prof18.kmp.framework.bundler

import com.prof18.kmp.framework.bundler.data.ErrorMessages
import com.prof18.kmp.framework.bundler.data.FrameworkType
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import javax.inject.Inject
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.property

internal const val KMP_FRAMEWORK_BUNDLER_EXTENSION = "frameworkBundlerConfig"

abstract class KMPFrameworkBundlerExtension @Inject constructor(
    objects: ObjectFactory,
) {

    val frameworkName: Property<String> = objects.property()
    val outputPath: Property<String> = objects.property()
    val versionName: Property<String> = objects.property()
    var frameworkType: FrameworkType = FrameworkType.XC_FRAMEWORK

    @get:Nested
    abstract val cocoaPodRepoInfo: CocoaPodRepoDSL

    // Internal variables
    internal val debugFrameworkList: MutableList<Framework> = mutableListOf()
    internal val releaseFrameworkList: MutableList<Framework> = mutableListOf()
    internal val xcFrameworkTasks: MutableList<DefaultTask> = mutableListOf()

    @Suppress("Unused") // Used by the external world
    fun cocoaPodRepoInfo(action: Action<CocoaPodRepoDSL>) {
        action.execute(cocoaPodRepoInfo)
    }

    fun validateUserInput() {
        if (!frameworkName.isPresent) {
            throw InvalidUserDataException(ErrorMessages.FRAMEWORK_NAME_NOT_PRESENT_MESSAGE)
        }
        if (!outputPath.isPresent) {
            throw InvalidUserDataException(ErrorMessages.OUTPUT_PATH_NOT_PRESENT_MESSAGE)
        }
        if (!versionName.isPresent) {
            throw InvalidUserDataException(ErrorMessages.VERSION_NAME_NOT_PRESENT_MESSAGE)
        }
    }
}

abstract class CocoaPodRepoDSL {
    abstract val summary: Property<String>
    abstract val homepage: Property<String>
    abstract val license: Property<String>
    abstract val authors: Property<String>
    abstract val gitUrl: Property<String>
}