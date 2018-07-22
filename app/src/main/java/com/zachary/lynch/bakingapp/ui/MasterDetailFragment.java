package com.zachary.lynch.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.IngredientsAdapter;
import com.zachary.lynch.bakingapp.adapters.StepsAdapter;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class MasterDetailFragment extends Fragment {

    private MasterListener mListener;
    private List<Ingredients> mIngredients;
    private ArrayList<Steps> mSteps;
    private ListView mStepsListView;


    public interface MasterListener{
        void onStepClicked(ArrayList<Steps> step, int index);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mIngredients = bundle.getParcelableArrayList("ingredients");
        mSteps = bundle.getParcelableArrayList("steps");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.master_fragment,container,false);
        ListView ingredientsListView = view.findViewById(R.id.ingredientsList);
        mStepsListView = view.findViewById(R.id.stepsList);

        IngredientsAdapter adapter = new IngredientsAdapter(getContext(), mIngredients);
        ingredientsListView.setAdapter(adapter);
        test();

        StepsAdapter stepsAdapter = new StepsAdapter(getContext(), mSteps);
        mStepsListView.setAdapter(stepsAdapter);
        mStepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onStepClicked(mSteps, position);
            }
        });
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
    private void test(){

    }
}
