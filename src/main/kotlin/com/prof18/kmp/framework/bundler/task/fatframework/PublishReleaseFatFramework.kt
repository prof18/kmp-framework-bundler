package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import com.prof18.kmp.framework.bundler.task.common.PrepareCocoaRepoForReleaseTasks
import com.prof18.kmp.framework.bundler.task.common.publishReleaseFramework
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

internal abstract class PublishReleaseFatFrameworkTask @Inject constructor(
    private val pluginConfig: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Publish the release FatFramework to a CocoaPod repository"
    }

    @TaskAction
    fun action() {
        project.publishReleaseFramework(pluginConfig)
    }

    internal companion object {
        const val NAME: String = "publishReleaseIosFatFramework"
    }
}

internal fun Project.registerPublishReleaseFatFrameworkTask(
    config: PluginConfig
) {
    tasks.register(
        PublishReleaseFatFrameworkTask.NAME,
        PublishReleaseFatFrameworkTask::class.java,
        config,
    ).apply {
        configure {
            mustRunAfter(PrepareCocoaRepoForReleaseTasks.NAME)
            dependsOn(PrepareCocoaRepoForReleaseTasks.NAME)
            dependsOn(BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME)
        }
    }
}