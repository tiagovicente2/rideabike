package tiagov.rideabike1.dominio.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tiagov.rideabike1.dominio.entity.Logins;

/**
 * Created by tiago on 27/09/2017.
 */

public class RepositoryLogin {

    private SQLiteDatabase conm;

    public RepositoryLogin(SQLiteDatabase conm){
        this.conm = conm;
    }



    public boolean login(String email, String password){

        boolean result = false;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EMAIL, SENHA ");
        sql.append("    FROM USER ");

        Cursor resultado = conm.rawQuery(sql.toString(), null);

        String a, b;

        if (resultado.moveToFirst()) {
            do {
                a = resultado.getString(0);

                if (a.equals(email)) {
                    b = resultado.getString(1);
                    if(b.equals(password))
                        result = true;
                }
            } while (resultado.moveToNext());
        }
        return result;
    }

}
