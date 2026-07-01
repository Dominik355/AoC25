import net.ltgt.gradle.errorprone.errorprone

plugins {
    application
    id("net.ltgt.errorprone") version "5.1.0"
}

group = "eu.bilik.aoc25"
version = "2025"

repositories {
    mavenCentral()
}

dependencies {
    // 2.41.0 is the latest Error Prone compatible with NullAway 0.12.7
    // (2.50.0 removed predicates.type.DescendantOf, which NullAway still references).
    errorprone("com.google.errorprone:error_prone_core:2.41.0")
    errorprone("com.uber.nullaway:nullaway:0.12.7")
    compileOnly("org.jspecify:jspecify:1.0.0")
    implementation("io.vavr:vavr:1.0.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

// ----------- Error Prone + NullAway -----------
// Run static analysis on every Java compilation.
tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        // NullAway only checks the packages we declare as annotated.
        check("NullAway", net.ltgt.gradle.errorprone.CheckSeverity.ERROR)
        option("NullAway:AnnotatedPackages", "eu.bilik.aoc25")
    }
}

// The application plugin needs a default main class, point it at day01.
application {
    mainClass = "eu.bilik.aoc25.day01.Solution"
}

// ----------- Generate a run task per day -----------
// Scans for `dayNN` packages and registers a JavaExec task named after each (e.g. `day01`).
val pkgRoot = layout.projectDirectory.dir("src/main/java/eu/bilik/aoc25").asFile
val dayPattern = Regex("""day\d{2}""")

val days = (pkgRoot.listFiles() ?: emptyArray())
    .filter { it.isDirectory && dayPattern.matches(it.name) }
    .map { it.name }
    .sorted()

days.forEach { day ->
    tasks.register<JavaExec>(day) {
        group = "aoc"
        description = "Run $day (eu.bilik.aoc25.$day.Solution)"
        classpath = sourceSets["main"].runtimeClasspath
        mainClass = "eu.bilik.aoc25.$day.Solution"
        // Run from the project root so `inputs/dayNN.txt` resolves.
        workingDir = layout.projectDirectory.asFile
        standardInput = System.`in`
    }
}
