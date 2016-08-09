package com.vstechlab.testapp.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vstechlab.testapp.R;
import com.vstechlab.testapp.data.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    private List<User> users;
    private ItemListener mListener;
    private Context mContext;

    public UsersAdapter(List<User> users, ItemListener mListener, Context context) {
        this.users = users;
        this.mListener = mListener;
        mContext = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        return new UserViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        final User user = users.get(position);
        Picasso.with(mContext)
                .load(user.getProfile())
                .into(holder.cvProfile);
        holder.tvUserName.setText(user.getUsername());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickEdit(user);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickDelete(user);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static final class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView tvUserName;
        @BindView(R.id.iv_profile) CircleImageView cvProfile;
        @BindView(R.id.iv_edit) ImageView ivEdit;
        @BindView(R.id.iv_delete) ImageView ivDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onUserClick(User user);
        void onClickDelete(User user);
        void onClickEdit(User user);
    }
}
