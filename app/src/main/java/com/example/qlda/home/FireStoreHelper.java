package com.example.qlda.home;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreHelper {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void saveTable(Table table) {
        Map<String, Object> tableData = new HashMap<>();
        tableData.put("id", table.getId());
        tableData.put("tableName", table.getTableName());
        tableData.put("color", table.getColor());

        // Lưu `Table`
        db.collection("tables").document(String.valueOf(table.getId()))
                .set(tableData)
                .addOnSuccessListener(aVoid -> {
                    // Sau khi lưu `Table`, lưu `WorkListPage`
                    for (WorkListPage page : table.getWorkListPages()) {
                        saveWorkListPage(table.getId(), page);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    System.out.println("Error saving table: " + e.getMessage());
                });
    }

    public void saveWorkListPage(int tableId, WorkListPage page) {
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("id", page.getId());
        pageData.put("workListName", page.getWorkListName());

        db.collection("tables").document(String.valueOf(tableId))
                .collection("workListPages").document(String.valueOf(page.getId()))
                .set(pageData)
                .addOnSuccessListener(aVoid -> {
                    // Sau khi lưu `WorkListPage`, lưu `Element`
                    for (Element element : page.getElements()) {
                        saveElement(tableId, page.getId(), element);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    System.out.println("Error saving work list page: " + e.getMessage());
                });
    }

    public void saveElement(int tableId, int pageId, Element element) {
        Map<String, Object> elementData = new HashMap<>();
        elementData.put("id", element.getId());
        elementData.put("elementName", element.getElementName());
        elementData.put("description", element.getDescription());
        elementData.put("startDate", element.getStartDate());
        elementData.put("endDate", element.getEndDate());

        db.collection("tables").document(String.valueOf(tableId))
                .collection("workListPages").document(String.valueOf(pageId))
                .collection("elements").document(String.valueOf(element.getId()))
                .set(elementData)
                .addOnSuccessListener(aVoid -> {
                    // Thành công
                    System.out.println("Element saved successfully");
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    System.out.println("Error saving element: " + e.getMessage());
                });
    }

    public void fetchAllTables(OnCompleteListener<List<Table>> listener) {
        db.collection("tables")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Table> tables = new ArrayList<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                        MyCustomLog.DebugLog("Firebase Store = Table Doc", document.toString());
                        Table table = document.toObject(Table.class);

                        // Fetch WorkListPages for each Table
                        fetchWorkListPages(table.getId(), table, new OnCompleteListener<Table>() {
                            @Override
                            public void onSuccess(Table tableWithPages) {
                                tables.add(tableWithPages);

                                // Check if all tables are fetched
                                if (tables.size() == queryDocumentSnapshots.size()) {
                                    listener.onSuccess(tables);
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                listener.onFailure(e);
                            }
                        });
                    }

                    if (queryDocumentSnapshots.isEmpty()) {
                        listener.onSuccess(tables); // Return empty list if no documents found
                    }

                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                });
    }

    private void fetchWorkListPages(int tableId, Table table, OnCompleteListener<Table> listener) {
        MyCustomLog.DebugLog("Firebase Store = Work List doc", "Fetching");
        db.collection("tables").document(String.valueOf(tableId))
                .collection("workListPages")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        MyCustomLog.DebugLog("Firebase Store = Work List doc", document.toString());
                        WorkListPage page = document.toObject(WorkListPage.class);
                        table.addWorkListPage(page);

                        // Fetch Elements for each WorkListPage
                        fetchElements(tableId, page);
                    }

                    listener.onSuccess(table);
                })
                .addOnFailureListener(e -> {
                    MyCustomLog.DebugLog("Firebase Store = Work List doc", "Failure");

                    listener.onFailure(e);
                });
    }

    private void fetchElements(int tableId, WorkListPage page) {
        MyCustomLog.DebugLog("Firebase Store = Element doc", "Fetching");

        db.collection("tables").document(String.valueOf(tableId))
                .collection("workListPages").document(String.valueOf(page.getId()))
                .collection("elements")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        MyCustomLog.DebugLog("Firebase Store = Element Doc", document.toString());
                        Element element = document.toObject(Element.class);
                        page.addElement(element);
                    }
                })
                .addOnFailureListener(e -> {
                    MyCustomLog.DebugLog("Firebase Store = Element doc", "Failure");
                });
    }

    public interface OnCompleteListener<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

}

