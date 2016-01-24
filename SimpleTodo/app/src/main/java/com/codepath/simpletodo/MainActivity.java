package com.codepath.simpletodo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.simpletodo.sqlite.TodosDatabaseHelper;
import com.codepath.simpletodo.sqlite.models.Todos;
import com.codepath.simpletodo.customadapters.TodosAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Todos> items;
    ArrayAdapter<Todos> itemsAdapter;
    ListView lvItems;
    TodosDatabaseHelper dbHelper;

    private final int REQUEST_CODE = 20;
    private int idx = 0; // storing the last KEY_TODO_ID value

    private final String ACTION_ADD = "add";
    private final String ACTION_DELETE = "delete";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();

        // Get singleton instance of database
        dbHelper = TodosDatabaseHelper.getInstance(this);
        // Initialize ArrayList:items
        List<Todos> todos = dbHelper.getAllTodos();
        readItems(todos);

        itemsAdapter = new TodosAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todos t = items.get(position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems(ACTION_DELETE, t);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Todos t = items.get(position);
                launchEditItemActivity(position, t.value);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etAddItem);
        String itemText = etNewItem.getText().toString().trim(); // getText() will not return null, so it's safe from NullPointerException
        if (itemText == null || itemText.isEmpty()) {
            Toast.makeText(this, "Title " + getResources().getString(R.string.err_should_not_be_empty), Toast.LENGTH_SHORT).show();
            etNewItem.setText("");
        }
        else {
            Todos t = new Todos(++idx, itemText);
            itemsAdapter.add(t);
            etNewItem.setText("");
            writeItems(ACTION_ADD, t);
        }
    }

    private void readItems(List<Todos> todos) {
        for (Todos t : todos) {
            items.add(t);
            if ( idx == 0 || (idx <= t.id) )
                idx = t.id;
        }
    }

    private void writeItems(String action, Todos t) {
        if (dbHelper != null && t != null) {
            if (ACTION_ADD.equalsIgnoreCase(action)) {
                dbHelper.addOrUpdateTodo(t);

            } else if (ACTION_DELETE.equalsIgnoreCase(action)) {
                dbHelper.deleteTodos(t.id);
            }
        }
    }

    public void launchEditItemActivity(int position, String originalItem) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("originalItem", originalItem);
        i.putExtra("position", position);
        startActivityForResult(i, REQUEST_CODE); // brings up the second activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedItem = data.getExtras().getString("editedItem");
            int position = data.getIntExtra("position", -1);

            if (position != -1) {
                Todos t = items.get(position);
                t.value = editedItem;
                items.set(position, t);
                itemsAdapter.notifyDataSetChanged();
                writeItems(ACTION_ADD, t);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.codepath.simpletodo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.codepath.simpletodo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
