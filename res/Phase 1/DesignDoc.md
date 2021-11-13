#Design Document
##Haunted Pyramid Productivity V 1.0

### SOLID

- Single Responsibility Principle

Calendar related objects all focus on only one responsibility, which is to read the events information and
display them on the calendar. 

OurCalendar (entity) is being used by CalendarManager, GetCalendar and its subclasses (use cases).
CalendarManager and GetCalendar are being used by DisplayCalendar and its subclasses (Presenters).
CalendarController cooperates with DisplayCalendar.

### Clean Architecture

### Design Patterns

- Strategy Pattern

DisplayMenu

- Factory Method Pattern

DisplayCalendarFactory

### Use of GitHub features

### Code Style and Documentation

####Testing

####Refactoring

- Attempted to avoid long method (some methods go over 10 lines because of conditional statements 
that need to be applied)
- Attempted to keep the number of parameters to be four or less (there are few cases where the method 
has five parameters (majorly from extractions to avoid long method code smell), but tried our best to not go over it)

####Code Organization

####Functionality
