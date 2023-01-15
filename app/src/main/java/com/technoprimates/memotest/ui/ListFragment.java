package com.technoprimates.memotest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
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

    @Override
    public void onViewCodeRequest(int pos) {
        Code code = adapter.getCodeAtPos(pos);
        mViewModel.setCurrentCode(code);
        NavHostFragment.findNavController(ListFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    public void onDeleteCodeRequest(int pos) {

        // delete selected Code
        Code code = adapter.getCodeAtPos(pos);
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

}