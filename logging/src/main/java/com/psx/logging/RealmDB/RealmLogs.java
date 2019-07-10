package com.psx.logging.RealmDB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmLogs extends RealmObject {

    @PrimaryKey
    public long timestamp;
    public String className;
    public String message;
}
