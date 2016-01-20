package es.florida_uni.dam.practica3;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.widget.ProgressBar;

public class TareaMuyCostosa implements Runnable {

    public final static int MSG_TAREA_FINALIZADA = 1;

    final static String TAG = "SYP-practica3";

    volatile boolean interrumpido = false;

    MainActivity actividadPadre;
    Handler handler;

    public TareaMuyCostosa(MainActivity actividadPadre, Handler handler) {
        this.actividadPadre = actividadPadre;
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "Tarea muy costosa iniciada");

        try {
            for (int i = 0; i < 100; i++) {
                if (interrumpido) return;
                Log.i(TAG, "Tarea muy costosa en marcha...");
                mostrarProgreso(i);
                Thread.sleep(50);
            }
            Log.i(TAG, "Tarea muy costosa finalizada");
            notificarFinalizacion();
        } catch (InterruptedException e) {
            return;
        }

        Log.i(TAG, "Tarea muy costosa finalizada");
    }

    // TODO: Mostrar el progreso en la barra principal
    // Debéis hacerlo utilizando runOnUIThread o enviando un runnable
    private void mostrarProgreso(final int progreso) {
        actividadPadre.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actividadPadre.getBarraProgreso().setProgress(progreso);
            }
        });


    }

    // TODO: Avisar de la finalización de la tarea
    // Debéis hacerlo enviando un mensaje a la actividad principal
    // No utilices runnables aquí
    private void notificarFinalizacion() {
        String message = "";
        if (interrumpido) {
            message = "Interrumpido";

        } else {
            message = "Finalizado";

        }
        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = message; // Put the string into Message, into "obj" field.
        msg.setTarget(handler); // Set the Handler
        msg.sendToTarget();
//....
    }

    synchronized public void cancel() {
        if (interrumpido) return;
        interrumpido = true;
        Thread.currentThread().interrupt();
    }
}
