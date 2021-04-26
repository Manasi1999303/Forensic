package com.example.forensicproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
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
import java.security.Permission;
import java.sql.Date;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ContactsActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    TextView tv_phonebook;
    Button saveCantactText;
    String mText;
    private static final int WRITE_EXTERNAL_STORAGE_CODE  = 2;
    private static final int READ_CONTACTS_CODE  = 1;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);


        arrayList = new ArrayList<>();
        tv_phonebook = findViewById(R.id.tv_phonebook);
        saveCantactText = findViewById(R.id.save_contact_txt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        && checkSelfPermission(Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        else
        {
            getContact();
        }

        saveCantactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = tv_phonebook.getText().toString();
                //saveToTxtFile(mText);
                if (ContextCompat.checkSelfPermission(ContactsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(ContactsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                    }else
                    {
                        // ActivityCompat.requestPermissions(CallLogActivity.this,
                        // new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                        save(mText);
                    }
                }else {
                    //do stuff
                    saveToTxtFile(mText);
                }
            }
        });

    }

    private void getContact()
    {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);

        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            arrayList.add("Name: " + name + "\n" + "Mobile Number: " + mobile + "\n" + "--------------------------" + "\n");
            tv_phonebook.setText(arrayList.toString());

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
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

    private void saveToTxtFile(String mText) {
        mText = tv_phonebook.getText().toString();

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
            tv_phonebook.setText("");
            Toast.makeText(this, fileName+" is saved to \n"+dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void save(String v)
    {
        mText = tv_phonebook.getText().toString();
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