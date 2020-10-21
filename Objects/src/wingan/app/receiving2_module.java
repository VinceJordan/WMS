package wingan.app;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class receiving2_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static receiving2_module mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.receiving2_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (receiving2_module).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.receiving2_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.receiving2_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (receiving2_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (receiving2_module) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return receiving2_module.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (receiving2_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (receiving2_module) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            receiving2_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (receiving2_module) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.sql.SQL _connection = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor2 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor3 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor4 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor5 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor6 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor7 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor8 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor9 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor10 = null;
public static String _progpath = "";
public static double _scaleauto = 0;
public static String[] _texts = null;
public static String _languageid = "";
public static String _sval = "";
public static double _val = 0;
public static String _op0 = "";
public static double _result1 = 0;
public static String _txt = "";
public static int _new1 = 0;
public static String _old_doc_no = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _uploadbitmap = null;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static String _principal_id = "";
public static String _product_id = "";
public static String _reason = "";
public static String _input_type = "";
public static String _caseper = "";
public static String _pcsper = "";
public static String _dozper = "";
public static String _boxper = "";
public static String _bagper = "";
public static String _packper = "";
public static int _total_pieces = 0;
public static int _price = 0;
public static int _total_price = 0;
public static String _transaction_number = "";
public static String _scan_code = "";
public static int _total_order = 0;
public static int _total_input = 0;
public static String _lifespan_month = "";
public static String _lifespan_year = "";
public static String _default_reading = "";
public static String _monthexp = "";
public static String _yearexp = "";
public static String _monthmfg = "";
public static String _yearmfg = "";
public static String _error_trigger = "";
public static String _security_trigger = "";
public anywheresoftware.b4a.objects.IME _ctrl = null;
public anywheresoftware.b4a.phone.Phone _phone = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn2 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable _table_purchase_order = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn0 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn7 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn8 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn9 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnback = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnclr = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaperroll = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvpaperroll = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlkeyboard = null;
public anywheresoftware.b4a.objects.StringUtils _stu = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_answer = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_calcu = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xdatetemplate _datetemplate = null;
public wingan.app.b4xdatetemplate _datetemplate2 = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_principal = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_delivery = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_okay = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview_dr = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_doc_no = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_driver = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_trucking = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_plate = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_trucktype = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_save = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_order = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_input = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_balance = null;
public wingan.app.b4xcombobox _cmb_invoice = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_input = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_manufactured = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_expiration = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvl_list = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_cancel = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_add = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_security = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_password = null;
public wingan.app.b4xcombobox _cmb_account = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_invoice = null;
public wingan.app.b4xtable _table_invoice = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_totalamt = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_totalcase = null;
public wingan.app.b4xcombobox _cmb_inv = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_expiration = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_manufacturing = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
public wingan.app.dashboard_module _dashboard_module = null;
public wingan.app.return_module _return_module = null;
public wingan.app.cancelled_module _cancelled_module = null;
public wingan.app.sales_return_module _sales_return_module = null;
public wingan.app.preparing_module _preparing_module = null;
public wingan.app.loading_module _loading_module = null;
public wingan.app.daily_count _daily_count = null;
public wingan.app.inventory_table _inventory_table = null;
public wingan.app.montlhy_inventory2_module _montlhy_inventory2_module = null;
public wingan.app.price_checking_module _price_checking_module = null;
public wingan.app.expiration_module _expiration_module = null;
public wingan.app.daily_inventory_module _daily_inventory_module = null;
public wingan.app.daily_template _daily_template = null;
public wingan.app.database_module _database_module = null;
public wingan.app.monthly_inventory_module _monthly_inventory_module = null;
public wingan.app.monthlyinv_module _monthlyinv_module = null;
public wingan.app.number _number = null;
public wingan.app.receiving_module _receiving_module = null;
public wingan.app.scale_calc _scale_calc = null;
public wingan.app.starter _starter = null;
public wingan.app.httputils2service _httputils2service = null;
public wingan.app.b4xcollections _b4xcollections = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static void  _activity_create(boolean _firsttime) throws Exception{
ResumableSub_Activity_Create rsub = new ResumableSub_Activity_Create(null,_firsttime);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Create extends BA.ResumableSub {
public ResumableSub_Activity_Create(wingan.app.receiving2_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.receiving2_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 180;BA.debugLine="Activity.LoadLayout(\"receiving2\")";
parent.mostCurrent._activity.LoadLayout("receiving2",mostCurrent.activityBA);
 //BA.debugLineNum = 182;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 183;BA.debugLine="uploadBitmap = LoadBitmap(File.DirAssets, \"upload";
parent._uploadbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"upload.png");
 //BA.debugLineNum = 186;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 187;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 188;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 189;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 190;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 191;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 192;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 193;BA.debugLine="ACToolBarLight1.SubTitle = \"PO NO : \" & RECEIVING";
parent.mostCurrent._actoolbarlight1.setSubTitle(BA.ObjectToCharSequence("PO NO : "+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ ));
 //BA.debugLineNum = 195;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = RECEIVING_MODULE.prin";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent.mostCurrent._receiving_module._principal_name /*String*/ ));
 //BA.debugLineNum = 197;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 198;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 199;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 201;BA.debugLine="If connection.IsInitialized = False Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._connection.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 202;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 205;BA.debugLine="phone.SetScreenOrientation(0)";
parent.mostCurrent._phone.SetScreenOrientation(processBA,(int) (0));
 //BA.debugLineNum = 207;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 208;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 209;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 212;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 213;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 215;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 216;BA.debugLine="Dialog.Initialize (Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 217;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 218;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 219;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 220;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 221;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 222;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 223;BA.debugLine="Dialog.BodyTextColor = Colors.Black";
parent.mostCurrent._dialog._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 225;BA.debugLine="DateTemplate.Initialize";
parent.mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 226;BA.debugLine="DateTemplate.MinYear = 2016";
parent.mostCurrent._datetemplate._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 227;BA.debugLine="DateTemplate.MaxYear = 2030";
parent.mostCurrent._datetemplate._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 228;BA.debugLine="DateTemplate.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 229;BA.debugLine="DateTemplate.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 230;BA.debugLine="DateTemplate.btnMonthLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 231;BA.debugLine="DateTemplate.btnMonthRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 232;BA.debugLine="DateTemplate.btnYearLeft.Color = Colors.RGB(82,16";
parent.mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 233;BA.debugLine="DateTemplate.btnYearRight.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 234;BA.debugLine="DateTemplate.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 236;BA.debugLine="DateTemplate2.Initialize";
parent.mostCurrent._datetemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 237;BA.debugLine="DateTemplate2.MinYear = 2016";
parent.mostCurrent._datetemplate2._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 238;BA.debugLine="DateTemplate2.MaxYear = 2030";
parent.mostCurrent._datetemplate2._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 239;BA.debugLine="DateTemplate2.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate2._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 240;BA.debugLine="DateTemplate2.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate2._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 241;BA.debugLine="DateTemplate2.btnMonthLeft.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate2._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 242;BA.debugLine="DateTemplate2.btnMonthRight.Color = Colors.RGB(82";
parent.mostCurrent._datetemplate2._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 243;BA.debugLine="DateTemplate2.btnYearLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate2._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 244;BA.debugLine="DateTemplate2.btnYearRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate2._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 245;BA.debugLine="DateTemplate2.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate2._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 247;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 248;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 249;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 250;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 251;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 252;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 254;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 255;BA.debugLine="Dim Ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 256;BA.debugLine="Ref.Target = EDITTEXT_DRIVER ' The text field bei";
_ref.Target = (Object)(parent.mostCurrent._edittext_driver.getObject());
 //BA.debugLineNum = 257;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 258;BA.debugLine="Ref.Target = EDITTEXT_PLATE";
_ref.Target = (Object)(parent.mostCurrent._edittext_plate.getObject());
 //BA.debugLineNum = 259;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 260;BA.debugLine="Ref.Target = EDITTEXT_TRUCKING";
_ref.Target = (Object)(parent.mostCurrent._edittext_trucking.getObject());
 //BA.debugLineNum = 261;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 262;BA.debugLine="Ref.Target = EDITTEXT_TRUCKTYPE";
_ref.Target = (Object)(parent.mostCurrent._edittext_trucktype.getObject());
 //BA.debugLineNum = 263;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 264;BA.debugLine="Ref.Target = EDITTEXT_DOC_NO";
_ref.Target = (Object)(parent.mostCurrent._edittext_doc_no.getObject());
 //BA.debugLineNum = 265;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 266;BA.debugLine="Ref.Target = EDITTEXT_QUANTITY";
_ref.Target = (Object)(parent.mostCurrent._edittext_quantity.getObject());
 //BA.debugLineNum = 267;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 268;BA.debugLine="Ref.Target = EDITTEXT_PASSWORD";
_ref.Target = (Object)(parent.mostCurrent._edittext_password.getObject());
 //BA.debugLineNum = 269;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 271;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 272;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 274;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 275;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 276;BA.debugLine="EDITTEXT_PASSWORD.Background = bg";
parent.mostCurrent._edittext_password.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 278;BA.debugLine="VIEW_DR";
_view_dr();
 //BA.debugLineNum = 279;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 280;BA.debugLine="LOAD_PURCHASE_HEADER";
_load_purchase_header();
 //BA.debugLineNum = 281;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 282;BA.debugLine="LOAD_PURCHASE_ORDER";
_load_purchase_order();
 //BA.debugLineNum = 283;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 284;BA.debugLine="INPUT_MANUAL";
_input_manual();
 //BA.debugLineNum = 285;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = -1;
;
 //BA.debugLineNum = 286;BA.debugLine="LOAD_INVOICE_HEADER";
_load_invoice_header();
 //BA.debugLineNum = 288;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item2 = null;
 //BA.debugLineNum = 289;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 290;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 291;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 292;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 293;BA.debugLine="Dim item2 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item2 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item2 = mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (0),BA.ObjectToCharSequence("upload"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 294;BA.debugLine="item2.ShowAsAction = item2.SHOW_AS_ACTION_ALWAYS";
_item2.setShowAsAction(_item2.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 295;BA.debugLine="UpdateIcon(\"upload\", uploadBitmap)";
_updateicon("upload",_uploadbitmap);
 //BA.debugLineNum = 296;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 2304;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 2305;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 2306;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 2308;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 305;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 306;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("787556097","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 307;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 308;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 309;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 298;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 299;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("787490561","Resuming...",0);
 //BA.debugLineNum = 300;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 301;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 302;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 304;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.receiving2_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.receiving2_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 358;BA.debugLine="If Item.Title = \"cart\" Then";
if (true) break;

case 1:
//if
this.state = 10;
if ((_item.getTitle()).equals("cart")) { 
this.state = 3;
}else if((_item.getTitle()).equals("upload")) { 
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 10;
 //BA.debugLineNum = 359;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("788080386","Resuming...",0);
 //BA.debugLineNum = 360;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 361;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 362;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 364;BA.debugLine="Msgbox2Async(\"Are you sure you want to upload th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to upload this transaction?"),BA.ObjectToCharSequence("Upload Receiving"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 365;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 11;
return;
case 11:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 366;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 9;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 367;BA.debugLine="DELETE_RECEIVING_DISC";
_delete_receiving_disc();
 if (true) break;

case 9:
//C
this.state = 10;
;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 370;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 339;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 340;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 341;BA.debugLine="StartActivity(RECEIVING_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._receiving_module.getObject()));
 //BA.debugLineNum = 342;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return "";
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 743;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 744;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("788670209","Connection broken...",0);
 //BA.debugLineNum = 745;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 746;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 747;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 748;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 749;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 750;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 751;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 752;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 754;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 756;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.receiving2_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.receiving2_module parent;
byte[] _buffer;
String _add_query = "";
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _row1 = 0;
int step8;
int limit8;
int step22;
int limit22;
int step30;
int limit30;
int step34;
int limit34;
int step49;
int limit49;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 630;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("788604673","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 633;BA.debugLine="Dim add_query As String = \" AND principal_id = '\"";
_add_query = " AND principal_id = '"+parent.mostCurrent._receiving_module._principal_id /*String*/ +"'";
 //BA.debugLineNum = 634;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 635;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0'"+_add_query+" ORDER BY product_id")));
 //BA.debugLineNum = 636;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 1:
//if
this.state = 40;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 3;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 637;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 638;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 639;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step8 = 1;
limit8 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 64;
if (true) break;

case 64:
//C
this.state = 7;
if ((step8 > 0 && _row <= limit8) || (step8 < 0 && _row >= limit8)) this.state = 6;
if (true) break;

case 65:
//C
this.state = 64;
_row = ((int)(0 + _row + step8)) ;
if (true) break;

case 6:
//C
this.state = 65;
 //BA.debugLineNum = 640;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 641;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 642;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 644;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) '";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 645;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 66;
return;
case 66:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 646;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 8:
//if
this.state = 13;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 10:
//C
this.state = 13;
 //BA.debugLineNum = 647;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 648;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 650;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 13:
//C
this.state = 40;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 655;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 16:
//for
this.state = 19;
step22 = 1;
limit22 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 67;
if (true) break;

case 67:
//C
this.state = 19;
if ((step22 > 0 && _row <= limit22) || (step22 < 0 && _row >= limit22)) this.state = 18;
if (true) break;

case 68:
//C
this.state = 67;
_row = ((int)(0 + _row + step22)) ;
if (true) break;

case 18:
//C
this.state = 68;
 //BA.debugLineNum = 656;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 657;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 658;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;
if (true) break;

case 19:
//C
this.state = 40;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 661;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 662;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 22:
//if
this.state = 39;
if (parent._cursor4.getRowCount()>0) { 
this.state = 24;
}else {
this.state = 38;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 663;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 25:
//for
this.state = 36;
step30 = 1;
limit30 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 69;
if (true) break;

case 69:
//C
this.state = 36;
if ((step30 > 0 && _row <= limit30) || (step30 < 0 && _row >= limit30)) this.state = 27;
if (true) break;

case 70:
//C
this.state = 69;
_row = ((int)(0 + _row + step30)) ;
if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 664;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 665;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT princip";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._cursor4.GetString("principal_id")+"'")));
 //BA.debugLineNum = 666;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 28:
//if
this.state = 35;
if (parent._cursor5.getRowCount()>0) { 
this.state = 30;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 667;BA.debugLine="For row1 = 0 To cursor5.RowCount - 1";
if (true) break;

case 31:
//for
this.state = 34;
step34 = 1;
limit34 = (int) (parent._cursor5.getRowCount()-1);
_row1 = (int) (0) ;
this.state = 71;
if (true) break;

case 71:
//C
this.state = 34;
if ((step34 > 0 && _row1 <= limit34) || (step34 < 0 && _row1 >= limit34)) this.state = 33;
if (true) break;

case 72:
//C
this.state = 71;
_row1 = ((int)(0 + _row1 + step34)) ;
if (true) break;

case 33:
//C
this.state = 72;
 //BA.debugLineNum = 668;BA.debugLine="cursor5.Position = row1";
parent._cursor5.setPosition(_row1);
 if (true) break;
if (true) break;

case 34:
//C
this.state = 35;
;
 //BA.debugLineNum = 670;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CRL";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent._cursor4.GetString("product_desc")+" "+anywheresoftware.b4a.keywords.Common.CRLF+"belongs to principal :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent._cursor5.GetString("principal_name")+""+anywheresoftware.b4a.keywords.Common.CRLF+"which your selected principal is :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._monthly_inventory_module._principal_name /*String*/ +""),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 35:
//C
this.state = 70;
;
 if (true) break;
if (true) break;

case 36:
//C
this.state = 39;
;
 if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 674;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not RE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 39:
//C
this.state = 40;
;
 //BA.debugLineNum = 676;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;
;
 //BA.debugLineNum = 680;BA.debugLine="If trigger = 0 Then";

case 40:
//if
this.state = 63;
if (_trigger==0) { 
this.state = 42;
}if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 681;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.Leng";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 682;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM pu";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM purchase_order_ref_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 684;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 43:
//if
this.state = 62;
if (parent._cursor7.getRowCount()>0) { 
this.state = 45;
}else {
this.state = 51;
}if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 685;BA.debugLine="For row = 0 To cursor7.RowCount - 1";
if (true) break;

case 46:
//for
this.state = 49;
step49 = 1;
limit49 = (int) (parent._cursor7.getRowCount()-1);
_row = (int) (0) ;
this.state = 73;
if (true) break;

case 73:
//C
this.state = 49;
if ((step49 > 0 && _row <= limit49) || (step49 < 0 && _row >= limit49)) this.state = 48;
if (true) break;

case 74:
//C
this.state = 73;
_row = ((int)(0 + _row + step49)) ;
if (true) break;

case 48:
//C
this.state = 74;
 //BA.debugLineNum = 686;BA.debugLine="cursor7.Position = row";
parent._cursor7.setPosition(_row);
 //BA.debugLineNum = 687;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 688;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 75;
return;
case 75:
//C
this.state = 74;
;
 //BA.debugLineNum = 689;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 76;
return;
case 76:
//C
this.state = 74;
;
 //BA.debugLineNum = 690;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 691;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 77;
return;
case 77:
//C
this.state = 74;
;
 //BA.debugLineNum = 692;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 693;BA.debugLine="input_type = \"BARCODE\"";
parent._input_type = "BARCODE";
 //BA.debugLineNum = 694;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 695;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 696;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 697;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 78;
return;
case 78:
//C
this.state = 74;
;
 //BA.debugLineNum = 698;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 699;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 79;
return;
case 79:
//C
this.state = 74;
;
 //BA.debugLineNum = 700;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;
if (true) break;

case 49:
//C
this.state = 62;
;
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 704;BA.debugLine="If RECEIVING_MODULE.transaction_type = \"PURCHAS";
if (true) break;

case 52:
//if
this.state = 61;
if ((parent.mostCurrent._receiving_module._transaction_type /*String*/ ).equals("PURCHASE ORDER")) { 
this.state = 54;
}else if((parent.mostCurrent._receiving_module._transaction_type /*String*/ ).equals("AUTO SHIP")) { 
this.state = 60;
}if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 705;BA.debugLine="Msgbox2Async(\"The product you scanned (\"&LABEL";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned ("+parent.mostCurrent._label_load_description.getText()+") is not IN THE PURCHASE ORDER. Do you want to received this product?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 706;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 80;
return;
case 80:
//C
this.state = 55;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 707;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 55:
//if
this.state = 58;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 57;
}if (true) break;

case 57:
//C
this.state = 58;
 //BA.debugLineNum = 708;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 709;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 81;
return;
case 81:
//C
this.state = 58;
;
 //BA.debugLineNum = 710;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 82;
return;
case 82:
//C
this.state = 58;
;
 //BA.debugLineNum = 711;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 712;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 83;
return;
case 83:
//C
this.state = 58;
;
 //BA.debugLineNum = 713;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 714;BA.debugLine="input_type = \"BARCODE\"";
parent._input_type = "BARCODE";
 //BA.debugLineNum = 715;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 716;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 717;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"NO EXPIRATION";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 718;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 84;
return;
case 84:
//C
this.state = 58;
;
 //BA.debugLineNum = 719;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 720;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 85;
return;
case 85:
//C
this.state = 58;
;
 //BA.debugLineNum = 721;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 58:
//C
this.state = 61;
;
 if (true) break;

case 60:
//C
this.state = 61;
 //BA.debugLineNum = 724;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 725;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 86;
return;
case 86:
//C
this.state = 61;
;
 //BA.debugLineNum = 726;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 87;
return;
case 87:
//C
this.state = 61;
;
 //BA.debugLineNum = 727;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 728;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 88;
return;
case 88:
//C
this.state = 61;
;
 //BA.debugLineNum = 729;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 730;BA.debugLine="input_type = \"BARCODE\"";
parent._input_type = "BARCODE";
 //BA.debugLineNum = 731;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 732;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 733;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 734;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 89;
return;
case 89:
//C
this.state = 61;
;
 //BA.debugLineNum = 735;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 736;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 90;
return;
case 90:
//C
this.state = 61;
;
 //BA.debugLineNum = 737;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 61:
//C
this.state = 62;
;
 if (true) break;

case 62:
//C
this.state = 63;
;
 if (true) break;

case 63:
//C
this.state = -1;
;
 //BA.debugLineNum = 742;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 757;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 758;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("788735745","Connection terminated...",0);
 //BA.debugLineNum = 759;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 760;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 334;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 335;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 336;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 337;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return null;
}
public static String  _btnback_click() throws Exception{
 //BA.debugLineNum = 1887;BA.debugLine="Sub btnBack_Click";
 //BA.debugLineNum = 1888;BA.debugLine="If sVal.Length > 0 Then";
if (_sval.length()>0) { 
 //BA.debugLineNum = 1889;BA.debugLine="Txt = sVal.SubString2(0, sVal.Length - 1)";
_txt = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1890;BA.debugLine="sVal = sVal.SubString2(0, sVal.Length - 1)";
_sval = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1891;BA.debugLine="UpdateTape";
_updatetape();
 };
 //BA.debugLineNum = 1893;BA.debugLine="End Sub";
return "";
}
public static String  _btncharsize_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 1907;BA.debugLine="Sub btnCharSize_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 1908;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1909;BA.debugLine="lblPaperRoll.TextSize = 16 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (16*_scaleauto));
 }else {
 //BA.debugLineNum = 1911;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 };
 //BA.debugLineNum = 1913;BA.debugLine="End Sub";
return "";
}
public static String  _btnclr_click() throws Exception{
 //BA.debugLineNum = 1868;BA.debugLine="Sub btnClr_Click";
 //BA.debugLineNum = 1875;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1876;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1877;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1878;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1879;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1880;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1881;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1882;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1883;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1884;BA.debugLine="LABEL_LOAD_ANSWER.Text = \"0\"";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1886;BA.debugLine="End Sub";
return "";
}
public static String  _btndigit_click() throws Exception{
String _bs = "";
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
 //BA.debugLineNum = 1675;BA.debugLine="Sub btnDigit_Click";
 //BA.debugLineNum = 1676;BA.debugLine="Dim bs As String, Send As View";
_bs = "";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 1678;BA.debugLine="If New1 = 0 Then";
if (_new1==0) { 
 //BA.debugLineNum = 1679;BA.debugLine="New1 = 1";
_new1 = (int) (1);
 };
 //BA.debugLineNum = 1682;BA.debugLine="Send = Sender";
_send = (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 1683;BA.debugLine="bs = Send.Tag";
_bs = BA.ObjectToString(_send.getTag());
 //BA.debugLineNum = 1685;BA.debugLine="Select bs";
switch (BA.switchObjectToInt(_bs,"0","1","2","3","4","5","6","7","8","9","3.1415926535897932",".","a","b","c","d","e","y","g","s","x","f")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: 
case 6: 
case 7: 
case 8: 
case 9: 
case 10: 
case 11: {
 //BA.debugLineNum = 1687;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"g","s","m","x")) {
case 0: 
case 1: 
case 2: 
case 3: {
 //BA.debugLineNum = 1689;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1692;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1693;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1694;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1695;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1696;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1697;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 };
 //BA.debugLineNum = 1700;BA.debugLine="If bs = \"3.1415926535897932\" Then";
if ((_bs).equals("3.1415926535897932")) { 
 //BA.debugLineNum = 1701;BA.debugLine="If sVal <> \"\" Then";
if ((_sval).equals("") == false) { 
 //BA.debugLineNum = 1702;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1704;BA.debugLine="Txt = Txt & cPI";
_txt = _txt+BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 //BA.debugLineNum = 1705;BA.debugLine="sVal = cPI";
_sval = BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 }else if((_bs).equals(".")) { 
 //BA.debugLineNum = 1707;BA.debugLine="If sVal.IndexOf(\".\") < 0 Then";
if (_sval.indexOf(".")<0) { 
 //BA.debugLineNum = 1708;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 1709;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 }else {
 //BA.debugLineNum = 1712;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 1713;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 break; }
case 12: 
case 13: 
case 14: 
case 15: 
case 16: 
case 17: {
 //BA.debugLineNum = 1716;BA.debugLine="If sVal =\"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1717;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1719;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1721;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1723;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 18: 
case 19: 
case 20: {
 //BA.debugLineNum = 1725;BA.debugLine="If sVal = \"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1726;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1728;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1730;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1732;BA.debugLine="If Op0 = \"\" Then";
if ((_op0).equals("")) { 
 //BA.debugLineNum = 1733;BA.debugLine="Result1 = sVal";
_result1 = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1735;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 21: {
 //BA.debugLineNum = 1737;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1738;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1739;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1740;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1741;BA.debugLine="Op0 = \"\"";
_op0 = "";
 };
 break; }
}
;
 //BA.debugLineNum = 1745;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1746;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 1894;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 1895;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1896;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1897;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1898;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1899;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1900;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1901;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1902;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1903;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1904;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(mostCurrent._label_load_answer.getText()));
 //BA.debugLineNum = 1905;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1906;BA.debugLine="End Sub";
return "";
}
public static void  _button_add_click() throws Exception{
ResumableSub_BUTTON_ADD_Click rsub = new ResumableSub_BUTTON_ADD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_ADD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_ADD_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
String _expdate = "";
String _datenow = "";
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1188;BA.debugLine="Dim EXPDATE As String";
_expdate = "";
 //BA.debugLineNum = 1189;BA.debugLine="Dim DATENOW As String";
_datenow = "";
 //BA.debugLineNum = 1192;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"\" Or EDITTEXT_QUANTI";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_quantity.getText()).equals("") || (double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))<=0) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1193;BA.debugLine="ToastMessageShow(\"Please input a valid quantity\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a valid quantity"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1195;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text <> \"NO EXPIRATION\"";
if (true) break;

case 6:
//if
this.state = 23;
if ((parent.mostCurrent._label_load_expiration.getText()).equals("NO EXPIRATION") == false) { 
this.state = 8;
}else {
this.state = 22;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1196;BA.debugLine="EXPDATE = DateTime.DateParse(LABEL_LOAD_EXPIRAT";
_expdate = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(parent.mostCurrent._label_load_expiration.getText()));
 //BA.debugLineNum = 1197;BA.debugLine="DATENOW = DateTime.DateParse(DateTime.Date(Date";
_datenow = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
 //BA.debugLineNum = 1198;BA.debugLine="If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then";
if (true) break;

case 9:
//if
this.state = 20;
if ((double)(Double.parseDouble(_daysbetweendates((long)(Double.parseDouble(_datenow)),(long)(Double.parseDouble(_expdate)))))<=0) { 
this.state = 11;
}else if((double)(Double.parseDouble(_daysbetweendates((long)(Double.parseDouble(_datenow)),(long)(Double.parseDouble(_expdate)))))<=120) { 
this.state = 13;
}else {
this.state = 19;
}if (true) break;

case 11:
//C
this.state = 20;
 //BA.debugLineNum = 1199;BA.debugLine="ToastMessageShow(\"You cannot input a expiratio";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You cannot input a expiration date from to date or back date."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1201;BA.debugLine="Msgbox2Async(\"The expiration you inputing is 6";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The expiration you inputing is 6 months below. Do you want to received this product?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1202;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 14;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1203;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 14:
//if
this.state = 17;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 16;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1204;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300,True";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1205;BA.debugLine="PANEL_BG_SECURITY.BringToFront";
parent.mostCurrent._panel_bg_security.BringToFront();
 //BA.debugLineNum = 1206;BA.debugLine="GET_SECURITY";
_get_security();
 //BA.debugLineNum = 1207;BA.debugLine="security_trigger = \"BELOW SIX\"";
parent._security_trigger = "BELOW SIX";
 //BA.debugLineNum = 1208;BA.debugLine="EDITTEXT_PASSWORD.RequestFocus";
parent.mostCurrent._edittext_password.RequestFocus();
 //BA.debugLineNum = 1209;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 26;
return;
case 26:
//C
this.state = 17;
;
 //BA.debugLineNum = 1210;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PASSWORD)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_password.getObject()));
 if (true) break;

case 17:
//C
this.state = 20;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1213;BA.debugLine="INPUT_RECEIVED";
_input_received();
 if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
 if (true) break;

case 23:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = -1;
;
 //BA.debugLineNum = 1220;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_calcu_click() throws Exception{
 //BA.debugLineNum = 1938;BA.debugLine="Sub BUTTON_CALCU_Click";
 //BA.debugLineNum = 1939;BA.debugLine="oncal";
_oncal();
 //BA.debugLineNum = 1940;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,True)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1941;BA.debugLine="PANEL_BG_CALCU.BringToFront";
mostCurrent._panel_bg_calcu.BringToFront();
 //BA.debugLineNum = 1942;BA.debugLine="End Sub";
return "";
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 1462;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 1463;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 1464;BA.debugLine="End Sub";
return "";
}
public static String  _button_document_click() throws Exception{
 //BA.debugLineNum = 771;BA.debugLine="Sub BUTTON_DOCUMENT_Click";
 //BA.debugLineNum = 772;BA.debugLine="VIEW_DR";
_view_dr();
 //BA.debugLineNum = 773;BA.debugLine="End Sub";
return "";
}
public static String  _button_exit_calcu_click() throws Exception{
 //BA.debugLineNum = 1935;BA.debugLine="Sub BUTTON_EXIT_CALCU_Click";
 //BA.debugLineNum = 1936;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1937;BA.debugLine="End Sub";
return "";
}
public static String  _button_exit_click() throws Exception{
 //BA.debugLineNum = 1111;BA.debugLine="Sub BUTTON_EXIT_Click";
 //BA.debugLineNum = 1112;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1113;BA.debugLine="LOAD_PURCHASE_ORDER";
_load_purchase_order();
 //BA.debugLineNum = 1114;BA.debugLine="End Sub";
return "";
}
public static String  _button_invoice_exit_click() throws Exception{
 //BA.debugLineNum = 2014;BA.debugLine="Sub BUTTON_INVOICE_EXIT_Click";
 //BA.debugLineNum = 2015;BA.debugLine="PANEL_BG_INVOICE.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_invoice.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2016;BA.debugLine="End Sub";
return "";
}
public static void  _button_list_click() throws Exception{
ResumableSub_BUTTON_LIST_Click rsub = new ResumableSub_BUTTON_LIST_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_LIST_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_LIST_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1949;BA.debugLine="LOAD_INVOICE";
_load_invoice();
 //BA.debugLineNum = 1950;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1951;BA.debugLine="GET_TOTAL_AMT";
_get_total_amt();
 //BA.debugLineNum = 1952;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1953;BA.debugLine="LOAD_PER_INVOICE";
_load_per_invoice();
 //BA.debugLineNum = 1954;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 1955;BA.debugLine="GET_TOTAL_CASE";
_get_total_case();
 //BA.debugLineNum = 1956;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 4;
return;
case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1957;BA.debugLine="PANEL_BG_INVOICE.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_invoice.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1958;BA.debugLine="PANEL_BG_INVOICE.BringToFront";
parent.mostCurrent._panel_bg_invoice.BringToFront();
 //BA.debugLineNum = 1959;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_manual_click() throws Exception{
ResumableSub_BUTTON_MANUAL_Click rsub = new ResumableSub_BUTTON_MANUAL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_MANUAL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_MANUAL_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1622;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1623;BA.debugLine="PANEL_BG_SECURITY.BringToFront";
parent.mostCurrent._panel_bg_security.BringToFront();
 //BA.debugLineNum = 1624;BA.debugLine="GET_SECURITY";
_get_security();
 //BA.debugLineNum = 1625;BA.debugLine="security_trigger = \"MANUAL BUTTON\"";
parent._security_trigger = "MANUAL BUTTON";
 //BA.debugLineNum = 1626;BA.debugLine="EDITTEXT_PASSWORD.RequestFocus";
parent.mostCurrent._edittext_password.RequestFocus();
 //BA.debugLineNum = 1627;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1628;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PASSWORD)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_password.getObject()));
 //BA.debugLineNum = 1629;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_okay_click() throws Exception{
 //BA.debugLineNum = 891;BA.debugLine="Sub BUTTON_OKAY_Click";
 //BA.debugLineNum = 892;BA.debugLine="If BUTTON_OKAY.Text = \" Cancel\" Then";
if ((mostCurrent._button_okay.getText()).equals(" Cancel")) { 
 //BA.debugLineNum = 893;BA.debugLine="BUTTON_OKAY.Text = \" Okay\"";
mostCurrent._button_okay.setText(BA.ObjectToCharSequence(" Okay"));
 //BA.debugLineNum = 894;BA.debugLine="BUTTON_OKAY.TextColor = Colors.Blue";
mostCurrent._button_okay.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 895;BA.debugLine="BUTTON_SAVE.Text = \" Save\"";
mostCurrent._button_save.setText(BA.ObjectToCharSequence(" Save"));
 //BA.debugLineNum = 896;BA.debugLine="BUTTON_SAVE.TextColor = Colors.RGB(0,255,0)";
mostCurrent._button_save.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (255),(int) (0)));
 //BA.debugLineNum = 897;BA.debugLine="EDITTEXT_DOC_NO.Text = \"\"";
mostCurrent._edittext_doc_no.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 898;BA.debugLine="EDITTEXT_DRIVER.Text= \"\"";
mostCurrent._edittext_driver.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 899;BA.debugLine="EDITTEXT_PLATE.Text= \"\"";
mostCurrent._edittext_plate.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 900;BA.debugLine="EDITTEXT_TRUCKTYPE.Text= \"\"";
mostCurrent._edittext_trucktype.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 901;BA.debugLine="EDITTEXT_TRUCKING.Text= \"\"";
mostCurrent._edittext_trucking.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 903;BA.debugLine="PANEL_BG_DELIVERY.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_delivery.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 906;BA.debugLine="End Sub";
return "";
}
public static void  _button_save_click() throws Exception{
ResumableSub_BUTTON_SAVE_Click rsub = new ResumableSub_BUTTON_SAVE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SAVE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SAVE_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
String _query = "";
String _query2 = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 843;BA.debugLine="If EDITTEXT_PLATE.Text = \"\" Or EDITTEXT_PLATE.Tex";
if (true) break;

case 1:
//if
this.state = 32;
if ((parent.mostCurrent._edittext_plate.getText()).equals("") || parent.mostCurrent._edittext_plate.getText().length()<=5) { 
this.state = 3;
}else if((parent.mostCurrent._edittext_trucking.getText()).equals("") || parent.mostCurrent._edittext_plate.getText().length()<=5) { 
this.state = 5;
}else if((parent.mostCurrent._edittext_trucktype.getText()).equals("") || parent.mostCurrent._edittext_plate.getText().length()<=5) { 
this.state = 7;
}else if((parent.mostCurrent._edittext_driver.getText()).equals("") || parent.mostCurrent._edittext_plate.getText().length()<=5) { 
this.state = 9;
}else if((parent.mostCurrent._edittext_doc_no.getText()).equals("") || parent.mostCurrent._edittext_plate.getText().length()<=5) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 32;
 //BA.debugLineNum = 844;BA.debugLine="ToastMessageShow(\"Please input a valid plate num";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a valid plate number (5+ char)"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 32;
 //BA.debugLineNum = 846;BA.debugLine="ToastMessageShow(\"Please input a valid trucking";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a valid trucking (5+ char)"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 7:
//C
this.state = 32;
 //BA.debugLineNum = 848;BA.debugLine="ToastMessageShow(\"Please input a valid  valid tr";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a valid  valid truck type"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 9:
//C
this.state = 32;
 //BA.debugLineNum = 850;BA.debugLine="ToastMessageShow(\"Please input a valid  driver (";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a valid  driver (5+ char)"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 32;
 //BA.debugLineNum = 852;BA.debugLine="ToastMessageShow(\"Please input a valid  document";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a valid  document number (5+ char)"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 854;BA.debugLine="If BUTTON_SAVE.Text = \" Save\" Then";
if (true) break;

case 14:
//if
this.state = 31;
if ((parent.mostCurrent._button_save.getText()).equals(" Save")) { 
this.state = 16;
}else {
this.state = 24;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 855;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM r";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' and dr_no = '"+parent.mostCurrent._edittext_doc_no.getText().toUpperCase()+"'")));
 //BA.debugLineNum = 856;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 17:
//if
this.state = 22;
if (parent._cursor3.getRowCount()>0) { 
this.state = 19;
}else {
this.state = 21;
}if (true) break;

case 19:
//C
this.state = 22;
 //BA.debugLineNum = 857;BA.debugLine="ToastMessageShow(\"Delivery No. already exist\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Delivery No. already exist"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 859;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO receiving";
parent._connection.ExecNonQuery("INSERT INTO receiving_delivery_table VALUES ('"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"','"+parent.mostCurrent._edittext_doc_no.getText().toUpperCase()+"','"+parent.mostCurrent._edittext_trucking.getText().toUpperCase()+"','"+parent.mostCurrent._edittext_trucktype.getText().toUpperCase()+"','"+parent.mostCurrent._edittext_plate.getText().toUpperCase()+"','"+parent.mostCurrent._edittext_driver.getText().toUpperCase()+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+parent.mostCurrent._login_module._tab_id /*String*/ +"','"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 862;BA.debugLine="ToastMessageShow(\"DR ADDED\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("DR ADDED"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 863;BA.debugLine="EDITTEXT_DOC_NO.Text = \"\"";
parent.mostCurrent._edittext_doc_no.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 864;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 865;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 33;
return;
case 33:
//C
this.state = 22;
;
 //BA.debugLineNum = 866;BA.debugLine="CHECK_DOC";
_check_doc();
 if (true) break;

case 22:
//C
this.state = 31;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 869;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM r";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' and dr_no = '"+parent.mostCurrent._edittext_doc_no.getText().toUpperCase()+"'")));
 //BA.debugLineNum = 870;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 25:
//if
this.state = 30;
if (parent._cursor3.getRowCount()>0) { 
this.state = 27;
}else {
this.state = 29;
}if (true) break;

case 27:
//C
this.state = 30;
 //BA.debugLineNum = 871;BA.debugLine="ToastMessageShow(\"Delivery No. already exist\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Delivery No. already exist"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 873;BA.debugLine="ProgressDialogShow2(\"Updating...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Updating..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 874;BA.debugLine="Dim query As String = \"UPDATE receiving_delive";
_query = "UPDATE receiving_delivery_table SET dr_no = ? , trucking = ?, truck_type = ?, driver = ?, user_info = ? WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' and dr_no = '"+parent._old_doc_no+"'";
 //BA.debugLineNum = 875;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._edittext_doc_no.getText().toUpperCase(),parent.mostCurrent._edittext_trucking.getText().toUpperCase(),parent.mostCurrent._edittext_trucktype.getText().toUpperCase(),parent.mostCurrent._edittext_driver.getText().toUpperCase(),parent.mostCurrent._login_module._username /*String*/ }));
 //BA.debugLineNum = 876;BA.debugLine="Sleep(750)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (750));
this.state = 34;
return;
case 34:
//C
this.state = 30;
;
 //BA.debugLineNum = 877;BA.debugLine="Dim query2 As String = \"UPDATE receiving_disc_";
_query2 = "UPDATE receiving_disc_table SET dr_no = ?  WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' and dr_no = '"+parent._old_doc_no+"'";
 //BA.debugLineNum = 878;BA.debugLine="connection.ExecNonQuery2(query2,Array As Strin";
parent._connection.ExecNonQuery2(_query2,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._edittext_doc_no.getText().toUpperCase()}));
 //BA.debugLineNum = 879;BA.debugLine="Sleep(750)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (750));
this.state = 35;
return;
case 35:
//C
this.state = 30;
;
 //BA.debugLineNum = 880;BA.debugLine="ToastMessageShow(\"Document Updated\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Document Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 881;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 882;BA.debugLine="BUTTON_OKAY.Text = \" Okay\"";
parent.mostCurrent._button_okay.setText(BA.ObjectToCharSequence(" Okay"));
 //BA.debugLineNum = 883;BA.debugLine="BUTTON_OKAY.TextColor = Colors.Blue";
parent.mostCurrent._button_okay.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 884;BA.debugLine="BUTTON_SAVE.Text = \" Save\"";
parent.mostCurrent._button_save.setText(BA.ObjectToCharSequence(" Save"));
 //BA.debugLineNum = 885;BA.debugLine="BUTTON_SAVE.TextColor = Colors.RGB(0,255,0)";
parent.mostCurrent._button_save.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (255),(int) (0)));
 if (true) break;

case 30:
//C
this.state = 31;
;
 if (true) break;

case 31:
//C
this.state = 32;
;
 if (true) break;

case 32:
//C
this.state = -1;
;
 //BA.debugLineNum = 889;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 890;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_search_click() throws Exception{
 //BA.debugLineNum = 838;BA.debugLine="Sub BUTTON_SEARCH_Click";
 //BA.debugLineNum = 839;BA.debugLine="CHECK_PLATE";
_check_plate();
 //BA.debugLineNum = 840;BA.debugLine="End Sub";
return "";
}
public static String  _button_security_cancel_click() throws Exception{
 //BA.debugLineNum = 1547;BA.debugLine="Sub BUTTON_SECURITY_CANCEL_Click";
 //BA.debugLineNum = 1548;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1549;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1550;BA.debugLine="CTRL.HideKeyboard";
mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1551;BA.debugLine="End Sub";
return "";
}
public static void  _button_security_confirm_click() throws Exception{
ResumableSub_BUTTON_SECURITY_CONFIRM_Click rsub = new ResumableSub_BUTTON_SECURITY_CONFIRM_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SECURITY_CONFIRM_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SECURITY_CONFIRM_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1477;BA.debugLine="If EDITTEXT_PASSWORD.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 32;
if ((parent.mostCurrent._edittext_password.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 32;
 //BA.debugLineNum = 1478;BA.debugLine="ToastMessageShow(\"Empty Password\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Empty Password"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1480;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM us";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM users_table WHERE user = '"+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and pass = '"+parent.mostCurrent._edittext_password.getText()+"'")));
 //BA.debugLineNum = 1481;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 6:
//if
this.state = 31;
if (parent._cursor2.getRowCount()>0) { 
this.state = 8;
}else {
this.state = 30;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1482;BA.debugLine="If security_trigger = \"MANUAL TABLE\" Then";
if (true) break;

case 9:
//if
this.state = 28;
if ((parent._security_trigger).equals("MANUAL TABLE")) { 
this.state = 11;
}else if((parent._security_trigger).equals("MANUAL BUTTON")) { 
this.state = 17;
}else if((parent._security_trigger).equals("BELOW SIX")) { 
this.state = 27;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1483;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1484;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 1485;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED IN THE SYSTEM\")";
_ls.Add((Object)("BARCODE NOT REGISTERED IN THE SYSTEM"));
 //BA.debugLineNum = 1486;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 1487;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 1488;BA.debugLine="ls.Add(\"SCANNER CAN READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN READ BARCODE"));
 //BA.debugLineNum = 1489;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 1490;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True)";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1491;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 12;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 1492;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 12:
//if
this.state = 15;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 1493;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, Fal";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1494;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
parent.mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1495;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1496;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 34;
return;
case 34:
//C
this.state = 15;
;
 //BA.debugLineNum = 1497;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 1498;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1499;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 35;
return;
case 35:
//C
this.state = 15;
;
 //BA.debugLineNum = 1500;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1501;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 36;
return;
case 36:
//C
this.state = 15;
;
 //BA.debugLineNum = 1502;BA.debugLine="input_type = \"MANUAL \" & CMB_ACCOUNT.cmbBox.S";
parent._input_type = "MANUAL "+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 1503;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 1504;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1505;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 37;
return;
case 37:
//C
this.state = 15;
;
 //BA.debugLineNum = 1506;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 1507;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 38;
return;
case 38:
//C
this.state = 15;
;
 //BA.debugLineNum = 1508;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 15:
//C
this.state = 28;
;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1511;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1512;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
parent.mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1513;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1514;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1515;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 1516;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 1517;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 1518;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 1519;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 1520;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 1521;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True)";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1522;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 39;
return;
case 39:
//C
this.state = 18;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 1523;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 18:
//if
this.state = 25;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 1524;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 1526;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 40;
return;
case 40:
//C
this.state = 21;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1527;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 21:
//if
this.state = 24;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 23;
}if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1528;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = SearchTemplate";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate._selecteditem /*String*/ ));
 //BA.debugLineNum = 1529;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 41;
return;
case 41:
//C
this.state = 24;
;
 //BA.debugLineNum = 1530;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 1531;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 24;
;
 //BA.debugLineNum = 1532;BA.debugLine="GET_MANUAL_ORDER";
_get_manual_order();
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = 28;
;
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1536;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1537;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
parent.mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1538;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1539;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 28;
;
 //BA.debugLineNum = 1540;BA.debugLine="INPUT_RECEIVED";
_input_received();
 if (true) break;

case 28:
//C
this.state = 31;
;
 if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 1543;BA.debugLine="ToastMessageShow(\"Wrong Password\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Wrong Password"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 31:
//C
this.state = 32;
;
 if (true) break;

case 32:
//C
this.state = -1;
;
 //BA.debugLineNum = 1546;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _calcresult1(String _op) throws Exception{
String _res = "";
 //BA.debugLineNum = 1831;BA.debugLine="Sub CalcResult1(Op As String)";
 //BA.debugLineNum = 1832;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1834;BA.debugLine="Result1 = Result1 + Val";
_result1 = _result1+_val;
 break; }
case 1: {
 //BA.debugLineNum = 1836;BA.debugLine="Result1 = Result1 - Val";
_result1 = _result1-_val;
 break; }
case 2: {
 //BA.debugLineNum = 1838;BA.debugLine="Result1 = Result1 * Val";
_result1 = _result1*_val;
 break; }
case 3: {
 //BA.debugLineNum = 1840;BA.debugLine="Result1 = Result1 / Val";
_result1 = _result1/(double)_val;
 break; }
case 4: {
 //BA.debugLineNum = 1842;BA.debugLine="Result1 = Result1 * Result1";
_result1 = _result1*_result1;
 break; }
case 5: {
 //BA.debugLineNum = 1844;BA.debugLine="Result1 = Sqrt(Result1)";
_result1 = anywheresoftware.b4a.keywords.Common.Sqrt(_result1);
 break; }
case 6: {
 //BA.debugLineNum = 1846;BA.debugLine="If Result1 <> 0 Then";
if (_result1!=0) { 
 //BA.debugLineNum = 1847;BA.debugLine="Result1 = 1 / Result1";
_result1 = 1/(double)_result1;
 };
 break; }
case 7: {
 //BA.debugLineNum = 1850;BA.debugLine="Result1 = Result1 * Val / 100";
_result1 = _result1*_val/(double)100;
 break; }
}
;
 //BA.debugLineNum = 1852;BA.debugLine="Dim res As String = Result1";
_res = BA.NumberToString(_result1);
 //BA.debugLineNum = 1853;BA.debugLine="LABEL_LOAD_ANSWER.Text = res";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence(_res));
 //BA.debugLineNum = 1854;BA.debugLine="End Sub";
return "";
}
public static void  _check_doc() throws Exception{
ResumableSub_CHECK_DOC rsub = new ResumableSub_CHECK_DOC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CHECK_DOC extends BA.ResumableSub {
public ResumableSub_CHECK_DOC(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _i = 0;
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 775;BA.debugLine="LISTVIEW_DR.Clear";
parent.mostCurrent._listview_dr.Clear();
 //BA.debugLineNum = 776;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM rec";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 777;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 778;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step4 = 1;
limit4 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 779;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 780;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 781;BA.debugLine="LISTVIEW_DR.AddTwoLines2(cursor1.GetString(\"dr_";
parent.mostCurrent._listview_dr.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor1.GetString("dr_no")),BA.ObjectToCharSequence(parent._cursor1.GetString("trucking")+"-"+parent._cursor1.GetString("truck_type")+"-"+parent._cursor1.GetString("plate_no")+"-"+parent._cursor1.GetString("driver")+""),(Object)(parent._cursor1.GetString("dr_no")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 783;BA.debugLine="BUTTON_OKAY.Visible = True";
parent.mostCurrent._button_okay.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 785;BA.debugLine="BUTTON_OKAY.Visible = False";
parent.mostCurrent._button_okay.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 787;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _check_plate() throws Exception{
ResumableSub_CHECK_PLATE rsub = new ResumableSub_CHECK_PLATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CHECK_PLATE extends BA.ResumableSub {
public ResumableSub_CHECK_PLATE(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _i = 0;
int step3;
int limit3;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 822;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM rec";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE plate_no = '"+parent.mostCurrent._edittext_plate.getText().toUpperCase()+"'")));
 //BA.debugLineNum = 823;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 824;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 825;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 826;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 827;BA.debugLine="EDITTEXT_DRIVER.Text = cursor2.GetString(\"drive";
parent.mostCurrent._edittext_driver.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("driver")));
 //BA.debugLineNum = 828;BA.debugLine="EDITTEXT_TRUCKING.Text = cursor2.GetString(\"tru";
parent.mostCurrent._edittext_trucking.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("trucking")));
 //BA.debugLineNum = 829;BA.debugLine="EDITTEXT_TRUCKTYPE.Text = cursor2.GetString(\"tr";
parent.mostCurrent._edittext_trucktype.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("truck_type")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 831;BA.debugLine="EDITTEXT_DOC_NO.RequestFocus";
parent.mostCurrent._edittext_doc_no.RequestFocus();
 //BA.debugLineNum = 832;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = 10;
;
 //BA.debugLineNum = 833;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_DOC_NO)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_doc_no.getObject()));
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 835;BA.debugLine="ToastMessageShow(\"No history for this plate numb";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No history for this plate number."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 837;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _clear_input() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 1371;BA.debugLine="Sub CLEAR_INPUT";
 //BA.debugLineNum = 1372;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1373;BA.debugLine="bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (255),(int) (70)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1374;BA.debugLine="BUTTON_ADD.Background = bg";
mostCurrent._button_add.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1375;BA.debugLine="BUTTON_ADD.Text = \" SAVE\"";
mostCurrent._button_add.setText(BA.ObjectToCharSequence(" SAVE"));
 //BA.debugLineNum = 1376;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1377;BA.debugLine="BUTTON_CANCEL.Visible = False";
mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1378;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_inv_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_INV_SelectedIndexChanged rsub = new ResumableSub_CMB_INV_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_INV_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_INV_SelectedIndexChanged(wingan.app.receiving2_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.receiving2_module parent;
int _index;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 2093;BA.debugLine="GET_TOTAL_AMT";
_get_total_amt();
 //BA.debugLineNum = 2094;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 2095;BA.debugLine="LOAD_PER_INVOICE";
_load_per_invoice();
 //BA.debugLineNum = 2096;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 2097;BA.debugLine="GET_TOTAL_CASE";
_get_total_case();
 //BA.debugLineNum = 2098;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 2099;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _cmb_invoice_selectedindexchanged(int _index) throws Exception{
 //BA.debugLineNum = 1115;BA.debugLine="Sub CMB_INVOICE_SelectedIndexChanged (Index As Int";
 //BA.debugLineNum = 1116;BA.debugLine="If default_reading = \"BOTH\" Or default_reading =";
if ((_default_reading).equals("BOTH") || (_default_reading).equals("Expiration Date")) { 
 //BA.debugLineNum = 1117;BA.debugLine="OpenLabel(LABEL_LOAD_EXPIRATION)";
_openlabel(mostCurrent._label_load_expiration);
 }else if((_default_reading).equals("Manufacturing Date")) { 
 //BA.debugLineNum = 1119;BA.debugLine="OpenLabel(LABEL_LOAD_MANUFACTURED)";
_openlabel(mostCurrent._label_load_manufactured);
 }else {
 };
 //BA.debugLineNum = 1123;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_unit_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_UNIT_SelectedIndexChanged rsub = new ResumableSub_CMB_UNIT_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_UNIT_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.receiving2_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.receiving2_module parent;
int _index;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1179;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 1180;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 1181;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1182;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 1183;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 2107;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 2108;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 2109;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 2110;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 2111;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 2112;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 2113;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 2102;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 2103;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 2104;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,receiving2_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 2105;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 2106;BA.debugLine="End Sub";
return null;
}
public static String  _daysbetweendates(long _date1,long _date2) throws Exception{
 //BA.debugLineNum = 1184;BA.debugLine="Sub DaysBetweenDates(Date1 As Long, Date2 As Long)";
 //BA.debugLineNum = 1185;BA.debugLine="Return Floor((Date2 - Date1) / DateTime.TicksPerD";
if (true) return BA.NumberToString(anywheresoftware.b4a.keywords.Common.Floor((_date2-_date1)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerDay));
 //BA.debugLineNum = 1186;BA.debugLine="End Sub";
return "";
}
public static void  _delete_receiving_delivery() throws Exception{
ResumableSub_DELETE_RECEIVING_DELIVERY rsub = new ResumableSub_DELETE_RECEIVING_DELIVERY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RECEIVING_DELIVERY extends BA.ResumableSub {
public ResumableSub_DELETE_RECEIVING_DELIVERY(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2190;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_rece";
_cmd = _createcommand("delete_receiving_delivery",new Object[]{(Object)(parent.mostCurrent._receiving_module._purchase_order_no /*String*/ )});
 //BA.debugLineNum = 2191;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2192;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2193;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 2194;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 2195;BA.debugLine="INSERT_DAILY_DELIVERY";
_insert_daily_delivery();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2197;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 2198;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2199;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2200;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2201;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2202;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 2203;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2204;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2205;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 2206;BA.debugLine="DELETE_RECEIVING_DISC";
_delete_receiving_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 2208;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2209;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 2212;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2213;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static void  _delete_receiving_disc() throws Exception{
ResumableSub_DELETE_RECEIVING_DISC rsub = new ResumableSub_DELETE_RECEIVING_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RECEIVING_DISC extends BA.ResumableSub {
public ResumableSub_DELETE_RECEIVING_DISC(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2115;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2116;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 2117;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Data\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Data"));
 //BA.debugLineNum = 2118;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching Data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching Data..."));
 //BA.debugLineNum = 2119;BA.debugLine="LABEL_MSGBOX1.Text = \"Loading, Please wait...\"";
parent.mostCurrent._label_msgbox1.setText(BA.ObjectToCharSequence("Loading, Please wait..."));
 //BA.debugLineNum = 2120;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_rece";
_cmd = _createcommand("delete_receiving_disc",new Object[]{(Object)(parent.mostCurrent._receiving_module._purchase_order_no /*String*/ )});
 //BA.debugLineNum = 2121;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2122;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2123;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 2124;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 2125;BA.debugLine="INSERT_DAILY_DISC";
_insert_daily_disc();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2127;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 2128;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2129;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2130;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2131;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2132;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 2133;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2134;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2135;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 2136;BA.debugLine="DELETE_RECEIVING_DISC";
_delete_receiving_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 2138;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2139;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 2142;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2143;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_details() throws Exception{
ResumableSub_GET_DETAILS rsub = new ResumableSub_GET_DETAILS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_DETAILS extends BA.ResumableSub {
public ResumableSub_GET_DETAILS(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
String _total_case_order = "";
String _total_case_received = "";
String _balance = "";
int _total_received = 0;
int _total_pcs_order = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _ia = 0;
int step11;
int limit11;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 936;BA.debugLine="Dim total_case_order As String";
_total_case_order = "";
 //BA.debugLineNum = 937;BA.debugLine="Dim total_case_received As String";
_total_case_received = "";
 //BA.debugLineNum = 938;BA.debugLine="Dim balance As String";
_balance = "";
 //BA.debugLineNum = 939;BA.debugLine="Dim total_received As Int";
_total_received = 0;
 //BA.debugLineNum = 940;BA.debugLine="Dim total_pcs_order As Int";
_total_pcs_order = 0;
 //BA.debugLineNum = 942;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 943;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 944;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 946;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT a.*, b.tot";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.total_pcs_received as 'total_pcs_received', c.* FROM "+"(Select * FROM product_table WHERE product_desc = '"+parent.mostCurrent._label_load_description.getText()+"') As c "+"LEFT JOIN "+"(Select *, sum(total_pieces) As 'total_pcs_order' FROM purchase_order_ref_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' GROUP BY po_doc_no, product_description) as a "+"ON a.product_description = c.product_desc "+"LEFT JOIN "+"(Select *, sum(total_pieces) As 'total_pcs_received' FROM receiving_disc_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' GROUP BY po_doc_no, product_description) as b "+"ON b.product_description = c.product_desc")));
 //BA.debugLineNum = 954;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 79;
if (parent._cursor8.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 78;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 955;BA.debugLine="For ia = 0 To cursor8.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 76;
step11 = 1;
limit11 = (int) (parent._cursor8.getRowCount()-1);
_ia = (int) (0) ;
this.state = 80;
if (true) break;

case 80:
//C
this.state = 76;
if ((step11 > 0 && _ia <= limit11) || (step11 < 0 && _ia >= limit11)) this.state = 6;
if (true) break;

case 81:
//C
this.state = 80;
_ia = ((int)(0 + _ia + step11)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 956;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 82;
return;
case 82:
//C
this.state = 7;
;
 //BA.debugLineNum = 957;BA.debugLine="cursor8.Position = ia";
parent._cursor8.setPosition(_ia);
 //BA.debugLineNum = 958;BA.debugLine="If cursor8.GetString(\"total_pcs_received\") = Nu";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor8.GetString("total_pcs_received")== null || (parent._cursor8.GetString("total_pcs_received")).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 959;BA.debugLine="total_received = 0";
_total_received = (int) (0);
 //BA.debugLineNum = 960;BA.debugLine="total_case_received = 0";
_total_case_received = BA.NumberToString(0);
 //BA.debugLineNum = 961;BA.debugLine="total_input = 0";
parent._total_input = (int) (0);
 //BA.debugLineNum = 962;BA.debugLine="Log(cursor8.GetString(\"total_pcs_received\") &";
anywheresoftware.b4a.keywords.Common.LogImpl("789391131",parent._cursor8.GetString("total_pcs_received")+" -",0);
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 964;BA.debugLine="total_received = cursor8.GetString(\"total_pcs_";
_total_received = (int)(Double.parseDouble(parent._cursor8.GetString("total_pcs_received")));
 //BA.debugLineNum = 965;BA.debugLine="total_input = cursor8.GetString(\"total_pcs_rec";
parent._total_input = (int)(Double.parseDouble(parent._cursor8.GetString("total_pcs_received")));
 //BA.debugLineNum = 966;BA.debugLine="total_case_received = Number.Format3((total_re";
_total_case_received = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 967;BA.debugLine="Log(cursor8.GetString(\"total_pcs_received\") &";
anywheresoftware.b4a.keywords.Common.LogImpl("789391136",parent._cursor8.GetString("total_pcs_received")+" +",0);
 if (true) break;
;
 //BA.debugLineNum = 971;BA.debugLine="If cursor8.GetString(\"total_pcs_order\") = Null";

case 12:
//if
this.state = 17;
if (parent._cursor8.GetString("total_pcs_order")== null || (parent._cursor8.GetString("total_pcs_order")).equals("")) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 972;BA.debugLine="total_pcs_order = 0";
_total_pcs_order = (int) (0);
 //BA.debugLineNum = 973;BA.debugLine="total_case_order = 0";
_total_case_order = BA.NumberToString(0);
 //BA.debugLineNum = 974;BA.debugLine="total_order = 0";
parent._total_order = (int) (0);
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 976;BA.debugLine="total_pcs_order = cursor8.GetString(\"total_pcs";
_total_pcs_order = (int)(Double.parseDouble(parent._cursor8.GetString("total_pcs_order")));
 //BA.debugLineNum = 977;BA.debugLine="total_order = cursor8.GetString(\"total_pcs_ord";
parent._total_order = (int)(Double.parseDouble(parent._cursor8.GetString("total_pcs_order")));
 //BA.debugLineNum = 978;BA.debugLine="total_case_order = Number.Format3((total_pcs_o";
_total_case_order = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_pcs_order/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 981;BA.debugLine="balance = Number.Format3(((total_received - tot";
_balance = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-_total_pcs_order)/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 984;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor8.GetString(\"c.";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("c.product_variant")));
 //BA.debugLineNum = 985;BA.debugLine="principal_id = cursor8.GetString(\"c.principal_i";
parent._principal_id = parent._cursor8.GetString("c.principal_id");
 //BA.debugLineNum = 986;BA.debugLine="product_id = cursor8.GetString(\"c.product_id\")";
parent._product_id = parent._cursor8.GetString("c.product_id");
 //BA.debugLineNum = 988;BA.debugLine="If total_case_order.SubString(total_case_order.";
if (true) break;

case 18:
//if
this.state = 23;
if ((double)(Double.parseDouble(_total_case_order.substring((int) (_total_case_order.indexOf(".")+1))))>0) { 
this.state = 20;
}else {
this.state = 22;
}if (true) break;

case 20:
//C
this.state = 23;
 //BA.debugLineNum = 989;BA.debugLine="LABEL_LOAD_ORDER.Text = Number.Format3((total_";
parent.mostCurrent._label_load_order.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_pcs_order/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15))+" CASE"));
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 991;BA.debugLine="LABEL_LOAD_ORDER.Text = Number.Format3((total_";
parent.mostCurrent._label_load_order.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_pcs_order/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15))+" CASE"));
 if (true) break;
;
 //BA.debugLineNum = 993;BA.debugLine="If total_case_received.SubString(total_case_rec";

case 23:
//if
this.state = 28;
if ((double)(Double.parseDouble(_total_case_received.substring((int) (_total_case_received.indexOf(".")+1))))>0) { 
this.state = 25;
}else {
this.state = 27;
}if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 994;BA.debugLine="LABEL_LOAD_INPUT.Text = Number.Format3((total_";
parent.mostCurrent._label_load_input.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15))+" CASE"));
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 996;BA.debugLine="LABEL_LOAD_INPUT.Text = Number.Format3((total_";
parent.mostCurrent._label_load_input.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15))+" CASE"));
 if (true) break;
