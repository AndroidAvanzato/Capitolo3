package it.androidavanzato.canvas;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;

public class HtmlTextViewRowWrapper {

    private ImageView star;
    private TextView text;
    private Item item;


    public HtmlTextViewRowWrapper(View view) {
        star = (ImageView) view.findViewById(R.id.star);
        text = (TextView) view.findViewById(R.id.text);

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
        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher, 0, 0, 0);

        String title = item.getTitle();
        String subtitle = item.getSubtitle();

        String htmlString = MessageFormat.format("<big><big>{0}</big></big><br/><big>{1}</big>", title, subtitle);
        text.setText(Html.fromHtml(htmlString));
        star.setSelected(item.isStarred());
    }
}
