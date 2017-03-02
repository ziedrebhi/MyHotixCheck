package hotix.myhotix.checkin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import hotix.myhotix.checkin.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnCustomIconTextTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCustomIconTextTabs = (Button) findViewById(R.id.btnCustomIconTabs);

        btnCustomIconTextTabs.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnCustomIconTabs:

                Intent intent = new Intent(MainActivity.this, ClientsActivity.class);
                intent.putExtra("NBR_CLIENTS", 5);
                startActivity(intent);
                break;
        }
    }
}
