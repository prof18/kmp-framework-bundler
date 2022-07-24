# KMP Framework Bundler

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.prof18.kmp-framework-bundler/kmp-framework-bundler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.prof18.kmp-framework-bundler/kmp-framework-bundler/badge.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


**KMP Framework Bundler** is a Gradle plugin for Kotlin Multiplatform projects that generate a **XCFramework** for Apple targets or a **FatFramework** for iOS targets, and manages the publishing process in a CocoaPod Repository.

This plugin is a rewrite of the archived [KMP FatFramework Cocoa](https://github.com/prof18/kmp-fatframework-cocoa). There is a migration guide [below](#migration-from-kmp-fatframework-cocoa).

## Setup

The library is uploaded on MavenCentral, so you can easily add the dependency on the `plugins` block:

```kotlin
plugins {
    id("com.prof18.kmp-framework-bundler") version "<latest-version>"
}
```

You can configure the plugin with the `frameworkBundlerConfig` block in your `build.gradle[.kts]`.

The required fields are the following:

- the name of the framework
- the output path
- the version name
- the framework type: 
  - XC_FRAMEWORK
  - XC_FRAMEWORK_LEGACY_BUILD 
  - FAT_FRAMEWORK

```kotlin
frameworkBundlerConfig { 
  frameworkName.set("LibraryName")
  outputPath.set("$rootDir/../test-dest")
  versionName.set("1.0.0")
  frameworkType = FrameworkType.XC_FRAMEWORK
}
```

If the selected `frameworkType` is `XC_FRAMEWORK`, the official Gradle task provided by the Kotlin Team will be used to build the XCFramework. 
To use this official task, some setup is required:

```kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
}

kotlin {
    val xcf = XCFramework()

    ios {
        binaries.framework("LibraryName") {
            xcf.add(this)
        }
    }
    watchos {
        binaries.framework("LibraryName") {
            xcf.add(this)
        }
    }
    tvos {
        binaries.framework("LibraryName") {
            xcf.add(this)
        }
    }
    
    ...
}
```
Additional information is available [in the official Kotlin doc](https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks).

If the selected `frameworkType` is `XC_FRAMEWORK_LEGACY_BUILD`, a custom task will be used to build the XCFramework. This is required before Kotlin 1.5.30. 

If the selected `frameworkType` is `FAT_FRAMEWORK`, then a FatFramework will be built. When using a FatFramework, only iOS targets can be packed together. With XCFramework you can pack together all the Apple families: iOS, macOS, etc.

If you want to generate a CocoaPod repository, you have also to provide some parameters in the `cocoaPodRepoInfo` block:

- a summary of the library
- the homepage of the library
- the license of the library
- the authors of the library
- the URL of the git repository that hosts the CocoaPod repo: this field is mandatory

```kotlin
frameworkBundlerConfig {
    frameworkName.set("LibraryName")
    outputPath.set("$rootDir/../test-dest")
    versionName.set("1.0.0")
    frameworkType = XC_FRAMEWORK

    cocoaPodRepoInfo {
        summary.set("This is a test KMP framework")
        homepage.set("https://github.com/prof18/cocoa-repo-xcframework-test")
        license.set("Apache")
        authors.set("\"Marco Gomiero\" => \"mg@me.com\"")
        gitUrl.set("git@github.com:prof18/cocoa-repo-xcframework-test.git")
    }
}
```

## Usage

The plugin adds five Gradle tasks to your project. The tasks relative to XCFramework or FatFramework will be added based on the selected `frameworkType`, e.g. if you choose an XCFramework, then only tasks related to XCFrameworks will appear. 

- `buildDebugIosFatFramework` that creates a **FatFramework** with the `Debug` target.


- `buildDebugXCFramework` that creates a **XCFramework** with the `Debug` target.


- `buildReleaseIosFatFramework` that creates a **FatFramework** with the `Release` target.


- `buildReleaseXCFramework` that creates a **XCFramework** with the `Release` target.


- `generateCocoaPodRepo` that generates a CocoaPod repository ready to host the Framework.


- `publishDebugIosFatFramework` that publishes the `Debug` version of the **FatFramework** in the CocoaPod repository.


- `publishDebugXCFramework` that publishes the `Debug` version of the **XCFramework** in the CocoaPod repository.
  <br><br>
  The "publishDebug" task (for both the type of frameworks) takes care of everything:
    - changing the working branch of the CocoaPod repo from main/master to develop
    - building the debug framework
    - updating the version name inside the Podspec file
    - committing the changes
    - and publishing to remote

  In this way, in the iOS project, you can use the latest changes published on the develop branch:

  ```ruby
  pod '<your-library-name>', :git => "git@github.com:<git-username>/<repo-name>.git", :branch => 'develop'
  ```
  To run this task, the `outputPath` provided in the `frameworkBundlerConfig` block must be a git repository.


- `publishReleaseIosFatFramework` that publishes the `Release` version of the **FatFramework** in the CocoaPod repository.


- `publishReleaseXCFramework` that publishes the `Release` version of the **XCFramework** in the CocoaPod repository.
  <br><br>
  The "publishRelease" task (for both the type of frameworks) takes care of everything:
    - changing the working branch of the CocoaPod repo from develop to main/master
    - building the release framework
    - updating the version name inside the Podspec file
    - committing the changes
    - tagging the commit
    - and publishing to remote

  In this way, in the iOS project, you have to specify a version:

  ```ruby
  pod '<your-library-name>', :git => "git@github.com:<git-username>/<repo-name>.git", :tag => '<version-number>'
  ```

   To run this task, the `outputPath` provided in the `frameworkBundlerConfig` block must be a git repository.

## Sample Project

To see the plugin in action, I've published [a little sample project](https://github.com/prof18/kmp-framework-bundler-test-project). This project will publish a FatFramework to: https://github.com/prof18/fat-framework-cocoa-repo-test and an XCFramework to https://github.com/prof18/xcframework-cocoa-repo-test/tree/main. 
The framework are imported in the [FrameworkTest](https://github.com/prof18/FrameworkTest) iOS project.

## Migration from KMP FatFramework Cocoa

If you were a user of the KMP FatFramework Cocoa plugin, you can easily migrate to the KMP Framework Bundler. Here are the required changes:

1. The plugin id changed from `com.prof18.kmp.fatframework.cocoa` to `com.prof18.kmp-framework-bundler`
2. The name of the configuration DSL changed from `fatFrameworkCocoaConfig` to `frameworkBundlerConfig`
3. The `useXCFramework` flag is deprecated in favour of the `frameworkType` selection

For reference, the following old configuration

```kotlin
fatFrameworkCocoaConfig {
    frameworkName = "LibraryName"
    outputPath = "$rootDir/../test-dest"
    versionName = "1.0"
    useXCFramework = true

    cocoaPodRepoInfo {
        summary = "This is a test KMP framework"
        homepage = "https://github.com/prof18/ccoca-repo-test"
        license = "Apache"
        authors = "\"Marco Gomiero\" => \"mg@mail.it\""
        gitUrl = "git@github.com:prof18/ccoca-repo-test.git"
    }
}
```

become:
 
```kotlin
frameworkBundlerConfig {
    frameworkName.set("LibraryName")
    outputPath.set("$rootDir/../test-dest")
    versionName.set("1.0.0")
    frameworkType = XC_FRAMEWORK

    cocoaPodRepoInfo {
        summary.set("This is a test KMP framework")
        homepage.set("https://github.com/prof18/cocoa-repo-xcframework-test")
        license.set("Apache")
        authors.set("\"Marco Gomiero\" => \"mg@me.com\"")
        gitUrl.set("git@github.com:prof18/cocoa-repo-xcframework-test.git")
    }
}
```


## Further Readings

This plugin is born from a set of unbundled Gradle tasks that I was copying between every Kotlin Multiplatform project.
I've written about these tasks in a couple of articles:

- [Introducing Kotlin Multiplatform in an existing project](https://www.marcogomiero.com/posts/2021/kmp-existing-project/)
- [Building an XCFramework on Kotlin Multiplatform from Kotlin 1.5.30](https://www.marcogomiero.com/posts/2021/kmp-existing-project/)

For more info about CocoaPod, I suggest reading the following resources:

- [https://guides.cocoapods.org/making/private-cocoapods.html](https://guides.cocoapods.org/making/private-cocoapods.html)
- [https://guides.cocoapods.org/making/private-cocoapods.html](https://guides.cocoapods.org/making/private-cocoapods.html)

## License

```
   Copyright 2022 Marco Gomiero

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
