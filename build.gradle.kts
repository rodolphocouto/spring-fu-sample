import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.0"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("org.springframework.boot") version "2.1.0.RELEASE"
    id("org.jmailen.kotlinter") version "1.20.1"
    id("com.adarshr.test-logger") version "1.5.0"
}

dependencies {
    implementation("org.springframework.fu:spring-fu-kofu:0.0.3.BUILD-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.valiktor:valiktor-spring-boot-starter:0.3.1")
    implementation("org.valiktor:valiktor-javatime:0.3.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.10")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
    testImplementation("org.springframework:spring-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

testlogger {
    setTheme("mocha")
}

configurations.all {
    exclude(module = "javax.annotation-api")
    exclude(module = "hibernate-validator")
}