package messenger.messenger.messanger.messenger.services;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.squareup.otto.Subscribe;

import app.common.utils.Logger;
import app.common.utils.PrefUtils;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.Constants;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.CurtainConfig;
import messenger.messenger.messanger.messenger.utils.BusProvider;
import messenger.messenger.messanger.messenger.views.activities.Splash;
import timber.log.Timber;

import static app.common.utils.Utils.getDimens;

/**
 * Created by bedprakash.r on 01/04/18.
 */

public class CurtainService extends Service {
    private static final long WINDOW_CLOSE_DELAY = 2000;
    private static final String TAG = CurtainService.class.getSimpleName();
    private FrameLayout curtainWindow;
    private WindowManager windowManager;
    private float screenHeight;
    private int sliderHeight;
    private CurtainConfig curtainConfig;
    private View windowContainer;
    private View settingsWindow;
    private View brightnessWindow;
    private VisibilityHandler handler;
    static boolean isRunning = false;
    private int MIN_DISTANCE = 10;
    private boolean isStarted;

    public static void start(Context context) {
        Intent i = new Intent(context, CurtainService.class);
        context.startService(i);
        isRunning = true;
    }

    public static void stop(Context context) {
        Intent i = new Intent(context, CurtainService.class);
        context.stopService(i);
        isRunning = false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isIsRunning() {
        return isRunning;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        if (!AppUtils.canDrawOverlayViews(this)) {
            stopSelf();
            AppUtils.logException(new RuntimeException("Can not draw but service started"));
            isStarted = false;
            return;
        }
        if (isStarted) {
            return;
        }

        isStarted = true;


        BusProvider.getUiBus().register(this);
        isRunning = true;
        curtainConfig = AppUtils.getCurtainConfig();
        screenHeight = AppUtils.getScreenHeight(CurtainService.this);

        handler = new VisibilityHandler();

        //Inflate the chat head layout we created
        curtainWindow = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.layout_curtain, null);
        final WindowManager.LayoutParams params = getLayoutParams(PrefUtils.getFloat(this, Constants.CURTAIN_HEIGHT_PREF, curtainConfig.curtainHeight));


        //Specify the chat head position

        //Add the view to the curtainWindow
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(curtainWindow, params);
        }


        windowContainer = curtainWindow.findViewById(R.id.window_container);
        windowContainer.setBackgroundColor(Color.parseColor(curtainConfig.curtainColor));
        windowContainer.setAlpha(curtainConfig.curtainAlpha);

        windowContainer.setVisibility(View.VISIBLE);

