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

public class cancelled_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static cancelled_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.cancelled_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (cancelled_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.cancelled_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.cancelled_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (cancelled_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (cancelled_module) Resume **");
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
		return cancelled_module.class;
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
            BA.LogInfo("** Activity (cancelled_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (cancelled_module) Pause event (activity is not paused). **");
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
            cancelled_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (cancelled_module) Resume **");
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
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _addbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _uploadbitmap = null;
public static String _picklist_id = "";
public static String _date_dispatch = "";
public static String _route_name = "";
public static String _plate_no = "";
public static String _customer_id = "";
public static String _error_trigger = "";
public static String _principal_id = "";
public static String _product_id = "";
public static String _reason = "";
public static String _caseper = "";
public static String _pcsper = "";
public static String _dozper = "";
public static String _boxper = "";
public static String _bagper = "";
public static String _packper = "";
public static String _total_pieces = "";
public static String _scan_code = "";
public static String _transaction_number = "";
public static String _cancelled_trigger = "";
public static String _cancelled_id = "";
public static String _total_invoice = "";
public static String _total_cancelled = "";
public static String _cancelled_reason = "";
public static String _wrong_principal_id = "";
public static String _wrong_product_id = "";
public static String _wrong_reason = "";
public static String _wrong_caseper = "";
public static String _wrong_pcsper = "";
public static String _wrong_dozper = "";
public static String _wrong_boxper = "";
public static String _wrong_bagper = "";
public static String _wrong_packper = "";
public static String _wrong_total_pieces = "";
public static String _wrong_scan_code = "";
public static String _wrong_trigger = "";
public static String _item_number = "";
public static String _lifespan_month = "";
public static String _lifespan_year = "";
public static String _default_reading = "";
public static String _monthexp = "";
public static String _yearexp = "";
public static String _monthmfg = "";
public static String _yearmfg = "";
public anywheresoftware.b4a.objects.IME _ctrl = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xdatetemplate _datetemplate = null;
public wingan.app.b4xdatetemplate _datetemplate2 = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate2 = null;
public wingan.app.b4xinputtemplate _inputtemplate = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public anywheresoftware.b4a.objects.collections.List _invoice = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_customer_name = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_invoice_no = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_invoice_date = null;
public wingan.app.b4xtable _table_invoice_product = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_input = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public wingan.app.b4xcombobox _cmb_reason = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_principal = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_list = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_list = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvl_list = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_save = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_cancel = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_wrongserved_description = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_wrongserved_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_wrongserved_principal = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_wrongserved = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_wrongserved = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_manufactured = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_expiration = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_expiration = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_expiration = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_upload = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvl_invoice = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_invoice = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_cancelled_all = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
public wingan.app.dashboard_module _dashboard_module = null;
public wingan.app.return_module _return_module = null;
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
public ResumableSub_Activity_Create(wingan.app.cancelled_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.cancelled_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
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
 //BA.debugLineNum = 168;BA.debugLine="Activity.LoadLayout(\"cancelled\")";
parent.mostCurrent._activity.LoadLayout("cancelled",mostCurrent.activityBA);
 //BA.debugLineNum = 170;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 171;BA.debugLine="addBitmap = LoadBitmap(File.DirAssets, \"invoice.p";
parent._addbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"invoice.png");
 //BA.debugLineNum = 172;BA.debugLine="uploadBitmap = LoadBitmap(File.DirAssets, \"upload";
parent._uploadbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"upload.png");
 //BA.debugLineNum = 174;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 175;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 176;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 177;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 178;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 179;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 180;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 182;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 183;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 184;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 186;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 187;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 190;BA.debugLine="ACToolBarLight1.Elevation = 1dip";
parent.mostCurrent._actoolbarlight1.setElevation((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 193;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 194;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 196;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 197;BA.debugLine="p.SetLayoutAnimated(0, 10%x, 20%y, 80%y, 40%y)";
_p.SetLayoutAnimated((int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 198;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 200;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 201;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 202;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 203;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 204;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 205;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 206;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 207;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 208;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 210;BA.debugLine="DateTemplate.Initialize";
parent.mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 211;BA.debugLine="DateTemplate.MinYear = 2016";
parent.mostCurrent._datetemplate._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 212;BA.debugLine="DateTemplate.MaxYear = 2030";
parent.mostCurrent._datetemplate._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 213;BA.debugLine="DateTemplate.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 214;BA.debugLine="DateTemplate.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 215;BA.debugLine="DateTemplate.btnMonthLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 216;BA.debugLine="DateTemplate.btnMonthRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 217;BA.debugLine="DateTemplate.btnYearLeft.Color = Colors.RGB(82,16";
parent.mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 218;BA.debugLine="DateTemplate.btnYearRight.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 219;BA.debugLine="DateTemplate.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 221;BA.debugLine="DateTemplate2.Initialize";
parent.mostCurrent._datetemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 222;BA.debugLine="DateTemplate2.MinYear = 2016";
parent.mostCurrent._datetemplate2._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 223;BA.debugLine="DateTemplate2.MaxYear = 2030";
parent.mostCurrent._datetemplate2._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 224;BA.debugLine="DateTemplate2.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate2._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 225;BA.debugLine="DateTemplate2.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate2._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 226;BA.debugLine="DateTemplate2.btnMonthLeft.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate2._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 227;BA.debugLine="DateTemplate2.btnMonthRight.Color = Colors.RGB(82";
parent.mostCurrent._datetemplate2._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 228;BA.debugLine="DateTemplate2.btnYearLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate2._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 229;BA.debugLine="DateTemplate2.btnYearRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate2._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 230;BA.debugLine="DateTemplate2.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate2._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 232;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 233;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 234;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 235;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 236;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 237;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 239;BA.debugLine="SearchTemplate2.Initialize";
parent.mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 240;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 241;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 242;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 243;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 244;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 246;BA.debugLine="InputTemplate.Initialize";
parent.mostCurrent._inputtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 247;BA.debugLine="InputTemplate.SetBorderColor(Colors.Green,Colors.";
parent.mostCurrent._inputtemplate._setbordercolor /*String*/ (anywheresoftware.b4a.keywords.Common.Colors.Green,anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 248;BA.debugLine="InputTemplate.TextField1.TextColor = Colors.Black";
parent.mostCurrent._inputtemplate._textfield1 /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 252;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 253;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 255;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 256;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 268;BA.debugLine="WRONGSERVED_MANUAL";
_wrongserved_manual();
 //BA.debugLineNum = 269;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 270;BA.debugLine="LOAD_INVOICE_PRODUCT_HEADER";
_load_invoice_product_header();
 //BA.debugLineNum = 271;BA.debugLine="GET_INVOICE";
_get_invoice();
 //BA.debugLineNum = 272;BA.debugLine="End Sub";
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
public ResumableSub_Activity_CreateMenu(wingan.app.cancelled_module parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.cancelled_module parent;
de.amberhome.objects.appcompat.ACMenuWrapper _menu;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item1 = null;
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
 //BA.debugLineNum = 274;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("See Upload"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 275;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 276;BA.debugLine="UpdateIcon(\"See Upload\", uploadBitmap)";
_updateicon("See Upload",parent._uploadbitmap);
 //BA.debugLineNum = 277;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 278;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 279;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 280;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (2),(int) (2),BA.ObjectToCharSequence("Choose Invoice"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 281;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 282;BA.debugLine="UpdateIcon(\"Choose Invoice\", addBitmap)";
_updateicon("Choose Invoice",parent._addbitmap);
 //BA.debugLineNum = 283;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 284;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 2142;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 2143;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 2144;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 2146;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 292;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 293;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("734668545","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 294;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 295;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 296;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 297;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 286;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 288;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 289;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 362;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 363;BA.debugLine="If Item.Title = \"cart\" Then";
if ((_item.getTitle()).equals("cart")) { 
 //BA.debugLineNum = 364;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("735258370","Resuming...",0);
 //BA.debugLineNum = 365;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 366;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 367;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 368;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 369;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 370;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 371;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 373;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 374;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 }else if((_item.getTitle()).equals("Choose Invoice")) { 
 //BA.debugLineNum = 377;BA.debugLine="OPEN_INVOICE";
_open_invoice();
 }else if((_item.getTitle()).equals("See Upload")) { 
 //BA.debugLineNum = 379;BA.debugLine="LOAD_INVOICE_UPLOAD";
_load_invoice_upload();
 };
 //BA.debugLineNum = 381;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_navigationitemclick() throws Exception{
ResumableSub_ACToolBarLight1_NavigationItemClick rsub = new ResumableSub_ACToolBarLight1_NavigationItemClick(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_NavigationItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_NavigationItemClick(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 327;BA.debugLine="Msgbox2Async(\"Are you sure you want to exit?\", \"N";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to exit?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 328;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 329;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 330;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 331;BA.debugLine="StartActivity(RETURN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._return_module.getObject()));
 //BA.debugLineNum = 332;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _addbadgetoicon(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp,int _number1) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _mbmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _target = null;
 //BA.debugLineNum = 348;BA.debugLine="Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int)";
 //BA.debugLineNum = 349;BA.debugLine="Dim cvs1 As Canvas";
_cvs1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 350;BA.debugLine="Dim mbmp As Bitmap";
_mbmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 351;BA.debugLine="mbmp.InitializeMutable(32dip, 32dip)";
_mbmp.InitializeMutable(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 352;BA.debugLine="cvs1.Initialize2(mbmp)";
_cvs1.Initialize2((android.graphics.Bitmap)(_mbmp.getObject()));
 //BA.debugLineNum = 353;BA.debugLine="Dim target As Rect";
_target = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 354;BA.debugLine="target.Initialize(0, 0, mbmp.Width, mbmp.Height)";
_target.Initialize((int) (0),(int) (0),_mbmp.getWidth(),_mbmp.getHeight());
 //BA.debugLineNum = 355;BA.debugLine="cvs1.DrawBitmap(bmp, Null, target)";
_cvs1.DrawBitmap((android.graphics.Bitmap)(_bmp.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_target.getObject()));
 //BA.debugLineNum = 356;BA.debugLine="If Number1 > 0 Then";
if (_number1>0) { 
 //BA.debugLineNum = 357;BA.debugLine="cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, C";
_cvs1.DrawCircle((float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 358;BA.debugLine="cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8";
_cvs1.DrawText(mostCurrent.activityBA,BA.NumberToString(anywheresoftware.b4a.keywords.Common.Min(_number1,1000)),(float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (11))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (9),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 };
 //BA.debugLineNum = 360;BA.debugLine="Return mbmp";
if (true) return _mbmp;
 //BA.debugLineNum = 361;BA.debugLine="End Sub";
return null;
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 620;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 621;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("735520513","Connection broken...",0);
 //BA.debugLineNum = 622;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 623;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 624;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 625;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 626;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 627;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 628;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 629;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 631;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 633;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.cancelled_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.cancelled_module parent;
byte[] _buffer;
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _qrow = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int step8;
int limit8;
int step22;
int limit22;
int step36;
int limit36;
int step50;
int limit50;
int step64;
int limit64;
int step78;
int limit78;
int step92;
int limit92;
int step104;
int limit104;
int step140;
int limit140;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 436;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("735454977","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 438;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 441;BA.debugLine="If PANEL_BG_WRONGSERVED.Visible = True Then";
if (true) break;

case 1:
//if
this.state = 111;
if (parent.mostCurrent._panel_bg_wrongserved.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}else {
this.state = 43;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 443;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 444;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 4:
//if
this.state = 25;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 6;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 18;
}else {
this.state = 24;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 445;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 446;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 447;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step8 = 1;
limit8 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 112;
if (true) break;

case 112:
//C
this.state = 10;
if ((step8 > 0 && _row <= limit8) || (step8 < 0 && _row >= limit8)) this.state = 9;
if (true) break;

case 113:
//C
this.state = 112;
_row = ((int)(0 + _row + step8)) ;
if (true) break;

case 9:
//C
this.state = 113;
 //BA.debugLineNum = 448;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 449;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 450;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 452;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True)";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 453;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 114;
return;
case 114:
//C
this.state = 11;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 454;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 11:
//if
this.state = 16;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 13;
}else {
this.state = 15;
}if (true) break;

case 13:
//C
this.state = 16;
 //BA.debugLineNum = 455;BA.debugLine="LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = ls.G";
parent.mostCurrent._label_load_wrongserved_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 456;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 458;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 16:
//C
this.state = 25;
;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 463;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 19:
//for
this.state = 22;
step22 = 1;
limit22 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 115;
if (true) break;

case 115:
//C
this.state = 22;
if ((step22 > 0 && _row <= limit22) || (step22 < 0 && _row >= limit22)) this.state = 21;
if (true) break;

case 116:
//C
this.state = 115;
_row = ((int)(0 + _row + step22)) ;
if (true) break;

case 21:
//C
this.state = 116;
 //BA.debugLineNum = 464;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 465;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("735455006",BA.NumberToString(1),0);
 //BA.debugLineNum = 466;BA.debugLine="LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = curs";
parent.mostCurrent._label_load_wrongserved_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 467;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;
if (true) break;

case 22:
//C
this.state = 25;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 470;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 471;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not RE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 472;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 475;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 117;
return;
case 117:
//C
this.state = 26;
;
 //BA.debugLineNum = 476;BA.debugLine="If trigger = 0 Then";
if (true) break;

case 26:
//if
this.state = 41;
if (_trigger==0) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 477;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_wrongserved_description.getText()+"'")));
 //BA.debugLineNum = 478;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 29:
//for
this.state = 32;
step36 = 1;
limit36 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 118;
if (true) break;

case 118:
//C
this.state = 32;
if ((step36 > 0 && _qrow <= limit36) || (step36 < 0 && _qrow >= limit36)) this.state = 31;
if (true) break;

case 119:
//C
this.state = 118;
_qrow = ((int)(0 + _qrow + step36)) ;
if (true) break;

case 31:
//C
this.state = 119;
 //BA.debugLineNum = 479;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 480;BA.debugLine="LABEL_LOAD_WRONGSERVED_VARIANT.Text = cursor3.";
parent.mostCurrent._label_load_wrongserved_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 481;BA.debugLine="wrong_principal_id = cursor3.GetString(\"princi";
parent._wrong_principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 482;BA.debugLine="wrong_product_id = cursor3.GetString(\"product_";
parent._wrong_product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 484;BA.debugLine="wrong_caseper = cursor3.GetString(\"CASE_UNIT_P";
parent._wrong_caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 485;BA.debugLine="wrong_pcsper = cursor3.GetString(\"PCS_UNIT_PER";
parent._wrong_pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 486;BA.debugLine="wrong_dozper = cursor3.GetString(\"DOZ_UNIT_PER";
parent._wrong_dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 487;BA.debugLine="wrong_boxper = cursor3.GetString(\"BOX_UNIT_PER";
parent._wrong_boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 488;BA.debugLine="wrong_bagper = cursor3.GetString(\"BAG_UNIT_PER";
parent._wrong_bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 489;BA.debugLine="wrong_packper = cursor3.GetString(\"PACK_UNIT_P";
parent._wrong_packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 32:
//C
this.state = 33;
;
 //BA.debugLineNum = 492;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._wrong_principal_id+"'")));
 //BA.debugLineNum = 493;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 33:
//if
this.state = 40;
if (parent._cursor6.getRowCount()>0) { 
this.state = 35;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 494;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 36:
//for
this.state = 39;
step50 = 1;
limit50 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 120;
if (true) break;

case 120:
//C
this.state = 39;
if ((step50 > 0 && _row <= limit50) || (step50 < 0 && _row >= limit50)) this.state = 38;
if (true) break;

case 121:
//C
this.state = 120;
_row = ((int)(0 + _row + step50)) ;
if (true) break;

case 38:
//C
this.state = 121;
 //BA.debugLineNum = 495;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 496;BA.debugLine="LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = curso";
parent.mostCurrent._label_load_wrongserved_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 39:
//C
this.state = 40;
;
 if (true) break;

case 40:
//C
this.state = 41;
;
 //BA.debugLineNum = 499;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 122;
return;
case 122:
//C
this.state = 41;
;
 //BA.debugLineNum = 500;BA.debugLine="wrong_reason = \"N/A\"";
parent._wrong_reason = "N/A";
 //BA.debugLineNum = 501;BA.debugLine="wrong_scan_code = BytesToString(Buffer, 0, Buff";
parent._wrong_scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 if (true) break;

case 41:
//C
this.state = 111;
;
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 506;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 507;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 44:
//if
this.state = 65;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 46;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 58;
}else {
this.state = 64;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 508;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 509;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 510;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 47:
//for
this.state = 50;
step64 = 1;
limit64 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 123;
if (true) break;

case 123:
//C
this.state = 50;
if ((step64 > 0 && _row <= limit64) || (step64 < 0 && _row >= limit64)) this.state = 49;
if (true) break;

case 124:
//C
this.state = 123;
_row = ((int)(0 + _row + step64)) ;
if (true) break;

case 49:
//C
this.state = 124;
 //BA.debugLineNum = 511;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 512;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 513;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 50:
//C
this.state = 51;
;
 //BA.debugLineNum = 515;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) '";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 516;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 125;
return;
case 125:
//C
this.state = 51;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 517;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 51:
//if
this.state = 56;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 53;
}else {
this.state = 55;
}if (true) break;

case 53:
//C
this.state = 56;
 //BA.debugLineNum = 518;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 519;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 55:
//C
this.state = 56;
 //BA.debugLineNum = 521;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 56:
//C
this.state = 65;
;
 if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 526;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 59:
//for
this.state = 62;
step78 = 1;
limit78 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 126;
if (true) break;

case 126:
//C
this.state = 62;
if ((step78 > 0 && _row <= limit78) || (step78 < 0 && _row >= limit78)) this.state = 61;
if (true) break;

case 127:
//C
this.state = 126;
_row = ((int)(0 + _row + step78)) ;
if (true) break;

case 61:
//C
this.state = 127;
 //BA.debugLineNum = 527;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 528;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("735455069",BA.NumberToString(1),0);
 //BA.debugLineNum = 529;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 530;BA.debugLine="trigger = 0";
_trigger = (int) (0);
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
 //BA.debugLineNum = 533;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 534;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not REG";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 535;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 65:
//C
this.state = 66;
;
 //BA.debugLineNum = 538;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 128;
return;
case 128:
//C
this.state = 66;
;
 //BA.debugLineNum = 539;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM can";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_ref_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 540;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 66:
//if
this.state = 75;
if (parent._cursor4.getRowCount()>0) { 
this.state = 68;
}else {
this.state = 74;
}if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 541;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 69:
//for
this.state = 72;
step92 = 1;
limit92 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 129;
if (true) break;

case 129:
//C
this.state = 72;
if ((step92 > 0 && _row <= limit92) || (step92 < 0 && _row >= limit92)) this.state = 71;
if (true) break;

case 130:
//C
this.state = 129;
_row = ((int)(0 + _row + step92)) ;
if (true) break;

case 71:
//C
this.state = 130;
 //BA.debugLineNum = 542;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 543;BA.debugLine="total_invoice = cursor4.GetString(\"total_pieces";
parent._total_invoice = parent._cursor4.GetString("total_pieces");
 if (true) break;
if (true) break;

case 72:
//C
this.state = 75;
;
 if (true) break;

case 74:
//C
this.state = 75;
 //BA.debugLineNum = 546;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 547;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_description.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"is not in the invoice."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 548;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 75:
//C
this.state = 76;
;
 //BA.debugLineNum = 550;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 131;
return;
case 131:
//C
this.state = 76;
;
 //BA.debugLineNum = 551;BA.debugLine="If trigger = 0 Then";
if (true) break;

case 76:
//if
this.state = 110;
if (_trigger==0) { 
this.state = 78;
}if (true) break;

case 78:
//C
this.state = 79;
 //BA.debugLineNum = 552;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 553;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 79:
//for
this.state = 101;
step104 = 1;
limit104 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 132;
if (true) break;

case 132:
//C
this.state = 101;
if ((step104 > 0 && _qrow <= limit104) || (step104 < 0 && _qrow >= limit104)) this.state = 81;
if (true) break;

case 133:
//C
this.state = 132;
_qrow = ((int)(0 + _qrow + step104)) ;
if (true) break;

case 81:
//C
this.state = 82;
 //BA.debugLineNum = 554;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 555;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"pr";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 556;BA.debugLine="principal_id = cursor3.GetString(\"principal_id\"";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 557;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 559;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 560;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0 T";
if (true) break;

case 82:
//if
this.state = 85;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 84;
}if (true) break;

case 84:
//C
this.state = 85;
 //BA.debugLineNum = 561;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 563;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 Th";

case 85:
//if
this.state = 88;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 87;
}if (true) break;

case 87:
//C
this.state = 88;
 //BA.debugLineNum = 564;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 566;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 Th";

case 88:
//if
this.state = 91;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 90;
}if (true) break;

case 90:
//C
this.state = 91;
 //BA.debugLineNum = 567;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 569;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 Th";

case 91:
//if
this.state = 94;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 93;
}if (true) break;

case 93:
//C
this.state = 94;
 //BA.debugLineNum = 570;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 572;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 Th";

case 94:
//if
this.state = 97;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 96;
}if (true) break;

case 96:
//C
this.state = 97;
 //BA.debugLineNum = 573;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 575;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0 T";

case 97:
//if
this.state = 100;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 99;
}if (true) break;

case 99:
//C
this.state = 100;
 //BA.debugLineNum = 576;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 100:
//C
this.state = 133;
;
 //BA.debugLineNum = 579;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS\"";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 580;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 581;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 582;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 583;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 584;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS\"";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 586;BA.debugLine="default_reading = cursor3.GetString(\"default_ex";
parent._default_reading = parent._cursor3.GetString("default_expiration_date_reading");
 //BA.debugLineNum = 587;BA.debugLine="lifespan_year = cursor3.GetString(\"life_span_ye";
parent._lifespan_year = parent._cursor3.GetString("life_span_year");
 //BA.debugLineNum = 588;BA.debugLine="lifespan_month = cursor3.GetString(\"life_span_m";
parent._lifespan_month = parent._cursor3.GetString("life_span_month");
 if (true) break;
if (true) break;

case 101:
//C
this.state = 102;
;
 //BA.debugLineNum = 591;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 592;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 102:
//if
this.state = 109;
if (parent._cursor6.getRowCount()>0) { 
this.state = 104;
}if (true) break;

case 104:
//C
this.state = 105;
 //BA.debugLineNum = 593;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 105:
//for
this.state = 108;
step140 = 1;
limit140 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 134;
if (true) break;

case 134:
//C
this.state = 108;
if ((step140 > 0 && _row <= limit140) || (step140 < 0 && _row >= limit140)) this.state = 107;
if (true) break;

case 135:
//C
this.state = 134;
_row = ((int)(0 + _row + step140)) ;
if (true) break;

case 107:
//C
this.state = 135;
 //BA.debugLineNum = 594;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 595;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 108:
//C
this.state = 109;
;
 if (true) break;

case 109:
//C
this.state = 110;
;
 //BA.debugLineNum = 598;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 136;
return;
case 136:
//C
this.state = 110;
;
 //BA.debugLineNum = 599;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 600;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 601;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 602;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 603;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 137;
return;
case 137:
//C
this.state = 110;
;
 //BA.debugLineNum = 604;BA.debugLine="LOAD_CANCELLED_REASON";
_load_cancelled_reason();
 //BA.debugLineNum = 605;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 138;
return;
case 138:
//C
this.state = 110;
;
 //BA.debugLineNum = 606;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 607;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 608;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 609;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.Leng";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 610;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 611;BA.debugLine="cancelled_trigger = 0";
parent._cancelled_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 612;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 139;
return;
case 139:
//C
this.state = 110;
;
 //BA.debugLineNum = 613;BA.debugLine="GET_CANCELLED_COUNT";
_get_cancelled_count();
 //BA.debugLineNum = 614;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 //BA.debugLineNum = 615;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 if (true) break;

case 110:
//C
this.state = 111;
;
 if (true) break;

case 111:
//C
this.state = -1;
;
 //BA.debugLineNum = 619;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 634;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 635;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("735586049","Connection terminated...",0);
 //BA.debugLineNum = 636;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 637;BA.debugLine="End Sub";
return "";
}
public static void  _auto_fill() throws Exception{
ResumableSub_AUTO_FILL rsub = new ResumableSub_AUTO_FILL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_AUTO_FILL extends BA.ResumableSub {
public ResumableSub_AUTO_FILL(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
String _principal_name = "";
int _i = 0;
int _ia = 0;
int _qrow = 0;
int _row = 0;
String _query = "";
int step6;
int limit6;
int step18;
int limit18;
int step23;
int limit23;
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
 //BA.debugLineNum = 2066;BA.debugLine="Dim principal_name As String";
_principal_name = "";
 //BA.debugLineNum = 2067;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM cancelled_in";
parent._connection.ExecNonQuery("DELETE FROM cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'");
 //BA.debugLineNum = 2068;BA.debugLine="ProgressDialogShow2(\"Auto Filling...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Auto Filling..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2069;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT MAX(CAST(t";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as 'transaction_number' from cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 2070;BA.debugLine="If cursor9.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor9.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2071;BA.debugLine="For i = 0 To cursor9.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step6 = 1;
limit6 = (int) (parent._cursor9.getRowCount()-1);
_i = (int) (0) ;
this.state = 37;
if (true) break;

case 37:
//C
this.state = 13;
if ((step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6)) this.state = 6;
if (true) break;

case 38:
//C
this.state = 37;
_i = ((int)(0 + _i + step6)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2072;BA.debugLine="cursor9.Position = i";
parent._cursor9.setPosition(_i);
 //BA.debugLineNum = 2073;BA.debugLine="If cursor9.GetString(\"transaction_number\") = Nu";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor9.GetString("transaction_number")== null || (parent._cursor9.GetString("transaction_number")).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 2074;BA.debugLine="transaction_number = 1";
parent._transaction_number = BA.NumberToString(1);
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 2076;BA.debugLine="transaction_number = cursor9.GetString(\"transa";
parent._transaction_number = parent._cursor9.GetString("transaction_number");
 if (true) break;

case 12:
//C
this.state = 38;
;
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
 //BA.debugLineNum = 2081;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM can";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_ref_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 2082;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 15:
//if
this.state = 36;
if (parent._cursor4.getRowCount()>0) { 
this.state = 17;
}else {
this.state = 35;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 2083;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 39;
return;
case 39:
//C
this.state = 18;
;
 //BA.debugLineNum = 2084;BA.debugLine="For ia = 0 To cursor4.RowCount - 1";
if (true) break;

case 18:
//for
this.state = 33;
step18 = 1;
limit18 = (int) (parent._cursor4.getRowCount()-1);
_ia = (int) (0) ;
this.state = 40;
if (true) break;

case 40:
//C
this.state = 33;
if ((step18 > 0 && _ia <= limit18) || (step18 < 0 && _ia >= limit18)) this.state = 20;
if (true) break;

case 41:
//C
this.state = 40;
_ia = ((int)(0 + _ia + step18)) ;
if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 2085;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 21;
;
 //BA.debugLineNum = 2086;BA.debugLine="cursor4.Position = ia";
parent._cursor4.setPosition(_ia);
 //BA.debugLineNum = 2087;BA.debugLine="transaction_number = transaction_number + 1";
parent._transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._transaction_number))+1);
 //BA.debugLineNum = 2089;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent._cursor4.GetString("product_description")+"'")));
 //BA.debugLineNum = 2090;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 21:
//for
this.state = 24;
step23 = 1;
limit23 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 43;
if (true) break;

case 43:
//C
this.state = 24;
if ((step23 > 0 && _qrow <= limit23) || (step23 < 0 && _qrow >= limit23)) this.state = 23;
if (true) break;

case 44:
//C
this.state = 43;
_qrow = ((int)(0 + _qrow + step23)) ;
if (true) break;

case 23:
//C
this.state = 44;
 //BA.debugLineNum = 2091;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 2092;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 2093;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 if (true) break;
if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 2096;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 2097;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 25:
//if
this.state = 32;
if (parent._cursor6.getRowCount()>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 2098;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 28:
//for
this.state = 31;
step30 = 1;
limit30 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 31;
if ((step30 > 0 && _row <= limit30) || (step30 < 0 && _row >= limit30)) this.state = 30;
if (true) break;

case 46:
//C
this.state = 45;
_row = ((int)(0 + _row + step30)) ;
if (true) break;

case 30:
//C
this.state = 46;
 //BA.debugLineNum = 2099;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 2100;BA.debugLine="principal_name = cursor6.Getstring(\"principal";
_principal_name = parent._cursor6.GetString("principal_name");
 if (true) break;
if (true) break;

case 31:
//C
this.state = 32;
;
 if (true) break;

case 32:
//C
this.state = 41;
;
 //BA.debugLineNum = 2103;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 41;
;
 //BA.debugLineNum = 2105;BA.debugLine="Dim query As String = \"INSERT INTO cancelled_in";
_query = "INSERT INTO cancelled_invoice_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 2106;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._customer_id,parent.mostCurrent._label_load_customer_name.getText(),parent.mostCurrent._label_load_invoice_no.getText(),parent.mostCurrent._label_load_invoice_date.getText(),parent._transaction_number,parent._principal_id,_principal_name,parent._product_id,parent._cursor4.GetString("product_variant"),parent._cursor4.GetString("product_description"),parent._cursor4.GetString("unit"),parent._cursor4.GetString("quantity"),parent._cursor4.GetString("total_pieces"),parent._scan_code,parent._reason,parent._cancelled_reason,parent._cancelled_id,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 if (true) break;
if (true) break;

case 33:
//C
this.state = 36;
;
 //BA.debugLineNum = 2110;BA.debugLine="ToastMessageShow(\"Auto Filling Successfull\", Fal";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Auto Filling Successfull"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2111;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 48;
return;
case 48:
//C
this.state = 36;
;
 //BA.debugLineNum = 2112;BA.debugLine="LOAD_INVOICE_PRODUCT";
_load_invoice_product();
 //BA.debugLineNum = 2113;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 49;
return;
case 49:
//C
this.state = 36;
;
 //BA.debugLineNum = 2114;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 35:
//C
this.state = 36;
 if (true) break;

case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 2118;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 321;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 322;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 323;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 324;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 325;BA.debugLine="End Sub";
return null;
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 1325;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 1326;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 1327;BA.debugLine="End Sub";
return "";
}
public static void  _button_cancelled_all_click() throws Exception{
ResumableSub_BUTTON_CANCELLED_ALL_Click rsub = new ResumableSub_BUTTON_CANCELLED_ALL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CANCELLED_ALL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CANCELLED_ALL_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
int _result = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2040;BA.debugLine="Msgbox2Async(\"Are you sure you to cancelled all i";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you to cancelled all invoice?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2041;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 11;
return;
case 11:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 2042;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2043;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2044;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 2045;BA.debugLine="ls.Add(\"NOT ORDERED\")";
_ls.Add((Object)("NOT ORDERED"));
 //BA.debugLineNum = 2046;BA.debugLine="ls.Add(\"OVERSTOCK\")";
_ls.Add((Object)("OVERSTOCK"));
 //BA.debugLineNum = 2047;BA.debugLine="ls.Add(\"REFUSAL\")";
_ls.Add((Object)("REFUSAL"));
 //BA.debugLineNum = 2048;BA.debugLine="ls.Add(\"CLOSED STORE\")";
_ls.Add((Object)("CLOSED STORE"));
 //BA.debugLineNum = 2049;BA.debugLine="ls.Add(\"DOUBLE ENCODE\")";
_ls.Add((Object)("DOUBLE ENCODE"));
 //BA.debugLineNum = 2050;BA.debugLine="ls.Add(\"NO CASH OR CHEQUE AVAILABLE\")";
_ls.Add((Object)("NO CASH OR CHEQUE AVAILABLE"));
 //BA.debugLineNum = 2051;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2052;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 12;
return;
case 12:
//C
this.state = 4;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 2053;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2054;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 2055;BA.debugLine="reason = \"ALL CANCELLED\"";
parent._reason = "ALL CANCELLED";
 //BA.debugLineNum = 2056;BA.debugLine="cancelled_reason = ls.Get(Result2)";
parent._cancelled_reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 2057;BA.debugLine="cancelled_id = \"-\"";
parent._cancelled_id = "-";
 //BA.debugLineNum = 2058;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 7;
;
 //BA.debugLineNum = 2059;BA.debugLine="AUTO_FILL";
_auto_fill();
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
 //BA.debugLineNum = 2064;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_confirm_click() throws Exception{
ResumableSub_BUTTON_CONFIRM_Click rsub = new ResumableSub_BUTTON_CONFIRM_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CONFIRM_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CONFIRM_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1397;BA.debugLine="If LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = \"-\"";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._label_load_wrongserved_description.getText()).equals("-")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1398;BA.debugLine="ToastMessageShow(\"Please input a product first.\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a product first."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1400;BA.debugLine="PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, Fal";
parent.mostCurrent._panel_bg_wrongserved.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1401;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 1402;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1403;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1404;BA.debugLine="BUTTON_WRONGSERVED.Visible = True";
parent.mostCurrent._button_wrongserved.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1406;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_exit_click() throws Exception{
ResumableSub_BUTTON_EXIT_Click rsub = new ResumableSub_BUTTON_EXIT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_EXIT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_EXIT_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1005;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1006;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 1007;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1008;BA.debugLine="LOAD_INVOICE_PRODUCT";
_load_invoice_product();
 //BA.debugLineNum = 1009;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_expiration_click() throws Exception{
 //BA.debugLineNum = 1706;BA.debugLine="Sub BUTTON_EXPIRATION_Click";
 //BA.debugLineNum = 1707;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)";
mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1708;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 1709;BA.debugLine="End Sub";
return "";
}
public static String  _button_expiration_confirm_click() throws Exception{
 //BA.debugLineNum = 1693;BA.debugLine="Sub BUTTON_EXPIRATION_CONFIRM_Click";
 //BA.debugLineNum = 1694;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text = \"-\" Or LABEL_LOAD";
if ((mostCurrent._label_load_expiration.getText()).equals("-") || (mostCurrent._label_load_manufactured.getText()).equals("-")) { 
 //BA.debugLineNum = 1695;BA.debugLine="ToastMessageShow(\"Please input a expiration firs";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a expiration first."),anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 1697;BA.debugLine="INPUT_NEAR_EXPIRY";
_input_near_expiry();
 };
 //BA.debugLineNum = 1699;BA.debugLine="End Sub";
return "";
}
public static void  _button_expiration_exit_click() throws Exception{
ResumableSub_BUTTON_EXPIRATION_EXIT_Click rsub = new ResumableSub_BUTTON_EXPIRATION_EXIT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_EXPIRATION_EXIT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_EXPIRATION_EXIT_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1684;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 1685;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1686;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1687;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1688;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1689;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1690;BA.debugLine="ToastMessageShow(\"Near expiry cancel\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Near expiry cancel"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1691;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_invoice_exit_click() throws Exception{
 //BA.debugLineNum = 2035;BA.debugLine="Sub BUTTON_INVOICE_EXIT_Click";
 //BA.debugLineNum = 2036;BA.debugLine="PANEL_BG_INVOICE.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_invoice.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2038;BA.debugLine="End Sub";
return "";
}
public static String  _button_list_click() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 1024;BA.debugLine="Sub BUTTON_LIST_Click";
 //BA.debugLineNum = 1025;BA.debugLine="If cancelled_trigger = 0 Then";
if ((_cancelled_trigger).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 1026;BA.debugLine="PANEL_LIST.SetVisibleAnimated(300,True)";
mostCurrent._panel_list.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1027;BA.debugLine="PANEL_LIST.BringToFront";
mostCurrent._panel_list.BringToFront();
 //BA.debugLineNum = 1028;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1029;BA.debugLine="bg.Initialize2(Colors.RGB(0,127,255),5,0,Colors.";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (127),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1030;BA.debugLine="BUTTON_LIST.Background = bg";
mostCurrent._button_list.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1031;BA.debugLine="BUTTON_LIST.TextColor = Colors.White";
mostCurrent._button_list.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1032;BA.debugLine="cancelled_trigger = 1";
_cancelled_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1033;BA.debugLine="LOAD_LIST";
_load_list();
 }else if((_cancelled_trigger).equals(BA.NumberToString(1))) { 
 //BA.debugLineNum = 1035;BA.debugLine="PANEL_LIST.SetVisibleAnimated(300,False)";
mostCurrent._panel_list.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1036;BA.debugLine="PANEL_LIST.BringToFront";
mostCurrent._panel_list.BringToFront();
 //BA.debugLineNum = 1037;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1038;BA.debugLine="bg.Initialize2(Colors.White,5,0,Colors.Black)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1039;BA.debugLine="BUTTON_LIST.Background = bg";
mostCurrent._button_list.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1040;BA.debugLine="BUTTON_LIST.TextColor = Colors.RGB(82,169,255)";
mostCurrent._button_list.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 1041;BA.debugLine="cancelled_trigger = 0";
_cancelled_trigger = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1043;BA.debugLine="End Sub";
return "";
}
public static void  _button_save_click() throws Exception{
ResumableSub_BUTTON_SAVE_Click rsub = new ResumableSub_BUTTON_SAVE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SAVE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SAVE_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
int _i = 0;
String _query = "";
int _result = 0;
int step40;
int limit40;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1175;BA.debugLine="If LOGIN_MODULE.username <> \"\" Or LOGIN_MODULE.ta";
if (true) break;

case 1:
//if
this.state = 92;
if ((parent.mostCurrent._login_module._username /*String*/ ).equals("") == false || (parent.mostCurrent._login_module._tab_id /*String*/ ).equals("") == false) { 
this.state = 3;
}else {
this.state = 91;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1176;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"\" Or EDITTEXT_QUANT";
if (true) break;

case 4:
//if
this.state = 89;
if ((parent.mostCurrent._edittext_quantity.getText()).equals("") || (double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))<=0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 89;
 //BA.debugLineNum = 1177;BA.debugLine="Msgbox2Async(\"You cannot input a zero value qua";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You cannot input a zero value quantity."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1178;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 1179;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 93;
return;
case 93:
//C
this.state = 89;
;
 //BA.debugLineNum = 1180;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 1181;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 94;
return;
case 94:
//C
this.state = 89;
;
 //BA.debugLineNum = 1182;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1184;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then";
if (true) break;

case 9:
//if
this.state = 12;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1185;BA.debugLine="CMB_UNIT.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 1188;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = -1 Then";

case 12:
//if
this.state = 15;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 1189;BA.debugLine="CMB_REASON.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;

case 15:
//C
this.state = 16;
;
 //BA.debugLineNum = 1191;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("736765713",BA.NumberToString(1),0);
 //BA.debugLineNum = 1192;BA.debugLine="GET_TOTAL_CANCELLED";
_get_total_cancelled();
 //BA.debugLineNum = 1193;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 95;
return;
case 95:
//C
this.state = 16;
;
 //BA.debugLineNum = 1194;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("736765716",BA.NumberToString(2),0);
 //BA.debugLineNum = 1195;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmb";
if (true) break;

case 16:
//if
this.state = 29;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 18;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 20;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 22;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 24;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 26;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 28;
}if (true) break;

case 18:
//C
this.state = 29;
 //BA.debugLineNum = 1196;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 20:
//C
this.state = 29;
 //BA.debugLineNum = 1198;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 22:
//C
this.state = 29;
 //BA.debugLineNum = 1200;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 24:
//C
this.state = 29;
 //BA.debugLineNum = 1202;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 26:
//C
this.state = 29;
 //BA.debugLineNum = 1204;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 1206;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;
;
 //BA.debugLineNum = 1208;BA.debugLine="If BUTTON_SAVE.Text = \" SAVE\" Then";

case 29:
//if
this.state = 88;
if ((parent.mostCurrent._button_save.getText()).equals(" SAVE")) { 
this.state = 31;
}else {
this.state = 68;
}if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 1209;BA.debugLine="total_cancelled = total_cancelled + total_piec";
parent._total_cancelled = BA.NumberToString((double)(Double.parseDouble(parent._total_cancelled))+(double)(Double.parseDouble(parent._total_pieces)));
 //BA.debugLineNum = 1210;BA.debugLine="If total_cancelled > total_invoice Then";
if (true) break;

case 32:
//if
this.state = 66;
if ((double)(Double.parseDouble(parent._total_cancelled))>(double)(Double.parseDouble(parent._total_invoice))) { 
this.state = 34;
}else {
this.state = 36;
}if (true) break;

case 34:
//C
this.state = 66;
 //BA.debugLineNum = 1211;BA.debugLine="Msgbox2Async(\"The total pieces you cancelling";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The total pieces you cancelling is OVER to the total pieces that ordered in this invoice."),BA.ObjectToCharSequence("Over Cancelled!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 1213;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT MAX(CA";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as 'transaction_number' from cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 1214;BA.debugLine="If cursor9.RowCount > 0 Then";
if (true) break;

case 37:
//if
this.state = 50;
if (parent._cursor9.getRowCount()>0) { 
this.state = 39;
}if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 1215;BA.debugLine="For i = 0 To cursor9.RowCount - 1";
if (true) break;

case 40:
//for
this.state = 49;
step40 = 1;
limit40 = (int) (parent._cursor9.getRowCount()-1);
_i = (int) (0) ;
this.state = 96;
if (true) break;

case 96:
//C
this.state = 49;
if ((step40 > 0 && _i <= limit40) || (step40 < 0 && _i >= limit40)) this.state = 42;
if (true) break;

case 97:
//C
this.state = 96;
_i = ((int)(0 + _i + step40)) ;
if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 1216;BA.debugLine="cursor9.Position = i";
parent._cursor9.setPosition(_i);
 //BA.debugLineNum = 1217;BA.debugLine="If cursor9.GetString(\"transaction_number\")";
if (true) break;

case 43:
//if
this.state = 48;
if (parent._cursor9.GetString("transaction_number")== null || (parent._cursor9.GetString("transaction_number")).equals("")) { 
this.state = 45;
}else {
this.state = 47;
}if (true) break;

case 45:
//C
this.state = 48;
 //BA.debugLineNum = 1218;BA.debugLine="transaction_number = 1";
parent._transaction_number = BA.NumberToString(1);
 if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 1220;BA.debugLine="transaction_number = cursor9.GetString(\"tr";
parent._transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._cursor9.GetString("transaction_number")))+1);
 if (true) break;

case 48:
//C
this.state = 97;
;
 if (true) break;
if (true) break;

case 49:
//C
this.state = 50;
;
 if (true) break;
;
 //BA.debugLineNum = 1225;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = CMB_REAS";

case 50:
//if
this.state = 59;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("WRONG SERVED")) { 
this.state = 52;
}else if(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("SHORT RETURN") || parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DAMAGE")) { 
this.state = 54;
}else if(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("NEAR EXPIRY")) { 
this.state = 56;
}else {
this.state = 58;
}if (true) break;

case 52:
//C
this.state = 59;
 //BA.debugLineNum = 1226;BA.debugLine="cancelled_id = LABEL_LOAD_INVOICE_NO.Text&tr";
parent._cancelled_id = parent.mostCurrent._label_load_invoice_no.getText()+parent._transaction_number+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 1227;BA.debugLine="INPUT_WRONGSERVED";
_input_wrongserved();
 if (true) break;

case 54:
//C
this.state = 59;
 //BA.debugLineNum = 1230;BA.debugLine="cancelled_id = LABEL_LOAD_INVOICE_NO.Text&tr";
parent._cancelled_id = parent.mostCurrent._label_load_invoice_no.getText()+parent._transaction_number+parent.mostCurrent._login_module._username /*String*/ ;
 if (true) break;

case 56:
//C
this.state = 59;
 //BA.debugLineNum = 1232;BA.debugLine="cancelled_id = \"-\"";
parent._cancelled_id = "-";
 //BA.debugLineNum = 1233;BA.debugLine="INPUT_EXPIRY";
_input_expiry();
 //BA.debugLineNum = 1234;BA.debugLine="wrong_trigger = 0";
parent._wrong_trigger = BA.NumberToString(0);
 if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 1236;BA.debugLine="cancelled_id = \"-\"";
parent._cancelled_id = "-";
 //BA.debugLineNum = 1237;BA.debugLine="wrong_trigger = 0";
parent._wrong_trigger = BA.NumberToString(0);
 if (true) break;

case 59:
//C
this.state = 60;
;
 //BA.debugLineNum = 1239;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 98;
return;
case 98:
//C
this.state = 60;
;
 //BA.debugLineNum = 1241;BA.debugLine="If wrong_trigger = 0 Then";
if (true) break;

case 60:
//if
this.state = 65;
if ((parent._wrong_trigger).equals(BA.NumberToString(0))) { 
this.state = 62;
}else if((parent._wrong_trigger).equals(BA.NumberToString(1))) { 
this.state = 64;
}if (true) break;

case 62:
//C
this.state = 65;
 //BA.debugLineNum = 1242;BA.debugLine="Dim query As String = \"INSERT INTO cancelled";
_query = "INSERT INTO cancelled_invoice_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1243;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._customer_id,parent.mostCurrent._label_load_customer_name.getText(),parent.mostCurrent._label_load_invoice_no.getText(),parent.mostCurrent._label_load_invoice_date.getText(),parent._transaction_number,parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent._scan_code,parent._reason,parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._cancelled_id,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 1246;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 99;
return;
case 99:
//C
this.state = 65;
;
 //BA.debugLineNum = 1247;BA.debugLine="ToastMessageShow(\"Product Cancelled\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Cancelled"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1248;BA.debugLine="GET_CANCELLED_COUNT";
_get_cancelled_count();
 //BA.debugLineNum = 1249;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 100;
return;
case 100:
//C
this.state = 65;
;
 //BA.debugLineNum = 1250;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1251;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1252;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1253;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 101;
return;
case 101:
//C
this.state = 65;
;
 //BA.debugLineNum = 1254;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 //BA.debugLineNum = 1255;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 if (true) break;

case 64:
//C
this.state = 65;
 //BA.debugLineNum = 1257;BA.debugLine="Msgbox2Async(\"The wrong served product you i";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The wrong served product you inputted has no unit of "+parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+", Cannot proceed."),BA.ObjectToCharSequence("Warning!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 65:
//C
this.state = 66;
;
 if (true) break;

case 66:
//C
this.state = 88;
;
 if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 1261;BA.debugLine="Msgbox2Async(\"Are you sure you want to update";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update this item?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1262;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 102;
return;
case 102:
//C
this.state = 69;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1263;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 69:
//if
this.state = 87;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 71;
}if (true) break;

case 71:
//C
this.state = 72;
 //BA.debugLineNum = 1264;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 72:
//if
this.state = 86;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 74;
}if (true) break;

case 74:
//C
this.state = 75;
 //BA.debugLineNum = 1265;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = CMB_REA";
if (true) break;

case 75:
//if
this.state = 80;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("WRONG SERVED")) { 
this.state = 77;
}else {
this.state = 79;
}if (true) break;

case 77:
//C
this.state = 80;
 //BA.debugLineNum = 1266;BA.debugLine="UPDATE_WRONGSERVED";
_update_wrongserved();
 if (true) break;

case 79:
//C
this.state = 80;
 //BA.debugLineNum = 1268;BA.debugLine="wrong_trigger = 0";
parent._wrong_trigger = BA.NumberToString(0);
 if (true) break;
;
 //BA.debugLineNum = 1271;BA.debugLine="If wrong_trigger = 0 Then";

case 80:
//if
this.state = 85;
if ((parent._wrong_trigger).equals(BA.NumberToString(0))) { 
this.state = 82;
}else if((parent._wrong_trigger).equals(BA.NumberToString(1))) { 
this.state = 84;
}if (true) break;

case 82:
//C
this.state = 85;
 //BA.debugLineNum = 1272;BA.debugLine="Dim query As String = \"UPDATE cancelled_inv";
_query = "UPDATE cancelled_invoice_disc_table SET unit = ?, quantity = ?, total_pieces = ?, user_info = ?, date_registered = ? , time_registered = ? WHERE invoice_no = ? And product_description = ? and transaction_number = ?";
 //BA.debugLineNum = 1273;BA.debugLine="connection.ExecNonQuery2(query,Array As Str";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_invoice_no.getText(),parent.mostCurrent._label_load_description.getText(),parent._item_number}));
 //BA.debugLineNum = 1274;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 103;
return;
case 103:
//C
this.state = 85;
;
 //BA.debugLineNum = 1275;BA.debugLine="Dim query As String = \"UPDATE near_expiry_t";
_query = "UPDATE near_expiry_table SET unit = ?, quantity = ?, total_pieces = ?, user_info = ?, date_registered = ? , time_registered = ? WHERE invoice_no = ? And product_description = ? and transaction_number = ?";
 //BA.debugLineNum = 1276;BA.debugLine="connection.ExecNonQuery2(query,Array As Str";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_invoice_no.getText(),parent.mostCurrent._label_load_description.getText(),parent._item_number}));
 //BA.debugLineNum = 1277;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 104;
return;
case 104:
//C
this.state = 85;
;
 //BA.debugLineNum = 1278;BA.debugLine="ToastMessageShow(\"Transaction Updated\", Fal";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1279;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 1280;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 105;
return;
case 105:
//C
this.state = 85;
;
 //BA.debugLineNum = 1281;BA.debugLine="LOAD_LIST";
_load_list();
 //BA.debugLineNum = 1282;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 1283;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 if (true) break;

case 84:
//C
this.state = 85;
 //BA.debugLineNum = 1285;BA.debugLine="Msgbox2Async(\"The wrong served product you";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The wrong served product you inputted has no unit of "+parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+", Cannot proceed."),BA.ObjectToCharSequence("Warning!"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 85:
//C
this.state = 86;
;
 if (true) break;

case 86:
//C
this.state = 87;
;
 if (true) break;

case 87:
//C
this.state = 88;
;
 if (true) break;

case 88:
//C
this.state = 89;
;
 if (true) break;

case 89:
//C
this.state = 92;
;
 if (true) break;

case 91:
//C
this.state = 92;
 //BA.debugLineNum = 1294;BA.debugLine="Msgbox2Async(\"TABLET ID AND USERNAME CANNOT READ";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 92:
//C
this.state = -1;
;
 //BA.debugLineNum = 1296;BA.debugLine="End Sub";
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
public ResumableSub_BUTTON_UPLOAD_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1745;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT * FROM can";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 1746;BA.debugLine="If cursor9.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 12;
if (parent._cursor9.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1747;BA.debugLine="Msgbox2Async(\"Are you sure you want to upload th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to upload this cancelled invoice?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1748;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1749;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1750;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 8:
//C
this.state = 9;
 if (true) break;

case 9:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1755;BA.debugLine="Msgbox2Async(\"There's nothing to upload in this";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("There's nothing to upload in this invoice."),BA.ObjectToCharSequence("Empty Upload"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1758;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_wrongserved_click() throws Exception{
 //BA.debugLineNum = 1413;BA.debugLine="Sub BUTTON_WRONGSERVED_Click";
 //BA.debugLineNum = 1414;BA.debugLine="PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, True";
mostCurrent._panel_bg_wrongserved.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1415;BA.debugLine="PANEL_BG_WRONGSERVED.BringToFront";
mostCurrent._panel_bg_wrongserved.BringToFront();
 //BA.debugLineNum = 1416;BA.debugLine="End Sub";
return "";
}
public static void  _button_wrongserved_exit_click() throws Exception{
ResumableSub_BUTTON_WRONGSERVED_EXIT_Click rsub = new ResumableSub_BUTTON_WRONGSERVED_EXIT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_WRONGSERVED_EXIT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_WRONGSERVED_EXIT_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1388;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 //BA.debugLineNum = 1389;BA.debugLine="PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_wrongserved.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1390;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1391;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1392;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1393;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1394;BA.debugLine="ToastMessageShow(\"Wrong served cancel\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Wrong served cancel"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1395;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_wrongserved_manual_click() throws Exception{
ResumableSub_BUTTON_WRONGSERVED_MANUAL_Click rsub = new ResumableSub_BUTTON_WRONGSERVED_MANUAL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_WRONGSERVED_MANUAL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_WRONGSERVED_MANUAL_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;
int _qrow = 0;
int _row = 0;
int step16;
int limit16;
int step31;
int limit31;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1433;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1434;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 1435;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 1436;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 1437;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 1438;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 1439;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 1440;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1441;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 21;
return;
case 21:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 1442;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 20;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1443;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sea";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate2),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 1444;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 1445;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 22;
return;
case 22:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1446;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 19;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1448;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._searchtemplate2._selecteditem /*String*/ +"'")));
 //BA.debugLineNum = 1449;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step16 = 1;
limit16 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 23;
if (true) break;

case 23:
//C
this.state = 10;
if ((step16 > 0 && _qrow <= limit16) || (step16 < 0 && _qrow >= limit16)) this.state = 9;
if (true) break;

case 24:
//C
this.state = 23;
_qrow = ((int)(0 + _qrow + step16)) ;
if (true) break;

case 9:
//C
this.state = 24;
 //BA.debugLineNum = 1450;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1451;BA.debugLine="LABEL_LOAD_WRONGSERVED_VARIANT.Text = cursor3.";
parent.mostCurrent._label_load_wrongserved_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1452;BA.debugLine="LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = curs";
parent.mostCurrent._label_load_wrongserved_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 1453;BA.debugLine="wrong_principal_id = cursor3.GetString(\"princi";
parent._wrong_principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 1454;BA.debugLine="wrong_product_id = cursor3.GetString(\"product_";
parent._wrong_product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 1456;BA.debugLine="wrong_caseper = cursor3.GetString(\"CASE_UNIT_P";
parent._wrong_caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1457;BA.debugLine="wrong_pcsper = cursor3.GetString(\"PCS_UNIT_PER";
parent._wrong_pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1458;BA.debugLine="wrong_dozper = cursor3.GetString(\"DOZ_UNIT_PER";
parent._wrong_dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1459;BA.debugLine="wrong_boxper = cursor3.GetString(\"BOX_UNIT_PER";
parent._wrong_boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1460;BA.debugLine="wrong_bagper = cursor3.GetString(\"BAG_UNIT_PER";
parent._wrong_bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1461;BA.debugLine="wrong_packper = cursor3.GetString(\"PACK_UNIT_P";
parent._wrong_packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 1464;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._wrong_principal_id+"'")));
 //BA.debugLineNum = 1465;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 11:
