plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("com.typesafe.play:play_2.13:2.8.19")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    implementation("com.amazonaws:aws-xray-recorder-sdk-core:2.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
