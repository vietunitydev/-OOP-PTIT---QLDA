package com.example.qlda.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlda.R;

import java.util.ArrayList;
import java.util.List;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.ListWorkHolder>{
    LayoutInflater inflater;

    List<WorkListPage> workListPages;

    public WorkListAdapter(LayoutInflater inflater, List<WorkListPage> workListPage) {
        this.inflater = inflater;
        this.workListPages = workListPage;
    }

    @NonNull
    @Override
    public ListWorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_list, parent, false);

        return new WorkListAdapter.ListWorkHolder(view, inflater,6);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWorkHolder holder, int position) {
        // hàm này dùng để set content cho cái adapter mà mình cần
        // ví dụ set image cho background
        // holder.imageView.setImageResource(imageList.get(position));

    }

    @Override
    public int getItemCount() {
        return workListPages.size();
    }

    public static class ListWorkHolder extends RecyclerView.ViewHolder {
        LayoutInflater inflater;

        int numberElement = 0;

        final LinearLayout wl_content;
        final Button addElement;
        List<FrameLayout> elements = new ArrayList<>();
        public ListWorkHolder(@NonNull View itemView, LayoutInflater inflater, int numberE) {
            super(itemView);
            this.inflater = inflater;
            this.wl_content = itemView.findViewById(R.id.wl_content);
            this.addElement = itemView.findViewById(R.id.btnAdd);
            this.numberElement = numberE;
            Init();
        }

        private void Init()
        {
            for (int i =0; i<numberElement; i++)
            {
                // tạo các element dựa vào số element của page này được truyền vào
                CreateElement(i, "have to custom content");
            }
        }

        private void CreateElement(int index, String cnt)
        {
            // create các element ở đây
            FrameLayout element = (FrameLayout) inflater.inflate(R.layout.item_worklist, wl_content, false);
            elements.add(element);
            TextView text = element.findViewById(R.id.item_drag_text);
            text.setText(cnt);

            wl_content.addView(element, index);
        }
    }
}
