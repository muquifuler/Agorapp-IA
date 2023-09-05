package com.agora.agorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.agora.agorapp.databinding.ChooseBinding;

public class Choose extends Fragment  {

    private ChooseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ChooseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnPSOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynamic.recibirMensaje("PSOE");
                NavHostFragment.findNavController(Choose.this)
                        .navigate(R.id.action_Choose_to_Dynamic);
            }
        });

        binding.btnPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynamic.recibirMensaje("PP");
                NavHostFragment.findNavController(Choose.this)
                        .navigate(R.id.action_Choose_to_Dynamic);
            }
        });

        binding.btnVOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynamic.recibirMensaje("VOX");
                NavHostFragment.findNavController(Choose.this)
                        .navigate(R.id.action_Choose_to_Dynamic);
            }

        });

        binding.btnSUMAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynamic.recibirMensaje("SUMAR");
                NavHostFragment.findNavController(Choose.this)
                        .navigate(R.id.action_Choose_to_Dynamic);
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}