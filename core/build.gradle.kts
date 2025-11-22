plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

group = "com.dth.genetic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val gdxVersion: String by rootProject.extra
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
}
