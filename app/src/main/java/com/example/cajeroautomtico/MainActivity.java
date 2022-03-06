package com.example.cajeroautomtico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etvalor;
    RadioButton radiobtConsignar, radiobtRetirar;
    int saldoActual;
    TextView tvSaldoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etvalor = (EditText)findViewById(R.id.etvalor);
        radiobtRetirar = findViewById(R.id.radiobtRetirar);
        radiobtConsignar = findViewById(R.id.radiobtConsignar);
        tvSaldoActual = findViewById(R.id.tvSaldoActual);
        Consulta(null);
    }

    public void Consignar(View view)
    {
        AdminBD admin = new AdminBD(this, "BaseDatos", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        String Valor = etvalor.getText().toString();
        if (radiobtConsignar.isChecked())
        {
            if (!etvalor.getText().toString().isEmpty())
            {
                 saldoActual += Integer.parseInt(etvalor.getText().toString());
            }
            ContentValues insertar = new ContentValues();
            insertar.put("SaldoActual", saldoActual);
            BaseDatos.insert("Operaciones", null, insertar);
            BaseDatos.close();
            Toast.makeText(this, "Transacción exitosa", Toast.LENGTH_SHORT).show();
            Limpiar();
            Consulta(null);
        }
    }

    public void Retirar(View view)
    {
        AdminBD admin = new AdminBD(this, "BaseDatos", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        String Valor = etvalor.getText().toString();
        if (radiobtRetirar.isChecked())
        {
            if (!etvalor.getText().toString().isEmpty())
            {
                if (Integer.parseInt(etvalor.getText().toString()) > saldoActual)
                {
                        Toast.makeText(this, "El valor a retirar es mayor al saldo disponible", Toast.LENGTH_SHORT).show();
                        return;
                }
                else
                {
                    saldoActual -= Integer.parseInt(etvalor.getText().toString());
                }
            }
            ContentValues insertar = new ContentValues();
            insertar.put("SaldoActual", saldoActual);
            BaseDatos.insert("Operaciones", null, insertar);
            BaseDatos.close();
            Toast.makeText(this, "Transacción exitosa", Toast.LENGTH_SHORT).show();
            Limpiar();
            Consulta(null);
        }
    }
    public void Consulta (View view)
    {
        AdminBD adminBD = new AdminBD(this, "BaseDatos", null, 1);
        SQLiteDatabase sqLiteDatabase = adminBD.getWritableDatabase();

        Cursor Fila = sqLiteDatabase.rawQuery("select SaldoActual from Operaciones order by Transaccion desc limit 1", null);
        //Cursor Fila = sqLiteDatabase.rawQuery("select SaldoActual, TipoTransaccion from BancoTransacciones order by IdRows desc limit 1", null);
        if (Fila.moveToFirst())
        {
            saldoActual = Integer.parseInt(Fila.getString((0)));
            Toast.makeText(this, "Su saldo disponible es de: " + saldoActual,Toast.LENGTH_LONG).show();
            tvSaldoActual.setText("Su saldo disponible es de: " + saldoActual);
        }
    }

    public void Limpiar()
    {
        etvalor.setText("");
    }

}