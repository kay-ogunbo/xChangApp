package com.example.android.xchangapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ogunbowale on 11/1/2017.
 */

public class xCurrencyAdapter extends RecyclerView.Adapter<xCurrencyAdapter.DataViewHolder>{

    private List<Currency> currLists;
    private Context context;
    final private ListItemClickListner mOnClickListner;

    // click Listener interface
    public interface ListItemClickListner{
        void onListItemClick(int clickedItemIndex);
    }

    public xCurrencyAdapter(List<Currency> currLists, ListItemClickListner listner){
        this.currLists = currLists;
        mOnClickListner = listner;
    }

    @Override
    public xCurrencyAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DataViewHolder dataViewHolder = new DataViewHolder(view);

        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(xCurrencyAdapter.DataViewHolder holder, int position) {

        final Currency currList = currLists.get(position);

        holder.currSym.setText(currList.getCurrSym());
        holder.ethData.setText(String.format("%1$,.2f", currList.getEthValue()));
        holder.btcData.setText(String.format("%1$,.2f", currList.getBtcValue()));

    }


    @Override
    public int getItemCount() {
        return currLists.size();
    }

    // All required view components are defined here
    public class DataViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView currSym;
        TextView ethData;
        TextView btcData;

        public DataViewHolder(View v){
            super(v);

            currSym = (TextView) v.findViewById(R.id.currSym);
            ethData = (TextView) v.findViewById(R.id.ethVal);
            btcData = (TextView) v.findViewById(R.id.btcVal);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
             //Capture the item clicked by the user and put into an intent
            int clickedPosition = getAdapterPosition();
            mOnClickListner.onListItemClick(clickedPosition);
            Currency currency = currLists.get(clickedPosition);
            Intent i = new Intent(context, currency_conversion.class);
            i.putExtra(appConstant.CURR_SYM_Data, currency.getCurrSym());
            i.putExtra(appConstant.CURR_ETH_DATA_EXT, currency.getEthValue());
            i.putExtra(appConstant.CURR_BTC_DATA_EXT, currency.getBtcValue());
            context.startActivity(i);
        }
    }
}
