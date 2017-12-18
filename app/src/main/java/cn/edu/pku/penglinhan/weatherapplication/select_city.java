package cn.edu.pku.penglinhan.weatherapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.pku.penglinhan.app.MyApplication;
import cn.edu.pku.penglinhan.bean.City;

import static android.R.attr.data;

public class select_city extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mList;
    private String[] city_List;
    private List<City> cityList, filterDataList;
    private List<String> f;
    private ClearEditText mClearEditText;
    private ArrayAdapter<String> adapter;
    private TextView cityname;
    public select_city() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        initView();
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        mClearEditText = (ClearEditText) findViewById(R.id.search_city);
        mClearEditText.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int s, int start, int after) {

           }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterData(s.toString());
            mList.setAdapter(adapter);

             }

             @Override
             public void afterTextChanged(Editable s) {

               }
            }
        );
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                /*返回的参数*/
                /*Intent i = new Intent();
                i.putExtra("cityCode", "101160101");
                setResult(RESULT_OK, i);*/
                finish();
                break;
            default:
                break;
        }
    }

    private void initView() {
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        String cityname2=null;
        if (intent != null) {
            cityname2 = intent.getStringExtra("cityname");
        }
        cityname = (TextView) findViewById(R.id.title_name);
        cityname.setText(cityname2);


        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getCityList();
        f = new ArrayList<String>();
        for (City city : cityList) {
            f.add(city.getCity() + " " + city.getNumber());
        }
        int size = f.size();
        city_List = (String[]) f.toArray(new String[size]);
        adapter = new ArrayAdapter<String>(select_city.this, android.R.layout.simple_list_item_1, city_List);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String b = city_List[position].substring(city_List[position].indexOf(" ") + 1, city_List[position].length());
                Intent i = new Intent();
                i.putExtra("cityCode", b);
                setResult(RESULT_OK, i);
                finish();
                ;
            }
        });
    }

    private void filterData(String filterStr) {
        ;
        f = new ArrayList<String>();
        Log.d("Filter", filterStr);
        if (TextUtils.isEmpty(filterStr)) {
            for (City city : cityList) {
                f.add(city.getCity() + " " + city.getNumber());
            }
        } else {
            for (City city : cityList) {
                if (city.getCity().indexOf(filterStr.toString()) != -1) {
                    f.add(city.getCity() + " " + city.getNumber());
                }
            }
        }
        int size = f.size();
        city_List = (String[]) f.toArray(new String[size]);
        adapter = new ArrayAdapter<String>(select_city.this, android.R.layout.simple_list_item_1, city_List);

    }
}
