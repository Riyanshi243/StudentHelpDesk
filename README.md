# StudentHelpDesk
An android app aimed to help the placement cell and students to review and change the data in real time. The app will be having to interfaces - admin and user(student). The students can update few of their details without having to bother the admin. Admins can send notification regarding important announcements via notification feature.

## USE OUR APPLICATION - StudentHelpDesk

Download link: https://drive.google.com/file/d/1rK9VU7oEMCRgihpoBmxBfhWbKXNh0fGt/view?usp=sharing

### For Testing: <br>
Admin Email: superadmin@vanasthali.com <br>
Student Email: S1@vanasthali.com <br>
Company Email: c1@vanasthali.com <br>
Password for all: 12345678


<br>


## Purpose of Application
The Placement Management System, named StudentHelpDesk (SHD) is for the students and companies, which maintains the database for the students where all the students’ records are entered including their academic details, personal details and other related documents such as Resume or Marksheets. This system would provide the facility of viewing both the personal and academic information of the student, it would also search for eligible students and deal with the insertion, updations and deletion of records.

## User Classes and Characteristics

The major User classes in the StudentHelpDesk would be :

### 1. Student
1. New student needs to sign up by giving their complete details i.e., Personal details, Academic details and other related documents as mentioned by their college while college register in app.
2. They can update their profile information in real time, only when database is unlocked. Also, only changeable fields would be allowed to be changed directly by students, whereas non- changeable fields requires sending a change request to admin (e.g. – Name, college id, etc.).
3. They will receive notification from the companies as and when they arrive based on eligibility criteria, thereafter, they may register for the same.
#### Below is the student page
<table align="center">
  <tr>
    <td>Student Page</td>
  </tr>
  <tr>
    <td><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/StudentPage.jpg" alt="StudentPage" style="width:300px;height:620px;"></td>
  </tr>
 </table>

### 2. Admin
1. The Admin has supreme power of the application SHD.
2. Admin provides approval to Student and the corporate registration, thereafter, they can follow up for signup or login procedure.
3. Admin holds the responsibility for maintaining and updating the whole system.
4. Admin has the responsibility to notify the Company for any application from a student.
5. Admin has to notify the students regarding any changes in the procedure or selection.
6. Admin has to lock the database and notify students for the same, before sending the data to requiredcompany.
#### Below are the Data AdminPage and Department AdminPage
<table align="center">
  <tr>
    <td>Data Admin Page</td>
     <td>Department Admin Page</td>
  </tr>
  <tr>
    <td><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/AdminPage1.jpg" alt="DATA AdminPage" style="width:300px;height:670px;"></td>
    <td><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/AdminPage2.jpg" alt="AdminPage" style="width:300px;height:670px;"></td>
  </tr>
 </table>

### 3. Company
1. The Company initially has to register using the details that admin provides them.
2. The Company can notify the Admin or the TPO as well as send direct notifications to students regarding placement/internship after signup.
3. The Company can view shortlisted students’ details (based on eligibility criteria) as provided by TPO.
#### Below is the CompanyPage
<table align="center">
  <tr>
    <td>Company Page</td>
  </tr>
  <tr>
    <td><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/CompanyPage.jpg" alt="CompanyPage" style="width:300px;height:620px;"></td>
  </tr>
</table>

## Assumptions and Dependencies
### Assumptions:
1. We are assuming that the user should have some basic knowledge of Android mobile.
2. It is assumed that all users have good internet connectivity.
3. We are assuming that the Admin-User has all email ids to allow students to create all profiles.
### Dependencies:
1. If there is no internet connection, there will be faulty result at client side. Furthermore, Network error will be displayed.
2. This app won’t work in iOS.
3. Admin will have to allow each and every user of the college.


## System Features

### 1. Data Change with Request
<i><b>Brief:</b></i> Some of the information, which is of much more importance and set as non-changeable by superadmin eg., Name, Mother’s Name, roll number etc., can be changed only when the admin accepts the request of change.
<br>
<i><b>Benefits:</b></i> The benefit of this would be that the students would be able to edit the details wrongly entered during signup or change in span of time. This feature will provide status of the request at students side and for admins side, they will be required to accept the ones they find up to mark and required, and can deny the ones they find suspicious. In addition, the big benefit of this feature is that the admin will not have to change the database themselves, only accepting the request from the student will automatically change the required field in real time in the database.
<br>
<i><b>Requirements:</b></i> The students should send request for valid reasons only. For admins, it will be a big task to timely accept or deny the requirements as per them. They will be required to send the reason to deny and request.

