package com.example.forensicproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CallLogActivity extends AppCompatActivity {

    TextView textView;
    String mText;
    Button saveBtn;
    private static final int WRITE_EXTERNAL_STORAGE_CODE  = 2;
    private static final int READ_CALL_LOG_CODE  = 1;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    public String phNumber,dateString,callDuration,dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);
        saveBtn = findViewById(R.id.save_sms_text_btn);

        if (ContextCompat.checkSelfPermission(CallLogActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                    Manifest.permission.READ_CALL_LOG)){
                ActivityCompat.requestPermissions(CallLogActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG},READ_CALL_LOG_CODE);
            }else
            {
                ActivityCompat.requestPermissions(CallLogActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG},READ_CALL_LOG_CODE);
            }
        }else{
            //do stuff
            textView = (TextView) findViewById(R.id.textView);
            textView.setText(getCallsDetails());
        }


       saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = textView.getText().toString();
                //saveToTxtFile(mText);
                if (ContextCompat.checkSelfPermission(CallLogActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(CallLogActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                    }else
                    {
                        ActivityCompat.requestPermissions(CallLogActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                        saveToTxtFile(mText);
                    }
                }else {
                    //do stuff
                    saveToTxtFile(mText);

                }
            }
        });


    }



    private void saveToTxtFile(String mText) {
        mText = textView.getText().toString();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(System.currentTimeMillis());
            try {
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path + "/My Files/");
                dir.mkdirs();
                String fileName = "CallLogs_" + timeStamp +".txt";
                File file = new File(dir,fileName);
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(mText);
                bw.close();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(mText.getBytes());
                fileOutputStream.close();
                textView.setText("");
                Toast.makeText(this, fileName+" is saved to \n"+dir, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this,"CallLogs_.txt saved to My Files", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case READ_CALL_LOG_CODE:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(CallLogActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(this, "Permission granted!!", Toast.LENGTH_SHORT).show();
                        //do stuff
                        textView = (TextView) findViewById(R.id.textView);
                        textView.setText(getCallsDetails());
                    }
                }else{
                    Toast.makeText(this, "No Permission granted!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case WRITE_EXTERNAL_STORAGE_CODE:
            {
                if (grantResults.length > 0 && grantResults[0]
                 == PackageManager.PERMISSION_GRANTED){
                    saveToTxtFile(mText);
                }else{
                    Toast.makeText(this, "No Permission granted!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private String getCallsDetails()
    {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,null,null,null,null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details: \n \n\n");
        while (managedCursor.moveToNext()){
            phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDateTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
            dateString = formatter.format(callDateTime);
            callDuration = managedCursor.getString(duration);
            dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode){
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED CALL";
                    break;
            }
            sb.append("\nPhone Number: " + phNumber + "\nCallType: " + dir + "\nCall Date: " + dateString +
                    "\nCall Duration: " + callDuration + " sec");
            sb.append("\n\n--------------------------------------");

        }
        managedCursor.close();
        return sb.toString();

    }

   /* public void writeExternalStorage(View view)
    {
        String state ;
        state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File path = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState());
            File Dir = new File(path +"/MyAppFile");
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            File file = new File(Dir,"MyCallLogs.txt");
            String Message = textView.getText().toString();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(Message.getBytes());
                fileOutputStream.close();
                textView.setText("");
                Toast.makeText(getApplicationContext(), "Message Saved", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else
        {
            Toast.makeText(this, "SD card not found", Toast.LENGTH_SHORT).show();
        }

    }  */

    private void save(String v)
   {
       mText = textView.getText().toString();
       FileOutputStream fos = null;
       String FILE_NAME = "Call_Logs";
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

/* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
                            String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
                        }else
                        {
                            saveToTxtFile(mText);
                        }
                    }else {
                        saveToTxtFile(mText);
                    }

                 */



