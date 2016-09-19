# DragSlopLayout
一个辅助开发的UI库，适用于某些特殊场景，如固定范围拖拽、动画、模糊效果等。

Screenshot
---

- Drag模式，可以和 ViewPager 联动


- Animate模式，同样可以和 ViewPager 联动(自定义动画无联动效果)


- Blur模糊效果，包括局部模糊和全图模糊


Gradle
---

#### dependencies
```groovy
    compile 'com.dl7.drag:dragsloplayout:1.0.0'
```
#### Enable RenderScript support mode
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

### XML

