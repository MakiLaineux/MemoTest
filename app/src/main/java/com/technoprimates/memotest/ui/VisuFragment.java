package com.technoprimates.memotest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.technoprimates.memotest.MainViewModel;
import com.technoprimates.memotest.R;
import com.technoprimates.memotest.databinding.CodeViewBinding;
import com.technoprimates.memotest.db.Code;


public class VisuFragment extends Fragment {

    private CodeViewBinding binding;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CodeViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        Code code = mViewModel.getCurrentCode();
        if (code == null)
            return;

        // fill fields and make them inactive
        binding.contentId.getEditText().setText(String.valueOf(code.getCodeId()));
        binding.contentId.setEnabled(false);

        binding.contentCategory.getEditText().setText(code.getCodeCategory());
        binding.contentCategory.setEnabled(false);

        binding.contentCodename.getEditText().setText(code.getCodeName());
        binding.contentCodename.setEnabled(false);

        binding.contentCodeval.getEditText().setText(code.getCodeValue());
        binding.contentCodeval.setEnabled(false);

        binding.checkboxFingerprint.setChecked(code.getCodeProtectMode() == Code.FINGERPRINT_PROTECTED ? true : false);
        binding.checkboxFingerprint.setEnabled(false);

        /*
        binding.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(VisuFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}