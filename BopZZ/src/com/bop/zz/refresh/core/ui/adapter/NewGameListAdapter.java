package com.bop.zz.refresh.core.ui.adapter;

import java.util.List;

import com.bop.zz.R;
import com.bop.zz.refresh.core.http.model.MyAlbumsInfo;
import com.bop.zz.ui.ShareBottomDialog;
import com.bop.zz.widget.dialog.StyledDialog;
import com.bop.zz.widget.dialog.interfaces.MyDialogListener;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;

public class NewGameListAdapter extends ViewHolderAdapter<NewGameListAdapter.NewGameViewHolder, MyAlbumsInfo> {

    public NewGameListAdapter(Context context, List<MyAlbumsInfo> list) {
        super(context, list);
    }

    @Override
    public NewGameViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.adapter_list_item, parent);
        return new NewGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewGameViewHolder holder, int position) {
        MyAlbumsInfo gameInfo = getDatas().get(position);
        Picasso.with(getContext())
                .load(gameInfo.logo)
                .centerCrop()
                .placeholder(R.drawable.ic_gf_default_photo)
                .error(R.drawable.ic_gf_default_photo)
                .resize(300, 220)

                .config(Bitmap.Config.ARGB_8888)
                .into(holder.mIcGameIcon);
        holder.mTvGameName.setText(gameInfo.title);
        holder.mTvTime.setText(gameInfo.addtime);
        holder.mTvGameSocre.setText(getContext().getResources().getString(R.string.str_my_albums_scans, 0));
        holder.mTvGamePlayerNumber.setText(getContext().getResources().getString(R.string.str_my_albums_rewards, 0));
        holder.mTvGameCommentNumber.setText(getContext().getResources().getString(R.string.str_my_albums_rewards_money, "0.00"));
        
        holder.mTvShareBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 ShareBottomDialog dialog = new ShareBottomDialog();
				 if(getContext() instanceof FragmentActivity) {
					 dialog.show(((FragmentActivity)getContext()).getSupportFragmentManager());
				 }
			}
		});
        
        holder.mTvDelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 StyledDialog.buildIosAlert((FragmentActivity)getContext(), 
						 getContext().getResources().getString(R.string.str_title_comfirm_del), 
						 getContext().getResources().getString(R.string.str_title_comfirm_del_albums),  new MyDialogListener() {
	                    @Override
	                    public void onFirst() {
	                    }

	                    @Override
	                    public void onSecond() {
	                    }

	                    @Override
	                    public void onThird() {
	                    }
	                }).show();
			}
		});
        
        holder.mTvEditBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
    }

    static class NewGameViewHolder extends ViewHolderAdapter.ViewHolder {
    	TextView mTvTime;
    	ImageView mIcGameIcon;
        TextView mTvGameName;
        TextView mTvGameSocre;
        TextView mTvGamePlayerNumber;
        TextView mTvGameCommentNumber;

        TextView mTvShareBtn, mTvDelBtn, mTvEditBtn;
        
        public NewGameViewHolder(View view) {
            super(view);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
            mIcGameIcon = (ImageView) view.findViewById(R.id.ic_game_icon);
            mTvGameName = (TextView) view.findViewById(R.id.tv_game_name);
            mTvGameSocre = (TextView) view.findViewById(R.id.tv_game_socre);
            mTvGamePlayerNumber = (TextView) view.findViewById(R.id.tv_game_player_number);
            mTvGameCommentNumber = (TextView) view.findViewById(R.id.tv_game_comment_number);
            
            mTvShareBtn = (TextView) view.findViewById(R.id.tv_share);
            mTvDelBtn = (TextView) view.findViewById(R.id.tv_del); 
            mTvEditBtn = (TextView) view.findViewById(R.id.tv_edit); 
        }
    }
}