//if
this.state = 18;
if (parent._cursor6.getRowCount()>0) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1466;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 14:
//for
this.state = 17;
step31 = 1;
limit31 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 25;
if (true) break;

case 25:
//C
this.state = 17;
if ((step31 > 0 && _row <= limit31) || (step31 < 0 && _row >= limit31)) this.state = 16;
if (true) break;

case 26:
//C
this.state = 25;
_row = ((int)(0 + _row + step31)) ;
if (true) break;

case 16:
//C
this.state = 26;
 //BA.debugLineNum = 1467;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 1468;BA.debugLine="LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = curso";
parent.mostCurrent._label_load_wrongserved_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 1470;BA.debugLine="wrong_reason = ls.Get(Result2)";
parent._wrong_reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 1471;BA.debugLine="wrong_scan_code = \"N/A\"";
parent._wrong_scan_code = "N/A";
 if (true) break;

case 18:
//C
this.state = 19;
;
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
 //BA.debugLineNum = 1476;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _clear() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 1308;BA.debugLine="Sub CLEAR";
 //BA.debugLineNum = 1309;BA.debugLine="item_number = 0";
_item_number = BA.NumberToString(0);
 //BA.debugLineNum = 1310;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1311;BA.debugLine="bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (255),(int) (70)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1312;BA.debugLine="BUTTON_SAVE.Background = bg";
