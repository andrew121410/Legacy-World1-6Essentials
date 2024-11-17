import java.text.SimpleDateFormat
import java.util.*

plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.11" // https://plugins.gradle.org/plugin/io.freefair.lombok
    id("com.gradleup.shadow") version "8.3.5" // https://github.com/GradleUp/shadow
    id("net.kyori.blossom") version "2.1.0" // https://github.com/KyoriPowered/blossom
    id("xyz.wagyourtail.jvmdowngrader") version "1.2.1" // https://github.com/unimined/JvmDowngrader https://plugins.gradle.org/plugin/xyz.wagyourtail.jvmdowngrader
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/nms/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.github.andrew121410:CCUtilsJava:20b05043ca")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")

    // Paper goes first then CraftBukkit
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn("ourShadeDowngradedApi")
        dependsOn("processResources")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("Shadow-Legacy-World1-6Essentials.jar")

        relocate("org.bstats", "com.andrew121410.mc.world16essentials.bstats")
    }

    // Downgrade the jar to Java 8
    val ourDowngradeJar by creating(xyz.wagyourtail.jvmdg.gradle.task.DowngradeJar::class) {
        dependsOn(shadowJar)
        inputFile = shadowJar.get().archiveFile.get().asFile
        downgradeTo = JavaVersion.VERSION_1_8
        archiveClassifier.set("downgraded-8")
    }
    val ourShadeDowngradedApi by creating(xyz.wagyourtail.jvmdg.gradle.task.ShadeJar::class) {
        dependsOn(ourDowngradeJar)
        inputFile = ourDowngradeJar.archiveFile.get().asFile

        // Delete the downgraded-8 jar and the original jar.
        doLast {
            ourDowngradeJar.archiveFile.get().asFile.delete()
            shadowJar.get().archiveFile.get().asFile.delete()
        }

        archiveFileName.set("Legacy-World1-6Essentials.jar")
    }
}

var formattedDate: String = SimpleDateFormat("M/d/yyyy").format(Date())
sourceSets {
    main {
        blossom {
            javaSources {
                property("date_of_build", formattedDate)
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifact(tasks.named("ourShadeDowngradedApi"))
        }
    }
}
