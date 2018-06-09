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

public class PlantRegistrationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_QR_CODE = 0,REQUEST_CODE_PLANT_PHOTO=1;
    private String mId;
    private EditText editTextEquipmentCode,editTextName;
    private AutoCompleteTextView aCTextViewPlantType;
    private ImageView imageViewScanCode,imageViewPlantPhoto;
    private Button buttonSave,buttonCancel;
    private Bitmap mPhotoBitmap;
    private CoordinatorLayout content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_registration);
        editTextEquipmentCode = findViewById(R.id.editTextEquipmentCode);
        imageViewScanCode = findViewById(R.id.imageViewScanCode);
        imageViewPlantPhoto = findViewById(R.id.imageViewPlantPhoto);
        buttonSave= findViewById(R.id.buttonSave);
        buttonCancel= findViewById(R.id.buttonCancel);
        content = findViewById(R.id.content);

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
                if (validate()){
                    save();
                }
            }
        });
    }

    private boolean validate() {
        if (editTextEquipmentCode.getText().length()<3){
            Snackbar.make(content,getText(R.string.invalid_equipment_code),Snackbar.LENGTH_LONG).show();
            return false;
        }else if (editTextName.getText().length()<1){
            Snackbar.make(content,getText(R.string.name_your_plant),Snackbar.LENGTH_LONG).show();
            return false;
        }else return true;
    }

    private void save() {

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
