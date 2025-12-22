plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.25"

    id("org.springframework.boot") version "3.5.5" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.danilo"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.5")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation("software.amazon.awssdk:dynamodb:2.25.64")
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.25.64")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}