package com.dinhduc_company.stockmarket;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import DataBase.DBStock;
import DataBase.DBUsers;

/**
 * Created by duy on 4/2/2015.
 */
public class SignUp extends Activity {
    private String username, password, email, gender;

    EditText edit_username, edit_password, edit_email;
    RadioGroup radioGroup;
    RadioButton radMale, radFemale;
    ImageView imageViewUser, imageViewPass, imageViewEmail;

    private Integer[] image = {R.drawable.no, R.drawable.yes};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up);

        //=========== find view id  ===========
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_email = (EditText) findViewById(R.id.edit_email);
        radioGroup = (RadioGroup) findViewById(R.id.radGroup);
        radMale = (RadioButton) findViewById(R.id.male);
        radFemale = (RadioButton) findViewById(R.id.female);
        imageViewUser = (ImageView) findViewById(R.id.imageViewUser);
        imageViewPass = (ImageView) findViewById(R.id.imageViewPass);
        imageViewEmail = (ImageView) findViewById(R.id.imageViewEmail);

        //========


    }

    //=========  set listener for button sign up ==============
    public void onClickSignUp(View v) {
        boolean checkUser = checkUsername();
        boolean checkPass = checkPassword();
        boolean checkEmail = checkEmail();
        //===== get gender =====
        int isCheck = radioGroup.getCheckedRadioButtonId();
        switch (isCheck) {
            case R.id.male:
                gender = "Male";
                break;
            case R.id.female:
                gender = "Female";
                break;
            default:
                gender = null;
                break;
        }

        if (checkUser && checkPass && checkEmail) {
            DBUsers dbUsers = new DBUsers(this);
            dbUsers.open();
            dbUsers.insertUser(username, password, email, gender, 0, 0);
            dbUsers.close();

            DBStock dbStock = new DBStock(this, username);
            dbStock.open();
            dbStock.close();

            Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(getBaseContext(), Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

    }

    //==========  check username  ==============
    public boolean checkUsername() {
        boolean result = false;
        username = edit_username.getText().toString();
        if (username.equals("")) {
            display(imageViewUser, 0);
        } else {
            if (searchUser(username)) {
                display(imageViewUser, 0);
            } else {
                display(imageViewUser, 1);
                result = true;
            }
        }
        return result;
    }

    //===========  check password  ==============
    public boolean checkPassword() {
        boolean result = false;
        password = edit_password.getText().toString();
        if (password.equals("")) {
            display(imageViewPass, 0);
        } else {
            display(imageViewPass, 1);
            result = true;
        }
        return result;
    }

    //============ check email  ===============
    public boolean checkEmail() {
        boolean result = false;
        email = edit_email.getText().toString();
        if (email.equals("")) {
            display(imageViewEmail, 0);
        } else {
            if (searchEmail(email)) {
                display(imageViewEmail, 0);
            } else {
                display(imageViewEmail, 1);
                result = true;
            }
        }
        return result;
    }

    //=========  search database  return true if user exist into database ==================
    public boolean searchUser(String username) {
        boolean result = false;
        DBUsers db = new DBUsers(this);
        db.open();
        Cursor cursor = db.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(username)) {
                    result = true;
                    break;
                }
            } while (cursor.moveToNext());
        }
       /* if(db.getUser(username)==null){
            result = false;
        }*/
        db.close();
        return result;
    }

    //========== search database    return true if email exist into database ==============
    public boolean searchEmail(String email) {
        boolean result = false;
        DBUsers db = new DBUsers(this);
        db.open();
        Cursor cursor = db.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(3).equals(email)) {
                    result = true;
                    break;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    //=========  display image view true or false =================
    public void display(ImageView imageView, int a) {
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(image[a]);
    }

}
