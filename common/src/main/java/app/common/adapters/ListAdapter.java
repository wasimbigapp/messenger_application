package app.common.adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;
import app.common.models.local.AppListUpdate;
import app.common.views.BaseViewHolder;
import app.common.views.viewholders.LoaderViewHolder;

/**
 * List Adapter for all lists
 * Created by bedprakash.r on 07/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class ListAdapter extends RecyclerView.Adapter {

    private List<TypeAwareModel> items;

    int LOADER_VIEW_TYPE = 1001;
    private boolean showLoader;

    public ListAdapter() {
        items = new ArrayList<>();
    }

    @CallSuper
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOADER_VIEW_TYPE) {
            return getLoaderView(parent);
        }
        return BaseViewHolder.create(parent);
    }

    @CallSuper
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoaderViewHolder) {
            return;
        }

        bindHolder(holder, position);
    }

    protected void bindHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).update(items.get(position));
        } else {
            throw new IllegalArgumentException("View holder must extent BaseViewHolder");
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderPosition(position) && isShowLoader()) {
            return LOADER_VIEW_TYPE;
        }

        TypeAwareModel item = items.get(position);
        if (item == null) {
            return ViewType.APP_CARD;
        } else {
            return item.getType();
        }
    }

    private boolean isLoaderPosition(int position) {
        return position == items.size();
    }

    @Override
    public int getItemCount() {
        return items.size() + (isShowLoader() ? 1 : 0);
    }

    @SuppressWarnings("unchecked")
    @UiThread
    public void updateData(@NonNull AppListUpdate dataUpdate) {
        items.clear();
        items.addAll(dataUpdate.appCards);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    @UiThread
    public void updateData(@NonNull List<TypeAwareModel> dataUpdate) {
        items.clear();
        items.addAll(dataUpdate);
        notifyDataSetChanged();
    }

    public boolean isShowLoader() {
        return showLoader;
    }

    public void setShowLoader(boolean showLoader) {
        this.showLoader = showLoader;
        notifyDataSetChanged();
    }

    protected LoaderViewHolder getLoaderView(ViewGroup parent) {
        return LoaderViewHolder.create(parent);
    }
}
