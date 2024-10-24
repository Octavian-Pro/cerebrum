// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.android.application") version "8.1.2" apply false
  id("org.jetbrains.kotlin.android") version "1.8.10" apply false
  id("com.android.library") version "8.1.2" apply false
  id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

buildscript {
  repositories {
    mavenCentral()
    // Note: 2.9.0 and older are available on jcenter()
  }
  dependencies {
    // Android Gradle Plugin 3.4.0 or later supported.
    classpath("com.android.tools.build:gradle:8.0.0")
    classpath("io.objectbox:objectbox-gradle-plugin:3.7.1")
  }
}