package com.technoprimates.memotest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.technoprimates.memotest.MainViewModel;
import com.technoprimates.memotest.R;
import com.technoprimates.memotest.databinding.FragmentAddBinding;
import com.technoprimates.memotest.db.Code;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    public void validate() {

        String name = binding.contentCodename.getText().toString();
        String value = binding.contentCodeval.getText().toString();
        String categ = binding.contentCategory.getText().toString();
        int protectMode = binding.checkboxFingerprint.isChecked() ? Code.FINGERPRINT_PROTECTED : Code.NOT_FINGERPRINT_PROTECTED;

        // check name, value and categ
        if (name.equals("")) {
            binding.textviewThird.setText("No Name value");
            return;
        }
        if (value.equals("")) {
            binding.textviewThird.setText("No Value value");
            return;
        }
        if (categ.equals("")) {
            binding.textviewThird.setText("No categ value");
            return;
        }

        // check new name not already in viewmodel
        if (!mViewModel.checkCodeName(name)) {
            binding.textviewThird.setText("Name already exists");
            return;
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