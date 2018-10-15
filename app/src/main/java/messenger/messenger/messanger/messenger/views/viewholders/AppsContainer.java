package messenger.messenger.messanger.messenger.views.viewholders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.local.AppListUpdate;
import app.common.views.BaseViewHolder;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.AppsData;
import messenger.messenger.messanger.messenger.views.ListAdapter;

public class AppsContainer extends BaseViewHolder {

    private final ListAdapter appsListAdapter;
    private List<TypeAwareModel> appCards;

    public AppsContainer(View itemView) {
        super(itemView);
        // views inflation
        RecyclerView appList = itemView.findViewById(R.id.recycler_apps);

        appsListAdapter = new ListAdapter();
        appList.setLayoutManager(getHorizontalLayoutManager(itemView.getContext()));
        appList.setAdapter(appsListAdapter);


        // setting up headers
        View socialHeader = itemView.findViewById(R.id.header_social_media);
        TextView socialHeaderTitle = socialHeader.findViewById(R.id.txt_header);
        socialHeaderTitle.setText(R.string.header_social_apps);

    }

    public static AppsContainer create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_app_cards, parent, false);
        return new AppsContainer(view);
    }

    @Override
    public void update(TypeAwareModel typeAwareModel) {
        super.update(typeAwareModel);
        if (!(typeAwareModel instanceof AppsData) || AppUtils.isEmpty(((AppsData) typeAwareModel).models)) {
            return;
        }
        this.appCards = new ArrayList<>(((AppsData) typeAwareModel).models);
        appsListAdapter.updateData(this.appCards);
    }

    private RecyclerView.LayoutManager getHorizontalLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    public void update(AppListUpdate appCards) {
        if (appCards == null || AppUtils.isEmpty(appCards.appCards)) {
            return;
        }
//        this.appCards = appCards;
//        appsListAdapter.updateData(this.appCards);
    }
}
