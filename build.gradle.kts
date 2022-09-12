import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    id("com.google.cloud.tools.jib") version "3.2.1"
    id("nebula.release") version "16.0.0"
}

allOpen {
    annotation("javax.persistence.Entity") // @Entity가 붙은 클래스에 한해서만 all open 플러그인을 적용
    annotation("javax.persistence.MappedSuperclass") // @Entity가 붙은 클래스에 한해서만 all open 플러그인을 적용
}

noArg {
    annotation("javax.persistence.Entity") // @Entity가 붙은 클래스에 한해서만 no arg 플러그인을 적용
}


group = "day"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // stater
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testRuntimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("it.ozimov:embedded-redis:0.7.3") {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }

    // StringUtils
    implementation("org.apache.commons:commons-text:1.9")
    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    // jsoup
    implementation("org.jsoup:jsoup:1.15.1")
    // Logging
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    // twilio sdk
    implementation("com.twilio.sdk:twilio:8.35.0")
    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")

    // WebClient
    implementation("org.springframework.boot:spring-boot-starter-webflux")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        image = "adoptopenjdk/openjdk11:jre-11.0.6_10-alpine"
    }
    to {
        image = "wlsdn1962/toons-api"
        tags = setOf("${project.version.toString().replace('+', '-')}")
        auth {
            username = findProperty("docker.repo.username") as String?
            password = findProperty("docker.repo.password") as String?
        }
    }
}
