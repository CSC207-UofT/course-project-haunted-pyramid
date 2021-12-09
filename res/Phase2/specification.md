## School Scheduler
### By Haunted Pyramid
Create a program that allows a student to view and store their events, as well as have work period events automatically scheduled for them based on input hours they would like to spend working before a given event.

A User will log in to access their 'account' and see a calendar of the current month with their saved events listed by name and start/end time on each day. The User is able to add events, as well as
remove events, modify the starts and end times of events, modify the descriptions and names of events, and change their view of the calendar to a different month (in a range of 7 months). The program will create work session events for the user and automatically schedule and reschedule them based on the users' future scheduling. The program allows the user to mark work sessions as complete or incomplete and have the sessions rescheduled accordingly. The user can specify their free time during which work sessions are prohibited to be scheduled, their preferred method of scheduling according to settings that can be modified and saved. The user can export their entire schedule as iCalendar formatted file to view in different applications. When the user logs off, their profile info and events are serialized and saved to a dropbox.

### Changes from Phase 1

#### Presentation

In phase 1, the presenters were exclusively formatted strings sent to the text terminal leading a user through processes and re-presenting information after the processes were fully complete. Since then, we have implemented a fully functioning Java Swing GUI. Windows that present information refresh when information is changed, and the order in which processes occur is much more fluid with the use of Actions.

#### Functionality 

The Calendar can now be formatted and exported to iCal files. Also, WorkSessionScheduling has been updated significantly since Phase 1. Now several combinable options are available to the user to specify how they prefer workSessions to be scheduled in general, as well as a specific option for how long before each due date they want to start their work.

#### Data storage

In phase 1, we ensured that data would be serialized and saved to a dropbox upon exit. Now, recursive information is also serialized and saved, and a UserPreferences object has been added to the User object which is serialized along with their personal information, specifying their preferred settings when setting up their version of the program.

#### Cooperation

We set specific personal due dates at the beginning of phase 2 to clarify each persons' individual responsibilities and our overall goals for the course of the phase. We started using Git Issues and different branches for different aspects of the program so that multiple people could collaborate on one aspect, making it clear which branches contributed to which classes. We continued to use Discord to communicate frequently and kept each other updated and let each other know of changes in personal deadlines.

