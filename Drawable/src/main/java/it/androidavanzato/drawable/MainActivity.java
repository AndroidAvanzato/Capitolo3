package it.androidavanzato.drawable;

import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View.OnClickListener l = new View.OnClickListener() {
            @Override public void onClick(View v) {
                click(v);
            }
        };
        findViewById(R.id.button2).setOnClickListener(l);
        findViewById(R.id.button4).setOnClickListener(l);
        ImageView image = (ImageView) findViewById(R.id.white);
        image.setColorFilter(new LightingColorFilter(0xFFFFF000, 0xFF000000));
    }

    public void click(View view) {
        Toast.makeText(this, R.string.hello_world, Toast.LENGTH_LONG).show();
    }
}
