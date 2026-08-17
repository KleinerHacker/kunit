/*
 * Copyright (c) KleinerHacker alias Pfeiffer C Soft 2026.
 * This work is licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this software is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations.
 */

import com.github.jk1.license.render.ReportRenderer
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform") version "2.4.10"
    `maven-publish`
    id("org.jetbrains.kotlinx.kover") version "0.9.9"
    id("org.jetbrains.dokka") version "2.2.0"
    id("com.github.jk1.dependency-license-report") version "3.1.4"
    // Pinned to 3.2.2: 3.2.3+ regressed on Gradle 9 — io.spring.dependency-management observes
    // the `cyclonedxDirectBom` configuration as a variant before the plugin registers its artifacts,
    // which Gradle 9 then rejects ("Cannot mutate ... consumed as a variant"). See cyclonedx #821.
    id("org.cyclonedx.bom") version "3.4.1"
    id("app.cash.licensee") version "1.14.1"
}

group = "org.pcsoft.framework"

// A release passes the tag as -PreleaseVersion=<tag>; a local build stays on the snapshot.
version = (project.findProperty("releaseVersion") as String?)?.takeIf { it.isNotBlank() } ?: "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)

    // Gives the intermediate source sets (nativeMain, appleMain, ...) for free, so platform-specific
    // code can be added later without restructuring the build.
    applyDefaultHierarchyTemplate()

    jvm {
        compilerOptions {
            // JVM-only flag: strict JSR-305 nullability handling for Java interop.
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }

    js {
        browser()
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }

    linuxX64()
    mingwX64()
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// The API documentation shipped alongside every artifact. Maven clients look for a `javadoc` classifier,
// so the Dokka HTML output is packed under that name.
val dokkaHtmlJar = tasks.register<Jar>("dokkaHtmlJar") {
    group = "documentation"
    description = "Packs the Dokka HTML documentation as the publications' javadoc artifact"
    dependsOn("dokkaGeneratePublicationHtml")
    from(layout.buildDirectory.dir("dokka/html"))
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/KleinerHacker/kunit")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    // Applies to every publication the Kotlin Multiplatform plugin creates: the root module plus one per
    // target (kunit-jvm, kunit-js, kunit-wasm-js, kunit-linuxx64, ...).
    publications.withType<MavenPublication>().configureEach {
        artifact(dokkaHtmlJar)

        pom {
            name.set("kunit")
            description.set(
                "Kotlin Unit Framework - calculate with real physical units in Double precision instead of bare " +
                        "numbers. Code and documentation created with the help of AI."
            )
            url.set("https://github.com/KleinerHacker/kunit")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    id.set("KleinerHacker")
                    name.set("KleinerHacker alias Pfeiffer C Soft")
                    url.set("https://github.com/KleinerHacker")
                }
            }

            scm {
                connection.set("scm:git:https://github.com/KleinerHacker/kunit.git")
                developerConnection.set("scm:git:ssh://git@github.com/KleinerHacker/kunit.git")
                url.set("https://github.com/KleinerHacker/kunit")
            }
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                // Declarations annotated @Generated are intentionally uncoverable
                // (defensive / provably-unreachable code).
                annotatedBy("org.pcsoft.intellij.plugin.inno_setup.Generated")
            }
        }
    }
}

licenseReport {
    outputDir = layout.buildDirectory.dir("licences").get().asFile.absolutePath

    // Multiplatform: the JVM target's runtime classpath replaces the single-target `runtimeClasspath`.
    configurations = arrayOf("jvmRuntimeClasspath")

    renderers = arrayOf<ReportRenderer>(
        com.github.jk1.license.render.JsonReportRenderer(),
        com.github.jk1.license.render.SimpleHtmlReportRenderer()
    )
}

