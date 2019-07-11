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
import io.realm.RealmResults;
import timber.log.Timber;

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
        // Indicate whether the task finished successfully with the Result
        Timber.d("Doing WORK");
        fetchAndUploadLogs();
        return Result.success();
    }

    private void fetchAndUploadLogs() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Realm realm = Realm.getInstance(MyLog.getLibraryConfig())) {
                    RealmResults<RealmLogs> realmLogs = realm.where(RealmLogs.class).findAll();
                    final JSONArray jsonArray = new JSONArray();
                    for (RealmLogs logs : realmLogs) {
                        JSONObject jsonObject = convertRealmLogToJsonObject(logs);
                        jsonArray.put(jsonObject);
                    }
                    uploadLogs(jsonArray);
                }
            }
        });
        thread.start();
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
        try (Realm realm = Realm.getInstance(MyLog.getLibraryConfig())) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NotNull Realm realm) {
                    realm.where(RealmLogs.class).findAll().deleteAllFromRealm();
                }
            });
        }
    }
}
