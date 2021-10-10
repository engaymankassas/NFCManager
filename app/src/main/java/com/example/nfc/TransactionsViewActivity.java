package com.example.nfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nfc.Models.NfcTransaction;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transactions View");
        getSupportActionBar().setHomeButtonEnabled(true);
        List<NfcTransaction> list= NFCApp.getInstance().getDaoSession().getNfcTransactionDao().loadAll();
        addAllresults(list);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return  true;
    }
    private void addAllresults(List<NfcTransaction> list) {
        TableLayout table = (TableLayout) findViewById(R.id.transactionsTable);

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);
        table.setWeightSum(2.0f);

        int count = table.getChildCount();
        if(count>1)
            for (int i = 0; i < count; i++) {
                View child = table.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }

        //table.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        for (NfcTransaction item : list) {
            TableRow tr = new TableRow(this);

            //tr.setBackgroundColor(getColor(R.color.table_raw));
            TextView b = new TextView(this);
            b.setText(item.getTransactionNo().toString());
            b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b.setGravity(Gravity.CENTER);
            b.setTypeface(Typeface.SERIF, Typeface.BOLD);
            b.setHeight(100);

            TextView b1 = new TextView(this);
            b1.setText( item.getBalanceNo()+"");
            b1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b1.setGravity(Gravity.CENTER);
            b1.setTypeface(Typeface.SERIF, Typeface.BOLD);
            b1.setHeight(100);

            TextView b6 = new TextView(this);
            b6.setText( android.text.format.DateFormat.format("yy-MM-dd", item.getInsertDate()));
            b6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b6.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b6.setGravity(Gravity.CENTER);
            b6.setTypeface(Typeface.SERIF, Typeface.BOLD);
            b6.setHeight(100);

            TextView b2 = new TextView(this);
            b2.setText(item.getOldBalance_USD()+"");
            b2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b2.setHeight(100);
            b2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b2.setGravity(Gravity.CENTER);
            b2.setTypeface(Typeface.SERIF, Typeface.BOLD);

            TextView b3 = new TextView(this);
            b3.setText(item.getNewBalance_USD()+"");
            b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b3.setHeight(100);
            b3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b3.setGravity(Gravity.CENTER);
            b3.setTypeface(Typeface.SERIF, Typeface.BOLD);

            TextView b4 = new TextView(this);
            b4.setText(new DecimalFormat("##.##").format(item.getNewBalance_EUR()));
            b4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b4.setHeight(100);
            b4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b4.setGravity(Gravity.CENTER);
            b4.setTypeface(Typeface.SERIF, Typeface.BOLD);

            TextView b5 = new TextView(this);
            b5.setText(item.getExchangeRate().toString());
            b5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b5.setHeight(100);
            b5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            b5.setGravity(Gravity.CENTER);
            b5.setTypeface(Typeface.SERIF, Typeface.BOLD);



            tr.addView(b);
            tr.addView(b1);
            tr.addView(b6);

            tr.addView(b2);
            tr.addView(b3);
            tr.addView(b4);
            tr.addView(b5);

            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setGravity(Gravity.CENTER);

            table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

}