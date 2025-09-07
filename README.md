# Fast Shopping Mobile App - Test Automation Framework

## 📱 Overview
This project contains automated tests for the **Fast Shopping Mobile App** using **Java + Appium + TestNG** framework following the **Page Object Model (POM)** design pattern. The framework is specifically designed based on the actual app UI and user flows.

## 🎯 Test Scenarios Automated

Based on actual app screenshots and user flows:

### Scenario 1: Create List → Add Items → Mark Complete → Archive
- Create a new shopping list with random items
- Add multiple items to the list
- Mark all items as completed
- Archive the completed list when prompted

### Scenario 2: Create List → Add Items → Edit Item → Remove Item
- Create a new shopping list with random items
- Add multiple items to the list
- Edit a random item in the list
- Remove a random item from the list

### Scenario 3: Create List → Add Items → Remove All Items
- Create a new shopping list with random items
- Add multiple items to the list
- Remove all items from the list
- Handle empty list state

## 🏗️ Framework Architecture

### Streamlined Page Object Design
Based on actual app analysis, the framework uses only **2 main page objects**:

```
src/main/java/com/automation/pages/
├── BasePage.java              # Common page operations
├── ShoppingListsPage.java     # Home screen, lists overview, create lists
└── ListItemsPage.java         # Item management, add/edit/remove items
```

### Complete Project Structure
```
fast-shopping-mobile-automation/
├── pom.xml                    # Maven configuration
├── README.md                  # This file
├── .gitignore                 # Git ignore rules
├── src/
│   ├── main/java/com/automation/
│   │   ├── base/
│   │   │   └── BaseTest.java          # Driver setup & test hooks
│   │   ├── pages/
│   │   │   ├── BasePage.java          # Base page operations
│   │   │   ├── ShoppingListsPage.java # Lists & creation (Screenshots 1,2,3)
│   │   │   └── ListItemsPage.java     # Item management (Screenshots 4,5,6,7,8)
│   │   ├── utils/
│   │   │   ├── ConfigReader.java      # Configuration management
│   │   │   ├── WaitUtils.java         # Custom wait utilities
│   │   │   ├── ScreenshotUtils.java   # Screenshot capture
│   │   │   └── ExtentReportManager.java # Test reporting
│   │   └── listeners/
│   │       ├── ExtentReportListener.java  # Report listener
│   │       └── ScreenshotListener.java    # Screenshot listener
│   └── test/java/com/automation/
│       └── tests/
│           └── UpdatedShoppingListTest.java # Main test class
├── test-output/               # Generated reports & screenshots
├── apps/                      # APK storage
├── scripts/                   # Helper scripts
└── docs/                      # Documentation
```

## 🛠️ Technology Stack
- **Language**: Java 11+
- **Mobile Automation**: Appium 8.6.0
- **Test Framework**: TestNG 7.8.0
- **Build Tool**: Maven 3.6+
- **Reporting**: Extent Reports 5.0.9
- **Assertions**: AssertJ 3.24.2
- **Design Pattern**: Page Object Model (POM)

## 📋 Prerequisites

### Required Software
1. **Java JDK 11 or higher**
   ```bash
   java -version
   javac -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **Node.js & npm** (for Appium)
   ```bash
   node -v
   npm -v
   ```

4. **Appium Server**
   ```bash
   npm install -g appium@next
   appium driver install uiautomator2
   ```

5. **Android Studio & SDK**
   - Install Android Studio
   - Set up Android SDK and AVD
   - Or connect physical Android device

### Environment Variables
```bash
export JAVA_HOME=/path/to/java
export ANDROID_HOME=/path/to/android-sdk  
export PATH=$PATH: