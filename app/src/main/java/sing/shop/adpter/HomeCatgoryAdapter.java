package sing.shop.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sing.shop.R;
import sing.shop.bean.Campaign;
import sing.shop.bean.HomeCampaign;

public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder> {

    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;
    private LayoutInflater inflater;

    List<HomeCampaign> list;
    Context context;
    private OnCampaignClickListener campaignClickListener;

    public void setCampaignClickListener(OnCampaignClickListener campaignClickListener) {
        this.campaignClickListener = campaignClickListener;
    }

    public HomeCatgoryAdapter(List<HomeCampaign> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_L) {
            return new ViewHolder(inflater.inflate(R.layout.template_home_cardview, parent, false));
        }

        return new ViewHolder(inflater.inflate(R.layout.template_home_cardview2, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCampaign campaign = list.get(position);

        holder.textTitle.setText(campaign.getTitle());
        Picasso.with(context).load(campaign.getCpOne().getImgUrl()).into(holder.imgViewBig);
        Picasso.with(context).load(campaign.getCpTwo().getImgUrl()).into(holder.imgViewSmallTop);
        Picasso.with(context).load(campaign.getCpThree().getImgUrl()).into(holder.imgViewSmallBottom);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        }
        return VIEW_TYPE_L;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textTitle;
        ImageView imgViewBig;
        ImageView imgViewSmallTop;
        ImageView imgViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imgViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imgViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imgViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            itemView.findViewById(R.id.view_imgview_big).setOnClickListener(this);
            itemView.findViewById(R.id.view_imgview_small_top).setOnClickListener(this);
            itemView.findViewById(R.id.view_imgview_small_bottom).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeCampaign campaign = list.get(getLayoutPosition());

            if (campaignClickListener != null){
                switch (v.getId()){
                    case R.id.view_imgview_big:
                        campaignClickListener.onClick(v,campaign.getCpOne());
                        break;
                    case R.id.view_imgview_small_top:
                        campaignClickListener.onClick(v,campaign.getCpTwo());
                        break;
                    case R.id.view_imgview_small_bottom:
                        campaignClickListener.onClick(v,campaign.getCpThree());
                        break;
                }
            }
        }
    }

    public interface OnCampaignClickListener{
       void  onClick(View v,Campaign campaign);
    }
}

