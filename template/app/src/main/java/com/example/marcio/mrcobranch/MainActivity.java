package com.example.marcio.mrcobranch;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Code to add in the Camera when this button is pressed, open up the cameera
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
       imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }



       });
            //Add the items into the spinner for the drop down menu
        Spinner spinner = (Spinner) findViewById(R.id.clothingColour);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colours, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(this);
        String myStr = spinner.getSelectedItem().toString();
        EditText test1 = (EditText) findViewById(R.id.testResult1);
        test1.setText(myStr);
        Log.w("Heeeeere",myStr);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
          ;
           // EditText test1 = (EditText) findViewById(R.id.testResult1);
          //  test1.setText((String) parent.getItemAtPosition(pos));
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
