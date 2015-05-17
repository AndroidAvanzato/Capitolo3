package it.androidavanzato.canvas;

import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TextViewRowWrapper {

    private ImageView star;
    private TextView text;

    private final int subtitleTextSize;
    private final int titleTextSize;
    private Item item;

    public TextViewRowWrapper(View view) {
        star = (ImageView) view.findViewById(R.id.star);
        text = (TextView) view.findViewById(R.id.text);

        star.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                item.toggleStarred();
                v.setSelected(item.isStarred());
            }
        });

        view.setTag(this);

        titleTextSize = view.getResources().getDimensionPixelSize(R.dimen.title_text_size);
        subtitleTextSize = view.getResources().getDimensionPixelSize(R.dimen.subtitle_text_size);
    }

    public void populate(Item item) {
        this.item = item;
        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher, 0, 0, 0);

        String title = item.getTitle();
        String subtitle = item.getSubtitle();

SpannableString spannableString = new SpannableString(title + "\n" + subtitle);

int titleLength = title.length();
spannableString.setSpan(new AbsoluteSizeSpan(titleTextSize), 0, titleLength, 0);
int subtitleLength = subtitle.length();
spannableString.setSpan(new AbsoluteSizeSpan(subtitleTextSize), titleLength, titleLength + subtitleLength, 0);

text.setText(spannableString);
        star.setSelected(item.isStarred());
    }
}
