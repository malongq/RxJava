package com.izk.rxjavafive;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Malong
 * on 19/1/10.
 */
public class MlAdapter extends RecyclerView.Adapter<MlAdapter.ViewHolder> {

    private final List<UserBean> bean;

    public MlAdapter(List<UserBean> userBeans) {
        this.bean = userBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserBean userBean = bean.get(position);
        holder.tv_item.setText(userBean.getReason());
    }

    @Override
    public int getItemCount() {
        return bean.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_item;
        public ViewHolder(View view) {
            super(view);
            tv_item = view.findViewById(R.id.tv_item);
        }
    }

}
