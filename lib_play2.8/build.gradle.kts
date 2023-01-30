plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.diffplug.spotless") version "6.14.0"
}

version = "0.0.1-SNAPSHOT"
group = "net.moznion"

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("com.typesafe.play:play_2.13:2.8.19")
    implementation("com.typesafe.play:play-ahc-ws_2.13:2.8.19")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    implementation("com.amazonaws:aws-xray-recorder-sdk-core:2.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        googleJavaFormat()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "aws-xray-java-recorder-sdk-play2.8_2.13"
            from(components["java"])
            pom {
                name.set("aws-xray-java-recorder-sdk-play2.8_2.13")
                description.set("AWS X-Ray SDK for Play2.8 framework")
                url.set("https://github.com/moznion/aws-xray-java-recorder-sdk-play2")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("moznion")
                        name.set("Taiki Kawakami")
                        email.set("moznion@mail.moznion.net")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/moznion/aws-xray-java-recorder-sdk-play2.git")
                    developerConnection.set("scm:git:ssh://github.com/moznion/aws-xray-java-recorder-sdk-play2.git")
                    url.set("https://github.com/moznion/aws-xray-java-recorder-sdk-play2")
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl: String = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl: String = "https://oss.sonatype.org/content/repositories/snapshots"
            setUrl(uri(if ((version as String).endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl))
            credentials {
                username = fun(): String {
                    val sonatypeUsername = findProperty("sonatypeUsername") ?: return ""
                    return sonatypeUsername as String
                }()
                password = fun(): String {
                    val sonatypePassword = findProperty("sonatypePassword") ?: return ""
                    return sonatypePassword as String
                }()
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
