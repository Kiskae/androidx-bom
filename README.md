# AndroidX Bill-of-Materials

## Project Archived per 8th April 2021

As per 31th of March Bintray stopped accepting package updates.
After looking at the options I've decided to archive this project for the following reasons:

1. The gradle plugin used to generate the BOM is heavily based on the one provided by bintray and would require rewriting for a different target.
2. The project could very much use a rewrite in general.
3. Updates to the Jetpack recommended versions mostly happens in lockstep with actual version updates to AndroidX projects. This means that updating dependencies using this project involves the same amount of effort as updating the androidx dependencies directly.

## Legacy Readme

Exports the current [AndroidX Releases](https://developer.android.com/jetpack/androidx/versions/)
table as a maven bill-of-materials. 
Keep your `androidx.*` dependencies up-to-date with a single dependency.

## [Gradle `platform(..)` import](https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import)

`platform("net.serverpeon.androidx:androidx-bom:xxxx.xx.xx")` should work without any other modifications.

Using `enforcedPlatform` instead of `platform` is not recommended since it can force a downgrade on dependencies that require non-milestone
versions of `androidx.*` libraries.

For larger projects consider using the [The Java Platform Plugin `java-platform`](https://docs.gradle.org/current/userguide/java_platform_plugin.html)
to extract all dependency declarations into a shared project.

```kotlin
repositories {
    jcenter()
}

dependencies {
    api(platform("net.serverpeon.androidx:androidx-bom:xxxx.xx.xx"))
    api("androidx.appcompat:appcompat") // Selects appcompat:1.0.2
    
    // Unless some other dependency requires a newer version
    api(platform("net.serverpeon.androidx:androidx-bom:2019.08.07"))
    api("com.google.android.material:material:1.1.0-alpha09")
    api("androidx.appcompat:appcompat") // Selects appcompat:1.1.0-rc01
}
```

## [Spring Dependency Management Plugin](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/)

**NOTICE:** The spring dependency plugin strictly follows the `maven` implementation. This means that versions defined 
in the bill-of-materials are enforced on all dependencies, including ones transitively required by other dependencies.

This means that if one of your dependencies is a non-stable release of an `androidx.*` library it will potentially break
the build by forcing a downgrade.
To change this behavior to allow transitive upgrades by other dependencies see [non-stable releases](#non-stable-releases).

```kotlin
plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

repositories {
    jcenter()
}

dependencyManagement {
    imports {
        mavenBom("net.serverpeon.androidx:androidx-bom:xxxx.xx.xx")
    }
} 
```
 
#### Artifacts as per version `2019.08.15`
 
    global - Default dependency management for all configurations
        androidx.activity:activity 1.0.0-rc01
        androidx.activity:activity-ktx 1.0.0-rc01
        androidx.ads:ads-identifier 1.0.0-alpha01
        androidx.ads:ads-identifier-common 1.0.0-alpha01
        androidx.ads:ads-identifier-provider 1.0.0-alpha01
        androidx.annotation:annotation 1.1.0
        androidx.appcompat:appcompat 1.0.2
        androidx.arch.core:core-common 2.0.1
        androidx.arch.core:core-runtime 2.0.1
        androidx.arch.core:core-testing 2.0.1
        androidx.asynclayoutinflater:asynclayoutinflater 1.0.0
        androidx.autofill:autofill 1.0.0-alpha02
        androidx.benchmark:benchmark-common 1.0.0-alpha04
        androidx.benchmark:benchmark-gradle-plugin 1.0.0-alpha04
        androidx.benchmark:benchmark-junit4 1.0.0-alpha04
        androidx.biometric:biometric 1.0.0-alpha04
        androidx.browser:browser 1.0.0
        androidx.camera:camera-camera2 1.0.0-alpha04
        androidx.camera:camera-core 1.0.0-alpha04
        androidx.car:car 1.0.0-alpha7
        androidx.cardview:cardview 1.0.0
        androidx.collection:collection 1.1.0
        androidx.collection:collection-ktx 1.1.0
        androidx.concurrent:concurrent-futures 1.0.0-rc01
        androidx.constraintlayout:constraintlayout 1.1.3
        androidx.constraintlayout:constraintlayout-solver 1.1.3
        androidx.contentpager:contentpager 1.0.0
        androidx.coordinatorlayout:coordinatorlayout 1.0.0
        androidx.core:core 1.0.2
        androidx.core:core-ktx 1.0.2
        androidx.cursoradapter:cursoradapter 1.0.0
        androidx.customview:customview 1.0.0
        androidx.databinding:databinding-adapters 3.4.1
        androidx.databinding:databinding-compiler 3.4.1
        androidx.databinding:databinding-compiler-common 3.4.1
        androidx.databinding:databinding-runtime 3.4.1
        androidx.documentfile:documentfile 1.0.1
        androidx.drawerlayout:drawerlayout 1.0.0
        androidx.dynamicanimation:dynamicanimation 1.0.0
        androidx.emoji:emoji 1.0.0
        androidx.emoji:emoji-appcompat 1.0.0
        androidx.emoji:emoji-bundled 1.0.0
        androidx.enterprise:enterprise-feedback 1.0.0-alpha03
        androidx.enterprise:enterprise-feedback-testing 1.0.0-alpha03
        androidx.exifinterface:exifinterface 1.0.0
        androidx.fragment:fragment 1.0.0
        androidx.fragment:fragment-ktx 1.0.0
        androidx.gridlayout:gridlayout 1.0.0
        androidx.heifwriter:heifwriter 1.0.0
        androidx.interpolator:interpolator 1.0.0
        androidx.leanback:leanback 1.0.0
        androidx.leanback:leanback-preference 1.0.0
        androidx.legacy:legacy-preference-v14 1.0.0
        androidx.legacy:legacy-support-core-ui 1.0.0
        androidx.legacy:legacy-support-core-utils 1.0.0
        androidx.legacy:legacy-support-v13 1.0.0
        androidx.legacy:legacy-support-v4 1.0.0
        androidx.lifecycle:lifecycle-common 2.0.0
        androidx.lifecycle:lifecycle-common-java8 2.0.0
        androidx.lifecycle:lifecycle-compiler 2.0.0
        androidx.lifecycle:lifecycle-extensions 2.0.0
        androidx.lifecycle:lifecycle-livedata 2.0.0
        androidx.lifecycle:lifecycle-livedata-core 2.0.0
        androidx.lifecycle:lifecycle-process 2.0.0
        androidx.lifecycle:lifecycle-reactivestreams 2.0.0
        androidx.lifecycle:lifecycle-reactivestreams-ktx 2.0.0
        androidx.lifecycle:lifecycle-runtime 2.0.0
        androidx.lifecycle:lifecycle-service 2.0.0
        androidx.lifecycle:lifecycle-viewmodel 2.0.0
        androidx.lifecycle:lifecycle-viewmodel-ktx 2.0.0
        androidx.loader:loader 1.0.0
        androidx.localbroadcastmanager:localbroadcastmanager 1.0.0
        androidx.media:media 1.0.1
        androidx.media2:media2-common 1.0.0-rc01
        androidx.media2:media2-exoplayer 1.0.0-rc01
        androidx.media2:media2-player 1.0.0-rc01
        androidx.media2:media2-session 1.0.0-rc01
        androidx.mediarouter:mediarouter 1.0.0
        androidx.multidex:multidex 2.0.1
        androidx.navigation:navigation-common 2.0.0
        androidx.navigation:navigation-common-ktx 2.0.0
        androidx.navigation:navigation-fragment 2.0.0
        androidx.navigation:navigation-fragment-ktx 2.0.0
        androidx.navigation:navigation-runtime 2.0.0
        androidx.navigation:navigation-runtime-ktx 2.0.0
        androidx.navigation:navigation-safe-args-generator 2.0.0
        androidx.navigation:navigation-safe-args-gradle-plugin 2.0.0
        androidx.navigation:navigation-ui 2.0.0
        androidx.navigation:navigation-ui-ktx 2.0.0
        androidx.paging:paging-common 2.1.0
        androidx.paging:paging-common-ktx 2.1.0
        androidx.paging:paging-runtime 2.1.0
        androidx.paging:paging-runtime-ktx 2.1.0
        androidx.paging:paging-rxjava2 2.1.0
        androidx.paging:paging-rxjava2-ktx 2.1.0
        androidx.palette:palette 1.0.0
        androidx.palette:palette-ktx 1.0.0
        androidx.percentlayout:percentlayout 1.0.0
        androidx.preference:preference 1.0.0
        androidx.preference:preference-ktx 1.0.0
        androidx.print:print 1.0.0
        androidx.recommendation:recommendation 1.0.0
        androidx.recyclerview:recyclerview 1.0.0
        androidx.recyclerview:recyclerview-selection 1.0.0
        androidx.remotecallback:remotecallback 1.0.0-alpha02
        androidx.remotecallback:remotecallback-processor 1.0.0-alpha02
        androidx.room:room-common 2.1.0
        androidx.room:room-compiler 2.1.0
        androidx.room:room-guava 2.1.0
        androidx.room:room-ktx 2.1.0
        androidx.room:room-migration 2.1.0
        androidx.room:room-runtime 2.1.0
        androidx.room:room-rxjava2 2.1.0
        androidx.room:room-testing 2.1.0
        androidx.savedstate:savedstate 1.0.0-rc01
        androidx.security:security-crypto 1.0.0-alpha02
        androidx.sharetarget:sharetarget 1.0.0-alpha02
        androidx.slice:slice-builders 1.0.0
        androidx.slice:slice-core 1.0.0
        androidx.slice:slice-view 1.0.0
        androidx.slidingpanelayout:slidingpanelayout 1.0.0
        androidx.sqlite:sqlite 2.0.1
        androidx.sqlite:sqlite-framework 2.0.1
        androidx.sqlite:sqlite-ktx 2.0.1
        androidx.swiperefreshlayout:swiperefreshlayout 1.0.0
        androidx.test:core 1.2.0
        androidx.test:core-ktx 1.2.0
        androidx.test:monitor 1.2.0
        androidx.test:orchestrator 1.2.0
        androidx.test:rules 1.2.0
        androidx.test:runner 1.2.0
        androidx.textclassifier:textclassifier 1.0.0-alpha02
        androidx.transition:transition 1.1.0
        androidx.tvprovider:tvprovider 1.0.0
        androidx.vectordrawable:vectordrawable 1.0.0
        androidx.vectordrawable:vectordrawable-animated 1.0.0
        androidx.versionedparcelable:versionedparcelable 1.1.0
        androidx.viewpager:viewpager 1.0.0
        androidx.viewpager2:viewpager2 1.0.0-beta03
        androidx.wear:wear 1.0.0
        androidx.webkit:webkit 1.0.0
        androidx.work:work-gcm 2.2.0
        androidx.work:work-runtime 2.2.0
        androidx.work:work-runtime-ktx 2.2.0
        androidx.work:work-rxjava2 2.2.0
        androidx.work:work-testing 2.2.0
        com.android.tools.build.jetifier:jetifier-core 1.0.0-beta06
        com.android.tools.build.jetifier:jetifier-processor 1.0.0-beta06
        com.google.android.material:material 1.0.0
         
### Non-stable releases

One thing to be aware of is that the spring dependency plugin enforces `maven` behavior
where even transitive dependencies are subject to the version in the bill-of-materials.

This means that if you include a non-stable dependency which may depend on other non-stable artifacts 
this behavior causes the artifact with missing code and resources to be selected.

The following code changes this behavior to restore the transitive version of managed artifacts if they happen to be
an upgrade from the managed version.

```kotlin
import com.vdurmont.semver4j.Semver

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.vdurmont:semver4j:2.2.0")
    }
}

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

dependencyManagement {
    configurations.configureEach {
        val managedVersions = getManagedVersionsForConfigurationHierarchy(this)

        resolutionStrategy.eachDependency {
            // Check if the requested dependency has a version
            //    and that it is a managed version.
            if (!requested.version.isNullOrBlank() &&
                    requested.module.toString() in managedVersions) {
                val req = Semver(requested.version, Semver.SemverType.LOOSE)

                // Check if it was not a downgrade
                if (req.isGreaterThan(target.version)) {
                    because("some dependency requested a newer version")
                            .useVersion(req.originalValue)
                }
            }
        }
    }

    imports {
        ...
    }

    dependencies {
        // required for daynight support.
        dependency("com.google.android.material:material:1.1.0-alpha09")
    }
} 
```
         
### Version overrides

All artifacts belong to a shared release as per [AndroidX releases](https://developer.android.com/jetpack/androidx/versions/)
exposes a maven property in the form of `androidx.<Maven Group Id>.version`.

See [Dependency Management Plugin / Overriding Versions in a Bom](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/#dependency-management-configuration-bom-import-override)
for more information.

```kotlin
dependencyManagement {
    imports {
        mavenBom("net.serverpeon.androidx:androidx-bom:2019.08.07") {
            bomProperty("androidx.constraintlayout.version", "2.0.0-beta1")
        }
    }
}
```
 
         ....
         androidx.concurrent:concurrent-futures 1.0.0-rc01
         androidx.constraintlayout:constraintlayout 2.0.0-beta1
         androidx.constraintlayout:constraintlayout-solver 2.0.0-beta1
         androidx.contentpager:contentpager 1.0.0
         ....
