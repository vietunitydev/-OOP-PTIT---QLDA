package com.example.qlda.home;

import com.example.qlda.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class AppData {
    public List<Table> tables;

    public AppData() {
        tables = new ArrayList<>();
    }

    public List<Table> getTables() {
        return tables;
    }
    FireStoreHelper firestoreHelper = new FireStoreHelper();

    public void addTable(Table table) {
        tables.add(table);
    }

    public void SaveData(){
        MyCustomLog.DebugLog("FireBase Store", "Saving Data");
        for(Table table : tables){
            firestoreHelper.saveTable(table);
        }
        MyCustomLog.DebugLog("FireBase Store", "Saved Data");
    }

    public void FetchData(OnDataFetchedListener listener) {
        MyCustomLog.DebugLog("FireBase Store", "Fetching Data");

        firestoreHelper.fetchAllTables(new FireStoreHelper.OnCompleteListener<List<Table>>() {
            @Override
            public void onSuccess(List<Table> fetchedTables) {
                tables.clear();
                tables.addAll(fetchedTables);
                MyCustomLog.DebugLog("FireBase Store", "Fetched Data Successfully");

                for (Table table : tables) {
                    for (WorkListPage workListPage : table.getWorkListPages()) {
                        for (Element element : workListPage.getElements()) {
                            MyCustomLog.DebugLog("FireBase Store", "Element: " + element.getElementName());
                        }
                    }
                }

                if (listener != null) {
                    listener.onDataFetched();
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyCustomLog.DebugLog("FireBase Store", "Failed to Fetch Data: " + e.getMessage());
                // Xử lý lỗi nếu cần
            }
        });
    }

    // Interface để thông báo khi dữ liệu đã được lấy
    public interface OnDataFetchedListener {
        void onDataFetched();
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
