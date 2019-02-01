package foxsay.ru.loftmoney;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class EditTextWatcher implements TextWatcher {

    private EditText secondText;
    private Button button;

    public EditTextWatcher(EditText secondText, Button button) {
        this.secondText = secondText;
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(secondText.getText()) && !TextUtils.isEmpty(s)) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
    }
}
