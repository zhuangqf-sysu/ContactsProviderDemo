package com.example.zhuangqf.contactsproviderdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        showContacts();
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            List<Map<String,Object>> contacts = getContactNames();
            MyListAdapter myListAdapter = new MyListAdapter(this,contacts);
            listView.setAdapter(myListAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Map<String,Object>> getContactNames() {

        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                Bitmap photo = null;

                Map<String,Object> listItem = new HashMap<String, Object>();
                listItem.put("name", name);

                if(photoId>0){
                    Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
                    photo = BitmapFactory.decodeStream(input);
                    listItem.put("header", photo);
                }else{
                    listItem.put("header", R.mipmap.contacts);
                }

                listItems.add(listItem);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return listItems;
    }
}