plugins.withId("org.jetbrains.kotlin.multiplatform") {
    plugins.withId("app.cash.licensee") {
        extensions.configure<app.cash.licensee.LicenseeExtension> {
            listOf(
                "Apache-2.0",
            ).forEach(::allow)
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    //region Dokka
    register<Copy>("copyDokka") {
        group = "dokka"
        description = "Copy all Dokka to MkDocs"
        from(File("build/dokka"))
        into(File("docs/docs/dokka"))
        dependsOn("dokkaGeneratePublicationHtml")
    }

    register<Delete>("deleteDokka") {
        group = "dokka"
        description = "Delete Dokka"
        delete(File("docs/docs/dokka"))
    }
    //endregion

    //region Licencing
    register<Copy>("copyLicenceReport") {
        group = "licencing"
        description = "Copy licence report to MkDocs"
        from(File("build/licences"))
        into(File("docs/docs/licences"))
        dependsOn("generateLicenseReport")
    }

    register<Delete>("deleteLicenceReport") {
        group = "licencing"
        description = "Delete licence report"
        delete(File("docs/docs/licences"))
    }
    //endregion

    //region MkDocs
    // mike spawns `mkdocs` as a subprocess; on Windows the Python Scripts dir
    // (where mkdocs.exe lives) is often not on PATH. Resolve it once and prepend
    // it to PATH for the mike tasks. In CI (setup-python) it is already on PATH.
    val pythonScriptsDir: String? by lazy {
        runCatching {
            providers.exec {
                commandLine("python", "-c", "import sysconfig; print(sysconfig.get_path('scripts'))")
            }.standardOutput.asText.get().trim().ifEmpty { null }
        }.getOrNull()
    }

    fun Exec.withMikePath() {
        pythonScriptsDir?.let { dir ->
            environment("PATH", dir + File.pathSeparator + System.getenv("PATH"))
        }
    }

    register<Exec>("installMkDocs") {
        group = null
        description = "Install mkdocs"
        workingDir = file("docs")
        commandLine("python", "-m", "pip", "install", "--upgrade", "mkdocs")
    }

    register<Exec>("installMkDocsMaterial") {
        group = null
        description = "Install mkdocs-material"
        workingDir = file("docs")
        commandLine("python", "-m", "pip", "install", "--upgrade", "mkdocs-material")
    }

    register<Exec>("installGitHubPages") {
        group = null
        description = "Install ghp-import"
        workingDir = file("docs")
        commandLine("python", "-m", "pip", "install", "--upgrade", "ghp-import")
    }

    register<Exec>("installMike") {
        group = null
        description = "Install mike for versioned docs deployment"
        workingDir = file("docs")
        commandLine("python", "-m", "pip", "install", "--upgrade", "mike")
    }

    register<Exec>("installI18N") {
        group = null
        description = "Install i18n"
        workingDir = file("docs")
        commandLine("python", "-m", "pip", "install", "--upgrade", "mkdocs-static-i18n")
    }

    register("installDocs") {
        group = "MKDocs"
        description = "Install mkdocs and dependencies"
        dependsOn("installMkDocs")
        dependsOn("installMkDocsMaterial")
        dependsOn("installGitHubPages")
        dependsOn("installI18N")
        dependsOn("installMike")
    }

    register<Exec>("runDocs") {
        group = "MKDocs"
        description = "Run mkdocs serve and open browser (no version selector — that only appears on the deployed site)"
        workingDir = file("docs")
        commandLine("python", "-m", "mkdocs", "serve", "-o", "-w", ".", "-w", "./docs")
        dependsOn("installDocs", "copyDokka", "copyLicenceReport")
        finalizedBy("deleteDokka", "deleteLicenceReport")
    }

    register<Exec>("buildDocs") {
        group = "MKDocs"
        description =
            "Build the mkdocs site into build/docs (per mkdocs.yml site_dir; no serve, no deploy) — usable as a generation test"
        workingDir = file("docs")
        // --strict fails the build on warnings (broken links, missing pages …) so it acts as a test;
        // --clean wipes the previous output first.
        commandLine("python", "-m", "mkdocs", "build", "--clean", "--strict")
        dependsOn("installDocs", "copyDokka", "copyLicenceReport")
        finalizedBy("deleteDokka", "deleteLicenceReport")
    }

    register<Exec>("deployDocs") {
        group = "MKDocs"
        description =
            "Deploy a versioned docs snapshot via mike. Pass -PdocsVersion=<tag>; falls back to \"snapshot\" if no tag is given. Requires a pre-configured git push target."
        workingDir = file("docs")
        // Use -PdocsVersion=<tag> for a real release; otherwise deploy under the "snapshot" alias.
        // A "snapshot" deploy is never promoted to "latest".
        val ver = (project.findProperty("docsVersion") as String?)?.takeIf { it.isNotBlank() }
            ?: "snapshot"
        val setLatest = ver != "snapshot" && (project.findProperty("setLatest") as String?) != "false"
        val args = buildList {
            add("python"); add("-c"); add("from mike.driver import main; main()"); add("deploy"); add("--push")
            if (setLatest) {
                add("--update-aliases"); add(ver); add("latest")
            } else add(ver)
        }
        commandLine(args)
        withMikePath()
        dependsOn("installDocs", "copyDokka", "copyLicenceReport")
        finalizedBy("deleteDokka", "deleteLicenceReport")
    }

    register<Exec>("setDefaultDocs") {
        group = "MKDocs"
        description =
            "Set the default docs version shown at the root URL via mike (run once after the first release deploy)."
        workingDir = file("docs")
        commandLine("python", "-c", "from mike.driver import main; main()", "set-default", "--push", "latest")
        withMikePath()
        dependsOn("installDocs")
    }
    //endregion
}
