package puc.iot.com.iplant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {

    private List<Plant> mPlantsList;

    public  PlantsAdapter(List<Plant> mPlantsList) { this.mPlantsList = mPlantsList; }

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
}
