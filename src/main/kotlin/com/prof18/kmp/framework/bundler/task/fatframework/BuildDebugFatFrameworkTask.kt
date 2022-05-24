package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import java.io.File

internal const val BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME = "buildDebugIosFatFramework"

internal fun Project.registerBuildDebugFatFrameworkTask(
    config: PluginConfig,
) {
    tasks.register(BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME, FatFrameworkTask::class.java) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Debug Fat Framework"

        for (framework in config.debugFatFrameworkList) {
            dependsOn(framework.linkTaskName)
        }
        baseName = config.frameworkName.get()
        from(config.debugFatFrameworkList)
        destinationDir = File(config.outputPath.get())
    }
}