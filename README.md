<img width="1128" alt="login-page" src="https://user-images.githubusercontent.com/89669123/189007593-45be9e3c-7cfd-44c7-bd3d-dff1dcf7eeba.png">

<div align="center">
<h1>Radiology Information System</h1>
<p>
This is a Radiology Information System (RIS) that can create users, create patients with detailed background information, 
createappointments, check patients in and out, create orders for modalitites (X-ray, MRI, rtc.), upload patient modalities,
and write/store reports on patient modalities. 
</p>
</div>
<br/>

<h2>Navigation Menu Features</h2>
<h3><b>Home</b></h3>
<ul>
    <li>Shows a list of all tables that the user is authorized to view.</li>
</ul>

<h3><b>User Info</b></h3>
<ul>
    <li>Shows the user's personal and account information. User may change is at any time.</li>
</ul>

<h3><b>Admin</b></h3>
<ul>
    <li>Shows a table of all users. Any user with access to this feature can create or delete an application user.</li>
</ul>

<h3><b>Referrals</b></h3>
<ul>
    <li>Shows a table of all patients. Any user with access to this feature can create or delete a patient, view a
        patient's billing report, and add background information regarding previous or ongoing medical situations
        (surgeries, allergies, etc.). Additionally, they can view a patient's modality and write/store a report based on
        their analysis.
    </li>
</ul>

<h3><b>Appointments</b></h3>
<ul>
    <li>Shows a table of all appointments. Any user with access to this feature can create or delete an appointment, as
        well as check in or check out a patient.
    </li>
</ul>

<h3><b>Orders</b></h3>
<ul>
    <li>Shows a table of all modality orders that have been placed. Any user with access to this feature can create or
        delete orders, as well as fulfill orders with the needed modality.
    </li>
</ul>
<br/>

<h2>Application Users</h2>
<h3><b>Admin</b></h3>
<ul>
    <li>Full access to application features</li>
</ul>

<h3><b>Referral MD</b></h3>
<ul>
    <li>Access to home, user info, referrals, appointments, and orders</li>
</ul>

<h3><b>Radiologist</b></h3>
<ul>
    <li>Access to home, user info, referrals, appointments, and orders</li>
</ul>

<h3><b>Receptionist</b></h3>
<ul>
    <li>Access to home, user info, referrals, and appointments</li>
</ul>

<h3><b>Technician</b></h3>
<ul>
    <li>Access to home, user info, appointments, and orders</li>
</ul>

## Installation

Note: You must have Java and MySQL
<li> Installing the whole project from the "New_Master" branch</li>
<li> Import the ```ris_database.txt``` file into a MySQL schema, run it, and copy the schema's JDBC string*</li>
<li> Next, open up the project in an IDE and Navigate to ```src/main/java/com/risjavafx/Driver.java```</li>
<li> Replace the string that says "jdbc string" with the JDBC string you copied</li>
<li> Replace the string that says "username" with your MySQL username</li>
<li> Replace the string that says "password" with your MySQL password</li>

*If you right click the schema, typically an option will appear to copy the JDBC string. This may vary for different IDEs.

## Usage

You may now log into the application and use it. In your database, you will see a table entitled "users". It contains all the user information, including usernames, passwords, and user roles. Use this to decide who you'd like to sign in as.

## Screenshots

| | |
|----|----|
|<img width="1128" alt="admin-home" src="https://user-images.githubusercontent.com/89669123/189007637-3dafc1e5-09e5-4d6f-8497-015dbe3251d8.png">|<img width="1128" alt="admin-userinfo" src="https://user-images.githubusercontent.com/89669123/189007676-e53cd9f1-05a5-4089-8609-c74d02e35517.png">|

| | |
|----|----|
|<img width="1128" alt="tech-orders" src="https://user-images.githubusercontent.com/89669123/189008036-c673c360-96c4-4beb-9286-676a941487fa.png">|<img width="1128" alt="admin-patient-image" src="https://user-images.githubusercontent.com/89669123/189007710-0fab2962-590a-462d-9524-17431a43e7fe.png">|

## Contributors

John Misley, Caleb Coussan, Thomas Leach, Jonathan Garcia, Jared Hardee.
