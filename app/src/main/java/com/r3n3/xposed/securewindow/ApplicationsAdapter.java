package com.r3n3.xposed.securewindow;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.random.xposed.securewindow.R;

import java.util.List;

/**
 * Created by rene on 7/6/14.
 */
public class ApplicationsAdapter extends ArrayAdapter<PreferencesModel> {
    static class ApplicationHolder {
        ImageView imgIcon;
        TextView txtTitle;
        CheckBox chkChecked;
    }

    Context context;
    int layoutResourceId;
    List<PreferencesModel> applications = null;

    public ApplicationsAdapter(Context context, int resourceId,
                               List<PreferencesModel> applications) {

        super(context, resourceId, applications);


        this.applications = applications;
        this.context = context;
        this.layoutResourceId = resourceId;
        this.applications = applications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ApplicationHolder holder = null;
        PreferencesModel model = applications.get(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ApplicationHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.list_image);
            holder.txtTitle = (TextView) row.findViewById(R.id.title);
            holder.chkChecked = (CheckBox) row.findViewById(R.id.checked);
            row.setTag(holder);
        } else {
            holder = (ApplicationHolder) row.getTag();
        }
        PackageManager pm = context.getPackageManager();

        holder.txtTitle.setText(model.getAppInfo().loadLabel(pm));
        holder.imgIcon.setImageDrawable(model.getAppInfo().loadIcon(pm));
        holder.chkChecked.setChecked(model.isChecked());
        holder.chkChecked.setTag(position);

        holder.chkChecked.setOnClickListener(new CompoundButton.OnClickListener() {

            public void onClick(View view) {
                Integer position = (Integer)view.getTag();
                applications.get(position).setChecked( ((CheckBox)view).isChecked() );
            }
            /*
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                applications.get((Integer) compoundButton.getTag() ).setChecked(isChecked);
                //PreferencesModel model = ((PreferencesModel) compoundButton.getTag());
                //model.setChecked(isChecked);
            }*/
        });
        return row;
    }
}
