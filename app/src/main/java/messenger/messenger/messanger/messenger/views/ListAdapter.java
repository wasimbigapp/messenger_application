package messenger.messenger.messanger.messenger.views;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.news.shorts.views.adapters.NewsListAdapter;

import app.common.models.ViewType;
import app.common.models.local.AppListUpdate;
import messenger.messenger.messanger.messenger.utils.CurtainToggleCallback;
import messenger.messenger.messanger.messenger.views.viewholders.AppBrowsingViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.AppCardViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.AppLaunchViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.AppsContainer;
import messenger.messenger.messanger.messenger.views.viewholders.BannerAdBigViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.BannerAdSmallViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.BigAdsContainer;
import messenger.messenger.messanger.messenger.views.viewholders.ChatCurtainToggleViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.HeaderViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.NativeContentAdViewHolder;
import messenger.messenger.messanger.messenger.views.viewholders.SmallAdsContainer;

/**
 * List Adapter for all lists
 * Created by bedprakash.r on 07/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class ListAdapter extends NewsListAdapter {

    private AppListUpdate smallAds;
    private AppListUpdate bigAds;
    private CurtainToggleCallback callback;
    private AppListUpdate appCards;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {

        switch (type) {
            case ViewType.APP_LAUNCH_CARD: {
                return AppLaunchViewHolder.create(parent);
            }
            case ViewType.EXTERNAL_APP: {
                return AppBrowsingViewHolder.create(parent);
            }
            case ViewType.BANNER_AD_BIG: {
                return BannerAdBigViewHolder.create(parent);
            }
            case ViewType.BANNER_AD_SMALL: {
                return BannerAdSmallViewHolder.create(parent);
            }
            case ViewType.SMALL_ADS_CONTAINER: {
                return SmallAdsContainer.create(parent);
            }
            case ViewType.BIG_ADS_CONTAINER: {
                return BigAdsContainer.create(parent);
            }
            case ViewType.APPS_CONTAINER: {
                return AppsContainer.create(parent);
            }
            case ViewType.ANNOUNCEMENT_HEADER: {
                return HeaderViewHolder.create(parent);
            }
            case ViewType.BUTTON: {
                return ChatCurtainToggleViewHolder.create(parent, callback);
            }
            case ViewType.APP_CARD: {
                return AppCardViewHolder.create(parent);
            }
            case ViewType.NATIVE_CONTENT_AD: {
                return NativeContentAdViewHolder.create(parent);
            }
            default:
                return super.onCreateViewHolder(parent, type);
        }
    }

    @Override
    public void bindHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatCurtainToggleViewHolder) {
            ((ChatCurtainToggleViewHolder) holder).update();
        } else {
            super.bindHolder(holder, position);
        }
    }

    public void saveSmallAds(AppListUpdate update) {
        this.smallAds = update;
        notifyDataSetChanged();
    }

    public void saveBigAds(AppListUpdate update) {
        this.bigAds = update;
        notifyDataSetChanged();
    }

    public void saveApps(AppListUpdate update) {
        this.appCards = update;
        notifyDataSetChanged();
    }

    public void setCallback(CurtainToggleCallback callback) {
        this.callback = callback;
    }
}
