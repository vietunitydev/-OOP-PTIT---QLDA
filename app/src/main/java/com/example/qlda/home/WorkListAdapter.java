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
import java.util.Date;
import java.util.List;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.ListWorkHolder> {
    private final LayoutInflater inflater;
    private final List<WorkListPage> workListPages;
    private final OnElementClickListener onElementClickListener;

    public WorkListAdapter(LayoutInflater inflater, List<WorkListPage> workListPage, OnElementClickListener onElementClickListener) {
        this.inflater = inflater;
        this.workListPages = workListPage;
        this.onElementClickListener = onElementClickListener;
    }

    @NonNull
    @Override
    public ListWorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_worklist, parent, false);
        return new ListWorkHolder(view, inflater, onElementClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWorkHolder holder, int position) {
        WorkListPage currentPage = workListPages.get(position);
        holder.Init(currentPage);
    }

    @Override
    public int getItemCount() {
        return workListPages.size();
    }

    public interface OnElementClickListener {
        void onElementClick(WorkListPage parent, Element e);
    }

    public static class ListWorkHolder extends RecyclerView.ViewHolder {
        private final LayoutInflater inflater;
        private final LinearLayout wl_content;
        private final LinearLayout wl_content_scroll;
        private final Button addElement;
        private final OnElementClickListener onElementClickListener;
        private final List<FrameLayout> elements = new ArrayList<>();

        private WorkListPage workListPage;

        public ListWorkHolder(@NonNull View itemView, LayoutInflater inflater, OnElementClickListener onElementClickListener) {
            super(itemView);
            this.inflater = inflater;
            this.wl_content = itemView.findViewById(R.id.wl_content);
            this.wl_content_scroll = itemView.findViewById(R.id.wl_content_scrollview);
            this.addElement = itemView.findViewById(R.id.wl_content_btnAdd);
            this.onElementClickListener = onElementClickListener;
        }

        private void Init(WorkListPage workListPage) {

            this.workListPage = workListPage;

            TextView text = wl_content.findViewById(R.id.wl_content_worklist_name);
            text.setText(workListPage.getWorkListName());

            List<Element> elms = workListPage.getElements();
            for (int i = 0; i < elms.size(); i++) {
                CreateElement(i, elms.get(i).getElementName());
            }

            addElement.setOnClickListener(v -> {
                // tạo 1 element data mới
                Element newElement = new Element(1,"New element","", new Date(), new Date() );
                workListPage.addElement(newElement);
                CreateElement(elements.size(), newElement.getElementName());
            });
        }

        private void CreateElement(int index, String cnt) {
            FrameLayout element = (FrameLayout) inflater.inflate(R.layout.item_worklist, wl_content, false);
            elements.add(element);
            TextView text = element.findViewById(R.id.item_drag_text);
            text.setText(cnt);

            Button btn = element.findViewById(R.id.item_drag_btn);
            btn.setOnClickListener(v -> {
                if (onElementClickListener != null) {
                    onElementClickListener.onElementClick(workListPage, workListPage.getElements().get(index));
                }
            });

            wl_content_scroll.addView(element, index);
        }

        public WorkListPage getWorkListPage() {
            return workListPage;
        }
    }
}
