package com.prof18.kmpframeworkbundler.task.fatframework

import com.prof18.kmpframeworkbundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmpframeworkbundler.data.PluginConfig
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

        for (framework in config.debugFrameworkList) {
            dependsOn(framework.linkTaskName)
        }
        baseName = config.frameworkName.get()
        from(config.debugFrameworkList)
        destinationDir = File(config.outputPath.get())
    }
}