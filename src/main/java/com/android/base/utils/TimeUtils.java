package com.android.base.utils;

import com.android.base.R;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TimeUtils {


    private static long sTime;

    public static Date getCurrentDataTime(int i) throws ParseException {
        return new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * i);
    }


    public static long getEndTime(String mTime, String mTime2) {
        return Long.parseLong(mTime) - Long.parseLong(mTime2);
    }


    public static long getFromEndTime(String mTime1, String mTime2) {
        return Long.parseLong(mTime2) - Long.parseLong(mTime1);
    }



    public static boolean getJudgeStart(String mTime, String mServerTime) {
        return Long.parseLong(mTime) - Long.parseLong(mServerTime) > 0;
    }


    //判断活动的状态
    public static ArrayList<String> getTimeState(ArrayList<String> content, String mTime, int postion) {
        for (int i = 0; i < content.size(); i++) {
            int s = compare_date(content.get(i), mTime);
            if (i == postion && s < 0) {
                content.set(i, getHHmmTimes(content.get(i)) + "\n正在疯抢");
                continue;
            }
            if (s == 1) {
                content.set(i, getHHmmTimes(content.get(i)) + "\n即将开秒");
            } else {
                content.set(i, getHHmmTimes(content.get(i)) + "\n已开秒");
            }
        }
        return content;
    }


    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            DATE1 = DATE1.replaceAll("T", " ");
            DATE2 = DATE2.replaceAll("T", " ");
            Double time = Double.parseDouble(getTimes(DATE1));
            Double time1 = Double.parseDouble(getTimes(DATE2));

            if (time > time1) {
                return 1;
            } else if (time < time1) {
                return -1;
            } else {
                return 0; //刚好开始
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return 0;
    }



    public static int compare_dateRemove3zero(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(getTimesRemove3zero(DATE1));
            Date dt2 = df.parse(getTimesRemove3zero(DATE2));

            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0; //刚好开始
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return 0;
    }


    //时间格式转换  yyyy-MM-dd hh:mm 转换 hh:mm
    public static long getTime(String DATE1) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date dt1 = df.parse(DATE1);
            return dt1.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Long.parseLong(null);
    }



    public static String getTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sTime = format.parse(DATE1).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(sTime);
    }



    public static String getTimesRemove3zero(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.valueOf(format.format(Long.valueOf(DATE1)));
    }


    public static String getHHmmTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return String.valueOf(format.format(Long.valueOf(DATE1) * 1000));
    }


    public static String getDetialTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(Long.valueOf(DATE1) * 1000);
    }

    public static String getDetialTimesNot10000(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(Long.valueOf(DATE1));
    }

    public static String getDetialTimesxx(String DATE1,String format1) {
        Logger.e("--------getDetialTimesxx---->"+Locale.getDefault().getCountry());
        SimpleDateFormat format = new SimpleDateFormat(format1, Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        return format.format(Long.valueOf(DATE1));
    }


    public static String getDetialTimess(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(Long.valueOf(DATE1) * 1000);
    }


    public static String getDetialTimess(String DATE1, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(Long.valueOf(DATE1) * 1000);
    }



    public static String getDayTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        BigDecimal db = new BigDecimal(DATE1);
        return format.format(db.multiply(new BigDecimal(1000)));
    }



    public static String getY_M(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM");
        BigDecimal db = new BigDecimal(DATE1);


        return format.format(db.multiply(new BigDecimal(1000)));
    }


    public static String getYandM(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        BigDecimal db = new BigDecimal(DATE1);


        return format.format(db.multiply(new BigDecimal(1000)));
    }



    public static String getTimeForSimple(String time) {

        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH:mm");
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }


    public static String getTimeForDetail(String time) {


        SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }



    public static String getTimeForDetail2(String time) {


        SimpleDateFormat format = new SimpleDateFormat(" yyyy年MM月dd日 HH:mm");
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }



    public static String getTimeForDetail2(String time,String timeformat,boolean is1000) {


        SimpleDateFormat format = new SimpleDateFormat(timeformat);
        BigDecimal db = new BigDecimal(time);


        if(is1000){
            return format.format(db.multiply(new BigDecimal(1000)));
        }else{
            return format.format(db);
        }



    }



    public static String getTimeForDetailForCustom(String time, String formater) {


        SimpleDateFormat format = new SimpleDateFormat(formater);
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }

    // 将时间戳转为字符串（毫秒去掉3个0）
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        // 例如：

        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));//z这里原来是new Date(lcc_time*1000 )
        return re_StrTime;
    }

    // 将时间戳转为字符串（毫秒去掉3个0）
    public static String getStrTimeCommon(String cc_time, String fromat) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);//"yyyy年MM月dd日 HH:mm"
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));//z这里原来是new Date(lcc_time*1000 )
        return re_StrTime;
    }



    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }



    public static String datatoLong(String time, String format) {

        //  String type="yyyy-MM-dd-HH-mm-ss";
        String type = format;

        SimpleDateFormat sdr = new SimpleDateFormat(type,
                Locale.getDefault());
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            times = String.valueOf(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String String2Timestap(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long beginUseTime = 0;
        try {
            beginUseTime = sdf.parse(time).getTime() / 1000;
        } catch (Exception e) {

        }
        return beginUseTime + "";
    }


    public static String getTime() {

        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳

        String str = String.valueOf(time);

        return str;

    }


    public static long getTime1() {

        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳



        return time;

    }



    public static int[] formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = "剩余："+hour+"小时"+minute+"分"+second+"秒";

        return new int[]{hour,minute,second};
    }


    public static long getTimeNo1000() {


        return System.currentTimeMillis()/1000;

    }


    public static String getPublicTime(long time) {
        //获取time距离当前的秒数
        int ct = (int) (System.currentTimeMillis() / 1000 - time);

        if (ct <= 0) {
            return "刚刚";
        }

        if (ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = ct / 86400;
            return day + "天前";
        }
        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }


    public static long getPublicTimeDstNo1000(long time) {
        //获取time距离当前的秒数
        long ct =  (System.currentTimeMillis() - time);

        return ct;
    }



    public static final String FORMAT_TYPE_1 = "yyyy-MM-dd  HH:mm:ss";
    public static final String FORMAT_TYPE_2 = "yyyy.MM.dd";
    public static final String FORMAT_TYPE_3 = "MM月dd日 HH:mm";
    public static final String FORMAT_TYPE_4 = "HH:mm";
    public static final String FORMAT_TYPE_5 = "MM/dd/yyyy  HH:mm:ss";
    public static final String FORMAT_TYPE_6 = "MM月dd日";

    public static String getFormatTime(long time, String format,boolean is1000) {

        if(is1000){
            time*=1000;
        }

        return new SimpleDateFormat(format).format(new Date(time));

    }

    public static String getFormatTime(String time, String format) {

        return new SimpleDateFormat(format).format(new Date(time));

    }

    //将yyyy-mm-dd 转换成时间戳

    public static String getFormatTime2(long time, String format) {

        return new SimpleDateFormat(format).format(new Date(time * 1000));

    }




    // 获取今天的日期
    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }


    public static String getSpecialFormatTime() {
        return DATE_SPECIAL_FORMAT.format(new Date());
    }

    public static String getSpecialFormatTime(long sTime) {
        return DATE_SPECIAL_FORMAT.format(new Date(sTime));
    }

    private static final DateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final DateFormat DATE_SPECIAL_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static String getFormatTimeyyyyMMddHHmm(long timeStamp) {
        return DATE_FORMAT.format(new Date(timeStamp));
    }

    //时间格式转换  yyyy-MM-dd hh:mm 转换 hh:mm
    public static long getTime2(String DATE1) {
        try {
            Date dt1 = DATE_SPECIAL_FORMAT.parse(DATE1);
            return dt1.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return System.currentTimeMillis();
    }



    static String dayNames[] = {UIUtils.getString(R.string.str_7),
            UIUtils.getString(R.string.str_1),
            UIUtils.getString(R.string.str_2),
            UIUtils.getString(R.string.str_3),
            UIUtils.getString(R.string.str_4),
            UIUtils.getString(R.string.str_5),
            UIUtils.getString(R.string.str_6)};

    public static String getNewChatTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日 HH:mm";
        String yearTimeFormat = "yyyy年M月d日 HH:mm";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上";
        } else if (hour == 12) {
            am_pm = "中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午";
        } else if (hour >= 18) {
            am_pm = "晚上";
        }

        timeFormat = "M月d日 " + am_pm + "HH:mm";
        yearTimeFormat = "yyyy年M月d日 " + am_pm + "HH:mm";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = am_pm + getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + am_pm + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
