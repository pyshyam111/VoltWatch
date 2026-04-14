# ⚡ VoltWatch - Battery Management & Analytics App

VoltWatch is a modern Android application that provides real-time battery insights, smart alerts, and historical analytics using a clean MVVM architecture and Jetpack Compose UI.

---

## 🚀 Features

### 🔋 Real-Time Dashboard
- Battery Percentage
- Temperature (°C)
- Voltage (V)
- Battery Technology (Li-ion)
- Dynamic UI with color-based battery levels

### 📊 Battery Analytics
- Automatic battery logging every 15 minutes
- History screen with list + graphical chart
- Room Database for persistent storage

### 🔔 Smart Alert System
- User-defined battery percentage alert
- Real-time notification trigger
- Android 13+ notification permission support

### ⚙️ Background Processing
- WorkManager for periodic background tasks
- BroadcastReceiver for real-time battery updates

### 🤖 AI Smart Insights (Bonus)
- Battery health suggestions
- Usage recommendations
- Intelligent analysis of battery patterns

---

## 🧱 Architecture

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** MVVM
- **Async:** Coroutines + Flow
- **Database:** Room
- **Storage:** DataStore
- **Background Tasks:** WorkManager
- **System APIs:** BatteryManager + BroadcastReceiver

---

## 📂 Project Structure


com.example.voltwatch
│
├── data/
│ ├── local/ # Room Database
│ ├── datastore/ # DataStore (Alert storage)
│ └── repository/ # Repository layer
│
├── domain/
│ └── usecase/ # Business logic
│
├── ui/
│ ├── dashboard/ # Home screen
│ ├── history/ # Battery logs UI
│ ├── alert/ # Alert setup UI
│ ├── components/ # Reusable UI components
│ └── navigation/ # Navigation graph
│
├── receiver/ # Battery BroadcastReceiver
├── worker/ # WorkManager tasks
├── viewmodel/ # ViewModel layer
├── utils/ # Helpers (Notification, AI)
└── MainActivity.kt
---
## 🔧 Setup Instructions

1. Clone the repository:
git clone https://github.com/pyshyam111/VoltWatch.git
