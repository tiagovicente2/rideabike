package tiagov.rideabike1;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.*;

import java.util.Calendar;

import android.database.sqlite.*;
import android.database.SQLException;
import android.widget.LinearLayout;

import org.w3c.dom.Text;

import tiagov.rideabike1.databse.Database;
import tiagov.rideabike1.dominio.entity.Logins;
import tiagov.rideabike1.dominio.repository.RepositorySignin;

public class SigninActivity extends AppCompatActivity {

    // datepicker
    private static final String TAG = "SigninActivity";
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    // dados
    private EditText edtNome;
    private EditText edtEndereco;
    private EditText edtEmail;
    private EditText edtSenha;

    private Database dataBase;
    private SQLiteDatabase comn;
    private RepositorySignin repositorySignin;

    private LinearLayout mLayoutContentSignin;

    private Logins logins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtNome     = (EditText)findViewById(R.id.name);
        edtEndereco = (EditText)findViewById(R.id.address);
        edtEmail    = (EditText)findViewById(R.id.email_signin_form);
        edtSenha    = (EditText)findViewById(R.id.password);

        mLayoutContentSignin = (LinearLayout)findViewById(R.id.register_form);

        createConection();

        // Calendario dialog
        /**mDisplayDate = (EditText) findViewById(R.id.user_age);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SigninActivity.this,
                        mDateSetListener,
                        year,month,day);
                dialog.show();
           }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };**/
    }

    // Criando conexão com o banco
    private void createConection(){
        try{
            dataBase = new Database(this);

            comn = dataBase.getWritableDatabase();

            Snackbar.make(mLayoutContentSignin, "Conexão criado com sucesso!", Snackbar.LENGTH_SHORT)
                    .setAction("OK", null)
                    .show();

            repositorySignin = new RepositorySignin(comn);

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage("Erro ao criar o banco: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    public void regNew(View v) {

        //logins.nome = edtNome.getText().toString();
        //logins.email = edtEmail.getText().toString();
        //logins.endereco = edtEndereco.getText().toString();
        //logins.senha = edtSenha.getText().toString();

        logins = new Logins();

        if (!validaCampos()){

            try{
                if (!repositorySignin.verifEmail(logins)){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setMessage(getString(R.string.error_signin_email));
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                    edtEmail.requestFocus();
                }else{
                    repositorySignin.insertData(logins);
                    finish();
                }

            }catch (SQLException ex){
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Erro");
                dlg.setMessage("Erro ao inserir no banco " + ex.getMessage());
                dlg.setNeutralButton("OK", null);
                dlg.show();
            }
        }
    }

    public boolean validaCampos(){

        boolean cancel = false;

        String nome     = edtNome.getText().toString();
        String email    = edtEmail.getText().toString();
        String endereco = edtEndereco.getText().toString();
        String senha    = edtSenha.getText().toString();

        logins.nome     = nome;
        logins.email    = email;
        logins.endereco = endereco;
        logins.senha    = senha;

        View focusView = null;

        if (TextUtils.isEmpty(nome)){
            edtNome.setError(getString(R.string.error_field_required));
            focusView = edtNome;
            cancel = true;
        }
        else if (isNameValid(nome)) {
            edtNome.setError(getString(R.string.error_name));
            focusView = edtNome;
            cancel = true;
        }
        else if (isAddressValid(endereco)) {
            edtEndereco.setError(getString(R.string.error_field_required));
            focusView = edtEndereco;
            cancel = true;
        }
        else if (!isEmailValid(email)) {
            edtEmail.setError(getString(R.string.error_email));
            focusView = edtEmail;
            cancel = true;
        }
        else if (TextUtils.isEmpty(senha)) {
            edtSenha.setError(getString(R.string.error_field_required));
            focusView = edtSenha;
            cancel = true;
        }
        else if (TextUtils.isEmpty(senha)) {
            edtSenha.setError(getString(R.string.error_senha));
            focusView = edtSenha;
            cancel = true;
        }
        else if (!isPasswordValid(senha)) {
            edtSenha.setError(getString(R.string.error_senha));
            focusView = edtSenha;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }

        return cancel;
        /**
        if (res = TextUtils.isEmpty(nome)){
            edtNome.requestFocus();
        }else
            if (res = TextUtils.isEmpty(endereco)){
                edtEndereco.requestFocus();
            }else
                if (res = TextUtils.isEmpty(email)){
                    edtEmail.requestFocus();
                }else
                    if (res =  TextUtils.isEmpty(senha)){
                        edtSenha.requestFocus();
                    }

        if (res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Campos inválidos");
            dlg.setMessage("Campos não podem ser em branco ");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

        return res;**/
    }

    private boolean isPasswordValid(String senha){
        return senha.length() > 3;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isAddressValid(String endereco){
        return TextUtils.isEmpty(endereco);
    }

    private boolean isNameValid(String nome){
        return nome.isEmpty() || nome.length() < 4;
    }

}