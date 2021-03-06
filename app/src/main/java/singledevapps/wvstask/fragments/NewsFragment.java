package singledevapps.wvstask.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import singledevapps.wvstask.R;
import singledevapps.wvstask.adapter.NewsAdapter;
import singledevapps.wvstask.model.data.local.DatabaseHandler;
import singledevapps.wvstask.model.data.remote.AsyncTaskHelper;
import singledevapps.wvstask.model.News;
import singledevapps.wvstask.helper.ui.parallaxrecyclerview.ParallaxRecyclerView;


public class NewsFragment extends Fragment implements ResponseListner{
    private static final String URL ="url" ;
    private ParallaxRecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;
    private News model;
    private String source = "";

    private Boolean isStarted = false;
    private Boolean isVisible = false;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment instance(String source){
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, source);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible && isStarted){
            viewDidAppear();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (ParallaxRecyclerView) rootView.findViewById(R.id.recyclerview);
        source = getArguments().getString(URL);

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(getContext(), newsList);

//        ParallaxRecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        List<News> temp = new DatabaseHandler(getContext()).getOfflineNews(source);
        Log.i("tempFromDb","tempList:"+temp);
        newsList.addAll(temp);
        adapter.notifyDataSetChanged();
        //new AsyncTaskHelper(this,newsList,source, getContext()).execute();
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isStarted && isVisible) {
            viewDidAppear();
        }
    }

    private void viewDidAppear() {
//        new AsyncTaskHelper(this,newsList,source, getContext()).execute();
    }


    @Override
    public void onAsyncTaskComplete() {
        adapter.notifyDataSetChanged();
    }
}
