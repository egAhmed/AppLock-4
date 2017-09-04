package com.example.shashvatkedia.lock;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.media.MediaFormat.KEY_HEIGHT;
import static android.provider.Contacts.SettingsColumns.KEY;
//import static com.example.shashvatkedia.lock.DataBase.findInfo;

/**
 * Created by Shashvat Kedia on 27-08-2017.
 */

public class ApplicationAdapter extends ArrayAdapter<Row> {
    static PackageManager p;
    static Context con;
    ArrayList<Row> selected_apps=new ArrayList<Row>();
    ArrayList<Row> list=new ArrayList<Row>();
    public ApplicationAdapter(Context a, ArrayList<Row> info, PackageManager pm){
        super(a,0,info);
        con=a;
        list=info;
        p=a.getPackageManager();
    }
    DataBase data=DataBase.getInstance(con);
    ViewHolder hold=null;
    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        int vel=data.findInfo(getItem(position).getInfo().packageName);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.appsdisplay,parent,false);
            hold=new ViewHolder();
            hold.checked_state=convertView.findViewById(R.id.selected_state);
            hold.name=convertView.findViewById(R.id.AppName);
            hold.icon=convertView.findViewById(R.id.icon1);
            hold.packg=convertView.findViewById(R.id.package_name);
            if(vel==1){
                hold.checked_state.setSelected(true);
                list.get(position).setSelected(true);
            }
            else{
                hold.checked_state.setSelected(false);
                list.get(position).setSelected(false);
            }
            hold.checked_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    int getPosition=(Integer)buttonView.getTag();
                    buttonView.setSelected(buttonView.isChecked());
                    list.get(getPosition).setSelected(buttonView.isChecked());
                }
            });
            convertView.setTag(hold);
            convertView.setTag(R.id.selected_state,hold.checked_state);
            convertView.setTag(R.id.AppName,hold.name);
            convertView.setTag(R.id.icon1,hold.icon);
            convertView.setTag(R.id.package_name,hold.packg);
        }
        else{
            hold=(ViewHolder) convertView.getTag();
        }
        hold.checked_state.setTag(position);
        final Row pck=getItem(position);
        hold.name.setText(pck.getInfo().loadLabel(p));
        hold.packg.setText(pck.getInfo().packageName);
        try {
            Drawable icon1 = p.getApplicationIcon(pck.getInfo().packageName);
            hold.icon.setImageDrawable(icon1);
        }
        catch(PackageManager.NameNotFoundException e) {
            Log.e("#", "NameNotFound Error");
        }
        if(vel==1){
            hold.checked_state.setSelected(true);
            list.get(position).setSelected(true);
        }
        else{
            hold.checked_state.setSelected(false);
            list.get(position).setSelected(false);
        }
        hold.checked_state.setChecked(list.get(position).isSelected());
        return convertView;
    }
}

