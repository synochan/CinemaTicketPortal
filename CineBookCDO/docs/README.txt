===================================
CINEBOOK CDO - CINEMA BOOKING SYSTEM
===================================

A Java Swing-based cinema ticket booking system for cinemas in Cagayan de Oro (CDO), Philippines.

This application is a learning project designed to demonstrate basic concepts in Java Swing GUI development,
object-oriented programming, and simple data management without a database.

---------------------------------
HOW TO COMPILE AND RUN
---------------------------------

To compile the application, navigate to the CineBookCDO directory and run:

javac -d bin src/**/*.java

To run the application, execute:

java -cp bin Main

---------------------------------
SYSTEM REQUIREMENTS
---------------------------------

- Java Development Kit (JDK) 8 or higher
- Minimum screen resolution: 800x600
- No external JAR dependencies required

---------------------------------
FEATURES
---------------------------------

CUSTOMER SIDE:
- Browse movies showing in CDO cinemas
- View movie details, showtimes, and available cinemas
- Visual seat selection interface (Standard and Deluxe seats)
- Add snacks and drinks to your order
- Simulated payment processing (GCash, PayMaya, Credit Card)
- Digital ticket generation with simulated QR code

ADMIN SIDE:
- Secure admin login (default credentials: admin/admin123)
- Movie management (add, edit, delete movies)
- Seat availability management
- View and export reports (tickets sold, revenue, occupancy)

---------------------------------
SIMULATION NOTES
---------------------------------

As this is a learning project, several features are simulated:

- Payment processing: When you select a payment method, the system will simply display a 
  "Transaction successful" message rather than processing an actual payment.

- Email/SMS: When selecting to email a ticket, the system will show a message dialog 
  indicating that an email would have been sent in a real system.

- Database: All data is stored in memory and will be reset when the application is closed.

---------------------------------
PROJECT STRUCTURE
---------------------------------

The application follows the Model-View-Controller (MVC) pattern:

- Model: Data structures (Movie, Seat, Booking, etc.)
- View: GUI components (Swing forms, panels, dialogs)
- Controller: Business logic for handling operations

---------------------------------
KNOWN LIMITATIONS
---------------------------------

- Data is not persistent (resets when application is closed)
- Limited error handling in some edge cases
- Simplified business rules for educational purposes

---------------------------------
CONTACT & SUPPORT
---------------------------------

This is a learning project. For questions or suggestions, please contact the development team.

---------------------------------
LICENSE
---------------------------------

This project is created for educational purposes only. It is not intended for commercial use.

© 2023 CineBook CDO Project Team
