package com.example.fragmentslearn;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> implements Filterable {
    private ArrayList<Cases> mList;
    private ArrayList<Cases> mListFull;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(ArrayList<Cases> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relative_view,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Begins");
        if(mList != null || mList.size()!=0){
            Cases caseItem = mList.get(position);
            holder.deathNum.setText(""+caseItem.getTotalDeaths());
            holder.countryName.setText(caseItem.getCountryName());
        }
    }

    public Cases getPhoto(int position) {
        return ((mList != null) && (mList.size() != 0) ? mList.get(position) : null);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {

        return exampleFilter;
    }
//    To filter the recyclerview
    private Filter exampleFilter = new Filter() {
        @Override
        //Background Thread
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Cases> filteredList = new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                if(mListFull!=null || mListFull.size()>0){
                    filteredList.addAll(mListFull);
                }

            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                if(mListFull!=null && mListFull.size()>0){
                    for(Cases item : mListFull){
//                        the sequence to search for
//                        *@return true if this string contains {@code s},
                        if(item.getCountryName().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList; //Contains all the values computed by the filtering operation.
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(mList!=null){
//              Removes all of the elements from this list.  The list will
//              *be empty after this call returns.
                mList.clear();
                mList.addAll((ArrayList<Cases>)results.values);
                notifyDataSetChanged();
            }
        }
    };

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView countryName;
        TextView deathNum;
        TextView deathtoll;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            deathtoll = itemView.findViewById(R.id.deathToll);
            deathNum = itemView.findViewById(R.id.textView7);
        }
    }

    public void loadNewDataSet(ArrayList<Cases> list){
        this.mList = list;
        mListFull = new ArrayList<>(list);
        notifyDataSetChanged();
    }
}
