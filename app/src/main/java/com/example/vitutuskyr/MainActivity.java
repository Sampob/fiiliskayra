package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private NumberPicker picker;
    public static final String EXTRA = "com.example.firstapp.test";

    private ArrayList<Merkinta> lista;

    private TextView menoText;
    private ArrayList<String> menoLista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFile();

        picker = findViewById(R.id.numberPicker);

        String[] pickerNums = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        picker.setMinValue(1);
        picker.setMaxValue(pickerNums.length);
        picker.setWrapSelectorWheel(true);
        picker.setDisplayedValues(pickerNums);

        menoLista.add("Miten menee?"); menoLista.add("Mikä meno?"); menoLista.add("Kuis kulkee?");
        menoText = findViewById(R.id.menoText);
        menoText.setText(menoLista.get(new Random().nextInt(menoLista.size())));

    }

    @Override
    protected void onResume(){
        super.onResume();
        readFile();
    }

    public String getMessage() {
        EditText editText = findViewById(R.id.noteBox);
        return editText.getText().toString();
    }

    public int getNumber(){
        return picker.getValue();
    }

    public void saveThings(View v) {
        lista.add(new Merkinta(getMessage(), getNumber()));
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lista);
        editor.putString("lista", json);
        editor.apply();

        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista", null);
        Type type = new TypeToken<ArrayList<Merkinta>>() {}.getType();
        lista = gson.fromJson(json, type);

        if(lista == null) {
            lista = new ArrayList<>();
        }
    }

    public void infoButton(View v){
        Toast toast = Toast.makeText(MainActivity.this, "Tallenna fiiliksesi asteikolla 1-10 ja lisää muistiinpanoon mikä fiilis.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    public void menuPopup(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intentKayra = new Intent(this, Kayra.class);
        Intent intentPanot = new Intent(this, Muistiinpanot.class);
        switch (item.getItemId()) {
            case R.id.kayra:
                startActivity(intentKayra);
                return true;
            case R.id.muistiinpanot:
                startActivity(intentPanot);
                return true;
            default:
                return false;
        }
    }
}