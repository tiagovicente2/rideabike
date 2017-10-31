package tiagov.rideabike1.databse;

/**
 * Created by tiago on 27/09/2017.
 */

public class ScriptSQL {

    public static String getCreateTableUser(){

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS USER ( ");
        sqlBuilder.append("      CODIGO   INTEGER       PRIMARY KEY AUTOINCREMENT NOT NULL, " );
        sqlBuilder.append("      NOME     VARCHAR (250) NOT NULL DEFAULT (''), " );
        sqlBuilder.append("      ENDERECO VARCHAR (200) NOT NULL DEFAULT (''), ");
        sqlBuilder.append("      EMAIL    VARCHAR (200) NOT NULL DEFAULT (''), ");
        ///sqlBuilder.append("DATANASC           DATE, ");
        sqlBuilder.append("      SENHA    VARCHAR (50)  NOT NULL DEFAULT ('') ) ");

        return sqlBuilder.toString();
    }

}