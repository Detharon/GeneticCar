buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.robovm:robovm-gradle-plugin:1.0.0-beta-03")
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

    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/repositories/releases/")
    }
}
