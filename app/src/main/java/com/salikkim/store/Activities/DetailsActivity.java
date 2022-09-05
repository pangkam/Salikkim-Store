package com.salikkim.store.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import com.salikkim.store.Adapters.ViewPagerAdapter;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.Models.Colors;
import com.salikkim.store.R;
import com.salikkim.store.databinding.ActivityDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private String img_url = "https://hawtie.000webhostapp.com/salikkim_store/getimages.php";
    private String size_url = "https://hawtie.000webhostapp.com/salikkim_store/getsize.php";
    private String addr_url = "https://hawtie.000webhostapp.com/salikkim_store/getavailableaddresses.php";
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    private String name, discount, color, product_desc, available_addresses, seller_name, seller_id;
    private int id, price, salePrice;
    private NativeAd nativeAd;
    private List<String> size_string;
    private List<String> images_string;
    private List<String> addresses_string;
    private ViewPagerAdapter viewPagerAdapter;
    private ActivityDetailsBinding detailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = detailsBinding.getRoot();
        setContentView(view);
        detailsBinding.toolbarDetailsActivity.setTitle("");
        setSupportActionBar(detailsBinding.toolbarDetailsActivity);
        detailsBinding.toolbarDetailsActivity.setNavigationIcon(R.drawable.ic_arrow_back);
        detailsBinding.toolbarDetailsActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        if (savedInstanceState == null) {
            id = getIntent().getExtras().getInt("Product_id");
            name = getIntent().getExtras().getString("Name");
            seller_id = getIntent().getExtras().getString("Seller_id");
            seller_name = getIntent().getExtras().getString("Seller_name");
            price = getIntent().getExtras().getInt("Price");
            salePrice = getIntent().getExtras().getInt("Sale_price");
            discount = getIntent().getExtras().getString("Discount");
            color = getIntent().getExtras().getString("Color");
            product_desc = getIntent().getExtras().getString("Product_desc");
            available_addresses = getIntent().getExtras().getString("Available_addresses");
            initViews();

        }
        getImages();
        getAddresses();
        getSize();
        createNativeAd();
    }

    private void getAddresses() {
        String[] key = new String[]{"id"};
        String[] value = new String[]{available_addresses};
        addresses_string = new ArrayList<>();
        new VolleyRequest(key, value, addr_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        addresses_string.add(jsonObject.getString("Available_addresses"));
                        detailsBinding.availableAddressesDetail.append((i + 1) + "." + addresses_string.get(i));
                        detailsBinding.availableAddressesDetail.append("\n");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getImages() {
        String[] key = new String[]{"id"};
        String[] value = new String[]{String.valueOf(id)};
        images_string = new ArrayList<>();
        new VolleyRequest(key, value, img_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        images_string.add(jsonObject.getString("Url"));
                    }
                    viewPagerAdapter = new ViewPagerAdapter(images_string, DetailsActivity.this);
                    detailsBinding.viewPagerDetails.setAdapter(viewPagerAdapter);
                    detailsBinding.wormDotsIndicator.setViewPager2(detailsBinding.viewPagerDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSize() {
        String[] key = new String[]{"id"};
        String[] value = new String[]{String.valueOf(id)};
        size_string = new ArrayList<>();
        new VolleyRequest(key, value, size_url, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONArray jsonArray = new JSONArray(object);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        size_string.add(jsonObject.getString("Size"));
                        Chip size = new Chip(DetailsActivity.this);
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(DetailsActivity.this, null, 0, R.style.CustomChipChoice);
                        size.setChipDrawable(chipDrawable);
                        @SuppressLint("ResourceType") ColorStateList c = ContextCompat.getColorStateList(DetailsActivity.this, R.drawable.chip_selection_text);
                        size.setTextColor(c);
                        size.setPadding(10, 10, 10, 10);
                        size.setText(size_string.get(i));
                        detailsBinding.chipGroupSize.addView(size);
                    }
                    if (size_string.size() == 0) {
                        detailsBinding.outOfStock.setVisibility(View.VISIBLE);
                        detailsBinding.addCart.setVisibility(View.GONE);
                        detailsBinding.addWistlists.setVisibility(View.GONE);
                    } else {
                        detailsBinding.outOfStock.setVisibility(View.GONE);
                        detailsBinding.addCart.setVisibility(View.VISIBLE);
                        detailsBinding.addWistlists.setVisibility(View.VISIBLE);
                    }
                    detailsBinding.nestedScrollDetail.setVisibility(View.VISIBLE);
                    detailsBinding.shimmerDetail.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (size_string.size() >= 0)
            detailsBinding.chipGroupSize.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup group, int checkedId) {
                    for (int i = 0; i < detailsBinding.chipGroupSize.getChildCount(); i++) {
                        Chip chip = (Chip) detailsBinding.chipGroupSize.getChildAt(i);
                        if (chip.isChecked()) {
                            //this chip is selected.....
                        }
                    }
                }
            });
    }

    private void SnackBar(String s) {
        Snackbar.make(this.findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void initViews() {
        detailsBinding.titleDetail.setText(name);
        detailsBinding.priceDetail.setText(getString(R.string.Rs) + price);
        detailsBinding.priceDetail.setPaintFlags(detailsBinding.priceDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        detailsBinding.salePriceDetail.setText(getString(R.string.Rs) + salePrice);
        detailsBinding.offerDetail.setText(discount);
        detailsBinding.chipColorDetails.setText(color);
        detailsBinding.detailDetail.setText(product_desc.replace(",", "\n"));
        detailsBinding.sellerDetail.setText(seller_name);
        detailsBinding.detailChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + seller_id + "&text="));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        List<Colors> colorsList = new ArrayList<>();
        colorsList.add(new Colors("Aquamarine", "#7FFD4"));
        for (int i = 0; i < colorsList.size(); i++) {
            if (colorsList.get(i).getColor().equals(color))
                detailsBinding.chipColorDetails.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(colorsList.get(i).getValue())));
        }
    }

    private void createNativeAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        if (DetailsActivity.this.nativeAd != null) {
                            DetailsActivity.this.nativeAd.destroy();
                        }
                        DetailsActivity.this.nativeAd = nativeAd;
                        FrameLayout frameLayout = findViewById(R.id.adPlaceHolder_details);
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_native, null);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Toast.makeText(DetailsActivity.this, loadAdError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.media_view));

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }
}