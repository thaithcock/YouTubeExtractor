# YouTubeExtractor
A helper to extract the streaming URL from a YouTube video using RxJava and Retrofit

[![Build Status](https://travis-ci.org/Commit451/YouTubeExtractor.svg?branch=master)](https://travis-ci.org/Commit451/YouTubeExtractor)
[![](https://jitpack.io/v/Commit451/YouTubeExtractor.svg)](https://jitpack.io/#Commit451/YouTubeExtractor)

This library was originally found [here](https://github.com/flipstudio/YouTubeExtractor) in a project by [flipstudio](https://github.com/flipstudio). It has since been modified and cleaned up a bit to make it more user friendly.

# Gradle Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Then, add the library to your project `build.gradle`
```gradle
dependencies {
    compile 'com.github.Commit451:YouTubeExtractor:latest.version.here'
}
```

# Usage
Under the hood, this library uses [Retrofit](http://square.github.io/retrofit/) to fetch the video metadata as an RxJava Single. If you are familiar with the Retrofit public API, this library will be a breeze for you.

```java
//create one of these and reuse it for performance reasons
YouTubeExtractor extractor = YouTubeExtractor.create();
extractor.extract("9d8wWcJLnFI")
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(new SingleObserver<YouTubeExtractionResult>() {
        @Override
        public void onSubscribe(Disposable d) {
            //You should store this disposable and dispose when appropriate
        }
    
        @Override
        public void onSuccess(YouTubeExtractionResult value) {
            bindVideoResult(value);
        }
    
        @Override
        public void onError(Throwable e) {
            MainActivity.this.onError(e);
        }
    });
```
Note: the above example also requires [RxAndroid](https://github.com/ReactiveX/RxAndroid) for `AndroidSchedulers`

As you can with Retrofit+RxJava, you can also extract the result right away:
```java
// this will extract the result on the current thread. Don't use this on the main thread!
YouTubeExtractionResult result = extractor.extract(GRID_VIDEO_ID)
    .blockingGet();
```

# Video Playback
As stated before, this library was only created to extract video stream Urls from YouTube. I recommend using the [ExoMedia](https://github.com/brianwernick/ExoMedia) library to play the video streams to the user. See the sample app for this library for an example.

License
--------

    Copyright 2017 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
