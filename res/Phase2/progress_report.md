## Progress Report:


### Struggles:


First and foremost, it is quite important to note that our group heavily lacked manpower; meaning that we were severely "understaffed" in finishing this project in time whilst also managing to keep up with other personal responsibilities.

Our final head count is 5, almost half of what was allowed in the beginning of the project.

Nevertheless, we were able to accomplish our goals, through time investment and great dedication from our fellow teammates.

Another elephant in the room we must remark is the fact that we decided to make the program executable in Graphics User Interface.

It was difficult for us, in such a short amount of time, to learn and successfully implement Java Swing library into our program, such that it is not only runnable but also capable of handling the intricate tasks in our program.

Indeed, both syntax and logic errors that arose during Phase 2 were also more perplexing to resolve, as our program got bigger and convoluted.

The correct implementation of the safehold was not able to be fully implemented since local repository serialization is impossible when the client is not connected to wifi, or when the local repository is actually greater in file size than those in the cloud.\
Unfortunately, we decided to scrap the local repo serialization, since it was not perfect on submission, we thought that it would be better than to submit a buggy one.


### Perks

As aforementioned, we are quite proud that we were able to implement and show the executable program by the end of Phase 2.

Also, we were able to refine our program following the provided comments and improvements from the TA, which includes the exporting iCalendar formatted file feature added to the program.

Finding big and small improvements in our pre-existing functionalities were also very satisfying.

We also learned a lot about visual beautification of programs that only focused on performance, using external libraries.

Collaboration and quick, efficient problem-solving skills were critical protagonists in our endeavours, and the methodologies and strategies we grasped during the development of this project must be useful in our future courses and personal aspirations.


### Comparison To Previous Phases

Our program is fully implemented with most, if not all goals set in Phase 0, Phase 1, and Phase 2 completed and added into our program.

In Phase 1, our sole medium of showing the program's functionalities and capabilities were the Java console itself.

However, we made sure, for Phase 2, to add another interface which the client can use to experience our program.

We decided to utilize the JFrame/Swing library implementation, to provide GUI for the users.

Then we added visualizations in the pages so that the client can perform most of the useful functions of the program, but with much easier accessibility and convenience in navigating through the application.

Additionally, we added another extension of .ics file exportation, which clients can use to add real events and schedules created from our program directly into Calendar programs, such as Apple iCloud Calendar, Google Calendar, Notion, etc.

We also made sure to critically analyze, expand, improve and refactor our program for further enhancements.


### Simplified Summary And Plans Of The Project Since Phase 1

- Added updates of Recursive Events
- Added UserPreferences
- Refactored MainController and changed in precedence of instantiation of other controllers
- Refactored presenter classes to be used more often and legitimately
- Calendar and Event entities decoupling (now calendars only deal with UUIDs instead of Event object directly)
- Developed more types of auto scheduling using Strategy and Builder Design Patterns
- Enhanced user experiences
- Expanded unit tests
- Cleaned up helper classes and methods
- Improved schedule distribution algorithm
- Removed unused, unnecessary classes and methods
- Implemented Java Swing library
- Implemented JFrame Library
- Applied GUI
- Applied .ics file exportation extension
- General program beautification through windows
- And much more minor bug fixes and extensions


### Brief Summary of what each member has been working on since phase 1

#### Malik Lahlou
- Implemented the algorithms to update recursions in case a user decides to add/change or delete an event from a recursion.
- Refactored RecursiveController so that it respects clean architecture. Split the single huge method into 5 smaller methods that accomplish one task.
- Implemented GUI for Recursions. As explained in the accessibility report, this class has low tolerance for error. I only had time to create the menu and make it create recursive events; I wasn't able to quickly figure out why the new recursive events could npt be accessed (they can be accessed in the consol).
- Started working on the design and implementation of the Category class.

#### Shahzada Muhammad Shameel
- Worked on improving the runtime of worksession scheduler. The main problem was being caused by a method freeSlots being used. Changed implementation of method.
- Refactored code in Event Manager
- Implemented GUI for profile settings. Once button for profile settings is clicked, a pop up window appears showing different settings which can be changed. 
- Connected the buttons on the Profile settings window to the backend of the program.

#### Sebin Im

- Optimized much of the code and implemented basic Dropbox data serialization, through Dbx Library application.
- Fully implemented the final version of the Dropbox data serialization procedures, by connecting Dropbox cloud database with project with jackson API.
- Refactored the controllers and gateway to handle new and improved type of Events; changed referencing of events from List<*Event> to Map<User.UUID, List<*Event>>.
- Worked on the primary implementation of the JFrame, Javax, and Java Swing libraries. The initial window on boot to roughly show how the GUI should be coded and launched.
- Serialization and version control debugging.

#### Seo Won Yi

- After phase 1, focused on decoupling Calendar and Event classes by making calendar classes contain map of Events' UUID instead of the objects directly; Refactoring so that instead of calendar possessing Event object, it now possesses UUID of events.
- Allowed the program to export calendar schedule to iCal formatted file.
- Built MainMenu, the core of the GUI Program.
- Implemented iCal export feature to the GUI version of our program.
- Modified the EditEventWindow for its performance.
- Built GUI windows and implement functionalities necessary for calendar selection screen which now enables users to select the type of calendars.

#### Taite Cullen
- After Phase 1, implemented switch from Event use of integer ID to UUID
- Designed and implemented Work Session Scheduling strategies and builder pattern
- Updated User to use UserPreferences object for modifying and saving preferred setup of program 
- Refactored EventManager to delegate to DefaultEventInfoGetter
- Designed and implemented aspects of the Java Swing GUI:
  - Add/Edit, Select event to add/edit window, work session editing
  - Settings menu with options for updating UserPreferences, and free time window

### Significant Pull Requests

#### Malik Lahlou

- Created the algorithms that are used to update recursions
https://github.com/CSC207-UofT/course-project-haunted-pyramid/commit/366f3a92ddd81e831ba55c5d8c564fa279fd64ed
- Implemented the menu to create recursions.
https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/330


#### Shahzada Muhammad Shameel

This pull request was for the initial development on CalendarManager, which is a central part of the program.
https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/4

This pull request was for initial implementation of profile settings GUI. The GUI was an important part for phase 2.
https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/247


#### Sebin Im

Refactored the controllers and gateway to handle new and improved type of Events.
- https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/186
  (Changed referencing of events from List<Event> to Map<User.UUID, List<Event>>)

Added SignUp page and also major bugfixes in login procedures in both console and GUI.
- https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/257
  (New window appearing with necessary information, and also making sure serialization functions as intended)

#### Seo Won Yi

After finishing the refactoring task, focused on implementing file export function which would allow users to export the current saved schedule information in iCalendar format.
- https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/218
  (iCal export development + Java docs)

Built main menu GUI that would act as a core part of GUI version of our program.
- https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/234
  (GUI for Main Menu)

#### Taite Cullen

Implemented WorkSessionScheduler Strategies and Builder Pattern: 
- https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/210#issue-1068833602





