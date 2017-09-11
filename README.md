# Uni FrameWork - Dynamic Async Viewer

### Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
    
    
### Add the dependency
```
dependencies {
  compile 'com.github.JaeWoongOh:Uni:v1.5.5'
}
```

## Typical development process
1. Executes on pre initializes the view in layout 
2. Executes on background thread
2. Executes on post initializes the view in layout
