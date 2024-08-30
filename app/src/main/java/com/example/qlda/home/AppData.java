package com.example.qlda.home;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

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
    public void FetchData(){
        Table table = new Table("Project Management");

        addTable(table);

        for (int i = 1; i <= 4; i++) {
            WorkListPage workListPage = new WorkListPage("WorkList " + i);

            int numberOfElements = (int) (Math.random() * 4) + 3;

            for (int j = 1; j <= numberOfElements; j++) {
                Element element = new Element(
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

class Table {
    private String tableName;
    private List<WorkListPage> workListPages;

    public Table(String tableName) {
        this.tableName = tableName;
        this.workListPages = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<WorkListPage> getWorkListPages() {
        return workListPages;
    }

    public void addWorkListPage(WorkListPage workListPage) {
        workListPages.add(workListPage);
    }
}

class WorkListPage {
    private String workListName;
    private List<Element> elements;

    public WorkListPage(String workListName) {
        this.workListName = workListName;
        this.elements = new ArrayList<>();
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
    private String elementName;
    private String description;
    private Date startDate;
    private Date endDate;

    public Element(String elementName, String description, Date startDate, Date endDate) {
        this.elementName = elementName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
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
