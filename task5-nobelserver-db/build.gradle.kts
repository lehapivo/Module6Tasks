plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "com.example.nobelserver"
version = "1.0.0"

application {
    mainClass.set("com.example.nobelserver.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core-jvm:2.3.12")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.12")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.12")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.12")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.12")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.12")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.12")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.12")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.55.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.55.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.55.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.55.0")

    // PostgreSQL driver
    implementation("org.postgresql:postgresql:42.7.4")

    // HikariCP connection pool
    implementation("com.zaxxer:HikariCP:6.0.0")

    // JWT
    implementation("com.auth0:java-jwt:4.4.0")

    // BCrypt password hashing
    implementation("at.favre.lib:bcrypt:0.10.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.6")

    // Test
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.0")
}
