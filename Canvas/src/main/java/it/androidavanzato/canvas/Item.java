package it.androidavanzato.canvas;

import android.graphics.Bitmap;

public class Item {
    private String title;
    private String subtitle;
    private boolean starred;
    private Bitmap bitmap;

    public Item(int position, Bitmap bitmap) {
        this.bitmap = bitmap;
        title = "Title item " + (position + 1);
        subtitle = "Subtitle item " + (position + 1);
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isStarred() {
        return starred;
    }

    public void toggleStarred() {
        starred = !starred;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
