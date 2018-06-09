package puc.iot.com.iplant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        UtilsFireBase.getPlantsReference(mDatabase).child(equipmentCode).child(userId).setValue(userId);
        UtilsFireBase.getUserPlantsReference(mDatabase,userId).child(equipmentCode).setValue(plant);

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
}
