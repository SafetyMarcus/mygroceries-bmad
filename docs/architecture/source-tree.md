# Source Tree

This document outlines the directory structure of the MyGroceries project, which leverages Kotlin Multiplatform to share code across Android, iOS, and Web platforms, alongside a dedicated backend service.

```
mygroceries-bmad/
├── .bmad-core/                       # BMAD agent configurations
├── docs/                             # Project documentation
├── .gitignore
├── .DS_Store
├── .gemini/
├── .git/
├── .idea/
├── .windsurf/
├── mygroceries/                      # Root of the MyGroceries application
│   ├── .gradle/                          # Gradle wrapper files
│   ├── .github/                          # GitHub Actions workflows
│   ├── androidApp/                       # Android-specific application code
│   │   └── src/
│   │       └── main/
│   │           ├── AndroidManifest.xml
│   │           └── kotlin/com/safetymarcus/mygroceries/android/
│   │               └── MainActivity.kt
│   ├── build/                            # Build output directory
│   ├── gradle/                           # Gradle configuration files
│   │   └── wrapper/
│   ├── iosApp/                           # iOS-specific application code
│   │   └── iosApp/
│   │       ├── Assets.xcassets/
│   │       ├── ContentView.swift
│   │       ├── Info.plist
│   │       └── iOSApp.swift
│   ├── kotlin-js-store/                  # Kotlin/JS specific files (e.g., yarn.lock)
│   ├── server/                           # Backend service code
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/com/safetymarcus/mygroceries/server/
│   │           │   └── Application.kt
│   │           └── resources/
│   │               └── application.conf
│   ├── shared/                           # Kotlin Multiplatform shared module
│   │   └── src/
│   │       ├── androidMain/              # Android-specific shared code (e.g., platform-specific implementations)
│   │       │   └── kotlin/
│   │       ├── commonMain/               # Common shared code (business logic, data models, interfaces)
│   │       │   └── kotlin/com/safetymarcus/mygroceries/
│   │       │       ├── data/             # Data models and repositories
│   │       │       ├── domain/           # Business logic and use cases
│   │       │       └── presentation/     # Shared presentation logic (e.g., ViewModels)
│   │       │           └── viewmodel/  # Shared ViewModels for UI state
│   │       │           └── ui/         # Shared Composables (screens, components)
│   │       ├── iosMain/                  # iOS-specific shared code
│   │       │   └── kotlin/
│   │       └── jsMain/                   # Web-specific shared code
│   │           └── kotlin/
│   ├── web/                              # Web application code
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/com/safetymarcus/mygroceries/web/
│   │           │   └── Main.kt
│   │           └── resources/
│   │               └── index.html
│   ├── .gitignore                        # Git ignore file
│   ├── build.gradle.kts                  # Root build script for Gradle
│   ├── gradle.properties                 # Gradle properties
│   ├── gradlew                           # Gradle wrapper script (Linux/macOS)
│   ├── gradlew.bat                       # Gradle wrapper script (Windows)
│   ├── LICENSE                           # Project license
│   ├── README.md                         # Project README
│   └── settings.gradle.kts               # Gradle settings file
```

## Key Directories and Their Contents

*   **`mygroceries-bmad/`**: The root directory of the entire project, containing the MyGroceries application and supporting files.
*   **`.bmad-core/`**: BMAD agent configurations and related files.
*   **`docs/`**: Project documentation, including architecture, PRD, and other specifications.
*   **`mygroceries/`**: The root directory of the MyGroceries application itself.
*   **`androidApp/`**: Contains the Android application. This is where Android-specific UI and platform integrations reside.
*   **`iosApp/`**: Contains the iOS application. This is where iOS-specific UI and platform integrations reside.
*   **`server/`**: Houses the Kotlin Ktor backend service. This includes the main application entry point (`Application.kt`) and configuration files (`application.conf`).
*   **`shared/`**: This is the core of the Kotlin Multiplatform setup.
    *   **`commonMain/`**: Contains the platform-agnostic code, including data models, business logic (use cases), and interfaces for networking and persistence.
    *   **`androidMain/`**: Contains Android-specific implementations for interfaces defined in `commonMain`, or any Android-specific utilities.
    *   **`iosMain/`**: Contains iOS-specific implementations for interfaces defined in `commonMain`, or any iOS-specific utilities.
    *   **`jsMain/`**: Contains Web-specific implementations for interfaces defined in `commonMain`, or any Web-specific utilities.
*   **`web/`**: Contains the Kotlin/JS web application, including the main entry point (`Main.kt`) and the `index.html` file.
*   **`.gradle/`, `gradle/`**: Standard Gradle wrapper and configuration files.
*   **`.github/`**: Contains GitHub Actions workflows for CI/CD.
*   **`build/`**: The output directory for all build artifacts.

## Shared Module Details

The `shared` module is crucial for the multiplatform approach. It's structured to clearly separate concerns:

*   **`data/`**: Defines data classes, interfaces for data sources (e.g., `GroceryRepository`), and potentially implementations that can be shared (e.g., in-memory caches).
*   **`domain/`**: Contains the application's business rules. This includes use cases (interactors) that orchestrate data flow and apply business logic.
*   **`presentation/`**: Holds shared presentation logic, such as `ViewModel`s or `Presenter`s, that prepare data for the UI. Platform-specific UI layers then observe these shared presentation models.

This structure promotes a clean architecture, making the application easier to test, maintain, and scale across different platforms.