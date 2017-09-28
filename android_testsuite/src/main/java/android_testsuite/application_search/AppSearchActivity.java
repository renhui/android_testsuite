package android_testsuite.application_search;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android_testsuite.R;

/**
 * @author Ren Hui
 * @since 1.0.1.058
 */
public class AppSearchActivity extends Activity {

    private Button mSearchBtn;
    private EditText mSearchName;
    private ListView mAppLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchapp);
        this.mSearchBtn = (Button) findViewById(R.id.bt_start);
        this.mSearchName = (EditText) findViewById(R.id.search_name);
        this.mAppLv = (ListView) findViewById(R.id.listView);

        mSearchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mSearchName.getText().toString();
                if (!name.isEmpty()) {
                    SearchTask searchTask = new SearchTask();
                    searchTask.execute(name);
                } else {
                    String err = getResources().getString(R.string.inputApp);
                    Toast.makeText(AppSearchActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SearchTask extends AsyncTask<String, Void, ArrayList<HashMap<String, Object>>> {
        protected ArrayList<HashMap<String, Object>> doInBackground(String... params) {
            ApplicationInfo applicationInfo;
            PackageManager packageManager = AppSearchActivity.this.getPackageManager();
            List<PackageInfo> pkgList = packageManager.getInstalledPackages(0);
            ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
            for (PackageInfo pkgInfo : pkgList) {
                // 进行软件名称的匹配
                try {
                    applicationInfo = packageManager.getApplicationInfo(pkgInfo.packageName, 0);
                    if (params[0].equals(packageManager.getApplicationLabel(applicationInfo))) {
                        HashMap<String, Object> tmp = new HashMap<String, Object>();
                        tmp.put("appName", params[0]);
                        tmp.put("packageId", applicationInfo.packageName);
                        tmp.put("appIcon", applicationInfo.loadIcon(packageManager));
                        result.add(tmp);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("AppSearchActivity", e.toString());
                    continue;
                }
            }
            return result;
        }

        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            if (result.isEmpty()) {
                String err = getResources().getString(R.string.noApp);
                Toast.makeText(AppSearchActivity.this, err, Toast.LENGTH_SHORT).show();
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(AppSearchActivity.this, result,
                R.layout.item, new String[]{"appName", "packageId", "appIcon"},
                new int[]{R.id.app_name, R.id.package_id, R.id.icon}
            );
            mAppLv.setAdapter(simpleAdapter);
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Drawable) {
                        ImageView iv = (ImageView) view;
                        iv.setImageDrawable((Drawable) data);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

}


