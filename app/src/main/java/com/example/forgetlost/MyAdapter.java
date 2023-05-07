package com.example.forgetlost;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<HelperClassThings> dataList;

    public MyAdapter(Context context, List<HelperClassThings> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImgPath()).into(holder.listImage);
        holder.listName.setText(dataList.get(position).getName());
        holder.listDescribing.setText(dataList.get(position).getDescribing());
        holder.listData.setText(dataList.get(position).getData().substring( 0, dataList.get(position).getData().indexOf(",")));
        String idThing = dataList.get(position).getUserId();

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImgPath());
                intent.putExtra("idThing", idThing);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<HelperClassThings> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView listImage;
    TextView listName, listDescribing, listData;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        listImage = itemView.findViewById(R.id.listImage);
        recCard = itemView.findViewById(R.id.recCard);
        listDescribing = itemView.findViewById(R.id.listDescribing);
        listData = itemView.findViewById(R.id.listTime);
        listName = itemView.findViewById(R.id.listName);
    }
}