### 2. Data Change without Request
<i><b>Brief:</b></i> Some of the information, which is of much not of must importance and set as changeable by super admin eg., Aadhar, Address, Resume etc., can be changed by the students themselves and this will reflect it the database automatically without admin interference.
<br>
<i><b>Benefits:</b></i> The benefit of this would be that the students would be able to edit the details time to time as per their need with having to bother the admin of their college. In addition, the big benefit of this feature is to reduce the pressure work of the admin attending the unnecessary requests from students to change their profile, eg., resume of any person can change daily.
<br>
<i><b>Requirements:</b></i> The students should have good internet connectivity to avoid inconsistency at any place. Furthermore, it is their responsibility to keep their data correct as per requirement as admin will have no control over changeable details of any students. Sending inappropriate data to company would lead problem to student in future.

### 3. Notification
<i><b>Brief:</b></i>  The app will provide a notification feature for all the new announcements by the admin/company for the students via admin. The company can as well send direct notifications to students, as per their choice.
<br>
<i><b>Benefits:</b></i>  The benefit of this would be that since information will be direct admin, it will be authentic and trustworthy. The students would be aware of the upcoming companies and be able to apply for them as well as prepare on time. Admins can also send notifications regarding holidays and fee submissions. The big benefit of this feature will be to eliminate the mediocre in communication.
<br>
<i><b>Requirements:</b></i>  The User – Students, must turn on the notification alert for the application to receive the alert even when they are not using the application in current time.

### 4. Database Locking
<i><b>Brief:</b></i> Database locking feature is provided to the admin so that they could send notification to the students some time before, wherein students can upload their latest resume or change any details required to be changed by them in that time span. The admin can lock the database and send the details to the company after which he could again unlock it.
<br>
<i><b>Benefits:</b></i>  The benefit of this would be that the students would be able to edit the details before sending it to the company and would not bother the admins on the same. This feature smoothens the work of both the students as well as the company.
<br>
<i><b>Requirements:</b></i>  The User – Students, must turn on the notification alert for the application to receive the alert even when they are not using the application in current time. Otherwise, they would not receive the notification on correct time and admins would end up providing their old details to the company.

  
### 5. FAQ with hashtags
<i><b>Brief:</b></i> Frequently asked question (FAQ), is the feature to help students ask their doubts from admin directly. They may add Hashtags along, which will be important to related questions to each other and lead to easy search by others. They may tag any specific admin they want to answer their question. Then Admin will receive notification for the same and they can look into the matter after that.
<br>
<i><b>Benefits:</b></i> The benefits of this feature is to provide effective communication between Admins of college and college students.
<br>
<i><b>Requirements:</b></i> The students must not ask any irrelevant question to spam and make chaos for the admins. They must specify their question very clearly for the admins to avoid any kind of inconvenience for both the sides.

### 6. Help and About
<i><b>Brief:</b></i> Help and About feature is kept in the app the provide user friendly experience while using the app. This feature will be present on some of the activities to guide the user there way through.
<br>
<i><b>Benefits:</b></i> This feature will provide better understanding of the app and its flow to even a new android user.
<br>
<i><b>Requirements:</b></i> The user must have basic understanding of English language.

## Developers

<table>
  <tr>
    <td align="center">Riyanshi Verma</td>
     <td align="center">Vanshika</td>
    <td align="center">Mansi Bhatt</td>
  </tr>
  <tr>
    <td><a href="https://github.com/Riyanshi243" target="_blank"><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/Photo_RiyanshiVerma.jpg" alt="Riyanshi Verma" style="width:150px;height:150px;"></a></td>
    <td><a href="https://github.com/m-vanshika" target="_blank"><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/Photo_Vanshika.jpg" alt="Vanshika" style="width:150px;height:150px;"></a></td>
    <td><a href="https://github.com/mansibhatt3035" target="_blank"><img src="https://github.com/Riyanshi243/StudentHelpDesk/blob/master/Images/Photo_MansiBhatt.jpg" alt="Mansi Bhatt" style="width:150px;height:150px;"></a></td>
  </tr>
  <tr>
    <td align="center"><a href="https://www.linkedin.com/in/riyanshi-verma-779535191/" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="https://www.linkedin.com/in/riyanshi-verma-779535191/" height="20" width="30" /></a>
</p></td>
     <td align="center"><a href="https://www.linkedin.com/in/m-vanshika/" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="https://www.linkedin.com/in/m-vanshika/" height="20" width="30" /></a>
</p></td>
    <td align="center"><a href="https://www.linkedin.com/in/mansi-bhatt-740106205/" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="https://www.linkedin.com/in/mansi-bhatt-740106205/" height="20" width="30" /></a>
</p></td>
  </tr>
 </table>
