package messenger.messenger.messanger.messenger.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.common.models.ErrorInfo;
import app.common.models.local.AppListUpdate;
import app.common.utils.RetrofitCallbackWrapper;
import app.common.utils.Utils;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.BuildConfig;
import messenger.messenger.messanger.messenger.FirebaseDBHelper;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.AppBrowserModel;
import messenger.messenger.messanger.messenger.model.BannerAdBigModel;
import messenger.messenger.messanger.messenger.views.ListAdapter;
import timber.log.Timber;

/**
 * FastBrowsingFragment
 * Created by bedprakash.r on 06/01/18.
 */

@SuppressWarnings("unchecked")
public class FastBrowsingFragment extends Fragment {

    private static final String TAG = "FastBrowsingFragment";
    private ListAdapter appsListAdapter;
    private ListAdapter bigAdsAdapter;

    public static Fragment create() {
        return new FastBrowsingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fast_browsing_fragment, container, false);

        initViews(view);
        showApps();
        showBigAds();
        showBannerAd(view);
        return view;
    }


    private void initViews(View parent) {
        // views inflation
        RecyclerView appList = parent.findViewById(R.id.recycler_apps);
        RecyclerView bigAds = parent.findViewById(R.id.recycler_big_ads);

        appsListAdapter = new ListAdapter();
        appList.setLayoutManager(getHorizontalLayoutManager());
        appList.setAdapter(appsListAdapter);

        bigAdsAdapter = new ListAdapter();
        bigAds.setLayoutManager(getHorizontalLayoutManager());
        bigAds.setAdapter(bigAdsAdapter);


        // setting up headers

        View socialHeader = parent.findViewById(R.id.header_social_media);
        TextView socialHeaderTitle = socialHeader.findViewById(R.id.txt_header);
        socialHeaderTitle.setText(R.string.header_social_apps);

        View shoppingHeader = parent.findViewById(R.id.header_shopping);
        TextView shoppingHeaderTitle = shoppingHeader.findViewById(R.id.txt_header);
        shoppingHeaderTitle.setText(R.string.header_shopping);

    }

    private RecyclerView.LayoutManager getHorizontalLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    private void showApps() {
        FirebaseDBHelper.getFireBaseDb().fetchApps(new RetrofitCallbackWrapper<ArrayList<AppBrowserModel>>() {
            @Override
            public void onSuccess(@NonNull ArrayList<AppBrowserModel> appBrowserModels) {
                if (AppUtils.isEmpty(appBrowserModels)) {
                    return;
                }
                Collections.sort(appBrowserModels, (model, anotherModel) -> anotherModel.viewOrder - model.viewOrder);
                appsListAdapter.updateData(new AppListUpdate(appBrowserModels));
            }

            @Override
            public void onError(@NonNull ErrorInfo errorInfo) {
                Timber.e(errorInfo.errorMessage, "%s Error getting documents.", TAG);
            }
        });
    }

    private void showBigAds() {
        FirebaseDBHelper.getFireBaseDb().fetchBigAds(new RetrofitCallbackWrapper<ArrayList<BannerAdBigModel>>() {
            @Override
            public void onSuccess(@NonNull ArrayList<BannerAdBigModel> bigAds) {
                if (AppUtils.isEmpty(bigAds)) {
                    return;
                }
                updateBigAds(bigAds);

            }

            @Override
            public void onError(@NonNull ErrorInfo errorInfo) {
                Timber.e(errorInfo.errorMessage, "%s showBigAds Error getting documents.", TAG);
                return;
            }
        });
    }

    private void updateBigAds(@NonNull List<BannerAdBigModel> bigAds) {
        bigAdsAdapter.updateData(new AppListUpdate(bigAds));
    }

    private void showBannerAd(View view) {
        AdView mAdView = view.findViewById(R.id.ad_view_fast_browsing);
        AdRequest.Builder adRequest = new AdRequest.Builder();

        if (!Utils.userConsentGiven()) {
            Utils.putNonPersonalizedData(adRequest, null);
        }

        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(app.common.utils.Utils.getString(R.string.TEST_DEVICE));
        }

        if (Utils.canShowAds()) {
            mAdView.loadAd(adRequest.build());
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

}
