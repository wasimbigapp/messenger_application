package com.news.shorts.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.news.shorts.callbacks.EditionSelectionCallback;
import com.news.shorts.callbacks.LanguageItemSelectionCallback;
import com.news.shorts.data.EditionRepository;
import com.news.shorts.data.RepositoryStore;
import com.news.shorts.model.Country;
import com.news.shorts.model.Language;
import com.news.shorts.model.LanguageData;
import com.news.shorts.model.NewsPayload;
import com.news.shorts.utils.NewsConstants;
import com.news.shorts.views.adapters.LanguageListAdapter;
import com.shorts.news.R;

import java.util.ArrayList;
import java.util.List;

import app.common.models.local.AppListUpdate;
import app.common.utils.PrefUtils;

public class EditionSelectionFragment extends BaseDialogFragment implements AdapterView.OnItemSelectedListener, LanguageItemSelectionCallback {

    public static final String TAG = "EditionSelectionFragment";

    private LanguageData languageData;
    private LanguageListAdapter languageListAdapter;
    private CountrySpinnerAdapter countrySpinnerAdapter;

    private Spinner spin;
    private List<Country> countryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_edition_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.listview);
        spin = view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        countrySpinnerAdapter = new CountrySpinnerAdapter(getActivity(), R.id.txt_title, new ArrayList<>());
        countrySpinnerAdapter.setDropDownViewResource(R.layout.layout_language_card);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(countrySpinnerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        languageListAdapter = new LanguageListAdapter();
        languageListAdapter.setClickCallback(this);
        recyclerView.setAdapter(languageListAdapter);

        EditionRepository repository = (EditionRepository) RepositoryStore.getInstance().getRepository(EditionRepository.KEY);
        repository.getLanguageData().observe(this, this::updateLanguageData);
        repository.fetchData(0);

        return view;
    }

    private void updateLanguageData(@NonNull LanguageData newData) {
        this.languageData = newData;
        String countryName = PrefUtils.getString(getActivity(), NewsConstants.KEY_COUNTRY_NAME);
        String languageCode = PrefUtils.getString(getActivity(), NewsConstants.KEY_LANGUAGE_CODE);

        //pre-set the selected data
        int selectedCountryPos = 0;
        for (int i = 0; i < languageData.countries.size(); i++) {
            Country country = languageData.countries.get(i);
            if (country.displayName.equals(countryName)) {
                country.isSelected = true;
                selectedCountryPos = i;
                for (Language lan : country.languages) {
                    lan.isSelected = lan.languageCode.equals(languageCode);
                }
            } else {
                country.isSelected = false;
            }
        }

        countryList = newData.countries;
        countrySpinnerAdapter.clear();
        countrySpinnerAdapter.addAll(countryList);
        countrySpinnerAdapter.notifyDataSetChanged();
        spin.setSelection(selectedCountryPos);
    }

    private EditionSelectionCallback getCallback() {
        if (getParentFragment() instanceof EditionSelectionCallback) {
            return (EditionSelectionCallback) getParentFragment();
        } else if (getActivity() instanceof EditionSelectionCallback) {
            return (EditionSelectionCallback) getActivity();
        } else {
            throw new IllegalArgumentException("Caller must implement EditionSelectionCallback");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Country selectedCountry = countryList.get(i);
        if (selectedCountry == null) {
            return;
        }

        countrySpinnerAdapter.setSelectedPosition(i);

        PrefUtils.putString(getActivity(), NewsConstants.KEY_COUNTRY_NAME, languageData.countries.get(i).displayName);
        for (Country country : languageData.countries) {
            if (country.displayName.equals(languageData.countries.get(i).displayName)) {
                country.isSelected = true;
            } else {
                country.isSelected = false;
            }
        }
        languageListAdapter.updateData(new AppListUpdate<>(selectedCountry.languages));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onLanguageSelected(Language language) {
        PrefUtils.putString(getActivity(), NewsConstants.KEY_LANGUAGE_CODE, language.languageCode);
        unSelectOtherLanguages(language);
        getCallback().onEditionSelected(new NewsPayload(language.languageCode, 0));
        dismiss();
    }

    private void unSelectOtherLanguages(Language language) {
        for (Country country : languageData.countries) {
            for (Language lan : country.languages) {
                if (lan.languageCode.equals(language.languageCode)) {
                    lan.isSelected = true;
                } else {
                    lan.isSelected = false;
                }
            }
        }
    }

    public static class CountrySpinnerAdapter extends ArrayAdapter<Country> {

        private Activity activity;
        private ArrayList<Country> data;
        Country tempValues = null;
        LayoutInflater inflater;
        private int selectedPosition = -1;

        public CountrySpinnerAdapter(Activity activitySpinner, int textViewResourceId, ArrayList<Country> objects) {
            super(activitySpinner, textViewResourceId, objects);
            activity = activitySpinner;
            data = objects;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getDropDownCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);

        }

        private View getCustomView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_language_card_colapsed, parent, false);
            }

            TextView label = convertView.findViewById(R.id.txt_title);
            // Set values for spinner each row
            Country selected = null;
            if (selectedPosition >= 0) {
                selected = data.get(selectedPosition);
            }

            if (selected == null) {
                label.setText("Please select a country");
            } else {
                label.setText("Country: " + selected.displayName);
            }
            return convertView;
        }

        View getDropDownCustomView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_language_card, parent, false);
            }

            tempValues = null;
            tempValues = data.get(position);

            TextView label = convertView.findViewById(R.id.txt_title);
            ImageView check = convertView.findViewById(R.id.iv_check);

            // Set values for spinner each row
            label.setText(tempValues.displayName);

            if (tempValues.isSelected) {
                check.setVisibility(View.VISIBLE);
            } else {
                check.setVisibility(View.GONE);
            }

            return convertView;
        }

        void setSelectedPosition(int i) {
            selectedPosition = i;
            notifyDataSetChanged();
        }
    }
}
