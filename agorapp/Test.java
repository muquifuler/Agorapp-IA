package com.agora.agorapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.agora.agorapp.databinding.TestBinding;

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
import java.util.Random;

public class Test extends Fragment {

    private TestBinding binding;
    private String[][] propuestas;
    private Random random = new Random();
    private int autor = 0;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        propuestas = new String[][]{
                getResources().getStringArray(R.array.propuestas_sociedad_PSOE),
                getResources().getStringArray(R.array.propuestas_economia_PSOE),
                getResources().getStringArray(R.array.propuestas_internacional_PSOE),

                getResources().getStringArray(R.array.propuestas_sociedad_PP),
                getResources().getStringArray(R.array.propuestas_economia_PP),
                getResources().getStringArray(R.array.propuestas_internacional_PP),

                getResources().getStringArray(R.array.propuestas_sociedad_VOX),
                getResources().getStringArray(R.array.propuestas_economia_VOX),
                getResources().getStringArray(R.array.propuestas_internacional_VOX),

                getResources().getStringArray(R.array.propuestas_sociedad_SUMAR),
                getResources().getStringArray(R.array.propuestas_economia_SUMAR),
                getResources().getStringArray(R.array.propuestas_internacional_SUMAR)
        };

        newQuestion();

        binding.viewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Test.this)
                        .navigate(R.id.action_Test_to_TestResult);
            }
        });
        binding.muyacuerdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRes(2f);
            }
        });
        binding.acuerdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRes(1f);
            }
        });
        binding.nose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRes(0f);
            }
        });
        binding.desacuerdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRes(-1f);
            }
        });
        binding.muydesacuerdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRes(-2f);
            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newQuestion();
            }
        });
        binding.btnIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textPregunta = binding.textPregunta;
                TextView generandoRes = binding.generandoRes;

                if(textPregunta.getVisibility() == View.VISIBLE){
                    textPregunta.setVisibility(View.GONE);
                    generandoRes.setVisibility(View.VISIBLE);
                    new Test.ChatGPTTask().execute("Dame una explicación no muy extensa de esta propuesta: "+textPregunta.getText());
                }else{
                    System.out.println("vabien");
                }

            }
        });
    }

    public void setRes(float res){
        TextView resPartido = binding.resPartido;

        if (resPartido.getBackground().getConstantState().equals(ContextCompat.getDrawable(getContext(), R.color.transparent).getConstantState())) {
            float perfil[] = MainActivity.getPerfil();

            perfil[autor] += res;

            float sumTotal = perfil[0]+perfil[1]+perfil[2]+perfil[3];

            perfil[0] = (perfil[0]/sumTotal)*100 > 0 ? (perfil[0]/sumTotal)*100 : 0;
            perfil[1] = (perfil[1]/sumTotal)*100 > 0 ? (perfil[1]/sumTotal)*100 : 0;
            perfil[2] = (perfil[2]/sumTotal)*100 > 0 ? (perfil[2]/sumTotal)*100 : 0;
            perfil[3] = (perfil[3]/sumTotal)*100 > 0 ? (perfil[3]/sumTotal)*100 : 0;

            MainActivity.setPerfil(perfil[0], perfil[1], perfil[2], perfil[3]);

            if(autor == 0){
                resPartido.setText("PSOE");
                resPartido.setBackgroundResource(R.color.PSOE);
            }else if(autor == 1){
                resPartido.setText("PP");
                resPartido.setBackgroundResource(R.color.PP);
            }else if(autor == 2){
                resPartido.setText("VOX");
                resPartido.setBackgroundResource(R.color.VOX);
            }else if(autor == 3){
                resPartido.setText("SUMAR");
                resPartido.setBackgroundResource(R.color.SUMAR);
            }

            ImageButton nextBtn = binding.nextBtn;
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    public void newQuestion(){
        TextView textPregunta = binding.textPregunta;
        TextView resPartido = binding.resPartido;
        TextView textExplainPregunta = binding.textExplainPregunta;
        TextView generandoRes = binding.generandoRes;
        ImageButton nextBtn = binding.nextBtn;

        int numeroAleatorio = random.nextInt(12);
        int numeroAleatorio2 = random.nextInt(propuestas[numeroAleatorio].length);

        if(numeroAleatorio < 3){
            autor = 0;
        }else if(numeroAleatorio < 6){
            autor = 1;
        }else if(numeroAleatorio < 9){
            autor = 2;
        }else if(numeroAleatorio < 12){
            autor = 3;
        }

        textPregunta.setText(propuestas[numeroAleatorio][numeroAleatorio2]);

        resPartido.setBackgroundResource(R.color.transparent);
        resPartido.setText("");
        textPregunta.setVisibility(View.VISIBLE);
        generandoRes.setVisibility(View.GONE);
        textExplainPregunta.setVisibility(View.GONE);
        nextBtn.setVisibility(View.INVISIBLE);
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

                System.out.println(url);

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
            TextView textExplainPregunta = binding.textExplainPregunta;
            TextView generandoRes = binding.generandoRes;

            if (result != null && generandoRes.getVisibility() == View.VISIBLE) {
                textExplainPregunta.setText(result);
                generandoRes.setVisibility(View.GONE);
                textExplainPregunta.setVisibility(View.VISIBLE);
            }
        }
    }
}
