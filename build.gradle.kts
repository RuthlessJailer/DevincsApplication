import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	java
	maven
	kotlin("jvm") version "1.4.21"
	id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.ruthlessjailer.plugin.devincsapplication"
version = "1.0.0"

val finalName = "${project.name}.jar"
val copyDir = "D:/Gaming/Minecraft/Server/paper 1.16/plugins"
val mainClass = "$group.DevincsApplication"
val javaVersion = "1.11"

repositories {
	mavenCentral()
	mavenLocal()
	jcenter()
	maven {
		name = "spigotmc-repo"
		url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

		content {
			includeGroup("org.spigotmc")
		}
	}

	maven {
		name = "codemc-repo"
		url = uri("https://repo.codemc.org/repository/maven-public/")
	}

	maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
	maven { url = uri("https://oss.sonatype.org/content/repositories/central") }
	maven { url = uri("https://jitpack.io") }

}

dependencies {
//	api(group = "com.nesaak", name = "NoReflection", version = "0.1-SNAPSHOT")
	api(group = "com.ruthlessjailer.api.theseus", name = "Theseus", version = "1.2.4")
	api(group = "com.ruthlessjailer.api.poseidon", name = "Poseidon", version = "1.0.0")
//	api(group = "com.google.code.gson", name = "gson", version = "2.3.1")//1.8.8 released july 27 2015, latest release of gson before then is 2.3.1
	compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
	compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
	compileOnly("org.apache.commons:commons-lang3:3.11")
	compileOnly("commons-io:commons-io:2.8.0")
	compileOnly("com.github.MilkBowl:VaultAPI:1.5.6")
	compileOnly("mysql:mysql-connector-java:8.0.22")
//	compileOnly("com.plotsquared:PlotSquared-Bukkit:5.13.3") // PlotSquared Bukkit API
	implementation(kotlin("stdlib-jdk8"))
//	implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
}

tasks {
	named<ShadowJar>("shadowJar") {
		archiveFileName.set(finalName)
		mergeServiceFiles()
		minimize()
		manifest {
			attributes(mapOf("Main-Class" to mainClass))
		}
	}

	register<Copy>("copyReport") {
		from(file("$buildDir/libs/$finalName"))
		into(file(copyDir))
	}

	build {
		dependsOn(getByName("shadowJar"))
	}

	processResources {
		expand("version" to project.version, "name" to project.name, "mainClass" to mainClass, "price" to "\${price}")
	}

	jar {
		archiveFileName.set("${project.name}-${project.version}-unshaded.jar")
	}

	compileKotlin {
		targetCompatibility = javaVersion
	}

}