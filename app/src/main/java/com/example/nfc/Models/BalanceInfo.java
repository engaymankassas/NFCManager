package com.example.nfc.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

public class BalanceInfo {
    private int BalanceNo ;
    private int Balance ;
    private int OldBalance ;

    public int getBalanceNo() {
        return BalanceNo;
    }

    public void setBalanceNo(int balanceNo) {
        BalanceNo = balanceNo;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }

    public String toJSONString() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("no", BalanceNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jo.put("v", Balance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo.toString();
    }


    public int getOldBalance() {
        return OldBalance;
    }

    public void setOldBalance(int oldBalance) {
        OldBalance = oldBalance;
    }
}
