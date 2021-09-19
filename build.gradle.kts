plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("org.jetbrains.kotlin.kapt") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.5.4"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.21"
}

version = "0.1"
group = "io.yurick"
val kotlinVersion = project.properties["kotlinVersion"]
val micronautVersion = project.properties["micronautVersion"]

repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.yurick.*")
    }
}

dependencies {
    implementation("io.micronaut:micronaut-http-client:${micronautVersion}")
    implementation("io.micronaut:micronaut-runtime:${micronautVersion}")
    implementation("io.micronaut:micronaut-validation:${micronautVersion}")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime:2.3.1")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions:2.3.1")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.1")

    kapt("io.micronaut:micronaut-inject-java:${micronautVersion}")
    kaptTest("io.micronaut:micronaut-inject-java:${micronautVersion}")

    runtimeOnly("ch.qos.logback:logback-classic:1.2.5")
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
