package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.TileAdapter;

/**
 * Created by munirul.hoque on 11/15/2016.
 */

public class FourFrgment extends Fragment {
    public LinearLayout contactline1, contactline2, facebookarea;
    public ImageView icon1, icon2, icon3, icon4;
    public CardView card1, card2, card3, card4;
    public RecyclerView recyclerView;
    private View view;
    private RecyclerView.Adapter mAdapter;
    StaggeredGridLayoutManager layoutManager;
    public FourFrgment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_four,container,false);
       /* contactline1 = (LinearLayout)view.findViewById(R.id.contactline1);
        contactline2 = (LinearLayout)view.findViewById(R.id.contactline2);
        facebookarea = (LinearLayout)view.findViewById(R.id.facebookarea); */
      //  recyclerView = (RecyclerView)view.findViewById(R.id.contact_recycler);
        icon1 = (ImageView)view.findViewById(R.id.icon1);
        icon2 = (ImageView)view.findViewById(R.id.icon2);
        icon3 = (ImageView)view.findViewById(R.id.icon3);
        icon4 = (ImageView)view.findViewById(R.id.icon4);

        card1 = (CardView)view.findViewById(R.id.card1);
        card2 = (CardView)view.findViewById(R.id.card2);
        card3 = (CardView)view.findViewById(R.id.card3);
        card4 = (CardView)view.findViewById(R.id.card4);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
    }

    @Override
    public void onResume() {
        super.onResume();
        icon1.setImageDrawable(drawIcon(alphbetSelect("Call")));
        icon2.setImageDrawable(drawIcon(alphbetSelect("Call")));
        icon3.setImageDrawable(drawIcon(alphbetSelect("Facebook")));
        icon4.setImageDrawable(drawIcon(alphbetSelect("WWW")));

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:16272"));
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:09666700666"));
                startActivity(intent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // launchFacebook();
                String url = "https://www.facebook.com/symphonymobile/";
                PackageManager pkm = getContext().getPackageManager();
                Intent i = newFacebookIntent(pkm,url);
                startActivity(i);

            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.symphony-mobile.com";
               // Intent i = new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(url));
                //startActivity(i);
                Intent intent = new Intent(MySymphonyApp.getContext(), NewsWebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("targetUrl", url);
                intent.putExtra("SYSTRAY","systray");

                MySymphonyApp.getContext().startActivity(intent);


            }
        });

    }

    public final void launchFacebook() {
        final String urlFb = "fb://page/"+"@presagibd";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If a Facebook app is installed, use it. Otherwise, launch
        // a browser
        final PackageManager packageManager = getContext().getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/"+"@presagibd";
            intent.setData(Uri.parse(urlBrowser));
        }

        startActivity(intent);
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public TextDrawable drawIcon(String str)
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(40) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(str, color);
        return textDrawable;
    }

    public String alphbetSelect(String str)
    {
        String firstAlphabet;
        String ccName = str.trim();
        if(ccName != null)
        {
            firstAlphabet = ccName.substring(0,1);
        }
        else
        {
            firstAlphabet = "A";
        }

        return  firstAlphabet;
    }

}
