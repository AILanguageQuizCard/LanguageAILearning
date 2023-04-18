package com.example.chatgpt.voiceplay;

import android.content.Context;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author : Prashant Adesara
 * @url https://www.bytesbee.com
 * Navigation to new activity
 */
public class Screens {

    private final Context context;

    public Screens(Context con) {
        context = con;
    }


    private Toast toastMessage;

    public void showToast(final String strMsg) {
        try {
            if (toastMessage != null) {
                toastMessage.cancel();
            }
            if (!Utils.isEmpty(strMsg)) {
                toastMessage = Toast.makeText(context, strMsg, Toast.LENGTH_LONG);
                try {
                    @SuppressWarnings("deprecation") final LinearLayout toastLayout = (LinearLayout) toastMessage.getView();
                    final TextView txtToast = (TextView) toastLayout.getChildAt(0);
                    txtToast.setTypeface(Utils.getRegularFont(context));
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
                toastMessage.show();
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void showToast(final int strMsg) {
        showToast(context.getString(strMsg));
    }


}
