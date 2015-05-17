package it.androidavanzato.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_COUNT = 5;

    private LayoutInflater layoutInflater;

    private Item[] items;

    public MyAdapter(Context context, LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        items = new Item[VIEW_TYPE_COUNT * 10];
        Bitmap bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(i, bitmap);
        }
    }

    @Override public int getItemViewType(int position) {
        return position % VIEW_TYPE_COUNT;
    }

    @Override public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override public int getCount() {
        return items.length;
    }

    @Override public Item getItem(int position) {
        return items[position];
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        switch (position % VIEW_TYPE_COUNT) {
            case 0:
                return getViewWithRowWrapperLinearLayout(position, convertView, parent);
            case 1:
                return getViewWithRowWrapperRelativeLayout(position, convertView, parent);
            case 2:
                return getViewWithTextView(position, convertView, parent);
            case 3:
                return getViewWithHtmlTextView(position, convertView, parent);
            case 4:
                return getViewWithCustomView(position, convertView, parent);
        }
        return null;
    }


    private View getViewWithRowWrapperRelativeLayout(int position, View convertView, ViewGroup parent) {
        RowWrapper rowWrapper;
        if (convertView == null) {
            convertView = inflateRelativeLayout(parent);
            rowWrapper = new RowWrapper(convertView);
        } else {
            rowWrapper = (RowWrapper) convertView.getTag();
        }
        rowWrapper.populate(getItem(position));
        return convertView;
    }

    private View inflateRelativeLayout(ViewGroup parent) {
        return layoutInflater.inflate(R.layout.row_relative_layout, parent, false);
    }

    private View getViewWithRowWrapperLinearLayout(int position, View convertView, ViewGroup parent) {
        RowWrapper rowWrapper;
        if (convertView == null) {
            convertView = inflateLinearLayout(parent);
            rowWrapper = new RowWrapper(convertView);
        } else {
            rowWrapper = (RowWrapper) convertView.getTag();
        }
        rowWrapper.populate(getItem(position));
        return convertView;
    }

    private View inflateLinearLayout(ViewGroup parent) {
        return layoutInflater.inflate(R.layout.row_linear_layout, parent, false);
    }

    private View getViewWithCustomView(int position, View convertView, ViewGroup parent) {
        RowView rowView;
        if (convertView == null) {
            rowView = (RowView) inflateCustomView(parent);
        } else {
            rowView = (RowView) convertView;
        }

        rowView.populate(getItem(position));
        return rowView;
    }

    private View inflateCustomView(ViewGroup parent) {
        return layoutInflater.inflate(R.layout.row_custom_view, parent, false);
    }

    private View getViewWithHtmlTextView(int position, View convertView, ViewGroup parent) {
        HtmlTextViewRowWrapper rowWrapper;
        if (convertView == null) {
            convertView = inflateTextViewHtml(parent);
            rowWrapper = new HtmlTextViewRowWrapper(convertView);
        } else {
            rowWrapper = (HtmlTextViewRowWrapper) convertView.getTag();
        }
        rowWrapper.populate(getItem(position));
        return convertView;
    }

    private View getViewWithTextView(int position, View convertView, ViewGroup parent) {
        TextViewRowWrapper rowWrapper;
        if (convertView == null) {
            convertView = inflateTextView(parent);
            rowWrapper = new TextViewRowWrapper(convertView);
        } else {
            rowWrapper = (TextViewRowWrapper) convertView.getTag();
        }
        rowWrapper.populate(getItem(position));
        return convertView;
    }

    private View inflateTextView(ViewGroup parent) {
        return layoutInflater.inflate(R.layout.row_text_view, parent, false);
    }

    private View inflateTextViewHtml(ViewGroup parent) {
        return layoutInflater.inflate(R.layout.row_text_view_html, parent, false);
    }

}
