package com.andy.yuekao0714;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.library.BaseActvity;
import com.example.library.image.NetImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_photo)
public class PhotoActivity extends BaseActvity {
    String [] imageUrls;

    @ViewInject(R.id.vp)
    ViewPager vp;

    private List<View> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        String str =getIntent().getStringExtra("images");
        imageUrls=str.split("###");

        for (String url : imageUrls){
            ImageView imageView=new ImageView(this);
//            imageView.setImageResource(R.mipmap.ic_launcher);
            NetImageLoader.getInstance().display(url,imageView);
            list.add(imageView);
        }

        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }
        });
    }
}
