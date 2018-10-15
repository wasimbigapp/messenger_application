package messenger.messenger.messanger.messenger.views.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.news.shorts.model.NewsModel;
import com.news.shorts.views.NewsDetailActivity;

import app.common.data.NewsClickCallback;
import app.common.models.TypeAwareModel;
import app.common.models.ViewType;
import app.common.utils.ApplicationContextHolder;
import messenger.messenger.messanger.messenger.FeaturedTabViewModel;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.utils.BusProvider;
import messenger.messenger.messanger.messenger.utils.CurtainToggleCallback;
import messenger.messenger.messanger.messenger.views.ListAdapter;

/**
 * FeaturedFragment
 * Created by bedprakash.r on 06/01/18.
 */

@SuppressWarnings("unchecked")
public class FeaturedFragment extends Fragment implements CurtainToggleCallback, NewsClickCallback {

    private static final String TAG = "FeaturedFragment";
    private ListAdapter appsListAdapter;
    private FeaturedTabViewModel viewModel;

    public static FeaturedFragment create() {
        return new FeaturedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_featured_fragment, container, false);
        initViews(view);

        viewModel = ViewModelProviders.of(this, ApplicationContextHolder.getInstance().getFactory()).get(FeaturedTabViewModel.class);
        viewModel.getLiveData().observe(this, update -> {
            if (update != null) {
                appsListAdapter.updateData(update);
            }
        });

        viewModel.loadData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getUiBus().register(this);
    }

    @Override
    public void onStop() {
        BusProvider.getUiBus().unregister(this);
        super.onStop();
    }

    private void initViews(View parent) {
        // views inflation
        RecyclerView appList = parent.findViewById(R.id.list_featured);

        appsListAdapter = new ListAdapter();
        appsListAdapter.setCallback(this);
        appsListAdapter.setClickCallback(this);
        appList.setLayoutManager(getVerticalLayoutManager());
        appList.setAdapter(appsListAdapter);

        //appList.addItemDecoration(new GridItemDecoration(getDimens(R.dimen.grid_card_margin), 2, 2, 3));

    }

    private RecyclerView.LayoutManager getVerticalLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    private RecyclerView.LayoutManager getGridLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = appsListAdapter.getItemViewType(position);

                if (viewType == ViewType.APP_LAUNCH_CARD
                        || viewType == ViewType.EXTERNAL_APP) {
                    return 1;
                }

                return 3;
            }
        });

        return layoutManager;
    }

    @Override
    public void handleCurtainToggle(CompoundButton compoundButton, boolean turnedOn) {
        if (getContext() instanceof CurtainToggleCallback) {
            ((CurtainToggleCallback) getContext()).handleCurtainToggle(compoundButton, turnedOn);
        }
    }

    @Override
    public void onClick(TypeAwareModel model, int position) {
        if (model instanceof NewsModel) {
            NewsDetailActivity.start(getContext(), viewModel.getNewsPayload(), (NewsModel) model);
        } else {
            throw new IllegalArgumentException("Unhandled model " + model.getClass());
        }
    }
}
