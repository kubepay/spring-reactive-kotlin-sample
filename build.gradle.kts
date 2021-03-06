import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension

plugins {
    application
    id("org.jetbrains.kotlin.jvm") version "1.2.10"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.2.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.2.10"
    id("com.github.johnrengelman.shadow") version "2.0.1"
    id("io.spring.dependency-management") version "1.0.4.RELEASE"
    id("org.junit.platform.gradle.plugin") version "1.0.2"
}

//buildscript {
//    repositories {
//        mavenCentral()
//        maven { setUrl("https://plugins.gradle.org/m2/") }
//        maven { setUrl("https://repo.spring.io/snapshot") }
//        maven { setUrl("https://repo.spring.io/milestone") }
//        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
//    }
//    dependencies {
//        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.2")
//    }
//}
//
//apply {
//    plugin("org.junit.platform.gradle.plugin")
//}

application {
    mainClassName = "com.example.demo.ApplicationKt"
}

version = "1.0.0-SNAPSHOT"

val kotlinVersion = "1.2.10"
val springBootVersion = "2.0.0.M7"

// https://spring.io/blog/2016/12/16/dependency-management-plugin-1-0-0-rc1
dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-parent:$springBootVersion")
    }
}

configure<NoArgExtension> {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spring.io/snapshot")
    maven("https://repo.spring.io/milestone")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}


dependencies {
    //spring webflux
    compile("org.springframework:spring-webflux")
    compile("org.springframework:spring-context") {
        exclude(module = "spring-aop")
    }
    compile("com.fasterxml.jackson.core:jackson-databind")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    compile("io.netty:netty-buffer")
    compile("io.projectreactor.ipc:reactor-netty")
    compile("com.samskivert:jmustache")

    //spring security for webflux
    compile("org.springframework.security:spring-security-core")
    compile("org.springframework.security:spring-security-config")
    compile("org.springframework.security:spring-security-web")

    //spring data mongodb reactive
    compile("org.springframework.data:spring-data-mongodb")
    compile("org.mongodb:mongodb-driver-reactivestreams")

    //spring Session
    compile("org.springframework.session:spring-session-core")

    //kotlin
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    //slf4j and logback
    compile("org.slf4j:slf4j-api")
    compile("org.slf4j:jcl-over-slf4j")
    compile("ch.qos.logback:logback-classic")

    compile("com.google.code.findbugs:jsr305:3.0.2") // Needed for now, could be removed when KT-19419 will be fixed

    //test
    testCompile("org.springframework:spring-test") {
        exclude(module = "junit")
    }
    testCompile("io.projectreactor:reactor-test")
    testCompile("org.springframework.security:spring-security-test")
    testCompile("org.junit.jupiter:junit-jupiter-api")
    testRuntime("org.junit.jupiter:junit-jupiter-engine")
   // testRuntime("org.junit.platform:junit-platform-launcher:1.0.0")
}
