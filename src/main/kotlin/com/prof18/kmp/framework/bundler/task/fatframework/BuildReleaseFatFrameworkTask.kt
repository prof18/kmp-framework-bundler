package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import java.io.File

internal const val BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME = "buildReleaseIosFatFramework"

internal fun Project.registerBuildReleaseFatFrameworkTask(
    config: PluginConfig,
) {
    tasks.register(BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME, FatFrameworkTask::class.java) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Release Fat Framework"

        for (framework in config.releaseFrameworkList) {
            dependsOn(framework.linkTaskName)
        }
        baseName = config.frameworkName.get()
        from(config.releaseFrameworkList)
        destinationDir = File(config.outputPath.get())
    }
}