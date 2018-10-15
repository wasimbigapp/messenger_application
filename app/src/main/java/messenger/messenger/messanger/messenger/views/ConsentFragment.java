package messenger.messenger.messanger.messenger.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.common.models.CommonConstants;
import app.common.utils.ApplicationContextHolder;
import app.common.utils.PrefUtils;
import messenger.messenger.messanger.messenger.R;

import static app.common.utils.Utils.launchUrl;

/**
 * Created by bedprakash.r on 27/05/18.
 */

public class ConsentFragment extends DialogFragment {
    private View view;

    public static void start(FragmentManager manager) {
        new ConsentFragment().show(manager, "consent_fragemnt");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_consent_dialog, container, false);

        view.findViewById(R.id.txt_consent_learn_how).setOnClickListener(v -> launchUrl(getContext(), getString(R.string.privacy_policy_url)));

        view.findViewById(R.id.txt_consent_btn_pos).setOnClickListener(v -> {
            PrefUtils.putBoolean(getContext(), CommonConstants.IS_CONSENT_GIVEN, true);
            ApplicationContextHolder.getInstance().controlTrackingServices(getContext(), true);
            dismiss();
        });

        view.findViewById(R.id.txt_consent_btn_neg).setOnClickListener(v -> {
            PrefUtils.putBoolean(getContext(), CommonConstants.IS_CONSENT_GIVEN, false);
            ApplicationContextHolder.getInstance().controlTrackingServices(getContext(), false);
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }
}
