package tiagov.rideabike1.dominio.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

import tiagov.rideabike1.dominio.entity.Logins;

/**
 * Created by tiago on 27/09/2017.
 */

public class RepositorySignin {

    private SQLiteDatabase conm;

    public RepositorySignin(SQLiteDatabase conm){
        this.conm = conm;
    }

    // Verificando email
    public boolean verifEmail(Logins logins){
        boolean result = false;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EMAIL FROM USER ");

        Cursor resultado = conm.rawQuery(sql.toString(), null);

        if(resultado.moveToFirst()){
            do {
                if (logins.email.equals(resultado.getColumnName(0)) ){
                    result = true;
                    break;
                }
            }while (resultado.moveToNext());
        }

        return result;
    }

    // Inserindo dados
    public void insertData(Logins logins) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", logins.nome);
        contentValues.put("ENDERECO", logins.endereco);
        contentValues.put("EMAIL", logins.email);
        contentValues.put("SENHA", logins.senha);

        conm.insertOrThrow("USER", null, contentValues);
    }

    public List<Logins> login(){

        List<Logins> logins = new ArrayList<Logins>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EMAIL, SENHA ");
        sql.append("    FROM LOGINS ");

        conm.rawQuery(sql.toString(), null);

        //Cursor resultado = conm.rawQuery(sql.toString(), null);
        //resultado.moveToFirst();


        return logins;

    }

}
