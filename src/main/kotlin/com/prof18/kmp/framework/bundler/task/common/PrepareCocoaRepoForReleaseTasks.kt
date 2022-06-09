package com.prof18.kmp.framework.bundler.task.common

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.utils.execBashCommandInRepoAndThrowExecException
import com.prof18.kmp.framework.bundler.utils.executeBashCommand
import com.prof18.kmp.framework.bundler.utils.retrieveMainBranchName
import com.prof18.kmp.framework.bundler.data.PluginConfig
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecException
import javax.inject.Inject

internal abstract class PrepareCocoaRepoForReleaseTasks @Inject constructor(
    private val config: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Prepare the CocoaPod repository for release."
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

        // Check if master or main
        val branchName = project.retrieveMainBranchName(config.outputPath.get())

        // Checkout on selected branch
        project.execBashCommandInRepoAndThrowExecException(
            commandList = listOf("git", "checkout", branchName),
            workingDirPath = config.outputPath.get(),
            exceptionMessage = "Error while checking out to the $branchName branch. Are you sure it does exists?"
        )
    }

    internal companion object {
        const val NAME: String = "prepareIosCocoaRepoForRelease"
    }
}

internal fun Project.registerPrepareCocoaRepoForReleaseTasks(
    pluginConfig: PluginConfig,
) {
    tasks.register(
        PrepareCocoaRepoForReleaseTasks.NAME,
        PrepareCocoaRepoForReleaseTasks::class.java,
        pluginConfig,
    )
}