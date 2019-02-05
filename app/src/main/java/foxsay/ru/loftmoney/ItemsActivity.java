package foxsay.ru.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


public class ItemsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        adapter = new ItemsAdapter();

        recycler = findViewById(R.id.recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<Item> items = new ArrayList<>();
        items.add(new Item("Молоко", "70"));
        items.add(new Item("Зубная щётка", "70"));
        items.add(new Item("Сковородка с антипригарным покрытием", "1670"));

        adapter.setItems(items);
    }
}
