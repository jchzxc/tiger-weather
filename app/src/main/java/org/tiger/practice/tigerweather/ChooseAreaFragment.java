package org.tiger.practice.tigerweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.tiger.practice.tigerweather.db.City;
import org.tiger.practice.tigerweather.db.Country;
import org.tiger.practice.tigerweather.db.Province;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tiger on 2018/1/12.
 */

public class ChooseAreaFragment extends Fragment {
    // constant
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    private static final String ADDRESS_BASE = "http://guolin.tech/api/china";

    // widget
    private ProgressDialog mProgressDialog;
    private TextView mTitleTextView;
    private Button mBackButton;
    private ListView mListView;

    // datas
    private ArrayAdapter<String> mAdapter;
    private List<String> mDataSet = new ArrayList<>();
    private List<Province> mProvinces;
    private List<City> mCities;
    private List<Country> mCountries;

    // current
    private Province mCurrProvince;
    private City mCurrCity;
    private Country mCurrCountry;
    private int mCurrLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        mBackButton = (Button) view.findViewById(R.id.back_button);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mDataSet);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryProvinces();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrLevel == LEVEL_PROVINCE) {
                    mCurrProvince = mProvinces.get(position);
                    queryCities();
                } else if (mCurrLevel == LEVEL_CITY) {
                    mCurrCity = mCities.get(position);
                    queryCountries();
                } else if (mCurrLevel == LEVEL_COUNTRY) {
                    mCurrCountry = mCountries.get(position);
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", mCurrCountry.weatherId);
                    startActivity(intent);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrLevel == LEVEL_COUNTRY) {
                    queryCities();
                } else if (mCurrLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
    }

    // function
    private void queryProvinces() {
        mTitleTextView.setText("China");
        mBackButton.setVisibility(View.GONE);
        mProvinces = DataSupport.findAll(Province.class);
        if (mProvinces.size() > 0) {
            mDataSet.clear();
            for (Province p : mProvinces) {
                mDataSet.add(p.name + "(" + p.uid + ")");
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(ADDRESS_BASE, "province");
        }
    }

    private void queryCities() {
        mTitleTextView.setText(mCurrProvince.name);
        mBackButton.setVisibility(View.VISIBLE);
        mCities = DataSupport.where("provinceId = ?", String.valueOf(mCurrProvince.uid)).find(City.class);
        if (mCities.size() > 0) {
            mDataSet.clear();
            for (City c : mCities) {
                mDataSet.add(c.name + "(" + c.uid + ")");
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrLevel = LEVEL_CITY;
        } else {
            queryFromServer(ADDRESS_BASE + "/" + mCurrProvince.uid, "city");
        }
    }

    private void queryCountries() {
        mTitleTextView.setText(mCurrCity.name);
        mBackButton.setVisibility(View.VISIBLE);
        mCountries = DataSupport.where("cityId = ?", String.valueOf(mCurrCity.uid)).find(Country.class);
        if (mCountries.size() > 0) {
            mDataSet.clear();
            for (Country c : mCountries) {
                mDataSet.add(c.name + "(" + c.uid + ")");
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrLevel = LEVEL_COUNTRY;
        } else {
            queryFromServer(ADDRESS_BASE + "/" + mCurrProvince.uid + "/" + mCurrCity.uid, "country");
        }
    }

    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        Utility.requestHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "query server fail for type = " + type, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String text = response.body().string();
                boolean result = false;
                if (type.equals("province")) {
                    result = Utility.procProvinceResponse(text);
                } else if (type.equals("city")) {
                    result = Utility.procCityResponse(text, mCurrProvince.uid);
                } else if (type.equals("country")) {
                    result = Utility.procCountryResponse(text, mCurrCity.uid);
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if (type.equals("province")) {
                                queryProvinces();
                            } else if (type.equals("city")) {
                                queryCities();
                            } else if (type.equals("country")) {
                                queryCountries();
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(getContext(), "proc response fail for type = " + type, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
