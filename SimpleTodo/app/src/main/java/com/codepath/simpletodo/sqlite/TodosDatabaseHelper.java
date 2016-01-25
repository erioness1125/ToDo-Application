package com.codepath.simpletodo.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodo.sqlite.models.Todos;

import java.util.ArrayList;
import java.util.List;

public class TodosDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "simpleTodoDB";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    private static final String TABLE_TODOS = "t_todos";

    // Table Columns
    private static final String KEY_TODO_ID = "c_id";
    private static final String KEY_TODO_VALUE = "c_value";
    private static final String KEY_TODO_DUEDATE = "c_duedate";

    private static final String ERR_TAG = "ERROR";

    // singleton pattern
    private static TodosDatabaseHelper sInstance;

    public static synchronized TodosDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodosDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public TodosDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_VALUE + " TEXT," +
                KEY_TODO_DUEDATE + " INTEGER" +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    // Insert an item into the database
    public void addOrUpdateTodo(Todos todo) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_DUEDATE, todo.dueDate);
            values.put(KEY_TODO_VALUE, todo.value);
            values.put(KEY_TODO_ID, todo.id);

            // First try to update the item in case that already exists in the database
            int rows = db.update(TABLE_TODOS, values, KEY_TODO_ID + "= ?", new String[]{String.valueOf(todo.id)});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_TODO_ID, TABLE_TODOS, KEY_TODO_VALUE);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(todo.value)});
                try {
                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // insert new
                id = db.insertOrThrow(TABLE_TODOS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(ERR_TAG, "Error while trying to add or update item");
        } finally {
            db.endTransaction();
        }
    }

    public List<Todos> getAllTodos() {
        List<Todos> todos = new ArrayList<>();

        String SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_TODOS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Todos singleTodo = new Todos(
                            cursor.getInt( cursor.getColumnIndex(KEY_TODO_ID) ),
                            cursor.getString( cursor.getColumnIndex(KEY_TODO_VALUE)),
                            cursor.getLong(cursor.getColumnIndex(KEY_TODO_DUEDATE))
                    );
                    todos.add(singleTodo);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(ERR_TAG, "Error while trying to get todos from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;
    }

    // Update the existing item
    public int updateTodo(Todos todos) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_VALUE, todos.value);
        values.put(KEY_TODO_DUEDATE, todos.dueDate);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_TODOS, values, KEY_TODO_ID + " = ?",
                new String[] { String.valueOf(todos.id) });
    }

    // delete an item or all in the todos table
    public void deleteTodos(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            // Order of deletions is important when foreign key relationships exist.
            if (id < 0)
                db.delete(TABLE_TODOS, null, null);
            else {
                db.delete(TABLE_TODOS, KEY_TODO_ID + "=" + id, null);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(ERR_TAG, "Error while trying to delete item(s) in todos");
        } finally {
            db.endTransaction();
        }
    }
}
