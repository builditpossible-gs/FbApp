package fbapp.example.com.fbapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import fbapp.example.com.fbapp.Modules.FBDatumClass;
import fbapp.example.com.fbapp.R;


public class FacebookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<FBDatumClass> fbData;
    public Context context;

    private NativeAdsManager mAds;
    private NativeAd mAd = null;

    private int AD_TYPE = 1;
    private int POST_TYPE = 0;

    public FacebookAdapter(List<FBDatumClass> FbData, Context _context, NativeAdsManager ads) {
        fbData = FbData;
        context = _context;
        mAds = ads;
    }

    @Override
    public int getItemCount() {
        return fbData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position) % 4 == 3 ? AD_TYPE : POST_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == POST_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fb_post, parent, false);
            return new FbPostViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.large_ad, parent, false);
            return new AdHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == POST_TYPE) {

            FBDatumClass post = fbData.get(position);
            ((FbPostViewHolder)holder).bindView(post);

        } else {

            if (mAd != null) {
                ((AdHolder) holder).bindView(mAd);
            } else if (mAds != null && mAds.isLoaded()) {
                NativeAd mAd = mAds.nextNativeAd();
                ((AdHolder) holder).bindView(mAd);
            } else {
                ((AdHolder) holder).bindView(null);
            }
        }
    }

    public class FbPostViewHolder extends RecyclerView.ViewHolder {

        ImageView fbPostImage;
        ImageView fbPostStoryIcon;
        ImageView fbPostMessageIcon;
        TextView fbPostMessage;
        TextView fbPostStory;
        Button fbLinkButton;
        Button fbShareButton;
        RelativeLayout storyLayout;

        public FbPostViewHolder(View itemView) {
            super(itemView);

            fbPostImage = itemView.findViewById(R.id.fbPostimage);
            fbPostStoryIcon = itemView.findViewById(R.id.fbPostStoryIcon);
            fbPostMessageIcon = itemView.findViewById(R.id.fbPostMessageIcon);
            fbPostMessage = itemView.findViewById(R.id.fbPostMessage);
            fbPostStory = itemView.findViewById(R.id.fbPostStory);
            fbLinkButton = itemView.findViewById(R.id.fbLinkButton);
            storyLayout = itemView.findViewById(R.id.storyLayout);
            fbShareButton = itemView.findViewById(R.id.fbshareButton);

        }

        void bindView(final FBDatumClass post) {

            Object story = post.getFBStory();
            Object message = post.getFBMessage();
            Object image = post.getFBFullPicture();
            Object link = post.getFBLink();


            if (image == null) {
                fbPostImage.setVisibility(View.GONE);
            } else {
                Picasso.with(context).load(post.getFBFullPicture()).fit()
                        .into(fbPostImage);
            }

            if (message != null) {

                fbPostMessage.setText(post.getFBMessage());
                Picasso.with(context).load(R.drawable.ic_message).fit()
                        .placeholder(R.drawable.ic_message).into(fbPostMessageIcon);
            }

            if (story == null) {
                storyLayout.setVisibility(View.GONE);

            } else {

                fbPostStory.setText(post.getFBStory());
                Picasso.with(context).load(R.drawable.ic_story).fit()
                        .placeholder(R.drawable.ic_story).into(fbPostStoryIcon);
            }

            if (link == null) {

                fbLinkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(context.getResources().getString(R.string.facebook_page_url)));
                        context.startActivity(browserIntent);
                    }
                });

            } else {

                fbLinkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(post.getFBLink()));
                        context.startActivity(browserIntent);
                    }
                });
            }

            fbShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            post.getFBMessage());
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    context.startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
            });

        }

    }

    public class AdHolder extends RecyclerView.ViewHolder {
        private MediaView mAdMedia;
        private ImageView mAdImage;
        private ImageView mAdIcon;
        private TextView mAdTitle;
        private TextView mAdBody;
        private TextView mAdSocialContext;
        private Button mAdCallToAction;
        private ImageView mAdChoice;

        public AdHolder(View view) {
            super(view);

            mAdMedia = view.findViewById(R.id.adMedia);
            mAdImage = view.findViewById(R.id.adImage);
            mAdTitle = view.findViewById(R.id.adHeading);
            mAdBody = view.findViewById(R.id.adBody);
            mAdSocialContext = view.findViewById(R.id.adSocialContext);
            mAdCallToAction = view.findViewById(R.id.adCallToAction);
            mAdIcon = view.findViewById(R.id.adIcon);
            mAdChoice = view.findViewById(R.id.mAdChoice);
        }

        void bindView(NativeAd ad) {
            if (ad == null) {

                mAdMedia.setVisibility(View.GONE);
                mAdImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(R.drawable.header_image).into(mAdImage);
                mAdTitle.setText(R.string.adTempTitle);
                mAdCallToAction.setText(R.string.adTempButton);
                mAdSocialContext.setText(R.string.app_name);
                mAdBody.setText(R.string.adMessage);
            } else {
                mAdMedia.setVisibility(View.VISIBLE);
                mAdImage.setVisibility(View.GONE);
                mAdTitle.setText(ad.getAdTitle());
                mAdBody.setText(ad.getAdBody());
                mAdSocialContext.setText(ad.getAdSocialContext());
                mAdCallToAction.setText(ad.getAdCallToAction());
                mAdMedia.setNativeAd(ad);
                NativeAd.Image adIcon1 = ad.getAdChoicesIcon();
                NativeAd.downloadAndDisplayImage(adIcon1, mAdChoice);
                NativeAd.Image adIcon = ad.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, mAdIcon);

            }
        }
    }
}