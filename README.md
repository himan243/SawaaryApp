# Sawaary - Real-time Bus Tracking App

A comprehensive Android application that enables real-time bus tracking for both drivers and passengers, built with Jetpack Compose and Firebase.

## Features

### For Drivers
- **Trip Management**: Create, start, pause, and end trips
- **Real-time Location Sharing**: Share location with passengers every 30 seconds
- **Route Setup**: Define start and end points with waypoints
- **Passenger Count Tracking**: Monitor passenger capacity
- **Trip History**: View completed trips and statistics
- **Driver Profile**: Manage driver information and settings

### For Passengers
- **Real-time Bus Tracking**: View live bus locations on interactive maps
- **Nearby Bus Discovery**: Find buses within 20km radius
- **ETA Predictions**: Get accurate arrival time estimates
- **Route Information**: View bus routes and stops
- **Push Notifications**: Receive alerts for bus arrivals
- **Bus Details**: View bus information, capacity, and status

## Technical Architecture

### Tech Stack
- **Frontend**: Jetpack Compose, Material Design 3
- **Backend**: Firebase (Authentication, Realtime Database, Cloud Messaging)
- **Maps**: Google Maps Android API
- **Location Services**: Google Play Services Location
- **Navigation**: Navigation Compose
- **Architecture**: MVVM with Repository pattern

### Key Components
- **Authentication System**: Firebase Auth with user type selection
- **Location Tracking Service**: Background service for continuous GPS tracking
- **Real-time Database**: Firebase Realtime Database for live location updates
- **Push Notifications**: Firebase Cloud Messaging for alerts
- **Maps Integration**: Google Maps with custom markers and routes

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Google Play Services
- Firebase project
- Google Maps API key

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Sawaary
```

### 2. Firebase Setup

#### Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named "Sawaary"
3. Enable Authentication, Realtime Database, and Cloud Messaging

#### Configure Authentication
1. In Firebase Console, go to Authentication > Sign-in method
2. Enable Email/Password authentication
3. Configure any additional providers if needed

#### Setup Realtime Database
1. Go to Realtime Database
2. Create database in test mode (for development)
3. Set up security rules:

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "drivers": {
      "$uid": {
        ".read": "auth != null",
        ".write": "$uid === auth.uid"
      }
    },
    "bus_locations": {
      ".read": "auth != null",
      ".write": "auth != null"
    },
    "trips": {
      "$tripId": {
        ".read": "auth != null",
        ".write": "auth != null"
      }
    }
  }
}
```

#### Download Configuration
1. In Firebase Console, go to Project Settings
2. Add Android app with package name: `com.adtu.sawaary`
3. Download `google-services.json`
4. Replace the template file in `app/google-services.json`

### 3. Google Maps Setup

#### Get API Key
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Maps SDK for Android
4. Create API key with Android app restrictions
5. Add package name: `com.adtu.sawaary`
6. Add SHA-1 fingerprint of your debug keystore

#### Configure API Key
1. Open `app/src/main/res/values/strings.xml`
2. Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual API key

### 4. Build and Run

#### Debug Build
```bash
./gradlew assembleDebug
```

#### Release Build
```bash
./gradlew assembleRelease
```

## Project Structure

```
app/
├── src/main/java/com/adtu/sawaary/
│   ├── auth/                    # Authentication components
│   │   ├── AuthRepository.kt
│   │   └── AuthViewModel.kt
│   ├── data/model/              # Data models
│   │   ├── User.kt
│   │   ├── Driver.kt
│   │   ├── Bus.kt
│   │   ├── Location.kt
│   │   ├── Route.kt
│   │   └── BusStop.kt
│   ├── navigation/              # Navigation components
│   │   ├── Screen.kt
│   │   └── SawaaryNavigation.kt
│   ├── service/                 # Background services
│   │   ├── LocationTrackingService.kt
│   │   └── FirebaseMessagingService.kt
│   ├── ui/screens/              # UI screens
│   │   ├── auth/                # Authentication screens
│   │   ├── driver/              # Driver-specific screens
│   │   └── traveler/            # Passenger screens
│   └── MainActivity.kt
├── src/main/res/                # Resources
└── google-services.json         # Firebase configuration
```

## Key Features Implementation

### Location Tracking
- **Background Service**: Continuous GPS tracking with 30-second intervals
- **Battery Optimization**: Efficient location updates to minimize battery drain
- **Permission Handling**: Runtime permission requests for location access
- **Accuracy**: ±10 meters GPS accuracy with fallback to network location

### Real-time Updates
- **Firebase Realtime Database**: Live location synchronization
- **WebSocket Connection**: Persistent connection for real-time updates
- **Offline Support**: Basic offline map viewing and cached routes
- **Data Retention**: Location data automatically deleted after 7 days

### Security & Privacy
- **Data Encryption**: All location data encrypted in transit and at rest
- **User Consent**: Granular permission controls
- **Driver Verification**: License plate validation system
- **GDPR Compliance**: Data retention policies and user rights

## Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use Material Design 3 components
- Implement proper error handling
- Add comprehensive logging

### Testing
- Unit tests for business logic
- UI tests for critical user flows
- Integration tests for Firebase services
- Performance testing for location updates

### Performance
- Optimize location update frequency
- Implement proper caching strategies
- Monitor battery usage
- Minimize network requests

## Deployment

### Pre-deployment Checklist
- [ ] Update API keys for production
- [ ] Configure Firebase security rules
- [ ] Test on multiple devices and Android versions
- [ ] Verify location permissions on different Android versions
- [ ] Test offline functionality
- [ ] Validate push notifications
- [ ] Performance testing with multiple concurrent users

### Release Process
1. Update version code and name in `build.gradle.kts`
2. Generate signed APK/AAB
3. Upload to Google Play Console
4. Configure staged rollout
5. Monitor crash reports and user feedback

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation wiki

## Roadmap

### Phase 2 Features
- [ ] Enhanced notifications with custom sounds
- [ ] Route optimization algorithms
- [ ] Historical data and analytics dashboard
- [ ] Multi-language support
- [ ] Dark mode theme

### Future Enhancements
- [ ] Integration with public transport APIs
- [ ] Predictive arrival times using ML
- [ ] Multi-modal transport support
- [ ] Passenger capacity tracking
- [ ] Driver rating system
- [ ] Route planning and optimization

