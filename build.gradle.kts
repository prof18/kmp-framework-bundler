plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    alias(libs.plugins.com.vanniktech.maven.publish)
    alias(libs.plugins.binary.compatibility.validator)
}

val VERSION_NAME: String by project

val group = "com.prof18.kmp-framework-bundler"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kotlin.compiler.embeddable)

    testImplementation(libs.junit)
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

    val kotlinVersion = libs.versions.kotlin.get()
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
