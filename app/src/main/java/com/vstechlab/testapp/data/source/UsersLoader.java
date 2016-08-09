package com.vstechlab.testapp.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.vstechlab.testapp.data.User;

import java.util.List;

public class UsersLoader extends AsyncTaskLoader<List<User>>
        implements UsersRepository.UsersRepositoryObserver{

    private UsersRepository mRepository;

    public UsersLoader(Context context, @NonNull UsersRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public List<User> loadInBackground() {
        return mRepository.getUsers();
    }

    @Override
    public void deliverResult(List<User> data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver cached users immediately.
        if (mRepository.cachedUserAvailable()) {
            deliverResult(mRepository.getCachedUsers());
        }

        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedUserAvailable()){
            forceLoad();
        }

    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeObserver(this);
    }

    @Override
    public void onUserChanged() {
        if (isStarted())
            forceLoad();
    }
}
