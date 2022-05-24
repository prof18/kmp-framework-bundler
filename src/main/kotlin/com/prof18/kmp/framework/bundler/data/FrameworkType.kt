package com.prof18.kmp.framework.bundler.data

enum class FrameworkType {
    /**
     * Build a FatFramework instead of an XCFramework
     */
    FAT_FRAMEWORK,

    /**
     * This will use a custom task to build the XCFramework. This needs to be used before Kotlin 1.5.30
     *
     * See [com.prof18.kmp.framework.bundler.task.xcframework.legacy.buildXCFramework]
     */
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