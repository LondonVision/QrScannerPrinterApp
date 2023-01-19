package com.example.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.rcti.printerplus.printcom.models.PrintTool;
import es.rcti.printerplus.printcom.models.StructReport;

public class MainActivity extends AppCompatActivity {

    //initializing variables that are used in the project
    Button btn_scan;
    Button btn_print;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView grade;

    private JSONObject obj;

    private StructReport msr;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //This is the main function that runs the code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //This is the layout (xml file) that is showing at the beginning
        btn_scan = findViewById(R.id.btn_scan); //This is how you connect parts from the layout to variables
        btn_print = findViewById(R.id.btn_print);

        firstName = findViewById(R.id.firstNameTextView);
        lastName = findViewById(R.id.lastNameTextView);
        email = findViewById(R.id.emailTextView);
        grade = findViewById(R.id.gradeTextView);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");

        //This is a function that waits for a button to be clicked
        btn_scan.setOnClickListener(v->
        {
            msr = new StructReport(); //generates a report to be printed with printer plus app - makes use of printcom.aar in libs folder
            msr.addItemAlignment( StructReport.ALIGNMENT_CENTER );
            msr.addTextBold(false);
            msr.addTextUnderlined(false);
            msr.addItemSizeFont( StructReport.SIZE_FONT_3 );
            scanCode();

        });

        btn_print.setOnClickListener(v->{
            msr.addText(firstName.getText() + "\n" + lastName.getText() + "\n" + email.getText()
                    + "\nGrade: " + grade.getText());
            addData();
            progressDialog.show();
            PrintTool.sendOrder(MainActivity.this, msr); //sends the report to the printer - also uses printcom.aar

        });

    }
    private void scanCode(){ //this scans the qr code
        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options); //this launches the camera and prepares it to scan the qr code
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null){

            try { //attempts to extract data from qr code
                obj = new JSONObject(result.getContents());
                firstName.setText((String) obj.get("First Name"));
                lastName.setText((String)obj.get("Last Name"));
                email.setText((String)obj.get("Email"));
                grade.setText((String)obj.get("Grade"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    });

    public void addData(){ //this sends data to the spread sheet - requires volley import and gradle implementation
        String sFirstName = firstName.getText().toString();
        String sLastName = lastName.getText().toString();
        String sEmail = email.getText().toString();
        String sGrade = grade.getText().toString();

        //the url is from when you deploy the script for the spread sheet. The script code is in the helper scripts folder of the github
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwdZ-FD7fRD3St-Kyzd4tksafEb0WXBi_12pROsT1YJvzw8E-3FAV5oyCad2dsSzZa-cA/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(),SuccessActivity.class);
                startActivity(intent);
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){ //maps keywords to data values to be put into the spreadsheet
                Map<String,String> params = new HashMap<>();
                params.put("action","addStudent");
                params.put("vFirstName",sFirstName);
                params.put("vLastName",sLastName);
                params.put("vEmail",sEmail);
                params.put("vGrade",sGrade);
                return params;
            }
        };

        int socketTimeOut = 25000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}