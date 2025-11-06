pluginManagement {
    repositories {
        maven { url=uri("https://mirrors.huaweicloud.com/repository/maven/") }
        // 阿里云加速镜像
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter/") }

        // 官方源
//        mavenLocal()
//        google()
//        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        maven { url=uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        // 阿里云镜像
        maven { url = uri("https://maven.aliyun.com/repository/central/") }
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }

        // JitPack（可选）
        maven { url = uri("https://jitpack.io") }

        // 官方源
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "MineSweeper-Android-0.1"
include(":app")
