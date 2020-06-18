package cn.thundergaba.todaysfootline;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.like.LikeButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "NEWS_FRAGMENT";


    private String mParam1;
    private String mParam2;

    private NewsFragment.OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoView.
     */

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_views, container, false);
        RecyclerView newslist = view.findViewById(R.id.n_news_list);
        LinearLayoutManager news_list_manager = new LinearLayoutManager(getActivity());
        newslist.setLayoutManager(news_list_manager);
        UpdateNewsListByCategory("news_new",newslist);
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsFragment.OnFragmentInteractionListener) {
            mListener = (NewsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private void UpdateNewsListByCategory(String category,RecyclerView nview) {
        final String reqString = "https://api03.6bqb.com/toutiao/search?apikey=E449EF5F8EE5C6C6919C52AA98160F07&tag=__all__&page=0";
        new Thread(() -> {
            try {

                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(reqString)//请求接口。如果需要传参拼接到接口后面。
                        .build();
                Response response = null;
                response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()) {
                    Log.d(TAG, "response.code()==" + response.code());
                    Log.d(TAG, "response.message()==" + response.message());
                    String responsestring = response.body().string();
                    Log.d(TAG, "res==" + responsestring);
                    //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    JSONObject root_object = new JSONObject(responsestring);
                    JSONArray news_object_array = root_object.getJSONArray("data");
                    nview.post(() -> {
                        try {
                            List<News> newlist = new ArrayList<>();
                            for (int i = 0; i < news_object_array.length(); i++) {
                                JSONObject newsobject = news_object_array.getJSONObject(i);
                                News news = NewsJsonConversion.GetNewsFromJson(TAG,newsobject);
                                if(news != null) {
                                    newlist.add(news);
                                }
                            }
                            NewsFragment.NewsItemAdapter adapter;
                            adapter = new NewsFragment.NewsItemAdapter(newlist,getActivity());
                            nview.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }).start();
    }



    public class NewsItemAdapter extends RecyclerView.Adapter<NewsFragment.NewsItemAdapter.NewsViewholder>{
        List<News> list;

        Activity activity;

        public NewsItemAdapter(List<News> list,Activity activity){
            this.list = list;
            this.activity = activity;
        }
        @NonNull
        @Override
        public NewsFragment.NewsItemAdapter.NewsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newsitem,parent,false);
            final NewsFragment.NewsItemAdapter.NewsViewholder holder = new NewsFragment.NewsItemAdapter.NewsViewholder(view);
//            Button btn_comment = view.findViewById(R.id.btn_n_comment);
//            btn_comment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Jump to video detail activity
//                }
//            });
//            LikeButton btn_like = view.findViewById(R.id.btn_v_like);
            //TODO Get the user information and do the like


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsFragment.NewsItemAdapter.NewsViewholder holder, int position) {
            // TODO user avatar is not correctly loaded
            holder.avatar.setImageURI(Uri.parse(list.get(position).getNewsUserInfo().getAvatar_url()));
            holder.user.setText(list.get(position).getNewsUserInfo().getName());
            holder.news.setText(list.get(position).getTitle());
            //holder.time.setText(list.get(position).getNewsPublishTime());
            //TODO video not played
            //TODO get the thumbnail of each video
            // Bitmap map = new Bitmap();
            //holder.video.setBackground();
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class NewsViewholder extends RecyclerView.ViewHolder{
            ImageView avatar;
            TextView user;
            TextView news;
            TextView time;
            public NewsViewholder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.news_image);
                user = itemView.findViewById(R.id.news_source);
                news = itemView.findViewById(R.id.news_title);
                //time = itemView.findViewById(R.id.news_time);
            }
        }


    }
}