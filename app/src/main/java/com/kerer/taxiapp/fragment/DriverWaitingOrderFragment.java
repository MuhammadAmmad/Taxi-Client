package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kerer.taxiapp.R;
import com.kerer.taxiapp.model.Order;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ivan on 09.01.17.
 */

public class DriverWaitingOrderFragment extends Fragment {

    private static final String TAG = "DriverWaitingOrderFragment";

    public static DriverWaitingOrderFragment newInstance() {
        return new DriverWaitingOrderFragment();
    }

    @BindView(R.id.fragment_driver_vaiting_order_recycler_view)
    RecyclerView mRecyclerView;

    private OrdersAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_waiting_order, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("asfasf", "asfasfasf", "asfasfasf", "Asfasfasfasf", 22f, 12f, 2, "asfasf"));
        orders.add(new Order("asfasf", "asfasfasf", "asfasfasf", "Asfasfasfasf", 22f, 12f, 2, "asfasf"));
        orders.add(new Order("asfasf", "asfasfasf", "asfasfasf", "Asfasfasfasf", 22f, 12f, 2, "asfasf"));
        orders.add(new Order("asfasf", "asfasfasf", "asfasfasf", "Asfasfasfasf", 22f, 12f, 2, "asfasf"));
        orders.add(new Order("asfasf", "asfasfasf", "asfasfasf", "Asfasfasfasf", 22f, 12f, 2, "asfasf"));
        mAdapter = new OrdersAdapter(orders);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }

    public class OrdersHolder extends RecyclerView.ViewHolder {

        private Order mOrder;

        @BindView(R.id.order_list_item_destination)
        TextView mDestinationTv;
        @BindView(R.id.order_list_item_origin)
        TextView mOriginTv;
        @BindView(R.id.order_list_item_payment)
        TextView mPaymentTv;
        @BindView(R.id.order_list_item_length)
        TextView mLengthTv;
        @BindView(R.id.order_list_item_get_order)
        ImageView mGetOrderImg;
        @BindView(R.id.order_list_item_hide_order)
        ImageView mHideOrderImg;
        @BindView(R.id.order_list_item_view_order)
        ImageView mViewOrderImg;

        public OrdersHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindOrder(Order order) {
            mOrder = order;

            mDestinationTv.setText(mOrder.getmDestination());
            mOriginTv.setText(mOrder.getmOrigin());
            mPaymentTv.setText(String.valueOf(mOrder.getmPayment()));
            mLengthTv.setText(String.valueOf(mOrder.getmDistance()));
        }

        @OnClick(R.id.order_list_item_get_order)
        public void onGetOrderClick() {

        }

        @OnClick(R.id.order_list_item_hide_order)
        public void onHideOrderClick() {

        }

        @OnClick(R.id.order_list_item_view_order)
        public void onViewOrderClick() {

        }
    }

    public class OrdersAdapter extends RecyclerView.Adapter<OrdersHolder>{

        private List<Order> mOrders;

        public OrdersAdapter(List<Order> mOrders) {
            this.mOrders = mOrders;
        }

        @Override
        public OrdersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.order_list_item, parent, false);

            return new OrdersHolder(v);
        }

        @Override
        public void onBindViewHolder(OrdersHolder holder, int position) {
            holder.bindOrder(mOrders.get(position));
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }
    }

}
