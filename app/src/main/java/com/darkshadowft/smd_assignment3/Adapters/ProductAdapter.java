package com.darkshadowft.smd_assignment3.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.darkshadowft.smd_assignment3.Product;
import com.darkshadowft.smd_assignment3.R;

import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
		private ArrayList<Product> products;
		private ArrayList<Product> filteredProducts;
		private ProductItemClickListener listener;
		private ArrayList<Integer> selectedItems;
		private Filter filter;

		private int mode = DEFAULT_MODE;
		public static final int DEFAULT_MODE = 0;
		public static final int SELECTABLE_MODE = 1;

		public class ProductViewHolder extends RecyclerView.ViewHolder {
				public ImageView image;
				public TextView name;
				public RatingBar simpleRatingBar;
				public TextView price;
				public CheckBox selected;

				public ProductViewHolder(View v){
						super(v);
						image = (ImageView) v.findViewById(R.id.product_image);
						name = (TextView) v.findViewById(R.id.product_name);
						simpleRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
						price = (TextView) v.findViewById(R.id.product_price);
						selected = (CheckBox) v.findViewById(R.id.item_check);

						v.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
										int index = (int) v.getTag();
										if (mode == SELECTABLE_MODE){
												selected.setChecked(!selected.isChecked()); // toggle
												selected.callOnClick();
										}
										else {
												listener.onClick(filteredProducts.get(index));
										}
								}
						});

						v.setOnLongClickListener(new View.OnLongClickListener() {
								@Override
								public boolean onLongClick(View v) {
										int index = (int) v.getTag();
										selected.setChecked(true);
										selected.callOnClick();
										listener.onLongClick(filteredProducts.get(index));
										return true;
								}
						});
						selected.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
										boolean checked = ((CheckBox) view).isChecked();
										int index = (int) v.getTag();
										if (checked){
												selectedItems.add(index);
										}
										else{
												selectedItems.remove(index);
										}
								}
						});
				}
		}

		public ProductAdapter(ArrayList<Product> ds, ProductItemClickListener ls){
				this.products = ds;
				this.filteredProducts = ds;
				this.listener = ls;
				selectedItems = new ArrayList<>();
		}

		@Override
		public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
				View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
				return new ProductViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ProductViewHolder holder, int position) {
				Product product = filteredProducts.get(position);
				holder.image.setImageResource(product.getImgResourceId());
				holder.name.setText(product.getName());
				holder.simpleRatingBar.setRating(product.getRating());
				holder.price.setText(product.getPrice());
				holder.itemView.setTag(position);
				if (mode == DEFAULT_MODE){
						holder.selected.setChecked(false);
						holder.selected.setVisibility(View.INVISIBLE);
				}
				else{
						holder.selected.setVisibility(View.VISIBLE);
				}
		}

		@Override
		public int getItemCount() {
				return filteredProducts.size();
		}

		public interface ProductItemClickListener {
				void onClick(Product p);
				void onLongClick(Product p);

		}

		@Override
		public Filter getFilter() {
				if (filter == null){
						filter = new ProductFilter();
				}
				return filter;
		}

		private class ProductFilter extends Filter {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
						FilterResults filterResults = new FilterResults();

						if (constraint != null && constraint.length() > 0){
								ArrayList<Product> filteredList = new ArrayList<Product>();
								for (int i = 0; i < products.size(); i++){
										if (products.get(i).getName().toLowerCase(Locale.ROOT).contains(constraint) ||
														products.get(i).getName().contains(constraint)
										){
												filteredList.add(products.get(i));
										}
								}

								filterResults.count = filteredList.size();
								filterResults.values = filteredList;
						}

						else {
								filterResults.count = products.size();
								filterResults.values = products;
						}

						return filterResults;
				}

				@Override
				protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
						filteredProducts = (ArrayList<Product>) filterResults.values;
						notifyDataSetChanged();
				}
		}

		public void setMode(int m){
				mode = m;
				if (mode == DEFAULT_MODE) {
						selectedItems.clear();
				}
				notifyDataSetChanged();
		}

		public void removeSelectedItems(){
				ArrayList<Product> removableItems = new ArrayList<>();
				for (int i=0; i < selectedItems.size(); i++ ){
						Integer item = selectedItems.get(i);
						removableItems.add( filteredProducts.get(item) );
				}

				for(Product product : removableItems){
						filteredProducts.remove(product);
						products.remove(product);
				}

				selectedItems.clear();
				notifyDataSetChanged();
		}

		public void updateData(ArrayList<Product> ds){
				products = ds;
				filteredProducts = products;
				notifyDataSetChanged();
		}
}
