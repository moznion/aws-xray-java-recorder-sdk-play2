plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.typesafe.play:play_2.13:2.8.19")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
