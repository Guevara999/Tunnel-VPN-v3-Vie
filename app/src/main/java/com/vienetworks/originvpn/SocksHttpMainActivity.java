package com.vienetworks.originvpn;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import cn.pedant.SweetAlert.widget.*;
import com.vienetworks.originvpn.*;
import com.vienetworks.originvpn.activities.*;
import com.vienetworks.originvpn.adapter.*;
import com.vienetworks.originvpn.fragments.*;
import com.vienetworks.originvpn.util.*;
import com.romzkie.ultrasshservice.*;
import com.romzkie.ultrasshservice.StatisticGraphData.*;
import com.romzkie.ultrasshservice.config.*;
import com.romzkie.ultrasshservice.logger.*;
import com.romzkie.ultrasshservice.tunnel.*;
import com.romzkie.ultrasshservice.util.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;
import com.sdsmdg.tastytoast.TastyToast;
import com.vienetworks.originvpn.view.TrafficSpeedMeasurer;
import com.vienetworks.originvpn.view.ITrafficSpeedListener;
import android.icu.text.SimpleDateFormat;
import android.view.View.OnClickListener;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import com.vienetworks.originvpn.R;
import com.vienetworks.originvpn.adapter.SpinnerAdapter;

/**
 * Activity Principal
 * @author SlipkHunter
 */

public class SocksHttpMainActivity extends BaseActivity
	implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,
