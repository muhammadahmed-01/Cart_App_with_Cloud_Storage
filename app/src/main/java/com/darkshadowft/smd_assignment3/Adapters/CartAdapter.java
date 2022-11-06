package com.darkshadowft.smd_assignment3.Adapters;

import static java.lang.Integer.parseInt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.darkshadowft.smd_assignment3.Cart;
import com.darkshadowft.smd_assignment3.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
		private ArrayList<Cart> cartItems;
		private CartItemClickListener listener;

		public CartAdapter(ArrayList<Cart> ds, CartItemClickListener listener){
				this.cartItems = ds;
				this.listener = listener;
		}

		public void updateData(ArrayList<Cart> ds){
				cartItems = ds;
				notifyDataSetChanged();
		}

		public class CartViewHolder extends RecyclerView.ViewHolder {
				ImageView image;
				TextView cartProductName;
				TextView cartProductDescription;
				TextView cartProductPrice;
				Button cartButtonIncrement;
				EditText cartProductQuantity;
				Button cartButtonDecrement;

				public CartViewHolder(View view){
						super(view);
						image = (ImageView) view.findViewById(R.id.product_image);
						cartProductName = (TextView) view.findViewById(R.id.product_name);
						cartProductDescription = (TextView) view.findViewById(R.id.product_description);
						cartProductPrice = (TextView) view.findViewById(R.id.product_price);
						cartButtonIncrement = (Button) view.findViewById(R.id.inc_btn);
						cartProductQuantity = (EditText) view.findViewById(R.id.product_quantity);
						cartButtonDecrement = (Button) view.findViewById(R.id.dec_btn);

						cartButtonIncrement.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
										listener.onClick(view, "inc", (int) v.getTag());
								}
						});

						cartButtonDecrement.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
										listener.onClick(view, "dec", (int) v.getTag());
								}
						});
				}
		}

		@NonNull
		@Override
		public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart_item, parent, false);
				return new CartViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
				Cart cartItem = cartItems.get(position);
				holder.image.setImageResource(cartItem.getImgResourceId());
				holder.cartProductName.setText(cartItem.getName());
				holder.cartProductDescription.setText(cartItem.getDescription());
				holder.cartProductPrice.setText(cartItem.getPrice());
				holder.cartButtonIncrement.setTag(position);
				holder.cartButtonDecrement.setTag(position);
				holder.cartProductQuantity.setText(Integer.toString(cartItem.getQuantity()));
		}

		@Override
		public int getItemCount() {
				return cartItems.size();
		}

		public interface CartItemClickListener {
				void onClick(View view, String mode, int index);
		}
}
