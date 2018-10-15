package messenger.messenger.messanger.messenger.views.viewholders;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.views.BaseViewHolder;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.PieChartView;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.AppLaunchCountModel;
import messenger.messenger.messanger.messenger.utils.AppIconRequestHandler;
import messenger.messenger.messanger.messenger.utils.AppLaunchCountHelper;

/**
 * App launch card
 * Created by bedprakash.r on 14/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class AppLaunchViewHolder extends BaseViewHolder implements View.OnClickListener {

    protected TextView appName, usage_times, usage_hrs;
    protected ImageView appIcon, app_details;
    protected Drawable defaultDrawable;
    protected LinearLayout details_layout;
    protected PieChartView chart;
    private AppLaunchCountModel data;
    protected TextView launchCount;
    List<SliceValue> pieData;


    public AppLaunchViewHolder(View itemView) {
        super(itemView);
        appName = itemView.findViewById(R.id.txt_app_name);
        usage_times = itemView.findViewById(R.id.usage_times);
        usage_hrs = itemView.findViewById(R.id.usage_hours);
        launchCount = itemView.findViewById(R.id.txt_launch_count);
        app_details = itemView.findViewById(R.id.app_details_IV);
        appIcon = itemView.findViewById(R.id.ic_app_icon);
        details_layout = itemView.findViewById(R.id.details_layout);
        chart = itemView.findViewById(R.id.chart);
        defaultDrawable = itemView.getContext().getResources().getDrawable(R.drawable.ic_default_icon);

        appIcon.setOnClickListener(this);
        appName.setOnClickListener(this);

        app_details.setOnClickListener(view -> {
            if (details_layout.getVisibility() == View.VISIBLE) {
                details_layout.setVisibility(View.GONE);
            } else {
                details_layout.setVisibility(View.VISIBLE);
            }
        });


    }

    public static AppLaunchViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_app_launch_card, parent, false);
        return new AppLaunchViewHolder(view);
    }

    public void update(TypeAwareModel model) {
        if (!(model instanceof AppLaunchCountModel)) {
            return;
        }
        this.data = (AppLaunchCountModel) model;
        appName.setText(data.getLabel());
        Picasso.with(appIcon.getContext())
                .load(AppIconRequestHandler.getAppUri(data.getPackageName()))
                .placeholder(defaultDrawable)
                .into(appIcon);

        updateLaunchCount();
    }

    @SuppressLint("DefaultLocale")
    private void updateLaunchCount() {
        pieData = new ArrayList<>();
        launchCount.setText(String.format("%dx (%d%%)", data.getLaunchCount(), getAppLaunchPercentage(data.getLaunchCount())));
        usage_times.setText(appIcon.getContext().getResources().getQuantityString(R.plurals.launch_count, data.getLaunchCount(), data.getLaunchCount()));

        int dif = 100 - getAppLaunchPercentage(data.getLaunchCount());
        pieData.add(new SliceValue(getAppLaunchPercentage(data.getLaunchCount()), Color.parseColor("#1787fb")));
        pieData.add(new SliceValue(dif, Color.parseColor("#33979797")));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1(data.getLabel()).setCenterText1FontSize(14);
        chart.setPieChartData(pieChartData);

    }

    private int getAppLaunchPercentage(int launchCount) {
        return (int) (((float) launchCount / (float) AppLaunchCountHelper.getTotalLaunchCount()) * 100);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View view) {

        // handle launch
        data.setLaunchCount(data.getLaunchCount() + 1);
        AppLaunchCountHelper.saveLaunchCount(data.getPackageName(), data.getLaunchCount());
        updateLaunchCount();
        AppUtils.launchPackage(data.getPackageName());
    }
}
