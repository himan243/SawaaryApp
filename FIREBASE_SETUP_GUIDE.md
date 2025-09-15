# Firebase Setup Guide for Sawaary

## 1. Firebase Authentication Setup

### Step 1: Enable Authentication Methods

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: `sawaary-7a9ce`
3. Navigate to **Authentication** → **Sign-in method**

#### Enable Email/Password Authentication:
1. Click on **Email/Password**
2. Toggle **Enable** to ON
3. Optionally enable **Email link (passwordless sign-in)**
4. Click **Save**

#### Configure Email Templates (Optional):
1. Go to **Authentication** → **Templates**
2. Customize email verification and password reset templates
3. Add your app logo and branding

### Step 2: Set Up User Management

1. Go to **Authentication** → **Users**
2. Configure user blocking policies if needed
3. Set up email verification requirements

## 2. Firebase Realtime Database Setup

### Step 1: Create Database

1. Go to **Realtime Database**
2. Click **Create Database**
3. Choose **Start in test mode** (for development)
4. Select a location (choose closest to your users)
5. Click **Done**

### Step 2: Configure Database Rules

1. Go to **Realtime Database** → **Rules**
2. Replace the default rules with the rules from `firebase-database-rules.json`
3. Click **Publish**

### Step 3: Database Structure

Your database will have this structure:
```
sawaary-7a9ce/
├── users/
│   └── {uid}/
│       ├── uid: string
│       ├── email: string
│       ├── name: string
│       ├── userType: "DRIVER" | "TRAVELER"
│       ├── phoneNumber: string
│       ├── isOnline: boolean
│       └── lastSeen: timestamp
├── drivers/
│   └── {uid}/
│       ├── uid: string
│       ├── licensePlate: string
│       ├── vehicleNumber: string
│       ├── driverLicense: string
│       ├── isVerified: boolean
│       ├── isActive: boolean
│       ├── currentLocation: Location
│       └── currentTrip: Trip
├── bus_locations/
│   └── {busId}/
│       ├── busId: string
│       ├── location: Location
│       └── isActive: boolean
├── trips/
│   └── {tripId}/
│       ├── tripId: string
│       ├── driverId: string
│       ├── busId: string
│       ├── route: Route
│       ├── startTime: timestamp
│       ├── status: "PENDING" | "IN_PROGRESS" | "PAUSED" | "COMPLETED"
│       └── currentLocation: Location
└── trip_history/
    └── {uid}/
        └── {tripId}/
            └── trip data...
```

## 3. Firebase Cloud Messaging Setup

### Step 1: Enable Cloud Messaging

1. Go to **Cloud Messaging**
2. The service is automatically enabled
3. Note down the **Server Key** (you'll need this for server-side notifications)

### Step 2: Configure Notification Settings

1. Go to **Project Settings** → **Cloud Messaging**
2. Add your app's SHA-1 fingerprint for Android
3. Configure notification channels

## 4. Firebase Analytics Setup

### Step 1: Enable Analytics

1. Go to **Analytics** → **Dashboard**
2. Analytics is automatically enabled
3. Configure custom events for tracking user behavior

### Step 2: Set Up Custom Events

Track these events in your app:
- `trip_started`
- `trip_completed`
- `bus_tracked`
- `location_shared`
- `notification_received`

## 5. Security Configuration

### Step 1: Database Security Rules

The provided rules ensure:
- Users can only read/write their own data
- Drivers can update their location and trip data
- All authenticated users can read bus locations
- Public data (routes, stops) is read-only for authenticated users

### Step 2: Authentication Security

1. Go to **Authentication** → **Settings**
2. Configure:
   - **Authorized domains** (add your domain if using web)
   - **Email action handlers** (for password reset, etc.)
   - **User blocking** policies

## 6. Testing Your Setup

### Step 1: Test Authentication

1. Run your app
2. Try to register a new user
3. Check **Authentication** → **Users** in Firebase Console
4. Verify user appears in the database

### Step 2: Test Database

1. Start a trip as a driver
2. Check **Realtime Database** for data updates
3. Verify location updates are being written

### Step 3: Test Notifications

1. Send a test notification from Firebase Console
2. Verify it appears on the device

## 7. Production Configuration

### Step 1: Update Database Rules for Production

```json
{
  "rules": {
    // More restrictive rules for production
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid && auth.token.email_verified == true"
      }
    }
  }
}
```

### Step 2: Enable App Check (Recommended)

1. Go to **App Check**
2. Enable for your Android app
3. Configure reCAPTCHA or other verification methods

### Step 3: Set Up Monitoring

1. Go to **Crashlytics** (if enabled)
2. Set up **Performance Monitoring**
3. Configure **Remote Config** for feature flags

## 8. Common Issues and Solutions

### Issue: Authentication not working
**Solution**: Check that Email/Password is enabled in Authentication settings

### Issue: Database rules blocking writes
**Solution**: Verify user is authenticated and has proper permissions

### Issue: Location updates not appearing
**Solution**: Check that the driver is properly authenticated and has write permissions

### Issue: Notifications not received
**Solution**: Verify FCM token is being generated and stored in database

## 9. Monitoring and Maintenance

### Daily Tasks:
- Monitor authentication failures
- Check database usage and costs
- Review error logs

### Weekly Tasks:
- Analyze user engagement metrics
- Review trip completion rates
- Check for any security issues

### Monthly Tasks:
- Update security rules if needed
- Review and optimize database structure
- Analyze performance metrics

## 10. Cost Optimization

### Database Optimization:
- Use `.indexOn` for frequently queried fields
- Implement data pagination for large datasets
- Set up data retention policies

### Storage Optimization:
- Compress images before upload
- Use appropriate image sizes
- Implement cleanup routines for old data

This setup will provide a robust foundation for your bus tracking app with proper security and scalability.

