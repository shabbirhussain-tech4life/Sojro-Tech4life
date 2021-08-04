package com.mdcbeta.patient.PersonalCare;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.mdcbeta.R;

import java.io.IOException;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class Utils {

    public static DisplayMetrics D;
    public static String INTENT_ACTION_EXPAND_PLAYER = "com.hostei.shakilmaknojia.expand.player";
    public static int screenHeight = -1;

    public static int GetPosition(View v){
        int Loc[] = new int[2];
        v.getLocationOnScreen(Loc);
        return Loc[1];
    }



    public static void print(String s){
        Log.w(" ", s);
    }


    public static int getHeight(Activity c){

        if(D == null)
            D = new DisplayMetrics();

        c.getWindowManager().getDefaultDisplay().getMetrics(D);
        return D.heightPixels;
    }

    public static int getWidth(Activity c){
        if(D == null)
            D = new DisplayMetrics();

        c.getWindowManager().getDefaultDisplay().getMetrics(D);
        return D.widthPixels;
    }

    public static int getActionBarHeight(Context c)
    {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,c.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static double vValue(final int n) {
        final double n2 = n / 255.0 / 255.0;
        if (n2 <= 0.03928) {
            return n2 / 12.92;
        }
        return Math.pow((0.055 + n2) / 1.055, 2.4);
    }


    public static int makeColorDarker(final int n) {
        final float[] array = new float[3];
        Color.colorToHSV(n, array);
        array[2] *= 0.8f;
        return Color.HSVToColor(array);
    }

    public static double luminanace(final int n, final int n2, final int n3) {
        return 0.2126 * vValue(n) + 0.7152 * vValue(n2) + 0.0722 * vValue(n3);
    }

    public static boolean isOnLowConnectiviy(final Context context) {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == 0) {
            final int subtype = activeNetworkInfo.getSubtype();
            if ((subtype == 1 || subtype == 2 || subtype == 4)) {
                return true;
            }
        }
        return false;
    }

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

    public static void tintWidget(Context context,View view, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
        DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(color));
        view.setBackgroundDrawable(wrappedDrawable);
    }


    public static String  getPath(Uri uri, Activity activity) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static void efficientImageSet(String path,ImageView imageView){

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int orientaion = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);


        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;



        switch(orientaion) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = RotateBitmap(BitmpUtils.decodeSampledBitmapFromFile(path, 500, 500), 90);
                imageView.setImageBitmap(bitmap);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = RotateBitmap(BitmpUtils.decodeSampledBitmapFromFile(path,500,500), 180);
                imageView.setImageBitmap(bitmap);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = RotateBitmap(BitmpUtils.decodeSampledBitmapFromFile(path,500,500), 270);
                imageView.setImageBitmap(bitmap);
                break;
            default:
                bitmap = BitmpUtils.decodeSampledBitmapFromFile(path,500,500);
                imageView.setImageBitmap(bitmap);
                break;
        }



    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
