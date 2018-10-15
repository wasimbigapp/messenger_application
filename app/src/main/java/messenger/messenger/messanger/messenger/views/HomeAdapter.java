package messenger.messenger.messanger.messenger.views;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.news.shorts.views.adapters.NewsAdapter;
import com.news.shorts.views.fragments.ListFragment;

import org.jetbrains.annotations.NotNull;

import app.common.adapters.FragmentPayload;
import app.common.adapters.PageTypes;
import messenger.messenger.messanger.messenger.views.fragments.FastBrowsingFragment;
import messenger.messenger.messanger.messenger.views.fragments.FeaturedFragment;

/**
 * Adapter to home screen pager
 * Created by bedprakash.r on 06/01/18.
 */
public class HomeAdapter extends NewsAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getPagerItem(@NonNull FragmentPayload payload) {
        switch (payload.getPageType()) {
            case PageTypes.FAST_BROWSING: {
                return FastBrowsingFragment.create();
            }
            case PageTypes.FEATURED: {
                return FeaturedFragment.create();
            }
            case PageTypes.NEWS: {
                return ListFragment.create(payload);
            }
            default:
                return super.getPagerItem(payload);
        }
    }
}
