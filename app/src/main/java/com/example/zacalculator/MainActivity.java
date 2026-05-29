package com.example.zacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvGoldTypeStatus, tvGoldValue, tvZakatPayable, tvTotalZakat;
    EditText etGW, etGP;
    Switch swGT;
    Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Zakat Calculator");
        }

        // Link views
        etGW = findViewById(R.id.etGW);
        etGP = findViewById(R.id.etGP);
        swGT = findViewById(R.id.swGT);
        tvGoldTypeStatus = findViewById(R.id.tvGoldTypeStatus);
        tvGoldValue = findViewById(R.id.tvGoldValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);
        btnCalculate = findViewById(R.id.btnCalculate);

        // Switch logic: ON = Keep, OFF = Wear
        swGT.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvGoldTypeStatus.setText("Wear (200g)");
            } else {
                tvGoldTypeStatus.setText("Keep (85g)");
            }
        });

        // Calculate button logic
        btnCalculate.setOnClickListener(v -> {
            try {
                double weight = Double.parseDouble(etGW.getText().toString());
                double price = Double.parseDouble(etGP.getText().toString());
                double uruf = swGT.isChecked() ? 200 : 85; // ON=Keep, OFF=Wear

                double totalGoldValue = weight * price;
                double zakatPayableValue = (weight - uruf) * price;
                if (zakatPayableValue < 0) zakatPayableValue = 0;
                double totalZakat = zakatPayableValue * 0.025; // ✅ 2.5%

                tvGoldValue.setText(String.format("RM %.2f", totalGoldValue));
                tvZakatPayable.setText(String.format("RM %.2f", zakatPayableValue));
                tvTotalZakat.setText(String.format("RM %.2f", totalZakat));
            } catch (Exception e) {
                Toast.makeText(this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (selected == R.id.menuShare) {
            String shareMessage = "Check out ZaCalculator! Accurate Zakat on Gold calculation: https://github.com/Ash-Zen0131/ZaCalculator";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "ZaCalculator App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
