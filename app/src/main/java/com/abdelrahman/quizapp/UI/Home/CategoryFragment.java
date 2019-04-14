package com.abdelrahman.quizapp.UI.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdelrahman.quizapp.Interface.ItemClickListener;
import com.abdelrahman.quizapp.Model.Category;
import com.abdelrahman.quizapp.R;
import com.abdelrahman.quizapp.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryFragment extends Fragment {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    private View mFragment;

    RecyclerView categoryList;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    public CategoryFragment() {
        // Required empty public constructor
    }


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_category, container, false);

        categoryList = mFragment.findViewById(R.id.home_category_list);
        categoryList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(container.getContext());
        categoryList.setLayoutManager(mLayoutManager);

        loadCategories();

        return mFragment;
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.category_item,
                CategoryViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.mTextViewCategory.setText(model.getName());
                Picasso.with(getActivity())
                        .load(model.getImage())
                        .into(viewHolder.mImageViewCategory);

                viewHolder.setmItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s | %s",adapter.getRef(position).getKey(),
                                model.getName()),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        categoryList.setAdapter(adapter);
    }
}
