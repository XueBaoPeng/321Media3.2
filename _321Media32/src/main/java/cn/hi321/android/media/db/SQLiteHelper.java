package cn.hi321.android.media.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import cn.hi321.android.media.utils.Utils;
/**
 * @author yanggf
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	

	public SQLiteHelper(Context context) {
		super(context, Utils.DB_NAME, null, Utils.DB_VERSION);

	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists playhistoryinfos(_mid VARCHAR(20) primary key,_mediatype VARCHAR(20),_medianame VARCHAR(20),_hashid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER,_movieposition INTEGER,_movieplayedtime INTEGER,_size String,_percent String,_purl String)");
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		db.execSQL("DROP TABLE IF EXISTS playhistoryinfos");
		db.execSQL("create table if not exists playhistoryinfos(_mid VARCHAR(20) primary key,_mediatype VARCHAR(20),_medianame VARCHAR(20),_hashid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER,_movieposition INTEGER,_movieplayedtime INTEGER,_size String,_percent String,_purl String)");
	}

}
