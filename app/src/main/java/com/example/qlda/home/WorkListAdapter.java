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

        return new WorkListAdapter.ListWorkHolder(view, inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWorkHolder holder, int position) {
        // hàm này dùng để set content cho cái adapter mà mình cần
        // ví dụ set image cho background
        // holder.imageView.setImageResource(imageList.get(position));
        WorkListPage currentPage = workListPages.get(position);
        // Truyền WorkListPage hiện tại vào holder
        holder.Init(currentPage);
    }

    @Override
    public int getItemCount() {
        return workListPages.size();
    }

    public static class ListWorkHolder extends RecyclerView.ViewHolder {
        LayoutInflater inflater;
        final LinearLayout wl_content;
        final Button addElement;
        List<FrameLayout> elements = new ArrayList<>();
        public ListWorkHolder(@NonNull View itemView, LayoutInflater inflater) {
            super(itemView);
            this.inflater = inflater;

            this.wl_content = itemView.findViewById(R.id.wl_content);
            this.addElement = itemView.findViewById(R.id.wl_content_btnAdd);
        }

        private void Init(WorkListPage workListPage)
        {
            List<Element> elms = workListPage.getElements();
            for (int i =0; i< elms.size(); i++)
            {
                // tạo các element dựa vào số element của page này được truyền vào
                CreateElement(i, elms.get(i).getElementName());
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
