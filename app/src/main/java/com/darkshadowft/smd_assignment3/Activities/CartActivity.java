package com.darkshadowft.smd_assignment3.Activities;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darkshadowft.smd_assignment3.Adapters.CartAdapter;
import com.darkshadowft.smd_assignment3.Cart;
import com.darkshadowft.smd_assignment3.CartItemViewModel;
import com.darkshadowft.smd_assignment3.IProductDAO;
import com.darkshadowft.smd_assignment3.FirebaseDAO;
import com.darkshadowft.smd_assignment3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemClickListener {
		CartAdapter adapter;
		private ArrayList<Cart> dataSet = new ArrayList<Cart>();

		private TextView subtotal;
		private TextView shipping;
		private TextView taxes;
		private TextView total;

		private IProductDAO cartDAO;

		@Override
		protected void onCreate(@Nullable Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.cart);

				// setting recyclerview
				RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cart_list);
				recyclerView.setHasFixedSize(true);

				// use a linear layout manager
				RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
				recyclerView.setLayoutManager(layoutManager);

				cartDAO = new FirebaseDAO(new FirebaseDAO.DataObserver() {
						@Override
						public void update() {
								refresh();
						}
				}, "cart");;
				CartItemViewModel cartItemViewModel = new ViewModelProvider(CartActivity.this).get(CartItemViewModel.class);
				cartItemViewModel.setDao(cartDAO);
				dataSet = cartItemViewModel.getCartItems(savedInstanceState, "cart");

				// specify an adapter (see also next example)
				adapter = new CartAdapter(dataSet, this);
				recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
				recyclerView.setAdapter(adapter);

				Intent intent = getIntent();
				Cart cartItem = new Cart(intent.getStringExtra("description"),
								intent.getStringExtra("name"), intent.getStringExtra("price"),
								intent.getFloatExtra("rating", 0));
				cartItem.setImgResourceId(intent.getIntExtra("resId", 0));
				cartItem.setId(intent.getStringExtra("id"));

				boolean cartItemExists = false;
				for (int i = 0; i < dataSet.size(); i++){
						if (Objects.equals(dataSet.get(i).getId(), cartItem.getId())){
								cartItemExists = true;
								break;
						}
				}

				if (!cartItemExists){
						dataSet.add(cartItem);
						adapter.notifyDataSetChanged();
				}

				subtotal = (TextView) findViewById(R.id.cart_subtotal_value);
				shipping = (TextView) findViewById(R.id.cart_shipping_value);
				taxes = (TextView) findViewById(R.id.cart_taxes_value);
				total = (TextView) findViewById(R.id.card_total_value);

				Button btnSave = (Button) findViewById(R.id.btn_cart_order);
				btnSave.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								addToCart();
						}
				});

				Button btnCancel = (Button) findViewById(R.id.btn_cart_cancel);
				btnCancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								cancelCart();
						}
				});

				Button btnContactSupport = (Button) findViewById(R.id.btn_contact_customer_support);
				btnContactSupport.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								Intent intent = new Intent(Intent.ACTION_DIAL);
								intent.setData(Uri.parse("tel:0123456789"));
								startActivity(intent);
						}
				});
				calculateTotal();
		}

		public void calculateTotal(){
				if (dataSet.size() == 0){
						subtotal.setText("$0");
						shipping.setText("FREE");
						taxes.setText("$0");
						total.setText("$0");
						return;
				}

				int subtotal_sum = 0;
				String price;
				int quantity;
				for (int i = 0; i < dataSet.size(); i++){
						price = dataSet.get(i).getPrice();
						quantity = dataSet.get(i).getQuantity();
						subtotal_sum += Integer.parseInt(price.substring(1, price.length())) * quantity;
				}
				subtotal.setText("$" + Integer.toString(subtotal_sum));
				shipping.setText("FREE");

				int taxes_sum = 15;
				taxes.setText("$" + Integer.toString(taxes_sum));

				total.setText("$" + Integer.toString(subtotal_sum + taxes_sum));
		}

		public void addToCart(){
				for (int i = 0; i < dataSet.size(); i++){
						dataSet.get(i).setDao(cartDAO);
						dataSet.get(i).save();
				}
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
		}

		public void cancelCart(){
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
		}

		@Override
		public void onClick(View view, String mode, int index) {
				if (mode.equals("inc")){
						TextView cartProductQuantity = (EditText) view.findViewById(R.id.product_quantity);
						int quantity = parseInt(cartProductQuantity.getText().toString());
						cartProductQuantity.setText(Integer.toString(quantity + 1));
						Cart cartItem = dataSet.get(index);
						cartItem.setQuantity(cartItem.getQuantity() + 1);
						dataSet.set(index, cartItem);
						calculateTotal();
				}
				else if (mode.equals("dec")){
						TextView cartProductQuantity = (EditText) view.findViewById(R.id.product_quantity);
						int quantity = parseInt(cartProductQuantity.getText().toString());
						if (quantity > 1){
								cartProductQuantity.setText(Integer.toString(quantity - 1));
								Cart cartItem = dataSet.get(index);
								cartItem.setQuantity(cartItem.getQuantity() - 1);
								dataSet.set(index, cartItem);
								calculateTotal();
						}
						else {
								dataSet.get(index).setDao(cartDAO);
								dataSet.get(index).delete();
								dataSet.remove(index);
								adapter.notifyDataSetChanged();
								calculateTotal();
						}
				}
		}

		public void onSaveInstanceState(Bundle state) {
				super.onSaveInstanceState(state);
				state.putSerializable("cart", dataSet);
		}

		public void refresh(){
				CartItemViewModel vm = new ViewModelProvider(this).get(CartItemViewModel.class);
				if (dataSet.size() == 0)
					dataSet = vm.update();
				else {
						HashMap<String, Cart> tempDataset = new HashMap<>();
						for (int i = 0; i < dataSet.size(); i++){
								tempDataset.put(dataSet.get(i).getId(), dataSet.get(i));
						}
						dataSet = vm.update();
						for (int i = 0; i < dataSet.size(); i++){
								tempDataset.put(dataSet.get(i).getId(), dataSet.get(i));
						}
						dataSet.clear();
						for (Map.Entry<String, Cart> entry: tempDataset.entrySet()){
								dataSet.add(entry.getValue());
						}
				}
				if (dataSet != null){
						adapter.updateData(dataSet);
				}
		}
}
