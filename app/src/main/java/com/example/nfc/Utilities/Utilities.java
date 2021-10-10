package com.example.nfc.Utilities;

import android.text.TextUtils;

import com.example.nfc.Models.BalanceInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilities {

    public static BalanceInfo getModelFromString(String str)  {

        BalanceInfo model=new BalanceInfo();

        if(TextUtils.isEmpty(str)) return null;
        JSONObject res = null;
        try {
            res = new JSONObject(str);
            model.setBalance(res.getInt("v"));
            model.setBalanceNo(res.getInt("no"));
            return  model;
        } catch (JSONException e) {

            e.printStackTrace();
            return  null;
        }



    }

}
