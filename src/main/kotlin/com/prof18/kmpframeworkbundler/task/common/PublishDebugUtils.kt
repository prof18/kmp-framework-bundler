package com.prof18.kmpframeworkbundler.task.common

import com.prof18.kmpframeworkbundler.data.PluginConfig
import com.prof18.kmpframeworkbundler.utils.execBashCommandInRepoAndThrowExecException
import org.gradle.api.Project
import org.gradle.process.internal.ExecException
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

internal fun Project.publishDebugFramework(config: PluginConfig) {
    val podSpecFile = config.getPodSpecFile()
    if (!podSpecFile.exists()) {
        throw ExecException("podspec file does not exists!")
    }

    val tempPodSpecFile = File("${config.outputPath.get()}/${config.frameworkName.get()}.podspec.new")

    val reader = podSpecFile.bufferedReader()
    val writer = tempPodSpecFile.bufferedWriter()
    var currentLine: String?

    try {
        while (reader.readLine().also { currLine -> currentLine = currLine } != null) {
            if (currentLine?.startsWith("s.version") == true) {
                writer.write("s.version       = \"${config.versionName.get()}\"" + System.lineSeparator())
            } else {
                writer.write(currentLine + System.lineSeparator())
            }
        }
    } catch (e: IOException) {
        throw ExecException("Unable to update the version on the podspec file")
    } finally {
        writer.close()
        reader.close()
    }

    val renameSuccessful = tempPodSpecFile.renameTo(podSpecFile)
    if (renameSuccessful) {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())

        execBashCommandInRepoAndThrowExecException(
            commandList = listOf("git", "add", "."),
            workingDirPath = config.outputPath.get(),
            exceptionMessage = "Unable to add the files"
        )

        execBashCommandInRepoAndThrowExecException(
            commandList = listOf(
                "git",
                "commit",
                "-m",
                "\"New debug release: ${config.versionName.get()} - ${dateFormatter.format(Date())}\""
            ),
            workingDirPath = config.outputPath.get(),
            exceptionMessage = "Unable to commit the changes"
        )

        execBashCommandInRepoAndThrowExecException(
            commandList = listOf("git", "push", "origin", "develop"),
            workingDirPath = config.outputPath.get(),
            exceptionMessage = "Unable to push the changes to remote"
        )
    }
}
