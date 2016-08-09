package com.vstechlab.testapp.users;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.vstechlab.testapp.R;
import com.vstechlab.testapp.data.User;
import com.vstechlab.testapp.data.source.UsersLoader;
import com.vstechlab.testapp.data.source.UsersRepository;
import com.vstechlab.testapp.data.source.local.UsersLocalDataSource;
import com.vstechlab.testapp.data.source.remote.UserApi;
import com.vstechlab.testapp.data.source.remote.UsersRemoteDataSource;
import com.vstechlab.testapp.edituser.EditActivity;
import com.vstechlab.testapp.edituser.UserEditContract;
import com.vstechlab.testapp.ui.DividerItemDecoration;
import com.vstechlab.testapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vstechlab.testapp.edituser.EditActivity.EXTRA_USER;

public class UsersActivity extends AppCompatActivity implements UsersContract.View {

    private static final String TAG = UsersActivity.class.getSimpleName();

    public static final int REQUEST_EDIT_USER = 100;

    @BindView(R.id.progress) ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.rv_users) RecyclerView mRvUsers;
    @BindView(R.id.fm_container) FrameLayout mFmContainer;
    @BindView(R.id.refresh_container) ScrollChildSwipeRefreshLayout mSwipeContainer;

    private UsersContract.Presenter mPresenter;

    private UsersAdapter mAdapter;

    private List<User> mUsers = new ArrayList<>();

    private UsersAdapter.ItemListener mItemListener = new UsersAdapter.ItemListener() {
        @Override
        public void onUserClick(User user) {
            mPresenter.openUserDetails(user);
        }

        @Override
        public void onClickDelete(User user) {
            mPresenter.onUserDelete(user);
        }

        @Override
        public void onClickEdit(User user) {
            Intent intent = new Intent(UsersActivity.this, EditActivity.class);
            intent.putExtra(EXTRA_USER, user);
            startActivityForResult(intent, REQUEST_EDIT_USER);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new UsersAdapter(mUsers, mItemListener, this);

        mRvUsers.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvUsers.setLayoutManager(linearLayoutManager);
        mRvUsers.addItemDecoration(new DividerItemDecoration(this));

        UsersRepository repository = UsersRepository.getInstance(
                UsersRemoteDataSource.getInstance(),
                UsersLocalDataSource.getInstance(this));

        UsersLoader loader = new UsersLoader(this, repository);

        mPresenter = new UsersPresenter(
                repository,
                this,
                loader,
                getSupportLoaderManager()
        );

        mSwipeContainer.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));

        mSwipeContainer.setScrollUpChild(mRvUsers);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.isNetworkAvailable(getApplicationContext())) {
                    mPresenter.loadUsers(true);
                } else {
                    mSwipeContainer.setRefreshing(false);
                    showNoInternet();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_USER) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(mFmContainer, "Username edited successfully.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        if (show)
            mProgressBar.setVisibility(View.VISIBLE);
        else
            mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingUsersError() {
        Snackbar.make(mFmContainer, "Failed to load users!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showUsers(@NonNull List<User> users) {
        mSwipeContainer.setRefreshing(false);
        mUsers.clear();
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUserDetailsUI(int userId) {
        // Todo implement if required.
    }

    @Override
    public void showNoUsers() {
        Snackbar.make(mFmContainer, "No User to Display!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showNoInternet() {
        Snackbar.make(mFmContainer, "Internet Not Available!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(UsersContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
