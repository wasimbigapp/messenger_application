package messenger.messenger.messanger.messenger.views.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.common.models.TypeAwareModel;
import app.common.views.BaseViewHolder;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.BannerAdModel;

import static app.common.utils.Utils.launchUrl;
import static app.common.utils.Utils.showErrorToast;

/**
 * Ad view holder
 * Created by bedprakash.r on 07/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class BannerAdViewHolder extends BaseViewHolder {

    private final ImageView imageView;
    private BannerAdModel ad;

    public BannerAdViewHolder(final View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_ad);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        itemView.setOnClickListener(view -> {
            // launch item here.
            if (ad == null || ad.landingUrl == null || ad.landingUrl.trim().length() == 0) {
                showErrorToast();
                return;
            }
            launchUrl(itemView.getContext(), ad.landingUrl);
        });
    }

    public void update(TypeAwareModel model) {
        if (!(model instanceof BannerAdModel)) {
            return;
        }
        this.ad = (BannerAdModel) model;
        if (ad.imageUrl == null || ad.imageUrl.trim().length() == 0) {
            imageView.setImageDrawable(null);
            return;
        }
        Picasso.with(imageView.getContext()).load(ad.imageUrl).tag(this).into(imageView);
    }

}
