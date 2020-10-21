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

public class sales_return_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static sales_return_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.sales_return_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (sales_return_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.sales_return_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.sales_return_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (sales_return_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (sales_return_module) Resume **");
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
		return sales_return_module.class;
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
            BA.LogInfo("** Activity (sales_return_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (sales_return_module) Pause event (activity is not paused). **");
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
            sales_return_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (sales_return_module) Resume **");
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
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _addbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _uploadbitmap = null;
public static String _return_id = "";
public static String _return_number = "";
public static String _customer_id = "";
public static String _plate_no = "";
public static String _sales_position_id = "";
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
public anywheresoftware.b4a.objects.LabelWrapper _label_load_customer_name = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_return_date = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_return_id = null;
public wingan.app.b4xcombobox _cmb_salesman = null;
public wingan.app.b4xcombobox _cmb_warehouse = null;
public wingan.app.b4xcombobox _cmb_reason = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_principal = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_exit = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_input = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_save = null;
public wingan.app.b4xtable _table_sales_return = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_cancel = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvl_rid = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_rid = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_manual = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_upload = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_logo = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_expiration = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_expiration = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_manufactured = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_expiration = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
public wingan.app.dashboard_module _dashboard_module = null;
public wingan.app.return_module _return_module = null;
public wingan.app.cancelled_module _cancelled_module = null;
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
public ResumableSub_Activity_Create(wingan.app.sales_return_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 161;BA.debugLine="Activity.LoadLayout(\"sales_return\")";
parent.mostCurrent._activity.LoadLayout("sales_return",mostCurrent.activityBA);
 //BA.debugLineNum = 163;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 164;BA.debugLine="addBitmap = LoadBitmap(File.DirAssets, \"add.png\")";
parent._addbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"add.png");
 //BA.debugLineNum = 165;BA.debugLine="uploadBitmap = LoadBitmap(File.DirAssets, \"invoic";
parent._uploadbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"invoice.png");
 //BA.debugLineNum = 167;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 168;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 169;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 170;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 171;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 172;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 173;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 175;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 176;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 177;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 179;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 180;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 183;BA.debugLine="ACToolBarLight1.Elevation = 1dip";
parent.mostCurrent._actoolbarlight1.setElevation((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 186;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 187;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 189;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 190;BA.debugLine="p.SetLayoutAnimated(0, 10%x, 20%y, 80%y, 40%y)";
_p.SetLayoutAnimated((int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 191;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 193;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 194;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 195;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 196;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 197;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 198;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 199;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 200;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 201;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 203;BA.debugLine="DateTemplate.Initialize";
parent.mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 204;BA.debugLine="DateTemplate.MinYear = 2016";
parent.mostCurrent._datetemplate._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 205;BA.debugLine="DateTemplate.MaxYear = 2030";
parent.mostCurrent._datetemplate._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 206;BA.debugLine="DateTemplate.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 207;BA.debugLine="DateTemplate.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 208;BA.debugLine="DateTemplate.btnMonthLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 209;BA.debugLine="DateTemplate.btnMonthRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 210;BA.debugLine="DateTemplate.btnYearLeft.Color = Colors.RGB(82,16";
parent.mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 211;BA.debugLine="DateTemplate.btnYearRight.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 212;BA.debugLine="DateTemplate.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 214;BA.debugLine="DateTemplate2.Initialize";
parent.mostCurrent._datetemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 215;BA.debugLine="DateTemplate2.MinYear = 2016";
parent.mostCurrent._datetemplate2._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 216;BA.debugLine="DateTemplate2.MaxYear = 2030";
parent.mostCurrent._datetemplate2._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 217;BA.debugLine="DateTemplate2.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate2._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 218;BA.debugLine="DateTemplate2.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate2._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 219;BA.debugLine="DateTemplate2.btnMonthLeft.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate2._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 220;BA.debugLine="DateTemplate2.btnMonthRight.Color = Colors.RGB(82";
parent.mostCurrent._datetemplate2._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 221;BA.debugLine="DateTemplate2.btnYearLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate2._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 222;BA.debugLine="DateTemplate2.btnYearRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate2._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 223;BA.debugLine="DateTemplate2.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate2._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 225;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 226;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 227;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 228;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 229;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 230;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 232;BA.debugLine="SearchTemplate2.Initialize";
parent.mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 233;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 234;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 235;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 236;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 237;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 239;BA.debugLine="InputTemplate.Initialize";
parent.mostCurrent._inputtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 240;BA.debugLine="InputTemplate.SetBorderColor(Colors.Green,Colors.";
parent.mostCurrent._inputtemplate._setbordercolor /*String*/ (anywheresoftware.b4a.keywords.Common.Colors.Green,anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 241;BA.debugLine="InputTemplate.TextField1.TextColor = Colors.Black";
parent.mostCurrent._inputtemplate._textfield1 /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 245;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 246;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 248;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 249;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 258;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 259;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 260;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 261;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 262;BA.debugLine="RETURN_MANUAL";
_return_manual();
 //BA.debugLineNum = 263;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 264;BA.debugLine="LOAD_WAREHOUSE";
_load_warehouse();
 //BA.debugLineNum = 265;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 266;BA.debugLine="GET_CUSTOMER";
_get_customer();
 //BA.debugLineNum = 267;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 8;
return;
case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 268;BA.debugLine="LOAD_RETURN_PRODUCT_HEADER";
_load_return_product_header();
 //BA.debugLineNum = 269;BA.debugLine="BUTTON_MANUAL.Visible = False";
parent.mostCurrent._button_manual.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 270;BA.debugLine="BUTTON_UPLOAD.Visible = False";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
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
public ResumableSub_Activity_CreateMenu(wingan.app.sales_return_module parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 273;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("See Returns ID"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 274;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 275;BA.debugLine="UpdateIcon(\"See Returns ID\", uploadBitmap)";
_updateicon("See Returns ID",parent._uploadbitmap);
 //BA.debugLineNum = 276;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (2),(int) (2),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 277;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 278;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 279;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("Add New Transaction"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 280;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 281;BA.debugLine="UpdateIcon(\"Add New Transaction\", addBitmap)";
_updateicon("Add New Transaction",parent._addbitmap);
 //BA.debugLineNum = 282;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 283;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 2102;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 2103;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 2104;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 2106;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 291;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 292;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("739976961","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 293;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 294;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 295;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 296;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 285;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 287;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 288;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 290;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.sales_return_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.sales_return_module parent;
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
this.state = 26;
if ((_item.getTitle()).equals("cart")) { 
this.state = 3;
}else if((_item.getTitle()).equals("See Returns ID")) { 
this.state = 11;
}else if((_item.getTitle()).equals("Add New Transaction")) { 
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 359;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("740566786","Resuming...",0);
 //BA.debugLineNum = 360;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 361;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 362;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 363;BA.debugLine="If ScannerOnceConnected=True Then";
if (true) break;

case 4:
//if
this.state = 9;
if (parent._scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 364;BA.debugLine="Ts.Enabled=True";
parent._ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 365;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 366;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 368;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 369;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 9:
//C
this.state = 26;
;
 if (true) break;

case 11:
//C
this.state = 26;
 //BA.debugLineNum = 372;BA.debugLine="LOAD_RETURN_UPLOAD";
_load_return_upload();
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 374;BA.debugLine="If LABEL_LOAD_RETURN_ID.Text <> \"-\" Then";
if (true) break;

case 14:
//if
this.state = 25;
if ((parent.mostCurrent._label_load_return_id.getText()).equals("-") == false) { 
this.state = 16;
}else {
this.state = 24;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 375;BA.debugLine="Msgbox2Async(\"Is this new transaction is still t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Is this new transaction is still the existing customer?"),BA.ObjectToCharSequence("Creating New Transaction"),"New Customer","","Existing Customer",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 376;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 17;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 377;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 17:
//if
this.state = 22;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 19;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 21;
}if (true) break;

case 19:
//C
this.state = 22;
 //BA.debugLineNum = 378;BA.debugLine="SHOW_CUSTOMER";
_show_customer();
 //BA.debugLineNum = 379;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 22;
;
 //BA.debugLineNum = 380;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 382;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = LABEL_LOAD_CUST";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_load_customer_name.getText()));
 //BA.debugLineNum = 383;BA.debugLine="NEW_TRANSACTION";
_new_transaction();
 //BA.debugLineNum = 384;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 22;
;
 //BA.debugLineNum = 385;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 if (true) break;

case 22:
//C
this.state = 25;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 388;BA.debugLine="SHOW_CUSTOMER";
_show_customer();
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
 //BA.debugLineNum = 391;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 325;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 326;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 327;BA.debugLine="StartActivity(RETURN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._return_module.getObject()));
 //BA.debugLineNum = 328;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _addbadgetoicon(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp,int _number1) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _mbmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _target = null;
 //BA.debugLineNum = 343;BA.debugLine="Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int)";
 //BA.debugLineNum = 344;BA.debugLine="Dim cvs1 As Canvas";
_cvs1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 345;BA.debugLine="Dim mbmp As Bitmap";
_mbmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 346;BA.debugLine="mbmp.InitializeMutable(32dip, 32dip)";
_mbmp.InitializeMutable(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 347;BA.debugLine="cvs1.Initialize2(mbmp)";
_cvs1.Initialize2((android.graphics.Bitmap)(_mbmp.getObject()));
 //BA.debugLineNum = 348;BA.debugLine="Dim target As Rect";
_target = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 349;BA.debugLine="target.Initialize(0, 0, mbmp.Width, mbmp.Height)";
_target.Initialize((int) (0),(int) (0),_mbmp.getWidth(),_mbmp.getHeight());
 //BA.debugLineNum = 350;BA.debugLine="cvs1.DrawBitmap(bmp, Null, target)";
_cvs1.DrawBitmap((android.graphics.Bitmap)(_bmp.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_target.getObject()));
 //BA.debugLineNum = 351;BA.debugLine="If Number1 > 0 Then";
if (_number1>0) { 
 //BA.debugLineNum = 352;BA.debugLine="cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, C";
_cvs1.DrawCircle((float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 353;BA.debugLine="cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8";
_cvs1.DrawText(mostCurrent.activityBA,BA.NumberToString(anywheresoftware.b4a.keywords.Common.Min(_number1,1000)),(float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (11))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (9),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 };
 //BA.debugLineNum = 355;BA.debugLine="Return mbmp";
if (true) return _mbmp;
 //BA.debugLineNum = 356;BA.debugLine="End Sub";
return null;
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 551;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 552;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("740828929","Connection broken...",0);
 //BA.debugLineNum = 553;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 554;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 555;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 556;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 557;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 558;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 559;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 560;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 562;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 564;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.sales_return_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.sales_return_module parent;
byte[] _buffer;
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _qrow = 0;
int step8;
int limit8;
int step22;
int limit22;
int step36;
int limit36;
int step72;
int limit72;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 446;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("740763393","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 448;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 451;BA.debugLine="If LABEL_LOAD_RETURN_ID.Text <> \"-\" Then";
if (true) break;

case 1:
//if
this.state = 63;
if ((parent.mostCurrent._label_load_return_id.getText()).equals("-") == false) { 
this.state = 3;
}else {
this.state = 62;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 452;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 453;BA.debugLine="If cursor2.RowCount >= 2 Then";
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
 //BA.debugLineNum = 454;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 455;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 456;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step8 = 1;
limit8 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 64;
if (true) break;

case 64:
//C
this.state = 10;
if ((step8 > 0 && _row <= limit8) || (step8 < 0 && _row >= limit8)) this.state = 9;
if (true) break;

case 65:
//C
this.state = 64;
_row = ((int)(0 + _row + step8)) ;
if (true) break;

case 9:
//C
this.state = 65;
 //BA.debugLineNum = 457;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 458;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 459;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 461;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True)";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 462;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 66;
return;
case 66:
//C
this.state = 11;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 463;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 464;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 465;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 467;BA.debugLine="trigger = 1";
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
 //BA.debugLineNum = 472;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 19:
//for
this.state = 22;
step22 = 1;
limit22 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 67;
if (true) break;

case 67:
//C
this.state = 22;
if ((step22 > 0 && _row <= limit22) || (step22 < 0 && _row >= limit22)) this.state = 21;
if (true) break;

case 68:
//C
this.state = 67;
_row = ((int)(0 + _row + step22)) ;
if (true) break;

case 21:
//C
this.state = 68;
 //BA.debugLineNum = 473;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 474;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("740763421",BA.NumberToString(1),0);
 //BA.debugLineNum = 475;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 476;BA.debugLine="trigger = 0";
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
 //BA.debugLineNum = 479;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 480;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not RE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 481;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 484;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 26;
;
 //BA.debugLineNum = 485;BA.debugLine="If trigger = 0 Then";
if (true) break;

case 26:
//if
this.state = 60;
if (_trigger==0) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 486;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 487;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 29:
//for
this.state = 51;
step36 = 1;
limit36 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 70;
if (true) break;

case 70:
//C
this.state = 51;
if ((step36 > 0 && _qrow <= limit36) || (step36 < 0 && _qrow >= limit36)) this.state = 31;
if (true) break;

case 71:
//C
this.state = 70;
_qrow = ((int)(0 + _qrow + step36)) ;
if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 488;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 489;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 490;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 491;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 493;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 494;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
if (true) break;

case 32:
//if
this.state = 35;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 34;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 495;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 497;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 35:
//if
this.state = 38;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 37;
}if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 498;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 500;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 38:
//if
this.state = 41;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 40;
}if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 501;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 503;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 41:
//if
this.state = 44;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 43;
}if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 504;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 506;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 44:
//if
this.state = 47;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 507;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 509;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 47:
//if
this.state = 50;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 49;
}if (true) break;

case 49:
//C
this.state = 50;
 //BA.debugLineNum = 510;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 50:
//C
this.state = 71;
;
 //BA.debugLineNum = 513;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 514;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 515;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 516;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 517;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 518;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 520;BA.debugLine="default_reading = cursor3.GetString(\"default_e";
parent._default_reading = parent._cursor3.GetString("default_expiration_date_reading");
 //BA.debugLineNum = 521;BA.debugLine="lifespan_year = cursor3.GetString(\"life_span_y";
parent._lifespan_year = parent._cursor3.GetString("life_span_year");
 //BA.debugLineNum = 522;BA.debugLine="lifespan_month = cursor3.GetString(\"life_span_";
parent._lifespan_month = parent._cursor3.GetString("life_span_month");
 if (true) break;
if (true) break;

case 51:
//C
this.state = 52;
;
 //BA.debugLineNum = 525;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 526;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 52:
//if
this.state = 59;
if (parent._cursor6.getRowCount()>0) { 
this.state = 54;
}if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 527;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 55:
//for
this.state = 58;
step72 = 1;
limit72 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 72;
if (true) break;

case 72:
//C
this.state = 58;
if ((step72 > 0 && _row <= limit72) || (step72 < 0 && _row >= limit72)) this.state = 57;
if (true) break;

case 73:
//C
this.state = 72;
_row = ((int)(0 + _row + step72)) ;
if (true) break;

case 57:
//C
this.state = 73;
 //BA.debugLineNum = 528;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 529;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 58:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = 60;
;
 //BA.debugLineNum = 532;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 533;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 74;
return;
case 74:
//C
this.state = 60;
;
 //BA.debugLineNum = 534;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 535;BA.debugLine="LOAD_WAREHOUSE";
_load_warehouse();
 //BA.debugLineNum = 536;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 75;
return;
case 75:
//C
this.state = 60;
;
 //BA.debugLineNum = 537;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 538;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 539;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 76;
return;
case 76:
//C
this.state = 60;
;
 //BA.debugLineNum = 540;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 541;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 542;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.Len";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 543;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 77;
return;
case 77:
//C
this.state = 60;
;
 //BA.debugLineNum = 544;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 if (true) break;

case 60:
//C
this.state = 63;
;
 if (true) break;

case 62:
//C
this.state = 63;
 //BA.debugLineNum = 548;BA.debugLine="Msgbox2Async(\"SELECT or CREATE a transaction fir";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SELECT or CREATE a transaction first before scannning."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 63:
//C
this.state = -1;
;
 //BA.debugLineNum = 550;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 565;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 566;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("740894465","Connection terminated...",0);
 //BA.debugLineNum = 567;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 568;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 320;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 321;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 322;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 323;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 324;BA.debugLine="End Sub";
return null;
}
public static void  _button_cancel_click() throws Exception{
ResumableSub_BUTTON_CANCEL_Click rsub = new ResumableSub_BUTTON_CANCEL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CANCEL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CANCEL_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 797;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 798;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 799;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 //BA.debugLineNum = 800;BA.debugLine="End Sub";
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
public ResumableSub_BUTTON_EXIT_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 792;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 793;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 794;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 //BA.debugLineNum = 795;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_expiration_click() throws Exception{
 //BA.debugLineNum = 2041;BA.debugLine="Sub BUTTON_EXPIRATION_Click";
 //BA.debugLineNum = 2042;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)";
mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2043;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 2044;BA.debugLine="End Sub";
return "";
}
public static String  _button_expiration_confirm_click() throws Exception{
 //BA.debugLineNum = 2024;BA.debugLine="Sub BUTTON_EXPIRATION_CONFIRM_Click";
 //BA.debugLineNum = 2025;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text = \"-\" Or LABEL_LOAD";
if ((mostCurrent._label_load_expiration.getText()).equals("-") || (mostCurrent._label_load_manufactured.getText()).equals("-")) { 
 //BA.debugLineNum = 2026;BA.debugLine="ToastMessageShow(\"Please input a product first.\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a product first."),anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 2028;BA.debugLine="If BUTTON_SAVE.Text = \" SAVE\" Then";
if ((mostCurrent._button_save.getText()).equals(" SAVE")) { 
 //BA.debugLineNum = 2029;BA.debugLine="INPUT_NEAR_EXPIRY";
_input_near_expiry();
 }else {
 //BA.debugLineNum = 2031;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Fals";
mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2032;BA.debugLine="BUTTON_EXPIRATION.Visible = True";
mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 2035;BA.debugLine="End Sub";
return "";
}
public static void  _button_expiration_exit_click() throws Exception{
ResumableSub_BUTTON_EXPIRATION_EXIT_Click rsub = new ResumableSub_BUTTON_EXPIRATION_EXIT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_EXPIRATION_EXIT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_EXPIRATION_EXIT_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 2011;BA.debugLine="If BUTTON_SAVE.Text = \" SAVE\" Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._button_save.getText()).equals(" SAVE")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 2012;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 2013;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2014;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 2015;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 2016;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 2017;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 2018;BA.debugLine="ToastMessageShow(\"Near expiry cancel\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Near expiry cancel"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 2020;BA.debugLine="LOAD_EXPIRATION";
_load_expiration();
 //BA.debugLineNum = 2021;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 2023;BA.debugLine="End Sub";
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
public ResumableSub_BUTTON_MANUAL_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;
int _qrow = 0;
int _row = 0;
int step20;
int limit20;
int step57;
int limit57;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1386;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1387;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 1388;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 1389;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 1390;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 1391;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 1392;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 1393;BA.debugLine="ls.Add(\"NO ACTUAL STOCK\")";
_ls.Add((Object)("NO ACTUAL STOCK"));
 //BA.debugLineNum = 1394;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1395;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 40;
return;
case 40:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 1396;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 39;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1397;BA.debugLine="ProgressDialogShow2(\"Loading.....\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading....."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1398;BA.debugLine="Sleep(3000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (3000));
this.state = 41;
return;
case 41:
//C
this.state = 4;
;
 //BA.debugLineNum = 1399;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sea";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 1400;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 1401;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1402;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 42;
return;
case 42:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1403;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 38;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1405;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._searchtemplate._selecteditem /*String*/ +"'")));
 //BA.debugLineNum = 1406;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 29;
step20 = 1;
limit20 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 43;
if (true) break;

case 43:
//C
this.state = 29;
if ((step20 > 0 && _qrow <= limit20) || (step20 < 0 && _qrow >= limit20)) this.state = 9;
if (true) break;

case 44:
//C
this.state = 43;
_qrow = ((int)(0 + _qrow + step20)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1407;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1408;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1409;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 1410;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 1411;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 1413;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1414;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
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
 //BA.debugLineNum = 1415;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 1417;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1418;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 1420;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1421;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 1423;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1424;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 1426;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1427;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 1429;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 25:
//if
this.state = 28;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1430;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 28:
//C
this.state = 44;
;
 //BA.debugLineNum = 1433;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1434;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1435;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1436;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1437;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1438;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 1440;BA.debugLine="default_reading = cursor3.GetString(\"default_e";
parent._default_reading = parent._cursor3.GetString("default_expiration_date_reading");
 //BA.debugLineNum = 1441;BA.debugLine="lifespan_year = cursor3.GetString(\"life_span_y";
parent._lifespan_year = parent._cursor3.GetString("life_span_year");
 //BA.debugLineNum = 1442;BA.debugLine="lifespan_month = cursor3.GetString(\"life_span_";
parent._lifespan_month = parent._cursor3.GetString("life_span_month");
 if (true) break;
if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 1445;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 1446;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 30:
//if
this.state = 37;
if (parent._cursor6.getRowCount()>0) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1447;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 33:
//for
this.state = 36;
step57 = 1;
limit57 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 36;
if ((step57 > 0 && _row <= limit57) || (step57 < 0 && _row >= limit57)) this.state = 35;
if (true) break;

case 46:
//C
this.state = 45;
_row = ((int)(0 + _row + step57)) ;
if (true) break;

case 35:
//C
this.state = 46;
 //BA.debugLineNum = 1448;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 1449;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 36:
//C
this.state = 37;
;
 if (true) break;

case 37:
//C
this.state = 38;
;
 //BA.debugLineNum = 1452;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 1453;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 1454;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1455;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 38;
;
 //BA.debugLineNum = 1456;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1457;BA.debugLine="LOAD_WAREHOUSE";
_load_warehouse();
 //BA.debugLineNum = 1458;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 48;
return;
case 48:
//C
this.state = 38;
;
 //BA.debugLineNum = 1459;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 1460;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 1461;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 49;
return;
case 49:
//C
this.state = 38;
;
 //BA.debugLineNum = 1462;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 1463;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 50;
return;
case 50:
//C
this.state = 38;
;
 //BA.debugLineNum = 1464;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 if (true) break;

case 38:
//C
this.state = 39;
;
 if (true) break;

case 39:
//C
this.state = -1;
;
 //BA.debugLineNum = 1468;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _button_rid_exit_click() throws Exception{
 //BA.debugLineNum = 1301;BA.debugLine="Sub BUTTON_RID_EXIT_Click";
 //BA.debugLineNum = 1302;BA.debugLine="PANEL_BG_RID.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_rid.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1303;BA.debugLine="End Sub";
return "";
}
public static void  _button_save_click() throws Exception{
ResumableSub_BUTTON_SAVE_Click rsub = new ResumableSub_BUTTON_SAVE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SAVE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SAVE_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
int _i = 0;
String _query = "";
int _result = 0;
String _insert_query = "";
int step37;
int limit37;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 892;BA.debugLine="If LOGIN_MODULE.username <> \"\" Or LOGIN_MODULE.ta";
if (true) break;

case 1:
//if
this.state = 81;
if ((parent.mostCurrent._login_module._username /*String*/ ).equals("") == false || (parent.mostCurrent._login_module._tab_id /*String*/ ).equals("") == false) { 
this.state = 3;
}else {
this.state = 80;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 893;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"\" Or EDITTEXT_QUANT";
if (true) break;

case 4:
//if
this.state = 78;
if ((parent.mostCurrent._edittext_quantity.getText()).equals("") || (double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))<=0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 78;
 //BA.debugLineNum = 894;BA.debugLine="Msgbox2Async(\"You cannot input a zero value qua";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You cannot input a zero value quantity."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 895;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 896;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 82;
return;
case 82:
//C
this.state = 78;
;
 //BA.debugLineNum = 897;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 898;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 83;
return;
case 83:
//C
this.state = 78;
;
 //BA.debugLineNum = 899;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 901;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then";
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
 //BA.debugLineNum = 902;BA.debugLine="CMB_UNIT.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 905;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = -1 Then";

case 12:
//if
this.state = 15;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 906;BA.debugLine="CMB_REASON.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 909;BA.debugLine="If CMB_WAREHOUSE.cmbBox.SelectedIndex = -1 Then";

case 15:
//if
this.state = 18;
if (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 910;BA.debugLine="CMB_WAREHOUSE.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;

case 18:
//C
this.state = 19;
;
 //BA.debugLineNum = 912;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 84;
return;
case 84:
//C
this.state = 19;
;
 //BA.debugLineNum = 913;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("742205206",BA.NumberToString(2),0);
 //BA.debugLineNum = 914;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmb";
if (true) break;

case 19:
//if
this.state = 32;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 21;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 23;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 25;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 27;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 29;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 31;
}if (true) break;

case 21:
//C
this.state = 32;
 //BA.debugLineNum = 915;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 23:
//C
this.state = 32;
 //BA.debugLineNum = 917;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 25:
//C
this.state = 32;
 //BA.debugLineNum = 919;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 27:
//C
this.state = 32;
 //BA.debugLineNum = 921;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 29:
//C
this.state = 32;
 //BA.debugLineNum = 923;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 925;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;
;
 //BA.debugLineNum = 927;BA.debugLine="If BUTTON_SAVE.Text = \" SAVE\" Then";

case 32:
//if
this.state = 77;
if ((parent.mostCurrent._button_save.getText()).equals(" SAVE")) { 
this.state = 34;
}else {
this.state = 60;
}if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 928;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT MAX(CA";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as 'transaction_number' from sales_return_disc_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"'")));
 //BA.debugLineNum = 929;BA.debugLine="If cursor9.RowCount > 0 Then";
if (true) break;

case 35:
//if
this.state = 48;
if (parent._cursor9.getRowCount()>0) { 
this.state = 37;
}if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 930;BA.debugLine="For i = 0 To cursor9.RowCount - 1";
if (true) break;

case 38:
//for
this.state = 47;
step37 = 1;
limit37 = (int) (parent._cursor9.getRowCount()-1);
_i = (int) (0) ;
this.state = 85;
if (true) break;

case 85:
//C
this.state = 47;
if ((step37 > 0 && _i <= limit37) || (step37 < 0 && _i >= limit37)) this.state = 40;
if (true) break;

case 86:
//C
this.state = 85;
_i = ((int)(0 + _i + step37)) ;
if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 931;BA.debugLine="cursor9.Position = i";
parent._cursor9.setPosition(_i);
 //BA.debugLineNum = 932;BA.debugLine="If cursor9.GetString(\"transaction_number\")";
if (true) break;

case 41:
//if
this.state = 46;
if (parent._cursor9.GetString("transaction_number")== null || (parent._cursor9.GetString("transaction_number")).equals("")) { 
this.state = 43;
}else {
this.state = 45;
}if (true) break;

case 43:
//C
this.state = 46;
 //BA.debugLineNum = 933;BA.debugLine="transaction_number = 1";
parent._transaction_number = BA.NumberToString(1);
 if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 935;BA.debugLine="transaction_number = cursor9.GetString(\"tr";
parent._transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._cursor9.GetString("transaction_number")))+1);
 if (true) break;

case 46:
//C
this.state = 86;
;
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
 //BA.debugLineNum = 939;BA.debugLine="GET_SALESMAN_ID";
_get_salesman_id();
 //BA.debugLineNum = 940;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 87;
return;
case 87:
//C
this.state = 49;
;
 //BA.debugLineNum = 942;BA.debugLine="If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WA";
if (true) break;

case 49:
//if
this.state = 58;
if (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("MAIN WAREHOUSE")) { 
this.state = 51;
}else {
this.state = 53;
}if (true) break;

case 51:
//C
this.state = 58;
 //BA.debugLineNum = 943;BA.debugLine="INPUT_EXPIRY";
_input_expiry();
 if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 945;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = CMB_REAS";
if (true) break;

case 54:
//if
this.state = 57;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("NEAR EXPIRY")) { 
this.state = 56;
}if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 946;BA.debugLine="INPUT_EXPIRY";
_input_expiry();
 if (true) break;

case 57:
//C
this.state = 58;
;
 if (true) break;

case 58:
//C
this.state = 77;
;
 //BA.debugLineNum = 951;BA.debugLine="Dim query As String = \"INSERT INTO sales_ret";
_query = "INSERT INTO sales_return_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 952;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._return_id,parent._transaction_number,parent._sales_position_id,parent.mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent._scan_code,parent._reason,parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 955;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 88;
return;
case 88:
//C
this.state = 77;
;
 //BA.debugLineNum = 956;BA.debugLine="ToastMessageShow(\"Product Return Added\",  Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Return Added"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 958;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 89;
return;
case 89:
//C
this.state = 77;
;
 //BA.debugLineNum = 959;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 962;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 963;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 90;
return;
case 90:
//C
this.state = 77;
;
 //BA.debugLineNum = 964;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(100, False";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (100),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 965;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 91;
return;
case 91:
//C
this.state = 77;
;
 //BA.debugLineNum = 966;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 if (true) break;

case 60:
//C
this.state = 61;
 //BA.debugLineNum = 968;BA.debugLine="Msgbox2Async(\"Are you sure you want to update";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update this item?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 969;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 92;
return;
case 92:
//C
this.state = 61;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 970;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 61:
//if
this.state = 76;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 63;
}if (true) break;

case 63:
//C
this.state = 64;
 //BA.debugLineNum = 971;BA.debugLine="GET_SALESMAN_ID";
_get_salesman_id();
 //BA.debugLineNum = 973;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 93;
return;
case 93:
//C
this.state = 64;
;
 //BA.debugLineNum = 974;BA.debugLine="If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_W";
if (true) break;

case 64:
//if
this.state = 75;
if (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("MAIN WAREHOUSE")) { 
this.state = 66;
}else {
this.state = 68;
}if (true) break;

case 66:
//C
this.state = 75;
 //BA.debugLineNum = 975;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_";
parent._connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+parent._item_number+"'");
 //BA.debugLineNum = 976;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 94;
return;
case 94:
//C
this.state = 75;
;
 //BA.debugLineNum = 977;BA.debugLine="transaction_number = item_number";
parent._transaction_number = parent._item_number;
 //BA.debugLineNum = 978;BA.debugLine="INPUT_EXPIRY";
_input_expiry();
 if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 980;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = CMB_REA";
if (true) break;

case 69:
//if
this.state = 74;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("NEAR EXPIRY")) { 
this.state = 71;
}else {
this.state = 73;
}if (true) break;

case 71:
//C
this.state = 74;
 //BA.debugLineNum = 981;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_";
parent._connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+parent._item_number+"'");
 //BA.debugLineNum = 982;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 95;
return;
case 95:
//C
this.state = 74;
;
 //BA.debugLineNum = 983;BA.debugLine="transaction_number = item_number";
parent._transaction_number = parent._item_number;
 //BA.debugLineNum = 984;BA.debugLine="INPUT_EXPIRY";
_input_expiry();
 if (true) break;

case 73:
//C
this.state = 74;
 //BA.debugLineNum = 986;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_";
parent._connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+parent._item_number+"'");
 //BA.debugLineNum = 987;BA.debugLine="ToastMessageShow(\"Expiry Deleted\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Expiry Deleted"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 74:
//C
this.state = 75;
;
 if (true) break;

case 75:
//C
this.state = 76;
;
 //BA.debugLineNum = 990;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 96;
return;
case 96:
//C
this.state = 76;
;
 //BA.debugLineNum = 991;BA.debugLine="Dim insert_query As String = \"INSERT INTO sal";
_insert_query = "INSERT INTO sales_return_disc_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_disc_table WHERE return_id = ? AND transaction_number = ?";
 //BA.debugLineNum = 992;BA.debugLine="connection.ExecNonQuery2(insert_query, Array";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,"EDITED",parent.mostCurrent._label_load_return_id.getText(),parent._item_number}));
 //BA.debugLineNum = 993;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 97;
return;
case 97:
//C
this.state = 76;
;
 //BA.debugLineNum = 994;BA.debugLine="Dim query As String = \"UPDATE sales_return_di";
_query = "UPDATE sales_return_disc_table SET salesman_position_id = ?, salesman_name = ?, unit = ?, quantity = ?, total_pieces = ?, warehouse = ?, return_reason = ?, user_info = ?, date_registered = ? , time_registered = ? WHERE return_id = ? and transaction_number = ?";
 //BA.debugLineNum = 995;BA.debugLine="connection.ExecNonQuery2(query,Array As Strin";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._sales_position_id,parent.mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_return_id.getText(),parent._item_number}));
 //BA.debugLineNum = 996;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 98;
return;
case 98:
//C
this.state = 76;
;
 //BA.debugLineNum = 997;BA.debugLine="ToastMessageShow(\"Transaction Updated\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 998;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 999;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 99;
return;
case 99:
//C
this.state = 76;
;
 //BA.debugLineNum = 1000;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 if (true) break;

case 76:
//C
this.state = 77;
;
 if (true) break;

case 77:
//C
this.state = 78;
;
 if (true) break;

case 78:
//C
this.state = 81;
;
 if (true) break;

case 80:
//C
this.state = 81;
 //BA.debugLineNum = 1006;BA.debugLine="Msgbox2Async(\"TABLET ID AND USERNAME CANNOT READ";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 81:
//C
this.state = -1;
;
 //BA.debugLineNum = 1008;BA.debugLine="End Sub";
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
public ResumableSub_BUTTON_UPLOAD_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1850;BA.debugLine="return_id = LABEL_LOAD_RETURN_ID.Text";
parent._return_id = parent.mostCurrent._label_load_return_id.getText();
 //BA.debugLineNum = 1851;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM sal";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"+parent._return_id+"'")));
 //BA.debugLineNum = 1852;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 12;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1853;BA.debugLine="Msgbox2Async(\"Are you sure you want to upload th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to upload this return?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1854;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1855;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1856;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
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
 //BA.debugLineNum = 1861;BA.debugLine="Msgbox2Async(\"There's nothing to upload in this";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("There's nothing to upload in this transaction."),BA.ObjectToCharSequence("Empty Upload"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1864;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _clear() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 1009;BA.debugLine="Sub CLEAR";
 //BA.debugLineNum = 1010;BA.debugLine="item_number = 0";
_item_number = BA.NumberToString(0);
 //BA.debugLineNum = 1011;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1012;BA.debugLine="bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (255),(int) (70)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1013;BA.debugLine="BUTTON_SAVE.Background = bg";
mostCurrent._button_save.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1014;BA.debugLine="BUTTON_SAVE.Text = \" SAVE\"";
mostCurrent._button_save.setText(BA.ObjectToCharSequence(" SAVE"));
 //BA.debugLineNum = 1015;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1016;BA.debugLine="BUTTON_CANCEL.Visible = False";
mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1017;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1018;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1019;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 1020;BA.debugLine="End Sub";
return "";
}
public static String  _clear_expiration() throws Exception{
 //BA.debugLineNum = 2036;BA.debugLine="Sub CLEAR_EXPIRATION";
 //BA.debugLineNum = 2037;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"-\"";
mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 2038;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"-\"";
mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 2039;BA.debugLine="BUTTON_EXPIRATION.Visible = False";
mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2040;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_reason_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_REASON_SelectedIndexChanged rsub = new ResumableSub_CMB_REASON_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_REASON_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_REASON_SelectedIndexChanged(wingan.app.sales_return_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.sales_return_module parent;
int _index;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 830;BA.debugLine="If BUTTON_SAVE.Text = \" SAVE\" Then";
if (true) break;

case 1:
//if
this.state = 46;
if ((parent.mostCurrent._button_save.getText()).equals(" SAVE")) { 
this.state = 3;
}else {
this.state = 33;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 831;BA.debugLine="If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WARE";
if (true) break;

case 4:
//if
this.state = 31;
if (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("MAIN WAREHOUSE")) { 
this.state = 6;
}else {
this.state = 16;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 832;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Tru";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 833;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
parent.mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 834;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 835;BA.debugLine="If default_reading = \"BOTH\" Or default_reading";
if (true) break;

case 7:
//if
this.state = 14;
if ((parent._default_reading).equals("BOTH") || (parent._default_reading).equals("Expiration Date")) { 
this.state = 9;
}else if((parent._default_reading).equals("Manufacturing Date")) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 9:
//C
this.state = 14;
 //BA.debugLineNum = 836;BA.debugLine="OpenLabel(LABEL_LOAD_EXPIRATION)";
_openlabel(parent.mostCurrent._label_load_expiration);
 if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 838;BA.debugLine="OpenLabel(LABEL_LOAD_MANUFACTURED)";
_openlabel(parent.mostCurrent._label_load_manufactured);
 if (true) break;

case 13:
//C
this.state = 14;
 if (true) break;

case 14:
//C
this.state = 31;
;
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 843;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON";
if (true) break;

case 17:
//if
this.state = 30;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("NEAR EXPIRY")) { 
this.state = 19;
}else {
this.state = 29;
}if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 844;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Tr";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 845;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
parent.mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 846;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 //BA.debugLineNum = 847;BA.debugLine="If default_reading = \"BOTH\" Or default_reading";
if (true) break;

case 20:
//if
this.state = 27;
if ((parent._default_reading).equals("BOTH") || (parent._default_reading).equals("Expiration Date")) { 
this.state = 22;
}else if((parent._default_reading).equals("Manufacturing Date")) { 
this.state = 24;
}else {
this.state = 26;
}if (true) break;

case 22:
//C
this.state = 27;
 //BA.debugLineNum = 848;BA.debugLine="OpenLabel(LABEL_LOAD_EXPIRATION)";
_openlabel(parent.mostCurrent._label_load_expiration);
 if (true) break;

case 24:
//C
this.state = 27;
 //BA.debugLineNum = 850;BA.debugLine="OpenLabel(LABEL_LOAD_MANUFACTURED)";
_openlabel(parent.mostCurrent._label_load_manufactured);
 if (true) break;

case 26:
//C
this.state = 27;
 if (true) break;

case 27:
//C
this.state = 30;
;
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 855;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 856;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 30;
;
 //BA.debugLineNum = 857;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 858;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 if (true) break;

case 30:
//C
this.state = 31;
;
 if (true) break;

case 31:
//C
this.state = 46;
;
 if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 862;BA.debugLine="If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WARE";
if (true) break;

case 34:
//if
this.state = 45;
if (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("MAIN WAREHOUSE")) { 
this.state = 36;
}else {
this.state = 38;
}if (true) break;

case 36:
//C
this.state = 45;
 //BA.debugLineNum = 863;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Tru";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 864;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
parent.mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 865;BA.debugLine="LOAD_EXPIRATION";
_load_expiration();
 if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 867;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON";
if (true) break;

case 39:
//if
this.state = 44;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("NEAR EXPIRY")) { 
this.state = 41;
}else {
this.state = 43;
}if (true) break;

case 41:
//C
this.state = 44;
 //BA.debugLineNum = 868;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Tr";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 869;BA.debugLine="PANEL_BG_EXPIRATION.BringToFront";
parent.mostCurrent._panel_bg_expiration.BringToFront();
 //BA.debugLineNum = 870;BA.debugLine="LOAD_EXPIRATION";
_load_expiration();
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 872;BA.debugLine="CLEAR_EXPIRATION";
_clear_expiration();
 if (true) break;

case 44:
//C
this.state = 45;
;
 if (true) break;

case 45:
//C
this.state = 46;
;
 if (true) break;

case 46:
//C
this.state = -1;
;
 //BA.debugLineNum = 876;BA.debugLine="End Sub";
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
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.sales_return_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 878;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 879;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 880;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 881;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 882;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_warehouse_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_WAREHOUSE_SelectedIndexChanged rsub = new ResumableSub_CMB_WAREHOUSE_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_WAREHOUSE_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_WAREHOUSE_SelectedIndexChanged(wingan.app.sales_return_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 823;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 824;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 825;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 826;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 827;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 828;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 584;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 585;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 586;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 587;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 588;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 589;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 590;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 579;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 580;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 581;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,sales_return_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 582;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 583;BA.debugLine="End Sub";
return null;
}
public static String  _daysbetweendates(long _date1,long _date2) throws Exception{
 //BA.debugLineNum = 2045;BA.debugLine="Sub DaysBetweenDates(Date1 As Long, Date2 As Long)";
 //BA.debugLineNum = 2046;BA.debugLine="Return Floor((Date2 - Date1) / DateTime.TicksPerD";
if (true) return BA.NumberToString(anywheresoftware.b4a.keywords.Common.Floor((_date2-_date1)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerDay));
 //BA.debugLineNum = 2047;BA.debugLine="End Sub";
return "";
}
public static void  _delete_return_disc() throws Exception{
ResumableSub_DELETE_RETURN_DISC rsub = new ResumableSub_DELETE_RETURN_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RETURN_DISC extends BA.ResumableSub {
public ResumableSub_DELETE_RETURN_DISC(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1610;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_retu";
_cmd = _createcommand("delete_return_disc",new Object[]{(Object)(parent._return_id)});
 //BA.debugLineNum = 1611;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1612;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1613;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1614;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1615;BA.debugLine="INSERT_RETURN_DISC";
_insert_return_disc();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1617;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1618;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1619;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1620;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1621;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1622;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1623;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1624;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1625;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1626;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1628;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1629;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1632;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1633;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static void  _delete_return_disc_trail() throws Exception{
ResumableSub_DELETE_RETURN_DISC_TRAIL rsub = new ResumableSub_DELETE_RETURN_DISC_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RETURN_DISC_TRAIL extends BA.ResumableSub {
public ResumableSub_DELETE_RETURN_DISC_TRAIL(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1680;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_retu";
_cmd = _createcommand("delete_return_disc_trail",new Object[]{(Object)(parent._return_id)});
 //BA.debugLineNum = 1681;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1682;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1683;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1684;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1685;BA.debugLine="INSERT_RETURN_DISC_TRAIL";
_insert_return_disc_trail();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1687;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1688;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1689;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1690;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1691;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1692;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1693;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1694;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1695;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1696;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1698;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1699;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1702;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1703;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_return_expiry() throws Exception{
ResumableSub_DELETE_RETURN_EXPIRY rsub = new ResumableSub_DELETE_RETURN_EXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RETURN_EXPIRY extends BA.ResumableSub {
public ResumableSub_DELETE_RETURN_EXPIRY(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1751;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_retu";
_cmd = _createcommand("delete_return_expiry",new Object[]{(Object)(parent._return_id)});
 //BA.debugLineNum = 1752;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1753;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1754;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1755;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1756;BA.debugLine="INSERT_RETURN_EXPIRY";
_insert_return_expiry();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1758;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1759;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1760;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1761;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1762;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1763;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1764;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1765;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1766;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1767;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1769;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1770;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1773;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1774;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_return_ref() throws Exception{
ResumableSub_DELETE_RETURN_REF rsub = new ResumableSub_DELETE_RETURN_REF(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RETURN_REF extends BA.ResumableSub {
public ResumableSub_DELETE_RETURN_REF(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1471;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1472;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 1473;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Data\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Data"));
 //BA.debugLineNum = 1474;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching Data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching Data..."));
 //BA.debugLineNum = 1475;BA.debugLine="LABEL_MSGBOX1.Text = \"Loading, Please wait...\"";
parent.mostCurrent._label_msgbox1.setText(BA.ObjectToCharSequence("Loading, Please wait..."));
 //BA.debugLineNum = 1476;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_retu";
_cmd = _createcommand("delete_return_ref",new Object[]{(Object)(parent._return_id)});
 //BA.debugLineNum = 1477;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1478;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1479;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1480;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1481;BA.debugLine="INSERT_RETURN_REF";
_insert_return_ref();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1483;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1484;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1485;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1486;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1487;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1488;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1489;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1490;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1491;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1492;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1494;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1495;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1498;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1499;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_return_ref_trail() throws Exception{
ResumableSub_DELETE_RETURN_REF_TRAIL rsub = new ResumableSub_DELETE_RETURN_REF_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_RETURN_REF_TRAIL extends BA.ResumableSub {
public ResumableSub_DELETE_RETURN_REF_TRAIL(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1543;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_retu";
_cmd = _createcommand("delete_return_ref_trail",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 1544;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1545;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1546;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1547;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1548;BA.debugLine="INSERT_RETURN_REF_TRAIL";
_insert_return_ref_trail();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1550;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1551;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1552;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1553;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1554;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1555;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1556;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1557;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1558;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1559;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1561;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1562;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1565;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1566;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _edittext_quantity_enterpressed() throws Exception{
 //BA.debugLineNum = 2092;BA.debugLine="Sub EDITTEXT_QUANTITY_EnterPressed";
 //BA.debugLineNum = 2093;BA.debugLine="OpenButton(BUTTON_SAVE)";
_openbutton(mostCurrent._button_save);
 //BA.debugLineNum = 2094;BA.debugLine="End Sub";
return "";
}
public static void  _get_customer() throws Exception{
ResumableSub_GET_CUSTOMER rsub = new ResumableSub_GET_CUSTOMER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_CUSTOMER extends BA.ResumableSub {
public ResumableSub_GET_CUSTOMER(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
int _isss = 0;
wingan.app.dbrequestmanager _req1 = null;
wingan.app.main._dbcommand _cmd1 = null;
wingan.app.httpjob _js = null;
wingan.app.main._dbresult _res1 = null;
Object[] _row1 = null;
String _query = "";
int step7;
int limit7;
anywheresoftware.b4a.BA.IterableList group17;
int index17;
int groupLen17;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 592;BA.debugLine="ProgressDialogShow2(\"Getting Customer...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Getting Customer..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 593;BA.debugLine="SearchTemplate2.CustomListView1.Clear";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 594;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE return_route_id = '"+parent.mostCurrent._return_module._return_route_id /*String*/ +"'")));
 //BA.debugLineNum = 595;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 26;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 25;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 596;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM customer_ta";
parent._connection.ExecNonQuery("DELETE FROM customer_table");
 //BA.debugLineNum = 597;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 4;
;
 //BA.debugLineNum = 598;BA.debugLine="For isss = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 23;
step7 = 1;
limit7 = (int) (parent._cursor3.getRowCount()-1);
_isss = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 23;
if ((step7 > 0 && _isss <= limit7) || (step7 < 0 && _isss >= limit7)) this.state = 6;
if (true) break;

case 29:
//C
this.state = 28;
_isss = ((int)(0 + _isss + step7)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 599;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 7;
;
 //BA.debugLineNum = 600;BA.debugLine="cursor3.Position = isss";
parent._cursor3.setPosition(_isss);
 //BA.debugLineNum = 601;BA.debugLine="Dim req1 As DBRequestManager = CreateRequest";
_req1 = _createrequest();
 //BA.debugLineNum = 602;BA.debugLine="Dim cmd1 As DBCommand = CreateCommand(\"select_r";
_cmd1 = _createcommand("select_return_customer",(Object[])(new String[]{parent._cursor3.GetString("picklist_id")}));
 //BA.debugLineNum = 603;BA.debugLine="Wait For (req1.ExecuteQuery(cmd1, 0, Null)) Job";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req1._executequery /*wingan.app.httpjob*/ (_cmd1,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 31;
return;
case 31:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 604;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 22;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 21;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 606;BA.debugLine="req1.HandleJobAsync(js, \"req1\")";
_req1._handlejobasync /*void*/ (_js,"req1");
 //BA.debugLineNum = 607;BA.debugLine="Wait For (req1) req1_Result(res1 As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req1_result", processBA, this, (Object)(_req1));
this.state = 32;
return;
case 32:
//C
this.state = 10;
_res1 = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 608;BA.debugLine="If res1.Rows.Size > 0 Then";
if (true) break;

case 10:
//if
this.state = 19;
if (_res1.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 12;
}else {
this.state = 18;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 609;BA.debugLine="For Each row1() As Object In res1.Rows";
if (true) break;

case 13:
//for
this.state = 16;
group17 = _res1.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index17 = 0;
groupLen17 = group17.getSize();
this.state = 33;
if (true) break;

case 33:
//C
this.state = 16;
if (index17 < groupLen17) {
this.state = 15;
_row1 = (Object[])(group17.Get(index17));}
if (true) break;

case 34:
//C
this.state = 33;
index17++;
if (true) break;

case 15:
//C
this.state = 34;
 //BA.debugLineNum = 610;BA.debugLine="Dim query As String = \"INSERT INTO customer_";
_query = "INSERT INTO customer_table VALUES (?,?)";
 //BA.debugLineNum = 611;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("customer_id"))))]),BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("account_customer_name"))))])}));
 //BA.debugLineNum = 612;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 34;
;
 //BA.debugLineNum = 613;BA.debugLine="ToastMessageShow(\"Customer Acquired\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Customer Acquired"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;
if (true) break;

case 16:
//C
this.state = 19;
;
 //BA.debugLineNum = 615;BA.debugLine="LOAD_CUSTOMER";
_load_customer();
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 617;BA.debugLine="Msgbox2Async(\"This picklist have no exisiting";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This picklist have no exisiting customer"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 620;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 621;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("741156638","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 622;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 623;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 22:
//C
this.state = 29;
;
 //BA.debugLineNum = 625;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 23:
//C
this.state = 26;
;
 //BA.debugLineNum = 627;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 628;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 36;
return;
case 36:
//C
this.state = 26;
;
 //BA.debugLineNum = 629;BA.debugLine="GET_SALESMAN";
_get_salesman();
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 631;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 632;BA.debugLine="Msgbox2Async(\"Picklist Route is empty, Please ad";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Picklist Route is empty, Please advice IT for this conflict."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 634;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _req1_result(wingan.app.main._dbresult _res1) throws Exception{
}
public static String  _get_customer_id() throws Exception{
int _i = 0;
 //BA.debugLineNum = 744;BA.debugLine="Sub GET_CUSTOMER_ID";
 //BA.debugLineNum = 745;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM cus";
_cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM customer_table WHERE customer_name = '"+mostCurrent._label_load_customer_name.getText()+"'")));
 //BA.debugLineNum = 746;BA.debugLine="For i = 0 To cursor4.RowCount - 1";
{
final int step2 = 1;
final int limit2 = (int) (_cursor4.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 747;BA.debugLine="cursor4.Position = i";
_cursor4.setPosition(_i);
 //BA.debugLineNum = 748;BA.debugLine="customer_id = cursor4.GetString(\"customer_id\")";
_customer_id = _cursor4.GetString("customer_id");
 }
};
 //BA.debugLineNum = 750;BA.debugLine="End Sub";
return "";
}
public static String  _get_exp() throws Exception{
 //BA.debugLineNum = 1894;BA.debugLine="Sub GET_EXP";
 //BA.debugLineNum = 1895;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = \"";
if ((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("01")) { 
 //BA.debugLineNum = 1896;BA.debugLine="monthexp = \"January\"";
_monthexp = "January";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("02")) { 
 //BA.debugLineNum = 1898;BA.debugLine="monthexp = \"February\"";
_monthexp = "February";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("03")) { 
 //BA.debugLineNum = 1900;BA.debugLine="monthexp = \"March\"";
_monthexp = "March";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("04")) { 
 //BA.debugLineNum = 1902;BA.debugLine="monthexp = \"April\"";
_monthexp = "April";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("05")) { 
 //BA.debugLineNum = 1904;BA.debugLine="monthexp = \"May\"";
_monthexp = "May";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("06")) { 
 //BA.debugLineNum = 1906;BA.debugLine="monthexp = \"June\"";
_monthexp = "June";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("07")) { 
 //BA.debugLineNum = 1908;BA.debugLine="monthexp = \"July\"";
_monthexp = "July";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("08")) { 
 //BA.debugLineNum = 1910;BA.debugLine="monthexp = \"August\"";
_monthexp = "August";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("09")) { 
 //BA.debugLineNum = 1912;BA.debugLine="monthexp = \"September\"";
_monthexp = "September";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("10")) { 
 //BA.debugLineNum = 1914;BA.debugLine="monthexp = \"October\"";
_monthexp = "October";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("11")) { 
 //BA.debugLineNum = 1916;BA.debugLine="monthexp = \"November\"";
_monthexp = "November";
 }else if((mostCurrent._label_load_expiration.getText().substring((int) (5),(int) (7))).equals("12")) { 
 //BA.debugLineNum = 1918;BA.debugLine="monthexp = \"December\"";
_monthexp = "December";
 }else {
 //BA.debugLineNum = 1920;BA.debugLine="monthexp = \"NO EXPIRATION\"";
_monthexp = "NO EXPIRATION";
 };
 //BA.debugLineNum = 1923;BA.debugLine="yearexp = LABEL_LOAD_EXPIRATION.Text.SubString2(0";
_yearexp = mostCurrent._label_load_expiration.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 1924;BA.debugLine="End Sub";
return "";
}
public static String  _get_expiration_span() throws Exception{
String _days_year = "";
String _days_month = "";
long _manufacturing = 0L;
 //BA.debugLineNum = 1956;BA.debugLine="Sub GET_EXPIRATION_SPAN";
 //BA.debugLineNum = 1957;BA.debugLine="Dim days_year As String";
_days_year = "";
 //BA.debugLineNum = 1958;BA.debugLine="Dim days_month As String";
_days_month = "";
 //BA.debugLineNum = 1960;BA.debugLine="If lifespan_year <> \"\" Then";
if ((_lifespan_year).equals("") == false) { 
 //BA.debugLineNum = 1961;BA.debugLine="days_year = lifespan_year.SubString2(0,lifespan_";
_days_year = _lifespan_year.substring((int) (0),(int) (_lifespan_year.indexOf("Y")-1));
 }else {
 //BA.debugLineNum = 1963;BA.debugLine="days_year = 0";
_days_year = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1966;BA.debugLine="If lifespan_month <> \"\" Then";
if ((_lifespan_month).equals("") == false) { 
 //BA.debugLineNum = 1967;BA.debugLine="days_month = lifespan_month.SubString2(0,lifespa";
_days_month = _lifespan_month.substring((int) (0),(int) (_lifespan_month.indexOf("M")-1));
 }else {
 //BA.debugLineNum = 1969;BA.debugLine="days_month = 0";
_days_month = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1972;BA.debugLine="Log(days_year & \" \" &days_month)";
anywheresoftware.b4a.keywords.Common.LogImpl("744105744",_days_year+" "+_days_month,0);
 //BA.debugLineNum = 1974;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text <> \"NO EXPIRATION\"";
if ((mostCurrent._label_load_expiration.getText()).equals("NO EXPIRATION") == false) { 
 //BA.debugLineNum = 1975;BA.debugLine="If lifespan_year = \"\" And lifespan_month = \"\" Th";
if ((_lifespan_year).equals("") && (_lifespan_month).equals("")) { 
 }else {
 //BA.debugLineNum = 1978;BA.debugLine="Dim manufacturing As Long = DateTime.Add(DateTi";
_manufacturing = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_expiration.getText()),(int) (-(double)(Double.parseDouble(_days_year))),(int) (-(double)(Double.parseDouble(_days_month))),(int) (0));
 //BA.debugLineNum = 1979;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(ma";
mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_manufacturing)));
 };
 };
 //BA.debugLineNum = 1982;BA.debugLine="End Sub";
return "";
}
public static String  _get_manufacturing_span() throws Exception{
String _days_year = "";
String _days_month = "";
long _expiration = 0L;
 //BA.debugLineNum = 1983;BA.debugLine="Sub GET_MANUFACTURING_SPAN";
 //BA.debugLineNum = 1984;BA.debugLine="Dim days_year As String";
_days_year = "";
 //BA.debugLineNum = 1985;BA.debugLine="Dim days_month As String";
_days_month = "";
 //BA.debugLineNum = 1987;BA.debugLine="If lifespan_year <> \"\" Then";
if ((_lifespan_year).equals("") == false) { 
 //BA.debugLineNum = 1988;BA.debugLine="days_year = lifespan_year.SubString2(0,lifespan_";
_days_year = _lifespan_year.substring((int) (0),(int) (_lifespan_year.indexOf("Y")-1));
 }else {
 //BA.debugLineNum = 1990;BA.debugLine="days_year = 0";
_days_year = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1993;BA.debugLine="If lifespan_month <> \"\" Then";
if ((_lifespan_month).equals("") == false) { 
 //BA.debugLineNum = 1994;BA.debugLine="days_month = lifespan_month.SubString2(0,lifespa";
_days_month = _lifespan_month.substring((int) (0),(int) (_lifespan_month.indexOf("M")-1));
 }else {
 //BA.debugLineNum = 1996;BA.debugLine="days_month = 0";
_days_month = BA.NumberToString(0);
 };
 //BA.debugLineNum = 1999;BA.debugLine="Log(days_year & \" \" &days_month)";
anywheresoftware.b4a.keywords.Common.LogImpl("744171280",_days_year+" "+_days_month,0);
 //BA.debugLineNum = 2001;BA.debugLine="If LABEL_LOAD_MANUFACTURED.Text <> \"NO EXPIRATION";
if ((mostCurrent._label_load_manufactured.getText()).equals("NO EXPIRATION") == false) { 
 //BA.debugLineNum = 2002;BA.debugLine="If lifespan_year = \"\" And lifespan_month = \"\" Th";
if ((_lifespan_year).equals("") && (_lifespan_month).equals("")) { 
 }else {
 //BA.debugLineNum = 2005;BA.debugLine="Dim expiration As Long = DateTime.Add(DateTime.";
_expiration = anywheresoftware.b4a.keywords.Common.DateTime.Add(anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_manufactured.getText()),(int)(Double.parseDouble(_days_year)),(int)(Double.parseDouble(_days_month)),(int) (0));
 //BA.debugLineNum = 2006;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = DateTime.Date(expi";
mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(_expiration)));
 };
 };
 //BA.debugLineNum = 2009;BA.debugLine="End Sub";
return "";
}
public static String  _get_mfg() throws Exception{
 //BA.debugLineNum = 1925;BA.debugLine="Sub GET_MFG";
 //BA.debugLineNum = 1926;BA.debugLine="If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) =";
if ((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("01")) { 
 //BA.debugLineNum = 1927;BA.debugLine="monthmfg = \"January\"";
_monthmfg = "January";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("02")) { 
 //BA.debugLineNum = 1929;BA.debugLine="monthmfg = \"February\"";
_monthmfg = "February";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("03")) { 
 //BA.debugLineNum = 1931;BA.debugLine="monthmfg = \"March\"";
_monthmfg = "March";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("04")) { 
 //BA.debugLineNum = 1933;BA.debugLine="monthmfg = \"April\"";
_monthmfg = "April";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("05")) { 
 //BA.debugLineNum = 1935;BA.debugLine="monthmfg = \"May\"";
_monthmfg = "May";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("06")) { 
 //BA.debugLineNum = 1937;BA.debugLine="monthmfg = \"June\"";
_monthmfg = "June";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("07")) { 
 //BA.debugLineNum = 1939;BA.debugLine="monthmfg = \"July\"";
_monthmfg = "July";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("08")) { 
 //BA.debugLineNum = 1941;BA.debugLine="monthmfg = \"August\"";
_monthmfg = "August";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("09")) { 
 //BA.debugLineNum = 1943;BA.debugLine="monthmfg = \"September\"";
_monthmfg = "September";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("10")) { 
 //BA.debugLineNum = 1945;BA.debugLine="monthmfg = \"October\"";
_monthmfg = "October";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("11")) { 
 //BA.debugLineNum = 1947;BA.debugLine="monthmfg = \"November\"";
_monthmfg = "November";
 }else if((mostCurrent._label_load_manufactured.getText().substring((int) (5),(int) (7))).equals("12")) { 
 //BA.debugLineNum = 1949;BA.debugLine="monthmfg = \"December\"";
_monthmfg = "December";
 }else {
 //BA.debugLineNum = 1951;BA.debugLine="monthmfg = \"NO MANUFACTURING\"";
_monthmfg = "NO MANUFACTURING";
 };
 //BA.debugLineNum = 1954;BA.debugLine="yearmfg = LABEL_LOAD_MANUFACTURED.Text.SubString2";
_yearmfg = mostCurrent._label_load_manufactured.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 1955;BA.debugLine="End Sub";
return "";
}
public static String  _get_return_number() throws Exception{
int _i = 0;
 //BA.debugLineNum = 733;BA.debugLine="Sub GET_RETURN_NUMBER";
 //BA.debugLineNum = 734;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT MAX(CAST(r";
_cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT MAX(CAST(return_number as INT)) as 'return_number' from sales_return_ref_table")));
 //BA.debugLineNum = 735;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
{
final int step2 = 1;
final int limit2 = (int) (_cursor6.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 736;BA.debugLine="cursor6.Position = i";
_cursor6.setPosition(_i);
 //BA.debugLineNum = 737;BA.debugLine="If cursor6.GetString(\"return_number\") = Null Or";
if (_cursor6.GetString("return_number")== null || (_cursor6.GetString("return_number")).equals("")) { 
 //BA.debugLineNum = 738;BA.debugLine="return_number =1";
_return_number = BA.NumberToString(1);
 }else {
 //BA.debugLineNum = 740;BA.debugLine="return_number = cursor6.GetString(\"return_numbe";
_return_number = BA.NumberToString((double)(Double.parseDouble(_cursor6.GetString("return_number")))+1);
 };
 }
};
 //BA.debugLineNum = 743;BA.debugLine="End Sub";
return "";
}
public static void  _get_salesman() throws Exception{
ResumableSub_GET_SALESMAN rsub = new ResumableSub_GET_SALESMAN(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_SALESMAN extends BA.ResumableSub {
public ResumableSub_GET_SALESMAN(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
int _iaaa = 0;
wingan.app.dbrequestmanager _req1 = null;
wingan.app.main._dbcommand _cmd1 = null;
wingan.app.httpjob _js = null;
wingan.app.main._dbresult _res1 = null;
Object[] _row1 = null;
String _query = "";
int _result = 0;
int step6;
int limit6;
anywheresoftware.b4a.BA.IterableList group16;
int index16;
int groupLen16;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 636;BA.debugLine="ProgressDialogShow2(\"Getting Salesman...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Getting Salesman..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 637;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE  return_route_id = '"+parent.mostCurrent._return_module._return_route_id /*String*/ +"'")));
 //BA.debugLineNum = 638;BA.debugLine="If cursor9.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 38;
if (parent._cursor9.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 37;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 639;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM salesman_ta";
parent._connection.ExecNonQuery("DELETE FROM salesman_table");
 //BA.debugLineNum = 640;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 39;
return;
case 39:
//C
this.state = 4;
;
 //BA.debugLineNum = 641;BA.debugLine="For iaaa = 0 To cursor9.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 23;
step6 = 1;
limit6 = (int) (parent._cursor9.getRowCount()-1);
_iaaa = (int) (0) ;
this.state = 40;
if (true) break;

case 40:
//C
this.state = 23;
if ((step6 > 0 && _iaaa <= limit6) || (step6 < 0 && _iaaa >= limit6)) this.state = 6;
if (true) break;

case 41:
//C
this.state = 40;
_iaaa = ((int)(0 + _iaaa + step6)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 642;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 7;
;
 //BA.debugLineNum = 643;BA.debugLine="cursor9.Position = iaaa";
parent._cursor9.setPosition(_iaaa);
 //BA.debugLineNum = 645;BA.debugLine="Dim req1 As DBRequestManager = CreateRequest";
_req1 = _createrequest();
 //BA.debugLineNum = 646;BA.debugLine="Dim cmd1 As DBCommand = CreateCommand(\"select_s";
_cmd1 = _createcommand("select_salesman",(Object[])(new String[]{parent._cursor9.GetString("picklist_id")}));
 //BA.debugLineNum = 647;BA.debugLine="Wait For (req1.ExecuteQuery(cmd1, 0, Null)) Job";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req1._executequery /*wingan.app.httpjob*/ (_cmd1,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 43;
return;
case 43:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 648;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 22;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 21;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 650;BA.debugLine="req1.HandleJobAsync(js, \"req1\")";
_req1._handlejobasync /*void*/ (_js,"req1");
 //BA.debugLineNum = 651;BA.debugLine="Wait For (req1) req1_Result(res1 As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req1_result", processBA, this, (Object)(_req1));
this.state = 44;
return;
case 44:
//C
this.state = 10;
_res1 = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 652;BA.debugLine="If res1.Rows.Size > 0 Then";
if (true) break;

case 10:
//if
this.state = 19;
if (_res1.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 12;
}else {
this.state = 18;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 653;BA.debugLine="For Each row1() As Object In res1.Rows";
if (true) break;

case 13:
//for
this.state = 16;
group16 = _res1.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index16 = 0;
groupLen16 = group16.getSize();
this.state = 45;
if (true) break;

case 45:
//C
this.state = 16;
if (index16 < groupLen16) {
this.state = 15;
_row1 = (Object[])(group16.Get(index16));}
if (true) break;

case 46:
//C
this.state = 45;
index16++;
if (true) break;

case 15:
//C
this.state = 46;
 //BA.debugLineNum = 654;BA.debugLine="Dim query As String = \"INSERT INTO salesman_";
_query = "INSERT INTO salesman_table VALUES (?,?,?,?)";
 //BA.debugLineNum = 655;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("employee_id"))))]),BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("sales_position_id"))))]),BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("sales_position_name"))))]),BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("last_name"))))])+" "+BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("first_name"))))])}));
 //BA.debugLineNum = 657;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 46;
;
 //BA.debugLineNum = 658;BA.debugLine="ToastMessageShow(\"Salesman Acquired\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Salesman Acquired"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 659;BA.debugLine="Log(row1(res1.Columns.Get(\"sales_position_na";
anywheresoftware.b4a.keywords.Common.LogImpl("741222168",BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("sales_position_name"))))])+"("+BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("last_name"))))])+" "+BA.ObjectToString(_row1[(int)(BA.ObjectToNumber(_res1.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("first_name"))))])+")",0);
 if (true) break;
if (true) break;

case 16:
//C
this.state = 19;
;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 662;BA.debugLine="Msgbox2Async(\"This picklist have no exisiting";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This picklist have no exisiting customer"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 665;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 666;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("741222175","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 667;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 668;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 22:
//C
this.state = 41;
;
 //BA.debugLineNum = 670;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 23:
//C
this.state = 24;
;
 //BA.debugLineNum = 672;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 673;BA.debugLine="LOAD_SALESMAN";
_load_salesman();
 //BA.debugLineNum = 674;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 48;
return;
case 48:
//C
this.state = 24;
;
 //BA.debugLineNum = 675;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM sa";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_ref_table WHERE return_route_id = '"+parent.mostCurrent._return_module._return_route_id /*String*/ +"'")));
 //BA.debugLineNum = 676;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 24:
//if
this.state = 35;
if (parent._cursor7.getRowCount()>0) { 
this.state = 26;
}else {
this.state = 34;
}if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 677;BA.debugLine="Msgbox2Async(\"Do you want to add a new transac";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Do you want to add a new transaction?"),BA.ObjectToCharSequence("Option"),"NEW","CANCEL","SEARCH EXISTING",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 678;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 49;
return;
case 49:
//C
this.state = 27;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 679;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 27:
//if
this.state = 32;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 29;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 31;
}if (true) break;

case 29:
//C
this.state = 32;
 //BA.debugLineNum = 680;BA.debugLine="SHOW_CUSTOMER";
_show_customer();
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 682;BA.debugLine="LOAD_RETURN_UPLOAD";
_load_return_upload();
 if (true) break;

case 32:
//C
this.state = 35;
;
 if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 685;BA.debugLine="SHOW_CUSTOMER";
_show_customer();
 if (true) break;

case 35:
//C
this.state = 38;
;
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 688;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 689;BA.debugLine="Msgbox2Async(\"Picklist Route is empty, Please ad";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Picklist Route is empty, Please advice IT for this conflict."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 38:
//C
this.state = -1;
;
 //BA.debugLineNum = 691;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_salesman_id() throws Exception{
int _i = 0;
 //BA.debugLineNum = 883;BA.debugLine="Sub GET_SALESMAN_ID";
 //BA.debugLineNum = 884;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM sal";
_cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM salesman_table WHERE sales_position_name = '"+mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem().substring((int) (0),mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem().indexOf("("))+"'")));
 //BA.debugLineNum = 885;BA.debugLine="For i = 0 To cursor4.RowCount - 1";
{
final int step2 = 1;
final int limit2 = (int) (_cursor4.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 886;BA.debugLine="cursor4.Position = i";
_cursor4.setPosition(_i);
 //BA.debugLineNum = 887;BA.debugLine="sales_position_id = cursor4.GetString(\"sales_pos";
_sales_position_id = _cursor4.GetString("sales_position_id");
 //BA.debugLineNum = 888;BA.debugLine="Log(sales_position_id)";
anywheresoftware.b4a.keywords.Common.LogImpl("742139653",_sales_position_id,0);
 }
};
 //BA.debugLineNum = 890;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 334;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 335;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 336;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 337;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 338;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 341;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 342;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 101;BA.debugLine="Private NameColumn(8) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (8)];
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
 //BA.debugLineNum = 117;BA.debugLine="Private LABEL_LOAD_CUSTOMER_NAME As Label";
mostCurrent._label_load_customer_name = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Private LABEL_LOAD_RETURN_DATE As Label";
mostCurrent._label_load_return_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Private LABEL_LOAD_RETURN_ID As Label";
mostCurrent._label_load_return_id = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 120;BA.debugLine="Private CMB_SALESMAN As B4XComboBox";
mostCurrent._cmb_salesman = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 121;BA.debugLine="Private CMB_WAREHOUSE As B4XComboBox";
mostCurrent._cmb_warehouse = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 122;BA.debugLine="Private CMB_REASON As B4XComboBox";
mostCurrent._cmb_reason = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 123;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 125;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 127;BA.debugLine="Private LABEL_LOAD_PRINCIPAL As Label";
mostCurrent._label_load_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Private BUTTON_EXIT As Button";
mostCurrent._button_exit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private PANEL_BG_INPUT As Panel";
mostCurrent._panel_bg_input = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Private BUTTON_SAVE As Button";
mostCurrent._button_save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Private TABLE_SALES_RETURN As B4XTable";
mostCurrent._table_sales_return = new wingan.app.b4xtable();
 //BA.debugLineNum = 132;BA.debugLine="Private BUTTON_CANCEL As Button";
mostCurrent._button_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 133;BA.debugLine="Private LVL_RID As ListView";
mostCurrent._lvl_rid = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 134;BA.debugLine="Private PANEL_BG_RID As Panel";
mostCurrent._panel_bg_rid = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 135;BA.debugLine="Private BUTTON_MANUAL As Button";
mostCurrent._button_manual = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 136;BA.debugLine="Private BUTTON_UPLOAD As Button";
mostCurrent._button_upload = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 138;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 139;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 140;BA.debugLine="Private LABEL_HEADER_LOGO As Label";
mostCurrent._label_header_logo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 141;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 142;BA.debugLine="Private PANEL_BG_EXPIRATION As Panel";
mostCurrent._panel_bg_expiration = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Private LABEL_LOAD_EXPIRATION As Label";
mostCurrent._label_load_expiration = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 144;BA.debugLine="Private LABEL_LOAD_MANUFACTURED As Label";
mostCurrent._label_load_manufactured = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 145;BA.debugLine="Private BUTTON_EXPIRATION As Button";
mostCurrent._button_expiration = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 146;BA.debugLine="End Sub";
return "";
}
public static void  _input_expiry() throws Exception{
ResumableSub_INPUT_EXPIRY rsub = new ResumableSub_INPUT_EXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_EXPIRY extends BA.ResumableSub {
public ResumableSub_INPUT_EXPIRY(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 2067;BA.debugLine="GET_EXP";
_get_exp();
 //BA.debugLineNum = 2068;BA.debugLine="GET_MFG";
_get_mfg();
 //BA.debugLineNum = 2069;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 2070;BA.debugLine="Dim query As String = \"INSERT INTO sales_return_e";
_query = "INSERT INTO sales_return_expiry_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 2071;BA.debugLine="connection.ExecNonQuery2(query,Array As String(L";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._label_load_return_id.getText(),parent._transaction_number,parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent._monthexp,parent._yearexp,parent.mostCurrent._label_load_expiration.getText(),parent._monthmfg,parent._yearmfg,parent.mostCurrent._label_load_manufactured.getText(),parent._scan_code,parent._reason,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 2075;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 2076;BA.debugLine="ToastMessageShow(\"Product Expiration Added Succes";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Expiration Added Succesfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2077;BA.debugLine="End Sub";
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
public ResumableSub_INPUT_NEAR_EXPIRY(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 2049;BA.debugLine="If LABEL_LOAD_EXPIRATION.Text <> \"-\" Then";
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
 //BA.debugLineNum = 2050;BA.debugLine="Dim EXPDATE As Long = DateTime.DateParse(LABEL_L";
_expdate = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(parent.mostCurrent._label_load_expiration.getText());
 //BA.debugLineNum = 2051;BA.debugLine="Dim  DATENOW As Long = DateTime.DateParse(DateTi";
_datenow = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2052;BA.debugLine="If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then";
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
 //BA.debugLineNum = 2053;BA.debugLine="ToastMessageShow(\"You cannot input a expiration";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You cannot input a expiration date from to date or back date."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 2055;BA.debugLine="PANEL_BG_EXPIRATION.SetVisibleAnimated(300, Fal";
parent.mostCurrent._panel_bg_expiration.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2056;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 9;
;
 //BA.debugLineNum = 2057;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 2058;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 2059;BA.debugLine="BUTTON_EXPIRATION.Visible = True";
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
 //BA.debugLineNum = 2065;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_return_disc() throws Exception{
ResumableSub_INSERT_RETURN_DISC rsub = new ResumableSub_INSERT_RETURN_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_RETURN_DISC extends BA.ResumableSub {
public ResumableSub_INSERT_RETURN_DISC(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1635;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1636;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT * FROM sal";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"+parent._return_id+"'")));
 //BA.debugLineNum = 1637;BA.debugLine="If cursor8.RowCount > 0 Then";
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
 //BA.debugLineNum = 1638;BA.debugLine="For i = 0 To cursor8.RowCount - 1";
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
 //BA.debugLineNum = 1639;BA.debugLine="cursor8.Position = i";
parent._cursor8.setPosition(_i);
 //BA.debugLineNum = 1640;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 1641;BA.debugLine="Log (cursor8.GetString(\"product_description\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("743450375",parent._cursor8.GetString("product_description"),0);
 //BA.debugLineNum = 1642;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_return_disc",(Object[])(new String[]{parent._cursor8.GetString("return_id"),parent._cursor8.GetString("transaction_number"),parent._cursor8.GetString("salesman_position_id"),parent._cursor8.GetString("salesman_name"),parent._cursor8.GetString("principal_id"),parent._cursor8.GetString("principal_name"),parent._cursor8.GetString("warehouse"),parent._cursor8.GetString("product_id"),parent._cursor8.GetString("product_variant"),parent._cursor8.GetString("product_description"),parent._cursor8.GetString("unit"),parent._cursor8.GetString("quantity"),parent._cursor8.GetString("total_pieces"),parent._cursor8.GetString("scan_code"),parent._cursor8.GetString("input_reason"),parent._cursor8.GetString("return_reason"),parent._cursor8.GetString("date_registered"),parent._cursor8.GetString("time_registered"),parent._cursor8.GetString("user_info"),parent._cursor8.GetString("tab_id")}));
 //BA.debugLineNum = 1646;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1647;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor8.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor8.GetString("product_description")));
 //BA.debugLineNum = 1648;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1649;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1652;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1653;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1654;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1655;BA.debugLine="Log(\"INSERT_RETURN_DISC ERROR: \" & js.ErrorMes";
anywheresoftware.b4a.keywords.Common.LogImpl("743450389","INSERT_RETURN_DISC ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1656;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1657;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1658;BA.debugLine="Sleep(1000)";
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
 //BA.debugLineNum = 1660;BA.debugLine="js.Release";
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
 //BA.debugLineNum = 1663;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1664;BA.debugLine="If error_trigger = 0 Then";
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
 //BA.debugLineNum = 1665;BA.debugLine="DELETE_RETURN_DISC_TRAIL";
_delete_return_disc_trail();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1667;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1668;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1669;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1670;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1671;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1672;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1674;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1675;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1678;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_return_disc_trail() throws Exception{
ResumableSub_INSERT_RETURN_DISC_TRAIL rsub = new ResumableSub_INSERT_RETURN_DISC_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_RETURN_DISC_TRAIL extends BA.ResumableSub {
public ResumableSub_INSERT_RETURN_DISC_TRAIL(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1705;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1706;BA.debugLine="cursor9 = connection.ExecQuery(\"SELECT * FROM sal";
parent._cursor9 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_disc_table_trail WHERE return_id = '"+parent._return_id+"'")));
 //BA.debugLineNum = 1707;BA.debugLine="If cursor9.RowCount > 0 Then";
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
 //BA.debugLineNum = 1708;BA.debugLine="For i = 0 To cursor9.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor9.getRowCount()-1);
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
 //BA.debugLineNum = 1709;BA.debugLine="cursor9.Position = i";
parent._cursor9.setPosition(_i);
 //BA.debugLineNum = 1710;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 1711;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_return_disc_trail",(Object[])(new String[]{parent._cursor9.GetString("return_id"),parent._cursor9.GetString("transaction_number"),parent._cursor9.GetString("salesman_position_id"),parent._cursor9.GetString("salesman_name"),parent._cursor9.GetString("principal_id"),parent._cursor9.GetString("principal_name"),parent._cursor9.GetString("warehouse"),parent._cursor9.GetString("product_id"),parent._cursor9.GetString("product_variant"),parent._cursor9.GetString("product_description"),parent._cursor9.GetString("unit"),parent._cursor9.GetString("quantity"),parent._cursor9.GetString("total_pieces"),parent._cursor9.GetString("scan_code"),parent._cursor9.GetString("input_reason"),parent._cursor9.GetString("return_reason"),parent._cursor9.GetString("date_registered"),parent._cursor9.GetString("time_registered"),parent._cursor9.GetString("user_info"),parent._cursor9.GetString("tab_id"),parent._cursor9.GetString("date_modified"),parent._cursor9.GetString("time_modified"),parent._cursor9.GetString("modified_by"),parent._cursor9.GetString("modified_type")}));
 //BA.debugLineNum = 1716;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1717;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor9.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor9.GetString("product_description")));
 //BA.debugLineNum = 1718;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1719;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1722;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1723;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1724;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1725;BA.debugLine="Log(\"INSERT_RETURN_DISC_TRAIL ERROR: \" & js.Er";
anywheresoftware.b4a.keywords.Common.LogImpl("743581461","INSERT_RETURN_DISC_TRAIL ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1726;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1727;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1728;BA.debugLine="Sleep(1000)";
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
 //BA.debugLineNum = 1730;BA.debugLine="js.Release";
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
 //BA.debugLineNum = 1733;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1734;BA.debugLine="If error_trigger = 0 Then";
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
 //BA.debugLineNum = 1735;BA.debugLine="DELETE_RETURN_EXPIRY";
_delete_return_expiry();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1738;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1739;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1740;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1741;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1742;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1743;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1745;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1746;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1749;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_return_expiry() throws Exception{
ResumableSub_INSERT_RETURN_EXPIRY rsub = new ResumableSub_INSERT_RETURN_EXPIRY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_RETURN_EXPIRY extends BA.ResumableSub {
public ResumableSub_INSERT_RETURN_EXPIRY(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1776;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1777;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM sal";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_expiry_table WHERE return_id = '"+parent._return_id+"'")));
 //BA.debugLineNum = 1778;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 1779;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
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
 //BA.debugLineNum = 1780;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 1781;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 30;
return;
case 30:
//C
this.state = 7;
;
 //BA.debugLineNum = 1782;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_return_expiry",(Object[])(new String[]{parent._cursor2.GetString("return_id"),parent._cursor2.GetString("transaction_number"),parent._cursor2.GetString("principal_id"),parent._cursor2.GetString("principal_name"),parent._cursor2.GetString("warehouse"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("product_variant"),parent._cursor2.GetString("product_description"),parent._cursor2.GetString("unit"),parent._cursor2.GetString("quantity"),parent._cursor2.GetString("total_pieces"),parent._cursor2.GetString("month_expired"),parent._cursor2.GetString("year_expired"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("month_manufactured"),parent._cursor2.GetString("year_manufactured"),parent._cursor2.GetString("date_manufactured"),parent._cursor2.GetString("scan_code"),parent._cursor2.GetString("input_reason"),parent._cursor2.GetString("date_registered"),parent._cursor2.GetString("time_registered"),parent._cursor2.GetString("user_info"),parent._cursor2.GetString("tab_id"),"EXISTING"}));
 //BA.debugLineNum = 1787;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1788;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor2.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor2.GetString("product_description")));
 //BA.debugLineNum = 1789;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 31;
return;
case 31:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1790;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1793;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1794;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1795;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1796;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("743712533","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1797;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1798;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1799;BA.debugLine="Sleep(1000)";
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
 //BA.debugLineNum = 1801;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1803;BA.debugLine="If error_trigger = 0 Then";

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
 //BA.debugLineNum = 1804;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1805;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploaded Successfully...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploaded Successfully..."));
 //BA.debugLineNum = 1806;BA.debugLine="ToastMessageShow(\"Uploaded Successfully\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploaded Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1807;BA.debugLine="LOG_RETURN_UPLOAD";
_log_return_upload();
 //BA.debugLineNum = 1808;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 33;
return;
case 33:
//C
this.state = 24;
;
 //BA.debugLineNum = 1809;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = \"-\"";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1810;BA.debugLine="LABEL_LOAD_RETURN_DATE.Text = \"-\"";
parent.mostCurrent._label_load_return_date.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1811;BA.debugLine="LABEL_LOAD_RETURN_ID.Text = \"-\"";
parent.mostCurrent._label_load_return_id.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1812;BA.debugLine="return_id = \"-\"";
parent._return_id = "-";
 //BA.debugLineNum = 1813;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 //BA.debugLineNum = 1814;BA.debugLine="BUTTON_MANUAL.Visible = False";
parent.mostCurrent._button_manual.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1815;BA.debugLine="BUTTON_UPLOAD.Visible = False";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1816;BA.debugLine="PANEL_BG_RID.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_rid.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1817;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 34;
return;
case 34:
//C
this.state = 24;
;
 //BA.debugLineNum = 1818;BA.debugLine="LOAD_RETURN_UPLOAD";
_load_return_upload();
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1820;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1821;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1822;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1823;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 35;
return;
case 35:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1824;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1825;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 1827;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1828;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1832;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1833;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploaded Successfully...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploaded Successfully..."));
 //BA.debugLineNum = 1834;BA.debugLine="ToastMessageShow(\"Uploaded Successfully\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploaded Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1835;BA.debugLine="LOG_RETURN_UPLOAD";
_log_return_upload();
 //BA.debugLineNum = 1836;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = 27;
;
 //BA.debugLineNum = 1837;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = \"-\"";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1838;BA.debugLine="LABEL_LOAD_RETURN_DATE.Text = \"-\"";
parent.mostCurrent._label_load_return_date.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1839;BA.debugLine="LABEL_LOAD_RETURN_ID.Text = \"-\"";
parent.mostCurrent._label_load_return_id.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1840;BA.debugLine="return_id = \"-\"";
parent._return_id = "-";
 //BA.debugLineNum = 1841;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 //BA.debugLineNum = 1842;BA.debugLine="BUTTON_MANUAL.Visible = False";
parent.mostCurrent._button_manual.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1843;BA.debugLine="BUTTON_UPLOAD.Visible = False";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1844;BA.debugLine="PANEL_BG_RID.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_rid.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1845;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 37;
return;
case 37:
//C
this.state = 27;
;
 //BA.debugLineNum = 1846;BA.debugLine="LOAD_RETURN_UPLOAD";
_load_return_upload();
 if (true) break;

case 27:
//C
this.state = -1;
;
 //BA.debugLineNum = 1848;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_return_ref() throws Exception{
ResumableSub_INSERT_RETURN_REF rsub = new ResumableSub_INSERT_RETURN_REF(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_RETURN_REF extends BA.ResumableSub {
public ResumableSub_INSERT_RETURN_REF(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1501;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1502;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT * FROM sal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_ref_table WHERE return_id = '"+parent._return_id+"'")));
 //BA.debugLineNum = 1503;BA.debugLine="If cursor6.RowCount > 0 Then";
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
 //BA.debugLineNum = 1504;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
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
 //BA.debugLineNum = 1505;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 1506;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 1507;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_return_ref",(Object[])(new String[]{parent._cursor6.GetString("return_route_id"),parent._cursor6.GetString("return_id"),parent._cursor6.GetString("return_date"),parent._cursor6.GetString("return_number"),parent._cursor6.GetString("customer_id"),parent._cursor6.GetString("customer_name"),parent._cursor6.GetString("date_registered"),parent._cursor6.GetString("time_registered"),parent._cursor6.GetString("user_info"),parent._cursor6.GetString("tab_id")}));
 //BA.debugLineNum = 1509;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor6.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor6.GetString("return_id")));
 //BA.debugLineNum = 1510;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1511;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1512;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1515;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1516;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1517;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1518;BA.debugLine="Log(\"INSERT_RETURN_REF ERROR: \" & js.ErrorMess";
anywheresoftware.b4a.keywords.Common.LogImpl("743188242","INSERT_RETURN_REF ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1519;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1520;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1521;BA.debugLine="Sleep(1000)";
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
 //BA.debugLineNum = 1523;BA.debugLine="js.Release";
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
 //BA.debugLineNum = 1526;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1527;BA.debugLine="If error_trigger = 0 Then";
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
 //BA.debugLineNum = 1528;BA.debugLine="DELETE_RETURN_REF_TRAIL";
_delete_return_ref_trail();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1530;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1531;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1532;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1533;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1534;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1535;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1537;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1538;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1541;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_return_ref_trail() throws Exception{
ResumableSub_INSERT_RETURN_REF_TRAIL rsub = new ResumableSub_INSERT_RETURN_REF_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_RETURN_REF_TRAIL extends BA.ResumableSub {
public ResumableSub_INSERT_RETURN_REF_TRAIL(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1568;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1569;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM sal";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_ref_table_trail")));
 //BA.debugLineNum = 1570;BA.debugLine="If cursor7.RowCount > 0 Then";
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
 //BA.debugLineNum = 1571;BA.debugLine="For i = 0 To cursor7.RowCount - 1";
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
 //BA.debugLineNum = 1572;BA.debugLine="cursor7.Position = i";
parent._cursor7.setPosition(_i);
 //BA.debugLineNum = 1573;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 1574;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_re";
_cmd = _createcommand("insert_return_ref_trail",(Object[])(new String[]{parent._cursor7.GetString("return_route_id"),parent._cursor7.GetString("return_id"),parent._cursor7.GetString("return_date"),parent._cursor7.GetString("return_number"),parent._cursor7.GetString("customer_id"),parent._cursor7.GetString("customer_name"),parent._cursor7.GetString("date_registered"),parent._cursor7.GetString("time_registered"),parent._cursor7.GetString("user_info"),parent._cursor7.GetString("tab_id"),parent._cursor7.GetString("date_modified"),parent._cursor7.GetString("time_modified"),parent._cursor7.GetString("modified_by"),parent._cursor7.GetString("modified_type")}));
 //BA.debugLineNum = 1577;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1578;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1579;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1582;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1583;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1584;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1585;BA.debugLine="Log(\"INSERT_RETURN_REF_TRAIL ERROR: \" & js.Err";
anywheresoftware.b4a.keywords.Common.LogImpl("743319314","INSERT_RETURN_REF_TRAIL ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1586;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1587;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1588;BA.debugLine="Sleep(1000)";
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
 //BA.debugLineNum = 1590;BA.debugLine="js.Release";
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
 //BA.debugLineNum = 1593;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1594;BA.debugLine="If error_trigger = 0 Then";
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
 //BA.debugLineNum = 1595;BA.debugLine="DELETE_RETURN_DISC";
_delete_return_disc();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1597;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1598;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1599;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1600;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1601;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1602;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1604;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1605;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 1608;BA.debugLine="End Sub";
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
public ResumableSub_LABEL_LOAD_EXPIRATION_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1867;BA.debugLine="Dialog.Title = \"Select Expiration Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Expiration Date");
 //BA.debugLineNum = 1868;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(255,109,81)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (109),(int) (81));
 //BA.debugLineNum = 1869;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)("NO EXP"),(Object)("CANCEL")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1870;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 1871;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = DateTime.Date(DateT";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 //BA.debugLineNum = 1875;BA.debugLine="GET_EXPIRATION_SPAN";
_get_expiration_span();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1877;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = \"-\"";
parent.mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence("-"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1879;BA.debugLine="End Sub";
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
public ResumableSub_LABEL_LOAD_MANUFACTURED_Click(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1881;BA.debugLine="Dialog.Title = \"Select Manufacturing Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Manufacturing Date");
 //BA.debugLineNum = 1882;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(91,255,81)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (91),(int) (255),(int) (81));
 //BA.debugLineNum = 1883;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate2, \"\",";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate2),(Object)(""),(Object)("NO MFG"),(Object)("CANCEL")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1884;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 1885;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(Dat";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate2._getdate /*long*/ ())));
 //BA.debugLineNum = 1889;BA.debugLine="GET_MANUFACTURING_SPAN";
_get_manufacturing_span();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1891;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = \"-\"";
parent.mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence("-"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1893;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_customer() throws Exception{
ResumableSub_LOAD_CUSTOMER rsub = new ResumableSub_LOAD_CUSTOMER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_CUSTOMER extends BA.ResumableSub {
public ResumableSub_LOAD_CUSTOMER(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
anywheresoftware.b4a.objects.collections.List _items = null;
int _iss = 0;
int step7;
int limit7;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 718;BA.debugLine="SearchTemplate2.CustomListView1.Clear";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 719;BA.debugLine="Dialog.Title = \"Please Choose Customer\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Please Choose Customer");
 //BA.debugLineNum = 720;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 721;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 722;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 723;BA.debugLine="cursor10 = connection.ExecQuery(\"SELECT * FROM cu";
parent._cursor10 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM customer_table GROUP BY customer_id")));
 //BA.debugLineNum = 724;BA.debugLine="For iss = 0 To cursor10.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step7 = 1;
limit7 = (int) (parent._cursor10.getRowCount()-1);
_iss = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step7 > 0 && _iss <= limit7) || (step7 < 0 && _iss >= limit7)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_iss = ((int)(0 + _iss + step7)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 725;BA.debugLine="cursor10.Position = iss";
parent._cursor10.setPosition(_iss);
 //BA.debugLineNum = 726;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 727;BA.debugLine="Log(cursor10.GetString(\"customer_name\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("741353226",parent._cursor10.GetString("customer_name"),0);
 //BA.debugLineNum = 728;BA.debugLine="Items.Add(cursor10.GetString(\"customer_name\"))";
_items.Add((Object)(parent._cursor10.GetString("customer_name")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 730;BA.debugLine="SearchTemplate2.SetItems(Items)";
parent.mostCurrent._searchtemplate2._setitems /*Object*/ (_items);
 //BA.debugLineNum = 731;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_expiration() throws Exception{
int _row = 0;
 //BA.debugLineNum = 2078;BA.debugLine="Sub LOAD_EXPIRATION";
 //BA.debugLineNum = 2079;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM sal";
_cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM sales_return_expiry_table WHERE return_id = '"+mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+_item_number+"'")));
 //BA.debugLineNum = 2080;BA.debugLine="If cursor2.RowCount > 0 Then";
if (_cursor2.getRowCount()>0) { 
 //BA.debugLineNum = 2081;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor2.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit3 ;_row = _row + step3 ) {
 //BA.debugLineNum = 2082;BA.debugLine="cursor2.Position = row";
_cursor2.setPosition(_row);
 //BA.debugLineNum = 2084;BA.debugLine="LABEL_LOAD_EXPIRATION.Text = cursor2.GetString(";
mostCurrent._label_load_expiration.setText(BA.ObjectToCharSequence(_cursor2.GetString("date_expired")));
 //BA.debugLineNum = 2085;BA.debugLine="LABEL_LOAD_MANUFACTURED.Text = cursor2.GetStrin";
mostCurrent._label_load_manufactured.setText(BA.ObjectToCharSequence(_cursor2.GetString("date_manufactured")));
 }
};
 //BA.debugLineNum = 2087;BA.debugLine="BUTTON_EXPIRATION.Visible = True";
mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 };
 //BA.debugLineNum = 2090;BA.debugLine="End Sub";
return "";
}
public static String  _load_reason() throws Exception{
 //BA.debugLineNum = 801;BA.debugLine="Sub LOAD_REASON";
 //BA.debugLineNum = 802;BA.debugLine="If CMB_WAREHOUSE.SelectedIndex = CMB_WAREHOUSE.cm";
if (mostCurrent._cmb_warehouse._getselectedindex /*int*/ ()==mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BO WAREHOUSE")) { 
 //BA.debugLineNum = 803;BA.debugLine="CMB_REASON.cmbBox.Clear";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 804;BA.debugLine="CMB_REASON.cmbBox.Add(\"DAMAGED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DAMAGED");
 //BA.debugLineNum = 805;BA.debugLine="CMB_REASON.cmbBox.Add(\"EXPIRED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("EXPIRED");
 //BA.debugLineNum = 806;BA.debugLine="CMB_REASON.cmbBox.Add(\"NEAR EXPIRY\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NEAR EXPIRY");
 //BA.debugLineNum = 807;BA.debugLine="CMB_REASON.cmbBox.Add(\"FACTORY DEFECT\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("FACTORY DEFECT");
 //BA.debugLineNum = 808;BA.debugLine="CMB_REASON.cmbBox.Add(\"DENTED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DENTED");
 //BA.debugLineNum = 809;BA.debugLine="CMB_REASON.cmbBox.Add(\"EMPTY\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("EMPTY");
 //BA.debugLineNum = 810;BA.debugLine="CMB_REASON.cmbBox.Add(\"INFESTED\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("INFESTED");
 //BA.debugLineNum = 811;BA.debugLine="CMB_REASON.cmbBox.Add(\"NO RETURN\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NO RETURN");
 }else {
 //BA.debugLineNum = 813;BA.debugLine="CMB_REASON.cmbBox.Clear";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 814;BA.debugLine="CMB_REASON.cmbBox.Add(\"NEAR EXPIRY\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NEAR EXPIRY");
 //BA.debugLineNum = 815;BA.debugLine="CMB_REASON.cmbBox.Add(\"SLOW MOVING\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("SLOW MOVING");
 //BA.debugLineNum = 816;BA.debugLine="CMB_REASON.cmbBox.Add(\"OVER STOCK\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("OVER STOCK");
 //BA.debugLineNum = 817;BA.debugLine="CMB_REASON.cmbBox.Add(\"NO CASH/CHEQUE\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NO CASH/CHEQUE");
 //BA.debugLineNum = 818;BA.debugLine="CMB_REASON.cmbBox.Add(\"CLOSE ACCOUNT\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CLOSE ACCOUNT");
 //BA.debugLineNum = 819;BA.debugLine="CMB_REASON.cmbBox.Add(\"NO RETURN\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NO RETURN");
 };
 //BA.debugLineNum = 821;BA.debugLine="End Sub";
return "";
}
public static void  _load_return_product() throws Exception{
ResumableSub_LOAD_RETURN_PRODUCT rsub = new ResumableSub_LOAD_RETURN_PRODUCT(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_RETURN_PRODUCT extends BA.ResumableSub {
public ResumableSub_LOAD_RETURN_PRODUCT(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _ic = 0;
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
 //BA.debugLineNum = 1034;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 1;
;
 //BA.debugLineNum = 1035;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1036;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 1;
;
 //BA.debugLineNum = 1037;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1038;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 1039;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT a.*, b.dat";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*, b.date_expired FROM sales_return_disc_table as a LEFT JOIN sales_return_expiry_table as b ON a.return_id = b.return_id AND a.transaction_number = b.transaction_number WHERE a.return_id = '"+parent._return_id+"'")));
 //BA.debugLineNum = 1040;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 16;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1041;BA.debugLine="For ic = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step8 = 1;
limit8 = (int) (parent._cursor1.getRowCount()-1);
_ic = (int) (0) ;
this.state = 23;
if (true) break;

case 23:
//C
this.state = 13;
if ((step8 > 0 && _ic <= limit8) || (step8 < 0 && _ic >= limit8)) this.state = 6;
if (true) break;

case 24:
//C
this.state = 23;
_ic = ((int)(0 + _ic + step8)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1042;BA.debugLine="cursor1.Position = ic";
parent._cursor1.setPosition(_ic);
 //BA.debugLineNum = 1043;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 25;
return;
case 25:
//C
this.state = 7;
;
 //BA.debugLineNum = 1044;BA.debugLine="Dim row(8) As Object";
_row = new Object[(int) (8)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 1045;BA.debugLine="row(0) = cursor1.GetString(\"transaction_number\"";
_row[(int) (0)] = (Object)(parent._cursor1.GetString("transaction_number"));
 //BA.debugLineNum = 1046;BA.debugLine="row(1) = cursor1.GetString(\"product_variant\")";
_row[(int) (1)] = (Object)(parent._cursor1.GetString("product_variant"));
 //BA.debugLineNum = 1047;BA.debugLine="row(2) = cursor1.GetString(\"product_description";
_row[(int) (2)] = (Object)(parent._cursor1.GetString("product_description"));
 //BA.debugLineNum = 1048;BA.debugLine="row(3) = cursor1.GetString(\"unit\")";
_row[(int) (3)] = (Object)(parent._cursor1.GetString("unit"));
 //BA.debugLineNum = 1049;BA.debugLine="row(4) = cursor1.GetString(\"quantity\")";
_row[(int) (4)] = (Object)(parent._cursor1.GetString("quantity"));
 //BA.debugLineNum = 1050;BA.debugLine="row(5) = cursor1.GetString(\"warehouse\")";
_row[(int) (5)] = (Object)(parent._cursor1.GetString("warehouse"));
 //BA.debugLineNum = 1051;BA.debugLine="row(6) = cursor1.GetString(\"return_reason\")";
_row[(int) (6)] = (Object)(parent._cursor1.GetString("return_reason"));
 //BA.debugLineNum = 1052;BA.debugLine="If cursor1.GetString(\"date_expired\") = Null Or";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor1.GetString("date_expired")== null || (parent._cursor1.GetString("date_expired")).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1053;BA.debugLine="row(7) = \"-\"";
_row[(int) (7)] = (Object)("-");
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1055;BA.debugLine="row(7) = cursor1.GetString(\"date_expired\")";
_row[(int) (7)] = (Object)(parent._cursor1.GetString("date_expired"));
 if (true) break;

case 12:
//C
this.state = 24;
;
 //BA.debugLineNum = 1057;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;
if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 1059;BA.debugLine="TABLE_SALES_RETURN.NumberOfFrozenColumns = 1";
parent.mostCurrent._table_sales_return._numberoffrozencolumns /*int*/  = (int) (1);
 //BA.debugLineNum = 1060;BA.debugLine="TABLE_SALES_RETURN.RowHeight = 50dip";
parent.mostCurrent._table_sales_return._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 1061;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 26;
return;
case 26:
//C
this.state = 16;
;
 //BA.debugLineNum = 1062;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 16;
;
 //BA.debugLineNum = 1063;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1065;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 16:
//C
this.state = 17;
;
 //BA.debugLineNum = 1067;BA.debugLine="TABLE_SALES_RETURN.SetData(Data)";
parent.mostCurrent._table_sales_return._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 1068;BA.debugLine="If XSelections.IsInitialized = False Then";
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
 //BA.debugLineNum = 1069;BA.debugLine="XSelections.Initialize(TABLE_SALES_RETURN)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_sales_return);
 //BA.debugLineNum = 1070;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 1072;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = -1;
;
 //BA.debugLineNum = 1073;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_return_product_header() throws Exception{
 //BA.debugLineNum = 1022;BA.debugLine="Sub LOAD_RETURN_PRODUCT_HEADER";
 //BA.debugLineNum = 1023;BA.debugLine="NameColumn(0)=TABLE_SALES_RETURN.AddColumn(\"#\", T";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("#",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1024;BA.debugLine="NameColumn(1)=TABLE_SALES_RETURN.AddColumn(\"Produ";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1025;BA.debugLine="NameColumn(2)=TABLE_SALES_RETURN.AddColumn(\"Produ";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1026;BA.debugLine="NameColumn(3)=TABLE_SALES_RETURN.AddColumn(\"Unit\"";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Unit",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1027;BA.debugLine="NameColumn(4)=TABLE_SALES_RETURN.AddColumn(\"Qty\",";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Qty",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1028;BA.debugLine="NameColumn(5)=TABLE_SALES_RETURN.AddColumn(\"Wareh";
mostCurrent._namecolumn[(int) (5)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Warehouse",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1029;BA.debugLine="NameColumn(6)=TABLE_SALES_RETURN.AddColumn(\"Reaso";
mostCurrent._namecolumn[(int) (6)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Reason",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1030;BA.debugLine="NameColumn(7)=TABLE_SALES_RETURN.AddColumn(\"Expir";
mostCurrent._namecolumn[(int) (7)] = mostCurrent._table_sales_return._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Expiry",mostCurrent._table_sales_return._column_type_text /*int*/ );
 //BA.debugLineNum = 1032;BA.debugLine="End Sub";
return "";
}
public static void  _load_return_upload() throws Exception{
ResumableSub_LOAD_RETURN_UPLOAD rsub = new ResumableSub_LOAD_RETURN_UPLOAD(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_RETURN_UPLOAD extends BA.ResumableSub {
public ResumableSub_LOAD_RETURN_UPLOAD(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _rows = 0;
int step22;
int limit22;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1265;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1267;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1268;BA.debugLine="LVL_RID.Background = bg";
parent.mostCurrent._lvl_rid.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1269;BA.debugLine="LVL_RID.Clear";
parent.mostCurrent._lvl_rid.Clear();
 //BA.debugLineNum = 1270;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 1271;BA.debugLine="LVL_RID.TwoLinesLayout.Label.Typeface = Typeface.";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1272;BA.debugLine="LVL_RID.TwoLinesLayout.Label.TextSize = 16";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().Label.setTextSize((float) (16));
 //BA.debugLineNum = 1273;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.Top = -1%y";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setTop((int) (-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)));
 //BA.debugLineNum = 1274;BA.debugLine="LVL_RID.TwoLinesLayout.label.Height = 6%y";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 1275;BA.debugLine="LVL_RID.TwoLinesLayout.Label.TextColor = Colors.B";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1276;BA.debugLine="LVL_RID.TwoLinesLayout.Label.Gravity = Gravity.CE";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 1277;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.Typeface = Typ";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1278;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.Top = 4.5%y";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4.5),mostCurrent.activityBA));
 //BA.debugLineNum = 1279;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.TextSize = 11";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setTextSize((float) (11));
 //BA.debugLineNum = 1280;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.Height = 3%y";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA));
 //BA.debugLineNum = 1281;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.TextColor = Co";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 1282;BA.debugLine="LVL_RID.TwoLinesLayout.SecondLabel.Gravity = Grav";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 1283;BA.debugLine="LVL_RID.TwoLinesLayout.ItemHeight = 8%y";
parent.mostCurrent._lvl_rid.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1284;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 1;
;
 //BA.debugLineNum = 1285;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT a.*,b.upl";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT a.*,b.upload_date,b.upload_time, b.user_info as 'upload_user' FROM sales_return_ref_table as a "+"LEFT JOIN return_upload_table As b "+"ON a.return_id = b.return_id WHERE a.return_route_id = '"+parent.mostCurrent._return_module._return_route_id /*String*/ +"'")));
 //BA.debugLineNum = 1288;BA.debugLine="If cursor3.RowCount > 0 Then";
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
 //BA.debugLineNum = 1289;BA.debugLine="For rows= 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step22 = 1;
limit22 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 17;
if (true) break;

case 17:
//C
this.state = 13;
if ((step22 > 0 && _rows <= limit22) || (step22 < 0 && _rows >= limit22)) this.state = 6;
if (true) break;

case 18:
//C
this.state = 17;
_rows = ((int)(0 + _rows + step22)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1290;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 1291;BA.debugLine="If cursor3.GetString(\"upload_date\") = Null Or c";
if (true) break;

case 7:
//if
this.state = 12;
if (parent._cursor3.GetString("upload_date")== null || (parent._cursor3.GetString("upload_date")).equals("")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 1292;BA.debugLine="LVL_RID.AddTwoLines2(cursor3.GetString(\"return";
parent.mostCurrent._lvl_rid.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor3.GetString("return_id")),BA.ObjectToCharSequence("Customer : "+parent._cursor3.GetString("customer_name")+" Status : Saved"),(Object)(parent._cursor3.GetString("return_id")));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1294;BA.debugLine="LVL_RID.AddTwoLines2(cursor3.GetString(\"return";
parent.mostCurrent._lvl_rid.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor3.GetString("return_id")),BA.ObjectToCharSequence("Customer : "+parent._cursor3.GetString("customer_name")+" Status : Uploaded"),(Object)(parent._cursor3.GetString("return_id")));
 if (true) break;

case 12:
//C
this.state = 18;
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
this.state = -1;
;
 //BA.debugLineNum = 1298;BA.debugLine="PANEL_BG_RID.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_rid.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1299;BA.debugLine="PANEL_BG_RID.BringToFront";
parent.mostCurrent._panel_bg_rid.BringToFront();
 //BA.debugLineNum = 1300;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_salesman() throws Exception{
int _iqq = 0;
 //BA.debugLineNum = 777;BA.debugLine="Sub LOAD_SALESMAN";
 //BA.debugLineNum = 778;BA.debugLine="CMB_SALESMAN.cmbBox.Clear";
mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 779;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT * FROM sal";
_cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM salesman_table GROUP BY employee_id")));
 //BA.debugLineNum = 780;BA.debugLine="For iqq = 0 To cursor8.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor8.getRowCount()-1);
_iqq = (int) (0) ;
for (;_iqq <= limit3 ;_iqq = _iqq + step3 ) {
 //BA.debugLineNum = 781;BA.debugLine="cursor8.Position = iqq";
_cursor8.setPosition(_iqq);
 //BA.debugLineNum = 782;BA.debugLine="CMB_SALESMAN.cmbBox.Add(cursor8.GetString(\"sales";
mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(_cursor8.GetString("sales_position_name")+"("+_cursor8.GetString("employee_name")+")");
 }
};
 //BA.debugLineNum = 784;BA.debugLine="CMB_SALESMAN.cmbBox.Add(\"NO SALESMAN\")";
mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("NO SALESMAN");
 //BA.debugLineNum = 785;BA.debugLine="End Sub";
return "";
}
public static String  _load_warehouse() throws Exception{
 //BA.debugLineNum = 786;BA.debugLine="Sub LOAD_WAREHOUSE";
 //BA.debugLineNum = 787;BA.debugLine="CMB_WAREHOUSE.cmbBox.Clear";
mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 788;BA.debugLine="CMB_WAREHOUSE.cmbBox.add(\"BO WAREHOUSE\")";
mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BO WAREHOUSE");
 //BA.debugLineNum = 789;BA.debugLine="CMB_WAREHOUSE.cmbBox.add(\"MAIN WAREHOUSE\")";
mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("MAIN WAREHOUSE");
 //BA.debugLineNum = 790;BA.debugLine="End Sub";
return "";
}
public static void  _log_return_upload() throws Exception{
ResumableSub_LOG_RETURN_UPLOAD rsub = new ResumableSub_LOG_RETURN_UPLOAD(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_RETURN_UPLOAD extends BA.ResumableSub {
public ResumableSub_LOG_RETURN_UPLOAD(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 1259;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM return_uploa";
parent._connection.ExecNonQuery("DELETE FROM return_upload_table WHERE return_id = '"+parent._return_id+"'");
 //BA.debugLineNum = 1260;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1261;BA.debugLine="Dim query As String = \"INSERT INTO return_upload_";
_query = "INSERT INTO return_upload_table VALUES (?,?,?,?,?)";
 //BA.debugLineNum = 1262;BA.debugLine="connection.ExecNonQuery2(query,Array As String(re";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._return_id,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 1263;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _lvl_rid_itemclick(int _position,Object _value) throws Exception{
ResumableSub_LVL_RID_ItemClick rsub = new ResumableSub_LVL_RID_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LVL_RID_ItemClick extends BA.ResumableSub {
public ResumableSub_LVL_RID_ItemClick(wingan.app.sales_return_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.sales_return_module parent;
int _position;
Object _value;
int _result = 0;
int _rows = 0;
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
 //BA.debugLineNum = 1307;BA.debugLine="Msgbox2Async(\"Return ID : \" & Value ,\"Option\", \"E";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Return ID : "+BA.ObjectToString(_value)),BA.ObjectToCharSequence("Option"),"ENTER","CANCEL","UPLOAD",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1308;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1309;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 26;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1310;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM sa";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_ref_table WHERE return_id = '"+BA.ObjectToString(_value)+"'")));
 //BA.debugLineNum = 1311;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 11;
if (parent._cursor3.getRowCount()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1312;BA.debugLine="For rows= 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step6 = 1;
limit6 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 10;
if ((step6 > 0 && _rows <= limit6) || (step6 < 0 && _rows >= limit6)) this.state = 9;
if (true) break;

case 29:
//C
this.state = 28;
_rows = ((int)(0 + _rows + step6)) ;
if (true) break;

case 9:
//C
this.state = 29;
 //BA.debugLineNum = 1313;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 1314;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = cursor3.GetStr";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("customer_name")));
 //BA.debugLineNum = 1315;BA.debugLine="LABEL_LOAD_RETURN_DATE.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_return_date.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("return_date")));
 //BA.debugLineNum = 1316;BA.debugLine="LABEL_LOAD_RETURN_ID.Text = cursor3.GetString(";
parent.mostCurrent._label_load_return_id.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("return_id")));
 //BA.debugLineNum = 1317;BA.debugLine="return_id = cursor3.GetString(\"return_id\")";
parent._return_id = parent._cursor3.GetString("return_id");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 1319;BA.debugLine="BUTTON_MANUAL.Visible = True";
parent.mostCurrent._button_manual.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1320;BA.debugLine="BUTTON_UPLOAD.Visible = True";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1321;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 //BA.debugLineNum = 1322;BA.debugLine="PANEL_BG_RID.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_rid.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 26;
;
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 1325;BA.debugLine="return_id = Value";
parent._return_id = BA.ObjectToString(_value);
 //BA.debugLineNum = 1326;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM sa";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"+BA.ObjectToString(_value)+"'")));
 //BA.debugLineNum = 1327;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 14:
//if
this.state = 25;
if (parent._cursor1.getRowCount()>0) { 
this.state = 16;
}else {
this.state = 24;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 1328;BA.debugLine="Msgbox2Async(\"Are you sure you want to upload t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to upload this return?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1329;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 17;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1330;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 17:
//if
this.state = 22;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 19;
}else {
this.state = 21;
}if (true) break;

case 19:
//C
this.state = 22;
 //BA.debugLineNum = 1331;BA.debugLine="DELETE_RETURN_REF";
_delete_return_ref();
 if (true) break;

case 21:
//C
this.state = 22;
 if (true) break;

case 22:
//C
this.state = 25;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1336;BA.debugLine="Msgbox2Async(\"There's nothing to upload in this";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("There's nothing to upload in this transaction."),BA.ObjectToCharSequence("Empty Upload"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
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
 //BA.debugLineNum = 1340;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _lvl_rid_itemlongclick(int _position,Object _value) throws Exception{
ResumableSub_LVL_RID_ItemLongClick rsub = new ResumableSub_LVL_RID_ItemLongClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LVL_RID_ItemLongClick extends BA.ResumableSub {
public ResumableSub_LVL_RID_ItemLongClick(wingan.app.sales_return_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.sales_return_module parent;
int _position;
Object _value;
int _result = 0;
int _rows = 0;
String _insert_query = "";
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
 //BA.debugLineNum = 1342;BA.debugLine="Msgbox2Async(\"Do you want to delete this Return I";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Do you want to delete this Return ID "+BA.ObjectToString(_value)+"?"),BA.ObjectToCharSequence("Deleting return ID"),"DELETE","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1343;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1344;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1345;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM sa";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"+BA.ObjectToString(_value)+"'")));
 //BA.debugLineNum = 1346;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 11;
if (parent._cursor3.getRowCount()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1347;BA.debugLine="For rows= 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step6 = 1;
limit6 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 14;
if (true) break;

case 14:
//C
this.state = 10;
if ((step6 > 0 && _rows <= limit6) || (step6 < 0 && _rows >= limit6)) this.state = 9;
if (true) break;

case 15:
//C
this.state = 14;
_rows = ((int)(0 + _rows + step6)) ;
if (true) break;

case 9:
//C
this.state = 15;
 //BA.debugLineNum = 1348;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 1349;BA.debugLine="Dim insert_query As String = \"INSERT INTO sale";
_insert_query = "INSERT INTO sales_return_disc_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_disc_table WHERE return_id = ? AND transaction_number = ?";
 //BA.debugLineNum = 1350;BA.debugLine="connection.ExecNonQuery2(insert_query, Array A";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,"DELETED",parent._cursor3.GetString("return_id"),parent._cursor3.GetString("transaction_number")}));
 //BA.debugLineNum = 1351;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 //BA.debugLineNum = 1352;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_ret";
parent._connection.ExecNonQuery("DELETE FROM sales_return_disc_table WHERE return_id = '"+parent._cursor3.GetString("return_id")+"' AND transaction_number = '"+parent._cursor3.GetString("transaction_number")+"'");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 if (true) break;

case 11:
//C
this.state = 12;
;
 //BA.debugLineNum = 1355;BA.debugLine="Dim insert_query As String = \"INSERT INTO sales_";
_insert_query = "INSERT INTO sales_return_ref_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_ref_table WHERE return_id = ?";
 //BA.debugLineNum = 1356;BA.debugLine="connection.ExecNonQuery2(insert_query, Array As";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,"DELETED",BA.ObjectToString(_value)}));
 //BA.debugLineNum = 1357;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 17;
return;
case 17:
//C
this.state = 12;
;
 //BA.debugLineNum = 1358;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_retur";
parent._connection.ExecNonQuery("DELETE FROM sales_return_ref_table WHERE return_id = '"+BA.ObjectToString(_value)+"'");
 //BA.debugLineNum = 1359;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1360;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 //BA.debugLineNum = 1361;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = \"-\"";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1362;BA.debugLine="LABEL_LOAD_RETURN_DATE.Text = \"-\"";
parent.mostCurrent._label_load_return_date.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1363;BA.debugLine="LABEL_LOAD_RETURN_ID.Text = \"-\"";
parent.mostCurrent._label_load_return_id.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1364;BA.debugLine="BUTTON_MANUAL.Visible = False";
parent.mostCurrent._button_manual.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1365;BA.debugLine="BUTTON_UPLOAD.Visible = False";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1366;BA.debugLine="PANEL_BG_RID.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_rid.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1368;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _new_transaction() throws Exception{
ResumableSub_NEW_TRANSACTION rsub = new ResumableSub_NEW_TRANSACTION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_NEW_TRANSACTION extends BA.ResumableSub {
public ResumableSub_NEW_TRANSACTION(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
String _cexpdate = "";
String _cxmo = "";
String _cxday = "";
String _cxtime = "";
String _cxmin = "";
String _cxsecs = "";
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
 //BA.debugLineNum = 752;BA.debugLine="GET_RETURN_NUMBER";
_get_return_number();
 //BA.debugLineNum = 753;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 754;BA.debugLine="Dim CExpDate As String = DateTime.GetYear(DateTim";
_cexpdate = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 755;BA.debugLine="Dim Cxmo As String = DateTime.GetMonth(DateTime.N";
_cxmo = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 756;BA.debugLine="Dim Cxday As String = DateTime.GetDayOfMonth(Date";
_cxday = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 757;BA.debugLine="Dim Cxtime As String = DateTime.GetHour(DateTime.";
_cxtime = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 758;BA.debugLine="Dim Cxmin As String = DateTime.GetMinute(DateTime";
_cxmin = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 759;BA.debugLine="Dim Cxsecs As String = DateTime.GetSecond(DateTim";
_cxsecs = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetSecond(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 760;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 761;BA.debugLine="return_id = CExpDate.SubString(2)&Cxmo&Cxday&Cxti";
parent._return_id = _cexpdate.substring((int) (2))+_cxmo+_cxday+_cxtime+"-"+parent._return_number+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 762;BA.debugLine="Log(return_id)";
anywheresoftware.b4a.keywords.Common.LogImpl("741549835",parent._return_id,0);
 //BA.debugLineNum = 763;BA.debugLine="LABEL_LOAD_RETURN_DATE.Text = DateTime.Date(DateT";
parent.mostCurrent._label_load_return_date.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
 //BA.debugLineNum = 764;BA.debugLine="LABEL_LOAD_RETURN_ID.Text = return_id";
parent.mostCurrent._label_load_return_id.setText(BA.ObjectToCharSequence(parent._return_id));
 //BA.debugLineNum = 765;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 766;BA.debugLine="GET_CUSTOMER_ID";
_get_customer_id();
 //BA.debugLineNum = 767;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 4;
return;
case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 768;BA.debugLine="Dim query As String = \"INSERT INTO sales_return_r";
_query = "INSERT INTO sales_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 769;BA.debugLine="connection.ExecNonQuery2(query,Array As String(RE";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._return_module._return_route_id /*String*/ ,parent._return_id,parent.mostCurrent._label_load_return_date.getText(),parent._return_number,parent._customer_id,parent.mostCurrent._label_load_customer_name.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 771;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 772;BA.debugLine="ToastMessageShow(\"Transaction Created\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Created"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 773;BA.debugLine="BUTTON_MANUAL.Visible = True";
parent.mostCurrent._button_manual.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 774;BA.debugLine="BUTTON_UPLOAD.Visible = True";
parent.mostCurrent._button_upload.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 775;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _openbutton(anywheresoftware.b4a.objects.ButtonWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 2096;BA.debugLine="Sub OpenButton(se As Button)";
 //BA.debugLineNum = 2097;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 2098;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 2099;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 2100;BA.debugLine="End Sub";
return "";
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 298;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 299;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 300;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 301;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 303;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 304;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 305;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 306;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 307;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 25;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 26;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 27;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 29;BA.debugLine="Dim cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim addBitmap As Bitmap";
_addbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim uploadBitmap As Bitmap";
_uploadbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim return_id As String";
_return_id = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim return_number As String";
_return_number = "";
 //BA.debugLineNum = 36;BA.debugLine="Dim customer_id As String";
_customer_id = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim plate_no As String";
_plate_no = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim sales_position_id As String";
_sales_position_id = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim error_trigger As String";
_error_trigger = "";
 //BA.debugLineNum = 42;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 43;BA.debugLine="Dim product_id As String";
_product_id = "";
 //BA.debugLineNum = 44;BA.debugLine="Dim reason As String";
_reason = "";
 //BA.debugLineNum = 45;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 47;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 49;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 54;BA.debugLine="Dim cancelled_trigger As String";
_cancelled_trigger = "";
 //BA.debugLineNum = 55;BA.debugLine="Dim cancelled_id As String";
_cancelled_id = "";
 //BA.debugLineNum = 56;BA.debugLine="Dim total_invoice As String";
_total_invoice = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim total_cancelled As String";
_total_cancelled = "";
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
public static void  _return_manual() throws Exception{
ResumableSub_RETURN_MANUAL rsub = new ResumableSub_RETURN_MANUAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_RETURN_MANUAL extends BA.ResumableSub {
public ResumableSub_RETURN_MANUAL(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
anywheresoftware.b4a.objects.collections.List _items = null;
int _isq = 0;
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
 //BA.debugLineNum = 1371;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 1;
;
 //BA.debugLineNum = 1372;BA.debugLine="SearchTemplate.CustomListView1.Clear";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 1373;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 1374;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1375;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 1376;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 1377;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' ORDER BY product_desc ASC")));
 //BA.debugLineNum = 1378;BA.debugLine="For Isq = 0 To cursor5.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step8 = 1;
limit8 = (int) (parent._cursor5.getRowCount()-1);
_isq = (int) (0) ;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 4;
if ((step8 > 0 && _isq <= limit8) || (step8 < 0 && _isq >= limit8)) this.state = 3;
if (true) break;

case 7:
//C
this.state = 6;
_isq = ((int)(0 + _isq + step8)) ;
if (true) break;

case 3:
//C
this.state = 7;
 //BA.debugLineNum = 1379;BA.debugLine="cursor5.Position = Isq";
parent._cursor5.setPosition(_isq);
 //BA.debugLineNum = 1380;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 7;
;
 //BA.debugLineNum = 1381;BA.debugLine="Items.Add(cursor5.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor5.GetString("product_desc")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1383;BA.debugLine="SearchTemplate.SetItems(Items)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (_items);
 //BA.debugLineNum = 1384;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 422;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 423;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 424;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("740697858","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 425;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 426;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 427;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 428;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 429;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 430;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 432;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 433;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 434;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 435;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 436;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 438;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("740697872","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 439;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 440;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 441;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 444;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 309;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 310;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 311;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 312;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 313;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 314;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 315;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 316;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 317;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 318;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 319;BA.debugLine="End Sub";
return "";
}
public static void  _show_customer() throws Exception{
ResumableSub_SHOW_CUSTOMER rsub = new ResumableSub_SHOW_CUSTOMER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_SHOW_CUSTOMER extends BA.ResumableSub {
public ResumableSub_SHOW_CUSTOMER(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 695;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 696;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 5;
return;
case 5:
//C
this.state = 1;
;
 //BA.debugLineNum = 697;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sear";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate2),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 698;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 699;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 705;BA.debugLine="SearchTemplate2.SearchField.Text = \"\"";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._settext /*String*/ ("");
 //BA.debugLineNum = 706;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = 1;
;
 //BA.debugLineNum = 707;BA.debugLine="SearchTemplate2.SearchField.RequestFocusAndShowKe";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._requestfocusandshowkeyboard /*String*/ ();
 //BA.debugLineNum = 709;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 710;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 711;BA.debugLine="LABEL_LOAD_CUSTOMER_NAME.Text = SearchTemplate2.";
parent.mostCurrent._label_load_customer_name.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate2._selecteditem /*String*/ ));
 //BA.debugLineNum = 712;BA.debugLine="NEW_TRANSACTION";
_new_transaction();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 716;BA.debugLine="End Sub";
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
public ResumableSub_ShowPairedDevices(wingan.app.sales_return_module parent) {
this.parent = parent;
}
wingan.app.sales_return_module parent;
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
 //BA.debugLineNum = 395;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 396;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 397;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 398;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 399;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 400;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
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
 //BA.debugLineNum = 401;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 402;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 404;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 405;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 407;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 408;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 409;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 410;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
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
 //BA.debugLineNum = 411;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 413;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 415;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("740632341",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 416;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 417;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 418;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
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
 //BA.debugLineNum = 421;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_sales_return_cellclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
 //BA.debugLineNum = 1126;BA.debugLine="Sub TABLE_SALES_RETURN_CellClicked (ColumnId As St";
 //BA.debugLineNum = 1127;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 1128;BA.debugLine="Dim RowData As Map = TABLE_SALES_RETURN.GetRow(Ro";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._table_sales_return._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 1129;BA.debugLine="End Sub";
return "";
}
public static void  _table_sales_return_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_SALES_RETURN_CellLongClicked rsub = new ResumableSub_TABLE_SALES_RETURN_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_SALES_RETURN_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_SALES_RETURN_CellLongClicked(wingan.app.sales_return_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.sales_return_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
int _result = 0;
int _qrow = 0;
int _row = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
String _insert_query = "";
int step9;
int limit9;
int step48;
int limit48;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1131;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 1133;BA.debugLine="Dim RowData As Map = TABLE_SALES_RETURN.GetRow(Ro";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_sales_return._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 1134;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 1136;BA.debugLine="Msgbox2Async(\"Item : #\" & RowData.Get(\"#\") & CRLF";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Item : #"+BA.ObjectToString(_rowdata.Get((Object)("#")))+anywheresoftware.b4a.keywords.Common.CRLF+"Product : "+BA.ObjectToString(_rowdata.Get((Object)("Product Description")))+anywheresoftware.b4a.keywords.Common.CRLF+"Count : "+BA.ObjectToString(_rowdata.Get((Object)("Qty")))+" "+BA.ObjectToString(_rowdata.Get((Object)("Unit")))+anywheresoftware.b4a.keywords.Common.CRLF+"Warehouse : "+BA.ObjectToString(_rowdata.Get((Object)("Warehouse")))+anywheresoftware.b4a.keywords.Common.CRLF+"Reason : "+BA.ObjectToString(_rowdata.Get((Object)("Reason")))),BA.ObjectToCharSequence("Option"),"EDIT","CANCEL","DELETE",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1139;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 60;
return;
case 60:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1140;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 59;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 54;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1142;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+BA.ObjectToString(_rowdata.Get((Object)("Product Description")))+"'")));
 //BA.debugLineNum = 1143;BA.debugLine="If cursor3.RowCount > 0 Then";
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
 //BA.debugLineNum = 1144;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 29;
step9 = 1;
limit9 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 61;
if (true) break;

case 61:
//C
this.state = 29;
if ((step9 > 0 && _qrow <= limit9) || (step9 < 0 && _qrow >= limit9)) this.state = 9;
if (true) break;

case 62:
//C
this.state = 61;
_qrow = ((int)(0 + _qrow + step9)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1145;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1146;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1147;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 1148;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 1149;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 1151;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1152;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
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
 //BA.debugLineNum = 1153;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 1155;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1156;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 1158;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1159;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 1161;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1162;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 1164;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1165;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 1167;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 25:
//if
this.state = 28;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1168;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 28:
//C
this.state = 62;
;
 //BA.debugLineNum = 1171;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1172;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1173;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1174;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1175;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1176;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 1178;BA.debugLine="default_reading = cursor3.GetString(\"default_e";
parent._default_reading = parent._cursor3.GetString("default_expiration_date_reading");
 //BA.debugLineNum = 1179;BA.debugLine="lifespan_year = cursor3.GetString(\"life_span_y";
parent._lifespan_year = parent._cursor3.GetString("life_span_year");
 //BA.debugLineNum = 1180;BA.debugLine="lifespan_month = cursor3.GetString(\"life_span_";
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
 //BA.debugLineNum = 1184;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 63;
return;
case 63:
//C
this.state = 31;
;
 //BA.debugLineNum = 1185;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM sa";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+BA.ObjectToString(_rowdata.Get((Object)("#")))+"'")));
 //BA.debugLineNum = 1186;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 31:
//if
this.state = 52;
if (parent._cursor4.getRowCount()>0) { 
this.state = 33;
}else {
this.state = 51;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 1187;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 34:
//for
this.state = 49;
step48 = 1;
limit48 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 64;
if (true) break;

case 64:
//C
this.state = 49;
if ((step48 > 0 && _row <= limit48) || (step48 < 0 && _row >= limit48)) this.state = 36;
if (true) break;

case 65:
//C
this.state = 64;
_row = ((int)(0 + _row + step48)) ;
if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 1188;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 1189;BA.debugLine="EDITTEXT_QUANTITY.Text = cursor4.GetString(\"qu";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("quantity")));
 //BA.debugLineNum = 1190;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 66;
return;
case 66:
//C
this.state = 37;
;
 //BA.debugLineNum = 1191;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor4.GetString("unit")));
 //BA.debugLineNum = 1192;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 67;
return;
case 67:
//C
this.state = 37;
;
 //BA.debugLineNum = 1193;BA.debugLine="CMB_SALESMAN.SelectedIndex = CMB_SALESMAN.cmbB";
parent.mostCurrent._cmb_salesman._setselectedindex /*int*/ (parent.mostCurrent._cmb_salesman._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor4.GetString("salesman_name")));
 //BA.debugLineNum = 1194;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 37;
;
 //BA.debugLineNum = 1195;BA.debugLine="CMB_WAREHOUSE.SelectedIndex = CMB_WAREHOUSE.cm";
parent.mostCurrent._cmb_warehouse._setselectedindex /*int*/ (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor4.GetString("warehouse")));
 //BA.debugLineNum = 1196;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 37;
;
 //BA.debugLineNum = 1197;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 1198;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 70;
return;
case 70:
//C
this.state = 37;
;
 //BA.debugLineNum = 1199;BA.debugLine="CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.I";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor4.GetString("return_reason")));
 //BA.debugLineNum = 1200;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor4.Getstring(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor4.GetString("principal_name")));
 //BA.debugLineNum = 1201;BA.debugLine="item_number = cursor4.GetString(\"transaction_n";
parent._item_number = parent._cursor4.GetString("transaction_number");
 //BA.debugLineNum = 1202;BA.debugLine="scan_code = cursor4.GetString(\"scan_code\")";
parent._scan_code = parent._cursor4.GetString("scan_code");
 //BA.debugLineNum = 1203;BA.debugLine="reason = cursor4.GetString(\"input_reason\")";
parent._reason = parent._cursor4.GetString("input_reason");
 //BA.debugLineNum = 1204;BA.debugLine="If cursor4.GetString(\"warehouse\") = \"MAIN WARE";
if (true) break;

case 37:
//if
this.state = 48;
if ((parent._cursor4.GetString("warehouse")).equals("MAIN WAREHOUSE")) { 
this.state = 39;
}else {
this.state = 41;
}if (true) break;

case 39:
//C
this.state = 48;
 //BA.debugLineNum = 1205;BA.debugLine="BUTTON_EXPIRATION.Visible = True";
parent.mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1206;BA.debugLine="LOAD_EXPIRATION";
_load_expiration();
 if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 1208;BA.debugLine="If cursor4.GetString(\"return_reason\") = \"NEAR";
if (true) break;

case 42:
//if
this.state = 47;
if ((parent._cursor4.GetString("return_reason")).equals("NEAR EXPIRY")) { 
this.state = 44;
}else {
this.state = 46;
}if (true) break;

case 44:
//C
this.state = 47;
 //BA.debugLineNum = 1209;BA.debugLine="BUTTON_EXPIRATION.Visible = True";
parent.mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1210;BA.debugLine="LOAD_EXPIRATION";
_load_expiration();
 if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 1212;BA.debugLine="BUTTON_EXPIRATION.Visible = False";
parent.mostCurrent._button_expiration.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 47:
//C
this.state = 48;
;
 if (true) break;

case 48:
//C
this.state = 65;
;
 if (true) break;
if (true) break;

case 49:
//C
this.state = 52;
;
 if (true) break;

case 51:
//C
this.state = 52;
 if (true) break;

case 52:
//C
this.state = 59;
;
 //BA.debugLineNum = 1224;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1225;BA.debugLine="PANEL_BG_INPUT.BringToFront";
parent.mostCurrent._panel_bg_input.BringToFront();
 //BA.debugLineNum = 1227;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1228;BA.debugLine="bg.Initialize2(Colors.RGB(0,167,255), 5, 0, Colo";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (167),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1229;BA.debugLine="BUTTON_SAVE.Background = bg";
parent.mostCurrent._button_save.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1230;BA.debugLine="BUTTON_SAVE.Text = \" Edit\"";
parent.mostCurrent._button_save.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 1231;BA.debugLine="BUTTON_CANCEL.Visible = True";
parent.mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1232;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 71;
return;
case 71:
//C
this.state = 59;
;
 //BA.debugLineNum = 1233;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 1234;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 72;
return;
case 72:
//C
this.state = 59;
;
 //BA.debugLineNum = 1235;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 1236;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 73;
return;
case 73:
//C
this.state = 59;
;
 //BA.debugLineNum = 1237;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 1241;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this item?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1243;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 74;
return;
case 74:
//C
this.state = 55;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1244;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 1245;BA.debugLine="Dim insert_query As String = \"INSERT INTO sales";
_insert_query = "INSERT INTO sales_return_disc_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_disc_table WHERE return_id = ? AND transaction_number = ?";
 //BA.debugLineNum = 1246;BA.debugLine="connection.ExecNonQuery2(insert_query, Array As";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,"DELETED",parent.mostCurrent._label_load_return_id.getText(),BA.ObjectToString(_rowdata.Get((Object)("#")))}));
 //BA.debugLineNum = 1247;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 75;
return;
case 75:
//C
this.state = 58;
;
 //BA.debugLineNum = 1248;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_retu";
parent._connection.ExecNonQuery("DELETE FROM sales_return_disc_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+BA.ObjectToString(_rowdata.Get((Object)("#")))+"'");
 //BA.debugLineNum = 1249;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 76;
return;
case 76:
//C
this.state = 58;
;
 //BA.debugLineNum = 1250;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM sales_retu";
parent._connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"+parent.mostCurrent._label_load_return_id.getText()+"' AND transaction_number = '"+BA.ObjectToString(_rowdata.Get((Object)("#")))+"'");
 //BA.debugLineNum = 1251;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1252;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 77;
return;
case 77:
//C
this.state = 58;
;
 //BA.debugLineNum = 1253;BA.debugLine="LOAD_RETURN_PRODUCT";
_load_return_product();
 if (true) break;

case 58:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = -1;
;
 //BA.debugLineNum = 1256;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_sales_return_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _maxheight = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 1074;BA.debugLine="Sub TABLE_SALES_RETURN_DataUpdated";
 //BA.debugLineNum = 1075;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 1076;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)]),(Object)(mostCurrent._namecolumn[(int) (5)]),(Object)(mostCurrent._namecolumn[(int) (6)]),(Object)(mostCurrent._namecolumn[(int) (7)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 1078;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 1079;BA.debugLine="Dim MaxHeight As Int";
_maxheight = 0;
 //BA.debugLineNum = 1080;BA.debugLine="For i = 0 To TABLE_SALES_RETURN.VisibleRowIds.Si";
{
final int step5 = 1;
final int limit5 = mostCurrent._table_sales_return._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 1081;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 1082;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 1083;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 1085;BA.debugLine="MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.";
_maxheight = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxheight,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 1088;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 1089;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 1090;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 1095;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group16 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen16 = group16.length
;int index16 = 0;
;
for (; index16 < groupLen16;index16++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group16[index16]);
 //BA.debugLineNum = 1097;BA.debugLine="For i = 0 To TABLE_SALES_RETURN.VisibleRowIds.Si";
{
final int step17 = 1;
final int limit17 = mostCurrent._table_sales_return._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit17 ;_i = _i + step17 ) {
 //BA.debugLineNum = 1098;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 1099;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 1100;BA.debugLine="lbl.Font = xui.CreateDefaultBoldFont(18)";
_lbl.setFont(mostCurrent._xui.CreateDefaultBoldFont((float) (18)));
 }
};
 }
};
 //BA.debugLineNum = 1118;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group23 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)])};
final int groupLen23 = group23.length
;int index23 = 0;
;
for (; index23 < groupLen23;index23++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group23[index23]);
 //BA.debugLineNum = 1119;BA.debugLine="Column.InternalSortMode= \"ASC\"";
_column.InternalSortMode /*String*/  = "ASC";
 }
};
 //BA.debugLineNum = 1121;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 1122;BA.debugLine="TABLE_SALES_RETURN.Refresh";
mostCurrent._table_sales_return._refresh /*String*/ ();
 //BA.debugLineNum = 1123;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 1125;BA.debugLine="End Sub";
return "";
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 569;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 570;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 571;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 572;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("740960003","Trying to connect...",0);
 //BA.debugLineNum = 573;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 574;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 575;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 330;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 331;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 332;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
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
