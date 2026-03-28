import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.jpa") version "1.9.25"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    api(project(":domain-model"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}


