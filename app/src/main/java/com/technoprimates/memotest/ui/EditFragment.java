package com.technoprimates.memotest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.technoprimates.memotest.MainViewModel;
import com.technoprimates.memotest.R;
import com.technoprimates.memotest.databinding.CodeEditBinding;
import com.technoprimates.memotest.db.Code;

public class EditFragment extends Fragment {

    public static final String TAG = "EDITFRAG";
    private CodeEditBinding binding;
    private MainViewModel mViewModel;
    private boolean mUpdateMode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // add fragment-specific menu using MenuProvider
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_add, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_save) {
                    onSaveClicked();
                    return true;
                }
            return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // binding
        binding = CodeEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    }

    @SuppressWarnings("ConstantConditions")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if a new Code is to be created or if an existing one is to be updated
        if (mViewModel.getCurrentCode() != null) {
            // update an existing code available in the ViewModel
            mUpdateMode = true;

            // fill the fields with existing code
            binding.contentCodename.getEditText().setText(mViewModel.getCurrentCode().getCodeName());
            binding.contentCodeval.getEditText().setText(mViewModel.getCurrentCode().getCodeValue());
            binding.contentCategory.getEditText().setText(mViewModel.getCurrentCode().getCodeCategory());
            binding.contentComments.getEditText().setText(mViewModel.getCurrentCode().getCodeComments());

            // Set the fragment title
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.title_update);

        } else {
            // insert a new code
            mUpdateMode = false;
            binding.contentCodename.getEditText().setText("");
            binding.contentCodeval.getEditText().setText("");
            binding.contentCategory.getEditText().setText("");
            binding.contentComments.getEditText().setText("");

            // Set the fragment title
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.title_add);
        }

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

    }

    @SuppressWarnings("ConstantConditions")
    private Code getUserInput() {
        // get user inputs
        String name = binding.contentCodename.getEditText().getText().toString();
        String value = binding.contentCodeval.getEditText().getText().toString();
        String categ = binding.contentCategory.getEditText().getText().toString();
        String comments = binding.contentComments.getEditText().getText().toString();
        int protectMode = binding.checkboxFingerprint.isChecked() ? Code.FINGERPRINT_PROTECTED : Code.NOT_FINGERPRINT_PROTECTED;

        // check name, value and categ
        if (name.equals("")) {
            binding.contentCodename.setError(getString(R.string.err_noname));
            binding.contentCodename.requestFocus();
            return null;
        } else {
            binding.contentCodename.setError(null);
            binding.contentCodename.setHelperTextEnabled(false);
        }

        if (value.equals("")) {
            binding.contentCodeval.setError(getString(R.string.err_nocodeval));
            binding.contentCodeval.requestFocus();
            return null;
        } else {
            binding.contentCodeval.setError(null);
            binding.contentCodeval.setHelperTextEnabled(false);
        }

        // checks ok, build Code object with user input
        return (new Code(name, value, categ, comments, protectMode));
    }

    private void onSaveClicked () {
        Code code = getUserInput();

        // good effort User, please try again
        if (code == null) return;

        if (!mUpdateMode) {
            // Insertion
            // Insert only if the name provided is not already in the database
            if (mViewModel.codenameAlreadyExists(code)) {
                // name already exists : refuse insertion
                binding.contentCodename.setError(getString(R.string.err_name_already_exists));
                binding.contentCodename.requestFocus();
                return;
            } else {
                // request the view model to insert the code
                mViewModel.insertCode(code);
            }
            // (end of insert mode)

        } else {
            // Update unless the user provided a new name
            // and this name already exists in database
            if ((!code.getCodeName().equals(mViewModel.getCurrentCode().getCodeName()))
                    && (mViewModel.codenameAlreadyExists(code))) {
                binding.contentCodename.setError(getString(R.string.err_name_already_exists));
                binding.contentCodename.requestFocus();
                return;
            } else {
                // request the view model to update the code
                mViewModel.updateCode(code);
            }
        }

        // save completed, return to the list fragment
        NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_EditFragment_to_ListFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}