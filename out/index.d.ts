import * as React from "react";
import { ViewProperties } from 'react-native';
export interface UnityViewMessageEventData {
    message: string;
}
export interface IUnityToReactMessage {
    method: string;
    arguments: any;
}
export interface UnityViewMessage {
    name: string;
    data: any;
    callBack?: (data: any) => void;
}
export interface UnityViewProps extends ViewProperties {
    /**
     * Receive message from unity.
     */
    onMessage?: (message: IUnityToReactMessage) => void;
    onUnityMessage?: (handler: MessageHandler) => void;
}
export declare class MessageHandler {
    static deserialize(viewHandler: number, message: string): MessageHandler;
    id: number;
    seq: 'start' | 'end' | '';
    name: string;
    data: any;
    private viewHandler;
    constructor(viewHandler: number);
    send(data: any): void;
}
export default class UnityView extends React.Component<UnityViewProps> {
    static propTypes: any;
    /**
     * Send Message to Unity.
     * This message will be handled by the RNListener in Unity.
     * @param method Method name to call.
     * @param args Arguments of the call.
     */
    postMessage(method: string, args: any): void;
    /**
     * Send Global Message to Unity.
     * @param gameObject The Name of GameObject. Also can be a path string.
     * @param methodName Method name in GameObject instance.
     * @param message The message will post.
     */
    postGlobalMessage(gameObject: string, methodName: string, message: string): void;
    /**
     * Pause the unity player
     */
    pause(): void;
    /**
     * Resume the unity player
     */
    resume(): void;
    /**
     * Send Message to UnityMessageManager.
     * @param message The message will post.
     */
    postMessageToUnityManager(message: string | UnityViewMessage): void;
    private getViewHandle;
    private onMessage;
    render(): JSX.Element;
}
