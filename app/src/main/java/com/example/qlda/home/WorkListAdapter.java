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
        holder.imageView.setImageResource(imageList.get(position));

//        holder.drag.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                ClipData.Item item = new ClipData.Item(String.valueOf(position));
//                ClipData dragData = new ClipData("image_position", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
//
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
//                v.startDragAndDrop(dragData, shadowBuilder, v, 0);
//                return true;
//            }
//            return false;
//        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ListWorkHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final FrameLayout drag;
        public ListWorkHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.drag = itemView.findViewById(R.id.item_drag);
        }
    }
}
