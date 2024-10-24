plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.apollographql.apollo3")
    id("kotlinx-serialization")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.goudurixx.pokedex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.goudurixx.pokedex"
        minSdk = 27
        targetSdk = 34
        versionCode = 100001
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            proguardFiles(
                // Includes the default ProGuard rules files that are packaged with
                // the Android Gradle plugin. To learn more, go to the section about
                // R8 configuration files.
                getDefaultProguardFile("proguard-android-optimize.txt"),

                // Includes a local, custom Proguard rules file
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
//            isDebuggable = true
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion =  "1.5.9"
    }

}

apollo {
    service("service") {
        packageName.set("com.goudurixx.pokedex")
    }
}

dependencies {
    val navigation = "2.7.7"
    val hilt = "2.48"
    val ktor = "2.3.2"
    val room = "2.6.1"
    val material3 = "1.2.0"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
//    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9") TODO FIX
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // material icons
    implementation("androidx.compose.material:material-icons-extended:1.6.2")

    //material 3
    implementation("androidx.compose.material3:material3:$material3")
    implementation("androidx.compose.material3:material3-window-size-class:$material3")


    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation")
    implementation("androidx.navigation:navigation-compose:$navigation")


    // data injection Hilt
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")

    // service ktor
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-client-json:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-client-logging:$ktor")

    // Apollo graphql
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // hilt x compose navigation
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    // image fetch coil
    implementation("io.coil-kt:coil-compose:2.4.0")
//    implementation("io.coil-kt:coil-svg:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")


    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation ("androidx.palette:palette-ktx:1.0.0")

//    // pager
//    implementation ("com.google.accompanist:accompanist-pager:0.21.2-beta")
//    implementation ("com.google.accompanist:accompanist-pager-indicators:0.21.2-beta")

    // Paging 3
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")
    // room
    implementation("androidx.room:room-runtime:$room")
    annotationProcessor("androidx.room:room-compiler:$room")
    kapt("androidx.room:room-compiler:$room")
    implementation ("androidx.room:room-ktx:$room")
    implementation ("androidx.room:room-paging:$room")

    implementation("com.google.code.gson:gson:2.10")

    implementation("androidx.compose.animation:animation-graphics-android:1.7.0-alpha06")


}