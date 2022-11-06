package com.darkshadowft.smd_assignment3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.darkshadowft.smd_assignment3.Product;
import com.darkshadowft.smd_assignment3.R;

import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
		String id;
		ImageView image;
		int imgResourceId;
		TextView name;
		RatingBar rating;
		TextView price;
		TextView descriptionHead;
		TextView description;
		Button btn_buy;
		private ActivityResultLauncher<Intent> cartLauncher;
		private HashMap<String, Product> cartData;

		@Override
		protected void onCreate(@Nullable Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.product_detail);

				name = (TextView) findViewById(R.id.product_detail_name);
				image = (ImageView) findViewById(R.id.product_detail_image);
				rating = (RatingBar) findViewById(R.id.product_detail_rating);
				price = (TextView) findViewById(R.id.product_detail_price);
				descriptionHead = (TextView) findViewById(R.id.product_detail_description_head);
				description = (TextView) findViewById(R.id.product_detail_description);
				btn_buy = (Button) findViewById(R.id.btn_product_buy);

				Intent intent = getIntent();
				id = intent.getStringExtra("id");
				imgResourceId = intent.getIntExtra("resId", 0);
				image.setImageResource(imgResourceId);
				name.setText(intent.getStringExtra("name"));
				rating.setRating(intent.getFloatExtra("rating", 0));
				price.setText(intent.getStringExtra("price"));
				descriptionHead.setText("Description");
				description.setText(intent.getStringExtra("description"));

				btn_buy.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
								addItemToCart();
						}
				});

				cartLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
						@Override
						public void onActivityResult(ActivityResult result) {
								if (result.getResultCode() == RESULT_OK){
										Intent intent = new Intent();
										setResult(RESULT_OK, intent);
										finish();
								}
						}
				});
		}

		public void addItemToCart() {
				Intent intent = new Intent(this, CartActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("resId", imgResourceId);
				intent.putExtra("name", name.getText().toString());
				intent.putExtra("rating", rating.getRating());
				intent.putExtra("price", price.getText().toString());
				intent.putExtra("description", description.getText().toString());
				cartLauncher.launch(intent);
		}
}
