package com.dinuscxj.pullzoomrecyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.pullzoom.PullZoomRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PullZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_zoom_header);
    }

    public abstract static class PullZoomFragment extends Fragment {

        protected PullZoomRecyclerView mRecyclerView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_pull_zoom_header, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mRecyclerView = (PullZoomRecyclerView) view.findViewById(R.id.recycler_view);
            mRecyclerView.setAdapter(createPullZoomAdapter(createPullZoomData()));
        }

        private List<Integer> createPullZoomData() {
            List<Integer> pullZoomData = new ArrayList<>();
            Collections.addAll(pullZoomData,
                    new Integer[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
                            R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8,
                            R.drawable.p9, R.drawable.p10});

            return pullZoomData;
        }

        protected abstract PullZoomAdapter createPullZoomAdapter(List<Integer> pullZoomData);

        public class PullZoomAdapter extends RecyclerListAdapter {

            protected List<Integer> listData;

            public PullZoomAdapter() {
                addViewType(Integer.class, new ViewHolderFactory<PullZoomItemHolder>() {
                    @Override
                    public PullZoomItemHolder onCreateViewHolder(ViewGroup parent) {
                        return new PullZoomItemHolder(parent);
                    }
                });
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        }

        private class PullZoomItemHolder extends RecyclerListAdapter.ViewHolder<Integer> {
            private ImageView imageView;
            private TextView textView;

            public PullZoomItemHolder(@NonNull ViewGroup parent) {
                this(LayoutInflater.from(getActivity()).inflate(R.layout.item_pull_zoom_item, parent, false));
            }

            public PullZoomItemHolder(@NonNull View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                textView = (TextView) view.findViewById(R.id.text_view);
            }

            @Override
            public void bind(Integer item, int position) {
                imageView.setImageResource(item);
                textView.setText(getString(R.string.image_name, position));
            }
        }
    }
}
