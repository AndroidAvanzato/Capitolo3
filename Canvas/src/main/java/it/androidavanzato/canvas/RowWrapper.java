package it.androidavanzato.canvas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RowWrapper {

    private ImageView image;
    private TextView text1;
    private TextView text2;
    private ImageView star;
    private Item item;

    public RowWrapper(View view) {
        image = (ImageView) view.findViewById(R.id.image);
        text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        star = (ImageView) view.findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                item.toggleStarred();
                v.setSelected(item.isStarred());
            }
        });
        view.setTag(this);
    }

    public void populate(Item item) {
        this.item = item;
        text1.setText(item.getTitle());
        text2.setText(item.getSubtitle());
        image.setImageResource(R.drawable.ic_launcher);
        star.setSelected(item.isStarred());
    }
}
