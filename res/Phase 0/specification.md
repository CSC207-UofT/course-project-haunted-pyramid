## School Scheduler
### By Haunted Pyramid
Create a program that allows a human student to view and store their events, as well as have work period events automatically 
scheduled for them based on input hours they would like to spend working before a given event.

A User will log in to access their 'account' and see a calendar of the current month with their saved events listed by 
name and start/end time on each day. The User will be able to add events with a given start and end time and enrolled 
course they are associated with, as well as being able to
remove events, modify the start and end times of events, and change their view of the calendar to a different month (in 
a range of 7 months). They will be able to enroll in courses, view lists of events by course, and change their calendar
to a weekly or daily view for a specified week or day. When adding events, a user will be able to choose what kind of 
event from a list - Assignment, Lecture, Tutorial, Test, etc. Events such as Assignment will require the user to input a
typical work session length and a number of hours total they would like to spend before the due date on the assignment. 
The program will create work session events for the user, indicated to by the Assigmnent event, and automatically schedule
and reschedule them based on the users' future scheduling. An event like lecture will require the user to input the days
of the week on which the lecture occurs, the date of the first lecture and the date of the last lecture. Some Events
such as Tutorials will require users to specify information about repetition and work sessions. The program 
should create a list of events of the same name with different dates indicated to by eachother and schedule them. 
When the user logs off, their events should be saved to a file (or db) under their Student ID. 
