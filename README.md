# student-version

## setup:
1. Download android studio: https://developer.android.com/studio
2. Using Android Studio, click file -> open -> select this repository
3. Click "build" button, which is a green hammer on the top of Android Studio
4. On the top of the screen of Android Studio, click "AVD manager"
5. Create a new virtual device using Android 9.0
6. Click the "run" button, which is the green arrow on the top of Android Studio, ensure that you are running on the emulater you made.
7. There is no user authentication so when "login in" just put in whatever you want.

# Developer Manual (front end)

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

