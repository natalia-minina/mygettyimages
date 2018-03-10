package com.example.mygettyimages.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mygettyimages.R;
import com.example.mygettyimages.activities.BaseActivity;
import com.example.mygettyimages.interfaces.SearchInterface;
import com.example.mygettyimages.models.GettyImage;
import com.example.mygettyimages.utils.GraphicUtils;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ImagesAdapter extends RealmRecyclerViewAdapter<GettyImage, ImagesAdapter.ImageViewHolder> implements SearchInterface {

    private List<GettyImage> items;
    private BaseActivity context;
    private int imageW;
    private Filter filter = new SearchFilter();
    private String lastQuery = null;

    public ImagesAdapter(BaseActivity context, int columnWidth, OrderedRealmCollection<GettyImage> items) {
        super(items, true);
        this.items = items;
        this.context = context;

        imageW = columnWidth;
    }

    @Override
    public synchronized void query(String query) {
        filter.filter(query);
    }

    @Override
    public String getLastQuery() {
        return lastQuery;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater
                .from(context)
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final GettyImage item = items.get(position);
        String image = "";
        String name = "";
        if (item != null) {
            image = item.getUrl();
            if (!TextUtils.isEmpty(item.getName())) {
                name = item.getName();
            }
        }
        holder.photoNameTV.setText(name);

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) holder.photoIV.getLayoutParams();

        float ratio;
        if (item.getMaxDimensionsHeight() > 0 && item.getMaxDimensionsWidth() > 0) {
            ratio = (float) item.getMaxDimensionsHeight() / item.getMaxDimensionsWidth();
        } else {
            ratio = 1;
        }
        rlp.width = imageW;
        rlp.height = (int) (imageW * ratio);
        holder.photoIV.setLayoutParams(rlp);

        GraphicUtils.displayImage(image, holder.photoIV);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoIV;

        public TextView photoNameTV;

        public ImageViewHolder(View view) {
            super(view);
            photoIV = view.findViewById(R.id.photoIV);
            photoNameTV = view.findViewById(R.id.photoNameTV);
        }
    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence query) {
            if (query == null) {
                query = "";
            }

            GettyImage image = context.getAppNetworkManager().searchGettyImage(query.toString());

            final FilterResults result = new FilterResults();
            result.values = image;
            if (image != null) {
                result.count = 1;
                lastQuery = query.toString();

            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            GettyImage item = (GettyImage) results.values;
            if (item != null) {
                context.clearFocusInSearchViewFromActionBar();
                context.getAppDBManager().addGettyImageInBackgroundThread(item);
            }

        }
    }

}