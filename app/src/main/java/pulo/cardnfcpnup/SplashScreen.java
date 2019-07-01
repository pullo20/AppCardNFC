package pulo.cardnfcpnup;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent mainIntent = new Intent(SplashScreen.this,MenuHalteActivity.class);
//                    BackRun backRun = new BackRun();
//                    backRun.userLogin();
//                    backRun.getData();
                    startActivity(mainIntent);
                finish();
            }
        }, 1000);
    }
}
