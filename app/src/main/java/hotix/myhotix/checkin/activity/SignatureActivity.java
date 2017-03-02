package hotix.myhotix.checkin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hotix.myhotix.checkin.SignatureMainLayout;

public class SignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SignatureMainLayout(this));

    }
}
