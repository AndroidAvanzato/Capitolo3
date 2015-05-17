package it.androidavanzato.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private List<ImageView> images;
    private View mainLayout;
    private int scaledTouchSlop;
    private int minimumFlingVelocity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        minimumFlingVelocity = ViewConfiguration.get(this).getScaledMinimumFlingVelocity();
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);
        images = new ArrayList<ImageView>();
        images.add((ImageView) findViewById(R.id.item_5));
        images.add((ImageView) findViewById(R.id.item_4));
        images.add((ImageView) findViewById(R.id.item_3));
        images.add((ImageView) findViewById(R.id.item_2));
        images.add((ImageView) findViewById(R.id.item_1));
        for (ImageView image : images) {
            image.setVisibility(View.INVISIBLE);
        }
    }

    private VelocityTracker mVelocityTracker = null;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        private float startX;
        private float startTranslationX;
        private boolean dragging;
        private int pointerId;
        private float xVelocity;


        @Override public boolean onTouch(final View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            float rawX = event.getRawX();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    pointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        mVelocityTracker.clear();
                    }
                    mVelocityTracker.addMovement(event);

                    dragging = false;
                    startX = rawX;
                    startTranslationX = v.getTranslationX();
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    resetView(v, startTranslationX);
                    return false;
                case MotionEvent.ACTION_UP:
                    if (isSamePointerId(event)) {
                        return manageActionUp(v, rawX);
                    } else {
                        return false;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (isSamePointerId(event)) {
                        float newTranslationX = Math.max(startTranslationX, rawX - startX + startTranslationX);
                        event.offsetLocation(newTranslationX, 0);
                        mVelocityTracker.addMovement(event);

                        if (dragging || rawX - startX >= scaledTouchSlop) {
                            mVelocityTracker.computeCurrentVelocity(1000);
                            xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                                    pointerId);
                            dragging = true;
                            v.setTranslationX(newTranslationX);
                        }
                        return true;
                    } else {
                        return false;
                    }
                case MotionEvent.ACTION_POINTER_UP:
                    if (isSamePointerId(event)) {
                        return manageActionUp(v, rawX);
                    } else {
                        return false;
                    }
            }
            return false;
        }

        private boolean isSamePointerId(MotionEvent event) {
            return pointerId == event.getPointerId(MotionEventCompat.getActionIndex(event));
        }

        private boolean manageActionUp(final View v, float rawX) {
            float offset = rawX - startX;
            if (offset < scaledTouchSlop && !dragging) {
                return false;
            } else {
                if (xVelocity > minimumFlingVelocity) {
                    int mainLayoutWidth = mainLayout.getWidth();
                    float movement = mainLayoutWidth - (offset + startTranslationX);
                    int duration = (int) (movement * 1000 / xVelocity);
                    duration = Math.min(duration, 1500);
                    v.animate().translationX(mainLayoutWidth).setStartDelay(0).alpha(0.5f).setDuration(duration).setInterpolator(null)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override public void onAnimationEnd(Animator animation) {
                                    images.remove(v);
                                    animateAllViews();
                                }
                            });
                } else {
                    resetView(v, startTranslationX);
                }
                return true;
            }
        }
    };

    private View.OnTouchListener onTouchListenerMultiTouch = new View.OnTouchListener() {
        private float startX;
        private float startTranslationX;
        private boolean dragging;
        private int pointerId;


        @Override public boolean onTouch(final View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            float rawX = event.getRawX();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    pointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
                    dragging = false;
                    startX = rawX;
                    startTranslationX = v.getTranslationX();
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    resetView(v, startTranslationX);
                    return false;
                case MotionEvent.ACTION_UP:
                    if (isSamePointerId(event)) {
                        return manageActionUp(v, rawX);
                    } else {
                        return false;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (isSamePointerId(event)) {
                        if (dragging || rawX - startX >= scaledTouchSlop) {
                            dragging = true;
                            v.setTranslationX(Math.max(startTranslationX, rawX - startX + startTranslationX));
                        }
                        return true;
                    } else {
                        return false;
                    }
                case MotionEvent.ACTION_POINTER_UP:
                    if (isSamePointerId(event)) {
                        return manageActionUp(v, rawX);
                    } else {
                        return false;
                    }
            }
            return false;
        }

        private boolean isSamePointerId(MotionEvent event) {
            return pointerId == event.getPointerId(MotionEventCompat.getActionIndex(event));
        }

        private boolean manageActionUp(View v, float rawX) {
            float offset = rawX - startX;
            if (offset < scaledTouchSlop && !dragging) {
                return false;
            } else {
                if (offset + startTranslationX > mainLayout.getWidth() / 2) {
                    hideView(v);
                } else {
                    resetView(v, startTranslationX);
                }
                return true;
            }
        }

    };

    private View.OnTouchListener onTouchListenerSingleTouch = new View.OnTouchListener() {
        private float startX;
        private float startTranslationX;
        private boolean dragging;

        @Override public boolean onTouch(final View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dragging = false;
                    startX = event.getRawX();
                    startTranslationX = v.getTranslationX();
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    resetView(v, startTranslationX);
                    return false;
                case MotionEvent.ACTION_UP:
                    float offset = event.getRawX() - startX;
                    if (offset < scaledTouchSlop && !dragging) {
                        return false;
                    } else {
                        if (offset + startTranslationX > mainLayout.getWidth() / 2) {
                            hideView(v);
                        } else {
                            resetView(v, startTranslationX);
                        }
                        return true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (dragging || event.getRawX() - startX >= scaledTouchSlop) {
                        dragging = true;
                        v.setTranslationX(Math.max(startTranslationX, event.getRawX() - startX + startTranslationX));
                        return true;
                    } else {
                        return false;
                    }
            }
            return false;
        }

    };

    private void resetView(View v, float startTranslationX) {
        v.animate().translationX(startTranslationX).setDuration(800).setInterpolator(new BounceInterpolator());
    }

    private void hideView(final View v) {
        v.animate().translationX(mainLayout.getWidth()).alpha(0.5f).setDuration(400).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                images.remove(v);
                animateAllViews();
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
        }
    };


    public void start(View view) {
        int parentHeight = mainLayout.getHeight();
        for (ImageView image : images) {
            image.setY(parentHeight);
            image.setVisibility(View.VISIBLE);
            image.setOnTouchListener(onTouchListener);
            image.setOnClickListener(onClickListener);
            image.setTranslationX(0);
            image.setAlpha(1f);
        }

        animateAllViews();
    }

    private void animateAllViews() {
        int i = 0;
        for (ImageView image : images) {
            image.animate()
                    .translationY(-image.getHeight() * i)
                    .translationX(i * 20)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(400).start();
            i++;
        }
    }
}
