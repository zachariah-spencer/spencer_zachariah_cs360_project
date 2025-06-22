package com.example.spencer_zachariah_cs360_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spencer_zachariah_cs360_project.db.DatabaseHelper;
import com.example.spencer_zachariah_cs360_project.db.Item;
import com.example.spencer_zachariah_cs360_project.db.ItemAdapter;
import java.util.ArrayList;
import java.util.List;

public class DataDisplayActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_REQUEST = 200;

    private DatabaseHelper dbHelper;
    private ItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        RecyclerView recycler = findViewById(R.id.data_recycler);
        adapter = new ItemAdapter(this, new ItemAdapter.OnItemChangedListener() {
            @Override
            public void onDelete(Item item) {
                dbHelper.deleteItem(item.id);
                loadItems();
            }

            @Override
            public void onUpdate(Item item) {
                showUpdateDialog(item);
            }
        });
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);

        Button addButton = findViewById(R.id.add_data_button);
        addButton.setOnClickListener(v -> showAddDialog());

        loadItems();
    }

    /**
     * Retrieve all items from database and display in the adapter.
     */
    private void loadItems() {
        Cursor cursor = dbHelper.getAllItems();
        List<Item> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME));
            int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_QTY));
            list.add(new Item(id, name, qty));
        }
        cursor.close();
        adapter.setItems(list);
    }

    private void showAddDialog() {
        final EditText nameInput = new EditText(this);
        nameInput.setHint(R.string.item_name_hint);
        final EditText qtyInput = new EditText(this);
        qtyInput.setHint(R.string.item_quantity_hint);
        qtyInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.add_data)
                .setPositiveButton(android.R.string.ok, (d, which) -> {
                    String name = nameInput.getText().toString().trim();
                    int qty = parseQty(qtyInput.getText().toString());
                    dbHelper.addItem(name, qty);
                    checkLowInventoryAndNotify(name, qty);
                    loadItems();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setView(getDialogLayout(nameInput, qtyInput));
        dialog.show();
    }

    private void showUpdateDialog(Item item) {
        final EditText qtyInput = new EditText(this);
        qtyInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        qtyInput.setText(String.valueOf(item.quantity));

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(item.name)
                .setView(qtyInput)
                .setPositiveButton(android.R.string.ok, (d, which) -> {
                    int qty = parseQty(qtyInput.getText().toString());
                    dbHelper.updateItem(item.id, qty);
                    checkLowInventoryAndNotify(item.name, qty);
                    loadItems();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    /**
     * Helper to place two EditTexts in a vertical layout.
     */
    @NonNull
    private android.widget.LinearLayout getDialogLayout(EditText nameInput, EditText qtyInput) {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(nameInput);
        layout.addView(qtyInput);
        return layout;
    }

    private int parseQty(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Send an SMS message when inventory is low and permission is granted.
     */
    private void checkLowInventoryAndNotify(String name, int qty) {
        if (qty > 5) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager.getDefault().sendTextMessage("5554", null,
                    "Low inventory: " + name + " (" + qty + ")", null, null);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)) {
            Toast.makeText(this, R.string.sms_permission_prompt, Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.sms_permission_granted, Toast.LENGTH_SHORT).show();
        }
    }
}