package com.example.forensicproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
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

public class NewContactsActivity extends AppCompatActivity {

    TextView textView;
    Button saveContactTextBtn;
    String mText;
    private static final int WRITE_EXTERNAL_STORAGE_CODE  = 2;
    private static final int READ_CONTACTS_CODE  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contacts);
        saveContactTextBtn = findViewById(R.id.save_sms_text_btn);

        if (ContextCompat.checkSelfPermission(NewContactsActivity.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewContactsActivity.this,
                    Manifest.permission.READ_CONTACTS)){
                ActivityCompat.requestPermissions(NewContactsActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},1);
            }else
            {
                ActivityCompat.requestPermissions(NewContactsActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},1);
            }
        }else{
            //do stuff
            textView = (TextView) findViewById(R.id.tv_phonebook);
            textView.setText(getContacts());
        }

        saveContactTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = textView.getText().toString();
                //saveToTxtFile(mText);
                if (ContextCompat.checkSelfPermission(NewContactsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewContactsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(NewContactsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                    }else
                    {
                        ActivityCompat.requestPermissions(NewContactsActivity.this,
                          new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                       // save(mText);
                    }
                }else {
                    //do stuff
                    saveToTxtFile(mText);
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case READ_CONTACTS_CODE:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(NewContactsActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(this, "Permission granted!!", Toast.LENGTH_SHORT).show();
                        //do stuff
                        textView = (TextView) findViewById(R.id.tv_phonebook);
                        textView.setText(getContacts());
                    }
                }else{
                    Toast.makeText(this, "No Permission granted!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case WRITE_EXTERNAL_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    saveToTxtFile(mText);
                } else {
                    Toast.makeText(this, "No Permission granted!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private String getContacts()
    {
        StringBuffer sb = new StringBuffer();
         Cursor managedCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
    //String name = managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    // String mobile = managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        sb.append("Contacts : \n");
        while (managedCursor.moveToNext()){

        String name = managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String mobile = managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        sb.append("\nName : " + name + "\nMobile Number: " + mobile + "\n");
        sb.append("\n--------------------------------------\n");

    }
        managedCursor.close();
        return sb.toString();
}

    private void saveToTxtFile(String mText) {
        mText = textView.getText().toString();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        try {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/My Files/");
            dir.mkdirs();
            String fileName = "Contacts_" + timeStamp +".txt";
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
            Toast.makeText(this, "Contacts_.txt saved to My Files", Toast.LENGTH_SHORT).show();
        }

    }

    private void save(String v)
    {
        mText = textView.getText().toString();
        FileOutputStream fos = null;
        String FILE_NAME = "Contacts_";
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