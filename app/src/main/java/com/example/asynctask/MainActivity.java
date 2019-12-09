package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button button,button2;
    private EditText time;
    private TextView finalResult;


    // AsyncTask used for short operations (a few seconds at the most.)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = findViewById(R.id.in_time);
        button = findViewById(R.id.btn_run);
        button2 = findViewById(R.id.btn_second_activity);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
        finalResult = findViewById(R.id.tv_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = time.getText().toString();
                runner.execute(sleepTime);
            }
        });
    }

    public void SecondActivity(View view) {
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
    }


// First parameter --> Params, the type of the parameters sent to the task upon execution.
// Second parameter --> Progress, the type of the progress units published during the background computation.
// Third parameter --> Result, the type of the result of the background computation.

    class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        ProgressDialog progressDialog;


        // this is the first thing we do in the asyncTask -->
        // show something on screen like progressbar or any animation to user
        @Override
        protected void onPreExecute() {
            // This method contains the code which is executed before the background processing starts
            progressDialog = ProgressDialog.show(MainActivity.this, "ProgressDialog", "wait for " + time.getText().toString() + "second");
            super.onPreExecute();
        }



        @Override // it take first parameter Params
        protected String doInBackground(String... params) {
            // This method contains the code which needs to be executed in background.
            //  In this method we can send results multiple times to the UI thread by publishProgress() method.
            //To notify that the background processing has been completed we just need to use the return statements

            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;
                Thread.sleep(time);
                resp = "Slept for " + params[0] + "second";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // This method is called after doInBackground method completes processing.
            // Result from doInBackground is passed to this method
            progressDialog.dismiss();
            finalResult.setText(result);
        }



        @Override
        protected void onProgressUpdate(String... text) {
            // This method receives progress updates from doInBackground method, which is published via publishProgress method,
            // and this method can use this progress update to update the UI thread
            finalResult.setText(text[0]);
        }
    }
}
