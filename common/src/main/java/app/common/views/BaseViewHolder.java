package app.common.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.common.models.TypeAwareModel;
import bpr10.git.common.R;

/**
 * Created by bedprakash.r on 13/06/18.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public static BaseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empty, parent, false);
        return new BaseViewHolder(view);
    }

    public void update(TypeAwareModel typeAwareModel) {
    }
}
