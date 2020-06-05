import org.gradle.jvm.tasks.Jar

plugins {
    application
    kotlin("jvm") version "1.3.72"
}

group = "pl.kpmarczynski.jsonplaceholderprocessor"
version = "1.0.0"

application {
    mainClassName = "$group.MainKt"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.ajalt:clikt:2.7.1")
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.github.kittinunf.fuel:fuel-json:2.2.1"){
        exclude("org.json","json")
    }

    implementation("org.json:json:20200518")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.github.kittinunf.fuel:fuel-test:2.0.1")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "pl.kpmarczynski.jsonplaceholderprocessor.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
