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

public class expiration_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static expiration_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.expiration_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (expiration_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.expiration_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.expiration_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (expiration_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (expiration_module) Resume **");
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
		return expiration_module.class;
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
            BA.LogInfo("** Activity (expiration_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (expiration_module) Pause event (activity is not paused). **");
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
            expiration_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (expiration_module) Resume **");
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
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _dowloadbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _uploadbitmap = null;
public static String _principal_id = "";
public static String _scan_code = "";
public static String _product_id = "";
public static String _principal_name = "";
public static String _caseper = "";
public static String _pcsper = "";
public static String _dozper = "";
public static String _boxper = "";
public static String _bagper = "";
public static String _packper = "";
public static String _total_pieces = "";
public static String _error_trigger = "";
public static long _expdate = 0L;
public static long _datenow = 0L;
public anywheresoftware.b4a.objects.IME _ctrl = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xdatetemplate _datetemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate2 = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xcombobox _cmb_principal = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_start = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_add = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_delisted = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_document = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public wingan.app.b4xtable _table_expiration_date = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_new = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_qty = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_expdate = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_delisted = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_delisted = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview_delisted = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_document = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview_document = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_date = null;
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
public ResumableSub_Activity_Create(wingan.app.expiration_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 110;BA.debugLine="Activity.LoadLayout(\"expiration\")";
parent.mostCurrent._activity.LoadLayout("expiration",mostCurrent.activityBA);
 //BA.debugLineNum = 112;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 113;BA.debugLine="dowloadBitmap = LoadBitmap(File.DirAssets, \"downl";
parent._dowloadbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"download.png");
 //BA.debugLineNum = 114;BA.debugLine="uploadBitmap = LoadBitmap(File.DirAssets, \"upload";
parent._uploadbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"upload.png");
 //BA.debugLineNum = 116;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 117;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 118;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 119;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 120;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 122;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 124;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 125;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 126;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 128;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 129;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 132;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 133;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 134;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 136;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 137;BA.debugLine="Dialog.PutAtTop = True";
parent.mostCurrent._dialog._putattop /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 138;BA.debugLine="Dialog.Initialize (Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 139;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 140;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 141;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 142;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 143;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 144;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 145;BA.debugLine="Dialog.BodyTextColor = Colors.Black";
parent.mostCurrent._dialog._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 147;BA.debugLine="DateTemplate.Initialize";
parent.mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 148;BA.debugLine="DateTemplate.MinYear = 2016";
parent.mostCurrent._datetemplate._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 149;BA.debugLine="DateTemplate.MaxYear = 2030";
parent.mostCurrent._datetemplate._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 150;BA.debugLine="DateTemplate.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 151;BA.debugLine="DateTemplate.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 152;BA.debugLine="DateTemplate.btnMonthLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 153;BA.debugLine="DateTemplate.btnMonthRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 154;BA.debugLine="DateTemplate.btnYearLeft.Color = Colors.RGB(82,16";
parent.mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 155;BA.debugLine="DateTemplate.btnYearRight.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 156;BA.debugLine="DateTemplate.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 158;BA.debugLine="SearchTemplate2.Initialize";
parent.mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 159;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 160;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 161;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 162;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 163;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 165;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 166;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 168;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 169;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255), 5, 0, Colo";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 170;BA.debugLine="EDITTEXT_QTY.Background = bg";
parent.mostCurrent._edittext_qty.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 173;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 174;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 176;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 177;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 179;BA.debugLine="POPULATE_PRINCIPAL";
_populate_principal();
 //BA.debugLineNum = 180;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 181;BA.debugLine="LOAD_EXPIRATION_HEADER";
_load_expiration_header();
 //BA.debugLineNum = 182;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 183;BA.debugLine="GET_PRINCIPAL_ID";
_get_principal_id();
 //BA.debugLineNum = 184;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 185;BA.debugLine="LOG_EXPIRATIONTBL";
_log_expirationtbl();
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
 //BA.debugLineNum = 187;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 188;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 189;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 190;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 191;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("download"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 192;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 193;BA.debugLine="UpdateIcon(\"download\", dowloadBitmap)";
_updateicon("download",_dowloadbitmap);
 //BA.debugLineNum = 194;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (2),(int) (2),BA.ObjectToCharSequence("upload"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 195;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 196;BA.debugLine="UpdateIcon(\"upload\", uploadBitmap)";
_updateicon("upload",_uploadbitmap);
 //BA.debugLineNum = 197;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 206;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 207;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("770254593","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 208;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 209;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 210;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 200;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("770189057","Resuming...",0);
 //BA.debugLineNum = 201;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 202;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 203;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.expiration_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 268;BA.debugLine="If Item.Title = \"cart\" Then";
if (true) break;

case 1:
//if
this.state = 22;
if ((_item.getTitle()).equals("cart")) { 
this.state = 3;
}else if((_item.getTitle()).equals("download")) { 
this.state = 11;
}else if((_item.getTitle()).equals("upload")) { 
this.state = 17;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 269;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("770778882","Resuming...",0);
 //BA.debugLineNum = 270;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 271;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 272;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 273;BA.debugLine="If ScannerOnceConnected=True Then";
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
 //BA.debugLineNum = 274;BA.debugLine="Ts.Enabled=True";
parent._ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 275;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 276;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 278;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 279;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 9:
//C
this.state = 22;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 282;BA.debugLine="Msgbox2Async(\"Are you sure to update your local";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure to update your local expiration data?"),BA.ObjectToCharSequence("Sync Expiration Data"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 283;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 23;
return;
case 23:
//C
this.state = 12;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 284;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 285;BA.debugLine="DOWNLOAD_RECEIVING_EXPIRATION";
_download_receiving_expiration();
 if (true) break;

case 15:
//C
this.state = 22;
;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 288;BA.debugLine="Msgbox2Async(\"Are you sure to upload and delist";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure to upload and delist this expiration?"),BA.ObjectToCharSequence("Sync Delisting Data"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 289;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 24;
return;
case 24:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 290;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 18:
//if
this.state = 21;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 291;BA.debugLine="INSERT_DELISTED";
_insert_delisted();
 if (true) break;

case 21:
//C
this.state = 22;
;
 if (true) break;

case 22:
//C
this.state = -1;
;
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 236;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 237;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 238;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _addbadgetoicon(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp,int _number1) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _mbmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _target = null;
 //BA.debugLineNum = 253;BA.debugLine="Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int)";
 //BA.debugLineNum = 254;BA.debugLine="Dim cvs1 As Canvas";
_cvs1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 255;BA.debugLine="Dim mbmp As Bitmap";
_mbmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 256;BA.debugLine="mbmp.InitializeMutable(32dip, 32dip)";
_mbmp.InitializeMutable(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 257;BA.debugLine="cvs1.Initialize2(mbmp)";
_cvs1.Initialize2((android.graphics.Bitmap)(_mbmp.getObject()));
 //BA.debugLineNum = 258;BA.debugLine="Dim target As Rect";
_target = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 259;BA.debugLine="target.Initialize(0, 0, mbmp.Width, mbmp.Height)";
_target.Initialize((int) (0),(int) (0),_mbmp.getWidth(),_mbmp.getHeight());
 //BA.debugLineNum = 260;BA.debugLine="cvs1.DrawBitmap(bmp, Null, target)";
_cvs1.DrawBitmap((android.graphics.Bitmap)(_bmp.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_target.getObject()));
 //BA.debugLineNum = 261;BA.debugLine="If Number1 > 0 Then";
if (_number1>0) { 
 //BA.debugLineNum = 262;BA.debugLine="cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, C";
_cvs1.DrawCircle((float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 263;BA.debugLine="cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8";
_cvs1.DrawText(mostCurrent.activityBA,BA.NumberToString(anywheresoftware.b4a.keywords.Common.Min(_number1,1000)),(float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (11))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (9),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 };
 //BA.debugLineNum = 265;BA.debugLine="Return mbmp";
if (true) return _mbmp;
 //BA.debugLineNum = 266;BA.debugLine="End Sub";
return null;
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 457;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 458;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("771041025","Connection broken...",0);
 //BA.debugLineNum = 459;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 460;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 461;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 462;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 463;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 464;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 465;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 466;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 468;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.expiration_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.expiration_module parent;
byte[] _buffer;
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _row1 = 0;
int _qrow = 0;
int step10;
int limit10;
int step24;
int limit24;
int step32;
int limit32;
int step36;
int limit36;
int step50;
int limit50;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 349;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("770975489","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 351;BA.debugLine="If principal_id = \"\" Then";
if (true) break;

case 1:
//if
this.state = 88;
if ((parent._principal_id).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 88;
 //BA.debugLineNum = 352;BA.debugLine="Msgbox2Async(\"Please select a principal first.\",";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please select a principal first."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 354;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 355;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' and principal_id ='"+parent._principal_id+"' ORDER BY product_id")));
 //BA.debugLineNum = 356;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 6:
//if
this.state = 45;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 8;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 20;
}else {
this.state = 26;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 357;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 358;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 359;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 9:
//for
this.state = 12;
step10 = 1;
limit10 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 89;
if (true) break;

case 89:
//C
this.state = 12;
if ((step10 > 0 && _row <= limit10) || (step10 < 0 && _row >= limit10)) this.state = 11;
if (true) break;

case 90:
//C
this.state = 89;
_row = ((int)(0 + _row + step10)) ;
if (true) break;

case 11:
//C
this.state = 90;
 //BA.debugLineNum = 360;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 361;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 362;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 364;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True)";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 365;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 91;
return;
case 91:
//C
this.state = 13;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 366;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 13:
//if
this.state = 18;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 367;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 368;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 370;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 18:
//C
this.state = 45;
;
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 375;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 21:
//for
this.state = 24;
step24 = 1;
limit24 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 92;
if (true) break;

case 92:
//C
this.state = 24;
if ((step24 > 0 && _row <= limit24) || (step24 < 0 && _row >= limit24)) this.state = 23;
if (true) break;

case 93:
//C
this.state = 92;
_row = ((int)(0 + _row + step24)) ;
if (true) break;

case 23:
//C
this.state = 93;
 //BA.debugLineNum = 376;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 377;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 378;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;
if (true) break;

case 24:
//C
this.state = 45;
;
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 381;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 382;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 27:
//if
this.state = 44;
if (parent._cursor4.getRowCount()>0) { 
this.state = 29;
}else {
this.state = 43;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 383;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 30:
//for
this.state = 41;
step32 = 1;
limit32 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 94;
if (true) break;

case 94:
//C
this.state = 41;
if ((step32 > 0 && _row <= limit32) || (step32 < 0 && _row >= limit32)) this.state = 32;
if (true) break;

case 95:
//C
this.state = 94;
_row = ((int)(0 + _row + step32)) ;
if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 384;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 385;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT princi";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._cursor4.GetString("principal_id")+"'")));
 //BA.debugLineNum = 386;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 33:
//if
this.state = 40;
if (parent._cursor7.getRowCount()>0) { 
this.state = 35;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 387;BA.debugLine="For row1 = 0 To cursor7.RowCount - 1";
if (true) break;

case 36:
//for
this.state = 39;
step36 = 1;
limit36 = (int) (parent._cursor7.getRowCount()-1);
_row1 = (int) (0) ;
this.state = 96;
if (true) break;

case 96:
//C
this.state = 39;
if ((step36 > 0 && _row1 <= limit36) || (step36 < 0 && _row1 >= limit36)) this.state = 38;
if (true) break;

case 97:
//C
this.state = 96;
_row1 = ((int)(0 + _row1 + step36)) ;
if (true) break;

case 38:
//C
this.state = 97;
 //BA.debugLineNum = 388;BA.debugLine="cursor7.Position = row1";
parent._cursor7.setPosition(_row1);
 if (true) break;
if (true) break;

case 39:
//C
this.state = 40;
;
 //BA.debugLineNum = 390;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CR";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent._cursor4.GetString("product_desc")+" "+anywheresoftware.b4a.keywords.Common.CRLF+"belongs to principal :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent._cursor7.GetString("principal_name")+""),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 40:
//C
this.state = 95;
;
 if (true) break;
if (true) break;

case 41:
//C
this.state = 44;
;
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 394;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not R";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 44:
//C
this.state = 45;
;
 //BA.debugLineNum = 396;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;
;
 //BA.debugLineNum = 398;BA.debugLine="If trigger = 0 Then";

case 45:
//if
this.state = 87;
if (_trigger==0) { 
this.state = 47;
}if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 399;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.Len";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 400;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 401;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 48:
//for
this.state = 86;
step50 = 1;
limit50 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 98;
if (true) break;

case 98:
//C
this.state = 86;
if ((step50 > 0 && _qrow <= limit50) || (step50 < 0 && _qrow >= limit50)) this.state = 50;
if (true) break;

case 99:
//C
this.state = 98;
_qrow = ((int)(0 + _qrow + step50)) ;
if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 402;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 403;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 404;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 405;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 407;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 408;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
if (true) break;

case 51:
//if
this.state = 54;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 53;
}if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 409;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 411;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 54:
//if
this.state = 57;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 56;
}if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 412;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 414;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 57:
//if
this.state = 60;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 59;
}if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 415;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 417;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 60:
//if
this.state = 63;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 62;
}if (true) break;

case 62:
//C
this.state = 63;
 //BA.debugLineNum = 418;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 420;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 63:
//if
this.state = 66;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 65;
}if (true) break;

case 65:
//C
this.state = 66;
 //BA.debugLineNum = 421;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 423;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 66:
//if
this.state = 69;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 68;
}if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 424;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 69:
//C
this.state = 70;
;
 //BA.debugLineNum = 426;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 100;
return;
case 100:
//C
this.state = 70;
;
 //BA.debugLineNum = 427;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"case_ba";
if (true) break;

case 70:
//if
this.state = 73;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("case_bar_code"))) { 
this.state = 72;
}if (true) break;

case 72:
//C
this.state = 73;
 //BA.debugLineNum = 428;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE"));
 if (true) break;
;
 //BA.debugLineNum = 430;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"bar_cod";

case 73:
//if
this.state = 76;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("bar_code"))) { 
this.state = 75;
}if (true) break;

