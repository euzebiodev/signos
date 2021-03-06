package mobile.devmedia.com.signos;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Principal extends AppCompatActivity {
    private Spinner spinnerDia = null;
    private Spinner spinnerMes = null;

    private void validarData(){
        int dia = spinnerDia.getSelectedItemPosition();
        int mes = spinnerMes.getSelectedItemPosition();

        dia ++;
        mes ++;

        if (dia > 29 && mes == 2) {
            spinnerDia.setSelection(28);
        } else {
            if (( mes == 4 || mes == 6 || mes == 9 || mes == 11) &&  (dia > 30)) {
                spinnerDia.setSelection(29);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        StrictMode.ThreadPolicy policy = new   StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        spinnerDia = (Spinner) findViewById(R.id.spinnerDia);
        spinnerMes = (Spinner) findViewById(R.id.spinnerMes);

        ArrayAdapter<CharSequence> adapter_dia = ArrayAdapter.createFromResource(this, R.array.dias, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_mes = ArrayAdapter.createFromResource(this, R.array.meses, android.R.layout.simple_spinner_item);

        adapter_dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_mes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDia.setAdapter(adapter_dia);
        spinnerMes.setAdapter(adapter_mes);

        spinnerDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                validarData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        // spinner mes
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                validarData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        Button enviar = (Button) findViewById(R.id.buttonVMS);
        enviar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                int posicaoDia = spinnerDia.getSelectedItemPosition();
                int posicaoMes = spinnerMes.getSelectedItemPosition();

                posicaoDia++;
                posicaoMes++;

                InterpretadorSigno Interpretador = new InterpretadorSigno();

                Signo signoResultado = Interpretador.interpretar(posicaoDia, posicaoMes);

                if(signoResultado.setPrevisao() == false){
                    Toast.makeText(getApplicationContext(), "N??o foi poss??vel conectar. Cheque a internet e tente novamente.", Toast.LENGTH_LONG).show();
                }

                Bundle args = new Bundle();
                args.putSerializable("resultado", signoResultado);

                Intent intent = new Intent(Principal.this, Resultado.class);
                intent.putExtra("signo", args);

                startActivity(intent);
            }
        });
    }
}
