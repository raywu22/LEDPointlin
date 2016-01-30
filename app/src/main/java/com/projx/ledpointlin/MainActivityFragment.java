package com.projx.ledpointlin;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private FrameLayout mInputGrid;//holds grid of InputSquares that will be used to read and display input
    private ArrayList<ArrayList<InputSquare>> mInputSquares;//2d arraylist of input squares to populate mInputGrid with - where (0,0) refers to top left square
    private PixelGridView mGridView;

    public MainActivityFragment() {
        mInputSquares = new ArrayList<ArrayList<InputSquare>>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        mInputGrid = (FrameLayout) rootView.findViewById(R.id.mainGrid);
        mGridView = new PixelGridView(this.getContext(), 3, 3);

        mGridView.setPixelChangedListener(new PixelGridView.PixelChangedListener(){
            public void onPixelsChanged()
            {

            }
        });

        mGridView.setBaseColor(1,1, Color.YELLOW);

        mInputGrid.addView(mGridView);

        return rootView;
    }



}