case 75:
//C
this.state = 76;
 //BA.debugLineNum = 431;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS"));
 if (true) break;
;
 //BA.debugLineNum = 433;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"box_bar";

case 76:
//if
this.state = 79;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("box_bar_code"))) { 
this.state = 78;
}if (true) break;

case 78:
//C
this.state = 79;
 //BA.debugLineNum = 434;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX"));
 if (true) break;
;
 //BA.debugLineNum = 436;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"pack_ba";

case 79:
//if
this.state = 82;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("pack_bar_code"))) { 
this.state = 81;
}if (true) break;

case 81:
//C
this.state = 82;
 //BA.debugLineNum = 437;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK"));
 if (true) break;
;
 //BA.debugLineNum = 439;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"bag_bar";

case 82:
//if
this.state = 85;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("bag_bar_code"))) { 
this.state = 84;
}if (true) break;

case 84:
//C
this.state = 85;
 //BA.debugLineNum = 440;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG"));
 if (true) break;

case 85:
//C
this.state = 99;
;
 //BA.debugLineNum = 444;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 445;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 446;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 447;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 448;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 449;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 86:
//C
this.state = 87;
;
 //BA.debugLineNum = 452;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 if (true) break;

case 87:
//C
this.state = 88;
;
 if (true) break;

case 88:
//C
this.state = -1;
;
 //BA.debugLineNum = 456;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 471;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 472;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("771106561","Connection terminated...",0);
 //BA.debugLineNum = 473;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 474;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 230;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 231;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 232;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 233;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
