package sing.shop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import java.util.List;

import sing.shop.Contants;
import sing.shop.R;
import sing.shop.adpter.HomeCatgoryAdapter;
import sing.shop.adpter.decoration.DividerItemDecoration;
import sing.shop.bean.Banner;
import sing.shop.bean.Campaign;
import sing.shop.bean.HomeCampaign;
import sing.shop.http.OkHttpHelper;
import sing.shop.http.SpotsCallBack;

public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;
    private HomeCatgoryAdapter mAdatper;
    private static final String TAG = "HomeFragment";
    private Gson mGson = new Gson();
    private List<Banner> mBannners;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        requestImages();
        initRecyclerView(view);

        return view;
    }

    private void requestImages(){
        httpHelper.get(Contants.BANNER + "?type=1", new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> bannners) {
                mBannners = bannners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        httpHelper.get(Contants.CAMPAIGN_HOME, new SpotsCallBack<List<HomeCampaign>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initData(List<HomeCampaign> data) {
        mAdatper = new HomeCatgoryAdapter(data,getActivity());

        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        mAdatper.setCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View v, Campaign campaign) {
                Toast.makeText(getActivity(), campaign.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSlider() {

        if (mBannners != null){
            for(Banner bannner : mBannners){
                TextSliderView textSliderView= new TextSliderView(this.getActivity());
                textSliderView.image(bannner.getImgUrl());
                textSliderView.description(bannner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}