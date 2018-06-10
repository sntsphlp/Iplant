package puc.iot.com.iplant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public final class UtilsFireBase {
    public static DatabaseReference getUserPlantsReference(String userId ){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        return database.child("user").child(userId).child("plants");
    }

    public static DatabaseReference getPlantsReference(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        return database.child("plants");
    }

    public static DatabaseReference getPlantsTypesReference() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        return database.child("types");
    }

    public static DatabaseReference getPlantReference(String equipmentCode) {
        DatabaseReference plantsReference = getPlantsReference();
        return plantsReference.child(equipmentCode);
    }
}
