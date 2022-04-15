package Hasbi.contactapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class UpdateContact extends AppCompatActivity {
    private AppDatabase db;
    private FloatingActionButton fab;
    private EditText name,job,email,phone;
    public static int contactId;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_view);
        fab = findViewById(R.id.fab);
        setAllViews();
        db = AppDatabase.getInstance(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateContact.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Gson gson = new Gson();
       Contact contact = gson.fromJson(getIntent().getStringExtra("contactJson"), Contact.class);
        contactId = contact.getId();
        name.setText(contact.getName());
        phone.setText(contact.getPhone());
        job.setText(contact.getJob());
        email.setText(contact.getEmail());
        if (contact.getPhoto() != null)
            Picasso.get().load("file:///android_asset/" + contact.getPhoto()+".png").into(imageView,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e("Error "," opening image"+"file:///android_asset/" + contact.getPhoto());
                        }
                    });
    }

    private void setAllViews() {
        name = findViewById(R.id.name);
        job = findViewById(R.id.job);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        imageView = findViewById(R.id.profil_pic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.check:
                //get Updated person from edit text
                String nameTxt=name.getText().toString().trim();
                String jobTxt=job.getText().toString().trim();
                String phoneTxt=phone.getText().toString().trim();
                String emailTxt=email.getText().toString().trim();
                //update person in DB
                db.contactDao().update(contactId, nameTxt,jobTxt,emailTxt,phoneTxt);
                Intent intent =new Intent(UpdateContact.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshViews() {
        name.setText("");
        job.setText("");
        email.setText("");
        phone.setText("");
    }
}