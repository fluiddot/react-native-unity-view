package com.reactnative.unity.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.unity3d.player.UnityPlayer;

import java.util.concurrent.CopyOnWriteArraySet;

import org.json.JSONObject;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by xzper on 2018-03-08.
 * Edited by fluiddot 2018-06-17
 */

public class UnityUtils {
    private static UnityPlayer unityPlayer;

    private static final CopyOnWriteArraySet<UnityEventListener> mUnityEventListeners =
            new CopyOnWriteArraySet<>();

    public static UnityPlayer getPlayer() {
        return unityPlayer;
    }

    public static boolean hasUnityPlayer() {
        return unityPlayer != null;
    }

    public static void createPlayer(Context context) {
        if (unityPlayer != null) {
            return;
        }
        final Activity activity = ((Activity)context);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().setFormat(PixelFormat.RGBA_8888);
                int flag = activity.getWindow().getAttributes().flags;
                boolean fullScreen = false;
                if((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                    fullScreen = true;
                }

                unityPlayer = new UnityPlayer(activity);

                try {
                    // wait a moument. fix unity cannot start when startup.
                    Thread.sleep( 1000 );
                } catch (Exception e) {
                }

                // start unity
                addUnityViewToBackground();
                unityPlayer.windowFocusChanged(true);
                unityPlayer.requestFocus();
                unityPlayer.resume();

                // restore window layout
                if (!fullScreen) {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        });
    }

    public static void postMessage(String gameObject, String methodName, String message) {
        UnityPlayer.UnitySendMessage(gameObject, methodName, message);
    }

    /**
     * Invoke by unity C#
     */
    public static void onUnityMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject = new JSONObject(message);
        }
        catch(org.json.JSONException exception) {
          System.out.println("JSON parse exception: " + exception);
        }

        for (UnityEventListener listener : mUnityEventListeners) {
            try {
                listener.onMessage(jsonObject);
            } catch (Exception e) {
            }
        }
    }

    public static void addUnityEventListener(UnityEventListener listener) {
        mUnityEventListeners.add(listener);
    }

    public static void removeUnityEventListener(UnityEventListener listener) {
        mUnityEventListeners.remove(listener);
    }

    public static void addUnityViewToBackground() {
        if (unityPlayer.getParent() != null) {
            ((ViewGroup)unityPlayer.getParent()).removeView(unityPlayer);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unityPlayer.setZ(-1f);
        }
        final Activity activity = ((Activity)unityPlayer.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1, 1);
        activity.addContentView(unityPlayer, layoutParams);
    }

    public static void addUnityViewToGroup(ViewGroup group) {
        if (unityPlayer.getParent() != null) {
            ((ViewGroup)unityPlayer.getParent()).removeView(unityPlayer);
        }
        group.addView(unityPlayer, MATCH_PARENT, MATCH_PARENT);
        unityPlayer.windowFocusChanged(true);
        unityPlayer.requestFocus();
        unityPlayer.resume();
    }
}
