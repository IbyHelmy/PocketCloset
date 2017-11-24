package com.example.pocketdevs.pocketcloset.ui;

/**
 * Created by Saif on 2017-10-30.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.adapter.ImageAdapter;
import com.example.pocketdevs.pocketcloset.adapter.SectionedGridRecyclerViewAdapter;
import com.example.pocketdevs.pocketcloset.common.ListClickInterface;
import com.example.pocketdevs.pocketcloset.entity.Clothes;

import java.util.ArrayList;
import java.util.List;


public class ViewClosetActivity extends AppCompatActivity {

    public static final int UPDATE_IMAGE_REQ_CODE = 332;
    private List<Clothes> head, top, bottom, foot, outer, skirts, dresses, accessories;

    private List<SectionedGridRecyclerViewAdapter.Section> sections;

    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_closet);

        emptyView = findViewById(R.id.emptyView);

        recyclerView = findViewById(android.R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        loadItems();
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            loadItems();
        }
    }

    /**
     * Load items from database
     */
    public void loadItems(){
        final List<Clothes> oTempList = new ArrayList<>();
        sections = new ArrayList<>();

        head = Clothes.find(Clothes.class, "type = ?", "0");
        top = Clothes.find(Clothes.class, "type = ?", "1");
        bottom = Clothes.find(Clothes.class, "type = ?", "2");
        foot = Clothes.find(Clothes.class, "type = ?", "3");
        outer = Clothes.find(Clothes.class, "type = ?", "4");
        skirts = Clothes.find(Clothes.class, "type = ?", "5");
        dresses = Clothes.find(Clothes.class, "type = ?", "6");
        accessories = Clothes.find(Clothes.class, "type = ?", "7");

        oTempList.addAll(head);
        oTempList.addAll(top);
        oTempList.addAll(bottom);
        oTempList.addAll(foot);
        oTempList.addAll(outer);
        oTempList.addAll(skirts);
        oTempList.addAll(dresses);
        oTempList.addAll(accessories);

        if(!head.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Head wear"));
        if(!top.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size(), "Top wear"));
        if(!bottom.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size() + top.size(), "Bottom wear"));
        if(!foot.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size() + top.size() + bottom.size(), "Foot wear"));
        if(!outer.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size() + top.size() + bottom.size() + foot.size(), "Outerwear"));
        if(!skirts.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size() + top.size() + bottom.size() + foot.size() + outer.size(), "Skirts"));
        if(!dresses.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size() + top.size() + bottom.size() + foot.size() + outer.size() + skirts.size(), "Dresses"));
        if(!accessories.isEmpty())
            sections.add(new SectionedGridRecyclerViewAdapter.Section(head.size() + top.size() + bottom.size() + foot.size() + outer.size() + skirts.size() + dresses.size(), "Accessories"));


        final ImageAdapter imageAdapter = new ImageAdapter(this, oTempList, new ListClickInterface() {
            @Override
            public void onItemClick(View view, int position) {
                Intent n = new Intent(ViewClosetActivity.this, ViewImageActivity.class);

                List<Clothes> temp = new ArrayList<>();

                if(!head.isEmpty()) {
                    temp.add(null);
                    temp.addAll(head);
                }

                if(!top.isEmpty()){
                    temp.add(null);
                    temp.addAll(top);
                }
                if(!bottom.isEmpty()){
                    temp.add(null);
                    temp.addAll(bottom);
                }
                if(!foot.isEmpty()){
                    temp.add(null);
                    temp.addAll(foot);
                }
                if(!outer.isEmpty()){
                    temp.add(null);
                    temp.addAll(outer);
                }
                if(!skirts.isEmpty()){
                    temp.add(null);
                    temp.addAll(skirts);
                }
                if(!dresses.isEmpty()){
                    temp.add(null);
                    temp.addAll(dresses);
                }
                if(!accessories.isEmpty()){
                    temp.add(null);
                    temp.addAll(accessories);
                }

                n.putExtra("clothId", temp.get(position).getId());
                startActivityForResult(n, UPDATE_IMAGE_REQ_CODE);
            }
        });

        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new SectionedGridRecyclerViewAdapter(this, R.layout.section ,R.id.section_text, recyclerView, imageAdapter, ContextCompat.getColor(this, R.color.md_grey_600));
        mSectionedAdapter.setSections(sections.toArray(dummy));

        recyclerView.setAdapter(mSectionedAdapter);

        //show empty message if list is empty
        if(oTempList==null || oTempList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}
