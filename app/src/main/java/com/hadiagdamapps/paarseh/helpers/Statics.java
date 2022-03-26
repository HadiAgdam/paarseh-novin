package com.hadiagdamapps.paarseh.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hadiagdamapps.paarseh.activity.login.LoginActivity;

public class Statics {

    public static void backToLogin(Context self){
        Intent intent = new Intent(self, LoginActivity.class);
        self.startActivity(intent);
    }

    public static final String BASE_URL = "https://hadiagdam.pythonanywhere.com/";
}
