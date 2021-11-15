#Design Document
##Haunted Pyramid Productivity V 1.0

#### SOLID

- Single Responsibility Principle

Calendar related objects all focus on only one responsibility, which is to read the events information and
display them on the calendar. 

OurCalendar (entity) is being used by CalendarManager, GetCalendar and its subclasses (use cases).
CalendarManager and GetCalendar are being used by DisplayCalendar and its subclasses (Presenters).
CalendarController cooperates with DisplayCalendar.

Each controller has a separate responsibility. The MainController instantiates and delegates to EventController, CalendarController
and UserController, which in turn delegate to their more specific controllers. EventController delegates to RecursionController
and WorkSessionController for cases that require accessing any Manager aside from EventManager. 
The RecursiveController contains a UserSpecific RecursiveController, and user the EventController's EventManager, 
while the WorkSessionController 

The EventManager performs many functions, all related to the sorting, finding, filtering, modifying and creating of events. 
To better adhere to the Single Responsibility principle, some methods for sorting and returning lists of events should be 
part of a different Classes. Using strategies for sorting and returning sub-lists and information about the relationships
between events in EventManager would make this Class smaller and easier to understand.

- Open / Closed Principle Principle

the Event and Event collection handling classes are open for extension - an Event is a very basic object that contains 
general references to other Events that can be handled in different ways by different kinds of Managers. 
our idea to generalize repetition and courses to the more broad collections - courses whose times are related 
by some pattern or reference eachother in some way - makes the event handling functionality open for extension.
events and collections of events.
- Liskov Substitution Principle

- Interface Segregation Principle

The EventListObserver interface contains only one method, used by the EventManager to update observers whenever an Event 
in its map is changed. This adheres to the Interface segregation principle. There is no case of an object having unused
implementations of methods in an interface it inherits from.

- Dependency Inversion Principle


#### Clean Architecture



#### Design Patterns

- Strategy Pattern

DisplayMenu

- Factory Method Pattern

DisplayCalendarFactory

#### Use of GitHub features

#### Code Style and Documentation

####Testing
CalendarManager, EventManager, and WorkSessionScheduler have mostly complete junit test files associated. 

####Refactoring

- Attempted to avoid long method (some methods go over 10 lines because of conditional statements 
that need to be applied)
- Attempted to keep the number of parameters to be four or less (there are few cases where the method 
has five parameters (majorly from extractions to avoid long method code smell, but tried our best to not go over it)

####Code Organization

####Functionality
