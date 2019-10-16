package com.missouristate.embry.locatordatabase;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    DatabaseManager dbManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DatabaseManager(this);
        updateView();
    }

    public void updateView() {
        ArrayList<Locat> locats = dbManager.selectAll();
        if(locats.size() > 0) {
            // create ScrollView and GridLayout
            ScrollView scrollView = new ScrollView(this);
            GridLayout grid = new GridLayout(this);
            grid.setRowCount(locats.size());
            grid.setColumnCount(3); ////////////////////////////////

            // create arrays of components
            EditText [] names = new EditText[locats.size()];
            EditText [] ids = new EditText[locats.size()];
            Button [] buttons = new Button[locats.size()];
            ButtonHandler bh = new ButtonHandler();

            // retrieve width of screen
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;

            int i = 0;

            for (Locat locat : locats) {
                // create the TextView

                // create the Inputs
                names[i] = new EditText(this);
                names[i].setText(locat.getName());
                names[i].setId(10 * i);
                ids[i] = new EditText((this));
                ids[i].setText(String.valueOf(locat.getId()));
                ids[i].setId(10 * i + 1);

                // create the button
                buttons[i] = new Button(this);
                buttons[i].setText("Update");
                buttons[i].setId(i);

                // set up event handling
                buttons[i].setOnClickListener(bh);

                // add the elements to grid

                grid.addView(names[i], (int)(width * .72), ViewGroup.LayoutParams.WRAP_CONTENT);
                grid.addView(ids[i], 0, 0); //invisible
                grid.addView(buttons[i], (int)(width * .28), ViewGroup.LayoutParams.WRAP_CONTENT);
                i++;
            }
            Button backButton = new Button(this);
            backButton.setText(R.string.back);
            backButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UpdateActivity.this.finish();
                }
            });
            grid.addView(backButton);
            scrollView.addView(grid);
            setContentView(scrollView);
        }
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            // retrieve id and new coords
            int locId = v.getId();
            EditText firstET = findViewById(10 * locId);
            EditText hiddenET = findViewById(10 * locId + 1);
            String newName = firstET.getText().toString();
            String theID = hiddenET.getText().toString();
            int intID = Integer.valueOf(theID);

            try {
                dbManager.updateById(intID, newName);
                Toast.makeText(UpdateActivity.this, "Location updated", Toast.LENGTH_SHORT).show();

                updateView();
            } catch(NumberFormatException nfe) {
                Toast.makeText(UpdateActivity.this, "Somehow you broke it", Toast.LENGTH_LONG).show();
            }
        }
    }
}