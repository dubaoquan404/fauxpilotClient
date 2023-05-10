// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

plugins {
  id("java")
  id("org.jetbrains.intellij") version "1.13.3"
}

group = "org.intellij.sdk"
version = "1.0.0"

repositories {
  mavenCentral()
}

java {
//  sourceCompatibility = JavaVersion.VERSION_17
  sourceCompatibility = JavaVersion.VERSION_11
}

// See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
//  version.set("2022.2.5")
  version.set("2021.1.1")
}

tasks {
  buildSearchableOptions {
    enabled = false
  }

  patchPluginXml {
    version.set("${project.version}")
    sinceBuild.set("211")
//    sinceBuild.set("222")
    untilBuild.set("231.*")
  }
}
