package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import com.prof18.kmp.framework.bundler.task.common.PrepareCocoaRepoForReleaseTasks
import com.prof18.kmp.framework.bundler.task.common.publishReleaseFramework
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

internal abstract class PublishReleaseXCFrameworkTask @Inject constructor(
    private val pluginConfig: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Publish a Release XCFramework to the CocoaPod Repository"
    }

    @TaskAction
    fun action() {
        project.publishReleaseFramework(pluginConfig)
    }

    internal companion object {
        const val NAME: String = "publishReleaseXCFramework"
    }
}

internal fun Project.registerPublishReleaseXCFrameworkTask(
    pluginConfig: PluginConfig,
) {
    tasks.register(
        PublishReleaseXCFrameworkTask.NAME,
        PublishReleaseXCFrameworkTask::class.java,
        pluginConfig,
    ).apply {
        configure {
            mustRunAfter(PrepareCocoaRepoForReleaseTasks.NAME)
            dependsOn(PrepareCocoaRepoForReleaseTasks.NAME)
            dependsOn(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME)
        }
    }
}
