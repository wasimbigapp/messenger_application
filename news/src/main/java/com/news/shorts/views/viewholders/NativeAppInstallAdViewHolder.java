package com.news.shorts.views.viewholders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.shorts.news.R;

import app.common.models.TypeAwareModel;
import app.common.utils.Utils;
import app.common.views.BaseViewHolder;


/**
 * Created by bhaskar on 29/06/18.
 */

public class NativeAppInstallAdViewHolder extends BaseViewHolder {

    private Context context;
    private FrameLayout frame;
    private AdLoader adLoader;

    public NativeAppInstallAdViewHolder(View view) {
        super(view);
        context = view.getContext();
        frame = view.findViewById(R.id.fl_adplaceholder);
    }

    public static NativeAppInstallAdViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ad_card, parent, false);
        return new NativeAppInstallAdViewHolder(view);
    }

    public void update(@Nullable TypeAwareModel model) {
        loadAd();
    }


    private void loadAd() {
        if (adLoader != null) {
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(context, Utils.getNativeAdId());
        builder.forAppInstallAd(ad -> {
            // some code that displays the app install ad.
            NativeAppInstallAdView adView = (NativeAppInstallAdView) LayoutInflater.from(context)
                    .inflate(R.layout.ad_app_install, null);
            populateAppInstallAdView(ad, adView);
            frame.removeAllViews();
            frame.addView(adView);

        });
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Handle the failure by logging, altering the UI, and so on.
            }
        });
        adLoader = builder.build();

        adLoader.loadAd(Utils.getAdRequestData());
    }


    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {


        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        //Hard coding text as INSTALL because callToAction text containing _ symbols.
//        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                .getDrawable());

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

}
