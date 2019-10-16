package com.missouristate.embry.locatordatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbManager = new DatabaseManager(this);

        updateView();
    }

    public void updateView() {
        ArrayList<Locat> locats = dbManager.selectAll();
        GridLayout grid = new GridLayout(this);
        grid.setRowCount(locats.size()+1);
        grid.setColumnCount(4);
        grid.setUseDefaultMargins(true);
        int i = 1;
        TextView[][] names = new TextView[locats.size()+1][4];

        names[0][0] = new TextView(this);
        names[0][1] = new TextView(this);
        names[0][2] = new TextView(this);
        names[0][3] = new TextView(this);
        names[0][0].setText("ID");
        names[0][1].setText("Name");
        names[0][2].setText("Latitude");
        names[0][3].setText("Longitude");
        grid.addView(names[0][0], ViewGroup.LayoutParams.WRAP_CONTENT);
        grid.addView(names[0][1], ViewGroup.LayoutParams.WRAP_CONTENT);
        grid.addView(names[0][2], ViewGroup.LayoutParams.WRAP_CONTENT);
        grid.addView(names[0][3], ViewGroup.LayoutParams.WRAP_CONTENT);

        for (Locat locat : locats) {
            names[i][0] = new TextView(this);
            names[i][1] = new TextView(this);
            names[i][2] = new TextView(this);
            names[i][3] = new TextView(this);
            names[i][0].setText(String.valueOf(locat.getId()));
            names[i][1].setText(locat.getName());
            names[i][2].setText(locat.getLat().substring(0, ((locat.getLat().length()/2)+1)));
            names[i][3].setText(locat.getLong().substring(0, ((locat.getLong().length()/2)+1)));
            grid.addView(names[i][0], ViewGroup.LayoutParams.WRAP_CONTENT);
            grid.addView(names[i][1], ViewGroup.LayoutParams.WRAP_CONTENT);
            grid.addView(names[i][2], ViewGroup.LayoutParams.WRAP_CONTENT);
            grid.addView(names[i][3], ViewGroup.LayoutParams.WRAP_CONTENT);
            i++;
        }
        ViewGroup startlist = findViewById(R.id.scrollchild);
        startlist.addView(grid); //looks ugly, but it works
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        try {
            switch (id) {
                case R.id.action_add:
                    try {
                        Intent insertIntent = new Intent(this, InsertActivity.class);
                        this.startActivityForResult(insertIntent, 2);
                    } catch (Error error) {
                        Toast.makeText(this, "Please enable Location services", Toast.LENGTH_LONG).show();
                    }
                    return true;
                case R.id.action_delete:
                    Intent deleteIntent = new Intent(this, DeleteActivity.class);
                    this.startActivityForResult(deleteIntent, 2);
                    return true;
                case R.id.action_update:
                    Intent updateIntent = new Intent(this, UpdateActivity.class);
                    this.startActivityForResult(updateIntent, 2);
                    return true;
                case R.id.action_exit:
                    this.finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch(Error error){
            Toast.makeText(this, "Please enable Location services", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, getIntent());
        ViewGroup startlist = findViewById(R.id.scrollchild);
        startlist.removeAllViews();
        updateView();
    }
}