package com.darkshadowft.smd_assignment3.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.darkshadowft.smd_assignment3.R;

public class ProductActivity extends Activity {
		EditText textArea;
		String noteId;

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.product);
				textArea = (EditText) findViewById(R.id.text_area);

				Intent intent = getIntent();
				String content = intent.getStringExtra("content");
				textArea.setText(content);
				noteId = intent.getStringExtra("id");
		}

		public void buttonClick(View v){

				if(v.getId() == R.id.button_save){
						saveNote();
				}

				if(v.getId() == R.id.button_cancel){
						cancelNote();
				}

		}

		private void saveNote(){
				Intent result = new Intent();
				result.putExtra("content",textArea.getText().toString());
				result.putExtra("id",noteId);
				setResult(RESULT_OK,result);
				finish();
		}

		private void cancelNote(){
				Intent result = new Intent();
				setResult(RESULT_CANCELED,result);
				finish();
		}
}
