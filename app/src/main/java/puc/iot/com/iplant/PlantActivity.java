package puc.iot.com.iplant;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PlantActivity extends AppCompatActivity {

    private TextView textViewName,textViewType,textViewLastWater,textViewStatus;
    private String id;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewName = findViewById(R.id.textViewName);
        textViewType = findViewById(R.id.textViewType);
        textViewLastWater = findViewById(R.id.textViewLastWater);
        textViewStatus = findViewById(R.id.textViewStatus);
        toggleButton = findViewById(R.id.toggleButton);
        //toolbar.setTitle("Nome");
        Intent intent = getIntent();
        if (intent.hasExtra("id")){
            id=intent.getExtras().getString("id");
        }else {
            Toast.makeText(getApplicationContext(),"ERRO",Toast.LENGTH_LONG).show();
            this.finish();
        }
        Notifications.needTurnOnWater(getApplicationContext(),id,"Cacto da janela");
        String[] ids =  {"dfd","ewtew","efefs","saggd"};
        Notifications.needTurnOnWater(getApplicationContext(),ids);
        Notifications.needTurnOffWater(getApplicationContext(),id,"Cacto da janela");
    }

}
