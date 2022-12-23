plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "party.morino"
version = "0.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")

    // Paper
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Paper
    implementation("io.papermc", "paperlib", "1.0.7")
    compileOnly("io.papermc.paper", "paper-api", "1.19.3-R0.1-SNAPSHOT")

    // Command
    implementation("cloud.commandframework", "cloud-paper", "1.8.0")

    // Config
    implementation("org.spongepowered", "configurate-hocon", "4.1.2")
    implementation("net.kyori", "adventure-serializer-configurate4", "4.12.0")

    // Event
    //implementation("net.kyori", "event-api","5.0.0-SNAPSHOT")

    // Utils
    implementation("com.google.inject", "guice", "5.1.0")

    // jars
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

bukkit {
    name = rootProject.name
    version = project.version as String
    main = "party.morino.carbonjapanaizer.CarbonJapanizer"
    apiVersion = "1.19"
    description = "Carbonローマ字変換アドオン"
    author = "Unitarou"
    website = "https://morino.party"
    depend = listOf("CarbonChat")
}

tasks {
    shadowJar {
        relocate("io.papermc.lib", "party.morino.carbonjapanizer.paperlib")
        this.archiveClassifier.set(null as String?)

    }
    runServer {
        minecraftVersion("1.19.3")
    }
}
