package com.hdweiss.morgand.orgdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "OrgNodes")
public class OrgNode {

    @DatabaseField(generatedId = true)
    public int Id;

    @DatabaseField
    public String name = "";

    public OrgNode() {
    }

    public String toString() {
        return name;
    }
}