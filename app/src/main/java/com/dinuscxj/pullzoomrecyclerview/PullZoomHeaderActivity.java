package com.dinuscxj.pullzoomrecyclerview;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class PullZoomHeaderActivity extends PullZoomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PullZoomHeaderFragment.newInstance())
                .commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PullZoomHeaderFragment extends PullZoomFragment {

        public static PullZoomHeaderFragment newInstance() {
            return new PullZoomHeaderFragment();
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2) {
                {
                    setSpanSizeLookup(new SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return position == 0 ? 2 : 1;
                        }
                    });
                }
            });
        }

        @Override
        protected PullZoomAdapter createPullZoomAdapter(List<Integer> pullZoomData) {
            return new PullZoomHeaderAdapter(pullZoomData);
        }

        private class PullZoomHeaderAdapter extends PullZoomAdapter {
            public PullZoomHeaderAdapter() {
                super();
                addViewType(TYPE_HEADER, new ViewHolderFactory<PullZoomHeaderHolder>() {
                    @Override
                    public PullZoomHeaderHolder onCreateViewHolder(ViewGroup parent) {
                        return new PullZoomHeaderHolder(parent);
                    }
                });
            }

            public PullZoomHeaderAdapter(List<Integer> listData) {
                this();
                this.listData = listData;
            }

            @Override
            public Object getItem(int position) {
                if (position == 0) {
                    return ITEM_HEADER;
                }
                return listData.get(--position);
            }

            @Override
            public int getItemCount() {
                return listData.size() + 1;
            }

            private class PullZoomHeaderHolder extends RecyclerListAdapter.ViewHolder<Object> {
                private ImageView zoomView;
                private ViewGroup zoomHeaderContainer;

                public PullZoomHeaderHolder(@NonNull ViewGroup parent) {
                    this(LayoutInflater.from(getActivity()).inflate(R.layout.item_pull_zoom_header, parent, false));
                }

                public PullZoomHeaderHolder(@NonNull View view) {
                    super(view);
                    zoomView = (ImageView) view.findViewById(R.id.zoom_image_view);
                    zoomHeaderContainer = (ViewGroup) view.findViewById(R.id.zoom_header_container);
                }

                @Override
                public void bind(Object item, int position) {
                    mRecyclerView.setZoomView(zoomView);
                    mRecyclerView.setHeaderContainer(zoomHeaderContainer);
                }
            }
        }
    }
}
