import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
  id("org.springframework.boot") version "2.2.6.RELEASE" apply false
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  id("java")
  id("jacoco")
  id("org.sonarqube")
  kotlin("jvm")
  kotlin("plugin.spring")
}

group = "fr.edjaz.microservices.api"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

base {
  archivesBaseName = "base-api"
}

val developmentOnly by configurations.creating
configurations {
  runtimeClasspath {
    extendsFrom(developmentOnly)
  }
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
  maven { url = URI("https://oss.jfrog.org/artifactory/oss-snapshot-local/") }
}


dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  compileOnly("com.google.code.findbugs:jsr305:3.0.2")
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("io.springfox:springfox-swagger2:${property("springfoxVersion")}")
}

apply(plugin = "io.spring.dependency-management")

dependencyManagement {
  imports {
    mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
  }
}


tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}


tasks.jacocoTestReport {
  reports {
    xml.isEnabled = true
    csv.isEnabled = false
    html.isEnabled = true
  }
}

sonarqube {
  properties {
    property("sonar.sources", "src/main/")
  }
}
