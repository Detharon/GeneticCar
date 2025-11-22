plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

group = "com.dth.genetic"
version = "1.0-SNAPSHOT"

extra["appName"] = "Genetic Car"
extra["assetsDir"] = File("../core/assets")
extra["mainClassName"] = "com.dth.geneticcar.desktop.DesktopGame"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation("org.jfree:jfreechart:1.0.19")

    val gdxVersion: String by rootProject.extra
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:$gdxVersion")

    // TODO: Replace them with modern alternatives from Maven
    implementation(files("libs/miglayout15-swing.jar"))
}

tasks.register<JavaExec>("run") {
    dependsOn("classes")
    mainClass = project.property("mainClassName") as String
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = project.property("assetsDir") as File
    isIgnoreExitValue = true
}

tasks.register<Jar>("dist") {
    from(sourceSets["main"].output.classesDirs)
    from(sourceSets["main"].output.resourcesDir)

    from({
        configurations.runtimeClasspath.get().map { zipTree(it) }
    }) {
        // exclude signature files that cause SecurityException
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
        // TODO: This should not be necessary, should be fixed after dependencies are done properly
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    from(project.property("assetsDir") as File)
    manifest {
        attributes["Main-Class"] = project.property("mainClassName") as String
    }
}
