package com.shashi.telanganacinizens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MyActivity extends Activity {

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isLogin = sp.getBoolean("isLogin",false);
        if(isLogin){
            startActivity(new Intent(MyActivity.this, ScreenSlideActivity.class));
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    public void open(View v){
        startActivity(new Intent(MyActivity.this, ScreenSlideActivity.class));
        //startActivityForResult(new Intent(MyActivity.this, ScreenSlideActivity.class),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        boolean isLogin = sp.getBoolean("isLogin",false);
        if(isLogin){
            //startActivity(new Intent(MyActivity.this, ScreenSlideActivity.class));
            finish();
        }
    }

    public void goToReg(View v){
        startActivityForResult(new Intent(MyActivity.this, Register.class),1);
    }

}
