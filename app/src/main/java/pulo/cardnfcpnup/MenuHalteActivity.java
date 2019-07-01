package pulo.cardnfcpnup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MenuHalteActivity extends AppCompatActivity {
public static String halte,Mhalte;
public static int harga = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_halte);

        findViewById(R.id.btn_halte1).setOnClickListener(v -> {
            halte = "halte1";
            Mhalte = "HALTE 2";
            pindah();
        });
        findViewById(R.id.btn_halte2).setOnClickListener(v -> {
//            harga = 10000;
            halte = "halte2";
            Mhalte = "HALTE 2";
            pindah();
        });
        findViewById(R.id.btn_halte3).setOnClickListener(v -> {
//            harga = 15000;
            halte = "halte3";
            Mhalte = "HALTE 3";
            pindah();
        });
        findViewById(R.id.btn_halte4).setOnClickListener(v -> {
//            harga = 20000;
            halte = "halte4";
            Mhalte = "HALTE 4";
            pindah();
        });
    }
    private void pindah()
    {
        Intent pnup = new Intent(getApplicationContext(),TransaksiActivity.class);
        startActivity(pnup);
    }
}
