package messenger.messenger.messanger.messenger.views.viewholders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.utils.Utils;
import app.common.views.BaseViewHolder;
import messenger.messenger.messanger.messenger.R;


/**
 * Created by bhaskar on 31/06/18.
 */

public class NativeContentAdViewHolder extends BaseViewHolder {

    private Context context;
    private FrameLayout frame;
    private AdLoader adLoader;

    public NativeContentAdViewHolder(View view) {
        super(view);
        context = view.getContext();
        frame = view.findViewById(R.id.fl_adplaceholder);
    }

    public static NativeContentAdViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_native_content_ad_frame, parent, false);
        return new NativeContentAdViewHolder(view);
    }

    public void update(@Nullable TypeAwareModel model) {
        loadAd();
    }


    private void loadAd() {
        if (adLoader != null) {
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(context, Utils.getNativeAdId());
        builder.forContentAd(ad -> {
                NativeContentAdView adView = (NativeContentAdView) LayoutInflater.from(context)
                        .inflate(R.layout.layout_native_content_ad, null);
                populateContentAdView(ad, adView);
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


    private void populateContentAdView(NativeContentAd nativeAd,
                                          NativeContentAdView adView) {


        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.

                super.onVideoEnd();
            }
        });

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        ImageView mainImageView = adView.findViewById(R.id.ad_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            // At least one image is guaranteed.
            List<NativeAd.Image> images = nativeAd.getImages();
            if (!Utils.isEmpty(images)) {
                mainImageView.setImageDrawable(images.get(0).getDrawable());
            }

        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ll_main));
        adView.setLogoView(adView.findViewById(R.id.ad_app_icon));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getLogo() == null) {
            adView.getLogoView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(
                    nativeAd.getLogo().getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }

        adView.setNativeAd(nativeAd);
    }

}
