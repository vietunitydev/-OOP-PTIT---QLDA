package com.example.qlda.home;

import com.example.qlda.Data.AppData;
import com.example.qlda.Data.ElementData;
import com.example.qlda.Data.TableData;
import com.example.qlda.Data.UserData;
import com.example.qlda.Data.WorkListPageData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreHelper {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void fetchAllData(OnCompleteListener<List<TableData>> listener) {

        // fetch nhung project ma nguoi nay quan ly thoi
        UserData user = AppData.myUserData;
        List<String> owned = user.getOwnedId();

        MyCustomLog.DebugLog("OwnID", owned.get(0));
        MyCustomLog.DebugLog("OwnID", "fetched");

        db.collection("tables")
                .whereIn(FieldPath.documentId(), owned)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TableData> tables = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            TableData table = document.toObject(TableData.class);
                            fetchWorkListPages(table, fetchedTable -> {
                                tables.add(fetchedTable);
                                if (tables.size() == queryDocumentSnapshots.size())
                                {
                                    MyCustomLog.DebugLog("Fetch ALl Data", AppData.convertToJson(tables));
                                    listener.onComplete(tables);
                                }
                });
            }
        }).addOnFailureListener(e -> {
            handleError("Error fetching tables", e);
            listener.onComplete(null);
        });
    }
    private void fetchWorkListPages(TableData table, OnCompleteListener<TableData> listener) {

        db.collection("tables").document(table.getId())
                .collection("workListPages").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        WorkListPageData page = document.toObject(WorkListPageData.class);
                        fetchElements(table, page, fetchedPage -> {
                            table.addWorkListPage(fetchedPage);
                            if (table.getWorkListPages().size() == queryDocumentSnapshots.size()) {
                                listener.onComplete(table); // All pages for this table fetched
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    handleError("Error fetching work list pages", e);
                    listener.onComplete(null);
                });
    }

    private void fetchElements(TableData table, WorkListPageData page, OnCompleteListener<WorkListPageData> listener) {
        db.collection("tables").document(table.getId())
                .collection("workListPages").document(page.getId())
                .collection("elements").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        ElementData element = document.toObject(ElementData.class);
                        page.addElement(element);
                    }
                    listener.onComplete(page);
                }).addOnFailureListener(e -> {
                    handleError("Error fetching elements", e);
                    listener.onComplete(null);
                });
    }

    public void uploadAllData(TableData table) {
        // Đẩy dữ liệu của Table
        saveTable(table, success -> {
            if (success) {
                // Sau khi lưu Table thành công, lưu các WorkListPage
                for (WorkListPageData page : table.getWorkListPages()) {
                    saveWorkListPage(table.getId(), page, pageSuccess -> {
                        if (pageSuccess) {
                            // Sau khi lưu WorkListPage thành công, lưu các Element
                            for (ElementData element : page.getElements()) {
                                saveElement(table.getId(), page.getId(), element);
                            }
                        }
                    });
                }
            }
        });
    }

    public String getNewIDTable() {
        return db.collection("tables").document().getId();
    }

    public String getNewIDPage(String idTable) {
        return db.collection("tables")
                .document(idTable)
                .collection("workListPages")
                .document().getId();
    }

    public String getNewIDElement(String idTable,String idPage) {
        MyCustomLog.DebugLog("Create New Element", "New Element 1");

        return db.collection("tables")
                .document(idTable)
                .collection("workListPages")
                .document(idPage)
                .collection("elements")
                .document().getId();
    }

    private void saveTable(TableData table, OnCompleteListener<Boolean> listener) {
        Map<String, Object> tableData = mapTableToData(table);
        db.collection("tables").document(table.getId())
                .set(tableData)
                .addOnSuccessListener(aVoid -> listener.onComplete(true))
                .addOnFailureListener(e -> {
                    handleError("Error saving table", e);
                    listener.onComplete(false);
                });
    }

    private void saveWorkListPage(String tableId, WorkListPageData page, OnCompleteListener<Boolean> listener) {
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

    private void saveElement(String tableId, String pageId, ElementData element) {
        Map<String, Object> elementData = mapElementToData(element);
        db.collection("tables").document(tableId)
                .collection("workListPages").document(pageId)
                .collection("elements").document(element.getId())
                .set(elementData)
                .addOnSuccessListener(aVoid -> logSuccess())
                .addOnFailureListener(e -> handleError("Error saving element", e));
    }

    public void saveUserToFirestore(UserData user) {
        Map<String, Object> userMap = convertUserToMap(user);

        db.collection("users")
                .document(user.getId())
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    //
                })
                .addOnFailureListener(e -> {
                    //
                });
    }
    public void getUserFromFirestore(String userId, OnCompleteListener<UserData> listener) {
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    UserData user = document.toObject(UserData.class);
                    listener.onComplete(user);
                }
                else {
                    listener.onComplete(null);
                }
            }
            else {
                listener.onComplete(null);
            }
        });
    }

    private Map<String, Object> convertUserToMap(UserData user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("displayName", user.getDisplayName());
        userMap.put("avatar", user.getAvatar());
        userMap.put("role", user.getRole());
        userMap.put("createAt", user.getCreateAt());
        userMap.put("updatedAt", user.getUpdatedAt());
        userMap.put("ownedId", user.getOwnedId());
        userMap.put("managedId", user.getManagedId());
        return userMap;
    }

    private Map<String, Object> mapTableToData(TableData table) {
        Map<String, Object> tableData = new HashMap<>();
        tableData.put("id", table.getId());
        tableData.put("title", table.getTitle());
        tableData.put("color", table.getColor());
        tableData.put("createdAt", table.getCreatedAt());
        tableData.put("updatedAt", table.getUpdatedAt());
        tableData.put("destroy", table.isDestroy());
        return tableData;
    }

    private Map<String, Object> mapWorkListPageToData(WorkListPageData page) {
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("id", page.getId());
        pageData.put("tableId", page.getTableId());
        pageData.put("title", page.getTitle());
        pageData.put("createdAt", page.getCreatedAt());
        pageData.put("updatedAt", page.getUpdatedAt());
        pageData.put("destroy", page.isDestroy());
        return pageData;
    }

    private Map<String, Object> mapElementToData(ElementData element) {
        Map<String, Object> elementData = new HashMap<>();
        elementData.put("id", element.getId());
        elementData.put("workListPageID", element.getWorkListPageID());
        elementData.put("tableID", element.getTableID());
        elementData.put("title", element.getTitle());
        elementData.put("description", element.getDescription());
        elementData.put("comments", element.getComments());
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


