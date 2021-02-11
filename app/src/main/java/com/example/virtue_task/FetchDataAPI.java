package com.example.virtue_task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FetchDataAPI extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ListView listView;
    // URL to get contacts JSON
    private static String url = "https://api.androidhive.info/contacts/";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.list);
        contactList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
        Log.i("fect activty","Ok");
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FetchDataAPI.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();
            String jsonstr = httpHandler.makeServiceCall(url);
            Log.i("task", "URL Response" + jsonstr);
            if (jsonstr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonstr);
                    JSONArray contacts=jsonObject.getJSONArray("contacts");
                    for (int i=0;i<contacts.length();i++){
                        JSONObject c=contacts.getJSONObject(i);

                        //Getting Data From JSON Object
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");

                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");
Log.i("data From Json","data "+id+name+email+address+gender);

                        HashMap<String,String> contact=new HashMap<>();
                        // adding each child node to HashMap object

                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);
                        contact.put("address", address);
                        contact.put("gender", gender);
                        contact.put("home",home);
                        contact.put("office",office);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    Log.e("task", "Json parsing error: " + e.getMessage());
                    e.printStackTrace();
                }
            }else {
                Log.e("task", "Couldn't get json from server.");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            ListAdapter listAdapter=new
                    SimpleAdapter(FetchDataAPI.this,contactList,R.layout.list_item,
                    new String[]{"name", "email","mobile","address", "gender"},
                    new int[]{R.id.name,R.id.email, R.id.mobile,R.id.address,R.id.gender});
            listView.setAdapter(listAdapter);
        }
    }
}
