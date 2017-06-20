package com.example.nubia.contacttest.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nubia.contacttest.adapter.ContactAdapter;
import com.example.nubia.contacttest.R;
import com.example.nubia.contacttest.bean.Contact;
import com.example.nubia.contacttest.bean.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String URI = "content://com.example.nubia.contacttest.provider/contact";
    private static final int ADD_ACTIVITY_CODE = 1;
    private static final int EDIT_ACTIVITY_CODE = 2;
    private List<Contact> mContactList = new ArrayList<>();
    private ContactAdapter mContactAdapter;
    private Contact mContact;
    private AdapterView.AdapterContextMenuInfo mMenuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addContact = (FloatingActionButton)findViewById(R.id.add_contact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStart = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intentStart, ADD_ACTIVITY_CODE);
            }
        });
        ListView listView = (ListView)findViewById(R.id.list_view);
        registerForContextMenu(listView);
        mContactAdapter = new ContactAdapter(MainActivity.this, R.layout.contact_view, mContactList);
        listView.setAdapter(mContactAdapter);
        readContact();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.prompt);
        menu.add(Menu.NONE, R.id.menu_edit_contact, Menu.NONE, R.string.menuEditTitle);
        menu.add(Menu.NONE, R.id.menu_delete_contact, Menu.NONE, R.string.menuDeleteTitle);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = mMenuInfo.position;
        long editIdL = mContactAdapter.getItemId(pos);
        mContact = mContactAdapter.getItem(pos);
        switch (item.getItemId()){
            case R.id.menu_edit_contact:
                String editName = mContact.getName();
                String editNumber = mContact.getNumber();
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(DatabaseConstant.DB_ID, editIdL);
                intent.putExtra(DatabaseConstant.DB_NAME, editName);
                intent.putExtra(DatabaseConstant.DB_NUMBER, editNumber);
                startActivityForResult(intent, EDIT_ACTIVITY_CODE);
                break;
            case R.id.menu_delete_contact:
                deleteContactFromDB(editIdL);
                mContactList.remove(mContact);
                mContactAdapter.notifyDataSetChanged();
                break;
            default:
                return super.onContextItemSelected(item);
        }
       return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ADD_ACTIVITY_CODE:
                if (resultCode == RESULT_OK){
                    String name = data.getStringExtra(DatabaseConstant.DB_NAME);
                    String number = data.getStringExtra(DatabaseConstant.DB_NUMBER);
                    String idStr = data.getStringExtra(DatabaseConstant.DB_ID);
                    int id = Integer.parseInt(idStr);
                    mContact = new Contact();
                    mContact.setName(name);
                    mContact.setNumber(number);
                    mContact.setId(id);
                    mContactList.add(mContact);
                    mContactAdapter.notifyDataSetChanged();
                }
                break;
            case EDIT_ACTIVITY_CODE:
                if (resultCode == RESULT_OK){
                    String editName = data.getStringExtra(DatabaseConstant.DB_NAME);
                    String editNumber = data.getStringExtra(DatabaseConstant.DB_NUMBER);
                    int pos = mMenuInfo.position;
                    mContactAdapter.getItem(pos).setName(editName);
                    mContactAdapter.getItem(pos).setNumber(editNumber);
                    mContactAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    private void readContact(){
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(URI);
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null){
                while (cursor.moveToNext()){
                    String displayName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.DB_NAME));
                    String displayNumber = cursor.getString(cursor.getColumnIndex(DatabaseConstant.DB_NUMBER));
                    int displayId = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.DB_ID));
                    mContact = new Contact();
                    mContact.setId(displayId);
                    mContact.setName(displayName);
                    mContact.setNumber(displayNumber);
                    mContactList.add(mContact);
                }
                mContactAdapter.notifyDataSetChanged();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
    }

    private void deleteContactFromDB(long id){
        Uri uri = Uri.parse(URI + "/" + id);
        getContentResolver().delete(uri, null, null);
    }
}

