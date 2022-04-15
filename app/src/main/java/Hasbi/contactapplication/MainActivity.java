package Hasbi.contactapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Contact> contacts =new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AppDatabase db;
    MainAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.CALL_PHONE
                },
                1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                startActivity(intent);
            }
        });


        recyclerView=findViewById(R.id.recyclerView);

        //initialize database
        db = AppDatabase.getInstance(this);
        //get data in contacts list
        contacts = db.contactDao().getAll();

        //initialize & set linear layout manager in recycleview
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter=new MainAdapter(MainActivity.this, contacts, new MainAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                Intent intent = new Intent(MainActivity.this,UpdateContact.class);
                Gson gson = new Gson();
                String contactJson = gson.toJson(contact);
                intent.putExtra("contactJson", contactJson);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(Contact contact) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_warn)
                        .setTitle("Delete this contact ?")
                        .setMessage("This contact will be deleted permanently !")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.contactDao().delete(contact);
                                contacts.clear();
                                contacts.addAll(db.contactDao().getAll());
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
        //recyclerView.scrollToPosition(contacts.size()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //la recherche
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search contact...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override //every change of carac
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
                case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}