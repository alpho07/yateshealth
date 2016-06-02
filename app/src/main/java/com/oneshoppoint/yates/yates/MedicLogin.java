package com.oneshoppoint.yates.yates;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by stephineosoro on 31/05/16.
 */
public class MedicLogin extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _emailText.setText(getDefaults("email", this));
        _passwordText.setText(getDefaults("password", this));

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();

            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MedicLogin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        logindetail();
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void logindetail() {
        // Tag used to cancel the request    Nairobi coordinates are: -1.288999, 36.888550      Lattitude: -1.292065 Longitude: 36.821946  -1.292065, 36.821946
        String tag_string_req = "req_login";

//        pDialog.setMessage("Loading Matatus...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://www.oneshoppoint.com/openpath/login.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("All Data", "response from the server is: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");


                    //successfully gotten matatu data
                    if (success.equals("success")) {
                        String driver_passenger = jObj.getString("driver_passenger");
                        String sacco=jObj.getString("sacco");
                        setDefaults("sacco",sacco,getBaseContext());
//                        Toast.makeText(getApplicationContext(),
//                                "Welcome!", Toast.LENGTH_LONG).show();
                        if (driver_passenger.equals("Driver")||driver_passenger.equals("driver")) {

//                            Intent intent = new Intent(getApplicationContext(), MainActivity1.class);
//                            startActivity(intent);
                        }else{
//                            Intent intent = new Intent(getApplicationContext(), NewHomeRate.class);
//                            startActivity(intent);
                        }
//                        String regno = jObj.getString("regno");

//                        res = jObj.getJSONArray("All");
//
//                        Log.e("result: ", res.toString());
//
//                        // looping through All res
//                        for (int i = 0; i < res.length(); i++) {
//                            JSONObject c = res.getJSONObject(i);
//
//                            // Storing each json item in variable
//
//                            String regno = c.getString("regno");
//                            String description = c.getString("sacco");
//                            String image = c.getString("image");
//                            image = "http://www.oneshoppoint.com/" + image;
//                            Items items = new Items();
//                            items.setTitle(regno);
//                            items.setThumbnailUrl(image);
//                            items.setDescription(description);
//                            itemsList.add(items);
//                        }


//                        adapter = new LatestMatatu(getBaseContext(), itemsList);
//                        recyclerView.setAdapter(adapter);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("success");
                        Toast.makeText(getApplicationContext(),
                                "No internet connection!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MedicLogin.class);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No internet connection! " , Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Getting data error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet connection!", Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _emailText.getText().toString());
                params.put("password", _passwordText.getText().toString());
                setDefaults("email", _emailText.getText().toString(), getBaseContext());
                setDefaults("password",_passwordText.getText().toString(),getBaseContext());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();

    }
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}


