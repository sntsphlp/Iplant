package puc.iot.com.iplant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {

    private List<Plant> mPlantsList;
    private List<String> mIdsList;
    private Context context;

    public  PlantsAdapter(Context context) {
        this.context = context;
        mPlantsList = new ArrayList<>();
        mIdsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_plant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PlantsAdapter.ViewHolder holder, int i) {
        final Plant plant = mPlantsList.get(i);
        holder.textViewPlantName.setText(plant.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context,PlantActivity.class);
                intent.putExtra(Plant._ID,plant.getId());
                intent.putExtra(Plant.NAME,plant.getName());
                intent.putExtra(Plant.HUMIDITY,plant.getHumidity());
                intent.putExtra(Plant.IS_OPEN_TAP,plant.isOpen_tap());

                context.startActivity(intent);
            }
        });

        float humidity = plant.getHumidity();
        holder.textViewHumidity.setText(humidity+"%");
        if (humidity<Plant.DRY){
            holder.content.setBackgroundColor(ContextCompat.getColor(context,R.color.colorDesertPrimary));
        }else if (humidity<Plant.NORMAL) {
            holder.content.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGreenPrimary));
        }else if (humidity>Plant.HUMID) {
            holder.content.setBackgroundColor(ContextCompat.getColor(context,R.color.colorHumidPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return mPlantsList.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPlantName,textViewLastWater,textViewHumidity;
        private ConstraintLayout content;

        ViewHolder(View itemView) {
            super(itemView);
            textViewPlantName=itemView.findViewById(R.id.textViewPlantName);
            textViewLastWater=itemView.findViewById(R.id.textViewLastWater);
            textViewHumidity=itemView.findViewById(R.id.textViewHumidity);
            content =itemView.findViewById(R.id.content);


        }
    }
    public void add(Plant plant){
        if (mPlantsList!=null){
            mPlantsList.add(plant);
            notifyItemInserted(mPlantsList.size() - 1);
        }
    }


    public void add(String newPlantId) {
        Plant plant = new Plant(newPlantId);
        mPlantsList.add(plant);
        int position = mPlantsList.size() - 1;
        getPlatValues(position);
        notifyItemInserted(position);
    }

    public void remove(String removedPlantId) {
        Plant plant = new Plant(removedPlantId);
        mPlantsList.indexOf(plant);
    }

    private void getPlatValues(final int position) {

        Plant plant = mPlantsList.get(position);
        DatabaseReference userPlants = UtilsFireBase.getPlantReference(plant.getId());
        userPlants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Plant newPlant = dataSnapshot.getValue(Plant.class);
                assert newPlant != null;
                mPlantsList.get(position).update(newPlant);
                notifyItemChanged(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
