#Design Document
##Haunted Pyramid Productivity V 1.0

### SOLID

- Single Responsibility Principle

Calendar related objects all focus on only one responsibility, which is to read the events information and
display them on the calendar.

Each controller has a separate responsibility. The MainController instantiates and delegates to EventController, CalendarController
and UserController, which in turn delegate to their more specific controllers. EventController delegates to RecursionController
and WorkSessionController for cases that require accessing any Manager aside from EventManager. 
The RecursiveController contains a UserSpecific RecursiveController, and user the EventController's EventManager, 
while the WorkSessionController 

The EventManager performs many functions, all related to the sorting, finding, filtering, modifying and creating of events. 
To better adhere to the Single Responsibility principle, some methods for sorting and returning lists of events should be 
part of a different Classes. Using strategies for sorting and returning sub-lists and information about the relationships
between events in EventManager would make this Class smaller and easier to understand.

- Open / Closed Principle



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


### Clean Architecture

- Calendar Classes 

OurCalendar (entity) is being used by CalendarManager, GetCalendar and its subclasses (use cases).
CalendarManager and GetCalendar are being used by DisplayCalendar and its subclasses (Presenters).
CalendarController (Controller) cooperates with DisplayCalendar.

Only one class, IOSerializable and helpers, directly interacts with the external storage dropbox and serialized data. 

EventManager accesses only the Event class. All other classes consistently use the EventManager class
to manipulate Events, either by referring to them by ID or passing Event objects to EventManager using 
EventManager methods Events. 

IOController controls the user input and handles exceptions in UserInput before passing to other Controllers. Other 
controllers do not directly use scanner or read user input.

### Design Patterns

- Strategy Pattern

- DisplayMenu utilizes strategy pattern to display different types of contents with the minimum effort.
All the menu strategies implement MenuContent interface. The interface is used as a parameter for DisplayMenu class. 
DisplayMenu class sets which menu content to show and applies it.

With more time, a strategy pattern should be implemented in Autoschedule to better encapsulate a variety
of methods and steps for sorting days and times to find ideal ones based on User Preferences.

— RecursiveEvent utilizes strategy pattern to get different 'rules' to recurse over events. Depending on the user 
choice, recursiveEvent can repeat a set of events a fixed amount of times, or repeat them over and over again between
two dates. More ways of repetition can be added latter on by creating a class that implements the DateGetter interface
and override the method listOfDatesInCycles, which returns a list of repetitions of input events, given the repetition 
'rule' of this new class.

- Factory Method Pattern

In order to display three different types of calendar effectively, we adopt factory method pattern.
Depending on the user input different type of DisplayCalendar's child class will be initialized.
By running the overridden method displayCalendar(), the image gets displayed.

- Observer Pattern

To keep workSchedules updated by due date and Recursion up to date with exceptions eventually, every time an event is 
changed in event manager it updates all observing managers which adjust accordingly.
We created the EventListObserver interface and an update method in EventManager, but we still need to implement the 
update method in EventManager observers. Our goal it to allow users, in case they edit or delete an event, to carry
this change the recursion the modified event is part of, and to modify study session according to the new change as
well.

### Use of GitHub features

Github pull requests and commit logs were used to confirm changes and determine errors in merging.

### Code Style and Documentation

All classes are fully documented other than RecursionController and WorkSessionController.
All use case and entity classes are fully documented with descriptions of the class and authors at
the top. 

If a programmer were to open this project to a random class they would have a harder time understanding
the managers since their output is not primarily for the user, nor is it an entity object to be used. However, the 
core entities are clear and easy to understand, and each of the controllers performs a unique task despite the 
programmer possibly needing to refer to the @see tags in the documentation for the presenters and Manager classes
to which the Controllers refer.

###Testing

All the major use cases classes and entities such as OurCalendar, Event, and EventManager are tested using
junit.
All the calendar presenter classes were tested very frequently while building it by every component. 

###Refactoring

- Attempted to avoid long method (some methods go over 10 lines because of conditional statements 
that need to be applied)
- Attempted to keep the number of parameters to be four or less. There are few cases where the method 
has five parameters which are majorly from extractions to avoid long method code smell, 
but tried our best to not go over it.

####Code Organization

####Functionality
