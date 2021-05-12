package com.example.onepos.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.adapter.ModifierAdapter;
import com.example.onepos.view.dialog.DiscountDialog;
import com.example.onepos.viewmodel.OrderViewModel;
import com.google.android.material.tabs.TabLayout;

public class ModifierFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "modifier_fragment";
    private static final String CATEGORY_ID = "category_id";
    private OrderViewModel orderViewModel;
    private ModifierAdapter modifierAdapter;
    private RecyclerView rvModifier;
    private GridLayoutManager gridLayoutManager;
    private View rootView;
    private TabLayout tabLayout;
    private Button btnPlus, btnMinus, btnDiscount;
    private OrderListener listener;

    public static ModifierFragment newInstance() {
        ModifierFragment modifierFragment = new ModifierFragment();
        //Bundle bundle = new Bundle();
        //bundle.putLong(CATEGORY_ID, categoryId);
        //modifierFragment.setArguments(bundle);
        return modifierFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_modifier, container, false);
        listener = (OrderListener) getParentFragment();
        initLayout(rootView);
        int indexSelected = orderViewModel.getIndexSelectedReceipt();
        if (indexSelected!=-1) {
            long categoryId = orderViewModel.getReceipt().getItemCategoryId(indexSelected);
            orderViewModel.getModifierItemsByCategory(categoryId);
        }
        orderViewModel.getLiveListModifier().observe(this, list->{
            modifierAdapter.setList(orderViewModel.getModifierItemsByType(tabLayout.getSelectedTabPosition()+1));
        });
        return rootView;
    }

    private void initLayout(View rootView) {
        btnPlus = rootView.findViewById(R.id.btn_plus);
        btnPlus.setOnClickListener(this);
        btnMinus = rootView.findViewById(R.id.btn_minus);
        btnMinus.setOnClickListener(this);
        btnDiscount = rootView.findViewById(R.id.btn_discount);
        btnDiscount.setOnClickListener(this);
        rvModifier = rootView.findViewById(R.id.rv_modifier);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvModifier.setLayoutManager(gridLayoutManager);
        modifierAdapter = new ModifierAdapter(getParentFragment());
        rvModifier.setAdapter(modifierAdapter);         //TODO move some lines to onAttach()
        ((DefaultItemAnimator)(rvModifier.getItemAnimator())).setSupportsChangeAnimations(false);
        tabLayout = rootView.findViewById(R.id.tabs_modifier);
        loadTabs(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int type = tab.getPosition() +1;
                modifierAdapter.setList(orderViewModel.getModifierItemsByType(type));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void loadTabs(TabLayout tabLayout) {
        final String[] MODIFIER_TYPES = getResources().getStringArray(R.array.modifier_types);
        for (int i = 0; i < MODIFIER_TYPES.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(MODIFIER_TYPES[i]);
            tabLayout.addTab(tab);
        }
    }
    /*void setCategoryId(long categoryId) {
        if (this.categoryId!=categoryId) {
            this.categoryId = categoryId;
            orderViewModel.getModifierItems(modifierAdapter.getCurrentModifierType(), categoryId);
        }
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_discount:
                DiscountDialog dialog = new DiscountDialog(getContext()) {
                    @Override
                    public void callback(int indexType, int indexPercentage) {
                        listener.onOrder(indexPercentage, indexType==0?OrderFragment.REQUEST_DISCOUNT_ORDER:OrderFragment.REQUEST_DISCOUNT_ITEM);
                    }
                };
                dialog.show();
                break;
            case R.id.btn_plus:
                listener.onOrder(-1, OrderFragment.REQUEST_PLUS);
                break;

            case R.id.btn_minus: ;
                listener.onOrder(-1, OrderFragment.REQUEST_MINUS);
                break;
        }
    }
}
