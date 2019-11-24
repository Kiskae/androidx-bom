plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation("com.squareup.moshi:moshi:1.8.0")
    api("com.vdurmont:semver4j:2.2.0")
    api("org.jsoup:jsoup:1.12.1")
}