package com.application.team480.kitty_pokedex;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This class is used for result activity to display a list of result.
 */
public class ResultAdapter extends ArrayAdapter<Result> {
    private Context context;
    private List<Result> data;

    public ResultAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    public void setData(@NonNull List<Result> objects) {
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_result, parent, false);

        TextView breed = view.findViewById(R.id.breed);
        TextView percentage = view.findViewById(R.id.percentage);

        breed.setText(data.get(position).getBreed());
        percentage.setText((data.get(position).getPercentage() * 100) + "%");

        return view;
    }
}
