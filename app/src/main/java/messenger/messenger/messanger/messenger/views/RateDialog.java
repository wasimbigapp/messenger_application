package messenger.messenger.messanger.messenger.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;

import app.common.utils.PrefUtils;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.Constants;
import messenger.messenger.messanger.messenger.R;

/**
 * Rating dialog
 * Created by bedprakash.r on 20/01/18.
 */

public class RateDialog extends DialogFragment implements View.OnClickListener {

    private RatingBar ratingBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rating, container, false);
        ratingBar = view.findViewById(R.id.app_rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setProgressDrawable(stars);
        View submitButton = view.findViewById(R.id.btn_submit_rating);
        submitButton.setOnClickListener(this);
        return view;
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.transparentDialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.rating_style);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        PrefUtils.putBoolean(getContext(), Constants.APP_RATE_DONE, true);
        int i = v.getId();
        if (i == R.id.btn_submit_rating) {
            Context context = getContext();
            if (context == null) {
                return;
            }
            dismiss();
            float rating = ratingBar.getRating();
            if (Float.compare(rating, 3.0f) > 0) {
                // Good Rating. Send to play store
                AppUtils.openAppPlayStore(context, context.getPackageName());
            } else {
                // Bad Rating . Open GMail
                AppUtils.openReviewMailer(context);
            }
        }
    }
}
