package com.example.topupnfcbus.Fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.nio.charset.Charset;

import com.example.topupnfcbus.Listener;
import com.example.topupnfcbus.TambahSaldoActivity;
import com.example.topupnfcbus.R;

public class FragmentTambahSaldo extends DialogFragment {
    
    public static final String TAG = FragmentTambahSaldo.class.getSimpleName();

    public static FragmentTambahSaldo newInstance() {

        return new FragmentTambahSaldo();
    }

    private TextView mTvMessage;
    private ProgressBar mProgress;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,true);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = view.findViewById(R.id.tv_message);
        mProgress =  view.findViewById(R.id.progress);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (TambahSaldoActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef, String messageToWrite){

        mProgress.setVisibility(View.VISIBLE);
        writeToNfc(ndef,messageToWrite);
    }

    private void writeToNfc(Ndef ndef, String message){

        mTvMessage.setText(getString(R.string.message_write_progress));
        if (ndef != null) {

            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                //Write Successful
                mTvMessage.setText(getString(R.string.message_write_success));


            } catch (IOException | FormatException e) {
                e.printStackTrace();
                mTvMessage.setText(getString(R.string.message_write_error));

            } finally {
                mProgress.setVisibility(View.GONE);
            }

        }
    }
}
