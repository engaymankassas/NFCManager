package com.example.nfc.Models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;

@Entity
public class NfcTransaction {
    @Id(autoincrement = true)
    @Index(unique = true)
    Long TransactionNo;

    Date InsertDate;
    int BalanceNo;
    int OldBalance_USD;
    int NewBalance_USD;
    Double NewBalance_EUR;
    Double ExchangeRate;
    @Generated(hash = 1332605399)
    public NfcTransaction(Long TransactionNo, Date InsertDate, int BalanceNo,
            int OldBalance_USD, int NewBalance_USD, Double NewBalance_EUR,
            Double ExchangeRate) {
        this.TransactionNo = TransactionNo;
        this.InsertDate = InsertDate;
        this.BalanceNo = BalanceNo;
        this.OldBalance_USD = OldBalance_USD;
        this.NewBalance_USD = NewBalance_USD;
        this.NewBalance_EUR = NewBalance_EUR;
        this.ExchangeRate = ExchangeRate;
    }
    @Generated(hash = 332716483)
    public NfcTransaction() {
    }
    public Long getTransactionNo() {
        return this.TransactionNo;
    }
    public void setTransactionNo(Long TransactionNo) {
        this.TransactionNo = TransactionNo;
    }
    public Date getInsertDate() {
        return this.InsertDate;
    }
    public void setInsertDate(Date InsertDate) {
        this.InsertDate = InsertDate;
    }
    public int getBalanceNo() {
        return this.BalanceNo;
    }
    public void setBalanceNo(int BalanceNo) {
        this.BalanceNo = BalanceNo;
    }
    public int getOldBalance_USD() {
        return this.OldBalance_USD;
    }
    public void setOldBalance_USD(int OldBalance_USD) {
        this.OldBalance_USD = OldBalance_USD;
    }
    public int getNewBalance_USD() {
        return this.NewBalance_USD;
    }
    public void setNewBalance_USD(int NewBalance_USD) {
        this.NewBalance_USD = NewBalance_USD;
    }
    public Double getNewBalance_EUR() {
        return this.NewBalance_EUR;
    }
    public void setNewBalance_EUR(Double NewBalance_EUR) {
        this.NewBalance_EUR = NewBalance_EUR;
    }
    public Double getExchangeRate() {
        return this.ExchangeRate;
    }
    public void setExchangeRate(Double ExchangeRate) {
        this.ExchangeRate = ExchangeRate;
    }
   
}
