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

import java.util.ArrayList;
import java.util.List;

class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {

    private List<Plant> mPlantsList;
    private Context context;

    public  PlantsAdapter(Context context, List<Plant> mPlantsList) {
        this.context = context;
        this.mPlantsList = mPlantsList;
    }
    public  PlantsAdapter(Context context) {
        this.context = context;
        this.mPlantsList = new ArrayList<>();
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
                intent.putExtra("id",plant.getId());
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

    public void change(Plant plant) {
        int i = mPlantsList.indexOf(plant);
        if (i>=0) {
            mPlantsList.get(i).setHumidity(plant.getHumidity());
            notifyItemChanged(i);
        }
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
}
