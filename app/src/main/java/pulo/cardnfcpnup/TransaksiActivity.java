package pulo.cardnfcpnup;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Locale;

public class  TransaksiActivity extends AppCompatActivity  {

    TextView jmlSaldo,ket,POShalte;
    public static final String TAG = TransaksiActivity.class.getSimpleName();
    private NfcAdapter mNfcAdapter;
    String status= null;
    int hasil =0;
    String[] data = null;

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        jmlSaldo = findViewById(R.id.jmlSaldo);
        ket = findViewById(R.id.ket);
        ket.setText(R.string.message_tap_tag);
        POShalte = findViewById(R.id.halte);
        POShalte.setText(MenuHalteActivity.Mhalte);
        initNFC();

        ImageView home = (ImageView)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        findViewById(R.id.btn_halte1).setOnClickListener(v -> {
//
//        });
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
//    private void showTambahFragment() {
//
////        isWrite = true;
//
//        fragmentTransaksi = (FragmentTransaksi) getFragmentManager().findFragmentByTag(FragmentTransaksi.TAG);
//
//        if (fragmentTransaksi== null) {
//
//            fragmentTransaksi = FragmentTransaksi.newInstance();
//        }
//        fragmentTransaksi.show(getFragmentManager(), FragmentTransaksi.TAG);
//
//    }

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
            try{

                ndef.connect();
                baca_data(ndef);
                if (data[1].equals("off"))
                {
                    if (hasil > MenuHalteActivity.harga){

                        status = data[0]+",on,"+MenuHalteActivity.halte;
                        simpan_data(ndef,status);
                        jmlSaldo.setText("SALDO:"+formatRupiah.format(hasil));
                        baca_data(ndef);
                        ket.setText("KARTU ON");
                    }else {
                        ket.setText("SALDO TIDAK MENCUKUPI");
                    }


                }
                else if (data[1].equals("on") && MenuHalteActivity.halte.equals("halte1"))
                {

                    if (data[2].equals("halte2"))
                    {
                        hasil -= MenuHalteActivity.harga * 3;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    } else if (data[2].equals("halte3"))
                    {
                        hasil -= MenuHalteActivity.harga * 2;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte4"))
                    {
                        hasil -= MenuHalteActivity.harga;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }
                }else if (data[1].equals("on") && MenuHalteActivity.halte.equals("halte2"))
                {
                    if (data[2].equals("halte3")){
                        hasil -= MenuHalteActivity.harga * 3;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte4"))
                    {
                        hasil -= MenuHalteActivity.harga * 2;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte1"))
                    {
                        hasil -= MenuHalteActivity.harga;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }
                }else if (data[1].equals("on") && MenuHalteActivity.halte.equals("halte3"))
                {
                    if (data[2].equals("halte4")){
                        hasil -= MenuHalteActivity.harga * 3;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte1"))
                    {
                        hasil -= MenuHalteActivity.harga * 2;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte2"))
                    {
                        hasil -= MenuHalteActivity.harga;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }

                }else if (data[1].equals("on") && MenuHalteActivity.halte.equals("halte4"))
                {
                    if (data[2].equals("halte1")){
                        hasil -= MenuHalteActivity.harga * 3;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte2"))
                    {
                        hasil -= MenuHalteActivity.harga * 2;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }else if (data[2].equals("halte3"))
                    {
                        hasil -= MenuHalteActivity.harga;
                        status = hasil + ",off";
                        jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
                        simpan_data(ndef, status);
                        ket.setText("KARTU OFF");
                    }
                }
                ndef.close();
            } catch (IOException | FormatException e) {
                e.printStackTrace();

            }
            }
        }
//        private void transaksi()
//        {
//
//                hasil -= MenuHalteActivity.harga;
//                status = hasil + ",off";
//                jmlSaldo.setText("SALDO:" + formatRupiah.format(hasil));
//
//
//        }
        private void simpan_data(Ndef ndef, String pesan) {
            if (ndef != null) {
                try {
//
                    NdefRecord mimeRecord = NdefRecord.createMime("text/plain", pesan.getBytes(Charset.forName("US-ASCII")));
                    ndef.writeNdefMessage(new NdefMessage(mimeRecord));
//
                    ket.setText(getString(R.string.message_write_success));

                } catch (IOException | FormatException e) {
                    e.printStackTrace();
                    ket.setText(getString(R.string.message_write_error));
                }
            }
        }
        private void baca_data(Ndef ndef) throws IOException, FormatException {
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);
            data = message.split(",");
            hasil = Integer.valueOf(data[0]);
        }
}

