package com.technoprimates.memotest.ui;

import android.widget.TextView;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import com.technoprimates.memotest.R;

public class CodeViewHolder extends RecyclerView.ViewHolder {
    final TextView item_dbId;
    final TextView item_rvId;
    final TextView item_categ;
    final TextView item_codeName;
    final TextView item_codeUpdateDay;

    CodeViewHolder(View itemView) {
        super(itemView);
        item_dbId = itemView.findViewById(R.id.dbId);
        item_rvId = itemView.findViewById(R.id.rvId);
        item_categ = itemView.findViewById(R.id.categ);
        item_codeName = itemView.findViewById(R.id.codeName);
        item_codeUpdateDay = itemView.findViewById(R.id.codeUpdateDay);
    }
}