package com.prof18.kmp.framework.bundler.task

import com.prof18.kmp.framework.bundler.KMPFrameworkBundlerPlugin
import com.prof18.kmp.framework.bundler.data.ErrorMessages
import com.prof18.kmp.framework.bundler.data.PluginConfig
import com.prof18.kmp.framework.bundler.data.isXCFramework
import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.utils.execBashCommandThrowExecException
import com.prof18.kmp.framework.bundler.utils.retrieveMainBranchName
import groovy.text.SimpleTemplateEngine
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

internal abstract class GenerateCocoaPodRepositoryTask @Inject constructor(
    private val conf: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Create a CocoaPod repository to distribute the FatFramework"
    }

    @TaskAction
    fun action() {

        if (!conf.cocoaPodRepoInfo.gitUrl.isPresent) {
            throw InvalidUserDataException(ErrorMessages.GIT_URL_NOT_PRESENT)
        }

        val podspecFileName = "${conf.frameworkName.get()}.podspec"

        val parentFile = File(conf.outputPath.get())

        val podspecFile = File("${conf.outputPath.get()}/$podspecFileName").apply {
            this.parentFile.mkdirs()
            createNewFile()
        }

        val frameworkName = if (conf.frameworkType.isXCFramework()) {
            "${conf.frameworkName.get()}.xcframework"
        } else {
            "${conf.frameworkName.get()}.framework"
        }

        val templateMap = mapOf(
            "name" to conf.frameworkName.get(),
            "version" to conf.versionName.get(),
            "summary" to conf.cocoaPodRepoInfo.summary.getOrElse(""),
            "homepage" to conf.cocoaPodRepoInfo.homepage.getOrElse(""),
            "license" to conf.cocoaPodRepoInfo.license.getOrElse(""),
            "authors" to conf.cocoaPodRepoInfo.authors.getOrElse(""),
            "gitUrl" to conf.cocoaPodRepoInfo.gitUrl.get(),
            "frameworkName" to frameworkName
        )

        val templateFile = KMPFrameworkBundlerPlugin::class.java.getResource("/template/Framework.podspec.template")

        SimpleTemplateEngine()
            .createTemplate(templateFile)
            .make(templateMap)
            .writeTo(podspecFile.writer())

        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "init"),
            exceptionMessage = "Unable to create the git repository"
        )

        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "branch", "-m", "main"),
            exceptionMessage = "Unable to rename to main branch"
        )

        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "add", "."),
            exceptionMessage = "Unable to add changes on main"
        )

        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "commit", "-m", "\"First Commit\""),
            exceptionMessage = "Unable to commit changes on main"
        )

        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "checkout", "-b", "develop"),
            exceptionMessage = "Unable to create the develop branch"
        )

        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "remote", "add", "origin", conf.cocoaPodRepoInfo.gitUrl.get()),
            exceptionMessage = "Unable to push on remote repository"
        )

        val branchName = project.retrieveMainBranchName(conf.outputPath.get())
        project.execBashCommandThrowExecException(
            output = parentFile,
            commandList = listOf("git", "push", "origin", "develop", branchName),
            exceptionMessage = "Unable to push on remote repository"
        )
    }

    internal companion object {
        const val NAME = "generateCocoaPodRepo"
    }
}

internal fun Project.registerGenerateCocoaPodRepositoryTask(
    pluginConfig: PluginConfig,
) {
    tasks.register(
        GenerateCocoaPodRepositoryTask.NAME,
        GenerateCocoaPodRepositoryTask::class.java,
        pluginConfig,
    )
}