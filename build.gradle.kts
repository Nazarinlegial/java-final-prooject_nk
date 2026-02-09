plugins {
    id("java")
    application
    jacoco
}

group = "global.goit.java_final_n_kovalchuk"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("global.goit.java_final_n_kovalchuk.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.1")
    
    // OpenCSV for CSV processing
    implementation("com.opencsv:opencsv:5.9")
    
    // JUnit 5 for testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // Mockito for mocking
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "global.goit.java_final_n_kovalchuk.Main"
        )
    }
    // Include all dependencies in the JAR (fat jar)
    from(configurations.runtimeClasspath.get().map { zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
