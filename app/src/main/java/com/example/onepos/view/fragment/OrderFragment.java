package com.example.onepos.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.ItemTranslation;
import com.example.onepos.model.OrderItem;
import com.example.onepos.util.MLog;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.activity.ModifyActivity;
import com.example.onepos.view.activity.OrderActivity;
import com.example.onepos.view.adapter.CategoryAdapter;
import com.example.onepos.view.adapter.ReceiptAdapter;
import com.example.onepos.viewmodel.OrderViewModel;

import java.util.Locale;

public class OrderFragment extends Fragment implements View.OnClickListener, OrderListener{

    public static final String TAG = "order_fragment";
    public static final String CUSTOMER_ORDER_ID = "customer_order_id";
    public static final String TAG_CHANGE_LANG = "tag_change_lan";

    public static final int REQUEST_CATEGORY = 0;
    public static final int REQUEST_TYPE_SUBMENUITEM = 1;
    public static final int REQUEST_TYPE_ADDTOORDER = 2;
    public static final int REQUEST_TYPE_RETURN = 3;
    public static final int REQUEST_RECEIPT = 4;
    public static final int REQUEST_MODIFIER = 5;
    public static final int REQUEST_PLUS = 6;
    public static final int REQUEST_MINUS = 7;
    public static final int REQUEST_DISCOUNT_ORDER = 8;
    public static final int REQUEST_DISCOUNT_ITEM = 9;

    private OrderViewModel viewModel;
    private RecyclerView rvReceipt, rvCategory;
    private TextView tvTotalNum, tvSubtotalNum, tvTaxNum, tvSubtotalTitle;
    private CategoryAdapter categoryAdapter;
    private ReceiptAdapter receiptAdapter;
    private String FORMAT_CURRENCY;
    private String FORMAT_DISCOUNT;
    private View rootView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private Button btnOrderType, btnPrint, btnCancel, btnDeliveryCharge, btnLang;
    private String[] ORDER_TYPES;

