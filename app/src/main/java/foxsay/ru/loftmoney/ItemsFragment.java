package foxsay.ru.loftmoney;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {

    private static final String TAG = "itemsFragment";

    private static final int ADD_ITEM_REQUEST_CODE = 111;

    public static ItemsFragment newInstance(String type) {
        ItemsFragment fragment = new ItemsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);

        fragment.setArguments(bundle);

        return fragment;

    }


    public static final String KEY_TYPE = "type";

    private SwipeRefreshLayout refresh;

    private String token = "$2y$10$MI9aJHOPZNR1WLHMPoRkx.6geJcwuzU/JxArRxeOoK9KXyPs3DzfG";

    private ItemsAdapter adapter;
    private String type;

    private Api api;

    public ItemsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();

        type = getArguments().getString(KEY_TYPE);

        Application application = getActivity().getApplication();
        App app = (App) application;
        api = app.getApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        int color1 = requireContext().getResources().getColor(R.color.colorPrimary);
        int color2 = requireContext().getResources().getColor(R.color.colorAccent);

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(color1, color2);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        RecyclerView recycler = view.findViewById(R.id.recycler);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        loadItems();
    }

    private void loadItems() {
        Call call = api.getItems(type, token);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                refresh.setRefreshing(false);
                List<Item> items = (List<Item>) response.body();
                adapter.setItems(items);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    void onFabClick() {
        Intent intent = new Intent(requireContext(), AddItemActivity.class);
        startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                String name = data.getStringExtra(AddItemActivity.KEY_NAME);
                String price = data.getStringExtra(AddItemActivity.KEY_PRICE);

                Item item = new Item(name, Double.valueOf(price), type);
                adapter.addItem(item);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
