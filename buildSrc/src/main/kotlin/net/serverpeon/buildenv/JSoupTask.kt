package net.serverpeon.buildenv

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.workers.WorkerExecutor
import java.net.URL
import java.nio.file.Files
import javax.inject.Inject

abstract class JSoupTask @Inject constructor(
        private val executor: WorkerExecutor
) : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract var parser: Class<out JsoupSpec<*>>

    @get:OutputFiles
    abstract val targets: MapProperty<URL, RegularFile>

    @get:Internal
    val entry: Provider<Map.Entry<URL, RegularFile>>
        get() = targets.map { it.entries.single() }

    init {
        group = "scrape"

        outputDir.set(project.layout.buildDirectory.dir("scrapes/$name"))
    }

    fun entry(target: URL, output: String) {
        targets.put(target, outputDir.file(output))
    }

    @TaskAction
    fun doParse() {
        Files.createDirectories(outputDir.get().asFile.toPath())
        val queue = executor.noIsolation()
        targets.get().forEach { (url, target) ->
            queue.submit(parser) {
                this.url.set(url)
                this.output.set(target)
            }
        }
    }
}