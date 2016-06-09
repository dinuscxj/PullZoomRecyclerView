package com.dinuscxj.pullzoomrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinuscxj.pullzoom.PullZoomBaseView;

import java.util.List;


public class PullZoomFooterActivity extends PullZoomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PullZoomFooterFragment.newInstance())
                .commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PullZoomFooterFragment extends PullZoomFragment {

        public static PullZoomFooterFragment newInstance() {
            return new PullZoomFooterFragment();
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        @Override
        protected PullZoomAdapter createPullZoomAdapter(List<Integer> pullZoomData) {
            return new PullZoomFooterAdapter(pullZoomData);
        }


        private class PullZoomFooterAdapter extends PullZoomAdapter {
            public PullZoomFooterAdapter() {
                super();
                addViewType(TYPE_FOOTER, new ViewHolderFactory<PullZoomFooterHolder>() {
                    @Override
                    public PullZoomFooterHolder onCreateViewHolder(ViewGroup parent) {
                        return new PullZoomFooterHolder(parent);
                    }
                });
            }

            public PullZoomFooterAdapter(List<Integer> listData) {
                this();
                this.listData = listData;
            }

            @Override
            public Object getItem(int position) {
                if (position == listData.size()) {
                    return ITEM_FOOTER;
                }

                return listData.get(position);
            }

            @Override
            public int getItemCount() {
                return listData.size() + 1;
            }

            private class PullZoomFooterHolder extends ViewHolder<Object> {
                private View zoomView;
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
                            if (newScrollValue > scrollValueForSkip && !isMeetSkip) {
                                isMeetSkip = true;
                                zoomStatusView.setSelected(true);
                                zoomStatusView.setText(getString(R.string.loosen_the_jump));
                            } else if (newScrollValue <= scrollValueForSkip && isMeetSkip) {
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
                            if (newScrollValue > scrollValueForSkip) {
                                zoomStatusView.setSelected(false);
                                zoomStatusView.setText(getString(R.string.pull_up_jump));

                                Intent intent = new Intent(getActivity(), PullZoomHeaderActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        }
    }
}
