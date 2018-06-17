import * as React from "react";
import { ViewProperties } from 'react-native';
export interface IUnityMessage {
    method: string;
    arguments: any;
}
export interface UnityViewProps extends ViewProperties {
    /**
     * Receive message from unity.
     */
    onMessage?: (message: IUnityMessage) => void;
}
export default class UnityView extends React.Component<UnityViewProps> {
    static propTypes: any;
    /**
     * Send Message to Unity.
     * This message will be handled by the RNListener in Unity.
     * @param method Method name to call.
     * @param args Arguments of the call.
     */
    postMessage(method: string, args?: any): void;
    /**
     * Pause the unity player
     */
    pause(): void;
    /**
     * Resume the unity player
     */
    resume(): void;
    private getViewHandle;
    private onMessage;
    render(): JSX.Element;
}
