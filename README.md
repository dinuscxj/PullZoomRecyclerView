
## PullZoomRecyclerView
Using recyclerview requires three steps:<br/>
* Step one: use the PullZoomRecyclerView in XML<br/>
* Step two: call the function setAdapter and the function setLayoutManager<br/>
* Step threee: call the function setZoomView and the function setHeaderContainer<br/>
![](https://raw.githubusercontent.com/dinuscxj/PullZoomRecyclerView/master/Preview/device-2015-08-30-182407.png?width=300)
![](https://raw.githubusercontent.com/dinuscxj/PullZoomRecyclerView/master/Preview/device-2015-08-30-182433.png?width=300)<br/>

## Features
 * Two Pull Zoom mode (ZOOM_FOOTER or ZOOM_HEADER)
 * Listening pull process (including pullStart、 pullZooming, pullEnd)
 * You can reset the default smooth scroll to top interplator(the default is DecelerateInterpolator)

## Usage
 ```java
 <app.dinus.com.pullzoomrecyclerview.recyclerview.PullZoomRecyclerVie
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
 ```
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
