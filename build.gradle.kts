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

val fixtureClasspath by configurations.creating

// Append any extra dependencies to the test fixtures via a custom configuration classpath. This
// allows us to apply additional plugins in a fixture while still leveraging dependency resolution
// and de-duplication semantics.
tasks.pluginUnderTestMetadata {
    pluginClasspath.from(fixtureClasspath)
}

dependencies {
    compileOnly(gradleApi())

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")

    testImplementation("junit:junit:4.13.2")
    fixtureClasspath(kotlin("gradle-plugin"))

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
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
        create("frameworkBundler") {
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
        set("signing.secretKeyRingFile", System.getenv("SIGNING_SECRET_KEY_RING_FILE"))
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
                // TODO: change name
                description.set("Gradle plugin to distribute a Kotlin Multiplatform iOS library in a FatFramework with CocoaPod")
                url.set("https://github.com/prof18/kmp-fatframework-cocoa")

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

                    // TODO: update and change name
                    connection.set("scm:git:https://github.com/prof18/kmp-fatframework-cocoa")
                    developerConnection.set("scm:git:ssh://git@github.com/prof18/kmp-fatframework-cocoa.git")
                    url.set("https://github.com/prof18/kmp-fatframework-cocoa")
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

// TODO: enable signing before publishing
//signing {
//    sign(publishing.publications["pluginMaven"])
//}
