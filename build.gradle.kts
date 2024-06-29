plugins {
    application
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.philosobyte"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    configureEach {
        exclude(module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.3.1")
    implementation("org.springframework.boot:spring-boot-starter-log4j2:3.3.1")

    implementation("org.apache.ignite:ignite-core:2.16.0")
    implementation("org.apache.ignite:ignite-spring:2.16.0")
    implementation("org.apache.ignite:ignite-slf4j:2.16.0")
    testImplementation("org.apache.ignite:ignite-core:2.16.0") {
        artifact {
            classifier = "tests"
        }
    }
    testImplementation("org.junit.vintage:junit-vintage-engine:5.10.3")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.3")
}

//application {
//    applicationDefaultJvmArgs = listOf(
//        "--add-opens", "java.base/jdk.internal.access=ALL-UNNAMED",
//        "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED",
//        "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
//        "--add-opens", "java.base/sun.util.calendar=ALL-UNNAMED",
//        "--add-opens", "java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED",
//        "--add-opens", "jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED",
//        "--add-opens", "java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED",
//        "--add-opens", "jdk.management/com.sun.management.internal=ALL-UNNAMED",
//        "--add-opens", "java.base/java.io=ALL-UNNAMED",
//        "--add-opens", "java.base/java.nio=ALL-UNNAMED",
//        "--add-opens", "java.base/java.net=ALL-UNNAMED",
//        "--add-opens", "java.base/java.util=ALL-UNNAMED",
//        "--add-opens", "java.base/java.util.concurrent=ALL-UNNAMED",
//        "--add-opens", "java.base/java.util.concurrent.locks=ALL-UNNAMED",
//        "--add-opens", "java.base/java.util.concurrent.atomic=ALL-UNNAMED",
//        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
//        "--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED",
//        "--add-opens", "java.base/java.math=ALL-UNNAMED",
//        "--add-opens", "java.sql/java.sql=ALL-UNNAMED",
//        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
//        "--add-opens", "java.base/java.time=ALL-UNNAMED",
//        "--add-opens", "java.base/java.text=ALL-UNNAMED",
//        "--add-opens", "java.management/sun.management=ALL-UNNAMED",
//        "--add-opens", "java.desktop/java.awt.font=ALL-UNNAMED"
//    )
//}
//
//tasks.withType<JavaExec> {
//    jvmArgs("--add-opens", "java.base/jdk.internal.access=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/sun.util.calendar=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED")
//    jvmArgs("--add-opens", "jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED")
//    jvmArgs("--add-opens", "jdk.management/com.sun.management.internal=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.io=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.nio=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.net=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.util=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.util.concurrent=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.util.concurrent.locks=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.util.concurrent.atomic=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.math=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.sql/java.sql=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.base/java.text=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.management/sun.management=ALL-UNNAMED")
//    jvmArgs("--add-opens", "java.desktop/java.awt.font=ALL-UNNAMED")
//}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("junit-vintage", "junit-jupiter")
    }

    jvmArgs("--add-opens", "java.base/jdk.internal.access=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/sun.util.calendar=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED")
    jvmArgs("--add-opens", "jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED")
    jvmArgs("--add-opens", "jdk.management/com.sun.management.internal=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.io=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.nio=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.net=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.util=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.util.concurrent=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.util.concurrent.locks=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.util.concurrent.atomic=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.math=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.sql/java.sql=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.text=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.management/sun.management=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.desktop/java.awt.font=ALL-UNNAMED")
}
