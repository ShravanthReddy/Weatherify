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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class weatherAdapterDailyForecast extends RecyclerView.Adapter<weatherAdapterDailyForecast.ViewHolder> {

    private Context context;
    private ArrayList<weatherDailyForecast> weatherDailyForecastArrayList = new ArrayList<>();

    public weatherAdapterDailyForecast(Context context, ArrayList<weatherDailyForecast> weatherDailyForecastArrayList) {
        this.context = context;
        this.weatherDailyForecastArrayList = weatherDailyForecastArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_weather_updates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        weatherDailyForecast component = weatherDailyForecastArrayList.get(position);
        holder.dailyForecastDayTV.setText(component.getDate());
        holder.minTemp.setText(component.getMinTemp() + "°");
        holder.maxTemp.setText(component.getMaxTemp() + "°");
        holder.dailyForecastConditionTV.setText(component.getCondition());
        Picasso.get().load(component.getConditionIcon()).into(holder.dailyForecastConditionIV);

        holder.humidityResultDailyTV.setText(component.getHumidity() + "%");
        holder.windSpeedResultDailyTV.setText(component.getWindSpeed() + " m/s");
        holder.sunriseResultDailyTV.setText(component.getSunRise() + ", " + component.getSunSet());

        boolean isExpanded = weatherDailyForecastArrayList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return weatherDailyForecastArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dailyForecastDayTV, dailyForecastConditionTV, maxTemp, minTemp, humidityResultDailyTV, windSpeedResultDailyTV, sunriseResultDailyTV, sunsetResultDailyTV;
        private ImageView dailyForecastConditionIV;
        private ConstraintLayout expandableLayout, titleLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            sunriseResultDailyTV = itemView.findViewById(R.id.sunriseResultDailyTV);
            windSpeedResultDailyTV = itemView.findViewById(R.id.windSpeedResultDailyTV);
            humidityResultDailyTV = itemView.findViewById(R.id.humidityResultDailyTV);
            titleLayout = itemView.findViewById(R.id.titleLayout);
            dailyForecastDayTV = itemView.findViewById(R.id.dailyForecastDayTV);
            dailyForecastConditionTV = itemView.findViewById(R.id.dailyForecastConditionTV);
            maxTemp = itemView.findViewById(R.id.maxTemp);
            minTemp = itemView.findViewById(R.id.minTemp);
            dailyForecastConditionIV = itemView.findViewById(R.id.dailyForecastConditionIV);

            titleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    weatherDailyForecast weatherDailyForecast = weatherDailyForecastArrayList.get(getAdapterPosition());
                    weatherDailyForecast.setExpanded(!weatherDailyForecast.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
