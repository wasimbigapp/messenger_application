package messenger.messenger.messanger.messenger.views.viewholders;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.R;

/**
 * Created by bedprakash.r on 07/01/18.
 */

public class BannerAdSmallViewHolder extends BannerAdViewHolder {

    public BannerAdSmallViewHolder(View itemView) {
        super(itemView);
        CardView cardView = (CardView) itemView;
        cardView.setPreventCornerOverlap(false);
    }

    public static BannerAdSmallViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_banner_small, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (AppUtils.getScreenWidth(parent.getContext()) * .8);
        params.height = (int) (params.width * .45);
        view.setLayoutParams(params);
        return new BannerAdSmallViewHolder(view);
    }
}
