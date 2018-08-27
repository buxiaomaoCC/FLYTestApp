package comvoice.example.zhangbin.liveapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbin on 2018/6/6.
 */

public class LiveAdapter extends BaseAdapter {
    private Context context;
    private List<String>mDataList;
    public LiveAdapter(Context context,List<String>mDataList){
        this.context=context;
       this.mDataList=mDataList;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.liveitem,null);
            viewHolder=new ViewHolder();
            viewHolder.textView=view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(mDataList.get(i));
        return view;
    }

    class ViewHolder{
        private TextView textView;
    }
}
