# react-native-unity-view

Integrate unity3d within a React Native app. Add a react native component to show unity. Works on both iOS and Android.

## How to use

### Install

```
npm install git://github.com/fluiddot/react-native-unity-view.git --save
(original) npm install react-native-unity-view --save

react-native link react-native-unity-view
```

### Add Unity Project

1. Create an unity project, Example: 'Cube'.
2. Create a folder named `unity` in react native project folder.
3. Move unity project folder to `unity` folder.

Now your project files should look like this.

```
.
├── android
├── ios
├── unity
│   └── <Your Unity Project>    // Example: Cube
├── node_modules
├── package.json
├── README.md
```

### Configure Player Settings

1. First Open Unity Project.
2. Click Menu: File => Build Settings => Player Settings
3. Add RNBridge.package from `node_modules/react-native-unity-view/unity/RNBridge.unitypackage`
4. Setup export settings in: `unity/Assets/RNBridge/Editor/ExportSettings`

- `Xcode project Name`: Your react native xcode project name
- `iOS Build Mode`: Change this depending whether you want to build for device or simulator
- `iOS Build Type`: Debug or Release

**IOS Platform**:

Other Settings find the Rendering part, uncheck the `Auto Graphics API` and select only `OpenGLES2`.

### Build

Open your unity project in Unity Editor. Now you can export unity project with `Build/Export Android` or `Build/Export IOS` menu.

![image](https://user-images.githubusercontent.com/7069719/37091489-5417a66c-2243-11e8-8946-4d9e1ac652e8.png)

Android will export unity project to `android/UnityExport`.

IOS will export unity project to `ios/UnityExport`.

### Configure Native Build

#### Android Build

Make alterations to the following files:

- `android/settings.gradle`

```
...
include ":UnityExport"
project(":UnityExport").projectDir = file("./UnityExport")
```

#### IOS Build

1. Open your react native project in XCode.
2. Drag `node_modules/react-native-unity-view/ios/Config.xcconfig` to yout XCode project. Choose `Create folder references`.
3. Setting `.xcconfig` to project.
![image](https://user-images.githubusercontent.com/7069719/37093471-638b7810-224a-11e8-8263-b9882f707c15.png)
4. Go to Targets => Build Settings. Change `Dead Code Stripping` to `YES`.
![image](https://user-images.githubusercontent.com/7069719/37325486-182c7bd4-26c9-11e8-9fc0-8e1a149d30b2.png)

### Use In React Native

#### Props

##### `onMessage`

Receive message from unity.

Example:

1. Send Message use C#.

```
UnityMessageManager.Instance.SendMessageToRN("click");
```

2. Receive Message in javascript

```
onMessage(event) {
    console.log('OnUnityMessage: ' + event.nativeEvent.message);    // OnUnityMessage: click
}

render() {
    return (
        <View style={[styles.container]}>
            <UnityView
                style={style.unity}
                onMessage={this.onMessage.bind(this)}
            />
        </View>
    );
}
```

#### Methods

##### `postMessage(gameObject: string, methodName: string, message: string)`

Send message to unity.

* `gameObject` The Name of GameObject. Also can be a path string.
* `methodName` Method name in GameObject instance.
* `message` The message will post.

Example:

1. Add a message handle method in `MonoBehaviour`.

```
public class Rotate : MonoBehaviour {
    void handleMessage(string message) {
		Debug.Log("onMessage:" + message);
	}
}
```

2. Add Unity component to a GameObject.

3. Send message use javascript.

```
onToggleRotate() {
    if (this.unity) {
      // gameobject param also can be 'Cube'.
      this.unity.postMessage('GameObject/Cube', 'toggleRotate', 'message');
    }
}

render() {
    return (
        <View style={[styles.container]}>
            <UnityView
                ref={(ref) => this.unity = ref}
                style={style.unity}
            />
            <Button label="Toggle Rotate" onPress={this.onToggleRotate.bind(this)} />
        </View>
    );
}

```

##### `postMessageToUnityManager(message: string)`

Send message to `UnityMessageManager`.

Please copy [`UnityMessageManager.cs`](https://github.com/f111fei/react-native-unity-demo/blob/master/unity/Cube/Assets/Scripts/UnityMessageManager.cs) to your unity project and rebuild first.

Same to `postMessage('UnityMessageManager', 'onMessage', message)`

This is recommended to use.

* `message` The message will post.

Example:

1. Add a message handle method in C#.

```
void Awake()
{
    UnityMessageManager.Instance.OnMessage += toggleRotate;
}

void onDestroy()
{
    UnityMessageManager.Instance.OnMessage -= toggleRotate;
}

void toggleRotate(string message)
{
    Debug.Log("onMessage:" + message);
    canRotate = !canRotate;
}
```

2. Send message use javascript.

```
onToggleRotate() {
    if (this.unity) {
      this.unity.postMessageToUnityManager('message');
    }
}

render() {
    return (
        <View style={[styles.container]}>
            <UnityView
                ref={(ref) => this.unity = ref}
                style={style.unity}
            />
            <Button label="Toggle Rotate" onPress={this.onToggleRotate.bind(this)} />
        </View>
    );
}
```

##### `pause()`

Pause the unity player.

Thanks [@wezzle](https://github.com/wezzle). See [#13](https://github.com/f111fei/react-native-unity-view/pull/13)

##### `resume()`

Resume the unity player.


#### Example Code

```
import React from 'react';
import { StyleSheet, Image, View, Dimensions } from 'react-native';
import UnityView from 'react-native-unity-view';

export default class App extends React.Component<Props, State> {
    render() {
    return (
      <View style={styles.container}>
        <UnityView style={{ position: 'absolute', left: 0, right: 0, top: 0, bottom: 0, }} /> : null}
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
      </View>
    );
  }
}
```

Enjoy!!!

