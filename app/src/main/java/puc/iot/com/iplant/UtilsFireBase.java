package puc.iot.com.iplant;

import com.google.firebase.database.DatabaseReference;

public final class UtilsFireBase {
    public static DatabaseReference getUserPlantsReference(DatabaseReference database,String userId ){
       return database.child("user").child(userId).child("plants");
    }

    public static DatabaseReference getPlantsReference(DatabaseReference database ){
        return database.child("plants");
    }
}
