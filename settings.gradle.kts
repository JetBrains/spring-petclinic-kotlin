rootProject.name = "spring-petclinic-kotlin"

pluginManagement {
    val kotlin_repo_url = providers.gradleProperty("kotlin_repo_url").orElse("N/A")
    val kotlin_version = providers.gradleProperty("kotlin_version").orElse("N/A")

    repositories {
		maven { url = uri("https://repo.spring.io/snapshot") }
		maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
        if (kotlin_repo_url.get().isNotEmpty() && kotlin_version.get().isNotEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(kotlin_repo_url)
                }
                filter {
                    includeVersionByRegex("org.jetbrains.kotlin.*", ".*", kotlin_version.get())
                }
            }
        }
	}

    plugins {
        kotlin("jvm") version kotlin_version
        kotlin("plugin.spring") version kotlin_version
    }
}
