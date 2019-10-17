package com.matriot.varahi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.matriot.varahi.JSONparser.Crypter;
import com.matriot.varahi.JSONparser.Parser;
import com.matriot.varahi.model.LognData;
import com.matriot.varahi.model.UserSessionManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    Button button;
    TextView newUser;
    EditText text1, text2;
    int count = 5;
    String email;
    String passw;
    Registraion register;
    private static ArrayList<LognData> lognData=new ArrayList<>();

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new UserSessionManager(getApplicationContext());

        if (session.checkLogin())
            finish();

        button = (Button) findViewById(R.id.submit);
        text1 = (EditText) findViewById(R.id.email);
        text2 = (EditText) findViewById(R.id.password);
        newUser = findViewById(R.id.newuser);

        new GetDataTask().execute();

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Registraion.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                checkinfo();

            }
        });
    }



    void checkinfo() {
        boolean isvalid = true;
        //check if email fild is empty//
        if (isEmpty(text1)) {
            text1.setError("Enter Email Address to Login!");
            isvalid = false;
        } else {
            if (!isEmail(text1)) {
                text1.setError("Enter Valid Email Address");//check valid email formate//
                isvalid = false;
            }
        }

        //check is password filed is emptoy//
        if (isEmpty(text2)) {
            text2.setError("Enter Password!");
        }

        //athenthication//
        if (isvalid) {
            email = text1.getText().toString();
            passw = text2.getText().toString();

            if (text1.getText().toString().equals("admin@matriot.com") && text2.getText().toString().equals("1234")) {
                session.createUserLoginSession(email,
                        passw);

                // Starting MainActivity
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();
            } else {
                try {
                    if(checkAuthorization()) {
                        session.createUserLoginSession(email,
                                passw);
                        Intent intent=new Intent(MainActivity.this,navigation.class);
                        startActivity(intent);
                        Log.d("Login sussesful", "checkinfo: ");
                        finish();

                    }else{
                        Toast t = Toast.makeText(this, "Wrong Email or Password! You Got " + (count - 1) + " Chances", Toast.LENGTH_LONG);
                        t.show();
                        count--;
                        text1.setText("");
                        text2.setText("");
                        if (count == 0) {
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean checkAuthorization() throws Exception {
        boolean permission=false;
        for(int listlength=0;listlength<lognData.size();listlength++){
            String a= Crypter.decrypt(lognData.get(listlength).getPassword());
            Log.d("jfdkj", "checkAuthorization: "+a);
            if(lognData.get(listlength).getName().equals(email) && passw.equals(a)){
                permission=true;
            }
        }
        return permission;
    }


    //method to find emailfoemate//
    boolean isEmail (EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    //method to find text field is empty//
    boolean isEmpty (EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    class GetDataTask extends AsyncTask<Void, Void, Void> {


        int jIndex=0;


        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            JSONObject jsonObject = Parser.getDataFromWeb();

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if(jsonObject.length() > 0) {
                        /**
                         * Getting Array named "contacts" From MAIN Json Object
                         */
                        JSONArray array = jsonObject.getJSONArray("Sheet1");

                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if(lenArray > 0) {
                            for( ; jIndex < lenArray; jIndex++) {

                                /**
                                 * Creating Every time New Object
                                 * and
                                 * Adding into List
                                 */
                                LognData model = new LognData();

                                /**
                                 * Getting Inner Object from contacts array...
                                 * and
                                 * From that We will get Name of that Contact
                                 *
                                 */
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String name = innerObject.getString("Email_ID");
                                String password =innerObject.getString("Password");

                                /**
                                 * Getting Object from Object "phone"
                                 */
                                //JSONObject phoneObject = innerObject.getJSONObject(Keys.KEY_PHONE);
                                //String phone = phoneObject.getString(Keys.KEY_MOBILE);

                                model.setName(name);
                                model.setPassword(password);
                                Log.d("hfhsdjg", "doInBackground: "+model.getName()+model.getPassword());

                                /**
                                 * Adding name and phone concatenation in List...
                                 */
                                lognData.add(model);
                            }
                        }
                    }
                } else {

                }
            }  catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /**
             * Checking if List size if more than zero then
             * Update ListView
             */
            if(lognData.size() > 0) {

            } else {

            }
        }
    }
}
