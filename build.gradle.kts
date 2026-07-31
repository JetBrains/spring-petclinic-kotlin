import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

description = "Kotlin version of the Spring Petclinic application"
group = "org.springframework.samples"
// Align with Spring Version
version = "4.0.2"

plugins {
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.cloud.tools.jib") version "3.5.4"
    kotlin("jvm")
    kotlin("plugin.spring")
}

val boostrapVersion = "5.3.8"
val fontAwesomeVersion = "4.7.0"
val webjarsLocatorLiteVersion = "1.1.4"
val kotlin_repo_url = providers.gradleProperty("kotlin_repo_url").orElse("N/A")
val kotlin_version = providers.gradleProperty("kotlin_version").orElse("N/A")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xreport-all-warnings",
            "-Xrender-internal-diagnostic-names",
            "-Xuse-fir-experimental-checkers"
            )
        extraWarnings.set(true)
        allWarningsAsErrors.set(false)
        project.findProperty("kotlin_additional_cli_options")?.let {
            freeCompilerArgs.addAll((it as String).trim().split(" ").toList())
        }
        getOverriddenKotlinLanguageVersion(project)?.also { languageVersion.set(KotlinVersion.fromVersion(it)) }
        getOverriddenKotlinApiVersion(project)?.also { apiVersion.set(KotlinVersion.fromVersion(it)) }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
        if (kotlin_repo_url.get().isNotEmpty() && kotlin_version.get().isNotEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(kotlin_repo_url)
                }
                filter {
                    includeVersionByRegex("org.jetbrains.kotlin", ".*", kotlin_version.get())
                }
            }
        }
    }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.glassfish.jaxb:jaxb-runtime")
    implementation("javax.cache:cache-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.webjars.npm:bootstrap:$boostrapVersion")
    implementation("org.webjars.npm:font-awesome:$fontAwesomeVersion")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.boot:spring-boot-starter-webflux")
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

        runtimeOnly("com.h2database:h2")
        runtimeOnly("com.mysql:mysql-connector-j")
        runtimeOnly("org.webjars:webjars-locator-lite:${webjarsLocatorLiteVersion}")

        developmentOnly("org.springframework.boot:spring-boot-devtools")
    }

    jib {
        to {
            image = "springcommunity/spring-petclinic-kotlin"
            tags = setOf(project.version.toString(), "latest")
        }
    }
}

