package com.github.lzyzsd.swipelayoutexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.viewdraghelperdemo.R;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity {

    private boolean interceptActionViewTouchEvent = false;
//    LinearLayoutManager linearLayoutManager;
    private boolean isOnTop = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        (findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        });

//        final RecyclerView listView = (RecyclerView) findViewById(R.id.listview);
//        linearLayoutManager = new LinearLayoutManager(this);
//        listView.setLayoutManager(linearLayoutManager);
//        RecyclerAdapter adapter = new RecyclerAdapter();
//        listView.setAdapter(adapter);
        String[] data = new String[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + "aaaaaaaaaaaaaaaaaaaaaaaaaa";
        }

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.activity_list_item, android.R.id.text1, data);
//        listView.setAdapter(adapter);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(newsPagerAdapter);


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
//                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
//                    interceptActionViewTouchEvent = false;
//                } else {
//                    interceptActionViewTouchEvent = true;
//                }
                interceptActionViewTouchEvent = isOnTop;
            }
        });

        newsPagerAdapter.setOnScrollListener(new BlankFragment.OnScrollListener() {
            @Override
            public void isScrolledToTop(boolean is) {
                swipeLayout.setShouldInterceptDragEvent(is);
                isOnTop = is;
            }
        });

//        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                    swipeLayout.setShouldInterceptDragEvent(true);
//                } else {
//                    swipeLayout.setShouldInterceptDragEvent(false);
//                }
//            }
//        });

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                System.out.println("---------------firstVisibleItem: " + firstVisibleItem
//                    + "--------" + view.getScrollY());
//                if (firstVisibleItem == 0 && view.getScrollY() <= 0) {
//                    swipeLayout.setShouldInterceptDragEvent(true);
//                } else {
//                    swipeLayout.setShouldInterceptDragEvent(false);
//                }
//            }
//        });
    }

    public static class NewsPagerAdapter extends FragmentStatePagerAdapter {
        private static final String[] titles = new String[] { "我的事件", "重要消息", "财经日历"};
        BlankFragment[] blankFragments = new BlankFragment[]{
            new BlankFragment(),
            new BlankFragment(),
            new BlankFragment()
        };

        public void setOnScrollListener(BlankFragment.OnScrollListener onScrollListener) {
            blankFragments[0].setOnScrollListener(onScrollListener);
            blankFragments[1].setOnScrollListener(onScrollListener);
            blankFragments[2].setOnScrollListener(onScrollListener);
        }

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return blankFragments[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

        String[] data = new String[100];

        public RecyclerAdapter() {
            super();
            for (int i = 0; i < data.length; i++) {
                data[i] = i + "aaaaaaaaaaaaaaaaaaaaaaaaaa";
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.activity_list_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int i) {
            viewHolder.textView.setText(data[i]);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
