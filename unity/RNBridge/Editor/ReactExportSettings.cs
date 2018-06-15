using UnityEngine;
using UnityEditor;
using System.Collections;

[CreateAssetMenu(fileName = "ExportSettings", menuName = "React Export/Export settings", order = 1)]
public class ReactExportSettings : ScriptableObject
{
    public string XcodeProjectRoot = "../ios";
    public string XcodeProjectName = "ReactExample";
    public iOSSdkVersion iOSBuildMode = iOSSdkVersion.DeviceSDK;
    public iOSBuildType iOSBuildType = iOSBuildType.Release;
}