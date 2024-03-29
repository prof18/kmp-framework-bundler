package com.prof18.kmpframeworkbundler.task.xcframework

import com.prof18.kmpframeworkbundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmpframeworkbundler.data.PluginConfig
import org.gradle.api.Project

internal const val BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME = "buildReleaseXCFramework"

internal fun Project.registerBuildReleaseXCFrameworkTask(
    config: PluginConfig,
) {
    tasks.register(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Release XCFramework"

        val debugBuildTask = config.getXCFrameworkReleaseBuildTask()
        dependsOn(debugBuildTask.name)

        doLast {
            val frameworkLocation = config.getXCFrameworkDebugBuildTask().outputDir
            val release = "${frameworkLocation.absolutePath}/release"
            copy {
                from(release)
                into(config.outputPath.get())
            }
        }
    }
}
