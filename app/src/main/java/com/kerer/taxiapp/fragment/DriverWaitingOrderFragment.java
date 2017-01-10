package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.interfaces.FirebaseDatabaseReferences;
import com.kerer.taxiapp.interfaces.OrderStatuses;
import com.kerer.taxiapp.model.Order;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */

public class DriverWaitingOrderFragment extends Fragment implements FirebaseDatabaseReferences, OrderStatuses {

    private static final String TAG = "DriverWaitingOrderFragment";

    public static DriverWaitingOrderFragment newInstance() {
        return new DriverWaitingOrderFragment();
    }

    @BindView(R.id.fragment_driver_vaiting_order_recycler_view)
    RecyclerView mRecyclerView;

    private OrdersAdapter mAdapter;

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase
                .child(ORDERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Order> orders = new ArrayList<Order>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            orders.add(dataSnapshot1.getValue(Order.class));
                        }
                        updateUi(orders);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_waiting_order, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            //Removing from orders
            mDatabase
                    .child(ORDERS)
                    .child(mOrder.getmKey())
                    .removeValue();
            //set active order to driver
            mOrder.setmStatus(STATUS_GETED);
            mDatabase
                    .child(DRIVERS)
                    .child(ACTIVE_ORDERS)
                    .child(mOrder.getmKey())
                    .setValue(mOrder)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //TODO open maps activity
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
            //set driver to client order
            mDatabase
                    .child(CLIENTS)
                    .child(mOrder.getmClientUid())
                    .child(ACTIVE_ORDERS)
                    .child(mOrder.getmKey())
                    .child(DRIVER_UID)
                    .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            //set order status to 1
            mDatabase
                    .child(CLIENTS)
                    .child(mOrder.getmClientUid())
                    .child(ACTIVE_ORDERS)
                    .child(mOrder.getmKey())
                    .child(STATUS)
                    .setValue(STATUS_GETED);
        }

        @OnClick(R.id.order_list_item_hide_order)
        public void onHideOrderClick() {

        }

        @OnClick(R.id.order_list_item_view_order)
        public void onViewOrderClick() {

        }
    }

    public class OrdersAdapter extends RecyclerView.Adapter<OrdersHolder> {

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

    private void updateUi(List<Order> orders) {
        if (orders.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new OrdersAdapter(orders);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
