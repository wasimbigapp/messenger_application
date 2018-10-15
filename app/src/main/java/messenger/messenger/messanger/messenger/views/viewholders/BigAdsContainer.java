package messenger.messenger.messanger.messenger.views.viewholders;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.local.AppListUpdate;
import app.common.views.BaseViewHolder;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.BannerAdBigModel;
import messenger.messenger.messanger.messenger.model.BigAdsData;
import messenger.messenger.messanger.messenger.views.ListAdapter;

/**
 * Small ads container view holder
 * Created by bedprakash.r on 15/01/18.
 */
@SuppressWarnings("unchecked,WeakerAccess")
public class BigAdsContainer extends BaseViewHolder {

    private static ListAdapter bigAdsAdapter;
    private final RecyclerView adsList;
    private final View rootView;

    public List<BannerAdBigModel> bigAds;

    public BigAdsContainer(View itemView) {
        super(itemView);
        this.rootView = itemView;
        adsList = itemView.findViewById(R.id.recycler_big_ads);
        bigAdsAdapter = new ListAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        adsList.setLayoutManager(layoutManager);
        adsList.setAdapter(bigAdsAdapter);

        TextView txtTitle = itemView.findViewById(R.id.txt_header);
        txtTitle.setText(R.string.explore);
    }

    public static BigAdsContainer create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_ad_container, parent, false);
        return new BigAdsContainer(view);
    }

    public void update(@Nullable TypeAwareModel bigAds) {
        if (!(bigAds instanceof BigAdsData) || AppUtils.isEmpty(((BigAdsData) bigAds).models)) {
            rootView.setVisibility(View.GONE);
            return;
        }
        rootView.setVisibility(View.VISIBLE);
        this.bigAds = ((BigAdsData) bigAds).models;
        bigAdsAdapter.updateData(new AppListUpdate(this.bigAds));
        if (((BigAdsData) bigAds).position < ((BigAdsData) bigAds).models.size()) {
            adsList.scrollToPosition(((BigAdsData) bigAds).position);
        }
    }
}
