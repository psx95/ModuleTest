package com.psx.aftereffects;

import android.animation.Animator;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.psx.commons.ExchangeObject;
import com.psx.commons.MainApplication;
import com.psx.commons.Modules;
import com.psx.logging.Grove;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AfterEffects {

    private static MainApplication mainApplication = null;
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void init(@NonNull MainApplication applicationInstance) {
        AfterEffects.mainApplication = applicationInstance;
        compositeDisposable.add(applicationInstance.getEventBus()
                .toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) {
                        if (object instanceof ExchangeObject) {
                            ExchangeObject exchangeObject = (ExchangeObject) object;
                            if (exchangeObject.to == Modules.AFTER_EFFECTS) {
                                if (exchangeObject.from == Modules.SIMPLE_MATHS) {
                                    showAnimation((String) exchangeObject.data[0]);
                                }
                            }
                        }
                    }
                }));
    }

    public static void teardown() {
        mainApplication = null;
        compositeDisposable.clear();
        Grove.d("After Effects Teardown", AfterEffects.class);
    }

    private static void showAnimation(String operationPerformed) {
        if (mainApplication == null) {
            Grove.e("Activity Awareness is null", AfterEffects.class);
            return;
        }
        switch (operationPerformed) {
            case "ADDITION":
                Grove.e("AFTER effects for addition", AfterEffects.class);
                startSuccessAnimation(R.layout.sucess_view_add);
                break;
            case "SUBTRACTION":
                Grove.e("AFTER effects for subtraction", AfterEffects.class);
                startSuccessAnimation(R.layout.sucess_view_sub);
                break;
            case "MULTIPLICATION":
                Grove.d("AFTER effects for deletion", AfterEffects.class);
                startSuccessAnimation(R.layout.sucess_view_mult);
                break;
            case "DIVISION":
                startSuccessAnimation(R.layout.sucess_view_div);
                break;
        }
    }

    private static void startSuccessAnimation(int layoutID) {
        final FrameLayout rootView = mainApplication.getCurrentActivity().findViewById(android.R.id.content);
        final View[] v = {View.inflate(mainApplication.getCurrentActivity(), layoutID, rootView)};
        v[0].bringToFront();
        final LottieAnimationView lottieAnimationView = v[0].findViewById(R.id.animation_view);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.removeAnimatorListener(this);
                rootView.removeView(rootView.findViewById(R.id.success_view));
                v[0] = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
