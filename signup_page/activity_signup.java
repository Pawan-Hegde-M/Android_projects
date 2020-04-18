package com.example.rouge.libraryapp;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class activity_signup extends AppCompatActivity {
    EditText _textuser, _textpass, _textpass2, _textname, _textph, _textem;//edittexts for user details
    Button btnsignup;	//button for signup
    String username;
    String password;
    String password2;
    String email;
    String phone;
    String name;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        _textuser = (EditText) findViewById(R.id.username);
        _textpass = (EditText) findViewById(R.id.password);
        _textpass2 = (EditText) findViewById(R.id.password2);
        _textem = (EditText) findViewById(R.id.email);
        _textph = (EditText) findViewById(R.id.phone);
        _textname = (EditText) findViewById(R.id.name);
        btnsignup = (Button) findViewById(R.id.signup);


    }

    public void signup(View View) {									//signup function called on button click
        username = _textuser.getText().toString().trim();
         password = _textpass.getText().toString().trim();
         password2 = _textpass2.getText().toString().trim();
         email = _textem.getText().toString().trim();
         phone = _textph.getText().toString().trim();
         name = _textname.getText().toString().trim();
         type = "Signup";
        if((username.isEmpty()) || (password.isEmpty()) || (password2.isEmpty()) || (name.isEmpty()) || (phone.isEmpty()) || (email.isEmpty())) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AlertDialog.Builder ad=new AlertDialog.Builder(activity_signup.this);
            ad.setTitle("ALERT");
            ad.setMessage("Do you really want to signup?");
            ad.setCancelable(false);
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    signupFunction(type, username, password, password2, name, phone, email);
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog a=ad.create();
            a.show();
        }
    }

    public void signupFunction(String type, final String username, String password, String password2, String name, String phone, String email) {
        class signupclass extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... strings) {
                String result = "";
                String user_name = strings[1];
                String password = strings[2];
                String conpass = strings[3];
                String name = strings[4];
                String phone = strings[5];
                String email = strings[6];
                String type = strings[0];
                String login_url = "http://172.16.2.91/library/user/signup.php";		//url where the php files are placed on remote server
                if (type.equals("Signup")) {
                    if (password.length()>=8 && password.equals(conpass) && user_name.length()==10 ) {
                        try {
                            URL url = new URL(login_url);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setReadTimeout(14000);

                            httpURLConnection.setConnectTimeout(14000);

                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoInput(true);

                            httpURLConnection.setDoOutput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(

                                    new OutputStreamWriter(outputStream, "UTF-8"));

                            String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")  + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                            bufferedWriter.write(post_data);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                            String line = "";
                            while ((line = bufferedreader.readLine()) != null) {
                                result += line;
                            }
                            bufferedreader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            return result;

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return "Something went wrong";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("success")){
                    Toast.makeText(activity_signup.this,s, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_signup.this, LoginActivity.class);//call next activity on successful registration
                    startActivity(intent);
                    activity_signup.this.finish();
                }
                else{
                    Toast.makeText(activity_signup.this,s, Toast.LENGTH_SHORT).show();
                }
            }
        }
        signupclass sign=new signupclass();
        sign.execute(type,username,password,password2,name,phone,email);

    }
    public void onBackPressed() {
        Intent intent = new Intent(activity_signup.this, LoginActivity.class);
        startActivity(intent);										//call next activity on back press
        activity_signup.this.finish();
        super.onBackPressed();
    }
}

