package puc.iot.com.iplant;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PlantActivity extends AppCompatActivity {

    private TextView textViewType,textViewLastWater,textViewStatus;
    private ToggleButton toggleButton;
    private Plant mPlant;
    private Toolbar toolbar;
    private ImageView imageViewGround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        toolbar = findViewById(R.id.toolbar);
        textViewType = findViewById(R.id.textViewType);
        textViewLastWater = findViewById(R.id.textViewLastWater);
        textViewStatus = findViewById(R.id.textViewStatus);
        toggleButton = findViewById(R.id.toggleButton);
        imageViewGround= findViewById(R.id.imageViewGround);

        getIntentValues();
        getPlatValues();
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mPlant.setOpen_tap(true);
                else
                    mPlant.setOpen_tap(false);
                UtilsFireBase.getPlantReference(mPlant.getId())
                        .child("open_tap")
                        .setValue(mPlant.isOpen_tap());
                update();
            }
        });
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        if (intent.hasExtra(Plant._ID)){
            String id= Objects.requireNonNull(intent.getExtras()).getString(Plant._ID);
            mPlant = new Plant(id);
        }else {
            Toast.makeText(getApplicationContext(),"ERRO",Toast.LENGTH_LONG).show();
            this.finish();
        }

        if (intent.hasExtra(Plant.NAME)){
            String name= Objects.requireNonNull(intent.getExtras()).getString(Plant.NAME);
            mPlant.setName(name);
            toolbar.setTitle("Nome");
            setSupportActionBar(toolbar);
        }
        if (intent.hasExtra(Plant.HUMIDITY)){
            Float humidity= Objects.requireNonNull(intent.getExtras()).getFloat(Plant.HUMIDITY);
            mPlant.setHumidity(humidity);
        }
        if (intent.hasExtra(Plant.IS_OPEN_TAP)){
            boolean is_open_tap = Objects.requireNonNull(intent.getExtras()).getBoolean(Plant.IS_OPEN_TAP);
            mPlant.setOpen_tap(is_open_tap);
        }
    }

    private void getPlatValues() {
        if (mPlant!=null) {
            DatabaseReference userPlants = UtilsFireBase.getPlantReference(mPlant.getId());
            userPlants.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Plant newPlant = dataSnapshot.getValue(Plant.class);
                    assert newPlant != null;
                    mPlant.update(newPlant);
                    update();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void update() {
        if (mPlant!=null){
            toolbar.setTitle(mPlant.getName());
            textViewType.setText(mPlant.getType());
            textViewStatus.setText(getStatusString(mPlant.getStatus()));
            setBackGround(mPlant.getStatus());
        }
    }


    private String getStatusString(int status) {
        return "Implementar";
    }
    private void setBackGround(int status){
        if (status==Plant.DRY){
            imageViewGround.setImageResource(R.drawable.desert_ground);
        }else if (status==Plant.NORMAL){
            imageViewGround.setImageResource(R.drawable.normal_ground);
        }else if (status==Plant.HUMID){
            imageViewGround.setImageResource(R.drawable.humid_ground);
        }else if (status==Plant.WATERING_NORMAL){
            imageViewGround.setImageResource(R.drawable.water_ground);
        }else if (status==Plant.WATERING_HUMID){
            imageViewGround.setImageResource(R.drawable.humid_ground);
        }
    }
}
