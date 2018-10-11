package com.example.dickyeka.garasibuku;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DICKYEKA on 20/05/2017.
 */

public class ServerResponse {
    // variable name should be same as in the json response from php    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    String getMessage() {
        return message;
    }

    boolean getSuccess() {
        return success;
    }
}
