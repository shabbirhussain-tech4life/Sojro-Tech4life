package com.mdcbeta.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mdcbeta.data.model.UserLogin;

/**
 * Created by Shakil Karim on 3/26/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE = "dumdata.db";
    public static String ID = "id";
    public static String CASECODE = "case_code";
    public static String REFERRER = "referer_id";
    public static String PATIENTID = "patient_id";
    public static String FIRSTNAME = "fname";
    public static String LASTNAME = "lname";
    public static String GENDER = "gender";
    public static String LOCATION = "location_id";
    public static String AGEINY = "y_age";
    public static String AGEINM = "month_age";
    public static String COMPLAIN = "complain";
    public static String HISTORY = "history";
    public static String COMMENTS = "comments";
    public static String ORIGINDATE = "created_at";
    public static String QUESTION = "question";
    public static String FORMID = "form_id";
    public static String IMAGEURL = "imageurl";
    public static String DATA = "data";
    public static String CREATEDAS = "created_as";
    public static String CREATEDAT = "created_as";
    public static String COMMENTEDNAME = "commented_name";
    public static String COMMENT = "comment";
    public static String CREATEDDATE = "created_date";

    public static String TABLE ="mytable";
    public static String TABLE1 ="records";
    public static String TABLE_COMMENT ="table_comment";
    public static String TABLE2 ="users";

    String br;
    String test;
    String login;

    public DbHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        br = "CREATE TABLE "+TABLE+"("+REFERRER+ " Text, "+PATIENTID+ " Text, "+FIRSTNAME+ " Text, "+LASTNAME+ " Text, "+GENDER+ " Text, "+AGEINY+ " Text, "+AGEINM+ " Text, "+LOCATION+ " Text, "+COMPLAIN+ " Text, "+HISTORY+ " Text, "+COMMENTS+ " Text, "+ORIGINDATE+ " Text, "+IMAGEURL+ " Text);";

        test = "CREATE TABLE "+TABLE1+"(id INTEGER PRIMARY KEY AUTOINCREMENT, case_code TEXT, fname TEXT, lname TEXT, gender TEXT, y_age TEXT, month_age TEXT, m_age TEXT, disease_id INTEGER, location_id INTEGER, history TEXT, question TEXT, city INTEGER, form_id INTEGER, patient_id INTEGER, referer_id INTEGER, data TEXT, created_as TEXT, created_at TEXT, updated_at TEXT, deleted_at TEXT);";

        login = "CREATE TABLE "+TABLE2+"(user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);";

        db.execSQL(br);
        db.execSQL(test);
        db.execSQL(login);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE );

        onCreate(db);

    }

    /*  public List<DataModel> getdata(){
          // DataModel dataModel = new DataModel();
          List<DataModel> data=new ArrayList<>();
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor cursor = db.rawQuery("select * from "+TABLE+" ;",null);
          StringBuffer stringBuffer = new StringBuffer();
          DataModel dataModel = null;
          while (cursor.moveToNext()) {
              dataModel= new DataModel();

              String referrer = cursor.getString(cursor.getColumnIndexOrThrow("referer_id"));
              String patientid = cursor.getString(cursor.getColumnIndexOrThrow("patient_id"));
              String firstname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
              String lastname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
              String location = cursor.getString(cursor.getColumnIndexOrThrow("location_id"));
              String ageinyear = cursor.getString(cursor.getColumnIndexOrThrow("y_age"));
              String ageinmonth = cursor.getString(cursor.getColumnIndexOrThrow("month_age"));
              String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
              String complain = cursor.getString(cursor.getColumnIndexOrThrow("complain"));
              String history = cursor.getString(cursor.getColumnIndexOrThrow("history"));
              String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));
              String dateorigin = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
              String imageurl = cursor.getString(cursor.getColumnIndexOrThrow("imageurl"));

            //  dataModel.setId(id);
              dataModel.setReferrer(referrer);
              dataModel.setPatientid(patientid);
              dataModel.setFirstname(firstname);
              dataModel.setLastname(lastname);
              dataModel.setLocation(location);
              dataModel.setAgeinyear(ageinyear);
              dataModel.setAgeinmonth(ageinmonth);
              dataModel.setGender(gender);
              dataModel.setComplains(complain);
              dataModel.setHistory(history);
              dataModel.setComments(comments);
              dataModel.setOrigindate(dateorigin);
              dataModel.setImageurl(imageurl);

              stringBuffer.append(dataModel);
              // stringBuffer.append(dataModel);
              data.add(dataModel);
          }

          for (DataModel mo:data ) {

              Log.i("Hellomo",""+mo.getCity());
          }

          return data;
      }
  */
    public void insertrecords(String referer_id,String case_code ,String firstname,String lastname, String gender, String y_age, String month_age, String location,String complain, String history,String comments,String dateorigin){
        System.out.print("Hello "+br);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put("referer_id", referer_id);
        contentValues.put("case_code", case_code);
        contentValues.put("fname", firstname);
        contentValues.put("lname", lastname);
        contentValues.put("gender", gender);
        contentValues.put("y_age", y_age);
        contentValues.put("month_age", month_age);
        contentValues.put("location_id", location);
        contentValues.put("question", complain);
        contentValues.put("history", history);
        contentValues.put("data", comments);
        contentValues.put("created_at", dateorigin);

        db.insert(TABLE1,null,contentValues);
        db.close();

    }

    public void insertuser(String username, String password){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);

        db.insert(TABLE2,null,contentValues);
        db.close();
    }

    public void addUser(UserLogin usr)
    {
        // getting db instance for writing the user
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        // cv.put(User_id,usr.getId());
        cv.put("username",usr.getName());
        cv.put("password",usr.getPassword());
        //inserting row
        db.insert(TABLE2, null, cv);
        //close the database to avoid any leak
        db.close();
    }
    public int checkUser(UserLogin us)
    {
        int id=-1;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT user_id FROM users WHERE username=? AND password=?",new String[]{us.getName(),us.getPassword()});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public void insertcomments(String patientid, String commentedname,String createddate ,String textcomment){
        System.out.print("Hello "+br);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put(PATIENTID, patientid);
        contentValues.put(COMMENTEDNAME, commentedname);
        contentValues.put(CREATEDDATE, createddate);
        contentValues.put(COMMENT, textcomment);

        db.insert(TABLE_COMMENT,null,contentValues);
        db.close();

    }

}