;
 //BA.debugLineNum = 998;BA.debugLine="If balance.SubString(balance.IndexOf(\".\")+1) >";

case 28:
//if
this.state = 33;
if ((double)(Double.parseDouble(_balance.substring((int) (_balance.indexOf(".")+1))))>0) { 
this.state = 30;
}else {
this.state = 32;
}if (true) break;

case 30:
//C
this.state = 33;
 //BA.debugLineNum = 999;BA.debugLine="LABEL_LOAD_BALANCE.Text = Number.Format3(((tot";
parent.mostCurrent._label_load_balance.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-_total_pcs_order)/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15))+" CASE"));
 if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1001;BA.debugLine="LABEL_LOAD_BALANCE.Text = Number.Format3(((tot";
parent.mostCurrent._label_load_balance.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-_total_pcs_order)/(double)(double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15))+" CASE"));
 if (true) break;
;
 //BA.debugLineNum = 1004;BA.debugLine="If balance > 0 Then";

case 33:
//if
this.state = 40;
if ((double)(Double.parseDouble(_balance))>0) { 
this.state = 35;
}else if((double)(Double.parseDouble(_balance))<0) { 
this.state = 37;
}else {
this.state = 39;
}if (true) break;

case 35:
//C
this.state = 40;
 //BA.debugLineNum = 1005;BA.debugLine="LABEL_LOAD_BALANCE.Color = Colors.Blue";
