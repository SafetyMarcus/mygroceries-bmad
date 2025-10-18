# 8. Source Tree

The project will follow the standard Kotlin Multiplatform directory structure.

```plaintext
my-groceries/
├── .bmad-core/               # BMAD agent configurations
├── build.gradle.kts          # Root build file
├── settings.gradle.kts       # Project settings
├── gradle/
├── docs/
│   ├── prd.md
│   ├── architecture.md       # This document
│   └── front-end-spec.md
│
├── shared/                   # Shared KMP module (common code)
│   ├── src/
│   │   ├── commonMain/
│   │   │   └── kotlin/
│   │   │       └── com/example/mygroceries/
│   │   │           ├── model/          # Data classes (Category, Product, etc.)
│   │   │           ├── remote/         # Ktor HTTP client and API service
│   │   │           └── presentation/   # Shared UI logic and components
│   │   │               ├── viewmodel/  # Shared ViewModels for UI state
│   │   │               └── ui/         # Shared Composables (screens, components)
│   │   ├── androidMain/
│   │   ├── iosMain/
│   │   └── jvmMain/
│
├── server/                   # Ktor backend module
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/
│   │       │   └── com/example/mygroceries/
│   │       │       ├── Application.kt  # Ktor server entry point
│   │       │       ├── plugins/        # Ktor plugin configs (Serialization, Routing)
│   │       │       ├── db/             # Database connection, migrations, repositories
│   │       │       └── routes/         # API route definitions
│   │       └── resources/
│   │           ├── application.conf    # Ktor configuration
│   │           └── db/migration/       # SQL migration files
│
├── web/                      # Web client module (Compose for Web)
│   └── src/
│       └── jsMain/
│           └── kotlin/
│
├── android/                  # Android client module
│   └── src/
│       └── main/
│           └── java/
│
└── ios/                      # iOS client module
    └── src/
        └── main/
            └── kotlin/
```