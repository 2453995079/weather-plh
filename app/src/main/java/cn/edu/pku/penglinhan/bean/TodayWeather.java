package cn.edu.pku.penglinhan.bean;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;
    private String fengli,fengli1,fengli2,fengli3,fengli4,fengli5;
    private String date,date1,date2,date3,date4,date5;
    private String high,high1,high2,high3,high4,high5;
    private String low,low1,low2,low3,low4,low5;
    private String type,type1,type2,type3,type4,type5;
    public String getCity()
    {return city;}
    public String getUpdatetime()
    {return updatetime;}
    public String getWendu()
    {return wendu;}
    public String getShidu()
    {return shidu;}
    public String getPm25()
    {return pm25;}
    public String getQuality()
    {return quality;}
    public String getDate()
    {return date;}
    public String getDate1()
    {return date1;}
    public String getDate2()
    {return date2;}
    public String getDate3()
    {return date3;}
    public String getDate4()
    {return date4;}
    public String getDate5()
    {return date5;}
    public String getHigh()
    {return high;}
    public String getHigh1()
    {return high1;}
    public String getHigh2()
    {return high2;}
    public String getHigh3()
    {return high3;}
    public String getHigh4()
    {return high4;}
    public String getHigh5()
    {return high5;}
    public String getLow()
    {return low;}
    public String getLow1()
    {return low1;}
    public String getLow2()
    {return low2;}
    public String getLow3()
    {return low3;}
    public String getLow4()
    {return low4;}
    public String getLow5()
    {return low5;}
    public String getType()
    {return type;}
    public String getType1()
    {return type1;}
    public String getType2()
    {return type2;}
    public String getType3()
    {return type3;}
    public String getType4()
    {return type4;}
    public String getType5()
    {return type5;}
    public String getFengli()
    {return fengli;}
    public String getFengli1()
    {return fengli1;}
    public String getFengli2()
    {return fengli2;}
    public String getFengli3()
    {return fengli3;}
    public String getFengli4()
    {return fengli4;}
    public String getFengli5()
    {return fengli5;}
    public void setCity(String city){
        this.city=city;
    }
    public void setUpdatetime(String updatetime){
        this.updatetime=updatetime;
    }
    public void setWendu(String wendu){
        this.wendu=wendu;
    }
    public void setShidu(String shidu){
        this.shidu=shidu;
    }
    public void setPm25(String pm25){
        this.pm25=pm25;
    }
    public void setQuality(String quality){
        this.quality=quality;
    }
    public void setFengxiang(String fengxiang){
        this.fengxiang=fengxiang;
    }
    public void setFengli(String fengli){this.fengli=fengli;}
    public void setFengli1(String fengli){
        this.fengli1=fengli;
    }
    public void setFengli2(String fengli){
        this.fengli2=fengli;
    }
    public void setFengli3(String fengli){
        this.fengli3=fengli;
    }
    public void setFengli4(String fengli){
        this.fengli4=fengli;
    }
    public void setFengli5(String fengli){
        this.fengli5=fengli;
    }
    public void setDate(String date){
        this.date=date;
    }
    public void setDate1(String date){
        this.date1=date;
    }
    public void setDate2(String date){
        this.date2=date;
    }
    public void setDate3(String date){
        this.date3=date;
    }
    public void setDate4(String date){
        this.date4=date;
    }
    public void setDate5(String date){
        this.date5=date;
    }
    public void setHigh(String high){
        this.high=high;
    }
    public void setHigh1(String high){
        this.high1=high;
    }
    public void setHigh2(String high){
        this.high2=high;
    }
    public void setHigh3(String high){
        this.high3=high;
    }
    public void setHigh4(String high){
        this.high4=high;
    }
    public void setHigh5(String high){
        this.high5=high;
    }
    public void setLow(String low){
        this.low=low;
    }
    public void setLow1(String low){
        this.low1=low;
    }
    public void setLow2(String low){
        this.low2=low;
    }
    public void setLow3(String low){
        this.low3=low;
    }
    public void setLow4(String low){
        this.low4=low;
    }
    public void setLow5(String low){
        this.low5=low;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setType1(String type){
        this.type1=type;
    }
    public void setType2(String type){
        this.type2=type;
    }
    public void setType3(String type){
        this.type3=type;
    }
    public void setType4(String type){
        this.type4=type;
    }
    public void setType5(String type){
        this.type5=type;
    }


    @Override
    public String toString(){
        return "TodayWeather{"+
                "city='"+city+"\'"+
                "updatetime='"+updatetime+"\'"+
                "wendu='"+wendu+"\'"+
                "shidu='"+shidu+"\'"+
                "pm25='"+pm25+"\'"+
                "quality='"+quality+"\'"+
                "fengxiang='"+fengxiang+"\'"+
                "fengli='"+fengli+"\'"+
                "date='"+date+"\'"+
                "high='"+high+"\'"+
                "low='"+low+"\'"+
                "type='"+type+"\'"+
                '}';
    }

}
