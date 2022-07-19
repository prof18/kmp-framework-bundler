package com.prof18.kmpframeworkbundler.task.common

import com.prof18.kmpframeworkbundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmpframeworkbundler.utils.execBashCommandInRepoAndThrowExecException
import com.prof18.kmpframeworkbundler.utils.executeBashCommand
import com.prof18.kmpframeworkbundler.data.PluginConfig
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecException
import javax.inject.Inject

internal abstract class PrepareCocoaRepoForDebugTasks @Inject constructor(
    private val config: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Prepare the CocoaPod repository for debug."
    }

    @TaskAction
    fun action() {
        try {
            project.executeBashCommand(
                showOutput = false,
                workingDirPath = config.outputPath.get(),
                commandList = listOf("git", "rev-parse", "--is-inside-work-tree")
            )
        } catch (e: ExecException) {
            throw InvalidUserDataException("The provided output folder is not a git repository!")
        }

        // Checkout on develop
        project.execBashCommandInRepoAndThrowExecException(
            commandList = listOf("git", "checkout", "develop"),
            workingDirPath = config.outputPath.get(),
            exceptionMessage = "Error while checking out to the develop branch. Are you sure it does exists?"
        )
    }

    internal companion object {
        const val NAME: String = "prepareIosCocoaRepoForDebug"
    }
}

internal fun Project.registerPrepareCocoaRepoForDebugTasks(
    pluginConfig: PluginConfig,
) {
    tasks.register(
        PrepareCocoaRepoForDebugTasks.NAME,
        PrepareCocoaRepoForDebugTasks::class.java,
        pluginConfig,
    )
}