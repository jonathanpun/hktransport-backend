import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.8"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id ("org.jetbrains.kotlin.plugin.noarg") version "1.6.21"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.serialization") version "1.6.10"
	kotlin("plugin.allopen") version "1.4.32"
}

noArg {
	annotation("javax.persistence.Entity")
}

//https://spring.io/guides/tutorials/spring-boot-kotlin/
allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

group = "com.jonathanpun"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	val coroutinesVersion = "1.6.1"
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.75.Final") {
		artifact {
			classifier = "osx-aarch_64"
		}
	}
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("mysql:mysql-connector-java")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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