CompoundButton.OnCheckedChangeListener, SkStatus.StateListener,PayloadGenerator.OnDismissListener
{
	private static final String TAG = SocksHttpMainActivity.class.getSimpleName();
	private static final String UPDATE_VIEWS = "MainUpdate";
    public static final String OPEN_LOGS = "com.vienetworks.originvpn:openLogs";
 
    public static int PICK_FILE = 1;
    private SweetAlertDialog nops;
    private LinearLayout mLinearLayoutHeader;
    private LinearLayout mLinearLayout;
    private LogsAdapter mLogAdapter;
    private ImageView mArrow1;
    private RecyclerView logList;
    TextView MsgAdmin;
    TextView Date;
    private ViewPager vp;
    private FloatingActionButton deleteLogs;
    private TabLayout tabs;
    private TextView status;
    private static final boolean SHOW_SPEED_IN_BITS = false;
    private TrafficSpeedMeasurer mTrafficSpeedMeasurer;
    
	private Switch customSetUP;

	private Spinner sportSetup;

	private Spinner portAuto;

	private Spinner methodSpinner;

	private DrawerPanelMain mDrawerPanel;
	
	private Settings mConfig;
	private Toolbar toolbar_main;
	private Handler mHandler;
	
	private LinearLayout mainLayout;
	private LinearLayout loginLayout;
	private LinearLayout proxyInputLayout;
	private LinearLayout ssl_layout;
	private TextView proxyText;
	private RadioGroup metodoConexaoRadio;
	private LinearLayout payloadLayout;
	private TextInputEditText sslEdit;
	private TextInputEditText payloadEdit;
	private SwitchCompat customPayloadSwitch;
	private Button starterButton;
	
	private ImageButton inputPwShowPass;
	private TextInputEditText inputPwUser;
	private TextInputEditText inputPwPass;
	
	private LinearLayout configMsgLayout;
	private TextView configMsgText;

	private ConfigUtil config;

	private Spinner serverSpinner;
	private Spinner payloadSpinner;
    private static final String[] tabTitle = {"Trang Chủ","Nhật Ký Kết Nối"};
	private SpinnerAdapter serverAdapter;
	private SpinnerAdapter payloadAdapter;

	private ArrayList<JSONObject> serverList;
	private ArrayList<JSONObject> payloadList;
	
	String[] countryNames={"Tuỳ Chỉnh Payload","Tuỳ Chỉnh SNI"};
    int flags[] = {R.drawable.tweaks, R.drawable.tweaks};

	private CustomAdapter customAdapter;

	private TextView bytes_in, bytes_out;

	private SweetAlertDialog pDialog;
	


	
	private SweetAlertDialog mDialog;
	
	private AlertDialog dialog;

	

    String MSGadmn = ("https://marspaste.com/raw/hsbbrgi8ji");
       
  
    
	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		mHandler = new Handler();
		mConfig = new Settings(this);
		mDrawerPanel = new DrawerPanelMain(this);
		new SMSuPdater(this);
		new TorrentDetection(this, torrentList).init();
                
        
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // portrait only
		
		SharedPreferences prefs = getSharedPreferences(SocksHttpApp.PREFS_GERAL, Context.MODE_PRIVATE);

		boolean showFirstTime = prefs.getBoolean("connect_first_time", true);
		int lastVersion = prefs.getInt("last_version", 0);

		// se primeira vez
		if (showFirstTime)
        {
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean("connect_first_time", false);
            pEdit.apply();

			Settings.setDefaultConfig(this);

			
        }

		try {
			int idAtual = ConfigParser.getBuildId(this);

			if (lastVersion < idAtual) {
				SharedPreferences.Editor pEdit = prefs.edit();
				pEdit.putInt("last_version", idAtual);
				pEdit.apply();

				// se estiver atualizando
				if (!showFirstTime) {
					if (lastVersion <= 12) {
						Settings.setDefaultConfig(this);
						Settings.clearSettings(this);

						Toast.makeText(this, "Cài Đặt Đã Được Làm Sạch Để Tránh Lỗi",
							Toast.LENGTH_LONG).show();
					}
				}

			}
		} catch(IOException e) {}
		
		
		// set layout
		doLayout();
                

            mArrow1 = (ImageView)findViewById(R.id.mArrowDown);
            mArrow1.animate().setDuration(500).rotation(180);
            mLinearLayout = (LinearLayout) findViewById(R.id.expandable);
            mLinearLayout.setVisibility(View.GONE);
            mLinearLayoutHeader = (LinearLayout) findViewById(R.id.headerz);
            mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mLinearLayout.getVisibility()==View.GONE){
                            expand();
                        }else{
                            collapse();
                        }
                    }
                });
            

		// verifica se existe algum problema
		SkProtect.CharlieProtect();

            PackageInfo pinfo = Utils.getAppInfo(this);
            if (pinfo != null) {
                String version_nome = pinfo.versionName;
                int version_code = pinfo.versionCode;
                String header_text = String.format("Phiên Bản Ứng Dụng: v%s ", version_nome, version_code);

                TextView app_info_text = (TextView) findViewById(R.id.app_info);
                app_info_text.setText(header_text);
     
            TextView version = (TextView)findViewById (R.id.config_version_info);
				version.setText("Phiên Bản Cấu Hình"+": v" +  config.getVersion());
                
		// recebe local dados
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_VIEWS);
		filter.addAction(OPEN_LOGS);
		
		LocalBroadcastManager.getInstance(this)
			.registerReceiver(mActivityReceiver, filter);
			
		doUpdateLayout();
	
        
	}
    }
    /** Interstitial Ads **/

    
    
	private String[] torrentList = new String[] {
		"com.tdo.showbox",
		"com.nitroxenon.terrarium",
		"com.pklbox.translatorspro",
		"com.xunlei.downloadprovider",
		"com.epic.app.iTorrent",
		"hu.bute.daai.amorg.drtorrent",
		"com.mobilityflow.torrent.prof",
		"com.brute.torrentolite",
		"com.nebula.swift",
		"tv.bitx.media",
		"com.DroiDownloader",
		"bitking.torrent.downloader",
		"org.transdroid.lite",
		"com.mobilityflow.tvp",
		"com.gabordemko.torrnado",
		"com.frostwire.android",
		"com.vuze.android.remote",
		"com.akingi.torrent",
		"com.utorrent.web",
		"com.paolod.torrentsearch2",
		"com.delphicoder.flud.paid",
		"com.teeonsoft.ztorrent",
		"megabyte.tdm",
		"com.bittorrent.client.pro",
		"com.mobilityflow.torrent",
		"com.utorrent.client",
		"com.utorrent.client.pro",
		"com.bittorrent.client",
		"torrent",
		"com.AndroidA.DroiDownloader",
		"com.indris.yifytorrents",
		"com.delphicoder.flud",
		"com.oidapps.bittorrent",
		"dwleee.torrentsearch",
		"com.vuze.torrent.downloader",
		"megabyte.dm",
		"com.fgrouptech.kickasstorrents",
		"com.jrummyapps.rootbrowser.classic",
		"com.bittorrent.client",
		"hu.tagsoft.ttorrent.lite",
		"co.we.torrent",
        };
	
	
	
    private ITrafficSpeedListener mStreamSpeedListener = new ITrafficSpeedListener() {

        @Override
        public void onTrafficSpeedMeasured(final double upStream, final double downStream) {
            runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String upStreamSpeed = Utils.parseSpeed(upStream, SHOW_SPEED_IN_BITS);
                        String downStreamSpeed = Utils.parseSpeed(downStream, SHOW_SPEED_IN_BITS);
                        bytes_in.setText("" + downStreamSpeed);
                        bytes_out.setText(upStreamSpeed +"");
                    }
                });
        }
    };
        
        
	/**
	 * Layout
	 */
	 
	private void doLayout() {
            setContentView(R.layout.abc_search_dropdown_item_icons_2line1);

		toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
            status= (TextView) findViewById(R.id.status1);
		mDrawerPanel.setDrawer(toolbar_main);
		setSupportActionBar(toolbar_main);
		TunnelManagerThread.a(this);
		// set ADS
		
            MsgAdmin = (TextView) findViewById(R.id.admin_msg);
                
		mainLayout = (LinearLayout) findViewById(R.id.activity_mainLinearLayout);
		loginLayout = (LinearLayout) findViewById(R.id.activity_mainInputPasswordLayout);
		starterButton = (Button) findViewById(R.id.activity_starterButtonMain);

		inputPwUser = (TextInputEditText) findViewById(R.id.activity_mainInputPasswordUserEdit);
		inputPwPass = (TextInputEditText) findViewById(R.id.activity_mainInputPasswordPassEdit);

		inputPwShowPass = (ImageButton) findViewById(R.id.activity_mainInputShowPassImageButton);

            Date = (TextView) findViewById(R.id.txtDate);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("hh:mm, E", Locale.getDefault());    
            String date = df.format(c.getTime());
            Date.setText(date);
            

		proxyInputLayout = (LinearLayout) findViewById(R.id.activity_mainInputProxyLayout);
		proxyText = (TextView) findViewById(R.id.activity_mainProxyText);
      
        final SharedPreferences prefs = mConfig.getPrefsPrivate();
        SharedPreferences.Editor edit = prefs.edit();
		final SharedPreferences sPrefs = mConfig.getPrefsPrivate();
        sPrefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
		sPrefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();
        config = new ConfigUtil(this);
		serverSpinner = (Spinner) findViewById(R.id.serverSpinner);
		payloadSpinner = (Spinner) findViewById(R.id.payloadSpinner);
      
		serverList = new ArrayList<>();
		payloadList = new ArrayList<>();

		serverAdapter = new SpinnerAdapter(this, R.id.serverSpinner, serverList);
		payloadAdapter = new SpinnerAdapter(this, R.id.payloadSpinner, payloadList);
		
		serverSpinner.setAdapter(serverAdapter);
		payloadSpinner.setAdapter(payloadAdapter);

		loadServer();
		loadNetworks();
		updateConfig(true);

		
		metodoConexaoRadio = (RadioGroup) findViewById(R.id.activity_mainMetodoConexaoRadio);
		customPayloadSwitch = (SwitchCompat) findViewById(R.id.activity_mainCustomPayloadSwitch);

		starterButton.setOnClickListener(this);
		proxyInputLayout.setOnClickListener(this);

		payloadLayout = (LinearLayout) findViewById(R.id.activity_mainInputPayloadLinearLayout);
		payloadEdit = (TextInputEditText) findViewById(R.id.activity_mainInputPayloadEditText);
		
		ssl_layout = (LinearLayout) findViewById(R.id.activity_ssl_layout);
		sslEdit = (TextInputEditText) findViewById(R.id.activity_sni_edit);
		
		bytes_in = (TextView) findViewById (R.id.bytes_in);
		bytes_out = (TextView) findViewById (R.id.bytes_out);
                mTrafficSpeedMeasurer = new TrafficSpeedMeasurer(TrafficSpeedMeasurer.TrafficType.ALL);
                mTrafficSpeedMeasurer.startMeasuring();
                
		configMsgLayout = (LinearLayout) findViewById(R.id.activity_mainMensagemConfigLinearLayout);
		configMsgText = (TextView) findViewById(R.id.activity_mainMensagemConfigTextView);
		portAuto = (Spinner) findViewById(R.id.portAuto);
		List<String> Listportauto = new ArrayList<String>();
		Listportauto.add("Tự Động");
		ArrayAdapter<String> Adptorportaut = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Listportauto);


		Adptorportaut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		portAuto.setAdapter(Adptorportaut);


		portAuto.setSelection(sPrefs.getInt("PortAuto", 0));



		portAuto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int position, long p4)
				{

					try
					{
						sPrefs.edit().putInt("PortAuto", position).apply();


						if(position == 0) {

							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "").apply();
						}

						doUpdateLayout();
					}
					catch (Exception e)
					{}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{

				}
			});
		sportSetup = (Spinner) findViewById(R.id.portSpinner);

		List<String> Listport = new ArrayList<String>();
		Listport.add("80");
		Listport.add("443");
		Listport.add("3128");
		Listport.add("8080");
		Listport.add("8081");
		Listport.add("8789");
		Listport.add("8799");
		Listport.add("8888");
		Listport.add("8000");

		ArrayAdapter<String> Adptorport = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Listport);

		Adptorport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sportSetup.setAdapter(Adptorport);

		sportSetup.setSelection(sPrefs.getInt("Port", 0));

		sportSetup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int position, long p4)
				{

					try
					{
						sPrefs.edit().putInt("Port", position).apply();


						if(position == 0) {

							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "80").apply();

						}else if(position == 1){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "443").apply();		


						}else if(position == 2){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "3128").apply();		
						}else if(position == 3){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "8080").apply();		

						}else if(position == 4){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "8081").apply();		

						}else if(position == 5){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "8789").apply();		


						}else if(position == 6){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "8799").apply();	


						}else if(position == 7){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "8888").apply();		



						}else if(position == 8){



							sPrefs.edit().putString(Settings.PROXY_PORTA_KEY, "8000").apply();		


						}

						doUpdateLayout();
					}
					catch (Exception e)
					{}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{

				}
			});

		methodSpinner = (Spinner) findViewById(R.id.methodSpinner);

		customAdapter = new CustomAdapter(this,flags,countryNames);

		methodSpinner.setAdapter(customAdapter);

		methodSpinner.setSelection(sPrefs.getInt("method", 0));

		methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int position, long p4)
				{

					try
					{

						sPrefs.edit().putInt("method", position).apply();


						sPrefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();


						if (position == 0){ // SSH

							SharedPreferences prefs = mConfig.getPrefsPrivate();
							payloadEdit.setText(prefs.getString("CustomPayload", ""));
							
							SharedPreferences.Editor edit = prefs.edit();
							edit.putInt("TunneType", 1).apply();
							
							setupSSH();
							
							payloadLayout.setVisibility(View.VISIBLE);
							ssl_layout.setVisibility(View.GONE);
							
							portAuto.setVisibility(View.GONE);
							portAuto.setClickable(false);
							portAuto.setEnabled(false);
							sportSetup.setVisibility(View.VISIBLE);
							sportSetup.setClickable(true);
							sportSetup.setEnabled(true);
				
						}else if(position == 1){ // SSL
							
							SharedPreferences prefs = mConfig.getPrefsPrivate();
							sslEdit.setText(prefs.getString("CustomSNI", ""));

							SharedPreferences.Editor edit = prefs.edit();
							edit.putInt("TunneType", 2).apply();
							
							setupSSL();
							
                            payloadLayout.setVisibility(View.GONE);
							ssl_layout.setVisibility(View.VISIBLE);
							
							portAuto.setVisibility(View.VISIBLE);
							portAuto.setClickable(true);
							portAuto.setEnabled(true);
							sportSetup.setVisibility(View.GONE);
							sportSetup.setClickable(false);
							sportSetup.setEnabled(false);

						}
						//Atualiza informações
						doUpdateLayout();
					}
					catch (Exception e)
					{}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{

				}
			});
			
		customSetUP = (Switch) findViewById(R.id.customSetup);
		customSetUP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(customSetUP.isChecked())
					{
						
						SharedPreferences prefs = mConfig.getPrefsPrivate();
						SharedPreferences.Editor edit = prefs.edit();
						edit.putInt("CustomSetup", 1).apply();
						//mPrefs.getBoolean(Settings.TETHERING_SUBNET, true);

						portAuto.setVisibility(View.GONE);
						portAuto.setClickable(false);
						portAuto.setEnabled(false);
						sportSetup.setVisibility(View.VISIBLE);
						sportSetup.setClickable(true);
						sportSetup.setEnabled(true);

						payloadSpinner.setVisibility(View.GONE);
						payloadSpinner.setClickable(false);
						payloadSpinner.setEnabled(false);
						methodSpinner.setVisibility(View.VISIBLE);
						methodSpinner.setClickable(true);
						methodSpinner.setEnabled(true);
						
						if (prefs.getInt("TunneType", 0) == 1){ // SSH
						
						payloadLayout.setVisibility(View.VISIBLE);
						ssl_layout.setVisibility(View.GONE);
						
					    setupSSH();
						
						}else{ // SSL
						
							payloadLayout.setVisibility(View.GONE);
							ssl_layout.setVisibility(View.VISIBLE);
							methodSpinner.setSelection(1);
							
							setupSSL();
							
							portAuto.setVisibility(View.VISIBLE);
							portAuto.setClickable(true);
							portAuto.setEnabled(true);
							sportSetup.setVisibility(View.GONE);
							sportSetup.setClickable(false);
							sportSetup.setEnabled(false);
							
				
						}

					}


					else {
						

						SharedPreferences prefs = mConfig.getPrefsPrivate();
						SharedPreferences.Editor edit = prefs.edit();
						edit.putInt("CustomSetup", 0).apply();

						payloadSpinner.setVisibility(View.VISIBLE);
						payloadSpinner.setClickable(true);
						payloadSpinner.setEnabled(true);
						methodSpinner.setVisibility(View.GONE);
						methodSpinner.setClickable(false);
						methodSpinner.setEnabled(false);

						portAuto.setVisibility(View.VISIBLE);
						portAuto.setClickable(true);
						portAuto.setEnabled(true);
						sportSetup.setVisibility(View.GONE);
						sportSetup.setClickable(false);
						sportSetup.setEnabled(false);
						
						payloadLayout.setVisibility(View.GONE);
						ssl_layout.setVisibility(View.GONE);


					}
					
				}		
				
			});
		
	
		// fix bugs
		if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				inputPwUser.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
				inputPwPass.setText(mConfig.getPrivString(Settings.SENHA_KEY));
			}
		}
        customPayloadSwitch.setChecked(true);
        
        edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, !true);
       
		metodoConexaoRadio.setOnCheckedChangeListener(this);
		// customPayloadSwitch.setOnCheckedChangeListener(this);
		inputPwShowPass.setOnClickListener(this);
        
        doTabs();
	}
	
	private void loading(){
		pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#33CC33"));
		pDialog.setTitleText("Kiểm Tra Cập Nhật");
		pDialog.setContentText("Vui Lòng Đợi Trong Khi Kiểm Tra...");
		pDialog.setCancelable(true);
		pDialog.show();
	}
	
	private void setupSSH(){
		
		try
		{
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();

			SharedPreferences.Editor edit = prefs.edit();
		
			int pos1 = serverSpinner.getSelectedItemPosition();
			String ssh_port = config.getServersArray().getJSONObject(pos1).getString("ServerPort");
			edit.putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();

			if (prefs.getString(Settings.PROXY_PORTA_KEY, "").isEmpty()){
				edit.putString(Settings.PROXY_PORTA_KEY, "8080").apply();
				sportSetup.setSelection(3);
			}
			
			
		}
		catch (Exception e)
		{}
	}
	
	private void setupSSL(){
		
		setupSSH(); // fix bug
			
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_SSL).apply();

		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(Settings.SERVIDOR_PORTA_KEY, "443").apply();
			
	}

	

    @Override
    public void onCheckedChanged(CompoundButton p1, boolean p2) {
    }
	
	public class DrawerPanelMain
	implements NavigationView.OnNavigationItemSelectedListener
	{
		private AppCompatActivity mActivity;

		public DrawerPanelMain(AppCompatActivity activity) {
			mActivity = activity;
		}


		private DrawerLayout drawerLayout;
		private ActionBarDrawerToggle toggle;

		public void setDrawer(Toolbar toolbar) {
			NavigationView drawerNavigationView = (NavigationView) mActivity.findViewById(R.id.drawerNavigationView);
			drawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawerLayoutMain);

			// set drawer
			toggle = new ActionBarDrawerToggle(mActivity,
											   drawerLayout, toolbar, R.string.open, R.string.cancel);

			drawerLayout.setDrawerListener(toggle);

			toggle.syncState();

			
			// set navigation view
			drawerNavigationView.setNavigationItemSelectedListener(this);
		}

		public ActionBarDrawerToggle getToogle() {
			return toggle;
		}

		public DrawerLayout getDrawerLayout() {
			return drawerLayout;
		}

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			int id = item.getItemId();

			switch(id)
			{
				case R.id.mipaygen:
					if (SkStatus.isTunnelActive()) {
						Toast.makeText(SocksHttpMainActivity.this, "Dịch Vụ VPN Đang Chạy, Hãy Dừng Nó Trước!",Toast.LENGTH_SHORT).show();

					}
					else {
						if (customSetUP.isChecked()) {
							PayloadGenerator();
						}else{
							Toast.makeText(SocksHttpMainActivity.this, "Vui Lòng Chế Độ Tùy Chỉnh Payload!",Toast.LENGTH_SHORT).show();

						}
					}
					drawerLayout.closeDrawers();
				break;
				
                            case R.id.miupdate:

                                loading();
                                updateConfig(false);
                                // drawerLayout.closeDrawers();

                                break;

                            case R.id.import_config:
                                offlineUpdate();

                                break;     
								
				
				
				
					
					
				case R.id.miPhoneConfg:
					PackageInfo app_info = Utils.getAppInfo(mActivity);
					if (app_info != null) {
						String aparelho_marca = Build.BRAND.toUpperCase();

						if (aparelho_marca.equals("SAMSUNG") || aparelho_marca.equals("HUAWEI")) {
							Toast.makeText(mActivity, R.string.error_no_supported, Toast.LENGTH_SHORT)
								.show();
						}
						else {
							try {
								Intent in = new Intent(Intent.ACTION_MAIN);
								in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								in.setClassName("com.android.settings", "com.android.settings.RadioInfo");
								mActivity.startActivity(in);
							} catch(Exception e) {
								Toast.makeText(mActivity, R.string.error_no_supported, Toast.LENGTH_SHORT)
									.show();
							}
						}
					}
					break;


				case R.id.taiphatwifi:
					String url5 = "http://play.google.com/store/apps/details?id=com.gorillasoftware.everyproxy&hl=vi&gl=US&referrer=utm_source%3Dgoogle%26utm_medium%3Dorganic%26utm_term%3Dt%E1%BA%A3i+every+proxy";
                    Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse(url5));
                    intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(Intent.createChooser(intent7, mActivity.getText(R.string.open_with)));
					break;

				case R.id.phatwifi:
					String ur6 = "android-app://com.gorillasoftware.everyproxy";
					Intent intent8 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur6));
					intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(Intent.createChooser(intent8,getText(R.string.open_with)));
					break;
					
					
					
					
				case R.id.ungho:
					String ur24 = "https://me.momo.vn/donateHaLongVu";
                    Intent intent13 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur24));
                    intent13.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(Intent.createChooser(intent13, mActivity.getText(R.string.open_with)));
					break;
					
				case R.id.dotocdomang:
					String ur25 = "https://www.speedtest.net/";
                    Intent intent14 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur25));
                    intent14.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(Intent.createChooser(intent14, mActivity.getText(R.string.open_with)));
					break;
                           
				
					
					
                            case R.id.closed_app:           
                                drawerLayout.closeDrawers();
								
				
					
                                
			}

			return true;
		}

	}
    
	public void release(){
		new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE)
			.setTitleText("Cấu Hình Hiện Tại")
			.setContentText("Phiên Bản Hiện Tại: "+config.getVersion()+"\n"+config.getNote())
			.setConfirmText("Đồng Ý")
			.show();
	}
	
    private void clearz()
    {
        nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE);
        nops.setTitleText("Bạn có chắc chắn xóa dữ liệu ứng dụng không?");
        nops.setContentText("Hệ thống sẽ xóa tất cả dữ liệu cập nhật \n và chúng không thể khôi phục được.");
        nops.setCancelText("Hủy");
        nops.setConfirmText("Xoá");
        nops.showCancelButton(true);
        nops.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    nops.cancel();
                }
            });
        nops.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    try {
                        // clearing app data
                        String packageName = getApplicationContext().getPackageName();
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec("pm clear "+packageName);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        nops.show();
    }
        
        
        
        
    public void doTabs() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        deleteLogs = (FloatingActionButton)findViewById(R.id.delete_log);
        mLogAdapter = new LogsAdapter(layoutManager,this);
        logList = (RecyclerView) findViewById(R.id.recyclerLog);
        logList.setAdapter(mLogAdapter);
        logList.setLayoutManager(layoutManager);
        mLogAdapter.scrollToLastPosition();
        vp = (ViewPager)findViewById(R.id.viewpager);
        tabs = (TabLayout)findViewById(R.id.tablayout);
        vp.setAdapter(new MyAdapter(Arrays.asList(tabTitle)));
        vp.setOffscreenPageLimit(2);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(vp);
        deleteLogs.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View p1)
                {
                    mLogAdapter.clearLog();
                    SkStatus.logInfo("<font color='red'>Đã Xóa Nhật Ký!</font>");
                    // TODO: Implement this method
                }


            });

    }
    
        
    public class MyAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            // TODO: Implement this method
            return 2;
        }

        @Override
        public boolean isViewFromObject(View p1, Object p2)
        {
            // TODO: Implement this method
            return p1 == p2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            int[] ids = new int[]{R.id.tab1, R.id.tab2};
            int id = 0;
            id = ids[position];
            // TODO: Implement this method
            return findViewById(id);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            // TODO: Implement this method
            return titles.get(position);
        }

        private List<String> titles;
        public MyAdapter(List<String> str)
        {
            titles = str;
        }
	}
        
	private void doUpdateLayout() {
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		boolean isRunning = SkStatus.isTunnelActive();
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		
		setStarterButton(starterButton, this);

		String proxyStr = getText(R.string.no_value).toString();

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			proxyStr = "*******";
			proxyInputLayout.setEnabled(false);
		}
		else {
			String proxy = mConfig.getPrivString(Settings.PROXY_IP_KEY);

			if (proxy != null && !proxy.isEmpty())
				proxyStr = String.format("%s:%s", proxy, mConfig.getPrivString(Settings.PROXY_PORTA_KEY));
			proxyInputLayout.setEnabled(!isRunning);
		} 

		proxyText.setText(proxyStr);


		switch (tunnelType) {
			case Settings.bTUNNEL_TYPE_SSH_DIRECT:
				((AppCompatRadioButton) findViewById(R.id.activity_mainSSHDirectRadioButton))
					.setChecked(true);
				break;

			case Settings.bTUNNEL_TYPE_SSH_PROXY:
				((AppCompatRadioButton) findViewById(R.id.activity_mainSSHProxyRadioButton))
					.setChecked(true);
                break;
            case Settings.bTUNNEL_TYPE_SSH_SSL:
                ((AppCompatRadioButton) findViewById(R.id.activity_mainSSHSSLRadioButton))
                    .setChecked(true);
                break;
		}

		int msgVisibility = View.GONE;
		int loginVisibility = View.GONE;
		String msgText = "";
		boolean enabled_radio = !isRunning;

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			
			if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				loginVisibility = View.VISIBLE;
				
				inputPwUser.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
				inputPwPass.setText(mConfig.getPrivString(Settings.SENHA_KEY));
				
				inputPwUser.setEnabled(!isRunning);
				inputPwPass.setEnabled(!isRunning);
				inputPwShowPass.setEnabled(!isRunning);
				
				//inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			
			String msg = mConfig.getPrivString(Settings.CONFIG_MENSAGEM_KEY);
			if (!msg.isEmpty()) {
				msgText = msg.replace("\n", "<br/>");
				msgVisibility = View.VISIBLE;
			}
			
			if (mConfig.getPrivString(Settings.PROXY_IP_KEY).isEmpty() ||
					mConfig.getPrivString(Settings.PROXY_PORTA_KEY).isEmpty()) {
				enabled_radio = false;
			}
		}

		loginLayout.setVisibility(loginVisibility);
		configMsgText.setText(msgText.isEmpty() ? "" : Html.fromHtml(msgText));
		configMsgLayout.setVisibility(msgVisibility);
		
		// desativa/ativa radio group
		for (int i = 0; i < metodoConexaoRadio.getChildCount(); i++) {
			metodoConexaoRadio.getChildAt(i).setEnabled(enabled_radio);
		}
	}
	
	
	private synchronized void doSaveData() {
		try {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();
			
			edit.apply();
			if (mainLayout != null && !isFinishing())
				mainLayout.requestFocus();

			if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
				if (payloadEdit != null && !prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
					int pos = payloadSpinner.getSelectedItemPosition();
                    // int modeType = prefs.getInt("TunnelMode",modeGroup.getCheckedRadioButtonId());
					
				
					if (prefs.getInt("CustomSetup", 0) == 1){ // Custom setup on
						
						if (prefs.getInt("TunneType", 0) == 1){ // SSH
						
						String payload = payloadEdit.getText().toString();
						edit.putString(Settings.CUSTOM_PAYLOAD_KEY, payload);
						
						edit.putString("CustomPayload", payload).apply();
						
						}else{ // SSL
							
							String sni = sslEdit.getText().toString();
							edit.putString(Settings.CUSTOM_PAYLOAD_KEY, sni);

							edit.putString("CustomSNI", sni).apply();
							
							}

					}else{
					
                   
                   boolean sslType = config.getNetworksArray().getJSONObject(pos).getBoolean("isSSL");
               
               
                   if (sslType) {
                       prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_SSL).apply();
                       String sni = config.getNetworksArray().getJSONObject(pos).getString("SNI");
                       edit.putString(Settings.CUSTOM_PAYLOAD_KEY, sni);
                   } else {
                       prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();
                       String payload = config.getNetworksArray().getJSONObject(pos).getString("Payload");
                       edit.putString(Settings.CUSTOM_PAYLOAD_KEY, payload);
                   }
				   
				} // end
				   
				   
                             
         
					
				}
			}
			else {
				if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
					edit.putString(Settings.USUARIO_KEY, inputPwUser.getEditableText().toString());
					edit.putString(Settings.SENHA_KEY, inputPwPass.getEditableText().toString());
				}
			}

			edit.apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setSpinner(){
		SharedPreferences prefs = mConfig.getPrefsPrivate();
        int server = prefs.getInt("LastSelectedServer", 0);
        int payload = prefs.getInt("LastSelectedPayload", 0);
        serverSpinner.setSelection(server);
        payloadSpinner.setSelection(payload);
	}
	
	private void saveSpinner(){
		SharedPreferences prefs = mConfig.getPrefsPrivate();
        SharedPreferences.Editor edit = prefs.edit();
        int server = serverSpinner.getSelectedItemPosition();
        int payload = payloadSpinner.getSelectedItemPosition();
        edit.putInt("LastSelectedServer", server);
        edit.putInt("LastSelectedPayload", payload);
        edit.apply();
	}
	

	private void loadServerData() {
		try {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();
			
			serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                        SharedPreferences prefs = mConfig.getPrefsPrivate();
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putInt("LastSelectedServer", p3).apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> p1) {
                    }
                });

            payloadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                        SharedPreferences prefs = mConfig.getPrefsPrivate();
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putInt("LastSelectedPayload", p3).apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> p1) {
                    }
                });
			
        //    int modeType = prefs.getInt("TunnelMode",modeGroup.getCheckedRadioButtonId());
            int pos1 = serverSpinner.getSelectedItemPosition();
            int pos2 = payloadSpinner.getSelectedItemPosition();
			
			boolean sslType = config.getNetworksArray().getJSONObject(pos2).getBoolean("isSSL");
			
		    if (prefs.getInt("CustomSetup", 0) == 0){ // Custom setup off

				if (sslType) {
					String ssl_port = config.getServersArray().getJSONObject(pos1).getString("SSLPort");
					edit.putString(Settings.SERVIDOR_PORTA_KEY, ssl_port);
				} else {
					String ssh_port = config.getServersArray().getJSONObject(pos1).getString("ServerPort");
					edit.putString(Settings.SERVIDOR_PORTA_KEY, ssh_port);
				}
			} // end
            
			String ssh_server = config.getServersArray().getJSONObject(pos1).getString("ServerIP");
			String remote_proxy = config.getServersArray().getJSONObject(pos1).getString("ProxyIP");
			String proxy_port = config.getServersArray().getJSONObject(pos1).getString("ProxyPort");
			String ssh_user = config.getServersArray().getJSONObject(pos1).getString("ServerUser");
			String ssh_pass = config.getServersArray().getJSONObject(pos1).getString("ServerPass");

			edit.putString(Settings.USUARIO_KEY, ssh_user);
			edit.putString(Settings.SENHA_KEY, ssh_pass);
			edit.putString(Settings.SERVIDOR_KEY, ssh_server);
			edit.putString(Settings.PROXY_IP_KEY, remote_proxy);
			
			if (prefs.getInt("CustomSetup", 0) == 0){  // Custom setup off
			  edit.putString(Settings.PROXY_PORTA_KEY, proxy_port);
			} // end

			edit.apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadServer() {
		try {
			if (serverList.size() > 0) {
				serverList.clear();
				serverAdapter.notifyDataSetChanged();
			}
			for (int i = 0; i < config.getServersArray().length(); i++) {
				JSONObject obj = config.getServersArray().getJSONObject(i);
				serverList.add(obj);
				serverAdapter.notifyDataSetChanged();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadNetworks() {
		try {
			if (payloadList.size() > 0) {
				payloadList.clear();
				payloadAdapter.notifyDataSetChanged();
			}
			for (int i = 0; i < config.getNetworksArray().length(); i++) {
				JSONObject obj = config.getNetworksArray().getJSONObject(i);
				payloadList.add(obj);
				payloadAdapter.notifyDataSetChanged();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateConfig(final boolean isOnCreate) {
		new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {
			@Override
			public void onUpdateListener(String result) {
				try {
					if (!result.contains("Error on getting data")) {
                                            String json_data = AESCrypt.decrypt(config.SocksHttpMainActivity, result);
						if (isNewVersion(json_data)) {
							newUpdateDialog(result);
						} else {
							if (!isOnCreate) {
								noUpdateDialog();
							}
						}
					} else if(result.contains("Error on getting data") && !isOnCreate){
						errorUpdateDialog(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start(isOnCreate);
	}

	private boolean isNewVersion(String result) {
		try {
			String current = config.getVersion();
			String update = new JSONObject(result).getString("Version");
			return config.versionCompare(update, current);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	
    private void newUpdateDialog(final String result) throws JSONException, GeneralSecurityException{


        String json_data = AESCrypt.decrypt(config.SocksHttpMainActivity, result);
        String notes = new JSONObject(json_data).getString("ReleaseNotes");
        nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE);
        nops.setTitleText("Có Bản Cập Nhật Mới");
		nops.setContentText("Vui Lòng Cập Nhật Để Có Trải Nghiệm Mới Và Tốt Nhất");
        nops.setConfirmText("Cập Nhật");
        nops.setCancelText("Hủy Bỏ");
        nops.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method
                    try
                    {
                        File file = new File(getFilesDir(), "Config.json");
                        OutputStream out = new FileOutputStream(file);
                        out.write(result.getBytes());
                        out.flush();
                        out.close();
                        restart_app();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }});

        nops.setCancelClickListener((new SweetAlertDialog.OnSweetClickListener() {

                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog)
                                        {
                                            nops.cancel();
                                        }
                                    }));
        nops.show();

    }

	private void noUpdateDialog() {
        new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Tunnel VPN")
            .setContentText("Cấu Hình Của Bạn Hiện Đang Là Phiên Bản Mới Nhất")
            .show();
		pDialog.dismiss();
	}

	private void errorUpdateDialog(String error) {
        new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Lỗi Khi Kiểm Tra Cập Nhật")
			.setContentText("Vui Lòng Kiểm Tra Kết Nối Internet Trước Khi Kiểm Tra♲︎︎︎")
            .show();
		pDialog.dismiss();
	}
	

	private void restart_app() {
		Intent intent = new Intent(this, SocksHttpMainActivity.class);
		int i = 123456;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + ((long) 1000), pendingIntent);
		finish();
	}
        
    public void offlineUpdate() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TunnelManagerThread.a(this);
        if (requestCode == PICK_FILE)
        {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String intentData = importer(uri);                  
                    File file = new File(getFilesDir(), "Config.json");
                    OutputStream out = new FileOutputStream(file);
                    out.write(intentData.getBytes());
                    out.flush();
                    out.close();
                    loadServer();
                    loadNetworks();
                    restart_app();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String importer(Uri uri)
    {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();
        }
        catch (IOException e) {e.printStackTrace();}
        return builder.toString();
    } 
    
	/**
	 * Tunnel SSH
	 */

	public void startOrStopTunnel(Activity activity) {
		if (SkStatus.isTunnelActive()) {
			TunnelManagerHelper.stopSocksHttp(activity);
		}
		else {
			// oculta teclado se vísivel, tá com bug, tela verde
			//Utils.hideKeyboard(activity);
			TunnelManagerThread.a(this);
			Settings config = new Settings(activity);
			
			if (config.getPrefsPrivate()
					.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				if (inputPwUser.getText().toString().isEmpty() || 
						inputPwPass.getText().toString().isEmpty()) {
					Toast.makeText(this, R.string.error_userpass_empty, Toast.LENGTH_SHORT)
						.show();
					return;
				}
			}
			
			Intent intent = new Intent(activity, LaunchVpn.class);
			intent.setAction(Intent.ACTION_MAIN);
			
			if (config.getHideLog()) {
				intent.putExtra(LaunchVpn.EXTRA_HIDELOG, true);
			}
			
			activity.startActivity(intent);
		}
	}
    
    private String con, country, tweaks, payloadInfo, selectedServer;
    
    private void showTweaks(){
  
        try {
            int server = serverSpinner.getSelectedItemPosition();
            int payload = payloadSpinner.getSelectedItemPosition();
            selectedServer = config.getServersArray().getJSONObject(server).getString("Name");
            String selectedPayload = config.getNetworksArray().getJSONObject(payload).getString("Name");

            String pInfo = config.getNetworksArray().getJSONObject(payload).getString("Info");
            boolean directModeType = config.getNetworksArray().getJSONObject(payload).getBoolean("isSSL");
            if (directModeType) {
                con = "SSL/TLS";
            } else {
                con = "SSH/WS";
            }
            
            country = selectedServer;
            tweaks = selectedPayload;
            payloadInfo = "Kiểu kết nối: " + pInfo;

            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        SharedPreferences prefs = mConfig.getPrefsPrivate();

        if (prefs.getInt("CustomSetup", 0) == 1){ // Custom Setup

            if (prefs.getInt("TunneType", 0) == 1){ // SSH
            
                con = "SSH/WS";
                tweaks = "Custom Payload";
                payloadInfo = "";

            }else if (prefs.getInt("TunneType", 0) == 2){ // SSL
            
                con = "SSL/TLS";
                tweaks = "Custom SNI";
                payloadInfo = "";

			}
            
         }
        
        new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Chế Độ: "+con)
            .setContentText("Máy Chủ: "+country + "\n" + "Nhà Mạng: " + tweaks + "\n" + payloadInfo)
            .setConfirmText("Kết Nối")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method
                    startOrStopTunnel(SocksHttpMainActivity.this);
                    doSaveData();
                    loadServerData();
                   // start();
				    saveSpinner();
                    
                    sweetAlertDialog.dismiss();
                    
                }})
            .setCancelText("Hủy Bỏ")
            .showCancelButton(true)
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                  sweetAlertDialog.dismiss();
                }


            })

            .show();
    }

	public void setStarterButton(Button starterButton, Activity activity) {
		String state = SkStatus.getLastState();
		boolean isRunning = SkStatus.isTunnelActive();

		if (starterButton != null) {
			int resId;
			
			SharedPreferences prefsPrivate = new Settings(activity).getPrefsPrivate();

			if (ConfigParser.isValidadeExpirou(prefsPrivate
					.getLong(Settings.CONFIG_VALIDADE_KEY, 0))) {
				resId = R.string.expired;
				starterButton.setEnabled(false);

				if (isRunning) {
					startOrStopTunnel(activity);
				}
			}
			else if (prefsPrivate.getBoolean(Settings.BLOQUEAR_ROOT_KEY, false) &&
					ConfigParser.isDeviceRooted(activity)) {
			   resId = R.string.blocked;
			   starterButton.setEnabled(false);
			   
			   Toast.makeText(activity, R.string.error_root_detected, Toast.LENGTH_SHORT)
					.show();

			   if (isRunning) {
				   startOrStopTunnel(activity);
			   }
			}
			else if (SkStatus.SSH_INICIANDO.equals(state)) {
				resId = R.string.stop;
				starterButton.setEnabled(false);
				methodSpinner.setEnabled(false);
				customSetUP.setEnabled(false);
				serverSpinner.setEnabled(false);
				payloadSpinner.setEnabled(false);
				sslEdit.setEnabled(false);
				payloadEdit.setEnabled(false);
				sportSetup.setEnabled(false);
				portAuto.setEnabled(false);
			}
			else if (SkStatus.SSH_PARANDO.equals(state)) {
				resId = R.string.state_stopping;
				starterButton.setEnabled(false);
			}else if (SkStatus.SSH_DESCONECTADO.equals(state)){
				resId = R.string.start;
				starterButton.setEnabled(true);
				customSetUP.setEnabled(true);
				methodSpinner.setEnabled(true);
				serverSpinner.setEnabled(true);
				payloadSpinner.setEnabled(true);
				sslEdit.setEnabled(true);
				payloadEdit.setEnabled(true);
				sportSetup.setEnabled(true);
				portAuto.setEnabled(true);
				//stop();
                //timer_layout.setVisibility(View.VISIBLE);
                
				}else {
				resId = isRunning ? R.string.stop : R.string.start;
				starterButton.setEnabled(true);
			}

			starterButton.setText(resId);
		}
	}
	

	
	@Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (mDrawerPanel.getToogle() != null)
			mDrawerPanel.getToogle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerPanel.getToogle() != null)
			mDrawerPanel.getToogle().onConfigurationChanged(newConfig);
    }
	
	private boolean isMostrarSenha = false;
	
	@Override
	public void onClick(View p1)
	{
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		switch (p1.getId()) {
			case R.id.activity_starterButtonMain:
               
                if (SkStatus.isTunnelActive()) {
                
                    
                    new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Ngắt Kết Nối VPN")
                        .setContentText("Bạn Có Chắc Chắn Muốn Ngắt Kết Nối Không?")
                        .setConfirmText("Ngắt Kết Nối")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog)
                            {
                                // TODO: Implement this method
                                TunnelManagerHelper.stopSocksHttp(SocksHttpMainActivity.this);

                                sweetAlertDialog.dismiss();

                            }})
                        .setCancelText("Hủy Bỏ")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog)
                            {
                                sweetAlertDialog.dismiss();
                            }


                        })

                        .show();
                    
                    
                }
                else {
                
                showTweaks();
                
                }

				break;

			case R.id.activity_mainInputProxyLayout:
				if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
					doSaveData();

					DialogFragment fragProxy = new ProxyRemoteDialogFragment();
					fragProxy.show(getSupportFragmentManager(), "proxyDialog");
				}
				break;

			
				
			case R.id.activity_mainInputShowPassImageButton:
				isMostrarSenha = !isMostrarSenha;
				if (isMostrarSenha) {
					inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					inputPwShowPass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_black_24dp));
				}
				else {
					inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					inputPwShowPass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off_black_24dp));
				}
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup p1, int p2)
	{
		SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
        TunnelManagerThread.a(this);
		switch (p1.getCheckedRadioButtonId()) {
			case R.id.activity_mainSSHDirectRadioButton:
				edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
				proxyInputLayout.setVisibility(View.GONE);
				break;

			case R.id.activity_mainSSHProxyRadioButton:
				edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY);
				proxyInputLayout.setVisibility(View.VISIBLE);
				break;
		}

		edit.apply();

		//doSaveData();
		doUpdateLayout();
	}
  
        class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Phản ứng: ", "> " + line);   //here u ll get whole response...... :-) 

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MsgAdmin.setText(result);

        }
    }
    

	

	@Override
    public void updateState(final String state, String msg, int localizedResId, final ConnectionStatus level, Intent intent)
    {
        mHandler.post(new Runnable() {
                @Override
                public void run() {
                    doUpdateLayout();
                    if (SkStatus.isTunnelActive()){

                            if (level.equals(ConnectionStatus.LEVEL_CONNECTED)){
                                status.setText(R.string.connected);                              
                               // start();
                               // timer_layout.setVisibility(View.VISIBLE); 
                                TastyToast.makeText(getApplicationContext(), "Đã kết nối", TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS);
                            }

                            if (level.equals(ConnectionStatus.LEVEL_NOTCONNECTED)){
                                status.setText(R.string.servicestop);
                            }       

                            if (level.equals(ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED)){
                                status.setText(R.string.authenticating);
                            }               

                            if (level.equals(ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET)){
                                status.setText(R.string.connecting);
                            }                       
                            if (level.equals(ConnectionStatus.LEVEL_AUTH_FAILED)){
                                status.setText(R.string.authfailed);
                            }                                       
                            if (level.equals(ConnectionStatus.UNKNOWN_LEVEL)){
                                status.setText(R.string.disconnected);
                                TastyToast.makeText(getApplicationContext(), "Đã ngắt kết nối", TastyToast.LENGTH_LONG,
                                                    TastyToast.ERROR);
                            }                               
                            //if (level.equals(ConnectionStatus.LEVEL_RECONNECTING)){
                            //              status.setText(R.string.reconnecting);
                                                     
                        if (level.equals(ConnectionStatus.LEVEL_NONETWORK)){
                            status.setText(R.string.nonetwork);
                           
				 methodSpinner.setEnabled(false);
				 customSetUP.setEnabled(false);
				 serverSpinner.setEnabled(false);
				 payloadSpinner.setEnabled(false);
				 sslEdit.setEnabled(false);
				 payloadEdit.setEnabled(false);
				 sportSetup.setEnabled(false);
				 portAuto.setEnabled(false);
										                                                                                            
                           							
                            }
                        }}
                   
            });
		
		switch (state) {
			case SkStatus.SSH_CONECTADO:
				// carrega ads banner
			
			break;
		}
	}


	/**
	 * Recebe locais Broadcast
	 */

	private BroadcastReceiver mActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;

            if (action.equals(UPDATE_VIEWS) && !isFinishing()) {
				doUpdateLayout();
			}else if (action.equals(OPEN_LOGS)) {
				vp.setCurrentItem(1, true);
			}
			
        }
    };


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerPanel.getToogle() != null && mDrawerPanel.getToogle().onOptionsItemSelected(item)) {
            return true;
        }

		// Menu Itens
		switch (item.getItemId()) {
			
			case R.id.miSettings:
				Intent intentSettings = new Intent(this, ConfigGeralActivity.class);
				//intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentSettings);
				break;
                        // logs opções
			case R.id.molq:
				String ur21 = "android-app://com.garena.game.kgvn";
				Intent intent10 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur21));
				intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent10,getText(R.string.open_with)));
				break;
				
			case R.id.moff:
				String ur22 = "android-app://com.dts.freefireth";
				Intent intent11 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur22));
				intent11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent11,getText(R.string.open_with)));
				break;
				
			case R.id.mott:
				String ur23 = "android-app://com.ss.android.ugc.trill";
				Intent intent12 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur23));
				intent12.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent12,getText(R.string.open_with)));
				break;
				
			case R.id.mophatwifi:
				String ur24 = "android-app://com.ostechnologies.vpnhotspotproxy";
				Intent intent13 = new Intent(Intent.ACTION_VIEW, Uri.parse(ur24));
				intent13.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent13,getText(R.string.open_with)));
				break;
                                            
				
            case R.id.miExit:
            onBackPressed();			                      
            }
           return false;
        }
                    
               
        private void PayloadGenerator() {

		PayloadGenerator gen = new PayloadGenerator(this);
		gen.setDismissListener(this);
		dialog = new AlertDialog.Builder(this).create();
		dialog.setView(gen);
		dialog.show();

	}

	@Override
	public void onDismiss(String payload)
	{
		payloadEdit.setText(payload);
		Toast.makeText(this,"Tải trọng được tạo thành công!",Toast.LENGTH_SHORT).show();
		dialog.dismiss();
		// TODO: Implement this method
	}

	


	@Override
	public void onBackPressed()
	{
		new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.attention))
            .setContentText(getString(R.string.alert_exit))
            .setConfirmText(getString(R.string.exit))
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method

                    Utils.exitAll(SocksHttpMainActivity.this);

                }})
            .setCancelText(getString(R.string.minimize))
			.showCancelButton(true)
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }


            })

            .show();
	}
        
    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //Update Height
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                    layoutParams.height = value;
                    mLinearLayout.setLayoutParams(layoutParams);
                }
            });
        return animator;
    }
        
    private void collapse() {
        int finalHeight = mLinearLayout.getHeight();
        mArrow1.animate().setDuration(500).rotation(180);
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator p1)
                {
                    // TODO: Implement this method
                }

                @Override
                public void onAnimationCancel(Animator p1)
                {
                    // TODO: Implement this method
                }

                @Override
                public void onAnimationRepeat(Animator p1)
                {
                    // TODO: Implement this method
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //Height=0, but it set visibility to GONE
                    mLinearLayout.setVisibility(View.GONE);
                }

            });
        mAnimator.start();
    }

    private void expand() {
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);
        mArrow1.animate().setDuration(500).rotation(0);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
        mAnimator.start();    
    }
    
        
        private void checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected())
        {
	        toolbar_main.setSubtitle("WIFI: "+TunnelUtils.getLocalIpAddress());
			toolbar_main.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitleText);

        } else if (mMobile.isConnected()) {
            
				toolbar_main.setSubtitle("DI ĐỘNG: "+TunnelUtils.getLocalIpAddress());
			toolbar_main.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitleText);
			
        } else {
				toolbar_main.setSubtitle("KHÔNG CÓ KẾT NỐI INTERNET");
			toolbar_main.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitleText);
        }
	}
	
	private void updateHeaderCallback() {
            new JsonTask().execute(MSGadmn);
		}

	@Override
    public void onResume() {
        super.onResume();
            mTrafficSpeedMeasurer.registerListener(mStreamSpeedListener);
            updateHeaderCallback();
		setSpinner();
		TunnelManagerThread.a(this);
	   // showInterstitial();
        
		
		
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		
		if (prefs.getInt("CustomSetup", 0) == 1){ // Custom Setup
		
		   customSetUP.setChecked(true);

			if (prefs.getInt("TunneType", 0) == 1){ // SSH

				setupSSH();

			}else if (prefs.getInt("TunneType", 0) == 2){ // SSL

				setupSSL();

			}

		}else{
			payloadLayout.setVisibility(View.GONE);
			ssl_layout.setVisibility(View.GONE);
		}
	    
		
		new Timer().schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								updateHeaderCallback();
								checkNetwork();
								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}
			}, 0,1000);
		
		
		SkStatus.addStateListener(this);
		
		
    }

	@Override
	protected void onPause()
	{
		super.onPause();
            mTrafficSpeedMeasurer.removeListener(mStreamSpeedListener);
		doSaveData();
		
		SkStatus.removeStateListener(this);
		
		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		LocalBroadcastManager.getInstance(this)
			.unregisterReceiver(mActivityReceiver);
			
		
	}


	/**
	 * DrawerLayout Listener
	 */

	/**
	 * Utils
	 */

	public static void updateMainViews(Context context) {
		Intent updateView = new Intent(UPDATE_VIEWS);
		LocalBroadcastManager.getInstance(context)
			.sendBroadcast(updateView);
	}}
	
	

