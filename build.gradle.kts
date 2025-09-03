import org.gradle.kotlin.dsl.provideDelegate

/* ------------------------------ Plugins ------------------------------ */
plugins {
    id("java") // Import Java plugin.
    id("java-library") // Import Java Library plugin.
    id("com.diffplug.spotless") version "7.0.4" // Import Spotless plugin.
    id("com.gradleup.shadow") version "8.3.6" // Import Shadow plugin.
    id("checkstyle") // Import Checkstyle plugin.
    eclipse // Import Eclipse plugin.
    kotlin("jvm") version "2.1.21" // Import Kotlin JVM plugin.
}

extra["kotlinAttribute"] = Attribute.of("kotlin-tag", Boolean::class.javaObjectType)

val kotlinAttribute: Attribute<Boolean> by rootProject.extra

/* --------------------------- JDK / Kotlin ---------------------------- */
java {
    sourceCompatibility = JavaVersion.VERSION_17 // Compile with JDK 17 compatibility.
    toolchain { // Select Java toolchain.
        languageVersion.set(JavaLanguageVersion.of(17)) // Use JDK 17.
        vendor.set(JvmVendorSpec.GRAAL_VM) // Use GraalVM CE.
    }
}

kotlin { jvmToolchain(17) }

/* ----------------------------- Metadata ------------------------------ */
group = "fr.neatmonster" // Declare bundle identifier.

version = "3.17.1-SNAPSHOT" // Declare plugin version (will be in .jar).

val apiVersion = "1.19" // Declare minecraft server target version.

/* ----------------------------- Resources ----------------------------- */
tasks.named<ProcessResources>("processResources") {
    val props =
        mapOf(
            "version" to version.toString(),
            "apiVersion" to apiVersion,
            "project" to mapOf("groupId" to group.toString(), "artifactId" to "ncpplugin", "name" to "NCPPlugin"),
        )
    inputs.properties(props) // Indicates to rerun if version changes.
    from("NCPPlugin/src/main/resources") {
        include("plugin.yml")
        expand(props)
    }
    from("LICENSE.txt") { into("/") } // Bundle licenses into jarfiles.
}

/* ---------------------------- Repos ---------------------------------- */
repositories {
    mavenCentral() // Import the Maven Central Maven Repository.
    gradlePluginPortal() // Import the Gradle Plugin Portal Maven Repository.
    maven { url = uri("https://repo.purpurmc.org/snapshots") } // Import the PurpurMC Maven Repository.
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots") }
    maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
    maven { url = uri("https://repo.loohpjames.com/repository/") }
    maven { url = uri("file://${System.getProperty("user.home")}/.m2/repository") }
    System.getProperty("SELF_MAVEN_LOCAL_REPO")?.let { // TrueOG Bootstrap mavenLocal().
        val dir = file(it)
        if (dir.isDirectory) {
            println("Using SELF_MAVEN_LOCAL_REPO at: $it")
            maven { url = uri("file://${dir.absolutePath}") }
        } else {
            mavenLocal()
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://repo.purpurmc.org/snapshots") }
        maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots") }
        maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
        maven { url = uri("https://repo.loohpjames.com/repository/") }
        maven { url = uri("file://${System.getProperty("user.home")}/.m2/repository") }
        System.getProperty("SELF_MAVEN_LOCAL_REPO")?.let {
            val dir = file(it)
            if (dir.isDirectory) {
                println("Using SELF_MAVEN_LOCAL_REPO at: $it")
                maven { url = uri("file://${dir.absolutePath}") }
            } else {
                mavenLocal()
            }
        }
    }
}

/* ---------------------- Java project deps ---------------------------- */
dependencies { implementation(project(":NCPPlugin")) }

apply(from = "eclipse.gradle.kts") // Import eclipse classpath support script.

/* ---------------------- Reproducible jars ---------------------------- */
tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible .jars
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

/* ----------------------------- Shadow -------------------------------- */
tasks.shadowJar {
    archiveFileName.set("NoCheatPlus-${version}.jar")
    archiveClassifier.set("") // Use empty string instead of null.
    minimize()
}

tasks.jar { archiveClassifier.set("part") } // Applies to root jarfile only.

tasks.build { dependsOn(tasks.spotlessApply, tasks.shadowJar) } // Build depends on spotless and shadow.

/* --------------------------- Javac opts ------------------------------- */
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters") // Enable reflection for java code.
    options.isFork = true // Run javac in its own process.
    options.compilerArgs.add("-Xlint:deprecation") // Trigger deprecation warning messages.
    options.encoding = "UTF-8" // Use UTF-8 file encoding.
}

