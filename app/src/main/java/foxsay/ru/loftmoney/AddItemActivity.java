package foxsay.ru.loftmoney;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

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

        name.addTextChangedListener(addItemWatcher);
        price.addTextChangedListener(addItemWatcher);

    }

    private TextWatcher addItemWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String nameEdit = name.getText().toString().trim();
            String priceEdit = price.getText().toString().trim();

            addBtn.setEnabled(!TextUtils.isEmpty(nameEdit) && !TextUtils.isEmpty(priceEdit));
        }
    };
}

