/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellSignalStrength;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
  Boolean signUpModeActive = true;
  TextView logInTextView;
  EditText userNameTextView;
  EditText passwordTextView;
  public void showUserList()
  {
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }
  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
    {
      singnUpClicked(view);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
  if (view.getId() == R.id.logInTextView)
  {
    Button signUpButton = findViewById(R.id.signUpButton);
    if (signUpModeActive){
      signUpModeActive = false;
      signUpButton.setText("Log in");
      logInTextView.setText("or,SignUp");
    }else {
      signUpModeActive = true;
      signUpButton.setText("SignUp");
      logInTextView.setText("or,SignIn");
    }
  }else if (view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundLayout)
  {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
  }
  }
  public void singnUpClicked(View view)
  {


    if (userNameTextView.getText().toString().matches("") || passwordTextView.getText().toString().matches(""))
    {
      Toast.makeText(this, "username/password required", Toast.LENGTH_SHORT).show();
    }else {
      if (signUpModeActive) {
        ParseUser user = new ParseUser();
        user.setUsername(userNameTextView.getText().toString());
        user.setPassword(passwordTextView.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Success", "Signup Successful");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }else {//login condition
        ParseUser.logInInBackground(userNameTextView.getText().toString(), passwordTextView.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user!=null)
            {
              Log.i("Log in success","Ok!");
              showUserList();
            }else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");
    userNameTextView = findViewById(R.id.userNameTextView);
    passwordTextView = findViewById(R.id.passwordEditText);
    logInTextView = findViewById(R.id.logInTextView);
    ConstraintLayout backgroundLayout = findViewById(R.id.backgroundLayout);
    ImageView logoImageView = findViewById(R.id.logoImageView);
    backgroundLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);
    logInTextView.setOnClickListener(this);
    passwordTextView.setOnKeyListener(this);
    if (ParseUser.getCurrentUser() != null)
    {
      showUserList();
    }
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}