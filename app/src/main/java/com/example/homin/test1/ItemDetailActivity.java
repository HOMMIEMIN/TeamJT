package com.example.homin.test1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ItemDetailActivity extends Activity {

    private List<ItemMemo> memoList;
    private List<ItemPerson> personList;
    private RecyclerView recyclerView;
    private TextView tv;

    class CustomDetailAdapter extends RecyclerView.Adapter<CustomDetailAdapter.CustomHolder>{


        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ItemDetailActivity.this);
            View itemView = inflater.inflate(R.layout.layout_detaiitem,parent,false);
            CustomHolder holder = new CustomHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, int position) {
            if(type == 0){
                Glide.with(ItemDetailActivity.this).load(memoList.get(position).getImageUrl())
                        .bitmapTransform(new CropCircleTransformation(ItemDetailActivity.this)).centerCrop()
                        .into(holder.iv);
                String a = String.format("이름: %s\n제목 : %s\n내용 : %s\n시간 : %s\n",memoList.get(position).getUserName(),memoList.get(position).getTitle(),
                        memoList.get(position).getContent(),memoList.get(position).getTime());
                holder.tv.setText(a);
            }else{
                Glide.with(ItemDetailActivity.this).load(personList.get(position).getImage())
                        .bitmapTransform(new CropCircleTransformation(ItemDetailActivity.this)).centerCrop()
                        .into(holder.iv);
                String person = String.format("이름 : %s\n, 아이디 : : %s\n",personList.get(position).getUserId(), personList.get(position).getUserName());
                holder.tv.setText(personList.get(position).getUserName());
            }



        }

        @Override
        public int getItemCount() {
            return size;
        }

        class CustomHolder extends RecyclerView.ViewHolder{
                TextView tv;
                ImageView iv;

            public CustomHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.textView_detail);
                iv = itemView.findViewById(R.id.imageview_detail);
            }
        }
    }

    private int size;
    private int type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_detail);
        recyclerView = findViewById(R.id.recyclerView);
        tv = findViewById(R.id.textView_detail);



        Intent intent = getIntent();
        String key = intent.getStringExtra(MapsActivity.MARKER_LIST);
        if(key.equals("memo")){
            memoList = DaoImple.getInstance().getItemMemoList();
            tv.setText("메모 목록");
        }else{
            personList = DaoImple.getInstance().getItemPersonList();
            tv.setText("유저 목록");
        }


        if(memoList != null){
            size = memoList.size();
            type = 0;
        }else{
            size = personList.size();
            type = 1;
        }

        CustomDetailAdapter adapter = new CustomDetailAdapter();
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
