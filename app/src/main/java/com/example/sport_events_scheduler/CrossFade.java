package com.example.sport_events_scheduler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class CrossFade {

    static void animate(View fadeInObj, View fadeOutObj, int duration) {

        /** Set the fadeIn view to 0% opacity but visible, so that it is visible
         (but fully transparent) during the animation. */
        fadeInObj.setAlpha(0f);
        fadeInObj.setVisibility(View.VISIBLE);

        /** Animate the fadeIn view to 100% opacity, and clear any animation
         listener set on the view. */
        fadeInObj.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null);

        /** Animate the fadeOut view to 0% opacity. After the animation ends,
         set its visibility to GONE as an optimization step (it won't
         participate in layout passes, etc.) */
        fadeOutObj.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fadeOutObj.setVisibility(View.GONE);
                    }
                });
    }
}
