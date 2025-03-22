pluginManagement {

    repositories {

        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {

            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin/")
        }
        mavenLocal()
        mavenCentral()
    }
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

        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {

            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin/")
        }
        mavenLocal()
        mavenCentral()
    }
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
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MineSweeper-Android-0.1"
include(":app")
 