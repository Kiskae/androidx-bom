package net.serverpeon.buildenv

import com.squareup.moshi.JsonAdapter
import okio.Okio
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.net.URL

abstract class JsoupSpec<T> : WorkAction<JsoupSpec.Input> {
    enum class ParserType {
        HTML {
            override val parser: Parser
                get() = Parser.htmlParser()
        },
        XML {
            override val parser: Parser
                get() = Parser.xmlParser()
        };

        internal abstract val parser: Parser
    }

    private fun extract(from: URL, to: RegularFile) {
        val document = Jsoup.connect(from.toString()).parser(parserType.parser).get()
        Okio.buffer(Okio.sink(to.asFile)).use {
            adapter.toJson(it, document.parse())
        }
    }

    final override fun execute() {
        parameters.apply {
            extract(url.get(), output.get())
        }
    }

    interface Input : WorkParameters {
        val url: Property<URL>
        val output: RegularFileProperty
    }

    protected abstract fun Document.parse(): T

    protected abstract val parserType: ParserType

    protected abstract val adapter: JsonAdapter<T>
}