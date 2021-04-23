/*
 * Copyright 2021 Google, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("net.ltgt.gradle.incap:incap:0.2")
    kapt("net.ltgt.gradle.incap:incap-processor:0.2")
}
