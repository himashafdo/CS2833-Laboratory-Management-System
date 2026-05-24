# Team 01 — Test Report
Module Tested: Demand Monitoring & Analytics  
Date: 24 May 2026


Test ID: T01-001  
Feature: Demand Monitoring Section  
Description: Verify the demand section loads correctly  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Scroll to Demand Monitoring section  
Expected Result: Cards show numbers, chart renders, heatmap visible  
Actual Result: All cards, chart and heatmap loaded correctly  
Status: PASS  


Test ID: T01-002  
Feature: Total Reservations Card  
Description: Verify total reservations card shows correct count  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Check dm-total card  
Expected Result: Shows correct total count matching reservations  
Actual Result: Count displayed correctly  
Status: PASS  


Test ID: T01-003  
Feature: Active Now Card  
Description: Verify active now card shows live reservation count  
Precondition: Logged in as Lab Technician, a live reservation exists  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Check Active Now card during a live reservation  
Expected Result: Shows count of currently active reservations  
Actual Result: Count displayed correctly  
Status: PASS  


Test ID: T01-004  
Feature: Most Used Equipment Chart  
Description: Verify equipment bar chart renders top 8 items  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Scroll to Most Used Equipment bar chart  
Expected Result: Top 8 equipment shown, bars render correctly  
Actual Result: Chart rendered with correct bars  
Status: PASS  


Test ID: T01-005  
Feature: Lab Utilisation Bars  
Description: Verify all 5 labs shown with proportional bars  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. View Lab Utilisation section  
Expected Result: All 5 labs shown with correct proportional bars  
Actual Result: All 5 labs displayed correctly  
Status: PASS  


Test ID: T01-006  
Feature: Peak Usage Heatmap  
Description: Verify 7x24 heatmap grid renders correctly  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. View Peak Usage Hours heatmap  
Expected Result: 7x24 grid renders, darker cells on busier slots  
Actual Result: Heatmap rendered correctly with visible intensity differences  
Status: PASS  


Test ID: T01-007  
Feature: Refresh Button  
Description: Verify refresh button reloads data without page refresh  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Click the Refresh button  
Expected Result: All data reloads without page refresh  
Actual Result: Data reloaded without full page refresh  
Status: PASS  

Test ID: T01-008  
Feature: Sidebar Scroll Link  
Description: Verify sidebar link scrolls to demand section  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Click Demand Monitoring in the sidebar  
Expected Result: Page scrolls smoothly to demand section  
Actual Result: Page scrolled smoothly to correct section  
Status: PASS  


Test ID: SEC001  
Feature: Authentication Guard  
Description: Verify admin page redirects unauthenticated users  
Precondition: Not logged in (incognito window)  
Steps:  
1. Open incognito window  
2. Navigate directly to /admin-dashboard.html  
Expected Result: Redirected to /login.html  
Actual Result: Redirected to /login.html  
Status: PASS  


Test ID: SEC002  
Feature: Role Guard — Student  
Description: Verify student cannot access admin dashboard  
Precondition: Logged in as Student  
Steps:  
1. Login as Student  
2. Manually navigate to /admin-dashboard.html  
Expected Result: Redirected to /student-dashboard.html  
Actual Result: Redirected to /student-dashboard.html  
Status: PASS  


Test ID: SEC003  
Feature: Role Guard — Lab Technician  
Description: Verify lab tech cannot access lead admin dashboard  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Manually navigate to /lead-admin-dashboard.html  
Expected Result: Redirected to /admin-dashboard.html  
Actual Result: Redirected to /admin-dashboard.html  
Status: PASS  


Test ID: SEC004  
Feature: Form Validation  
Description: Verify empty form submission is rejected  
Precondition: Logged in as Lab Technician  
Steps:  
1. Login as Lab Technician  
2. Navigate to /admin-dashboard.html  
3. Submit a form with all fields empty  
Expected Result: Validation error shown, form not submitted  
Actual Result: Validation error displayed correctly  
Status: PASS  


Test ID: SEC005  
Feature: API Authentication  
Description: Verify API rejects requests without a token  
Precondition: None  
Steps:  
1. Copy API endpoint URL from browser DevTools Network tab  
2. Open Hoppscotch (https://hoppscotch.io)  
3. Paste URL and send GET request with no Authorization header  
Expected Result: 401 Unauthorized  
Actual Result: 200 OK — full reservation/user data returned without any token  
Status: FAIL  


## Bugs Found

Bug: SEC005 — API returns data without authentication  
GitHub Issue: - https://github.com/himashafdo/CS2833-Laboratory-Management-System/issues/26
Severity: High — private user data is publicly accessible without login