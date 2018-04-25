package com.example.homin.test1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.List;

public class ItemDetailActivity extends Activity {

    private List<ItemMemo> memoList;
    private List<ItemPerson> personList;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_detail);
        recyclerView = findViewById(R.id.recyclerView);


        Intent intent = getIntent();
        String key = intent.getStringExtra(MapsActivity.MARKER_LIST);
        if(key.equals("memo")){
            memoList = DaoImple.getInstance().getItemMemoList();
        }else{
            personList = DaoImple.getInstance().getItemPersonList();
        }





    }
}
