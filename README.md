# Mathly - AI-Powered Math Solver

🧮 **Mathly** is an educational Android app that helps users solve mathematical equations step-by-step using AI.

## Features

- 📸 **Scan Equations**: Use your camera to scan handwritten or printed equations
- ⌨️ **Type Equations**: Manually input equations for solving
- 🤖 **AI-Powered Solutions**: Get detailed step-by-step solutions using OpenAI's GPT API
- 📱 **Modern UI**: Beautiful Material 3 design with Jetpack Compose
- 💾 **Local Storage**: Save and view your recent equations and solutions

## Tech Stack

### Architecture
- **Clean Architecture** with clear separation of concerns
- **MVVM** pattern with Jetpack Compose
- **Repository Pattern** for data management

### Core Technologies
- **Kotlin** - Primary language
- **Jetpack Compose** - Modern UI toolkit
- **Hilt** - Dependency injection
- **Room** - Local database
- **Retrofit** - Network communication
- **Navigation Compose** - Navigation
- **Coroutines & Flow** - Asynchronous programming

### AI & ML
- **OpenAI GPT API** - Equation solving
- **Google ML Kit OCR** - Text recognition from images
- **CameraX** - Camera integration

## Project Structure

```
app/src/main/java/com/zahid/mathly/
├── data/
│   ├── local/           # Room database, entities, DAOs
│   ├── remote/          # API interfaces and services
│   └── repository/      # Repository implementations
├── di/                  # Hilt dependency injection modules
├── domain/
│   ├── model/           # Domain entities
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic use cases
├── presentation/
│   ├── navigation/      # Navigation setup
│   ├── ui/
│   │   ├── screens/     # Compose UI screens
│   │   ├── state/       # UI state classes
│   │   └── theme/       # Material 3 theme
│   └── viewmodel/       # ViewModels
└── utils/               # Utility classes
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+
- OpenAI API key

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/mathly.git
   cd mathly
   ```

2. **Add your OpenAI API key**
   - Open `app/src/main/java/com/zahid/mathly/data/repository/EquationRepositoryImpl.kt`
   - Replace `"your-openai-api-key-here"` with your actual OpenAI API key

3. **Build and run**
   ```bash
   ./gradlew build
   ```

### Configuration

#### OpenAI API Setup
1. Get your API key from [OpenAI Platform](https://platform.openai.com/api-keys)
2. Update the API key in `EquationRepositoryImpl.kt`
3. The app will use GPT-3.5-turbo for equation solving

#### Camera Permissions
The app requests camera permissions for scanning equations. Grant permissions when prompted.

## Usage

### Main Screen
- **Scan Equation**: Tap to open camera and scan handwritten/printed equations
- **Type Equation**: Manually enter equations for solving

### Input Screen
- Enter mathematical expressions like:
  - `2x + 5 = 13`
  - `x² - 4x + 4 = 0`
  - `3x + 2y = 10`
  - `∫(x² + 2x)dx`

### Result Screen
- View step-by-step solutions
- See calculations and intermediate results
- Get the final answer

## Architecture Details

### Clean Architecture Layers

#### Presentation Layer
- **Screens**: Compose UI components
- **ViewModels**: Handle UI state and business logic
- **Navigation**: Screen navigation using Navigation Compose

#### Domain Layer
- **Models**: Core business entities (Equation, Solution)
- **UseCases**: Business logic operations
- **Repository Interfaces**: Data access contracts

#### Data Layer
- **Remote**: OpenAI API integration
- **Local**: Room database for caching
- **Repository Implementations**: Data orchestration

### Key Components

#### EquationRepository
```kotlin
interface EquationRepository {
    suspend fun solveEquation(equation: Equation): Result<Solution>
    suspend fun saveEquation(equation: Equation): Result<String>
    fun getRecentEquations(): Flow<List<Equation>>
}
```

#### OpenAIService
```kotlin
class OpenAIService @Inject constructor(
    private val api: OpenAIApi
) {
    suspend fun solveEquation(equation: String, apiKey: String): Result<Solution>
}
```

## Dependencies

### Core Dependencies
- **Hilt**: Dependency injection
- **Room**: Local database
- **Retrofit**: Network communication
- **Navigation Compose**: Navigation
- **Coroutines**: Asynchronous programming

### UI Dependencies
- **Jetpack Compose**: Modern UI toolkit
- **Material 3**: Design system
- **CameraX**: Camera integration

### AI/ML Dependencies
- **OpenAI API**: Equation solving
- **ML Kit**: Text recognition

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- OpenAI for providing the GPT API
- Google for ML Kit and CameraX
- Android team for Jetpack Compose

## Support

For support, email support@mathly.app or create an issue in this repository.

---

**Note**: This is a demo implementation. For production use, ensure proper API key management and error handling. 