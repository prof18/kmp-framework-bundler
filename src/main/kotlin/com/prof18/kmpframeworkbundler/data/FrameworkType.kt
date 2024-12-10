package com.prof18.kmpframeworkbundler.data

enum class FrameworkType {
    /**
     * Build a FatFramework instead of an XCFramework
     */
    FAT_FRAMEWORK,

    /**
     * This will use the official Gradle task provided by the Kotlin Team to build an XCFramework.
     * More info [here](https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks)
     */
    XC_FRAMEWORK,
}

internal fun FrameworkType.isXCFramework(): Boolean {
    return this != FrameworkType.FAT_FRAMEWORK
}