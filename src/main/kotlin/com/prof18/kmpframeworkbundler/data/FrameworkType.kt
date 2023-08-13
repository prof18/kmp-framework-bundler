package com.prof18.kmpframeworkbundler.data

enum class FrameworkType {
    /**
     * Build a FatFramework instead of an XCFramework
     */
    FAT_FRAMEWORK,

    /**
     * This will use a custom task to build the XCFramework. This needs to be used before Kotlin 1.5.30
     *
     * See [com.prof18.kmpframeworkbundler.task.xcframework.legacy.buildXCFramework]
     */
    @Deprecated(
        message = "Please use FrameworkType.XC_FRAMEWORK, that will trigger the official Gradle task. " +
                "This entry will be removed in a future library version",
        replaceWith = ReplaceWith(
            expression = "FrameworkType.XC_FRAMEWORK",
            "com.prof18.kmpframeworkbundler.data.XC_FRAMEWORK"
        )
    )
    XC_FRAMEWORK_LEGACY_BUILD,

    /**
     * This will use the official Gradle task provided by the Kotlin Team to build an XCFramework.
     * More info [here](https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks)
     */
    XC_FRAMEWORK,
}

internal fun FrameworkType.isXCFramework(): Boolean {
    return this != FrameworkType.FAT_FRAMEWORK
}