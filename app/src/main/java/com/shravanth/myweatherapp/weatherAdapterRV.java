package com.shravanth.myweatherapp;

import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class weatherAdapterRV extends RecyclerView.Adapter<weatherAdapterRV.ViewHolder> {
    private Context context;
    private ArrayList<weatherComponents> weatherRVComponentsArrayList = new ArrayList<>();

    public weatherAdapterRV(Context context, ArrayList<weatherComponents> weatherRVComponentsArrayList) {
        this.context = context;
        this.weatherRVComponentsArrayList = weatherRVComponentsArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dailyforecast_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        weatherComponents component = weatherRVComponentsArrayList.get(position);
        Picasso.get().load(component.getIcon()).into(holder.IVCondition);
        //Toast.makeText(context, "" + component.getDayOrNight(), Toast.LENGTH_SHORT).show();
        char dayOrNight = component.getDayOrNight().charAt(component.getDayOrNight().length() - 1);
        int i = Character.compare(dayOrNight, 'd');
        if (i == 0) {
            holder.cardBackgroundRL.setBackground(ContextCompat.getDrawable(context, R.drawable.dailyforecast_bg_day));
        } else {
            holder.cardBackgroundRL.setBackground(ContextCompat.getDrawable(context, R.drawable.dailyforecast_bg_night));
        }

        holder.windSpeedResultHourly.setText(component.getWindSpeed() + " m/s");
        holder.TVTemperature.setText(component.getTemperature() + "°C");
        holder.TVTime.setText(component.getTime());
        holder.humidityResultHourly.setText(component.getHumidity()+ "%");
        holder.pressureResultHourly.setText(component.getPressure() + "hPa");
        holder.dewPointResultHourly.setText(component.getDewPoint() + "°C");

        boolean isExpanded = weatherRVComponentsArrayList.get(position).isExpanded();
        holder.expandableLayoutHourly.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return weatherRVComponentsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TVTime, TVTemperature, windSpeedResultHourly, humidityResultHourly, pressureResultHourly, dewPointResultHourly;
        private ImageView IVCondition;
        private ConstraintLayout cardBackgroundRL;
        private ConstraintLayout titleLayoutHourly, expandableLayoutHourly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TVTemperature = itemView.findViewById(R.id.TVTemperature);
            titleLayoutHourly = itemView.findViewById(R.id.titleLayoutHourly);
            expandableLayoutHourly = itemView.findViewById(R.id.expandableLayoutHourly);
            cardBackgroundRL = itemView.findViewById(R.id.cardBackgroundRL);
            TVTime = itemView.findViewById(R.id.TVTime);
            pressureResultHourly = itemView.findViewById(R.id.pressureResultHourly);
            dewPointResultHourly = itemView.findViewById(R.id.dewPointResultHourly);
            humidityResultHourly = itemView.findViewById(R.id.humidityResultHourly);

            windSpeedResultHourly = itemView.findViewById(R.id.windSpeedResultHourly);

            IVCondition = itemView.findViewById(R.id.IVCondition);
            int previousExpandedPosition = -1;

            titleLayoutHourly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    weatherComponents weatherDailyForecast = weatherRVComponentsArrayList.get(getAdapterPosition());

                    weatherDailyForecast.setExpanded(!weatherDailyForecast.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });

        }
    }
}