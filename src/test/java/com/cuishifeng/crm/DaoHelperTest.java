package com.cuishifeng.crm;

import junit.framework.TestCase;

public class DaoHelperTest extends TestCase {

    public void testCreateInstance() throws Exception {

        KeyValue keyValue = new KeyValue();
        keyValue.setKey("hello");

        DbQuery dbQuery = new DbQuery();
        dbQuery.select().from("db_demo").where().column("id").equal("123");

    }
}