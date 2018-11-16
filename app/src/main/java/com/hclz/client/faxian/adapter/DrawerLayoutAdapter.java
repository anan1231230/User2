package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.faxian.bean.StyleSelect;

import java.util.List;
import java.util.Map;

/**
 * Created by hjm on 16/9/1.
 */

public class DrawerLayoutAdapter extends RecyclerView.Adapter<DrawerLayoutAdapter.DrawerLayoutViewHolder> {

    private List<String> datas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public DrawerLayoutAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public DrawerLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DrawerLayoutViewHolder holder = new DrawerLayoutViewHolder(LayoutInflater.from(context).inflate(R.layout.item_drawer_lv, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerLayoutViewHolder holder, final int position) {
        Map<String, String> mapSelectScreen = StyleSelect.getInstence().getSelectScreen();
        holder.nameTv.setText(datas.get(position));
        if (mapSelectScreen.containsKey(datas.get(position))) {
            holder.typeTv.setText(mapSelectScreen.get(datas.get(position)));
            holder.typeTv.setTextColor(Color.parseColor("#df253d"));
        } else {
            holder.typeTv.setText("全部");
            holder.typeTv.setTextColor(Color.parseColor("#626262"));
        }
        holder.drawerItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, position);
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

        TextView nameTv, typeTv;
        RelativeLayout drawerItemLayout;

        public DrawerLayoutViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            typeTv = (TextView) itemView.findViewById(R.id.type_tv);
            drawerItemLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_item);
        }
    }
}
