import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    api(project(":domain-model"))
    testImplementation(project(":data-rds"))
    testImplementation(project(":data-redis"))
    testImplementation(project(":test-support"))
    testImplementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
