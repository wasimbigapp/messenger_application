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
import messenger.messenger.messanger.messenger.model.BannerAdSmallModel;
import messenger.messenger.messanger.messenger.model.SmallAdsData;
import messenger.messenger.messanger.messenger.views.ListAdapter;

/**
 * Small ads container view holder
 * Created by bedprakash.r on 15/01/18.
 */
@SuppressWarnings("unchecked,WeakerAccess")
public class SmallAdsContainer extends BaseViewHolder {

    private static ListAdapter smallAdsAdapter;

    public List<BannerAdSmallModel> smallAds;
    private RecyclerView adsList;
    private View rootView;

    public SmallAdsContainer(View itemView) {
        super(itemView);
        this.rootView = itemView;
        adsList = itemView.findViewById(R.id.recycler_small_ads);
        smallAdsAdapter = new ListAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        adsList.setLayoutManager(layoutManager);
        adsList.setAdapter(smallAdsAdapter);

        TextView txtTitle = itemView.findViewById(R.id.txt_header);
        txtTitle.setText(R.string.explore);

    }

    public static SmallAdsContainer create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_ad_container, parent, false);
        return new SmallAdsContainer(view);
    }

    public void update(@Nullable TypeAwareModel smallAds) {
        if (!(smallAds instanceof SmallAdsData) || AppUtils.isEmpty(((SmallAdsData) smallAds).models)) {
            rootView.setVisibility(View.GONE);
            return;
        }
        rootView.setVisibility(View.VISIBLE);

        this.smallAds = ((SmallAdsData) smallAds).models;
        smallAdsAdapter.updateData(new AppListUpdate(this.smallAds));
        if (((SmallAdsData) smallAds).position < ((SmallAdsData) smallAds).models.size()) {
            adsList.scrollToPosition(((SmallAdsData) smallAds).position);
        }
    }
}
