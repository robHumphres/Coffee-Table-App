package com.example.forexamplejohn.ssh_table;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast; // making some toast.... if needed.... probably wont need... who knows...
import com.jcraft.jsch.*;
import android.view.View;
import java.util.Properties;
import android.os.AsyncTask;
import java.io.ByteArrayOutputStream;





public class openClose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_close);


    }// on create
// the true mvp http://stackoverflow.com/questions/14323661/simple-ssh-connect-with-jsch
    public static String executeRemoteCommand(String username,String password,String hostname,int port,String s)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand(s);
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }


        public void OnClick(View v) {

            switch (v.getId()) {

                case R.id.openButton:
                    Toast.makeText(getApplicationContext(), "open button worked", Toast.LENGTH_SHORT).show();
                    new AsyncTask<Integer, Void, Void>(){
                        @Override
                        protected Void doInBackground(Integer... params) {
                            try {
                                executeRemoteCommand("pi","raspberry","192.168.1.109", 22,"ls > open.txt");
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error connecting to SSH", Toast.LENGTH_LONG).show();
                            } // catch
                            return null;
                        }// background
                    }.execute(1);
                    break;
                case R.id.closeButton:
                    Toast.makeText(getApplicationContext(), "close button worked", Toast.LENGTH_SHORT).show();
                    new AsyncTask<Integer, Void, Void>(){
                        @Override
                        protected Void doInBackground(Integer... params) {
                            try {
                                executeRemoteCommand("pi","raspberry","192.168.1.109", 22,"python hello.py > allout.txt 2>&1");
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error connecting to SSH", Toast.LENGTH_LONG).show();
                            } // catch
                            return null;
                        }// background
                    }.execute(1);
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "Error switch Case didn't work", Toast.LENGTH_LONG).show();

                    break;
            }

        }// button on click


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }// if r action settings

        return super.onOptionsItemSelected(item);
    }// end of boolean onOption items..

}// end of openClose class

