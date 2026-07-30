package com.vienetworks.originvpn.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import com.vienetworks.originvpn.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;
import android.widget.*;

public class SpinnerAdapter extends ArrayAdapter<JSONObject> {

    private int spinner_id;

    public SpinnerAdapter(Context context, int spinner_id, ArrayList<JSONObject> list) {
        super(context, R.layout.spinner_item, list);
        this.spinner_id = spinner_id;
    }

    @Override
    public JSONObject getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return view(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return view(position, convertView, parent);
    }

    private View view(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        TextView tv = v.findViewById(R.id.itemName);
        TextView extra = v.findViewById(R.id.textExtra);
        TextView info = v.findViewById(R.id.info);
        ImageView im = v.findViewById(R.id.itemImage);
        try {
            tv.setText(getItem(position).getString("Name"));
            if (spinner_id == R.id.serverSpinner) {
                getServerIcon(position, im, info, extra);
            } else if (spinner_id == R.id.payloadSpinner) {
                getPayloadIcon(position, im, extra, info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void getServerIcon(int position, ImageView im, TextView info, TextView extra) throws Exception {
        InputStream inputStream = getContext().getAssets().open("flags/" + getItem(position).getString("FLAG"));
        im.setImageDrawable(Drawable.createFromStream(inputStream, getItem(position).getString("FLAG")));
        if (inputStream != null) {
            inputStream.close();
        }

        info.setText(getItem(position).getString("sInfo"));

        String name = getItem(position).getString("sInfo").toLowerCase();

        if (name.contains("$")) {                      
            extra.setText("15K/30DAY");           
            extra.setTextColor(Color.GREEN);
            im.setImageResource(R.drawable.xgame);

        } else if (name.contains("°")) {                    
            extra.setText("15K/30DAY");          
            extra.setTextColor(Color.CYAN);
            im.setImageResource(R.drawable.netflix);

        } else if (name.contains("torrent")) {                    
            extra.setText("Private Server");          
            extra.setTextColor(Color.BLACK);
            im.setImageResource(R.drawable.torrent);

        } else if (name.contains("voip")) {                    
            extra.setText("Private Server");          
            extra.setTextColor(Color.MAGENTA);
            im.setImageResource(R.drawable.voip);

        } else if (name.contains("#")) {                    
            extra.setText("FREE");          
            extra.setTextColor(Color.RED);

        }
    }

    private void getPayloadIcon(int position, ImageView im, TextView extra, TextView info) throws Exception {
        String name = getItem(position).getString("Name").toLowerCase();
        info.setText(getItem(position).getString("Info"));
         boolean sslType = getItem(position).getBoolean("isSSL");
         if (sslType) {
         extra.setText("SSL/TLS");
         } else{
			 extra.setText("SSH/WS");
         }
         
        if (name.contains("viettel")) {
            im.setImageResource(R.drawable.ic_viettel);
        } else if (name.contains("liên quân")) {
            im.setImageResource(R.drawable.ic_lienquan);                     
        } else if (name.contains("zing mp3")) {
            im.setImageResource(R.drawable.ic_zingmp3);
		} else if (name.contains("mobifone")) {
			im.setImageResource(R.drawable.ic_mobifone);
        } else if (name.contains("mobion")) {
            im.setImageResource(R.drawable.ic_mobion);
        } else if (name.contains("vietnamobile")) {
            im.setImageResource(R.drawable.ic_vietnamobile);
        } else if (name.contains("vinaphone")) {
            im.setImageResource(R.drawable.ic_vinaphone);
        }else if(name.contains("tik tok")) {
            im.setImageResource(R.drawable.ic_tiktok);
        }else if(name.contains("dito")) {
            im.setImageResource(R.drawable.ic_dito);
        }else if(name.contains("gomo")) {
            im.setImageResource(R.drawable.ic_gomo);  
        } else if (name.contains("sts")) {
            im.setImageResource(R.drawable.ic_smart_tnt_sun);         
        }else if(name.contains("")) {
            im.setImageResource(R.drawable.ic_rocket);
            
        }
    }

}
