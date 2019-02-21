package foxsay.ru.loftmoney;


import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

    private ItemsAdapter adapter;
    private String type;


    private Api api;
    private ActionMode actionMode;

    public ItemsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getArguments().getString(KEY_TYPE);

        adapter = new ItemsAdapter(type);
        adapter.setListener(new AdapterListener());

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
                if (actionMode != null) {
                    actionMode.finish();
                }
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String token = preferences.getString("auth_token", null);

        Call<List<Item>> call = api.getItems(type, token);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                refresh.setRefreshing(false);
                List<Item> items = response.body();

                adapter.setItems(items);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                refresh.setRefreshing(false);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void removeItem(Long id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String token = preferences.getString("auth_token", null);

        Call<Object> call = api.removeItem(id, token);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    void onFabClick() {
        Intent intent = new Intent(requireContext(), AddItemActivity.class);
        intent.putExtra(AddItemActivity.KEY_TYPE, type);
        startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            loadItems();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class AdapterListener implements ItemsAdapterListener {

        @Override
        public void onItemClick(Item item, int position) {

            if (actionMode == null) {
                return;
            }

            toggleItem(position);

            if (actionMode != null) {
                onListItemSelect();
            }
        }

        @Override
        public void onItemLongClick(Item item, int position) {

            if (actionMode != null) {
                return;
            }

            getActivity().startActionMode(new ActionModaCallback());
            toggleItem(position);
            onListItemSelect();
        }

        private void toggleItem(int position) {
            adapter.toggleItem(position);
        }
    }

    private class ActionModaCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = new MenuInflater(requireContext());
            inflater.inflate(R.menu.menu_action_mode, menu);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.delete_item) {
                showDialog();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelections();
        }

        void removeSelectedItems() {
            List<Integer> selectedPositions = adapter.getSelectedPositions();

            for (int i = selectedPositions.size() - 1; i >= 0; i--) {
                Item item = adapter.removeItem(selectedPositions.get(i));
                removeItem(item.getId());
            }

            actionMode.finish();
        }

        void showDialog() {
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setMessage(R.string.dialog_message)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeSelectedItems();
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();

            dialog.show();
        }
    }

    private void onListItemSelect() {
        if (actionMode != null)
            actionMode.setTitle(ItemsFragment.this.getResources().getString(R.string.selected_label) + " " + String.valueOf(adapter.getSelectedPositions().size()));
    }
}
