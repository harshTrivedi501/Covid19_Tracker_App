package learncodeonline.in.covid_19_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText countryFetcher;
    Button submit;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryFetcher=findViewById(R.id.countryFetcher);
        submit=findViewById(R.id.submit);
        output=findViewById(R.id.output);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country=countryFetcher.getText().toString();
                if(country.isEmpty()){
                    countryFetcher.setError("Please provide Country name");
                    return;
                }
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://covid-19-data.p.rapidapi.com/country?format=json&name="+country, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                Log.i("JSON", "JSON: " + jsonArray);
                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    Log.i("JSON OBJECT",""+jsonObject);
                                    String country=jsonObject.getString("country");
                                    String confirmed=jsonObject.getString("confirmed");
                                    String recovered=jsonObject.getString("recovered");
                                    String critical=jsonObject.getString("critical");
                                    String deaths=jsonObject.getString("deaths");
                                    output.setText("Country: "+country+
                                            "\nConfirmed: "+confirmed+
                                            "\nRecovered: "+recovered+
                                            "\nCritical: "+critical+
                                            "\nDeaths: "+deaths);
                                } catch (JSONException e) {
                                    output.setText("NOT FOUND");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(MainActivity.this, ""+volleyError, Toast.LENGTH_SHORT).show();

                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String,String> headers=new HashMap<String, String>();
                        headers.put("x-rapidapi-host", "covid-19-data.p.rapidapi.com");
                        headers.put("x-rapidapi-key", "d6870c0ec1mshae89fcfac2aefb9p12c261jsne2c6cad1cef5");
                        return headers;
                    }
                };
                MySingleTon.getInstance(MainActivity.this).addToRequestQue(jsonArrayRequest);
            }
        });
    }
}
