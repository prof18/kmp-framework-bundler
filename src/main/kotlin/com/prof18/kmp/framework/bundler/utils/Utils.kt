package com.prof18.kmp.framework.bundler.utils

import org.gradle.api.Project
import org.gradle.process.internal.ExecException
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import java.io.ByteArrayOutputStream
import java.io.File

internal const val PLUGIN_TASKS_GROUP = "kmp-framework-bundler"

internal fun Framework.dsymFile(frameworkName: String): File {
    return File(outputFile.parent, "$frameworkName.framework.dSYM")
}

internal fun Project.executeBashCommand(showOutput: Boolean = true, workingDirPath: String, commandList: List<String>): String {
    return ByteArrayOutputStream().use { outputStream ->
        project.exec {
            workingDir = File(workingDirPath)
            commandLine(commandList)
            standardOutput = outputStream
        }
        val output = outputStream.toString()
        if (showOutput) {
            print(output)
        }
        return@use output
    }
}

internal fun Project.executeBashCommand(showOutput: Boolean = true, workingDirFile: File, commandList: List<String>): String {
    return ByteArrayOutputStream().use { outputStream ->
        project.exec {
            workingDir = workingDirFile
            commandLine(commandList)
            standardOutput = outputStream
        }
        val output = outputStream.toString()
        if (showOutput) {
            print(output)
        }
        return@use output
    }
}

internal fun Project.execBashCommandInRepoAndThrowExecException(commandList: List<String>, workingDirPath: String, exceptionMessage: String) {
    try {
        executeBashCommand(
            workingDirPath = workingDirPath,
            commandList = commandList
        )
    } catch (e: ExecException) {
        println(e)
        e.printStackTrace()
        throw  ExecException(exceptionMessage)
    }
}

internal fun Project.execBashCommandThrowExecException(output: File, commandList: List<String>, exceptionMessage: String) {
    try {
        executeBashCommand(
            workingDirFile = output,
            commandList = commandList
        )
    } catch (e: ExecException) {
        throw  ExecException(exceptionMessage)
    }
}

internal fun Project.retrieveMainBranchName(outputPath: String): String {
    var branchName = ""
    try {
        val checkMainOutput = executeBashCommand(
            showOutput = false,
            workingDirPath = outputPath,
            commandList = listOf("git", "branch", "--list", "main")
        )
        if (checkMainOutput.contains("main")) {
            branchName = "main"
        }

        println(branchName)

        if (branchName.isEmpty()) {
            val checkMasterOutput = executeBashCommand(
                showOutput = false,
                workingDirPath = outputPath,
                commandList = listOf("git", "branch", "--list", "master")
            )
            if (checkMasterOutput.contains("master")) {
                branchName = "master"
            }
        }
    } catch (e: ExecException) {
        throw ExecException("Error while checking if the main or master branch exists. Are you sure it does exists?")
    }
    return branchName
}