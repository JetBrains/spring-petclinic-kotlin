rootProject.name = "spring-petclinic-kotlin"

pluginManagement {
    val kotlin_repo_url: String? by settings
	repositories {
		maven { url = uri("https://repo.spring.io/snapshot") }
		maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
        kotlin_repo_url?.also { maven(it) }
	}

    val kotlin_version: String? by settings
    plugins {
        kotlin("jvm") version (kotlin_version ?: "2.2.0")
        kotlin("plugin.spring") version (kotlin_version ?: "2.2.0")
    }
}
