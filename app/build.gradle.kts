import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.gmsGoogleService)
    alias(libs.plugins.firebaseCrashlytics)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists() && keystorePropertiesFile.canRead()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.muedsa.hatv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.muedsa.hatv"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1-alpha01"

    }

    signingConfigs {
        create("release") {
            if (keystoreProperties.containsKey("hatv.signingConfig.storeFile")) {
                storeFile = file(keystoreProperties["hatv.signingConfig.storeFile"] as String)
                storePassword = keystoreProperties["hatv.signingConfig.storePassword"] as String
                keyAlias = keystoreProperties["hatv.signingConfig.keyAlias"] as String
                keyPassword = keystoreProperties["hatv.signingConfig.keyPassword"] as String
            } else {
                val debugSigningConfig = signingConfigs.getByName("debug")
                storeFile = debugSigningConfig.storeFile
                storePassword = debugSigningConfig.storePassword
                keyAlias = debugSigningConfig.keyAlias
                keyPassword = debugSigningConfig.keyPassword
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    flavorDimensions += "version"

    productFlavors {
        create("demo") {
            dimension = "version"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
        }

        create("full") {
            dimension = "version"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.core.splashscreen)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.runtime)
    implementation(libs.runtime.livedata)
    implementation(libs.runtime.rxjava3)
    implementation(libs.ui)
    implementation(libs.ui)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.tv.foundation)
    implementation(libs.tv.material)

    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    // implementation(libs.leanback)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.transformers)
    // implementation(libs.coil.transformers.gpu)

    implementation(libs.qrcode)

    implementation(libs.timber)

    implementation(libs.media3)
    implementation(libs.media3.ui)
    implementation(libs.media3.hls)

    implementation(libs.akdanmaku)

    implementation(libs.rxjava3)
    implementation(libs.rxjava3.android)

    implementation(libs.jsoup)

    testImplementation(libs.junit4)
    testImplementation(libs.robolectric)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}

kapt {
    correctErrorTypes = true
}