dependencies {
    implementation(project(":hw18-jdbc:util"))
    implementation(project(":hw18-jdbc:demo"))

    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
}
