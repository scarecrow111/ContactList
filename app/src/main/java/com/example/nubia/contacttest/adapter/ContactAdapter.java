package com.example.nubia.contacttest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nubia.contacttest.R;
import com.example.nubia.contacttest.bean.Contact;

import java.util.List;

/**
 * Created by nubia on 2017/6/14.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    private int resourceId;
    private List<Contact> mContactList;
    public ContactAdapter(Context context, int textViewResourceId, List<Contact> mContactList){
        super(context, textViewResourceId, mContactList);
        resourceId = textViewResourceId;
        this.mContactList = mContactList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contactName = (TextView)view.findViewById(R.id.display_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.contactName.setText(contact.getName());
        return view;
    }

    @Override
    public long getItemId(int position) {
        return mContactList.get(position).getId();
    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Contact getItem(int position) {
        return mContactList.get(position);
    }

    class ViewHolder{
        TextView contactName;
    }
}