mostCurrent._button_save.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1313;BA.debugLine="BUTTON_SAVE.Text = \" SAVE\"";
mostCurrent._button_save.setText(BA.ObjectToCharSequence(" SAVE"));
 //BA.debugLineNum = 1314;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1315;BA.debugLine="BUTTON_CANCEL.Visible = False";
mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1316;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1317;BA.debugLine="PANEL_LIST.SetVisibleAnimated(300,False)";
mostCurrent._panel_list.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1318;BA.debugLine="PANEL_LIST.BringToFront";
mostCurrent._panel_list.BringToFront();
 //BA.debugLineNum = 1319;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1320;BA.debugLine="bg.Initialize2(Colors.White,5,0,Colors.Black)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1321;BA.debugLine="BUTTON_LIST.Background = bg";
mostCurrent._button_list.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1322;BA.debugLine="BUTTON_LIST.TextColor = Colors.RGB(82,169,255)";
mostCurrent._button_list.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 1323;BA.debugLine="cancelled_trigger = 0";
_cancelled_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1324;BA.debugLine="End Sub";
return "";
}
public static String  _clear_expiration() throws Exception{
 //BA.debugLineNum = 1700;BA.debugLine="Sub CLEAR_EXPIRATION";
 //BA.debugLineNum = 1701;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"-\"";
mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1702;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"-\"";
mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1703;BA.debugLine="BUTTON_EXPIRATION.Visible = False";
mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1704;BA.debugLine="End Sub";
return "";
}
public static void  _clear_invoice() throws Exception{
ResumableSub_CLEAR_INVOICE rsub = new ResumableSub_CLEAR_INVOICE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CLEAR_INVOICE extends BA.ResumableSub {
public ResumableSub_CLEAR_INVOICE(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 2121;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = \"-\"";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 2122;BA.debugLine="LABEL_LOAD_INVOICE_DATE.Text = \"-\"";
parent.mostCurrent._label_load_invoice_date.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 2123;BA.debugLine="LABEL_LOAD_INVOICE_NO.Text = \"-\"";
parent.mostCurrent._label_load_invoice_no.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 2124;BA.debugLine="BUTTON_UPLOAD.Visible = False";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2125;BA.debugLine="BUTTON_CANCELLED_ALL.Visible = False";
parent.mostCurrent._button_cancelled_all.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2126;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 2127;BA.debugLine="LOAD_INVOICE_PRODUCT";
_load_invoice_product();
 //BA.debugLineNum = 2128;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 2130;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _clear_wrongserved() throws Exception{
 //BA.debugLineNum = 1407;BA.debugLine="Sub CLEAR_WRONGSERVED";
 //BA.debugLineNum = 1408;BA.debugLine="LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = \"-\"";
mostCurrent._label_load_wrongserved_principal.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1409;BA.debugLine="LABEL_LOAD_WRONGSERVED_VARIANT.Text = \"-\"";
mostCurrent._label_load_wrongserved_variant.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1410;BA.debugLine="LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = \"-\"";
mostCurrent._label_load_wrongserved_description.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1411;BA.debugLine="BUTTON_WRONGSERVED.Visible = False";
mostCurrent._button_wrongserved.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1412;BA.debugLine="End Sub";
return "";
}
public static String  _cmb_reason_selectedindexchanged(int _index) throws Exception{
 //BA.debugLineNum = 1328;BA.debugLine="Sub CMB_REASON_SelectedIndexChanged (Index As Int)";
 //BA.debugLineNum = 1329;BA.debugLine="If CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.I";
if (mostCurrent._cmb_reason._getselectedindex /*int*/ ()==mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("WRONG SERVED")) { 
 //BA.debugLineNum = 1330;BA.debugLine="PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, Tru";
mostCurrent._panel_bg_wrongserved.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1331;BA.debugLine="PANEL_BG_WRONGSERVED.BringToFront";
mostCurrent._panel_bg_wrongserved.BringToFront();
 //BA.debugLineNum = 1332;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 //BA.debugLineNum = 1333;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 }else if(mostCurrent._cmb_reason._getselectedindex /*int*/ ()==mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("NEAR EXPIRY")) { 
 //BA.debugLineNum = 1335;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True";
mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1336;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 1337;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 //BA.debugLineNum = 1338;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 1339;BA.debugLine="If default_reading = \"BOTH\" Or default_reading =";
if ((_default_reading).equals("BOTH") || (_default_reading).equals("Expiration Date")) { 
 //BA.debugLineNum = 1340;BA.debugLine="OpenLabel(LABEL_LOAD_EXPIRATION)";
_openlabel(mostCurrent._label_load_expiration);
 }else if((_default_reading).equals("Manufacturing Date")) { 
 //BA.debugLineNum = 1342;BA.debugLine="OpenLabel(LABEL_LOAD_MANUFACTURED)";
_openlabel(mostCurrent._label_load_manufactured);
 }else {
 };
 }else {
 //BA.debugLineNum = 1347;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1348;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1349;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 1350;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 };
 //BA.debugLineNum = 1352;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_unit_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_UNIT_SelectedIndexChanged rsub = new ResumableSub_CMB_UNIT_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_UNIT_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.cancelled_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1354;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 1355;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1356;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 1357;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 1358;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 653;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 654;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 655;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 656;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 657;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 658;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 648;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 649;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 650;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,cancelled_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 651;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 652;BA.debugLine="End Sub";
return null;
}
public static String  _daysbetweendates(long _date1,long _date2) throws Exception{
 //BA.debugLineNum = 1710;BA.debugLine="Sub DaysBetweenDates(Date1 As Long, Date2 As Long)";
 //BA.debugLineNum = 1711;BA.debugLine="Return Floor((Date2 - Date1) / DateTime.TicksPerD";
if (true) return BA.NumberToString(anywheresoftware.b4a.keywords.Common.Floor((_date2-_date1)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerDay));
 //BA.debugLineNum = 1712;BA.debugLine="End Sub";
return "";
}
public static void  _delete_cancelled_invoice_disc() throws Exception{
ResumableSub_DELETE_CANCELLED_INVOICE_DISC rsub = new ResumableSub_DELETE_CANCELLED_INVOICE_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_CANCELLED_INVOICE_DISC extends BA.ResumableSub {
public ResumableSub_DELETE_CANCELLED_INVOICE_DISC(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1761;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1762;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 1763;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Data\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Data"));
 //BA.debugLineNum = 1764;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching Data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching Data..."));
 //BA.debugLineNum = 1765;BA.debugLine="LABEL_MSGBOX1.Text = \"Loading, Please wait...\"";
parent.mostCurrent._label_msgbox1.setText(BA.ObjectToCharSequence("Loading, Please wait..."));
 //BA.debugLineNum = 1766;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_canc";
_cmd = _createcommand("delete_cancelled_invoice_disc",new Object[]{(Object)(parent.mostCurrent._label_load_invoice_no.getText())});
 //BA.debugLineNum = 1767;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1768;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1769;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1770;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1771;BA.debugLine="INSERT_CANCELLED_INVOICE_DISC";
_insert_cancelled_invoice_disc();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1773;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1774;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1775;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1776;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1777;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1778;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1779;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1780;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1781;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1782;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1784;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1785;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1788;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1789;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static void  _delete_cancelled_nearexpiry() throws Exception{
ResumableSub_DELETE_CANCELLED_NEAREXPIRY rsub = new ResumableSub_DELETE_CANCELLED_NEAREXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_CANCELLED_NEAREXPIRY extends BA.ResumableSub {
public ResumableSub_DELETE_CANCELLED_NEAREXPIRY(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1906;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_near";
_cmd = _createcommand("delete_near_expiry",new Object[]{(Object)(parent.mostCurrent._label_load_invoice_no.getText())});
 //BA.debugLineNum = 1907;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1908;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1909;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1910;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1911;BA.debugLine="INSERT_CANCELLED_NEAREXPIRY";
_insert_cancelled_nearexpiry();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1913;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1914;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1915;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1916;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1917;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1918;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1919;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1920;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1921;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1922;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1924;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1925;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1928;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1929;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_cancelled_wrongserved() throws Exception{
ResumableSub_DELETE_CANCELLED_WRONGSERVED rsub = new ResumableSub_DELETE_CANCELLED_WRONGSERVED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_CANCELLED_WRONGSERVED extends BA.ResumableSub {
public ResumableSub_DELETE_CANCELLED_WRONGSERVED(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1835;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_canc";
_cmd = _createcommand("delete_cancelled_wrong_served",new Object[]{(Object)(parent.mostCurrent._label_load_invoice_no.getText())});
 //BA.debugLineNum = 1836;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1837;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1838;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1839;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1840;BA.debugLine="INSERT_CANCELLED_WRONGSERVED";
_insert_cancelled_wrongserved();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1842;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1843;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1844;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1845;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1846;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1847;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1848;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1849;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1850;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1851;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1853;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1854;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1857;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1858;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _edittext_quantity_enterpressed() throws Exception{
 //BA.debugLineNum = 2132;BA.debugLine="Sub EDITTEXT_QUANTITY_EnterPressed";
 //BA.debugLineNum = 2133;BA.debugLine="OpenButton(BUTTON_SAVE)";
_openbutton(mostCurrent._button_save);
 //BA.debugLineNum = 2134;BA.debugLine="End Sub";
return "";
}
public static String  _get_cancelled_count() throws Exception{
int _rowq = 0;
 //BA.debugLineNum = 1297;BA.debugLine="Sub GET_CANCELLED_COUNT";
 //BA.debugLineNum = 1298;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT COUNT(prod";
_cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT COUNT(product_id) as 'count_cancelled' FROM cancelled_invoice_disc_table WHERE invoice_no = '"+mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 1299;BA.debugLine="If cursor8.RowCount > 0 Then";
if (_cursor8.getRowCount()>0) { 
 //BA.debugLineNum = 1300;BA.debugLine="For rowq = 0 To cursor8.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor8.getRowCount()-1);
_rowq = (int) (0) ;
for (;_rowq <= limit3 ;_rowq = _rowq + step3 ) {
 //BA.debugLineNum = 1301;BA.debugLine="cursor8.Position = rowq";
_cursor8.setPosition(_rowq);
 //BA.debugLineNum = 1302;BA.debugLine="BUTTON_LIST.Text = \" Cancelled(\"&cursor8.GetStr";
mostCurrent._button_list.setText(BA.ObjectToCharSequence(" Cancelled("+_cursor8.GetString("count_cancelled")+")"));
 }
};
 }else {
 //BA.debugLineNum = 1305;BA.debugLine="BUTTON_LIST.Text = \" Cancelled(0)\"";
mostCurrent._button_list.setText(BA.ObjectToCharSequence(" Cancelled(0)"));
 };
 //BA.debugLineNum = 1307;BA.debugLine="End Sub";
return "";
}
public static String  _get_exp() throws Exception{
 //BA.debugLineNum = 1566;BA.debugLine="Sub GET_EXP";
 //BA.debugLineNum = 1567;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = \"";
if ((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("01")) { 
 //BA.debugLineNum = 1568;BA.debugLine="monthexp = \"January\"";
_monthexp = "January";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("02")) { 
 //BA.debugLineNum = 1570;BA.debugLine="monthexp = \"February\"";
_monthexp = "February";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("03")) { 
 //BA.debugLineNum = 1572;BA.debugLine="monthexp = \"March\"";
_monthexp = "March";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("04")) { 
 //BA.debugLineNum = 1574;BA.debugLine="monthexp = \"April\"";
_monthexp = "April";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("05")) { 
 //BA.debugLineNum = 1576;BA.debugLine="monthexp = \"May\"";
_monthexp = "May";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("06")) { 
 //BA.debugLineNum = 1578;BA.debugLine="monthexp = \"June\"";
_monthexp = "June";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("07")) { 
 //BA.debugLineNum = 1580;BA.debugLine="monthexp = \"July\"";
_monthexp = "July";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("08")) { 
 //BA.debugLineNum = 1582;BA.debugLine="monthexp = \"August\"";
_monthexp = "August";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("09")) { 
 //BA.debugLineNum = 1584;BA.debugLine="monthexp = \"September\"";
_monthexp = "September";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("10")) { 
 //BA.debugLineNum = 1586;BA.debugLine="monthexp = \"October\"";
_monthexp = "October";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("11")) { 
 //BA.debugLineNum = 1588;BA.debugLine="monthexp = \"November\"";
_monthexp = "November";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("12")) { 
 //BA.debugLineNum = 1590;BA.debugLine="monthexp = \"December\"";
_monthexp = "December";
 }else {
 //BA.debugLineNum = 1592;BA.debugLine="monthexp = \"NO EXPIRATION\"";
_monthexp = "NO EXPIRATION";
 };
 //BA.debugLineNum = 1595;BA.debugLine="yearexp = LABEL_LOAD_EXPIRATION.Text.SubString2(0";
_yearexp = mostCurrent._label_load_expiration.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 1596;BA.debugLine="End Sub";
return "";
}
public static String  _get_expiration_span() throws Exception{
String _days_year = "";
String _days_month = "";
long _manufacturing = 0L;
 //BA.debugLineNum = 1629;BA.debugLine="Sub GET_EXPIRATION_SPAN";
 //BA.debugLineNum = 1630;BA.debugLine="Dim days_year As String";
_days_year = "";
 //BA.debugLineNum = 1631;BA.debugLine="Dim days_month As String";
_days_month = "";
 //BA.debugLineNum = 1633;BA.debugLine="If lifespan_year <> \"\" Then";
if ((_lifespan_year).equals("") == false) { 
 //BA.debugLineNum = 1634;BA.debugLine="days_year = lifespan_year.SubString2(0,lifespan_";
_days_year = _lifespan_year.substring((int) (0),(int) (_lifespan_year.indexOf("Y")-1));
 }else {
 //BA.debugLineNum = 1636;BA.debugLine="days_year = 0";
_days_year = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1639;BA.debugLine="If lifespan_month <> \"\" Then";
if ((_lifespan_month).equals("") == false) { 
 //BA.debugLineNum = 1640;BA.debugLine="days_month = lifespan_month.SubString2(0,lifespa";
_days_month = _lifespan_month.substring((int) (0),(int) (_lifespan_month.indexOf("M")-1));
 }else {
 //BA.debugLineNum = 1642;BA.debugLine="days_month = 0";
_days_month = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1645;BA.debugLine="Log(days_year & \" \" &days_month)";
anywheresoftware.b4a.keywords.Common.LogImpl("738010896",_days_year+" "+_days_month,0);
 //BA.debugLineNum = 1647;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text <> \"NO EXPIRATION\"";
if ((mostCurrent._label_load_expiration.getText()).equals("NO EXPIRATION") == false) { 
 //BA.debugLineNum = 1648;BA.debugLine="If lifespan_year = \"\" And lifespan_month = \"\" Th";
if ((_lifespan_year).equals("") && (_lifespan_month).equals("")) { 
 }else {
 //BA.debugLineNum = 1651;BA.debugLine="Dim manufacturing As Long = DateTime.Add(DateTi";
_manufacturing = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_expiration.getText()),(int) (-(double)(Double.parseDouble(_days_year))),(int) (-(double)(Double.parseDouble(_days_month))),(int) (0));
 //BA.debugLineNum = 1652;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(ma";
mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_manufacturing)));
 };
 };
 //BA.debugLineNum = 1655;BA.debugLine="End Sub";
return "";
}
public static void  _get_invoice() throws Exception{
ResumableSub_GET_INVOICE rsub = new ResumableSub_GET_INVOICE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_INVOICE extends BA.ResumableSub {
public ResumableSub_GET_INVOICE(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
wingan.app.dbrequestmanager _req1 = null;
wingan.app.main._dbcommand _cmd1 = null;
wingan.app.httpjob _js = null;
wingan.app.main._dbresult _res1 = null;
Object[] _row1 = null;
int _rows = 0;
anywheresoftware.b4a.BA.IterableList group14;
int index14;
int groupLen14;
anywheresoftware.b4a.BA.IterableList group22;
int index22;
int groupLen22;
int step53;
int limit53;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 661;BA.debugLine="ProgressDialogShow2(\"Getting Invoice...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Getting Invoice..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 662;BA.debugLine="SearchTemplate.CustomListView1.Clear";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 663;BA.debugLine="Dialog.Title = \"Select Invoice\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Invoice");
 //BA.debugLineNum = 664;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,168,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (168),(int) (255));
 //BA.debugLineNum = 665;BA.debugLine="Invoice.Initialize";
parent.mostCurrent._invoice.Initialize();
 //BA.debugLineNum = 666;BA.debugLine="Invoice.Clear";
parent.mostCurrent._invoice.Clear();
 //BA.debugLineNum = 667;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 668;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_canc";
_cmd = _createcommand("select_cancelled_pick",(Object[])(new String[]{parent.mostCurrent._return_module._route_name /*String*/ ,parent.mostCurrent._return_module._date_return /*String*/ ,parent.mostCurrent._return_module._plate_no /*String*/ }));
 //BA.debugLineNum = 669;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 37;
return;
case 37:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 670;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 32;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 31;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 671;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 672;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 38;
return;
case 38:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 673;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 29;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 28;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 674;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 26;
group14 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index14 = 0;
groupLen14 = group14.getSize();
this.state = 39;
if (true) break;

case 39:
//C
this.state = 26;
if (index14 < groupLen14) {
this.state = 9;
_row = (Object[])(group14.Get(index14));}
if (true) break;

case 40:
//C
this.state = 39;
index14++;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 677;BA.debugLine="Dim req1 As DBRequestManager = CreateRequest";
_req1 = _createrequest();
 //BA.debugLineNum = 678;BA.debugLine="Dim cmd1 As DBCommand = CreateCommand(\"select_";
_cmd1 = _createcommand("select_cancelled_invoice",(Object[])(new String[]{BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_id"))))])}));
 //BA.debugLineNum = 679;BA.debugLine="Wait For (req1.ExecuteQuery(cmd1, 0, Null)) Jo";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req1._executequery /*wingan.app.httpjob*/ (_cmd1,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 41;
return;
case 41:
//C
this.state = 10;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 680;BA.debugLine="If js.Success Then";
if (true) break;

case 10:
//if
this.state = 25;
if (_js._success /*boolean*/ ) { 
this.state = 12;
}else {
this.state = 24;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 681;BA.debugLine="req1.HandleJobAsync(js, \"req1\")";
_req1._handlejobasync /*void*/ (_js,"req1");
 //BA.debugLineNum = 682;BA.debugLine="Wait For (req1) req1_Result(res1 As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req1_result", processBA, this, (Object)(_req1));
this.state = 42;
return;
case 42:
//C
this.state = 13;
_res1 = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 683;BA.debugLine="If res1.Rows.Size > 0 Then";
if (true) break;

case 13:
//if
this.state = 22;
if (_res1.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 684;BA.debugLine="For Each row1() As Object In res1.Rows";
if (true) break;

case 16:
//for
this.state = 19;
group22 = _res1.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index22 = 0;
groupLen22 = group22.getSize();
this.state = 43;
if (true) break;

case 43:
//C
this.state = 19;
if (index22 < groupLen22) {
this.state = 18;
_row1 = (Object[])(group22.Get(index22));}
if (true) break;

case 44:
//C
this.state = 43;
index22++;
if (true) break;

case 18:
//C
this.state = 44;
 //BA.debugLineNum = 685;BA.debugLine="Invoice.Add(row1(res1.Columns.Get(\"InvoiceN";
parent.mostCurrent._invoice.Add(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("InvoiceNo"))))]);
 if (true) break;
if (true) break;

case 19:
//C
this.state = 22;
;
 //BA.debugLineNum = 687;BA.debugLine="BUTTON_UPLOAD.Visible = False";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 688;BA.debugLine="BUTTON_CANCELLED_ALL.Visible = False";
parent.mostCurrent._button_cancelled_all.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 690;BA.debugLine="Msgbox2Async(\"This picklist have no exisitin";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This picklist have no exisiting invoice"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 22:
//C
this.state = 25;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 693;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 694;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("735848226","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 695;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 696;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 25:
//C
this.state = 40;
;
 //BA.debugLineNum = 698;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 26:
//C
this.state = 29;
;
 //BA.debugLineNum = 700;BA.debugLine="SearchTemplate.SetItems(Invoice)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (parent.mostCurrent._invoice);
 //BA.debugLineNum = 701;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 702;BA.debugLine="OPEN_INVOICE";
_open_invoice();
 if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 704;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 705;BA.debugLine="Msgbox2Async(\"Picklist Route is empty, Please a";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Picklist Route is empty, Please advice IT for this conflict."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 29:
//C
this.state = 32;
;
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 708;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 709;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("735848241","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 710;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 711;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 32:
//C
this.state = 33;
;
 //BA.debugLineNum = 713;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 714;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 716;BA.debugLine="For rows = 0 To Invoice.Size - 1";
if (true) break;

case 33:
//for
this.state = 36;
step53 = 1;
limit53 = (int) (parent.mostCurrent._invoice.getSize()-1);
_rows = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 36;
if ((step53 > 0 && _rows <= limit53) || (step53 < 0 && _rows >= limit53)) this.state = 35;
if (true) break;

case 46:
//C
this.state = 45;
_rows = ((int)(0 + _rows + step53)) ;
if (true) break;

case 35:
//C
this.state = 46;
 if (true) break;
if (true) break;

case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 718;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _req1_result(wingan.app.main._dbresult _res1) throws Exception{
}
public static void  _get_invoice_products() throws Exception{
ResumableSub_GET_INVOICE_PRODUCTS rsub = new ResumableSub_GET_INVOICE_PRODUCTS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_INVOICE_PRODUCTS extends BA.ResumableSub {
public ResumableSub_GET_INVOICE_PRODUCTS(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
String _invoice_no = "";
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group11;
int index11;
int groupLen11;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 738;BA.debugLine="Dim invoice_no As String = SearchTemplate.Selecte";
_invoice_no = parent.mostCurrent._searchtemplate._selecteditem /*String*/ .toUpperCase();
 //BA.debugLineNum = 739;BA.debugLine="ProgressDialogShow2(\"Getting Invoice Products...\"";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Getting Invoice Products..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 740;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 741;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_invo";
_cmd = _createcommand("select_invoice_product",(Object[])(new String[]{_invoice_no}));
 //BA.debugLineNum = 742;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 743;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 744;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM cancelled_i";
parent._connection.ExecNonQuery("DELETE FROM cancelled_invoice_ref_table WHERE invoice_no = '"+_invoice_no+"'");
 //BA.debugLineNum = 745;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 746;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 18;
return;
case 18:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 747;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 748;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group11 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index11 = 0;
groupLen11 = group11.getSize();
this.state = 19;
if (true) break;

case 19:
//C
this.state = 10;
if (index11 < groupLen11) {
this.state = 9;
_row = (Object[])(group11.Get(index11));}
if (true) break;

case 20:
//C
this.state = 19;
index11++;
if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 749;BA.debugLine="customer_id = row(res.Columns.Get(\"customer\"))";
parent._customer_id = BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("customer"))))]);
 //BA.debugLineNum = 750;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = row(res.Column";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("account_customer_name"))))]));
 //BA.debugLineNum = 751;BA.debugLine="LABEL_LOAD_INVOICE_NO.Text = row(res.Columns.G";
parent.mostCurrent._label_load_invoice_no.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("invoice_no"))))]));
 //BA.debugLineNum = 752;BA.debugLine="LABEL_LOAD_INVOICE_DATE.Text = DateTime.Date(";
parent.mostCurrent._label_load_invoice_date.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(BA.ObjectToLongNumber(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("invoice_date"))))]))));
 //BA.debugLineNum = 753;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO cancelled";
