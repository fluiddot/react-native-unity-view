import * as React from "react";
import { requireNativeComponent, ViewProperties, findNodeHandle, NativeModules, NativeSyntheticEvent } from 'react-native';
import * as PropTypes from "prop-types";
import * as ViewPropTypes from 'react-native/Libraries/Components/View/ViewPropTypes';

const { UIManager } = NativeModules;

const UNITY_VIEW_REF = 'unityview';

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
    public static propTypes = {
        ...ViewPropTypes,
        onMessage: PropTypes.func
    }

    /**
     * Send Message to Unity.
     * This message will be handled by the RNListener in Unity.
     * @param method Method name to call.
     * @param args Arguments of the call.
     */
    public postMessage(method: string, args?: any) {
        UIManager.dispatchViewManagerCommand(
            this.getViewHandle(),
            UIManager.UnityView.Commands.postMessage,
            ['RNListener', 'CallFromNative', JSON.stringify({method, arguments: args})]
        );
    };

    /**
     * Pause the unity player
     */
    public pause() {
        UIManager.dispatchViewManagerCommand(
            this.getViewHandle(),
            UIManager.UnityView.Commands.pause,
            []
        );
    };

    /**
     * Resume the unity player
     */
    public resume() {
        UIManager.dispatchViewManagerCommand(
            this.getViewHandle(),
            UIManager.UnityView.Commands.resume,
            []
        );
    };

    private getViewHandle() {
        return findNodeHandle(this.refs[UNITY_VIEW_REF] as any);
    }

    private onMessage(event: NativeSyntheticEvent<IUnityMessage>) {
        const message = event.nativeEvent
        if (this.props.onMessage) {
            this.props.onMessage(message);
        }
    }

    public render() {
        const { ...props } = this.props;
        return (
            <NativeUnityView
                ref={UNITY_VIEW_REF}
                {...props}
                onMessage={this.onMessage.bind(this)}
            >
            </NativeUnityView>
        );
    }
}

const NativeUnityView = requireNativeComponent<UnityViewProps>('UnityView', UnityView);