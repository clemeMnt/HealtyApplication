package com.exemple.healtyapplication.ui.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.model.Food;
import com.google.firebase.firestore.model.ObjectValue;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FoodFragment extends Fragment {

    Button search;
    TextView resp, t1, t2, t3, t4, t5, t6, t7, t8;
    EditText food;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragmment_food, container, false);
        search = (Button) root.findViewById(R.id.button_searchfood);
        resp = (TextView) root.findViewById(R.id.food_resp);
        t1 = (TextView) root.findViewById(R.id.textName);
        t2 = (TextView) root.findViewById(R.id.textSugar);
        t3 = (TextView) root.findViewById(R.id.textFiber);
        t4 = (TextView) root.findViewById(R.id.textSodium);
        t5 = (TextView) root.findViewById(R.id.textPotassium);
        t6 = (TextView) root.findViewById(R.id.textCalories);
        t7 = (TextView) root.findViewById(R.id.textProtein);
        t8 = (TextView) root.findViewById(R.id.textFat);
        food = (EditText) root.findViewById(R.id.text_search);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String _food = food.getText().toString();
                    searchAPI(_food);
                    hideKeybaord(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void searchAPI(String _food) throws IOException{
        OkHttpClient client = new OkHttpClient();

        if(_food.isEmpty()){
            Toast.makeText(getActivity(), "Field is empty", Toast.LENGTH_SHORT).show();
        }else {
            Request request = new Request.Builder()
                    .url("https://calorieninjas.p.rapidapi.com/v1/nutrition?query=" + _food)
                    .addHeader("x-rapidapi-key", "049777918amsh7a758a90f749ed2p1cac0ajsnf320bb3aca1a")
                    .addHeader("x-rapidapi-host", "calorieninjas.p.rapidapi.com")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error in your research, try another ingredients", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String myResponse = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject Jobject = null;
                            try {
                                Jobject = new JSONObject(myResponse);
                                JSONArray Jarray = Jobject.getJSONArray("items");

                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject object = Jarray.getJSONObject(i);
                                    Food f = new Food(object.getDouble("sugar_g"),
                                            object.getDouble("fiber_g"),
                                            object.getLong("sodium_mg"),
                                            object.getString("name"),
                                            object.getLong("potassium_mg"),
                                            object.getDouble("calories"),
                                            object.getDouble("protein_g"),
                                            object.getDouble("fat_total_g"));

                                    if (f.getName().isEmpty()) {
                                        Toast.makeText(getActivity(), "Error in your request, try another ingredients", Toast.LENGTH_SHORT).show();
                                    } else {
                                        t1.setText(f.getName());
                                        t2.setText(String.valueOf(f.getSugarG()));
                                        t3.setText(String.valueOf(f.getFiberG()));
                                        t4.setText(String.valueOf(f.getSodiumMg()));
                                        t5.setText(String.valueOf(f.getPotasiumMg()));
                                        t6.setText(String.valueOf(f.getCalories()));
                                        t7.setText(String.valueOf(f.getProteinG()));
                                        t8.setText(String.valueOf(f.getFatTotalG()));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Oups.. something append to convert your research", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}