# student-version
[User Manual](#User-Manual)
[Developer Manual](#Developer-Manual)

## setup:
1. Download android studio: https://developer.android.com/studio
2. Using Android Studio, click file -> open -> select this repository
3. Click "build" button, which is a green hammer on the top of Android Studio
4. On the top of the screen of Android Studio, click "AVD manager"
5. Create a new virtual device using Android 9.0
6. Click the "run" button, which is the green arrow on the top of Android Studio, ensure that you are running on the emulater you made.
7. There is no user authentication so when "login in" just put in whatever you want.

# User Manual

This is a user manula for the front end of the student mobile application.

## What does the student app do?

The student app allows students to join the virtual and physical queues in for a given company and a given career fair. Students will also manage their account on the app so they can edit things such as their resume and their profile information. 

Students would use this application to avoid long lines at crowded career fairs to save time in the recruiting process.

## How to install

Currently the application is only available to install by cloning the github repository. Here are the steps to get set up:
1. Clone the github repo [student-version](https://github.com/Elegant-Queueing/student-version)
2.  Download android studio:  [https://developer.android.com/studio](https://developer.android.com/studio)
3.  Using Android Studio, click file -> open -> select this repository
4.  Click "build" button, which is a green hammer on the top of Android Studio
5.  On the top of the screen of Android Studio, click "AVD manager"
6.  Create a new virtual device using Android 9.0
7. If you are going to run the back end locally, ensure you have the redis server and the backend server set up and running. See the backend documentation for more details. If you are running the backend on an instance, disregard this step.
8. Ensure you are connected to the corrent API by looking at and editing the "API_URL" variable in the MainActivity class in Android Studio. If running localy, this should be "http://10.0.2.2:8080" If running on an instance should be the url of that instance.

## How to run

After following all the steps above, click the "run" button on the top of Android Studio to run the app on your emulator. The run button is a green arrow.

## How to use

Here is a tutorial on how to use the features  of the app. Features that are not yet implimented and are not available are marked as such.
![Starting screen](https://i.imgur.com/QEXs2Pd.png)

In the starting screen click sign in if you already have an account and sign up if you do not, then fill out the prompted information.
**This feature is a work in progress, to progress from this screen, log in with any dummy information**

![fairs screen](https://i.imgur.com/080dINq.png)

After logging in you will see the fairs that are available to you. Simply click the fair that you are attending in the "Upcoming" section to see the companies you can join the queue of in that fair. To see the companies you visited in the past, click a fair in the "Past" section to see the companies you visited at that fair. To find a specific fair, use the search bar.
**Past fairs and search are not yet implemented**

![companies screen](https://i.imgur.com/r4WrXZt.png)
To join the queue of a company, simply tap a company. You can see how compatible you are with each company and what the current wait time of that company is. To find a specific comapny, use the search bar.
**Search, match percentage, and wait times are not yet implemented on this screen**


![company profile](https://i.imgur.com/VIxACVb.png)

When you a click on a company you can join the queue by clicking join queue. You can and also view the bio, estimated wait time, and the recruiters at that career fair. You can also see why you matched with this company. To get more info on a recruiter, click more info on their card.
**Recruiter information and match percentage features are not yet implemented**

When you join a queue you will see who you are meeting and if you are meeting as well as have the option to leave the queue. If you are in the top 5 of the queue you will be told as such and be prompted to join the physical queue and scan the employee's QR code indicating this. Once you do this the employee will register you when you arrive and be automatically removed from the queue.
**QR code scanning is not yet implemented, simply click "I'm here" in the current version in order to join the physical queue. Employee information is also not yet implemented.**

![profile page](https://i.imgur.com/8wn10pq.png)

To edit your profile click the top left profile icon. This will allow you to edit your resume as well.
**This is not yet implemented.**

## How to report a bug

To report a bug, use [github issues](https://github.com/Elegant-Queueing/student-version/issues).
When reporting a bug, be sure to include the following vital information:

 - What version of the app are you running?
 - What version of android are you running the app on?
 - A clear and concise summary of the issue.
 - A list of steps to reproduce the issue.
 - An explaination of what you expected to happen, and what actually happened.


# Developer Manual

This is a developer manula for the front end of the student mobile application.

## How to obtain source code
Clone this [github repository](https://github.com/Elegant-Queueing/student-version) to obtain the source code. The back-end is obtained through a [seperate repository](https://github.com/Elegant-Queueing/q-backend).

The best way to view source code is through android studio. Follow these instructions to do so:
1. Clone the github repo [student-version](https://github.com/Elegant-Queueing/student-version)
2.  Download android studio:  [https://developer.android.com/studio](https://developer.android.com/studio)
3.  Using Android Studio, click file -> open -> select this repository

## Layout of directory structure
The way the directory structure is represented in a file system is different from the way it is represented in Android Studio. For this documentation I will be refrencing the way it is layed out in Android Studio.

This layout structure is as such:

 - app
	 - manifests
	 - java
	 - java (generated)
	 - res
 - Gradle Scripts

### Now to look at each of these individually:

#### manifests
contains the AndroidManifest.xml file

#### java
 - com.example.q_student
	 - Contains the activities and fragments for the app
- com.example.q_student (androidTest)
	- Contains the Instrumented Tests that run on the android device
- com.example.q_student (test)
	- Contains the unit tests that run on the machine
- Objects
	- Contains Java objects used to hold data (such as Fair and Company)

#### java (generated)
Contains Android Studio generated files (BuildConfig)

#### res
This contains resources used by the activies and views
 - drawable
	 - contains pictures and icons
- layout
	- Contains layouts
- mipmap
	- Contains mipmap images
- values
	- Contains varius values:
		- colors
		- dimensions
		- ids
		- strings
		- styles
#### Gradle Scripts
contains the Gradle build files

## How to build

After following the instructions to open the file in Android Studio, simply click "build" button, which is a green hammer on the top of Android Studio

## How to test
in com.example.q_student (test) there is a file called ExampleUnitTest. Open this file and you will see all the unit tests for the application. There are two arrows next to the header for the ExampleUnitTest class, click this to run all tests, or click the arrow next to a specific test method to run that specific test.

## How to create new tests

In either ExampleUnitTest or ExampleInstrumentedTest create new methods with the @test header to create a new test. Use assert statements for these methods, and ensure they are named clearly to state what is being tested, using underscores instead of camel cases.

## How to build a release

In the generated Java BuildConfig files, manually update the version code and the version name before release.

