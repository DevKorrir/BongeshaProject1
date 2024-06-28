plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "dev.korryr.bongesha"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.korryr.bongesha"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.test.espresso:espresso-core:3.6.1@aar")
    //implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:2.8.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt:coil-compose:2.4.0")
    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:33.1.1"))

    // Declare the dependency for the Firebase Authentication library
    implementation ("com.google.firebase:firebase-auth-ktx")

    //Biometric fingerprint
    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

    //Google sign in

    implementation ("androidx.core:core-ktx:1.13.1")
    //implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation ("androidx.compose.material:material:1.6.8")
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")

    //implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    //implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation ("io.coil-kt:coil-compose:2.4.0")
    //searchbar
    implementation ("androidx.core:core-ktx:1.13.1")
    //implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation ("androidx.compose.ui:ui:$ 1.5.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:$1.5.1")
    implementation ("androidx.compose.material:material:1.6.8")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:$1.5.1")
    debugImplementation ("androidx.compose.ui:ui-tooling:$1.5.1")
    //noinspection GradleDependency
    debugImplementation ("androidx.compose.ui:ui-test-manifest:$1.5.1")

    //OpenAI interface
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

}
dependencies {

}
