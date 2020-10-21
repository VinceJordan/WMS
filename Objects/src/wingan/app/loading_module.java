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

public class loading_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static loading_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.loading_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (loading_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.loading_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.loading_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (loading_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (loading_module) Resume **");
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
		return loading_module.class;
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
            BA.LogInfo("** Activity (loading_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (loading_module) Pause event (activity is not paused). **");
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
            loading_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (loading_module) Resume **");
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
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _clearbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _addbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static String _picklist_id = "";
public static anywheresoftware.b4a.phone.Phone _phone = null;
public static anywheresoftware.b4a.obejcts.TTS _tts1 = null;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static String _security_trigger = "";
public static String _over_short_trigger = "";
public static String _product_id = "";
public static String _principal_id = "";
public static String _principal_name = "";
public static String _reason = "";
public static String _scan_code = "";
public static String _caseper = "";
public static String _pcsper = "";
public static String _dozper = "";
public static String _boxper = "";
public static String _bagper = "";
public static String _packper = "";
public static String _total_pieces = "";
public static String _scan_next_trigger = "";
public static String _scan_trigger = "";
public static String _loading_count = "";
public static String _downloaded_count = "";
public static int _loaded_count = 0;
public static int _uploaded_count = 0;
public static String _unit_trigger = "";
public anywheresoftware.b4a.objects.IME _ctrl = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _bg_enable = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _bg_disable = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _bg_update = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public wingan.app.b4xtable _loading_table = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_type = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_type = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_name = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_date = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_status = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_onhold = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_delivery = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_autofill = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_upload = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_security = null;
public wingan.app.b4xcombobox _cmb_account = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_password = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_pcs_qty_loading = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_case_qty_loading = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_box_qty_loading = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_doz_qty_loading = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_pack_qty_loading = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_bag_qty_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_desc_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_pcs_qty_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_case_qty_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_box_qty_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_doz_qty_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_pack_qty_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_bag_qty_loading = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_loading = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_pcs_loading = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_case_loading = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_box_loading = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_pack_loading = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_doz_loading = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_bag_loading = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_checker = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_trail = null;
public wingan.app.b4xcombobox _cmb_picker = null;
public wingan.app.b4xcombobox _cmb_reason = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_pushcart = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_helper3 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_helper2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_helper1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_driver = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_plate = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_delivery = null;
public wingan.app.b4xcombobox _cmb_supervisor = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_scancode = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_trail_pass = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
public wingan.app.dashboard_module _dashboard_module = null;
public wingan.app.return_module _return_module = null;
public wingan.app.cancelled_module _cancelled_module = null;
public wingan.app.sales_return_module _sales_return_module = null;
public wingan.app.preparing_module _preparing_module = null;
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
public wingan.app.receiving2_module _receiving2_module = null;
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
public ResumableSub_Activity_Create(wingan.app.loading_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.loading_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg2 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg3 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg4 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg5 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg6 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg7 = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 163;BA.debugLine="Activity.LoadLayout(\"picklist_loading\")";
parent.mostCurrent._activity.LoadLayout("picklist_loading",mostCurrent.activityBA);
 //BA.debugLineNum = 165;BA.debugLine="clearBitmap = LoadBitmap(File.DirAssets, \"clear.p";
parent._clearbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"clear.png");
 //BA.debugLineNum = 166;BA.debugLine="addBitmap = LoadBitmap(File.DirAssets, \"pencil.pn";
parent._addbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"pencil.png");
 //BA.debugLineNum = 167;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 169;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 170;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 171;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 172;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 173;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 174;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 175;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 177;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 178;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 179;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 182;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 183;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 185;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 186;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 187;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 189;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 190;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 193;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 194;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 195;BA.debugLine="EDITTEXT_TYPE.Background = bg";
parent.mostCurrent._edittext_type.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 196;BA.debugLine="EDITTEXT_PASSWORD.Background = bg";
parent.mostCurrent._edittext_password.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 198;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 199;BA.debugLine="bg2.Initialize2(Colors.RGB(182,217,255), 0, 1, Co";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (182),(int) (217),(int) (255)),(int) (0),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 200;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.Background = bg2";
parent.mostCurrent._edittext_pcs_qty_loading.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 201;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.Background = bg2";
parent.mostCurrent._edittext_case_qty_loading.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 202;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.Background = bg2";
parent.mostCurrent._edittext_doz_qty_loading.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 203;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.Background = bg2";
parent.mostCurrent._edittext_box_qty_loading.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 204;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.Background = bg2";
parent.mostCurrent._edittext_bag_qty_loading.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 205;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.Background = bg2";
parent.mostCurrent._edittext_pack_qty_loading.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 207;BA.debugLine="Dim bg3 As ColorDrawable";
_bg3 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 208;BA.debugLine="bg3.Initialize2(Colors.White, 5, 1, Colors.RGB(21";
_bg3.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 209;BA.debugLine="EDITTEXT_QUANTITY.Background = bg3";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg3.getObject()));
 //BA.debugLineNum = 212;BA.debugLine="Dim bg4 As ColorDrawable";
_bg4 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 213;BA.debugLine="bg4.Initialize2(Colors.White, 5, 1, Colors.RGB(21";
_bg4.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 214;BA.debugLine="EDITTEXT_PLATE.Background = bg4";
parent.mostCurrent._edittext_plate.setBackground((android.graphics.drawable.Drawable)(_bg4.getObject()));
 //BA.debugLineNum = 216;BA.debugLine="Dim bg5 As ColorDrawable";
_bg5 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 217;BA.debugLine="bg5.Initialize2(Colors.White, 5, 1, Colors.RGB(21";
_bg5.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 218;BA.debugLine="EDITTEXT_PUSHCART.Background = bg5";
parent.mostCurrent._edittext_pushcart.setBackground((android.graphics.drawable.Drawable)(_bg5.getObject()));
 //BA.debugLineNum = 220;BA.debugLine="Dim bg6 As ColorDrawable";
_bg6 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 221;BA.debugLine="bg6.Initialize2(Colors.White, 5, 1, Colors.RGB(21";
_bg6.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 222;BA.debugLine="EDITTEXT_DRIVER.Background = bg6";
parent.mostCurrent._edittext_driver.setBackground((android.graphics.drawable.Drawable)(_bg6.getObject()));
 //BA.debugLineNum = 223;BA.debugLine="EDITTEXT_HELPER1.Background = bg6";
parent.mostCurrent._edittext_helper1.setBackground((android.graphics.drawable.Drawable)(_bg6.getObject()));
 //BA.debugLineNum = 224;BA.debugLine="EDITTEXT_HELPER2.Background = bg6";
parent.mostCurrent._edittext_helper2.setBackground((android.graphics.drawable.Drawable)(_bg6.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="EDITTEXT_HELPER3.Background = bg6";
parent.mostCurrent._edittext_helper3.setBackground((android.graphics.drawable.Drawable)(_bg6.getObject()));
 //BA.debugLineNum = 228;BA.debugLine="Dim bg7 As ColorDrawable";
_bg7 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 229;BA.debugLine="bg7.Initialize2(Colors.White, 5, 1, Colors.RGB(21";
_bg7.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 230;BA.debugLine="EDITTEXT_TRAIL_PASS.Background = bg7";
parent.mostCurrent._edittext_trail_pass.setBackground((android.graphics.drawable.Drawable)(_bg7.getObject()));
 //BA.debugLineNum = 232;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 233;BA.debugLine="LOAD_LOADING_HEADER";
_load_loading_header();
 //BA.debugLineNum = 235;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 236;BA.debugLine="Dim Ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 237;BA.debugLine="Ref.Target = EDITTEXT_TYPE ' The text field being";
_ref.Target = (Object)(parent.mostCurrent._edittext_type.getObject());
 //BA.debugLineNum = 238;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 240;BA.debugLine="picklist_id = \"\"";
parent._picklist_id = "";
 //BA.debugLineNum = 242;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 244;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 245;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 247;BA.debugLine="BG_ENABLE.Initialize2(Colors.RGB(27,255,0),0,1,Co";
parent.mostCurrent._bg_enable.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (27),(int) (255),(int) (0)),(int) (0),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 248;BA.debugLine="BG_DISABLE.Initialize2(Colors.RGB(174,174,174),0,";
parent.mostCurrent._bg_disable.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (174),(int) (174),(int) (174)),(int) (0),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 249;BA.debugLine="BG_UPDATE.Initialize2(Colors.Yellow,0,1,Colors.Wh";
parent.mostCurrent._bg_update.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Yellow,(int) (0),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
ResumableSub_Activity_CreateMenu rsub = new ResumableSub_Activity_CreateMenu(null,_menu);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_CreateMenu extends BA.ResumableSub {
public ResumableSub_Activity_CreateMenu(wingan.app.loading_module parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.loading_module parent;
de.amberhome.objects.appcompat.ACMenuWrapper _menu;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 253;BA.debugLine="CLEAR_PICKLIST";
_clear_picklist();
 //BA.debugLineNum = 254;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 255;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 256;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 257;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 258;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 2423;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 2424;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 2425;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 2427;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 270;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 271;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("750921473","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 272;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 273;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 274;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 275;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 260;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 261;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("750855937","Resuming...",0);
 //BA.debugLineNum = 262;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 263;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 264;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 266;BA.debugLine="If TTS1.IsInitialized = False Then";
if (_tts1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 267;BA.debugLine="TTS1.Initialize(\"TTS1\")";
_tts1.Initialize(processBA,"TTS1");
 };
 //BA.debugLineNum = 269;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 317;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 318;BA.debugLine="If Item.Title = \"Load Picklist\" Then";
if ((_item.getTitle()).equals("Load Picklist")) { 
 //BA.debugLineNum = 319;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,True)";
mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 320;BA.debugLine="PANEL_BG_TYPE.BringToFront";
mostCurrent._panel_bg_type.BringToFront();
 //BA.debugLineNum = 321;BA.debugLine="EDITTEXT_TYPE.Text = \"\"";
mostCurrent._edittext_type.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 322;BA.debugLine="EDITTEXT_TYPE.RequestFocus";
mostCurrent._edittext_type.RequestFocus();
 //BA.debugLineNum = 323;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_TYPE)";
mostCurrent._ctrl.ShowKeyboard((android.view.View)(mostCurrent._edittext_type.getObject()));
 }else if((_item.getTitle()).equals("cart")) { 
 //BA.debugLineNum = 326;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("751380233","Resuming...",0);
 //BA.debugLineNum = 327;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 328;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 329;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 330;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 331;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 332;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 333;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 335;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 336;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 }else if((_item.getTitle()).equals("Clear")) { 
 //BA.debugLineNum = 339;BA.debugLine="CLEAR_PICKLIST";
_clear_picklist();
 };
 //BA.debugLineNum = 341;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 299;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 300;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 301;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 302;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 303;BA.debugLine="End Sub";
return "";
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 547;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 548;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("751642369","Connection broken...",0);
 //BA.debugLineNum = 549;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 550;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 551;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 552;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 553;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 554;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 555;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 556;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 558;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 560;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.loading_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.loading_module parent;
byte[] _buffer;
int _result = 0;
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int step26;
int limit26;
int step40;
int limit40;
int step64;
int limit64;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 396;BA.debugLine="If scan_trigger = 0 Then";
if (true) break;

case 1:
//if
this.state = 83;
if ((parent._scan_trigger).equals(BA.NumberToString(0))) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 397;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buff";
anywheresoftware.b4a.keywords.Common.LogImpl("751576834","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 398;BA.debugLine="If picklist_id = \"\" Then";
if (true) break;

case 4:
//if
this.state = 82;
if ((parent._picklist_id).equals("")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 82;
 //BA.debugLineNum = 399;BA.debugLine="picklist_id = BytesToString(Buffer, 0, Buffer.L";
parent._picklist_id = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8").trim();
 //BA.debugLineNum = 400;BA.debugLine="VALIDATE_PICKLIST_STATUS";
_validate_picklist_status();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 403;BA.debugLine="Log(\"aaa\")";
anywheresoftware.b4a.keywords.Common.LogImpl("751576840","aaa",0);
 //BA.debugLineNum = 404;BA.debugLine="If scan_next_trigger = 1 Then";
if (true) break;

case 9:
//if
this.state = 18;
if ((parent._scan_next_trigger).equals(BA.NumberToString(1))) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 405;BA.debugLine="Msgbox2Async(\"You scan another item and will l";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You scan another item and will left the recent item with no transaction."+anywheresoftware.b4a.keywords.Common.CRLF+"Are you sure you want to continue to the next item?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 406;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 84;
return;
case 84:
//C
this.state = 12;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 407;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 12:
//if
this.state = 17;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 408;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 410;BA.debugLine="scan_next_trigger = 1";
parent._scan_next_trigger = BA.NumberToString(1);
 if (true) break;

case 17:
//C
this.state = 18;
;
 if (true) break;

case 18:
//C
this.state = 19;
;
 //BA.debugLineNum = 413;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 85;
return;
case 85:
//C
this.state = 19;
;
 //BA.debugLineNum = 414;BA.debugLine="If scan_next_trigger = 0 Then";
if (true) break;

case 19:
//if
this.state = 81;
if ((parent._scan_next_trigger).equals(BA.NumberToString(0))) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 415;BA.debugLine="scan_next_trigger = 1";
parent._scan_next_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 416;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 417;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 419;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 420;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 22:
//if
this.state = 43;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 24;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 36;
}else {
this.state = 42;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 421;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 422;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 423;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 25:
//for
this.state = 28;
step26 = 1;
limit26 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 86;
if (true) break;

case 86:
//C
this.state = 28;
if ((step26 > 0 && _row <= limit26) || (step26 < 0 && _row >= limit26)) this.state = 27;
if (true) break;

case 87:
//C
this.state = 86;
_row = ((int)(0 + _row + step26)) ;
if (true) break;

case 27:
//C
this.state = 87;
 //BA.debugLineNum = 424;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 425;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 426;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 28:
//C
this.state = 29;
;
 //BA.debugLineNum = 428;BA.debugLine="InputListAsync(ls, \"Choose Description :\", -1";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose Description :"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 429;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 88;
return;
case 88:
//C
this.state = 29;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 430;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 29:
//if
this.state = 34;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 31;
}else {
this.state = 33;
}if (true) break;

case 31:
//C
this.state = 34;
 //BA.debugLineNum = 431;BA.debugLine="LABEL_LOAD_DESC_LOADING.Text = ls.Get(Result";
parent.mostCurrent._label_load_desc_loading.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 432;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 434;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 34:
//C
this.state = 43;
;
 if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 441;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 37:
//for
this.state = 40;
step40 = 1;
limit40 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 89;
if (true) break;

case 89:
//C
this.state = 40;
if ((step40 > 0 && _row <= limit40) || (step40 < 0 && _row >= limit40)) this.state = 39;
if (true) break;

case 90:
//C
this.state = 89;
_row = ((int)(0 + _row + step40)) ;
if (true) break;

case 39:
//C
this.state = 90;
 //BA.debugLineNum = 442;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 443;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("751576880",BA.NumberToString(1),0);
 //BA.debugLineNum = 444;BA.debugLine="LABEL_LOAD_DESC_LOADING.Text = cursor2.GetSt";
parent.mostCurrent._label_load_desc_loading.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 445;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;
if (true) break;

case 40:
//C
this.state = 43;
;
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 450;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 451;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 //BA.debugLineNum = 452;BA.debugLine="CLEAR_PICKLIST_SKU";
_clear_picklist_sku();
 //BA.debugLineNum = 453;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 91;
return;
case 91:
//C
this.state = 43;
;
 //BA.debugLineNum = 454;BA.debugLine="PANEL_BG_LOADING.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_loading.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 43:
//C
this.state = 44;
;
 //BA.debugLineNum = 457;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 92;
return;
case 92:
//C
this.state = 44;
;
 //BA.debugLineNum = 458;BA.debugLine="If trigger = 0 Then";
if (true) break;

case 44:
//if
this.state = 80;
if (_trigger==0) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 459;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.L";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8").trim();
 //BA.debugLineNum = 460;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 93;
return;
case 93:
//C
this.state = 47;
;
 //BA.debugLineNum = 461;BA.debugLine="ProgressDialogShow2(\"Loading...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 462;BA.debugLine="CLEAR_PICKLIST_SKU";
_clear_picklist_sku();
 //BA.debugLineNum = 463;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 94;
return;
case 94:
//C
this.state = 47;
;
 //BA.debugLineNum = 464;BA.debugLine="GET_PRODUCT_DETAILS";
_get_product_details();
 //BA.debugLineNum = 465;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 95;
return;
case 95:
//C
this.state = 47;
;
 //BA.debugLineNum = 468;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_loading_ref_table WHERE product_description ='"+parent.mostCurrent._label_load_desc_loading.getText()+"' AND picklist_id = '"+parent._picklist_id+"'")));
 //BA.debugLineNum = 469;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 47:
//if
this.state = 79;
if (parent._cursor4.getRowCount()>0) { 
this.state = 49;
}else {
this.state = 78;
}if (true) break;

case 49:
//C
this.state = 50;
 //BA.debugLineNum = 470;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 50:
//for
this.state = 76;
step64 = 1;
limit64 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 96;
if (true) break;

case 96:
//C
this.state = 76;
if ((step64 > 0 && _row <= limit64) || (step64 < 0 && _row >= limit64)) this.state = 52;
if (true) break;

case 97:
//C
this.state = 96;
_row = ((int)(0 + _row + step64)) ;
if (true) break;

case 52:
//C
this.state = 53;
 //BA.debugLineNum = 471;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 472;BA.debugLine="LABEL_LOAD_VARIANT_LOADING.Text = cursor4.G";
parent.mostCurrent._label_load_variant_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("product_variant")));
 //BA.debugLineNum = 473;BA.debugLine="If cursor4.GetString(\"unit\") = \"CASE\" Then";
if (true) break;

case 53:
//if
this.state = 56;
if ((parent._cursor4.GetString("unit")).equals("CASE")) { 
this.state = 55;
}if (true) break;

case 55:
//C
this.state = 56;
 //BA.debugLineNum = 474;BA.debugLine="LABEL_CASE_QTY_LOADING.Text = cursor4.GetS";
parent.mostCurrent._label_case_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 475;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.Text = cursor4.G";
parent.mostCurrent._edittext_case_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 476;BA.debugLine="BUTTON_CASE_LOADING.Enabled = True";
parent.mostCurrent._button_case_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 477;BA.debugLine="BUTTON_CASE_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_case_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 56:
//C
this.state = 57;
;
 //BA.debugLineNum = 479;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 98;
return;
case 98:
//C
this.state = 57;
;
 //BA.debugLineNum = 480;BA.debugLine="If cursor4.GetString(\"unit\") = \"PCS\" Then";
if (true) break;

case 57:
//if
this.state = 60;
if ((parent._cursor4.GetString("unit")).equals("PCS")) { 
this.state = 59;
}if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 481;BA.debugLine="LABEL_PCS_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._label_pcs_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 482;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.Text = cursor4.Ge";
parent.mostCurrent._edittext_pcs_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 483;BA.debugLine="BUTTON_PCS_LOADING.Enabled = True";
parent.mostCurrent._button_pcs_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 484;BA.debugLine="BUTTON_PCS_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_pcs_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 60:
//C
this.state = 61;
;
 //BA.debugLineNum = 486;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 99;
return;
case 99:
//C
this.state = 61;
;
 //BA.debugLineNum = 487;BA.debugLine="If cursor4.GetString(\"unit\") = \"BOX\" Then";
if (true) break;

case 61:
//if
this.state = 64;
if ((parent._cursor4.GetString("unit")).equals("BOX")) { 
this.state = 63;
}if (true) break;

case 63:
//C
this.state = 64;
 //BA.debugLineNum = 488;BA.debugLine="LABEL_BOX_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._label_box_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 489;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.Text = cursor4.Ge";
parent.mostCurrent._edittext_box_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 490;BA.debugLine="BUTTON_BOX_LOADING.Enabled = True";
parent.mostCurrent._button_box_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 491;BA.debugLine="BUTTON_BOX_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_box_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 64:
//C
this.state = 65;
;
 //BA.debugLineNum = 493;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 100;
return;
case 100:
//C
this.state = 65;
;
 //BA.debugLineNum = 494;BA.debugLine="If cursor4.GetString(\"unit\") = \"DOZ\" Then";
if (true) break;

case 65:
//if
this.state = 68;
if ((parent._cursor4.GetString("unit")).equals("DOZ")) { 
this.state = 67;
}if (true) break;

case 67:
//C
this.state = 68;
 //BA.debugLineNum = 495;BA.debugLine="LABEL_DOZ_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._label_doz_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 496;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.Text = cursor4.Ge";
parent.mostCurrent._edittext_doz_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 497;BA.debugLine="BUTTON_DOZ_LOADING.Enabled = True";
parent.mostCurrent._button_doz_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 498;BA.debugLine="BUTTON_DOZ_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_doz_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 68:
//C
this.state = 69;
;
 //BA.debugLineNum = 500;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 101;
return;
case 101:
//C
this.state = 69;
;
 //BA.debugLineNum = 501;BA.debugLine="If cursor4.GetString(\"unit\") = \"BAG\" Then";
if (true) break;

case 69:
//if
this.state = 72;
if ((parent._cursor4.GetString("unit")).equals("BAG")) { 
this.state = 71;
}if (true) break;

case 71:
//C
this.state = 72;
 //BA.debugLineNum = 502;BA.debugLine="LABEL_BAG_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._label_bag_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 503;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.Text = cursor4.Ge";
parent.mostCurrent._edittext_bag_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 504;BA.debugLine="BUTTON_BAG_LOADING.Enabled = True";
parent.mostCurrent._button_bag_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 505;BA.debugLine="BUTTON_BAG_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_bag_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;
;
 //BA.debugLineNum = 507;BA.debugLine="If cursor4.GetString(\"unit\") = \"PACK\" Then";

case 72:
//if
this.state = 75;
if ((parent._cursor4.GetString("unit")).equals("PACK")) { 
this.state = 74;
}if (true) break;

case 74:
//C
this.state = 75;
 //BA.debugLineNum = 508;BA.debugLine="LABEL_PACK_QTY_LOADING.Text = cursor4.GetS";
parent.mostCurrent._label_pack_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 509;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.Text = cursor4.G";
parent.mostCurrent._edittext_pack_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 510;BA.debugLine="BUTTON_PACK_LOADING.Enabled = True";
parent.mostCurrent._button_pack_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 511;BA.debugLine="BUTTON_PACK_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_pack_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 75:
//C
this.state = 97;
;
 if (true) break;
if (true) break;

case 76:
//C
this.state = 79;
;
 //BA.debugLineNum = 514;BA.debugLine="NOT_UNIT_TRIGGER";
_not_unit_trigger();
 //BA.debugLineNum = 515;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 516;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 102;
return;
case 102:
//C
this.state = 79;
;
 //BA.debugLineNum = 517;BA.debugLine="PANEL_BG_LOADING.SetVisibleAnimated(300,True";
parent.mostCurrent._panel_bg_loading.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 518;BA.debugLine="PANEL_BG_LOADING.BringToFront";
parent.mostCurrent._panel_bg_loading.BringToFront();
 //BA.debugLineNum = 519;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 520;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 103;
return;
case 103:
//C
this.state = 79;
;
 //BA.debugLineNum = 521;BA.debugLine="ORDER_SPEECH";
_order_speech();
 //BA.debugLineNum = 522;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 78:
//C
this.state = 79;
 //BA.debugLineNum = 524;BA.debugLine="PANEL_BG_LOADING.SetVisibleAnimated(300, Fal";
parent.mostCurrent._panel_bg_loading.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 525;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CR";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_desc_loading.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"IS NOT THE PICKLIST."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 526;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 104;
return;
case 104:
//C
this.state = 79;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 527;BA.debugLine="PANEL_BG_TRAIL.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_trail.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 528;BA.debugLine="GET_VISOR";
_get_visor();
 //BA.debugLineNum = 529;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 105;
return;
case 105:
//C
this.state = 79;
;
 //BA.debugLineNum = 530;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 531;BA.debugLine="GET_UNIT";
_get_unit();
 //BA.debugLineNum = 532;BA.debugLine="CMB_PICKER.SelectedIndex = -1";
parent.mostCurrent._cmb_picker._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 533;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 106;
return;
case 106:
//C
this.state = 79;
;
 //BA.debugLineNum = 534;BA.debugLine="OpenSpinner(CMB_PICKER.cmbBox)";
