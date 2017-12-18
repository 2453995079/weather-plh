package cn.edu.pku.penglinhan.weatherapplication;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.penglinhan.bean.TodayWeather;
import cn.edu.pku.penglinhan.bean.ViewPagerAdapter;
import cn.edu.pku.penglinhan.util.NetUtil;
/**
 * Created by Administrator on 2017/10/11 0011.
 */

public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private static final int UPDATE_TODAY_WEATHER = 1;/*与线程有关*/
    private String city_code1;
    private ImageView mUpdateBtn;/*按钮的变量*/
    private ImageView mCitySelect;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv,temperaturemon;
    private TextView fengli1,fengli2,fengli3,fengli4,fengli5,fengli6,date1,date2,date3,date4,date5,date6,tem1,tem2,tem3,tem4,tem5,tem6,type1,type2,type3,type4,type5,type6;
    private ImageView weatherImg, pmImg;
    private ViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids={R.id.iv1,R.id.iv2};
    /*线程给主线程的消息*/
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);/*更新页面数据*/
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
    /*添加点击按钮的监听变量
    * R.id:是布局文件中设置过的id*/
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
    /*连接网络判断网络是否能连接成功*/
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this,"网络OK！", Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this,"网络挂了！", Toast.LENGTH_LONG).show();
        }
        initView();/*初始化将*/
        initDots();/*小圆点切换*/
        mCitySelect=(ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
    }

    @Override
    /*点击按钮的事件响应*/
    public void onClick(View view) {
        if(view.getId()==R.id.title_city_manager){


            Intent i=new Intent(this, select_city.class);
            i.putExtra("cityname",cityTv.getText().toString());
            //startActivity(i);
            startActivityForResult(i,1);/*用于接受onActivityResult返回的参数*/
        }
        if (view.getId() == R.id.title_update_btn){/*刷新按钮的响应 */

            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode;
            if(city_code1==null)
            { cityCode = sharedPreferences.getString("main_city_code","101010100");}/*通过SharedPreferences读取城市id，如果没有定义则缺省为101010100（北京城市ID）。*/
            else
            { cityCode=city_code1;}
            Log.d("myWeather",cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);/*获取网络数据的函数*/
            }else
            {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了！",Toast.LENGTH_LONG).show();
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            city_code1=newCityCode;
            Log.d("myWeather", "选择的城市代码为"+newCityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     *
     * @param cityCode
     * 获取网上数据
     */
    private void queryWeatherCode(String cityCode)  {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {/*线程*/
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather=null;
                try{
                    URL url = new URL(address);/*地址*/
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather", str);/*输出答案*/
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather=parseXML(responseStr);/*用与解析网上数据*/
                    if(todayWeather !=null){
                        Log.d("myWeather",todayWeather.toString());
                        /*通过消息机制将解析的天气对象，通过消息发送给主线程，主线程接受消息后调用更新函数来更新界面数据*/
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    /*解析获取的网上数据网上数据*/
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        int fengxiangCount=0;
        int fengliCount =0;
        int dateCount=0;
        int highCount=0;
        int lowCount=0;
        int typeCount =0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli2(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli3(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli4(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fl_1") ) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli5(xmlPullParser.getText());

                            }else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate1(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate2(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate3(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate4(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date_1") ) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate5(xmlPullParser.getText());

                            }else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh1(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh2(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh3(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh4(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high_1") ) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh5(xmlPullParser.getText().substring(2).trim());

                            }else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow1(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow2(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow3(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow4(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow5(xmlPullParser.getText().substring(2).trim());

                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType2(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType3(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType4(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type_1") ) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType5(xmlPullParser.getText());

                            }
                        }

                        break;


                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
    /*小圆点切换动画*/
    void initDots(){
        dots= new ImageView[views.size()];
        for(int i=0;i<views.size();i++){
            dots[i]=(ImageView) findViewById(ids[i]);
        }
    }
    /*初始化设置设为*/
    void initView(){
        city_code1=null;
        LayoutInflater inflater= LayoutInflater.from(this);
        views =new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1,null));
        views.add(inflater.inflate(R.layout.page2,null));
        vpAdapter = new ViewPagerAdapter(views,this);
        vp = (ViewPager)findViewById(R.id.weather_viewpager);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);

        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        temperaturemon = (TextView) findViewById(R.id.temperaturemon);
        date1=(TextView) views.get(0).findViewById(R.id.week_today1);
        date2=(TextView) views.get(0).findViewById(R.id.week_today2);
        date3=(TextView) views.get(0).findViewById(R.id.week_today3);
        date4=(TextView) views.get(1).findViewById(R.id.week_today4);
        date5=(TextView) views.get(1).findViewById(R.id.week_today5);
        date6=(TextView) views.get(1).findViewById(R.id.week_today6);
        tem1=(TextView) views.get(0).findViewById(R.id.temperaturemon1);
        tem2=(TextView) views.get(0).findViewById(R.id.temperaturemon2);
        tem3=(TextView) views.get(0).findViewById(R.id.temperaturemon3);
        tem4=(TextView) views.get(1).findViewById(R.id.temperaturemon4);
        tem5=(TextView) views.get(1).findViewById(R.id.temperaturemon5);
        tem6=(TextView) views.get(1).findViewById(R.id.temperaturemon6);
        type1=(TextView) views.get(0).findViewById(R.id.climate1);
        type2=(TextView) views.get(0).findViewById(R.id.climate2);
        type3=(TextView) views.get(0).findViewById(R.id.climate3);
        type4=(TextView) views.get(1).findViewById(R.id.climate4);
        type5=(TextView) views.get(1).findViewById(R.id.climate5);
        type6=(TextView) views.get(1).findViewById(R.id.climate6);
        fengli1=(TextView) views.get(0).findViewById(R.id.wind1);
        fengli2=(TextView) views.get(0).findViewById(R.id.wind2);
        fengli3=(TextView) views.get(0).findViewById(R.id.wind3);
        fengli4=(TextView) views.get(1).findViewById(R.id.wind4);
        fengli5=(TextView) views.get(1).findViewById(R.id.wind5);
        fengli6=(TextView) views.get(1).findViewById(R.id.wind6);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        temperaturemon.setText("N/A");

        date1.setText("N/A");date2.setText("N/A");date3.setText("N/A");date4.setText("N/A");date5.setText("N/A");date6.setText("N/A");
        tem1.setText("N/A");tem2.setText("N/A");tem3.setText("N/A");tem4.setText("N/A");tem5.setText("N/A");tem6.setText("N/A");
        type1.setText("N/A");type2.setText("N/A");type3.setText("N/A");type4.setText("N/A");type5.setText("N/A");type6.setText("N/A");
        fengli1.setText("N/A");fengli2.setText("N/A");fengli3.setText("N/A");fengli4.setText("N/A");fengli5.setText("N/A");fengli6.setText("N/A");


    }
    /*更新界面数据*/
    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        temperaturemon.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());
        date1.setText(todayWeather.getDate5());
        date2.setText(todayWeather.getDate());
        date3.setText(todayWeather.getDate1());
        date4.setText(todayWeather.getDate2());
        date5.setText(todayWeather.getDate3());
        date6.setText(todayWeather.getDate4());
        tem1.setText(todayWeather.getHigh5()+"~"+todayWeather.getLow5());
        tem2.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        tem3.setText(todayWeather.getHigh1()+"~"+todayWeather.getLow1());
        tem4.setText(todayWeather.getHigh2()+"~"+todayWeather.getLow2());
        tem5.setText(todayWeather.getHigh3()+"~"+todayWeather.getLow3());
        tem6.setText(todayWeather.getHigh4()+"~"+todayWeather.getLow4());
        type1.setText(todayWeather.getType5());
        type2.setText(todayWeather.getType());
        type3.setText(todayWeather.getType1());
        type4.setText(todayWeather.getType2());
        type5.setText(todayWeather.getType3());
        type6.setText(todayWeather.getType4());
        fengli1.setText(todayWeather.getFengli5());
        fengli2.setText(todayWeather.getFengli());
        fengli3.setText(todayWeather.getFengli1());
        fengli4.setText(todayWeather.getFengli2());
        fengli5.setText(todayWeather.getFengli3());
        fengli6.setText(todayWeather.getFengli4());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int a=0;a<ids.length;a++){
            if(a==position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else{
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

