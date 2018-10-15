package messenger.messenger.messanger.messenger;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.news.shorts.data.NewsRepository;
import com.news.shorts.data.RepositoryStore;
import com.news.shorts.model.NewsPayload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import app.common.BaseViewModel;
import app.common.models.TypeAwareModel;
import app.common.models.TypeAwareModelImpl;
import app.common.models.ViewType;
import app.common.utils.JsonUtils;
import app.common.utils.Logger;
import app.common.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import messenger.messenger.messanger.messenger.api.RetrofitClient;
import messenger.messenger.messanger.messenger.model.AppBrowserModel;
import messenger.messenger.messanger.messenger.model.AppLaunchCountModel;
import messenger.messenger.messanger.messenger.model.AppsData;
import messenger.messenger.messanger.messenger.model.BannerAdBigModel;
import messenger.messenger.messanger.messenger.model.BannerAdSmallModel;
import messenger.messenger.messanger.messenger.model.BigAdsData;
import messenger.messenger.messanger.messenger.model.CardScreenType;
import messenger.messenger.messanger.messenger.model.ExternalAppModel;
import messenger.messenger.messanger.messenger.model.HeaderData;
import messenger.messenger.messanger.messenger.model.SmallAdsData;
import messenger.messenger.messanger.messenger.utils.AppLaunchCountHelper;
import timber.log.Timber;

import static app.common.utils.Utils.readJsonFile;
import static messenger.messenger.messanger.messenger.api.RetrofitClient.BIG;
import static messenger.messenger.messanger.messenger.api.RetrofitClient.SMALL;

public class FeaturedTabViewModel extends BaseViewModel {

    private static final Comparator<AppLaunchCountModel> LAUNCH_COUNT = (object1, object2) -> object2.getLaunchCount() - object1.getLaunchCount();
    private static final int NEWS_CARD_IN_FEATURED = 10;
    private List<TypeAwareModel> appLaunchCards;
    private List<BannerAdBigModel> bigBanners;
    private List<BannerAdSmallModel> smallBanners;
    private List<AppBrowserModel> socialApps;
    private List<TypeAwareModel> newsCards;
    MutableLiveData<List<TypeAwareModel>> liveData;

    public FeaturedTabViewModel(@NonNull Application application) {
        super(application);
        liveData = new MutableLiveData<>();
    }

    public void loadData() {
        loadAppLaunchCards();
        loadBigAds();
        loadSmallAds();
        loadSocialApps();
        loadNews();
    }

    private void loadNews() {
        NewsRepository repository = (NewsRepository) RepositoryStore.getInstance().getRepository(getNewsPayload());
        repository.getModels().observeForever(data -> {
            if (Utils.isEmpty(data)) {
                return;
            }
            newsCards = data;
            updateData();
        });
        repository.fetchData(0);
    }

    private void updateData() {
        List<TypeAwareModel> finalModels = new ArrayList<>();
        finalModels.add(0, new TypeAwareModelImpl(ViewType.BUTTON));
        finalModels.add(1, new TypeAwareModelImpl(ViewType.ANNOUNCEMENT_HEADER));

        if (appLaunchCards != null) {
            finalModels.addAll(appLaunchCards);
        }

        if (newsCards != null) {
            HeaderData newsHeader = new HeaderData();
            newsHeader.headerTitle = Utils.getString(R.string.tab_news);
            finalModels.add(newsHeader);

            int endIndex = newsCards.size() > NEWS_CARD_IN_FEATURED - 1 ? NEWS_CARD_IN_FEATURED - 1 : newsCards.size() - 1;
            finalModels.addAll(newsCards.subList(0, endIndex));
        }

        if (socialApps != null) {
            finalModels.add(new AppsData(socialApps));
        }

        finalModels.add(new TypeAwareModelImpl(ViewType.NATIVE_CONTENT_AD));

        if (bigBanners != null) {
            finalModels.add(new BigAdsData(bigBanners, AppUtils.getBigBannerPosition(bigBanners)));
        }
        if (smallBanners != null) {
            finalModels.add(new SmallAdsData(smallBanners, AppUtils.getSmallBannerPosition(smallBanners)));
        }
        liveData.setValue(finalModels);
    }

    private void loadSocialApps() {
        Disposable d = RetrofitClient.getMessengerClient().getAppCardsRx(CardScreenType.APP_LIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    socialApps = data;
                    updateData();
                }, Logger::printStackTrace);
        addDisposable(d);
    }

    private void loadSmallAds() {
        Disposable d = RetrofitClient.getMessengerClient().getSmallAdsRx(SMALL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    smallBanners = data;
                    updateData();
                }, Logger::printStackTrace);
        addDisposable(d);

    }

    private void loadBigAds() {
        Disposable d = RetrofitClient.getMessengerClient().getBigAdsRx(BIG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    bigBanners = data;
                    updateData();
                }, Logger::printStackTrace);
        addDisposable(d);
    }

    private void loadAppLaunchCards() {
        Disposable d = Observable.fromCallable(this::loadAppCards)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    appLaunchCards = data;
                    updateData();
                }, Logger::printStackTrace);
        addDisposable(d);
    }

    private List<TypeAwareModel> loadAppCards() {
        HashMap trackingPackages = new HashMap<String, Integer>();

        try {
            // load map from assets
            trackingPackages = JsonUtils.fromJsonToObj(readJsonFile(Constants.APPS_LIST_JSON),
                    trackingPackages.getClass());
        } catch (IOException ignored) {

        }

        if (trackingPackages == null) {
            trackingPackages = new HashMap<>();
        }

        PackageManager mPm = getApplication().getPackageManager();

        // Retrieve all installed applications.
        List<ApplicationInfo> apps = mPm.getInstalledApplications(0);

        if (apps == null) {
            apps = new ArrayList<>();
        }


        AppLaunchCountHelper.resetTotalLaunchCount();

        // Create corresponding array of entries and load their labels.
        List<AppLaunchCountModel> entries = new ArrayList<>(apps.size());
        for (int i = 0; i < apps.size(); i++) {
            ApplicationInfo packageInfo = apps.get(i);

            if (packageInfo == null) {
                continue;
            }

            if (!trackingPackages.containsKey(packageInfo.packageName)) {
                continue;
            }

            AppLaunchCountModel entry = new AppLaunchCountModel();
            int appLaunchCount = AppLaunchCountHelper.getLaunchCount(packageInfo.packageName);
            entry.setLaunchCount(appLaunchCount);
            AppLaunchCountHelper.increaseTotalLaunchCount(appLaunchCount);
            CharSequence label = packageInfo.loadLabel(mPm);
            entry.setmLabel(label != null ? label.toString() : packageInfo.packageName);
            entry.setPackageName(packageInfo.packageName);
            entries.add(entry);
        }

        // Sort the list.
        Collections.sort(entries, LAUNCH_COUNT);

        ArrayList<ExternalAppModel> appCards = null;
        try {
            appCards = RetrofitClient.getMessengerClient().getAppCardForLandingScreen(CardScreenType.MAIN_SCREEN).execute().body();
        } catch (IOException e) {
            Timber.e(e);
        }


        List<TypeAwareModel> list = new ArrayList<>(entries);
        if (!AppUtils.isEmpty(appCards)) {
            Collections.sort(appCards, (model, anotherModel) -> anotherModel.viewOrder - model.viewOrder);
            list.addAll(appCards);
            return list;
        }

        return list;
    }


    public MutableLiveData<List<TypeAwareModel>> getLiveData() {
        return liveData;
    }

    public NewsPayload getNewsPayload() {
        return new NewsPayload("en", 0);
    }
}