parent._connection.ExecNonQuery("INSERT INTO cancelled_invoice_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("customer"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("account_customer_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("invoice_no"))))])+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(BA.ObjectToLongNumber(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("invoice_date"))))]))+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prod_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prod_brand"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prod_desc"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("qty"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_pcs"))))])+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+parent.mostCurrent._login_module._username /*String*/ +"','"+parent.mostCurrent._login_module._tab_id /*String*/ +"')");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 757;BA.debugLine="LOAD_INVOICE_PRODUCT";
_load_invoice_product();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 759;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 760;BA.debugLine="Msgbox2Async(\"Invoice is empty, Please advice I";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Invoice is empty, Please advice IT for this conflict."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 763;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 764;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("735979292","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 765;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 766;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 768;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 769;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 770;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_manufacturing_span() throws Exception{
String _days_year = "";
String _days_month = "";
long _expiration = 0L;
 //BA.debugLineNum = 1656;BA.debugLine="Sub GET_MANUFACTURING_SPAN";
 //BA.debugLineNum = 1657;BA.debugLine="Dim days_year As String";
_days_year = "";
 //BA.debugLineNum = 1658;BA.debugLine="Dim days_month As String";
_days_month = "";
 //BA.debugLineNum = 1660;BA.debugLine="If lifespan_year <> \"\" Then";
if ((_lifespan_year).equals("") == false) { 
 //BA.debugLineNum = 1661;BA.debugLine="days_year = lifespan_year.SubString2(0,lifespan_";
_days_year = _lifespan_year.substring((int) (0),(int) (_lifespan_year.indexOf("Y")-1));
 }else {
 //BA.debugLineNum = 1663;BA.debugLine="days_year = 0";
_days_year = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1666;BA.debugLine="If lifespan_month <> \"\" Then";
if ((_lifespan_month).equals("") == false) { 
 //BA.debugLineNum = 1667;BA.debugLine="days_month = lifespan_month.SubString2(0,lifespa";
_days_month = _lifespan_month.substring((int) (0),(int) (_lifespan_month.indexOf("M")-1));
 }else {
 //BA.debugLineNum = 1669;BA.debugLine="days_month = 0";
_days_month = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1672;BA.debugLine="Log(days_year & \" \" &days_month)";
anywheresoftware.b4a.keywords.Common.LogImpl("738076432",_days_year+" "+_days_month,0);
 //BA.debugLineNum = 1674;BA.debugLine="If LABEL_LOAD_MANUFACTURED.Text <> \"NO EXPIRATION";
if ((mostCurrent._label_load_manufactured.getText()).equals("NO EXPIRATION") == false) { 
 //BA.debugLineNum = 1675;BA.debugLine="If lifespan_year = \"\" And lifespan_month = \"\" Th";
if ((_lifespan_year).equals("") && (_lifespan_month).equals("")) { 
 }else {
 //BA.debugLineNum = 1678;BA.debugLine="Dim expiration As Long = DateTime.Add(DateTime.";
_expiration = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_manufactured.getText()),(int)(Double.parseDouble(_days_year)),(int)(Double.parseDouble(_days_month)),(int) (0));
 //BA.debugLineNum = 1679;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = DateTime.Date(expi";
mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_expiration)));
 };
 };
 //BA.debugLineNum = 1682;BA.debugLine="End Sub";
