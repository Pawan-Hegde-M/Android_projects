package com.example.rouge.libraryapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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

public class LoginActivity extends AppCompatActivity {
    EditText _textuser, _textpass;  //two edittext for username,password
    Button _btnlogin;				//one button for login

    boolean backpresstwice=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _textpass = (EditText) findViewById(R.id.password111);  //password edittext object
        _textuser = (EditText) findViewById(R.id.username111);	//username edittext object
        _btnlogin = (Button) findViewById(R.id.login111);		//button object
    }
    public void login(View View){
        String username=_textuser.getText().toString().trim();  //login function called on buttonclick
        String password=_textpass.getText().toString().trim();
        String type="Login";
        LoginFunction(type,username,password);
    }
    public void LoginFunction(String type, final String username, String password){
 class LoginClass extends AsyncTask<String,Void ,String>{
     @Override
     protected String doInBackground(String... strings) {
         String result="";
         String user_name=strings[1];
         String password=strings[2];
         String type = strings[0];
         String login_url="http://172.16.2.3/library/user/login.php";  //url where the php files are placed in remote server    
         if(type.equals("Login")) {
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

                 String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
         return "connect to network";
     }

     @Override
     protected void onPreExecute() {
         super.onPreExecute();
     }

     @Override
     protected void onPostExecute(String s) {

        if(s.equals("success")){
             Intent intent1 = new Intent(LoginActivity.this, Home.class).putExtra("usnm",username); //call the next class on successfull login
             startActivity(intent1);
             LoginActivity.this.finish();
         }
         else{
             Toast.makeText(LoginActivity.this,s, Toast.LENGTH_SHORT).show();
         }
     }


 }
        LoginClass loginClass =new LoginClass();
  loginClass.execute(type,username,password);

    }
    @Override
    public void onBackPressed() {
        if(backpresstwice) {
            super.onBackPressed();
            return;
        }

        this.backpresstwice=true;
        Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backpresstwice=false;
            }
        },2000);
    }

}
