package com.example.qlda.home;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class AppData {
    private static final FireStoreHelper firestoreHelper = new FireStoreHelper();

    private List<TableData> tables;

    public List<TableData> getTables(){
        return tables;
    }

//    public static void SaveData() {
//        MyCustomLog.DebugLog("FireBase Store", "Saving Data");
//        for (TableData table : tables) {
//            firestoreHelper.saveTable(table);
//        }
//        MyCustomLog.DebugLog("FireBase Store", "Saved Data");
//    }

    public void fetchData(OnDataFetchedListener listener) {
        MyCustomLog.DebugLog("FireBase Store", "Fetching Data");

        firestoreHelper.fetchAllData(new FireStoreHelper.OnCompleteListener<List<TableData>>() {
            @Override
            public void onComplete(List<TableData> fetchedTables) {
                if(fetchedTables == null){
                    return;
                }
                tables.clear();
                tables.addAll(fetchedTables);
                MyCustomLog.DebugLog("FireBase Store", "Fetched Data Successfully");

                if (listener != null) {
                    listener.onDataFetched();
                }
            }
        });
    }

    public void InitTable(){

        tables = new ArrayList<>();

        // Table 1
        TableData table1 = new TableData("table-id-01", "Project A", "#FF5733", new Date());

        WorkListPageData workListPage1 = new WorkListPageData("worklist-id-01", "table-id-01", "Design Phase", new Date());
        workListPage1.addElement(new ElementData("element-id-01", "worklist-id-01", "table-id-01", "Design Phase Task 1", "Description for Design Phase task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage1.addElement(new ElementData("element-id-02", "worklist-id-01", "table-id-01", "Design Phase Task 2", "Description for Design Phase task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage1.addElement(new ElementData("element-id-03", "worklist-id-01", "table-id-01", "Design Phase Task 3", "Description for Design Phase task 3", new ArrayList<>(), new Date(), new Date(), false));

        WorkListPageData workListPage2 = new WorkListPageData("worklist-id-02", "table-id-01", "Development Phase", new Date());
        workListPage2.addElement(new ElementData("element-id-04", "worklist-id-02", "table-id-01", "Development Task 1", "Description for development task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage2.addElement(new ElementData("element-id-05", "worklist-id-02", "table-id-01", "Development Task 2", "Description for development task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage2.addElement(new ElementData("element-id-06", "worklist-id-02", "table-id-01", "Development Task 3", "Description for development task 3", new ArrayList<>(), new Date(), new Date(), false));
        workListPage2.addElement(new ElementData("element-id-07", "worklist-id-02", "table-id-01", "Development Task 4", "Description for development task 4", new ArrayList<>(), new Date(), new Date(), false));

        table1.addWorkListPage(workListPage1);
        table1.addWorkListPage(workListPage2);

        // Table 2
        TableData table2 = new TableData("table-id-02", "Project B", "#33FF57", new Date());

        WorkListPageData workListPage3 = new WorkListPageData("worklist-id-03", "table-id-02", "Research Phase", new Date());
        workListPage3.addElement(new ElementData("element-id-08", "worklist-id-03", "table-id-02", "Research Task 1", "Description for research task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage3.addElement(new ElementData("element-id-09", "worklist-id-03", "table-id-02", "Research Task 2", "Description for research task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage3.addElement(new ElementData("element-id-10", "worklist-id-03", "table-id-02", "Research Task 3", "Description for research task 3", new ArrayList<>(), new Date(), new Date(), false));

        WorkListPageData workListPage4 = new WorkListPageData("worklist-id-04", "table-id-02", "Implementation Phase", new Date());
        workListPage4.addElement(new ElementData("element-id-11", "worklist-id-04", "table-id-02", "Implementation Task 1", "Description for implementation task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage4.addElement(new ElementData("element-id-12", "worklist-id-04", "table-id-02", "Implementation Task 2", "Description for implementation task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage4.addElement(new ElementData("element-id-13", "worklist-id-04", "table-id-02", "Implementation Task 3", "Description for implementation task 3", new ArrayList<>(), new Date(), new Date(), false));

        WorkListPageData workListPage5 = new WorkListPageData("worklist-id-05", "table-id-02", "Testing Phase", new Date());
        workListPage5.addElement(new ElementData("element-id-14", "worklist-id-05", "table-id-02", "Testing Task 1", "Description for testing task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage5.addElement(new ElementData("element-id-15", "worklist-id-05", "table-id-02", "Testing Task 2", "Description for testing task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage5.addElement(new ElementData("element-id-16", "worklist-id-05", "table-id-02", "Testing Task 3", "Description for testing task 3", new ArrayList<>(), new Date(), new Date(), false));
        workListPage5.addElement(new ElementData("element-id-17", "worklist-id-05", "table-id-02", "Testing Task 4", "Description for testing task 4", new ArrayList<>(), new Date(), new Date(), false));

        table2.addWorkListPage(workListPage3);
        table2.addWorkListPage(workListPage4);
        table2.addWorkListPage(workListPage5);

        // Table 3
        TableData table3 = new TableData("table-id-03", "Project C", "#3357FF", new Date());

        WorkListPageData workListPage6 = new WorkListPageData("worklist-id-06", "table-id-03", "Planning Phase", new Date());
        workListPage6.addElement(new ElementData("element-id-18", "worklist-id-06", "table-id-03", "Planning Task 1", "Description for planning task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage6.addElement(new ElementData("element-id-19", "worklist-id-06", "table-id-03", "Planning Task 2", "Description for planning task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage6.addElement(new ElementData("element-id-20", "worklist-id-06", "table-id-03", "Planning Task 3", "Description for planning task 3", new ArrayList<>(), new Date(), new Date(), false));

        WorkListPageData workListPage7 = new WorkListPageData("worklist-id-07", "table-id-03", "Execution Phase", new Date());
        workListPage7.addElement(new ElementData("element-id-21", "worklist-id-07", "table-id-03", "Execution Task 1", "Description for execution task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage7.addElement(new ElementData("element-id-22", "worklist-id-07", "table-id-03", "Execution Task 2", "Description for execution task 2", new ArrayList<>(), new Date(), new Date(), false));
        workListPage7.addElement(new ElementData("element-id-23", "worklist-id-07", "table-id-03", "Execution Task 3", "Description for execution task 3", new ArrayList<>(), new Date(), new Date(), false));

        WorkListPageData workListPage8 = new WorkListPageData("worklist-id-08", "table-id-03", "Review Phase", new Date());
        workListPage8.addElement(new ElementData("element-id-24", "worklist-id-08", "table-id-03", "Review Task 1", "Description for review task 1", new ArrayList<>(), new Date(), new Date(), false));
        workListPage8.addElement(new ElementData("element-id-25", "worklist-id-08", "table-id-03", "Review Task 2", "Description for review task 2", new ArrayList<>(), new Date(), new Date(), false));

        table3.addWorkListPage(workListPage6);
        table3.addWorkListPage(workListPage7);
        table3.addWorkListPage(workListPage8);

        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
    }

    public void uploadDataToServer() {
        if (tables != null && !tables.isEmpty()) {
            for (TableData table : tables) {
                firestoreHelper.uploadAllData(table);
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
