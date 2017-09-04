package com.wocao.sherlock;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ExitApplication extends Application
{

    private List<Activity> activityList = new ArrayList<Activity>() ;

    public <T> T[] toArray(T[] array) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Activity> subList(int start, int end) {
        // TODO Auto-generated method stub
        return null;
    }

    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Activity set(int location, Activity object) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean retainAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean remove(Object object) {
        // TODO Auto-generated method stub
        return false;
    }

    public Activity remove(int location) {
        // TODO Auto-generated method stub
        return null;
    }


    public ListIterator<Activity> listIterator(int location) {
        // TODO Auto-generated method stub
        return null;
    }

    public ListIterator<Activity> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public int lastIndexOf(Object object) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Iterator<Activity> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    public int indexOf(Object object) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Activity get(int location) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean containsAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean contains(Object object) {
        // TODO Auto-generated method stub
        return false;
    }

    public void clear() {
        // TODO Auto-generated method stub

    }

    public boolean addAll(int arg0, Collection<? extends Activity> arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean addAll(Collection<? extends Activity> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void add(int location, Activity object) {
        // TODO Auto-generated method stub

    }

    public boolean add(Activity object) {
        // TODO Auto-generated method stub
        return false;
    }

    private static ExitApplication instance;

    public ExitApplication()
    {
    }
    //鍗曚緥妯″紡涓幏鍙栧敮涓�鐨凟xitApplication瀹炰緥
    public static ExitApplication getInstance()
    {
        if(null == instance)
        {
            instance = new ExitApplication();
        }
        return instance;

    }
    //娣诲姞Activity鍒板鍣ㄤ腑
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
//閬嶅巻鎵�鏈堿ctivity骞秄inish

    public void exit()
    {

        for(Activity activity:activityList)
        {
            activity.finish();
        }

       // System.exit(0);

    }
}