        LayoutTransition layoutTransition = curtainWindow.getLayoutTransition();
        layoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {

            @Override
            public void endTransition(LayoutTransition arg0, ViewGroup arg1,
                                      View arg2, int arg3) {
                int i = arg2.getId();
                if (i == R.id.window_container) {
                    if (LayoutTransition.DISAPPEARING == arg3) {
                        stopSelf();
                    }
                }
            }

            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {
                switch (view.getId()) {
                    //....
                }

            }
        });


        // setting up settings window.
        settingsWindow = LayoutInflater.from(this).inflate(R.layout.layout_curtain_settings, null);
        WindowManager.LayoutParams sliderParams = getTouchableLayoutParams();
        sliderParams.gravity = Gravity.TOP;
        sliderParams.y = getCurtainY();

        windowManager.addView(settingsWindow, sliderParams);


        settingsWindow.findViewById(R.id.curtain_close).setOnClickListener(view -> hideCurtain());
        settingsWindow.findViewById(R.id.curtain_settings).setOnClickListener(view -> openSettings());

        settingsWindow.findViewById(R.id.ic_brightness).setOnClickListener(view -> {
            // show brightness window
            if (brightnessWindow != null && brightnessWindow.getVisibility() == View.VISIBLE) {
                removeViewFromWindow(brightnessWindow);
                brightnessWindow = null;
            } else {
                brightnessWindow();
            }

        });

        View rootSettings = settingsWindow.findViewById(R.id.root_options);
        View settingsToggle = settingsWindow.findViewById(R.id.curtain_settings_show);
        settingsToggle.setOnClickListener(view -> {
            if (rootSettings.getVisibility() == View.VISIBLE) {
                rootSettings.setVisibility(View.GONE);
            } else {
                rootSettings.setVisibility(View.VISIBLE);
            }

        });


        View slider = settingsWindow.findViewById(R.id.slider);
        sliderHeight = slider.getHeight();
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            float initialTouchX = 0;
            float initialTouchY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        sliderHeight = slider.getHeight();
                        return true;

                    case MotionEvent.ACTION_UP:

                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < MIN_DISTANCE && Ydiff < MIN_DISTANCE) {
                            v.performClick();
                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.

//                        if (!isValidMove(event.getRawY(), initialTouchY)) {
//                            return false;
//                        }

                        WindowManager.LayoutParams updateParams = getLayoutParams((event.getRawY() - sliderHeight) / screenHeight);
                        sliderParams.y = getCurtainY();

                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(curtainWindow, updateParams);
                        windowManager.updateViewLayout(settingsWindow, sliderParams);
                        return true;
                }
                return false;
            }
        };
        slider.setOnTouchListener(touchListener);
        settingsToggle.setOnTouchListener(touchListener);

    }

    @SuppressLint("RtlHardcoded")
    @NonNull
    private WindowManager.LayoutParams getLayoutParams(float heightPercentage) {
        //Add the view to the curtainWindow.
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                (int) (heightPercentage * screenHeight),
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);


        params.gravity = Gravity.TOP | Gravity.LEFT | Gravity.START;
        curtainConfig.curtainHeight = heightPercentage;
        AppUtils.saveCurtainConfig(curtainConfig);
        return params;
    }

    @NonNull
    private WindowManager.LayoutParams getTouchableLayoutParams() {
        return new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_SYSTEM_ERROR : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    private void hideCurtain() {
        windowContainer.setVisibility(View.GONE);
        settingsWindow.setVisibility(View.GONE);
        if (brightnessWindow != null) {
            brightnessWindow.setVisibility(View.GONE);
        }
    }

    private void openSettings() {
        Intent i = new Intent(this, Splash.class);
        i.putExtra(Constants.FORWARD_ACTION, Constants.ACTION_COLOR_PICKER);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private boolean isValidMove(float rawY, float initialTouchY) {

        Timber.d("rawY:" + rawY + " initialTouchY " + initialTouchY + " sliderHeight:" + sliderHeight + " screenHeight:" + screenHeight);

        // if moved less that 2% ignore
//        if ((initialTouchY - rawY) < screenHeight * .02) {
//            return false;
//        }
        // If to the top then don't allow.
        if (rawY < sliderHeight / 2) {
            return false;
        }

        // if beyond bottom don't allow
        return !(rawY > screenHeight);
    }

    @SuppressLint("InflateParams")
    private void brightnessWindow() {
        brightnessWindow = LayoutInflater.from(this).inflate(R.layout.layout_curtain_brightness, null);
        SeekBar alphaSeekbar = brightnessWindow.findViewById(R.id.seek_alpha);
        alphaSeekbar.setProgress((int) (curtainConfig.curtainAlpha * 100));
        Message message = new Message();
        message.what = VisibilityHandler.HIDE_BRIGHTNESS_WINDOW;
        alphaSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                seekBar.setAlpha(i / 100);
                float alpha = 1 - ((100 - i)) / 100f;
                windowContainer.setAlpha(alpha);
                curtainConfig.curtainAlpha = alpha;
                AppUtils.saveCurtainConfig(curtainConfig);
                handler.removeMessages(VisibilityHandler.HIDE_BRIGHTNESS_WINDOW);
                Message newMsg = new Message();
                newMsg.what = VisibilityHandler.HIDE_BRIGHTNESS_WINDOW;
                handler.sendMessageDelayed(newMsg, WINDOW_CLOSE_DELAY);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        WindowManager.LayoutParams params = getTouchableLayoutParams();
        params.horizontalMargin = getDimens(R.dimen.activity_horizontal_margin);
        windowManager.addView(brightnessWindow, params);
        handler.sendMessageDelayed(message, WINDOW_CLOSE_DELAY);
    }

    @Override
    public void onDestroy() {
        if (!isStarted) {
            return;
        }
        super.onDestroy();
        if (windowManager != null) {
            if (curtainWindow != null) {
                removeViewFromWindow(curtainWindow);
            }
            if (settingsWindow != null) {
                removeViewFromWindow(settingsWindow);
            }
            if (brightnessWindow != null) {
                removeViewFromWindow(brightnessWindow);
            }
            handler = null;
        }
        isRunning = false;

        curtainConfig.curtainOn = false;
        BusProvider.getUiBus().unregister(this);
        BusProvider.getUiBus().post(curtainConfig);
    }

    int getCurtainY() {
        return (int) (curtainConfig.curtainHeight * screenHeight);
    }

    @Subscribe
    public void onCurtainConfigChange(@NonNull CurtainConfig config) {
        this.curtainConfig = config;
        windowContainer.setBackgroundColor(Color.parseColor(config.curtainColor));
    }

    private void removeViewFromWindow(View view) {
        try {
            if (windowManager != null) {
                windowManager.removeView(view);
            }
        } catch (Exception e) {
            Logger.error(TAG, "view not found", e);
        }

    }

    @SuppressLint("HandlerLeak")
    public class VisibilityHandler extends Handler {
        private static final int HIDE_BRIGHTNESS_WINDOW = 1;

        @Override
        public void handleMessage(Message msg) {
            if (windowManager == null) {
                return;
            }
            switch (msg.what) {
                case HIDE_BRIGHTNESS_WINDOW: {
                    if (brightnessWindow != null) {
                        removeViewFromWindow(brightnessWindow);
                        brightnessWindow = null;
                    }
                }
            }
        }
    }

}
