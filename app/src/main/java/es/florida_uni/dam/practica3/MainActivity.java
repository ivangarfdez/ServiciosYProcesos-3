package es.florida_uni.dam.practica3;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "SYP-practica3";

    TareaMuyCostosa tareaMuyCostosa;
    public ProgressBar barraProgreso;
    ReceptorMensajes handler = new ReceptorMensajes();


    // TODO: Programar el handler para recibir el mensaje de finalización y actuar en consecuencia
    // Tendrá que llamar a la función tareaMuyCostosaFinalizada()
    //
    // OJO: para aplicaciones reales habría que hacer el código un poco más complejo, ver:
    // http://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler
    // http://www.intertech.com/Blog/android-non-ui-to-ui-thread-communications-part-3-of-5/
    // No uses esto directamente en una aplicación real

    public ReceptorMensajes getHandler() {
        return handler;
    }

    public void setHandler(ReceptorMensajes handler) {
        this.handler = handler;
    }

    class ReceptorMensajes extends Handler {
        @Override
        public void handleMessage(Message mensaje) {
            String msg = (String) mensaje.obj;
            if(msg.equals("Finalizado")){
                tareaMuyCostosaFinalizada();
                return;
            }
            detenerTareaMuyCostosa();
            super.handleMessage(mensaje);
        }
    }

    private void tareaMuyCostosaFinalizada(){
        // Aqui podríamos actualizar el interface de usuario, etc.
        Log.i(TAG, "Tratamiento del mensaje de finalización de la tarea muy costosa");
        tareaMuyCostosa = null;
        Toast.makeText(
                this, "¡Tarea muy costosa finalizada!", Toast.LENGTH_SHORT
                ).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barraProgreso = (ProgressBar) findViewById(R.id.barraProgreso);
        barraProgreso.setMax(100);

        instalarManejadores();
    }

    private void instalarManejadores(){
        Button btnTareaCostosa = (Button) findViewById(R.id.btnTareaCostosa);
        Button btnSaludar = (Button) findViewById(R.id.btnSaludar);
        Button btnDetener = (Button) findViewById(R.id.btnDetener);

        // Manejador del botón de saludar
        btnSaludar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.i(TAG, "Saludando");
                Toast.makeText(
                        MainActivity.this, "Hola", Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Manejador del botón que ejecuta la tarea costosa
        btnTareaCostosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.i(TAG, "Ejecutando tarea muy costosa");
                ejecutarTareaMuyCostosa();
            }
        });


        // Manejador del botón que detiene la tarea costosa
        btnDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.i(TAG, "Deteniendo tarea muy costosa");
                detenerTareaMuyCostosa();
            }
        });
    }

    // Lanzamiento de la tarea muy costosa
    private void ejecutarTareaMuyCostosa(){
        if(tareaMuyCostosaEjecutandose()) {
            Log.i(TAG, "La tarea muy costosa ya se estaba ejecutando");
            return;
        }

        tareaMuyCostosa = new TareaMuyCostosa(this,handler);
        new Thread(tareaMuyCostosa).start();
    }

    // Detención de la tarea muy costosa
    private void detenerTareaMuyCostosa(){
        if(!tareaMuyCostosaEjecutandose()) {
            Log.i(TAG, "La tarea muy costosa no se estaba ejecutando, no se puede detener");
            return;
        }
        Toast.makeText(MainActivity.this, "¡Tarea muy costosa detenida!", Toast.LENGTH_SHORT).show();
        tareaMuyCostosa.cancel();
        tareaMuyCostosa = null;
    }

    public ProgressBar getBarraProgreso() {
        return barraProgreso;
    }

    public void setBarraProgreso(ProgressBar barraProgreso) {
        this.barraProgreso = barraProgreso;
    }

    private boolean tareaMuyCostosaEjecutandose(){
        return tareaMuyCostosa != null;
    }

}
