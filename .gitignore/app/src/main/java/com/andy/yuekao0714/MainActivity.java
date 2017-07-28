package com.andy.yuekao0714;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.library.BaseActvity;
import com.example.library.http.MyCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActvity {
    @ViewInject(R.id.tablayout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    private List<TabData.DataBean> listTabs=new ArrayList<>();

    private final String tabUrl = "http://lf.snssdk.com/neihan/service/tabs/?essence=1&iid=3216590132&device_id=32613520945&ac=wifi&channel=360&aid=7&app_name=joke_essay&version_code=612&version_name=6.1.2&device_platform=android&ssmix=a&device_type=sansung&device_brand=xiaomi&os_api=28&os_version=6.10.1&uuid=326135942187625&openudid=3dg6s95rhg2a3dg5&manifest_version_code=612&resolution=1450*2800&dpi=620&update_version_code=6120";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadTabData();
    }
    private void initTabLayout() {
//        for (TabData.DataBean bean : listTabs){
//            mTabLayout.addTab(mTabLayout.newTab().setText(bean.getName()));
//        }
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MyFragment.getInstace("http://lf.snssdk.com/neihan/stream/mix/v1/?content_type=-103");
            }

            @Override
            public int getCount() {
                return listTabs.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return listTabs.get(position).getName();
            }
        });

        mTabLayout.setupWithViewPager(vp);
    }

    private void loadTabData() {
        http().get(tabUrl, new MyCallback<TabData>() {
            @Override
            public void success(TabData tabData) {
                listTabs.addAll(tabData.getData());
                initTabLayout();
            }

            @Override
            public void error(Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
