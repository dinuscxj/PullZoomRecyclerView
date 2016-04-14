package app.dinus.com.pullzoomrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.dinus.com.pullzoomrecyclerview.recyclerview.PullZoomBaseView;
import app.dinus.com.pullzoomrecyclerview.recyclerview.PullZoomRecyclerView;
import app.dinus.com.pullzoomrecyclerview.recyclerview.RecyclerListAdapter;


public class PullZoomFooterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_zoom_header);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PullZoomHeaderFragment.newInstance())
                .commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PullZoomHeaderFragment extends Fragment {

        private PullZoomRecyclerView mRecyclerView;

        public PullZoomHeaderFragment() {
        }

        public static PullZoomHeaderFragment newInstance(){
            return new PullZoomHeaderFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_pull_zoom_header, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mRecyclerView = (PullZoomRecyclerView) view.findViewById(R.id.recycler_view);
            mRecyclerView.setAdapter(new PullZoomAdapter(createPullZoomData()));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        private List<Integer> createPullZoomData(){
            List<Integer> pullZoomData = new ArrayList<>();
            Collections.addAll(pullZoomData,
                    new Integer[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
                            R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8,
                            R.drawable.p9, R.drawable.p10});

            return pullZoomData;
        }

        //you can also exends RecyclerView.Adapter
        private class PullZoomAdapter extends RecyclerListAdapter{
            public PullZoomAdapter() {
                addViewType(Integer.class, new ViewHolderFactory<PullZoomItemHolder>() {
                    @Override
                    public PullZoomItemHolder onCreateViewHolder(ViewGroup parent) {
                        return new PullZoomItemHolder(parent);
                    }
                });

                addViewType(TYPE_FOOTER, new ViewHolderFactory<PullZoomFooterHolder>() {
                    @Override
                    public PullZoomFooterHolder onCreateViewHolder(ViewGroup parent) {
                        return new PullZoomFooterHolder(parent);
                    }
                });
            }

            private List<Integer> listData ;

            public PullZoomAdapter(List<Integer> listData) {
                this();
                this.listData = listData;
            }

            @Override
            public Object getItem(int position) {
                if (position == listData.size()){
                    return ITEM_FOOTER;
                }

                return listData.get(position);
            }

            @Override
            public int getItemCount() {
                return listData.size() + 1;
            }

            private class PullZoomFooterHolder extends ViewHolder<Object>{
                private View zoomView ;
                private ViewGroup zoomHeaderContainer;
                private TextView zoomStatusView;
                private int scrollValueForSkip;
                private boolean isMeetSkip;

                public PullZoomFooterHolder(@NonNull ViewGroup parent) {
                    this(LayoutInflater.from(getActivity()).inflate(R.layout.item_pull_zoom_footer, parent, false));
                }
                public PullZoomFooterHolder(@NonNull View view) {
                    super(view);
                    zoomView = view.findViewById(R.id.zoom_image_view);
                    zoomHeaderContainer = (ViewGroup) view.findViewById(R.id.zoom_header_container);
                    zoomStatusView = (TextView) view.findViewById(R.id.skip_pull_zoom_header);

                    scrollValueForSkip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                }

                @Override
                public void bind(Object item, int position) {
                    mRecyclerView.setZoomView(zoomView);
                    mRecyclerView.setModel(PullZoomBaseView.ZOOM_FOOTER);
                    mRecyclerView.setHeaderContainer(zoomHeaderContainer);
                    mRecyclerView.setOnPullZoomListener(new PullZoomBaseView.OnPullZoomListener() {
                        @Override
                        public void onPullZooming(float newScrollValue) {
                            if (newScrollValue > scrollValueForSkip && !isMeetSkip){
                                isMeetSkip = true;
                                zoomStatusView.setSelected(true);
                                zoomStatusView.setText(getString(R.string.loosen_the_jump));
                            } else if (newScrollValue <= scrollValueForSkip && isMeetSkip){
                                isMeetSkip = false;
                                zoomStatusView.setSelected(false);
                                zoomStatusView.setText(getString(R.string.pull_up_jump));
                            }
                        }

                        @Override
                        public void onPullStart() {

                        }

                        @Override
                        public void onPullZoomEnd(float newScrollValue) {
                            if (newScrollValue > scrollValueForSkip){
                                zoomStatusView.setSelected(false);
                                zoomStatusView.setText(getString(R.string.pull_up_jump));

                                Intent intent = new Intent(getActivity(), PullZoomHeaderActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            private class PullZoomItemHolder extends ViewHolder<Integer>{
                private ImageView imageView ;
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
}
