package com.news.shorts.views.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.news.shorts.callbacks.EditionSelectionCallback;
import com.news.shorts.model.NewsModel;
import com.news.shorts.model.NewsPayload;
import com.news.shorts.utils.NewsConstants;
import com.news.shorts.viewmodels.NewsViewModel;
import com.news.shorts.views.NewsDetailActivity;
import com.news.shorts.views.adapters.NewsListAdapter;
import com.shorts.news.R;

import java.util.ArrayList;
import java.util.List;

import app.common.adapters.FragmentPayload;
import app.common.callbacks.ErrorCallback;
import app.common.data.NewsClickCallback;
import app.common.models.CommonConstants;
import app.common.models.ErrorCodes;
import app.common.models.ErrorInfo;
import app.common.models.PageStateData;
import app.common.models.TypeAwareModel;
import app.common.models.TypeAwareModelImpl;
import app.common.models.ViewType;
import app.common.models.local.AppListUpdate;
import app.common.utils.ApplicationContextHolder;
import app.common.utils.PrefUtils;
import app.common.utils.Utils;
import hugo.weaving.DebugLog;

/**
 * Created by bedprakash.r on 05/06/18.
 */

public class ListFragment extends Fragment implements ErrorCallback, View.OnClickListener, NewsClickCallback, EditionSelectionCallback {

    @NonNull
    private FragmentPayload payload;
    private RecyclerView recyclerView;
    private NewsListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private NewsViewModel model;
    private View rootError;
    private View btnError;
    private View progressBar;
    private NewsPayload newsPayload;


    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            model.onScrollChanged(visibleItemCount, totalItemCount, firstVisibleItemPosition);
            model.setCurrentPosition(firstVisibleItemPosition);
        }
    };


    public static Fragment create(@NonNull FragmentPayload payload) {
        Bundle args = new Bundle();
        args.putSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD, payload);
        Fragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArgs();
    }


    private void extractArgs() {
        Bundle args = getArguments();
        if (args == null || !(args.getSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD) instanceof FragmentPayload)) {
            throw new IllegalArgumentException("Must send FragmentPayload valid arguments");
        }

        payload = (FragmentPayload) args.getSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.pb_list);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = view.findViewById(R.id.btn_filter);
        fab.setOnClickListener(this);

        adapter = new NewsListAdapter();
        adapter.setClickCallback(this);
        recyclerView.setAdapter(adapter);

        model = ViewModelProviders.of(this, ApplicationContextHolder.getInstance().getFactory()).get(NewsViewModel.class);
        newsPayload = new NewsPayload(PrefUtils.getString(getActivity(), NewsConstants.KEY_LANGUAGE_CODE, CommonConstants.EN_CODE), 0);

        model.setPayload(newsPayload);

        model.setErrorCallback(this);
        ApplicationContextHolder.getInstance().setNewsModel(model);

        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        model.getModels().observe(this, this::handleDataUpdate);
        model.loadData(null);
        model.getPageState().observe(this, this::handlePageStateChange);


        rootError = view.findViewById(R.id.layout_error);
        btnError = rootError.findViewById(R.id.txt_error_title);
        rootError.setOnClickListener(this);
        return view;
    }

    @DebugLog
    private void handleDataUpdate(List<TypeAwareModel> users) {
        if (users != null) {
            List<TypeAwareModel> listWithAds = new ArrayList<>();
            int ad_pos = 4;
            int ads = 0;
            for (TypeAwareModel model : users) {
                if (listWithAds.size() == ad_pos) {
                    listWithAds.add(new TypeAwareModelImpl(ViewType.NATIVE_APP_INSTALL_AD));
                    ads += 1;
                    ad_pos += (ads + 4);
                }
                listWithAds.add(model);
            }
            adapter.updateData(new AppListUpdate<>(listWithAds));
            hideError();
        } else {
            showError(new ErrorInfo());
        }
    }

    private void handlePageStateChange(PageStateData pageStateData) {
        if (pageStateData == null) {
            return;
        }
        switch (pageStateData.state) {
            case ERROR: {
                showError(pageStateData.getError());
                hideLoader();
                break;
            }
            case LOADING: {
                showLoader();
                hideError();
                break;
            }
            case LOADED: {
                if (adapter.getItemCount() == 0) {
                    showError(new ErrorInfo(Utils.getString(R.string.error_empty_news), ErrorCodes.EMPTY_NEWS));
                    return;
                }
                hideError();
                hideLoader();
                break;
            }
        }
    }

    private void showLoader() {
        if (adapter.getItemCount() > 0) {
            adapter.setShowLoader(true);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        progressBar.setVisibility(View.GONE);
        adapter.setShowLoader(false);
    }

    @Override
    public void showError(@Nullable ErrorInfo errorInfo) {
        if (adapter.getItemCount() > 0) {
            return;
        }
        if (errorInfo == null) {
            errorInfo = new ErrorInfo();
        }
        rootError.setVisibility(View.VISIBLE);
        rootError.setTag(errorInfo.errorCode);

    }

    @DebugLog
    public void hideError() {
        rootError.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(model.getCurrentPosition());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.layout_error) {
            if (view.getTag().equals(ErrorCodes.EMPTY_NEWS)) {
                newsPayload = new NewsPayload(CommonConstants.EN_CODE, 0);
                model.setPayload(newsPayload);
            }
            model.loadData(null);

        } else if (i == R.id.btn_filter) {
            EditionSelectionFragment fragment = new EditionSelectionFragment();
            fragment.show(getChildFragmentManager(), EditionSelectionFragment.TAG);
        }
    }

    @Override
    public void onClick(TypeAwareModel model, int position) {
        if (model instanceof NewsModel) {
            NewsDetailActivity.start(getContext(), new NewsPayload(newsPayload.languageCode, position), (NewsModel) model);
        } else {
            throw new IllegalArgumentException("Unhandled model " + model.getClass());
        }
    }

    @Override
    public void onEditionSelected(@NonNull NewsPayload payload) {
        //noinspection StatementWithEmptyBody
        if (payload.languageCode.equals(this.newsPayload.languageCode)) {
            // Do nothing
        } else {
            newsPayload = payload;
            model.setPayload(payload);
        }
    }

}
