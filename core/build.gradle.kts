plugins {
    java
}

group = "com.dth.genetic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Replace them with modern alternatives from Maven
    implementation(files("libs/gdx.jar"))
    implementation(files("libs/gdx-sources.jar"))
    implementation(files("libs/forms-1.3.0.jar"))
}
