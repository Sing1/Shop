package sing.shop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import sing.shop.MainActivity;
import sing.shop.R;
import sing.shop.adpter.CartAdapter;
import sing.shop.adpter.decoration.DividerItemDecoration;
import sing.shop.bean.ShoppingCart;
import sing.shop.utils.CartProvider;
import sing.shop.widget.CnToolbar;

public class CartFragment extends Fragment implements View.OnClickListener {

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;
    private CnToolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ViewUtils.inject(this, view);

        cartProvider = new CartProvider(getContext());

        showData();

        return view;
    }


    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    public void refData() {
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            mToolbar = (CnToolbar) activity.findViewById(R.id.toolbar);
            changeToolbar();
        }
    }

    public void changeToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();

        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (ACTION_EDIT == action) {
            showDelControl();
        } else if (ACTION_CAMPLATE == action) {
            hideDelControl();
        }
    }

    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    private void hideDelControl() {
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);

        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    @OnClick(R.id.btn_del)
    public void delCart(View view){
        mAdapter.delCart();
    }
}
