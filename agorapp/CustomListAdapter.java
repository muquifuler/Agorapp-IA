package com.agora.agorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {
    private Context context;
    private List<String> items;
    private List<Boolean> switchStates;

    public CustomListAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;

        switchStates = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            switchStates.add(false);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        SwitchCompat switchCompat;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textPropuesta);
            switchCompat = itemView.findViewById(R.id.switchPropuesta);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_view_propuestas, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String item = items.get(position);
        holder.textView.setText(item);

        boolean switchSt = switchStates.get(position);
        holder.switchCompat.setChecked(switchSt);

        holder.switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchStates.set(position, isChecked);

            if (isChecked) {
                System.out.println("Se marcó el ítem en la posición " + position + ": " + item);
            } else {
                System.out.println("Se desmarcó el ítem en la posición " + position + ": " + item);
            }

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Boolean> getSwitchStates() {
        return switchStates;
    }
}

