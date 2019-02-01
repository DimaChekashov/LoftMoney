package foxsay.ru.loftmoney;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText name;
    private EditText price;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = findViewById(R.id.add_item_name);
        price = findViewById(R.id.add_item_price);
        addBtn = findViewById(R.id.add_item_button);

        name.addTextChangedListener(new EditTextWatcher(price, addBtn));
        price.addTextChangedListener(new EditTextWatcher(name, addBtn));

    }
}