_openspinner(parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 535;BA.debugLine="EDITTEXT_QUANTITY.Enabled = True";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 536;BA.debugLine="over_short_trigger = 0";
parent._over_short_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 537;BA.debugLine="scan_trigger = 1";
parent._scan_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 538;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 79:
//C
this.state = 80;
;
 if (true) break;

case 80:
//C
this.state = 81;
;
 //BA.debugLineNum = 541;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 107;
return;
case 107:
//C
this.state = 81;
;
 if (true) break;

case 81:
//C
this.state = 82;
;
 if (true) break;

case 82:
//C
this.state = 83;
;
 if (true) break;

case 83:
//C
this.state = -1;
;
 //BA.debugLineNum = 546;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 561;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 562;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("751707905","Connection terminated...",0);
 //BA.debugLineNum = 563;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 564;BA.debugLine="End Sub";
return "";
}
public static void  _auto_fill() throws Exception{
ResumableSub_AUTO_FILL rsub = new ResumableSub_AUTO_FILL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_AUTO_FILL extends BA.ResumableSub {
public ResumableSub_AUTO_FILL(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _ia = 0;
String _query = "";
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
 //BA.debugLineNum = 1214;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_loading_ref_table WHERE picklist_id = '"+parent._picklist_id+"'")));
 //BA.debugLineNum = 1215;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 16;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1216;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1217;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
parent.mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1218;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1219;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 17;
return;
case 17:
//C
this.state = 4;
;
 //BA.debugLineNum = 1220;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist_lo";
parent._connection.ExecNonQuery("DELETE FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id.trim()+"' and quantity = '0'");
 //BA.debugLineNum = 1221;BA.debugLine="ProgressDialogShow2(\"Auto Filling...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Auto Filling..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1222;BA.debugLine="For ia = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step9 = 1;
limit9 = (int) (parent._cursor4.getRowCount()-1);
_ia = (int) (0) ;
this.state = 18;
if (true) break;

case 18:
//C
this.state = 13;
if ((step9 > 0 && _ia <= limit9) || (step9 < 0 && _ia >= limit9)) this.state = 6;
if (true) break;

case 19:
//C
this.state = 18;
_ia = ((int)(0 + _ia + step9)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1223;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 7;
;
 //BA.debugLineNum = 1224;BA.debugLine="cursor4.Position = ia";
parent._cursor4.setPosition(_ia);
 //BA.debugLineNum = 1226;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id+"' AND product_description = '"+parent._cursor4.GetString("product_description")+"' AND unit = '"+parent._cursor4.GetString("unit")+"'")));
 //BA.debugLineNum = 1227;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor5.getRowCount()>0) { 
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
 //BA.debugLineNum = 1233;BA.debugLine="LABEL_LOAD_DESC_LOADING.text = cursor4.GetStri";
parent.mostCurrent._label_load_desc_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("product_description")));
 //BA.debugLineNum = 1234;BA.debugLine="GET_PRODUCT_DETAILS";
_get_product_details();
 //BA.debugLineNum = 1235;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 12;
;
 //BA.debugLineNum = 1237;BA.debugLine="Dim query As String = \"INSERT INTO picklist_lo";
_query = "INSERT INTO picklist_loading_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1238;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._cursor4.GetString("product_id"),parent._cursor4.GetString("product_variant"),parent._cursor4.GetString("product_description"),parent._cursor4.GetString("unit"),parent._cursor4.GetString("quantity"),parent._cursor4.GetString("quantity"),parent._cursor4.GetString("total_pieces"),"LOADED",parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"AUTO FILL "+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),"AUTO FILL "+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 if (true) break;

case 12:
//C
this.state = 19;
;
 if (true) break;
if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 1247;BA.debugLine="ToastMessageShow(\"Auto Filling Succesfull\", Fals";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Auto Filling Succesfull"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1248;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 16;
;
 //BA.debugLineNum = 1249;BA.debugLine="LOAD_LOADING_PICKLIST";
_load_loading_picklist();
 //BA.debugLineNum = 1250;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 23;
return;
case 23:
//C
this.state = 16;
;
 //BA.debugLineNum = 1251;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 15:
//C
this.state = 16;
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 1255;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 294;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 295;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 296;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 297;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 298;BA.debugLine="End Sub";
return null;
}
public static void  _button_autofill_click() throws Exception{
ResumableSub_BUTTON_AUTOFILL_Click rsub = new ResumableSub_BUTTON_AUTOFILL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_AUTOFILL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_AUTOFILL_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1203;BA.debugLine="GET_SECURITY";
_get_security();
 //BA.debugLineNum = 1204;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1205;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1206;BA.debugLine="PANEL_BG_SECURITY.BringToFront";
parent.mostCurrent._panel_bg_security.BringToFront();
 //BA.debugLineNum = 1207;BA.debugLine="EDITTEXT_PASSWORD.RequestFocus";
parent.mostCurrent._edittext_password.RequestFocus();
 //BA.debugLineNum = 1208;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1209;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PASSWORD)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_password.getObject()));
 //BA.debugLineNum = 1210;BA.debugLine="security_trigger = \"AUTO FILL\"";
parent._security_trigger = "AUTO FILL";
 //BA.debugLineNum = 1211;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_bag_loading_click() throws Exception{
ResumableSub_BUTTON_BAG_LOADING_Click rsub = new ResumableSub_BUTTON_BAG_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_BAG_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_BAG_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1796;BA.debugLine="If EDITTEXT_BAG_QTY_LOADING.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_bag_qty_loading.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1797;BA.debugLine="Msgbox2Async(\"Please input a quantity to the uni";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please input a quantity to the unit you LOADING, Empty value cannot proceed."),BA.ObjectToCharSequence("Null Quantity!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1799;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1800;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 //BA.debugLineNum = 1801;BA.debugLine="If EDITTEXT_BAG_QTY_LOADING.Text > LABEL_BAG_QTY";
if (true) break;

case 6:
//if
this.state = 23;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_bag_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))) { 
this.state = 8;
}else if((double)(Double.parseDouble(parent.mostCurrent._edittext_bag_qty_loading.getText()))<(double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 23;
 //BA.debugLineNum = 1802;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_BAG_QTY_LOADI";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._edittext_bag_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))));
 //BA.debugLineNum = 1803;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1804;BA.debugLine="OVER_PREPARE";
_over_prepare();
 if (true) break;

case 10:
//C
this.state = 23;
 //BA.debugLineNum = 1806;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_BAG_QTY_LOADING.";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._edittext_bag_qty_loading.getText()))));
 //BA.debugLineNum = 1807;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1808;BA.debugLine="SHORT_PREPARE";
_short_prepare();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1810;BA.debugLine="If unit_trigger = 1 Then";
if (true) break;

case 13:
//if
this.state = 22;
if ((parent._unit_trigger).equals(BA.NumberToString(1))) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1811;BA.debugLine="Msgbox2Async(\"The unit \" & LABEL_LOAD_SCANCODE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The unit "+parent.mostCurrent._label_load_scancode.getText()+" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?"),BA.ObjectToCharSequence("Unit not in order!"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1812;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1813;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1814;BA.debugLine="LOADING_BAG";
_loading_bag();
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1817;BA.debugLine="LOADING_BAG";
_loading_bag();
 if (true) break;

case 22:
//C
this.state = 23;
;
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
 //BA.debugLineNum = 1821;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_box_loading_click() throws Exception{
ResumableSub_BUTTON_BOX_LOADING_Click rsub = new ResumableSub_BUTTON_BOX_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_BOX_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_BOX_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1714;BA.debugLine="If EDITTEXT_BOX_QTY_LOADING.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_box_qty_loading.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1715;BA.debugLine="Msgbox2Async(\"Please input a quantity to the uni";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please input a quantity to the unit you LOADING, Empty value cannot proceed."),BA.ObjectToCharSequence("Null Quantity!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1717;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1718;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 //BA.debugLineNum = 1719;BA.debugLine="If EDITTEXT_BOX_QTY_LOADING.Text > LABEL_BOX_QTY";
if (true) break;

case 6:
//if
this.state = 23;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_box_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))) { 
this.state = 8;
}else if((double)(Double.parseDouble(parent.mostCurrent._edittext_box_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 23;
 //BA.debugLineNum = 1720;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_BOX_QTY_LOADI";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._edittext_box_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))));
 //BA.debugLineNum = 1721;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1722;BA.debugLine="OVER_PREPARE";
_over_prepare();
 if (true) break;

case 10:
//C
this.state = 23;
 //BA.debugLineNum = 1724;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_BOX_QTY_LOADING.";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._edittext_box_qty_loading.getText()))));
 //BA.debugLineNum = 1725;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1726;BA.debugLine="SHORT_PREPARE";
_short_prepare();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1729;BA.debugLine="If unit_trigger = 1 Then";
if (true) break;

case 13:
//if
this.state = 22;
if ((parent._unit_trigger).equals(BA.NumberToString(1))) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1730;BA.debugLine="Msgbox2Async(\"The unit \" & LABEL_LOAD_SCANCODE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The unit "+parent.mostCurrent._label_load_scancode.getText()+" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?"),BA.ObjectToCharSequence("Unit not in order!"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1731;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1732;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1733;BA.debugLine="LOADING_BOX";
_loading_box();
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1736;BA.debugLine="LOADING_BOX";
_loading_box();
 if (true) break;

case 22:
//C
this.state = 23;
;
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
 //BA.debugLineNum = 1740;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 849;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 850;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 851;BA.debugLine="End Sub";
return "";
}
public static void  _button_case_loading_click() throws Exception{
ResumableSub_BUTTON_CASE_LOADING_Click rsub = new ResumableSub_BUTTON_CASE_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CASE_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CASE_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1686;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 1;
;
 //BA.debugLineNum = 1687;BA.debugLine="If EDITTEXT_CASE_QTY_LOADING.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_case_qty_loading.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1688;BA.debugLine="Msgbox2Async(\"Please input a quantity to the uni";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please input a quantity to the unit you LOADING, Empty value cannot proceed."),BA.ObjectToCharSequence("Null Quantity!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1690;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1691;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 //BA.debugLineNum = 1692;BA.debugLine="If EDITTEXT_CASE_QTY_LOADING.Text > LABEL_CASE_Q";
if (true) break;

case 6:
//if
this.state = 23;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_case_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_case_qty_loading.getText()))) { 
this.state = 8;
}else if((double)(Double.parseDouble(parent.mostCurrent._edittext_case_qty_loading.getText()))<(double)(Double.parseDouble(parent.mostCurrent._label_case_qty_loading.getText()))) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 23;
 //BA.debugLineNum = 1693;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_CASE_QTY_LOAD";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._edittext_case_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._label_case_qty_loading.getText()))));
 //BA.debugLineNum = 1694;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1695;BA.debugLine="OVER_PREPARE";
_over_prepare();
 if (true) break;

case 10:
//C
this.state = 23;
 //BA.debugLineNum = 1697;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_CASE_QTY_LOADING";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._label_case_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._edittext_case_qty_loading.getText()))));
 //BA.debugLineNum = 1698;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1699;BA.debugLine="SHORT_PREPARE";
_short_prepare();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1701;BA.debugLine="If unit_trigger = 1 Then";
if (true) break;

case 13:
//if
this.state = 22;
if ((parent._unit_trigger).equals(BA.NumberToString(1))) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1702;BA.debugLine="Msgbox2Async(\"The unit \" & LABEL_LOAD_SCANCODE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The unit "+parent.mostCurrent._label_load_scancode.getText()+" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?"),BA.ObjectToCharSequence("Unit not in order!"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1703;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 26;
return;
case 26:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1704;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1705;BA.debugLine="LOADING_CASE";
_loading_case();
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1708;BA.debugLine="LOADING_CASE";
_loading_case();
 if (true) break;

case 22:
//C
this.state = 23;
;
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
 //BA.debugLineNum = 1712;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_delivery_click() throws Exception{
ResumableSub_BUTTON_DELIVERY_Click rsub = new ResumableSub_BUTTON_DELIVERY_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DELIVERY_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DELIVERY_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 2091;BA.debugLine="OPEN_DELIVERY";
_open_delivery();
 //BA.debugLineNum = 2092;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 2093;BA.debugLine="GET_DELIVERY_DETAILS";
_get_delivery_details();
 //BA.debugLineNum = 2094;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_doz_loading_click() throws Exception{
ResumableSub_BUTTON_DOZ_LOADING_Click rsub = new ResumableSub_BUTTON_DOZ_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DOZ_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DOZ_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1742;BA.debugLine="If EDITTEXT_DOZ_QTY_LOADING.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_doz_qty_loading.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1743;BA.debugLine="Msgbox2Async(\"Please input a quantity to the uni";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please input a quantity to the unit you LOADING, Empty value cannot proceed."),BA.ObjectToCharSequence("Null Quantity!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1745;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1746;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 //BA.debugLineNum = 1747;BA.debugLine="If EDITTEXT_DOZ_QTY_LOADING.Text > LABEL_DOZ_QTY";
if (true) break;

case 6:
//if
this.state = 23;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_doz_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))) { 
this.state = 8;
}else if((double)(Double.parseDouble(parent.mostCurrent._edittext_doz_qty_loading.getText()))<(double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 23;
 //BA.debugLineNum = 1748;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_DOZ_QTY_LOADI";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._edittext_doz_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))));
 //BA.debugLineNum = 1749;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1750;BA.debugLine="OVER_PREPARE";
_over_prepare();
 if (true) break;

case 10:
//C
this.state = 23;
 //BA.debugLineNum = 1752;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_DOZ_QTY_LOADING.";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._edittext_doz_qty_loading.getText()))));
 //BA.debugLineNum = 1753;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1754;BA.debugLine="SHORT_PREPARE";
_short_prepare();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1756;BA.debugLine="If unit_trigger = 1 Then";
if (true) break;

case 13:
//if
this.state = 22;
if ((parent._unit_trigger).equals(BA.NumberToString(1))) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1757;BA.debugLine="Msgbox2Async(\"The unit \" & LABEL_LOAD_SCANCODE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The unit "+parent.mostCurrent._label_load_scancode.getText()+" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?"),BA.ObjectToCharSequence("Unit not in order!"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1758;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1759;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1760;BA.debugLine="LOADING_DOZ";
_loading_doz();
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1763;BA.debugLine="LOADING_DOZ";
_loading_doz();
 if (true) break;

case 22:
//C
this.state = 23;
;
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
 //BA.debugLineNum = 1767;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_exit_loading_click() throws Exception{
ResumableSub_BUTTON_EXIT_LOADING_Click rsub = new ResumableSub_BUTTON_EXIT_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_EXIT_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_EXIT_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1865;BA.debugLine="CLEAR_PICKLIST_SKU";
_clear_picklist_sku();
 //BA.debugLineNum = 1866;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1867;BA.debugLine="PANEL_BG_LOADING.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_loading.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1868;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1869;BA.debugLine="XSelections.Clear";
parent.mostCurrent._xselections._clear /*String*/ ();
 //BA.debugLineNum = 1870;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1871;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1872;BA.debugLine="LOAD_LOADING_PICKLIST";
_load_loading_picklist();
 //BA.debugLineNum = 1873;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_load_click() throws Exception{
ResumableSub_BUTTON_LOAD_Click rsub = new ResumableSub_BUTTON_LOAD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_LOAD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_LOAD_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 853;BA.debugLine="picklist_id = EDITTEXT_TYPE.Text.ToUpperCase";
parent._picklist_id = parent.mostCurrent._edittext_type.getText().toUpperCase();
 //BA.debugLineNum = 854;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 855;BA.debugLine="VALIDATE_PICKLIST_STATUS";
_validate_picklist_status();
 //BA.debugLineNum = 856;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 857;BA.debugLine="ENABLE_PICKLIST";
_enable_picklist();
 //BA.debugLineNum = 858;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_okay_click() throws Exception{
ResumableSub_BUTTON_OKAY_Click rsub = new ResumableSub_BUTTON_OKAY_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_OKAY_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_OKAY_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _query = "";
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
 //BA.debugLineNum = 1986;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"0\" Or  EDITTEXT_QUAN";
if (true) break;

case 1:
//if
this.state = 64;
if ((parent.mostCurrent._edittext_quantity.getText()).equals("0") || (parent.mostCurrent._edittext_quantity.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 64;
 //BA.debugLineNum = 1987;BA.debugLine="ToastMessageShow(\"Quantity is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Quantity is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1989;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM us";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM users_table WHERE user = '"+parent.mostCurrent._cmb_supervisor._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and pass = '"+parent.mostCurrent._edittext_trail_pass.getText()+"'")));
 //BA.debugLineNum = 1990;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 6:
//if
this.state = 63;
if (parent._cursor2.getRowCount()>0) { 
this.state = 8;
}else {
this.state = 62;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1991;BA.debugLine="ProgressDialogShow2(\"Saving...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Saving..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1992;BA.debugLine="If CMB_PICKER.cmbBox.SelectedIndex = -1 Then CM";
if (true) break;

case 9:
//if
this.state = 14;
if (parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 11;
;}if (true) break;

case 11:
//C
this.state = 14;
parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 1993;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = -1 Then CM";
if (true) break;

case 15:
//if
this.state = 20;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 17;
;}if (true) break;

case 17:
//C
this.state = 20;
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
if (true) break;

case 20:
//C
this.state = 21;
;
 //BA.debugLineNum = 1994;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB_";
if (true) break;

case 21:
//if
this.state = 26;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 23;
;}if (true) break;

case 23:
//C
this.state = 26;
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 1996;BA.debugLine="Dim query As String = \"INSERT INTO picklist_tab";
_query = "INSERT INTO picklist_table_trail VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1997;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+" "+parent.mostCurrent._cmb_supervisor._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._label_load_checker.getText(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"LOADING",parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 2000;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 65;
return;
case 65:
//C
this.state = 27;
;
 //BA.debugLineNum = 2002;BA.debugLine="PANEL_BG_TRAIL.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_trail.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2004;BA.debugLine="If over_short_trigger = 1 Then";
if (true) break;

case 27:
//if
this.state = 60;
if ((parent._over_short_trigger).equals(BA.NumberToString(1))) { 
this.state = 29;
}else if((parent._over_short_trigger).equals(BA.NumberToString(2))) { 
this.state = 45;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 2005;BA.debugLine="Msgbox2Async(\"Over prepared reported! Please b";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Over prepared reported! Please bring back the over items to the warehouse and load only the corresponding order quantity."),BA.ObjectToCharSequence("Notice"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2006;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 66;
return;
case 66:
//C
this.state = 30;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2008;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cm";
if (true) break;

case 30:
//if
this.state = 43;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 32;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 34;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 36;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 38;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 40;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 42;
}if (true) break;

case 32:
//C
this.state = 43;
 //BA.debugLineNum = 2009;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.Text = LABEL_PCS_QTY";
parent.mostCurrent._edittext_pcs_qty_loading.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_pcs_qty_loading.getText()));
 //BA.debugLineNum = 2010;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 67;
return;
case 67:
//C
this.state = 43;
;
 //BA.debugLineNum = 2011;BA.debugLine="LOADING_PCS";
_loading_pcs();
 if (true) break;

case 34:
//C
this.state = 43;
 //BA.debugLineNum = 2013;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.Text = LABEL_CASE_Q";
parent.mostCurrent._edittext_case_qty_loading.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_case_qty_loading.getText()));
 //BA.debugLineNum = 2014;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 43;
;
 //BA.debugLineNum = 2015;BA.debugLine="LOADING_CASE";
_loading_case();
 if (true) break;

case 36:
//C
this.state = 43;
 //BA.debugLineNum = 2017;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.Text = LABEL_BOX_QTY";
parent.mostCurrent._edittext_box_qty_loading.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_box_qty_loading.getText()));
 //BA.debugLineNum = 2018;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 43;
;
 //BA.debugLineNum = 2019;BA.debugLine="LOADING_BOX";
_loading_box();
 if (true) break;

case 38:
//C
this.state = 43;
 //BA.debugLineNum = 2021;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.Text = LABEL_DOZ_QTY";
parent.mostCurrent._edittext_doz_qty_loading.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_doz_qty_loading.getText()));
 //BA.debugLineNum = 2022;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 70;
return;
case 70:
//C
this.state = 43;
;
 //BA.debugLineNum = 2023;BA.debugLine="LOADING_DOZ";
_loading_doz();
 if (true) break;

case 40:
//C
this.state = 43;
 //BA.debugLineNum = 2025;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.Text = LABEL_PACK_Q";
parent.mostCurrent._edittext_pack_qty_loading.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_pack_qty_loading.getText()));
 //BA.debugLineNum = 2026;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 71;
return;
case 71:
//C
this.state = 43;
;
 //BA.debugLineNum = 2027;BA.debugLine="LOADING_PACK";
_loading_pack();
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 2029;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.Text = LABEL_BAG_QTY";
parent.mostCurrent._edittext_bag_qty_loading.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_bag_qty_loading.getText()));
 //BA.debugLineNum = 2030;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 72;
return;
case 72:
//C
this.state = 43;
;
 //BA.debugLineNum = 2031;BA.debugLine="LOADING_BAG";
_loading_bag();
 if (true) break;

case 43:
//C
this.state = 60;
;
 if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 2034;BA.debugLine="Msgbox2Async(\"Short prepared reported! Please";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Short prepared reported! Please call the picker of this pickist and check strictly if the item quantity really short before getting the shorted item."),BA.ObjectToCharSequence("Notice"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2035;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 73;
return;
case 73:
//C
this.state = 46;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2037;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cm";
if (true) break;

case 46:
//if
this.state = 59;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 48;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 50;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 52;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 54;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 56;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 58;
}if (true) break;

case 48:
//C
this.state = 59;
 //BA.debugLineNum = 2038;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 74;
return;
case 74:
//C
this.state = 59;
;
 //BA.debugLineNum = 2039;BA.debugLine="LOADING_PCS";
_loading_pcs();
 if (true) break;

case 50:
//C
this.state = 59;
 //BA.debugLineNum = 2041;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 75;
return;
case 75:
//C
this.state = 59;
;
 //BA.debugLineNum = 2042;BA.debugLine="LOADING_CASE";
_loading_case();
 if (true) break;

case 52:
//C
this.state = 59;
 //BA.debugLineNum = 2044;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 76;
return;
case 76:
//C
this.state = 59;
;
 //BA.debugLineNum = 2045;BA.debugLine="LOADING_BOX";
_loading_box();
 if (true) break;

case 54:
//C
this.state = 59;
 //BA.debugLineNum = 2047;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 77;
return;
case 77:
//C
this.state = 59;
;
 //BA.debugLineNum = 2048;BA.debugLine="LOADING_DOZ";
_loading_doz();
 if (true) break;

case 56:
//C
this.state = 59;
 //BA.debugLineNum = 2050;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 78;
return;
case 78:
//C
this.state = 59;
;
 //BA.debugLineNum = 2051;BA.debugLine="LOADING_PACK";
_loading_pack();
 if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 2053;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 79;
return;
case 79:
//C
this.state = 59;
;
 //BA.debugLineNum = 2054;BA.debugLine="LOADING_BAG";
_loading_bag();
 if (true) break;

case 59:
//C
this.state = 60;
;
 if (true) break;

case 60:
//C
this.state = 63;
;
 //BA.debugLineNum = 2057;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 2058;BA.debugLine="scan_trigger = 0";
parent._scan_trigger = BA.NumberToString(0);
 if (true) break;

case 62:
//C
this.state = 63;
 //BA.debugLineNum = 2060;BA.debugLine="ToastMessageShow(\"Wrong Password\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Wrong Password"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 63:
//C
this.state = 64;
;
 if (true) break;

case 64:
//C
this.state = -1;
;
 //BA.debugLineNum = 2063;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_pack_loading_click() throws Exception{
