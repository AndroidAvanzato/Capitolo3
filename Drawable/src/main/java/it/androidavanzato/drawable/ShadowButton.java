package it.androidavanzato.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

public class ShadowButton extends Button {
    public ShadowButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShadowButton,
                0, 0);

        int borderColor = 0;
        int backgroundColor = 0;

        try {
            borderColor = a.getColor(R.styleable.ShadowButton_borderColor, R.color.button_red_border);
            backgroundColor = a.getColor(R.styleable.ShadowButton_backgroundColor, R.color.button_red_background);
        } finally {
            a.recycle();
        }

        Resources resources = getResources();

        StateListDrawable background = (StateListDrawable) resources.getDrawable(R.drawable.button).mutate();
        DrawableContainer.DrawableContainerState containerState = (DrawableContainer.DrawableContainerState) background.getConstantState();
        final Drawable[] children = containerState.getChildren();

        GradientDrawable pressedDrawable = (GradientDrawable) children[0];
        pressedDrawable.setColor(borderColor);

        LayerDrawable normalDrawable = (LayerDrawable) children[1];
        GradientDrawable normalBackground = (GradientDrawable) normalDrawable.getDrawable(0);
        GradientDrawable normalBorder = (GradientDrawable) normalDrawable.getDrawable(1);
        normalBackground.setColor(borderColor);
        normalBorder.setColor(backgroundColor);

        setBackgroundDrawable(background);
        setMinHeight(resources.getDimensionPixelSize(R.dimen.button_min_height));
        setMinWidth(resources.getDimensionPixelSize(R.dimen.button_min_width));
        setGravity(Gravity.CENTER);
        int padding = resources.getDimensionPixelSize(R.dimen.button_padding);
        setPadding(padding, padding, padding, padding);
    }
}
