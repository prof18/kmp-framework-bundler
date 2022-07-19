package com.prof18.kmpframeworkbundler.data

internal object ErrorMessages {
    const val FRAMEWORK_NAME_NOT_PRESENT_MESSAGE = "You must provide the property `frameworkName`"
    const val OUTPUT_PATH_NOT_PRESENT_MESSAGE = "You must provide the property `outputPath`"
    const val VERSION_NAME_NOT_PRESENT_MESSAGE = "You must provide the property `versionName`"
    const val EMPTY_XC_FRAMEWORK_TASKS = "The XCFramework is not setup correctly. Please follow the instructions reported here: https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks"
    const val USING_LEGACY_BUILD_SYSTEM = "You are still using the legacy XCFramework build system. Please set 'frameworkType' to `FrameworkType.XC_FRAMEWORK` to use the default one"
    const val XC_FRAMEWORK_DEBUG_TASK_NOT_FOUND = "The task to build the Debug XCFramework was not found. Please make sure that everything is setup correctly. https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks"
    const val XC_FRAMEWORK_RELEASE_TASK_NOT_FOUND = "The task to build the Release XCFramework was not found. Please make sure that everything is setup correctly. https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks"

    const val GIT_URL_NOT_PRESENT = "You must provide the property `gitUrl` inside the `cocoaPodRepoInfo` block"
}