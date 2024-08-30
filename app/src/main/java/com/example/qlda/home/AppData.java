package com.example.qlda.home;

import com.example.qlda.R;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AppData {
    private List<Table> tables;

    public AppData() {
        tables = new ArrayList<>();
    }

    public List<Table> getTables() {
        return tables;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public void fetchData() {
        Table table = new Table(1, "Project Management", "FF5733");

        addTable(table);

        for (int i = 1; i <= 4; i++) {
            WorkListPage workListPage = new WorkListPage(i, "WorkList " + i);

            int numberOfElements = (int) (Math.random() * 4) + 3;

            for (int j = 1; j <= numberOfElements; j++) {
                Element element = new Element(
                        j,
                        "Element " + j,
                        "Description of Element " + j,
                        new Date(),
                        new Date()
                );
                workListPage.addElement(element);
            }

            table.addWorkListPage(workListPage);
        }
    }
}

class Table implements Serializable {
    private int id;
    private String tableName;
    private String color;
    private List<WorkListPage> workListPages;

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

class WorkListPage {
    private int id;
    private String workListName;
    private List<Element> elements;

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

class Element {
    private int id;
    private String elementName;
    private String description;
    private Date startDate;
    private Date endDate;

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
