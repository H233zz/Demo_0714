package com.andy.yuekao0714;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.App;
import com.example.library.adapter.CommAdapter;
import com.example.library.http.MyCallback;
import com.example.library.image.NetImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：yekh
 * 创建时间：2017/7/14 16:06
 */
@ContentView(R.layout.frag)
public class MyFragment extends Fragment{
    private String urlPath ;
    @ViewInject(R.id.lv)
    private ListView lv;
    private List<Data.DataBeanX.DataBean> list=new ArrayList<>();
    private MyAdapter adapter;
    public static MyFragment getInstace(String url){
        MyFragment fragment=new MyFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= x.view().inject(this,inflater,container);
        urlPath=getArguments().getString("url");
        adapter=new MyAdapter(getActivity(),list,R.layout.item);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data.DataBeanX.DataBean bean = list.get(position);
                Data.DataBeanX.DataBean.GroupBean.LargeImageBean images=bean.getGroup().large_image;
                if(images.url_list!=null&&images.url_list.size()>0){
                    StringBuffer sb=new StringBuffer();
                    for(int i=0;i<images.url_list.size();i++){
                        sb.append(images.url_list.get(i).url+"###");
                    }

                    Intent intent=new Intent(getActivity(),PhotoActivity.class);
                    intent.putExtra("images",sb.substring(0,sb.length()-3));
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "没有大图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadData();
        return view;
    }

    private void loadData() {
        App.getApp().http().get(urlPath, new MyCallback<Data>() {

            @Override
            public void success(Data data) {
                list.addAll(data.getData().getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error", "error: "+throwable.getMessage());
            }
        });
    }
    class MyAdapter extends CommAdapter<Data.DataBeanX.DataBean,MyAdapter.ViewHolder>{


        public MyAdapter(Context context, List<Data.DataBeanX.DataBean> list, int itemLayoutId) {
            super(context, list, itemLayoutId);
        }

        @Override
        public void setViewByHolder(ViewHolder viewHolder, Data.DataBeanX.DataBean dataBean) {
            if(dataBean.getComments().size()>0){
                viewHolder.name.setText(dataBean.getComments().get(0).text);
                NetImageLoader.getInstance().display(dataBean.getComments().get(0).user_profile_image_url,viewHolder.image);
            }
        }

        @Override
        public ViewHolder getViewHolder() {
            return new ViewHolder();
        }

        class ViewHolder{
            @ViewInject(R.id.name)
            TextView name;
            @ViewInject(R.id.image)
            ImageView image;
        }
    }
}
