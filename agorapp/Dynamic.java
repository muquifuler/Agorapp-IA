package com.agora.agorapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agora.agorapp.databinding.DynamicBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dynamic extends Fragment {

    private DynamicBinding binding;
    private static String mensaje = ""; // titulo
    public static LinearLayout contentIA;
    public static TextView textIA;
    private static TextView generandoRes2;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private CustomListAdapter adapterS;
    private CustomListAdapter adapterE;
    private CustomListAdapter adapterI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DynamicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    static public void recibirMensaje(String mensajeRecibido) {
        mensaje = mensajeRecibido;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentDynaminc();

        generandoRes2 = binding.generandoRes2;
        contentIA = binding.contentIA;
        textIA = binding.textIA;
        EditText editText = binding.editTextTextPersonName;

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    callAI();
                    return true;
                }
                return false;
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { callAI(); }
        });

        binding.closeIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentIA.setVisibility(View.GONE);
            }
        });
    }

    private void callAI(){
        generandoRes2.setVisibility(View.VISIBLE);
        contentIA.setVisibility(View.GONE);

        EditText editText = binding.editTextTextPersonName;

        hideKeyboard(editText);
        String enteredText = editText.getText().toString();

        String[] propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_PSOE);
        String[] propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_PSOE);
        String[] propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_PSOE);

        if(mensaje.equals("PP")){
            propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_PP);
            propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_PP);
            propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_PP);
        }else if(mensaje.equals("VOX")){
            propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_VOX);
            propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_VOX);
            propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_VOX);
        }else if(mensaje.equals("SUMAR")){
            propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_SUMAR);
            propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_SUMAR);
            propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_SUMAR);
        }

        List<String> itemsS = new ArrayList<>(Arrays.asList(propuestas_sociedad));
        List<String> itemsE = new ArrayList<>(Arrays.asList(propuestas_economia));
        List<String> itemsI = new ArrayList<>(Arrays.asList(propuestas_internacional));

        int id = 1;

        String textosMarcados = "";
        for (int i = 0; i < adapterS.getSwitchStates().size(); i++) {
            if (adapterS.getSwitchStates().get(i)) {
                textosMarcados = textosMarcados+(id)+". "+itemsS.get(i)+" ";
                id++;

            }
        }
        for (int i = 0; i < adapterE.getSwitchStates().size(); i++) {
            if (adapterE.getSwitchStates().get(i)) {
                textosMarcados = textosMarcados+(id)+". "+itemsE.get(i)+" ";
                id++;
            }
        }
        for (int i = 0; i < adapterI.getSwitchStates().size(); i++) {
            if (adapterI.getSwitchStates().get(i)) {
                textosMarcados = textosMarcados+(id)+". "+itemsI.get(i)+" ";
                id++;
            }
        }

        if(enteredText.equals("")) enteredText = "dame una explicación";

        if(!textosMarcados.equals("")){
            new ChatGPTTask().execute("Propuestas: "+textosMarcados+" Instrucciones adicionales: ya se que no tienes opiniones personales, se objetivo, "+enteredText);
        }else{
            String sociedad = String.join(" ", itemsS);
            String economia = String.join(" ", itemsE);
            String internacional = String.join(" ", itemsI);

            new ChatGPTTask().execute("Propuestas: "+sociedad.substring(0, 1100)+" "+economia.substring(0, 1100)+" "+internacional.substring(0, 1100)+" Instrucciones adicionales: ya se que no tienes opiniones personales, se objetivo, "+enteredText);
        }
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @SuppressLint("ResourceAsColor")
    public void setContentDynaminc(){
        Button titleDynamic = binding.titleDynamic;
        TextView fuente = binding.fuente;

        String urlTemplate = getString(R.string.url);
        SpannableString spannableString = new SpannableString(urlTemplate);

        Button btnContract1 = binding.btnContract1;
        Button btnContract2 = binding.btnContract2;
        Button btnContract3 = binding.btnContract3;

        titleDynamic.setText(mensaje);

        List<String> itemsS = new ArrayList<>();
        List<String> itemsE = new ArrayList<>();
        List<String> itemsI = new ArrayList<>();

        if(mensaje.equals("PSOE")){
            titleDynamic.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PSOE));
            btnContract1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PSOE));
            btnContract2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PSOE));
            btnContract3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PSOE));

            String[] propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_PSOE);
            itemsS.addAll(Arrays.asList(propuestas_sociedad));

            String[] propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_PSOE);
            itemsE.addAll(Arrays.asList(propuestas_economia));

            String[] propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_PSOE);
            itemsI.addAll(Arrays.asList(propuestas_internacional));

            String dynamicUrl = "https://www.psoe.es/media-content/2019/03/110-Compromisos-PSOE-programa-electoral-2019.pdf";
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dynamicUrl));
                    startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan, urlTemplate.indexOf("aquí"), urlTemplate.indexOf("aquí") + "aquí".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }else if(mensaje.equals("PP")){
            titleDynamic.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PP));
            btnContract1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PP));
            btnContract2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PP));
            btnContract3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.PP));

            String[] propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_PP);
            itemsS.addAll(Arrays.asList(propuestas_sociedad));

            String[] propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_PP);
            itemsE.addAll(Arrays.asList(propuestas_economia));

            String[] propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_PP);
            itemsI.addAll(Arrays.asList(propuestas_internacional));

            String dynamicUrl = "https://www.pp.es/sites/default/files/documentos/programa_electoral_pp_23j_feijoo_2023.pdf";
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dynamicUrl));
                    startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan, urlTemplate.indexOf("aquí"), urlTemplate.indexOf("aquí") + "aquí".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }else if(mensaje.equals("VOX")){
            titleDynamic.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.VOX));
            btnContract1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.VOX));
            btnContract2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.VOX));
            btnContract3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.VOX));

            String[] propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_VOX);
            itemsS.addAll(Arrays.asList(propuestas_sociedad));

            String[] propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_VOX);
            itemsE.addAll(Arrays.asList(propuestas_economia));

            String[] propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_VOX);
            itemsI.addAll(Arrays.asList(propuestas_internacional));

            String dynamicUrl = "https://www.voxespana.es/wp-content/uploads/2019/04/100medidasngal_101319181010040327.pdf";
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dynamicUrl));
                    startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan, urlTemplate.indexOf("aquí"), urlTemplate.indexOf("aquí") + "aquí".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }else if(mensaje.equals("SUMAR")){
            titleDynamic.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.SUMAR));
            btnContract1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.SUMAR));
            btnContract2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.SUMAR));
            btnContract3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.SUMAR));

            String[] propuestas_sociedad = getResources().getStringArray(R.array.propuestas_sociedad_SUMAR);
            itemsS.addAll(Arrays.asList(propuestas_sociedad));

            String[] propuestas_economia = getResources().getStringArray(R.array.propuestas_economia_SUMAR);
            itemsE.addAll(Arrays.asList(propuestas_economia));

            String[] propuestas_internacional = getResources().getStringArray(R.array.propuestas_internacional_SUMAR);
            itemsI.addAll(Arrays.asList(propuestas_internacional));

            String dynamicUrl = "https://movimientosumar.es/wp-content/uploads/2023/07/Un-Programa-para-ti.pdf";
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dynamicUrl));
                    startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan, urlTemplate.indexOf("aquí"), urlTemplate.indexOf("aquí") + "aquí".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        fuente.setText(spannableString);
        fuente.setMovementMethod(LinkMovementMethod.getInstance());

        //---------------------------------------------------------
        adapterS = new CustomListAdapter(requireContext(), itemsS);
        RecyclerView propuestasSociedadRe = binding.propuestasSociedadRe;

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        propuestasSociedadRe.setLayoutManager(layoutManager);

        propuestasSociedadRe.setAdapter(adapterS);
        //---------------------------------------------------------
        adapterE = new CustomListAdapter(requireContext(), itemsE);
        RecyclerView propuestasEconomiaRe = binding.propuestasEconomiaRe;

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
        propuestasEconomiaRe.setLayoutManager(layoutManager2);

        propuestasEconomiaRe.setAdapter(adapterE);
        //---------------------------------------------------------
        adapterI = new CustomListAdapter(requireContext(), itemsI);
        RecyclerView propuestasInternacionalRe = binding.propuestasInternacionalRe;

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireContext());
        propuestasInternacionalRe.setLayoutManager(layoutManager3);

        propuestasInternacionalRe.setAdapter(adapterI);

        binding.btnContract1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(propuestasSociedadRe.getVisibility() == View.VISIBLE){

                    propuestasSociedadRe.setVisibility(View.GONE);
                }else{
                    propuestasSociedadRe.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.btnContract2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(propuestasEconomiaRe.getVisibility() == View.VISIBLE){
                    propuestasEconomiaRe.setVisibility(View.GONE);
                }else{
                    propuestasEconomiaRe.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.btnContract3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(propuestasInternacionalRe.getVisibility() == View.VISIBLE){
                    propuestasInternacionalRe.setVisibility(View.GONE);
                }else{
                    propuestasInternacionalRe.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ChatGPTTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            try {
                // Construir la URL
                URL url = new URL(API_URL);

                // Crear una conexión HTTP
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer ");-

                // Crear el cuerpo de la solicitud JSON
                String jsonInputString = "{\n" +
                        "\"model\": \"gpt-3.5-turbo\",\n" +
                        "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],\n" +
                        "\"max_tokens\": 1000,\n" +
                        "\"temperature\": 0.5\n" +
                        "}";

                // Enviar la solicitud
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Leer la respuesta
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray choicesArray = jsonObject.getJSONArray("choices");
                    JSONObject firstChoice = choicesArray.getJSONObject(0);
                    JSONObject messageObject = firstChoice.getJSONObject("message");
                    String content = messageObject.getString("content");

                    return content;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                contentIA.setVisibility(View.VISIBLE);
                generandoRes2.setVisibility(View.GONE);
                textIA.setText(result);
            }
        }
    }
}

