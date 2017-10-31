package tiagov.rideabike1.databse;

/**
 * Created by tiago on 26/09/2017.
 */

import android.content.Context;
import android.database.sqlite.*;


public class Database extends SQLiteOpenHelper {

    public Database(Context context){
        super(context, "USER", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ScriptSQL.getCreateTableUser() );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
