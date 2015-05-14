package com.dinhduc_company.stockmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by NguyenDinh on 4/6/2015.
 */
public class BrowerImage extends Activity {
    GridView gridView;
    Integer[] listImage = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g,
            R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l};
    BrowerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brower_image);
        gridView = (GridView) findViewById(R.id.listAvatar);
        adapter = new BrowerAdapter(this, R.layout.brower_item, listImage);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent();
                intent.putExtra("imageRes", listImage[i]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
