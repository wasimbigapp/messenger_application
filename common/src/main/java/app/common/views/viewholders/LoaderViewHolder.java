package app.common.views.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.common.views.BaseViewHolder;
import bpr10.git.common.R;

public class LoaderViewHolder extends BaseViewHolder {
    public LoaderViewHolder(View itemView) {
        super(itemView);
    }


    public static LoaderViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_loader, parent, false);
        return new LoaderViewHolder(view);
    }
}
