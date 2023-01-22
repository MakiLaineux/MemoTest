package com.technoprimates.memotest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.technoprimates.memotest.MainViewModel;
import com.technoprimates.memotest.R;
import com.technoprimates.memotest.databinding.CodeViewBinding;
import com.technoprimates.memotest.db.Code;

public class AddFragment extends Fragment {

    private CodeViewBinding binding;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CodeViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // start with focus on first input
        binding.contentCodename.requestFocus();

        // set dropdown list for categories
        String[] categs = getResources().getStringArray(R.array.categs);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.categ_dropdown_item, categs);
        binding.autoCompleteTextView.setAdapter(arrayAdapter);

        // listeners, triggered when losing the focus, for clearing a previous "empty field" error message
        binding.inputCodename.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // lost the focus, there may be an error msg to clear
                if (binding.contentCodename.getError() != null) {
                    // there is currently an error msg, check if it is to be cleared
                    if ((binding.contentCodename.getEditText() != null)
                            && (!TextUtils.isEmpty(binding.contentCodename.getEditText().getText().toString()))) {
                        // Code name not empty, the error message can be cleared
                        binding.contentCodename.setError(null);
                    }
                }
            }
        });
        binding.inputCodeval.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // lost the focus, there may be an error msg to clear
                if (binding.contentCodeval.getError() != null) {
                    // there is currently an error msg, check if it is to be cleared
                    if ((binding.contentCodeval.getEditText() != null)
                        && (!TextUtils.isEmpty(binding.contentCodeval.getEditText().getText().toString()))) {
                        // Code name not empty, the error message can be cleared
                        binding.contentCodeval.setError(null);
                    }
                }
            }
        });

        // listener for the validation button
        binding.buttonAdd.setOnClickListener(v -> validate());
    }

    public void validate() {
        String name = binding.contentCodename.getEditText().getText().toString();
        String value = binding.contentCodeval.getEditText().getText().toString();
        String categ = binding.contentCategory.getEditText().getText().toString();
        int protectMode = binding.checkboxFingerprint.isChecked() ? Code.FINGERPRINT_PROTECTED : Code.NOT_FINGERPRINT_PROTECTED;

        // check name, value and categ
        if (name.equals("")) {
            binding.contentCodename.setError("No Name value");
            binding.contentCodename.requestFocus();
            return;
        } else {
            binding.contentCodename.setError(null);
            binding.contentCodename.setHelperTextEnabled(false);
        }

        if (value.equals("")) {
            binding.contentCodeval.setError("No code value");
            binding.contentCodeval.requestFocus();
            return;
        } else {
            binding.contentCodeval.setError(null);
            binding.contentCodeval.setHelperTextEnabled(false);
        }

        // check new name not already in viewmodel
        if (!mViewModel.checkCodeName(name)) {
            binding.contentCodename.setError("Name already exists");
            binding.contentCodename.requestFocus();
            return;
        } else {
            binding.contentCodename.setError(null);
        }

        Code code = new Code(name, value, categ, protectMode);
        mViewModel.insertCode(code);
        NavHostFragment.findNavController(AddFragment.this).navigate(R.id.action_ThirdFragment_to_FirstFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}