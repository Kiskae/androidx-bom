import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import net.serverpeon.buildenv.JSoupTask
import net.serverpeon.buildenv.ManifestTask
import net.serverpeon.buildenv.subjects.JetpackVersions
import net.serverpeon.buildenv.subjects.MavenGroup
import net.serverpeon.buildenv.subjects.fromJson
import net.serverpeon.buildenv.subjects.moshi
import java.net.URL
import java.nio.file.Files
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    `java-platform`
    `maven-publish`
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.jfrog.bintray") version "1.8.4"
}

val versionManifest = layout.projectDirectory.file("latest.json")

// Keys not listed in https://developer.android.com/jetpack/androidx/versions/ (yet)
val extraKeys = listOf("legacy-google-material")

// These artifacts have entirely different groups in practise.
val groupMapping = mapOf(
        "arch" to "androidx.arch.core",
        "jetifier" to "com.android.tools.build.jetifier",
        "legacy-google-material" to "com.google.android.material"
)

val VERSION_FORMAT = DateTimeFormatter.ofPattern("uuuu.MM.dd")

tasks {
    val parseJetpackVersions by registering(JSoupTask::class) {
        entry(URL("https://developer.android.com/jetpack/androidx/versions/"), "output.json")

        parser = JetpackVersions.Spec::class.java

        outputs.upToDateWhen { false }
    }

    val checkLastUpdated by registering {
        dependsOn(parseJetpackVersions)

        outputs.upToDateWhen {
            val old = versionManifest.asFile.takeIf {
                Files.exists(it.toPath())
            }?.let {
                runCatching {
                    ManifestTask.loadManifest(it)
                }.getOrNull()
            } ?: return@upToDateWhen false

            val new = parseJetpackVersions.flatMap(JSoupTask::entry).map { (_, versionsFile) ->
                moshi.adapter(JetpackVersions::class.java).fromJson(versionsFile)!!
            }.get()

            old.lastUpdate == new.lastChanged
        }
    }

    val parseArtifacts by registering(JSoupTask::class) {
        dependsOn(checkLastUpdated)

        parser = MavenGroup.Spec::class.java

        targets.putAll(parseJetpackVersions.flatMap(JSoupTask::entry).map { (_, versionsFile) ->
            val artifacts = (moshi.adapter(JetpackVersions::class.java)
                    .fromJson(versionsFile)!!
                    .artifacts
                    .keys + extraKeys).toSet()

            artifacts.associate {
                val group = groupMapping.getOrDefault(it, "androidx.$it")
                        .replace('.', '/')

                val groupIndex = URL("https://dl.google.com/dl/android/maven2/$group/group-index.xml")

                groupIndex to outputDir.file("$it.json")
            }.mapValues {
                it.value.get().asFile
            }
        })
    }

    val updateManifest by registering(ManifestTask::class) {
        dependsOn(parseArtifacts)

        preferredVersions.set(project.layout.file(
                parseJetpackVersions.flatMap(JSoupTask::entry).map { it.value }
        ))
        mavenArtifacts.set(parseArtifacts.flatMap(JSoupTask::targets))

        manifestOutput.set(versionManifest)
    }

    val injectDependenciesFromManifest by registering {
        dependsOn(updateManifest)

        doLast {
            val manifest = updateManifest.get().loadDefinedManifest()

            publishing.publications.named<MavenPublication>("androidxPlatform") {
                pom {
                    properties.putAll(manifest.groups.entries.filter {
                        it.value.preferred != null
                    }.associate {
                        "androidx.${it.key}.version" to it.value.preferred!!.originalValue
                    })

                    // https://developer.android.com/jetpack/androidx/releases#august_7_2019
                    val anchor = manifest.lastUpdate.format(DateTimeFormatter.ofPattern(
                            "LLLL'_'d'_'uuuu",
                            Locale.US
                    )).toLowerCase()
                    url.set("https://developer.android.com/jetpack/androidx/releases#$anchor")
                }


                version = manifest.lastUpdate.format(VERSION_FORMAT)
            }

            dependencyManagement {
                dependencies {
                    manifest.groups.entries.flatMap {
                        it.value.toMavenCoordinates("\${androidx.${it.key}.version}").values
                    }.forEach { coordinate ->
                        dependency(coordinate)
                    }
                }
            }
        }
    }

    val guardAgainstWrongVersion by registering {
        dependsOn(updateManifest)

        doLast("Guard against version mismatches") {
            if (!updateManifest.get().state.upToDate) {
                // Since bintray does not allow deferred configuration we must specify the version beforehand.
                //   This task will fail if this session happened to change the manifest which would mean the
                //       version is out of sync.
                throw GradleException("Please run bintrayUpload again, it was using the wrong version id.")
            }
        }
    }

    withType<GenerateMavenPom>().configureEach {
        dependsOn(injectDependenciesFromManifest)
    }

    withType<BintrayUploadTask>().configureEach {
        dependsOn(guardAgainstWrongVersion)
    }
}

publishing {
    publications.register<MavenPublication>("androidxPlatform") {
        groupId = "net.serverpeon.androidx"
        artifactId = "androidx-bom"

        pom {
            description.set("Generated Bill-of-Materials for the AndroidX Jetpack project")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }

                license {
                    name.set("developer.android.com Content License")
                    url.set("https://developer.android.com/license")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/Kiskae/androidx-bom")
                developerConnection.set("scm:git:ssh://github.com/Kiskae/androidx-bom")
                url.set("https://github.com/Kiskae/androidx-bom/")
            }
        }

        from(components["javaPlatform"])
    }
}

bintray {
    user = (project.findProperty("bintrayUser")
            ?: System.getenv("BINTRAY_USER"))?.toString()
    key = (project.findProperty("bintrayApiKey")
            ?: System.getenv("BINTRAY_API_KEY"))?.toString()

    setPublications("androidxPlatform")

    pkg.apply {
        repo = "maven"
        name = "androidx-bom"

        version.apply {
            name = runCatching {
                tasks.getByName<ManifestTask>("updateManifest").loadDefinedManifest().lastUpdate.format(
                        VERSION_FORMAT
                )
            }.getOrNull()
        }
    }
}