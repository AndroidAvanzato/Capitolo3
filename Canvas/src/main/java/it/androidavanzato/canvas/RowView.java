package it.androidavanzato.canvas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RowView extends View {

    private Paint titlePaint;
    private Paint subtitlePaint;
    private Paint imagePaint;
    private final int padding;
    private final int imageTopMargin;
    private final int titleTopMargin;
    private final int subTitleTopMargin;
    private final int textLeftMargin;
    private final int starTopMargin;
    private Item item;
    private Drawable starDrawable;
    private Rect starDrawableRect;

    public RowView(Context context) {
        this(context, null);
    }

    public RowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        titlePaint = createTextPaint(R.dimen.title_text_size);
        subtitlePaint = createTextPaint(R.dimen.subtitle_text_size);

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);

        Resources resources = getResources();
        padding = resources.getDimensionPixelSize(R.dimen.root_layout_padding);
        imageTopMargin = resources.getDimensionPixelSize(R.dimen.image_top_margin);
        titleTopMargin = resources.getDimensionPixelSize(R.dimen.title_top_margin);
        subTitleTopMargin = resources.getDimensionPixelSize(R.dimen.subtitle_top_margin);
        textLeftMargin = resources.getDimensionPixelSize(R.dimen.text_left_margin);
        starTopMargin = resources.getDimensionPixelSize(R.dimen.star_top_margin);

        starDrawable = resources.getDrawable(R.drawable.star);

        starDrawable.setBounds(0, 0, starDrawable.getIntrinsicWidth(), starDrawable.getIntrinsicHeight());

        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                item.toggleStarred();
                updateStarDrawableState(false);
                return false;
            }
        });

        final int[] location = new int[2];
        setOnTouchListener(new OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                getLocationOnScreen(location);
                if (starDrawableRect.contains((int) event.getRawX(), (int) event.getRawY() - location[1])) {
                    gestureDetector.onTouchEvent(event);
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            updateStarDrawableState(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            updateStarDrawableState(false);
                            break;
                    }
                    return true;
                } else {
                    updateStarDrawableState(false);
                    return false;
                }
            }
        });
    }

    private Paint createTextPaint(int textSize) {
        Resources resources = getResources();
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(resources.getDimension(textSize));
        titlePaint.setColor(resources.getColor(R.color.text_color));
        titlePaint.setAntiAlias(true);
        return titlePaint;
    }

    private void updateStarDrawableState(boolean pressed) {
        if (pressed) {
            starDrawable.setState(new int[]{android.R.attr.state_pressed});
        } else {
            starDrawable.setState(item.isStarred() ? new int[]{android.R.attr.state_selected} : new int[0]);
        }
        invalidate();
    }

    public void populate(Item item) {
        this.item = item;
        updateStarDrawableState(false);
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas.drawText(item.getTitle(), textLeftMargin, titleTopMargin, titlePaint);
        canvas.drawText(item.getSubtitle(), textLeftMargin, subTitleTopMargin, subtitlePaint);
        canvas.drawBitmap(item.getBitmap(), padding, imageTopMargin, imagePaint);
        if (starDrawableRect == null) {
            int starSize = starDrawable.getIntrinsicWidth();
            int left = getWidth() - padding - starSize;
            starDrawableRect = new Rect(left, starTopMargin, left + starSize, starTopMargin + starSize);
        }
        canvas.translate(starDrawableRect.left, starDrawableRect.top);
        starDrawable.draw(canvas);
    }
}
