package net.serverpeon.buildenv.subjects

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.vdurmont.semver4j.Semver
import okio.Okio
import java.io.File
import java.time.LocalDate

private object LocalDateAdapter : JsonAdapter<LocalDate>() {
    override fun fromJson(reader: JsonReader): LocalDate? = LocalDate.parse(reader.nextString())

    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        if (value == null || value == LocalDate.MIN) {
            writer.nullValue()
        } else {
            writer.value(value.toString())
        }
    }
}

private object SemVerAdapter : JsonAdapter<Semver>() {
    override fun fromJson(reader: JsonReader): Semver? = Semver(reader.nextString())

    override fun toJson(writer: JsonWriter, value: Semver?) {
        writer.value(value!!.originalValue)
    }

}

val moshi: Moshi = Moshi.Builder()
        .add(LocalDate::class.java, LocalDateAdapter.nullSafe())
        .add(Semver::class.java, SemVerAdapter.nullSafe())
        .build()

fun <T> JsonAdapter<T>.fromJson(file: File): T? = Okio.buffer(Okio.source(file)).use(this::fromJson)