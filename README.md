# ReflecTell
***The StoryLab at Texas A&M Android Wear Application***

### Purpose
The purpose of this application is to create an informal science learning application on a wearable device. This program can then be modified based on the scope of the study for future potential publications. This is the **Storified** version of the app.

- - - -
### Layout
* IntroTheme
* Theme
* NewMainMenu
    * LevelSelect
        * MessagePrompt
        * MessageRecord
        * MessagePromptEnd
    * TopicReview
    * ChapterCollection
    
- - - -
### Class Initialization
The application loads initially into the IntroTheme class, displaying the initial purpose of the application as well as the current science topic. Next, the Theme class is loaded which allows the user to select a different theme which changes the assets of the application. Once the user selects a theme, the main data structure, ChapterDataSingleton, is loaded. This singleton contains all of the data required written in corresponding text files for the remainder of the application. The user in then placed in the main menu, where they can record new stories, review the lesson, or playback old recordings.

- - - -
### Recording Notes
To compensate Android Wear from being unable to record using the general Android recording class, the MessageRecord class contains code to record an audio stream into a .PCM file. This .PCM file is then given a header, translated into a .WAV file, and the .PCM is then deleted. The application then uses the .WAV file for audio playback in the ChapterCollection class.

- - - -
### Side Classes

#### UnitDataSingleton
Contains data regarding the current science unit for the app. This currently servers next to no function in the application, but was created to be able to switch between multiple science versions remotely from a web interface for the teacher.

#### MessagePromptNotification
Creates a notification that places the user into the highest level available to them. Currently, a MessagePromptNotification is created every time the app is loaded, set for activation in 5 hours. This code can be seen in the NewMainMenu main.

- - - -
### To run
Download this repository directly to Android Studio. Can either run via the debugger or create a .APK file to install via scripts.
