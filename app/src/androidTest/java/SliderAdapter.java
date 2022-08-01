import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a1tapevents.R;
import com.example.a1tapevents.SliderData;

import java.util.ArrayList;
import java.util.List;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;


//public class SliderAdapter {


    public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

        // list for storing urls of images.
        private final List<SliderData> mSliderItems;

        // Constructor
        public SliderAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
            this.mSliderItems = sliderDataArrayList;
        }

        @Override
        public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
            return new SliderAdapterViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

            final SliderData sliderItem = mSliderItems.get(position);

            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImgUrl())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);
        }
        @Override
        public int getCount() {
            return mSliderItems.size();
        }

        static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
            View itemView;
            ImageView imageViewBackground;

            public SliderAdapterViewHolder(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.myimage);
                this.itemView = itemView;
            }
        }
    }

}
