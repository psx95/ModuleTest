package com.psx.logging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.psx.logging.RealmDB.RealmLogs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class UploadWorker extends Worker {

    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NotNull
    @Override
    public Result doWork() {
        // Do the work here--in this case, fetch logs from DB and upload logs.
        JSONArray jsonArray = fetchLogs();
        // Indicate whether the task finished successfully with the Result
        return uploadLogs(jsonArray);
    }

    private JSONArray fetchLogs() {
        final JSONArray jsonArray = new JSONArray();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<RealmLogs> realmLogs = realm.where(RealmLogs.class).findAllAsync();
            realmLogs.addChangeListener(new RealmChangeListener<RealmResults<RealmLogs>>() {
                @Override
                public void onChange(@NotNull RealmResults<RealmLogs> realmLogs) {
                    if (realmLogs.isLoaded()) {
                        // LOGS Loading is successul, upload
                        for (RealmLogs logs : realmLogs) {
                            JSONObject jsonObject = convertRealmLogToJsonObject(logs);
                            jsonArray.put(jsonObject);
                        }
                    }
                }
            });
        }
        return jsonArray;
    }

    //TODO :  Write method to convert
    private JSONObject convertRealmLogToJsonObject(RealmLogs realmLogs) {
        return new JSONObject();
    }

    //TODO : Write method to upload Logs
    private Result uploadLogs(JSONArray jsonArray) {
        Result result = Result.success();
        // TODO: Make it conditional, based on API Failure
        boolean isSuccess = true;
        if (isSuccess) {
            deleteAllLogsFromRealm();
        }
        return Result.success();
    }

    private void deleteAllLogsFromRealm() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NotNull Realm realm) {
                    realm.where(RealmLogs.class).findAll().deleteAllFromRealm();
                }
            });
        }
    }
}
