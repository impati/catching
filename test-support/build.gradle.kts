import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {

    api(project(":domain-model"))
    runtimeOnly("com.h2database:h2")
}


tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
