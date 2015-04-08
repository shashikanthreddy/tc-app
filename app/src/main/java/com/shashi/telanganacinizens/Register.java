package com.shashi.telanganacinizens;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class Register extends Activity {

    EditText name;
    EditText email;
    EditText password;
    ProgressBar loading;
    SharedPreferences.Editor spe;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spe = sp.edit();

        loading = (ProgressBar)findViewById(R.id.loading);
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        email.setText(getDeviceEmail(getApplicationContext()));
        //email.setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToLogin(View v){
        startActivity(new Intent(Register.this, MyActivity.class));
    }

    public void register(View v) throws IOException {
        loading.setVisibility(View.VISIBLE);
        new RegisterS().execute();
    }

    public class RegisterS extends AsyncTask <Void, Void, HttpResponse>{

        @Override
        protected HttpResponse doInBackground(Void... voids) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://shashi-msg.appspot.com/v1/register");

            JSONObject js = new JSONObject();
            try {
                js.put("name", name.getText().toString());
                js.put("email", email.getText().toString());
                js.put("password", password.getText().toString());
                StringEntity se = new StringEntity(js.toString());

                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                post.setEntity(se);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            HttpResponse response = null;
            try {
                response = client.execute(post);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(HttpResponse response) {
            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode==200){
                String responseText = null;
                try {
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    try {
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String result = sb.toString();
                    //responseText = EntityUtils.toString(response.getEntity());
                    System.out.println(responseText);
                    Toast.makeText(Register.this,result,Toast.LENGTH_LONG);
                    spe.putBoolean("isLogin",true);
                    spe.commit();
                    startActivity(new Intent(Register.this, ScreenSlideActivity.class));
                    finish();
                }catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(response);
        }
    }


    public String getDeviceEmail(Context context){
        AccountManager accManager = AccountManager.get(context);
        Account acc[] = accManager.getAccountsByType("com.google");
        if(acc.length>0)
            return acc[0].name;
        else
            return "";
    }
}
