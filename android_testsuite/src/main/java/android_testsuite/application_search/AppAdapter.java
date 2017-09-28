package android_testsuite.application_search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import android_testsuite.R;

/**
 * app列表适配器
 *
 * @author renhui
 */
public class AppAdapter extends BaseAdapter {
    private Context mContext;
    private List<AppInfo> mApps;

    public AppAdapter(Context context, List<AppInfo> apps) {
        mContext = context;
        mApps = apps;
    }

    @Override
    public int getCount() {
        return mApps.size();
    }

    @Override
    public Object getItem(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =  LinearLayout.inflate(mContext, R.layout.item_app_list, null);
        }
        ImageView appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
        TextView appName = (TextView) convertView.findViewById(R.id.app_name);

        appIcon.setBackgroundDrawable(mApps.get(position).getAppIcon());
        appName.setText(mApps.get(position).getPid());
        appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mApps.get(position).getAppName(), Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}
