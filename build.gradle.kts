buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlinx:binary-compatibility-validator:0.10.0")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.25.3")
    }
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

apply(plugin = "binary-compatibility-validator")
apply(plugin = "com.vanniktech.maven.publish")


val kotlinVersion: String by project
val VERSION_NAME: String by project

val group = "com.prof18.kmp-framework-bundler"

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
}


tasks.withType<JavaCompile>().configureEach {
    options.release.set(11)
}

tasks.withType<Test> {
    useJUnit()
    dependsOn("publishToMavenLocal")

    systemProperty("defaultKotlinVersion", kotlinVersion)
    systemProperty("kotlinVersion", kotlinVersion)
    systemProperty("pluginVersion", VERSION_NAME)
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

gradlePlugin {
    plugins {
        create("KMPFrameworkBundler") {
            id = group
            implementationClass = "com.prof18.kmpframeworkbundler.KMPFrameworkBundlerPlugin"
            version = VERSION_NAME
        }
    }
}
