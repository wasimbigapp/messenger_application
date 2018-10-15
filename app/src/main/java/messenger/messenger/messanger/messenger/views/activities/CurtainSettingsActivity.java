package messenger.messenger.messanger.messenger.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import app.common.views.activities.BaseActivity;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.CurtainConfig;
import messenger.messenger.messanger.messenger.services.CurtainService;

/**
 * Created by bedprakash.r on 04/04/18.
 */

public class CurtainSettingsActivity extends BaseActivity implements View.OnClickListener, ColorPickerDialogListener {

    private CurtainConfig curtainConfig;

    public static void launch(Context context) {
        Intent i = new Intent(context, CurtainSettingsActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_settings);
        findViewById(R.id.settings_color_curtain).setOnClickListener(this);
//        findViewById(R.id.settings_color_slider).setOnClickListener(this);
        curtainConfig = AppUtils.getCurtainConfig();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CurtainService.stop(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.settings_color_curtain) {
            ColorPickerDialog.newBuilder()
                    .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                    .setAllowPresets(true)
                    .setDialogId(R.id.settings_color_curtain)
                    .setColor(Color.parseColor(curtainConfig.curtainColor))
                    .show(this);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onColorSelected(int dialogId, int color) {
        if (dialogId == R.id.settings_color_curtain) {
            curtainConfig.curtainColor = String.format("#%06X", 0xFFFFFF & color);
        }
        AppUtils.saveCurtainConfig(curtainConfig);
    }

    @Override
    public void onColorChanged(int dialogId, int color) {
        curtainConfig.curtainColor = String.format("#%06X", 0xFFFFFF & color);
        AppUtils.saveCurtainConfig(curtainConfig);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
