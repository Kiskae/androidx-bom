package net.serverpeon.buildenv

import com.squareup.moshi.JsonAdapter
import okio.Okio
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.File
import java.net.URL
import javax.inject.Inject

abstract class JsoupSpec<T> @Inject constructor(
        private val url: URL,
        private val output: File
) : Runnable {
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

    final override fun run() {
        val document = Jsoup.connect(url.toString()).parser(parser).get()
        Okio.buffer(Okio.sink(output)).use {
            adapter.toJson(it, document.parse())
        }
    }

    protected abstract fun Document.parse(): T

    protected abstract val parser: Parser

    protected abstract val adapter: JsonAdapter<T>
}