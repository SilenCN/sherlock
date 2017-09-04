package com.wocao.sherlock.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.wocao.sherlock.Assist.AssistUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Widget.WrapContentHeightViewPager;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by silen on 17-6-10.
 */

public class TimeSettingDialog extends DialogFragment {
    private Context context;
    private long time;
    private long duration;
    private OnTimeSettingListener onTimeSettingListener;

    public TimeSettingDialog(Context context) {
        this.context=context;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置时间");
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final TabLayout tabLayout = new TabLayout(getActivity());
        tabLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(tabLayout);
        final WrapContentHeightViewPager viewPager = new WrapContentHeightViewPager(getActivity());

        viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(viewPager);

        final View layout1 = LayoutInflater.from(getActivity()).inflate(R.layout.time_set_cutdown_view, null);
        final View layout2 = LayoutInflater.from(getActivity()).inflate(R.layout.time_set_timer_view, null);

        final NumberPicker numberPickerDay = (NumberPicker) layout1.findViewById(R.id.TimeSetCutdownNumberPickerDay);
        final NumberPicker numberPickerHour = (NumberPicker) layout1.findViewById(R.id.TimeSetCutdownNumberPickerHour);
        final NumberPicker numberPickerMin = (NumberPicker) layout1.findViewById(R.id.TimeSetCutdownNumberPickerMin);
        numberPickerDay.setMinValue(0);
        numberPickerDay.setMaxValue(15);//15
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(59);

        final NumberPicker numberPickerTimerDay = (NumberPicker) layout2.findViewById(R.id.TimeSetTimerNumberPickerDay);
        final NumberPicker numberPickerTimerHour = (NumberPicker) layout2.findViewById(R.id.TimeSetTimerNumberPickerHour);
        final NumberPicker numberPickerTimerMin = (NumberPicker) layout2.findViewById(R.id.TimeSetTimerNumberPickerMin);
        numberPickerTimerHour.setMinValue(0);
        numberPickerTimerHour.setMaxValue(23);
        numberPickerTimerMin.setMinValue(0);
        numberPickerTimerMin.setMaxValue(59);

        Calendar calendar = Calendar.getInstance();
        numberPickerTimerHour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        numberPickerTimerMin.setValue(calendar.get(Calendar.MINUTE));

        final String dayToPicker[] = new String[15];//15

        for (int i = 0; i < 15; i++) {//15
            dayToPicker[i] = "" + calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        numberPickerTimerDay.setDisplayedValues(dayToPicker);
        numberPickerTimerDay.setMinValue(0);
        numberPickerTimerDay.setMaxValue(dayToPicker.length - 1);
        numberPickerTimerDay.setValue(0);

        PagerAdapter mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                if (position == 0) {
                    ((ViewPager) container).addView(layout1);
                    return layout1;
                } else {
                    ((ViewPager) container).addView(layout2);
                    return layout2;
                }
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "倒计时";
                } else {
                    return "正计时";
                }

            }
        };

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    GregorianCalendar calendar1 = new GregorianCalendar();
                    calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH) + numberPickerTimerDay.getValue(), numberPickerTimerHour.getValue(), numberPickerTimerMin.getValue(), 0);
                    long duration = (calendar1.getTimeInMillis() - System.currentTimeMillis()) / 1000;
                    if (duration < 0) {
                        numberPickerDay.setValue(0);
                        numberPickerHour.setValue(0);
                        numberPickerMin.setValue(0);
                    } else {
                        numberPickerDay.setValue((int) duration / (60 * 60 * 24));
                        numberPickerHour.setValue((int) (duration % (60 * 60 * 24)) / (60 * 60));
                        numberPickerMin.setValue((int) (duration % (60 * 60)) / 60);
                    }
                } else {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH) + numberPickerDay.getValue(), numberPickerHour.getValue() + calendar2.get(Calendar.HOUR_OF_DAY), numberPickerMin.getValue() + calendar2.get(Calendar.MINUTE), 0);
                    numberPickerTimerDay.setValue(numberPickerDay.getValue());
                    numberPickerTimerHour.setValue(calendar2.get(Calendar.HOUR_OF_DAY));
                    numberPickerTimerMin.setValue(calendar2.get(Calendar.MINUTE) + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setTabsFromPagerAdapter(mAdapter);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        builder.setView(linearLayout);

        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GregorianCalendar calendar1 = new GregorianCalendar();
                if (tabLayout.getSelectedTabPosition() == 0) {
                    calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH) + numberPickerDay.getValue(), numberPickerHour.getValue() + calendar1.get(Calendar.HOUR_OF_DAY), numberPickerMin.getValue() + calendar1.get(Calendar.MINUTE), calendar1.get(Calendar.SECOND));
                } else {
                    calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH) + numberPickerTimerDay.getValue(), numberPickerTimerHour.getValue(), numberPickerTimerMin.getValue(), 0);
                }


                if ((duration = (time = calendar1.getTimeInMillis()) - System.currentTimeMillis()) > 0) {

                    if (null!=onTimeSettingListener)
                        onTimeSettingListener.set(time);
/*
                    if (!AssistUtils.checkAssistIsInstall(context)) {
                        AssistUtils.installAssist(context);
                    } else {
                        File filec = new File(Environment.getExternalStorageDirectory().getPath() + "/WocaoStudioSoftware/Sherlock/assist.apk");
                        if (filec.exists()) {
                            filec.delete();
                        }



                        MainActivity.dialog b = new MainActivity.dialog();
                        b.show(getSupportFragmentManager(), null);
                    }*/
                } else {
                    Toast.makeText(context, R.string.setTime_TimeError, Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public TimeSettingDialog setOnTimeSettingListener(OnTimeSettingListener onTimeSettingListener) {
        this.onTimeSettingListener = onTimeSettingListener;
        return this;
    }

    public interface OnTimeSettingListener{
        void set(long time);
    }
}
