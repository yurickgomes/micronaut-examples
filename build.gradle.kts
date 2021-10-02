plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("org.jetbrains.kotlin.kapt") version "1.5.31"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.micronaut.application") version "2.0.6"
}

group = "io.yurick"

val kotlinVersion = project.properties["kotlinVersion"]
val micronautVersion = project.properties["micronautVersion"]

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            when (requested.name) {
                "kotlin-stdlib-jdk7" -> useVersion("$kotlinVersion")
                "kotlin-stdlib-jdk8" -> useVersion("$kotlinVersion")
                "kotlin-reflect" -> useVersion("$kotlinVersion")
                "kotlin-stdlib-common" -> useVersion("$kotlinVersion")
            }
        }
    }
}

micronaut {
    version("$micronautVersion")
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.yurick.*")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")

    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-management")

    kapt("io.micronaut:micronaut-inject-java")
    kaptTest("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")

    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client")

    implementation("io.micronaut.aws:micronaut-aws-sdk-v2")
    implementation("software.amazon.awssdk:dynamodb:2.17.46") {
        exclude(group = "software.amazon.awssdk", module = "apache-client")
    }
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.17.46")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

    runtimeOnly("ch.qos.logback:logback-classic:1.2.6")
}

application {
    mainClass.set("io.yurick.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }
}
