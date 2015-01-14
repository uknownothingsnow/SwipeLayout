package com.github.lzyzsd.swipelayoutexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lzyzsd.viewdraghelperdemo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    public interface OnScrollListener {
        void isScrolledToTop(boolean is);
    }

    public BlankFragment() {
        // Required empty public constructor
    }

    OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        final RecyclerView listView = (RecyclerView) view.findViewById(R.id.listview);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        listView.setLayoutManager(linearLayoutManager);
        MainActivity.RecyclerAdapter adapter = new MainActivity.RecyclerAdapter();
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                    swipeLayout.setShouldInterceptDragEvent(true);
                    onScrollListener.isScrolledToTop(true);
                } else {
//                    swipeLayout.setShouldInterceptDragEvent(false);
                    onScrollListener.isScrolledToTop(false);
                }
            }
        });


        return view;
    }


}
