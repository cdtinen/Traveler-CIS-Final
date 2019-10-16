package com.missouristate.embry.locatordatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {
    private DatabaseManager dbManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DatabaseManager(this);
        updateView();
    }

    // Build a View dynamically
    public void updateView() {
        ArrayList<Locat> locats = dbManager.selectAll();
        RelativeLayout layout = new RelativeLayout(this);
        ScrollView scrollView = new ScrollView(this);
        RadioGroup group = new RadioGroup(this);
        int i = 0;
        for (Locat locat : locats) {
            RadioButton rb = new RadioButton(this);
            rb.setId(i);
            i++;
            rb.setText(locat.toString());
            group.addView(rb);
        }
        // set up event handling
        RadioButtonHandler rbh = new RadioButtonHandler();
        group.setOnCheckedChangeListener(rbh);

        // create a back button
        Button backButton = new Button(this);
        backButton.setText(R.string.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteActivity.this.finish();
            }
        });

        scrollView.addView(group);
        layout.addView(scrollView);

        // add back button at bottom
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(50, 50, 50, 50);
        layout.addView(backButton, params);

        setContentView(layout);
    }

    private class RadioButtonHandler implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkId) {
            String rawID = ((RadioButton)findViewById(checkId)).getText().toString();
            rawID = rawID.substring(0 ,(rawID.indexOf(","))); //might sub here
            int theID = Integer.valueOf(rawID);
            dbManager.deleteById(theID);
            Toast.makeText(DeleteActivity.this, "Location deleted", Toast.LENGTH_SHORT).show();
            // update screen
            updateView();
        }
    }
}