parent.mostCurrent._label_load_balance.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 if (true) break;

case 37:
//C
this.state = 40;
 //BA.debugLineNum = 1007;BA.debugLine="LABEL_LOAD_BALANCE.Color = Colors.Red";
parent.mostCurrent._label_load_balance.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 1009;BA.debugLine="LABEL_LOAD_BALANCE.Color = Colors.Green";
parent.mostCurrent._label_load_balance.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 if (true) break;

case 40:
//C
this.state = 41;
;
 //BA.debugLineNum = 1012;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1013;BA.debugLine="If cursor8.GetString(\"CASE_UNIT_PER_PCS\") > 0 T";
if (true) break;

case 41:
//if
this.state = 44;
if ((double)(Double.parseDouble(parent._cursor8.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 43;
}if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 1014;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 1016;BA.debugLine="If cursor8.GetString(\"PCS_UNIT_PER_PCS\") > 0 Th";

case 44:
//if
this.state = 47;
if ((double)(Double.parseDouble(parent._cursor8.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 1017;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 1019;BA.debugLine="If cursor8.GetString(\"DOZ_UNIT_PER_PCS\") > 0 Th";

case 47:
//if
this.state = 50;
if ((double)(Double.parseDouble(parent._cursor8.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 49;
}if (true) break;

case 49:
//C
this.state = 50;
 //BA.debugLineNum = 1020;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 1022;BA.debugLine="If cursor8.GetString(\"BOX_UNIT_PER_PCS\") > 0 Th";

case 50:
//if
this.state = 53;
if ((double)(Double.parseDouble(parent._cursor8.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 52;
}if (true) break;

case 52:
//C
this.state = 53;
 //BA.debugLineNum = 1023;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 1025;BA.debugLine="If cursor8.GetString(\"BAG_UNIT_PER_PCS\") > 0 Th";

case 53:
//if
this.state = 56;
if ((double)(Double.parseDouble(parent._cursor8.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 55;
}if (true) break;

case 55:
//C
this.state = 56;
 //BA.debugLineNum = 1026;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 1028;BA.debugLine="If cursor8.GetString(\"PACK_UNIT_PER_PCS\") > 0 T";

case 56:
//if
this.state = 59;
if ((double)(Double.parseDouble(parent._cursor8.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 58;
}if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 1029;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 59:
//C
this.state = 60;
;
 //BA.debugLineNum = 1031;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 83;
return;
case 83:
//C
this.state = 60;
;
 //BA.debugLineNum = 1032;BA.debugLine="If scan_code.Trim = cursor8.GetString(\"case_bar";
if (true) break;

case 60:
//if
this.state = 63;
if ((parent._scan_code.trim()).equals(parent._cursor8.GetString("case_bar_code"))) { 
this.state = 62;
}if (true) break;

case 62:
//C
this.state = 63;
 //BA.debugLineNum = 1033;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE"));
 if (true) break;
;
 //BA.debugLineNum = 1035;BA.debugLine="If scan_code.Trim = cursor8.GetString(\"bar_code";

case 63:
//if
this.state = 66;
if ((parent._scan_code.trim()).equals(parent._cursor8.GetString("bar_code"))) { 
this.state = 65;
}if (true) break;

case 65:
//C
this.state = 66;
 //BA.debugLineNum = 1036;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS"));
 if (true) break;
;
 //BA.debugLineNum = 1038;BA.debugLine="If scan_code.Trim = cursor8.GetString(\"box_bar_";

case 66:
//if
this.state = 69;
if ((parent._scan_code.trim()).equals(parent._cursor8.GetString("box_bar_code"))) { 
this.state = 68;
}if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 1039;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX"));
 if (true) break;
;
 //BA.debugLineNum = 1041;BA.debugLine="If scan_code.Trim = cursor8.GetString(\"pack_bar";

case 69:
//if
this.state = 72;
if ((parent._scan_code.trim()).equals(parent._cursor8.GetString("pack_bar_code"))) { 
this.state = 71;
}if (true) break;

case 71:
//C
this.state = 72;
 //BA.debugLineNum = 1042;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK"));
 if (true) break;
;
 //BA.debugLineNum = 1044;BA.debugLine="If scan_code.Trim = cursor8.GetString(\"bag_bar_";

case 72:
//if
this.state = 75;
if ((parent._scan_code.trim()).equals(parent._cursor8.GetString("bag_bar_code"))) { 
this.state = 74;
}if (true) break;

case 74:
//C
this.state = 75;
 //BA.debugLineNum = 1045;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG"));
 if (true) break;

case 75:
//C
this.state = 81;
;
 //BA.debugLineNum = 1049;BA.debugLine="caseper = cursor8.GetString(\"CASE_UNIT_PER_PCS\"";
parent._caseper = parent._cursor8.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1050;BA.debugLine="pcsper = cursor8.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor8.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1051;BA.debugLine="dozper = cursor8.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor8.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1052;BA.debugLine="boxper = cursor8.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor8.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1053;BA.debugLine="bagper = cursor8.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor8.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1054;BA.debugLine="packper = cursor8.GetString(\"PACK_UNIT_PER_PCS\"";
parent._packper = parent._cursor8.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 1055;BA.debugLine="price = cursor8.GetString(\"PCS_COMPANY\")";
parent._price = (int)(Double.parseDouble(parent._cursor8.GetString("PCS_COMPANY")));
 //BA.debugLineNum = 1057;BA.debugLine="default_reading = cursor8.GetString(\"default_ex";
parent._default_reading = parent._cursor8.GetString("default_expiration_date_reading");
 //BA.debugLineNum = 1058;BA.debugLine="lifespan_year = cursor8.GetString(\"life_span_ye";
parent._lifespan_year = parent._cursor8.GetString("life_span_year");
 //BA.debugLineNum = 1059;BA.debugLine="lifespan_month = cursor8.GetString(\"life_span_m";
parent._lifespan_month = parent._cursor8.GetString("life_span_month");
 if (true) break;
if (true) break;

case 76:
//C
this.state = 79;
;
 //BA.debugLineNum = 1061;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 78:
//C
this.state = 79;
 //BA.debugLineNum = 1063;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1064;BA.debugLine="ToastMessageShow(\"Data is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Data is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 79:
//C
this.state = -1;
;
 //BA.debugLineNum = 1067;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_exp() throws Exception{
 //BA.debugLineNum = 1221;BA.debugLine="Sub GET_EXP";
 //BA.debugLineNum = 1222;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = \"";
if ((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("01")) { 
 //BA.debugLineNum = 1223;BA.debugLine="monthexp = \"January\"";
_monthexp = "January";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("02")) { 
 //BA.debugLineNum = 1225;BA.debugLine="monthexp = \"February\"";
_monthexp = "February";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("03")) { 
 //BA.debugLineNum = 1227;BA.debugLine="monthexp = \"March\"";
_monthexp = "March";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("04")) { 
 //BA.debugLineNum = 1229;BA.debugLine="monthexp = \"April\"";
_monthexp = "April";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("05")) { 
 //BA.debugLineNum = 1231;BA.debugLine="monthexp = \"May\"";
_monthexp = "May";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("06")) { 
 //BA.debugLineNum = 1233;BA.debugLine="monthexp = \"June\"";
_monthexp = "June";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("07")) { 
 //BA.debugLineNum = 1235;BA.debugLine="monthexp = \"July\"";
_monthexp = "July";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("08")) { 
 //BA.debugLineNum = 1237;BA.debugLine="monthexp = \"August\"";
_monthexp = "August";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("09")) { 
 //BA.debugLineNum = 1239;BA.debugLine="monthexp = \"September\"";
_monthexp = "September";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("10")) { 
 //BA.debugLineNum = 1241;BA.debugLine="monthexp = \"October\"";
_monthexp = "October";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("11")) { 
 //BA.debugLineNum = 1243;BA.debugLine="monthexp = \"November\"";
_monthexp = "November";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("12")) { 
 //BA.debugLineNum = 1245;BA.debugLine="monthexp = \"December\"";
_monthexp = "December";
 }else {
 //BA.debugLineNum = 1247;BA.debugLine="monthexp = \"NO EXPIRATION\"";
_monthexp = "NO EXPIRATION";
 };
 //BA.debugLineNum = 1250;BA.debugLine="yearexp = LABEL_LOAD_EXPIRATION.Text.SubString2(0";
_yearexp = mostCurrent._label_load_expiration.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 1251;BA.debugLine="End Sub";
return "";
}
public static String  _get_expiration_span() throws Exception{
String _days_year = "";
String _days_month = "";
long _manufacturing = 0L;
 //BA.debugLineNum = 1124;BA.debugLine="Sub GET_EXPIRATION_SPAN";
 //BA.debugLineNum = 1125;BA.debugLine="Dim days_year As String";
_days_year = "";
 //BA.debugLineNum = 1126;BA.debugLine="Dim days_month As String";
_days_month = "";
 //BA.debugLineNum = 1128;BA.debugLine="If lifespan_year <> \"\" Then";
if ((_lifespan_year).equals("") == false) { 
 //BA.debugLineNum = 1129;BA.debugLine="days_year = lifespan_year.SubString2(0,lifespan_";
_days_year = _lifespan_year.substring((int) (0),(int) (_lifespan_year.indexOf("Y")-1));
 }else {
 //BA.debugLineNum = 1131;BA.debugLine="days_year = 0";
_days_year = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1134;BA.debugLine="If lifespan_month <> \"\" Then";
if ((_lifespan_month).equals("") == false) { 
 //BA.debugLineNum = 1135;BA.debugLine="days_month = lifespan_month.SubString2(0,lifespa";
_days_month = _lifespan_month.substring((int) (0),(int) (_lifespan_month.indexOf("M")-1));
 }else {
 //BA.debugLineNum = 1137;BA.debugLine="days_month = 0";
_days_month = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1140;BA.debugLine="Log(days_year & \" \" &days_month)";
anywheresoftware.b4a.keywords.Common.LogImpl("789849872",_days_year+" "+_days_month,0);
 //BA.debugLineNum = 1142;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text <> \"NO EXPIRATION\"";
if ((mostCurrent._label_load_expiration.getText()).equals("NO EXPIRATION") == false) { 
 //BA.debugLineNum = 1143;BA.debugLine="If lifespan_year = \"\" And lifespan_month = \"\" Th";
if ((_lifespan_year).equals("") && (_lifespan_month).equals("")) { 
 }else {
 //BA.debugLineNum = 1146;BA.debugLine="Dim manufacturing As Long = DateTime.Add(DateTi";
_manufacturing = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_expiration.getText()),(int) (-(double)(Double.parseDouble(_days_year))),(int) (-(double)(Double.parseDouble(_days_month))),(int) (0));
 //BA.debugLineNum = 1147;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(ma";
mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_manufacturing)));
 };
 };
 //BA.debugLineNum = 1150;BA.debugLine="End Sub";
return "";
}
public static void  _get_manual_order() throws Exception{
ResumableSub_GET_MANUAL_ORDER rsub = new ResumableSub_GET_MANUAL_ORDER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_MANUAL_ORDER extends BA.ResumableSub {
public ResumableSub_GET_MANUAL_ORDER(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _row = 0;
int _result = 0;
int step3;
int limit3;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1570;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM pur";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM purchase_order_ref_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 1572;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 20;
if (parent._cursor7.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1573;BA.debugLine="For row = 0 To cursor7.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor7.getRowCount()-1);
_row = (int) (0) ;
this.state = 21;
if (true) break;

case 21:
//C
this.state = 7;
if ((step3 > 0 && _row <= limit3) || (step3 < 0 && _row >= limit3)) this.state = 6;
if (true) break;

case 22:
//C
this.state = 21;
_row = ((int)(0 + _row + step3)) ;
if (true) break;

case 6:
//C
this.state = 22;
 //BA.debugLineNum = 1574;BA.debugLine="cursor7.Position = row";
parent._cursor7.setPosition(_row);
 //BA.debugLineNum = 1575;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1576;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 23;
return;
case 23:
//C
this.state = 22;
;
 //BA.debugLineNum = 1577;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1578;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 24;
return;
case 24:
//C
this.state = 22;
;
 //BA.debugLineNum = 1579;BA.debugLine="input_type = \"MANUAL \" & CMB_ACCOUNT.cmbBox.Sel";
parent._input_type = "MANUAL "+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 1580;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 1581;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1582;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 25;
return;
case 25:
//C
this.state = 22;
;
 //BA.debugLineNum = 1583;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 1584;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 26;
return;
case 26:
//C
this.state = 22;
;
 //BA.debugLineNum = 1585;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;
if (true) break;

case 7:
//C
this.state = 20;
;
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1589;BA.debugLine="If RECEIVING_MODULE.transaction_type = \"PURCHASE";
if (true) break;

case 10:
//if
this.state = 19;
if ((parent.mostCurrent._receiving_module._transaction_type /*String*/ ).equals("PURCHASE ORDER")) { 
this.state = 12;
}else if((parent.mostCurrent._receiving_module._transaction_type /*String*/ ).equals("AUTO SHIP")) { 
this.state = 18;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1590;BA.debugLine="Msgbox2Async(\"The produt you scanned (\"&LABEL_L";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The produt you scanned ("+parent.mostCurrent._label_load_description.getText()+") is not IN THE PURCHASE ORDER. Do you want to received this product?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1591;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 13;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1592;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 13:
//if
this.state = 16;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1593;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1594;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 28;
return;
case 28:
//C
this.state = 16;
;
 //BA.debugLineNum = 1595;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1596;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 16;
;
 //BA.debugLineNum = 1597;BA.debugLine="input_type = \"MANUAL \" & CMB_ACCOUNT.cmbBox.Se";
parent._input_type = "MANUAL "+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 1598;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 1599;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1600;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 30;
return;
case 30:
//C
this.state = 16;
;
 //BA.debugLineNum = 1601;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 1602;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 31;
return;
case 31:
//C
this.state = 16;
;
 //BA.debugLineNum = 1603;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 16:
//C
this.state = 19;
;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1606;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1607;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 32;
return;
case 32:
//C
this.state = 19;
;
 //BA.debugLineNum = 1608;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1609;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 33;
return;
case 33:
//C
this.state = 19;
;
 //BA.debugLineNum = 1610;BA.debugLine="input_type = \"MANUAL \" & CMB_ACCOUNT.cmbBox.Sel";
parent._input_type = "MANUAL "+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 1611;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 1612;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1613;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 34;
return;
case 34:
//C
this.state = 19;
;
 //BA.debugLineNum = 1614;BA.debugLine="LOAD_DOCUMENT";
_load_document();
 //BA.debugLineNum = 1615;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 19;
;
 //BA.debugLineNum = 1616;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 19:
//C
this.state = 20;
;
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 1619;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_manufacturing_span() throws Exception{
String _days_year = "";
String _days_month = "";
long _expiration = 0L;
 //BA.debugLineNum = 1151;BA.debugLine="Sub GET_MANUFACTURING_SPAN";
 //BA.debugLineNum = 1152;BA.debugLine="Dim days_year As String";
_days_year = "";
 //BA.debugLineNum = 1153;BA.debugLine="Dim days_month As String";
_days_month = "";
 //BA.debugLineNum = 1155;BA.debugLine="If lifespan_year <> \"\" Then";
if ((_lifespan_year).equals("") == false) { 
 //BA.debugLineNum = 1156;BA.debugLine="days_year = lifespan_year.SubString2(0,lifespan_";
_days_year = _lifespan_year.substring((int) (0),(int) (_lifespan_year.indexOf("Y")-1));
 }else {
 //BA.debugLineNum = 1158;BA.debugLine="days_year = 0";
_days_year = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1161;BA.debugLine="If lifespan_month <> \"\" Then";
if ((_lifespan_month).equals("") == false) { 
 //BA.debugLineNum = 1162;BA.debugLine="days_month = lifespan_month.SubString2(0,lifespa";
_days_month = _lifespan_month.substring((int) (0),(int) (_lifespan_month.indexOf("M")-1));
 }else {
 //BA.debugLineNum = 1164;BA.debugLine="days_month = 0";
_days_month = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1167;BA.debugLine="Log(days_year & \" \" &days_month)";
anywheresoftware.b4a.keywords.Common.LogImpl("789915408",_days_year+" "+_days_month,0);
 //BA.debugLineNum = 1169;BA.debugLine="If LABEL_LOAD_MANUFACTURED.Text <> \"NO EXPIRATION";
if ((mostCurrent._label_load_manufactured.getText()).equals("NO EXPIRATION") == false) { 
 //BA.debugLineNum = 1170;BA.debugLine="If lifespan_year = \"\" And lifespan_month = \"\" Th";
if ((_lifespan_year).equals("") && (_lifespan_month).equals("")) { 
 }else {
 //BA.debugLineNum = 1173;BA.debugLine="Dim expiration As Long = DateTime.Add(DateTime.";
_expiration = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_manufactured.getText()),(int)(Double.parseDouble(_days_year)),(int)(Double.parseDouble(_days_month)),(int) (0));
 //BA.debugLineNum = 1174;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = DateTime.Date(expi";
mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_expiration)));
 };
 };
 //BA.debugLineNum = 1177;BA.debugLine="End Sub";
return "";
}
public static String  _get_mfg() throws Exception{
 //BA.debugLineNum = 1252;BA.debugLine="Sub GET_MFG";
 //BA.debugLineNum = 1253;BA.debugLine="If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) =";
if ((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("01")) { 
 //BA.debugLineNum = 1254;BA.debugLine="monthmfg = \"January\"";
_monthmfg = "January";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("02")) { 
 //BA.debugLineNum = 1256;BA.debugLine="monthmfg = \"February\"";
_monthmfg = "February";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("03")) { 
 //BA.debugLineNum = 1258;BA.debugLine="monthmfg = \"March\"";
_monthmfg = "March";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("04")) { 
 //BA.debugLineNum = 1260;BA.debugLine="monthmfg = \"April\"";
_monthmfg = "April";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("05")) { 
 //BA.debugLineNum = 1262;BA.debugLine="monthmfg = \"May\"";
_monthmfg = "May";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("06")) { 
 //BA.debugLineNum = 1264;BA.debugLine="monthmfg = \"June\"";
_monthmfg = "June";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("07")) { 
 //BA.debugLineNum = 1266;BA.debugLine="monthmfg = \"July\"";
_monthmfg = "July";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("08")) { 
 //BA.debugLineNum = 1268;BA.debugLine="monthmfg = \"August\"";
_monthmfg = "August";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("09")) { 
 //BA.debugLineNum = 1270;BA.debugLine="monthmfg = \"September\"";
_monthmfg = "September";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("10")) { 
 //BA.debugLineNum = 1272;BA.debugLine="monthmfg = \"October\"";
_monthmfg = "October";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("11")) { 
 //BA.debugLineNum = 1274;BA.debugLine="monthmfg = \"November\"";
_monthmfg = "November";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("12")) { 
 //BA.debugLineNum = 1276;BA.debugLine="monthmfg = \"December\"";
_monthmfg = "December";
 }else {
 //BA.debugLineNum = 1278;BA.debugLine="monthmfg = \"NO MANUFACTURING\"";
_monthmfg = "NO MANUFACTURING";
 };
 //BA.debugLineNum = 1281;BA.debugLine="yearmfg = LABEL_LOAD_MANUFACTURED.Text.SubString2";
_yearmfg = mostCurrent._label_load_manufactured.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 1282;BA.debugLine="End Sub";
return "";
}
public static void  _get_security() throws Exception{
ResumableSub_GET_SECURITY rsub = new ResumableSub_GET_SECURITY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_SECURITY extends BA.ResumableSub {
public ResumableSub_GET_SECURITY(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _i = 0;
int step3;
int limit3;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1468;BA.debugLine="CMB_ACCOUNT.cmbBox.Clear";
parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1469;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table WHERE Position LIKE '%LOGISTIC OFFICER%' ORDER BY User ASC")));
 //BA.debugLineNum = 1470;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1471;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 1472;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 1473;BA.debugLine="CMB_ACCOUNT.cmbBox.Add(cursor3.GetString(\"User\")";
parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor3.GetString("User"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1475;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_total_amt() throws Exception{
int _row = 0;
 //BA.debugLineNum = 1970;BA.debugLine="Sub GET_TOTAL_AMT";
 //BA.debugLineNum = 1971;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT sum(amount";
_cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT sum(amount) as 'amt' FROM receiving_disc_table WHERE dr_no = '"+mostCurrent._cmb_inv._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' AND po_doc_no = '"+mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 1972;BA.debugLine="If cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 1973;BA.debugLine="For row = 0 To cursor1.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor1.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit3 ;_row = _row + step3 ) {
 //BA.debugLineNum = 1974;BA.debugLine="cursor1.Position = row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 1975;BA.debugLine="If cursor1.GetString(\"amt\") = Null Then";
if (_cursor1.GetString("amt")== null) { 
 //BA.debugLineNum = 1976;BA.debugLine="LABEL_LOAD_TOTALAMT.text = \"₱0\"";
mostCurrent._label_load_totalamt.setText(BA.ObjectToCharSequence("₱0"));
 }else {
 //BA.debugLineNum = 1978;BA.debugLine="LABEL_LOAD_TOTALAMT.text = \"₱\" & Number.Format";
mostCurrent._label_load_totalamt.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(_cursor1.GetString("amt"))),(int) (0),(int) (2),(int) (2),".",",",",",(int) (0),(int) (15))));
 };
 }
};
 }else {
 };
 //BA.debugLineNum = 1984;BA.debugLine="End Sub";
return "";
}
public static String  _get_total_case() throws Exception{
String _total_case_dr = "";
int _row = 0;
 //BA.debugLineNum = 1985;BA.debugLine="Sub GET_TOTAL_CASE";
 //BA.debugLineNum = 1986;BA.debugLine="Dim total_case_dr As String";
_total_case_dr = "";
 //BA.debugLineNum = 1987;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT sum(total_";
_cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT sum(total_case) as 'total_case_dr' FROM "+"(Select a.*, sum(total_pieces) As 'tot_pcs', b.CASE_UNIT_PER_PCS, (sum(total_pieces) / b.CASE_UNIT_PER_PCS) as 'total_case' FROM "+"receiving_disc_table As a "+"LEFT JOIN "+"product_table As b "+"ON a.product_id = b.product_id "+"WHERE a.dr_no = '"+mostCurrent._cmb_inv._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' AND a.po_doc_no = '"+mostCurrent._receiving_module._purchase_order_no /*String*/ +"' "+"GROUP BY a.product_id, a.unit) ")));
 //BA.debugLineNum = 1995;BA.debugLine="If cursor4.RowCount > 0 Then";
if (_cursor4.getRowCount()>0) { 
 //BA.debugLineNum = 1996;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_cursor4.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit4 ;_row = _row + step4 ) {
 //BA.debugLineNum = 1997;BA.debugLine="cursor4.Position = row";
_cursor4.setPosition(_row);
 //BA.debugLineNum = 1998;BA.debugLine="If cursor4.GetString(\"total_case_dr\") = Null Th";
if (_cursor4.GetString("total_case_dr")== null) { 
 //BA.debugLineNum = 1999;BA.debugLine="LABEL_LOAD_TOTALCASE.text = \"0\"";
mostCurrent._label_load_totalcase.setText(BA.ObjectToCharSequence("0"));
 }else {
 //BA.debugLineNum = 2001;BA.debugLine="total_case_dr = Number.Format3(cursor4.GetStri";
_total_case_dr = mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(_cursor4.GetString("total_case_dr"))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 2003;BA.debugLine="If total_case_dr.SubString(total_case_dr.Index";
if ((double)(Double.parseDouble(_total_case_dr.substring((int) (_total_case_dr.indexOf(".")+1))))>0) { 
 //BA.debugLineNum = 2004;BA.debugLine="LABEL_LOAD_TOTALCASE.text = Number.Format3(cu";
mostCurrent._label_load_totalcase.setText(BA.ObjectToCharSequence(mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(_cursor4.GetString("total_case_dr"))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15))));
 }else {
 //BA.debugLineNum = 2006;BA.debugLine="LABEL_LOAD_TOTALCASE.text = Number.Format3(cu";
mostCurrent._label_load_totalcase.setText(BA.ObjectToCharSequence(mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(_cursor4.GetString("total_case_dr"))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15))));
 };
 };
 }
};
 }else {
 };
 //BA.debugLineNum = 2013;BA.debugLine="End Sub";
return "";
}
public static String  _get_transaction_number() throws Exception{
int _i = 0;
 //BA.debugLineNum = 1283;BA.debugLine="Sub GET_TRANSACTION_NUMBER";
 //BA.debugLineNum = 1284;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT MAX(CAST(t";
_cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as transaction_number from receiving_disc_table WHERE po_doc_no = '"+mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 1285;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
{
final int step2 = 1;
final int limit2 = (int) (_cursor2.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 1286;BA.debugLine="cursor2.Position = i";
_cursor2.setPosition(_i);
 //BA.debugLineNum = 1287;BA.debugLine="If cursor2.GetString(\"transaction_number\") = Nul";
if (_cursor2.GetString("transaction_number")== null || (_cursor2.GetString("transaction_number")).equals("")) { 
 //BA.debugLineNum = 1288;BA.debugLine="transaction_number =1";
_transaction_number = BA.NumberToString(1);
 }else {
 //BA.debugLineNum = 1290;BA.debugLine="transaction_number = cursor2.GetString(\"transac";
_transaction_number = BA.NumberToString((double)(Double.parseDouble(_cursor2.GetString("transaction_number")))+1);
 };
 }
};
 //BA.debugLineNum = 1293;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 348;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 349;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 350;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 351;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 352;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 355;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 356;BA.debugLine="End Sub";
return null;
}
public static String  _getvalue(String _op) throws Exception{
 //BA.debugLineNum = 1747;BA.debugLine="Sub GetValue(Op As String)";
 //BA.debugLineNum = 1748;BA.debugLine="If Op0 = \"e\" And (Op = \"s\" Or Op = \"g\" Or Op = \"x";
if ((_op0).equals("e") && ((_op).equals("s") || (_op).equals("g") || (_op).equals("x"))) { 
 //BA.debugLineNum = 1749;BA.debugLine="Val = Result1";
_val = _result1;
 }else {
 //BA.debugLineNum = 1751;BA.debugLine="Val = sVal";
_val = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1754;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1756;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1757;BA.debugLine="If Op0 = \"a\" Or Op0 = \"b\" Or Op0 = \"c\" Or Op0 =";
if ((_op0).equals("a") || (_op0).equals("b") || (_op0).equals("c") || (_op0).equals("d") || (_op0).equals("y")) { 
 //BA.debugLineNum = 1758;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1759;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1761;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1762;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1763;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1764;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1765;BA.debugLine="Op0 = \"e\"";
_op0 = "e";
 //BA.debugLineNum = 1766;BA.debugLine="Op = \"e\"";
_op = "e";
 //BA.debugLineNum = 1767;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1770;BA.debugLine="If New1 = 1 Then";
if (_new1==1) { 
 //BA.debugLineNum = 1771;BA.debugLine="Result1 = Val";
_result1 = _val;
 //BA.debugLineNum = 1772;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1773;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1774;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1775;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1776;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 //BA.debugLineNum = 1778;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1779;BA.debugLine="New1 = 2";
_new1 = (int) (2);
 //BA.debugLineNum = 1780;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1781;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1784;BA.debugLine="If Op = \"e\" Then";
if ((_op).equals("e")) { 
 //BA.debugLineNum = 1785;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1786;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1788;BA.debugLine="Txt = Txt & CRLF & \" =  \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+" =  ";
 //BA.debugLineNum = 1789;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1790;BA.debugLine="Txt = Txt & Result1";
_txt = _txt+BA.NumberToString(_result1);
 }else {
 //BA.debugLineNum = 1792;BA.debugLine="If Op0 = \"g\" Or Op0 = \"s\" Or Op0 = \"x\" Then";
if ((_op0).equals("g") || (_op0).equals("s") || (_op0).equals("x")) { 
 //BA.debugLineNum = 1793;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1794;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1795;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1798;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1799;BA.debugLine="If Op0<>\"e\" Then";
if ((_op0).equals("e") == false) { 
 //BA.debugLineNum = 1800;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1802;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1803;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1804;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1805;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1806;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 };
 //BA.debugLineNum = 1809;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1810;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 90;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 92;BA.debugLine="Dim phone As Phone";
mostCurrent._phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 94;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 95;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 96;BA.debugLine="Private NameColumn(5) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (5)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 97;BA.debugLine="Private NameColumn2(5) As B4XTableColumn";
mostCurrent._namecolumn2 = new wingan.app.b4xtable._b4xtablecolumn[(int) (5)];
{
int d0 = mostCurrent._namecolumn2.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn2[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 99;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 100;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 103;BA.debugLine="Private TABLE_PURCHASE_ORDER As B4XTable";
mostCurrent._table_purchase_order = new wingan.app.b4xtable();
 //BA.debugLineNum = 106;BA.debugLine="Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn";
mostCurrent._btn0 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn3 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn4 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn5 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn6 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn7 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn8 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn9 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Dim btnBack, btnClr, btnExit As Button";
mostCurrent._btnback = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnclr = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 108;BA.debugLine="Dim lblPaperRoll As Label";
mostCurrent._lblpaperroll = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Dim scvPaperRoll As ScrollView";
mostCurrent._scvpaperroll = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 110;BA.debugLine="Dim pnlKeyboard As Panel";
mostCurrent._pnlkeyboard = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Dim stu As StringUtils";
mostCurrent._stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 112;BA.debugLine="Private LABEL_LOAD_ANSWER As Label";
mostCurrent._label_load_answer = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 113;BA.debugLine="Private PANEL_BG_CALCU As Panel";
mostCurrent._panel_bg_calcu = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 118;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 121;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 122;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private DateTemplate As B4XDateTemplate";
mostCurrent._datetemplate = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 124;BA.debugLine="Private DateTemplate2 As B4XDateTemplate";
mostCurrent._datetemplate2 = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 125;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 127;BA.debugLine="Private LABEL_LOAD_PRINCIPAL As Label";
mostCurrent._label_load_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Private PANEL_BG_DELIVERY As Panel";
mostCurrent._panel_bg_delivery = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private BUTTON_OKAY As Button";
mostCurrent._button_okay = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Private LISTVIEW_DR As ListView";
mostCurrent._listview_dr = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Private EDITTEXT_DOC_NO As EditText";
mostCurrent._edittext_doc_no = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 132;BA.debugLine="Private EDITTEXT_DRIVER As EditText";
mostCurrent._edittext_driver = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 133;BA.debugLine="Private EDITTEXT_TRUCKING As EditText";
mostCurrent._edittext_trucking = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 134;BA.debugLine="Private EDITTEXT_PLATE As EditText";
mostCurrent._edittext_plate = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 135;BA.debugLine="Private EDITTEXT_TRUCKTYPE As EditText";
mostCurrent._edittext_trucktype = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 136;BA.debugLine="Private BUTTON_SAVE As Button";
mostCurrent._button_save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 138;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 139;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 140;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 141;BA.debugLine="Private LABEL_LOAD_ORDER As Label";
mostCurrent._label_load_order = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 142;BA.debugLine="Private LABEL_LOAD_INPUT As Label";
mostCurrent._label_load_input = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Private LABEL_LOAD_BALANCE As Label";
mostCurrent._label_load_balance = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 144;BA.debugLine="Private CMB_INVOICE As B4XComboBox";
mostCurrent._cmb_invoice = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 145;BA.debugLine="Private PANEL_BG_INPUT As Panel";
mostCurrent._panel_bg_input = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 146;BA.debugLine="Private LABEL_LOAD_MANUFACTURED As Label";
mostCurrent._label_load_manufactured = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 147;BA.debugLine="Private LABEL_LOAD_EXPIRATION As Label";
mostCurrent._label_load_expiration = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Private LVL_LIST As ListView";
mostCurrent._lvl_list = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 149;BA.debugLine="Private BUTTON_CANCEL As Button";
mostCurrent._button_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 150;BA.debugLine="Private BUTTON_ADD As Button";
mostCurrent._button_add = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 151;BA.debugLine="Private PANEL_BG_SECURITY As Panel";
mostCurrent._panel_bg_security = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 152;BA.debugLine="Private EDITTEXT_PASSWORD As EditText";
mostCurrent._edittext_password = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 153;BA.debugLine="Private CMB_ACCOUNT As B4XComboBox";
mostCurrent._cmb_account = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 154;BA.debugLine="Private PANEL_BG_INVOICE As Panel";
mostCurrent._panel_bg_invoice = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 155;BA.debugLine="Private TABLE_INVOICE As B4XTable";
mostCurrent._table_invoice = new wingan.app.b4xtable();
 //BA.debugLineNum = 156;BA.debugLine="Private LABEL_LOAD_TOTALAMT As Label";
mostCurrent._label_load_totalamt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 157;BA.debugLine="Private LABEL_LOAD_TOTALCASE As Label";
mostCurrent._label_load_totalcase = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 158;BA.debugLine="Private CMB_INV As B4XComboBox";
mostCurrent._cmb_inv = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 159;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 160;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 161;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 162;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 163;BA.debugLine="Private PANEL_BG_EXPIRATION As Panel";
mostCurrent._panel_bg_expiration = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 164;BA.debugLine="Private LABEL_LOAD_MANUFACTURING As Label";
mostCurrent._label_load_manufacturing = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static void  _input_manual() throws Exception{
ResumableSub_INPUT_MANUAL rsub = new ResumableSub_INPUT_MANUAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_MANUAL extends BA.ResumableSub {
public ResumableSub_INPUT_MANUAL(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
String _principal_query = "";
anywheresoftware.b4a.objects.collections.List _items = null;
int _i = 0;
int step9;
int limit9;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1554;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 1555;BA.debugLine="Dim principal_query As String  = \"AND principal_i";
_principal_query = "AND principal_id = '"+parent.mostCurrent._receiving_module._principal_id /*String*/ +"'";
 //BA.debugLineNum = 1556;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 1;
;
 //BA.debugLineNum = 1557;BA.debugLine="SearchTemplate.CustomListView1.Clear";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 1558;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1559;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 1560;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 1561;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' "+_principal_query+" ORDER BY product_desc ASC")));
 //BA.debugLineNum = 1562;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step9 = 1;
limit9 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 4;
if ((step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9)) this.state = 3;
if (true) break;

case 7:
//C
this.state = 6;
_i = ((int)(0 + _i + step9)) ;
if (true) break;

case 3:
//C
this.state = 7;
 //BA.debugLineNum = 1563;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 7;
;
 //BA.debugLineNum = 1564;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 1565;BA.debugLine="Items.Add(cursor2.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor2.GetString("product_desc")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1567;BA.debugLine="SearchTemplate.SetItems(Items)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (_items);
 //BA.debugLineNum = 1568;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _input_received() throws Exception{
ResumableSub_INPUT_RECEIVED rsub = new ResumableSub_INPUT_RECEIVED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_RECEIVED extends BA.ResumableSub {
public ResumableSub_INPUT_RECEIVED(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _trigger = 0;
int _result = 0;
String _query = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1295;BA.debugLine="GET_EXP";
_get_exp();
 //BA.debugLineNum = 1296;BA.debugLine="GET_MFG";
_get_mfg();
 //BA.debugLineNum = 1297;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 58;
return;
case 58:
//C
this.state = 1;
;
 //BA.debugLineNum = 1298;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex <= -1 Then CMB_U";
if (true) break;

case 1:
//if
this.state = 6;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()<=-1) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1299;BA.debugLine="If CMB_INVOICE.SelectedIndex <= -1 Then CMB_INVOI";
if (true) break;

case 7:
//if
this.state = 12;
if (parent.mostCurrent._cmb_invoice._getselectedindex /*int*/ ()<=-1) { 
this.state = 9;
;}if (true) break;

case 9:
//C
this.state = 12;
parent.mostCurrent._cmb_invoice._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 1300;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 59;
return;
case 59:
//C
this.state = 13;
;
 //BA.debugLineNum = 1301;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBo";
if (true) break;

case 13:
//if
this.state = 26;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 15;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 17;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 19;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 21;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 23;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 25;
}if (true) break;

case 15:
//C
this.state = 26;
 //BA.debugLineNum = 1302;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.text";
parent._total_pieces = (int) ((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 17:
//C
this.state = 26;
 //BA.debugLineNum = 1304;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.text";
parent._total_pieces = (int) ((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 19:
//C
this.state = 26;
 //BA.debugLineNum = 1306;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.text";
parent._total_pieces = (int) ((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 21:
//C
this.state = 26;
 //BA.debugLineNum = 1308;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.text";
parent._total_pieces = (int) ((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 23:
//C
this.state = 26;
 //BA.debugLineNum = 1310;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.text";
parent._total_pieces = (int) ((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 1312;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.text";
parent._total_pieces = (int) ((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 1314;BA.debugLine="total_price = Number.Format3((total_pieces * pric";
parent._total_price = (int)(Double.parseDouble(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(parent._total_pieces*parent._price),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15))));
 //BA.debugLineNum = 1316;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 1317;BA.debugLine="If total_pieces + total_input > total_order Then";
if (true) break;

case 27:
//if
this.state = 44;
if (parent._total_pieces+parent._total_input>parent._total_order) { 
this.state = 29;
}else {
this.state = 43;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 1318;BA.debugLine="If RECEIVING_MODULE.transaction_type = \"PURCHASE";
if (true) break;

case 30:
//if
this.state = 41;
if ((parent.mostCurrent._receiving_module._transaction_type /*String*/ ).equals("PURCHASE ORDER")) { 
this.state = 32;
}else if((parent.mostCurrent._receiving_module._transaction_type /*String*/ ).equals("AUTO SHIP")) { 
this.state = 40;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1319;BA.debugLine="Msgbox2Async(\"The quantity you will input will";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The quantity you will input will over from the quantity of the purchase order, Do you want to continue"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1320;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 60;
return;
case 60:
//C
this.state = 33;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1321;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 33:
//if
this.state = 38;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 35;
}else {
this.state = 37;
}if (true) break;

case 35:
//C
this.state = 38;
 //BA.debugLineNum = 1322;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 1324;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 38:
//C
this.state = 41;
;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 1327;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 41:
//C
this.state = 44;
;
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 1330;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;
;
 //BA.debugLineNum = 1333;BA.debugLine="If trigger = 1 Then";

case 44:
//if
this.state = 57;
if (_trigger==1) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 1334;BA.debugLine="If BUTTON_ADD.Text = \" SAVE\" Then";
if (true) break;

case 47:
//if
this.state = 56;
if ((parent.mostCurrent._button_add.getText()).equals(" SAVE")) { 
this.state = 49;
}else {
this.state = 51;
}if (true) break;

case 49:
//C
this.state = 56;
 //BA.debugLineNum = 1335;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 61;
return;
case 61:
//C
this.state = 56;
;
 //BA.debugLineNum = 1336;BA.debugLine="GET_TRANSACTION_NUMBER";
_get_transaction_number();
 //BA.debugLineNum = 1337;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 62;
return;
case 62:
//C
this.state = 56;
;
 //BA.debugLineNum = 1338;BA.debugLine="Dim query As String = \"INSERT INTO receiving_di";
_query = "INSERT INTO receiving_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1339;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._receiving_module._purchase_order_no /*String*/ ,parent.mostCurrent._cmb_invoice._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._transaction_number,parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),BA.NumberToString(parent._total_pieces),BA.NumberToString(parent._total_price),parent._input_type,parent._reason,parent._scan_code,parent._monthexp,parent._yearexp,parent.mostCurrent._label_load_expiration.getText(),parent._monthmfg,parent._yearmfg,parent.mostCurrent._label_load_manufactured.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ ,"EXISTING"}));
 //BA.debugLineNum = 1344;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 63;
return;
case 63:
//C
this.state = 56;
;
 //BA.debugLineNum = 1345;BA.debugLine="ToastMessageShow(\"Product Expiration Added Succ";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Expiration Added Succesfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1346;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 1347;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 64;
return;
case 64:
//C
this.state = 56;
;
 //BA.debugLineNum = 1348;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1349;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 65;
return;
case 65:
//C
this.state = 56;
;
 //BA.debugLineNum = 1350;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 1352;BA.debugLine="Msgbox2Async(\"Are you sure you want to update th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update this item?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1353;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 66;
return;
case 66:
//C
this.state = 52;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1354;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 52:
//if
this.state = 55;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 54;
}if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 1357;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 67;
return;
case 67:
//C
this.state = 55;
;
 //BA.debugLineNum = 1358;BA.debugLine="Dim query As String = \"UPDATE receiving_disc_ta";
_query = "UPDATE receiving_disc_table SET dr_no = ?, unit = ?, quantity = ?, total_pieces = ?, amount= ?, month_expired = ?, year_expired = ?, date_expired = ?, month_manufactured = ?, year_manufactured = ?, date_manufactured = ?, user_info = ? WHERE po_doc_no = ? AND transaction_number = ?";
 //BA.debugLineNum = 1359;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_invoice._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),BA.NumberToString(parent._total_pieces),BA.NumberToString(parent._total_price),parent._monthexp,parent._yearexp,parent.mostCurrent._label_load_expiration.getText(),parent._monthmfg,parent._yearmfg,parent.mostCurrent._label_load_manufactured.getText(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._receiving_module._purchase_order_no /*String*/ ,parent._transaction_number}));
 //BA.debugLineNum = 1360;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 55;
;
 //BA.debugLineNum = 1361;BA.debugLine="ToastMessageShow(\"Transaction Updated\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1362;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 1363;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 55;
;
 //BA.debugLineNum = 1364;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1365;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 70;
return;
case 70:
//C
this.state = 55;
;
 //BA.debugLineNum = 1366;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 55:
//C
this.state = 56;
;
 if (true) break;

case 56:
//C
this.state = 57;
;
 if (true) break;

case 57:
//C
this.state = -1;
;
 //BA.debugLineNum = 1370;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_daily_delivery() throws Exception{
ResumableSub_INSERT_DAILY_DELIVERY rsub = new ResumableSub_INSERT_DAILY_DELIVERY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DAILY_DELIVERY extends BA.ResumableSub {
public ResumableSub_INSERT_DAILY_DELIVERY(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _i = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2215;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 2216;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT * FROM rec";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 2217;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor8.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2218;BA.debugLine="For i = 0 To cursor8.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor8.getRowCount()-1);
_i = (int) (0) ;
this.state = 27;
if (true) break;

case 27:
//C
this.state = 13;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 28:
//C
this.state = 27;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2219;BA.debugLine="cursor8.Position = i";
parent._cursor8.setPosition(_i);
 //BA.debugLineNum = 2220;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 2221;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_receiving_delivery",(Object[])(new String[]{parent._cursor8.GetString("po_doc_no"),parent._cursor8.GetString("dr_no"),parent._cursor8.GetString("trucking"),parent._cursor8.GetString("truck_type"),parent._cursor8.GetString("plate_no"),parent._cursor8.GetString("driver"),parent._cursor8.GetString("date_registered"),parent._cursor8.GetString("time_registered"),parent._cursor8.GetString("tab_id"),parent._cursor8.GetString("user_info")}));
 //BA.debugLineNum = 2223;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2224;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor8.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor8.GetString("trucking")));
 //BA.debugLineNum = 2225;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2226;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2229;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 2230;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2231;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2232;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("792930066","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2233;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2234;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2235;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 28;
;
 //BA.debugLineNum = 2237;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 2240;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 2241;BA.debugLine="If error_trigger = 0 Then";
if (true) break;

case 15:
//if
this.state = 26;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 26;
 //BA.debugLineNum = 2242;BA.debugLine="UPDATE_RECEIVING";
_update_receiving();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 2244;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2245;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2246;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2247;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2248;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 20:
//if
this.state = 25;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 22;
}else {
this.state = 24;
}if (true) break;

case 22:
//C
this.state = 25;
 //BA.debugLineNum = 2249;BA.debugLine="DELETE_RECEIVING_DISC";
_delete_receiving_disc();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 2251;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2252;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 2255;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_daily_disc() throws Exception{
ResumableSub_INSERT_DAILY_DISC rsub = new ResumableSub_INSERT_DAILY_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DAILY_DISC extends BA.ResumableSub {
public ResumableSub_INSERT_DAILY_DISC(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _i = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2145;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 2146;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM rec";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_disc_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 2147;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor7.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2148;BA.debugLine="For i = 0 To cursor7.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor7.getRowCount()-1);
_i = (int) (0) ;
this.state = 27;
if (true) break;

case 27:
//C
this.state = 13;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 28:
//C
this.state = 27;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2149;BA.debugLine="cursor7.Position = i";
parent._cursor7.setPosition(_i);
 //BA.debugLineNum = 2150;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 2151;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_receiving_disc",(Object[])(new String[]{parent._cursor7.GetString("po_doc_no"),parent._cursor7.GetString("dr_no"),parent._cursor7.GetString("transaction_number"),parent._cursor7.GetString("product_id"),parent._cursor7.GetString("product_variant"),parent._cursor7.GetString("product_description"),parent._cursor7.GetString("unit"),parent._cursor7.GetString("quantity"),parent._cursor7.GetString("total_pieces"),parent._cursor7.GetString("amount"),parent._cursor7.GetString("transaction_type"),parent._cursor7.GetString("reason"),parent._cursor7.GetString("scan_code"),parent._cursor7.GetString("month_expired"),parent._cursor7.GetString("year_expired"),parent._cursor7.GetString("date_expired"),parent._cursor7.GetString("month_manufactured"),parent._cursor7.GetString("year_manufactured"),parent._cursor7.GetString("date_manufactured"),parent._cursor7.GetString("date_registered"),parent._cursor7.GetString("time_registered"),parent._cursor7.GetString("user_info"),parent._cursor7.GetString("tab_id"),parent._cursor7.GetString("status")}));
 //BA.debugLineNum = 2156;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2157;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor7.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor7.GetString("product_description")));
 //BA.debugLineNum = 2158;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2159;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2162;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 2163;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2164;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2165;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("792798997","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2166;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2167;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2168;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 28;
;
 //BA.debugLineNum = 2170;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 2173;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 2174;BA.debugLine="If error_trigger = 0 Then";
if (true) break;

case 15:
//if
this.state = 26;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 26;
 //BA.debugLineNum = 2175;BA.debugLine="DELETE_RECEIVING_DELIVERY";
_delete_receiving_delivery();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 2177;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2178;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2179;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2180;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2181;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 20:
//if
this.state = 25;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 22;
}else {
this.state = 24;
}if (true) break;

case 22:
//C
this.state = 25;
 //BA.debugLineNum = 2182;BA.debugLine="DELETE_RECEIVING_DISC";
_delete_receiving_disc();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 2184;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2185;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 2188;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _label_load_expiration_click() throws Exception{
ResumableSub_LABEL_LOAD_EXPIRATION_Click rsub = new ResumableSub_LABEL_LOAD_EXPIRATION_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_EXPIRATION_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_EXPIRATION_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1084;BA.debugLine="Dialog.Title = \"Select Expiration Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Expiration Date");
 //BA.debugLineNum = 1085;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(255,109,81)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (109),(int) (81));
 //BA.debugLineNum = 1086;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)("NO EXP"),(Object)("CANCEL")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1087;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}else if(_result==parent.mostCurrent._xui.DialogResponse_Negative) { 
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1088;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = DateTime.Date(DateT";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 //BA.debugLineNum = 1089;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1090;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 1091;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1092;BA.debugLine="GET_EXPIRATION_SPAN";
_get_expiration_span();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1094;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1096;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _label_load_manufactured_click() throws Exception{
ResumableSub_LABEL_LOAD_MANUFACTURED_Click rsub = new ResumableSub_LABEL_LOAD_MANUFACTURED_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_MANUFACTURED_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_MANUFACTURED_Click(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1098;BA.debugLine="Dialog.Title = \"Select Manufacturing Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Manufacturing Date");
 //BA.debugLineNum = 1099;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(91,255,81)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (91),(int) (255),(int) (81));
 //BA.debugLineNum = 1100;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate2, \"\",";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate2),(Object)(""),(Object)("NO MFG"),(Object)("CANCEL")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1101;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}else if(_result==parent.mostCurrent._xui.DialogResponse_Negative) { 
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1102;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(Dat";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate2._getdate /*long*/ ())));
 //BA.debugLineNum = 1103;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1104;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 1105;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1106;BA.debugLine="GET_MANUFACTURING_SPAN";
_get_manufacturing_span();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1108;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"NO MANUFACTURING";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("NO MANUFACTURING"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1110;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _listview_dr_itemlongclick(int _position,Object _value) throws Exception{
ResumableSub_LISTVIEW_DR_ItemLongClick rsub = new ResumableSub_LISTVIEW_DR_ItemLongClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LISTVIEW_DR_ItemLongClick extends BA.ResumableSub {
public ResumableSub_LISTVIEW_DR_ItemLongClick(wingan.app.receiving2_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.receiving2_module parent;
int _position;
Object _value;
int _result = 0;
int _i = 0;
int step11;
int limit11;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 908;BA.debugLine="Msgbox2Async(\"What do you want to do in this Docu";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("What do you want to do in this Document No?"),BA.ObjectToCharSequence("Options"),"Edit","","Delete",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 909;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 15;
return;
case 15:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 910;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 911;BA.debugLine="old_doc_no = Value";
parent._old_doc_no = BA.ObjectToString(_value);
 //BA.debugLineNum = 912;BA.debugLine="BUTTON_SAVE.Text = \" Edit\"";
parent.mostCurrent._button_save.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 913;BA.debugLine="BUTTON_SAVE.TextColor = Colors.Blue";
parent.mostCurrent._button_save.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 914;BA.debugLine="BUTTON_OKAY.Text = \" Cancel\"";
parent.mostCurrent._button_okay.setText(BA.ObjectToCharSequence(" Cancel"));
 //BA.debugLineNum = 915;BA.debugLine="BUTTON_OKAY.TextColor = Colors.Red";
parent.mostCurrent._button_okay.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 916;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM re";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE dr_no = '"+BA.ObjectToString(_value)+"'")));
 //BA.debugLineNum = 917;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 13;
if (parent._cursor4.getRowCount()>0) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 918;BA.debugLine="For i = 0 To cursor4.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step11 = 1;
limit11 = (int) (parent._cursor4.getRowCount()-1);
_i = (int) (0) ;
this.state = 16;
if (true) break;

case 16:
//C
this.state = 10;
if ((step11 > 0 && _i <= limit11) || (step11 < 0 && _i >= limit11)) this.state = 9;
if (true) break;

case 17:
//C
this.state = 16;
_i = ((int)(0 + _i + step11)) ;
if (true) break;

case 9:
//C
this.state = 17;
 //BA.debugLineNum = 919;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 17;
;
 //BA.debugLineNum = 920;BA.debugLine="cursor4.Position = i";
parent._cursor4.setPosition(_i);
 //BA.debugLineNum = 921;BA.debugLine="EDITTEXT_PLATE.Text = cursor4.GetString(\"plate";
parent.mostCurrent._edittext_plate.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("plate_no")));
 //BA.debugLineNum = 922;BA.debugLine="EDITTEXT_DOC_NO.Text = cursor4.GetString(\"dr_n";
parent.mostCurrent._edittext_doc_no.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("dr_no")));
 //BA.debugLineNum = 923;BA.debugLine="EDITTEXT_DRIVER.Text = cursor4.GetString(\"driv";
parent.mostCurrent._edittext_driver.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("driver")));
 //BA.debugLineNum = 924;BA.debugLine="EDITTEXT_TRUCKING.Text = cursor4.GetString(\"tr";
parent.mostCurrent._edittext_trucking.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("trucking")));
 //BA.debugLineNum = 925;BA.debugLine="EDITTEXT_TRUCKTYPE.Text = cursor4.GetString(\"t";
parent.mostCurrent._edittext_trucktype.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("truck_type")));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 928;BA.debugLine="ToastMessageShow(\"No dr data\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No dr data"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 931;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_document() throws Exception{
int _row = 0;
 //BA.debugLineNum = 1068;BA.debugLine="Sub LOAD_DOCUMENT";
 //BA.debugLineNum = 1069;BA.debugLine="CMB_INVOICE.cmbBox.Clear";
mostCurrent._cmb_invoice._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1070;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM rec";
_cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"+mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 1071;BA.debugLine="If cursor2.RowCount > 0 Then";
if (_cursor2.getRowCount()>0) { 
 //BA.debugLineNum = 1072;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_cursor2.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit4 ;_row = _row + step4 ) {
 //BA.debugLineNum = 1073;BA.debugLine="cursor2.Position = row";
_cursor2.setPosition(_row);
 //BA.debugLineNum = 1074;BA.debugLine="CMB_INVOICE.cmbBox.Add(cursor2.GetString(\"dr_no";
mostCurrent._cmb_invoice._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(_cursor2.GetString("dr_no"));
 }
};
 //BA.debugLineNum = 1076;BA.debugLine="CMB_INVOICE.SelectedIndex = - 1";
mostCurrent._cmb_invoice._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1077;BA.debugLine="OpenSpinner(CMB_INVOICE.cmbBox)";
_openspinner(mostCurrent._cmb_invoice._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 };
 //BA.debugLineNum = 1079;BA.debugLine="End Sub";
return "";
}
public static String  _load_invoice() throws Exception{
int _row = 0;
 //BA.debugLineNum = 1960;BA.debugLine="Sub LOAD_INVOICE";
 //BA.debugLineNum = 1961;BA.debugLine="CMB_INV.cmbBox.Clear";
mostCurrent._cmb_inv._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1962;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM rec";
_cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"+mostCurrent._receiving_module._purchase_order_no /*String*/ +"'")));
 //BA.debugLineNum = 1963;BA.debugLine="If cursor2.RowCount > 0 Then";
if (_cursor2.getRowCount()>0) { 
 //BA.debugLineNum = 1964;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_cursor2.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit4 ;_row = _row + step4 ) {
 //BA.debugLineNum = 1965;BA.debugLine="cursor2.Position = row";
_cursor2.setPosition(_row);
 //BA.debugLineNum = 1966;BA.debugLine="CMB_INV.cmbBox.Add(cursor2.GetString(\"dr_no\"))";
mostCurrent._cmb_inv._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(_cursor2.GetString("dr_no"));
 }
};
 };
 //BA.debugLineNum = 1969;BA.debugLine="End Sub";
return "";
}
public static String  _load_invoice_header() throws Exception{
 //BA.debugLineNum = 2018;BA.debugLine="Sub LOAD_INVOICE_HEADER";
 //BA.debugLineNum = 2019;BA.debugLine="NameColumn2(0)=TABLE_INVOICE.AddColumn(\"Product V";
mostCurrent._namecolumn2[(int) (0)] = mostCurrent._table_invoice._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._table_invoice._column_type_text /*int*/ );
 //BA.debugLineNum = 2020;BA.debugLine="NameColumn2(1)=TABLE_INVOICE.AddColumn(\"Product D";
mostCurrent._namecolumn2[(int) (1)] = mostCurrent._table_invoice._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._table_invoice._column_type_text /*int*/ );
 //BA.debugLineNum = 2021;BA.debugLine="NameColumn2(2)=TABLE_INVOICE.AddColumn(\"Unit\", TA";
mostCurrent._namecolumn2[(int) (2)] = mostCurrent._table_invoice._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Unit",mostCurrent._table_invoice._column_type_text /*int*/ );
 //BA.debugLineNum = 2022;BA.debugLine="NameColumn2(3)=TABLE_INVOICE.AddColumn(\"Qty\", TAB";
mostCurrent._namecolumn2[(int) (3)] = mostCurrent._table_invoice._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Qty",mostCurrent._table_invoice._column_type_text /*int*/ );
 //BA.debugLineNum = 2023;BA.debugLine="NameColumn2(4)=TABLE_INVOICE.AddColumn(\"Amount\",";
mostCurrent._namecolumn2[(int) (4)] = mostCurrent._table_invoice._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Amount",mostCurrent._table_invoice._column_type_text /*int*/ );
 //BA.debugLineNum = 2024;BA.debugLine="End Sub";
return "";
}
public static void  _load_list() throws Exception{
ResumableSub_LOAD_LIST rsub = new ResumableSub_LOAD_LIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_LIST extends BA.ResumableSub {
public ResumableSub_LOAD_LIST(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _numbercount = 0;
int _i = 0;
int step21;
int limit21;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1380;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1382;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1383;BA.debugLine="LVL_LIST.Background = bg";
parent.mostCurrent._lvl_list.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1384;BA.debugLine="LVL_LIST.Clear";
parent.mostCurrent._lvl_list.Clear();
 //BA.debugLineNum = 1385;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 11;
return;
case 11:
//C
this.state = 1;
;
 //BA.debugLineNum = 1386;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.Typeface = Typeface";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1387;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.TextSize = 20";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 1388;BA.debugLine="LVL_LIST.TwoLinesLayout.label.Height = 8%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1389;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.TextColor = Colors.";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1390;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.Gravity = Gravity.C";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 1391;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Typeface = Ty";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1392;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Top = 7%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 1393;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.TextSize = 12";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTextSize((float) (12));
 //BA.debugLineNum = 1394;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Height = 4%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 1395;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.TextColor = C";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 1396;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Gravity = Gra";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 1397;BA.debugLine="LVL_LIST.TwoLinesLayout.ItemHeight = 12%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 1398;BA.debugLine="Dim numbercount As Int = 0";
_numbercount = (int) (0);
 //BA.debugLineNum = 1400;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT * FROM rec";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_disc_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' and product_description = '"+parent.mostCurrent._label_load_description.getText()+"' ORDER by transaction_number")));
 //BA.debugLineNum = 1401;BA.debugLine="If cursor9.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor9.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1402;BA.debugLine="For i = 0 To cursor9.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step21 = 1;
limit21 = (int) (parent._cursor9.getRowCount()-1);
_i = (int) (0) ;
this.state = 12;
if (true) break;

case 12:
//C
this.state = 7;
if ((step21 > 0 && _i <= limit21) || (step21 < 0 && _i >= limit21)) this.state = 6;
if (true) break;

case 13:
//C
this.state = 12;
_i = ((int)(0 + _i + step21)) ;
if (true) break;

case 6:
//C
this.state = 13;
 //BA.debugLineNum = 1403;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = 13;
;
 //BA.debugLineNum = 1404;BA.debugLine="cursor9.Position = i";
parent._cursor9.setPosition(_i);
 //BA.debugLineNum = 1405;BA.debugLine="numbercount = numbercount + 1";
_numbercount = (int) (_numbercount+1);
 //BA.debugLineNum = 1406;BA.debugLine="LVL_LIST.AddTwoLines2(numbercount & \".) \"& curs";
parent.mostCurrent._lvl_list.AddTwoLines2(BA.ObjectToCharSequence(BA.NumberToString(_numbercount)+".) "+parent._cursor9.GetString("quantity")+" "+parent._cursor9.GetString("unit")),BA.ObjectToCharSequence(" DOC NO: "+parent._cursor9.GetString("dr_no")+" EXP DATE: "+parent._cursor9.GetString("date_expired")),(Object)(parent._cursor9.GetString("transaction_number")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 1411;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_per_invoice() throws Exception{
ResumableSub_LOAD_PER_INVOICE rsub = new ResumableSub_LOAD_PER_INVOICE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_PER_INVOICE extends BA.ResumableSub {
public ResumableSub_LOAD_PER_INVOICE(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _ia = 0;
Object[] _row = null;
int step8;
int limit8;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2026;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 2027;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2028;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 1;
;
 //BA.debugLineNum = 2029;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2030;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 2031;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT *, sum(qua";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT *, sum(quantity) as 'qty', sum(amount) as 'amt' FROM receiving_disc_table WHERE dr_no = '"+parent.mostCurrent._cmb_inv._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' AND po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' GROUP BY product_id, unit ORDER BY product_variant")));
 //BA.debugLineNum = 2032;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2033;BA.debugLine="For ia = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step8 = 1;
limit8 = (int) (parent._cursor3.getRowCount()-1);
_ia = (int) (0) ;
this.state = 17;
if (true) break;

case 17:
//C
this.state = 7;
if ((step8 > 0 && _ia <= limit8) || (step8 < 0 && _ia >= limit8)) this.state = 6;
if (true) break;

case 18:
//C
this.state = 17;
_ia = ((int)(0 + _ia + step8)) ;
if (true) break;

case 6:
//C
this.state = 18;
 //BA.debugLineNum = 2034;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 19;
return;
case 19:
//C
this.state = 18;
;
 //BA.debugLineNum = 2035;BA.debugLine="cursor3.Position = ia";
parent._cursor3.setPosition(_ia);
 //BA.debugLineNum = 2037;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 2038;BA.debugLine="row(0) = cursor3.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor3.GetString("product_variant"));
 //BA.debugLineNum = 2039;BA.debugLine="row(1) = cursor3.GetString(\"product_description";
_row[(int) (1)] = (Object)(parent._cursor3.GetString("product_description"));
 //BA.debugLineNum = 2040;BA.debugLine="row(2) = cursor3.GetString(\"unit\")";
_row[(int) (2)] = (Object)(parent._cursor3.GetString("unit"));
 //BA.debugLineNum = 2041;BA.debugLine="row(3) = cursor3.GetString(\"qty\")";
_row[(int) (3)] = (Object)(parent._cursor3.GetString("qty"));
 //BA.debugLineNum = 2042;BA.debugLine="row(4) = Number.Format3(cursor3.GetString(\"amt\"";
_row[(int) (4)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(parent._cursor3.GetString("amt"))),(int) (0),(int) (2),(int) (2),".",",",",",(int) (0),(int) (15)));
 //BA.debugLineNum = 2043;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 2045;BA.debugLine="TABLE_INVOICE.RowHeight = 30dip";
parent.mostCurrent._table_invoice._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30));
 //BA.debugLineNum = 2046;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 20;
return;
case 20:
//C
this.state = 10;
;
 //BA.debugLineNum = 2047;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2049;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 2050;BA.debugLine="ToastMessageShow(\"Data is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Data is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 2052;BA.debugLine="TABLE_INVOICE.SetData(Data)";
parent.mostCurrent._table_invoice._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 2053;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 11:
//if
this.state = 14;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2054;BA.debugLine="XSelections.Initialize(TABLE_INVOICE)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_invoice);
 //BA.debugLineNum = 2055;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 2057;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = -1;
;
 //BA.debugLineNum = 2058;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_purchase_header() throws Exception{
 //BA.debugLineNum = 372;BA.debugLine="Sub LOAD_PURCHASE_HEADER";
 //BA.debugLineNum = 373;BA.debugLine="NameColumn(0)=TABLE_PURCHASE_ORDER.AddColumn(\"Pro";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_purchase_order._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._table_purchase_order._column_type_text /*int*/ );
 //BA.debugLineNum = 374;BA.debugLine="NameColumn(1)=TABLE_PURCHASE_ORDER.AddColumn(\"Pro";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_purchase_order._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._table_purchase_order._column_type_text /*int*/ );
 //BA.debugLineNum = 375;BA.debugLine="NameColumn(2)=TABLE_PURCHASE_ORDER.AddColumn(\"Tot";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_purchase_order._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Total Case",mostCurrent._table_purchase_order._column_type_text /*int*/ );
 //BA.debugLineNum = 376;BA.debugLine="NameColumn(3)=TABLE_PURCHASE_ORDER.AddColumn(\"Tot";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_purchase_order._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Total Received",mostCurrent._table_purchase_order._column_type_text /*int*/ );
 //BA.debugLineNum = 377;BA.debugLine="NameColumn(4)=TABLE_PURCHASE_ORDER.AddColumn(\"Bal";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_purchase_order._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Balance",mostCurrent._table_purchase_order._column_type_text /*int*/ );
 //BA.debugLineNum = 378;BA.debugLine="End Sub";
return "";
}
public static void  _load_purchase_order() throws Exception{
ResumableSub_LOAD_PURCHASE_ORDER rsub = new ResumableSub_LOAD_PURCHASE_ORDER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_PURCHASE_ORDER extends BA.ResumableSub {
public ResumableSub_LOAD_PURCHASE_ORDER(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _total_received = 0;
String _total_case_order = "";
String _total_case_received = "";
String _balance = "";
int _ia = 0;
Object[] _row = null;
int step12;
int limit12;
int step51;
int limit51;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 380;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 70;
return;
case 70:
//C
this.state = 1;
;
 //BA.debugLineNum = 381;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 382;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 71;
return;
case 71:
//C
this.state = 1;
;
 //BA.debugLineNum = 383;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 384;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 385;BA.debugLine="Dim total_received As Int";
_total_received = 0;
 //BA.debugLineNum = 387;BA.debugLine="Dim total_case_order As String";
_total_case_order = "";
 //BA.debugLineNum = 388;BA.debugLine="Dim total_case_received As String";
_total_case_received = "";
 //BA.debugLineNum = 389;BA.debugLine="Dim balance As String";
_balance = "";
 //BA.debugLineNum = 390;BA.debugLine="cursor10 = connection.ExecQuery(\"SELECT a.*, b.to";
parent._cursor10 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.total_pcs_received , c.CASE_UNIT_PER_PCS FROM "+"(Select *, sum(total_pieces) As 'total_pcs_order' FROM purchase_order_ref_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' GROUP BY po_doc_no, product_description) as a "+"LEFT JOIN "+"(Select *, sum(total_pieces) As 'total_pcs_received' FROM receiving_disc_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' GROUP BY po_doc_no, product_description) as b "+"ON a.po_doc_no = b.po_doc_no And a.product_description = b.product_description "+"LEFT JOIN "+"(Select * FROM product_table) As c "+"ON a.product_description = c.product_desc")));
 //BA.debugLineNum = 398;BA.debugLine="If cursor10.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 32;
if (parent._cursor10.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 31;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 399;BA.debugLine="For ia = 0 To cursor10.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 29;
step12 = 1;
limit12 = (int) (parent._cursor10.getRowCount()-1);
_ia = (int) (0) ;
this.state = 72;
if (true) break;

case 72:
//C
this.state = 29;
if ((step12 > 0 && _ia <= limit12) || (step12 < 0 && _ia >= limit12)) this.state = 6;
if (true) break;

case 73:
//C
this.state = 72;
_ia = ((int)(0 + _ia + step12)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 400;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 74;
return;
case 74:
//C
this.state = 7;
;
 //BA.debugLineNum = 401;BA.debugLine="cursor10.Position = ia";
parent._cursor10.setPosition(_ia);
 //BA.debugLineNum = 402;BA.debugLine="If cursor10.GetString(\"total_pcs_received\") = N";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor10.GetString("total_pcs_received")== null || (parent._cursor10.GetString("total_pcs_received")).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 403;BA.debugLine="total_received = 0";
_total_received = (int) (0);
 //BA.debugLineNum = 404;BA.debugLine="total_case_received = 0";
_total_case_received = BA.NumberToString(0);
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 406;BA.debugLine="total_received = cursor10.GetString(\"total_pcs";
_total_received = (int)(Double.parseDouble(parent._cursor10.GetString("total_pcs_received")));
 //BA.debugLineNum = 407;BA.debugLine="total_case_received = Number.Format3((total_re";
_total_case_received = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 410;BA.debugLine="total_case_order = Number.Format3((cursor10.Get";
_total_case_order = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(parent._cursor10.GetString("total_pcs_order")))/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 411;BA.debugLine="balance = Number.Format3(((total_received - cur";
_balance = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-(double)(Double.parseDouble(parent._cursor10.GetString("total_pcs_order"))))/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 414;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 415;BA.debugLine="row(0) = cursor10.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor10.GetString("product_variant"));
 //BA.debugLineNum = 416;BA.debugLine="row(1) = cursor10.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor10.GetString("product_description"));
 //BA.debugLineNum = 417;BA.debugLine="Log(total_case_order.SubString(total_case_order";
anywheresoftware.b4a.keywords.Common.LogImpl("788211494",_total_case_order.substring((int) (_total_case_order.indexOf(".")+1)),0);
 //BA.debugLineNum = 418;BA.debugLine="If total_case_order.SubString(total_case_order.";
if (true) break;

case 13:
//if
this.state = 18;
if ((double)(Double.parseDouble(_total_case_order.substring((int) (_total_case_order.indexOf(".")+1))))>0) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 419;BA.debugLine="row(2) = Number.Format3((cursor10.GetString(\"t";
_row[(int) (2)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(parent._cursor10.GetString("total_pcs_order")))/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15)));
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 421;BA.debugLine="row(2) = Number.Format3((cursor10.GetString(\"t";
_row[(int) (2)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(parent._cursor10.GetString("total_pcs_order")))/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15)));
 if (true) break;
;
 //BA.debugLineNum = 423;BA.debugLine="If total_case_received.SubString(total_case_rec";

case 18:
//if
this.state = 23;
if ((double)(Double.parseDouble(_total_case_received.substring((int) (_total_case_received.indexOf(".")+1))))>0) { 
this.state = 20;
}else {
this.state = 22;
}if (true) break;

case 20:
//C
this.state = 23;
 //BA.debugLineNum = 424;BA.debugLine="row(3) = Number.Format3((total_received / curs";
_row[(int) (3)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15)));
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 426;BA.debugLine="row(3) = Number.Format3((total_received / curs";
_row[(int) (3)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15)));
 if (true) break;
;
 //BA.debugLineNum = 428;BA.debugLine="If balance.SubString(balance.IndexOf(\".\")+1) >";

case 23:
//if
this.state = 28;
if ((double)(Double.parseDouble(_balance.substring((int) (_balance.indexOf(".")+1))))>0) { 
this.state = 25;
}else {
this.state = 27;
}if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 429;BA.debugLine="row(4) = Number.Format3(((total_received - cur";
_row[(int) (4)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-(double)(Double.parseDouble(parent._cursor10.GetString("total_pcs_order"))))/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15)));
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 431;BA.debugLine="row(4) = Number.Format3(((total_received - cur";
_row[(int) (4)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-(double)(Double.parseDouble(parent._cursor10.GetString("total_pcs_order"))))/(double)(double)(Double.parseDouble(parent._cursor10.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15)));
 if (true) break;

case 28:
//C
this.state = 73;
;
 //BA.debugLineNum = 433;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;
if (true) break;

case 29:
//C
this.state = 32;
;
 //BA.debugLineNum = 436;BA.debugLine="RECEIVING_MODULE.transaction_type = \"PURCHASE OR";
parent.mostCurrent._receiving_module._transaction_type /*String*/  = "PURCHASE ORDER";
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 438;BA.debugLine="RECEIVING_MODULE.transaction_type = \"AUTO SHIP\"";
parent.mostCurrent._receiving_module._transaction_type /*String*/  = "AUTO SHIP";
 if (true) break;

case 32:
//C
this.state = 33;
;
 //BA.debugLineNum = 441;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT a.*, b.CAS";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.CASE_UNIT_PER_PCS FROM"+"(Select *, sum(total_pieces) As total_pcs_received FROM receiving_disc_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' AND product_description "+"Not IN (Select product_description FROM purchase_order_ref_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"') GROUP BY product_id) as a "+"LEFT JOIN "+"product_table As b "+"ON a.product_description = b.product_desc GROUP BY a.product_id")));
 //BA.debugLineNum = 447;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 33:
//if
this.state = 65;
if (parent._cursor6.getRowCount()>0) { 
this.state = 35;
}else {
this.state = 64;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 448;BA.debugLine="For ia = 0 To cursor6.RowCount - 1";
if (true) break;

case 36:
//for
this.state = 62;
step51 = 1;
limit51 = (int) (parent._cursor6.getRowCount()-1);
_ia = (int) (0) ;
this.state = 75;
if (true) break;

case 75:
//C
this.state = 62;
if ((step51 > 0 && _ia <= limit51) || (step51 < 0 && _ia >= limit51)) this.state = 38;
if (true) break;

case 76:
//C
this.state = 75;
_ia = ((int)(0 + _ia + step51)) ;
if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 449;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 77;
return;
case 77:
//C
this.state = 39;
;
 //BA.debugLineNum = 450;BA.debugLine="cursor6.Position = ia";
parent._cursor6.setPosition(_ia);
 //BA.debugLineNum = 452;BA.debugLine="If cursor6.GetString(\"total_pcs_received\") = Nu";
if (true) break;

case 39:
//if
this.state = 44;
if (parent._cursor6.GetString("total_pcs_received")== null || (parent._cursor6.GetString("total_pcs_received")).equals("")) { 
this.state = 41;
}else {
this.state = 43;
}if (true) break;

case 41:
//C
this.state = 44;
 //BA.debugLineNum = 453;BA.debugLine="total_received = 0";
_total_received = (int) (0);
 //BA.debugLineNum = 454;BA.debugLine="total_case_received = 0";
_total_case_received = BA.NumberToString(0);
 //BA.debugLineNum = 455;BA.debugLine="Log(total_received & \" -\")";
anywheresoftware.b4a.keywords.Common.LogImpl("788211532",BA.NumberToString(_total_received)+" -",0);
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 457;BA.debugLine="total_received = cursor6.GetString(\"total_pcs_";
_total_received = (int)(Double.parseDouble(parent._cursor6.GetString("total_pcs_received")));
 //BA.debugLineNum = 458;BA.debugLine="total_case_received = Number.Format3((total_re";
_total_case_received = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor6.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 459;BA.debugLine="Log(total_received & \" +\")";
anywheresoftware.b4a.keywords.Common.LogImpl("788211536",BA.NumberToString(_total_received)+" +",0);
 if (true) break;
;
 //BA.debugLineNum = 462;BA.debugLine="If cursor6.GetString(\"product_description\") = N";

case 44:
//if
this.state = 61;
if (parent._cursor6.GetString("product_description")== null || (parent._cursor6.GetString("product_description")).equals("")) { 
this.state = 46;
}else {
this.state = 48;
}if (true) break;

case 46:
//C
this.state = 61;
 if (true) break;

case 48:
//C
this.state = 49;
 //BA.debugLineNum = 464;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 465;BA.debugLine="row(0) = cursor6.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor6.GetString("product_variant"));
 //BA.debugLineNum = 466;BA.debugLine="row(1) = cursor6.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor6.GetString("product_description"));
 //BA.debugLineNum = 467;BA.debugLine="row(2) = 0";
_row[(int) (2)] = (Object)(0);
 //BA.debugLineNum = 468;BA.debugLine="If total_case_received.SubString(total_case_re";
if (true) break;

case 49:
//if
this.state = 54;
if ((double)(Double.parseDouble(_total_case_received.substring((int) (_total_case_received.indexOf(".")+1))))>0) { 
this.state = 51;
}else {
this.state = 53;
}if (true) break;

case 51:
//C
this.state = 54;
 //BA.debugLineNum = 469;BA.debugLine="row(3) = Number.Format3((total_received / cur";
_row[(int) (3)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor6.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15)));
 if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 471;BA.debugLine="row(3) = Number.Format3((total_received / cur";
_row[(int) (3)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(_total_received/(double)(double)(Double.parseDouble(parent._cursor6.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15)));
 if (true) break;

case 54:
//C
this.state = 55;
;
 //BA.debugLineNum = 474;BA.debugLine="balance = Number.Format3(((total_received - 0)";
_balance = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-0)/(double)(double)(Double.parseDouble(parent._cursor6.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 475;BA.debugLine="If balance.SubString(balance.IndexOf(\".\")+1) >";
if (true) break;

case 55:
//if
this.state = 60;
if ((double)(Double.parseDouble(_balance.substring((int) (_balance.indexOf(".")+1))))>0) { 
this.state = 57;
}else {
this.state = 59;
}if (true) break;

case 57:
//C
this.state = 60;
 //BA.debugLineNum = 476;BA.debugLine="row(4) = Number.Format3(((total_received - 0)";
_row[(int) (4)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-0)/(double)(double)(Double.parseDouble(parent._cursor6.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15)));
 if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 478;BA.debugLine="row(4) = Number.Format3(((total_received - 0)";
_row[(int) (4)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((_total_received-0)/(double)(double)(Double.parseDouble(parent._cursor6.GetString("CASE_UNIT_PER_PCS")))),(int) (0),(int) (0),(int) (0),".","","",(int) (0),(int) (15)));
 if (true) break;

case 60:
//C
this.state = 61;
;
 //BA.debugLineNum = 480;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 61:
//C
this.state = 76;
;
 if (true) break;
if (true) break;

case 62:
//C
this.state = 65;
;
 if (true) break;

case 64:
//C
this.state = 65;
 if (true) break;

case 65:
//C
this.state = 66;
;
 //BA.debugLineNum = 487;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 78;
return;
case 78:
//C
this.state = 66;
;
 //BA.debugLineNum = 488;BA.debugLine="TABLE_PURCHASE_ORDER.RowHeight = 50dip";
parent.mostCurrent._table_purchase_order._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 489;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 79;
return;
case 79:
//C
this.state = 66;
;
 //BA.debugLineNum = 490;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 491;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 80;
return;
case 80:
//C
this.state = 66;
;
 //BA.debugLineNum = 492;BA.debugLine="TABLE_PURCHASE_ORDER.SetData(Data)";
parent.mostCurrent._table_purchase_order._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 493;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 66:
//if
this.state = 69;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 68;
}if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 494;BA.debugLine="XSelections.Initialize(TABLE_PURCHASE_ORDER)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_purchase_order);
 //BA.debugLineNum = 495;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 69:
//C
this.state = -1;
;
 //BA.debugLineNum = 497;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 81;
return;
case 81:
//C
this.state = -1;
;
 //BA.debugLineNum = 498;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _loadtexts() throws Exception{
String _filename = "";
int _iq = 0;
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _reader = null;
 //BA.debugLineNum = 1914;BA.debugLine="Sub LoadTexts";
 //BA.debugLineNum = 1915;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1916;BA.debugLine="Dim iq As Int";
_iq = 0;
 //BA.debugLineNum = 1920;BA.debugLine="FileName = \"tapecalc_\" & LanguageID & \".txt\"";
_filename = "tapecalc_"+_languageid+".txt";
 //BA.debugLineNum = 1921;BA.debugLine="If File.Exists(ProgPath, FileName) = False Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_progpath,_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1922;BA.debugLine="FileName = \"tapecalc_en.txt\"";
_filename = "tapecalc_en.txt";
 };
 //BA.debugLineNum = 1925;BA.debugLine="Dim Reader As TextReader";
_reader = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1926;BA.debugLine="Reader.Initialize(File.OpenInput(ProgPath, FileNa";
_reader.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_progpath,_filename).getObject()));
 //BA.debugLineNum = 1927;BA.debugLine="iq = 0";
_iq = (int) (0);
 //BA.debugLineNum = 1928;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 //BA.debugLineNum = 1929;BA.debugLine="Do While Texts(iq) <> Null";
while (_texts[_iq]!= null) {
 //BA.debugLineNum = 1930;BA.debugLine="iq = iq + 1";
_iq = (int) (_iq+1);
 //BA.debugLineNum = 1931;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 }
;
 //BA.debugLineNum = 1933;BA.debugLine="Reader.Close";
_reader.Close();
 //BA.debugLineNum = 1934;BA.debugLine="End Sub";
return "";
}
public static void  _lvl_list_itemclick(int _position,Object _value) throws Exception{
ResumableSub_LVL_LIST_ItemClick rsub = new ResumableSub_LVL_LIST_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LVL_LIST_ItemClick extends BA.ResumableSub {
public ResumableSub_LVL_LIST_ItemClick(wingan.app.receiving2_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.receiving2_module parent;
int _position;
Object _value;
int _result = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _i = 0;
String _query = "";
int step12;
int limit12;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1413;BA.debugLine="Msgbox2Async(\"Please choose a command..\", \"Option";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please choose a command.."),BA.ObjectToCharSequence("Option"),"EDIT","CANCEL","DELETE",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1414;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 21;
return;
case 21:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1415;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 20;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1416;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1417;BA.debugLine="bg.Initialize2(Colors.RGB(0,167,255), 5, 0, Colo";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (167),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1418;BA.debugLine="BUTTON_ADD.Background = bg";
parent.mostCurrent._button_add.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1419;BA.debugLine="BUTTON_ADD.Text = \" Edit\"";
parent.mostCurrent._button_add.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 1420;BA.debugLine="BUTTON_CANCEL.Visible = True";
parent.mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1421;BA.debugLine="transaction_number = Value";
parent._transaction_number = BA.ObjectToString(_value);
 //BA.debugLineNum = 1422;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT * FROM re";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_disc_table WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"' AND transaction_number = '"+parent._transaction_number+"'")));
 //BA.debugLineNum = 1423;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 13;
if (parent._cursor6.getRowCount()>0) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1424;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step12 = 1;
limit12 = (int) (parent._cursor6.getRowCount()-1);
_i = (int) (0) ;
this.state = 22;
if (true) break;

case 22:
//C
this.state = 10;
if ((step12 > 0 && _i <= limit12) || (step12 < 0 && _i >= limit12)) this.state = 9;
if (true) break;

case 23:
//C
this.state = 22;
_i = ((int)(0 + _i + step12)) ;
if (true) break;

case 9:
//C
this.state = 23;
 //BA.debugLineNum = 1425;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 23;
;
 //BA.debugLineNum = 1426;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 1427;BA.debugLine="CMB_INVOICE.SelectedIndex = CMB_INVOICE.cmbBox";
parent.mostCurrent._cmb_invoice._setselectedindex /*int*/ (parent.mostCurrent._cmb_invoice._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("dr_no")));
 //BA.debugLineNum = 1428;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 23;
;
 //BA.debugLineNum = 1429;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = cursor6.GetStri";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("date_manufactured")));
 //BA.debugLineNum = 1430;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = cursor6.GetString";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("date_expired")));
 //BA.debugLineNum = 1431;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 26;
return;
case 26:
//C
this.state = 23;
;
 //BA.debugLineNum = 1432;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("unit")));
 //BA.debugLineNum = 1433;BA.debugLine="EDITTEXT_QUANTITY.Text = cursor6.GetString(\"qu";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("quantity")));
 //BA.debugLineNum = 1434;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 23;
;
 //BA.debugLineNum = 1435;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 1436;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 23;
;
 //BA.debugLineNum = 1437;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 1438;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 23;
;
 //BA.debugLineNum = 1439;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 1440;BA.debugLine="total_input = total_input - cursor6.GetString(";
parent._total_input = (int) (parent._total_input-(double)(Double.parseDouble(parent._cursor6.GetString("total_pieces"))));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 if (true) break;

case 12:
//C
this.state = 13;
 if (true) break;

case 13:
//C
this.state = 20;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1446;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this item?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1447;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1448;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 16:
//if
this.state = 19;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1452;BA.debugLine="Dim query As String = \"DELETE FROM receiving_di";
_query = "DELETE FROM receiving_disc_table WHERE po_doc_no = ? AND transaction_number = ?";
 //BA.debugLineNum = 1453;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._receiving_module._purchase_order_no /*String*/ ,BA.ObjectToString(_value)}));
 //BA.debugLineNum = 1454;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1455;BA.debugLine="GET_DETAILS";
_get_details();
 //BA.debugLineNum = 1458;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 19:
//C
this.state = 20;
;
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 1461;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _oncal() throws Exception{
 //BA.debugLineNum = 1632;BA.debugLine="Sub oncal";
 //BA.debugLineNum = 1633;BA.debugLine="LoadTexts";
_loadtexts();
 //BA.debugLineNum = 1635;BA.debugLine="Scale_Calc.SetRate(0.6)";
mostCurrent._scale_calc._setrate /*String*/ (mostCurrent.activityBA,0.6);
 //BA.debugLineNum = 1636;BA.debugLine="ScaleAuto = Scale_Calc.GetScaleDS";
_scaleauto = mostCurrent._scale_calc._getscaleds /*double*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1653;BA.debugLine="lblPaperRoll.Initialize(\"lblPaperRoll\")";
mostCurrent._lblpaperroll.Initialize(mostCurrent.activityBA,"lblPaperRoll");
 //BA.debugLineNum = 1654;BA.debugLine="scvPaperRoll.Panel.AddView(lblPaperRoll, 0, 0, 10";
mostCurrent._scvpaperroll.getPanel().AddView((android.view.View)(mostCurrent._lblpaperroll.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1655;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1656;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 //BA.debugLineNum = 1657;BA.debugLine="lblPaperRoll.Color = Colors.White";
mostCurrent._lblpaperroll.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1658;BA.debugLine="lblPaperRoll.TextColor = Colors.Black";
mostCurrent._lblpaperroll.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1674;BA.debugLine="End Sub";
return "";
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 317;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 318;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 319;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 320;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 312;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 313;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 314;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 315;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return "";
}
public static String  _operation(String _op) throws Exception{
 //BA.debugLineNum = 1811;BA.debugLine="Sub Operation(Op As String)";
 //BA.debugLineNum = 1812;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1814;BA.debugLine="Txt = Txt & CRLF & \"+ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"+ ";
 break; }
case 1: {
 //BA.debugLineNum = 1816;BA.debugLine="Txt = Txt & CRLF & \"- \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"- ";
 break; }
case 2: {
 //BA.debugLineNum = 1818;BA.debugLine="Txt = Txt & CRLF & \"× \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"× ";
 break; }
case 3: {
 //BA.debugLineNum = 1820;BA.debugLine="Txt = Txt & CRLF & \"/ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"/ ";
 break; }
case 4: {
 //BA.debugLineNum = 1822;BA.debugLine="Txt = Txt & CRLF & \"x2 \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"x2 ";
 break; }
case 5: {
 //BA.debugLineNum = 1824;BA.debugLine="Txt = Txt & CRLF & \"√ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"√ ";
 break; }
case 6: {
 //BA.debugLineNum = 1826;BA.debugLine="Txt = Txt & CRLF & \"1/x \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"1/x ";
 break; }
case 7: {
 //BA.debugLineNum = 1828;BA.debugLine="Txt = Txt & CRLF & \"% \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"% ";
 break; }
}
;
 //BA.debugLineNum = 1830;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_calcu_click() throws Exception{
 //BA.debugLineNum = 1943;BA.debugLine="Sub PANEL_BG_CALCU_Click";
 //BA.debugLineNum = 1944;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1945;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_delivery_click() throws Exception{
 //BA.debugLineNum = 2288;BA.debugLine="Sub PANEL_BG_DELIVERY_Click";
 //BA.debugLineNum = 2289;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2290;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_input_click() throws Exception{
 //BA.debugLineNum = 1080;BA.debugLine="Sub PANEL_BG_INPUT_Click";
 //BA.debugLineNum = 1081;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1082;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_invoice_click() throws Exception{
 //BA.debugLineNum = 2296;BA.debugLine="Sub PANEL_BG_INVOICE_Click";
 //BA.debugLineNum = 2297;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2298;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_msgbox_click() throws Exception{
 //BA.debugLineNum = 2292;BA.debugLine="Sub PANEL_BG_MSGBOX_Click";
 //BA.debugLineNum = 2293;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2294;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_security_click() throws Exception{
 //BA.debugLineNum = 2300;BA.debugLine="Sub PANEL_BG_SECURITY_Click";
 //BA.debugLineNum = 2301;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2302;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim connection As SQL";
_connection = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 13;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim cursor2 As Cursor";
_cursor2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim cursor3 As Cursor";
_cursor3 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim cursor4 As Cursor";
_cursor4 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim cursor5 As Cursor";
_cursor5 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim cursor6 As Cursor";
_cursor6 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim cursor7 As Cursor";
_cursor7 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim cursor8 As Cursor";
_cursor8 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim cursor9 As Cursor";
_cursor9 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim cursor10 As Cursor";
_cursor10 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim ProgPath = File.DirRootExternal & \"/TapeCalc\"";
_progpath = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/TapeCalc";
 //BA.debugLineNum = 26;BA.debugLine="Dim ScaleAuto As Double";
_scaleauto = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim Texts(8) As String";
_texts = new String[(int) (8)];
java.util.Arrays.fill(_texts,"");
 //BA.debugLineNum = 28;BA.debugLine="Dim LanguageID As String";
_languageid = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim sVal = \"\" As String";
_sval = "";
 //BA.debugLineNum = 31;BA.debugLine="Dim Val = 0 As Double";
_val = 0;
 //BA.debugLineNum = 32;BA.debugLine="Dim Op0 = \"\" As String";
_op0 = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim Result1 = 0 As Double";
_result1 = 0;
 //BA.debugLineNum = 34;BA.debugLine="Dim Txt = \"\" As String";
_txt = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim New1 = 0 As Int";
_new1 = (int) (0);
 //BA.debugLineNum = 38;BA.debugLine="Dim old_doc_no As String";
_old_doc_no = "";
 //BA.debugLineNum = 40;BA.debugLine="Private cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private uploadBitmap As Bitmap";
_uploadbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 45;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 46;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 50;BA.debugLine="Public principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 51;BA.debugLine="Public product_id As String";
_product_id = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim reason As String";
_reason = "";
 //BA.debugLineNum = 54;BA.debugLine="Dim input_type As String";
_input_type = "";
 //BA.debugLineNum = 55;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 56;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 58;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 59;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 60;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 61;BA.debugLine="Dim total_pieces As Int";
_total_pieces = 0;
 //BA.debugLineNum = 62;BA.debugLine="Dim price As Int";
_price = 0;
 //BA.debugLineNum = 63;BA.debugLine="Dim total_price As Int";
_total_price = 0;
 //BA.debugLineNum = 64;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 66;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 68;BA.debugLine="Dim total_order As Int";
_total_order = 0;
 //BA.debugLineNum = 69;BA.debugLine="Dim total_input As Int";
_total_input = 0;
 //BA.debugLineNum = 71;BA.debugLine="Dim lifespan_month As String";
_lifespan_month = "";
 //BA.debugLineNum = 72;BA.debugLine="Dim lifespan_year As String";
_lifespan_year = "";
 //BA.debugLineNum = 74;BA.debugLine="Dim default_reading As String";
_default_reading = "";
 //BA.debugLineNum = 76;BA.debugLine="Dim monthexp As String";
_monthexp = "";
 //BA.debugLineNum = 77;BA.debugLine="Dim yearexp As String";
_yearexp = "";
 //BA.debugLineNum = 79;BA.debugLine="Dim monthmfg As String";
_monthmfg = "";
 //BA.debugLineNum = 80;BA.debugLine="Dim yearmfg As String";
_yearmfg = "";
 //BA.debugLineNum = 81;BA.debugLine="Dim error_trigger As String";
_error_trigger = "";
 //BA.debugLineNum = 82;BA.debugLine="Dim security_trigger As String";
_security_trigger = "";
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 606;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 607;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 608;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("788539138","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 609;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 610;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 611;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 612;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 613;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 614;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 616;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 617;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 618;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 619;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 620;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 622;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("788539152","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 623;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 624;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 625;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 628;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 323;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 324;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 325;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 326;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 327;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 328;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 329;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 330;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 331;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 332;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
String _mac = "";
anywheresoftware.b4a.objects.collections.Map _paireddevices = null;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _iq = 0;
int _result = 0;
int step6;
int limit6;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 579;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 580;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 581;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 582;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 583;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 584;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
if (true) break;

case 1:
//for
this.state = 4;
step6 = 1;
limit6 = (int) (_paireddevices.getSize()-1);
_iq = (int) (0) ;
this.state = 18;
if (true) break;

case 18:
//C
this.state = 4;
if ((step6 > 0 && _iq <= limit6) || (step6 < 0 && _iq >= limit6)) this.state = 3;
if (true) break;

case 19:
//C
this.state = 18;
_iq = ((int)(0 + _iq + step6)) ;
if (true) break;

case 3:
//C
this.state = 19;
 //BA.debugLineNum = 585;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 586;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 588;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 589;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 591;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 592;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 593;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 8:
//if
this.state = 17;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 594;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
if (true) break;

case 11:
//if
this.state = 16;
if ((_ls.Get(_result)).equals((Object)("No device(s) found..."))) { 
this.state = 13;
}else {
this.state = 15;
}if (true) break;

case 13:
//C
this.state = 16;
 //BA.debugLineNum = 595;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 597;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 599;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("788473621",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 600;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 601;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 602;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 16:
//C
this.state = 17;
;
 if (true) break;

case 17:
//C
this.state = -1;
;
 //BA.debugLineNum = 605;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_invoice_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 2059;BA.debugLine="Sub TABLE_INVOICE_DataUpdated";
 //BA.debugLineNum = 2060;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 2062;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn2[(int) (0)]),(Object)(mostCurrent._namecolumn2[(int) (1)]),(Object)(mostCurrent._namecolumn2[(int) (2)]),(Object)(mostCurrent._namecolumn2[(int) (3)]),(Object)(mostCurrent._namecolumn2[(int) (4)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 2063;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 2064;BA.debugLine="For i = 0 To TABLE_INVOICE.VisibleRowIds.Size";
{
final int step4 = 1;
final int limit4 = mostCurrent._table_invoice._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 2065;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 2066;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 2067;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 2071;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 2072;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 2073;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 2077;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group14 = new Object[]{(Object)(mostCurrent._namecolumn2[(int) (0)]),(Object)(mostCurrent._namecolumn2[(int) (1)]),(Object)(mostCurrent._namecolumn2[(int) (2)]),(Object)(mostCurrent._namecolumn2[(int) (3)]),(Object)(mostCurrent._namecolumn2[(int) (4)])};
final int groupLen14 = group14.length
;int index14 = 0;
;
for (; index14 < groupLen14;index14++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group14[index14]);
 //BA.debugLineNum = 2079;BA.debugLine="For i = 0 To TABLE_INVOICE.VisibleRowIds.Size";
{
final int step15 = 1;
final int limit15 = mostCurrent._table_invoice._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit15 ;_i = _i + step15 ) {
 //BA.debugLineNum = 2080;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 2081;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 2082;BA.debugLine="lbl.Font = xui.CreateDefaultBoldFont(12)";
_lbl.setFont(mostCurrent._xui.CreateDefaultBoldFont((float) (12)));
 }
};
 }
};
 //BA.debugLineNum = 2086;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 2087;BA.debugLine="TABLE_INVOICE.Refresh";
mostCurrent._table_invoice._refresh /*String*/ ();
 //BA.debugLineNum = 2088;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 2090;BA.debugLine="End Sub";
return "";
}
public static String  _table_purchase_order_cellclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
 //BA.debugLineNum = 555;BA.debugLine="Sub TABLE_PURCHASE_ORDER_CellClicked (ColumnId As";
 //BA.debugLineNum = 556;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 557;BA.debugLine="Dim RowData As Map = TABLE_PURCHASE_ORDER.GetRow(";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._table_purchase_order._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 558;BA.debugLine="End Sub";
return "";
}
public static void  _table_purchase_order_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_PURCHASE_ORDER_CellLongClicked rsub = new ResumableSub_TABLE_PURCHASE_ORDER_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_PURCHASE_ORDER_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_PURCHASE_ORDER_CellLongClicked(wingan.app.receiving2_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.receiving2_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 560;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 562;BA.debugLine="Dim RowData As Map = TABLE_PURCHASE_ORDER.GetRow(";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_purchase_order._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 563;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 565;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 566;BA.debugLine="PANEL_BG_SECURITY.BringToFront";
parent.mostCurrent._panel_bg_security.BringToFront();
 //BA.debugLineNum = 567;BA.debugLine="GET_SECURITY";
_get_security();
 //BA.debugLineNum = 568;BA.debugLine="security_trigger = \"MANUAL TABLE\"";
parent._security_trigger = "MANUAL TABLE";
 //BA.debugLineNum = 569;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = RowData.Get(\"Produc";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_rowdata.Get((Object)("Product Description"))));
 //BA.debugLineNum = 570;BA.debugLine="EDITTEXT_PASSWORD.RequestFocus";
parent.mostCurrent._edittext_password.RequestFocus();
 //BA.debugLineNum = 571;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 572;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"NO EXPIRATION\"";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("NO EXPIRATION"));
 //BA.debugLineNum = 573;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 574;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PASSWORD)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_password.getObject()));
 //BA.debugLineNum = 575;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_purchase_order_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _maxheight = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
long _rowid = 0L;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl1 = null;
anywheresoftware.b4a.objects.collections.Map _row = null;
int _clr = 0;
String _othercolumnvalue = "";
 //BA.debugLineNum = 499;BA.debugLine="Sub TABLE_PURCHASE_ORDER_DataUpdated";
 //BA.debugLineNum = 500;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 501;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 503;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 504;BA.debugLine="Dim MaxHeight As Int";
_maxheight = 0;
 //BA.debugLineNum = 505;BA.debugLine="For i = 0 To TABLE_PURCHASE_ORDER.VisibleRowIds.";
{
final int step5 = 1;
final int limit5 = mostCurrent._table_purchase_order._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 506;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 507;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 508;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 513;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 514;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 515;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 520;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group15 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen15 = group15.length
;int index15 = 0;
;
for (; index15 < groupLen15;index15++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group15[index15]);
 //BA.debugLineNum = 522;BA.debugLine="For i = 0 To TABLE_PURCHASE_ORDER.VisibleRowIds.";
{
final int step16 = 1;
final int limit16 = mostCurrent._table_purchase_order._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit16 ;_i = _i + step16 ) {
 //BA.debugLineNum = 523;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 524;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 525;BA.debugLine="lbl.Font = xui.CreateDefaultBoldFont(18)";
_lbl.setFont(mostCurrent._xui.CreateDefaultBoldFont((float) (18)));
 }
};
 }
};
 //BA.debugLineNum = 529;BA.debugLine="For i = 0 To TABLE_PURCHASE_ORDER.VisibleRowIds.S";
{
final int step22 = 1;
final int limit22 = (int) (mostCurrent._table_purchase_order._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit22 ;_i = _i + step22 ) {
 //BA.debugLineNum = 530;BA.debugLine="Dim RowId As Long = TABLE_PURCHASE_ORDER.Visible";
_rowid = BA.ObjectToLongNumber(mostCurrent._table_purchase_order._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 531;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 532;BA.debugLine="Dim pnl1 As B4XView = NameColumn(4).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (4)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 533;BA.debugLine="Dim row As Map = TABLE_PURCHASE_ORDER.GetRow(Ro";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._table_purchase_order._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 534;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 535;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (4)].Id /*String*/ )));
 //BA.debugLineNum = 536;BA.debugLine="If OtherColumnValue > 0 Then";
if ((double)(Double.parseDouble(_othercolumnvalue))>0) { 
 //BA.debugLineNum = 537;BA.debugLine="clr = xui.Color_Blue";
_clr = mostCurrent._xui.Color_Blue;
 }else if((double)(Double.parseDouble(_othercolumnvalue))<0) { 
 //BA.debugLineNum = 539;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 }else {
 //BA.debugLineNum = 541;BA.debugLine="clr = xui.Color_Green";
_clr = mostCurrent._xui.Color_Green;
 };
 //BA.debugLineNum = 543;BA.debugLine="pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Co";
_pnl1.GetView((int) (0)).SetColorAndBorder(_clr,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)),(int) (0));
 };
 }
};
 //BA.debugLineNum = 547;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group39 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)])};
final int groupLen39 = group39.length
;int index39 = 0;
;
for (; index39 < groupLen39;index39++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group39[index39]);
 //BA.debugLineNum = 548;BA.debugLine="Column.InternalSortMode= \"ASC\"";
_column.InternalSortMode /*String*/  = "ASC";
 }
};
 //BA.debugLineNum = 550;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 551;BA.debugLine="TABLE_PURCHASE_ORDER.Refresh";
