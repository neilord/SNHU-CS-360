# Inventory Manager App

## Summary of the App

This mobile application was created to manage inventory easily and efficiently. The app allows users to log in, add new items, update and delete them, and receive SMS notifications if inventory levels are low. It was designed to meet the need for a simple inventory tracking tool that is easy to navigate and understand. It supports users by giving them control over their data while keeping the interface clean and fast to use.

## User Needs and UI Design

The app needed to provide:
- A **login and account creation screen** for new and existing users
- A **main inventory screen** showing items in a clear grid layout
- A way to **add, update, and delete** items
- **SMS notifications** to alert users when inventory is low

The UI was designed with clear buttons, simple dialogs, and large readable text. All screens have a logical flow based on user actions: login first, then manage inventory. Buttons like "Create Account" and "Add Item" are easy to find. Success messages and toasts give users feedback after every action. This approach was successful because it made the app intuitive, even for new users.

## Coding Approach

I started with the UI screens and layouts, then added functionality one piece at a time. I created a reusable `DatabaseHelper` to manage all SQLite database actions. I used `RecyclerView` for the grid and set up an adapter to display dynamic data. To manage SMS permission, I used Android's new Activity Result APIs for smoother permission requests. I tested after every small change to catch errors early. This strategy of building step-by-step and testing frequently is something I will continue to use on future projects.

## Testing and Debugging

I tested the app manually on the Android Emulator. I tested logging in, creating accounts, adding, updating, and deleting inventory items, and granting/denying SMS permissions. I also tested edge cases, like empty fields or duplicate usernames. Testing helped me find and fix small bugs early, and it made sure the app could handle real user actions without crashing.

Testing is important because it shows you how users might break your app by doing unexpected things. It also confirms that permissions and database connections are working properly, which are critical for a mobile app.

## Challenges and Innovation

One challenge was making sure that users could continue using the app even if they denied SMS permissions. I had to handle permission checks carefully so the app wouldn't crash or lock users out. I solved this by checking permissions before every SMS attempt and updating the UI accordingly.

## Strongest Area

The part of the app that demonstrates my skills best is the integration between the **SQLite database** and the **dynamic UI grid**. I successfully built a persistent storage system where users can add, delete, and view inventory items, and the RecyclerView updates immediately. I also applied best practices like clean code, meaningful variable names, and helpful comments to make the project easy to read and maintain.
