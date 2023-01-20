package com.technoprimates.memotest.ui;

import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.technoprimates.memotest.MainViewModel;
import com.technoprimates.memotest.R;
import com.technoprimates.memotest.databinding.FragmentListBinding;
import com.technoprimates.memotest.db.Code;

import java.util.List;

public class ListFragment extends Fragment implements CodeListAdapter.CodeActionListener {

    private MainViewModel mViewModel;
    private FragmentListBinding binding;
    private CodeListAdapter adapter;
    private Code currentCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            // must be from first fragment, otherwise fab is invisible TODO check
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });
        observerSetup();
        recyclerSetup();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void observerSetup() {
        mViewModel.getAllcodes().observe(getViewLifecycleOwner(),
                new Observer<List<Code>>() {
                    @Override
                    public void onChanged(@Nullable final List<Code> codes) {
                        adapter.setCodeList(codes); // update RV
                    }
                });
    }

    private void recyclerSetup() {
        adapter = new CodeListAdapter(R.layout.item_code, this);
        binding.codeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.codeRecycler.setAdapter(adapter);

        // swipe detection
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override //not used
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {return false;}

            @Override //swipe
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                onDeleteCodeRequest(viewHolder.getBindingAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.codeRecycler);
    }

    /**
     * {@inheritDoc}
     * This triggers immediately a navigation to the Visualization Fragment.
     * If the Code is protected by fingerprint, a authentication is performed first.
     */
    @Override
    public void onCodeClicked(int pos) {
        currentCode = adapter.getCodeAtPos(pos);
        if (currentCode == null) return;

        // check if the Code is fingerprint protected
        if (currentCode.getCodeProtectMode() == Code.FINGERPRINT_PROTECTED) {

            // Code protected : Ask for the user's fingerprint
            BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(getActivity())
                    .setTitle(getString(R.string.app_name))
                    .setSubtitle(getString(R.string.prompt_authentication_required))
                    .setDescription((getString(R.string.prompt_code_protected_by_fingerprint)))
                    .setNegativeButton("Cancel", requireActivity().getMainExecutor(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), getString(R.string.authentication_cancelled), Toast.LENGTH_LONG).show();
                        }
                    })
                    .build();

            // launch authentication : if successfull, the callback will launch the navigation to the visu fragment
            biometricPrompt.authenticate(getCancellationSignal(), requireActivity().getMainExecutor(), getAuthenticationCallback());

        } else {
            // No authentication required : navigate to visualization of currentCode
            navigateToCodeVisu();
        }
    }


    /**
     * Navigate to the visualization fragment
     */
    private void navigateToCodeVisu() {
        mViewModel.setCurrentCode(currentCode);
        NavHostFragment.findNavController(ListFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    public void onDeleteCodeRequest(int pos) {

        // delete selected Code
        Code code = adapter.getCodeAtPos(pos);
        if (code == null) return;

        mViewModel.deleteCode(code.getCodeName());

        // show snackbar with undo button
        Snackbar snackbar = Snackbar.make(binding.codeRecycler, "Code deleted at pos : "+pos, Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.insertCode(code);
            }
        });
        snackbar.show();
    }

    /**
     * Defines the methods to handle the authentication events
     */
    private AuthenticationCallback getAuthenticationCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                Toast.makeText(getActivity(), getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                Toast.makeText(getActivity(), getString(R.string.authentication_success), Toast.LENGTH_LONG).show();
                super.onAuthenticationSucceeded(result);
                navigateToCodeVisu();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(getActivity(), getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                super.onAuthenticationFailed();
            }
        };
    }

    /**
     * Defines the CancellationSignal object
     * Call cancel() on this object to cancel the authentication attempt
     */
    private CancellationSignal getCancellationSignal() {
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            /**
             * Callback launched after cancellation
             */
            @Override
            public void onCancel() {
                Snackbar snackbar = Snackbar.make(binding.codeRecycler, "Canceled via signal", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return cancellationSignal;
    }


}