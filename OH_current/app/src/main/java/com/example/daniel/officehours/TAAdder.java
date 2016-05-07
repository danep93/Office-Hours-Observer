package com.example.daniel.officehours;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class TAAdder extends AppCompatActivity {

    ArrayList<String> classList = new ArrayList<String>();
    Button submit;
    View selectedRadio;

    public void onClick(View view) {
        try {
            String s = ((RadioButton) view).getText().toString();
            Toast.makeText(TAAdder.this, "This is: " + s,
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_adder);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            classList = getIntent().getExtras().getStringArrayList("classList");
        }

        RadioGroup rg = new RadioGroup(this); //create the RadioGroup
        addRadioButtons(classList);
    }

    public void addRadioButtons(ArrayList<String> classList) {
        int listSize = classList.size();
        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < listSize; i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(i*1);
                rdbtn.setText(classList.get(i));
                ll.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);
        }

    }

}
