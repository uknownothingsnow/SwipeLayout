package com.github.lzyzsd.swipelayoutexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.lzyzsd.viewdraghelperdemo.R;

public class MainActivity extends ActionBarActivity {

    private boolean interceptActionViewTouchEvent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        final ListView listView = (ListView) findViewById(R.id.listview);
        String[] data = new String[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + "aaaaaaaaaaaaaaaaaaaaaaaaaa";
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.activity_list_item, android.R.id.text1, data);
        listView.setAdapter(adapter);

        final SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipe_layout);
        final View actionView = findViewById(R.id.action_view);
        actionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return interceptActionViewTouchEvent;
            }
        });
        View contentView = findViewById(R.id.content_view);

        swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onOpen() {
                interceptActionViewTouchEvent = true;
            }

            @Override
            public void onClosed() {
                if (listView.getFirstVisiblePosition() != 0) {
                    interceptActionViewTouchEvent = false;
                } else {
                    interceptActionViewTouchEvent = true;
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("---------------firstVisibleItem: " + firstVisibleItem
                    + "--------" + view.getScrollY());
                if (firstVisibleItem == 0 && view.getScrollY() <= 0) {
                    swipeLayout.setShouldInterceptDragEvent(true);
                } else {
                    swipeLayout.setShouldInterceptDragEvent(false);
                }
            }
        });
    }
}
