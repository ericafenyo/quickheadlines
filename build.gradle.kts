buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath(libs.kotlin.plugin)
    classpath(libs.android.build.tool.plugin)
  }
}

plugins{
  id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

task("clean") {
  delete("${rootProject.layout.buildDirectory}")
}