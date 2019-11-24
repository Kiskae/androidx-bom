package net.serverpeon.buildenv.subjects

import com.squareup.moshi.JsonAdapter
import com.vdurmont.semver4j.Semver
import net.serverpeon.buildenv.JsoupSpec
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

// Representation of https://developer.android.com/jetpack/androidx/versions/
data class JetpackVersions(
        val artifacts: Map<String, Artifact>,
        val lastChanged: LocalDate
) {
    data class Artifact(
            val latestUpdate: LocalDate,
            val stable: Semver?,
            val rc: Semver?,
            val beta: Semver?,
            val alpha: Semver?
    )

    companion object {
        private val dateFormatter = DateTimeFormatterBuilder()
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(' ')
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral(", ")
                .appendValue(ChronoField.YEAR)
                .toFormatter(Locale.US)

        // Raw HTML differs from chrome devtools
        private const val VERSIONS_ROW_SELECTOR = ".devsite-article-body tr:not(:first-child)"
        private const val LAST_UPDATE_SELECTOR = ".devsite-article-body table + p"
    }

    abstract class Spec : JsoupSpec<JetpackVersions>() {
        override fun Document.parse(): JetpackVersions {
            val artifacts = select(VERSIONS_ROW_SELECTOR).associate { row ->
                val columns = row.select("td").eachText()

                columns[0].trim() to Artifact(
                        LocalDate.parse(columns[1], dateFormatter),
                        columns[2].trim().takeUnless { it == "-" }?.let(::Semver),
                        columns[3].trim().takeUnless { it == "-" }?.let(::Semver),
                        columns[4].trim().takeUnless { it == "-" }?.let(::Semver),
                        columns[5].trim().takeUnless { it == "-" }?.let(::Semver)
                )
            }

            val dateString = selectFirst(LAST_UPDATE_SELECTOR).text().dropWhile {
                it != ':'
            }.drop(1).trim()

            return JetpackVersions(artifacts, LocalDate.parse(dateString, dateFormatter))
        }

        override val parserType: ParserType
            get() = ParserType.HTML
        override val adapter: JsonAdapter<JetpackVersions>
            get() = moshi.adapter(JetpackVersions::class.java)
    }
}