package com.example.qlda.home;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FireStoreHelper {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void uploadAllData(Table table) {
        // Đẩy dữ liệu của Table
        saveTable(table, success -> {
            if (success) {
                // Sau khi lưu Table thành công, lưu các WorkListPage
                for (WorkListPage page : table.getWorkListPages()) {
                    saveWorkListPage(table.getId(), page, pageSuccess -> {
                        if (pageSuccess) {
                            // Sau khi lưu WorkListPage thành công, lưu các Element
                            for (Element element : page.getElements()) {
                                saveElement(table.getId(), page.getId(), element);
                            }
                        }
                    });
                }
            }
        });
    }

    private void saveTable(Table table, OnCompleteListener<Boolean> listener) {
        Map<String, Object> tableData = mapTableToData(table);
        db.collection("tables").document(table.getId())
                .set(tableData)
                .addOnSuccessListener(aVoid -> listener.onComplete(true))
                .addOnFailureListener(e -> {
                    handleError("Error saving table", e);
                    listener.onComplete(false);
                });
    }

    private void saveWorkListPage(String tableId, WorkListPage page, OnCompleteListener<Boolean> listener) {
        Map<String, Object> pageData = mapWorkListPageToData(page);
        db.collection("tables").document(tableId)
                .collection("workListPages").document(page.getId())
                .set(pageData)
                .addOnSuccessListener(aVoid -> listener.onComplete(true))
                .addOnFailureListener(e -> {
                    handleError("Error saving work list page", e);
                    listener.onComplete(false);
                });
    }

    private void saveElement(String tableId, String pageId, Element element) {
        Map<String, Object> elementData = mapElementToData(element);
        db.collection("tables").document(tableId)
                .collection("workListPages").document(pageId)
                .collection("elements").document(element.getId())
                .set(elementData)
                .addOnSuccessListener(aVoid -> logSuccess())
                .addOnFailureListener(e -> handleError("Error saving element", e));
    }

    private Map<String, Object> mapTableToData(Table table) {
        Map<String, Object> tableData = new HashMap<>();
        tableData.put("id", table.getId());
        tableData.put("title", table.getTitle());
        tableData.put("color", table.getColor());
        tableData.put("createdAt", table.getCreatedAt());
        tableData.put("updatedAt", table.getUpdatedAt());
        tableData.put("destroy", table.isDestroy());
        return tableData;
    }

    private Map<String, Object> mapWorkListPageToData(WorkListPage page) {
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("id", page.getId());
        pageData.put("title", page.getTitle());
        pageData.put("createdAt", page.getCreatedAt());
        pageData.put("updatedAt", page.getUpdatedAt());
        pageData.put("destroy", page.isDestroy());
        return pageData;
    }

    private Map<String, Object> mapElementToData(Element element) {
        Map<String, Object> elementData = new HashMap<>();
        elementData.put("id", element.getId());
        elementData.put("title", element.getTitle());
        elementData.put("description", element.getDescription());
        elementData.put("createdAt", element.getCreatedAt());
        elementData.put("updatedAt", element.getUpdatedAt());
        elementData.put("destroy", element.isDestroy());
        return elementData;
    }

    private void handleError(String message, Exception e) {
        System.out.println(message + ": " + e.getMessage());
    }

    private void logSuccess() {
        System.out.println("Element saved successfully");
    }

    public interface OnCompleteListener<T> {
        void onComplete(T result);
    }
}