//                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
//                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
//                        if (dayOfMonth == todayOfMonth) {//表示是同一周
//                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
//                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
//                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getHourAndMin(timesamp);
//                            } else {
//                                result = getTime(timesamp, timeFormat);
//                            }
//                        } else {
//                            result = getTime(timesamp, timeFormat);
//                        }
//                        break;
                    default:
                        result = getTime(timesamp, timeFormat);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat);
            }
        } else {
            result = getYearTime(timesamp, yearTimeFormat);
        }
        return result;
    }


    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }


    public static String getTime(long time, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }


    public static String getYearTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }


   public static String string2TimezoneDefault(String srcDateTime,
            String dstTimeZoneId) {
            return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime,
                     "yyyy-MM-dd HH:mm:ss", dstTimeZoneId);
            }




    public static String string2Timezone(String srcFormater,
           String srcDateTime, String dstFormater, String dstTimeZoneId) {
              if (srcFormater == null || "".equals(srcFormater))
                       return null;
               if (srcDateTime == null || "".equals(srcDateTime))
                      return null;
              if (dstFormater == null || "".equals(dstFormater))
                      return null;
             if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
                     return null;
              SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
             try {
                      int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
                    Date d = sdf.parse(srcDateTime);
                      long nowTime = d.getTime();
                      long newNowTime = nowTime - diffTime;
                        d = new Date(newNowTime);
                       return date2String(dstFormater, d);
                    } catch (ParseException e) {
                     Logger.e(e.getMessage());
                    return null;
                   } finally {
                      sdf = null;
                   }
          }


    private static int getDiffTimeZoneRawOffset(String timeZoneId) {
              return TimeZone.getDefault().getRawOffset()
                    - TimeZone.getTimeZone(timeZoneId).getRawOffset();
            }



  public static String date2String(String formater, Date aDate) {
               if (formater == null || "".equals(formater))
                  return null;
               if (aDate == null)
                      return null;
              return (new SimpleDateFormat(formater)).format(aDate);
            }


}
