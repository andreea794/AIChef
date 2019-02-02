package com.teamalpha.aichef.slideuppanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamalpha.aichef.R;

public class ExampleFragment extends Fragment {

    private String title;
    private int page;

    public static ExampleFragment newInstance(String title, int page) {
        ExampleFragment exampleFragment = new ExampleFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        exampleFragment.setArguments(args);
        return exampleFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt");
        title = getArguments().getString("someTitle");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);
        return view;
    }
}
