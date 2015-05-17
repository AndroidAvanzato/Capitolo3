package it.androidavanzato.transitions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        setContentView(recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int space = 10;
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }
        });
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @Override public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder(MainActivity.this, getLayoutInflater().inflate(R.layout.row, viewGroup, false));
            }

            @Override public void onBindViewHolder(MyViewHolder viewHolder, int i) {
                viewHolder.populate(i);
            }

            @Override public int getItemCount() {
                return 20;
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        private int color;

        public MyViewHolder(final Activity activity, final View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    startDetailActivity(activity);
                }
            });
        }

        public void populate(int position) {
            color = Color.argb(255, position * 10, position * 23 % 255, position * 157 % 255);
            imageView.setColorFilter(color);
        }

        private void startDetailActivity(Activity activity) {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra("color", color);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                int[] location = new int[2];
                imageView.getLocationOnScreen(location);
                intent.putExtra("x", location[0]);
                intent.putExtra("y", location[1]);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } else {
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, "icon");
                activity.startActivity(intent, transitionActivityOptions.toBundle());
            }
        }
    }
}