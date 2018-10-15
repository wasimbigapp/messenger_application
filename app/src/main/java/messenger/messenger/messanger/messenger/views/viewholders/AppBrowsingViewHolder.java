package messenger.messenger.messanger.messenger.views.viewholders;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.common.models.TypeAwareModel;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.ExternalAppModel;
import messenger.messenger.messanger.messenger.utils.AppLaunchCountHelper;

import static app.common.utils.Utils.isEmpty;
import static app.common.utils.Utils.launchUrl;

/**
 * Created by bedprakash.r on 17/03/18.
 */

public class AppBrowsingViewHolder extends AppLaunchViewHolder {

    private ExternalAppModel appData;

    private AppBrowsingViewHolder(View itemView) {
        super(itemView);
    }

    public static AppBrowsingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_app_launch_card, parent, false);
        return new AppBrowsingViewHolder(view);
    }

    @Override
    public void update(TypeAwareModel model) {
        if (!(model instanceof ExternalAppModel)) {
            return;
        }
        this.appData = (ExternalAppModel) model;
        appName.setText(appData.appName);
        if (!isEmpty(appData.iconUrl)) {
            Picasso.with(appIcon.getContext())
                    .load(appData.iconUrl)
                    .placeholder(defaultDrawable)
                    .into(appIcon);
        }
        updateLaunchCount();

    }

    @SuppressLint("DefaultLocale")
    private void updateLaunchCount(){
        pieData = new ArrayList<>();
        launchCount.setText(String.format("%dx (%d%%)", appData.getLaunchCount(), getAppLaunchPercentage(appData.getLaunchCount())));
        usage_times.setText(appIcon.getContext().getResources().getQuantityString(R.plurals.launch_count, appData.getLaunchCount(), appData.getLaunchCount()));

        int dif = 100 - getAppLaunchPercentage(appData.getLaunchCount());
        pieData.add(new SliceValue(getAppLaunchPercentage(appData.getLaunchCount()), Color.parseColor("#1787fb")));
        pieData.add(new SliceValue(dif, Color.parseColor("#33979797")));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1(appData.appName).setCenterText1FontSize(14);
        chart.setPieChartData(pieChartData);
    }

    private int getAppLaunchPercentage(int launchCount) {
        return (int) (((float) launchCount / (float) AppLaunchCountHelper.getTotalLaunchCount()) * 100);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View view) {
        if (appData == null || appData.deeplink == null) {
            return;
        }
        appData.setLaunchCount(appData.getLaunchCount() + 1);
        updateLaunchCount();
        // handle launch
        launchUrl(view.getContext(), appData.deeplink);
    }
}
