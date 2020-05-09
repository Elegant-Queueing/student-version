package com.example.q_student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FairsView extends Fragment implements View.OnClickListener {
    LinearLayout view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((FairActivity) getActivity()).setActionBarTitle("My Fairs");
        View rootView = inflater.inflate(R.layout.fair_list, container, false);
        view = rootView.findViewById(R.id.list_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCompanies();
    }

    public void addCompanies() {
        for(int i = 0; i < 10; i++) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View cardView = inflater.inflate(R.layout.fair_list_item, null);
            cardView.setId(i);
            TextView fairName = cardView.findViewById(R.id.fair_name);
            String test = "Fair " + i;
            fairName.setText(test);
            TextView fairDesc = cardView.findViewById(R.id.fair_desc);
            String test2 = "This is the description for fair " + i + " in the Q student app.";
            fairDesc.setText(test2);

//            ImageView fairPicture = cardView.findViewById(R.id.fair_picture);
//            fairPicture.setImageResource(R.drawable.ic_account_circle_black_24dp);
            cardView.setOnClickListener(this);
            view.addView(cardView);
        }
    }

    public void onClick(View view)
    {
        Toast.makeText(this.getContext(), view.getId()+ "", Toast.LENGTH_SHORT).show();

    }
}
