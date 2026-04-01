import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("app.cash.sqldelight") version "2.2.1"
}

kotlin {
    // Android target obligatorio
    androidTarget()

    // Desktop JVM target
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    // iOS targets
    val iosX64 = iosX64()
    val iosArm64 = iosArm64()
    val iosSimulatorArm64 = iosSimulatorArm64()

    // Source sets
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation("app.cash.sqldelight:runtime:2.2.1")
                implementation("app.cash.sqldelight:coroutines-extensions:2.2.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.activity.compose)
                implementation("app.cash.sqldelight:android-driver:2.2.1")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation("app.cash.sqldelight:sqlite-driver:2.2.1")
            }
        }

        // iOS source set explícito
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64.compilations["main"].defaultSourceSet.dependsOn(this)
            iosArm64.compilations["main"].defaultSourceSet.dependsOn(this)
            iosSimulatorArm64.compilations["main"].defaultSourceSet.dependsOn(this)

            dependencies {
                implementation("app.cash.sqldelight:native-driver:2.2.1")
            }
        }
    }

    // iOS frameworks
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
}

// SQLDelight
sqldelight {
    databases {
        create("MiniPosDatabase") {
            packageName.set("co.com.computingsoftdev.minipos.database")
        }
    }
}

// Android
android {
    namespace = "co.com.computingsoftdev.minipos"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "co.com.computingsoftdev.minipos"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Compose Desktop
compose.desktop {
    application {
        mainClass = "co.com.computingsoftdev.minipos.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "co.com.computingsoftdev.minipos"
            packageVersion = "1.0.0"
        }
    }
}

// Debug
dependencies {
    debugImplementation(libs.compose.uiTooling)
}
