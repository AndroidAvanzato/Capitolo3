package it.androidavanzato.transitions;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class DetailActivity extends ActionBarActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        final ImageView imageView = (ImageView) findViewById(R.id.detail_icon);
        int color = getIntent().getIntExtra("color", 0);
        imageView.setColorFilter(color);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && savedInstanceState == null) {
            executeWhenMeasured(imageView, new Runnable() {
                @Override public void run() {
                    animateEnter(imageView);
                }
            });
        }
    }

    private void animateEnter(ImageView imageView) {
        int x = getIntent().getIntExtra("x", 0);
        int y = getIntent().getIntExtra("y", 0);
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        ObjectAnimator imageX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, x - location[0], 0);
        ObjectAnimator imageY = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, y - location[1], 0);

        ObjectAnimator textAlpha = ObjectAnimator.ofFloat(findViewById(R.id.text), View.ALPHA, 0, 1);
        ObjectAnimator backgroundAlpha = ObjectAnimator.ofFloat(findViewById(R.id.detail_background), View.ALPHA, 0, 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(imageX, imageY, textAlpha, backgroundAlpha);
        animatorSet.start();
    }

    public static void executeWhenMeasured(final View view, final Runnable layoutListener) {
        if (view.getWidth() == 0 && view.getHeight() == 0) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layoutListener.run();
                    removeLayoutListener(view, this);
                }
            });
        } else {
            layoutListener.run();
        }
    }

    private static void removeLayoutListener(View cover, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        ViewTreeObserver obs = cover.getViewTreeObserver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            obs.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        } else {
            obs.removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
    }

}
