package comvoice.example.zhangbin.gridviewver;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyGridAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    ArrayList<AppInfo> listInfo;
    Context context;
    public MyGridAdapter(Context context, ArrayList<AppInfo> listInfo){
        //inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(context);
        //this.inflater = inflater;
        this.listInfo = listInfo;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listInfo.size();
    }
    @Override
    public Object getItem(int index) {
        // TODO Auto-generated method stub
        return listInfo.get(index);
    }
    @Override
    public long getItemId(int index) {
        // TODO Auto-generated method stub
        return index;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.gridview_layout,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView1);
            holder.textView = (TextView)convertView.findViewById(R.id.textView1);
            convertView.setTag(holder);
        }else{

            holder = (ViewHolder)convertView.getTag();
        }
        AppInfo appInfo = listInfo.get(position);
        holder.imageView.setImageResource(appInfo.rid);
        holder.textView.setText(appInfo.title);
        return convertView;
    }
    public class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
