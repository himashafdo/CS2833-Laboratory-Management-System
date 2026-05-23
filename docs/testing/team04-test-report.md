T04-001
Dashboard stats load
Admin – dashboard.html
Logged in as Labtech
What was expected?
All 4 stat cards showing correct numbers.
What actually happened?
All the counts displayed on all 4 stat cards were correct.
Status: Pass

T04-002
Equipment status list 
Admin – dashboard.html
Logged in as Labtech
What was expected?
Displaying 5 equipment with status badges.
What actually happened?
Displayed 5 equipment with status badges.
Status: Pass

T04-003
Pending reservations 
Admin – dashboard.html
Logged in as Labtech
What was expected?
Showing pending bookings with review button.
What actually happened?
Showed pending bookings with review button.
Status: Pass

T04-004
Open issues list
Admin – dashboard.html
Logged in as Labtech
What was expected?
Showing unresolved issues.
What actually happened?
Displayed unresolved issues.
Status: Pass

T04-005
Activity feed
Admin – dashboard.html
Logged in as Labtech
What was expected?
Showing mix of reservations, issues and requests.
What actually happened?
Displayed mix of reservations, issues and requests.
Status: Pass

T04-006
Welcome badge	
dashboard.html	
Logged in as Lab Tech. Checked the Pending Actions count on the dashboard.
What was expected?	
The welcome badge should display the correct number of pending items.	
What actually happened?
The pending actions count displayed correctly on the dashboard.	
Pass

T04-007	
Manage users loads	
/admin-users.html	
Logged in as Lab Tech and opened /admin-users.html.	
What was expected?
The page should load and display only student accounts, not admin or technician accounts.	
What actually happened?
The page loaded successfully and displayed only student accounts.	
Pass

T04-008	
Search student	
/admin-users.html	
Typed a student name in the search box.	
What was expected?
The student list should filter according to the entered name.	
What actually happened?
The search function worked correctly and filtered the relevant student records.	
Pass

T04-009	
Delete student	
/admin-users.html	
Clicked the Delete button on a student account.	
What was expected?
A confirmation prompt should appear, and after confirmation the student account should be removed.
What actually happened?	
The confirmation message appeared correctly and the selected student account was deleted successfully.	
Pass

T04-010 
Student stats
Admin – dashboard.html
Logged in as Labtech
What should happen : 
Under all reservations student stats are shown.  The total number of tests, number of approved, no of pending, no of canceled ones and no of tests done today. 
What actually happened : 
All the counts displayed on the screen are correct.
Status : Pass

T04-011
Report issue form
Report-issue.html
Logged in as student
What should happen : 
When clicking the report issue, the form with issue type, issue title and description open up.
And it should be able to choose the issue type and the relevant device or area.
For example I choose equipment as issue type, under that I choose an oscilloscope.
What actually happened : 
It is working perfectly. Able to type the description and choose the error.
Status : Pass

T04-012   
Fill form and submit
Submit issue
Logged in as student
What should happen : 
When submitting an issue it should show that, “the issue report has been reported, our technician is notified”.
And  the issue should appears in the lab tech dashboard.
What actually happened : 
When the description is short within one line, It works perfectly.
When the description is long it raises an error.
“could not execute statement [Data truncation: Data too long for column 'description' at row 1]
Status : Pass

T04-013   
Admin–user.html
Student role guard
Logged in as student
What should happen : 
Should automatically redirected to the student instead of showing you the page or a blank screen.
What actually happened : 
It works perfectly. When access I try to access /admin-users.html while logged in as a student it redirect me to the student page.
Status : Pass
