# Pre-work - *Remind ME*

**Remind ME** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Ganesh Kudva**

Time spent: **6** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Animations in Dialog Fragment, to show time and date
* [x] Added splash screen

## Video Walkthrough

![Remind ME](RemindMe.gif)

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** I do not have much experience with iOS app development. I have heard, creating layouts is easier in iOS. I feel layouts in Android are a bit cumbersome & ends up taking significant amount of app development time.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** Adapters in Android act as a bridge between the UI components and the data source that fills the data into the UI components.They are a link between a set of data and **AdapterView** which displays data.
Let’s assume you want to display a list in your Android app. For this you will use the ListView provided by Android. ListViews don’t actually contain any data themselves. It’s just a UI element without data in it. You can populate your ListViews by using an Android adapter.
Adapter is an interface whose implementations provide data and control the display of that data. ListViews own adapters that completely control the ListView’s display. So adapters control the content displayed in the list as well as how to display it.

Android's ListView uses an Adapter to fill itself with Views. When the ListView is shown, it starts calling `getView` to populate itself. When the user scrolls a new view should be created, so for performance the ListView sends the Adapter an old view that it's not used any more in the convertView param.
`convertView` is used strictly to increase the performance of your Adapter. When a ListView uses an Adapter to fill its rows with Views, the adapter populates each list item with a View object by calling `getView()` on each row. The Adapter uses the convertView as a way of recycling old View objects that are no longer being used. In this way, the ListView can send the Adapter old, "recycled" view objects that are no longer being displayed instead of instantiating an entirely new object each time the Adapter wants to display a new list item. This is the purpose of the `convertView` parameter.

## Notes

Describe any challenges encountered while building the app.
* Faced some issue while interacting with SQLite. The way I created the table was wrong, resulting in improper/junk values being fetched from SQLite.
* Faced issues with Dialog Fragment, specifically while performing animations to display option for date and time. 

## License

    Copyright [2017] [Ganesh Kudva]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
