## Progress Report:


### Struggles:

First and foremost, it is quite important to note that our group heavily lacked manpower; meaning that we were severely "understaffed" in finishing this project in time whilst also managing to keep up with other personal responsibilities.\
Our final head count is 5, almost half of what was allowed in the beginning of the project.\
Nevertheless, we were able to accomplish our goals, through time investment and great dedication from our fellow groupmates.\
Another elephant in the room we must remark is the fact that we decided to make the program executable.\
It was difficult for us, in such a short amount of time, to learn and successfully implement such a complex library into our program, such that it is not only runnable but also capable of handling the intricate tasks in our program.\
Indeed, both syntax and logic errors that arose during Phase 2 were also more perplexing to resolve, as our program got bigger and convoluted.


### Perks

As aforementioned, we are quite proud that we were able to implement and show the executable program by the end of Phase 2.\
Also, we were able to refine our program following the provided comments and improvements from the TA.\
Finding big and small improvements in our pre-existing functionalities were also very satisfying.\
We also learned a lot about visual beautification of programs that only focused on performance, using external libraries.\
Collaboration and quick, efficient problem-solving skills were critical protagonists in our endeavours, and the methodologies and strategies we grasped during the development of this project must be useful in our future courses and personal aspirations.


### Comparison To Previous Phases

Our program is fully implemented with most, if not all goals set in Phase 0 and Phase 1 completed and added into our program.\
In Phase 1, our sole medium of showing the program's functionalities and capabilities were the Java console itself.\
However, we made sure, for Phase 2, to add another interface which the client can use to experience our program.\
We decided to utilize the JFrame/Swing library implementation, to create a launchable executable file.\
Then we added visualizations in the pages so that the client can perform most of the useful functions of the program, but with much easier accessibility and convenience in navigating through the application.\
Additionally, we added another extension of .ics file exportation, which clients can use to add real events and schedules created from our program directly into Calendar programs, such as Apple iCloud Calendar, Google Calendar, Notion, etc.\
We also made sure to critically analyze, expand, improve and refactor our program for further enhancements.


### Simplified Summary And Plans Of The Project Since Phase 1

- Added Recursive Events
- Added UserPreferences
- Refactored MainController and changes in precedence of instantiation of other controllers
- Refactored presenter classes to be used more often and legitimately
- Calendar and Event entities decoupling (now calendars only deal with UUIDs instead of Event object directly)
- Developed auto scheduling Strategy and Builder pattern
- Enhanced user experiences
- Expanded unittests
- Cleaned up helper classes and methods
- Improved schedule distribution algorithm
- Removed unused, unnecessary classes and methods
- Implemented Java Swing library
- Implemented JFrame Library
- Applied executablility extension
- Applied .ics file exportation extension
- General program beautification through windows
- And much more minor bug fixes and extensions


### Brief Summary of what each member has been working on since phase 1

#### Malik Lahlou

#### Shahzada Muhammad Shameel

#### Sebin Im

- Optimized much of the code and implemented basic Dropbox data serialization, through Dbx Library application.
- Fully implemented the final version of the Dropbox data serialization procedures, by connecting Dropbox cloud database with project with jackson API.
- Refactored the controllers and gateway to handle new and improved type of Events; changed referencing of events from List<*Event> to Map<User.UUID, List<*Event>>.
- Worked on the primary implementation of the JFrame, Javax, and Java Swing libraries. The initial window on boot to roughly show how the GUI should be coded and launched.
- Serialization and version control debugging.

#### Seo Won Yi

- After phase 1, focused on decoupling Calendar and Event classes by making calendar classes contain map of Events' UUID instead of the objects directly; Refactoring so that instead of calendar possessing Event object, it now possesses UUID of events.
- Implemented iCal export feature to the GUI version of our program.
- Modified the EditEventWindow for its performance.
- Built GUI windows and implement functionalities necessary for calendar selection screen which now enables users to select the type of calendars.

#### Taite Cullen


### Significant Pull Requests

#### Malik Lahlou

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

https://github.com/CSC207-UofT/course-project-haunted-pyramid/pull/210#issue-1068833602




