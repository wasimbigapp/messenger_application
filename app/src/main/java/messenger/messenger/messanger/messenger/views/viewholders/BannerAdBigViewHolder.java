package messenger.messenger.messanger.messenger.views.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.R;

/**
 * Created by bedprakash.r on 11/01/18.
 */

public class BannerAdBigViewHolder extends BannerAdViewHolder {

    public BannerAdBigViewHolder(View itemView) {
        super(itemView);
    }

    public static BannerAdBigViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_banner_big, parent, false);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (AppUtils.getScreenWidth(parent.getContext()) * .8);
        params.height = (int) (params.width * .83);
        view.setLayoutParams(params);

        return new BannerAdBigViewHolder(view);
    }
}
