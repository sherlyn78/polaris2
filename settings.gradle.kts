pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox" // SIEMPRE se deja así
                password = "pk.eyJ1Ijoic2hlcmx5bjU2IiwiYSI6ImNtaGkxYTA2ZDB3czEya244YjRocWhwYXgifQ.ohRAdZncx0fgA7toqLc-JQ" // tu token personal de Mapbox
            }
        }
    }
}

rootProject.name = "Polaris Guard"
include(":app")
