using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Runtime.InteropServices;
using UnityEngine.SceneManagement;

namespace ReactNative
{
    public class RNMessage
    {
        public string method;
        public JSON arguments;

        public RNMessage(string _method, Dictionary<string, object> _arguments)
        {
            this.method = _method;
            this.arguments = new JSON();
            this.arguments.fields = _arguments;
        }

        public RNMessage(string json)
        {
            JSON data = new JSON();
            data.serialized = json;
            this.method = data.ToString("method");
            this.arguments = data.ToJSON("arguments");
        }
    }

    public class RNBridge : MonoBehaviour
    {

#if UNITY_IOS && !UNITY_EDITOR
    [DllImport("__Internal")]
    private static extern void onUnityMessage (string message);
#elif UNITY_ANDROID && !UNITY_EDITOR
    private static void onUnityMessage (string message)
    {
		using (AndroidJavaClass jc = new AndroidJavaClass("com.reactnative.unity.view.UnityUtils"))
		{
			jc.CallStatic("onUnityMessage", message);
		}
    }
#else
        private static void onUnityMessage(string message) { }
#endif

        public static RNBridge Instance { get; private set; }

        public delegate void CallFromNativeDelegate(RNMessage message);
        public event CallFromNativeDelegate OnCallFromNative;

        static RNBridge()
        {
            GameObject go = new GameObject("RNListener");
            DontDestroyOnLoad(go);
            Instance = go.AddComponent<RNBridge>();
        }

        public void CallFromNative(string json)
        {
            if (OnCallFromNative != null)
            {
                OnCallFromNative(new RNMessage(json));
            }
        }

        public void CallToNative(RNMessage message)
        {
            RNBridge.onUnityMessage(JSON.SerializeAny(message));
        }

    }
}