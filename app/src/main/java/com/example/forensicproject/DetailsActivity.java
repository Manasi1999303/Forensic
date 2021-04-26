package com.example.forensicproject;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    TelephonyManager tm;
    TextView imeitxt,deviceName,version_txt;
    Button saveDetailsBtn;
    String mText;
    private static final int WRITE_EXTERNAL_STORAGE_CODE  = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imeitxt = (TextView)findViewById(R.id.imei);
        deviceName = (TextView)findViewById(R.id.device_name);
        version_txt = (TextView)findViewById(R.id.version);
        saveDetailsBtn = (Button)findViewById(R.id.save_details_btn);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},PackageManager.PERMISSION_GRANTED);
        buttongetIMEI();

        String mManufactrer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String version = Build.VERSION.RELEASE;
        String verName = Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();

        deviceName.setText(mManufactrer.toUpperCase() + "" + model);
        version_txt.setText(version + "" +verName);

        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = "\n\n" + imeitxt.getText().toString() +"\n\n" + deviceName.getText().toString()+ "\n\n" +version_txt.getText().toString();
                if (ContextCompat.checkSelfPermission(DetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(DetailsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(DetailsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                    }else
                    {
                        ActivityCompat.requestPermissions(DetailsActivity.this,
                          new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                        //save(mText);
                    }
                }else {
                    //do stuff
                    saveToTxtFile(mText);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buttongetIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);


        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        String stringIEMI  = telephonyManager.getImei();
        imeitxt.setText(stringIEMI);
    }

    private void saveToTxtFile(String mText) {
        mText = "\n\n" + imeitxt.getText().toString() +"\n\n" + deviceName.getText().toString()+ "\n\n" +version_txt.getText().toString();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        try {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/My Files/");
            dir.mkdirs();
            String fileName = "MobileDetails_" + timeStamp +".txt";
            File file = new File(dir,fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mText);
            bw.close();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(mText.getBytes());
            fileOutputStream.close();
            imeitxt.setText("");
            deviceName.setText("");
            version_txt.setText("");
            Toast.makeText(this, fileName+" is saved to \n"+dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "MobileDetails_.txt saved to My Files", Toast.LENGTH_SHORT).show();
        }

    }
    private void save(String v)
    {
        mText = "\n\n" + imeitxt.getText().toString() +"\n\n" + deviceName.getText().toString()+ "\n\n" +version_txt.getText().toString();
        FileOutputStream fos = null;
        String FILE_NAME = "Mobile_Details_";
        try {
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(mText.getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
