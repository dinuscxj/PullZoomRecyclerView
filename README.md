
## PullZoomRecyclerView
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PullZoomRecyclerView-green.svg?style=true)](https://android-arsenal.com/details/3/3343)

Using RecyclerView requires three steps:<br/>
* Step one: use the PullZoomRecyclerView in XML<br/>
* Step two: call the function setAdapter and the function setLayoutManager<br/>
* Step three: call the function setZoomView and the function setHeaderContainer<br/>
![](https://raw.githubusercontent.com/dinuscxj/PullZoomRecyclerView/master/Preview/PullZoomFooter.gif?width=300)
![](https://raw.githubusercontent.com/dinuscxj/PullZoomRecyclerView/master/Preview/PullZoomHeader.gif?width=300)<br/>

## Features
 * Two Pull Zoom mode (ZOOM_FOOTER or ZOOM_HEADER)
 * Listening pull process (including pullStart、 pullZooming, pullEnd)
 * You can reset the default smooth scroll to top interpolator(the default is DecelerateInterpolator)

## Usage
 Add dependency
 ```gradle
 dependencies {
    compile 'com.dinuscxj:pullzoomrecyclerview:1.0.0'
 }
 ```

 Used in xml
 ```xml
 <com.dinuscxj.pullzoom.PullZoomRecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
 ```

 Used in java
 ```java
 mPullZoomRecyclerView.setAdapter(new Adapter());
 mPullZoomRecyclerView.setLayoutMannager(new LinearLayoutManager(context));
 ```
 ```java 
 mPullZoomRecyclerView.setZoomView(zoomView);
 mPullZoomRecyclerView.setHeaderContainer(headerContainer);
 ```
## License
    Copyright 2015-2019 dinus

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
