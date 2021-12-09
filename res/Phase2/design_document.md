# Design Document

## Haunted Pyramid Productivity V 1.0

### SOLID

#### - Single Responsibility Principle

Calendar related objects all focus on only one responsibility, which is to read the events information and
display them on the calendar. We have made huge changes so that calendar objects do not depend on Event objects directly. We've built an intermediate class called EventCalendarCollaborator that deals with adding events to calendar. But, before that, calendars will only store UUID's of events to appropriate slots. 

In short, calendar object just has ID's of events in different dates.

In order to adhere Single Responsibility Principle, we've made separate GUI's for each task by involving corresponding controllers. 

Each controller has a separate responsibility. The MainController instantiates and delegates to EventController, CalendarController and UserController, which in turn delegate to their more specific controllers. EventController delegates to RecursionController and WorkSessionController for cases that require accessing any Manager aside from EventManager.

The RecursiveController contains a UserSpecific RecursiveController, and user the EventController's EventManager, while the WorkSessionController contains a UserSpecific workSessionScheduler that auto-schedules events based on Event parameters and user preferences.

The EventManager performs many functions, all related to the sorting, finding, filtering, modifying and creating of events.

To better adhere to the Single Responsibility principle, some methods for sorting and returning lists of events have been delegated to different Classes - the `WorkSessionManager` and `DefaultEventInformationGetter`. Using strategies for sorting and returning sub-lists and information about the relationships between events in EventManager would make this Class smaller and easier to understand.

#### - Open / Closed Principle

We've implemented Design Patterns such as Factory Method Design Pattern (CalendarDisplayFactory), Strategy Design Pattern (MenuContent Interface) or used abstractions (DisplayConflict) to increase extensibility for openness of the program while keeping the closeness from modification by decoupling several classes such as calendar-event coupling we had before phase 2. Our GUI still needs a lot of work to make it more flexible, but we've tried to separate the pages to keep the extensibility open.

The Event and Event collection handling classes are open for extension - an Event is a very basic object that contains general references to other Events that can be handled in different ways by different kinds of Managers.

Our idea to generalize repetition and courses to the more broad collections - courses whose times are related by some pattern or reference each other in some way - makes the event handling functionality open for extension.


#### - Liskov Substitution Principle

Abstract classes, interfaces can be substituted by their subclasses. For example, MeltParentWindow objects can be replaced by every class that is implementing it. Every Class that is extending `ConflictDisplay` can substitute the class.

#### - Interface Segregation Principle

The EventListObserver interface contains only one method, used by the EventManager to update observers whenever an Event in its map is changed. This adheres to the Interface Segregation Principle. There is no case of an object having unused implementations of methods in an interface it inherits from.

#### - Dependency Inversion Principle

Interfaces are utilized, but not necessarily for DIP.

`interface EventInfoGetter` is used so that classes that need to access information about events from their UUID's without directly accessing the Event class can be passed a general `EventInfoGetter` (i.e. `DefaultEventInfoGetter`).

### Clean Architecture

Our GUI classes only uses controllers' or abstract classes' methods to keep the clean architecture of the program. Our program adheres Clean Architecture by moving inwards from outer layers while strictly depending on only in one direction by one layer. Here are some examples.

- Calendar Classes

OurCalendar (entity) is being used by CalendarManager, GetCalendar and its subclasses (use cases).
CalendarManager and GetCalendar are being used by DisplayCalendar and its subclasses (Presenters).
CalendarController (Controller) cooperates with DisplayCalendar.


- Event Classes


EventManager accesses only the Event class. All other classes consistently use the EventManager class
to manipulate Events, either by referring to them by ID or passing Event objects to EventManager using
EventManager methods Events.

- Gateway Classes

ICalendar Class uses necessary Use Case classes to do its work. It does not import any of entities. 

IOSerializable and helpers, directly interact with the external storage dropbox and serialized data.

### Design Patterns

- Strategy Pattern

DisplayMenu utilizes strategy pattern to display different types of contents with the minimum effort.
All the menu strategies implement MenuContent interface. The interface is used as a parameter for DisplayMenu class. DisplayMenu class sets which menu content to show and applies it.

A strategy pattern is implemented by the WorkSessionScheduler class, which sorts days and times according to an ordered list of day sorters, and comes up with unsorted schedules using a modifiable time getter.

