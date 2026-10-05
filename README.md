# HearingTest

HearingTest is a native Android application written in Java as a graduation project. It provides tone-based hearing tests, stores individual results and displays audiograms for the left and right ears.

The main feature is comparative analysis against **both population reference norms and the user’s personal reference norms**. Population norms provide a reference based on age and sex, while personal norms are calculated from the user’s previous test results.

## Features

- Create, select and edit user profiles.
- Run standard and advanced hearing tests with separate left and right audio channels.
- Test eight frequencies: **125, 250, 500, 1,000, 2,000, 3,000, 4,000 and 8,000 Hz**.
- Generate stereo PCM audio using Android `AudioTrack`.
- Display test progress through `ViewModel` and `LiveData`.
- Save user profiles, audiograms and personal reference norms locally in SQLite.
- Browse saved results and view audiogram charts.
- Compare results with population and personal reference norms.
- Retain the selected user using `SharedPreferences`.
- Provide an optional WebView screen for connecting to a separate hearing-monitoring web application.

## Comparison with reference norms

### Population reference norms

`PopulationNorm` selects reference values from `AgeNormConstants` using the user’s age and sex. The current implementation uses the groups 20–29, 30–39, 40–49 and 50–59.

### Personal reference norms

`Median` calculates a reference audiogram from the selected user’s stored audiograms. It computes the median separately for each frequency and each ear; for an even number of records, it averages the two middle values using integer arithmetic.

Personal reference calculation can be requested from the preparation screen. The current application also recalculates it automatically when the number of saved audiograms is between three and five. With a single stored result, that result is used as the reference.

### Result analysis

`AnalyzeAudiogram` compares the measured values with the selected reference separately for each ear and produces a textual summary. The detailed result screen allows switching between population and personal references when a personal reference is available.

## Technology stack

| Area | Technologies |
| --- | --- |
| Language | Java, Java 8 source compatibility |
| UI | XML layouts, AndroidX Fragments, ViewPager2, Material Components |
| State | ViewModel, LiveData, SharedPreferences |
| Audio | AudioTrack, stereo 16-bit PCM, 44.1 kHz sample rate |
| Local storage | SQLite, SQLiteOpenHelper |
| Charts | AndroidPlot |
| Other dependencies | Jackson, Lombok |
| Build | Gradle 7.5, Android Gradle Plugin 7.4.2 |

## Build and run

### Requirements

- Android Studio with support for the project’s Gradle configuration.
- JDK 11 for the existing Android Gradle Plugin 7.4.2 configuration.
- Android SDK Platform 31.
- An Android device for checking actual audio playback and channel separation. Use Android 8.0 or later for this prototype.

### Android Studio

1. Clone or download the repository.
2. Open the project root containing `settings.gradle` and the `app` directory.
3. Install SDK Platform 31 if prompted and set the Gradle JDK to JDK 11.
4. Sync the project with Gradle.
5. Select the `app` run configuration and a device, then click **Run**.

### Command line

From the project root on Windows:

```powershell
.\gradlew.bat assembleDebug
```

On Linux or macOS:

```bash
chmod +x gradlew
./gradlew assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

The first build requires network access to download Gradle and dependencies. An Android SDK must be configured locally; do not commit your machine-specific `local.properties` file.

## Using the application

1. Create or select a user profile and enter the information needed to choose a population reference.
2. Open the test preparation screen and connect stereo headphones.
3. Choose whether to calculate a personal reference and select the standard or advanced test.
4. Respond using the test controls when a tone is audible; the advanced mode also provides a response for an inaudible tone.
5. Open the saved result to inspect the audiogram and detailed comparison.
6. Switch between population and personal references when available.

## Optional web application connection

Local profiles and results are stored in SQLite. The server connection screen is separate from the local test workflow.

`ConnectToServerFragment.java` currently contains a hardcoded development address:

```text
http://192.168.1.101:8080/hearing_monitoring/login
```

To try this feature, run the separate web application, replace the address in `webView.loadUrl(...)` with your server address, and ensure the Android device can reach it. The backend is not included in this Android project.

## Source organization

All package paths below are relative to `app/src/main/java/com/example/hearingtest/`.

| Path | Responsibility |
| --- | --- |
| `MainActivity.java` | Screen navigation and coordination of test results |
| `fragment/` | User profiles, preparation, tests, results and server connection screens |
| `viewmodel/` | Test state and observable progress |
| `tone/GenerationTone.java` | PCM tone generation and AudioTrack management |
| `audiogram/Audiogram.java` | Audiogram models |
| `norm/` | Population reference selection and personal median calculation |
| `analize/AnalyzeAudiogram.java` | Comparison with reference values |
| `db/` | SQLite schema and data access |
| `adapter/` | Chart and detail pager adapters |
| `users/` | User models and collections |
| `constants/` | UI strings and population reference data |

Layouts and other Android resources are stored in `app/src/main/res/`.

Areas for further development include audio generation and calibration, lifecycle and cancellation handling, fragment state restoration, headphone detection, older Android compatibility, and unit tests for median and comparison logic.

The application has not been clinically validated and is not a substitute for professional audiometry. Its numerical sound-level values should not be treated as calibrated hearing thresholds: results depend on the device, headphones and environment.

## Author

**Anastasiia Kharchenko**  
[GitHub](https://github.com/kharchenkoanastasiia1)
