Revvo 🏍️
Motorcycle Community & Group Ride App

Revvo is a mobile application designed to help motorcycle riders:

Discover and join group rides

Track rides using GPS

Share live ride location

Build riding communities

The project is built using Kotlin + Jetpack Compose + Firebase + Google Maps.

Project Status

Current phase:

Phase 1 — Base App Setup

Completed:

Android Studio project setup

Project architecture created

GitHub repository configured

Development branch workflow established

Project Architecture
com.revvo
│
├── ui
│   ├── screens
│   ├── components
│   └── navigation
│
├── data
│   ├── model
│   └── repository
│
├── viewmodel
│
├── services
│   ├── firebase
│   ├── maps
│   └── location
│
└── MainActivity.kt
Folder Responsibilities

ui/

App UI elements

screens → full screens
components → reusable UI elements
navigation → app navigation logic

data/

Data models and repositories

model → data classes
repository → data access logic

viewmodel/

Business logic

Connect UI with data

services/
External services

firebase → backend
maps → Google Maps integration
location → GPS tracking
Branch Workflow

We use 3 levels of branches.

main
↑
dev
↑
feature branches
Branch purposes

main

Stable production code

dev

Active development branch

feature branches

feature-home-ui
feature-map
feature-firebase-auth
feature-rides
Development Workflow
Step 1 — Pull latest code

Before starting work:

git checkout dev
git pull origin dev
Step 2 — Create a feature branch

Example:

git checkout -b feature-home-screen
Step 3 — Work on the feature

Commit regularly:

git add .
git commit -m "Added home screen UI"
Step 4 — Push feature branch
git push origin feature-home-screen
Step 5 — Merge into dev

After testing, merge feature into dev.

Development Phases
Phase 1 — Base UI

Goal:

App runs with UI screens

Features:

Home Screen

Ride Card

Bottom Navigation

Create Ride Screen

Phase 2 — Backend Integration

Goal:

Connect Firebase backend

Features:

Firebase Authentication

Firestore database

User profiles

Ride storage

Phase 3 — Maps & Tracking

Goal:

Ride tracking system

Features:

Google Maps integration

GPS tracking

Route display

Distance calculation

Phase 4 — Live Group Ride Mode

Goal:

Real-time ride experience

Features:

Live rider location

Group ride map

SOS emergency button

Team Responsibilities
Member 1

Project setup & navigation

MainActivity
AppNavHost
Routes
Member 2

UI components

RideCard
StatsCard
BottomNavigationBar
Member 3

Screens

HomeScreen
CreateRideScreen
RideDetailsScreen
ProfileScreen
Member 4

Data layer

Ride model
User model
ViewModels
Repositories
Member 5

Services

Firebase
Maps
Location tracking
Tech Stack
Kotlin
Jetpack Compose
Firebase
Google Maps API
Git + GitHub
Running the App

Clone repo

git clone https://github.com/Yashr23-ui/Revvo.git

Open in Android Studio

Run the app on emulator or device.

Future Features

Ride leaderboards

Route discovery

Bike garage

Ride photo feed

Contribution Guidelines

Work only on feature branches

Do not push directly to main

Always pull latest dev branch

Next Milestone
Implement Revvo Home Screen UI
How to add this to the repo

Create a file in the root folder:

README.md

Paste the content above.

Then run:

git add README.md
git commit -m "Added project README with architecture and workflow"
git push origin dev