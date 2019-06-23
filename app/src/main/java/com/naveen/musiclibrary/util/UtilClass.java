package com.naveen.musiclibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.view.Gravity;
import android.view.Window;

import com.naveen.musiclibrary.R;

public class UtilClass
{
    private static Dialog progressDialog;

    public static void showSimpleProgressDialog(Activity activity)
    {
        progressDialog = new Dialog( activity );
        progressDialog.setContentView( R.layout.smiple_progressbar);
        progressDialog.getWindow().setBackgroundDrawable( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        Window dialogWindow = progressDialog.getWindow();
        dialogWindow.setGravity( Gravity.CENTER );
        progressDialog.setCancelable( false );
        progressDialog.show();
    }
    public static void hideSimpleProgressDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static boolean isNetworkConnected(Context context)
    {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            assert cm != null;
            return cm.getActiveNetworkInfo() != null;
        } catch(Exception e) {
            return true;
        }
    }

}
