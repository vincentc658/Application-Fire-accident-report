package com.app.fire.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BaseAppCompat extends AppCompatActivity implements CallBackIntentData {
    LoadingPopUp loadingDialog = new LoadingPopUp(this);
    public static int TYPE_STUDENT = 1;
    public static int TYPE_MENTOR = 2;
    public static int TYPE_ADMIN = 3;

    public static String TABLE_CUSTOMER = "user";
    public static String TABLE_MENTOR = "mentor";

    public static String TABLE_MATERI = "materi";

    public static String TABLE_QUESTION = "question";
    public static String TABLE_ADMIN = "admin";

    private int requestCode;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        onGetData(data, requestCode);
                        requestCode=0;
                    }
                }
            });
    @Override
    public void onGetData(Intent intent,int requestCode) {}

    public void goToPage(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void goToPageActivityResult(Class<?> cls, int requestCode) {
        this.requestCode=requestCode;
        Intent intent = new Intent(this, cls);
        launcher.launch(intent);
    }
    public void goToPageActivityResult(Intent intent, int requestCode) {
        this.requestCode=requestCode;
        launcher.launch(intent);
    }
    public void goToPageActivityResult(Class<?> cls, int requestCode,Bundle bundle) {
        this.requestCode=requestCode;
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        launcher.launch(intent);
    }

    public void goToPageAndClearPrevious(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void goToPageAndClearPrevious(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToPage(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showLoading(String message) {
        if(loadingDialog!= null){
            loadingDialog.dismisDialog();
        }
        loadingDialog = new LoadingPopUp(this);
        loadingDialog.startLoadingDialog(message);
    }

    public void hideLoading() {
        loadingDialog.dismisDialog();
    }



    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public String convertToCurrency(long price){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(price);
    }
}
