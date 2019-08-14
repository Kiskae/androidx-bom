package net.serverpeon.buildenv

import com.squareup.moshi.JsonWriter
import com.vdurmont.semver4j.Semver
import net.serverpeon.buildenv.subjects.JetpackVersions
import net.serverpeon.buildenv.subjects.MavenGroup
import net.serverpeon.buildenv.subjects.fromJson
import net.serverpeon.buildenv.subjects.moshi
import okio.Okio
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URL
import java.time.LocalDate

abstract class ManifestTask : DefaultTask() {
    @get:Input
    abstract val mavenArtifacts: MapProperty<URL, File>

    @get:InputFile
    abstract val preferredVersions: RegularFileProperty

    @get:OutputFile
    abstract val manifestOutput: RegularFileProperty

    private var cachedManifest: VersionManifest? = null

    init {
        group = "build"
    }

    @TaskAction
    fun action() {
        val jetpackVersions = moshi.adapter(JetpackVersions::class.java).fromJson(preferredVersions.get().asFile)!!
        val mavenAdapter = moshi.adapter(MavenGroup::class.java)
        val actualVersions = mavenArtifacts.get().values.associate {
            it.nameWithoutExtension to mavenAdapter.fromJson(it)!!
        }

        val manifest = VersionManifest(
                jetpackVersions.lastChanged,
                actualVersions.mapValues { (groupName, maven) ->
                    VersionManifest.ArtifactGroup(
                            maven.group,
                            maven.artifacts,
                            jetpackVersions.artifacts[groupName]?.let {
                                it.stable ?: it.rc ?: it.beta ?: it.alpha
                            },
                            jetpackVersions.artifacts[groupName]
                                    ?: JetpackVersions.Artifact(
                                            LocalDate.MIN,
                                            null,
                                            null,
                                            null,
                                            null
                                    )
                    )
                }
        )

        cachedManifest = manifest

        Okio.buffer(Okio.sink(manifestOutput.get().asFile)).use {
            moshi.adapter(VersionManifest::class.java).toJson(JsonWriter.of(it).apply {
                indent = "  "
            }, manifest)
        }
    }

    fun loadDefinedManifest(): VersionManifest {
        return cachedManifest ?: loadManifest(manifestOutput.get().asFile)
    }

    data class VersionManifest(
            val lastUpdate: LocalDate,
            val groups: Map<String, ArtifactGroup>
    ) {
        data class ArtifactGroup(
                val groupId: String,
                val artifacts: Map<String, List<Semver>>,
                val preferred: Semver?,
                val jetpackVersions: JetpackVersions.Artifact
        ) {
            fun toMavenCoordinates(mavenProperty: String): Map<String, String> {
                return artifacts.mapValues { (artifact, versions) ->
                    val version = resolveVersion(
                            "$groupId:$artifact",
                            mavenProperty,
                            versions
                    )
                    "$groupId:$artifact:$version"
                }
            }

            private fun resolveVersion(artifactName: String,
                                       mavenProperty: String,
                                       versions: List<Semver>
            ): String {
                val sortedVersion = versions.sortedDescending()
                return when (preferred) {
                    null -> sortedVersion.firstOrNull { it.isStable } ?: sortedVersion.first()
                    in versions -> preferred
                    else -> when {
                        // Artifact might belong to the current non-stable version, check for presence.
                        jetpackVersions.stable in versions -> jetpackVersions.stable
                        jetpackVersions.rc in versions -> jetpackVersions.rc
                        jetpackVersions.beta in versions -> jetpackVersions.beta
                        jetpackVersions.alpha in versions -> jetpackVersions.alpha
                        else -> {
                            sortedVersion.firstOrNull {
                                // Find the last published version before the current preferred, no surprises.
                                it.isLowerThanOrEqualTo(preferred)
                            } ?: sortedVersion.firstOrNull()
                            ?: error("Unable to find preferred version for: $artifactName. " +
                                    "[preferred: $preferred, available: $sortedVersion]")
                        }
                    }
                }.let { if (it == preferred) mavenProperty else it!!.originalValue }
            }
        }
    }

    companion object {
        fun loadManifest(file: File): VersionManifest = moshi.adapter(VersionManifest::class.java).fromJson(file)!!
    }
}