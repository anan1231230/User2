package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.faxian.bean.StyleSelect;

import java.util.List;

/**
 * Created by hjm on 16/9/1.
 */

public class DrawerLayoutErjiAdapter extends RecyclerView.Adapter<DrawerLayoutErjiAdapter.DrawerLayoutViewHolder> {

    private List<String> datas;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private String title;

    public DrawerLayoutErjiAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClickListener(TextView name, ImageView right, int position, String title, String nameContent);
    }

    public void setDatas(List<String> datas, String title) {
        this.title = title;
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public DrawerLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DrawerLayoutViewHolder holder = new DrawerLayoutViewHolder(LayoutInflater.from(context).inflate(R.layout.item_drawer_erji_lv, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final DrawerLayoutViewHolder holder, final int position) {
        final String name = datas.get(position);
        if (StyleSelect.getInstence().isSelect(title, name)) {
            holder.nameTv.setTextColor(Color.parseColor("#df253d"));
            holder.rightIv.setVisibility(View.VISIBLE);
        } else {
            holder.nameTv.setTextColor(Color.parseColor("#626262"));
            holder.rightIv.setVisibility(View.GONE);
        }
        holder.nameTv.setText(name);
        holder.drawerItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(holder.nameTv, holder.rightIv, position, title, name);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class DrawerLayoutViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        ImageView rightIv;
        RelativeLayout drawerItemLayout;

        public DrawerLayoutViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            rightIv = (ImageView) itemView.findViewById(R.id.right_iv);
            drawerItemLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_item);
        }
    }
}