ResumableSub_BUTTON_PACK_LOADING_Click rsub = new ResumableSub_BUTTON_PACK_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_PACK_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_PACK_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1769;BA.debugLine="If EDITTEXT_PACK_QTY_LOADING.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_pack_qty_loading.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1770;BA.debugLine="Msgbox2Async(\"Please input a quantity to the uni";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please input a quantity to the unit you LOADING, Empty value cannot proceed."),BA.ObjectToCharSequence("Null Quantity!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1772;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1773;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 //BA.debugLineNum = 1774;BA.debugLine="If EDITTEXT_PACK_QTY_LOADING.Text > LABEL_PACK_Q";
if (true) break;

case 6:
//if
this.state = 23;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_pack_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))) { 
this.state = 8;
}else if((double)(Double.parseDouble(parent.mostCurrent._edittext_pack_qty_loading.getText()))<(double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 23;
 //BA.debugLineNum = 1775;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_PACK_QTY_LOAD";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._edittext_pack_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))));
 //BA.debugLineNum = 1776;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1777;BA.debugLine="OVER_PREPARE";
_over_prepare();
 if (true) break;

case 10:
//C
this.state = 23;
 //BA.debugLineNum = 1779;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_PACK_QTY_LOADING";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._edittext_pack_qty_loading.getText()))));
 //BA.debugLineNum = 1780;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1781;BA.debugLine="SHORT_PREPARE";
_short_prepare();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1783;BA.debugLine="If unit_trigger = 1 Then";
if (true) break;

case 13:
//if
this.state = 22;
if ((parent._unit_trigger).equals(BA.NumberToString(1))) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1784;BA.debugLine="Msgbox2Async(\"The unit \" & LABEL_LOAD_SCANCODE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The unit "+parent.mostCurrent._label_load_scancode.getText()+" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?"),BA.ObjectToCharSequence("Unit not in order!"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1785;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1786;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1787;BA.debugLine="LOADING_PACK";
_loading_pack();
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1790;BA.debugLine="LOADING_PACK";
_loading_pack();
 if (true) break;

case 22:
//C
this.state = 23;
;
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
 //BA.debugLineNum = 1794;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_pcs_loading_click() throws Exception{
ResumableSub_BUTTON_PCS_LOADING_Click rsub = new ResumableSub_BUTTON_PCS_LOADING_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_PCS_LOADING_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_PCS_LOADING_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1659;BA.debugLine="If EDITTEXT_PCS_QTY_LOADING.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_pcs_qty_loading.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1660;BA.debugLine="Msgbox2Async(\"Please input a quantity to the uni";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please input a quantity to the unit you LOADING, Empty value cannot proceed."),BA.ObjectToCharSequence("Null Quantity!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1662;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1663;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 //BA.debugLineNum = 1664;BA.debugLine="If EDITTEXT_PCS_QTY_LOADING.Text > LABEL_PCS_QTY";
if (true) break;

case 6:
//if
this.state = 23;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_pcs_qty_loading.getText()))>(double)(Double.parseDouble(parent.mostCurrent._label_pcs_qty_loading.getText()))) { 
this.state = 8;
}else if((double)(Double.parseDouble(parent.mostCurrent._edittext_pcs_qty_loading.getText()))<(double)(Double.parseDouble(parent.mostCurrent._label_pcs_qty_loading.getText()))) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 23;
 //BA.debugLineNum = 1665;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_PCS_QTY_LOADI";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._edittext_pcs_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._label_pcs_qty_loading.getText()))));
 //BA.debugLineNum = 1666;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1667;BA.debugLine="OVER_PREPARE";
_over_prepare();
 if (true) break;

case 10:
//C
this.state = 23;
 //BA.debugLineNum = 1669;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_PCS_QTY_LOADING.";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence((double)(Double.parseDouble(parent.mostCurrent._label_pcs_qty_loading.getText()))-(double)(Double.parseDouble(parent.mostCurrent._edittext_pcs_qty_loading.getText()))));
 //BA.debugLineNum = 1670;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1671;BA.debugLine="SHORT_PREPARE";
_short_prepare();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1673;BA.debugLine="If unit_trigger = 1 Then";
if (true) break;

case 13:
//if
this.state = 22;
if ((parent._unit_trigger).equals(BA.NumberToString(1))) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1674;BA.debugLine="Msgbox2Async(\"The unit \" & LABEL_LOAD_SCANCODE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The unit "+parent.mostCurrent._label_load_scancode.getText()+" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?"),BA.ObjectToCharSequence("Unit not in order!"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1675;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1676;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1677;BA.debugLine="LOADING_PCS";
_loading_pcs();
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1680;BA.debugLine="LOADING_PCS";
_loading_pcs();
 if (true) break;

case 22:
//C
this.state = 23;
;
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
 //BA.debugLineNum = 1684;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_save_click() throws Exception{
ResumableSub_BUTTON_SAVE_Click rsub = new ResumableSub_BUTTON_SAVE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SAVE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SAVE_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 2117;BA.debugLine="If EDITTEXT_PLATE.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 14;
if ((parent.mostCurrent._edittext_plate.getText()).equals("")) { 
this.state = 3;
}else if(parent.mostCurrent._edittext_plate.getText().length()<6) { 
this.state = 5;
}else if((parent.mostCurrent._edittext_driver.getText()).equals("")) { 
this.state = 7;
}else if((parent.mostCurrent._edittext_helper1.getText()).equals("")) { 
this.state = 9;
}else if((parent.mostCurrent._edittext_pushcart.getText()).equals("")) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 14;
 //BA.debugLineNum = 2118;BA.debugLine="ToastMessageShow(\"Please input a plate no.\", Fal";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a plate no."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2119;BA.debugLine="EDITTEXT_PLATE.RequestFocus";
parent.mostCurrent._edittext_plate.RequestFocus();
 //BA.debugLineNum = 2120;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 14;
;
 //BA.debugLineNum = 2121;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PLATE)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_plate.getObject()));
 if (true) break;

case 5:
//C
this.state = 14;
 //BA.debugLineNum = 2123;BA.debugLine="ToastMessageShow(\"Plate no. must be 6 or higher";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Plate no. must be 6 or higher letter/numbers."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2124;BA.debugLine="EDITTEXT_PLATE.RequestFocus";
parent.mostCurrent._edittext_plate.RequestFocus();
 //BA.debugLineNum = 2125;BA.debugLine="EDITTEXT_PLATE.SelectAll";
parent.mostCurrent._edittext_plate.SelectAll();
 //BA.debugLineNum = 2126;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 14;
;
 //BA.debugLineNum = 2127;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PLATE)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_plate.getObject()));
 if (true) break;

case 7:
//C
this.state = 14;
 //BA.debugLineNum = 2129;BA.debugLine="ToastMessageShow(\"Please input a driver.\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a driver."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2130;BA.debugLine="EDITTEXT_DRIVER.RequestFocus";
parent.mostCurrent._edittext_driver.RequestFocus();
 //BA.debugLineNum = 2131;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 17;
return;
case 17:
//C
this.state = 14;
;
 //BA.debugLineNum = 2132;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_DRIVER)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_driver.getObject()));
 if (true) break;

case 9:
//C
this.state = 14;
 //BA.debugLineNum = 2134;BA.debugLine="ToastMessageShow(\"Please input atleast helper #1";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input atleast helper #1."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2135;BA.debugLine="EDITTEXT_HELPER1.RequestFocus";
parent.mostCurrent._edittext_helper1.RequestFocus();
 //BA.debugLineNum = 2136;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 14;
;
 //BA.debugLineNum = 2137;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_HELPER1)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_helper1.getObject()));
 if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 2139;BA.debugLine="ToastMessageShow(\"Please input a pushcart count.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a pushcart count."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2140;BA.debugLine="EDITTEXT_PUSHCART.RequestFocus";
parent.mostCurrent._edittext_pushcart.RequestFocus();
 //BA.debugLineNum = 2141;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 14;
;
 //BA.debugLineNum = 2142;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PUSHCART)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_pushcart.getObject()));
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 2144;BA.debugLine="ProgressDialogShow2(\"Saving...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Saving..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2145;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist_de";
parent._connection.ExecNonQuery("DELETE FROM picklist_delivery_report_table WHERE picklist_id = '"+parent._picklist_id+"'");
 //BA.debugLineNum = 2146;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 14;
;
 //BA.debugLineNum = 2147;BA.debugLine="Dim query As String = \"INSERT INTO picklist_deli";
_query = "INSERT INTO picklist_delivery_report_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 2148;BA.debugLine="connection.ExecNonQuery2(query,Array As String(p";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._edittext_plate.getText(),parent.mostCurrent._edittext_driver.getText(),parent.mostCurrent._edittext_helper1.getText(),parent.mostCurrent._edittext_helper2.getText(),parent.mostCurrent._edittext_helper3.getText(),parent.mostCurrent._edittext_pushcart.getText(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 2154;BA.debugLine="PANEL_BG_DELIVERY.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_delivery.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2155;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 2156;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 2158;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_search_click() throws Exception{
ResumableSub_BUTTON_SEARCH_Click rsub = new ResumableSub_BUTTON_SEARCH_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SEARCH_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SEARCH_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 2097;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_delivery_report_table WHERE plate_no = '"+parent.mostCurrent._edittext_plate.getText()+"' ORDER BY date_time_registered DESC LIMIT 1")));
 //BA.debugLineNum = 2098;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 2099;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
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
 //BA.debugLineNum = 2100;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 2101;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 2102;BA.debugLine="EDITTEXT_PLATE.Text = cursor2.GetString(\"plate_";
parent.mostCurrent._edittext_plate.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("plate_no")));
 //BA.debugLineNum = 2103;BA.debugLine="EDITTEXT_PUSHCART.Text = cursor2.GetString(\"pus";
parent.mostCurrent._edittext_pushcart.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("pushcart")));
 //BA.debugLineNum = 2104;BA.debugLine="EDITTEXT_DRIVER.Text = cursor2.GetString(\"drive";
parent.mostCurrent._edittext_driver.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("driver")));
 //BA.debugLineNum = 2105;BA.debugLine="EDITTEXT_HELPER1.Text = cursor2.GetString(\"help";
parent.mostCurrent._edittext_helper1.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("helper1")));
 //BA.debugLineNum = 2106;BA.debugLine="EDITTEXT_HELPER2.Text = cursor2.GetString(\"help";
parent.mostCurrent._edittext_helper2.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("helper2")));
 //BA.debugLineNum = 2107;BA.debugLine="EDITTEXT_HELPER3.Text = cursor2.GetString(\"help";
parent.mostCurrent._edittext_helper3.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("helper3")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 2109;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2113;BA.debugLine="ToastMessageShow(\"No delivery history for this p";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No delivery history for this plate no."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 2115;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_security_cancel_click() throws Exception{
 //BA.debugLineNum = 1193;BA.debugLine="Sub BUTTON_SECURITY_CANCEL_Click";
 //BA.debugLineNum = 1194;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1195;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1196;BA.debugLine="CTRL.HideKeyboard";
mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1197;BA.debugLine="End Sub";
return "";
}
public static void  _button_security_confirm_click() throws Exception{
ResumableSub_BUTTON_SECURITY_CONFIRM_Click rsub = new ResumableSub_BUTTON_SECURITY_CONFIRM_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SECURITY_CONFIRM_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SECURITY_CONFIRM_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1168;BA.debugLine="If EDITTEXT_PASSWORD.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 24;
if ((parent.mostCurrent._edittext_password.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 24;
 //BA.debugLineNum = 1169;BA.debugLine="ToastMessageShow(\"Empty Password\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Empty Password"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1171;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM us";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM users_table WHERE user = '"+parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and pass = '"+parent.mostCurrent._edittext_password.getText()+"'")));
 //BA.debugLineNum = 1172;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 6:
//if
this.state = 23;
if (parent._cursor2.getRowCount()>0) { 
this.state = 8;
}else {
this.state = 22;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1173;BA.debugLine="If security_trigger = \"AUTO FILL\" Then";
if (true) break;

case 9:
//if
this.state = 20;
if ((parent._security_trigger).equals("AUTO FILL")) { 
this.state = 11;
}else if((parent._security_trigger).equals("OVERWRITE")) { 
this.state = 19;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1174;BA.debugLine="Msgbox2Async(\"Are you sure you want to AUTO FI";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to AUTO FILL this picklist?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1175;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 25;
return;
case 25:
//C
this.state = 12;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1176;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 12:
//if
this.state = 17;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 1177;BA.debugLine="AUTO_FILL";
_auto_fill();
 if (true) break;

case 16:
//C
this.state = 17;
 if (true) break;

case 17:
//C
this.state = 20;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1182;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1183;BA.debugLine="EDITTEXT_PASSWORD.Text = \"\"";
parent.mostCurrent._edittext_password.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1184;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1185;BA.debugLine="UPDATE_LOADING";
_update_loading();
 if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 1188;BA.debugLine="ToastMessageShow(\"Wrong Password\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Wrong Password"),anywheresoftware.b4a.keywords.Common.False);
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
 //BA.debugLineNum = 1192;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_upload_click() throws Exception{
ResumableSub_BUTTON_UPLOAD_Click rsub = new ResumableSub_BUTTON_UPLOAD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPLOAD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPLOAD_Click(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 2386;BA.debugLine="Msgbox2Async(\"Are you sure you want to UPLOAD thi";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to UPLOAD this picklist?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2387;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2388;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2389;BA.debugLine="DELETE_PICKLIST_LOADING";
_delete_picklist_loading();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2393;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _check_picklist_loaded() throws Exception{
ResumableSub_CHECK_PICKLIST_LOADED rsub = new ResumableSub_CHECK_PICKLIST_LOADED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CHECK_PICKLIST_LOADED extends BA.ResumableSub {
public ResumableSub_CHECK_PICKLIST_LOADED(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
int _ise = 0;
anywheresoftware.b4a.BA.IterableList group8;
int index8;
int groupLen8;
int step24;
int limit24;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2243;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 2244;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_coun";
_cmd = _createcommand("select_count_picklist_loaded",new Object[]{(Object)(parent._picklist_id)});
 //BA.debugLineNum = 2245;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 33;
return;
case 33:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2246;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 16;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2247;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 2248;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 34;
return;
case 34:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 2249;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2250;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group8 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index8 = 0;
groupLen8 = group8.getSize();
this.state = 35;
if (true) break;

case 35:
//C
this.state = 10;
if (index8 < groupLen8) {
this.state = 9;
_row = (Object[])(group8.Get(index8));}
if (true) break;

case 36:
//C
this.state = 35;
index8++;
if (true) break;

case 9:
//C
this.state = 36;
 //BA.debugLineNum = 2251;BA.debugLine="loaded_count = row(res.Columns.Get(\"loaded_cou";
parent._loaded_count = (int)(BA.ObjectToNumber(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("loaded_count"))))]));
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
 //BA.debugLineNum = 2254;BA.debugLine="loaded_count = 0";
parent._loaded_count = (int) (0);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 2257;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2258;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("756098832","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2259;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2260;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = 17;
;
 //BA.debugLineNum = 2262;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 2263;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 37;
return;
case 37:
//C
this.state = 17;
;
 //BA.debugLineNum = 2264;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT count(prod";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT count(product_id) as 'uploaded_count' FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id.trim()+"'")));
 //BA.debugLineNum = 2265;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 17:
//if
this.state = 26;
if (parent._cursor8.getRowCount()>0) { 
this.state = 19;
}else {
this.state = 25;
}if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 2266;BA.debugLine="For ise = 0 To cursor8.RowCount - 1";
if (true) break;

case 20:
//for
this.state = 23;
step24 = 1;
limit24 = (int) (parent._cursor8.getRowCount()-1);
_ise = (int) (0) ;
this.state = 38;
if (true) break;

case 38:
//C
this.state = 23;
if ((step24 > 0 && _ise <= limit24) || (step24 < 0 && _ise >= limit24)) this.state = 22;
if (true) break;

case 39:
//C
this.state = 38;
_ise = ((int)(0 + _ise + step24)) ;
if (true) break;

case 22:
//C
this.state = 39;
 //BA.debugLineNum = 2267;BA.debugLine="cursor8.Position = ise";
parent._cursor8.setPosition(_ise);
 //BA.debugLineNum = 2268;BA.debugLine="uploaded_count = cursor8.GetString(\"uploaded_co";
parent._uploaded_count = (int)(Double.parseDouble(parent._cursor8.GetString("uploaded_count")));
 if (true) break;
if (true) break;

case 23:
//C
this.state = 26;
;
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 2272;BA.debugLine="uploaded_count = 0";
parent._uploaded_count = (int) (0);
 if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 2274;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 40;
return;
case 40:
//C
this.state = 27;
;
 //BA.debugLineNum = 2275;BA.debugLine="LABEL_MSGBOX2.Text = \"Total Served : \" & loaded_c";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Total Served : "+BA.NumberToString(parent._loaded_count)+" "+"Total Uploaded : "+BA.NumberToString(parent._uploaded_count)));
 //BA.debugLineNum = 2276;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 41;
return;
case 41:
//C
this.state = 27;
;
 //BA.debugLineNum = 2277;BA.debugLine="If uploaded_count <> loaded_count Then";
if (true) break;

case 27:
//if
this.state = 32;
if (parent._uploaded_count!=parent._loaded_count) { 
this.state = 29;
}else {
this.state = 31;
}if (true) break;

case 29:
//C
this.state = 32;
 //BA.debugLineNum = 2278;BA.debugLine="ToastMessageShow(\"Uploading Order Not Complete.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Order Not Complete. Reuploading"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2279;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 32;
;
 //BA.debugLineNum = 2280;BA.debugLine="DELETE_PICKLIST_LOADING";
_delete_picklist_loading();
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 2282;BA.debugLine="DELETE_PICKLIST_LOADED_TRAIL";
_delete_picklist_loaded_trail();
 if (true) break;

case 32:
//C
this.state = -1;
;
 //BA.debugLineNum = 2284;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _jr) throws Exception{
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _check_picklist_loading() throws Exception{
ResumableSub_CHECK_PICKLIST_LOADING rsub = new ResumableSub_CHECK_PICKLIST_LOADING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CHECK_PICKLIST_LOADING extends BA.ResumableSub {
public ResumableSub_CHECK_PICKLIST_LOADING(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
int _ise = 0;
String _msg = "";
anywheresoftware.b4a.BA.IterableList group8;
int index8;
int groupLen8;
int step24;
int limit24;
int step42;
int limit42;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 628;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 629;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_coun";
_cmd = _createcommand("select_count_picklist_confirmed",new Object[]{(Object)(parent._picklist_id)});
 //BA.debugLineNum = 630;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 41;
return;
case 41:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 631;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 16;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 632;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 633;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 42;
return;
case 42:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 634;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 635;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group8 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index8 = 0;
groupLen8 = group8.getSize();
this.state = 43;
if (true) break;

case 43:
//C
this.state = 10;
if (index8 < groupLen8) {
this.state = 9;
_row = (Object[])(group8.Get(index8));}
if (true) break;

case 44:
//C
this.state = 43;
index8++;
if (true) break;

case 9:
//C
this.state = 44;
 //BA.debugLineNum = 636;BA.debugLine="loading_count = row(res.Columns.Get(\"loading_c";
parent._loading_count = BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("loading_count"))))]);
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
 //BA.debugLineNum = 639;BA.debugLine="loading_count = 0";
parent._loading_count = BA.NumberToString(0);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 642;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 643;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("752035600","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 644;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 645;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = 17;
;
 //BA.debugLineNum = 647;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 648;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 45;
return;
case 45:
//C
this.state = 17;
;
 //BA.debugLineNum = 649;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT count(prod";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT count(product_id) as 'downloaded_count' FROM picklist_loading_ref_table WHERE picklist_id = '"+parent._picklist_id.trim()+"'")));
 //BA.debugLineNum = 650;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 17:
//if
this.state = 26;
if (parent._cursor8.getRowCount()>0) { 
this.state = 19;
}else {
this.state = 25;
}if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 651;BA.debugLine="For ise = 0 To cursor8.RowCount - 1";
if (true) break;

case 20:
//for
this.state = 23;
step24 = 1;
limit24 = (int) (parent._cursor8.getRowCount()-1);
_ise = (int) (0) ;
this.state = 46;
if (true) break;

case 46:
//C
this.state = 23;
if ((step24 > 0 && _ise <= limit24) || (step24 < 0 && _ise >= limit24)) this.state = 22;
if (true) break;

case 47:
//C
this.state = 46;
_ise = ((int)(0 + _ise + step24)) ;
if (true) break;

case 22:
//C
this.state = 47;
 //BA.debugLineNum = 652;BA.debugLine="cursor8.Position = ise";
parent._cursor8.setPosition(_ise);
 //BA.debugLineNum = 653;BA.debugLine="downloaded_count = cursor8.GetString(\"downloade";
parent._downloaded_count = parent._cursor8.GetString("downloaded_count");
 if (true) break;
if (true) break;

case 23:
//C
this.state = 26;
;
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 657;BA.debugLine="downloaded_count = 0";
parent._downloaded_count = BA.NumberToString(0);
 if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 659;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 48;
return;
case 48:
//C
this.state = 27;
;
 //BA.debugLineNum = 660;BA.debugLine="LABEL_MSGBOX2.Text = \"Total Order : \" & loading_c";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Total Order : "+parent._loading_count+" "+"Total Downloaded : "+parent._downloaded_count));
 //BA.debugLineNum = 661;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 49;
return;
case 49:
//C
this.state = 27;
;
 //BA.debugLineNum = 662;BA.debugLine="If downloaded_count <> loading_count Then";
if (true) break;

case 27:
//if
this.state = 40;
if ((parent._downloaded_count).equals(parent._loading_count) == false) { 
this.state = 29;
}else {
this.state = 31;
}if (true) break;

case 29:
//C
this.state = 40;
 //BA.debugLineNum = 663;BA.debugLine="ToastMessageShow(\"Downloading Order Not Complete";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Downloading Order Not Complete. Redownloading"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 664;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 50;
return;
case 50:
//C
this.state = 40;
;
 //BA.debugLineNum = 665;BA.debugLine="DOWNLOAD_PICKLIST";
_download_picklist();
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 667;BA.debugLine="Dim msg As String = \"This are the sku that will";
_msg = "This are the sku that will be deleted or change quantity:"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 670;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM pi";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id+"' and confirm_status = '1'")));
 //BA.debugLineNum = 671;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 32:
//if
this.state = 39;
if (parent._cursor5.getRowCount()>0) { 
this.state = 34;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 672;BA.debugLine="For ise = 0 To cursor5.RowCount - 1";
if (true) break;

case 35:
//for
this.state = 38;
step42 = 1;
limit42 = (int) (parent._cursor5.getRowCount()-1);
_ise = (int) (0) ;
this.state = 51;
if (true) break;

case 51:
//C
this.state = 38;
if ((step42 > 0 && _ise <= limit42) || (step42 < 0 && _ise >= limit42)) this.state = 37;
if (true) break;

case 52:
//C
this.state = 51;
_ise = ((int)(0 + _ise + step42)) ;
if (true) break;

case 37:
//C
this.state = 52;
 //BA.debugLineNum = 673;BA.debugLine="cursor5.Position = ise";
parent._cursor5.setPosition(_ise);
 //BA.debugLineNum = 674;BA.debugLine="msg = msg & \" \" & cursor5.GetString(\"product_d";
_msg = _msg+" "+parent._cursor5.GetString("product_description")+" / "+parent._cursor5.GetString("quantity")+"-"+parent._cursor5.GetString("unit")+anywheresoftware.b4a.keywords.Common.CRLF;
 if (true) break;
if (true) break;

case 38:
//C
this.state = 39;
;
 //BA.debugLineNum = 678;BA.debugLine="Msgbox2Async(msg, \"PICKLIST ADJUSTMENT\", \"\", \"\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence(_msg),BA.ObjectToCharSequence("PICKLIST ADJUSTMENT"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 39:
//C
this.state = 40;
;
 //BA.debugLineNum = 680;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist_lo";
parent._connection.ExecNonQuery("DELETE FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id+"' and confirm_status = '1'");
 //BA.debugLineNum = 682;BA.debugLine="LABEL_MSGBOX2.Text = \"Picklist Downloaded Succes";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Picklist Downloaded Successfully.."));
 //BA.debugLineNum = 683;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 53;
return;
case 53:
//C
this.state = 40;
;
 //BA.debugLineNum = 684;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 685;BA.debugLine="LOAD_LOADING_HEADER";
_load_loading_header();
 //BA.debugLineNum = 686;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 54;
return;
case 54:
//C
this.state = 40;
;
 //BA.debugLineNum = 687;BA.debugLine="LOAD_LOADING_PICKLIST";
_load_loading_picklist();
 //BA.debugLineNum = 688;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 689;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 55;
return;
case 55:
//C
this.state = 40;
;
 //BA.debugLineNum = 690;BA.debugLine="GET_PREPARED_DETAILS";
_get_prepared_details();
 //BA.debugLineNum = 691;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 56;
return;
case 56:
//C
this.state = 40;
;
 //BA.debugLineNum = 692;BA.debugLine="GET_DELIVERY_DETAILS";
_get_delivery_details();
 //BA.debugLineNum = 693;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 57;
return;
case 57:
//C
this.state = 40;
;
 //BA.debugLineNum = 694;BA.debugLine="OPEN_DELIVERY";
_open_delivery();
 //BA.debugLineNum = 695;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 696;BA.debugLine="scan_trigger = 0";
parent._scan_trigger = BA.NumberToString(0);
 if (true) break;

case 40:
//C
this.state = -1;
;
 //BA.debugLineNum = 698;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _clear_picklist() throws Exception{
ResumableSub_CLEAR_PICKLIST rsub = new ResumableSub_CLEAR_PICKLIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CLEAR_PICKLIST extends BA.ResumableSub {
public ResumableSub_CLEAR_PICKLIST(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 822;BA.debugLine="ProgressDialogShow2(\"Clearing Picklist\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Clearing Picklist"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 823;BA.debugLine="ACToolBarLight1.Menu.RemoveItem(1)";
parent.mostCurrent._actoolbarlight1.getMenu().RemoveItem((int) (1));
 //BA.debugLineNum = 824;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("Load Picklist"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 825;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 826;BA.debugLine="UpdateIcon(\"Load Picklist\", addBitmap)";
_updateicon("Load Picklist",parent._addbitmap);
 //BA.debugLineNum = 827;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 828;BA.debugLine="LABEL_LOAD_NAME.Text = \"-\"";
parent.mostCurrent._label_load_name.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 829;BA.debugLine="LABEL_LOAD_DATE.Text = \"-\"";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 830;BA.debugLine="LABEL_LOAD_STATUS.Text = \"-\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 831;BA.debugLine="LABEL_LOAD_ONHOLD.Text = \"-\"";
parent.mostCurrent._label_load_onhold.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 832;BA.debugLine="BUTTON_DELIVERY.Enabled = False";
parent.mostCurrent._button_delivery.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 833;BA.debugLine="BUTTON_AUTOFILL.Enabled = False";
parent.mostCurrent._button_autofill.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 834;BA.debugLine="BUTTON_UPLOAD.Enabled = False";
parent.mostCurrent._button_upload.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 835;BA.debugLine="picklist_id = \"\"";
parent._picklist_id = "";
 //BA.debugLineNum = 836;BA.debugLine="LOADING_TABLE.Clear";
parent.mostCurrent._loading_table._clear /*void*/ ();
 //BA.debugLineNum = 837;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 838;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _clear_picklist_sku() throws Exception{
 //BA.debugLineNum = 1439;BA.debugLine="Sub CLEAR_PICKLIST_SKU";
 //BA.debugLineNum = 1440;BA.debugLine="LABEL_CASE_QTY_LOADING.Text = \"0\"";
mostCurrent._label_case_qty_loading.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1441;BA.debugLine="LABEL_PCS_QTY_LOADING.Text = \"0\"";
mostCurrent._label_pcs_qty_loading.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1442;BA.debugLine="LABEL_BOX_QTY_LOADING.Text = \"0\"";
mostCurrent._label_box_qty_loading.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1443;BA.debugLine="LABEL_DOZ_QTY_LOADING.Text = \"0\"";
mostCurrent._label_doz_qty_loading.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1444;BA.debugLine="LABEL_BAG_QTY_LOADING.Text = \"0\"";
mostCurrent._label_bag_qty_loading.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1445;BA.debugLine="LABEL_PACK_QTY_LOADING.Text = \"0\"";
mostCurrent._label_pack_qty_loading.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1446;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.Text = \"\"";
mostCurrent._edittext_case_qty_loading.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1447;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.Text = \"\"";
mostCurrent._edittext_pcs_qty_loading.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1448;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.Text = \"\"";
mostCurrent._edittext_box_qty_loading.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1449;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.Text = \"\"";
mostCurrent._edittext_doz_qty_loading.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1450;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.Text = \"\"";
mostCurrent._edittext_pack_qty_loading.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1451;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.Text = \"\"";
mostCurrent._edittext_bag_qty_loading.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1452;BA.debugLine="BUTTON_BAG_LOADING.Text = \"LOAD\"";
mostCurrent._button_bag_loading.setText(BA.ObjectToCharSequence("LOAD"));
 //BA.debugLineNum = 1453;BA.debugLine="BUTTON_BOX_LOADING.Text = \"LOAD\"";
mostCurrent._button_box_loading.setText(BA.ObjectToCharSequence("LOAD"));
 //BA.debugLineNum = 1454;BA.debugLine="BUTTON_CASE_LOADING.Text = \"LOAD\"";
mostCurrent._button_case_loading.setText(BA.ObjectToCharSequence("LOAD"));
 //BA.debugLineNum = 1455;BA.debugLine="BUTTON_DOZ_LOADING.Text = \"LOAD\"";
mostCurrent._button_doz_loading.setText(BA.ObjectToCharSequence("LOAD"));
 //BA.debugLineNum = 1456;BA.debugLine="BUTTON_PACK_LOADING.Text = \"LOAD\"";
mostCurrent._button_pack_loading.setText(BA.ObjectToCharSequence("LOAD"));
 //BA.debugLineNum = 1457;BA.debugLine="BUTTON_PCS_LOADING.Text = \"LOAD\"";
mostCurrent._button_pcs_loading.setText(BA.ObjectToCharSequence("LOAD"));
 //BA.debugLineNum = 1458;BA.debugLine="DISABLE_BUTTON";
_disable_button();
 //BA.debugLineNum = 1459;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_picker_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_PICKER_SelectedIndexChanged rsub = new ResumableSub_CMB_PICKER_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_PICKER_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_PICKER_SelectedIndexChanged(wingan.app.loading_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1966;BA.debugLine="CMB_SUPERVISOR.SelectedIndex = -1";
parent.mostCurrent._cmb_supervisor._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1967;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1968;BA.debugLine="OpenSpinner(CMB_SUPERVISOR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_supervisor._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1969;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_reason_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_REASON_SelectedIndexChanged rsub = new ResumableSub_CMB_REASON_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_REASON_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_REASON_SelectedIndexChanged(wingan.app.loading_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1976;BA.debugLine="CMB_UNIT.SelectedIndex = 1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (1));
 //BA.debugLineNum = 1977;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1978;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1979;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_supervisor_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_SUPERVISOR_SelectedIndexChanged rsub = new ResumableSub_CMB_SUPERVISOR_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_SUPERVISOR_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_SUPERVISOR_SelectedIndexChanged(wingan.app.loading_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1971;BA.debugLine="EDITTEXT_TRAIL_PASS.RequestFocus";
parent.mostCurrent._edittext_trail_pass.RequestFocus();
 //BA.debugLineNum = 1972;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1973;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_TRAIL_PASS)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_trail_pass.getObject()));
 //BA.debugLineNum = 1974;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_unit_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_UNIT_SelectedIndexChanged rsub = new ResumableSub_CMB_UNIT_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_UNIT_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.loading_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1981;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 1982;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1983;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 1984;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 580;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 581;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 582;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 583;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 584;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 585;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 586;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 575;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 576;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 577;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,loading_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 578;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 579;BA.debugLine="End Sub";
return null;
}
public static void  _delete_dispatch() throws Exception{
ResumableSub_DELETE_DISPATCH rsub = new ResumableSub_DELETE_DISPATCH(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_DISPATCH extends BA.ResumableSub {
public ResumableSub_DELETE_DISPATCH(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2331;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_disp";
_cmd = _createcommand("delete_dispatch",new Object[]{(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 2332;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2333;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2334;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2335;BA.debugLine="INSERT_DISPATCH";
_insert_dispatch();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2337;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2338;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("756295432","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2339;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2340;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2342;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2343;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_picklist_loaded_trail() throws Exception{
ResumableSub_DELETE_PICKLIST_LOADED_TRAIL rsub = new ResumableSub_DELETE_PICKLIST_LOADED_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_PICKLIST_LOADED_TRAIL extends BA.ResumableSub {
public ResumableSub_DELETE_PICKLIST_LOADED_TRAIL(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2286;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_pick";
_cmd = _createcommand("delete_picklist_trail",new Object[]{(Object)(parent._picklist_id.trim()),(Object)("LOADING")});
 //BA.debugLineNum = 2287;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2288;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2289;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2290;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 2291;BA.debugLine="INSERT_PICKLIST_LOADED_TRAIL";
_insert_picklist_loaded_trail();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2293;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2294;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2295;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2296;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 9;
return;
case 9:
//C
this.state = 6;
;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2298;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2299;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_picklist_loading() throws Exception{
ResumableSub_DELETE_PICKLIST_LOADING rsub = new ResumableSub_DELETE_PICKLIST_LOADING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_PICKLIST_LOADING extends BA.ResumableSub {
public ResumableSub_DELETE_PICKLIST_LOADING(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2197;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2198;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2199;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Picklist\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Picklist"));
 //BA.debugLineNum = 2200;BA.debugLine="LABEL_MSGBOX2.Text = \"Data getting ready..\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data getting ready.."));
 //BA.debugLineNum = 2201;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_pick";
_cmd = _createcommand("delete_picklist_loaded",new Object[]{(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 2202;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2203;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2204;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2205;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 2206;BA.debugLine="INSERT_PICKLIST_LOADING";
_insert_picklist_loading();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2208;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2209;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2210;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2211;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 9;
return;
case 9:
//C
this.state = 6;
;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2213;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2214;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _disable_button() throws Exception{
 //BA.debugLineNum = 2395;BA.debugLine="Sub DISABLE_BUTTON";
 //BA.debugLineNum = 2396;BA.debugLine="BUTTON_CASE_LOADING.Enabled = False";
mostCurrent._button_case_loading.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2397;BA.debugLine="BUTTON_CASE_LOADING.Background = BG_DISABLE";
mostCurrent._button_case_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_disable.getObject()));
 //BA.debugLineNum = 2398;BA.debugLine="BUTTON_PCS_LOADING.Enabled = False";
mostCurrent._button_pcs_loading.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2399;BA.debugLine="BUTTON_PCS_LOADING.Background = BG_DISABLE";
mostCurrent._button_pcs_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_disable.getObject()));
 //BA.debugLineNum = 2400;BA.debugLine="BUTTON_BOX_LOADING.Enabled = False";
mostCurrent._button_box_loading.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2401;BA.debugLine="BUTTON_BOX_LOADING.Background = BG_DISABLE";
mostCurrent._button_box_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_disable.getObject()));
 //BA.debugLineNum = 2402;BA.debugLine="BUTTON_DOZ_LOADING.Enabled = False";
mostCurrent._button_doz_loading.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2403;BA.debugLine="BUTTON_DOZ_LOADING.Background = BG_DISABLE";
mostCurrent._button_doz_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_disable.getObject()));
 //BA.debugLineNum = 2404;BA.debugLine="BUTTON_PACK_LOADING.Enabled = False";
mostCurrent._button_pack_loading.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2405;BA.debugLine="BUTTON_PACK_LOADING.Background = BG_DISABLE";
mostCurrent._button_pack_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_disable.getObject()));
 //BA.debugLineNum = 2406;BA.debugLine="BUTTON_BAG_LOADING.Enabled = False";
mostCurrent._button_bag_loading.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2407;BA.debugLine="BUTTON_BAG_LOADING.Background = BG_DISABLE";
mostCurrent._button_bag_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_disable.getObject()));
 //BA.debugLineNum = 2409;BA.debugLine="End Sub";
return "";
}
public static void  _download_picklist() throws Exception{
ResumableSub_DOWNLOAD_PICKLIST rsub = new ResumableSub_DOWNLOAD_PICKLIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DOWNLOAD_PICKLIST extends BA.ResumableSub {
public ResumableSub_DOWNLOAD_PICKLIST(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group13;
int index13;
int groupLen13;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 588;BA.debugLine="LABEL_MSGBOX2.Text = \"Getting Prepared...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Getting Prepared..."));
 //BA.debugLineNum = 589;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 590;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_pick";
_cmd = _createcommand("select_picklist_confirmed",new Object[]{(Object)(parent._picklist_id)});
 //BA.debugLineNum = 591;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 592;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 16;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 593;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist_lo";
parent._connection.ExecNonQuery("DELETE FROM picklist_loading_ref_table WHERE picklist_id = '"+parent._picklist_id+"'");
 //BA.debugLineNum = 594;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 4;
;
 //BA.debugLineNum = 595;BA.debugLine="connection.ExecNonQuery(\"UPDATE picklist_loading";
parent._connection.ExecNonQuery("UPDATE picklist_loading_disc_table SET confirm_status = '1' WHERE picklist_id = '"+parent._picklist_id+"'");
 //BA.debugLineNum = 596;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 4;
;
 //BA.debugLineNum = 597;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 598;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 20;
return;
case 20:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 599;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 600;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group13 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index13 = 0;
groupLen13 = group13.getSize();
this.state = 21;
if (true) break;

case 21:
//C
this.state = 10;
if (index13 < groupLen13) {
this.state = 9;
_row = (Object[])(group13.Get(index13));}
if (true) break;

case 22:
//C
this.state = 21;
index13++;
if (true) break;

case 9:
//C
this.state = 22;
 //BA.debugLineNum = 601;BA.debugLine="LABEL_LOAD_NAME.Text = row(res.Columns.Get(\"pi";
parent.mostCurrent._label_load_name.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_name"))))]));
 //BA.debugLineNum = 602;BA.debugLine="LABEL_LOAD_DATE.Text = row(res.Columns.Get(\"pi";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_date"))))]));
 //BA.debugLineNum = 604;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO picklist_";
parent._connection.ExecNonQuery("INSERT INTO picklist_loading_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_date"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prepared_quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prepared_total_pcs"))))])+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+parent.mostCurrent._login_module._tab_id /*String*/ +"','"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 607;BA.debugLine="Sleep(50)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (50));
this.state = 23;
return;
case 23:
//C
this.state = 22;
;
 //BA.debugLineNum = 608;BA.debugLine="connection.ExecNonQuery(\"UPDATE picklist_loadi";
parent._connection.ExecNonQuery("UPDATE picklist_loading_disc_table SET confirm_status = '0' WHERE picklist_id = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_id"))))])+"' and product_id = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"' and unit = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"' and quantity = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"'");
 //BA.debugLineNum = 609;BA.debugLine="LABEL_MSGBOX2.Text = \"Getting Order : \" & row(";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Getting Order : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 612;BA.debugLine="CHECK_PICKLIST_LOADING";
_check_picklist_loading();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 614;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 615;BA.debugLine="Msgbox2Async(\"The Picklist ID you type/scan is";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 616;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 13;
;
 //BA.debugLineNum = 617;BA.debugLine="CLEAR_PICKLIST";
_clear_picklist();
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 620;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 621;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("751970082","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 622;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 623;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 625;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 626;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _edittext_bag_qty_loading_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 1823;BA.debugLine="Sub EDITTEXT_BAG_QTY_LOADING_FocusChanged (HasFocu";
 //BA.debugLineNum = 1824;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1825;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.SelectAll";
mostCurrent._edittext_bag_qty_loading.SelectAll();
 }else {
 };
 //BA.debugLineNum = 1828;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_box_qty_loading_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 1842;BA.debugLine="Sub EDITTEXT_BOX_QTY_LOADING_FocusChanged (HasFocu";
 //BA.debugLineNum = 1843;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1844;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.SelectAll";
mostCurrent._edittext_box_qty_loading.SelectAll();
 }else {
 };
 //BA.debugLineNum = 1848;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_case_qty_loading_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 1849;BA.debugLine="Sub EDITTEXT_CASE_QTY_LOADING_FocusChanged (HasFoc";
 //BA.debugLineNum = 1850;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1851;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.SelectAll";
mostCurrent._edittext_case_qty_loading.SelectAll();
 }else {
 };
 //BA.debugLineNum = 1855;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_doz_qty_loading_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 1835;BA.debugLine="Sub EDITTEXT_DOZ_QTY_LOADING_FocusChanged (HasFocu";
 //BA.debugLineNum = 1836;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1837;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.SelectAll";
mostCurrent._edittext_doz_qty_loading.SelectAll();
 }else {
 };
 //BA.debugLineNum = 1841;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_pack_qty_loading_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 1829;BA.debugLine="Sub EDITTEXT_PACK_QTY_LOADING_FocusChanged (HasFoc";
 //BA.debugLineNum = 1830;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1831;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.SelectAll";
mostCurrent._edittext_pack_qty_loading.SelectAll();
 }else {
 };
 //BA.debugLineNum = 1834;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_pcs_qty_loading_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 1856;BA.debugLine="Sub EDITTEXT_PCS_QTY_LOADING_FocusChanged (HasFocu";
 //BA.debugLineNum = 1857;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1858;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.SelectAll";
mostCurrent._edittext_pcs_qty_loading.SelectAll();
 }else {
 };
 //BA.debugLineNum = 1862;BA.debugLine="End Sub";
return "";
}
public static void  _edittext_quantity_enterpressed() throws Exception{
ResumableSub_EDITTEXT_QUANTITY_EnterPressed rsub = new ResumableSub_EDITTEXT_QUANTITY_EnterPressed(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_EDITTEXT_QUANTITY_EnterPressed extends BA.ResumableSub {
public ResumableSub_EDITTEXT_QUANTITY_EnterPressed(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2412;BA.debugLine="If over_short_trigger = 0 Then";
if (true) break;

case 1:
//if
this.state = 4;
if ((parent._over_short_trigger).equals(BA.NumberToString(0))) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2413;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 2414;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 4;
;
 //BA.debugLineNum = 2415;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 2417;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _enable_picklist() throws Exception{
ResumableSub_ENABLE_PICKLIST rsub = new ResumableSub_ENABLE_PICKLIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ENABLE_PICKLIST extends BA.ResumableSub {
public ResumableSub_ENABLE_PICKLIST(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 840;BA.debugLine="ACToolBarLight1.Menu.RemoveItem(1)";
parent.mostCurrent._actoolbarlight1.getMenu().RemoveItem((int) (1));
 //BA.debugLineNum = 841;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 842;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("Clear"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 843;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 844;BA.debugLine="UpdateIcon(\"Clear\", clearBitmap)";
_updateicon("Clear",parent._clearbitmap);
 //BA.debugLineNum = 845;BA.debugLine="BUTTON_DELIVERY.Enabled = True";
parent.mostCurrent._button_delivery.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 846;BA.debugLine="BUTTON_AUTOFILL.Enabled = True";
parent.mostCurrent._button_autofill.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 847;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_delivery_details() throws Exception{
ResumableSub_GET_DELIVERY_DETAILS rsub = new ResumableSub_GET_DELIVERY_DETAILS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_DELIVERY_DETAILS extends BA.ResumableSub {
public ResumableSub_GET_DELIVERY_DETAILS(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 2074;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_delivery_report_table WHERE picklist_id = '"+parent._picklist_id+"'")));
 //BA.debugLineNum = 2075;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor8.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2076;BA.debugLine="For i = 0 To cursor8.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor8.getRowCount()-1);
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
 //BA.debugLineNum = 2077;BA.debugLine="cursor8.Position = i";
parent._cursor8.setPosition(_i);
 //BA.debugLineNum = 2078;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 2079;BA.debugLine="EDITTEXT_PLATE.Text = cursor8.GetString(\"plate_";
parent.mostCurrent._edittext_plate.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("plate_no")));
 //BA.debugLineNum = 2080;BA.debugLine="EDITTEXT_PUSHCART.Text = cursor8.GetString(\"pus";
parent.mostCurrent._edittext_pushcart.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("pushcart")));
 //BA.debugLineNum = 2081;BA.debugLine="EDITTEXT_DRIVER.Text = cursor8.GetString(\"drive";
parent.mostCurrent._edittext_driver.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("driver")));
 //BA.debugLineNum = 2082;BA.debugLine="EDITTEXT_HELPER1.Text = cursor8.GetString(\"help";
parent.mostCurrent._edittext_helper1.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("helper1")));
 //BA.debugLineNum = 2083;BA.debugLine="EDITTEXT_HELPER2.Text = cursor8.GetString(\"help";
parent.mostCurrent._edittext_helper2.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("helper2")));
 //BA.debugLineNum = 2084;BA.debugLine="EDITTEXT_HELPER3.Text = cursor8.GetString(\"help";
parent.mostCurrent._edittext_helper3.setText(BA.ObjectToCharSequence(parent._cursor8.GetString("helper3")));
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
 //BA.debugLineNum = 2087;BA.debugLine="ToastMessageShow(\"No existing picklist delivery\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No existing picklist delivery"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 2089;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_prepared_details() throws Exception{
