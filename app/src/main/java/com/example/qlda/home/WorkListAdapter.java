package com.example.qlda.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlda.R;

import java.util.List;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.ListWorkHolder>{

    private final List<Integer> imageList;

    public WorkListAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ListWorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_list, parent, false);
        return new WorkListAdapter.ListWorkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWorkHolder holder, int position) {
        // hàm này dùng để set content cho cái adapter mà mình cần
        // ví dụ set image cho background
        // holder.imageView.setImageResource(imageList.get(position));

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ListWorkHolder extends RecyclerView.ViewHolder {
        final FrameLayout drag;
        public ListWorkHolder(@NonNull View itemView) {
            super(itemView);
            this.drag = itemView.findViewById(R.id.item_drag);
        }
    }
}
