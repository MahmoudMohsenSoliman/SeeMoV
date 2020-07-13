package com.mahmoud.seemov.DetailActivity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.seemov.DetailActivity.Adapters.TrailerAdapter.TrailerHolder;
import com.mahmoud.seemov.Models.Trailer;
import com.mahmoud.seemov.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerHolder>  {

    private final Context mContext;
    private ArrayList<Trailer> mTrailers;
    private final ListItemClickListener mListItemClickListener;
    public TrailerAdapter(Context context,ListItemClickListener listItemClickListener)
    {
        mContext = context;
        mListItemClickListener = listItemClickListener;
    }
    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_trailer_list_item,parent,false);
        return  new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {

        Trailer trailerItem = mTrailers.get(position);

        holder.trailerTitle.setText(trailerItem.getTrailerTitle());

        //TODO(1) implement ClickListener to launch the trailer
    }

    @Override
    public int getItemCount() {
        if(mTrailers==null)return 0;
        return mTrailers.size();
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {

        mTrailers = trailers;
        notifyDataSetChanged();
    }

    class  TrailerHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        @BindView(R.id.trailer_name_text_view)
        TextView trailerTitle;

        TrailerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String path = mTrailers.get(position).getTrailerPath();

            mListItemClickListener.onListItemClick(path);
        }
    }
   public interface ListItemClickListener
    {
         void onListItemClick(String path);
    }
}
