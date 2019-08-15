# AndroidX Bill-of-Materials

 [ ![Download](https://api.bintray.com/packages/kiskae/maven/androidx-bom/images/download.svg) ](https://bintray.com/kiskae/maven/androidx-bom/_latestVersion)

Exports the current [AndroidX Releases](https://developer.android.com/jetpack/androidx/versions/)
table as a maven bill-of-materials. 
Keep your `androidx.*` dependencies up-to-date with a single dependency.

## [Gradle `platform(..)` import](https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import)

*not verified to work with android-gradle-plugin*

```kotlin
repositories {
    jcenter()
}

dependencies {
    <configuration>(platform("net.serverpeon.androidx:androidx-bom:xxxx.xx.xx"))
}
```

## [Spring Dependency Management Plugin](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/)

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
 
#### Artifacts as per version `2019.08.07`
 
     global - Default dependency management for all configurations
         androidx.activity:activity 1.0.0-rc01
         androidx.activity:activity-ktx 1.0.0-rc01
         androidx.ads:ads-identifier 1.0.0-alpha01
         androidx.ads:ads-identifier-common 1.0.0-alpha01
         androidx.ads:ads-identifier-provider 1.0.0-alpha01
         androidx.annotation:annotation 1.1.0
         androidx.appcompat:appcompat 1.0.2
         androidx.appcompat:appcompat-resources 1.1.0-rc01
         androidx.arch.core:core-common 2.0.1
         androidx.arch.core:core-runtime 2.0.1
         androidx.arch.core:core-testing 2.0.1
         androidx.asynclayoutinflater:asynclayoutinflater 1.0.0
         androidx.autofill:autofill 1.0.0-alpha02
         androidx.benchmark:benchmark 1.0.0-alpha03
         androidx.benchmark:benchmark-common 1.0.0-alpha04
         androidx.benchmark:benchmark-gradle-plugin 1.0.0-alpha04
         androidx.benchmark:benchmark-junit4 1.0.0-alpha04
         androidx.biometric:biometric 1.0.0-alpha04
         androidx.browser:browser 1.0.0
         androidx.camera:camera-camera2 1.0.0-alpha04
         androidx.camera:camera-core 1.0.0-alpha04
         androidx.camera:camera-extensions 1.0.0-alpha01
         androidx.camera:camera-view 1.0.0-alpha01
         androidx.car:car 1.0.0-alpha7
         androidx.car:car-cluster 1.0.0-alpha5
         androidx.cardview:cardview 1.0.0
         androidx.collection:collection 1.1.0
         androidx.collection:collection-ktx 1.1.0
         androidx.concurrent:concurrent-futures 1.0.0-rc01
         androidx.concurrent:concurrent-listenablefuture 1.0.0-beta01
         androidx.concurrent:concurrent-listenablefuture-callback 1.0.0-beta01
         androidx.concurrent:futures 1.0.0-alpha01
         androidx.constraintlayout:constraintlayout 1.1.3
         androidx.constraintlayout:constraintlayout-solver 1.1.3
         androidx.contentpager:contentpager 1.0.0
         androidx.coordinatorlayout:coordinatorlayout 1.0.0
         androidx.core:core 1.0.2
         androidx.core:core-ktx 1.0.2
         androidx.core:core-role 1.0.0-alpha01
         androidx.cursoradapter:cursoradapter 1.0.0
         androidx.customview:customview 1.0.0
         androidx.databinding:adapters 3.2.0-alpha11
         androidx.databinding:baseLibrary 3.2.0-alpha11
         androidx.databinding:compiler 3.2.0-alpha11
         androidx.databinding:compilerCommon 3.2.0-alpha11
         androidx.databinding:databinding-adapters 3.4.1
         androidx.databinding:databinding-common 3.4.1
         androidx.databinding:databinding-compiler 3.4.1
         androidx.databinding:databinding-compiler-common 3.4.1
         androidx.databinding:databinding-runtime 3.4.1
         androidx.databinding:library 3.2.0-alpha11
         androidx.databinding:viewbinding 3.6.0-alpha03
         androidx.documentfile:documentfile 1.0.1
         androidx.drawerlayout:drawerlayout 1.0.0
         androidx.dynamicanimation:dynamicanimation 1.0.0
         androidx.dynamicanimation:dynamicanimation-ktx 1.0.0-alpha02
         androidx.emoji:emoji 1.0.0
         androidx.emoji:emoji-appcompat 1.0.0
         androidx.emoji:emoji-bundled 1.0.0
         androidx.enterprise:enterprise-feedback 1.0.0-alpha03
         androidx.enterprise:enterprise-feedback-testing 1.0.0-alpha03
         androidx.exifinterface:exifinterface 1.0.0
         androidx.fragment:fragment 1.0.0
         androidx.fragment:fragment-ktx 1.0.0
         androidx.fragment:fragment-testing 1.1.0-rc04
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
         androidx.lifecycle:lifecycle-livedata-core-ktx 2.1.0-rc01
         androidx.lifecycle:lifecycle-livedata-ktx 2.1.0-rc01
         androidx.lifecycle:lifecycle-process 2.0.0
         androidx.lifecycle:lifecycle-reactivestreams 2.0.0
         androidx.lifecycle:lifecycle-reactivestreams-ktx 2.0.0
         androidx.lifecycle:lifecycle-runtime 2.0.0
         androidx.lifecycle:lifecycle-runtime-ktx 2.2.0-alpha03
         androidx.lifecycle:lifecycle-service 2.0.0
         androidx.lifecycle:lifecycle-viewmodel 2.0.0
         androidx.lifecycle:lifecycle-viewmodel-ktx 2.0.0
         androidx.lifecycle:lifecycle-viewmodel-savedstate 1.0.0-alpha03
         androidx.loader:loader 1.0.0
         androidx.localbroadcastmanager:localbroadcastmanager 1.0.0
         androidx.media:media 1.0.1
         androidx.media:media-widget 1.0.0-alpha5
         androidx.media2:media2 1.0.0-alpha04
         androidx.media2:media2-common 1.0.0-rc01
         androidx.media2:media2-exoplayer 1.0.0-rc01
         androidx.media2:media2-player 1.0.0-rc01
         androidx.media2:media2-session 1.0.0-rc01
         androidx.media2:media2-widget 1.0.0-beta01
         androidx.mediarouter:mediarouter 1.0.0
         androidx.multidex:multidex 2.0.1
         androidx.multidex:multidex-instrumentation 2.0.0
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
         androidx.room:room-coroutines 2.1.0-alpha04
         androidx.room:room-guava 2.1.0
         androidx.room:room-ktx 2.1.0
         androidx.room:room-migration 2.1.0
         androidx.room:room-runtime 2.1.0
         androidx.room:room-rxjava2 2.1.0
         androidx.room:room-testing 2.1.0
         androidx.savedstate:savedstate 1.0.0-rc01
         androidx.savedstate:savedstate-bundle 1.0.0-alpha01
         androidx.savedstate:savedstate-common 1.0.0-alpha01
         androidx.security:security-crypto 1.0.0-alpha02
         androidx.sharetarget:sharetarget 1.0.0-alpha02
         androidx.slice:slice-builders 1.0.0
         androidx.slice:slice-builders-ktx 1.0.0-alpha6
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
         androidx.work:work-gcm 2.2.0-rc01
         androidx.work:work-runtime 2.1.0
         androidx.work:work-runtime-ktx 2.1.0
         androidx.work:work-rxjava2 2.1.0
         androidx.work:work-testing 2.1.0
         com.android.tools.build.jetifier:jetifier-core 1.0.0-beta05
         com.android.tools.build.jetifier:jetifier-processor 1.0.0-beta05
         com.google.android.material:material 1.0.0
         
### ⚠ Non-stable releases and transitive dependencies

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
         androidx.concurrent:futures 1.0.0-alpha01
         androidx.constraintlayout:constraintlayout 2.0.0-beta1
         androidx.constraintlayout:constraintlayout-solver 2.0.0-beta1
         androidx.contentpager:contentpager 1.0.0
         ....