return null;
}
public static void  _button_add_click() throws Exception{
ResumableSub_BUTTON_ADD_Click rsub = new ResumableSub_BUTTON_ADD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_ADD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_ADD_Click(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 819;BA.debugLine="If LABEL_LOAD_DESCRIPTION.Text = \"-\" Then";
if (true) break;

case 1:
//if
this.state = 10;
if ((parent.mostCurrent._label_load_description.getText()).equals("-")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 10;
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 822;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 823;BA.debugLine="PANEL_BG_NEW.BringToFront";
parent.mostCurrent._panel_bg_new.BringToFront();
 //BA.debugLineNum = 824;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 11;
return;
case 11:
//C
this.state = 6;
;
 //BA.debugLineNum = 825;BA.debugLine="Dialog.Title = \"Select Expiration Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Expiration Date");
 //BA.debugLineNum = 826;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\",";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)(""),(Object)("CANCEL")));
this.state = 12;
return;
case 12:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 827;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 6:
//if
this.state = 9;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 8;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 828;BA.debugLine="LABEL_LOAD_EXPDATE.Text = DateTime.Date(DateTem";
parent.mostCurrent._label_load_expdate.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 //BA.debugLineNum = 829;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
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
 //BA.debugLineNum = 832;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 843;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 844;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 845;BA.debugLine="EDITTEXT_QTY.Text = \"\"";
mostCurrent._edittext_qty.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 846;BA.debugLine="End Sub";
return "";
}
public static void  _button_create_click() throws Exception{
ResumableSub_BUTTON_CREATE_Click rsub = new ResumableSub_BUTTON_CREATE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CREATE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CREATE_Click(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
String _monthexp = "";
String _yearexp = "";
String _transaction_id = "";
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
 //BA.debugLineNum = 859;BA.debugLine="If LABEL_LOAD_DESCRIPTION.Text = \"-\" Then";
if (true) break;

case 1:
//if
this.state = 58;
if ((parent.mostCurrent._label_load_description.getText()).equals("-")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 58;
 //BA.debugLineNum = 860;BA.debugLine="ToastMessageShow(\"Please scan a product first.\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please scan a product first."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 862;BA.debugLine="Dim monthexp As String";
_monthexp = "";
 //BA.debugLineNum = 863;BA.debugLine="Dim yearexp As String";
_yearexp = "";
 //BA.debugLineNum = 864;BA.debugLine="GET_REMAINING_DAYS";
_get_remaining_days();
 //BA.debugLineNum = 865;BA.debugLine="Log(DaysBetweenDates (DATENOW,EXPDATE))";
anywheresoftware.b4a.keywords.Common.LogImpl("772548359",_daysbetweendates(parent._datenow,parent._expdate),0);
 //BA.debugLineNum = 866;BA.debugLine="If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then";
if (true) break;

case 6:
//if
this.state = 57;
if ((double)(Double.parseDouble(_daysbetweendates(parent._datenow,parent._expdate)))<=0) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 57;
 //BA.debugLineNum = 867;BA.debugLine="ToastMessageShow(\"You cannot input a expiration";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You cannot input a expiration date from to date or back date."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 869;BA.debugLine="If EDITTEXT_QTY.Text = \"\"  Or EDITTEXT_QTY.Text";
if (true) break;

case 11:
//if
this.state = 56;
if ((parent.mostCurrent._edittext_qty.getText()).equals("") || (double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText()))<=0) { 
this.state = 13;
}else {
this.state = 15;
}if (true) break;

case 13:
//C
this.state = 56;
 //BA.debugLineNum = 870;BA.debugLine="ToastMessageShow(\"Please input a quantity high";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a quantity higher than zero."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 872;BA.debugLine="If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = \"";
if (true) break;

case 16:
//if
this.state = 41;
if ((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("01")) { 
this.state = 18;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("02")) { 
this.state = 20;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("03")) { 
this.state = 22;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("04")) { 
this.state = 24;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("05")) { 
this.state = 26;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("06")) { 
this.state = 28;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("07")) { 
this.state = 30;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("08")) { 
this.state = 32;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("09")) { 
this.state = 34;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("10")) { 
this.state = 36;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("11")) { 
this.state = 38;
}else if((parent.mostCurrent._label_load_expdate.getText().substring((int) (5),(int) (7))).equals("12")) { 
this.state = 40;
}if (true) break;

case 18:
//C
this.state = 41;
 //BA.debugLineNum = 873;BA.debugLine="monthexp = \"January\"";
_monthexp = "January";
 if (true) break;

case 20:
//C
this.state = 41;
 //BA.debugLineNum = 875;BA.debugLine="monthexp = \"February\"";
_monthexp = "February";
 if (true) break;

case 22:
//C
this.state = 41;
 //BA.debugLineNum = 877;BA.debugLine="monthexp = \"March\"";
_monthexp = "March";
 if (true) break;

case 24:
//C
this.state = 41;
 //BA.debugLineNum = 879;BA.debugLine="monthexp = \"April\"";
_monthexp = "April";
 if (true) break;

case 26:
//C
this.state = 41;
 //BA.debugLineNum = 881;BA.debugLine="monthexp = \"May\"";
_monthexp = "May";
 if (true) break;

case 28:
//C
this.state = 41;
 //BA.debugLineNum = 883;BA.debugLine="monthexp = \"June\"";
_monthexp = "June";
 if (true) break;

case 30:
//C
this.state = 41;
 //BA.debugLineNum = 885;BA.debugLine="monthexp = \"July\"";
_monthexp = "July";
 if (true) break;

case 32:
//C
this.state = 41;
 //BA.debugLineNum = 887;BA.debugLine="monthexp = \"August\"";
_monthexp = "August";
 if (true) break;

case 34:
//C
this.state = 41;
 //BA.debugLineNum = 889;BA.debugLine="monthexp = \"September\"";
_monthexp = "September";
 if (true) break;

case 36:
//C
this.state = 41;
 //BA.debugLineNum = 891;BA.debugLine="monthexp = \"October\"";
_monthexp = "October";
 if (true) break;

case 38:
//C
this.state = 41;
 //BA.debugLineNum = 893;BA.debugLine="monthexp = \"November\"";
_monthexp = "November";
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 895;BA.debugLine="monthexp = \"December\"";
_monthexp = "December";
 if (true) break;

case 41:
//C
this.state = 42;
;
 //BA.debugLineNum = 897;BA.debugLine="yearexp = LABEL_LOAD_EXPDATE.Text.SubString2(0";
_yearexp = parent.mostCurrent._label_load_expdate.getText().substring((int) (0),(int) (4));
 //BA.debugLineNum = 898;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 59;
return;
case 59:
//C
this.state = 42;
;
 //BA.debugLineNum = 899;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cm";
if (true) break;

case 42:
//if
this.state = 55;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 44;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 46;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 48;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 50;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 52;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 54;
}if (true) break;

case 44:
//C
this.state = 55;
 //BA.debugLineNum = 900;BA.debugLine="total_pieces = caseper * EDITTEXT_QTY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText())));
 if (true) break;

case 46:
//C
this.state = 55;
 //BA.debugLineNum = 902;BA.debugLine="total_pieces = pcsper * EDITTEXT_QTY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText())));
 if (true) break;

case 48:
//C
this.state = 55;
 //BA.debugLineNum = 904;BA.debugLine="total_pieces = dozper * EDITTEXT_QTY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText())));
 if (true) break;

case 50:
//C
this.state = 55;
 //BA.debugLineNum = 906;BA.debugLine="total_pieces = boxper * EDITTEXT_QTY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText())));
 if (true) break;

case 52:
//C
this.state = 55;
 //BA.debugLineNum = 908;BA.debugLine="total_pieces = bagper * EDITTEXT_QTY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText())));
 if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 910;BA.debugLine="total_pieces = packper * EDITTEXT_QTY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_qty.getText())));
 if (true) break;

case 55:
//C
this.state = 56;
;
 //BA.debugLineNum = 912;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 60;
return;
case 60:
//C
this.state = 56;
;
 //BA.debugLineNum = 913;BA.debugLine="Dim transaction_id As String = DateTime.GetYea";
_transaction_id = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 914;BA.debugLine="Log(DateTime.GetYear(DateTime.Now)&DateTime.Ge";
anywheresoftware.b4a.keywords.Common.LogImpl("772548408",BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+parent.mostCurrent._login_module._tab_id /*String*/ ,0);
 //BA.debugLineNum = 915;BA.debugLine="Dim query As String = \"INSERT INTO product_exp";
_query = "INSERT INTO product_expiration_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 916;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{_transaction_id,parent._principal_id,parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_qty.getText(),parent._total_pieces,_monthexp,_yearexp,parent.mostCurrent._label_load_expdate.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ ,"PENDING"}));
 //BA.debugLineNum = 918;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 61;
return;
case 61:
//C
this.state = 56;
;
 //BA.debugLineNum = 919;BA.debugLine="ToastMessageShow(\"Product Expiration Added Suc";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Expiration Added Succesfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 920;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 921;BA.debugLine="EDITTEXT_QTY.Text = \"\"";
parent.mostCurrent._edittext_qty.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 922;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 923;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 if (true) break;

case 56:
//C
this.state = 57;
;
 if (true) break;

case 57:
//C
this.state = 58;
;
 if (true) break;

case 58:
//C
this.state = -1;
;
 //BA.debugLineNum = 927;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_delisted_click() throws Exception{
ResumableSub_BUTTON_DELISTED_Click rsub = new ResumableSub_BUTTON_DELISTED_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DELISTED_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DELISTED_Click(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 934;BA.debugLine="If LABEL_LOAD_DESCRIPTION.Text = \"-\" Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._label_load_description.getText()).equals("-")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 935;BA.debugLine="ToastMessageShow(\"Please scan a product first.\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please scan a product first."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 937;BA.debugLine="PANEL_BG_DELISTED.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_delisted.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 938;BA.debugLine="PANEL_BG_DELISTED.BringToFront";
parent.mostCurrent._panel_bg_delisted.BringToFront();
 //BA.debugLineNum = 939;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 940;BA.debugLine="LOAD_DELISTED_LIST";
_load_delisted_list();
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 942;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_delisted_close_click() throws Exception{
 //BA.debugLineNum = 929;BA.debugLine="Sub BUTTON_DELISTED_CLOSE_Click";
 //BA.debugLineNum = 930;BA.debugLine="PANEL_BG_DELISTED.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_delisted.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 931;BA.debugLine="PANEL_BG_DELISTED.BringToFront";
mostCurrent._panel_bg_delisted.BringToFront();
 //BA.debugLineNum = 932;BA.debugLine="End Sub";
return "";
}
public static void  _button_document_click() throws Exception{
ResumableSub_BUTTON_DOCUMENT_Click rsub = new ResumableSub_BUTTON_DOCUMENT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DOCUMENT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DOCUMENT_Click(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 993;BA.debugLine="If LABEL_LOAD_DESCRIPTION.Text = \"-\" Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._label_load_description.getText()).equals("-")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 994;BA.debugLine="ToastMessageShow(\"Please scan a product first.\",F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please scan a product first."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 996;BA.debugLine="PANEL_BG_DOCUMENT.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_document.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 997;BA.debugLine="PANEL_BG_DOCUMENT.BringToFront";
parent.mostCurrent._panel_bg_document.BringToFront();
 //BA.debugLineNum = 998;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 999;BA.debugLine="LOAD_DOCUMENT_LIST";
_load_document_list();
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1001;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_document_close_click() throws Exception{
 //BA.debugLineNum = 1002;BA.debugLine="Sub BUTTON_DOCUMENT_CLOSE_Click";
 //BA.debugLineNum = 1003;BA.debugLine="PANEL_BG_DOCUMENT.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_document.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1004;BA.debugLine="PANEL_BG_DOCUMENT.BringToFront";
mostCurrent._panel_bg_document.BringToFront();
 //BA.debugLineNum = 1005;BA.debugLine="End Sub";
return "";
}
public static void  _button_manual_click() throws Exception{
ResumableSub_BUTTON_MANUAL_Click rsub = new ResumableSub_BUTTON_MANUAL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_MANUAL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_MANUAL_Click(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;
int _qrow = 0;
int step16;
int limit16;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1231;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1232;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 1233;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 1234;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 1235;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 1236;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 1237;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 1238;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1239;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 32;
return;
case 32:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 1240;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 31;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1241;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sea";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate2),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 1242;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 1243;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 33;
return;
case 33:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1244;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 30;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1245;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._searchtemplate2._selecteditem /*String*/ +"'")));
 //BA.debugLineNum = 1246;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 29;
step16 = 1;
limit16 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 34;
if (true) break;

case 34:
//C
this.state = 29;
if ((step16 > 0 && _qrow <= limit16) || (step16 < 0 && _qrow >= limit16)) this.state = 9;
if (true) break;

case 35:
//C
this.state = 34;
_qrow = ((int)(0 + _qrow + step16)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1247;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 1248;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 1249;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 1250;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 1251;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 1253;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1254;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
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
 //BA.debugLineNum = 1255;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 1257;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1258;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 1260;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1261;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 1263;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 1264;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 1266;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1267;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 1269;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 25:
//if
this.state = 28;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1270;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 28:
//C
this.state = 35;
;
 //BA.debugLineNum = 1273;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 1274;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 1275;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 1276;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 1277;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 1278;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 1281;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 1282;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 if (true) break;

case 30:
//C
this.state = 31;
;
 if (true) break;

case 31:
//C
this.state = -1;
;
 //BA.debugLineNum = 1285;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_principal_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged rsub = new ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged(wingan.app.expiration_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 809;BA.debugLine="GET_PRINCIPAL_ID";
_get_principal_id();
 //BA.debugLineNum = 810;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 811;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = \"-\"";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 812;BA.debugLine="LABEL_LOAD_VARIANT.Text = \"-\"";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 814;BA.debugLine="product_id = \"\"";
parent._product_id = "";
 //BA.debugLineNum = 815;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 //BA.debugLineNum = 816;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 502;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 503;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 504;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 505;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 506;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 507;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 508;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 497;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 498;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 499;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,expiration_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 500;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 501;BA.debugLine="End Sub";
return null;
}
public static String  _daysbetweendates(long _date1,long _date2) throws Exception{
 //BA.debugLineNum = 854;BA.debugLine="Sub DaysBetweenDates(Date1 As Long, Date2 As Long)";
 //BA.debugLineNum = 855;BA.debugLine="Return Floor((Date2 - Date1) / DateTime.TicksPerD";
if (true) return BA.NumberToString(anywheresoftware.b4a.keywords.Common.Floor((_date2-_date1)/(double)anywheresoftware.b4a.keywords.Common.DateTime.TicksPerDay));
 //BA.debugLineNum = 856;BA.debugLine="End Sub";
return "";
}
public static String  _disable_transaction() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 608;BA.debugLine="Sub DISABLE_TRANSACTION";
 //BA.debugLineNum = 609;BA.debugLine="principal_id = \"\"";
_principal_id = "";
 //BA.debugLineNum = 610;BA.debugLine="CMB_PRINCIPAL.cmbBox.Enabled = True";
mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 611;BA.debugLine="BUTTON_ADD.Enabled = False";
mostCurrent._button_add.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 612;BA.debugLine="BUTTON_DELISTED.Enabled = False";
mostCurrent._button_delisted.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 613;BA.debugLine="BUTTON_DOCUMENT.Enabled = False";
mostCurrent._button_document.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 614;BA.debugLine="BUTTON_START.Text = \" Start\"";
mostCurrent._button_start.setText(BA.ObjectToCharSequence(" Start"));
 //BA.debugLineNum = 615;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 616;BA.debugLine="bg.Initialize2(Colors.RGB(0,142,255), 5, 0, Color";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (142),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (142),(int) (255)));
 //BA.debugLineNum = 617;BA.debugLine="BUTTON_START.Background = bg";
mostCurrent._button_start.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 618;BA.debugLine="TABLE_EXPIRATION_DATE.Clear";
mostCurrent._table_expiration_date._clear /*void*/ ();
 //BA.debugLineNum = 619;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = \"-\"";
mostCurrent._label_load_description.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 620;BA.debugLine="LABEL_LOAD_VARIANT.Text = \"-\"";
mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 621;BA.debugLine="End Sub";
return "";
}
public static void  _download_audit_expiration() throws Exception{
ResumableSub_DOWNLOAD_AUDIT_EXPIRATION rsub = new ResumableSub_DOWNLOAD_AUDIT_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DOWNLOAD_AUDIT_EXPIRATION extends BA.ResumableSub {
public ResumableSub_DOWNLOAD_AUDIT_EXPIRATION(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group9;
int index9;
int groupLen9;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 558;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 559;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_expi";
_cmd = _createcommand("select_expiration_audit",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 560;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 15;
return;
case 15:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 561;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 562;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 563;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 564;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("771499783",BA.NumberToString(2),0);
 //BA.debugLineNum = 565;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 11;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 566;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group9 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index9 = 0;
groupLen9 = group9.getSize();
this.state = 17;
if (true) break;

case 17:
//C
this.state = 10;
if (index9 < groupLen9) {
this.state = 9;
_row = (Object[])(group9.Get(index9));}
if (true) break;

case 18:
//C
this.state = 17;
index9++;
if (true) break;

case 9:
//C
this.state = 18;
 //BA.debugLineNum = 567;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_e";
parent._connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("document_ref_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("time_registered"))))])+"','AUDIT')");
 //BA.debugLineNum = 571;BA.debugLine="LABEL_MSGBOX2.Text = \"Downloading Expiration :";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Downloading Expiration : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 573;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 if (true) break;

case 11:
//C
this.state = 14;
;
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 576;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 577;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 19;
return;
case 19:
//C
this.state = 14;
;
 //BA.debugLineNum = 578;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 579;BA.debugLine="Msgbox2Async(\"EXPIRATION TABLE IS NOT UPDATED.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("EXPIRATION TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 580;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 581;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 583;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _jr) throws Exception{
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _download_receiving_expiration() throws Exception{
ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION rsub = new ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION extends BA.ResumableSub {
public ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
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
 //BA.debugLineNum = 510;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 511;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 512;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_expi";
_cmd = _createcommand("select_expiration",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 513;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 514;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 515;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 516;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 4;
;
 //BA.debugLineNum = 517;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 518;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 519;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 520;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM product_exp";
parent._connection.ExecNonQuery("DELETE FROM product_expiration_ref_table");
 //BA.debugLineNum = 521;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 4;
;
 //BA.debugLineNum = 522;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 523;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 20;
return;
case 20:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 524;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("771434255",BA.NumberToString(2),0);
 //BA.debugLineNum = 525;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 526;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group17 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index17 = 0;
groupLen17 = group17.getSize();
this.state = 21;
if (true) break;

case 21:
//C
this.state = 10;
if (index17 < groupLen17) {
this.state = 9;
_row = (Object[])(group17.Get(index17));}
if (true) break;

case 22:
//C
this.state = 21;
index17++;
if (true) break;

case 9:
//C
this.state = 22;
 //BA.debugLineNum = 527;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_ex";
parent._connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("document_ref_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("legend"))))])+"')");
 //BA.debugLineNum = 531;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 22;
;
 //BA.debugLineNum = 532;BA.debugLine="LABEL_MSGBOX2.Text = \"Downloading Expiration :";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Downloading Expiration : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 535;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 13;
;
 //BA.debugLineNum = 536;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_u";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Expiration Table'");
 //BA.debugLineNum = 537;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 13;
;
 //BA.debugLineNum = 538;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_u";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Expiration Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 539;BA.debugLine="LABEL_MSGBOX2.Text = \"Expiration Downloaded Succ";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Expiration Downloaded Successfully"));
 //BA.debugLineNum = 540;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 26;
return;
case 26:
//C
this.state = 13;
;
 //BA.debugLineNum = 541;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 542;BA.debugLine="LOG_EXPIRATIONTBL";
_log_expirationtbl();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 544;BA.debugLine="ToastMessageShow(\"No expiration data for this P";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No expiration data for this Principal"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 545;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 27;
return;
case 27:
//C
this.state = 13;
;
 //BA.debugLineNum = 546;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 549;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 550;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 28;
return;
case 28:
//C
this.state = 16;
;
 //BA.debugLineNum = 551;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 552;BA.debugLine="Msgbox2Async(\"EXPIRATION TABLE IS NOT UPDATED.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("EXPIRATION TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 553;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 555;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 556;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _enable_transaction() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 598;BA.debugLine="Sub ENABLE_TRANSACTION";
 //BA.debugLineNum = 599;BA.debugLine="CMB_PRINCIPAL.cmbBox.Enabled = False";
mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 600;BA.debugLine="BUTTON_ADD.Enabled = True";
mostCurrent._button_add.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 601;BA.debugLine="BUTTON_DELISTED.Enabled = True";
mostCurrent._button_delisted.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 602;BA.debugLine="BUTTON_DOCUMENT.Enabled = True";
mostCurrent._button_document.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 603;BA.debugLine="BUTTON_START.Text = \" Close\"";
mostCurrent._button_start.setText(BA.ObjectToCharSequence(" Close"));
 //BA.debugLineNum = 604;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 605;BA.debugLine="bg.Initialize2(Colors.Red, 5, 0, Colors.Red)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Red,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 606;BA.debugLine="BUTTON_START.Background = bg";
mostCurrent._button_start.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 607;BA.debugLine="End Sub";
return "";
}
public static void  _get_principal_id() throws Exception{
ResumableSub_GET_PRINCIPAL_ID rsub = new ResumableSub_GET_PRINCIPAL_ID(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_PRINCIPAL_ID extends BA.ResumableSub {
public ResumableSub_GET_PRINCIPAL_ID(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 587;BA.debugLine="If CMB_PRINCIPAL.SelectedIndex <= -1 Then CMB_PRI";
if (true) break;

case 1:
//if
this.state = 6;
if (parent.mostCurrent._cmb_principal._getselectedindex /*int*/ ()<=-1) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
parent.mostCurrent._cmb_principal._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 588;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT principal_";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_id FROM principal_table WHERE principal_name = '"+parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 589;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step3 = 1;
limit3 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 10;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 9;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 590;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 591;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 592;BA.debugLine="principal_id = cursor2.GetString(\"principal_id\")";
parent._principal_id = parent._cursor2.GetString("principal_id");
 if (true) break;
if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 594;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 595;BA.debugLine="INPUT_MANUAL";
_input_manual();
 //BA.debugLineNum = 596;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _get_remaining_days() throws Exception{
 //BA.debugLineNum = 848;BA.debugLine="Sub GET_REMAINING_DAYS";
 //BA.debugLineNum = 849;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 850;BA.debugLine="EXPDATE = DateTime.DateParse(LABEL_LOAD_EXPDATE.T";
_expdate = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(mostCurrent._label_load_expdate.getText());
 //BA.debugLineNum = 851;BA.debugLine="DATENOW = DateTime.DateParse(DateTime.Date(DateTi";
_datenow = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 852;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 244;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 245;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 246;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 247;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 248;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 251;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 53;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 55;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 56;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 60;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 62;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 63;BA.debugLine="Private NameColumn(6) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (6)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 65;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 66;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private DateTemplate As B4XDateTemplate";
mostCurrent._datetemplate = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 68;BA.debugLine="Private SearchTemplate2 As B4XSearchTemplate";
mostCurrent._searchtemplate2 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 71;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 72;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 73;BA.debugLine="Private CMB_PRINCIPAL As B4XComboBox";
mostCurrent._cmb_principal = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 74;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private BUTTON_START As Button";
mostCurrent._button_start = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private BUTTON_ADD As Button";
mostCurrent._button_add = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private BUTTON_DELISTED As Button";
mostCurrent._button_delisted = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private BUTTON_DOCUMENT As Button";
mostCurrent._button_document = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private TABLE_EXPIRATION_DATE As B4XTable";
mostCurrent._table_expiration_date = new wingan.app.b4xtable();
 //BA.debugLineNum = 85;BA.debugLine="Private PANEL_BG_NEW As Panel";
mostCurrent._panel_bg_new = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private EDITTEXT_QTY As EditText";
mostCurrent._edittext_qty = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 88;BA.debugLine="Private LABEL_LOAD_EXPDATE As Label";
mostCurrent._label_load_expdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private PANEL_BG_DELISTED As Panel";
mostCurrent._panel_bg_delisted = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private PANEL_DELISTED As Panel";
mostCurrent._panel_delisted = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Private LISTVIEW_DELISTED As ListView";
mostCurrent._listview_delisted = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private PANEL_BG_DOCUMENT As Panel";
mostCurrent._panel_bg_document = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private LISTVIEW_DOCUMENT As ListView";
mostCurrent._listview_document = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private LABEL_LOAD_DATE As Label";
mostCurrent._label_load_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="End Sub";
return "";
}
public static void  _input_manual() throws Exception{
ResumableSub_INPUT_MANUAL rsub = new ResumableSub_INPUT_MANUAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_MANUAL extends BA.ResumableSub {
public ResumableSub_INPUT_MANUAL(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
anywheresoftware.b4a.objects.collections.List _items = null;
int _i = 0;
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
 //BA.debugLineNum = 1287;BA.debugLine="SearchTemplate2.CustomListView1.Clear";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 1288;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 1289;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1290;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 1291;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 1292;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' and principal_id = '"+parent._principal_id+"' ORDER BY product_desc ASC")));
 //BA.debugLineNum = 1293;BA.debugLine="For i = 0 To cursor7.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step7 = 1;
limit7 = (int) (parent._cursor7.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step7)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 1294;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 1295;BA.debugLine="cursor7.Position = i";
parent._cursor7.setPosition(_i);
 //BA.debugLineNum = 1296;BA.debugLine="Items.Add(cursor7.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor7.GetString("product_desc")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1298;BA.debugLine="SearchTemplate2.SetItems(Items)";
parent.mostCurrent._searchtemplate2._setitems /*Object*/ (_items);
 //BA.debugLineNum = 1299;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_delisted() throws Exception{
ResumableSub_INSERT_DELISTED rsub = new ResumableSub_INSERT_DELISTED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DELISTED extends BA.ResumableSub {
public ResumableSub_INSERT_DELISTED(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;
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
 //BA.debugLineNum = 1040;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1041;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 1042;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1043;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching data..."));
 //BA.debugLineNum = 1044;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING' ORDER by product_variant, product_description")));
 //BA.debugLineNum = 1045;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1046;BA.debugLine="For i3 = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step7 = 1;
limit7 = (int) (parent._cursor1.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 13;
if ((step7 > 0 && _i3 <= limit7) || (step7 < 0 && _i3 >= limit7)) this.state = 6;
if (true) break;

case 27:
//C
this.state = 26;
_i3 = ((int)(0 + _i3 + step7)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1047;BA.debugLine="cursor1.Position = i3";
parent._cursor1.setPosition(_i3);
 //BA.debugLineNum = 1048;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_ex";
_cmd = _createcommand("insert_expiration_delisted",(Object[])(new String[]{parent._cursor1.GetString("document_ref_no"),parent._cursor1.GetString("principal_id"),parent._cursor1.GetString("principal_name"),parent._cursor1.GetString("product_id"),parent._cursor1.GetString("product_variant"),parent._cursor1.GetString("product_description"),parent._cursor1.GetString("unit"),parent._cursor1.GetString("month_expired"),parent._cursor1.GetString("year_expired"),parent._cursor1.GetString("date_expired"),parent._cursor1.GetString("date_delisted"),parent._cursor1.GetString("time_delisted"),parent._cursor1.GetString("tab_id"),parent._cursor1.GetString("user_info")}));
 //BA.debugLineNum = 1052;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1053;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1054;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1055;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading :\" & cursor1.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading :"+parent._cursor1.GetString("product_description")));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1057;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1058;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1059;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1060;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1061;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 29;
return;
case 29:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 27;
;
 //BA.debugLineNum = 1063;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;
;
 //BA.debugLineNum = 1066;BA.debugLine="If error_trigger = 0 Then";

case 14:
//if
this.state = 25;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 25;
 //BA.debugLineNum = 1067;BA.debugLine="UPDATE_RECEIVING_DELISTING";
_update_receiving_delisting();
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1069;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1070;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 19;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1071;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 1072;BA.debugLine="INSERT_DELISTED";
_insert_delisted();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1074;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1075;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 1078;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_expiration() throws Exception{
ResumableSub_INSERT_EXPIRATION rsub = new ResumableSub_INSERT_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_EXPIRATION extends BA.ResumableSub {
public ResumableSub_INSERT_EXPIRATION(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
String _query = "";
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
 //BA.debugLineNum = 1185;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_expiration_disc_table WHERE status = 'PENDING' ORDER by product_variant, product_description")));
 //BA.debugLineNum = 1186;BA.debugLine="If cursor4.RowCount > 0 Then";
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
 //BA.debugLineNum = 1187;BA.debugLine="For i3 = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step3 = 1;
limit3 = (int) (parent._cursor4.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 13;
if ((step3 > 0 && _i3 <= limit3) || (step3 < 0 && _i3 >= limit3)) this.state = 6;
if (true) break;

case 27:
//C
this.state = 26;
_i3 = ((int)(0 + _i3 + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1188;BA.debugLine="cursor4.Position = i3";
parent._cursor4.setPosition(_i3);
 //BA.debugLineNum = 1189;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_ex";
_cmd = _createcommand("insert_expiration",(Object[])(new String[]{parent._cursor4.GetString("document_ref_no"),parent._cursor4.GetString("principal_id"),parent._cursor4.GetString("principal_name"),parent._cursor4.GetString("product_id"),parent._cursor4.GetString("product_variant"),parent._cursor4.GetString("product_description"),parent._cursor4.GetString("unit"),parent._cursor4.GetString("quantity"),parent._cursor4.GetString("total_pieces"),parent._cursor4.GetString("month_expired"),parent._cursor4.GetString("year_expired"),parent._cursor4.GetString("date_expired"),parent._cursor4.GetString("date_registered"),parent._cursor4.GetString("time_registered"),parent._cursor4.GetString("tab_id"),parent._cursor4.GetString("user_info"),"EXISTING"}));
 //BA.debugLineNum = 1193;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1194;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1195;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1196;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading :\" & cursor4.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading :"+parent._cursor4.GetString("product_description")));
 //BA.debugLineNum = 1197;BA.debugLine="Dim query As String = \"UPDATE product_expirati";
_query = "UPDATE product_expiration_disc_table SET status = 'UPLOADED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?";
 //BA.debugLineNum = 1198;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._cursor4.GetString("document_ref_no"),parent._cursor4.GetString("product_id"),parent._cursor4.GetString("date_expired"),parent._cursor4.GetString("unit")}));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1200;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1201;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1202;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1203;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 29;
return;
case 29:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 27;
;
 //BA.debugLineNum = 1205;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;
;
 //BA.debugLineNum = 1209;BA.debugLine="If error_trigger = 0 Then";

case 14:
//if
this.state = 25;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 25;
 //BA.debugLineNum = 1210;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1211;BA.debugLine="ToastMessageShow(\"Uploading Sucessfull..\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Sucessfull.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1212;BA.debugLine="DOWNLOAD_RECEIVING_EXPIRATION";
_download_receiving_expiration();
 //BA.debugLineNum = 1213;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 25;
;
 //BA.debugLineNum = 1214;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = \"-\"";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1215;BA.debugLine="LABEL_LOAD_VARIANT.Text = \"-\"";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 1216;BA.debugLine="product_id = \"\"";
parent._product_id = "";
 //BA.debugLineNum = 1217;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1219;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1220;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 31;
return;
case 31:
//C
this.state = 19;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1221;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 1222;BA.debugLine="INSERT_DELISTED";
_insert_delisted();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1224;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1225;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 1228;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _label_load_expdate_click() throws Exception{
ResumableSub_LABEL_LOAD_EXPDATE_Click rsub = new ResumableSub_LABEL_LOAD_EXPDATE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_EXPDATE_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_EXPDATE_Click(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 835;BA.debugLine="Dialog.Title = \"Select Expiration Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Expiration Date");
 //BA.debugLineNum = 836;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)(""),(Object)("CANCEL")));
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 837;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 838;BA.debugLine="LABEL_LOAD_EXPDATE.Text = DateTime.Date(DateTemp";
parent.mostCurrent._label_load_expdate.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 //BA.debugLineNum = 839;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 841;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _listview_delisted_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 972;BA.debugLine="Sub LISTVIEW_DELISTED_ItemClick (Position As Int,";
 //BA.debugLineNum = 974;BA.debugLine="End Sub";
return "";
}
public static void  _listview_delisted_itemlongclick(int _position,Object _value) throws Exception{
ResumableSub_LISTVIEW_DELISTED_ItemLongClick rsub = new ResumableSub_LISTVIEW_DELISTED_ItemLongClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LISTVIEW_DELISTED_ItemLongClick extends BA.ResumableSub {
public ResumableSub_LISTVIEW_DELISTED_ItemLongClick(wingan.app.expiration_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.expiration_module parent;
int _position;
Object _value;
String _date_expired = "";
String _unit = "";
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
 //BA.debugLineNum = 976;BA.debugLine="Dim date_expired As String = Value";
_date_expired = BA.ObjectToString(_value);
 //BA.debugLineNum = 977;BA.debugLine="Dim unit As String = Value";
_unit = BA.ObjectToString(_value);
 //BA.debugLineNum = 978;BA.debugLine="Msgbox2Async(\"Are you sure you want to cancel del";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to cancel delisting this expiration date"),BA.ObjectToCharSequence("Cancel Delisted"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 979;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 980;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 981;BA.debugLine="Dim query As String = \"DELETE from product_expir";
_query = "DELETE from product_expiration_delisted_table WHERE product_id = ? AND date_expired = ? AND unit = ? AND status = 'PENDING'";
 //BA.debugLineNum = 982;BA.debugLine="connection.ExecNonQuery2(query,Array As String(p";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._product_id,_date_expired.substring((int) (0),_date_expired.indexOf("/")),_unit.substring((int) (_unit.indexOf("/")+1),_unit.length())}));
 //BA.debugLineNum = 983;BA.debugLine="Log(date_expired.SubString2(0,date_expired.Index";
anywheresoftware.b4a.keywords.Common.LogImpl("772876040",_date_expired.substring((int) (0),_date_expired.indexOf("/")),0);
 //BA.debugLineNum = 984;BA.debugLine="Log(unit.SubString2(unit.IndexOf(\"/\")+1,unit.Len";
anywheresoftware.b4a.keywords.Common.LogImpl("772876041",_unit.substring((int) (_unit.indexOf("/")+1),_unit.length()),0);
 //BA.debugLineNum = 985;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = 4;
;
 //BA.debugLineNum = 986;BA.debugLine="LOAD_DELISTED_LIST";
_load_delisted_list();
 //BA.debugLineNum = 987;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 4;
;
 //BA.debugLineNum = 988;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 990;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_delisted_list() throws Exception{
ResumableSub_LOAD_DELISTED_LIST rsub = new ResumableSub_LOAD_DELISTED_LIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_DELISTED_LIST extends BA.ResumableSub {
public ResumableSub_LOAD_DELISTED_LIST(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _row = 0;
int step20;
int limit20;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 944;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 946;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 947;BA.debugLine="LISTVIEW_DELISTED.Background = bg";
parent.mostCurrent._listview_delisted.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 948;BA.debugLine="LISTVIEW_DELISTED.Clear";
parent.mostCurrent._listview_delisted.Clear();
 //BA.debugLineNum = 949;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 1;
;
 //BA.debugLineNum = 950;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.Label.Typeface =";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 951;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.Label.TextSize =";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 952;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.label.Height = 8";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 953;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.Label.TextColor";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 954;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.Label.Gravity =";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 955;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Type";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 956;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Top";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 957;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Text";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 958;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Heig";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 959;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Text";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 960;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Grav";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 961;BA.debugLine="LISTVIEW_DELISTED.TwoLinesLayout.ItemHeight = 9%y";
parent.mostCurrent._listview_delisted.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 963;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE product_id = '"+parent._product_id+"' AND status = 'PENDING' GROUP BY date_expired, unit ORDER BY date_expired ASC")));
 //BA.debugLineNum = 964;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 8;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 965;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step20 = 1;
limit20 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 10;
if (true) break;

case 10:
//C
this.state = 7;
if ((step20 > 0 && _row <= limit20) || (step20 < 0 && _row >= limit20)) this.state = 6;
if (true) break;

case 11:
//C
this.state = 10;
_row = ((int)(0 + _row + step20)) ;
if (true) break;

case 6:
//C
this.state = 11;
 //BA.debugLineNum = 966;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 967;BA.debugLine="LISTVIEW_DELISTED.AddTwoLines2(cursor2.GetStrin";
parent.mostCurrent._listview_delisted.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor2.GetString("date_expired")),BA.ObjectToCharSequence(parent._cursor2.GetString("unit")),(Object)(parent._cursor2.GetString("date_expired")+"/"+parent._cursor2.GetString("unit")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 8;
;
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 971;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_document_list() throws Exception{
ResumableSub_LOAD_DOCUMENT_LIST rsub = new ResumableSub_LOAD_DOCUMENT_LIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_DOCUMENT_LIST extends BA.ResumableSub {
public ResumableSub_LOAD_DOCUMENT_LIST(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _row = 0;
int step20;
int limit20;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1007;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1009;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1010;BA.debugLine="LISTVIEW_DOCUMENT.Background = bg";
parent.mostCurrent._listview_document.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1011;BA.debugLine="LISTVIEW_DOCUMENT.Clear";
parent.mostCurrent._listview_document.Clear();
 //BA.debugLineNum = 1012;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 1;
;
 //BA.debugLineNum = 1013;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.Label.Typeface =";
parent.mostCurrent._listview_document.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1014;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.Label.TextSize =";
parent.mostCurrent._listview_document.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 1015;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.label.Height = 8";
parent.mostCurrent._listview_document.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1016;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.Label.TextColor";
parent.mostCurrent._listview_document.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1017;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.Label.Gravity =";
parent.mostCurrent._listview_document.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 1018;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Type";
parent.mostCurrent._listview_document.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1019;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Top";
parent.mostCurrent._listview_document.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 1020;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Text";
parent.mostCurrent._listview_document.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 1021;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Heig";
parent.mostCurrent._listview_document.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 1022;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Text";
parent.mostCurrent._listview_document.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 1023;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Grav";
parent.mostCurrent._listview_document.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 1024;BA.debugLine="LISTVIEW_DOCUMENT.TwoLinesLayout.ItemHeight = 9%y";
parent.mostCurrent._listview_document.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 1026;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM (SE";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM (SELECT document_ref_no, unit, sum(quantity) as 'quantity', date_expired, legend, date_registered FROM product_expiration_ref_table WHERE product_id = '"+parent._product_id+"' GROUP BY date_expired,document_ref_no, unit "+"UNION "+"SELECT document_ref_no, unit, sum(quantity) as 'quantity', date_expired, 'ADDED IN TAB' as 'legend', date_registered FROM product_expiration_disc_table WHERE product_id = '"+parent._product_id+"' and status = 'PENDING' GROUP BY date_expired,document_ref_no, unit) GROUP BY date_expired,document_ref_no,unit ORDER BY date_registered")));
 //BA.debugLineNum = 1029;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 8;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1030;BA.debugLine="For row = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step20 = 1;
limit20 = (int) (parent._cursor3.getRowCount()-1);
_row = (int) (0) ;
this.state = 10;
if (true) break;

case 10:
//C
this.state = 7;
if ((step20 > 0 && _row <= limit20) || (step20 < 0 && _row >= limit20)) this.state = 6;
if (true) break;

case 11:
//C
this.state = 10;
_row = ((int)(0 + _row + step20)) ;
if (true) break;

case 6:
//C
this.state = 11;
 //BA.debugLineNum = 1031;BA.debugLine="cursor3.Position = row";
parent._cursor3.setPosition(_row);
 //BA.debugLineNum = 1032;BA.debugLine="LISTVIEW_DOCUMENT.AddTwoLines2(cursor3.GetStrin";
parent.mostCurrent._listview_document.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor3.GetString("date_expired")+" | "+parent._cursor3.GetString("quantity")+" "+parent._cursor3.GetString("unit")),BA.ObjectToCharSequence(parent._cursor3.GetString("document_ref_no")+" - "+parent._cursor3.GetString("legend")+" ("+parent._cursor3.GetString("date_registered")+")"),(Object)(parent._cursor3.GetString("date_expired")+" | "+parent._cursor3.GetString("unit")+" ! "+parent._cursor3.GetString("document_ref_no")+" / "+parent._cursor3.GetString("legend")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 8;
;
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 1037;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_expiration_header() throws Exception{
 //BA.debugLineNum = 623;BA.debugLine="Sub LOAD_EXPIRATION_HEADER";
 //BA.debugLineNum = 624;BA.debugLine="NameColumn(0)=TABLE_EXPIRATION_DATE.AddColumn(\"Ye";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_expiration_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Year",mostCurrent._table_expiration_date._column_type_text /*int*/ );
 //BA.debugLineNum = 625;BA.debugLine="NameColumn(1)=TABLE_EXPIRATION_DATE.AddColumn(\"Mo";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_expiration_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Month",mostCurrent._table_expiration_date._column_type_text /*int*/ );
 //BA.debugLineNum = 626;BA.debugLine="NameColumn(2)=TABLE_EXPIRATION_DATE.AddColumn(\"Da";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_expiration_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Date",mostCurrent._table_expiration_date._column_type_text /*int*/ );
 //BA.debugLineNum = 627;BA.debugLine="NameColumn(3)=TABLE_EXPIRATION_DATE.AddColumn(\"Un";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_expiration_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Unit",mostCurrent._table_expiration_date._column_type_text /*int*/ );
 //BA.debugLineNum = 628;BA.debugLine="NameColumn(4)=TABLE_EXPIRATION_DATE.AddColumn(\"Qu";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_expiration_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Quantity",mostCurrent._table_expiration_date._column_type_text /*int*/ );
 //BA.debugLineNum = 629;BA.debugLine="NameColumn(5)=TABLE_EXPIRATION_DATE.AddColumn(\"Da";
mostCurrent._namecolumn[(int) (5)] = mostCurrent._table_expiration_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Days To Expired",mostCurrent._table_expiration_date._column_type_text /*int*/ );
 //BA.debugLineNum = 630;BA.debugLine="End Sub";
return "";
}
public static void  _load_product_expiration() throws Exception{
ResumableSub_LOAD_PRODUCT_EXPIRATION rsub = new ResumableSub_LOAD_PRODUCT_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_PRODUCT_EXPIRATION extends BA.ResumableSub {
public ResumableSub_LOAD_PRODUCT_EXPIRATION(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
String _date_expired = "";
int _ia = 0;
Object[] _row = null;
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
 //BA.debugLineNum = 632;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 1;
;
 //BA.debugLineNum = 633;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 634;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 1;
;
 //BA.debugLineNum = 635;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 636;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 637;BA.debugLine="Dim date_expired As String";
_date_expired = "";
 //BA.debugLineNum = 638;BA.debugLine="cursor10 = connection.ExecQuery(\"SELECT *, sum(qu";
parent._cursor10 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT *, sum(quantity) as 'qty', julianday(date_expired) - julianday('now') AS 'days_to_expired' FROM (SELECT a.document_ref_no,a.product_id,a.unit,sum(a.quantity) as 'quantity',a.month_expired,a.year_expired,a.date_expired,b.status FROM product_expiration_ref_table as a "+"LEFT JOIN (SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING') As b "+"ON a.product_id = b.product_id And a.date_expired = b.date_expired And a.unit = b.unit And a.document_ref_no = b.document_ref_no "+"WHERE a.product_id = '"+parent._product_id+"' GROUP BY a.date_expired,b.status,a.unit "+"UNION "+"Select a.document_ref_no,a.product_id,a.unit,sum(a.quantity) As 'quantity',a.month_expired,a.year_expired,a.date_expired,b.status FROM (SELECT *  FROM product_expiration_disc_table WHERE status = 'PENDING') as a "+"LEFT JOIN (SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING') As b "+"ON a.product_id = b.product_id And a.date_expired = b.date_expired And a.unit = b.unit And a.document_ref_no = b.document_ref_no "+"WHERE a.product_id = '"+parent._product_id+"' GROUP BY a.date_expired,b.status,a.unit) GROUP BY date_expired,status,unit")));
 //BA.debugLineNum = 647;BA.debugLine="If cursor10.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor10.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 648;BA.debugLine="For ia = 0 To cursor10.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 11;
step9 = 1;
limit9 = (int) (parent._cursor10.getRowCount()-1);
_ia = (int) (0) ;
this.state = 21;
if (true) break;

case 21:
//C
this.state = 11;
if ((step9 > 0 && _ia <= limit9) || (step9 < 0 && _ia >= limit9)) this.state = 6;
if (true) break;

case 22:
//C
this.state = 21;
_ia = ((int)(0 + _ia + step9)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 649;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 23;
return;
case 23:
//C
this.state = 7;
;
 //BA.debugLineNum = 650;BA.debugLine="cursor10.Position = ia";
parent._cursor10.setPosition(_ia);
 //BA.debugLineNum = 651;BA.debugLine="If cursor10.GetString(\"status\") = Null Or curso";
if (true) break;

case 7:
//if
this.state = 10;
if (parent._cursor10.GetString("status")== null || (parent._cursor10.GetString("status")).equals("")) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 652;BA.debugLine="date_expired = cursor10.GetString(\"days_to_exp";
_date_expired = parent._cursor10.GetString("days_to_expired");
 //BA.debugLineNum = 653;BA.debugLine="Dim row(6) As Object";
_row = new Object[(int) (6)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 654;BA.debugLine="row(0) = cursor10.GetString(\"year_expired\")";
_row[(int) (0)] = (Object)(parent._cursor10.GetString("year_expired"));
 //BA.debugLineNum = 655;BA.debugLine="row(1) = cursor10.GetString(\"month_expired\")";
_row[(int) (1)] = (Object)(parent._cursor10.GetString("month_expired"));
 //BA.debugLineNum = 656;BA.debugLine="row(2) = cursor10.GetString(\"date_expired\")";
_row[(int) (2)] = (Object)(parent._cursor10.GetString("date_expired"));
 //BA.debugLineNum = 657;BA.debugLine="row(3) = cursor10.GetString(\"unit\")";
_row[(int) (3)] = (Object)(parent._cursor10.GetString("unit"));
 //BA.debugLineNum = 658;BA.debugLine="row(4) = cursor10.GetString(\"qty\")";
_row[(int) (4)] = (Object)(parent._cursor10.GetString("qty"));
 //BA.debugLineNum = 659;BA.debugLine="row(5) = date_expired.SubString2(0,date_expire";
_row[(int) (5)] = (Object)(_date_expired.substring((int) (0),_date_expired.indexOf(".")));
 //BA.debugLineNum = 660;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 10:
//C
this.state = 22;
;
 if (true) break;
if (true) break;

case 11:
//C
this.state = 14;
;
 //BA.debugLineNum = 663;BA.debugLine="TABLE_EXPIRATION_DATE.RowHeight = 50dip";
parent.mostCurrent._table_expiration_date._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 664;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 24;
return;
case 24:
//C
this.state = 14;
;
 //BA.debugLineNum = 665;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 667;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 668;BA.debugLine="ToastMessageShow(\"Data is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Data is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 670;BA.debugLine="TABLE_EXPIRATION_DATE.SetData(Data)";
parent.mostCurrent._table_expiration_date._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 671;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 15:
//if
this.state = 18;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 672;BA.debugLine="XSelections.Initialize(TABLE_EXPIRATION_DATE)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_expiration_date);
 //BA.debugLineNum = 673;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 675;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 676;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_expirationtbl() throws Exception{
ResumableSub_LOG_EXPIRATIONTBL rsub = new ResumableSub_LOG_EXPIRATIONTBL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_EXPIRATIONTBL extends BA.ResumableSub {
public ResumableSub_LOG_EXPIRATIONTBL(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 796;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Expiration Table'")));
 //BA.debugLineNum = 797;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor5.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 798;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor5.getRowCount()-1);
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
 //BA.debugLineNum = 799;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 800;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 801;BA.debugLine="ACToolBarLight1.SubTitle = \"Last Sync: \" & curs";
parent.mostCurrent._actoolbarlight1.setSubTitle(BA.ObjectToCharSequence("Last Sync: "+parent._cursor5.GetString("date_time_updated")));
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
 //BA.debugLineNum = 806;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 213;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 214;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 215;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 216;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_delisted_click() throws Exception{
 //BA.debugLineNum = 1307;BA.debugLine="Sub PANEL_BG_DELISTED_Click";
 //BA.debugLineNum = 1308;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1309;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_document_click() throws Exception{
 //BA.debugLineNum = 1310;BA.debugLine="Sub PANEL_BG_DOCUMENT_Click";
 //BA.debugLineNum = 1311;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1312;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_msgbox_click() throws Exception{
 //BA.debugLineNum = 1301;BA.debugLine="Sub PANEL_BG_MSGBOX_Click";
 //BA.debugLineNum = 1302;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1303;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_new_click() throws Exception{
 //BA.debugLineNum = 1304;BA.debugLine="Sub PANEL_BG_NEW_Click";
 //BA.debugLineNum = 1305;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1306;BA.debugLine="End Sub";
return "";
}
public static void  _populate_principal() throws Exception{
ResumableSub_POPULATE_PRINCIPAL rsub = new ResumableSub_POPULATE_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_POPULATE_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_POPULATE_PRINCIPAL(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
int _i = 0;
int step5;
int limit5;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 485;BA.debugLine="CMB_PRINCIPAL.cmbBox.Clear";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 486;BA.debugLine="CMB_PRINCIPAL.cmbBox.DropdownTextColor = Colors.B";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 487;BA.debugLine="CMB_PRINCIPAL.cmbBox.TextColor = Colors.White";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 488;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT principal_";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table ORDER BY principal_name ASC")));
 //BA.debugLineNum = 489;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step5 = 1;
limit5 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step5)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 490;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 491;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 492;BA.debugLine="CMB_PRINCIPAL.cmbBox.Add(cursor1.GetString(\"prin";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor1.GetString("principal_name"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim connection As SQL";
_connection = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 11;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim cursor2 As Cursor";
_cursor2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim cursor3 As Cursor";
_cursor3 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim cursor4 As Cursor";
_cursor4 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim cursor5 As Cursor";
_cursor5 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim cursor6 As Cursor";
_cursor6 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim cursor7 As Cursor";
_cursor7 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim cursor8 As Cursor";
_cursor8 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim cursor9 As Cursor";
_cursor9 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim cursor10 As Cursor";
_cursor10 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 24;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 25;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 27;BA.debugLine="Private cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private dowloadBitmap As Bitmap";
_dowloadbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private uploadBitmap As Bitmap";
_uploadbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim product_id As String";
_product_id = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim principal_name As String";
_principal_name = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 40;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 41;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 42;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 43;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 44;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 45;BA.debugLine="Dim error_trigger As String = 0";
_error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 47;BA.debugLine="Dim EXPDATE, DATENOW As Long";
_expdate = 0L;
_datenow = 0L;
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 325;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 326;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 327;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("770909954","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 328;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 329;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 330;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 331;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 332;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 333;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 335;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 336;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 337;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 338;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 339;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 341;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("770909968","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 342;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 343;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 344;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 219;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 220;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 221;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 222;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 223;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 224;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 225;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 226;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 227;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 228;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
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
 //BA.debugLineNum = 298;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 299;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 300;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 301;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 302;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 303;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
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
 //BA.debugLineNum = 304;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 305;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 307;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 308;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 310;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 311;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 312;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 313;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
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
 //BA.debugLineNum = 314;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 316;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 318;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("770844437",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 319;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 320;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 321;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
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
 //BA.debugLineNum = 324;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_expiration_date_cellclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
 //BA.debugLineNum = 752;BA.debugLine="Sub TABLE_EXPIRATION_DATE_CellClicked (ColumnId As";
 //BA.debugLineNum = 753;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 754;BA.debugLine="Dim RowData As Map = TABLE_EXPIRATION_DATE.GetRow";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._table_expiration_date._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 755;BA.debugLine="End Sub";
return "";
}
public static void  _table_expiration_date_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_EXPIRATION_DATE_CellLongClicked rsub = new ResumableSub_TABLE_EXPIRATION_DATE_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_EXPIRATION_DATE_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_EXPIRATION_DATE_CellLongClicked(wingan.app.expiration_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.expiration_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
int _result = 0;
int _i = 0;
String _query = "";
String _query1 = "";
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
 //BA.debugLineNum = 757;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 759;BA.debugLine="Dim RowData As Map = TABLE_EXPIRATION_DATE.GetRow";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_expiration_date._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 760;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 762;BA.debugLine="Msgbox2Async(\"Are you sure you want to delist thi";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delist this product unit expiration"),BA.ObjectToCharSequence("Delisting Expiration"),"YES","CANCEL","DELIST THEN ADD",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 763;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 19;
return;
case 19:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 764;BA.debugLine="If Result = DialogResponse.POSITIVE Or Result = D";
if (true) break;

case 1:
//if
this.state = 18;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE || _result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 3;
}else {
this.state = 17;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 765;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT DISTINCT";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT DISTINCT document_ref_no FROM product_expiration_ref_table WHERE product_id = '"+parent._product_id+"' and date_expired = '"+BA.ObjectToString(_rowdata.Get((Object)("Date")))+"' and unit = '"+BA.ObjectToString(_rowdata.Get((Object)("Unit")))+"'")));
 //BA.debugLineNum = 766;BA.debugLine="If cursor3.RowCount > 0 Then";
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
 //BA.debugLineNum = 767;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step9 = 1;
limit9 = (int) (parent._cursor3.getRowCount()-1);
_i = (int) (0) ;
this.state = 20;
if (true) break;

case 20:
//C
this.state = 10;
if ((step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9)) this.state = 9;
if (true) break;

case 21:
//C
this.state = 20;
_i = ((int)(0 + _i + step9)) ;
if (true) break;

case 9:
//C
this.state = 21;
 //BA.debugLineNum = 768;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 21;
;
 //BA.debugLineNum = 769;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 770;BA.debugLine="Dim query As String = \"INSERT INTO product_exp";
_query = "INSERT INTO product_expiration_delisted_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 771;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._cursor3.GetString("document_ref_no"),parent._principal_id,parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),BA.ObjectToString(_rowdata.Get((Object)("Unit"))),BA.ObjectToString(_rowdata.Get((Object)("Month"))),BA.ObjectToString(_rowdata.Get((Object)("Year"))),BA.ObjectToString(_rowdata.Get((Object)("Date"))),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ ,"PENDING"}));
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
 //BA.debugLineNum = 776;BA.debugLine="Dim query1 As String = \"DELETE FROM product_expi";
_query1 = "DELETE FROM product_expiration_disc_table WHERE product_id = ? AND date_expired = ? AND unit = ? AND status = 'PENDING'";
 //BA.debugLineNum = 777;BA.debugLine="connection.ExecNonQuery2(query1,Array As String(";
parent._connection.ExecNonQuery2(_query1,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._product_id,BA.ObjectToString(_rowdata.Get((Object)("Date"))),BA.ObjectToString(_rowdata.Get((Object)("Unit")))}));
 //BA.debugLineNum = 778;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 12;
;
 //BA.debugLineNum = 779;BA.debugLine="ToastMessageShow(\"Expiration Delisted\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Expiration Delisted"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 780;BA.debugLine="EDITTEXT_QTY.Text = \"\"";
parent.mostCurrent._edittext_qty.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 781;BA.debugLine="LOAD_PRODUCT_EXPIRATION";
_load_product_expiration();
 //BA.debugLineNum = 782;BA.debugLine="If Result = DialogResponse.NEGATIVE Then";
if (true) break;

case 12:
//if
this.state = 15;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 783;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 784;BA.debugLine="PANEL_BG_NEW.BringToFront";
parent.mostCurrent._panel_bg_new.BringToFront();
 //BA.debugLineNum = 785;BA.debugLine="LABEL_LOAD_EXPDATE.Text = RowData.Get(\"Date\")";
parent.mostCurrent._label_load_expdate.setText(BA.ObjectToCharSequence(_rowdata.Get((Object)("Date"))));
 //BA.debugLineNum = 786;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 15;
;
 //BA.debugLineNum = 787;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 788;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 15;
;
 if (true) break;

case 15:
//C
this.state = 18;
;
 if (true) break;

case 17:
//C
this.state = 18;
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 793;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_expiration_date_dataupdated() throws Exception{
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
 //BA.debugLineNum = 677;BA.debugLine="Sub TABLE_EXPIRATION_DATE_DataUpdated";
 //BA.debugLineNum = 678;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 679;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 681;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 682;BA.debugLine="Dim MaxHeight As Int";
_maxheight = 0;
 //BA.debugLineNum = 683;BA.debugLine="For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds";
{
final int step5 = 1;
final int limit5 = mostCurrent._table_expiration_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 684;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 685;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 686;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 688;BA.debugLine="MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.";
_maxheight = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxheight,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 691;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 692;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 693;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 698;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group16 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (5)])};
final int groupLen16 = group16.length
;int index16 = 0;
;
for (; index16 < groupLen16;index16++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group16[index16]);
 //BA.debugLineNum = 700;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 701;BA.debugLine="Dim MaxHeight As Int";
_maxheight = 0;
 //BA.debugLineNum = 702;BA.debugLine="For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds";
{
final int step19 = 1;
final int limit19 = mostCurrent._table_expiration_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit19 ;_i = _i + step19 ) {
 //BA.debugLineNum = 703;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 704;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 705;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 707;BA.debugLine="MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.";
_maxheight = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxheight,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 710;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 711;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 712;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 726;BA.debugLine="For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds.";
{
final int step30 = 1;
final int limit30 = (int) (mostCurrent._table_expiration_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit30 ;_i = _i + step30 ) {
 //BA.debugLineNum = 727;BA.debugLine="Dim RowId As Long = TABLE_EXPIRATION_DATE.Visibl";
_rowid = BA.ObjectToLongNumber(mostCurrent._table_expiration_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 728;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 729;BA.debugLine="Dim pnl1 As B4XView = NameColumn(5).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (5)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 730;BA.debugLine="Dim row As Map = TABLE_EXPIRATION_DATE.GetRow(R";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._table_expiration_date._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 731;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 732;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (5)].Id /*String*/ )));
 //BA.debugLineNum = 733;BA.debugLine="If OtherColumnValue <= 30 Then";
if ((double)(Double.parseDouble(_othercolumnvalue))<=30) { 
 //BA.debugLineNum = 734;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 }else if((double)(Double.parseDouble(_othercolumnvalue))<=90) { 
 //BA.debugLineNum = 736;BA.debugLine="clr = xui.Color_ARGB(255,255,157,0)";
_clr = mostCurrent._xui.Color_ARGB((int) (255),(int) (255),(int) (157),(int) (0));
 }else if((double)(Double.parseDouble(_othercolumnvalue))<=150) { 
 //BA.debugLineNum = 738;BA.debugLine="clr = xui.Color_Yellow";
_clr = mostCurrent._xui.Color_Yellow;
 };
 //BA.debugLineNum = 740;BA.debugLine="pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Co";
_pnl1.GetView((int) (0)).SetColorAndBorder(_clr,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)),(int) (0));
 };
 }
};
 //BA.debugLineNum = 747;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 748;BA.debugLine="TABLE_EXPIRATION_DATE.Refresh";
mostCurrent._table_expiration_date._refresh /*String*/ ();
 //BA.debugLineNum = 749;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 751;BA.debugLine="End Sub";
return "";
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 475;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 476;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 477;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 478;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("771172099","Trying to connect...",0);
 //BA.debugLineNum = 479;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 480;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 481;BA.debugLine="End Sub";
return "";
}
public static void  _update_audit_delisting() throws Exception{
ResumableSub_UPDATE_AUDIT_DELISTING rsub = new ResumableSub_UPDATE_AUDIT_DELISTING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_AUDIT_DELISTING extends BA.ResumableSub {
public ResumableSub_UPDATE_AUDIT_DELISTING(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
String _query = "";
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
 //BA.debugLineNum = 1149;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING' ORDER by product_variant, product_description")));
 //BA.debugLineNum = 1150;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor5.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1151;BA.debugLine="For i3 = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 13;
if ((step3 > 0 && _i3 <= limit3) || (step3 < 0 && _i3 >= limit3)) this.state = 6;
if (true) break;

case 27:
//C
this.state = 26;
_i3 = ((int)(0 + _i3 + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1152;BA.debugLine="cursor5.Position = i3";
parent._cursor5.setPosition(_i3);
 //BA.debugLineNum = 1153;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_ex";
_cmd = _createcommand("update_expiration_audit_delisted",(Object[])(new String[]{"DELISTED",parent._cursor5.GetString("document_ref_no"),parent._cursor5.GetString("product_id"),parent._cursor5.GetString("date_expired"),parent._cursor5.GetString("unit")}));
 //BA.debugLineNum = 1155;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1156;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1157;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1158;BA.debugLine="LABEL_MSGBOX2.Text = \"Delisting :\" & cursor5.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Delisting :"+parent._cursor5.GetString("product_description")));
 //BA.debugLineNum = 1159;BA.debugLine="Dim query As String = \"UPDATE product_expirati";
_query = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?";
 //BA.debugLineNum = 1160;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._cursor5.GetString("document_ref_no"),parent._cursor5.GetString("product_id"),parent._cursor5.GetString("date_expired"),parent._cursor5.GetString("unit")}));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1162;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1163;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1164;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1165;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 29;
return;
case 29:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 27;
;
 //BA.debugLineNum = 1167;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;
;
 //BA.debugLineNum = 1171;BA.debugLine="If error_trigger = 0 Then";

case 14:
//if
this.state = 25;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 25;
 //BA.debugLineNum = 1172;BA.debugLine="INSERT_EXPIRATION";
_insert_expiration();
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1174;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1175;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 19;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1176;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 1177;BA.debugLine="INSERT_DELISTED";
_insert_delisted();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1179;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1180;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 1183;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _update_receiving_delisting() throws Exception{
ResumableSub_UPDATE_RECEIVING_DELISTING rsub = new ResumableSub_UPDATE_RECEIVING_DELISTING(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_RECEIVING_DELISTING extends BA.ResumableSub {
public ResumableSub_UPDATE_RECEIVING_DELISTING(wingan.app.expiration_module parent) {
this.parent = parent;
}
wingan.app.expiration_module parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
String _query = "";
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
 //BA.debugLineNum = 1080;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING' ORDER by product_variant, product_description")));
 //BA.debugLineNum = 1081;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 39;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 38;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1082;BA.debugLine="For i3 = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 25;
step3 = 1;
limit3 = (int) (parent._cursor2.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 40;
if (true) break;

case 40:
//C
this.state = 25;
if ((step3 > 0 && _i3 <= limit3) || (step3 < 0 && _i3 >= limit3)) this.state = 6;
if (true) break;

case 41:
//C
this.state = 40;
_i3 = ((int)(0 + _i3 + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1083;BA.debugLine="cursor2.Position = i3";
parent._cursor2.setPosition(_i3);
 //BA.debugLineNum = 1084;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_ex";
_cmd = _createcommand("update_expiration_receiving_delisted_old",(Object[])(new String[]{"DELISTED",parent._cursor2.GetString("document_ref_no"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("unit")}));
 //BA.debugLineNum = 1086;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1087;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 42;
return;
case 42:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1088;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1089;BA.debugLine="LABEL_MSGBOX2.Text = \"Delisting :\" & cursor2.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Delisting :"+parent._cursor2.GetString("product_description")));
 //BA.debugLineNum = 1090;BA.debugLine="Dim query As String = \"UPDATE product_expirati";
_query = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?";
 //BA.debugLineNum = 1091;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._cursor2.GetString("document_ref_no"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("unit")}));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1093;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1094;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1095;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1096;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 43;
return;
case 43:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 1098;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1099;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 44;
return;
case 44:
//C
this.state = 13;
;
 //BA.debugLineNum = 1100;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_ex";
_cmd = _createcommand("update_expiration_receiving_delisted",(Object[])(new String[]{"DELISTED",parent._cursor2.GetString("document_ref_no"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("unit")}));
 //BA.debugLineNum = 1102;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1103;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 45;
return;
case 45:
//C
this.state = 13;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1104;BA.debugLine="If js.Success Then";
if (true) break;

case 13:
//if
this.state = 18;
if (_js._success /*boolean*/ ) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 1105;BA.debugLine="LABEL_MSGBOX2.Text = \"Delisting :\" & cursor2.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Delisting :"+parent._cursor2.GetString("product_description")));
 //BA.debugLineNum = 1106;BA.debugLine="Dim query As String = \"UPDATE product_expirati";
_query = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?";
 //BA.debugLineNum = 1107;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._cursor2.GetString("document_ref_no"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("unit")}));
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 1109;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1110;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1111;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1112;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 46;
return;
case 46:
//C
this.state = 18;
;
 if (true) break;

case 18:
//C
this.state = 19;
;
 //BA.debugLineNum = 1114;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1115;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 47;
return;
case 47:
//C
this.state = 19;
;
 //BA.debugLineNum = 1116;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_ex";
_cmd = _createcommand("update_expiration_audit_delisted",(Object[])(new String[]{"DELISTED",parent._cursor2.GetString("document_ref_no"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("unit")}));
 //BA.debugLineNum = 1118;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1119;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 48;
return;
case 48:
//C
this.state = 19;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1120;BA.debugLine="If js.Success Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_js._success /*boolean*/ ) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 1121;BA.debugLine="LABEL_MSGBOX2.Text = \"Delisting :\" & cursor2.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Delisting :"+parent._cursor2.GetString("product_description")));
 //BA.debugLineNum = 1122;BA.debugLine="Dim query As String = \"UPDATE product_expirati";
_query = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?";
 //BA.debugLineNum = 1123;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._cursor2.GetString("document_ref_no"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("date_expired"),parent._cursor2.GetString("unit")}));
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1125;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1126;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1127;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1128;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 49;
return;
case 49:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = 41;
;
 //BA.debugLineNum = 1130;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1132;BA.debugLine="If error_trigger = 0 Then";

case 25:
//if
this.state = 36;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 27;
}else {
this.state = 29;
}if (true) break;

case 27:
//C
this.state = 36;
 //BA.debugLineNum = 1133;BA.debugLine="INSERT_EXPIRATION";
_insert_expiration();
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 1135;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1136;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 50;
return;
case 50:
//C
this.state = 30;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1137;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 30:
//if
this.state = 35;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 32;
}else {
this.state = 34;
}if (true) break;

case 32:
//C
this.state = 35;
 //BA.debugLineNum = 1138;BA.debugLine="INSERT_DELISTED";
_insert_delisted();
 if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 1140;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1141;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 35:
//C
this.state = 36;
;
 if (true) break;

case 36:
//C
this.state = 39;
;
 if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 1145;BA.debugLine="UPDATE_AUDIT_DELISTING";
_update_audit_delisting();
 if (true) break;

case 39:
//C
this.state = -1;
;
 //BA.debugLineNum = 1147;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 240;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 241;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 242;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
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
