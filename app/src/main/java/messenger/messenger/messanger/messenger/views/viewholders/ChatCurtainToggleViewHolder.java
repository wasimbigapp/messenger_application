package messenger.messenger.messanger.messenger.views.viewholders;

import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.common.views.BaseViewHolder;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.services.CurtainService;
import messenger.messenger.messanger.messenger.utils.CurtainToggleCallback;

/**
 * Created by bedprakash.r on 12/04/18.
 */

public class ChatCurtainToggleViewHolder extends BaseViewHolder {

    SwitchCompat curtainSwitch;

    public ChatCurtainToggleViewHolder(View itemView, CurtainToggleCallback callback) {
        super(itemView);

        curtainSwitch = itemView.findViewById(R.id.switch_curtain);
        if (callback != null) {
            curtainSwitch.setOnCheckedChangeListener(callback::handleCurtainToggle);
        }
    }

    public static ChatCurtainToggleViewHolder create(ViewGroup parent, CurtainToggleCallback callback) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_curtain_toggle, parent, false);
        return new ChatCurtainToggleViewHolder(view, callback);
    }

    public void update() {
        curtainSwitch.setChecked(CurtainService.isIsRunning());
    }
}
