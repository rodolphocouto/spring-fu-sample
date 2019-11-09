plugins {
    kotlin("jvm") version "1.3.50"
    id("org.jmailen.kotlinter") version "2.1.1"
    id("com.adarshr.test-logger") version "1.7.0"
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("jacoco")
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.2.0.RELEASE"))
    implementation(platform("org.springframework.boot.experimental:spring-boot-bom-r2dbc:0.1.0.M2"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.springframework.fu:spring-fu-kofu:0.2.2.BUILD-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.data:spring-data-r2dbc")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.valiktor:valiktor-core:0.9.0")
    implementation("org.valiktor:valiktor-javatime:0.9.0")
    implementation("org.valiktor:valiktor-spring-boot-starter:0.9.0")

    runtimeOnly("io.r2dbc:r2dbc-h2")

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("org.valiktor:valiktor-test:0.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

configurations.all {
    exclude(group = "junit")
    exclude(group = "org.mockito")
    exclude(module = "hibernate-validator")
    exclude(module = "jakarta.validation-api")
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
        }
    }

    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
        }
    }

    jacocoTestCoverageVerification {
        dependsOn(jacocoTestReport)
    }

    check {
        dependsOn(jacocoTestCoverageVerification)
    }
}

kotlinter {
    disabledRules = arrayOf("import-ordering")
}

testlogger {
    setTheme("mocha")
}