return "";
}
public static String  _get_mfg() throws Exception{
 //BA.debugLineNum = 1597;BA.debugLine="Sub GET_MFG";
 //BA.debugLineNum = 1598;BA.debugLine="If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) =";
if ((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("01")) { 
 //BA.debugLineNum = 1599;BA.debugLine="monthmfg = \"January\"";
_monthmfg = "January";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("02")) { 
 //BA.debugLineNum = 1601;BA.debugLine="monthmfg = \"February\"";
_monthmfg = "February";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("03")) { 
 //BA.debugLineNum = 1603;BA.debugLine="monthmfg = \"March\"";
_monthmfg = "March";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("04")) { 
 //BA.debugLineNum = 1605;BA.debugLine="monthmfg = \"April\"";
_monthmfg = "April";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("05")) { 
 //BA.debugLineNum = 1607;BA.debugLine="monthmfg = \"May\"";
_monthmfg = "May";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("06")) { 
 //BA.debugLineNum = 1609;BA.debugLine="monthmfg = \"June\"";
_monthmfg = "June";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("07")) { 
 //BA.debugLineNum = 1611;BA.debugLine="monthmfg = \"July\"";
_monthmfg = "July";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("08")) { 
 //BA.debugLineNum = 1613;BA.debugLine="monthmfg = \"August\"";
_monthmfg = "August";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("09")) { 
 //BA.debugLineNum = 1615;BA.debugLine="monthmfg = \"September\"";
_monthmfg = "September";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("10")) { 
 //BA.debugLineNum = 1617;BA.debugLine="monthmfg = \"October\"";
_monthmfg = "October";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("11")) { 
 //BA.debugLineNum = 1619;BA.debugLine="monthmfg = \"November\"";
_monthmfg = "November";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("12")) { 
 //BA.debugLineNum = 1621;BA.debugLine="monthmfg = \"December\"";
_monthmfg = "December";
 }else {
 //BA.debugLineNum = 1623;BA.debugLine="monthmfg = \"NO MANUFACTURING\"";
_monthmfg = "NO MANUFACTURING";
 };
 //BA.debugLineNum = 1626;BA.debugLine="yearmfg = LABEL_LOAD_MANUFACTURED.Text.SubString2";
_yearmfg = mostCurrent._label_load_manufactured.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 1627;BA.debugLine="End Sub";
return "";
}
public static String  _get_total_cancelled() throws Exception{
int _rowq = 0;
 //BA.debugLineNum = 1159;BA.debugLine="Sub GET_TOTAL_CANCELLED";
 //BA.debugLineNum = 1160;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT sum(total_";
_cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT sum(total_pieces) as 'total_cancelled' FROM cancelled_invoice_disc_table WHERE invoice_no = '"+mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 1161;BA.debugLine="If cursor7.RowCount > 0 Then";
if (_cursor7.getRowCount()>0) { 
 //BA.debugLineNum = 1162;BA.debugLine="For rowq = 0 To cursor7.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor7.getRowCount()-1);
_rowq = (int) (0) ;
for (;_rowq <= limit3 ;_rowq = _rowq + step3 ) {
 //BA.debugLineNum = 1163;BA.debugLine="cursor7.Position = rowq";
_cursor7.setPosition(_rowq);
 //BA.debugLineNum = 1164;BA.debugLine="If cursor7.GetString(\"total_cancelled\") = Null";
if (_cursor7.GetString("total_cancelled")== null || (_cursor7.GetString("total_cancelled")).equals("")) { 
 //BA.debugLineNum = 1165;BA.debugLine="total_cancelled = 0";
_total_cancelled = BA.NumberToString(0);
 }else {
 //BA.debugLineNum = 1167;BA.debugLine="total_cancelled = cursor7.GetString(\"total_can";
_total_cancelled = _cursor7.GetString("total_cancelled");
 };
 }
};
 }else {
 //BA.debugLineNum = 1171;BA.debugLine="total_cancelled = 0";
_total_cancelled = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1173;BA.debugLine="End Sub";
return "";
}
public static String  _get_wrongserved_details() throws Exception{
String _p_id = "";
int _rows = 0;
int _qrow = 0;
int _row = 0;
 //BA.debugLineNum = 1477;BA.debugLine="Sub GET_WRONGSERVED_DETAILS";
 //BA.debugLineNum = 1478;BA.debugLine="Dim p_id As String";
_p_id = "";
 //BA.debugLineNum = 1479;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM wro";
_cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM wrong_served_table WHERE cancelled_id = '"+_cancelled_id+"'")));
 //BA.debugLineNum = 1480;BA.debugLine="If cursor7.RowCount > 0 Then";
if (_cursor7.getRowCount()>0) { 
 //BA.debugLineNum = 1481;BA.debugLine="For rows= 0 To cursor7.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_cursor7.getRowCount()-1);
_rows = (int) (0) ;
for (;_rows <= limit4 ;_rows = _rows + step4 ) {
 //BA.debugLineNum = 1482;BA.debugLine="cursor7.Position = rows";
_cursor7.setPosition(_rows);
 //BA.debugLineNum = 1483;BA.debugLine="p_id = cursor7.GetString(\"product_id\")";
_p_id = _cursor7.GetString("product_id");
 }
};
 //BA.debugLineNum = 1485;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
_cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM product_table WHERE product_id ='"+_p_id+"'")));
 //BA.debugLineNum = 1486;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
{
final int step9 = 1;
final int limit9 = (int) (_cursor3.getRowCount()-1);
_qrow = (int) (0) ;
for (;_qrow <= limit9 ;_qrow = _qrow + step9 ) {
 //BA.debugLineNum = 1487;BA.debugLine="cursor3.Position = qrow";
_cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1488;BA.debugLine="LABEL_LOAD_WRONGSERVED_VARIANT.Text = cursor3.G";
mostCurrent._label_load_wrongserved_variant.setText(BA.ObjectToCharSequence(_cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1489;BA.debugLine="LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = curso";
mostCurrent._label_load_wrongserved_description.setText(BA.ObjectToCharSequence(_cursor3.GetString("product_desc")));
 //BA.debugLineNum = 1490;BA.debugLine="wrong_principal_id = cursor3.GetString(\"princip";
_wrong_principal_id = _cursor3.GetString("principal_id");
 //BA.debugLineNum = 1491;BA.debugLine="wrong_product_id = cursor3.GetString(\"product_i";
_wrong_product_id = _cursor3.GetString("product_id");
 //BA.debugLineNum = 1493;BA.debugLine="wrong_caseper = cursor3.GetString(\"CASE_UNIT_PE";
_wrong_caseper = _cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1494;BA.debugLine="wrong_pcsper = cursor3.GetString(\"PCS_UNIT_PER_";
_wrong_pcsper = _cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1495;BA.debugLine="wrong_dozper = cursor3.GetString(\"DOZ_UNIT_PER_";
_wrong_dozper = _cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1496;BA.debugLine="wrong_boxper = cursor3.GetString(\"BOX_UNIT_PER_";
_wrong_boxper = _cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1497;BA.debugLine="wrong_bagper = cursor3.GetString(\"BAG_UNIT_PER_";
_wrong_bagper = _cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1498;BA.debugLine="wrong_packper = cursor3.GetString(\"PACK_UNIT_PE";
_wrong_packper = _cursor3.GetString("PACK_UNIT_PER_PCS");
 }
};
 //BA.debugLineNum = 1501;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
_cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+_wrong_principal_id+"'")));
 //BA.debugLineNum = 1502;BA.debugLine="If cursor6.RowCount > 0 Then";
if (_cursor6.getRowCount()>0) { 
 //BA.debugLineNum = 1503;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
{
final int step24 = 1;
final int limit24 = (int) (_cursor6.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit24 ;_row = _row + step24 ) {
 //BA.debugLineNum = 1504;BA.debugLine="cursor6.Position = row";
_cursor6.setPosition(_row);
 //BA.debugLineNum = 1505;BA.debugLine="LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = cursor";
mostCurrent._label_load_wrongserved_principal.setText(BA.ObjectToCharSequence(_cursor6.GetString("principal_name")));
 }
};
 };
 };
 //BA.debugLineNum = 1509;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 339;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 340;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 341;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 342;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 343;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 346;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 89;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 94;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 97;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 98;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 100;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 101;BA.debugLine="Private NameColumn(5) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (5)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 104;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 105;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 107;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 108;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private DateTemplate As B4XDateTemplate";
mostCurrent._datetemplate = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 110;BA.debugLine="Private DateTemplate2 As B4XDateTemplate";
mostCurrent._datetemplate2 = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 111;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 112;BA.debugLine="Private SearchTemplate2 As B4XSearchTemplate";
mostCurrent._searchtemplate2 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 113;BA.debugLine="Private InputTemplate As B4XInputTemplate";
mostCurrent._inputtemplate = new wingan.app.b4xinputtemplate();
 //BA.debugLineNum = 115;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 116;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Dim Invoice As List";
mostCurrent._invoice = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 120;BA.debugLine="Private LABEL_LOAD_CUSTOMER_NAME As Label";
mostCurrent._label_load_customer_name = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Private LABEL_LOAD_INVOICE_NO As Label";
mostCurrent._label_load_invoice_no = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private LABEL_LOAD_INVOICE_DATE As Label";
mostCurrent._label_load_invoice_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private TABLE_INVOICE_PRODUCT As B4XTable";
mostCurrent._table_invoice_product = new wingan.app.b4xtable();
 //BA.debugLineNum = 124;BA.debugLine="Private PANEL_BG_INPUT As Panel";
mostCurrent._panel_bg_input = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 127;BA.debugLine="Private CMB_REASON As B4XComboBox";
mostCurrent._cmb_reason = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 128;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Private LABEL_LOAD_PRINCIPAL As Label";
mostCurrent._label_load_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Private PANEL_LIST As Panel";
mostCurrent._panel_list = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 132;BA.debugLine="Private BUTTON_LIST As Button";
mostCurrent._button_list = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 133;BA.debugLine="Private LVL_LIST As ListView";
mostCurrent._lvl_list = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 134;BA.debugLine="Private BUTTON_SAVE As Button";
mostCurrent._button_save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 135;BA.debugLine="Private BUTTON_CANCEL As Button";
mostCurrent._button_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 136;BA.debugLine="Private LABEL_LOAD_WRONGSERVED_DESCRIPTION As Lab";
mostCurrent._label_load_wrongserved_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Private LABEL_LOAD_WRONGSERVED_VARIANT As Label";
mostCurrent._label_load_wrongserved_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 138;BA.debugLine="Private LABEL_LOAD_WRONGSERVED_PRINCIPAL As Label";
mostCurrent._label_load_wrongserved_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 139;BA.debugLine="Private PANEL_BG_WRONGSERVED As Panel";
mostCurrent._panel_bg_wrongserved = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 140;BA.debugLine="Private BUTTON_WRONGSERVED As Button";
mostCurrent._button_wrongserved = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 141;BA.debugLine="Private LABEL_LOAD_MANUFACTURED As Label";
mostCurrent._label_load_manufactured = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 142;BA.debugLine="Private LABEL_LOAD_EXPIRATION As Label";
mostCurrent._label_load_expiration = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Private PANEL_BG_EXPIRATION As Panel";
mostCurrent._panel_bg_expiration = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 144;BA.debugLine="Private BUTTON_EXPIRATION As Button";
mostCurrent._button_expiration = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 145;BA.debugLine="Private BUTTON_UPLOAD As Button";
mostCurrent._button_upload = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 146;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 147;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 149;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 150;BA.debugLine="Private LVL_INVOICE As ListView";
mostCurrent._lvl_invoice = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 151;BA.debugLine="Private PANEL_BG_INVOICE As Panel";
mostCurrent._panel_bg_invoice = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 152;BA.debugLine="Private BUTTON_CANCELLED_ALL As Button";
mostCurrent._button_cancelled_all = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static void  _input_expiry() throws Exception{
ResumableSub_INPUT_EXPIRY rsub = new ResumableSub_INPUT_EXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_EXPIRY extends BA.ResumableSub {
public ResumableSub_INPUT_EXPIRY(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
String _query = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1732;BA.debugLine="GET_EXP";
_get_exp();
 //BA.debugLineNum = 1733;BA.debugLine="GET_MFG";
_get_mfg();
 //BA.debugLineNum = 1734;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1735;BA.debugLine="Dim query As String = \"INSERT INTO near_expiry_ta";
_query = "INSERT INTO near_expiry_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1736;BA.debugLine="connection.ExecNonQuery2(query,Array As String(L";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._label_load_invoice_no.getText(),parent._transaction_number,parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent._monthexp,parent._yearexp,parent.mostCurrent._label_load_expiration.getText(),parent._monthmfg,parent._yearmfg,parent.mostCurrent._label_load_manufactured.getText(),parent._scan_code,parent._reason,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 1740;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1741;BA.debugLine="ToastMessageShow(\"Product Expiration Added Succe";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Expiration Added Succesfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1742;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _input_near_expiry() throws Exception{
ResumableSub_INPUT_NEAR_EXPIRY rsub = new ResumableSub_INPUT_NEAR_EXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_NEAR_EXPIRY extends BA.ResumableSub {
public ResumableSub_INPUT_NEAR_EXPIRY(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
long _expdate = 0L;
long _datenow = 0L;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1714;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text <> \"-\" Then";
if (true) break;

case 1:
//if
this.state = 12;
if ((parent.mostCurrent._label_load_expiration.getText()).equals("-") == false) { 
this.state = 3;
}else {
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1715;BA.debugLine="Dim EXPDATE As Long = DateTime.DateParse(LABEL_L";
_expdate = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(parent.mostCurrent._label_load_expiration.getText());
 //BA.debugLineNum = 1716;BA.debugLine="Dim  DATENOW As Long = DateTime.DateParse(DateTi";
_datenow = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 1717;BA.debugLine="If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((double)(Double.parseDouble(_daysbetweendates(_datenow,_expdate)))<=0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 1718;BA.debugLine="ToastMessageShow(\"You cannot input a expiration";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You cannot input a expiration date from to date or back date."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 1720;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Fal";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1721;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 9;
;
 //BA.debugLineNum = 1722;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1723;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1724;BA.debugLine="BUTTON_EXPIRATION.Visible = True";
parent.mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 9:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1730;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _input_wrongserved() throws Exception{
String _query = "";
 //BA.debugLineNum = 1360;BA.debugLine="Sub INPUT_WRONGSERVED";
 //BA.debugLineNum = 1361;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBo";
if (mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
 //BA.debugLineNum = 1362;BA.debugLine="wrong_total_pieces = wrong_caseper * EDITTEXT_QU";
_wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(_wrong_caseper))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText())));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
 //BA.debugLineNum = 1364;BA.debugLine="wrong_total_pieces = wrong_pcsper * EDITTEXT_QUA";
_wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(_wrong_pcsper))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText())));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
 //BA.debugLineNum = 1366;BA.debugLine="wrong_total_pieces = wrong_dozper * EDITTEXT_QUA";
_wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(_wrong_dozper))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText())));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
 //BA.debugLineNum = 1368;BA.debugLine="wrong_total_pieces = wrong_boxper * EDITTEXT_QUA";
_wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(_wrong_boxper))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText())));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
 //BA.debugLineNum = 1370;BA.debugLine="wrong_total_pieces = wrong_bagper * EDITTEXT_QUA";
_wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(_wrong_bagper))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText())));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
 //BA.debugLineNum = 1372;BA.debugLine="wrong_total_pieces = wrong_packper * EDITTEXT_QU";
_wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(_wrong_packper))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText())));
 };
 //BA.debugLineNum = 1374;BA.debugLine="Log (wrong_total_pieces)";
anywheresoftware.b4a.keywords.Common.LogImpl("737158926",_wrong_total_pieces,0);
 //BA.debugLineNum = 1375;BA.debugLine="If wrong_total_pieces > 0 Then";
if ((double)(Double.parseDouble(_wrong_total_pieces))>0) { 
 //BA.debugLineNum = 1376;BA.debugLine="Log(\"open\")";
anywheresoftware.b4a.keywords.Common.LogImpl("737158928","open",0);
 //BA.debugLineNum = 1377;BA.debugLine="wrong_trigger = 0";
_wrong_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1378;BA.debugLine="Dim query As String = \"INSERT INTO wrong_served_";
_query = "INSERT INTO wrong_served_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1379;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
_connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._label_load_invoice_no.getText(),_cancelled_id,_wrong_principal_id,mostCurrent._label_load_wrongserved_principal.getText(),_product_id,mostCurrent._label_load_wrongserved_variant.getText(),mostCurrent._label_load_wrongserved_description.getText(),mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),mostCurrent._edittext_quantity.getText(),_wrong_total_pieces,_wrong_scan_code,_wrong_reason,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),mostCurrent._login_module._username /*String*/ ,mostCurrent._login_module._tab_id /*String*/ }));
 }else {
 //BA.debugLineNum = 1383;BA.debugLine="Log(\"close\")";
anywheresoftware.b4a.keywords.Common.LogImpl("737158935","close",0);
 //BA.debugLineNum = 1384;BA.debugLine="wrong_trigger = 1";
_wrong_trigger = BA.NumberToString(1);
 };
 //BA.debugLineNum = 1386;BA.debugLine="End Sub";
