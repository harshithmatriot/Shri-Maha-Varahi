package com.matriot.varahi;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import java.util.Calendar;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class Registraion extends Activity {

    EditText name, dob, emailId, password, phone, confirmPassword;
    RadioButton radioSexButton,radioLanguageButton;
    RadioGroup radioSex, radioLanguage;
    Button submitButton;
    ImageView calender;
    DatePickerDialog picker;
    String nameString, languageString, sexString, dobString, emailString, passwordString, phoneString, passwordEncrypted, passwordDecrypt, confirmPasswordString;
    AESCrypt crypt = new AESCrypt();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion);


        name = findViewById(R.id.name);
        dob = findViewById(R.id.date);
        emailId = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        radioSex = findViewById(R.id.radioSex);
        radioLanguage = findViewById(R.id.radiolanguage);
        submitButton = findViewById(R.id.submitButton);
        calender = findViewById(R.id.calender);



        dob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Registraion.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   String DATE_PATTERN =
                        "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";

                int selectedId=radioSex.getCheckedRadioButtonId();
                if(selectedId == -1){
                    Toast.makeText(getApplicationContext(), "Please Select Gender", Toast.LENGTH_LONG);
                    return;
                }
                Log.d("", "onClick: "+selectedId);
                radioSexButton=(RadioButton)findViewById(selectedId);
                int selectedLanguage=radioLanguage.getCheckedRadioButtonId();
                if(selectedLanguage == -1){
                    Toast.makeText(getApplicationContext(), "Please Select Language", Toast.LENGTH_LONG);
                    return;
                }

                radioLanguageButton=(RadioButton)findViewById(selectedLanguage);
                nameString = name.getText().toString();
                languageString = radioLanguageButton.getText().toString();
                sexString = radioSexButton.getText().toString();
                dobString = dob.getText().toString();
                emailString = emailId.getText().toString();
                passwordString = password.getText().toString();
                phoneString = phone.getText().toString();
                confirmPasswordString = confirmPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String phonePattern = "[0-9]{10}";

                Log.d("value", "onClick: "+phoneString);
                try {
                    passwordEncrypted = crypt.encrypt(passwordString);
                    Log.d("password", "onClick: "+passwordEncrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    passwordDecrypt = crypt.decrypt(passwordEncrypted);
                    Log.d("Decrypt", "onClick: "+passwordDecrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (emailString.matches(emailPattern))
                {
                    if(phoneString.matches(phonePattern)){

                        if(passwordString.equals(confirmPasswordString)){

                            Toast.makeText(getApplicationContext(),"Successfully Registered", Toast.LENGTH_SHORT).show();
                            new SendRequest().execute();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Password Mismatch", Toast.LENGTH_SHORT).show();
                            return;

                        }


                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }




            }
        });

    }




    DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker View, int day, int month, int year) {
            dob.setText(String.format("%02d.%02d.%04d", day, month+1, year));
        }
    };


    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                //Enter script URL Here
                URL url = new URL("https://script.google.com/macros/s/AKfycby4bRzs2yhVmu8MIXXYHbVbeC_YZkPDmYun0vredDW-Rkhctynr/exec");

                JSONObject postDataParams = new JSONObject();

                //int i;
                //for(i=1;i<=70;i++)


                //    String usn = Integer.toString(i);

                //Passing scanned code as parameter


                postDataParams.put("name",nameString);
                postDataParams.put("language",languageString);
                postDataParams.put("sex",sexString);
                postDataParams.put("dob",dobString);
                postDataParams.put("email",emailString);
                postDataParams.put("password",passwordEncrypted);
                postDataParams.put("phone",phoneString);


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {


                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();



                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {



        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public class AESCrypt
    {
        private static final String ALGORITHM = "AES";
        private static final String KEY = "1Hbfh667adfDEJ78";

        public  String encrypt(String value) throws Exception
        {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
            String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
            return encryptedValue64;

        }

        public  String decrypt(String value) throws Exception
        {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
            byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
            String decryptedValue = new String(decryptedByteValue,"utf-8");
            return decryptedValue;

        }

        private  Key generateKey() throws Exception
        {
            Key key = new SecretKeySpec(AESCrypt.KEY.getBytes(), AESCrypt.ALGORITHM);
            return key;
        }
    }


}
