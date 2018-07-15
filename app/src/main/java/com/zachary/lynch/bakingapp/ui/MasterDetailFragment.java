package com.zachary.lynch.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.IngredientsAdapter;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class MasterDetailFragment extends Fragment {

    private MasterListener mListener;
    private TextView mTextView;
    private List<Ingredients> mIngredients;
    private List<Steps> mSteps;
    private ListView mIngredientsListView;
    private ListView mStepsListView;
    private Bundle mBundle;


    public interface MasterListener{
        void onStepClicked(Bundle bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        mIngredients = mBundle.getParcelableArrayList("ingredients");
        mSteps = mBundle.getParcelableArrayList("steps");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.master_fragment,container,false);
        mIngredientsListView = view.findViewById(R.id.ingredientsList);
        mStepsListView = view.findViewById(R.id.stepsList);

        IngredientsAdapter adapter = new IngredientsAdapter(getContext(), mIngredients);
        mIngredientsListView.setAdapter(adapter);

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
