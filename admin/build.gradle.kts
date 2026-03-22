dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    
    implementation(project(":domain"))
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = true
}


