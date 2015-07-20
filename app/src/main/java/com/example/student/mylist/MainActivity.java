package com.example.student.mylist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {

    String lc = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl1);
       // String s1 = "#7fffd4";
        //rl.setBackgroundColor(Color.parseColor(s1));

        // READ INFO FROM THE FILE TO SEE THE SETTINGS OF BACKGROUND
        ReadFrom(null);


        String[] colourNames;
        colourNames = getResources().getStringArray(R.array.listArray);
        ListView lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);


        // SET THE BACKGROUND OF THE APP
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                String[] colourCodes;
                colourCodes = getResources().getStringArray(R.array.listValues);
                String selection = colourCodes[position];
                lc = selection;

                // CUT THE FIRST 2 CHARS IN COLOR CODE
                String temp = "#";
                temp = temp.concat(selection.substring(2));
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl1);
                rl.setBackgroundColor(Color.parseColor(temp));



            }
        });


        registerForContextMenu(lv);

        }

    // CODE WAS PUBLISHED BY ANDREW SMITH
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Write colour to SDCard");
        menu.add(0, v.getId(), 0, "Read colour from SDCard");
    }



    // WRITE THE COLOR ATO SD CARD
    // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

    public void WriteTo(){
        TextView tv = (TextView) findViewById(R.id.HW);
        if(lc != " ") {

            if (lc.length() == 8) {
                String temp = "#";
                lc = temp.concat(lc.substring(2));

            }

            try {
                File dir= Environment.getExternalStorageDirectory();
                File file = new File(dir,"cc.txt");
                FileOutputStream f = new FileOutputStream(file);

                byte[] contentInBytes = lc.getBytes();

                f.write(contentInBytes);
                f.flush();
                f.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());

            }
        }

      //  tv.setText("File is new Color is "+ lc);
    }

    // http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android

    public void ReadFrom(View v){
        TextView tv = (TextView) findViewById(R.id.HW);

        String ret = " ";

        try {
            FileInputStream inputStream = new FileInputStream(new File("storage/sdcard/cc.txt"));
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            tv.setText("NO FILE");
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl1);

        if(ret != " ") {

           // String temp = "#";
           // String so = "#f0ffff";
            rl.setBackgroundColor(Color.parseColor(ret));
            //tv.setText("Sc: " + so+ " Fc: "+ret);
        }

        else {
            String def = "#f0ffff";
            rl.setBackgroundColor(Color.parseColor(def));
       //     tv.setText("Default color is used");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       if (item.getTitle() == "Write colour to SDCard") {
           WriteTo();
        } else if (item.getTitle() == "Read colour from SDCard") {
            ReadFrom(null);
        } else {
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
