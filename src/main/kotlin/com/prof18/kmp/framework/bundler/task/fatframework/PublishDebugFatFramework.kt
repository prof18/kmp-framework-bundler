package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import com.prof18.kmp.framework.bundler.task.common.PrepareCocoaRepoForDebugTasks
import com.prof18.kmp.framework.bundler.task.common.publishDebugFramework
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject


internal abstract class PublishDebugFatFrameworkTask @Inject constructor(
    private val pluginConfig: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Publish the debug FatFramework to a CocoaPod repository"
    }

    @TaskAction
    fun action() {
       project.publishDebugFramework(pluginConfig)
    }

    internal companion object {
        const val NAME = "publishDebugIosFatFramework"
    }
}

internal fun Project.registerPublishDebugFatFrameworkTask(
    config: PluginConfig,
) {
    tasks.register(
        PublishDebugFatFrameworkTask.NAME,
        PublishDebugFatFrameworkTask::class.java,
        config,
    ).apply {
        configure {
            mustRunAfter(PrepareCocoaRepoForDebugTasks.NAME)
            dependsOn(PrepareCocoaRepoForDebugTasks.NAME)
            dependsOn(BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME)
        }
    }
}