/* ----------------------------- Auto Formatting ------------------------ */
spotless {
    java {
        eclipse().configFile("config/formatter/eclipse-java-formatter.xml") // Eclipse java formatting.
        leadingTabsToSpaces() // Convert leftover leading tabs to spaces.
        removeUnusedImports() // Remove imports that aren't being called.
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) } // JetBrains Kotlin formatting.
        target("build.gradle.kts", "settings.gradle.kts") // Gradle files to format.
    }
}

checkstyle {
    toolVersion = "10.18.1" // Declare checkstyle version to use.
    configFile = file("config/checkstyle/checkstyle.xml") // Point checkstyle to config file.
    isIgnoreFailures = true // Don't fail the build if checkstyle does not pass.
    isShowViolations = true // Show the violations in any IDE with the checkstyle plugin.
}

tasks.named("compileJava") {
    dependsOn("spotlessApply") // Run spotless before compiling with the JDK.
}

tasks.named("spotlessCheck") {
    dependsOn("spotlessApply") // Run spotless before checking if spotless ran.
}

/* ------------------------------ Eclipse SHIM ------------------------- */

// This can't be put in eclipse.gradle.kts because Gradle is weird.
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "eclipse")
    eclipse.project.name = "${project.name}-${rootProject.name}"
    tasks.withType<Jar>().configureEach { archiveBaseName.set("${project.name}-${rootProject.name}") }
    java {
        sourceCompatibility = JavaVersion.VERSION_17 // Compile with JDK 17 compatibility.
        toolchain { // Select Java toolchain.
            languageVersion.set(JavaLanguageVersion.of(17)) // Use JDK 17.
            vendor.set(JvmVendorSpec.GRAAL_VM) // Use GraalVM CE.
        }
    }
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-parameters") // Enable reflection for java code.
        options.isFork = true // Run javac in its own process.
        options.compilerArgs.add("-Xlint:deprecation") // Trigger deprecation warning messages.
        options.encoding = "UTF-8" // Use UTF-8 file encoding.
    }
}

/* ------------------------------ Modules ------------------------------ */

project(":NCPBuildBase") { dependencies { testImplementation("junit:junit:4.8.2") } }

project(":NCPCommons") {
    dependencies {
        api(project(":NCPBuildBase"))
        testImplementation("junit:junit:4.8.2")
    }
}

project(":NCPCore") {
    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT") // Declare Purpur/Spigot API version to be packaged.
        api(project(":NCPCommons"))
        testImplementation("junit:junit:4.12")
        testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    }
}

project(":NCPCompatBukkit") {
    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
        compileOnly(project(":NCPCore"))
        compileOnly(project(":NCPCommons"))
    }
}

project(":NCPCompatProtocolLib") {
    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
        compileOnly(project(":NCPCore"))
        compileOnly(project(":NCPCommons"))
        compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    }
}

project(":NCPPlugin") {
    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
        implementation(project(":NCPCore"))
        implementation(project(":NCPCompatBukkit"))
        implementation(project(":NCPCompatProtocolLib"))
        testImplementation("junit:junit:4.8.2")
    }
    tasks.named<ProcessResources>("processResources") { exclude("plugin.yml") }
}
