package foxsay.ru.loftmoney;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private static final String TAG = "itemsAdapter";

    private List<Item> items = Collections.emptyList();
    private ItemsAdapterListener listener = null;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    private String type;

    public ItemsAdapter(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        this.items.add(item);
        notifyItemInserted(items.size());
    }

    Item removeItem(int position) {
        Item item = items.get(position);
        this.items.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    void toggleItem(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.put(position, false);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    List<Integer> getSelectedPositions() {
        List<Integer> selectedPositions = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);

            if (selectedItems.get(key)) {
                selectedPositions.add(key);
            }
        }

        return selectedPositions;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);

        return new ItemViewHolder(view, getType());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bindItem(item, selectedItems.get(position));
        holder.setListener(item, listener, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView price;

        private Context context;

        public ItemViewHolder(@NonNull View itemView, String type) {
            super(itemView);

            context = itemView.getContext();

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);

            if (type.equals(Item.TYPE_INCOME)) {
                price.setTextColor(context.getResources().getColor(R.color.income_color));
            } else {
                price.setTextColor(context.getResources().getColor(R.color.expense_color));
            }
        }

        void bindItem(Item item, boolean selected) {
            name.setText(item.getName());
            price.setText(String.valueOf(item.getPrice()));

            itemView.setSelected(selected);
        }

        void setListener(Item item, ItemsAdapterListener listener, int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(item, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onItemLongClick(item, position);
                    }
                    return true;
                }
            });
        }

    }

}