return "";
}
public static void  _insert_cancelled_invoice_disc() throws Exception{
ResumableSub_INSERT_CANCELLED_INVOICE_DISC rsub = new ResumableSub_INSERT_CANCELLED_INVOICE_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_CANCELLED_INVOICE_DISC extends BA.ResumableSub {
public ResumableSub_INSERT_CANCELLED_INVOICE_DISC(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1791;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1792;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT * FROM can";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 1793;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor6.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1794;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor6.getRowCount()-1);
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
 //BA.debugLineNum = 1795;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 1796;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 1797;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_ca";
_cmd = _createcommand("insert_cancelled_invoice_disc",(Object[])(new String[]{parent._cursor6.GetString("customer_id"),parent._cursor6.GetString("customer_name"),parent._cursor6.GetString("invoice_no"),parent._cursor6.GetString("invoice_date"),parent._cursor6.GetString("transaction_number"),parent._cursor6.GetString("principal_id"),parent._cursor6.GetString("principal_name"),parent._cursor6.GetString("product_id"),parent._cursor6.GetString("product_variant"),parent._cursor6.GetString("product_description"),parent._cursor6.GetString("unit"),parent._cursor6.GetString("quantity"),parent._cursor6.GetString("total_pieces"),parent._cursor6.GetString("scan_code"),parent._cursor6.GetString("input_reason"),parent._cursor6.GetString("cancelled_reason"),parent._cursor6.GetString("cancelled_id"),parent._cursor6.GetString("date_registered"),parent._cursor6.GetString("time_registered"),parent._cursor6.GetString("user_info"),parent._cursor6.GetString("tab_id")}));
 //BA.debugLineNum = 1801;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1802;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor6.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor6.GetString("product_description")));
 //BA.debugLineNum = 1803;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1804;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1807;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1808;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1809;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1810;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("738731796","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1811;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1812;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1813;BA.debugLine="Sleep(1000)";
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
 //BA.debugLineNum = 1815;BA.debugLine="js.Release";
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
 //BA.debugLineNum = 1818;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1819;BA.debugLine="If error_trigger = 0 Then";
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
 //BA.debugLineNum = 1820;BA.debugLine="DELETE_CANCELLED_WRONGSERVED";
_delete_cancelled_wrongserved();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1822;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1823;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1824;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1825;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1826;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1827;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1829;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1830;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1833;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_cancelled_nearexpiry() throws Exception{
ResumableSub_INSERT_CANCELLED_NEAREXPIRY rsub = new ResumableSub_INSERT_CANCELLED_NEAREXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_CANCELLED_NEAREXPIRY extends BA.ResumableSub {
public ResumableSub_INSERT_CANCELLED_NEAREXPIRY(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1931;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1932;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM nea";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM near_expiry_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 1933;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 27;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 26;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1934;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 13;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 29:
//C
this.state = 28;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1935;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 1936;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 30;
return;
case 30:
//C
this.state = 7;
;
 //BA.debugLineNum = 1937;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_ca";
_cmd = _createcommand("insert_cancelled_near_expiry",(Object[])(new String[]{parent._cursor2.GetString("invoice_no"),parent._cursor2.GetString("transaction_number"),parent._cursor2.GetString("principal_id"),parent._cursor2.GetString("principal_name"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("product_variant"),parent._cursor2.GetString("product_description"),parent._cursor2.GetString("unit"),parent._cursor2.GetString("quantity"),parent._cursor2.GetString("total_pieces"),parent._cursor2.GetString("month_expired"),parent._cursor2.GetString("year_expired"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("month_manufactured"),parent._cursor2.GetString("year_manufactured"),parent._cursor2.GetString("date_manufactured"),parent._cursor2.GetString("scan_code"),parent._cursor2.GetString("input_reason"),parent._cursor2.GetString("date_registered"),parent._cursor2.GetString("time_registered"),parent._cursor2.GetString("user_info"),parent._cursor2.GetString("tab_id"),"EXISTING"}));
 //BA.debugLineNum = 1942;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1943;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor2.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor2.GetString("product_description")));
 //BA.debugLineNum = 1944;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 31;
return;
case 31:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1945;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1948;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1949;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1950;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1951;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("738993941","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1952;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1953;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1954;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 29;
;
 //BA.debugLineNum = 1956;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1958;BA.debugLine="If error_trigger = 0 Then";

case 13:
//if
this.state = 24;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 24;
 //BA.debugLineNum = 1959;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1960;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploaded Successfully...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploaded Successfully..."));
 //BA.debugLineNum = 1961;BA.debugLine="ToastMessageShow(\"Uploaded Successfully\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploaded Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1962;BA.debugLine="LOG_INVOICE_UPLOAD";
_log_invoice_upload();
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1964;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1965;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1966;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1967;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1968;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 18:
//if
this.state = 23;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 20;
}else {
this.state = 22;
}if (true) break;

case 20:
//C
this.state = 23;
 //BA.debugLineNum = 1969;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 1971;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1972;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 23:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = 27;
;
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 1976;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1977;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploaded Successfully...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploaded Successfully..."));
 //BA.debugLineNum = 1978;BA.debugLine="ToastMessageShow(\"Uploaded Successfully\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploaded Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1979;BA.debugLine="LOG_INVOICE_UPLOAD";
_log_invoice_upload();
 //BA.debugLineNum = 1980;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 34;
return;
case 34:
//C
this.state = 27;
;
 //BA.debugLineNum = 1981;BA.debugLine="CLEAR_INVOICE";
_clear_invoice();
 //BA.debugLineNum = 1982;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 27;
;
 //BA.debugLineNum = 1983;BA.debugLine="OPEN_INVOICE";
_open_invoice();
 if (true) break;

case 27:
//C
this.state = -1;
;
 //BA.debugLineNum = 1986;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_cancelled_wrongserved() throws Exception{
ResumableSub_INSERT_CANCELLED_WRONGSERVED rsub = new ResumableSub_INSERT_CANCELLED_WRONGSERVED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_CANCELLED_WRONGSERVED extends BA.ResumableSub {
public ResumableSub_INSERT_CANCELLED_WRONGSERVED(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1860;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1861;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM wro";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM wrong_served_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'")));
 //BA.debugLineNum = 1862;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 27;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 26;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1863;BA.debugLine="For i = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor4.getRowCount()-1);
_i = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 13;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 29:
//C
this.state = 28;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1864;BA.debugLine="cursor4.Position = i";
parent._cursor4.setPosition(_i);
 //BA.debugLineNum = 1865;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 30;
return;
case 30:
//C
this.state = 7;
;
 //BA.debugLineNum = 1866;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_ca";
_cmd = _createcommand("insert_cancelled_wrong_served",(Object[])(new String[]{parent._cursor4.GetString("invoice_no"),parent._cursor4.GetString("cancelled_id"),parent._cursor4.GetString("principal_id"),parent._cursor4.GetString("principal_name"),parent._cursor4.GetString("product_id"),parent._cursor4.GetString("product_variant"),parent._cursor4.GetString("product_description"),parent._cursor4.GetString("unit"),parent._cursor4.GetString("quantity"),parent._cursor4.GetString("total_pieces"),parent._cursor4.GetString("scan_code"),parent._cursor4.GetString("input_reason"),parent._cursor4.GetString("date_registered"),parent._cursor4.GetString("time_registered"),parent._cursor4.GetString("user_info"),parent._cursor4.GetString("tab_id")}));
 //BA.debugLineNum = 1870;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1871;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor4.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor4.GetString("product_description")));
 //BA.debugLineNum = 1872;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 31;
return;
case 31:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1873;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1876;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1877;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1878;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1879;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("738862868","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1880;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1881;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1882;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 29;
;
 //BA.debugLineNum = 1884;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1886;BA.debugLine="If error_trigger = 0 Then";

case 13:
//if
this.state = 24;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 24;
 //BA.debugLineNum = 1887;BA.debugLine="DELETE_CANCELLED_NEAREXPIRY";
_delete_cancelled_nearexpiry();
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1889;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1890;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1891;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1892;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1893;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 18:
//if
this.state = 23;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 20;
}else {
this.state = 22;
}if (true) break;

case 20:
//C
this.state = 23;
 //BA.debugLineNum = 1894;BA.debugLine="DELETE_CANCELLED_INVOICE_DISC";
_delete_cancelled_invoice_disc();
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 1896;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1897;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 23:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = 27;
;
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 1901;BA.debugLine="DELETE_CANCELLED_NEAREXPIRY";
_delete_cancelled_nearexpiry();
 if (true) break;

case 27:
//C
this.state = -1;
;
 //BA.debugLineNum = 1903;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 34;
return;
case 34:
//C
this.state = -1;
;
 //BA.debugLineNum = 1904;BA.debugLine="End Sub";
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
public ResumableSub_LABEL_LOAD_EXPIRATION_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1539;BA.debugLine="Dialog.Title = \"Select Expiration Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Expiration Date");
 //BA.debugLineNum = 1540;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(255,109,81)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (109),(int) (81));
 //BA.debugLineNum = 1541;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)("NO EXP"),(Object)("CANCEL")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1542;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 1543;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = DateTime.Date(DateT";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 //BA.debugLineNum = 1547;BA.debugLine="GET_EXPIRATION_SPAN";
_get_expiration_span();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1549;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"-\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("-"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1551;BA.debugLine="End Sub";
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
public ResumableSub_LABEL_LOAD_MANUFACTURED_Click(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1553;BA.debugLine="Dialog.Title = \"Select Manufacturing Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Manufacturing Date");
 //BA.debugLineNum = 1554;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(91,255,81)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (91),(int) (255),(int) (81));
 //BA.debugLineNum = 1555;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate2, \"\",";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate2),(Object)(""),(Object)("NO MFG"),(Object)("CANCEL")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1556;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 1557;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(Dat";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate2._getdate /*long*/ ())));
 //BA.debugLineNum = 1561;BA.debugLine="GET_MANUFACTURING_SPAN";
_get_manufacturing_span();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1563;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"-\"";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("-"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1565;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_cancelled_reason() throws Exception{
 //BA.debugLineNum = 1010;BA.debugLine="Sub LOAD_CANCELLED_REASON";
 //BA.debugLineNum = 1011;BA.debugLine="CMB_REASON.cmbBox.Clear";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1012;BA.debugLine="CMB_REASON.cmbBox.Add(\"NOT ORDERED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NOT ORDERED");
 //BA.debugLineNum = 1013;BA.debugLine="CMB_REASON.cmbBox.Add(\"OVERSTOCK\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("OVERSTOCK");
 //BA.debugLineNum = 1014;BA.debugLine="CMB_REASON.cmbBox.Add(\"NEAR EXPIRY\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NEAR EXPIRY");
 //BA.debugLineNum = 1015;BA.debugLine="CMB_REASON.cmbBox.Add(\"EXPIRED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("EXPIRED");
 //BA.debugLineNum = 1016;BA.debugLine="CMB_REASON.cmbBox.Add(\"DAMAGED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DAMAGED");
 //BA.debugLineNum = 1017;BA.debugLine="CMB_REASON.cmbBox.Add(\"SHORT RETURN\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("SHORT RETURN");
 //BA.debugLineNum = 1018;BA.debugLine="CMB_REASON.cmbBox.Add(\"WRONG SERVED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WRONG SERVED");
 //BA.debugLineNum = 1019;BA.debugLine="CMB_REASON.cmbBox.Add(\"REFUSAL\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("REFUSAL");
 //BA.debugLineNum = 1020;BA.debugLine="CMB_REASON.cmbBox.Add(\"CLOSED STORE\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CLOSED STORE");
 //BA.debugLineNum = 1021;BA.debugLine="CMB_REASON.cmbBox.Add(\"NO CASH OR CHEQUE AVAILABL";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NO CASH OR CHEQUE AVAILABLE");
 //BA.debugLineNum = 1022;BA.debugLine="End Sub";
return "";
}
public static void  _load_invoice_product() throws Exception{
ResumableSub_LOAD_INVOICE_PRODUCT rsub = new ResumableSub_LOAD_INVOICE_PRODUCT(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_INVOICE_PRODUCT extends BA.ResumableSub {
public ResumableSub_LOAD_INVOICE_PRODUCT(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _ic = 0;
Object[] _row = null;
int _ia = 0;
int step8;
int limit8;
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
 //BA.debugLineNum = 780;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 1;
;
 //BA.debugLineNum = 781;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 782;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = 1;
;
 //BA.debugLineNum = 783;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 784;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 785;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT a.*, b.qua";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.quantity as 'invoice_qty' FROM (SELECT *,sum(quantity) as 'cancelled_qty' FROM cancelled_invoice_disc_table GROUP BY invoice_no,product_id,unit) as a "+"LEFT JOIN cancelled_invoice_ref_table As b "+"ON a.invoice_no = b.invoice_no AND a.product_id = b.product_id AND a.unit = b.unit "+"WHERE a.invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' ORDER BY a.product_variant, a.product_description ASC")));
 //BA.debugLineNum = 789;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 790;BA.debugLine="For ic = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 11;
step8 = 1;
limit8 = (int) (parent._cursor1.getRowCount()-1);
_ic = (int) (0) ;
this.state = 37;
if (true) break;

case 37:
//C
this.state = 11;
if ((step8 > 0 && _ic <= limit8) || (step8 < 0 && _ic >= limit8)) this.state = 6;
if (true) break;

case 38:
//C
this.state = 37;
_ic = ((int)(0 + _ic + step8)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 791;BA.debugLine="cursor1.Position = ic";
parent._cursor1.setPosition(_ic);
 //BA.debugLineNum = 792;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 39;
return;
case 39:
//C
this.state = 7;
;
 //BA.debugLineNum = 793;BA.debugLine="If cursor1.GetString(\"invoice_qty\") = Null Or c";
if (true) break;

case 7:
//if
this.state = 10;
if (parent._cursor1.GetString("invoice_qty")== null || (parent._cursor1.GetString("invoice_qty")).equals("")) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 794;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 795;BA.debugLine="row(0) = cursor1.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor1.GetString("product_variant"));
 //BA.debugLineNum = 796;BA.debugLine="row(1) = cursor1.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor1.GetString("product_description"));
 //BA.debugLineNum = 797;BA.debugLine="row(2) = cursor1.GetString(\"unit\")";
_row[(int) (2)] = (Object)(parent._cursor1.GetString("unit"));
 //BA.debugLineNum = 798;BA.debugLine="row(3) = 0";
_row[(int) (3)] = (Object)(0);
 //BA.debugLineNum = 799;BA.debugLine="row(4) = cursor1.GetString(\"cancelled_qty\")";
_row[(int) (4)] = (Object)(parent._cursor1.GetString("cancelled_qty"));
 //BA.debugLineNum = 800;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 10:
//C
this.state = 38;
;
 if (true) break;
if (true) break;

case 11:
//C
this.state = 14;
;
 if (true) break;

case 13:
//C
this.state = 14;
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 806;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT a.*, b.tot";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.total_quantity as 'cancelled_qty' FROM cancelled_invoice_ref_table as a "+"LEFT JOIN (SELECT *,sum(quantity) as 'total_quantity' FROM cancelled_invoice_disc_table GROUP BY invoice_no,product_id,unit) As b "+"ON a.invoice_no = b.invoice_no AND a.product_id = b.product_id AND a.unit = b.unit "+"WHERE a.invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' ORDER BY a.product_variant, a.product_description ASC")));
 //BA.debugLineNum = 810;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 15:
//if
this.state = 30;
if (parent._cursor5.getRowCount()>0) { 
this.state = 17;
}else {
this.state = 29;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 811;BA.debugLine="For ia = 0 To cursor5.RowCount - 1";
if (true) break;

case 18:
//for
this.state = 27;
step25 = 1;
limit25 = (int) (parent._cursor5.getRowCount()-1);
_ia = (int) (0) ;
this.state = 40;
if (true) break;

case 40:
//C
this.state = 27;
if ((step25 > 0 && _ia <= limit25) || (step25 < 0 && _ia >= limit25)) this.state = 20;
if (true) break;

case 41:
//C
this.state = 40;
_ia = ((int)(0 + _ia + step25)) ;
if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 812;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 42;
return;
case 42:
//C
this.state = 21;
;
 //BA.debugLineNum = 813;BA.debugLine="cursor5.Position = ia";
parent._cursor5.setPosition(_ia);
 //BA.debugLineNum = 814;BA.debugLine="If cursor5.GetString(\"cancelled_qty\") = Null Or";
if (true) break;

case 21:
//if
this.state = 26;
if (parent._cursor5.GetString("cancelled_qty")== null || (parent._cursor5.GetString("cancelled_qty")).equals("")) { 
this.state = 23;
}else {
this.state = 25;
}if (true) break;

case 23:
//C
this.state = 26;
 //BA.debugLineNum = 815;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 43;
return;
case 43:
//C
this.state = 26;
;
 //BA.debugLineNum = 816;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 817;BA.debugLine="row(0) = cursor5.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor5.GetString("product_variant"));
 //BA.debugLineNum = 818;BA.debugLine="row(1) = cursor5.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor5.GetString("product_description"));
 //BA.debugLineNum = 819;BA.debugLine="row(2) = cursor5.GetString(\"unit\")";
_row[(int) (2)] = (Object)(parent._cursor5.GetString("unit"));
 //BA.debugLineNum = 820;BA.debugLine="row(3) = cursor5.GetString(\"quantity\")";
_row[(int) (3)] = (Object)(parent._cursor5.GetString("quantity"));
 //BA.debugLineNum = 821;BA.debugLine="row(4) = 0";
_row[(int) (4)] = (Object)(0);
 //BA.debugLineNum = 822;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 824;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 825;BA.debugLine="row(0) = cursor5.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor5.GetString("product_variant"));
 //BA.debugLineNum = 826;BA.debugLine="row(1) = cursor5.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor5.GetString("product_description"));
 //BA.debugLineNum = 827;BA.debugLine="row(2) = cursor5.GetString(\"unit\")";
_row[(int) (2)] = (Object)(parent._cursor5.GetString("unit"));
 //BA.debugLineNum = 828;BA.debugLine="row(3) = cursor5.GetString(\"quantity\")";
_row[(int) (3)] = (Object)(parent._cursor5.GetString("quantity"));
 //BA.debugLineNum = 829;BA.debugLine="row(4) = cursor5.GetString(\"cancelled_qty\")";
_row[(int) (4)] = (Object)(parent._cursor5.GetString("cancelled_qty"));
 //BA.debugLineNum = 830;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 26:
//C
this.state = 41;
;
 if (true) break;
if (true) break;

case 27:
//C
this.state = 30;
;
 //BA.debugLineNum = 834;BA.debugLine="TABLE_INVOICE_PRODUCT.RowHeight = 50dip";
parent.mostCurrent._table_invoice_product._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 835;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 44;
return;
case 44:
//C
this.state = 30;
;
 //BA.debugLineNum = 837;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 45;
return;
case 45:
//C
this.state = 30;
;
 //BA.debugLineNum = 838;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 839;BA.debugLine="BUTTON_UPLOAD.Visible = True";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 840;BA.debugLine="BUTTON_CANCELLED_ALL.Visible = True";
parent.mostCurrent._button_cancelled_all.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 842;BA.debugLine="ToastMessageShow(\"Invoice is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Invoice is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 30:
//C
this.state = 31;
;
 //BA.debugLineNum = 844;BA.debugLine="TABLE_INVOICE_PRODUCT.SetData(Data)";
parent.mostCurrent._table_invoice_product._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 845;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 31:
//if
this.state = 34;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 33;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 846;BA.debugLine="XSelections.Initialize(TABLE_INVOICE_PRODUCT)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_invoice_product);
 //BA.debugLineNum = 847;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 34:
//C
this.state = -1;
;
 //BA.debugLineNum = 849;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 46;
return;
case 46:
//C
this.state = -1;
;
 //BA.debugLineNum = 850;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_invoice_product_header() throws Exception{
 //BA.debugLineNum = 772;BA.debugLine="Sub LOAD_INVOICE_PRODUCT_HEADER";
 //BA.debugLineNum = 773;BA.debugLine="NameColumn(0)=TABLE_INVOICE_PRODUCT.AddColumn(\"Pr";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_invoice_product._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._table_invoice_product._column_type_text /*int*/ );
 //BA.debugLineNum = 774;BA.debugLine="NameColumn(1)=TABLE_INVOICE_PRODUCT.AddColumn(\"Pr";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_invoice_product._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._table_invoice_product._column_type_text /*int*/ );
 //BA.debugLineNum = 775;BA.debugLine="NameColumn(2)=TABLE_INVOICE_PRODUCT.AddColumn(\"Un";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_invoice_product._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Unit",mostCurrent._table_invoice_product._column_type_text /*int*/ );
 //BA.debugLineNum = 776;BA.debugLine="NameColumn(3)=TABLE_INVOICE_PRODUCT.AddColumn(\"In";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_invoice_product._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Invoiced",mostCurrent._table_invoice_product._column_type_text /*int*/ );
 //BA.debugLineNum = 777;BA.debugLine="NameColumn(4)=TABLE_INVOICE_PRODUCT.AddColumn(\"Ca";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_invoice_product._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Cancelled",mostCurrent._table_invoice_product._column_type_text /*int*/ );
 //BA.debugLineNum = 778;BA.debugLine="End Sub";
return "";
}
public static void  _load_invoice_upload() throws Exception{
ResumableSub_LOAD_INVOICE_UPLOAD rsub = new ResumableSub_LOAD_INVOICE_UPLOAD(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_INVOICE_UPLOAD extends BA.ResumableSub {
public ResumableSub_LOAD_INVOICE_UPLOAD(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _i = 0;
int _rows = 0;
int step20;
int limit20;
int step23;
int limit23;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1996;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1998;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1999;BA.debugLine="LVL_INVOICE.Background = bg";
parent.mostCurrent._lvl_invoice.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 2000;BA.debugLine="LVL_INVOICE.Clear";
parent.mostCurrent._lvl_invoice.Clear();
 //BA.debugLineNum = 2001;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 1;
;
 //BA.debugLineNum = 2002;BA.debugLine="LVL_INVOICE.TwoLinesLayout.Label.Typeface = Typef";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 2003;BA.debugLine="LVL_INVOICE.TwoLinesLayout.Label.TextSize = 20";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 2004;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.Top = -1%y";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setTop((int) (-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)));
 //BA.debugLineNum = 2005;BA.debugLine="LVL_INVOICE.TwoLinesLayout.label.Height = 6%y";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2006;BA.debugLine="LVL_INVOICE.TwoLinesLayout.Label.TextColor = Colo";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2007;BA.debugLine="LVL_INVOICE.TwoLinesLayout.Label.Gravity = Gravit";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2008;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.Typeface =";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 2009;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.Top = 4.3%";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4.3),mostCurrent.activityBA));
 //BA.debugLineNum = 2010;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.TextSize =";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 2011;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.Height = 3";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA));
 //BA.debugLineNum = 2012;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.TextColor";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 2013;BA.debugLine="LVL_INVOICE.TwoLinesLayout.SecondLabel.Gravity =";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 2014;BA.debugLine="LVL_INVOICE.TwoLinesLayout.ItemHeight = 8%y";
parent.mostCurrent._lvl_invoice.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2015;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 1;
;
 //BA.debugLineNum = 2016;BA.debugLine="For i = 0 To Invoice.Size - 1";
if (true) break;

case 1:
//for
this.state = 18;
step20 = 1;
limit20 = (int) (parent.mostCurrent._invoice.getSize()-1);
_i = (int) (0) ;
this.state = 21;
if (true) break;

case 21:
//C
this.state = 18;
if ((step20 > 0 && _i <= limit20) || (step20 < 0 && _i >= limit20)) this.state = 3;
if (true) break;

case 22:
//C
this.state = 21;
_i = ((int)(0 + _i + step20)) ;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 2017;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT a.*,b.upl";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*,b.upload_date,b.upload_time, b.user_info as 'upload_user' FROM (SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"+BA.ObjectToString(parent.mostCurrent._invoice.Get(_i))+"' GROUP BY invoice_no) as a "+"LEFT JOIN cancelled_upload_table As b "+"ON a.invoice_no = b.invoice_no")));
 //BA.debugLineNum = 2020;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 17;
if (parent._cursor3.getRowCount()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 2021;BA.debugLine="For rows= 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 16;
step23 = 1;
limit23 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 23;
if (true) break;

case 23:
//C
this.state = 16;
if ((step23 > 0 && _rows <= limit23) || (step23 < 0 && _rows >= limit23)) this.state = 9;
if (true) break;

case 24:
//C
this.state = 23;
_rows = ((int)(0 + _rows + step23)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 2022;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 2023;BA.debugLine="If cursor3.GetString(\"upload_date\") = Null Or";
if (true) break;

case 10:
//if
this.state = 15;
if (parent._cursor3.GetString("upload_date")== null || (parent._cursor3.GetString("upload_date")).equals("")) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 2024;BA.debugLine="LVL_INVOICE.AddTwoLines2(cursor3.GetString(\"i";
parent.mostCurrent._lvl_invoice.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor3.GetString("invoice_no")+" - SAVED"),BA.ObjectToCharSequence("User : "+parent._cursor3.GetString("user_info")),(Object)(parent._cursor3.GetString("invoice_no")));
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 2026;BA.debugLine="LVL_INVOICE.AddTwoLines2(cursor3.GetString(\"i";
parent.mostCurrent._lvl_invoice.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor3.GetString("invoice_no")+" - UPLOADED"),BA.ObjectToCharSequence("Upload by : "+parent._cursor3.GetString("upload_user")+" Date Time : "+parent._cursor3.GetString("upload_date")+" "+parent._cursor3.GetString("upload_time")),(Object)(parent._cursor3.GetString("invoice_no")));
 if (true) break;

case 15:
//C
this.state = 24;
;
 if (true) break;
if (true) break;

case 16:
//C
this.state = 17;
;
 if (true) break;

case 17:
//C
this.state = 22;
;
 if (true) break;
if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 2031;BA.debugLine="PANEL_BG_INVOICE.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_invoice.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2032;BA.debugLine="PANEL_BG_INVOICE.BringToFront";
parent.mostCurrent._panel_bg_invoice.BringToFront();
 //BA.debugLineNum = 2033;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_list() throws Exception{
ResumableSub_LOAD_LIST rsub = new ResumableSub_LOAD_LIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_LIST extends BA.ResumableSub {
public ResumableSub_LOAD_LIST(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _row = 0;
int _rows = 0;
int step21;
int limit21;
int step27;
int limit27;
int step35;
int limit35;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1045;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1047;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1048;BA.debugLine="LVL_LIST.Background = bg";
parent.mostCurrent._lvl_list.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1049;BA.debugLine="LVL_LIST.Clear";
parent.mostCurrent._lvl_list.Clear();
 //BA.debugLineNum = 1050;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 33;
return;
case 33:
//C
this.state = 1;
;
 //BA.debugLineNum = 1051;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.Typeface = Typeface";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1052;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.TextSize = 20";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 1053;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Top = -1%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTop((int) (-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)));
 //BA.debugLineNum = 1054;BA.debugLine="LVL_LIST.TwoLinesLayout.label.Height = 6%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 1055;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.TextColor = Colors.";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1056;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.Gravity = Gravity.C";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 1057;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Typeface = Ty";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1058;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Top = 4.3%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4.3),mostCurrent.activityBA));
 //BA.debugLineNum = 1059;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.TextSize = 14";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 1060;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Height = 3%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA));
 //BA.debugLineNum = 1061;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.TextColor = C";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 1062;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Gravity = Gra";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 1063;BA.debugLine="LVL_LIST.TwoLinesLayout.ItemHeight = 8%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1065;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM can";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' ORDER BY transaction_number ASC")));
 //BA.debugLineNum = 1066;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 32;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1067;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 31;
step21 = 1;
limit21 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 34;
if (true) break;

case 34:
//C
this.state = 31;
if ((step21 > 0 && _row <= limit21) || (step21 < 0 && _row >= limit21)) this.state = 6;
if (true) break;

case 35:
//C
this.state = 34;
_row = ((int)(0 + _row + step21)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1068;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 1069;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = 7;
;
 //BA.debugLineNum = 1070;BA.debugLine="If cursor2.GetString(\"cancelled_reason\") = \"WRO";
if (true) break;

case 7:
//if
this.state = 30;
if ((parent._cursor2.GetString("cancelled_reason")).equals("WRONG SERVED")) { 
this.state = 9;
}else if((parent._cursor2.GetString("cancelled_reason")).equals("NEAR EXPIRY")) { 
this.state = 19;
}else {
this.state = 29;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1071;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM wrong_served_table WHERE cancelled_id = '"+parent._cursor2.GetString("cancelled_id")+"'")));
 //BA.debugLineNum = 1072;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 10:
//if
this.state = 17;
if (parent._cursor3.getRowCount()>0) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1073;BA.debugLine="For rows= 0 To cursor3.RowCount - 1";
if (true) break;

case 13:
//for
this.state = 16;
step27 = 1;
limit27 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 37;
if (true) break;

case 37:
//C
this.state = 16;
if ((step27 > 0 && _rows <= limit27) || (step27 < 0 && _rows >= limit27)) this.state = 15;
if (true) break;

case 38:
//C
this.state = 37;
_rows = ((int)(0 + _rows + step27)) ;
if (true) break;

case 15:
//C
this.state = 38;
 //BA.debugLineNum = 1074;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 1075;BA.debugLine="LVL_LIST.AddTwoLines2(cursor2.GetString(\"qua";
parent.mostCurrent._lvl_list.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor2.GetString("quantity")+" "+parent._cursor2.GetString("unit")),BA.ObjectToCharSequence(parent._cursor2.GetString("cancelled_reason")+" ("+parent._cursor3.GetString("product_variant")+" - "+parent._cursor3.GetString("product_description")+")"),(Object)(parent._cursor2.GetString("transaction_number")));
 if (true) break;
if (true) break;

case 16:
//C
this.state = 17;
;
 if (true) break;

case 17:
//C
this.state = 30;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1079;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM near_expiry_table WHERE invoice_no = '"+parent._cursor2.GetString("invoice_no")+"' AND transaction_number = '"+parent._cursor2.GetString("transaction_number")+"'")));
 //BA.debugLineNum = 1080;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 20:
//if
this.state = 27;
if (parent._cursor3.getRowCount()>0) { 
this.state = 22;
}if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 1081;BA.debugLine="For rows= 0 To cursor3.RowCount - 1";
if (true) break;

case 23:
//for
this.state = 26;
step35 = 1;
limit35 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 39;
if (true) break;

case 39:
//C
this.state = 26;
if ((step35 > 0 && _rows <= limit35) || (step35 < 0 && _rows >= limit35)) this.state = 25;
if (true) break;

case 40:
//C
this.state = 39;
_rows = ((int)(0 + _rows + step35)) ;
if (true) break;

case 25:
//C
this.state = 40;
 //BA.debugLineNum = 1082;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 1083;BA.debugLine="LVL_LIST.AddTwoLines2(cursor2.GetString(\"qua";
parent.mostCurrent._lvl_list.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor2.GetString("quantity")+" "+parent._cursor2.GetString("unit")),BA.ObjectToCharSequence(parent._cursor2.GetString("cancelled_reason")+" (EXP DATE :"+parent._cursor3.GetString("date_expired")+")"),(Object)(parent._cursor2.GetString("transaction_number")));
 if (true) break;
if (true) break;

case 26:
//C
this.state = 27;
;
 if (true) break;

case 27:
//C
this.state = 30;
;
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 1087;BA.debugLine="LVL_LIST.AddTwoLines2(cursor2.GetString(\"quant";
parent.mostCurrent._lvl_list.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor2.GetString("quantity")+" "+parent._cursor2.GetString("unit")),BA.ObjectToCharSequence(parent._cursor2.GetString("cancelled_reason")),(Object)(parent._cursor2.GetString("transaction_number")));
 if (true) break;

case 30:
//C
this.state = 35;
;
 if (true) break;
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
 //BA.debugLineNum = 1092;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1093;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_invoice_upload() throws Exception{
ResumableSub_LOG_INVOICE_UPLOAD rsub = new ResumableSub_LOG_INVOICE_UPLOAD(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_INVOICE_UPLOAD extends BA.ResumableSub {
public ResumableSub_LOG_INVOICE_UPLOAD(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
String _query = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1989;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM cancelled_up";
parent._connection.ExecNonQuery("DELETE FROM cancelled_upload_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"'");
 //BA.debugLineNum = 1990;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1991;BA.debugLine="Dim query As String = \"INSERT INTO cancelled_uplo";
_query = "INSERT INTO cancelled_upload_table VALUES (?,?,?,?,?)";
 //BA.debugLineNum = 1992;BA.debugLine="connection.ExecNonQuery2(query,Array As String(LA";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._label_load_invoice_no.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 1993;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _lvl_list_itemclick(int _position,Object _value) throws Exception{
ResumableSub_LVL_LIST_ItemClick rsub = new ResumableSub_LVL_LIST_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LVL_LIST_ItemClick extends BA.ResumableSub {
public ResumableSub_LVL_LIST_ItemClick(wingan.app.cancelled_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.cancelled_module parent;
int _position;
Object _value;
int _row = 0;
int _result = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg2 = null;
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
 //BA.debugLineNum = 1095;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM can";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+parent.mostCurrent._label_load_description.getText()+"' and transaction_number = '"+BA.ObjectToString(_value)+"'")));
 //BA.debugLineNum = 1096;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 18;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1097;BA.debugLine="For row = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 17;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
_row = (int) (0) ;
this.state = 19;
if (true) break;

case 19:
//C
this.state = 17;
if ((step3 > 0 && _row <= limit3) || (step3 < 0 && _row >= limit3)) this.state = 6;
if (true) break;

case 20:
//C
this.state = 19;
_row = ((int)(0 + _row + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1098;BA.debugLine="cursor3.Position = row";
parent._cursor3.setPosition(_row);
 //BA.debugLineNum = 1099;BA.debugLine="Msgbox2Async(\"Item Number : \" & cursor3.GetStri";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Item Number : "+parent._cursor3.GetString("transaction_number")+anywheresoftware.b4a.keywords.Common.CRLF+"Unit : "+parent._cursor3.GetString("unit")+anywheresoftware.b4a.keywords.Common.CRLF+"Quantity : "+parent._cursor3.GetString("quantity")+anywheresoftware.b4a.keywords.Common.CRLF+"Total Pieces : "+parent._cursor3.GetString("total_pieces")+anywheresoftware.b4a.keywords.Common.CRLF),BA.ObjectToCharSequence("Option"),"EDIT","CANCEL","DELETE",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1104;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 21;
return;
case 21:
//C
this.state = 7;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1105;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 7:
//if
this.state = 16;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 9;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 16;
 //BA.debugLineNum = 1106;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1107;BA.debugLine="bg.Initialize2(Colors.RGB(0,167,255), 5, 0, C";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (167),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1108;BA.debugLine="BUTTON_SAVE.Background = bg";
parent.mostCurrent._button_save.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1109;BA.debugLine="BUTTON_SAVE.Text = \" Edit\"";
parent.mostCurrent._button_save.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 1110;BA.debugLine="BUTTON_CANCEL.Visible = True";
parent.mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1111;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 16;
;
 //BA.debugLineNum = 1114;BA.debugLine="EDITTEXT_QUANTITY.Text = cursor3.GetString(\"q";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("quantity")));
 //BA.debugLineNum = 1115;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 16;
;
 //BA.debugLineNum = 1116;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor3.GetString("unit")));
 //BA.debugLineNum = 1117;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 16;
;
 //BA.debugLineNum = 1118;BA.debugLine="CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor3.GetString("cancelled_reason")));
 //BA.debugLineNum = 1119;BA.debugLine="CMB_REASON.cmbBox.Enabled = False";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1120;BA.debugLine="item_number = cursor3.GetString(\"transaction_";
parent._item_number = parent._cursor3.GetString("transaction_number");
 //BA.debugLineNum = 1122;BA.debugLine="PANEL_LIST.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_list.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1123;BA.debugLine="PANEL_LIST.BringToFront";
parent.mostCurrent._panel_list.BringToFront();
 //BA.debugLineNum = 1124;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1125;BA.debugLine="bg2.Initialize2(Colors.White,5,0,Colors.Black";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1126;BA.debugLine="BUTTON_LIST.Background = bg2";
parent.mostCurrent._button_list.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 1127;BA.debugLine="BUTTON_LIST.TextColor = Colors.RGB(82,169,255";
parent.mostCurrent._button_list.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 1129;BA.debugLine="cancelled_trigger = 0";
parent._cancelled_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1130;BA.debugLine="cancelled_id = cursor3.GetString(\"cancelled_i";
parent._cancelled_id = parent._cursor3.GetString("cancelled_id");
 //BA.debugLineNum = 1131;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 16;
;
 //BA.debugLineNum = 1132;BA.debugLine="GET_WRONGSERVED_DETAILS";
_get_wrongserved_details();
 //BA.debugLineNum = 1134;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 1135;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 26;
return;
case 26:
//C
this.state = 16;
;
 //BA.debugLineNum = 1136;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1139;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this item?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1140;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 12;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1141;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 12:
//if
this.state = 15;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 1142;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM cancelle";
parent._connection.ExecNonQuery("DELETE FROM cancelled_invoice_disc_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+parent.mostCurrent._label_load_description.getText()+"' and transaction_number = '"+BA.ObjectToString(_value)+"'");
 //BA.debugLineNum = 1143;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1144;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 15;
;
 //BA.debugLineNum = 1145;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM wrong_se";
parent._connection.ExecNonQuery("DELETE FROM wrong_served_table WHERE cancelled_id = '"+parent._cursor3.GetString("cancelled_id")+"'");
 //BA.debugLineNum = 1146;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 15;
;
 //BA.debugLineNum = 1147;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM near_exp";
parent._connection.ExecNonQuery("DELETE FROM near_expiry_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+parent.mostCurrent._label_load_description.getText()+"' and transaction_number = '"+BA.ObjectToString(_value)+"'");
 //BA.debugLineNum = 1148;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1149;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 15;
;
 //BA.debugLineNum = 1150;BA.debugLine="LOAD_LIST";
_load_list();
 //BA.debugLineNum = 1151;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 31;
return;
case 31:
//C
this.state = 15;
;
 //BA.debugLineNum = 1152;BA.debugLine="GET_CANCELLED_COUNT";
_get_cancelled_count();
 if (true) break;

case 15:
//C
this.state = 16;
;
 if (true) break;

case 16:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 17:
//C
this.state = 18;
;
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 1158;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _open_invoice() throws Exception{
ResumableSub_OPEN_INVOICE rsub = new ResumableSub_OPEN_INVOICE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_OPEN_INVOICE extends BA.ResumableSub {
public ResumableSub_OPEN_INVOICE(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 722;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 723;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 7;
return;
case 7:
//C
this.state = 1;
;
 //BA.debugLineNum = 724;BA.debugLine="Dialog.Title = \"Select Invoice\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Invoice");
 //BA.debugLineNum = 725;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,168,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (168),(int) (255));
 //BA.debugLineNum = 726;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sear";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 727;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 728;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 729;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 8;
return;
case 8:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 730;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 731;BA.debugLine="GET_INVOICE_PRODUCTS";
_get_invoice_products();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 735;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _openbutton(anywheresoftware.b4a.objects.ButtonWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 2136;BA.debugLine="Sub OpenButton(se As Button)";
 //BA.debugLineNum = 2137;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 2138;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 2139;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 2140;BA.debugLine="End Sub";
return "";
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 299;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 300;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 301;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 302;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 303;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 304;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 305;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 306;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 307;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 24;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 25;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 26;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 28;BA.debugLine="Dim cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim addBitmap As Bitmap";
_addbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim uploadBitmap As Bitmap";
_uploadbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim picklist_id As String";
_picklist_id = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim date_dispatch As String";
_date_dispatch = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim route_name As String";
_route_name = "";
 //BA.debugLineNum = 36;BA.debugLine="Dim plate_no As String";
_plate_no = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim customer_id As String";
_customer_id = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim error_trigger As String";
_error_trigger = "";
 //BA.debugLineNum = 41;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 42;BA.debugLine="Dim product_id As String";
_product_id = "";
 //BA.debugLineNum = 43;BA.debugLine="Dim reason As String";
_reason = "";
 //BA.debugLineNum = 44;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 45;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 47;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 49;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim cancelled_trigger As String";
_cancelled_trigger = "";
 //BA.debugLineNum = 54;BA.debugLine="Dim cancelled_id As String";
_cancelled_id = "";
 //BA.debugLineNum = 55;BA.debugLine="Dim total_invoice As String";
_total_invoice = "";
 //BA.debugLineNum = 56;BA.debugLine="Dim total_cancelled As String";
_total_cancelled = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim cancelled_reason As String";
_cancelled_reason = "";
 //BA.debugLineNum = 61;BA.debugLine="Dim wrong_principal_id As String";
_wrong_principal_id = "";
 //BA.debugLineNum = 62;BA.debugLine="Dim wrong_product_id As String";
_wrong_product_id = "";
 //BA.debugLineNum = 63;BA.debugLine="Dim wrong_reason As String";
_wrong_reason = "";
 //BA.debugLineNum = 64;BA.debugLine="Dim wrong_caseper As String";
_wrong_caseper = "";
 //BA.debugLineNum = 65;BA.debugLine="Dim wrong_pcsper As String";
_wrong_pcsper = "";
 //BA.debugLineNum = 66;BA.debugLine="Dim wrong_dozper As String";
_wrong_dozper = "";
 //BA.debugLineNum = 67;BA.debugLine="Dim wrong_boxper As String";
_wrong_boxper = "";
 //BA.debugLineNum = 68;BA.debugLine="Dim wrong_bagper As String";
_wrong_bagper = "";
 //BA.debugLineNum = 69;BA.debugLine="Dim wrong_packper As String";
_wrong_packper = "";
 //BA.debugLineNum = 70;BA.debugLine="Dim wrong_total_pieces As String";
_wrong_total_pieces = "";
 //BA.debugLineNum = 71;BA.debugLine="Dim wrong_scan_code As String";
_wrong_scan_code = "";
 //BA.debugLineNum = 72;BA.debugLine="Dim wrong_trigger As String = 0";
_wrong_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 75;BA.debugLine="Dim item_number As String";
_item_number = "";
 //BA.debugLineNum = 78;BA.debugLine="Dim lifespan_month As String";
_lifespan_month = "";
 //BA.debugLineNum = 79;BA.debugLine="Dim lifespan_year As String";
_lifespan_year = "";
 //BA.debugLineNum = 80;BA.debugLine="Dim default_reading As String";
_default_reading = "";
 //BA.debugLineNum = 81;BA.debugLine="Dim monthexp As String";
_monthexp = "";
 //BA.debugLineNum = 82;BA.debugLine="Dim yearexp As String";
_yearexp = "";
 //BA.debugLineNum = 83;BA.debugLine="Dim monthmfg As String";
_monthmfg = "";
 //BA.debugLineNum = 84;BA.debugLine="Dim yearmfg As String";
_yearmfg = "";
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 412;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 413;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 414;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("735389442","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 415;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 416;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 417;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 418;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 419;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 420;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 422;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 423;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 424;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 425;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 426;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 428;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("735389456","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 429;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 430;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 431;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 434;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 310;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 311;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 312;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 313;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 314;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 315;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 316;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 317;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 318;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 319;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 320;BA.debugLine="End Sub";
return "";
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 385;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 386;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 387;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 388;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 389;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 390;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
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
 //BA.debugLineNum = 391;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 392;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 394;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 395;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 397;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 398;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 399;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 400;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
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
 //BA.debugLineNum = 401;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 403;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 405;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("735323925",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 406;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 407;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 408;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
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
 //BA.debugLineNum = 411;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_invoice_product_cellclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
 //BA.debugLineNum = 903;BA.debugLine="Sub TABLE_INVOICE_PRODUCT_CellClicked (ColumnId As";
 //BA.debugLineNum = 904;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 905;BA.debugLine="Dim RowData As Map = TABLE_INVOICE_PRODUCT.GetRow";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._table_invoice_product._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 906;BA.debugLine="End Sub";
return "";
}
public static void  _table_invoice_product_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_INVOICE_PRODUCT_CellLongClicked rsub = new ResumableSub_TABLE_INVOICE_PRODUCT_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_INVOICE_PRODUCT_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_INVOICE_PRODUCT_CellLongClicked(wingan.app.cancelled_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.cancelled_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
int _qrow = 0;
int _row = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int step16;
int limit16;
int step54;
int limit54;
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
 //BA.debugLineNum = 908;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 910;BA.debugLine="Dim RowData As Map = TABLE_INVOICE_PRODUCT.GetRow";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_invoice_product._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 911;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 913;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 914;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 915;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 916;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 917;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 918;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 919;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 920;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 921;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 50;
return;
case 50:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 922;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 49;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 924;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+BA.ObjectToString(_rowdata.Get((Object)("Product Description")))+"'")));
 //BA.debugLineNum = 925;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 30;
if (parent._cursor3.getRowCount()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 926;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 29;
step16 = 1;
limit16 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 51;
if (true) break;

case 51:
//C
this.state = 29;
if ((step16 > 0 && _qrow <= limit16) || (step16 < 0 && _qrow >= limit16)) this.state = 9;
if (true) break;

case 52:
//C
this.state = 51;
_qrow = ((int)(0 + _qrow + step16)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 927;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 928;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 929;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 930;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 931;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 933;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 934;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
if (true) break;

case 10:
//if
this.state = 13;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 935;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 937;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 938;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 940;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 941;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 943;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 944;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 946;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 947;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 949;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 25:
//if
this.state = 28;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 950;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 28:
//C
this.state = 52;
;
 //BA.debugLineNum = 953;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 954;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 955;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 956;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 957;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 958;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 960;BA.debugLine="default_reading = cursor3.GetString(\"default_e";
parent._default_reading = parent._cursor3.GetString("default_expiration_date_reading");
 //BA.debugLineNum = 961;BA.debugLine="lifespan_year = cursor3.GetString(\"life_span_y";
parent._lifespan_year = parent._cursor3.GetString("life_span_year");
 //BA.debugLineNum = 962;BA.debugLine="lifespan_month = cursor3.GetString(\"life_span_";
parent._lifespan_month = parent._cursor3.GetString("life_span_month");
 if (true) break;
if (true) break;

case 29:
//C
this.state = 30;
;
 if (true) break;

case 30:
//C
this.state = 31;
;
 //BA.debugLineNum = 966;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM ca";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM cancelled_invoice_ref_table WHERE invoice_no = '"+parent.mostCurrent._label_load_invoice_no.getText()+"' And product_description ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 967;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 31:
//if
this.state = 40;
if (parent._cursor4.getRowCount()>0) { 
this.state = 33;
}else {
this.state = 39;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 968;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 34:
//for
this.state = 37;
step54 = 1;
limit54 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 53;
if (true) break;

case 53:
//C
this.state = 37;
if ((step54 > 0 && _row <= limit54) || (step54 < 0 && _row >= limit54)) this.state = 36;
if (true) break;

case 54:
//C
this.state = 53;
_row = ((int)(0 + _row + step54)) ;
if (true) break;

case 36:
//C
this.state = 54;
 //BA.debugLineNum = 969;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 970;BA.debugLine="total_invoice = cursor4.GetString(\"total_piece";
parent._total_invoice = parent._cursor4.GetString("total_pieces");
 if (true) break;
if (true) break;

case 37:
//C
this.state = 40;
;
 if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 973;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 974;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CRLF";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_description.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"is not in the invoice."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 40:
//C
this.state = 41;
;
 //BA.debugLineNum = 976;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 977;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 41:
//if
this.state = 48;
if (parent._cursor6.getRowCount()>0) { 
this.state = 43;
}if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 978;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 44:
//for
this.state = 47;
step64 = 1;
limit64 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 47;
if ((step64 > 0 && _row <= limit64) || (step64 < 0 && _row >= limit64)) this.state = 46;
if (true) break;

case 56:
//C
this.state = 55;
_row = ((int)(0 + _row + step64)) ;
if (true) break;

case 46:
//C
this.state = 56;
 //BA.debugLineNum = 979;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 980;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 47:
//C
this.state = 48;
;
 if (true) break;

case 48:
//C
this.state = 49;
;
 //BA.debugLineNum = 983;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 984;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 985;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 986;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 987;BA.debugLine="CLEAR_WRONGSERVED";
_clear_wrongserved();
 //BA.debugLineNum = 988;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 989;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 990;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 991;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 57;
return;
case 57:
//C
this.state = 49;
;
 //BA.debugLineNum = 992;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 993;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 994;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 995;BA.debugLine="cancelled_trigger = 0";
parent._cancelled_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 996;BA.debugLine="LOAD_CANCELLED_REASON";
_load_cancelled_reason();
 //BA.debugLineNum = 997;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 58;
return;
case 58:
//C
this.state = 49;
;
 //BA.debugLineNum = 998;BA.debugLine="GET_CANCELLED_COUNT";
_get_cancelled_count();
 if (true) break;

case 49:
//C
this.state = -1;
;
 //BA.debugLineNum = 1002;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_invoice_product_dataupdated() throws Exception{
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
 //BA.debugLineNum = 851;BA.debugLine="Sub TABLE_INVOICE_PRODUCT_DataUpdated";
 //BA.debugLineNum = 852;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 853;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 855;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 856;BA.debugLine="Dim MaxHeight As Int";
_maxheight = 0;
 //BA.debugLineNum = 857;BA.debugLine="For i = 0 To TABLE_INVOICE_PRODUCT.VisibleRowIds";
{
final int step5 = 1;
final int limit5 = mostCurrent._table_invoice_product._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 858;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 859;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 860;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 862;BA.debugLine="MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.";
_maxheight = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxheight,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 865;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 866;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 867;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 872;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group16 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen16 = group16.length
;int index16 = 0;
;
for (; index16 < groupLen16;index16++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group16[index16]);
 //BA.debugLineNum = 874;BA.debugLine="For i = 0 To TABLE_INVOICE_PRODUCT.VisibleRowIds";
{
final int step17 = 1;
final int limit17 = mostCurrent._table_invoice_product._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit17 ;_i = _i + step17 ) {
 //BA.debugLineNum = 875;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 876;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 877;BA.debugLine="lbl.Font = xui.CreateDefaultBoldFont(18)";
_lbl.setFont(mostCurrent._xui.CreateDefaultBoldFont((float) (18)));
 }
};
 }
};
 //BA.debugLineNum = 881;BA.debugLine="For i = 0 To TABLE_INVOICE_PRODUCT.VisibleRowIds.";
{
final int step23 = 1;
final int limit23 = (int) (mostCurrent._table_invoice_product._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit23 ;_i = _i + step23 ) {
 //BA.debugLineNum = 882;BA.debugLine="Dim RowId As Long = TABLE_INVOICE_PRODUCT.Visibl";
_rowid = BA.ObjectToLongNumber(mostCurrent._table_invoice_product._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 883;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 884;BA.debugLine="Dim pnl1 As B4XView = NameColumn(4).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (4)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 885;BA.debugLine="Dim row As Map = TABLE_INVOICE_PRODUCT.GetRow(R";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._table_invoice_product._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 886;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 887;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (4)].Id /*String*/ )));
 //BA.debugLineNum = 888;BA.debugLine="If OtherColumnValue > 0 Then";
if ((double)(Double.parseDouble(_othercolumnvalue))>0) { 
 //BA.debugLineNum = 889;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 };
 //BA.debugLineNum = 891;BA.debugLine="pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Co";
_pnl1.GetView((int) (0)).SetColorAndBorder(_clr,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)),(int) (0));
 };
 }
};
 //BA.debugLineNum = 895;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group36 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)])};
final int groupLen36 = group36.length
;int index36 = 0;
;
for (; index36 < groupLen36;index36++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group36[index36]);
 //BA.debugLineNum = 896;BA.debugLine="Column.InternalSortMode= \"ASC\"";
_column.InternalSortMode /*String*/  = "ASC";
 }
};
 //BA.debugLineNum = 898;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 899;BA.debugLine="TABLE_INVOICE_PRODUCT.Refresh";
mostCurrent._table_invoice_product._refresh /*String*/ ();
 //BA.debugLineNum = 900;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 902;BA.debugLine="End Sub";
return "";
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 638;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 639;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 640;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 641;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("735651587","Trying to connect...",0);
 //BA.debugLineNum = 642;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 643;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 644;BA.debugLine="End Sub";
return "";
}
public static void  _update_wrongserved() throws Exception{
ResumableSub_UPDATE_WRONGSERVED rsub = new ResumableSub_UPDATE_WRONGSERVED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_WRONGSERVED extends BA.ResumableSub {
public ResumableSub_UPDATE_WRONGSERVED(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
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
 //BA.debugLineNum = 1511;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBo";
if (true) break;

case 1:
//if
this.state = 14;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 3;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 5;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 7;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 9;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 11;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 14;
 //BA.debugLineNum = 1512;BA.debugLine="wrong_total_pieces = wrong_caseper * EDITTEXT_QU";
parent._wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._wrong_caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 5:
//C
this.state = 14;
 //BA.debugLineNum = 1514;BA.debugLine="wrong_total_pieces = wrong_pcsper * EDITTEXT_QUA";
parent._wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._wrong_pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 7:
//C
this.state = 14;
 //BA.debugLineNum = 1516;BA.debugLine="wrong_total_pieces = wrong_dozper * EDITTEXT_QUA";
parent._wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._wrong_dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 9:
//C
this.state = 14;
 //BA.debugLineNum = 1518;BA.debugLine="wrong_total_pieces = wrong_boxper * EDITTEXT_QUA";
parent._wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._wrong_boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 1520;BA.debugLine="wrong_total_pieces = wrong_bagper * EDITTEXT_QUA";
parent._wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._wrong_bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1522;BA.debugLine="wrong_total_pieces = wrong_packper * EDITTEXT_QU";
parent._wrong_total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._wrong_packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 1524;BA.debugLine="Log (wrong_total_pieces)";
anywheresoftware.b4a.keywords.Common.LogImpl("737683214",parent._wrong_total_pieces,0);
 //BA.debugLineNum = 1525;BA.debugLine="If wrong_total_pieces > 0 Then";
if (true) break;

case 15:
//if
this.state = 20;
if ((double)(Double.parseDouble(parent._wrong_total_pieces))>0) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 1526;BA.debugLine="Log(\"open\")";
anywheresoftware.b4a.keywords.Common.LogImpl("737683216","open",0);
 //BA.debugLineNum = 1527;BA.debugLine="wrong_trigger = 0";
parent._wrong_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1528;BA.debugLine="Dim query As String = \"UPDATE wrong_served_table";
_query = "UPDATE wrong_served_table SET unit = ?, quantity = ?, total_pieces = ? , user_info = ?, date_registered = ? , time_registered = ? WHERE cancelled_id = ?";
 //BA.debugLineNum = 1529;BA.debugLine="connection.ExecNonQuery2(query,Array As String(C";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._wrong_total_pieces,parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent._cancelled_id}));
 //BA.debugLineNum = 1530;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 20;
;
 //BA.debugLineNum = 1531;BA.debugLine="ToastMessageShow(\"Transaction Updated\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1533;BA.debugLine="Log(\"close\")";
anywheresoftware.b4a.keywords.Common.LogImpl("737683223","close",0);
 //BA.debugLineNum = 1534;BA.debugLine="wrong_trigger = 1";
parent._wrong_trigger = BA.NumberToString(1);
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 1536;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 335;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 336;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 337;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static void  _wrongserved_manual() throws Exception{
ResumableSub_WRONGSERVED_MANUAL rsub = new ResumableSub_WRONGSERVED_MANUAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_WRONGSERVED_MANUAL extends BA.ResumableSub {
public ResumableSub_WRONGSERVED_MANUAL(wingan.app.cancelled_module parent) {
this.parent = parent;
}
wingan.app.cancelled_module parent;
anywheresoftware.b4a.objects.collections.List _items = null;
int _i = 0;
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
 //BA.debugLineNum = 1418;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 1;
;
 //BA.debugLineNum = 1419;BA.debugLine="SearchTemplate2.CustomListView1.Clear";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 1420;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 1421;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1422;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 1423;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 1424;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' ORDER BY product_desc ASC")));
 //BA.debugLineNum = 1425;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step8 = 1;
limit8 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 4;
if ((step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8)) this.state = 3;
if (true) break;

case 7:
//C
this.state = 6;
_i = ((int)(0 + _i + step8)) ;
if (true) break;

case 3:
//C
this.state = 7;
 //BA.debugLineNum = 1426;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 7;
;
 //BA.debugLineNum = 1427;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 1428;BA.debugLine="Items.Add(cursor2.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor2.GetString("product_desc")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1430;BA.debugLine="SearchTemplate2.SetItems(Items)";
parent.mostCurrent._searchtemplate2._setitems /*Object*/ (_items);
 //BA.debugLineNum = 1431;BA.debugLine="End Sub";
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
