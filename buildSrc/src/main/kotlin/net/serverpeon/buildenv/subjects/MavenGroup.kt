package net.serverpeon.buildenv.subjects

import com.squareup.moshi.JsonAdapter
import com.vdurmont.semver4j.Semver
import net.serverpeon.buildenv.JsoupSpec
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.File
import java.net.URL
import javax.inject.Inject

data class MavenGroup(val group: String, val artifacts: Map<String, List<Semver>>) {
    open class Spec @Inject constructor(
            url: URL,
            output: File
    ) : JsoupSpec<MavenGroup>(url, output) {
        override fun Document.parse(): MavenGroup {
            val target = this.children().first { it.childNodeSize() > 0 }
            return MavenGroup(
                    target.nodeName(),
                    target.childNodes().filter { it.hasAttr("versions") }.associate {
                        it.nodeName() to it.attr("versions").split(',').mapNotNull { version ->
                            // Since androidx uses semvar, discard ALL invalid versions
                            runCatching { Semver(version) }.getOrNull()
                        }
                    }
            )
        }

        override val parser: Parser
            get() = Parser.xmlParser()
        override val adapter: JsonAdapter<MavenGroup>
            get() = moshi.adapter(MavenGroup::class.java)

    }
}