package com.psx.logging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.androidnetworking.common.Priority;
import com.psx.logging.RealmDB.RealmLogs;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
                try (Realm realm = Realm.getInstance(Grove.getLibraryConfig())) {
                    RealmResults<RealmLogs> realmLogs = realm.where(RealmLogs.class).findAll();
                    Map<String, Object> additionalFields = new HashMap<>();
                    for (final RealmLogs log : realmLogs) {
                        additionalFields.clear();
                        additionalFields.put("timestamp", String.valueOf(log.timestamp));
                        additionalFields.put("classname", String.valueOf(log.className));
                        try {
                            Gelf.sendGelfMessage(log.message, additionalFields);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //uploadLogs(jsonArray);
                }
            }
        });
        thread.start();
    }

    private void uploadLogs(JSONArray jsonArray) {
        Rx2AndroidNetworking.post(Grove.uploadUrl)
                .addJSONArrayBody(jsonArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        // TODO : Parse the response from the server
                    }

                    @Override
                    public void onError(Throwable e) {
                        Grove.e(e.getMessage(), UploadWorker.class);
                    }

                    @Override
                    public void onComplete() {
                        deleteAllLogsFromRealm();
                    }
                });
    }

    private void deleteAllLogsFromRealm() {
        try (Realm realm = Realm.getInstance(Grove.getLibraryConfig())) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NotNull Realm realm) {
                    realm.where(RealmLogs.class).findAll().deleteAllFromRealm();
                }
            });
        }
    }
}
