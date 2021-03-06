package senanayake.udayanga.com.bustime.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import senanayake.udayanga.com.bustime.R;
import senanayake.udayanga.com.bustime.adapter.DBHelper;
import senanayake.udayanga.com.bustime.model.Route;

public class AddTimeActivity extends AppCompatActivity {
    Button timePicker, btnSubmit;
    EditText txtTime, txtRoute;
    CheckBox checkHoliday;
    int mHour, mMinute;
    AutoCompleteTextView txtFrom, txtTo, txtPlaceAdded;


    DBHelper helper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timePicker = findViewById(R.id.timePicker);
        txtTime = findViewById(R.id.textTime);

        fillSpinnerFrom(helper.getFromLocations());
        fillSpinnerTo(helper.getToLocations());
        fillSpinnerPlace(helper.getAddedLocations());

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        txtTime.setText(hour + ":" + minute);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btnSubmit = findViewById(R.id.btnSumbmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoute();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void fillSpinnerFrom(List<String> locations) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        txtFrom = findViewById(R.id.txtFrom);
        txtFrom.setAdapter(dataAdapter);

    }

    public void fillSpinnerTo(List<String> locations) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        txtTo = findViewById(R.id.txtTo);
        txtTo.setAdapter(dataAdapter);

    }

    public void fillSpinnerPlace(List<String> locations) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        txtPlaceAdded = findViewById(R.id.txtPlaceAdded);
        txtPlaceAdded.setAdapter(dataAdapter);

    }

    public void addRoute() {
        txtPlaceAdded = findViewById(R.id.txtPlaceAdded);
        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);
        txtRoute = findViewById(R.id.txtRoute);
        txtTime = findViewById(R.id.textTime);

        Route route = new Route();
        route.setPlaceAdded(txtPlaceAdded.getText().toString());
        route.setFrom(txtFrom.getText().toString());
        route.setTo(txtTo.getText().toString());
        route.setRouteNo(Integer.parseInt(txtRoute.getText().toString()));
        route.setTime(txtTime.getText().toString());
        helper.addRoute(route);
        Toast.makeText(AddTimeActivity.this, "Bus Route added successfully", Toast.LENGTH_SHORT).show();

        txtPlaceAdded.setText("");
        txtFrom.setText("");
        txtTo.setText("");
        txtRoute.setText("");
        txtTime.setText("");
    }
}