RecursiveEvent utilizes strategy pattern to get different 'rules' to recurse over events. Depending on the user choice, recursiveEvent can repeat a set of events a fixed amount of times, or repeat them over and over again between two dates. More ways of repetition can be added latter on by creating a class that implements the DateGetter interface and override the method listOfDatesInCycles, which returns a list of repetitions of input events, given the repetition 'rule' of this new class.

- Factory Method Pattern

In order to display three different types of calendar effectively, we adopt factory method pattern.
Depending on the user input different type of DisplayCalendar's child class will be initialized.
By running the overridden method displayCalendar(), the image gets displayed.

- Observer Pattern

To keep workSchedules updated by due date and Recursion up to date with exceptions eventually, every time an event is changed in event manager it updates all observing managers which adjust accordingly.
We created the EventListObserver interface and an update method in EventManager, but we still need to implement the update method in EventManager observers. Our goal it to allow users, in case they edit or delete an event, to carry this change the recursion the modified event is part of, and to modify study session according to the new change as well.

- Builder Pattern

WorkSessionScheduleBuilder uses a UserPreference object to construct a workSessionScheduler, adding the appropriate strategy classes and forming its lists of sorters according to the users preferences. 

### Use of GitHub features

GitHub pull requests and commit logs were used to confirm changes and determine errors in merging.

We've utilized pull requests mainly to avoid conflicts and resolve them if there are any. If there are any issues while reviewing the pull requests, we've effectively communicated with the responsible group mates to deal with the problems.

GitHub Issues were used to communicate what features were being worked on by whom, and GitHub Project was used to effectively see what issues are being dealt with.

### Code Style and Documentation

Every class is fully documented. We've tried to reduce the code smell such as long method by effectively extracting the methods. We've made sure there is no remaining style error in our classes.

If a programmer were to open this project to a random class they would have a harder time understanding
the managers since their output is not primarily for the user, nor is it an entity object to be used. However, the core entities are clear and easy to understand, and each of the controllers performs a unique task despite the programmer possibly needing to refer to the @see tags in the documentation for the presenters and Manager classes to which the Controllers refer.

#### Testing

All the major use cases classes and entities such as OurCalendar, Event, and EventManager are tested using
junit.

All the calendar presenter classes were tested very frequently while building it by every component.

All the GUI classes were tested frequently while building and compiling by every component as well. We've imagined numbers of situations to enhance error tolerance of the program (i.e. opening multiple windows, closing the window etc...)

### Refactoring

- Attempted to avoid long method (some methods go over 10 lines because of conditional statements
  that need to be applied)


- Attempted to keep the number of parameters to be four or less. There are few cases where the method
  has five parameters which are majorly from extractions to avoid long method code smell,
  but tried our best to not go over it. Or date info (for calendar classes we kept the usage of parameter year, month and date separately without making it into a separate object simply for simplicity of the matter.)


- Attempted to keep the codes legible by setting the names of the methods and attributes intuitively.


- Decoupled Event class and Calendar Class as a part of Phase 2 exercise. Calendar class now only depends on UUID.


- Separated a lot of methods from EventManager to newly created classes to avoid a single class from holding too many methods.

### Code Organization

We looked at various strategies for packaging the code. We looked at packaging by
Layer, by feature and by Inside/outside. We chose to package our code by Layer. It
became easier for everyone working on the project to navigate through various files.
It also allowed to not have conflicts while working on classes since different people
working on the project were working on separate layers of the code. With the
Inside/outside and by feature strategy it was not easy to navigate through the code, and it would also cause for conflicts as different people would be working on the same
folder of classes. Since people were not working on front or back end strictly or on
different features it made more sense to package the code by Layers.

### Functionality

A User can log in to access their 'account' (ability to 'load state' and save Users by serialization and dropbox file) and see a calendar of the current month with their saved events listed by name and start/end time on each day. 

The User can add events with a given end time by default, as well as remove events, modify the starts and end times of events, and change their view of the calendar to different months, weeks, or days (in a range of 7 months). A user can edit the recursion (for console) or prep time of an event, and have automatically scheduled work sessions scheduled and updated depending on their input. The program allows the user to mark work sessions as complete and have the sessions rescheduled accordingly. The user can specify their free time during which work sessions are prohibited to be scheduled, and their preferred method of scheduling (procrastinating, cram, spacing, etc...). 

A user can choose to export their entire saved schedule information into iCal formatted file with .ics extension. If using console, the user can choose to export the currently viewing calendars.

When the user logs off, their profile info and events are serialized and saved to a dropbox. 
