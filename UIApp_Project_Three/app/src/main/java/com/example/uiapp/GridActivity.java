package com.example.uiapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/** Inventory grid + SMS logic. */
public class GridActivity extends AppCompatActivity implements DataAdapter.OnItemClickListener {

    private DatabaseHelper db;
    private Cursor cursor;
    private final List<DataItem> items = new ArrayList<>();
    private DataAdapter adapter;

    private final ActivityResultLauncher<String> smsPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    granted -> updateSmsUi());

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_grid);

        db = new DatabaseHelper(this);

        /* ----------  RecyclerView  ---------- */
        RecyclerView rv = findViewById(R.id.dataRecyclerView);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new DataAdapter(items, this);
        rv.setAdapter(adapter);

        /* ----------  Buttons  ---------- */
        FloatingActionButton addBtn = findViewById(R.id.addDataButton);
        Button permBtn             = findViewById(R.id.requestPermissionButton);

        addBtn.setOnClickListener(v -> showAddDialog());
        permBtn.setOnClickListener(v ->
                smsPermission.launch(Manifest.permission.SEND_SMS));

        loadItems();
        updateSmsUi();
    }

    /* ----------  CRUD  ---------- */

    private void loadItems() {
        if (cursor != null) cursor.close();
        cursor = db.getAllItems();
        items.clear();
        while (cursor.moveToNext()) {
            long   id  = cursor.getLong (cursor.getColumnIndexOrThrow(DatabaseHelper.C_ID));
            String t   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.C_TITLE));
            String d   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.C_DETAILS));
            int    qty = cursor.getInt   (cursor.getColumnIndexOrThrow(DatabaseHelper.C_QTY));
            items.add(new DataItem(id, t, d, qty));
        }
        adapter.notifyDataSetChanged();
    }

    @Override public void onDeleteClick(int pos) {
        db.deleteItem(items.get(pos).getId());
        loadItems();
        maybeSendSms();
    }

    private void showAddDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_add_data, null);
        EditText tEt = v.findViewById(R.id.titleEditText);
        EditText dEt = v.findViewById(R.id.detailsEditText);
        EditText qEt = v.findViewById(R.id.qtyEditText);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_item)
                .setView(v)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String t = tEt.getText().toString().trim();
                    String de = dEt.getText().toString().trim();
                    int q = qEt.getText().toString().trim().isEmpty()
                            ? 1 : Integer.parseInt(qEt.getText().toString().trim());
                    if (!t.isEmpty()) {
                        db.addItem(t, de, q);
                        loadItems();
                        maybeSendSms();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /* ----------  SMS logic  ---------- */

    private void updateSmsUi() {
        Button permBtn = findViewById(R.id.requestPermissionButton);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            permBtn.setVisibility(View.GONE);
            findViewById(R.id.notificationStatusTextView)
                    .setVisibility(View.GONE);
        } else {
            permBtn.setVisibility(View.VISIBLE);
            findViewById(R.id.notificationStatusTextView)
                    .setVisibility(View.VISIBLE);
        }
    }

    private void maybeSendSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) return;

        if (db.isLowInventory()) {
            try {
                SmsManager.getDefault()
                        .sendTextMessage("1234567890", null,
                                "Inventory low! Please restock.", null, null);
                Toast.makeText(this, R.string.sms_sent, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.sms_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        db.close();
    }
}
