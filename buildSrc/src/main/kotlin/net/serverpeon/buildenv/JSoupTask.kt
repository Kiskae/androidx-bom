package net.serverpeon.buildenv

import okio.BufferedSink
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerExecutor
import org.jsoup.nodes.Document
import java.io.File
import java.net.URL
import java.nio.file.Files
import javax.inject.Inject

abstract class JSoupTask @Inject constructor(
        private val executor: WorkerExecutor
) : DefaultTask() {
    interface DocumentHandler {
        fun BufferedSink.emit(doc: Document)
    }

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract var parser: Class<out JsoupSpec<*>>

    @get:Input
    abstract val targets: MapProperty<URL, File>

    @get:Internal
    val entry: Provider<Map.Entry<URL, File>>
        get() = targets.map { it.entries.single() }

    init {
        group = "scrape"

        outputDir.convention(project.layout.buildDirectory.dir(project.provider {
            "scrapes/$name"
        }))
    }

    fun entry(target: URL, output: String) {
        targets.put(target, outputDir.file(output).map { it.asFile })
    }

    @TaskAction
    fun doParse() {
        Files.createDirectories(outputDir.get().asFile.toPath())

        targets.get().forEach { (url, target) ->
            executor.submit(parser) {
                isolationMode = IsolationMode.NONE

                setParams(url, target)
            }
        }
    }
}