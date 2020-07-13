package com.mahmoud.seemov.DetailActivity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.seemov.DetailActivity.Adapters.ReviewAdapter.ReviewHolder;
import com.mahmoud.seemov.Models.Review;
import com.mahmoud.seemov.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private final Context mContext;
    private ArrayList<Review> mReviews;

    public ReviewAdapter(Context context)
    {
        mContext = context;

    }
    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.row_review_list_item,parent,false);

        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {

        Review reviewItem = mReviews.get(position);

        holder.reviewerName.setText(reviewItem.getReviewerName());
        holder.reviewDesc.setText(reviewItem.getReviewDescription());
    }

    @Override
    public int getItemCount() {
        if(mReviews==null) return 0;

        return mReviews.size();
    }



    public void setReviews(ArrayList<Review> reviews) {

        mReviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.reviewer_name)
        TextView reviewerName;

        @BindView(R.id.expand_text_view)
        ExpandableTextView reviewDesc;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
              }
    }
}
