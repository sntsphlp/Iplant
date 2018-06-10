package puc.iot.com.iplant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class PlantRegistrationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_QR_CODE = 0,REQUEST_CODE_PLANT_PHOTO=1;
    private String mId;
    private EditText editTextEquipmentCode,editTextName;
    private AutoCompleteTextView aCTextViewPlantType;
    private ImageView imageViewScanCode,imageViewPlantPhoto;
    private Button buttonSave,buttonCancel;
    private Bitmap mPhotoBitmap;
    private CoordinatorLayout content;
    private DatabaseReference mDatabase;
    private String userId;
    private List<String> mTypes;
    private Plant mPlant;
    private boolean mIsNew=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_registration);
        editTextEquipmentCode = findViewById(R.id.editTextEquipmentCode);
        editTextName = findViewById(R.id.editTextName);
        imageViewScanCode = findViewById(R.id.imageViewScanCode);
        imageViewPlantPhoto = findViewById(R.id.imageViewPlantPhoto);
        aCTextViewPlantType= findViewById(R.id.aCTextViewPlantType);
        buttonSave= findViewById(R.id.buttonSave);
        buttonCancel= findViewById(R.id.buttonCancel);
        content = findViewById(R.id.content);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
            userId = user.getUid();
        else {
            Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_LONG).show();
            finish();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imageViewScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantRegistrationActivity.this,QRCodeScannerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_QR_CODE);
            }
        });
        imageViewPlantPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE_PLANT_PHOTO);
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true){
                    PlantRegistrationActivity.this.finish();
                }else{

                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    save();
            }
        });
        editTextEquipmentCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String equipmentCode = s.toString();
                if (equipmentCode.length()>=8)
                    getValues(equipmentCode);
            }
        });
        getTypes();
    }

    private void save() {
        String equipmentCode = editTextEquipmentCode.getText().toString();
        String plantName = editTextName.getText().toString();
        String plantType = aCTextViewPlantType.getText().toString();
        if (equipmentCode.length()==0){
            makeSnackBar(R.string.equipment_code_is_required);
            return;
        }
        if (equipmentCode.length()<8){
            makeSnackBar(R.string.wrong_equipment_code);
            return;
        }
        if (plantName.length()==0){
            makeSnackBar(R.string.plant_name_is_required);
            return;
        }
        Plant plant = new Plant(equipmentCode,plantName);
        if (plantType.length()>0){
            plant.setType(plantType);
        }
        plant.setHumidity(0f);
        UtilsFireBase.getPlantsReference().child(equipmentCode).setValue(plant);
        UtilsFireBase.getUserPlantsReference(userId).child(equipmentCode).setValue(equipmentCode);
        if (mTypes!=null){
            if (!mTypes.contains(plantType))
            UtilsFireBase.getPlantsTypesReference().push().setValue(plantType);
        }
    }

    private void makeSnackBar(int value) {
        Snackbar mySnackBar = Snackbar.make(findViewById(R.id.content),
                value, Snackbar.LENGTH_SHORT);
        mySnackBar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_QR_CODE:{
                if (resultCode==RESULT_OK){
                    String equipmentCode=data.getExtras().getString(QRCodeScannerActivity.RESULT_ACTIVITY);
                    editTextEquipmentCode.setText(equipmentCode);
                    getValues(equipmentCode);
                }else {
                    Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
                }
            }case REQUEST_CODE_PLANT_PHOTO:{
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    mPhotoBitmap = (Bitmap) extras.get("data");
                    imageViewPlantPhoto.setImageBitmap(mPhotoBitmap);
                }
            }
        }
    }

    public void getTypes() {
        UtilsFireBase.getPlantsTypesReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] types = dataSnapshot.getValue(String[].class);
                if (types!=null) {
                    mTypes = Arrays.asList(types);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, types);
                    aCTextViewPlantType.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getValues(String equipmentCode) {
        UtilsFireBase.getPlantReference(equipmentCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Plant plant = dataSnapshot.getValue(Plant.class);
                if (plant!=null) {
                    mPlant = plant;
                    editTextName.setText(plant.getName());
                    aCTextViewPlantType.setText(plant.getType());
                    mIsNew = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
