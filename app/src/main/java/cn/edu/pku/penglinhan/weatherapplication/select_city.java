package cn.edu.pku.penglinhan.weatherapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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
    private List<City> cityList;
    private List<String>f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        initView();
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                /*返回的参数*/
                Intent i = new Intent();
                i.putExtra("cityCode", "101160101");
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }

    private void initView() {
        City filterDateList;
        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication=(MyApplication)getApplication();
        cityList=myApplication.getCityList();
        List<String> f = new ArrayList<String>();
        for(City city:cityList){
            f.add(city.getCity()+" "+city.getNumber());
        }
        int size=f.size();
        city_List=(String[])f.toArray(new String[size]);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(select_city.this,android.R.layout.simple_list_item_1,city_List);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                City city =cityList.get(position);
                Intent i =new Intent();
                i.putExtra("cityCode",city.getNumber());
                setResult(RESULT_OK,i);
                finish();
               ;
            }
        });
    }
}
