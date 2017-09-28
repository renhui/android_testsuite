package android_testsuite.application_search;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android_testsuite.R;

/**
 * @author Ren Hui
 * @since 1.0.1.058
 */
public class UidActivity extends Activity {
    private Button mButton_UidSearch;
    private Button mButton_UidList;
    private ListView mListView;
    private TextView mUidSame;
    private TextView mUidAll;
    private ArrayList<HashMap<String, Object>> mList;
    private ScanPackageOnDevice mScanPackageOnDevice;
    private ArrayList<AppInfo> mAppInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uid);
        mButton_UidSearch = (Button) findViewById(R.id.uidSearch);
        mListView = (ListView) findViewById(R.id.appList);
        mList = new ArrayList<>();
        mUidSame = (TextView) findViewById(R.id.explain_sameUid);
        mUidAll = (TextView) findViewById(R.id.explain_allUid);
        mButton_UidSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUidSame.setVisibility(View.VISIBLE);
                mUidAll.setVisibility(View.GONE);
                AppSearchTask appSearchTask = new AppSearchTask();
                appSearchTask.execute(true);
            }
        });

        mButton_UidList = (Button) findViewById(R.id.uidList);
        mButton_UidList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUidAll.setVisibility(View.VISIBLE);
                mUidSame.setVisibility(View.GONE);
                AppSearchTask appSearchTask = new AppSearchTask();
                appSearchTask.execute(false);
            }
        });
    }

    //相同uid检查
    private ArrayList<AppInfo> findSameUidApp() {
        ArrayList<AppInfo> resultList = new ArrayList<AppInfo>();
        Iterator it = mAppInfoList.iterator();
        boolean flag = true;
        AppInfo tmpLast = (AppInfo) it.next();
        AppInfo tmp = null;

        while (it.hasNext()) {
            tmp = (AppInfo) it.next();
            //判断是否有相同的uid，
            if (tmp.getUid() == tmpLast.getUid()) {
                if (flag) {
                    //同一uid首次遇到相同的，需加入上次的AppInfo
                    resultList.add(tmpLast);
                    //将标志置为false;
                    flag = false;
                }
                resultList.add(tmp);
            } else {
                tmpLast = tmp;
                //出现不同uid，标志置true
                flag = true;
            }
        }
        return resultList;
    }

    //显示结果
    public void displayApp(List<AppInfo> resList) {
        if(resList == null) {
            return ;
        }
        //清除上次显示的内容
        mList.clear();
        for(AppInfo info : resList) {
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("uid", info.getUid());
            tmp.put("pid",info.getPid());
            tmp.put("appName", info.getAppName());
            mList.add(tmp);
        }

        AppAdapter adapter = new AppAdapter(this, resList);
        mListView.setAdapter(adapter);
    }

    //排序方法
    public class MyComparator implements Comparator {
        @Override
        public int compare(Object app1, Object app2) {
            AppInfo a = (AppInfo) app1;
            AppInfo b = (AppInfo) app2;

            return (a.getUid() - b.getUid());
        }
    }

    //异步操作任务
    private class AppSearchTask extends AsyncTask<Boolean, Void, ArrayList<AppInfo>> {
        protected ArrayList<AppInfo> doInBackground(Boolean... booleans) {
            mScanPackageOnDevice = new ScanPackageOnDevice(UidActivity.this);
            mAppInfoList = mScanPackageOnDevice.getAppOnDevice();

            if (mAppInfoList.isEmpty()) {
                return null;
            }
            //排序
            Collections.sort(mAppInfoList, new MyComparator());

            if(booleans[0]) {
                //检查相同uid
                ArrayList<AppInfo> result = findSameUidApp();
                return result;
            } else {
                //遍历uid
                return mAppInfoList;
            }
        }

        //显示结果
        protected void onPostExecute(ArrayList<AppInfo> result) {
            displayApp(result);
        }
    }
}
