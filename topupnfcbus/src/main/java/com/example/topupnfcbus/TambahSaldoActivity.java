package com.example.topupnfcbus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import com.example.topupnfcbus.Fragment.FragmentTambahSaldo;

public class TambahSaldoActivity extends AppCompatActivity implements Listener{

    public static final String TAG = TambahSaldoActivity.class.getSimpleName();
    Button button1,button2,button3,button4,button5,button6;
    private FragmentTambahSaldo mFragmentTambahSaldo;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private NfcAdapter mNfcAdapter;
    TextView jml_saldo;
    String jumlah = null;
    int hasil = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_saldo);
        initNFC();
        jml_saldo = findViewById(R.id.jmlSaldo);
        button1 = findViewById(R.id.btn5000);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                jumlah = "5000";
            }
        });
        button2 = findViewById(R.id.btn10000);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahSaldoActivity.this.showDialog();
                jumlah = "10000";
            }
        });
        button3 = findViewById(R.id.btn15000);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahSaldoActivity.this.showDialog();
                jumlah = "15000";
            }
        });
        button4 = findViewById(R.id.btn20000);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahSaldoActivity.this.showDialog();
                jumlah = "20000";
            }
        });
        button5 = findViewById(R.id.btn50000);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahSaldoActivity.this.showDialog();
                jumlah = "50000";
            }
        });
        button6 = findViewById(R.id.btn100000);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahSaldoActivity.this.showDialog();
                jumlah = "100000";
            }
        });


    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void showTambahFragment() {

        isWrite = true;

        mFragmentTambahSaldo = (FragmentTambahSaldo) getFragmentManager().findFragmentByTag(FragmentTambahSaldo.TAG);

        if (mFragmentTambahSaldo == null) {

            mFragmentTambahSaldo = FragmentTambahSaldo.newInstance();
        }
        mFragmentTambahSaldo.show(getFragmentManager(), FragmentTambahSaldo.TAG);

    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Apakah Anda Yakin Ingin Menambah Saldo?");

        alertDialogBuilder
                .setMessage("Jika Iya, Tekan Ya!")
                .setIcon(R.drawable.ic_nfc_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TambahSaldoActivity.this.showTambahFragment();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);
            try {


//                if (isDialogDisplayed) {

                    if (isWrite) {
                        hasil += Integer.valueOf(jumlah);
                        jumlah = String.valueOf(hasil);
                        String messageToWrite = jumlah + ",off";
                        mFragmentTambahSaldo = (FragmentTambahSaldo) getFragmentManager().findFragmentByTag(FragmentTambahSaldo.TAG);
                        mFragmentTambahSaldo.onNfcDetected(ndef, messageToWrite);


                    } else {
                        ndef.connect();
                        NdefMessage ndefMessage = ndef.getNdefMessage();
                        String message = new String(ndefMessage.getRecords()[0].getPayload());
//                       if(message.length()>1){
                        Log.d(TAG, "readFromNFC: " + message);
                        String[] dt = message.split(",");
                        if (dt.length > 1) {
                            hasil = Integer.valueOf(dt[0]);
                            Locale localeID = new Locale("in", "ID");
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                            jml_saldo.setText(formatRupiah.format(hasil));
                        } else {
                            jml_saldo.setText("Kartu Masih Kosong");
                        }
                        ndef.close();
                }

            } catch (IOException | FormatException e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                jml_saldo.setText(e.toString());
            }

        }

    }
}