ResumableSub_GET_PREPARED_DETAILS rsub = new ResumableSub_GET_PREPARED_DETAILS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_PREPARED_DETAILS extends BA.ResumableSub {
public ResumableSub_GET_PREPARED_DETAILS(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _i = 0;
String _picker = "";
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
int step4;
int limit4;
anywheresoftware.b4a.BA.IterableList group15;
int index15;
int groupLen15;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1879;BA.debugLine="CMB_PICKER.cmbBox.Clear";
parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1881;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table WHERE Position = 'LOGISTIC PERSONEL' ORDER BY User ASC")));
 //BA.debugLineNum = 1882;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 24;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1883;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 23;
step4 = 1;
limit4 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 25;
if (true) break;

case 25:
//C
this.state = 23;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 26:
//C
this.state = 25;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1884;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 1885;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 27;
return;
case 27:
//C
this.state = 7;
;
 //BA.debugLineNum = 1886;BA.debugLine="Dim picker As String = \"%\"& cursor1.GetString(\"";
_picker = "%"+parent._cursor1.GetString("User")+"%";
 //BA.debugLineNum = 1888;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 1889;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_pi";
_cmd = _createcommand("select_picklist_prepared",new Object[]{(Object)(parent._picklist_id),(Object)(_picker)});
 //BA.debugLineNum = 1890;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDo";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1891;BA.debugLine="If jr.Success Then";
if (true) break;

case 7:
//if
this.state = 22;
if (_jr._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 21;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1892;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 1893;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 29;
return;
case 29:
//C
this.state = 10;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 1894;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 10:
//if
this.state = 19;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 12;
}else {
this.state = 18;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1895;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 13:
//for
this.state = 16;
group15 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index15 = 0;
groupLen15 = group15.getSize();
this.state = 30;
if (true) break;

case 30:
//C
this.state = 16;
if (index15 < groupLen15) {
this.state = 15;
_row = (Object[])(group15.Get(index15));}
if (true) break;

case 31:
//C
this.state = 30;
index15++;
if (true) break;

case 15:
//C
this.state = 31;
 if (true) break;
if (true) break;

case 16:
//C
this.state = 19;
;
 //BA.debugLineNum = 1897;BA.debugLine="CMB_PICKER.cmbBox.Add(cursor1.GetString(\"User";
parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor1.GetString("User"));
 //BA.debugLineNum = 1898;BA.debugLine="LABEL_LOAD_CHECKER.Text = row(res.Columns.Get";
parent.mostCurrent._label_load_checker.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prepared_by"))))]));
 if (true) break;

case 18:
//C
this.state = 19;
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1903;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1904;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("754919194","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1905;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1906;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 22:
//C
this.state = 26;
;
 //BA.debugLineNum = 1908;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 if (true) break;
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
 //BA.debugLineNum = 1914;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_product_details() throws Exception{
ResumableSub_GET_PRODUCT_DETAILS rsub = new ResumableSub_GET_PRODUCT_DETAILS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_PRODUCT_DETAILS extends BA.ResumableSub {
public ResumableSub_GET_PRODUCT_DETAILS(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _qrow = 0;
int _row = 0;
int step3;
int limit3;
int step30;
int limit30;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1351;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_desc_loading.getText()+"'")));
 //BA.debugLineNum = 1352;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 30;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1353;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 21;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 31;
if (true) break;

case 31:
//C
this.state = 21;
if ((step3 > 0 && _qrow <= limit3) || (step3 < 0 && _qrow >= limit3)) this.state = 6;
if (true) break;

case 32:
//C
this.state = 31;
_qrow = ((int)(0 + _qrow + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1354;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1355;BA.debugLine="LABEL_LOAD_VARIANT_LOADING.Text = cursor3.GetSt";
parent.mostCurrent._label_load_variant_loading.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1356;BA.debugLine="principal_id = cursor3.GetString(\"principal_id\"";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 1357;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 1359;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS\"";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1360;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1361;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1362;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1363;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1364;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS\"";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 1367;BA.debugLine="If scan_code = cursor3.GetString(\"case_bar_code";
if (true) break;

case 7:
//if
this.state = 20;
if ((parent._scan_code).equals(parent._cursor3.GetString("case_bar_code"))) { 
this.state = 9;
}else if((parent._scan_code).equals(parent._cursor3.GetString("bar_code"))) { 
this.state = 11;
}else if((parent._scan_code).equals(parent._cursor3.GetString("box_bar_code"))) { 
this.state = 13;
}else if((parent._scan_code).equals(parent._cursor3.GetString("pack_bar_code"))) { 
this.state = 15;
}else if((parent._scan_code).equals(parent._cursor3.GetString("bag_bar_code"))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 1368;BA.debugLine="LABEL_LOAD_SCANCODE.Text = \"CASE\"";
parent.mostCurrent._label_load_scancode.setText(BA.ObjectToCharSequence("CASE"));
 if (true) break;

case 11:
//C
this.state = 20;
 //BA.debugLineNum = 1370;BA.debugLine="LABEL_LOAD_SCANCODE.Text = \"PCS\"";
parent.mostCurrent._label_load_scancode.setText(BA.ObjectToCharSequence("PCS"));
 if (true) break;

case 13:
//C
this.state = 20;
 //BA.debugLineNum = 1372;BA.debugLine="LABEL_LOAD_SCANCODE.Text = \"BOX\"";
parent.mostCurrent._label_load_scancode.setText(BA.ObjectToCharSequence("BOX"));
 if (true) break;

case 15:
//C
this.state = 20;
 //BA.debugLineNum = 1374;BA.debugLine="LABEL_LOAD_SCANCODE.Text = \"PACK\"";
parent.mostCurrent._label_load_scancode.setText(BA.ObjectToCharSequence("PACK"));
 if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 1376;BA.debugLine="LABEL_LOAD_SCANCODE.Text = \"CASE\"";
parent.mostCurrent._label_load_scancode.setText(BA.ObjectToCharSequence("CASE"));
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1378;BA.debugLine="LABEL_LOAD_SCANCODE.Text = \"-\"";
parent.mostCurrent._label_load_scancode.setText(BA.ObjectToCharSequence("-"));
 if (true) break;

case 20:
//C
this.state = 32;
;
 if (true) break;
if (true) break;

case 21:
//C
this.state = 22;
;
 //BA.debugLineNum = 1386;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 1387;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 22:
//if
this.state = 29;
if (parent._cursor6.getRowCount()>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1388;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 25:
//for
this.state = 28;
step30 = 1;
limit30 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 33;
if (true) break;

case 33:
//C
this.state = 28;
if ((step30 > 0 && _row <= limit30) || (step30 < 0 && _row >= limit30)) this.state = 27;
if (true) break;

case 34:
//C
this.state = 33;
_row = ((int)(0 + _row + step30)) ;
if (true) break;

case 27:
//C
this.state = 34;
 //BA.debugLineNum = 1389;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 1390;BA.debugLine="principal_name = cursor6.Getstring(\"principal_";
parent._principal_name = parent._cursor6.GetString("principal_name");
 if (true) break;
if (true) break;

case 28:
//C
this.state = 29;
;
 if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 1395;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 30;
;
 if (true) break;

case 30:
//C
this.state = -1;
;
 //BA.debugLineNum = 1397;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_security() throws Exception{
ResumableSub_GET_SECURITY rsub = new ResumableSub_GET_SECURITY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_SECURITY extends BA.ResumableSub {
public ResumableSub_GET_SECURITY(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1159;BA.debugLine="CMB_ACCOUNT.cmbBox.Clear";
parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1160;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table WHERE Position LIKE '%LOGISTIC OFFICER%' ORDER BY User ASC")));
 //BA.debugLineNum = 1161;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
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
 //BA.debugLineNum = 1162;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 1163;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 1164;BA.debugLine="CMB_ACCOUNT.cmbBox.Add(cursor3.GetString(\"User\")";
parent.mostCurrent._cmb_account._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor3.GetString("User"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1166;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_status() throws Exception{
ResumableSub_GET_STATUS rsub = new ResumableSub_GET_STATUS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_STATUS extends BA.ResumableSub {
public ResumableSub_GET_STATUS(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _order_count = 0;
int _serve_count = 0;
int _on_hold_count = 0;
int _ia = 0;
int _ib = 0;
int step6;
int limit6;
int step16;
int limit16;
int step33;
int limit33;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1087;BA.debugLine="Dim order_count As Int";
_order_count = 0;
 //BA.debugLineNum = 1088;BA.debugLine="Dim serve_count As Int";
_serve_count = 0;
 //BA.debugLineNum = 1089;BA.debugLine="Dim on_hold_count As Int";
_on_hold_count = 0;
 //BA.debugLineNum = 1092;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT COUNT(prod";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT COUNT(product_id) as 'order_count' FROM picklist_loading_ref_table WHERE picklist_id = '"+parent._picklist_id+"' GROUP BY product_id, unit")));
 //BA.debugLineNum = 1093;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1094;BA.debugLine="For ia = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step6 = 1;
limit6 = (int) (parent._cursor4.getRowCount()-1);
_ia = (int) (0) ;
this.state = 40;
if (true) break;

case 40:
//C
this.state = 7;
if ((step6 > 0 && _ia <= limit6) || (step6 < 0 && _ia >= limit6)) this.state = 6;
if (true) break;

case 41:
//C
this.state = 40;
_ia = ((int)(0 + _ia + step6)) ;
if (true) break;

case 6:
//C
this.state = 41;
 //BA.debugLineNum = 1095;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 41;
;
 //BA.debugLineNum = 1096;BA.debugLine="cursor4.Position = ia";
parent._cursor4.setPosition(_ia);
 //BA.debugLineNum = 1097;BA.debugLine="order_count = order_count + 1";
_order_count = (int) (_order_count+1);
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
 //BA.debugLineNum = 1104;BA.debugLine="order_count = 0";
_order_count = (int) (0);
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 1108;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT COUNT(prod";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT COUNT(product_id) as 'serve_count' FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id+"' GROUP BY product_id, unit")));
 //BA.debugLineNum = 1109;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 11:
//if
this.state = 20;
if (parent._cursor5.getRowCount()>0) { 
this.state = 13;
}else {
this.state = 19;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1110;BA.debugLine="For ib = 0 To cursor5.RowCount - 1";
if (true) break;

case 14:
//for
this.state = 17;
step16 = 1;
limit16 = (int) (parent._cursor5.getRowCount()-1);
_ib = (int) (0) ;
this.state = 43;
if (true) break;

case 43:
//C
this.state = 17;
if ((step16 > 0 && _ib <= limit16) || (step16 < 0 && _ib >= limit16)) this.state = 16;
if (true) break;

case 44:
//C
this.state = 43;
_ib = ((int)(0 + _ib + step16)) ;
if (true) break;

case 16:
//C
this.state = 44;
 //BA.debugLineNum = 1111;BA.debugLine="cursor5.Position = ib";
parent._cursor5.setPosition(_ib);
 //BA.debugLineNum = 1112;BA.debugLine="serve_count = serve_count + 1";
_serve_count = (int) (_serve_count+1);
 if (true) break;
if (true) break;

case 17:
//C
this.state = 20;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1119;BA.debugLine="serve_count = 0";
_serve_count = (int) (0);
 if (true) break;

case 20:
//C
this.state = 21;
;
 //BA.debugLineNum = 1122;BA.debugLine="LABEL_LOAD_STATUS.Text = serve_count & \" / \" & or";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence(BA.NumberToString(_serve_count)+" / "+BA.NumberToString(_order_count)));
 //BA.debugLineNum = 1123;BA.debugLine="If serve_count = order_count Then";
if (true) break;

case 21:
//if
this.state = 26;
if (_serve_count==_order_count) { 
this.state = 23;
}else {
this.state = 25;
}if (true) break;

case 23:
//C
this.state = 26;
 //BA.debugLineNum = 1124;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Green";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1125;BA.debugLine="BUTTON_UPLOAD.Enabled = True";
parent.mostCurrent._button_upload.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 1127;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Red";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 1128;BA.debugLine="BUTTON_UPLOAD.Enabled = False";
parent.mostCurrent._button_upload.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 1132;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT COUNT(prod";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT COUNT(product_id) as 'on_hold' FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id+"' AND loaded_status = 'ON HOLD' GROUP BY product_id, unit")));
 //BA.debugLineNum = 1133;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 27:
//if
this.state = 36;
if (parent._cursor6.getRowCount()>0) { 
this.state = 29;
}else {
this.state = 35;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 1134;BA.debugLine="For ib = 0 To cursor6.RowCount - 1";
if (true) break;

case 30:
//for
this.state = 33;
step33 = 1;
limit33 = (int) (parent._cursor6.getRowCount()-1);
_ib = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 33;
if ((step33 > 0 && _ib <= limit33) || (step33 < 0 && _ib >= limit33)) this.state = 32;
if (true) break;

case 46:
//C
this.state = 45;
_ib = ((int)(0 + _ib + step33)) ;
if (true) break;

case 32:
//C
this.state = 46;
 //BA.debugLineNum = 1135;BA.debugLine="cursor6.Position = ib";
parent._cursor6.setPosition(_ib);
 //BA.debugLineNum = 1136;BA.debugLine="on_hold_count = on_hold_count + 1";
_on_hold_count = (int) (_on_hold_count+1);
 if (true) break;
if (true) break;

case 33:
//C
this.state = 36;
;
 if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 1145;BA.debugLine="on_hold_count = 0";
_on_hold_count = (int) (0);
 if (true) break;
;
 //BA.debugLineNum = 1148;BA.debugLine="If on_hold_count <> 0 Then";

case 36:
//if
this.state = 39;
if (_on_hold_count!=0) { 
this.state = 38;
}if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 1149;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Red";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 1150;BA.debugLine="BUTTON_UPLOAD.Enabled = False";
parent.mostCurrent._button_upload.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 39:
//C
this.state = -1;
;
 //BA.debugLineNum = 1153;BA.debugLine="LABEL_LOAD_ONHOLD.Text = on_hold_count";
parent.mostCurrent._label_load_onhold.setText(BA.ObjectToCharSequence(_on_hold_count));
 //BA.debugLineNum = 1156;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_total_served() throws Exception{
int _row = 0;
 //BA.debugLineNum = 1398;BA.debugLine="Sub GET_TOTAL_SERVED";
 //BA.debugLineNum = 1400;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM pic";
_cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM picklist_LOADING_disc_table WHERE product_description ='"+mostCurrent._label_load_desc_loading.getText()+"' AND picklist_id = '"+_picklist_id+"'")));
 //BA.debugLineNum = 1401;BA.debugLine="If cursor7.RowCount > 0 Then";
if (_cursor7.getRowCount()>0) { 
 //BA.debugLineNum = 1402;BA.debugLine="For row = 0 To cursor7.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor7.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit3 ;_row = _row + step3 ) {
 //BA.debugLineNum = 1403;BA.debugLine="cursor7.Position = row";
_cursor7.setPosition(_row);
 //BA.debugLineNum = 1404;BA.debugLine="If cursor7.GetString(\"unit\") = \"CASE\" Then";
if ((_cursor7.GetString("unit")).equals("CASE")) { 
 //BA.debugLineNum = 1405;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.Text = cursor7.GetSt";
mostCurrent._edittext_case_qty_loading.setText(BA.ObjectToCharSequence(_cursor7.GetString("loaded_quantity")));
 //BA.debugLineNum = 1406;BA.debugLine="BUTTON_CASE_LOADING.Text = \"UPDATE\"";
mostCurrent._button_case_loading.setText(BA.ObjectToCharSequence("UPDATE"));
 //BA.debugLineNum = 1407;BA.debugLine="BUTTON_CASE_LOADING.Background = BG_UPDATE";
mostCurrent._button_case_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_update.getObject()));
 };
 //BA.debugLineNum = 1409;BA.debugLine="If cursor7.GetString(\"unit\") = \"PCS\" Then";
if ((_cursor7.GetString("unit")).equals("PCS")) { 
 //BA.debugLineNum = 1410;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.Text = cursor7.GetStr";
mostCurrent._edittext_pcs_qty_loading.setText(BA.ObjectToCharSequence(_cursor7.GetString("loaded_quantity")));
 //BA.debugLineNum = 1411;BA.debugLine="BUTTON_PCS_LOADING.Text = \"UPDATE\"";
mostCurrent._button_pcs_loading.setText(BA.ObjectToCharSequence("UPDATE"));
 //BA.debugLineNum = 1412;BA.debugLine="BUTTON_PCS_LOADING.Background = BG_UPDATE";
mostCurrent._button_pcs_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_update.getObject()));
 };
 //BA.debugLineNum = 1414;BA.debugLine="If cursor7.GetString(\"unit\") = \"BOX\" Then";
if ((_cursor7.GetString("unit")).equals("BOX")) { 
 //BA.debugLineNum = 1415;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.Text = cursor7.GetStr";
mostCurrent._edittext_box_qty_loading.setText(BA.ObjectToCharSequence(_cursor7.GetString("loaded_quantity")));
 //BA.debugLineNum = 1416;BA.debugLine="BUTTON_BOX_LOADING.Text = \"UPDATE\"";
mostCurrent._button_box_loading.setText(BA.ObjectToCharSequence("UPDATE"));
 //BA.debugLineNum = 1417;BA.debugLine="BUTTON_BOX_LOADING.Background = BG_UPDATE";
mostCurrent._button_box_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_update.getObject()));
 };
 //BA.debugLineNum = 1419;BA.debugLine="If cursor7.GetString(\"unit\") = \"DOZ\" Then";
if ((_cursor7.GetString("unit")).equals("DOZ")) { 
 //BA.debugLineNum = 1420;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.Text = cursor7.GetStr";
mostCurrent._edittext_doz_qty_loading.setText(BA.ObjectToCharSequence(_cursor7.GetString("loaded_quantity")));
 //BA.debugLineNum = 1421;BA.debugLine="BUTTON_DOZ_LOADING.Text = \"UPDATE\"";
mostCurrent._button_doz_loading.setText(BA.ObjectToCharSequence("UPDATE"));
 //BA.debugLineNum = 1422;BA.debugLine="BUTTON_DOZ_LOADING.Background = BG_UPDATE";
mostCurrent._button_doz_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_update.getObject()));
 };
 //BA.debugLineNum = 1424;BA.debugLine="If cursor7.GetString(\"unit\") = \"BAG\" Then";
if ((_cursor7.GetString("unit")).equals("BAG")) { 
 //BA.debugLineNum = 1425;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.Text = cursor7.GetStr";
mostCurrent._edittext_bag_qty_loading.setText(BA.ObjectToCharSequence(_cursor7.GetString("loaded_quantity")));
 //BA.debugLineNum = 1426;BA.debugLine="BUTTON_BAG_LOADING.Text = \"UPDATE\"";
mostCurrent._button_bag_loading.setText(BA.ObjectToCharSequence("UPDATE"));
 //BA.debugLineNum = 1427;BA.debugLine="BUTTON_BAG_LOADING.Background = BG_UPDATE";
mostCurrent._button_bag_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_update.getObject()));
 };
 //BA.debugLineNum = 1429;BA.debugLine="If cursor7.GetString(\"unit\") = \"PACK\" Then";
if ((_cursor7.GetString("unit")).equals("PACK")) { 
 //BA.debugLineNum = 1430;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.Text = cursor7.GetSt";
mostCurrent._edittext_pack_qty_loading.setText(BA.ObjectToCharSequence(_cursor7.GetString("loaded_quantity")));
 //BA.debugLineNum = 1431;BA.debugLine="BUTTON_PACK_LOADING.Text = \"UPDATE\"";
mostCurrent._button_pack_loading.setText(BA.ObjectToCharSequence("UPDATE"));
 //BA.debugLineNum = 1432;BA.debugLine="BUTTON_PACK_LOADING.Background = BG_UPDATE";
mostCurrent._button_pack_loading.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg_update.getObject()));
 };
 }
};
 };
 //BA.debugLineNum = 1438;BA.debugLine="End Sub";
return "";
}
public static String  _get_unit() throws Exception{
int _qrow = 0;
 //BA.debugLineNum = 1933;BA.debugLine="Sub GET_UNIT";
 //BA.debugLineNum = 1934;BA.debugLine="CMB_UNIT.cmbBox.Enabled = True";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1936;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pro";
_cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+mostCurrent._label_load_desc_loading.getText()+"'")));
 //BA.debugLineNum = 1937;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor3.getRowCount()-1);
