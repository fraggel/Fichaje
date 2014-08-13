package es.fraggel.fichaje.fichajeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends Activity implements View.OnClickListener {
    SharedPreferences sp=null;
    SharedPreferences.Editor editor=null;
    Button entrada=null;
    Button salidaF=null;
    Button entradaF=null;
    Button salida=null;
    Button verhoras=null;
    File elHoy=null;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            /*sp=getApplicationContext().getSharedPreferences("fichajeApp", Context.MODE_PRIVATE);
            editor = sp.edit();
            entrada=(Button) findViewById(R.id.entrada);
    */
            entrada=(Button)findViewById(R.id.entrar);
            salidaF=(Button)findViewById(R.id.salirF);
            entradaF=(Button)findViewById(R.id.entrarF);
            salida=(Button)findViewById(R.id.salir);
            verhoras=(Button)findViewById(R.id.verhoras);
            entrada.setOnClickListener(this);
            salidaF.setOnClickListener(this);
            entradaF.setOnClickListener(this);
            salida.setOnClickListener(this);
            verhoras.setOnClickListener(this);

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
            elHoy=new File(ff.getAbsolutePath()+"/"+dia+mes+anyo+".log");
            /*FileOutputStream bos=null;
            bos=new FileOutputStream(elHoy,true);
            bos.write(("080510"+"--EN\n").getBytes());

            bos.write(("083010"+"--SF\n").getBytes());

            bos.write(("083510"+"--EF\n").getBytes());

            bos.write(("093010"+"--SF\n").getBytes());

            bos.write(("093510"+"--EF\n").getBytes());

            bos.write(("113010"+"--SF\n").getBytes());

            bos.write(("113510"+"--EF\n").getBytes());

            bos.write(("132010"+"--SF\n").getBytes());

            bos.write(("133510"+"--EF\n").getBytes());

            bos.write(("150510"+"--SA\n").getBytes());
            bos.flush();
            bos.close();*/
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        FileOutputStream bos=null;
        try {
            String horas="";
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
            horas=hora+min+seg;
            if(view.getId()==R.id.entrar){
                bos=new FileOutputStream(elHoy,true);
                //bos.write((Calendar.getInstance().getTimeInMillis()+"--EN\n").getBytes());
                bos.write((horas+"--EN\n").getBytes());
                bos.flush();
                bos.close();
                Toast.makeText(getApplicationContext(),"ENTRADA FICHADA",Toast.LENGTH_SHORT).show();
            }else if(view.getId()==R.id.salirF){
                bos=new FileOutputStream(elHoy,true);
                //bos.write((Calendar.getInstance().getTimeInMillis()+"--SF\n").getBytes());
                bos.write((horas+"--SF\n").getBytes());
                bos.flush();
                bos.close();
                Toast.makeText(getApplicationContext(),"SALIDA FUMAR FICHADA",Toast.LENGTH_SHORT).show();
            }else if(view.getId()==R.id.entrarF){
                bos=new FileOutputStream(elHoy,true);
                //bos.write((Calendar.getInstance().getTimeInMillis()+"--EF\n").getBytes());
                bos.write((horas+"--EF\n").getBytes());
                bos.flush();
                bos.close();
                Toast.makeText(getApplicationContext(),"ENTRADA FUMAR FICHADA",Toast.LENGTH_SHORT).show();
            }else if(view.getId()==R.id.salir){
                bos=new FileOutputStream(elHoy,true);
                //bos.write((Calendar.getInstance().getTimeInMillis()+"--SA\n").getBytes());
                bos.write((horas+"--SA\n").getBytes());
                bos.flush();
                bos.close();
                Toast.makeText(getApplicationContext(),"SALIDA FICHADA",Toast.LENGTH_SHORT).show();
            }else if(view.getId()==R.id.verhoras){
                Intent it=new Intent(this,MainActivity2.class);
                startActivity(it);
            }

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error Click",Toast.LENGTH_SHORT).show();
        }
    }
}