mostCurrent._table_purchase_order._refresh /*String*/ ();
 //BA.debugLineNum = 552;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 554;BA.debugLine="End Sub";
return "";
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 761;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 762;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 763;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 764;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("788801283","Trying to connect...",0);
 //BA.debugLineNum = 765;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 766;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 767;BA.debugLine="End Sub";
return "";
}
public static void  _update_receiving() throws Exception{
ResumableSub_UPDATE_RECEIVING rsub = new ResumableSub_UPDATE_RECEIVING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_RECEIVING extends BA.ResumableSub {
public ResumableSub_UPDATE_RECEIVING(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2257;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Status...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Status..."));
 //BA.debugLineNum = 2258;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_rece";
_cmd = _createcommand("update_receiving_ref",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())),(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())),(Object)("RECEIVED"),(Object)(parent.mostCurrent._receiving_module._purchase_order_no /*String*/ )});
 //BA.debugLineNum = 2259;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2260;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2261;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 2262;BA.debugLine="connection.ExecNonQuery(\"UPDATE receiving_ref_ta";
parent._connection.ExecNonQuery("UPDATE receiving_ref_table SET received_date = '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', received_time = '"+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', po_status = 'RECEIVED' WHERE po_doc_no = '"+parent.mostCurrent._receiving_module._purchase_order_no /*String*/ +"'");
 //BA.debugLineNum = 2264;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2265;BA.debugLine="ToastMessageShow(\"Transaction Uploaded Succefull";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Uploaded Succefully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2266;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 2267;BA.debugLine="StartActivity(RECEIVING_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._receiving_module.getObject()));
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2269;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 2270;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2271;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("792995599","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2272;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2273;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2274;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 14;
return;
case 14:
//C
this.state = 6;
;
 //BA.debugLineNum = 2275;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2276;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 15;
return;
case 15:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2277;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 2278;BA.debugLine="DELETE_RECEIVING_DISC";
_delete_receiving_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 2280;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2281;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 2284;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2285;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 344;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 345;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 346;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static void  _updatetape() throws Exception{
ResumableSub_UpdateTape rsub = new ResumableSub_UpdateTape(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UpdateTape extends BA.ResumableSub {
public ResumableSub_UpdateTape(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
float _hr = 0f;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1856;BA.debugLine="Dim hr As Float";
_hr = 0f;
 //BA.debugLineNum = 1858;BA.debugLine="lblPaperRoll.Text = Txt";
parent.mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(parent._txt));
 //BA.debugLineNum = 1860;BA.debugLine="hr = stu.MeasureMultilineTextHeight(lblPaperRoll,";
_hr = (float) (parent.mostCurrent._stu.MeasureMultilineTextHeight((android.widget.TextView)(parent.mostCurrent._lblpaperroll.getObject()),BA.ObjectToCharSequence(parent._txt)));
 //BA.debugLineNum = 1861;BA.debugLine="If hr > scvPaperRoll.Height Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_hr>parent.mostCurrent._scvpaperroll.getHeight()) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1862;BA.debugLine="lblPaperRoll.Height = hr";
parent.mostCurrent._lblpaperroll.setHeight((int) (_hr));
 //BA.debugLineNum = 1863;BA.debugLine="scvPaperRoll.Panel.Height = hr";
parent.mostCurrent._scvpaperroll.getPanel().setHeight((int) (_hr));
 //BA.debugLineNum = 1864;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 4;
;
 //BA.debugLineNum = 1865;BA.debugLine="scvPaperRoll.ScrollPosition = hr";
parent.mostCurrent._scvpaperroll.setScrollPosition((int) (_hr));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1867;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _view_dr() throws Exception{
ResumableSub_VIEW_DR rsub = new ResumableSub_VIEW_DR(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_VIEW_DR extends BA.ResumableSub {
public ResumableSub_VIEW_DR(wingan.app.receiving2_module parent) {
this.parent = parent;
}
wingan.app.receiving2_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _background = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _background2 = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 789;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 791;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 792;BA.debugLine="LISTVIEW_DR.Background = bg";
parent.mostCurrent._listview_dr.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 793;BA.debugLine="Dim background As ColorDrawable";
_background = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 794;BA.debugLine="PANEL_BG_DELIVERY.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_delivery.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 795;BA.debugLine="PANEL_BG_DELIVERY.BringToFront";
parent.mostCurrent._panel_bg_delivery.BringToFront();
 //BA.debugLineNum = 796;BA.debugLine="background.Initialize2(Colors.White, 5, 1, Colors";
_background.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 797;BA.debugLine="EDITTEXT_TRUCKING .Background = background";
parent.mostCurrent._edittext_trucking.setBackground((android.graphics.drawable.Drawable)(_background.getObject()));
 //BA.debugLineNum = 798;BA.debugLine="EDITTEXT_DRIVER.Background = background";
parent.mostCurrent._edittext_driver.setBackground((android.graphics.drawable.Drawable)(_background.getObject()));
 //BA.debugLineNum = 799;BA.debugLine="EDITTEXT_DOC_NO.Background = background";
parent.mostCurrent._edittext_doc_no.setBackground((android.graphics.drawable.Drawable)(_background.getObject()));
 //BA.debugLineNum = 800;BA.debugLine="EDITTEXT_TRUCKTYPE.Background = background";
parent.mostCurrent._edittext_trucktype.setBackground((android.graphics.drawable.Drawable)(_background.getObject()));
 //BA.debugLineNum = 801;BA.debugLine="Dim background2 As ColorDrawable";
_background2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 802;BA.debugLine="background2.Initialize2(Colors.White, 5, 1, Color";
_background2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 803;BA.debugLine="EDITTEXT_PLATE.Background = background2";
parent.mostCurrent._edittext_plate.setBackground((android.graphics.drawable.Drawable)(_background2.getObject()));
 //BA.debugLineNum = 804;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 805;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.Label.Typeface = Typef";
parent.mostCurrent._listview_dr.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 806;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.Label.TextSize = 20";
parent.mostCurrent._listview_dr.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 807;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.label.Height = 8%y";
parent.mostCurrent._listview_dr.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 808;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.Label.TextColor = Colo";
parent.mostCurrent._listview_dr.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 809;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.Label.Gravity = Gravit";
parent.mostCurrent._listview_dr.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 810;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.SecondLabel.Typeface =";
parent.mostCurrent._listview_dr.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 811;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.SecondLabel.Top = 7%y";
parent.mostCurrent._listview_dr.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 812;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.SecondLabel.TextSize =";
parent.mostCurrent._listview_dr.getTwoLinesLayout().SecondLabel.setTextSize((float) (10));
 //BA.debugLineNum = 813;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.SecondLabel.Height = 4";
parent.mostCurrent._listview_dr.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 814;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.SecondLabel.TextColor";
parent.mostCurrent._listview_dr.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 815;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.SecondLabel.Gravity =";
parent.mostCurrent._listview_dr.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 816;BA.debugLine="LISTVIEW_DR.TwoLinesLayout.ItemHeight = 12%y";
parent.mostCurrent._listview_dr.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 817;BA.debugLine="EDITTEXT_PLATE.RequestFocus";
parent.mostCurrent._edittext_plate.RequestFocus();
 //BA.debugLineNum = 818;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 819;BA.debugLine="CHECK_DOC";
_check_doc();
 //BA.debugLineNum = 820;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
	public boolean _onCreateOptionsMenu(android.view.Menu menu) {
		if (processBA.subExists("activity_createmenu")) {
			processBA.raiseEvent2(null, true, "activity_createmenu", false, new de.amberhome.objects.appcompat.ACMenuWrapper(menu));
			return true;
		}
		else
			return false;
	}
}
