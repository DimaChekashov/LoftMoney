package foxsay.ru.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    public final static String KEY_NAME = "name";
    public final static String KEY_PRICE = "price";

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                String nameText = name.getText().toString();
                String priceText = price.getText().toString();

                intent.putExtra(KEY_NAME, nameText);
                intent.putExtra(KEY_PRICE, priceText);

                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
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