_qrow = (int) (0) ;
for (;_qrow <= limit3 ;_qrow = _qrow + step3 ) {
 //BA.debugLineNum = 1938;BA.debugLine="cursor3.Position = qrow";
_cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1939;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
_product_id = _cursor3.GetString("product_id");
 //BA.debugLineNum = 1940;BA.debugLine="LABEL_LOAD_VARIANT_LOADING.Text = cursor3.GetStr";
mostCurrent._label_load_variant_loading.setText(BA.ObjectToCharSequence(_cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1942;BA.debugLine="CMB_UNIT.cmbBox.Clear";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1943;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0 Th";
if ((double)(Double.parseDouble(_cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
 //BA.debugLineNum = 1944;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 };
 //BA.debugLineNum = 1946;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 The";
if ((double)(Double.parseDouble(_cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
 //BA.debugLineNum = 1947;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 };
 //BA.debugLineNum = 1949;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 The";
if ((double)(Double.parseDouble(_cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
 //BA.debugLineNum = 1950;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 };
 //BA.debugLineNum = 1952;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 The";
if ((double)(Double.parseDouble(_cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
 //BA.debugLineNum = 1953;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 };
 //BA.debugLineNum = 1955;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 The";
if ((double)(Double.parseDouble(_cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
 //BA.debugLineNum = 1956;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 };
 //BA.debugLineNum = 1958;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0 Th";
if ((double)(Double.parseDouble(_cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
 //BA.debugLineNum = 1959;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 };
 }
};
 //BA.debugLineNum = 1964;BA.debugLine="End Sub";
return "";
}
public static void  _get_visor() throws Exception{
ResumableSub_GET_VISOR rsub = new ResumableSub_GET_VISOR(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_VISOR extends BA.ResumableSub {
public ResumableSub_GET_VISOR(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 1916;BA.debugLine="CMB_SUPERVISOR.cmbBox.Clear";
parent.mostCurrent._cmb_supervisor._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1917;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table WHERE Position LIKE '%LOGISTIC OFFICER%' ORDER BY User ASC")));
 //BA.debugLineNum = 1918;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step3 = 1;
limit3 = (int) (parent._cursor6.getRowCount()-1);
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
 //BA.debugLineNum = 1919;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 1920;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 1921;BA.debugLine="CMB_SUPERVISOR.cmbBox.Add(cursor6.GetString(\"Use";
parent.mostCurrent._cmb_supervisor._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor6.GetString("User"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1923;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 308;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 309;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 310;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 311;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 312;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 315;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 71;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 72;BA.debugLine="Dim BG_ENABLE As ColorDrawable";
mostCurrent._bg_enable = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 73;BA.debugLine="Dim BG_DISABLE As ColorDrawable";
mostCurrent._bg_disable = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 74;BA.debugLine="Dim BG_UPDATE As ColorDrawable";
mostCurrent._bg_update = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 77;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 78;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 80;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 81;BA.debugLine="Private NameColumn(6) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (6)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 83;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 84;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 85;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 87;BA.debugLine="Private LOADING_TABLE As B4XTable";
mostCurrent._loading_table = new wingan.app.b4xtable();
 //BA.debugLineNum = 89;BA.debugLine="Private PANEL_BG_TYPE As Panel";
mostCurrent._panel_bg_type = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private EDITTEXT_TYPE As EditText";
mostCurrent._edittext_type = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private LABEL_LOAD_NAME As Label";
mostCurrent._label_load_name = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private LABEL_LOAD_DATE As Label";
mostCurrent._label_load_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private LABEL_LOAD_STATUS As Label";
mostCurrent._label_load_status = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private LABEL_LOAD_ONHOLD As Label";
mostCurrent._label_load_onhold = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private BUTTON_DELIVERY As Button";
mostCurrent._button_delivery = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private BUTTON_AUTOFILL As Button";
mostCurrent._button_autofill = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private BUTTON_UPLOAD As Button";
mostCurrent._button_upload = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 105;BA.debugLine="Private PANEL_BG_SECURITY As Panel";
mostCurrent._panel_bg_security = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 106;BA.debugLine="Private CMB_ACCOUNT As B4XComboBox";
mostCurrent._cmb_account = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 107;BA.debugLine="Private EDITTEXT_PASSWORD As EditText";
mostCurrent._edittext_password = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private LABEL_LOAD_NAME As Label";
mostCurrent._label_load_name = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 110;BA.debugLine="Private LABEL_LOAD_DATE As Label";
mostCurrent._label_load_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private EDITTEXT_PCS_QTY_LOADING As EditText";
mostCurrent._edittext_pcs_qty_loading = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 112;BA.debugLine="Private EDITTEXT_CASE_QTY_LOADING As EditText";
mostCurrent._edittext_case_qty_loading = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 113;BA.debugLine="Private EDITTEXT_BOX_QTY_LOADING As EditText";
mostCurrent._edittext_box_qty_loading = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 114;BA.debugLine="Private EDITTEXT_DOZ_QTY_LOADING As EditText";
mostCurrent._edittext_doz_qty_loading = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 115;BA.debugLine="Private EDITTEXT_PACK_QTY_LOADING As EditText";
mostCurrent._edittext_pack_qty_loading = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 116;BA.debugLine="Private EDITTEXT_BAG_QTY_LOADING As EditText";
mostCurrent._edittext_bag_qty_loading = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Private LABEL_LOAD_VARIANT_LOADING As Label";
mostCurrent._label_load_variant_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Private LABEL_LOAD_DESC_LOADING As Label";
mostCurrent._label_load_desc_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Private LABEL_PCS_QTY_LOADING As Label";
mostCurrent._label_pcs_qty_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 120;BA.debugLine="Private LABEL_CASE_QTY_LOADING As Label";
mostCurrent._label_case_qty_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Private LABEL_BOX_QTY_LOADING As Label";
mostCurrent._label_box_qty_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private LABEL_DOZ_QTY_LOADING As Label";
mostCurrent._label_doz_qty_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private LABEL_PACK_QTY_LOADING As Label";
mostCurrent._label_pack_qty_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private LABEL_BAG_QTY_LOADING As Label";
mostCurrent._label_bag_qty_loading = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private PANEL_BG_LOADING As Panel";
mostCurrent._panel_bg_loading = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private BUTTON_PCS_LOADING As Button";
mostCurrent._button_pcs_loading = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 127;BA.debugLine="Private BUTTON_CASE_LOADING As Button";
mostCurrent._button_case_loading = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Private BUTTON_BOX_LOADING As Button";
mostCurrent._button_box_loading = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private BUTTON_PACK_LOADING As Button";
mostCurrent._button_pack_loading = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Private BUTTON_DOZ_LOADING As Button";
mostCurrent._button_doz_loading = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Private BUTTON_BAG_LOADING As Button";
mostCurrent._button_bag_loading = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 132;BA.debugLine="Private LABEL_LOAD_CHECKER As Label";
mostCurrent._label_load_checker = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 133;BA.debugLine="Private PANEL_BG_TRAIL As Panel";
mostCurrent._panel_bg_trail = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 134;BA.debugLine="Private CMB_PICKER As B4XComboBox";
mostCurrent._cmb_picker = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 135;BA.debugLine="Private CMB_REASON As B4XComboBox";
mostCurrent._cmb_reason = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 136;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 138;BA.debugLine="Private EDITTEXT_PUSHCART As EditText";
mostCurrent._edittext_pushcart = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 139;BA.debugLine="Private EDITTEXT_HELPER3 As EditText";
mostCurrent._edittext_helper3 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 140;BA.debugLine="Private EDITTEXT_HELPER2 As EditText";
mostCurrent._edittext_helper2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 141;BA.debugLine="Private EDITTEXT_HELPER1 As EditText";
mostCurrent._edittext_helper1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 142;BA.debugLine="Private EDITTEXT_DRIVER As EditText";
mostCurrent._edittext_driver = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Private EDITTEXT_PLATE As EditText";
mostCurrent._edittext_plate = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 144;BA.debugLine="Private PANEL_BG_DELIVERY As Panel";
mostCurrent._panel_bg_delivery = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 145;BA.debugLine="Private CMB_SUPERVISOR As B4XComboBox";
mostCurrent._cmb_supervisor = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 146;BA.debugLine="Private LABEL_LOAD_SCANCODE As Label";
mostCurrent._label_load_scancode = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 147;BA.debugLine="Private EDITTEXT_TRAIL_PASS As EditText";
mostCurrent._edittext_trail_pass = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static void  _insert_dispatch() throws Exception{
ResumableSub_INSERT_DISPATCH rsub = new ResumableSub_INSERT_DISPATCH(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DISPATCH extends BA.ResumableSub {
public ResumableSub_INSERT_DISPATCH(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2345;BA.debugLine="GET_DELIVERY_DETAILS";
_get_delivery_details();
 //BA.debugLineNum = 2346;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 1;
;
 //BA.debugLineNum = 2347;BA.debugLine="PANEL_BG_DELIVERY.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_delivery.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2348;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 2349;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 1;
;
 //BA.debugLineNum = 2350;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_disp";
_cmd = _createcommand("insert_dispatch",new Object[]{(Object)(parent._picklist_id.trim()),(Object)(parent.mostCurrent._label_load_name.getText()),(Object)(parent.mostCurrent._label_load_date.getText()),(Object)("-"),(Object)(parent.mostCurrent._edittext_plate.getText().trim().toUpperCase()),(Object)(parent.mostCurrent._edittext_driver.getText().toUpperCase()),(Object)(parent.mostCurrent._edittext_helper1.getText().toUpperCase()),(Object)(parent.mostCurrent._edittext_helper2.getText().toUpperCase()),(Object)(parent.mostCurrent._edittext_helper3.getText().toUpperCase()),(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())),(Object)("-"),(Object)("-"),(Object)("-"),(Object)("-"),(Object)("-"),(Object)("-"),(Object)("-"),(Object)("-")});
 //BA.debugLineNum = 2353;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2354;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 9;
return;
case 9:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2355;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2356;BA.debugLine="UPDATE_LOADED";
_update_loaded();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2358;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2359;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("756360975","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2360;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2361;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2363;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2364;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_picklist_loaded_trail() throws Exception{
ResumableSub_INSERT_PICKLIST_LOADED_TRAIL rsub = new ResumableSub_INSERT_PICKLIST_LOADED_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_PICKLIST_LOADED_TRAIL extends BA.ResumableSub {
public ResumableSub_INSERT_PICKLIST_LOADED_TRAIL(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 2301;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 2303;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_table_trail WHERE picklist_id = '"+parent._picklist_id.trim()+"'")));
 //BA.debugLineNum = 2304;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2305;BA.debugLine="For i3 = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor4.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 16;
if (true) break;

case 16:
//C
this.state = 13;
if ((step4 > 0 && _i3 <= limit4) || (step4 < 0 && _i3 >= limit4)) this.state = 6;
if (true) break;

case 17:
//C
this.state = 16;
_i3 = ((int)(0 + _i3 + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2306;BA.debugLine="cursor4.Position = i3";
parent._cursor4.setPosition(_i3);
 //BA.debugLineNum = 2307;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_pi";
_cmd = _createcommand("insert_picklist_trail",(Object[])(new String[]{parent._cursor4.GetString("picklist_id"),parent._cursor4.GetString("picklist_name"),parent._cursor4.GetString("picklist_date"),parent._cursor4.GetString("product_id"),parent._cursor4.GetString("product_variant"),parent._cursor4.GetString("product_description"),parent._cursor4.GetString("unit"),parent._cursor4.GetString("quantity"),parent._cursor4.GetString("reason"),parent._cursor4.GetString("preparing_checker"),parent._cursor4.GetString("loading_checker"),parent._cursor4.GetString("picker"),parent._cursor4.GetString("transaction_type"),parent._cursor4.GetString("date_time_registered"),parent._cursor4.GetString("tab_id")}));
 //BA.debugLineNum = 2311;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2312;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 18;
return;
case 18:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2313;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 2314;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading :\" & cursor4.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading :"+parent._cursor4.GetString("product_description")));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2316;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("756229904","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2317;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2318;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2319;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2320;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 19;
return;
case 19:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 17;
;
 //BA.debugLineNum = 2322;BA.debugLine="js.Release";
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
this.state = -1;
;
 //BA.debugLineNum = 2327;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 2328;BA.debugLine="DELETE_DISPATCH";
_delete_dispatch();
 //BA.debugLineNum = 2329;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_picklist_loading() throws Exception{
ResumableSub_INSERT_PICKLIST_LOADING rsub = new ResumableSub_INSERT_PICKLIST_LOADING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_PICKLIST_LOADING extends BA.ResumableSub {
public ResumableSub_INSERT_PICKLIST_LOADING(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 2216;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 2218;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_loading_disc_table WHERE picklist_id = '"+parent._picklist_id.trim()+"' GROUP BY product_id, unit")));
 //BA.debugLineNum = 2219;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2220;BA.debugLine="For i3 = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor3.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 16;
if (true) break;

case 16:
//C
this.state = 13;
if ((step4 > 0 && _i3 <= limit4) || (step4 < 0 && _i3 >= limit4)) this.state = 6;
if (true) break;

case 17:
//C
this.state = 16;
_i3 = ((int)(0 + _i3 + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2221;BA.debugLine="cursor3.Position = i3";
parent._cursor3.setPosition(_i3);
 //BA.debugLineNum = 2222;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_pi";
_cmd = _createcommand("insert_picklist_loaded",(Object[])(new String[]{parent._cursor3.GetString("picklist_id"),parent._cursor3.GetString("picklist_name"),parent._cursor3.GetString("picklist_date"),parent._cursor3.GetString("principal_id"),parent._cursor3.GetString("principal_name"),parent._cursor3.GetString("product_id"),parent._cursor3.GetString("product_variant"),parent._cursor3.GetString("product_description"),parent._cursor3.GetString("unit"),parent._cursor3.GetString("quantity"),parent._cursor3.GetString("loaded_quantity"),parent._cursor3.GetString("loaded_total_pcs"),parent._cursor3.GetString("loaded_status"),parent._cursor3.GetString("load_by"),parent._cursor3.GetString("load_date"),parent._cursor3.GetString("load_time"),parent._cursor3.GetString("scan_code"),parent._cursor3.GetString("reason")}));
 //BA.debugLineNum = 2226;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2227;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 18;
return;
case 18:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2228;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 2229;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading :\" & cursor3.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading :"+parent._cursor3.GetString("product_description")));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2231;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("756033296","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2232;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2233;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2234;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2235;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 19;
return;
case 19:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 17;
;
 //BA.debugLineNum = 2237;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 2239;BA.debugLine="CHECK_PICKLIST_LOADED";
_check_picklist_loaded();
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 2241;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_loading_header() throws Exception{
 //BA.debugLineNum = 860;BA.debugLine="Sub LOAD_LOADING_HEADER";
 //BA.debugLineNum = 861;BA.debugLine="NameColumn(0)=LOADING_TABLE.AddColumn(\"Status\", L";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._loading_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Status",mostCurrent._loading_table._column_type_text /*int*/ );
 //BA.debugLineNum = 862;BA.debugLine="NameColumn(1)=LOADING_TABLE.AddColumn(\"Product Va";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._loading_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._loading_table._column_type_text /*int*/ );
 //BA.debugLineNum = 863;BA.debugLine="NameColumn(2)=LOADING_TABLE.AddColumn(\"Product De";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._loading_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._loading_table._column_type_text /*int*/ );
 //BA.debugLineNum = 864;BA.debugLine="NameColumn(3)=LOADING_TABLE.AddColumn(\"Unit\", LOA";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._loading_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Unit",mostCurrent._loading_table._column_type_text /*int*/ );
 //BA.debugLineNum = 865;BA.debugLine="NameColumn(4)=LOADING_TABLE.AddColumn(\"Served\", L";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._loading_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Served",mostCurrent._loading_table._column_type_text /*int*/ );
 //BA.debugLineNum = 866;BA.debugLine="NameColumn(5)=LOADING_TABLE.AddColumn(\"Loaded\", L";
mostCurrent._namecolumn[(int) (5)] = mostCurrent._loading_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Loaded",mostCurrent._loading_table._column_type_text /*int*/ );
 //BA.debugLineNum = 867;BA.debugLine="End Sub";
return "";
}
public static void  _load_loading_picklist() throws Exception{
ResumableSub_LOAD_LOADING_PICKLIST rsub = new ResumableSub_LOAD_LOADING_PICKLIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_LOADING_PICKLIST extends BA.ResumableSub {
public ResumableSub_LOAD_LOADING_PICKLIST(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 869;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 1;
;
 //BA.debugLineNum = 870;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 871;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 1;
;
 //BA.debugLineNum = 872;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 873;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 875;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT a.*, b.loa";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.loaded_status , b.loaded_quantity FROM picklist_loading_ref_table as a LEFT OUTER JOIN picklist_loading_disc_table as b ON "+"a.picklist_id = b.picklist_id And a.product_id = b.product_id And a.unit = b.unit "+"WHERE a.picklist_id = '"+parent._picklist_id+"' "+"ORDER BY a.product_variant ASC")));
 //BA.debugLineNum = 879;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 16;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 880;BA.debugLine="For ia = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step8 = 1;
limit8 = (int) (parent._cursor4.getRowCount()-1);
_ia = (int) (0) ;
this.state = 23;
if (true) break;

case 23:
//C
this.state = 13;
if ((step8 > 0 && _ia <= limit8) || (step8 < 0 && _ia >= limit8)) this.state = 6;
if (true) break;

case 24:
//C
this.state = 23;
_ia = ((int)(0 + _ia + step8)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 881;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 25;
return;
case 25:
//C
this.state = 7;
;
 //BA.debugLineNum = 882;BA.debugLine="cursor4.Position = ia";
parent._cursor4.setPosition(_ia);
 //BA.debugLineNum = 883;BA.debugLine="If cursor4.GetString(\"loaded_status\") = Null Or";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor4.GetString("loaded_status")== null || (parent._cursor4.GetString("loaded_status")).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 884;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 26;
return;
case 26:
//C
this.state = 12;
;
 //BA.debugLineNum = 885;BA.debugLine="Dim row(6) As Object";
_row = new Object[(int) (6)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 886;BA.debugLine="row(0) = \"UNLOADED\"";
_row[(int) (0)] = (Object)("UNLOADED");
 //BA.debugLineNum = 887;BA.debugLine="row(1) = cursor4.GetString(\"product_variant\")";
_row[(int) (1)] = (Object)(parent._cursor4.GetString("product_variant"));
 //BA.debugLineNum = 888;BA.debugLine="row(2) = cursor4.GetString(\"product_descriptio";
_row[(int) (2)] = (Object)(parent._cursor4.GetString("product_description"));
 //BA.debugLineNum = 889;BA.debugLine="row(3) = cursor4.GetString(\"unit\")";
_row[(int) (3)] = (Object)(parent._cursor4.GetString("unit"));
 //BA.debugLineNum = 890;BA.debugLine="row(4) = cursor4.GetString(\"quantity\")";
_row[(int) (4)] = (Object)(parent._cursor4.GetString("quantity"));
 //BA.debugLineNum = 891;BA.debugLine="row(5) = 0";
_row[(int) (5)] = (Object)(0);
 //BA.debugLineNum = 892;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 894;BA.debugLine="Dim row(6) As Object";
_row = new Object[(int) (6)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 895;BA.debugLine="row(0) = cursor4.GetString(\"loaded_status\")";
_row[(int) (0)] = (Object)(parent._cursor4.GetString("loaded_status"));
 //BA.debugLineNum = 896;BA.debugLine="row(1) = cursor4.GetString(\"product_variant\")";
_row[(int) (1)] = (Object)(parent._cursor4.GetString("product_variant"));
 //BA.debugLineNum = 897;BA.debugLine="row(2) = cursor4.GetString(\"product_descriptio";
_row[(int) (2)] = (Object)(parent._cursor4.GetString("product_description"));
 //BA.debugLineNum = 898;BA.debugLine="row(3) = cursor4.GetString(\"unit\")";
_row[(int) (3)] = (Object)(parent._cursor4.GetString("unit"));
 //BA.debugLineNum = 899;BA.debugLine="row(4) = cursor4.GetString(\"quantity\")";
_row[(int) (4)] = (Object)(parent._cursor4.GetString("quantity"));
 //BA.debugLineNum = 900;BA.debugLine="row(5) = cursor4.GetString(\"loaded_quantity\")";
_row[(int) (5)] = (Object)(parent._cursor4.GetString("loaded_quantity"));
 //BA.debugLineNum = 901;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 12:
//C
this.state = 24;
;
 if (true) break;
if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 907;BA.debugLine="LOADING_TABLE.NumberOfFrozenColumns = 1";
parent.mostCurrent._loading_table._numberoffrozencolumns /*int*/  = (int) (1);
 //BA.debugLineNum = 908;BA.debugLine="LOADING_TABLE.RowHeight = 50dip";
parent.mostCurrent._loading_table._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 909;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 27;
return;
case 27:
//C
this.state = 16;
;
 //BA.debugLineNum = 910;BA.debugLine="GET_STATUS";
_get_status();
 //BA.debugLineNum = 911;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 16;
;
 //BA.debugLineNum = 912;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 916;BA.debugLine="ToastMessageShow(\"Picklist is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Picklist is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = 17;
;
 //BA.debugLineNum = 918;BA.debugLine="LOADING_TABLE.SetData(Data)";
parent.mostCurrent._loading_table._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 920;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 17:
//if
this.state = 20;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 19;
}if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 921;BA.debugLine="XSelections.Initialize(LOADING_TABLE)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._loading_table);
 //BA.debugLineNum = 922;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 924;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = -1;
;
 //BA.debugLineNum = 925;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_reason() throws Exception{
 //BA.debugLineNum = 1924;BA.debugLine="Sub LOAD_REASON";
 //BA.debugLineNum = 1925;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1926;BA.debugLine="CMB_REASON.cmbBox.Clear";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1927;BA.debugLine="CMB_REASON.cmbBox.Add(\"WRONG SELECTION\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WRONG SELECTION");
 //BA.debugLineNum = 1928;BA.debugLine="CMB_REASON.cmbBox.Add(\"WRONG SIZE\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WRONG SIZE");
 //BA.debugLineNum = 1929;BA.debugLine="CMB_REASON.cmbBox.Add(\"WRONG FLAVOR/COLOR\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WRONG FLAVOR/COLOR");
 //BA.debugLineNum = 1930;BA.debugLine="CMB_REASON.cmbBox.Add(\"WRONG SKU\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WRONG SKU");
 //BA.debugLineNum = 1931;BA.debugLine="CMB_REASON.cmbBox.Add(\"WRONG SCAN\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WRONG SCAN");
 //BA.debugLineNum = 1932;BA.debugLine="End Sub";
return "";
}
public static void  _loading_bag() throws Exception{
ResumableSub_LOADING_BAG rsub = new ResumableSub_LOADING_BAG(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_BAG extends BA.ResumableSub {
public ResumableSub_LOADING_BAG(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_status = "";
String _speech1 = "";
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
 //BA.debugLineNum = 1625;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1626;BA.debugLine="total_pieces = EDITTEXT_BAG_QTY_LOADING.Text * ba";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent.mostCurrent._edittext_bag_qty_loading.getText()))*(double)(Double.parseDouble(parent._bagper)));
 //BA.debugLineNum = 1628;BA.debugLine="Dim LOADING_status As String";
_loading_status = "";
 //BA.debugLineNum = 1630;BA.debugLine="If EDITTEXT_BAG_QTY_LOADING.Text <> LABEL_BAG_QTY";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._edittext_bag_qty_loading.getText()).equals(parent.mostCurrent._label_bag_qty_loading.getText()) == false) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1631;BA.debugLine="LOADING_status = \"ON HOLD\"";
_loading_status = "ON HOLD";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1633;BA.debugLine="LOADING_status = \"LOADED\"";
_loading_status = "LOADED";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1636;BA.debugLine="Dim SPEECH1 As String";
_speech1 = "";
 //BA.debugLineNum = 1637;BA.debugLine="If BUTTON_BAG_LOADING.Text = \"LOADED\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent.mostCurrent._button_bag_loading.getText()).equals("LOADED")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1638;BA.debugLine="SPEECH1 = \"LOADED\"";
_speech1 = "LOADED";
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1640;BA.debugLine="SPEECH1 = \"UPDATED\"";
_speech1 = "UPDATED";
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1642;BA.debugLine="Dim query As String = \"DELETE FROM picklist_LOADI";
_query = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?";
 //BA.debugLineNum = 1643;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_desc_loading.getText(),"BAG"}));
 //BA.debugLineNum = 1644;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 1645;BA.debugLine="Dim query As String = \"INSERT INTO picklist_LOADI";
_query = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1646;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),"BAG",parent.mostCurrent._label_bag_qty_loading.getText(),parent.mostCurrent._edittext_bag_qty_loading.getText(),parent._total_pieces,_loading_status,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._scan_code,parent._reason,parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 1649;BA.debugLine="ToastMessageShow(EDITTEXT_BAG_QTY_LOADING.Text &";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent.mostCurrent._edittext_bag_qty_loading.getText()+" "+"BAG "+_speech1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1650;BA.debugLine="TTS1.Speak(EDITTEXT_BAG_QTY_LOADING.Text & \" \" &";
parent._tts1.Speak(parent.mostCurrent._edittext_bag_qty_loading.getText()+" "+"BAG "+_speech1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1651;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 1652;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1653;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1654;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1655;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1656;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _loading_box() throws Exception{
ResumableSub_LOADING_BOX rsub = new ResumableSub_LOADING_BOX(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_BOX extends BA.ResumableSub {
public ResumableSub_LOADING_BOX(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_status = "";
String _speech1 = "";
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
 //BA.debugLineNum = 1528;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1529;BA.debugLine="total_pieces = EDITTEXT_BOX_QTY_LOADING.Text * bo";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent.mostCurrent._edittext_box_qty_loading.getText()))*(double)(Double.parseDouble(parent._boxper)));
 //BA.debugLineNum = 1531;BA.debugLine="Dim LOADING_status As String";
_loading_status = "";
 //BA.debugLineNum = 1533;BA.debugLine="If EDITTEXT_BOX_QTY_LOADING.Text <> LABEL_BOX_QTY";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._edittext_box_qty_loading.getText()).equals(parent.mostCurrent._label_box_qty_loading.getText()) == false) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1534;BA.debugLine="LOADING_status = \"ON HOLD\"";
_loading_status = "ON HOLD";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1536;BA.debugLine="LOADING_status = \"LOADED\"";
_loading_status = "LOADED";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1538;BA.debugLine="Dim SPEECH1 As String";
_speech1 = "";
 //BA.debugLineNum = 1539;BA.debugLine="If BUTTON_BOX_LOADING.Text = \"LOAD\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent.mostCurrent._button_box_loading.getText()).equals("LOAD")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1540;BA.debugLine="SPEECH1 = \"LOADED\"";
_speech1 = "LOADED";
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1542;BA.debugLine="SPEECH1 = \"UPDATED\"";
_speech1 = "UPDATED";
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1544;BA.debugLine="Dim query As String = \"DELETE FROM picklist_LOADI";
_query = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?";
 //BA.debugLineNum = 1545;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_desc_loading.getText(),"BOX"}));
 //BA.debugLineNum = 1546;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 1547;BA.debugLine="Dim query As String = \"INSERT INTO picklist_LOADI";
_query = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1548;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),"BOX",parent.mostCurrent._label_box_qty_loading.getText(),parent.mostCurrent._edittext_box_qty_loading.getText(),parent._total_pieces,_loading_status,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._scan_code,parent._reason,parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 1551;BA.debugLine="ToastMessageShow(EDITTEXT_BOX_QTY_LOADING.Text &";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent.mostCurrent._edittext_box_qty_loading.getText()+" "+"BOX "+_speech1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1552;BA.debugLine="TTS1.Speak(EDITTEXT_BOX_QTY_LOADING.Text & \" \" &";
parent._tts1.Speak(parent.mostCurrent._edittext_box_qty_loading.getText()+" "+"BOX "+_speech1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1554;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 1555;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1556;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1557;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1558;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1559;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _loading_case() throws Exception{
ResumableSub_LOADING_CASE rsub = new ResumableSub_LOADING_CASE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_CASE extends BA.ResumableSub {
public ResumableSub_LOADING_CASE(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_status = "";
String _speech1 = "";
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
 //BA.debugLineNum = 1494;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1495;BA.debugLine="total_pieces = EDITTEXT_CASE_QTY_LOADING.Text * c";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent.mostCurrent._edittext_case_qty_loading.getText()))*(double)(Double.parseDouble(parent._caseper)));
 //BA.debugLineNum = 1497;BA.debugLine="Dim LOADING_status As String";
_loading_status = "";
 //BA.debugLineNum = 1499;BA.debugLine="If EDITTEXT_CASE_QTY_LOADING.Text <> LABEL_CASE_Q";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._edittext_case_qty_loading.getText()).equals(parent.mostCurrent._label_case_qty_loading.getText()) == false) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1500;BA.debugLine="LOADING_status = \"ON HOLD\"";
_loading_status = "ON HOLD";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1502;BA.debugLine="LOADING_status = \"LOADED\"";
_loading_status = "LOADED";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1505;BA.debugLine="Dim SPEECH1 As String";
_speech1 = "";
 //BA.debugLineNum = 1506;BA.debugLine="If BUTTON_CASE_LOADING.Text = \"LOAD\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent.mostCurrent._button_case_loading.getText()).equals("LOAD")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1507;BA.debugLine="SPEECH1 = \"LOADED\"";
_speech1 = "LOADED";
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1509;BA.debugLine="SPEECH1 = \"UPDATED\"";
_speech1 = "UPDATED";
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1512;BA.debugLine="Dim query As String = \"DELETE FROM picklist_LOADI";
_query = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?";
 //BA.debugLineNum = 1513;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_desc_loading.getText(),"CASE"}));
 //BA.debugLineNum = 1514;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 1515;BA.debugLine="Dim query As String = \"INSERT INTO picklist_LOADI";
_query = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1516;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),"CASE",parent.mostCurrent._label_case_qty_loading.getText(),parent.mostCurrent._edittext_case_qty_loading.getText(),parent._total_pieces,_loading_status,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._scan_code,parent._reason,parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 1519;BA.debugLine="ToastMessageShow(EDITTEXT_CASE_QTY_LOADING.Text &";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent.mostCurrent._edittext_case_qty_loading.getText()+" "+"CASE "+_speech1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1520;BA.debugLine="TTS1.Speak(EDITTEXT_CASE_QTY_LOADING.Text & \" \" &";
parent._tts1.Speak(parent.mostCurrent._edittext_case_qty_loading.getText()+" "+"CASE "+_speech1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1521;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 1522;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1523;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1524;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1525;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1526;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _loading_doz() throws Exception{
ResumableSub_LOADING_DOZ rsub = new ResumableSub_LOADING_DOZ(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_DOZ extends BA.ResumableSub {
public ResumableSub_LOADING_DOZ(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_status = "";
String _speech1 = "";
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
 //BA.debugLineNum = 1561;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1562;BA.debugLine="total_pieces = EDITTEXT_DOZ_QTY_LOADING.Text * do";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent.mostCurrent._edittext_doz_qty_loading.getText()))*(double)(Double.parseDouble(parent._dozper)));
 //BA.debugLineNum = 1564;BA.debugLine="Dim LOADING_status As String";
_loading_status = "";
 //BA.debugLineNum = 1566;BA.debugLine="If EDITTEXT_DOZ_QTY_LOADING.Text <> LABEL_DOZ_QTY";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._edittext_doz_qty_loading.getText()).equals(parent.mostCurrent._label_doz_qty_loading.getText()) == false) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1567;BA.debugLine="LOADING_status = \"ON HOLD\"";
_loading_status = "ON HOLD";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1569;BA.debugLine="LOADING_status = \"LOADED\"";
_loading_status = "LOADED";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1571;BA.debugLine="Dim SPEECH1 As String";
_speech1 = "";
 //BA.debugLineNum = 1572;BA.debugLine="If BUTTON_BOX_LOADING.Text = \"LOADED\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent.mostCurrent._button_box_loading.getText()).equals("LOADED")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1573;BA.debugLine="SPEECH1 = \"LOADING\"";
_speech1 = "LOADING";
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1575;BA.debugLine="SPEECH1 = \"UPDATED\"";
_speech1 = "UPDATED";
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1577;BA.debugLine="Dim query As String = \"DELETE FROM picklist_LOADI";
_query = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?";
 //BA.debugLineNum = 1578;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_desc_loading.getText(),"DOZ"}));
 //BA.debugLineNum = 1579;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 1580;BA.debugLine="Dim query As String = \"INSERT INTO picklist_LOADI";
_query = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1581;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),"DOZ",parent.mostCurrent._label_doz_qty_loading.getText(),parent.mostCurrent._edittext_doz_qty_loading.getText(),parent._total_pieces,_loading_status,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._scan_code,parent._reason,parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 1584;BA.debugLine="ToastMessageShow(EDITTEXT_DOZ_QTY_LOADING.Text &";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent.mostCurrent._edittext_doz_qty_loading.getText()+" "+"DOZEN "+_speech1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1585;BA.debugLine="TTS1.Speak(EDITTEXT_DOZ_QTY_LOADING.Text & \" \" &";
parent._tts1.Speak(parent.mostCurrent._edittext_doz_qty_loading.getText()+" "+"DOZEN "+_speech1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1586;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 1587;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1588;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1589;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1590;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1591;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _loading_pack() throws Exception{
ResumableSub_LOADING_PACK rsub = new ResumableSub_LOADING_PACK(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_PACK extends BA.ResumableSub {
public ResumableSub_LOADING_PACK(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_status = "";
String _speech1 = "";
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
 //BA.debugLineNum = 1593;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1594;BA.debugLine="total_pieces = EDITTEXT_PACK_QTY_LOADING.Text * p";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent.mostCurrent._edittext_pack_qty_loading.getText()))*(double)(Double.parseDouble(parent._packper)));
 //BA.debugLineNum = 1596;BA.debugLine="Dim LOADING_status As String";
_loading_status = "";
 //BA.debugLineNum = 1598;BA.debugLine="If EDITTEXT_PACK_QTY_LOADING.Text <> LABEL_PACK_Q";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._edittext_pack_qty_loading.getText()).equals(parent.mostCurrent._label_pack_qty_loading.getText()) == false) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1599;BA.debugLine="LOADING_status = \"ON HOLD\"";
_loading_status = "ON HOLD";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1601;BA.debugLine="LOADING_status = \"LOADED\"";
_loading_status = "LOADED";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1603;BA.debugLine="Dim SPEECH1 As String";
_speech1 = "";
 //BA.debugLineNum = 1604;BA.debugLine="If BUTTON_PACK_LOADING.Text = \"LOAD\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent.mostCurrent._button_pack_loading.getText()).equals("LOAD")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1605;BA.debugLine="SPEECH1 = \"LOADED\"";
_speech1 = "LOADED";
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1607;BA.debugLine="SPEECH1 = \"UPDATED\"";
_speech1 = "UPDATED";
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1609;BA.debugLine="Dim query As String = \"DELETE FROM picklist_LOADI";
_query = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?";
 //BA.debugLineNum = 1610;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_desc_loading.getText(),"PACK"}));
 //BA.debugLineNum = 1611;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 1612;BA.debugLine="Dim query As String = \"INSERT INTO picklist_LOADI";
_query = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1613;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),"PACK",parent.mostCurrent._label_pack_qty_loading.getText(),parent.mostCurrent._edittext_pack_qty_loading.getText(),parent._total_pieces,_loading_status,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._scan_code,parent._reason,parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 1616;BA.debugLine="ToastMessageShow(EDITTEXT_PACK_QTY_LOADING.Text &";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent.mostCurrent._edittext_pack_qty_loading.getText()+" "+"PACK "+_speech1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1617;BA.debugLine="TTS1.Speak(EDITTEXT_PACK_QTY_LOADING.Text & \" \" &";
parent._tts1.Speak(parent.mostCurrent._edittext_pack_qty_loading.getText()+" "+"PACK "+_speech1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1618;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 1619;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1620;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1621;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1622;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1623;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _loading_pcs() throws Exception{
ResumableSub_LOADING_PCS rsub = new ResumableSub_LOADING_PCS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_PCS extends BA.ResumableSub {
public ResumableSub_LOADING_PCS(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_status = "";
String _speech1 = "";
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
 //BA.debugLineNum = 1462;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1463;BA.debugLine="total_pieces = EDITTEXT_PCS_QTY_LOADING.Text * pc";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent.mostCurrent._edittext_pcs_qty_loading.getText()))*(double)(Double.parseDouble(parent._pcsper)));
 //BA.debugLineNum = 1465;BA.debugLine="Dim LOADING_status As String";
_loading_status = "";
 //BA.debugLineNum = 1467;BA.debugLine="If EDITTEXT_PCS_QTY_LOADING.Text <> LABEL_PCS_QTY";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._edittext_pcs_qty_loading.getText()).equals(parent.mostCurrent._label_pcs_qty_loading.getText()) == false) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1468;BA.debugLine="LOADING_status = \"ON HOLD\"";
_loading_status = "ON HOLD";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1470;BA.debugLine="LOADING_status = \"LOADED\"";
_loading_status = "LOADED";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 1472;BA.debugLine="Dim SPEECH1 As String";
_speech1 = "";
 //BA.debugLineNum = 1473;BA.debugLine="If BUTTON_PCS_LOADING.Text = \"LOAD\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent.mostCurrent._button_pcs_loading.getText()).equals("LOAD")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1474;BA.debugLine="SPEECH1 = \"LOADED\"";
_speech1 = "LOADED";
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1476;BA.debugLine="SPEECH1 = \"UPDATED\"";
_speech1 = "UPDATED";
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1478;BA.debugLine="Dim query As String = \"DELETE FROM picklist_LOADI";
_query = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?";
 //BA.debugLineNum = 1479;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_desc_loading.getText(),"PCS"}));
 //BA.debugLineNum = 1480;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 1481;BA.debugLine="Dim query As String = \"INSERT INTO picklist_LOADI";
_query = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1482;BA.debugLine="connection.ExecNonQuery2(query,Array As String(pi";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._picklist_id,parent.mostCurrent._label_load_name.getText(),parent.mostCurrent._label_load_date.getText(),parent._principal_id,parent._principal_name,parent._product_id,parent.mostCurrent._label_load_variant_loading.getText(),parent.mostCurrent._label_load_desc_loading.getText(),"PCS",parent.mostCurrent._label_pcs_qty_loading.getText(),parent.mostCurrent._edittext_pcs_qty_loading.getText(),parent._total_pieces,_loading_status,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._scan_code,parent._reason,parent.mostCurrent._login_module._tab_id /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 1485;BA.debugLine="ToastMessageShow(EDITTEXT_PCS_QTY_LOADING.Text &";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent.mostCurrent._edittext_pcs_qty_loading.getText()+" "+"PIECE(S) "+_speech1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1486;BA.debugLine="TTS1.Speak(EDITTEXT_PCS_QTY_LOADING.Text & \" \" &";
parent._tts1.Speak(parent.mostCurrent._edittext_pcs_qty_loading.getText()+" "+"PIECES "+_speech1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1487;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 1488;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1489;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1490;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1491;BA.debugLine="scan_next_trigger = 0";
parent._scan_next_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1492;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _loading_table_cellclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_LOADING_TABLE_CellClicked rsub = new ResumableSub_LOADING_TABLE_CellClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOADING_TABLE_CellClicked extends BA.ResumableSub {
public ResumableSub_LOADING_TABLE_CellClicked(wingan.app.loading_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.loading_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
int _arow = 0;
int step25;
int limit25;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 987;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 989;BA.debugLine="Dim RowData As Map = LOADING_TABLE.GetRow(RowId)";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._loading_table._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 990;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 991;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 992;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED IN THE SYSTEM\")";
_ls.Add((Object)("BARCODE NOT REGISTERED IN THE SYSTEM"));
 //BA.debugLineNum = 993;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 994;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 995;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 996;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 997;BA.debugLine="ls.Add(\"FREE ITEM\")";
_ls.Add((Object)("FREE ITEM"));
 //BA.debugLineNum = 998;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 999;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 41;
return;
case 41:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 1000;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 40;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}else {
this.state = 39;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1001;BA.debugLine="ProgressDialogShow2(\"Loading...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1002;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 1003;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 1004;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 4;
;
 //BA.debugLineNum = 1005;BA.debugLine="LABEL_LOAD_DESC_LOADING.Text = RowData.Get(\"Prod";
parent.mostCurrent._label_load_desc_loading.setText(BA.ObjectToCharSequence(_rowdata.Get((Object)("Product Description"))));
 //BA.debugLineNum = 1006;BA.debugLine="CLEAR_PICKLIST_SKU";
_clear_picklist_sku();
 //BA.debugLineNum = 1007;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 4;
;
 //BA.debugLineNum = 1008;BA.debugLine="GET_PRODUCT_DETAILS";
_get_product_details();
 //BA.debugLineNum = 1009;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 44;
return;
case 44:
//C
this.state = 4;
;
 //BA.debugLineNum = 1010;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM pi";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_loading_ref_table WHERE product_description ='"+parent.mostCurrent._label_load_desc_loading.getText()+"' AND picklist_id = '"+parent._picklist_id+"'")));
 //BA.debugLineNum = 1011;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 37;
if (parent._cursor4.getRowCount()>0) { 
this.state = 6;
}else {
this.state = 36;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1012;BA.debugLine="For arow = 0 To cursor4.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 34;
step25 = 1;
limit25 = (int) (parent._cursor4.getRowCount()-1);
_arow = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 34;
if ((step25 > 0 && _arow <= limit25) || (step25 < 0 && _arow >= limit25)) this.state = 9;
if (true) break;

case 46:
//C
this.state = 45;
_arow = ((int)(0 + _arow + step25)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1013;BA.debugLine="cursor4.Position = arow";
parent._cursor4.setPosition(_arow);
 //BA.debugLineNum = 1014;BA.debugLine="LABEL_LOAD_VARIANT_LOADING.Text = cursor4.GetS";
parent.mostCurrent._label_load_variant_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("product_variant")));
 //BA.debugLineNum = 1015;BA.debugLine="If cursor4.GetString(\"unit\") = \"CASE\" Then";
if (true) break;

case 10:
//if
this.state = 13;
if ((parent._cursor4.GetString("unit")).equals("CASE")) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1016;BA.debugLine="LABEL_CASE_QTY_LOADING.Text = cursor4.GetStri";
parent.mostCurrent._label_case_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1017;BA.debugLine="EDITTEXT_CASE_QTY_LOADING.Text = cursor4.GetS";
parent.mostCurrent._edittext_case_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1018;BA.debugLine="BUTTON_CASE_LOADING.Enabled = True";
parent.mostCurrent._button_case_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1019;BA.debugLine="BUTTON_CASE_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_case_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 1021;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 14;
;
 //BA.debugLineNum = 1022;BA.debugLine="If cursor4.GetString(\"unit\") = \"PCS\" Then";
if (true) break;

case 14:
//if
this.state = 17;
if ((parent._cursor4.GetString("unit")).equals("PCS")) { 
this.state = 16;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1023;BA.debugLine="LABEL_PCS_QTY_LOADING.Text = cursor4.GetStrin";
parent.mostCurrent._label_pcs_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1024;BA.debugLine="EDITTEXT_PCS_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._edittext_pcs_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1025;BA.debugLine="BUTTON_PCS_LOADING.Enabled = True";
parent.mostCurrent._button_pcs_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1026;BA.debugLine="BUTTON_PCS_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_pcs_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 1028;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 48;
return;
case 48:
//C
this.state = 18;
;
 //BA.debugLineNum = 1029;BA.debugLine="If cursor4.GetString(\"unit\") = \"BOX\" Then";
if (true) break;

case 18:
//if
this.state = 21;
if ((parent._cursor4.GetString("unit")).equals("BOX")) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 1030;BA.debugLine="LABEL_BOX_QTY_LOADING.Text = cursor4.GetStrin";
parent.mostCurrent._label_box_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1031;BA.debugLine="EDITTEXT_BOX_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._edittext_box_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1032;BA.debugLine="BUTTON_BOX_LOADING.Enabled = True";
parent.mostCurrent._button_box_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1033;BA.debugLine="BUTTON_BOX_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_box_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 21:
//C
this.state = 22;
;
 //BA.debugLineNum = 1035;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 49;
return;
case 49:
//C
this.state = 22;
;
 //BA.debugLineNum = 1036;BA.debugLine="If cursor4.GetString(\"unit\") = \"DOZ\" Then";
if (true) break;

case 22:
//if
this.state = 25;
if ((parent._cursor4.GetString("unit")).equals("DOZ")) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1037;BA.debugLine="LABEL_DOZ_QTY_LOADING.Text = cursor4.GetStrin";
parent.mostCurrent._label_doz_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1038;BA.debugLine="EDITTEXT_DOZ_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._edittext_doz_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1039;BA.debugLine="BUTTON_DOZ_LOADING.Enabled = True";
parent.mostCurrent._button_doz_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1040;BA.debugLine="BUTTON_DOZ_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_doz_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 1042;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 50;
return;
case 50:
//C
this.state = 26;
;
 //BA.debugLineNum = 1043;BA.debugLine="If cursor4.GetString(\"unit\") = \"PACK\" Then";
if (true) break;

case 26:
//if
this.state = 29;
if ((parent._cursor4.GetString("unit")).equals("PACK")) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 1044;BA.debugLine="LABEL_PACK_QTY_LOADING.Text = cursor4.GetStri";
parent.mostCurrent._label_pack_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1045;BA.debugLine="EDITTEXT_PACK_QTY_LOADING.Text = cursor4.GetS";
parent.mostCurrent._edittext_pack_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1046;BA.debugLine="BUTTON_PACK_LOADING.Enabled = True";
parent.mostCurrent._button_pack_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1047;BA.debugLine="BUTTON_PACK_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_pack_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 1049;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 51;
return;
case 51:
//C
this.state = 30;
;
 //BA.debugLineNum = 1050;BA.debugLine="If cursor4.GetString(\"unit\") = \"BAG\" Then";
if (true) break;

case 30:
//if
this.state = 33;
if ((parent._cursor4.GetString("unit")).equals("BAG")) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1051;BA.debugLine="LABEL_BAG_QTY_LOADING.Text = cursor4.GetStrin";
parent.mostCurrent._label_bag_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1052;BA.debugLine="EDITTEXT_BAG_QTY_LOADING.Text = cursor4.GetSt";
parent.mostCurrent._edittext_bag_qty_loading.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1053;BA.debugLine="BUTTON_BAG_LOADING.Enabled = True";
parent.mostCurrent._button_bag_loading.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1054;BA.debugLine="BUTTON_BAG_LOADING.Background = BG_ENABLE";
parent.mostCurrent._button_bag_loading.setBackground((android.graphics.drawable.Drawable)(parent.mostCurrent._bg_enable.getObject()));
 if (true) break;

case 33:
//C
this.state = 46;
;
 //BA.debugLineNum = 1056;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 52;
return;
case 52:
//C
this.state = 46;
;
 if (true) break;
if (true) break;

case 34:
//C
this.state = 37;
;
 //BA.debugLineNum = 1058;BA.debugLine="NOT_UNIT_TRIGGER";
_not_unit_trigger();
 //BA.debugLineNum = 1059;BA.debugLine="GET_TOTAL_SERVED";
_get_total_served();
 //BA.debugLineNum = 1060;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 53;
return;
case 53:
//C
this.state = 37;
;
 //BA.debugLineNum = 1061;BA.debugLine="PANEL_BG_LOADING.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_loading.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1062;BA.debugLine="PANEL_BG_LOADING.BringToFront";
parent.mostCurrent._panel_bg_loading.BringToFront();
 //BA.debugLineNum = 1063;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 54;
return;
case 54:
//C
this.state = 37;
;
 //BA.debugLineNum = 1064;BA.debugLine="ORDER_SPEECH";
_order_speech();
 //BA.debugLineNum = 1065;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 1067;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CRLF";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_desc_loading.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"IS NOT THE PICKLIST."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1068;BA.debugLine="CLEAR_PICKLIST_SKU";
_clear_picklist_sku();
 //BA.debugLineNum = 1069;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 55;
return;
case 55:
//C
this.state = 37;
;
 //BA.debugLineNum = 1070;BA.debugLine="PANEL_BG_LOADING.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_loading.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 37:
//C
this.state = 40;
;
 if (true) break;

case 39:
//C
this.state = 40;
 if (true) break;

case 40:
//C
this.state = -1;
;
 //BA.debugLineNum = 1077;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _loading_table_celllongclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
 //BA.debugLineNum = 1078;BA.debugLine="Sub LOADING_TABLE_CellLongClicked (ColumnId As Str";
 //BA.debugLineNum = 1079;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 1081;BA.debugLine="Dim RowData As Map = LOADING_TABLE.GetRow(RowId)";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._loading_table._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 1082;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 1084;BA.debugLine="End Sub";
return "";
}
public static String  _loading_table_dataupdated() throws Exception{
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
 //BA.debugLineNum = 926;BA.debugLine="Sub LOADING_TABLE_DataUpdated";
 //BA.debugLineNum = 927;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 928;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)]),(Object)(mostCurrent._namecolumn[(int) (5)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 930;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 931;BA.debugLine="Dim MaxHeight As Int";
_maxheight = 0;
 //BA.debugLineNum = 932;BA.debugLine="For i = 0 To LOADING_TABLE.VisibleRowIds.Size";
{
final int step5 = 1;
final int limit5 = mostCurrent._loading_table._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 933;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 934;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 935;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 937;BA.debugLine="MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.";
_maxheight = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxheight,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 940;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 941;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 942;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 947;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group16 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)]),(Object)(mostCurrent._namecolumn[(int) (5)])};
final int groupLen16 = group16.length
;int index16 = 0;
;
for (; index16 < groupLen16;index16++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group16[index16]);
 //BA.debugLineNum = 949;BA.debugLine="For i = 0 To LOADING_TABLE.VisibleRowIds.Size";
{
final int step17 = 1;
final int limit17 = mostCurrent._loading_table._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit17 ;_i = _i + step17 ) {
 //BA.debugLineNum = 950;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 951;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 952;BA.debugLine="lbl.Font = xui.CreateDefaultBoldFont(18)";
_lbl.setFont(mostCurrent._xui.CreateDefaultBoldFont((float) (18)));
 }
};
 }
};
 //BA.debugLineNum = 956;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group23 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)])};
final int groupLen23 = group23.length
;int index23 = 0;
;
for (; index23 < groupLen23;index23++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group23[index23]);
 //BA.debugLineNum = 957;BA.debugLine="Column.InternalSortMode= \"DESC\"";
_column.InternalSortMode /*String*/  = "DESC";
 }
};
 //BA.debugLineNum = 960;BA.debugLine="For i = 0 To LOADING_TABLE.VisibleRowIds.Size - 1";
{
final int step26 = 1;
final int limit26 = (int) (mostCurrent._loading_table._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit26 ;_i = _i + step26 ) {
 //BA.debugLineNum = 961;BA.debugLine="Dim RowId As Long = LOADING_TABLE.VisibleRowIds.";
_rowid = BA.ObjectToLongNumber(mostCurrent._loading_table._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 962;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 963;BA.debugLine="Dim pnl1 As B4XView = NameColumn(0).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (0)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 964;BA.debugLine="Dim row As Map = LOADING_TABLE.GetRow(RowId)";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._loading_table._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 965;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 966;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (0)].Id /*String*/ )));
 //BA.debugLineNum = 967;BA.debugLine="If OtherColumnValue = (\"UNLOADED\") Then";
if ((_othercolumnvalue).equals(("UNLOADED"))) { 
 //BA.debugLineNum = 968;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 }else if((_othercolumnvalue).equals(("LOADED"))) { 
 //BA.debugLineNum = 970;BA.debugLine="clr = xui.Color_Green";
_clr = mostCurrent._xui.Color_Green;
 }else {
 //BA.debugLineNum = 972;BA.debugLine="clr = xui.Color_Yellow";
_clr = mostCurrent._xui.Color_Yellow;
 };
 //BA.debugLineNum = 974;BA.debugLine="pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Co";
_pnl1.GetView((int) (0)).SetColorAndBorder(_clr,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)),(int) (0));
 };
 }
};
 //BA.debugLineNum = 981;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 982;BA.debugLine="LOADING_TABLE.Refresh";
mostCurrent._loading_table._refresh /*String*/ ();
 //BA.debugLineNum = 983;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 985;BA.debugLine="End Sub";
return "";
}
public static String  _not_unit_trigger() throws Exception{
 //BA.debugLineNum = 2429;BA.debugLine="Sub NOT_UNIT_TRIGGER";
 //BA.debugLineNum = 2430;BA.debugLine="If LABEL_LOAD_SCANCODE.Text = \"PCS\" And LABEL_PCS";
if ((mostCurrent._label_load_scancode.getText()).equals("PCS") && (mostCurrent._label_pcs_qty_loading.getText()).equals("0")) { 
 //BA.debugLineNum = 2431;BA.debugLine="unit_trigger = 1";
_unit_trigger = BA.NumberToString(1);
 }else if((mostCurrent._label_load_scancode.getText()).equals("CASE") && (mostCurrent._label_case_qty_loading.getText()).equals("0")) { 
 //BA.debugLineNum = 2433;BA.debugLine="unit_trigger = 1";
_unit_trigger = BA.NumberToString(1);
 }else if((mostCurrent._label_load_scancode.getText()).equals("BOX") && (mostCurrent._label_box_qty_loading.getText()).equals("0")) { 
 //BA.debugLineNum = 2435;BA.debugLine="unit_trigger = 1";
_unit_trigger = BA.NumberToString(1);
 }else if((mostCurrent._label_load_scancode.getText()).equals("DOZ") && (mostCurrent._label_doz_qty_loading.getText()).equals("0")) { 
 //BA.debugLineNum = 2437;BA.debugLine="unit_trigger = 1";
_unit_trigger = BA.NumberToString(1);
 }else if((mostCurrent._label_load_scancode.getText()).equals("BAG") && (mostCurrent._label_bag_qty_loading.getText()).equals("0")) { 
 //BA.debugLineNum = 2439;BA.debugLine="unit_trigger = 1";
_unit_trigger = BA.NumberToString(1);
 }else if((mostCurrent._label_load_scancode.getText()).equals("PACK") && (mostCurrent._label_pack_qty_loading.getText()).equals("0")) { 
 //BA.debugLineNum = 2441;BA.debugLine="unit_trigger = 1";
_unit_trigger = BA.NumberToString(1);
 }else if((mostCurrent._label_load_scancode.getText()).equals("-")) { 
 //BA.debugLineNum = 2443;BA.debugLine="unit_trigger = 0";
_unit_trigger = BA.NumberToString(0);
 }else {
 //BA.debugLineNum = 2445;BA.debugLine="unit_trigger = 0";
_unit_trigger = BA.NumberToString(0);
 };
 //BA.debugLineNum = 2447;BA.debugLine="End Sub";
return "";
}
public static void  _open_delivery() throws Exception{
ResumableSub_OPEN_DELIVERY rsub = new ResumableSub_OPEN_DELIVERY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_OPEN_DELIVERY extends BA.ResumableSub {
public ResumableSub_OPEN_DELIVERY(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 2066;BA.debugLine="PANEL_BG_DELIVERY.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_delivery.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2067;BA.debugLine="PANEL_BG_DELIVERY.BringToFront";
parent.mostCurrent._panel_bg_delivery.BringToFront();
 //BA.debugLineNum = 2068;BA.debugLine="EDITTEXT_PLATE.RequestFocus";
parent.mostCurrent._edittext_plate.RequestFocus();
 //BA.debugLineNum = 2069;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 2070;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PLATE)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_plate.getObject()));
 //BA.debugLineNum = 2071;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 277;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 278;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 279;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 280;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 281;BA.debugLine="End Sub";
return "";
}
public static void  _order_speech() throws Exception{
ResumableSub_ORDER_SPEECH rsub = new ResumableSub_ORDER_SPEECH(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ORDER_SPEECH extends BA.ResumableSub {
public ResumableSub_ORDER_SPEECH(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
String _loading_speech_pcs = "";
String _loading_speech_case = "";
String _loading_speech_box = "";
String _loading_speech_doz = "";
String _loading_speech_pack = "";
String _loading_speech_bag = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1266;BA.debugLine="Dim LOADING_SPEECH_PCS As String";
_loading_speech_pcs = "";
 //BA.debugLineNum = 1267;BA.debugLine="Dim LOADING_SPEECH_CASE As String";
_loading_speech_case = "";
 //BA.debugLineNum = 1268;BA.debugLine="Dim LOADING_SPEECH_BOX As String";
_loading_speech_box = "";
 //BA.debugLineNum = 1269;BA.debugLine="Dim LOADING_SPEECH_DOZ As String";
_loading_speech_doz = "";
 //BA.debugLineNum = 1270;BA.debugLine="Dim LOADING_SPEECH_PACK As String";
_loading_speech_pack = "";
 //BA.debugLineNum = 1271;BA.debugLine="Dim LOADING_SPEECH_BAG As String";
_loading_speech_bag = "";
 //BA.debugLineNum = 1273;BA.debugLine="If LABEL_CASE_QTY_LOADING.Text > 0 Then";
if (true) break;

case 1:
//if
this.state = 12;
if ((double)(Double.parseDouble(parent.mostCurrent._label_case_qty_loading.getText()))>0) { 
this.state = 3;
}else {
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1274;BA.debugLine="If LABEL_PCS_QTY_LOADING.Text > 0 Or _ 						LAB";
if (true) break;

case 4:
//if
this.state = 9;
if ((double)(Double.parseDouble(parent.mostCurrent._label_pcs_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))>0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1279;BA.debugLine="LOADING_SPEECH_CASE = LABEL_CASE_QTY_LOADING.Te";
_loading_speech_case = parent.mostCurrent._label_case_qty_loading.getText()+" CASE and ";
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1281;BA.debugLine="LOADING_SPEECH_CASE = LABEL_CASE_QTY_LOADING.Te";
_loading_speech_case = parent.mostCurrent._label_case_qty_loading.getText()+" CASE. ";
 if (true) break;

case 9:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1284;BA.debugLine="LOADING_SPEECH_CASE = \" \"";
_loading_speech_case = " ";
 if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 1286;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 67;
return;
case 67:
//C
this.state = 13;
;
 //BA.debugLineNum = 1287;BA.debugLine="If LABEL_PCS_QTY_LOADING.Text > 0 Then";
if (true) break;

case 13:
//if
this.state = 24;
if ((double)(Double.parseDouble(parent.mostCurrent._label_pcs_qty_loading.getText()))>0) { 
this.state = 15;
}else {
this.state = 23;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1288;BA.debugLine="If LABEL_BOX_QTY_LOADING.Text > 0 Or _ 						LAB";
if (true) break;

case 16:
//if
this.state = 21;
if ((double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))>0) { 
this.state = 18;
}else {
this.state = 20;
}if (true) break;

case 18:
//C
this.state = 21;
 //BA.debugLineNum = 1292;BA.debugLine="LOADING_SPEECH_PCS = LABEL_PCS_QTY_LOADING.Text";
_loading_speech_pcs = parent.mostCurrent._label_pcs_qty_loading.getText()+" PIECES and ";
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 1294;BA.debugLine="LOADING_SPEECH_PCS = LABEL_PCS_QTY_LOADING.Text";
_loading_speech_pcs = parent.mostCurrent._label_pcs_qty_loading.getText()+" PIECES. ";
 if (true) break;

case 21:
//C
this.state = 24;
;
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1297;BA.debugLine="LOADING_SPEECH_PCS = \" \"";
_loading_speech_pcs = " ";
 if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 1299;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 25;
;
 //BA.debugLineNum = 1300;BA.debugLine="If LABEL_BOX_QTY_LOADING.Text > 0 Then";
if (true) break;

case 25:
//if
this.state = 36;
if ((double)(Double.parseDouble(parent.mostCurrent._label_box_qty_loading.getText()))>0) { 
this.state = 27;
}else {
this.state = 35;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1302;BA.debugLine="If LABEL_DOZ_QTY_LOADING.Text > 0 Or _ 						LAB";
if (true) break;

case 28:
//if
this.state = 33;
if ((double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))>0) { 
this.state = 30;
}else {
this.state = 32;
}if (true) break;

case 30:
//C
this.state = 33;
 //BA.debugLineNum = 1305;BA.debugLine="LOADING_SPEECH_BOX = LABEL_BOX_QTY_LOADING.Text";
_loading_speech_box = parent.mostCurrent._label_box_qty_loading.getText()+" BOX and ";
 if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1307;BA.debugLine="LOADING_SPEECH_BOX = LABEL_BOX_QTY_LOADING.Text";
_loading_speech_box = parent.mostCurrent._label_box_qty_loading.getText()+" BOX. ";
 if (true) break;

case 33:
//C
this.state = 36;
;
 if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 1310;BA.debugLine="LOADING_SPEECH_BOX = \" \"";
_loading_speech_box = " ";
 if (true) break;

case 36:
//C
this.state = 37;
;
 //BA.debugLineNum = 1312;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 37;
;
 //BA.debugLineNum = 1313;BA.debugLine="If LABEL_DOZ_QTY_LOADING.Text > 0 Then";
if (true) break;

case 37:
//if
this.state = 48;
if ((double)(Double.parseDouble(parent.mostCurrent._label_doz_qty_loading.getText()))>0) { 
this.state = 39;
}else {
this.state = 47;
}if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 1314;BA.debugLine="If LABEL_PACK_QTY_LOADING.Text > 0 Or _ 						LA";
if (true) break;

case 40:
//if
this.state = 45;
if ((double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))>0 || (double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))>0) { 
this.state = 42;
}else {
this.state = 44;
}if (true) break;

case 42:
//C
this.state = 45;
 //BA.debugLineNum = 1316;BA.debugLine="LOADING_SPEECH_DOZ = LABEL_DOZ_QTY_LOADING.Text";
_loading_speech_doz = parent.mostCurrent._label_doz_qty_loading.getText()+" DOZEN and ";
 if (true) break;

case 44:
//C
this.state = 45;
 //BA.debugLineNum = 1318;BA.debugLine="LOADING_SPEECH_DOZ = LABEL_DOZ_QTY_LOADING.Text";
_loading_speech_doz = parent.mostCurrent._label_doz_qty_loading.getText()+" DOZEN. ";
 if (true) break;

case 45:
//C
this.state = 48;
;
 if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 1321;BA.debugLine="LOADING_SPEECH_DOZ = \" \"";
_loading_speech_doz = " ";
 if (true) break;

case 48:
//C
this.state = 49;
;
 //BA.debugLineNum = 1323;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 70;
return;
case 70:
//C
this.state = 49;
;
 //BA.debugLineNum = 1324;BA.debugLine="If LABEL_PACK_QTY_LOADING.Text > 0 Then";
if (true) break;

case 49:
//if
this.state = 60;
if ((double)(Double.parseDouble(parent.mostCurrent._label_pack_qty_loading.getText()))>0) { 
this.state = 51;
}else {
this.state = 59;
}if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 1325;BA.debugLine="If LABEL_BAG_QTY_LOADING.Text > 0 Then";
if (true) break;

case 52:
//if
this.state = 57;
if ((double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))>0) { 
this.state = 54;
}else {
this.state = 56;
}if (true) break;

case 54:
//C
this.state = 57;
 //BA.debugLineNum = 1326;BA.debugLine="LOADING_SPEECH_PACK = LABEL_PACK_QTY_LOADING.Te";
_loading_speech_pack = parent.mostCurrent._label_pack_qty_loading.getText()+" PACK and ";
 if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 1328;BA.debugLine="LOADING_SPEECH_PACK = LABEL_PACK_QTY_LOADING.Te";
_loading_speech_pack = parent.mostCurrent._label_pack_qty_loading.getText()+" PACK. ";
 if (true) break;

case 57:
//C
this.state = 60;
;
 if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 1331;BA.debugLine="LOADING_SPEECH_PACK = \" \"";
_loading_speech_pack = " ";
 if (true) break;

case 60:
//C
this.state = 61;
;
 //BA.debugLineNum = 1333;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 71;
return;
case 71:
//C
this.state = 61;
;
 //BA.debugLineNum = 1334;BA.debugLine="If LABEL_BAG_QTY_LOADING.Text > 0 Then";
if (true) break;

case 61:
//if
this.state = 66;
if ((double)(Double.parseDouble(parent.mostCurrent._label_bag_qty_loading.getText()))>0) { 
this.state = 63;
}else {
this.state = 65;
}if (true) break;

case 63:
//C
this.state = 66;
 //BA.debugLineNum = 1335;BA.debugLine="LOADING_SPEECH_BAG = LABEL_BAG_QTY_LOADING.Text";
_loading_speech_bag = parent.mostCurrent._label_bag_qty_loading.getText()+" BAG. ";
 if (true) break;

case 65:
//C
this.state = 66;
 //BA.debugLineNum = 1337;BA.debugLine="LOADING_SPEECH_BAG = \" \"";
_loading_speech_bag = " ";
 if (true) break;

case 66:
//C
this.state = -1;
;
 //BA.debugLineNum = 1339;BA.debugLine="Sleep(20)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (20));
this.state = 72;
return;
case 72:
//C
this.state = -1;
;
 //BA.debugLineNum = 1340;BA.debugLine="TTS1.Speak(LOADING_SPEECH_CASE _ 					 & LOADING_";
parent._tts1.Speak(_loading_speech_case+_loading_speech_pcs+_loading_speech_box+_loading_speech_doz+_loading_speech_pack+_loading_speech_bag,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1346;BA.debugLine="Sleep(20)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (20));
this.state = 73;
return;
case 73:
//C
this.state = -1;
;
 //BA.debugLineNum = 1347;BA.debugLine="TTS1.Speak(LOADING_SPEECH_CASE & LOADING_SPEECH_P";
parent._tts1.Speak(_loading_speech_case+_loading_speech_pcs+_loading_speech_box+_loading_speech_doz+_loading_speech_pack+_loading_speech_bag,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1348;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _over_prepare() throws Exception{
ResumableSub_OVER_PREPARE rsub = new ResumableSub_OVER_PREPARE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_OVER_PREPARE extends BA.ResumableSub {
public ResumableSub_OVER_PREPARE(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 2161;BA.debugLine="over_short_trigger = 1";
parent._over_short_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 2162;BA.debugLine="Msgbox2Async(\"The product :\"& CRLF &\"\"&LABEL_LOAD";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_desc_loading.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"IS OVER PREPARE, Please register this report after you click OK."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2163;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2164;BA.debugLine="PANEL_BG_TRAIL.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_trail.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2165;BA.debugLine="PANEL_BG_TRAIL.BringToFront";
parent.mostCurrent._panel_bg_trail.BringToFront();
 //BA.debugLineNum = 2166;BA.debugLine="GET_VISOR";
_get_visor();
 //BA.debugLineNum = 2167;BA.debugLine="CMB_REASON.cmbBox.Enabled = False";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2168;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2169;BA.debugLine="CMB_REASON.cmbBox.Clear";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 2170;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 2171;BA.debugLine="CMB_REASON.cmbBox.Add(\"OVER PREPARE\")";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("OVER PREPARE");
 //BA.debugLineNum = 2172;BA.debugLine="CMB_PICKER.SelectedIndex = -1";
parent.mostCurrent._cmb_picker._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 2173;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 2174;BA.debugLine="OpenSpinner(CMB_PICKER.cmbBox)";
_openspinner(parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 2175;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 2176;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_QUANTITY.Text.S";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent.mostCurrent._edittext_quantity.getText().substring((int) (0),parent.mostCurrent._edittext_quantity.getText().indexOf("."))));
 //BA.debugLineNum = 2177;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _panel_bg_loading_click() throws Exception{
 //BA.debugLineNum = 1874;BA.debugLine="Sub PANEL_BG_LOADING_Click";
 //BA.debugLineNum = 1875;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1876;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_security_click() throws Exception{
 //BA.debugLineNum = 1198;BA.debugLine="Sub PANEL_BG_SECURITY_Click";
 //BA.debugLineNum = 1199;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1200;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_trail_click() throws Exception{
 //BA.debugLineNum = 2419;BA.debugLine="Sub PANEL_BG_TRAIL_Click";
 //BA.debugLineNum = 2420;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2421;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 22;BA.debugLine="Dim clearBitmap As Bitmap";
_clearbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim addBitmap As Bitmap";
_addbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim picklist_id As String";
_picklist_id = "";
 //BA.debugLineNum = 29;BA.debugLine="Dim phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 32;BA.debugLine="Dim TTS1 As TTS";
_tts1 = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 35;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 36;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 37;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 39;BA.debugLine="Dim security_trigger As String";
_security_trigger = "";
 //BA.debugLineNum = 40;BA.debugLine="Dim over_short_trigger As String";
_over_short_trigger = "";
 //BA.debugLineNum = 43;BA.debugLine="Dim product_id As String";
_product_id = "";
 //BA.debugLineNum = 44;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 45;BA.debugLine="Dim principal_name As String";
_principal_name = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim reason As String";
_reason = "";
 //BA.debugLineNum = 47;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 49;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 54;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 56;BA.debugLine="Dim scan_next_trigger As String";
_scan_next_trigger = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim scan_trigger As String";
_scan_trigger = "";
 //BA.debugLineNum = 59;BA.debugLine="Dim loading_count As String";
_loading_count = "";
 //BA.debugLineNum = 60;BA.debugLine="Dim downloaded_count As String";
_downloaded_count = "";
 //BA.debugLineNum = 61;BA.debugLine="Dim loaded_count As Int";
_loaded_count = 0;
 //BA.debugLineNum = 62;BA.debugLine="Dim uploaded_count As Int";
_uploaded_count = 0;
 //BA.debugLineNum = 64;BA.debugLine="Dim unit_trigger As String";
_unit_trigger = "";
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 372;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 373;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 374;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("751511298","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 375;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 376;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 377;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 378;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 379;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 380;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 382;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 383;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 384;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 385;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 386;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 388;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("751511312","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 389;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 390;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 391;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 394;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 283;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 284;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 285;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 286;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 287;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 288;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 289;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 290;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 291;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 292;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static void  _short_prepare() throws Exception{
ResumableSub_SHORT_PREPARE rsub = new ResumableSub_SHORT_PREPARE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_SHORT_PREPARE extends BA.ResumableSub {
public ResumableSub_SHORT_PREPARE(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 2179;BA.debugLine="over_short_trigger = 2";
parent._over_short_trigger = BA.NumberToString(2);
 //BA.debugLineNum = 2180;BA.debugLine="Msgbox2Async(\"The product :\"& CRLF &\"\"&LABEL_LOAD";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_desc_loading.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"IS SHORT PREPARE, Please register this report after you click OK."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2181;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 1;
return;
case 1:
//C
this.state = -1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2182;BA.debugLine="PANEL_BG_TRAIL.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_trail.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2183;BA.debugLine="GET_VISOR";
_get_visor();
 //BA.debugLineNum = 2184;BA.debugLine="CMB_REASON.cmbBox.Enabled = False";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2185;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2186;BA.debugLine="CMB_REASON.cmbBox.Clear";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 2187;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 2188;BA.debugLine="CMB_REASON.cmbBox.Add(\"SHORT PREPARE\")";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("SHORT PREPARE");
 //BA.debugLineNum = 2189;BA.debugLine="CMB_PICKER.SelectedIndex = -1";
parent.mostCurrent._cmb_picker._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 2190;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 2191;BA.debugLine="OpenSpinner(CMB_PICKER.cmbBox)";
_openspinner(parent.mostCurrent._cmb_picker._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 2192;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 2193;BA.debugLine="EDITTEXT_QUANTITY.Text = EDITTEXT_QUANTITY.Text.S";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent.mostCurrent._edittext_quantity.getText().substring((int) (0),parent.mostCurrent._edittext_quantity.getText().indexOf("."))));
 //BA.debugLineNum = 2194;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 345;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 346;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 347;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 348;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 349;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 350;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
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
 //BA.debugLineNum = 351;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 352;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 354;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 355;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 357;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 358;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 359;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 360;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
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
 //BA.debugLineNum = 361;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 363;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 365;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("751445781",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 366;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 367;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 368;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
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
 //BA.debugLineNum = 371;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 565;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 566;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 567;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 568;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("751773443","Trying to connect...",0);
 //BA.debugLineNum = 569;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 570;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 571;BA.debugLine="End Sub";
return "";
}
public static String  _tts1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 1257;BA.debugLine="Sub TTS1_Ready (Success As Boolean)";
 //BA.debugLineNum = 1258;BA.debugLine="If Success Then";
if (_success) { 
 }else {
 };
 //BA.debugLineNum = 1264;BA.debugLine="End Sub";
return "";
}
public static void  _update_loaded() throws Exception{
ResumableSub_UPDATE_LOADED rsub = new ResumableSub_UPDATE_LOADED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_LOADED extends BA.ResumableSub {
public ResumableSub_UPDATE_LOADED(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
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
 //BA.debugLineNum = 2366;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Status...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Status..."));
 //BA.debugLineNum = 2367;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_pick";
_cmd = _createcommand("update_picklist_loaded",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())),(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 2368;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 2369;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 2370;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2371;BA.debugLine="LABEL_MSGBOX2.Text = \"Picklist uploaded succesfu";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Picklist uploaded succesfully..."));
 //BA.debugLineNum = 2372;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2373;BA.debugLine="Msgbox2Async(\"Picklist loaded and uploaded succe";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Picklist loaded and uploaded successfully."),BA.ObjectToCharSequence("Notice"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2374;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 8;
return;
case 8:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2375;BA.debugLine="CLEAR_PICKLIST";
_clear_picklist();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2377;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2378;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("756426509","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 2379;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2380;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2382;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 2383;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _update_loading() throws Exception{
ResumableSub_UPDATE_LOADING rsub = new ResumableSub_UPDATE_LOADING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_LOADING extends BA.ResumableSub {
public ResumableSub_UPDATE_LOADING(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 805;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Status...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Status..."));
 //BA.debugLineNum = 806;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_pick";
_cmd = _createcommand("update_picklist_loading",new Object[]{(Object)(parent.mostCurrent._login_module._tab_id /*String*/ ),(Object)(parent.mostCurrent._login_module._username /*String*/ ),(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())),(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 807;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 808;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 809;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 810;BA.debugLine="DOWNLOAD_PICKLIST";
_download_picklist();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 812;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 813;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("752166665","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 814;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 815;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 817;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 818;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 304;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 305;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 306;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 307;BA.debugLine="End Sub";
return "";
}
public static void  _validate_picklist_status() throws Exception{
ResumableSub_VALIDATE_PICKLIST_STATUS rsub = new ResumableSub_VALIDATE_PICKLIST_STATUS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_VALIDATE_PICKLIST_STATUS extends BA.ResumableSub {
public ResumableSub_VALIDATE_PICKLIST_STATUS(wingan.app.loading_module parent) {
this.parent = parent;
}
wingan.app.loading_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
int _result = 0;
anywheresoftware.b4a.BA.IterableList group12;
int index12;
int groupLen12;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 700;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 701;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 702;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Downloading Picklist\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Downloading Picklist"));
 //BA.debugLineNum = 703;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching data..."));
 //BA.debugLineNum = 704;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 705;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_pick";
_cmd = _createcommand("select_picklist_status",new Object[]{(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 706;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 63;
return;
case 63:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 707;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 62;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 61;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 708;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 709;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 64;
return;
case 64:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 710;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 59;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 58;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 711;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 56;
group12 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index12 = 0;
groupLen12 = group12.getSize();
this.state = 65;
if (true) break;

case 65:
//C
this.state = 56;
if (index12 < groupLen12) {
this.state = 9;
_row = (Object[])(group12.Get(index12));}
if (true) break;

case 66:
//C
this.state = 65;
index12++;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 712;BA.debugLine="If row(res.Columns.Get(\"PickListStatus\")) = \"C";
if (true) break;

case 10:
//if
this.state = 55;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))]).equals((Object)("CONFIRMED"))) { 
this.state = 12;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))]).equals((Object)("LOADING"))) { 
this.state = 26;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))]).equals((Object)("LOADED"))) { 
this.state = 40;
}else {
this.state = 54;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 713;BA.debugLine="If row(res.Columns.Get(\"PreparingChecker\")) =";
if (true) break;

case 13:
//if
this.state = 24;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PreparingChecker"))))]).equals((Object)(parent.mostCurrent._login_module._username /*String*/ ))) { 
this.state = 15;
}else {
this.state = 23;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 714;BA.debugLine="Msgbox2Async(\"You are the Preparing Checker";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You are the Preparing Checker of this picklist, are you authorize to Load this?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 716;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 67;
return;
case 67:
//C
this.state = 16;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 717;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 16:
//if
this.state = 21;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 18;
}else {
this.state = 20;
}if (true) break;

case 18:
//C
this.state = 21;
 //BA.debugLineNum = 718;BA.debugLine="security_trigger = \"OVERWRITE\"";
parent._security_trigger = "OVERWRITE";
 //BA.debugLineNum = 719;BA.debugLine="GET_SECURITY";
_get_security();
 //BA.debugLineNum = 720;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 21;
;
 //BA.debugLineNum = 721;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, T";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 722;BA.debugLine="PANEL_BG_SECURITY.BringToFront";
parent.mostCurrent._panel_bg_security.BringToFront();
 //BA.debugLineNum = 723;BA.debugLine="EDITTEXT_PASSWORD.RequestFocus";
parent.mostCurrent._edittext_password.RequestFocus();
 //BA.debugLineNum = 724;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 21;
;
 //BA.debugLineNum = 725;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PASSWORD)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_password.getObject()));
 if (true) break;

case 20:
//C
this.state = 21;
 if (true) break;

case 21:
//C
this.state = 24;
;
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 730;BA.debugLine="UPDATE_LOADING";
_update_loading();
 if (true) break;

case 24:
//C
this.state = 55;
;
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 733;BA.debugLine="Log(98)";
anywheresoftware.b4a.keywords.Common.LogImpl("752101154",BA.NumberToString(98),0);
 //BA.debugLineNum = 734;BA.debugLine="If row(res.Columns.Get(\"LoadingTabletID\")) =";
if (true) break;

case 27:
//if
this.state = 38;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingTabletID"))))]).equals((Object)(parent.mostCurrent._login_module._tab_id /*String*/ ))) { 
this.state = 29;
}else {
this.state = 31;
}if (true) break;

case 29:
//C
this.state = 38;
 //BA.debugLineNum = 735;BA.debugLine="DOWNLOAD_PICKLIST";
_download_picklist();
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 737;BA.debugLine="Msgbox2Async(\"The picklist you scan :\" & CRL";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The picklist you scan :"+anywheresoftware.b4a.keywords.Common.CRLF+" Picklist Name : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PicklistName"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" is NOW LOADING by :"+anywheresoftware.b4a.keywords.Common.CRLF+" Loading Checker : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingChecker"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Date & Time Loading : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingDateTIme"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Tablet : TABLET "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingTabletID"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Do you want to overwrite this picklist?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 745;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 70;
return;
case 70:
//C
this.state = 32;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 746;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 32:
//if
this.state = 37;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 34;
}else {
this.state = 36;
}if (true) break;

case 34:
//C
this.state = 37;
 //BA.debugLineNum = 747;BA.debugLine="UPDATE_LOADING";
_update_loading();
 if (true) break;

case 36:
//C
this.state = 37;
 if (true) break;

case 37:
//C
this.state = 38;
;
 if (true) break;

case 38:
//C
this.state = 55;
;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 753;BA.debugLine="If row(res.Columns.Get(\"LoadingTabletID\")) =";
if (true) break;

case 41:
//if
this.state = 52;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingTabletID"))))]).equals((Object)(parent.mostCurrent._login_module._tab_id /*String*/ )) && (_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingChecker"))))]).equals((Object)(parent.mostCurrent._login_module._username /*String*/ ))) { 
this.state = 43;
}else {
this.state = 51;
}if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 754;BA.debugLine="Msgbox2Async(\"You've already prepared this p";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You've already prepared this picklist, Do you want to preparing check it again?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 756;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 71;
return;
case 71:
//C
this.state = 44;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 757;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 44:
//if
this.state = 49;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 46;
}else {
this.state = 48;
}if (true) break;

case 46:
//C
this.state = 49;
 //BA.debugLineNum = 758;BA.debugLine="security_trigger = \"OVERWRITE\"";
parent._security_trigger = "OVERWRITE";
 //BA.debugLineNum = 759;BA.debugLine="GET_SECURITY";
_get_security();
 //BA.debugLineNum = 760;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 72;
return;
case 72:
//C
this.state = 49;
;
 //BA.debugLineNum = 761;BA.debugLine="PANEL_BG_SECURITY.SetVisibleAnimated(300, T";
parent.mostCurrent._panel_bg_security.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 762;BA.debugLine="PANEL_BG_SECURITY.BringToFront";
parent.mostCurrent._panel_bg_security.BringToFront();
 //BA.debugLineNum = 763;BA.debugLine="EDITTEXT_PASSWORD.RequestFocus";
parent.mostCurrent._edittext_password.RequestFocus();
 //BA.debugLineNum = 764;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 73;
return;
case 73:
//C
this.state = 49;
;
 //BA.debugLineNum = 765;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PASSWORD)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_password.getObject()));
 if (true) break;

case 48:
//C
this.state = 49;
 if (true) break;

case 49:
//C
this.state = 52;
;
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 772;BA.debugLine="Msgbox2Async(\"The picklist you scan :\" & CRL";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The picklist you scan :"+anywheresoftware.b4a.keywords.Common.CRLF+" Picklist Name : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PicklistName"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" is already Loaded by :"+anywheresoftware.b4a.keywords.Common.CRLF+" Preparing Checker : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingChecker"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Date & Time Loading : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingDateTIme"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Date & Time Loaded : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingDateTime"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Tablet : TABLET "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("LoadingTabletid"))))])),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 780;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 52:
//C
this.state = 55;
;
 if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 783;BA.debugLine="Msgbox2Async(\"The picklist you scan :\" & CRLF";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The picklist you scan :"+anywheresoftware.b4a.keywords.Common.CRLF+" Picklist Name : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PicklistName"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" is in status of :"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))])+" cannot be load."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 787;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 55:
//C
this.state = 66;
;
 if (true) break;
if (true) break;

case 56:
//C
this.state = 59;
;
 if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 791;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 792;BA.debugLine="Msgbox2Async(\"The Picklist ID you type/scan is";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 793;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 74;
return;
case 74:
//C
this.state = 59;
;
 //BA.debugLineNum = 794;BA.debugLine="CLEAR_PICKLIST";
_clear_picklist();
 if (true) break;

case 59:
//C
this.state = 62;
;
 if (true) break;

case 61:
//C
this.state = 62;
 //BA.debugLineNum = 797;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 798;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("752101219","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 799;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 800;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 62:
//C
this.state = -1;
;
 //BA.debugLineNum = 802;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 803;BA.debugLine="End Sub";
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
