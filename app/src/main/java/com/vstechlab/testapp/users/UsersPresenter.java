package com.vstechlab.testapp.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.vstechlab.testapp.data.User;
import com.vstechlab.testapp.data.source.UsersLoader;
import com.vstechlab.testapp.data.source.UsersRepository;

import java.util.List;

public class UsersPresenter implements LoaderManager.LoaderCallbacks<List<User>>,
        UsersContract.Presenter{

    private final static int USERS_LOADER_ID = 1;

    private static final String TAG = UsersPresenter.class.getSimpleName();

    private final UsersRepository mUserRepository;

    private final UsersContract.View mUsersView;

    private final UsersLoader mLoader;

    private final LoaderManager mLoaderManager;

    private List<User> mCurrentUser;

    private boolean mFirstLoad;

    public UsersPresenter(@NonNull UsersRepository mUserRepository,
                          @NonNull UsersContract.View mUsersView,
                          @NonNull UsersLoader mLoader,
                          @NonNull LoaderManager mLoaderManager) {

        this.mUserRepository = mUserRepository;
        this.mUsersView = mUsersView;
        this.mLoader = mLoader;
        this.mLoaderManager = mLoaderManager;

        mUsersView.setPresenter(this);
    }

    @Override
    public void loadUsers(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad) {
            mFirstLoad = false;
            mUserRepository.refreshUsers();
        } else {
            if (mCurrentUser != null) {
                processUsers(mCurrentUser);
            }
        }
    }

    @Override
    public void openUserDetails(@NonNull User user) {
        mUsersView.showUserDetailsUI(user.getId());
    }

    @Override
    public void onUserDelete(@NonNull User user) {
        mUserRepository.deleteUser(user);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(USERS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        mUsersView.showLoadingIndicator(true);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        mUsersView.showLoadingIndicator(false);

        mCurrentUser = data;

        if (mCurrentUser == null) {
            mUsersView.showLoadingUsersError();
        } else {
            processUsers(data);
        }
    }

    private void processUsers(List<User> data) {
        if (data.isEmpty())
            mUsersView.showNoUsers();
        else
            mUsersView.showUsers(data);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        // no-op
    }
}
