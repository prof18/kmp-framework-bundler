import java.util.*

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlinx:binary-compatibility-validator:0.10.0")
    }
}

apply(plugin = "binary-compatibility-validator")

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    signing
}

val kotlinVersion: String by project
val pluginVersion: String by project

val group = "com.prof18.kmp.framework.bundler"

version = pluginVersion

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")

    testImplementation("junit:junit:4.13.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }

    withJavadocJar()
    withSourcesJar()
}


tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

tasks.withType<Test> {
    useJUnit()
    dependsOn("publishToMavenLocal")

    systemProperty("defaultKotlinVersion", kotlinVersion)
    systemProperty("kotlinVersion", kotlinVersion)
    systemProperty("pluginVersion", pluginVersion)
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

gradlePlugin {
    plugins {
        create("KMPFrameworkBundler") {
            id = group
            implementationClass = "com.prof18.kmp.framework.bundler.KMPFrameworkBundlerPlugin"
            version = pluginVersion
        }
    }
}

val local = Properties()
val localProperties: File = rootProject.file("local.properties")
if (localProperties.exists()) {
    localProperties.inputStream().use { local.load(it) }
    local.forEach { name, value ->
        ext {
            set(name as String, value)
        }
    }
} else {
    ext {
        set("signing.keyId", System.getenv("SIGNING_KEY_ID"))
        set("signing.password", System.getenv("SIGNING_PASSWORD"))
        set("signing.key", System.getenv("SIGNING_KEY"))
        set("ossrhUsername", System.getenv("OSSRH_USERNAME"))
        set("ossrhPassword", System.getenv("OSSRH_PASSWORD"))
    }
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            pom {
                groupId = group
                artifactId = "com.prof18.kmp.framework.bundler.gradle.plugin"

                name.set("KMP Framework Bundler")
                description.set("Gradle plugin to manage the distribution via CocoaPod of a Kotlin Multiplatform library for Apple platforms with a XCFramework or a FatFramework")
                url.set("https://github.com/prof18/kmp-framework-bundler")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("prof18")
                        name.set("Marco Gomiero")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/prof18/kmp-framework-bundler")
                    developerConnection.set("scm:git:ssh://git@github.com/prof18/kmp-framework-bundler.git")
                    url.set("https://github.com/prof18/kmp-framework-bundler")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
            name = "sonatype"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl)
            credentials {
                username = local.getProperty("ossrhUsername") ?: System.getenv("OSSRH_USERNAME")
                password = local.getProperty("ossrhPassword") ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.ext["signing.keyId"] as? String,
        rootProject.ext["signing.key"] as? String,
        rootProject.ext["signing.password"] as? String,
    )

    sign(publishing.publications["pluginMaven"])
}
