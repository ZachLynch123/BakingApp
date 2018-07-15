package com.zachary.lynch.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.List;

public class MasterDetailFragment extends Fragment {

    private MasterListener mListener;
    private TextView mTextView;


    public interface MasterListener{
        void onResponse(List<Ingredients> ingredients, List<Steps> steps);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.master_fragment,container,false);
        mTextView = view.findViewById(R.id.textView360);
        return view;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MasterListener){
            mListener = (MasterListener) context;
        }else{
            throw new RuntimeException(context.toString()
             + " doesn't implement masterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
