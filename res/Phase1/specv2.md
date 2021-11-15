## School Scheduler
### By Haunted Pyramid
Create a program that allows a human student to view and store their events, as well as have work period events automatically
scheduled for them based on input hours they would like to spend working before a given event.

A User will log in to access their 'account' and see a calendar of the current month with their saved events listed by
name and start/end time on each day. The User will be able to add events with a given end time by default, as well as being 
able to remove events, modify the starts and end times of events, and change their view of the calendar to a different month (in
a range of 7 months). They will be able to choose events to edit by changing their calendar
to a weekly or daily view for a specified week or day. When adding events, a user will be able to choose what kind of
event from a list - Assignment, Lecture, Tutorial, Test, etc. Events such as Assignment will require the user to input a
typical work session length and a number of hours total they would like to spend before the due date on the assignment.
The program will create work session events for the user and automatically schedule
and reschedule them based on the users' future scheduling. The program allows the user to mark work sessions as complete
or incomplete and have the sessions rescheduled accordingly. The user can specify their free time during which work sessions
are prohibited to be scheduled, and their preffered method of scheduling (procrastinating or not).
When the user logs off, their profile info and events should be serialized and saved to a dropbox. 

### Changes from Phase 0

#### Presentation

In phase 0, the presentation was only available for monthly calendar. Now, you can view weekly and daily calendars. Instead of typing fixed texts, we've added menu screen to visually improve the user experience. For what we have tested, any form of inputs do not halt the program from operating and we will kindly ask you to retype in the right format. 

#### Functionality

In phase 0, only limited amount of work could be done (add event). Now, you can add / remove / modify information of the event as well as add additional functions such as make recursion (repetition) or set up work sessions to prepare the event. We have implemented basic algorithm that will set up the work sessions for the available times indicated by the user. 

#### Data storage

In phase 0, we were storing data in local serializable files. Now, the files exist in DropBox. We've performed testings to proudly confirm that the files save and load successfully and we've made it so that any file that were copied to read the data gets deleted as you exit the program.

#### Cooperation

In phase 0, we've dealt with individual classes and tried to have single responsibility for each person. In phase 1, we had multiple people working on the same goal (developing and implementing various features of event) by fully utilizing the github features. We've had frequent meetings to keep up everyone's motivation and share opinions. 
