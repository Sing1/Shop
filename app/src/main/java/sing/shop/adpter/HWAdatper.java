package sing.shop.adpter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import sing.shop.R;
import sing.shop.bean.ShoppingCart;
import sing.shop.bean.Wares;
import sing.shop.utils.CartProvider;
import sing.shop.utils.ToastUtils;

public class HWAdatper extends SimpleAdapter<Wares> {

    private CartProvider cartProvider;

    public HWAdatper(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getButton(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartProvider = new CartProvider(context);
                cartProvider.put(convertData(wares));
                ToastUtils.show(context, "已添加到购物车");
            }
        });
    }

    public ShoppingCart convertData(Wares item){
        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
