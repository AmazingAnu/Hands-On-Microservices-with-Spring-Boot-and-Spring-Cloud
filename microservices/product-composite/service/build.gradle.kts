import java.net.URI

plugins {
	id ("org.springframework.boot") version "2.2.6.RELEASE"
	id ("io.spring.dependency-management") version "1.0.9.RELEASE"
	id ("java")
	id ("jacoco")
	id ("org.sonarqube")
	id ("com.google.cloud.tools.jib") version "1.8.0"
  kotlin("jvm")
  kotlin("plugin.spring")
}

group = "fr.edjaz.microservices.composite.product"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


base {
	archivesBaseName  = "product-composite-service"
}

jib {
	from {
		image = "openjdk:12.0.2"
	}
	to {
		image = "edjaz/product-composite/service"
	}
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
	maven { url = URI("https://repo.spring.io/milestone") }
}



dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	compileOnly ("com.google.code.findbugs:jsr305:3.0.2")
	compileOnly ("org.projectlombok:lombok")
	annotationProcessor ("org.projectlombok:lombok")

	implementation (project(":api"))
	implementation (project(":microservices:product-composite:api"))
	implementation (project(":microservices:product:api"))
	implementation (project(":microservices:review:api"))
	implementation (project(":microservices:recommendation:api"))

	developmentOnly ("org.springframework.boot:spring-boot-devtools")

	implementation (project(":util"))
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")

	implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")
	implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

	implementation ("org.springframework.cloud:spring-cloud-starter-kubernetes")
	//implementation ("org.springframework.cloud:spring-cloud-starter-kubernetes-ribbon")
	implementation ("org.springframework.cloud:spring-cloud-kubernetes-discovery")
	implementation ("org.springframework.cloud:spring-cloud-kubernetes-config")

	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("org.springframework.cloud:spring-cloud-starter-zipkin")
	implementation("org.springframework.retry:spring-retry")
	implementation("io.springfox:springfox-swagger2:${property("springfoxVersion")}")
	implementation("io.springfox:springfox-swagger-ui:${property("springfoxVersion")}")
	implementation("io.springfox:springfox-spring-webflux:${property("springfoxVersion")}")

  implementation("io.github.resilience4j:resilience4j-spring-boot2:${property("resilience4jVersion")}")
  implementation("io.github.resilience4j:resilience4j-reactor:${property("resilience4jVersion")}")
  implementation("org.springframework.boot:spring-boot-starter-aop")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }

	testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")
	testImplementation("io.projectreactor:reactor-test")

	//compile "io.dekorate:kubernetes-spring-starter:0.9.9"
	//annotationProcessor "io.dekorate:kubernetes-annotations:0.9.9"

	implementation("io.micrometer:micrometer-registry-prometheus")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

sonarqube {
	properties {
		property ("sonar.sources", "src/main/")
		property ("sonar.tests", "src/test/")
	}
}

