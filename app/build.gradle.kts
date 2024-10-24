import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.cerebrum.app"
  compileSdk = 34
  //MAJOR=0
  //MINOR=0
  //PATCH=1
  //VERSION_CODE=1
  var major = 0
  var minor = 0
  var patch = 1
  var versionCode = 1
  val versionPropsFile = file("version.properties")
  if (versionPropsFile.canRead()) {
    val props = Properties().apply {
      load(FileInputStream(versionPropsFile))
    }
    major = props.getProperty("MAJOR").toInt()
    minor = props.getProperty("MINOR").toInt()
    patch = props.getProperty("PATCH").toInt() + 1
    versionCode = props.getProperty("VERSION_CODE").toInt()
    props.setProperty("MAJOR", major.toString())
    props.setProperty("MINOR", minor.toString())
    props.setProperty("PATCH", patch.toString())
    props.setProperty("VERSION_CODE", versionCode.toString())
    props.store(versionPropsFile.writer(), null)
  }

  defaultConfig {
    applicationId = "com.cerebrum.app"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "$major.$minor.$patch"
    setProperty("archivesBaseName", "Cerebrum-$versionName")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.3"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
  implementation(platform("androidx.compose:compose-bom:2023.03.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3:1.2.0-beta01")
  implementation("androidx.compose.material:material:1.5.4")
  implementation("com.kizitonwose.calendar:compose:2.5.0-alpha01")
  implementation(project(mapOf("path" to ":data")))
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  //implementation("com.github.bumptech.glide:glide:4.16.0")
  implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
  implementation("io.coil-kt:coil-compose:2.5.0")
  implementation("androidx.navigation:navigation-compose:2.7.6")

  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

  implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("com.googlecode.libphonenumber:libphonenumber:8.12.37")

}