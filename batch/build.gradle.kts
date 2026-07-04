import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":data-rds"))

    testImplementation(project(":test-support"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
