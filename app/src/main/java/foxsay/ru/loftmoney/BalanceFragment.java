package foxsay.ru.loftmoney;


import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {

    private static final String TAG = "balanceFragment";

    public BalanceFragment() {
        // Required empty public constructor
    }

    public static BalanceFragment newInstance() {

        Bundle args = new Bundle();

        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Api api;

    private TextView balanceView;
    private TextView expenseView;
    private TextView incomeView;
    private DiagramView diagramView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application application = getActivity().getApplication();
        App app = (App) application;
        api = app.getApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        balanceView = view.findViewById(R.id.balance_value);
        expenseView = view.findViewById(R.id.expense_value);
        incomeView = view.findViewById(R.id.income_value);
        diagramView = view.findViewById(R.id.diagram_view);
    }

    private void loadBalance() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String token = preferences.getString("auth_token", null);

        Call<BalanceResponse> call = api.balance(token);

        call.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<BalanceResponse> call, @NonNull Response<BalanceResponse> response) {
                BalanceResponse balanceResponse = response.body();

                if (balanceResponse != null) {
                    int balance = balanceResponse.getTotalIncome() - balanceResponse.getTotalExpense();

                    balanceView.setText(getString(R.string.count, balance));
                    expenseView.setText(getString(R.string.count, balanceResponse.getTotalExpense()));
                    incomeView.setText(getString(R.string.count, balanceResponse.getTotalIncome()));

                    diagramView.update(balanceResponse.getTotalIncome(), balanceResponse.getTotalExpense());
                } else {
                    Log.i(TAG, "balanceResponse: " + balanceResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BalanceResponse> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            loadBalance();
        }
    }
}
