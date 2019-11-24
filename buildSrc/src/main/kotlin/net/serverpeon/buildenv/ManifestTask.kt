package net.serverpeon.buildenv

import com.squareup.moshi.JsonWriter
import com.vdurmont.semver4j.Semver
import net.serverpeon.buildenv.subjects.JetpackVersions
import net.serverpeon.buildenv.subjects.MavenGroup
import net.serverpeon.buildenv.subjects.fromJson
import net.serverpeon.buildenv.subjects.moshi
import okio.Okio
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.*
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.time.LocalDate

abstract class ManifestTask : DefaultTask() {
    @get:Internal
    //FIXME: Can't be @Input since its not serializable
    abstract val mavenArtifacts: MapProperty<URL, RegularFile>

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
        val actualVersions = mavenArtifacts.get().values.map { it.asFile }.associate {
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
            fun toMavenCoordinates(mavenProperty: String?): Map<String, String> {
                return artifacts.entries.mapNotNull { (artifact, versions) ->
                    resolveVersion(
                            "$groupId:$artifact",
                            mavenProperty,
                            versions
                    )?.let { version ->
                        artifact to "$groupId:$artifact:$version"
                    }
                }.toMap()
            }

            private fun resolveVersion(artifactName: String,
                                       mavenProperty: String?,
                                       versions: List<Semver>
            ): String? {
                return when (preferred) {
                    null -> versions.sortedDescending().let { sortedVersions ->
                        sortedVersions.firstOrNull { it.isStable } ?: sortedVersions.first()
                    }.originalValue
                    in versions -> mavenProperty ?: preferred.originalValue
                    else -> {
                        logger.info("Unable to find preferred version for: $artifactName. " +
                                "[preferred: $preferred, available: $versions]")
                        null
                    }
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ManifestTask::class.java)

        fun loadManifest(file: File): VersionManifest = moshi.adapter(VersionManifest::class.java).fromJson(file)!!
    }
}