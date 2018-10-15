package messenger.messenger.messanger.messenger.views.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.common.models.TypeAwareModel;
import app.common.views.BaseViewHolder;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.HeaderData;

/**
 * Created by bedprakash.r on 15/01/18.
 */

public class HeaderViewHolder extends BaseViewHolder {

    private TextView txtHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        txtHeader = itemView.findViewById(R.id.txt_header);
    }

    public static HeaderViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void update(TypeAwareModel typeAwareModel) {
        if (!(typeAwareModel instanceof HeaderData)) {
            return;
        }
        HeaderData headerData = (HeaderData) typeAwareModel;

        txtHeader.setText(headerData.headerTitle);
    }
}
