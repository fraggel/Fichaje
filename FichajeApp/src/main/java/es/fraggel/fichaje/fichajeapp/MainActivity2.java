package es.fraggel.fichaje.fichajeapp;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
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
            horaFinCalc=String.valueOf(Integer.parseInt(horaFinCalc)-total);
            BigDecimal bd = null;
            BigDecimal bd2 = null;
            bd = new BigDecimal((Integer.parseInt(horaFinCalc) - Integer.parseInt(horaInicioCalc)));
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
            bd = new BigDecimal((Integer.parseInt(horaFinCalc)));
            intsBB = splitToComponentTimes(bd);
            if(tempo!=0){
                if(finestimado){
                    texto.setText("Estimado:" +String.valueOf(intsA[0] + ":" + intsA[1] + ":" + intsA[2]) + "\n FALTAN: "+tempo+" minutos\nDEBERIA SALIR A "+intsBB[0]+":"+intsBB[1]+":"+intsBB[2]);
                }else {
                    texto.setText(String.valueOf(intsA[0] + ":" + intsA[1] + ":" + intsA[2]) + "\n FALTAN: "+tempo+" minutos\nDEBERIA SALIR A "+intsBB[0]+":"+intsBB[1]+":"+intsBB[2]);
                }

            }else {
                    texto.setText("Tiempo Actual: "+String.valueOf(intsA[0] + ":" + intsA[1] + ":" + intsA[2]) + "");
            }
            /*for(int xx=0;xx<listaSF.size();xx++){
                sf=sf+Long.parseLong((String)listaSF.get(xx));
            }
            for(int xx=0;xx<listaEF.size();xx++){
                ef=ef+Long.parseLong((String)listaEF.get(xx));
            }*/
            /*long horaInicio=0L;
            ArrayList listaSF=new ArrayList();
            ArrayList listaEF=new ArrayList();
            long horaFin=0L;
            while(linea!=null){
                String tipo=linea.split("--")[1];
                if("EN".equals(tipo)){
                    horaInicio=Long.parseLong(linea.split("--")[0]);
                }else if("SF".equals(tipo)){
                    listaSF.add(linea.split("--")[0]);
                }else if("EF".equals(tipo)){
                    listaEF.add(linea.split("--")[0]);
                }else if("SA".equals(tipo)){
                    horaFin=Long.parseLong(linea.split("--")[0]);
                }
                linea=br.readLine();
            }
            if(horaFin==0L){
                Calendar cc=Calendar.getInstance();
                cc.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 15, 00, 00);
                horaFin=cc.getTimeInMillis();
            }
            long sf=0L;
            long ef=0L;
            for(int xx=0;xx<listaSF.size();xx++){
                sf=sf+Long.parseLong((String)listaSF.get(xx));
            }
            for(int xx=0;xx<listaEF.size();xx++){
                ef=ef+Long.parseLong((String)listaEF.get(xx));
            }
            Calendar fff=Calendar.getInstance();
            fff.setTimeInMillis(ef-sf);

            Calendar in=Calendar.getInstance();
            in.setTimeInMillis((horaFin-horaInicio));

            Calendar total=Calendar.getInstance();
            total.setTimeInMillis(in.getTimeInMillis()-fff.getTimeInMillis());
            Calendar totalTemp=Calendar.getInstance();
            totalTemp.setTimeInMillis(total.getTimeInMillis());
            int temporal=0;

            while((totalTemp.get(Calendar.HOUR))<7){
                totalTemp.roll(Calendar.MINUTE,true);
                temporal=temporal+1;
            }
            if(total.getTimeInMillis()!=totalTemp.getTimeInMillis()){
                Toast.makeText(getApplicationContext(), "Necesitas aún "+temporal+" minutos", Toast.LENGTH_SHORT).show();
            }
            texto.setText(total.get(Calendar.HOUR_OF_DAY)+":"+total.get(Calendar.MINUTE)+":"+total.get(Calendar.SECOND));
            */
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
