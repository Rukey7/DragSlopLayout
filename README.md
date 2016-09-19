# DragSlopLayout
一个辅助开发的UI库，适用于某些特殊场景，如固定范围拖拽、动画、模糊效果等。

Screenshot
---

- Drag模式，可以和 ViewPager 联动


- Animate模式，同样可以和 ViewPager 联动(自定义动画无联动效果)


- Blur模糊效果，包括局部模糊和全图模糊


Gradle
---

### dependencies
```groovy
    compile 'com.dl7.drag:dragsloplayout:1.0.0'
```
### Enable RenderScript support mode
```groovy
android {
    defaultConfig {
        renderscriptTargetApi 23
        renderscriptSupportModeEnabled true
    }
}
```

Usage
---

### Setup
    ```xml
		<com.dl7.drag.DragSlopLayout
            android:id="@+id/drag_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/black"
            app:fix_height="80dp"
            app:mode="drag">
        
            <!-- Content View -->
            <android.support.v4.view.ViewPager
                android:id="@+id/vp_photo"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
        
            <!-- Drag View -->
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">
                // ......
            </LinearLayout>
            // ......
        </com.dl7.drag.DragSlopLayout>
	```
	
### 和 ViewPager 联动
    如果你的 Content View 为 ViewPager，可以通过以下方法来实现联动效果：
    ```groovy
    mDragLayout.interactWithViewPager(true);
    ```
    
### 实现拖拽和 ScrollView 的平滑滚动
    如果你的 Drag View 包含 ScrollView 或则 NestedScrollView，可以通过以下方法来实现平滑滚动：
    ```groovy
    mDragLayout.setAttachScrollView(mSvView);
    ```
    
### Content View 的动态模糊
    说是动态模糊可能不太准确，这功能是通过模糊预处理再来加载的，所以对于 Content View  为 ViewPager 的界面不适用，目前主要用来模糊固定的背景界面。可以通过以下方法启用模糊效果：
    ```groovy
    mDragLayout.setEnableBlur(true);
    ```
    可以控制局部模糊还是全背景模糊：
    ```groovy
    mDragLayout.setBlurFull(boolean blurFull);
    ```
    
### 控制 Drag View 的进入和退出
    在 Drag 模式：
    ```groovy
    mDragLayout.scrollInScreen(int duration);
    mDragLayout.scrollOutScreen(int duration);
    ```
    在 Animate 模式：
    ```groovy
    mDragLayout.startInAnim();
    mDragLayout.startOutAnim();
    ```
    
Thanks
---

- [500px-android-blur](https://github.com/500px/500px-android-blur)
- [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations)
    
License
-------

    Copyright 2016 Rukey7

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
