package com.technoprimates.memotest.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.technoprimates.memotest.db.Code;
import com.technoprimates.memotest.R;


import java.util.List;

public class CodeListAdapter extends RecyclerView.Adapter<CodeListAdapter.ViewHolder> {

    public interface CodeActionListener {
        void onViewCodeRequest(int pos);
    }

    private final int codeItemLayout;
    private List<Code> codeList;
    private CodeActionListener listener;

    public CodeListAdapter(int layoutId, CodeActionListener listener) {
        codeItemLayout = layoutId;
        this.listener = listener;
    }

    public void setCodeList(List<Code> codes) {
        codeList = codes;
        notifyDataSetChanged();
    }

    @Nullable
    public Code getCodeAtPos(int pos) {
        Code code;
        try {
            code = codeList.get(pos);
        } catch (Exception e) {
            return null;
        }
        return code;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(codeItemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int listPosition) {
        holder.item_dbId.setText(String.valueOf(codeList.get(listPosition).getCodeId()));
        holder.item_rvId.setText(String.valueOf(listPosition));
        holder.item_codeName.setText(codeList.get(listPosition).getCodeName());
        holder.item_codeVal.setText(codeList.get(listPosition).getCodeValue());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onViewCodeRequest(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return codeList == null ? 0 : codeList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_dbId;
        TextView item_rvId;
        TextView item_codeName;
        TextView item_codeVal;

        ViewHolder(View itemView) {
            super(itemView);
            item_dbId = itemView.findViewById(R.id.dbId);
            item_rvId = itemView.findViewById(R.id.rvId);
            item_codeName = itemView.findViewById(R.id.codeName);
            item_codeVal = itemView.findViewById(R.id.codeVal);
        }
    }
}
