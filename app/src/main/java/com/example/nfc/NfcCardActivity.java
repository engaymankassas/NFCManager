package com.example.nfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nfc.Models.BalanceInfo;
import com.example.nfc.Models.NfcTransaction;
import com.example.nfc.Utilities.Constants;
import com.example.nfc.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NfcCardActivity extends AppCompatActivity {

    private NfcAdapter adapter;
    PendingIntent pendingIntent;
    Tag tag;
    int balanceNo;
    int valueToPay;
    int oldBalance;
    List<BalanceInfo> list=new ArrayList<BalanceInfo>();

    boolean reset;
    boolean writeflag=false;

    Double usd_eur_value;
    TextView messagetxt;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_card);

        messagetxt=findViewById(R.id.messagetxt);
        messagetxt.setText("Please close the card from the phone");
        balanceNo= getIntent().getIntExtra("balanceNo",0);
        valueToPay= getIntent().getIntExtra("value",0);
        reset= getIntent().getBooleanExtra("reset",false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NFC Card Pay");

        NfcManager nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        adapter = nfcManager.getDefaultAdapter();
        Log.e("MainActivity", "adapter inititilized");

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            adapter.enableForegroundDispatch(this,pendingIntent,null,null);
        } catch (IllegalStateException ex) {
            Log.e("MainActivity" ,"Error enabling NFC foreground dispatch", ex);
        }
    }
    @Override
    protected void onPause() {
        try {
            adapter.disableForegroundDispatch(this);
        } catch (IllegalStateException ex) {
            Log.e("MainActivity" ,"Error disabling NFC foreground dispatch", ex);
        }
        super.onPause();
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            String ID = getID(tag);

            messagetxt.setText("Nfc Flag Deteced");
            ConnectTag(tag);
        }
    }

    private String getID(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');

        return sb.toString();
    }

    String ConnectTag(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                MifareClassic mifareTag = MifareClassic.get(tag);

                if(!reset)
                {

                    String info=readTag(mifareTag,balanceNo);
                    BalanceInfo b_info= Utilities.getModelFromString(info);



                    if(b_info!=null)
                    if(b_info.getBalance()>=valueToPay)
                    {
                        oldBalance=b_info.getBalance();
                        b_info.setOldBalance(oldBalance);
                        b_info.setBalance(b_info.getBalance()-valueToPay);
                        messagetxt.setText("Writing New Ballance "+b_info.getBalance()+"\n BalanceNo:"+b_info.getBalanceNo()+"\n ValueToPay:"+valueToPay);


                        writeTag(mifareTag,b_info);

                        if(writeflag)
                          list.add(b_info);



                        exchange_addTransactionsToDB(list);

                    }
                    else
                    {
                        messagetxt.setText("Balance is not enough , current balance "+b_info.getBalance()+"\n ValueToPay:"+valueToPay);


                    }
                    else
                    {

                        Toast.makeText(NfcCardActivity.this, "You need to reset and initial card before", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {

                    messagetxt.setText("Resetting Card..");


                    String info=readTag(mifareTag,1);
                    BalanceInfo b_info1= Utilities.getModelFromString(info);

                    if(b_info1==null)
                    {
                        b_info1=new BalanceInfo();
                        b_info1.setBalanceNo(Constants.Balance1No);
                        b_info1.setBalance(Constants.Balance1Balance);
                        b_info1.setOldBalance(0);

                    }
                    else
                    {

                        b_info1.setOldBalance(b_info1.getBalance());
                        b_info1.setBalanceNo(Constants.Balance1No);
                        b_info1.setBalance(Constants.Balance1Balance);
                    }
                    writeTag(mifareTag,b_info1);

                    if(writeflag)
                        list.add(b_info1);



                    info=readTag(mifareTag,2);
                    BalanceInfo b_info2= Utilities.getModelFromString(info);
                    if(b_info2==null) {
                        b_info2=new BalanceInfo();
                        b_info2.setBalanceNo(Constants.Balance2No);
                        b_info2.setBalance(Constants.Balance2Balance);
                        b_info2.setOldBalance(0);
                    }
                    else
                    {


                        b_info2.setOldBalance(b_info2.getBalance());
                        b_info2.setBalanceNo(Constants.Balance2No);
                        b_info2.setBalance(Constants.Balance2Balance);
                    }

                    writeTag(mifareTag,b_info2);
                    if(writeflag)
                        list.add(b_info2);


                    exchange_addTransactionsToDB(list);

                }

            }
        }
        Log.v("test", sb.toString());
        return sb.toString();
    }

    public void writeTag(MifareClassic mTag, BalanceInfo balance) {
        writeflag=false;
        try {
            mTag.connect();

            int BlockNo = 0;


            if (balance.getBalanceNo()==1)
                BlockNo = Constants.Balance1Block;
            else BlockNo =Constants.Balance2Block;

            String stringTowrite=balance.toJSONString();
            int remain=16-stringTowrite.length();
            for (int i=0;i<remain;i++)
                stringTowrite=stringTowrite+" ";

            boolean autha = mTag.authenticateSectorWithKeyB(Constants.CardSector, MifareClassic.KEY_DEFAULT);
            boolean authb = mTag.authenticateSectorWithKeyB(Constants.CardSector, MifareClassic.KEY_DEFAULT);

            if (autha && authb) {
                if(mTag.isConnected())
                {
                    mTag.writeBlock(BlockNo, stringTowrite.getBytes(Charset.forName("US-ASCII")));
                    Toast.makeText(NfcCardActivity.this, "Balance ("+balance.getBalanceNo()+") Card writing sucessfully", Toast.LENGTH_SHORT).show();
                    writeflag=true;
                }
            }
            if(writeflag==false)
            {
                Toast.makeText(NfcCardActivity.this, "Card Authentication failed", Toast.LENGTH_LONG).show();
messagetxt.setText("Card Authentication failed");

            }

            mTag.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NfcCardActivity.this, "writing or connecting failed", Toast.LENGTH_LONG).show();

        }
    }

    public String readTag(MifareClassic mTag,int BalanceNo) {
        try {
            int BlockNo = 0;


            if (BalanceNo==1)
                BlockNo = Constants.Balance1Block;
            else BlockNo = Constants.Balance2Block;

            mTag.connect();
            boolean autha=mTag.authenticateSectorWithKeyB(Constants.CardSector, MifareClassic.KEY_DEFAULT);
            boolean authb=mTag.authenticateSectorWithKeyB(Constants.CardSector, MifareClassic.KEY_DEFAULT);

            if (autha && authb)
            {

                byte[] payload = mTag.readBlock(BlockNo);


                //Toast.makeText(NfcCardActivity.this,"reading ...",Toast.LENGTH_SHORT).show();

                return new String(payload, Charset.forName("US-ASCII"));
            }

        } catch (IOException e) {
            Toast.makeText(NfcCardActivity.this, "reading or connecting failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (mTag != null) {
                try {
                    mTag.close();
                }
                catch (IOException e) {
                    Log.e("ReadNFC", "Error closing tag...", e);
                }
            }
        }
        return null;
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private void addTransactionsToDB(List<BalanceInfo> list, Double exchangeRate)
    {

        for (BalanceInfo item:list)
        {
            NfcTransaction model=new NfcTransaction();
            model.setBalanceNo(item.getBalanceNo());
            model.setExchangeRate(exchangeRate);
            model.setNewBalance_EUR(item.getBalance()/exchangeRate);
            model.setNewBalance_USD(item.getBalance());
            model.setOldBalance_USD(item.getOldBalance());
            model.setInsertDate(Calendar.getInstance().getTime());
            NFCApp.getInstance().getDaoSession().getNfcTransactionDao().insert(model);



        }
        Toast.makeText(NfcCardActivity.this,"Added to Db Sucessfully",Toast.LENGTH_SHORT).show();
        list.clear();


    }


    private void exchange_addTransactionsToDB(List<BalanceInfo> list) {


        if(list.size()==0) return;;
        String URL = Constants.EXCHANGEURL;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject  rates=res.getJSONObject("rates");
                    usd_eur_value= rates.getDouble("USD");

                    addTransactionsToDB(list,usd_eur_value);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NfcCardActivity.this,getString(R.string.connection_error),Toast.LENGTH_LONG).show();
            }
        }) ;




        NFCApp.getInstance().addToRequestQueue(request);


    }

}