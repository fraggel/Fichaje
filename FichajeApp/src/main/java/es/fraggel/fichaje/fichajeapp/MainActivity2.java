package es.fraggel.fichaje.fichajeapp;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity2 extends Activity implements  AdapterView.OnItemSelectedListener {
    Spinner spin=null;
    TextView texto=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_activity2);
            spin=(Spinner)findViewById(R.id.spinner);
            texto=(TextView)findViewById(R.id.textView);
            spin.setOnItemSelectedListener(this);
            File ff=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/FICHAJES/");
            ff.mkdir();
            String hoy="";
            String dia=String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            if(dia.length()<2){
                dia="0"+dia;
            }
            String mes=String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
            if(mes.length()<2){
                mes="0"+mes;
            }
            String anyo=String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            File[] files = ff.listFiles();
            String[] nombres=new String[files.length];
            for(int y=0;y<files.length;y++){
                File f=files[y];
                nombres[y]=f.getName();
            }
            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, nombres);
            spin.setAdapter(karant_adapter);


        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FICHAJES/" + adapterView.getItemAtPosition(i).toString());
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea = br.readLine();
            String horaInicioLeido = "";
            String horaInicioCalc= "";
            ArrayList listaSF = new ArrayList();
            ArrayList listaEF = new ArrayList();
            String horaFinLeido = "";
            String horaFinCalc= "";

            while (linea != null) {
                String tipo = linea.split("--")[1];
                if ("EN".equals(tipo)) {
                    horaInicioLeido = (linea.split("--")[0]);
                } else if ("SF".equals(tipo)) {
                    String sf=linea.split("--")[0];
                    listaSF.add(String.valueOf((Integer.parseInt(sf.substring(0, 2)) * 3600) + (Integer.parseInt(sf.substring(2, 4)) * 60) + (Integer.parseInt(sf.substring(4, 6)))));
                } else if ("EF".equals(tipo)) {
                    String ef=linea.split("--")[0];
                    listaEF.add(String.valueOf((Integer.parseInt(ef.substring(0, 2)) * 3600) + (Integer.parseInt(ef.substring(2, 4)) * 60) + (Integer.parseInt(ef.substring(4, 6)))));
                } else if ("SA".equals(tipo)) {
                    horaFinLeido = (linea.split("--")[0]);
                }
                linea = br.readLine();
            }
            br.close();
            horaInicioCalc = String.valueOf((Integer.parseInt(horaInicioLeido.substring(0, 2)) * 3600) + (Integer.parseInt(horaInicioLeido.substring(2, 4)) * 60) + (Integer.parseInt(horaInicioLeido.substring(4, 6))));
            boolean finestimado=false;
            if (horaFinLeido != null && !"".equals(horaFinLeido.trim())) {
                horaFinCalc = String.valueOf((Integer.parseInt(horaFinLeido.substring(0, 2)) * 3600) + (Integer.parseInt(horaFinLeido.substring(2, 4)) * 60) + (Integer.parseInt(horaFinLeido.substring(4, 6))));
            } else {
                String hora=String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                if(hora.length()<2){
                    hora="0"+hora;
                }
                String min=String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
                if(min.length()<2){
                    min="0"+min;
                }
                String seg=String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
                if(seg.length()<2){
                    seg="0"+seg;
                }
                horaFinCalc = String.valueOf((Integer.parseInt(hora)*3600)+(Integer.parseInt(min)*60)+(Integer.parseInt(seg)));
                finestimado=true;
            }
            int total=0;
            for(int xx=0;xx<listaSF.size();xx++){
                String sf=(String)listaSF.get(xx);
                String ef=(String)listaEF.get(xx);
                total=total+(Integer.parseInt(ef)-Integer.parseInt(sf));
            }
            horaFinCalc=String.valueOf(Integer.parseInt(horaFinCalc));
            BigDecimal bd = null;
            BigDecimal bd2 = null;
            bd = new BigDecimal((Integer.parseInt(horaFinCalc) - (Integer.parseInt(horaInicioCalc)+total)));
            int[] intsA = splitToComponentTimes(bd);
            int tempo=0;
            int[] intsB=intsA;
            int[] intsBB=null;
            while (intsB[0] < 7){
                horaFinCalc=String.valueOf((Integer.parseInt(horaFinCalc)+60));
                bd = new BigDecimal(((Integer.parseInt(horaFinCalc)) - Integer.parseInt(horaInicioCalc)));
                intsB = splitToComponentTimes(bd);
                tempo=tempo+1;
            }
            bd = new BigDecimal((Integer.parseInt(horaFinCalc)+total));
            intsBB = splitToComponentTimes(bd);
            if(tempo!=0){
                if(finestimado){
                    texto.setText("Estimado:" +String.valueOf(intsA[0] + ":" + intsA[1] + ":" + intsA[2]) + "\n FALTAN: "+(tempo+(splitToComponentTimes(new BigDecimal(total))[1]))+" minutos\nDEBERIA SALIR A "+intsBB[0]+":"+intsBB[1]+":"+intsBB[2]);
                }else {
                    texto.setText(String.valueOf(intsA[0] + ":" + intsA[1] + ":" + intsA[2]) + "\n FALTAN: "+(tempo+(splitToComponentTimes(new BigDecimal(total))[1]))+" minutos\nDEBERIA SALIR A "+intsBB[0]+":"+intsBB[1]+":"+intsBB[2]);
                }

            }else {
                    texto.setText("Tiempo Actual: "+String.valueOf(intsA[0] + ":" + intsA[1] + ":" + intsA[2]) + "");
            }
            bd = new BigDecimal(Integer.parseInt(horaInicioCalc));
            int[] intsAA = splitToComponentTimes(bd);
            texto.setText(texto.getText()+"\nEN: "+intsAA[0]+":"+intsAA[1]+":"+intsAA[2]);
            for(int xx=0;xx<listaSF.size();xx++){
                String sf=(String)listaSF.get(xx);
                String ef=(String)listaEF.get(xx);
                int[] ints = splitToComponentTimes(new BigDecimal(Integer.parseInt(sf)));
                int[] ints1 = splitToComponentTimes(new BigDecimal(Integer.parseInt(ef)));
                texto.setText(texto.getText()+"\nSF: "+ints[0]+":"+ints[1]+":"+ints[2]+"\nEF: "+ints1[0]+":"+ints1[1]+":"+ints1[2]);
            }
            if (horaFinLeido != null && !"".equals(horaFinLeido.trim())) {
                String horaFinn = String.valueOf((Integer.parseInt(horaFinLeido.substring(0, 2)) * 3600) + (Integer.parseInt(horaFinLeido.substring(2, 4)) * 60) + (Integer.parseInt(horaFinLeido.substring(4, 6))));
                bd = new BigDecimal((Integer.parseInt(horaFinn)));
                int[] intsAAA = splitToComponentTimes(bd);
                texto.setText(texto.getText()+"\nSA: "+intsAAA[0]+":"+intsAAA[1]+":"+intsAAA[2]);
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public static int[] splitToComponentTimes(BigDecimal biggy)
    {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
