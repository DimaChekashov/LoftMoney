package foxsay.ru.loftmoney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private ViewPager pager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainPagesAdapter adapter = new MainPagesAdapter(getSupportFragmentManager(), this);

        pager = findViewById(R.id.pager);
        tabs = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);

        setSupportActionBar(toolbar);

        tabs.setupWithViewPager(pager);

        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new PageChangeListener());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragments) {
                    if (fragment instanceof  ItemsFragment && fragment.getUserVisibleHint()) {
                        ((ItemsFragment) fragment).onFabClick();
                    }
                }
            }
        });
    }

    private class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case MainPagesAdapter.PAGE_INCOMES:
                case MainPagesAdapter.PAGE_EXPENSES:
                    fab.show();
                    break;
                case MainPagesAdapter.PAGE_BALANCE:
                    fab.hide();
                    break;
            }
        }
    }

}
