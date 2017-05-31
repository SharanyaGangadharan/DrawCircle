package com.example.shara.assignment3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public View simpleDrawing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDrawing = new SimpleDrawing(this);
        setContentView(simpleDrawing);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        menu.findItem(R.id.option).getSubMenu().findItem(R.id.draw).setChecked(true);
        menu.findItem(R.id.color).getSubMenu().findItem(R.id.black).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.draw:
                ((SimpleDrawing)simpleDrawing).setMode("Draw");
                Toast.makeText(this, "DRAW MODE", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;
            case R.id.delete:
                ((SimpleDrawing)simpleDrawing).setMode("Delete");
                Toast.makeText(this, "DELETE MODE", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;
            case R.id.move:
                ((SimpleDrawing)simpleDrawing).setMode("Move");
                Toast.makeText(this, "MOVE MODE", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;
            case R.id.red:
                ((SimpleDrawing)simpleDrawing).setColor("red");
                item.setChecked(true);
                break;
            case R.id.green:
                ((SimpleDrawing)simpleDrawing).setColor("green");
                item.setChecked(true);
                break;
            case R.id.black:
                ((SimpleDrawing)simpleDrawing).setColor("black");
                item.setChecked(true);
                break;
            case R.id.blue:
                ((SimpleDrawing)simpleDrawing).setColor("blue");
                item.setChecked(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}