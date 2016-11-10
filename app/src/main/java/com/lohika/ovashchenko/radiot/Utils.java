package com.lohika.ovashchenko.radiot;

/**
 * Created by ovashchenko on 11/8/16.
 */
import android.content.Context;
import android.content.res.TypedArray;

public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }

    public static int generateRand(int maxValue) {
        int result = (int)(Math.random() * maxValue);
        return result;
    }
}
