package com.application.team480.kitty_pokedex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by luis_cortes on 11/25/17.
 */

public class CharacteristicListAdapter extends ArrayAdapter<Characteristic> {
    private final String TAG = "CharacteristicList";
    private Context context;
    private List<Characteristic> characteristics;

    public CharacteristicListAdapter(@NonNull Context context, int resource, @NonNull List<Characteristic> objects) {
        super(context, resource, objects);
        this.context = context;
        this.characteristics = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.breed_characteristic_item, parent, false);

        TextView characterNameTextView = (TextView) view.findViewById(R.id.character_name_text_view);
        TextView characterNumberTextView = (TextView) view.findViewById(R.id.character_number_text_view);
        SeekBar characterNameSeekBar = (SeekBar) view.findViewById(R.id.character_name_seek_bar);

        Characteristic characteristic = characteristics.get(position);

        characterNameTextView.setText(
                formatType( characteristic.getType() )
        );
        characterNumberTextView.setText(characteristic.getScale());
        characterNameSeekBar.setProgress(Integer.parseInt( characteristic.getScale() ));
        characterNameSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        Log.i(TAG, "Type: " + characteristic.getType() + "\n"
                + "Scale: " + characteristic.getScale());

        return view;
    }

    private String formatType(CharacteristicType type) {
        if (type == CharacteristicType.AFFECTION) {
            return "Affection toward owners: ";
        } else if (type == CharacteristicType.ENERGY) {
            return "Energy level: ";
        } else if (type == CharacteristicType.FRIENDLINESS) {
            return "Friendliness towards strangers: ";
        } else if (type == CharacteristicType.FUR_LENGTH) {
            return "Length of fur: ";
        } else if (type == CharacteristicType.GROOMING) {
            return "Grooming Requirements: ";
        } else if (type == CharacteristicType.TRAINING) {
            return "Ease of training: ";
        } else {
            return "None: ";
        }
    }
}
