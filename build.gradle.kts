import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.20.1-R0.1-SNAPSHOT")

    // CarbonChat
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

version = "0.1"

paper {
    version = project.version as String
    main = "party.morino.carbonjapanizer.CarbonJapanizer"
    bootstrapper = "party.morino.carbonjapanizer.CarbonJapanizerBootstrapper"
    apiVersion = "1.20"
    generateLibrariesJson = true
    description = "Carbonローマ字変換アドオン"
    author = "Unitarou"
    website = "https://morino.party"
    serverDependencies {
        register("CarbonChat") {
            required = true
        }
    }
    permissions {
        register("carbonjapanizer.command.reload") {
            description = "Reloads CarbonJapanizer's config and translations."
            default = BukkitPluginDescription.Permission.Default.OP // TRUE, FALSE, OP or NOT_OP
        }

        register("carbonjapanizer.command.toggle") {
            description = "Toggles Romaji to Japanese conversion enable/disable"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks {
    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        this.options.release.set(17)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
    }

    runServer {
        minecraftVersion("1.20.1")
    }
}
