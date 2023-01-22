package com.technoprimates.memotest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.technoprimates.memotest.MainViewModel;
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
        if (binding.contentId.getEditText() != null) binding.contentId.getEditText().setText(String.valueOf(code.getCodeId()));
        binding.contentId.setEnabled(false);

        if (binding.contentCategory.getEditText() != null) binding.contentCategory.getEditText().setText(code.getCodeCategory());
        binding.contentCategory.setEnabled(false);

        if (binding.contentCodename.getEditText() != null) binding.contentCodename.getEditText().setText(code.getCodeName());
        binding.contentCodename.setEnabled(false);

        if (binding.contentCodeval.getEditText() != null) binding.contentCodeval.getEditText().setText(code.getCodeValue());
        binding.contentCodeval.setEnabled(false);

        binding.checkboxFingerprint.setChecked(code.getCodeProtectMode() == Code.FINGERPRINT_PROTECTED);
        binding.checkboxFingerprint.setEnabled(false);

        // TODO : modify button
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}