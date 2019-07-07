package com.psx.aftereffects;

import android.animation.Animator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;

public class AfterEffects {

    private static ActivityAwareness activityAwareness = null;
    private static final String TAG = AfterEffects.class.getSimpleName();

    public static void init(@NonNull ActivityAwareness activityAwareness) {
        AfterEffects.activityAwareness = activityAwareness;
    }

    public static void teardown() {
        activityAwareness = null;
    }

    public static void showAnimation(String operationPerformed) {
        if (activityAwareness == null) {
            Log.e(TAG, "Activity Awareness is null");
            return;
        }
        switch (operationPerformed) {
            case "ADDITION":
                startSuccessAnimation(R.layout.sucess_view_add);
                break;
            case "SUBTRACTION":
                startSuccessAnimation(R.layout.sucess_view_sub);
                break;
            case "MULTIPLICATION":
                startSuccessAnimation(R.layout.sucess_view_mult);
                break;
            case "DIVISION":
                startSuccessAnimation(R.layout.sucess_view_div);
                break;
        }
    }

    private static void startSuccessAnimation(int layoutID) {
        final FrameLayout rootView = activityAwareness.getCurrentActivity().findViewById(android.R.id.content);
        View v = View.inflate(activityAwareness.getCurrentActivity(), layoutID, rootView);
        v.bringToFront();
        final LottieAnimationView lottieAnimationView = v.findViewById(R.id.animation_view);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.removeAllAnimatorListeners();
                rootView.removeView(rootView.findViewById(R.id.success_view));
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
