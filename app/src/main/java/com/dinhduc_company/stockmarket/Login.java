package com.dinhduc_company.stockmarket;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DataBase.DBUsers;

/**
 * Created by duy on 4/2/2015.
 */
public class Login extends Activity {

    EditText edit_username, edit_password;
    public static String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        //==== find view id ======
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);

        //button sign up
        Button btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SignUp.class);
                startActivity(i);
            }
        });

        // set listener for button login
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAccount()) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Username or Password wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //==========  check account into database  ================
    private boolean checkAccount() {
        boolean result = false;
        username = edit_username.getText().toString();
        password = edit_password.getText().toString();
        if (username.equals("") || password.equals("")) {
            return false;
        }
        DBUsers db = new DBUsers(getBaseContext());
        db.open();
        Cursor cursor = db.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(username)) {
                    if (cursor.getString(2).equals(password)) {
                        result = true;
                        break;
                    }
                    break;
                }
            } while (cursor.moveToNext());
        }
        return result;
    }

    //========  get username of account login  =============
    public String getUsername() {
        return username;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
