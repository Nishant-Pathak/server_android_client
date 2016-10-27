package com.nishant.starterkit.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nishant.starterkit.data.model.Person;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PersonOpenHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;

  @Inject
  public PersonOpenHelper(Context context) {
    super(context, null, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Person.CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
