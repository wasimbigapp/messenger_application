package messenger.messenger.messanger.messenger.views.viewholders;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.common.models.TypeAwareModel;
import app.common.views.BaseViewHolder;
import bpr10.git.crhometabs.chrometabs.ChromeUtils;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.AppBrowserModel;
import messenger.messenger.messanger.messenger.model.ExternalBrowserOpened;
import messenger.messenger.messanger.messenger.utils.BusProvider;

import static app.common.utils.Utils.isEmpty;

/**
 * App cards holder
 * Created by bedprakash.r on 07/01/18.
 */

public class AppCardViewHolder extends BaseViewHolder implements View.OnClickListener {

    private final Drawable defaultDrawable;
    private TextView appName;
    private ImageView appIcon;

    private AppBrowserModel appBrowserModel;

    public AppCardViewHolder(View itemView) {
        super(itemView);
        appName = itemView.findViewById(R.id.txt_app_name);
        appIcon = itemView.findViewById(R.id.ic_app_icon);
        defaultDrawable = itemView.getContext().getResources().getDrawable(R.drawable.ic_default_icon);
        itemView.setOnClickListener(this);
    }

    public static AppCardViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_app_item, parent, false);
        return new AppCardViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    public void update(TypeAwareModel model) {
        if (!(model instanceof AppBrowserModel)) {
            return;
        }
        this.appBrowserModel = (AppBrowserModel) model;
        appName.setText(String.format("%s", appBrowserModel.appName));
        if (!isEmpty(appBrowserModel.iconUrl)) {
            Picasso.with(appIcon.getContext())
                    .load(appBrowserModel.iconUrl)
                    .placeholder(defaultDrawable)
                    .into(appIcon);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View view) {
        if (appBrowserModel == null || appBrowserModel.landingUrl == null) {
            return;
        }
        BusProvider.getUiBus().post(new ExternalBrowserOpened());
        ChromeUtils.openInExternalBrowser(appName.getContext(), appBrowserModel.landingUrl);
    }
}
