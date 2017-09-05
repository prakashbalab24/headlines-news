package singledevapps.wvstask.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import singledevapps.wvstask.R;
import singledevapps.wvstask.activities.NewsBrowser;
import singledevapps.wvstask.helper.NetworkHelper;
import singledevapps.wvstask.model.News;
import singledevapps.wvstask.parallaxrecyclerview.ParallaxViewHolder;

/**
 * Created by prakash on 11/6/17.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context mContext;
    private List<News> newsList;

    public class MyViewHolder extends ParallaxViewHolder {
        public TextView title,desc;
        public RelativeLayout relativeLayout;
        public LinearLayout linearLayout;

        @Override
        public int getParallaxImageId() {
            return R.id.background;
        }

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_card);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_desc);
            desc = (TextView) view.findViewById(R.id.tv_desc);

        }
    }


    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.desc.setText(news.getDescription());
        if (!news.getUrlToImage().isEmpty()) {
           // Picasso.with(mContext).load(news.getUrlToImage()).transform(new BlurTransformation(mContext)).into(holder.getBackgroundImage());
            Picasso.with(mContext).load(news.getUrlToImage()).resize(300,300).into(holder.getBackgroundImage());
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkHelper.checkConnection(mContext)){
                    Intent intent = new Intent(mContext, NewsBrowser.class);
                    intent.putExtra("url",news.getSourceUrl());
                    mContext.startActivity(intent);
                    return;
                }
                if(holder.linearLayout.getVisibility()== View.VISIBLE) {
                    holder.linearLayout.setVisibility(View.GONE);
                    return;
                }
                holder.linearLayout.setVisibility(View.VISIBLE);
            }
        });
//        holder.bck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, NewsDescription_Activity.class);
//                intent.putExtra("title",news.getTitle());
//                intent.putExtra("desc",news.getDescription());
//                intent.putExtra("bck",news.getUrlToImage());
//                mContext.startActivity(intent);
//            }
//        });
        holder.getBackgroundImage().reuse();
        }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
 