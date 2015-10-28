package com.example.root.tugas_uts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import domain.Kucing;

public class MainActivity extends AppCompatActivity {

    Kucing kucing = new Kucing();
    DBAdapter dbAdapter = null;

    EditText enama;
    TextView textView, textView2;
    RadioButton radioL, radioP;
    Button save;
    CheckBox cD, cA, cP;
    ListView ListKucing;

    private static final String OPTION[] = {"Edit", "Delete"}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(getApplicationContext());

        save = (Button) findViewById(R.id.save);
        enama = (EditText) findViewById(R.id.enama);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        radioL = (RadioButton) findViewById(R.id.radioL);
        radioP = (RadioButton) findViewById(R.id.radioP);
        cD = (CheckBox) findViewById(R.id.cD);
        cA = (CheckBox) findViewById(R.id.cA);
        cP = (CheckBox) findViewById(R.id.cP);
        ListKucing = (ListView) findViewById(R.id.ListKucing);

        ListKucing.setOnItemClickListener(new ListItemClick());
        ListKucing.setAdapter(new ListKucingAdapter(dbAdapter.getAllKucing()));
    }

    public class ListItemClick implements AdapterView.OnClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Kucing kucing = (Kucing) ListKucing.getItemAtPosition(position);
            showOptionDialog(kucing);
        }
    }

    public void showOptionDialog(Kucing kucing) {
        final Kucing mKucing;
        mKucing = kucing;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Test")
                .setItems(OPTION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int post) {

                        switch (post) {
                            case 0:
                                editKucing = mKucing;
                                enama.setText(mKucing.getNama_kucing());
                                radioL.setText(mKucing.getGender_kucing());
                                radioP.setText(mKucing.getGender_kucing());
                                cA.setText(mKucing.getJenis_kucing());
                                cD.setText(mKucing.getJenis_kucing());
                                cP.setText(mKucing.getJenis_kucing());
                                save.setText("Edit");
                            case 1 ;
                                dbAdapter.delete(mKucing);
                                ListKucing.setAdapter(new ListKucingAdapter(dbAdapter.getAllKucing()));
                                break;
                            default:
                                break;
                        }
                        }
        });
        final Dialog d = builder.create();
        d.show();
    }

    public void save(View v) {
        if (enama.getText().length() == 0) {
            enama.setError("Cannot Empty");
        } else {
            if (save.getText().equals("Edit")) {
                editKucing.setNama_Kucing(enama.getText().toString());
                editKucing.setGender_Kucing(radioL.getText().toString());
                editKucing.setGender_Kucing(radioP.getText().toString());
                editKucing.setJenis_Kucing(cA.getText().toString());
                editKucing.setJenis_Kucing(cP.getText().toString());
                editKucing.setJenis_Kucing(cD.getText().toString());
                dbAdapter.updateKucing(editKucing);
                save.setText("Simpan");
            } else {
                kucing.setNama_kucing(enama.getText().toString());
                kucing.setGender_kucing(radioL.getText().toString());
                kucing.setGender_kucing(radioP.getText().toString());
                kucing.setJenis_kucing(cA.getText().toString());
                kucing.setJenis_kucing(cD.getText().toString());
                kucing.setJenis_kucing(cP.getText().toString());
                dbAdapter.save(kucing);
            }
            enama.setText("");
            radioL.setText("");
            radioP.setText("");
            cD.setText("");
            cA.setText("");
            cP.setText("");
        }
        ListKucing.setAdapter(new ListKucingAdapter(dbAdapter.getAllKucing()));
    }

    public class ListKucingAdapter extends BaseAdapter{
        private List<Kucing> ListKucings;

    public ListKucingAdapter (List<Kucing> ListKucings) {this.ListKucings = ListKucings; }

    @Override
        public int getCount() { return this.ListKucings.size(); }
    @Override
        public Kucing getItem(int position) { return this.ListKucings.get(position); }
    @Override
        public long getItemId(int position) { return position; }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater
                    .from(getApplicationContext())
                    .inflate(R.layout.list_layout, parent, false);
        }
        final Kucing kucing = getItem(position);
        if(kucing != null) {
            TextView labelNama = (TextView) convertView
                    .findViewById(R.id.labelNama);
            labelNama.setText(kucing.getNama_kucing());
            TextView labelGender = (TextView) convertView
                    .findViewById(R.id.labelGender);
            labelGender.setText(kucing.getGender_kucing());
            TextView labelJenis = (TextView) convertView
                    .findViewById(R.id.labelJenis);
            labelJenis.setText(kucing.setJenis_kucing());
        }
        return convertView;
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