    public static OrderFragment newInstance(long id) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CUSTOMER_ORDER_ID, id);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        FORMAT_CURRENCY = getString(R.string.format_currency);
        FORMAT_DISCOUNT = getString(R.string.format_discount);
        ORDER_TYPES = getResources().getStringArray(R.array.order_types);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        boolean changeLang = false;
        if (savedInstanceState!=null)
            changeLang = savedInstanceState.getBoolean(TAG_CHANGE_LANG, false);
        long id = 0;
        if (getArguments()!=null)
            id = getArguments().getLong(CUSTOMER_ORDER_ID, 0);
        viewModel.initData(id, changeLang);
        initLayout(rootView);
        viewModel.getLiveListCategory().observe(this, list->{
            categoryAdapter.setListCategory(list);
        });
        if (changeLang||id>0)
            viewModel.getLiveFlag().observe(this, flag->{
                if (flag == 6) {
                    updateFields();
                }
                viewModel.getLiveFlag().removeObservers(this);
            });
        return rootView;
    }


    private void updateFields() {
        if (viewModel.getMode()== OrderActivity.MODE_CREATE_ORDER)
            return;
        btnOrderType.setText(ORDER_TYPES[viewModel.getOrderType()]);
        setMoneyFields();
        receiptAdapter.notifyDataSetChanged();
    }
    private void initLayout(View rootView) {
        rvCategory = rootView.findViewById(R.id.rv_category);
        initMenuItemFragment();
        rvReceipt = rootView.findViewById(R.id.rv_receipt);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCategory.setLayoutManager(gridLayoutManager);
        rvReceipt.setLayoutManager(linearLayoutManager);
        final int colorSelected = getContext().getColor(R.color.colorSelected);
        final int colorUnselected = getContext().getColor(R.color.colorUnselected);
        categoryAdapter = new CategoryAdapter(colorSelected, colorUnselected, viewModel.getIndexSelectedCategory(), this);
        receiptAdapter = new ReceiptAdapter(getActivity(), viewModel.getReceipt(), this);
        rvCategory.setAdapter(categoryAdapter);
        rvReceipt.setAdapter(receiptAdapter);
        ((DefaultItemAnimator)(rvReceipt.getItemAnimator())).setSupportsChangeAnimations(false);
        ((DefaultItemAnimator)(rvCategory.getItemAnimator())).setSupportsChangeAnimations(false);
        View viewSubtotal = rootView.findViewById(R.id.cell_subtotal);
        View viewTax = rootView.findViewById(R.id.cell_tax);
        View viewTotal = rootView.findViewById(R.id.cell_total);
        ((TextView)(viewTax.findViewById(R.id.tv_name))).setText(R.string.label_tax);
        ((TextView)(viewTotal.findViewById(R.id.tv_name))).setText(R.string.label_total);
        tvSubtotalNum = viewSubtotal.findViewById(R.id.tv_num);
        tvTaxNum = viewTax.findViewById(R.id.tv_num);
        tvTotalNum = viewTotal.findViewById(R.id.tv_num);
        tvSubtotalTitle = viewSubtotal.findViewById(R.id.tv_name);
        setMoneyFields();

        btnDeliveryCharge = rootView.findViewById(R.id.btn_delivery_fee);
        btnDeliveryCharge.setOnClickListener(this);
        btnOrderType = rootView.findViewById(R.id.btn_order_type);
        btnOrderType.setOnClickListener(this);
        btnOrderType.setText(ORDER_TYPES[viewModel.getOrderType()]);
        setDeliverChargeVisibility();
        btnPrint = rootView.findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);
        btnLang = rootView.findViewById(R.id.btn_lang);
        btnLang.setOnClickListener(this);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnCancel.setLongClickable(true);
        btnCancel.setOnLongClickListener(view -> {
            //clearFragments();
            getActivity().finish();
            return true;
        });

    }

    private void setDeliverChargeVisibility() {
        if (viewModel.getOrderType() == CustomerOrder.ORDER_TYPE_DELIVERY)
            btnDeliveryCharge.setVisibility(View.VISIBLE);
        else
            btnDeliveryCharge.setVisibility(View.GONE);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            btnOrderType.setText(ORDER_TYPES[viewModel.getOrderType()]);
            setDeliverChargeVisibility();
            viewModel.setIndexSelectedReceipt(-1);
            receiptAdapter.notifyDataSetChanged();
        }
    }

    private void setMoneyFields() {
        final CustomerOrder customerOrder = viewModel.getReceipt().getCustomerOrder();
        tvSubtotalNum.setText(String.format(FORMAT_CURRENCY, customerOrder.getSubtotal()));
        tvTaxNum.setText(String.format(FORMAT_CURRENCY, customerOrder.getTax()));
        tvTotalNum.setText(String.format(FORMAT_CURRENCY, customerOrder.getTotal()));
        String titileSubtotal = getString(R.string.label_subtotal);
        tvSubtotalTitle.setText(titileSubtotal);
        if (customerOrder.getDiscount()!=0)
            tvSubtotalTitle.append(String.format(FORMAT_DISCOUNT, (int)(customerOrder.getDiscount() * 100)));
    }

    private void initMenuItemFragment() {
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentByTag(MenuItemFragment.TAG);
        if (fragment==null)
            fm.beginTransaction()
                    .add(R.id.fragment_container, MenuItemFragment.newInstance(), MenuItemFragment.TAG)
                    .commit();
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_lang:
                switchLanguage();
                break;
            case R.id.btn_order_type:
                CustomerInfoFragment customerInfoFragment = CustomerInfoFragment.newInstance();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, customerInfoFragment, CustomerInfoFragment.TAG)
                        .hide(this)
                        .commit();
                break;
            case R.id.btn_print:
                if (viewModel.getReceipt().getNumOfItems() != 0) {
                    viewModel.saveOrder();
                    viewModel.printTicket();
                    if (viewModel.getMode()==OrderActivity.MODE_MODIFY_ORDER) {
                        Intent intent = new Intent();
                        final CustomerOrder customerOrder = viewModel.getReceipt().getCustomerOrder();
                        intent.putExtra(ModifyActivity.EXTRA_ID, customerOrder.getId());
                        intent.putExtra(ModifyActivity.EXTRA_ORDER_TYPE, customerOrder.getOrderType());
                        intent.putExtra(ModifyActivity.EXTRA_TOTAL, customerOrder.getTotal());
                        getActivity().setResult(ModifyActivity.RESULT_UPDATE_ORDER, intent);
                    }
                    getActivity().finish();
                }
                else
                    Toast.makeText(getActivity(), R.string.msg_empty_order, Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalArgumentException("The button id doesn't exist.");
        }
    }

    @Override
    public void onOrder(int index, int request) {
        switch (request) {
            case REQUEST_CATEGORY: {
                if (index == viewModel.getIndexSelectedCategory())
                    return;
                receiptAdapter.resetIndexSelected();
                viewModel.setIndexSelectedReceipt(-1);
                FragmentManager fm = getChildFragmentManager();
                Fragment fragment = fm.findFragmentByTag(MenuItemFragment.TAG);
                if (fragment != null && fragment.isHidden()) {
                    fm.beginTransaction()
                            .show(fragment)
                            .hide(fm.findFragmentByTag(ModifierFragment.TAG))
                            .commit();
                }
                viewModel.setMainLevel(true);
                viewModel.setIndexSelectxedCategory(index);
                break;
            }
            case REQUEST_TYPE_SUBMENUITEM:
                long id = viewModel.getLiveSublistMenuItem().getValue().get(index).getId();
                viewModel.setMainLevel(false);
                viewModel.getMenuItemsByCategory(id);
                break;

            case REQUEST_TYPE_ADDTOORDER:
                viewModel.setMainLevel(true);
                viewModel.addMenuItemToReceipt(index);
                viewModel.setIndexSelectxedCategory(viewModel.getIndexSelectedCategory());
                receiptAdapter.resetIndexSelected();
                viewModel.setIndexSelectedReceipt(-1);
                receiptAdapter.notifyItemInserted(viewModel.getReceipt().getNumOfItems()-1);
                setMoneyFields();
                break;

            case REQUEST_TYPE_RETURN:
                receiptAdapter.resetIndexSelected();
                viewModel.setIndexSelectedReceipt(-1);
                viewModel.setMainLevel(true);
                viewModel.setIndexSelectxedCategory(viewModel.getIndexSelectedCategory());
                break;

            case REQUEST_RECEIPT: {
                viewModel.setIndexSelectedReceipt(index);

                if (index == -1)
                    return;
                FragmentManager fm = getChildFragmentManager();
                final Fragment modifierFragment = fm.findFragmentByTag(ModifierFragment.TAG);
                if (modifierFragment != null) {
                    if (modifierFragment.isHidden()) {
                        fm.beginTransaction()
                                .show(modifierFragment)
                                .hide(fm.findFragmentByTag(MenuItemFragment.TAG))
                                .commit();
                    }
                    long categoryId = viewModel.getReceipt().getItemCategoryId(index);
                    viewModel.getModifierItemsByCategory(categoryId);
                } else {
                    fm.beginTransaction()
                            .add(R.id.fragment_container, ModifierFragment.newInstance(), ModifierFragment.TAG)
                            .hide(fm.findFragmentByTag(MenuItemFragment.TAG))
                            .commit();
                }
                break;
            }
            case REQUEST_MODIFIER:{
                int indexSelected = viewModel.getIndexSelectedReceipt();
                if (indexSelected == -1)
                    return;
                if (viewModel.getMode()==OrderActivity.MODE_MODIFY_ORDER && viewModel.getReceipt().get(viewModel.getIndexSelectedReceipt()).getMode()!= OrderItem.MODE_ADDED)
                    return;
                viewModel.getReceipt().addModifierItem(viewModel.getIndexSelectedReceipt(), viewModel.getModifierItems().get(index));
                receiptAdapter.notifyDataSetChanged();
                setMoneyFields();
                break;
            }
            case REQUEST_PLUS: {
                int indexSelected = viewModel.getIndexSelectedReceipt();
                if (indexSelected != -1) {
                    if (viewModel.getMode() == OrderActivity.MODE_MODIFY_ORDER && viewModel.getReceipt().get(indexSelected).getMode() != OrderItem.MODE_ADDED)
                        return;
                    viewModel.getReceipt().quantityIncrementByPos(indexSelected);
                    receiptAdapter.notifyItemChanged(indexSelected);
                    setMoneyFields();
                    break;
                }
            }
            case REQUEST_MINUS: {
                int indexSelected = viewModel.getIndexSelectedReceipt();
                if (indexSelected != -1) {
                    if (viewModel.getReceipt().get(indexSelected).getMode() != OrderItem.MODE_ADDED) {
                        viewModel.getReceipt().markAsDeleted(indexSelected);
                        receiptAdapter.notifyDataSetChanged();
                    }
                    else {
                        int numDeleted = viewModel.getReceipt().quantityDecrementByPos(indexSelected);
                        if (numDeleted>0) {
                            receiptAdapter.resetIndexSelected();
                            viewModel.setIndexSelectedReceipt(-1);
                            receiptAdapter.notifyItemRangeRemoved(indexSelected, numDeleted);
                        }
                        else
                            receiptAdapter.notifyItemChanged(indexSelected);
                    }
                    setMoneyFields();
                }
                break;
            }
            case REQUEST_DISCOUNT_ORDER:
            case REQUEST_DISCOUNT_ITEM:{
                int[] percentageValues = getActivity().getResources().getIntArray(R.array.discount_percentage_values);
                if (request == REQUEST_DISCOUNT_ORDER)
                    viewModel.getReceipt().setDiscount(percentageValues[index]/100.0);
                if (request == REQUEST_DISCOUNT_ITEM){
                    int indexSelected = viewModel.getIndexSelectedReceipt();
                    if (viewModel.getReceipt().get(indexSelected).getMode()!=OrderItem.MODE_ADDED)
                        return;
                    viewModel.getReceipt().setDiscount(indexSelected, percentageValues[index] / 100.0);
                    receiptAdapter.notifyItemChanged(indexSelected);
                }
                setMoneyFields();
                break;
            }
        }
    }

    private void switchLanguage() {
        if (viewModel.getLang() == ItemTranslation.LANG_US) {
            viewModel.setLang(ItemTranslation.LANG_CN);
            Configuration config = getResources().getConfiguration();
            config.setLocale(Locale.CHINA);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            getActivity().recreate();
        }
        else
            if (viewModel.getLang() == ItemTranslation.LANG_CN) {
                viewModel.setLang(ItemTranslation.LANG_US);
                Configuration config = getResources().getConfiguration();
                config.setLocale(Locale.US);
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                getActivity().recreate();
            }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(TAG_CHANGE_LANG, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
