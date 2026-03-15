buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("java")
}

group = "com.dth.geneticcar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "java")

    extra["appName"] = "Genetic Car"
    extra["gdxVersion"] = "1.0.0"

    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/repositories/releases/")
    }
}
