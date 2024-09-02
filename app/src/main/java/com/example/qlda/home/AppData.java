package com.example.qlda.home;

import com.example.qlda.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class AppData {
    public static List<Table> tables = new ArrayList<>();
    private static final FireStoreHelper firestoreHelper = new FireStoreHelper();

    public static List<Table> getTables() {
        return tables;
    }

    public static void addTable(Table table) {
        tables.add(table);
    }

    public static void SaveData() {
        MyCustomLog.DebugLog("FireBase Store", "Saving Data");
        for (Table table : tables) {
            firestoreHelper.saveTable(table);
        }
        MyCustomLog.DebugLog("FireBase Store", "Saved Data");
    }

    public static void FetchData(OnDataFetchedListener listener) {
        MyCustomLog.DebugLog("FireBase Store", "Fetching Data");

        firestoreHelper.fetchAllTables(new FireStoreHelper.OnCompleteListener<List<Table>>() {
            @Override
            public void onSuccess(List<Table> fetchedTables) {
                tables.clear();
                tables.addAll(fetchedTables);
                MyCustomLog.DebugLog("FireBase Store", "Fetched Data Successfully");

                if (listener != null) {
                    listener.onDataFetched();
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyCustomLog.DebugLog("FireBase Store", "Failed to Fetch Data: " + e.getMessage());
            }
        });
    }

    // Cập nhật Table theo id
    public static void updateTable(int id, String tableName, String color) {
        for (Table table : tables) {
            if (table.getId() == id) {
                table.setTableName(tableName);
                table.setColor(color);
//                firestoreHelper.saveTable(table); // Cập nhật trên Firestore
                break;
            }
        }
    }

    // Cập nhật WorkListPage theo id
    public static void updateWorkListPage(int tableId, int pageId, String workListName) {
        for (Table table : tables) {
            if (table.getId() == tableId) {
                for (WorkListPage page : table.getWorkListPages()) {
                    MyCustomLog.DebugLog("UpdateElement",String.format("page id %d count : %d name %s",pageId,page.getElements().size(),page.getWorkListName()));
                    if (page.getId() == pageId) {
                        page.setWorkListName(workListName);
//                        firestoreHelper.saveWorkListPage(tableId, page); // Cập nhật trên Firestore
                        break;
                    }
                }
                break;
            }
        }
    }

    // Cập nhật Element theo id
    public static void updateElement(int tableId, int pageId, int elementId, String elementName, String description, Date startDate, Date endDate) {
        for (Table table : tables) {
            if (table.getId() == tableId) {
//                MyCustomLog.DebugLog("UpdateElement",String.format("table id %d",tableId));
                for (WorkListPage page : table.getWorkListPages()) {
                    if (page.getId() == pageId) {
//                        MyCustomLog.DebugLog("UpdateElement",String.format("page id %d count : %d name %s",pageId,page.getElements().size(),page.getWorkListName()));
                        for (Element element : page.getElements()) {
//                            MyCustomLog.DebugLog("UpdateElement",String.format("element id %d",element));
                            if (element.getId() == elementId) {
                                element.setElementName(elementName);
                                element.setDescription(description);
                                element.setStartDate(startDate);
                                element.setEndDate(endDate);
//                                firestoreHelper.saveElement(tableId, pageId, element); // Cập nhật trên Firestore
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    // Interface để thông báo khi dữ liệu đã được lấy
    public interface OnDataFetchedListener {
        void onDataFetched();
    }

    public static <T> String convertToJson(T item) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(item);
    }
}

class Table implements Serializable {
    private int id;
    private String tableName;
    private String color;
    private final List<WorkListPage> workListPages;

    public Table(){
        this.workListPages = new ArrayList<>();
    }

    public Table(int id, String tableName, String color) {
        this.id = id;
        this.tableName = tableName;
        this.color = color;
        this.workListPages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<WorkListPage> getWorkListPages() {
        return workListPages;
    }

    public void addWorkListPage(WorkListPage workListPage) {
        workListPages.add(workListPage);
    }

    public int getRandomColor() {
        int[] colors = {
                R.color.blue,
                R.color.purple,
                R.color.pink,
                R.color.deep_purple
        };
        return colors[new Random().nextInt(colors.length)];
    }
}

class WorkListPage implements Serializable {
    private int id;
    private String workListName;
    private final List<Element> elements;

    public WorkListPage(){
        this.elements = new ArrayList<>();
    }

    public WorkListPage(int id, String workListName) {
        this.id = id;
        this.workListName = workListName;
        this.elements = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getWorkListName() {
        return workListName;
    }

    public void setWorkListName(String workListName) {
        this.workListName = workListName;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }
}

class Element implements Serializable {
    private int id;
    private String elementName;
    private String description;
    private Date startDate;
    private Date endDate;
    public Element(){

    }

    public Element(int id, String elementName, String description, Date startDate, Date endDate) {
        this.id = id;
        this.elementName = elementName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
