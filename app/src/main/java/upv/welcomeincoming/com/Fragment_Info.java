package upv.welcomeincoming.com;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import upv.welcomeincoming.com.infoFragments.Activity_Asignaturas;
import upv.welcomeincoming.com.infoFragments.Activity_Escuelas;
import upv.welcomeincoming.com.infoFragments.Activity_Transportes;
import upv.welcomeincoming.com.infoFragments.Activity_Upv;
import upv.welcomeincoming.com.infoFragments.Activity_Valencia;


public class Fragment_Info extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        LinearLayout infoupv = (LinearLayout) view.findViewById(R.id.linearInfoUpv);
        infoupv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Upv.class));
            }
        });
        LinearLayout asignaturas = (LinearLayout) view.findViewById(R.id.linearAsignaturas);
        asignaturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Asignaturas.class));
            }
        });
        LinearLayout escuelas = (LinearLayout) view.findViewById(R.id.linearEscuelas);
        escuelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Escuelas.class));
            }
        });
        LinearLayout infovalencia = (LinearLayout) view.findViewById(R.id.linearInfo);
        infovalencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Valencia.class));
            }
        });
        LinearLayout transportes = (LinearLayout) view.findViewById(R.id.linearTransporte);
        transportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Transportes.class));
            }
        });
        return view;
    }
}