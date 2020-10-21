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

public class return_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static return_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.return_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (return_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.return_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.return_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (return_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (return_module) Resume **");
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
		return return_module.class;
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
            BA.LogInfo("** Activity (return_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (return_module) Pause event (activity is not paused). **");
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
            return_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (return_module) Resume **");
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
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _addbitmap = null;
public static String _picklist_id = "";
public static String _date_dispatch = "";
public static String _date_return = "";
public static String _route_name = "";
public static String _plate_no = "";
public static String _transaction_type = "";
public static String _return_route_id = "";
public anywheresoftware.b4a.objects.IME _ctrl = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xdatetemplate _datetemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xinputtemplate _inputtemplate = null;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_type = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_type = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_exit_route = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_helper = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_driver = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_plate = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_route_name = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvl_picklist = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_route = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_route = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_helper2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_helper3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_date = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_time = null;
public wingan.app.b4xtable _table_route = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_delivered = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
public wingan.app.dashboard_module _dashboard_module = null;
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
public ResumableSub_Activity_Create(wingan.app.return_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.return_module parent;
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
 //BA.debugLineNum = 91;BA.debugLine="Activity.LoadLayout(\"return\")";
parent.mostCurrent._activity.LoadLayout("return",mostCurrent.activityBA);
 //BA.debugLineNum = 93;BA.debugLine="addBitmap = LoadBitmap(File.DirAssets, \"pencil.pn";
parent._addbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"pencil.png");
 //BA.debugLineNum = 95;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 96;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 97;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 98;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 99;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 100;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 101;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 103;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 104;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 105;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 107;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 108;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 111;BA.debugLine="ACToolBarLight1.Elevation = 1dip";
parent.mostCurrent._actoolbarlight1.setElevation((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 113;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 114;BA.debugLine="p.SetLayoutAnimated(0, 10%x, 20%y, 80%y, 40%y)";
_p.SetLayoutAnimated((int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 115;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 118;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 119;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 120;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 121;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 122;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 123;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 124;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 125;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 126;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 128;BA.debugLine="DateTemplate.Initialize";
parent.mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 129;BA.debugLine="DateTemplate.MinYear = 2016";
parent.mostCurrent._datetemplate._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 130;BA.debugLine="DateTemplate.MaxYear = 2030";
parent.mostCurrent._datetemplate._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 131;BA.debugLine="DateTemplate.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 132;BA.debugLine="DateTemplate.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 133;BA.debugLine="DateTemplate.btnMonthLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 134;BA.debugLine="DateTemplate.btnMonthRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 135;BA.debugLine="DateTemplate.btnYearLeft.Color = Colors.RGB(82,16";
parent.mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 136;BA.debugLine="DateTemplate.btnYearRight.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 137;BA.debugLine="DateTemplate.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 139;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 140;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 141;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 142;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 143;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 144;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 146;BA.debugLine="InputTemplate.Initialize";
parent.mostCurrent._inputtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 147;BA.debugLine="InputTemplate.SetBorderColor(Colors.Green,Colors.";
parent.mostCurrent._inputtemplate._setbordercolor /*String*/ (anywheresoftware.b4a.keywords.Common.Colors.Green,anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 148;BA.debugLine="InputTemplate.TextField1.TextColor = Colors.Black";
parent.mostCurrent._inputtemplate._textfield1 /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 152;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 153;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 155;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 156;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 157;BA.debugLine="EDITTEXT_TYPE.Background = bg";
parent.mostCurrent._edittext_type.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 160;BA.debugLine="LOAD_ROUTE_HEADER";
_load_route_header();
 //BA.debugLineNum = 161;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 162;BA.debugLine="LOAD_ROUTE";
_load_route();
 //BA.debugLineNum = 171;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
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
public ResumableSub_Activity_CreateMenu(wingan.app.return_module parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.return_module parent;
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
 //BA.debugLineNum = 174;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("Load Picklist"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 175;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 176;BA.debugLine="UpdateIcon(\"Load Picklist\", addBitmap)";
_updateicon("Load Picklist",parent._addbitmap);
 //BA.debugLineNum = 177;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 829;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 830;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 831;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 833;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 185;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 182;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 183;BA.debugLine="LOAD_ROUTE";
_load_route();
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 243;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 244;BA.debugLine="If Item.Title = \"Load Picklist\" Then";
if ((_item.getTitle()).equals("Load Picklist")) { 
 //BA.debugLineNum = 245;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,True)";
mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 246;BA.debugLine="PANEL_BG_TYPE.BringToFront";
mostCurrent._panel_bg_type.BringToFront();
 //BA.debugLineNum = 247;BA.debugLine="EDITTEXT_TYPE.Text = \"\"";
mostCurrent._edittext_type.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 248;BA.debugLine="EDITTEXT_TYPE.RequestFocus";
mostCurrent._edittext_type.RequestFocus();
 //BA.debugLineNum = 249;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_TYPE)";
mostCurrent._ctrl.ShowKeyboard((android.view.View)(mostCurrent._edittext_type.getObject()));
 };
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 211;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 212;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 213;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 214;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _addbadgetoicon(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp,int _number1) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _mbmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _target = null;
 //BA.debugLineNum = 229;BA.debugLine="Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int)";
 //BA.debugLineNum = 230;BA.debugLine="Dim cvs1 As Canvas";
_cvs1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 231;BA.debugLine="Dim mbmp As Bitmap";
_mbmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 232;BA.debugLine="mbmp.InitializeMutable(32dip, 32dip)";
_mbmp.InitializeMutable(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 233;BA.debugLine="cvs1.Initialize2(mbmp)";
_cvs1.Initialize2((android.graphics.Bitmap)(_mbmp.getObject()));
 //BA.debugLineNum = 234;BA.debugLine="Dim target As Rect";
_target = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 235;BA.debugLine="target.Initialize(0, 0, mbmp.Width, mbmp.Height)";
_target.Initialize((int) (0),(int) (0),_mbmp.getWidth(),_mbmp.getHeight());
 //BA.debugLineNum = 236;BA.debugLine="cvs1.DrawBitmap(bmp, Null, target)";
_cvs1.DrawBitmap((android.graphics.Bitmap)(_bmp.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_target.getObject()));
 //BA.debugLineNum = 237;BA.debugLine="If Number1 > 0 Then";
if (_number1>0) { 
 //BA.debugLineNum = 238;BA.debugLine="cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, C";
_cvs1.DrawCircle((float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 239;BA.debugLine="cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8";
_cvs1.DrawText(mostCurrent.activityBA,BA.NumberToString(anywheresoftware.b4a.keywords.Common.Min(_number1,1000)),(float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (11))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (9),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 };
 //BA.debugLineNum = 241;BA.debugLine="Return mbmp";
if (true) return _mbmp;
 //BA.debugLineNum = 242;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 206;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 207;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 208;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 209;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return null;
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 253;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 254;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static void  _button_cancelled_click() throws Exception{
ResumableSub_BUTTON_CANCELLED_Click rsub = new ResumableSub_BUTTON_CANCELLED_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CANCELLED_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CANCELLED_Click(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
String _picklist_details = "";
int _result = 0;
int _i = 0;
String _query = "";
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int step27;
int limit27;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 695;BA.debugLine="If transaction_type = \"SAVED\" Then";
if (true) break;

case 1:
//if
this.state = 28;
if ((parent._transaction_type).equals("SAVED")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 28;
 //BA.debugLineNum = 696;BA.debugLine="date_return = LABEL_LOAD_DATE.Text";
parent._date_return = parent.mostCurrent._label_load_date.getText();
 //BA.debugLineNum = 697;BA.debugLine="route_name = LABEL_LOAD_ROUTE_NAME.Text";
parent._route_name = parent.mostCurrent._label_load_route_name.getText();
 //BA.debugLineNum = 698;BA.debugLine="plate_no = LABEL_LOAD_PLATE.Text";
parent._plate_no = parent.mostCurrent._label_load_plate.getText();
 //BA.debugLineNum = 699;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 700;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 28;
;
 //BA.debugLineNum = 701;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 702;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 30;
return;
case 30:
//C
this.state = 28;
;
 //BA.debugLineNum = 703;BA.debugLine="StartActivity(CANCELLED_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._cancelled_module.getObject()));
 //BA.debugLineNum = 704;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\"";
_setanimation("right_to_center","center_to_left");
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 706;BA.debugLine="If LABEL_LOAD_DATE.Text = \"\" Then";
if (true) break;

case 6:
//if
this.state = 27;
if ((parent.mostCurrent._label_load_date.getText()).equals("")) { 
this.state = 8;
}else if((parent.mostCurrent._label_load_time.getText()).equals("")) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 27;
 //BA.debugLineNum = 707;BA.debugLine="ToastMessageShow(\"Please input a date returned.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a date returned."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = 27;
 //BA.debugLineNum = 709;BA.debugLine="ToastMessageShow(\"Please input a time returned\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a time returned"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 711;BA.debugLine="Dim picklist_details As String";
_picklist_details = "";
 //BA.debugLineNum = 712;BA.debugLine="Msgbox2Async(\"After you mark this as delivered,";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("After you mark this as delivered, we will proceed you to cancelled picklst. Are you sure you mark as delivered this route?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 713;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 31;
return;
case 31:
//C
this.state = 13;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 714;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 13:
//if
this.state = 26;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 715;BA.debugLine="NEW_TRANSACTION";
_new_transaction();
 //BA.debugLineNum = 716;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 32;
return;
case 32:
//C
this.state = 16;
;
 //BA.debugLineNum = 717;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 718;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 719;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Picklist\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Picklist"));
 //BA.debugLineNum = 720;BA.debugLine="LABEL_MSGBOX2.Text = \"Finalizing data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Finalizing data..."));
 //BA.debugLineNum = 721;BA.debugLine="For i = 0 To LVL_PICKLIST.Size - 1";
if (true) break;

case 16:
//for
this.state = 25;
step27 = 1;
limit27 = (int) (parent.mostCurrent._lvl_picklist.getSize()-1);
_i = (int) (0) ;
this.state = 33;
if (true) break;

case 33:
//C
this.state = 25;
if ((step27 > 0 && _i <= limit27) || (step27 < 0 && _i >= limit27)) this.state = 18;
if (true) break;

case 34:
//C
this.state = 33;
_i = ((int)(0 + _i + step27)) ;
if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 722;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 35;
return;
case 35:
//C
this.state = 19;
;
 //BA.debugLineNum = 723;BA.debugLine="picklist_details = LVL_PICKLIST.GetItem(i)";
_picklist_details = BA.ObjectToString(parent.mostCurrent._lvl_picklist.GetItem(_i));
 //BA.debugLineNum = 724;BA.debugLine="picklist_id = picklist_details.SubString2(0,p";
parent._picklist_id = _picklist_details.substring((int) (0),_picklist_details.indexOf("|"));
 //BA.debugLineNum = 725;BA.debugLine="Log(picklist_id)";
anywheresoftware.b4a.keywords.Common.LogImpl("76619167",parent._picklist_id,0);
 //BA.debugLineNum = 726;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist";
parent._connection.ExecNonQuery("DELETE FROM picklist_return_ref_table WHERE picklist_id = '"+parent._picklist_id+"'");
 //BA.debugLineNum = 727;BA.debugLine="Dim QUERY As String = \"insert into picklist_r";
_query = "insert into picklist_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 728;BA.debugLine="connection.ExecNonQuery2(QUERY,Array As Strin";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._return_route_id,_picklist_details.substring((int) (0),_picklist_details.indexOf("|")),_picklist_details.substring((int) (_picklist_details.indexOf("|")+1)),parent.mostCurrent._label_load_route_name.getText(),parent.mostCurrent._label_load_plate.getText(),parent.mostCurrent._label_load_driver.getText(),parent.mostCurrent._label_load_helper.getText(),parent.mostCurrent._label_load_helper2.getText(),parent.mostCurrent._label_load_helper3.getText(),parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._label_load_time.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 730;BA.debugLine="ToastMessageShow(\"Added Successfully\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Added Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 731;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = 19;
;
 //BA.debugLineNum = 732;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_";
_cmd = _createcommand("update_return",(Object[])(new String[]{parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._label_load_time.getText(),parent._return_route_id,parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ ,parent._picklist_id}));
 //BA.debugLineNum = 733;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatc";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 734;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 37;
return;
case 37:
//C
this.state = 19;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 735;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 736;BA.debugLine="UPDATE_DELIVERED";
_update_delivered();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 738;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 739;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("76619181","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 740;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT T";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 741;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 34;
;
 //BA.debugLineNum = 743;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 745;BA.debugLine="LOAD_ROUTE";
_load_route();
 //BA.debugLineNum = 746;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 38;
return;
case 38:
//C
this.state = 26;
;
 //BA.debugLineNum = 747;BA.debugLine="route_name = LABEL_LOAD_ROUTE_NAME.Text";
parent._route_name = parent.mostCurrent._label_load_route_name.getText();
 //BA.debugLineNum = 748;BA.debugLine="plate_no = LABEL_LOAD_PLATE.Text";
parent._plate_no = parent.mostCurrent._label_load_plate.getText();
 //BA.debugLineNum = 749;BA.debugLine="date_return = LABEL_LOAD_DATE.Text";
parent._date_return = parent.mostCurrent._label_load_date.getText();
 //BA.debugLineNum = 750;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 751;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 39;
return;
case 39:
//C
this.state = 26;
;
 //BA.debugLineNum = 752;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 753;BA.debugLine="ToastMessageShow(\"Route mark as delivered.\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Route mark as delivered."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 754;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 40;
return;
case 40:
//C
this.state = 26;
;
 //BA.debugLineNum = 755;BA.debugLine="StartActivity(CANCELLED_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._cancelled_module.getObject()));
 //BA.debugLineNum = 756;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_lef";
_setanimation("right_to_center","center_to_left");
 if (true) break;

case 26:
//C
this.state = 27;
;
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;

case 28:
//C
this.state = -1;
;
 //BA.debugLineNum = 760;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static void  _button_delivered_click() throws Exception{
ResumableSub_BUTTON_DELIVERED_Click rsub = new ResumableSub_BUTTON_DELIVERED_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DELIVERED_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DELIVERED_Click(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg2 = null;
String _picklist_details = "";
int _result = 0;
int _i = 0;
String _query = "";
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int step26;
int limit26;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 407;BA.debugLine="If LABEL_LOAD_DATE.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 28;
if ((parent.mostCurrent._label_load_date.getText()).equals("")) { 
this.state = 3;
}else if((parent.mostCurrent._label_load_time.getText()).equals("")) { 
this.state = 5;
}else {
this.state = 7;
}if (true) break;

case 3:
//C
this.state = 28;
 //BA.debugLineNum = 408;BA.debugLine="ToastMessageShow(\"Please input a date returned.\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a date returned."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 28;
 //BA.debugLineNum = 410;BA.debugLine="ToastMessageShow(\"Please input a time returned\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a time returned"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 412;BA.debugLine="If BUTTON_DELIVERED.Text = \"EDIT\" Then";
if (true) break;

case 8:
//if
this.state = 27;
if ((parent.mostCurrent._button_delivered.getText()).equals("EDIT")) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 10:
//C
this.state = 27;
 //BA.debugLineNum = 413;BA.debugLine="LABEL_LOAD_DATE.Enabled = True";
parent.mostCurrent._label_load_date.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 414;BA.debugLine="LABEL_LOAD_TIME.Enabled = True";
parent.mostCurrent._label_load_time.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 415;BA.debugLine="OpenLabel(LABEL_LOAD_DATE)";
_openlabel(parent.mostCurrent._label_load_date);
 //BA.debugLineNum = 417;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 418;BA.debugLine="bg2.Initialize2(Colors.ARGB(255,255,0,242), 5,";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (0),(int) (242)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 419;BA.debugLine="BUTTON_DELIVERED.Background = bg2";
parent.mostCurrent._button_delivered.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 420;BA.debugLine="BUTTON_DELIVERED.Text = \"DELIVERED\"";
parent.mostCurrent._button_delivered.setText(BA.ObjectToCharSequence("DELIVERED"));
 //BA.debugLineNum = 422;BA.debugLine="transaction_type = \"NEW\"";
parent._transaction_type = "NEW";
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 424;BA.debugLine="Dim picklist_details As String";
_picklist_details = "";
 //BA.debugLineNum = 425;BA.debugLine="Msgbox2Async(\"Are you sure you mark as delivere";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you mark as delivered this route?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 426;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 29;
return;
case 29:
//C
this.state = 13;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 427;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 13:
//if
this.state = 26;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 428;BA.debugLine="NEW_TRANSACTION";
_new_transaction();
 //BA.debugLineNum = 429;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 16;
;
 //BA.debugLineNum = 430;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 431;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 432;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Picklist\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Picklist"));
 //BA.debugLineNum = 433;BA.debugLine="LABEL_MSGBOX2.Text = \"Finalizing data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Finalizing data..."));
 //BA.debugLineNum = 434;BA.debugLine="For i = 0 To LVL_PICKLIST.Size - 1";
if (true) break;

case 16:
//for
this.state = 25;
step26 = 1;
limit26 = (int) (parent.mostCurrent._lvl_picklist.getSize()-1);
_i = (int) (0) ;
this.state = 31;
if (true) break;

case 31:
//C
this.state = 25;
if ((step26 > 0 && _i <= limit26) || (step26 < 0 && _i >= limit26)) this.state = 18;
if (true) break;

case 32:
//C
this.state = 31;
_i = ((int)(0 + _i + step26)) ;
if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 435;BA.debugLine="picklist_details = LVL_PICKLIST.GetItem(i)";
_picklist_details = BA.ObjectToString(parent.mostCurrent._lvl_picklist.GetItem(_i));
 //BA.debugLineNum = 436;BA.debugLine="picklist_id = picklist_details.SubString2(0,p";
parent._picklist_id = _picklist_details.substring((int) (0),_picklist_details.indexOf("|"));
 //BA.debugLineNum = 438;BA.debugLine="Log(picklist_id)";
anywheresoftware.b4a.keywords.Common.LogImpl("75832736",parent._picklist_id,0);
 //BA.debugLineNum = 439;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist";
parent._connection.ExecNonQuery("DELETE FROM picklist_return_ref_table WHERE picklist_id = '"+parent._picklist_id+"'");
 //BA.debugLineNum = 440;BA.debugLine="Dim QUERY As String = \"insert into picklist_r";
_query = "insert into picklist_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 441;BA.debugLine="connection.ExecNonQuery2(QUERY,Array As Strin";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._return_route_id,_picklist_details.substring((int) (0),_picklist_details.indexOf("|")),_picklist_details.substring((int) (_picklist_details.indexOf("|")+1)),parent.mostCurrent._label_load_route_name.getText(),parent.mostCurrent._label_load_plate.getText(),parent.mostCurrent._label_load_driver.getText(),parent.mostCurrent._label_load_helper.getText(),parent.mostCurrent._label_load_helper2.getText(),parent.mostCurrent._label_load_helper3.getText(),parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._label_load_time.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 443;BA.debugLine="ToastMessageShow(\"Added Successfully\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Added Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 444;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 33;
return;
case 33:
//C
this.state = 19;
;
 //BA.debugLineNum = 445;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_";
_cmd = _createcommand("update_return",(Object[])(new String[]{parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._label_load_time.getText(),parent._return_route_id,parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ ,parent._picklist_id}));
 //BA.debugLineNum = 446;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatc";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 447;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 34;
return;
case 34:
//C
this.state = 19;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 448;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 449;BA.debugLine="UPDATE_DELIVERED";
_update_delivered();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 451;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 452;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("75832750","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 453;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT T";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 454;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 32;
;
 //BA.debugLineNum = 456;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 458;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 459;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 26;
;
 //BA.debugLineNum = 460;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 461;BA.debugLine="ToastMessageShow(\"Route mark as delivered.\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Route mark as delivered."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 462;BA.debugLine="LOAD_ROUTE";
_load_route();
 if (true) break;

case 26:
//C
this.state = 27;
;
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;

case 28:
//C
this.state = -1;
;
 //BA.debugLineNum = 466;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_exit_route_click() throws Exception{
 //BA.debugLineNum = 492;BA.debugLine="Sub BUTTON_EXIT_ROUTE_Click";
 //BA.debugLineNum = 493;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
return "";
}
public static void  _button_load_click() throws Exception{
ResumableSub_BUTTON_LOAD_Click rsub = new ResumableSub_BUTTON_LOAD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_LOAD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_LOAD_Click(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 257;BA.debugLine="picklist_id = EDITTEXT_TYPE.Text.ToUpperCase";
parent._picklist_id = parent.mostCurrent._edittext_type.getText().toUpperCase();
 //BA.debugLineNum = 258;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 259;BA.debugLine="VALIDATE_PICKLIST_STATUS";
_validate_picklist_status();
 //BA.debugLineNum = 260;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_returns_click() throws Exception{
ResumableSub_BUTTON_RETURNS_Click rsub = new ResumableSub_BUTTON_RETURNS_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_RETURNS_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_RETURNS_Click(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
String _picklist_details = "";
int _result = 0;
int _i = 0;
String _query = "";
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int step27;
int limit27;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 762;BA.debugLine="If transaction_type = \"SAVED\" Then";
if (true) break;

case 1:
//if
this.state = 28;
if ((parent._transaction_type).equals("SAVED")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 28;
 //BA.debugLineNum = 763;BA.debugLine="date_return = LABEL_LOAD_DATE.Text";
parent._date_return = parent.mostCurrent._label_load_date.getText();
 //BA.debugLineNum = 764;BA.debugLine="route_name = LABEL_LOAD_ROUTE_NAME.Text";
parent._route_name = parent.mostCurrent._label_load_route_name.getText();
 //BA.debugLineNum = 765;BA.debugLine="plate_no = LABEL_LOAD_PLATE.Text";
parent._plate_no = parent.mostCurrent._label_load_plate.getText();
 //BA.debugLineNum = 766;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 767;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 28;
;
 //BA.debugLineNum = 768;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 769;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 30;
return;
case 30:
//C
this.state = 28;
;
 //BA.debugLineNum = 770;BA.debugLine="StartActivity(SALES_RETURN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._sales_return_module.getObject()));
 //BA.debugLineNum = 771;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\"";
_setanimation("right_to_center","center_to_left");
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 773;BA.debugLine="If LABEL_LOAD_DATE.Text = \"\" Then";
if (true) break;

case 6:
//if
this.state = 27;
if ((parent.mostCurrent._label_load_date.getText()).equals("")) { 
this.state = 8;
}else if((parent.mostCurrent._label_load_time.getText()).equals("")) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 8:
//C
this.state = 27;
 //BA.debugLineNum = 774;BA.debugLine="ToastMessageShow(\"Please input a date returned.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a date returned."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = 27;
 //BA.debugLineNum = 776;BA.debugLine="ToastMessageShow(\"Please input a time returned\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please input a time returned"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 778;BA.debugLine="Dim picklist_details As String";
_picklist_details = "";
 //BA.debugLineNum = 779;BA.debugLine="Msgbox2Async(\"After you mark this as delivered,";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("After you mark this as delivered, we will proceed you to sales return of this picklist. Are you sure you mark as delivered this route?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 780;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 31;
return;
case 31:
//C
this.state = 13;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 781;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 13:
//if
this.state = 26;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 782;BA.debugLine="NEW_TRANSACTION";
_new_transaction();
 //BA.debugLineNum = 783;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 32;
return;
case 32:
//C
this.state = 16;
;
 //BA.debugLineNum = 784;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 785;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 786;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Picklist\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Picklist"));
 //BA.debugLineNum = 787;BA.debugLine="LABEL_MSGBOX2.Text = \"Finalizing data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Finalizing data..."));
 //BA.debugLineNum = 788;BA.debugLine="For i = 0 To LVL_PICKLIST.Size - 1";
if (true) break;

case 16:
//for
this.state = 25;
step27 = 1;
limit27 = (int) (parent.mostCurrent._lvl_picklist.getSize()-1);
_i = (int) (0) ;
this.state = 33;
if (true) break;

case 33:
//C
this.state = 25;
if ((step27 > 0 && _i <= limit27) || (step27 < 0 && _i >= limit27)) this.state = 18;
if (true) break;

case 34:
//C
this.state = 33;
_i = ((int)(0 + _i + step27)) ;
if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 789;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 35;
return;
case 35:
//C
this.state = 19;
;
 //BA.debugLineNum = 790;BA.debugLine="picklist_details = LVL_PICKLIST.GetItem(i)";
_picklist_details = BA.ObjectToString(parent.mostCurrent._lvl_picklist.GetItem(_i));
 //BA.debugLineNum = 791;BA.debugLine="picklist_id = picklist_details.SubString2(0,p";
parent._picklist_id = _picklist_details.substring((int) (0),_picklist_details.indexOf("|"));
 //BA.debugLineNum = 792;BA.debugLine="Log(picklist_id)";
anywheresoftware.b4a.keywords.Common.LogImpl("76684703",parent._picklist_id,0);
 //BA.debugLineNum = 793;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM picklist";
parent._connection.ExecNonQuery("DELETE FROM picklist_return_ref_table WHERE picklist_id = '"+parent._picklist_id+"'");
 //BA.debugLineNum = 794;BA.debugLine="Dim QUERY As String = \"insert into picklist_r";
_query = "insert into picklist_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 795;BA.debugLine="connection.ExecNonQuery2(QUERY,Array As Strin";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._return_route_id,_picklist_details.substring((int) (0),_picklist_details.indexOf("|")),_picklist_details.substring((int) (_picklist_details.indexOf("|")+1)),parent.mostCurrent._label_load_route_name.getText(),parent.mostCurrent._label_load_plate.getText(),parent.mostCurrent._label_load_driver.getText(),parent.mostCurrent._label_load_helper.getText(),parent.mostCurrent._label_load_helper2.getText(),parent.mostCurrent._label_load_helper3.getText(),parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._label_load_time.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 797;BA.debugLine="ToastMessageShow(\"Added Successfully\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Added Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 798;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = 19;
;
 //BA.debugLineNum = 799;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_";
_cmd = _createcommand("update_return",(Object[])(new String[]{parent.mostCurrent._label_load_date.getText(),parent.mostCurrent._label_load_time.getText(),parent._return_route_id,parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ ,parent._picklist_id}));
 //BA.debugLineNum = 800;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatc";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 801;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 37;
return;
case 37:
//C
this.state = 19;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 802;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 803;BA.debugLine="UPDATE_DELIVERED";
_update_delivered();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 805;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, Fals";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 806;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("76684717","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 807;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT T";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 808;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 34;
;
 //BA.debugLineNum = 810;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 812;BA.debugLine="LOAD_ROUTE";
_load_route();
 //BA.debugLineNum = 813;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 38;
return;
case 38:
//C
this.state = 26;
;
 //BA.debugLineNum = 814;BA.debugLine="date_return = LABEL_LOAD_DATE.Text";
parent._date_return = parent.mostCurrent._label_load_date.getText();
 //BA.debugLineNum = 815;BA.debugLine="route_name = LABEL_LOAD_ROUTE_NAME.Text";
parent._route_name = parent.mostCurrent._label_load_route_name.getText();
 //BA.debugLineNum = 816;BA.debugLine="plate_no = LABEL_LOAD_PLATE.Text";
parent._plate_no = parent.mostCurrent._label_load_plate.getText();
 //BA.debugLineNum = 817;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 818;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 39;
return;
case 39:
//C
this.state = 26;
;
 //BA.debugLineNum = 819;BA.debugLine="ToastMessageShow(\"Route mark as delivered.\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Route mark as delivered."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 820;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 821;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 40;
return;
case 40:
//C
this.state = 26;
;
 //BA.debugLineNum = 822;BA.debugLine="StartActivity(SALES_RETURN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._sales_return_module.getObject()));
 //BA.debugLineNum = 823;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_lef";
_setanimation("right_to_center","center_to_left");
 if (true) break;

case 26:
//C
this.state = 27;
;
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;

case 28:
//C
this.state = -1;
;
 //BA.debugLineNum = 827;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _clear_route() throws Exception{
 //BA.debugLineNum = 482;BA.debugLine="Sub CLEAR_ROUTE";
 //BA.debugLineNum = 483;BA.debugLine="LABEL_LOAD_ROUTE_NAME.Text = \"\"";
mostCurrent._label_load_route_name.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 484;BA.debugLine="LABEL_LOAD_PLATE.Text = \"\"";
mostCurrent._label_load_plate.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 485;BA.debugLine="LABEL_LOAD_DRIVER.Text = \"\"";
mostCurrent._label_load_driver.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 486;BA.debugLine="LABEL_LOAD_HELPER.Text = \"\"";
mostCurrent._label_load_helper.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 487;BA.debugLine="LABEL_LOAD_HELPER2.text = \"\"";
mostCurrent._label_load_helper2.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 488;BA.debugLine="LABEL_LOAD_HELPER3.Text = \"\"";
mostCurrent._label_load_helper3.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 489;BA.debugLine="LABEL_LOAD_DATE.Text = \"\"";
mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 490;BA.debugLine="LABEL_LOAD_TIME.Text = \"\"";
mostCurrent._label_load_time.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 491;BA.debugLine="End Sub";
return "";
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 269;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 270;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 271;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 272;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 273;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 274;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 275;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 264;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 265;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 266;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,return_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 267;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 268;BA.debugLine="End Sub";
return null;
}
public static void  _get_pick() throws Exception{
ResumableSub_GET_PICK rsub = new ResumableSub_GET_PICK(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_PICK extends BA.ResumableSub {
public ResumableSub_GET_PICK(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
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
 //BA.debugLineNum = 355;BA.debugLine="LVL_PICKLIST.Clear";
parent.mostCurrent._lvl_picklist.Clear();
 //BA.debugLineNum = 356;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 357;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_carr";
_cmd = _createcommand("select_carried_pick",(Object[])(new String[]{parent.mostCurrent._label_load_route_name.getText(),parent._date_dispatch,parent.mostCurrent._label_load_plate.getText()}));
 //BA.debugLineNum = 358;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 359;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 360;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 361;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 18;
return;
case 18:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 362;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 363;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group9 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index9 = 0;
groupLen9 = group9.getSize();
this.state = 19;
if (true) break;

case 19:
//C
this.state = 10;
if (index9 < groupLen9) {
this.state = 9;
_row = (Object[])(group9.Get(index9));}
if (true) break;

case 20:
//C
this.state = 19;
index9++;
if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 364;BA.debugLine="LVL_PICKLIST.AddTwoLines2(row(res.Columns.Get(";
parent.mostCurrent._lvl_picklist.AddTwoLines2(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_name"))))]),BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_id"))))]),(Object)(BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_id"))))])+"|"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picklist_name"))))])));
 //BA.debugLineNum = 365;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 367;BA.debugLine="SHOW_ROUTE";
_show_route();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 369;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 370;BA.debugLine="Msgbox2Async(\"Picklist Route is empty, Please a";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Picklist Route is empty, Please advice IT for this conflict."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 374;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 375;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("75636117","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 376;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 377;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 379;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 380;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _get_route() throws Exception{
ResumableSub_GET_ROUTE rsub = new ResumableSub_GET_ROUTE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_ROUTE extends BA.ResumableSub {
public ResumableSub_GET_ROUTE(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group10;
int index10;
int groupLen10;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 320;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 321;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_rout";
_cmd = _createcommand("select_route",(Object[])(new String[]{parent._picklist_id}));
 //BA.debugLineNum = 322;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 323;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 324;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 325;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 18;
return;
case 18:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 327;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("75570568",BA.NumberToString(2),0);
 //BA.debugLineNum = 328;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 329;BA.debugLine="CLEAR_ROUTE";
_clear_route();
 //BA.debugLineNum = 330;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group10 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index10 = 0;
groupLen10 = group10.getSize();
this.state = 19;
if (true) break;

case 19:
//C
this.state = 10;
if (index10 < groupLen10) {
this.state = 9;
_row = (Object[])(group10.Get(index10));}
if (true) break;

case 20:
//C
this.state = 19;
index10++;
if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 331;BA.debugLine="date_dispatch = row(res.Columns.Get(\"date_disp";
parent._date_dispatch = BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_dispatch"))))]);
 //BA.debugLineNum = 332;BA.debugLine="LABEL_LOAD_ROUTE_NAME.Text = row(res.Columns.G";
parent.mostCurrent._label_load_route_name.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("route_name"))))]));
 //BA.debugLineNum = 333;BA.debugLine="LABEL_LOAD_PLATE.Text = row(res.Columns.Get(\"p";
parent.mostCurrent._label_load_plate.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("plate_no"))))]));
 //BA.debugLineNum = 334;BA.debugLine="LABEL_LOAD_DRIVER.Text = row(res.Columns.Get(\"";
parent.mostCurrent._label_load_driver.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("driver"))))]));
 //BA.debugLineNum = 335;BA.debugLine="LABEL_LOAD_HELPER.Text = row(res.Columns.Get(\"";
parent.mostCurrent._label_load_helper.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("helper1"))))]));
 //BA.debugLineNum = 336;BA.debugLine="LABEL_LOAD_HELPER2.Text = row(res.Columns.Get(";
parent.mostCurrent._label_load_helper2.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("helper2"))))]));
 //BA.debugLineNum = 337;BA.debugLine="LABEL_LOAD_HELPER3.Text = row(res.Columns.Get(";
parent.mostCurrent._label_load_helper3.setText(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("helper3"))))]));
 //BA.debugLineNum = 338;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 340;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 13;
;
 //BA.debugLineNum = 341;BA.debugLine="GET_PICK";
_get_pick();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 343;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 344;BA.debugLine="Msgbox2Async(\"Picklist Route is empty, Please a";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Picklist Route is empty, Please advice IT for this conflict."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 347;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 348;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("75570589","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 349;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 350;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 352;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 353;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 220;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 221;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 222;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 223;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 224;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 227;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 38;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 41;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 42;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 44;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 45;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private DateTemplate As B4XDateTemplate";
mostCurrent._datetemplate = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 47;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 48;BA.debugLine="Private InputTemplate As B4XInputTemplate";
mostCurrent._inputtemplate = new wingan.app.b4xinputtemplate();
 //BA.debugLineNum = 50;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 51;BA.debugLine="Private NameColumn(5) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (5)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 53;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 54;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private PANEL_BG_TYPE As Panel";
mostCurrent._panel_bg_type = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private EDITTEXT_TYPE As EditText";
mostCurrent._edittext_type = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private BUTTON_EXIT_ROUTE As Button";
mostCurrent._button_exit_route = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private LABEL_LOAD_HELPER As Label";
mostCurrent._label_load_helper = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private LABEL_LOAD_DRIVER As Label";
mostCurrent._label_load_driver = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private LABEL_LOAD_PLATE As Label";
mostCurrent._label_load_plate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private LABEL_LOAD_ROUTE_NAME As Label";
mostCurrent._label_load_route_name = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private LVL_PICKLIST As ListView";
mostCurrent._lvl_picklist = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private PANEL_ROUTE As Panel";
mostCurrent._panel_route = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private PANEL_BG_ROUTE As Panel";
mostCurrent._panel_bg_route = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private LABEL_LOAD_HELPER2 As Label";
mostCurrent._label_load_helper2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private LABEL_LOAD_HELPER3 As Label";
mostCurrent._label_load_helper3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private LABEL_LOAD_DATE As Label";
mostCurrent._label_load_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private LABEL_LOAD_TIME As Label";
mostCurrent._label_load_time = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private TABLE_ROUTE As B4XTable";
mostCurrent._table_route = new wingan.app.b4xtable();
 //BA.debugLineNum = 75;BA.debugLine="Private BUTTON_DELIVERED As Button";
mostCurrent._button_delivered = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static void  _label_load_date_click() throws Exception{
ResumableSub_LABEL_LOAD_DATE_Click rsub = new ResumableSub_LABEL_LOAD_DATE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_DATE_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_DATE_Click(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
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
 //BA.debugLineNum = 384;BA.debugLine="Dialog.Title = \"Select Returned Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Returned Date");
 //BA.debugLineNum = 385;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)(""),(Object)("NOW")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 386;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 387;BA.debugLine="LABEL_LOAD_DATE.Text = DateTime.Date(DateTemplate";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 //BA.debugLineNum = 388;BA.debugLine="OpenLabel(LABEL_LOAD_TIME)";
_openlabel(parent.mostCurrent._label_load_time);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 390;BA.debugLine="LABEL_LOAD_DATE.Text = DateTime.Date(DateTime.Now";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
 //BA.debugLineNum = 391;BA.debugLine="OpenLabel(LABEL_LOAD_TIME)";
_openlabel(parent.mostCurrent._label_load_time);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 393;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static void  _label_load_time_click() throws Exception{
ResumableSub_LABEL_LOAD_TIME_Click rsub = new ResumableSub_LABEL_LOAD_TIME_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_TIME_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_TIME_Click(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
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
 //BA.debugLineNum = 395;BA.debugLine="InputTemplate.lblTitle.Text = \"Enter Returned Tim";
parent.mostCurrent._inputtemplate._lbltitle /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setText(BA.ObjectToCharSequence("Enter Returned Time (HH:MM tt)"));
 //BA.debugLineNum = 396;BA.debugLine="InputTemplate.RegexPattern = \"^([0-9]|0[0-9]|1[0-";
parent.mostCurrent._inputtemplate._regexpattern /*String*/  = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] [A,P,p,a][M,m]$";
 //BA.debugLineNum = 397;BA.debugLine="Wait For (Dialog.ShowTemplate(InputTemplate, \"OK\"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._inputtemplate),(Object)("OK"),(Object)(""),(Object)("NOW")));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 398;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 399;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 400;BA.debugLine="LABEL_LOAD_TIME.Text = InputTemplate.Text";
parent.mostCurrent._label_load_time.setText(BA.ObjectToCharSequence(parent.mostCurrent._inputtemplate._text /*String*/ ));
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 402;BA.debugLine="LABEL_LOAD_TIME.Text = DateTime.Time(DateTime.No";
parent.mostCurrent._label_load_time.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 404;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_route() throws Exception{
ResumableSub_LOAD_ROUTE rsub = new ResumableSub_LOAD_ROUTE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_ROUTE extends BA.ResumableSub {
public ResumableSub_LOAD_ROUTE(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
String _picklist_carried = "";
anywheresoftware.b4a.objects.collections.List _data = null;
int _ia = 0;
Object[] _row = null;
int _ic = 0;
int step9;
int limit9;
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
 //BA.debugLineNum = 516;BA.debugLine="Dim picklist_carried As String";
_picklist_carried = "";
 //BA.debugLineNum = 517;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 1;
;
 //BA.debugLineNum = 518;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 519;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 1;
;
 //BA.debugLineNum = 520;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 521;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 522;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_return_ref_table GROUP BY return_route_id ORDER BY date_return DESC")));
 //BA.debugLineNum = 523;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 18;
if (parent._cursor5.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 17;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 524;BA.debugLine="For ia = 0 To cursor5.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 15;
step9 = 1;
limit9 = (int) (parent._cursor5.getRowCount()-1);
_ia = (int) (0) ;
this.state = 25;
if (true) break;

case 25:
//C
this.state = 15;
if ((step9 > 0 && _ia <= limit9) || (step9 < 0 && _ia >= limit9)) this.state = 6;
if (true) break;

case 26:
//C
this.state = 25;
_ia = ((int)(0 + _ia + step9)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 525;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 27;
return;
case 27:
//C
this.state = 7;
;
 //BA.debugLineNum = 526;BA.debugLine="cursor5.Position = ia";
parent._cursor5.setPosition(_ia);
 //BA.debugLineNum = 527;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 528;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE return_route_id = '"+parent._cursor5.GetString("return_route_id")+"' GROUP BY picklist_id")));
 //BA.debugLineNum = 529;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 7:
//if
this.state = 14;
if (parent._cursor4.getRowCount()>0) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 530;BA.debugLine="picklist_carried = \"\"";
_picklist_carried = "";
 //BA.debugLineNum = 531;BA.debugLine="For ic = 0 To cursor4.RowCount - 1";
if (true) break;

case 10:
//for
this.state = 13;
step16 = 1;
limit16 = (int) (parent._cursor4.getRowCount()-1);
_ic = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 13;
if ((step16 > 0 && _ic <= limit16) || (step16 < 0 && _ic >= limit16)) this.state = 12;
if (true) break;

case 29:
//C
this.state = 28;
_ic = ((int)(0 + _ic + step16)) ;
if (true) break;

case 12:
//C
this.state = 29;
 //BA.debugLineNum = 532;BA.debugLine="cursor4.Position = ic";
parent._cursor4.setPosition(_ic);
 //BA.debugLineNum = 533;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 30;
return;
case 30:
//C
this.state = 29;
;
 //BA.debugLineNum = 534;BA.debugLine="picklist_carried = picklist_carried & \", \" &";
_picklist_carried = _picklist_carried+", "+parent._cursor4.GetString("picklist_id");
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 536;BA.debugLine="row(0) = cursor5.GetString(\"date_return\")";
_row[(int) (0)] = (Object)(parent._cursor5.GetString("date_return"));
 //BA.debugLineNum = 537;BA.debugLine="row(1) = cursor5.GetString(\"route_name\")";
_row[(int) (1)] = (Object)(parent._cursor5.GetString("route_name"));
 //BA.debugLineNum = 538;BA.debugLine="row(2) = cursor5.GetString(\"plate_no\")";
_row[(int) (2)] = (Object)(parent._cursor5.GetString("plate_no"));
 //BA.debugLineNum = 539;BA.debugLine="row(3) = picklist_carried.SubString2(2,picklis";
_row[(int) (3)] = (Object)(_picklist_carried.substring((int) (2),_picklist_carried.length()));
 //BA.debugLineNum = 540;BA.debugLine="row(4) = cursor5.GetString(\"user_info\")";
_row[(int) (4)] = (Object)(parent._cursor5.GetString("user_info"));
 //BA.debugLineNum = 541;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 14:
//C
this.state = 26;
;
 if (true) break;
if (true) break;

case 15:
//C
this.state = 18;
;
 //BA.debugLineNum = 544;BA.debugLine="TABLE_ROUTE.NumberOfFrozenColumns = 1";
parent.mostCurrent._table_route._numberoffrozencolumns /*int*/  = (int) (1);
 //BA.debugLineNum = 545;BA.debugLine="TABLE_ROUTE.RowHeight = 50dip";
parent.mostCurrent._table_route._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 546;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 31;
return;
case 31:
//C
this.state = 18;
;
 //BA.debugLineNum = 548;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 32;
return;
case 32:
//C
this.state = 18;
;
 //BA.debugLineNum = 549;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 551;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 552;BA.debugLine="ToastMessageShow(\"Invoice is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Invoice is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 18:
//C
this.state = 19;
;
 //BA.debugLineNum = 554;BA.debugLine="TABLE_ROUTE.SetData(Data)";
parent.mostCurrent._table_route._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 555;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 19:
//if
this.state = 22;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 556;BA.debugLine="XSelections.Initialize(TABLE_ROUTE)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_route);
 //BA.debugLineNum = 557;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 22:
//C
this.state = -1;
;
 //BA.debugLineNum = 559;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 33;
return;
case 33:
//C
this.state = -1;
;
 //BA.debugLineNum = 560;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_route_header() throws Exception{
 //BA.debugLineNum = 508;BA.debugLine="Sub LOAD_ROUTE_HEADER";
 //BA.debugLineNum = 509;BA.debugLine="NameColumn(0)=TABLE_ROUTE.AddColumn(\"Date Return\"";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_route._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Date Return",mostCurrent._table_route._column_type_text /*int*/ );
 //BA.debugLineNum = 510;BA.debugLine="NameColumn(1)=TABLE_ROUTE.AddColumn(\"Route\", TABL";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_route._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Route",mostCurrent._table_route._column_type_text /*int*/ );
 //BA.debugLineNum = 511;BA.debugLine="NameColumn(2)=TABLE_ROUTE.AddColumn(\"Truck\", TABL";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_route._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Truck",mostCurrent._table_route._column_type_text /*int*/ );
 //BA.debugLineNum = 512;BA.debugLine="NameColumn(3)=TABLE_ROUTE.AddColumn(\"Carried Pick";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_route._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Carried Picklist",mostCurrent._table_route._column_type_text /*int*/ );
 //BA.debugLineNum = 513;BA.debugLine="NameColumn(4)=TABLE_ROUTE.AddColumn(\"User\", TABLE";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_route._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("User",mostCurrent._table_route._column_type_text /*int*/ );
 //BA.debugLineNum = 514;BA.debugLine="End Sub";
return "";
}
public static void  _new_transaction() throws Exception{
ResumableSub_NEW_TRANSACTION rsub = new ResumableSub_NEW_TRANSACTION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_NEW_TRANSACTION extends BA.ResumableSub {
public ResumableSub_NEW_TRANSACTION(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
String _cexpdate = "";
String _cxmo = "";
String _cxday = "";
String _cxtime = "";
String _cxmin = "";
String _cxsecs = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 496;BA.debugLine="Dim CExpDate As String = DateTime.GetYear(DateTim";
_cexpdate = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 497;BA.debugLine="Dim Cxmo As String = DateTime.GetMonth(DateTime.N";
_cxmo = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 498;BA.debugLine="Dim Cxday As String = DateTime.GetDayOfMonth(Date";
_cxday = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 499;BA.debugLine="Dim Cxtime As String = DateTime.GetHour(DateTime.";
_cxtime = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 500;BA.debugLine="Dim Cxmin As String = DateTime.GetMinute(DateTime";
_cxmin = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 501;BA.debugLine="Dim Cxsecs As String = DateTime.GetSecond(DateTim";
_cxsecs = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetSecond(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 502;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 503;BA.debugLine="return_route_id = \"RRID\"&CExpDate&Cxmo&Cxday&Cxti";
parent._return_route_id = "RRID"+_cexpdate+_cxmo+_cxday+_cxtime+_cxmin+_cxsecs+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 504;BA.debugLine="Log(return_route_id)";
anywheresoftware.b4a.keywords.Common.LogImpl("76094857",parent._return_route_id,0);
 //BA.debugLineNum = 505;BA.debugLine="ToastMessageShow(\"Transaction Created\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Created"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 506;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 189;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 190;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 191;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 192;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 20;BA.debugLine="Dim cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim addBitmap As Bitmap";
_addbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim picklist_id As String";
_picklist_id = "";
 //BA.debugLineNum = 25;BA.debugLine="Dim date_dispatch As String";
_date_dispatch = "";
 //BA.debugLineNum = 26;BA.debugLine="Dim date_return As String";
_date_return = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim route_name As String";
_route_name = "";
 //BA.debugLineNum = 28;BA.debugLine="Dim plate_no As String";
_plate_no = "";
 //BA.debugLineNum = 29;BA.debugLine="Dim transaction_type As String";
_transaction_type = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim return_route_id As String";
_return_route_id = "";
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 195;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 196;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 197;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 198;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 199;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 200;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 201;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 202;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 203;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 204;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public static void  _show_route() throws Exception{
ResumableSub_SHOW_ROUTE rsub = new ResumableSub_SHOW_ROUTE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_SHOW_ROUTE extends BA.ResumableSub {
public ResumableSub_SHOW_ROUTE(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg2 = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 621;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 623;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 624;BA.debugLine="LVL_PICKLIST.Background = bg";
parent.mostCurrent._lvl_picklist.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 625;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 626;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.Typeface = Type";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 627;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.TextSize = 16";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTextSize((float) (16));
 //BA.debugLineNum = 628;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.label.Height = 8%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 629;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.label.Top = -0.5%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTop((int) (-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0.5),mostCurrent.activityBA)));
 //BA.debugLineNum = 630;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.TextColor = Col";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 631;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.Gravity = Gravi";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 632;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Typeface";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 633;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Top = 5%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 634;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextSize";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTextSize((float) (13));
 //BA.debugLineNum = 635;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Height =";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 636;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextColor";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 637;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Gravity =";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 638;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.ItemHeight = 8%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 640;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 642;BA.debugLine="bg2.Initialize2(Colors.ARGB(255,255,0,242), 5, 0,";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (0),(int) (242)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 643;BA.debugLine="BUTTON_DELIVERED.Background = bg2";
parent.mostCurrent._button_delivered.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 644;BA.debugLine="BUTTON_DELIVERED.Text = \"DELIVERED\"";
parent.mostCurrent._button_delivered.setText(BA.ObjectToCharSequence("DELIVERED"));
 //BA.debugLineNum = 646;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 647;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 648;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 649;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 650;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 651;BA.debugLine="LABEL_LOAD_DATE.Enabled = True";
parent.mostCurrent._label_load_date.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 652;BA.debugLine="LABEL_LOAD_TIME.Enabled = True";
parent.mostCurrent._label_load_time.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 653;BA.debugLine="OpenLabel(LABEL_LOAD_DATE)";
_openlabel(parent.mostCurrent._label_load_date);
 //BA.debugLineNum = 655;BA.debugLine="transaction_type = \"NEW\"";
parent._transaction_type = "NEW";
 //BA.debugLineNum = 656;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _show_saved_route() throws Exception{
ResumableSub_SHOW_SAVED_ROUTE rsub = new ResumableSub_SHOW_SAVED_ROUTE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_SHOW_SAVED_ROUTE extends BA.ResumableSub {
public ResumableSub_SHOW_SAVED_ROUTE(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg2 = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 658;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 660;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 661;BA.debugLine="LVL_PICKLIST.Background = bg";
parent.mostCurrent._lvl_picklist.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 662;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 663;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.Typeface = Type";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 664;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.TextSize = 16";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTextSize((float) (16));
 //BA.debugLineNum = 665;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.label.Height = 8%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 666;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.label.Top = -0.5%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTop((int) (-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0.5),mostCurrent.activityBA)));
 //BA.debugLineNum = 667;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.TextColor = Col";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 668;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.Label.Gravity = Gravi";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 669;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Typeface";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 670;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Top = 5%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 671;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextSize";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTextSize((float) (13));
 //BA.debugLineNum = 672;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Height =";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 673;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextColor";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 674;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.SecondLabel.Gravity =";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 675;BA.debugLine="LVL_PICKLIST.TwoLinesLayout.ItemHeight = 8%y";
parent.mostCurrent._lvl_picklist.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 677;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 678;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 679;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 680;BA.debugLine="PANEL_BG_ROUTE.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_route.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 682;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 684;BA.debugLine="bg2.Initialize2(Colors.ARGB(255,0,173,255), 5, 0,";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (173),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 685;BA.debugLine="BUTTON_DELIVERED.Background = bg2";
parent.mostCurrent._button_delivered.setBackground((android.graphics.drawable.Drawable)(_bg2.getObject()));
 //BA.debugLineNum = 686;BA.debugLine="BUTTON_DELIVERED.Text = \"EDIT\"";
parent.mostCurrent._button_delivered.setText(BA.ObjectToCharSequence("EDIT"));
 //BA.debugLineNum = 688;BA.debugLine="LABEL_LOAD_DATE.Enabled = False";
parent.mostCurrent._label_load_date.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 689;BA.debugLine="LABEL_LOAD_TIME.Enabled = False";
parent.mostCurrent._label_load_time.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 691;BA.debugLine="transaction_type = \"SAVED\"";
parent._transaction_type = "SAVED";
 //BA.debugLineNum = 692;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_route_cellclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
 //BA.debugLineNum = 561;BA.debugLine="Sub TABLE_ROUTE_CellClicked (ColumnId As String, R";
 //BA.debugLineNum = 562;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 563;BA.debugLine="Dim RowData As Map = TABLE_ROUTE.GetRow(RowId)";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._table_route._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 564;BA.debugLine="End Sub";
return "";
}
public static void  _table_route_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_ROUTE_CellLongClicked rsub = new ResumableSub_TABLE_ROUTE_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_ROUTE_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_ROUTE_CellLongClicked(wingan.app.return_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.return_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
int _ia = 0;
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
 //BA.debugLineNum = 566;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 568;BA.debugLine="Dim RowData As Map = TABLE_ROUTE.GetRow(RowId)";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_route._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 569;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 570;BA.debugLine="CLEAR_ROUTE";
_clear_route();
 //BA.debugLineNum = 571;BA.debugLine="LVL_PICKLIST.Clear";
parent.mostCurrent._lvl_picklist.Clear();
 //BA.debugLineNum = 572;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT * FROM pic";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE date_return = '"+BA.ObjectToString(_rowdata.Get((Object)("Date Return")))+"' AND route_name = '"+BA.ObjectToString(_rowdata.Get((Object)("Route")))+"' AND plate_no = '"+BA.ObjectToString(_rowdata.Get((Object)("Truck")))+"'")));
 //BA.debugLineNum = 573;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor6.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 574;BA.debugLine="For ia = 0 To cursor6.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step8 = 1;
limit8 = (int) (parent._cursor6.getRowCount()-1);
_ia = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step8 > 0 && _ia <= limit8) || (step8 < 0 && _ia >= limit8)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_ia = ((int)(0 + _ia + step8)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 575;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 576;BA.debugLine="cursor6.Position = ia";
parent._cursor6.setPosition(_ia);
 //BA.debugLineNum = 577;BA.debugLine="return_route_id = cursor6.GetString(\"return_rou";
parent._return_route_id = parent._cursor6.GetString("return_route_id");
 //BA.debugLineNum = 578;BA.debugLine="LABEL_LOAD_ROUTE_NAME.Text = cursor6.GetString(";
parent.mostCurrent._label_load_route_name.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("route_name")));
 //BA.debugLineNum = 579;BA.debugLine="LABEL_LOAD_PLATE.Text = cursor6.GetString(\"plat";
parent.mostCurrent._label_load_plate.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("plate_no")));
 //BA.debugLineNum = 580;BA.debugLine="LABEL_LOAD_DRIVER.Text = cursor6.GetString(\"dri";
parent.mostCurrent._label_load_driver.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("driver")));
 //BA.debugLineNum = 581;BA.debugLine="LABEL_LOAD_HELPER.Text = cursor6.GetString(\"hel";
parent.mostCurrent._label_load_helper.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("helper1")));
 //BA.debugLineNum = 582;BA.debugLine="LABEL_LOAD_HELPER2.Text = cursor6.GetString(\"he";
parent.mostCurrent._label_load_helper2.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("helper2")));
 //BA.debugLineNum = 583;BA.debugLine="LABEL_LOAD_HELPER3.Text = cursor6.GetString(\"he";
parent.mostCurrent._label_load_helper3.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("helper3")));
 //BA.debugLineNum = 584;BA.debugLine="LVL_PICKLIST.AddTwoLines2(cursor6.GetString(\"pi";
parent.mostCurrent._lvl_picklist.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor6.GetString("picklist_name")),BA.ObjectToCharSequence(parent._cursor6.GetString("picklist_id")),(Object)(parent._cursor6.GetString("picklist_id")+"|"+parent._cursor6.GetString("picklist_name")));
 //BA.debugLineNum = 585;BA.debugLine="LABEL_LOAD_DATE.Text = cursor6.GetString(\"date_";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("date_return")));
 //BA.debugLineNum = 586;BA.debugLine="LABEL_LOAD_TIME.Text = cursor6.GetString(\"time_";
parent.mostCurrent._label_load_time.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("time_return")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 588;BA.debugLine="SHOW_SAVED_ROUTE";
_show_saved_route();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 590;BA.debugLine="ToastMessageShow(\"Invoice is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Invoice is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 593;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_route_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 594;BA.debugLine="Sub TABLE_ROUTE_DataUpdated";
 //BA.debugLineNum = 595;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 596;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 598;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 600;BA.debugLine="For i = 0 To TABLE_ROUTE.VisibleRowIds.Size";
{
final int step4 = 1;
final int limit4 = mostCurrent._table_route._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 601;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 602;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 603;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 608;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 609;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 610;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 614;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 615;BA.debugLine="TABLE_ROUTE.Refresh";
mostCurrent._table_route._refresh /*String*/ ();
 //BA.debugLineNum = 616;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 618;BA.debugLine="End Sub";
return "";
}
public static void  _update_delivered() throws Exception{
ResumableSub_UPDATE_DELIVERED rsub = new ResumableSub_UPDATE_DELIVERED(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_DELIVERED extends BA.ResumableSub {
public ResumableSub_UPDATE_DELIVERED(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 468;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_pick";
_cmd = _createcommand("update_picklist_delivered",new Object[]{(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 469;BA.debugLine="Dim jr As HttpJob = CreateRequest.ExecuteBatch(Ar";
_jr = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 470;BA.debugLine="Wait For(jr) JobDone(jr As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_jr));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 471;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 474;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 475;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("75898248","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 476;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 477;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 479;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 480;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 216;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 217;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 218;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
return "";
}
public static void  _validate_picklist_status() throws Exception{
ResumableSub_VALIDATE_PICKLIST_STATUS rsub = new ResumableSub_VALIDATE_PICKLIST_STATUS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_VALIDATE_PICKLIST_STATUS extends BA.ResumableSub {
public ResumableSub_VALIDATE_PICKLIST_STATUS(wingan.app.return_module parent) {
this.parent = parent;
}
wingan.app.return_module parent;
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
 //BA.debugLineNum = 277;BA.debugLine="PANEL_BG_TYPE.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_type.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 278;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 279;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 280;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Downloading Picklist\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Downloading Picklist"));
 //BA.debugLineNum = 281;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching data..."));
 //BA.debugLineNum = 282;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 283;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_pick";
_cmd = _createcommand("select_picklist_status",new Object[]{(Object)(parent._picklist_id.trim())});
 //BA.debugLineNum = 284;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 25;
return;
case 25:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 285;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 24;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 23;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 286;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 287;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 26;
return;
case 26:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 288;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 21;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 20;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 289;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 18;
group13 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index13 = 0;
groupLen13 = group13.getSize();
this.state = 27;
if (true) break;

case 27:
//C
this.state = 18;
if (index13 < groupLen13) {
this.state = 9;
_row = (Object[])(group13.Get(index13));}
if (true) break;

case 28:
//C
this.state = 27;
index13++;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 290;BA.debugLine="If row(res.Columns.Get(\"PickListStatus\")) = \"D";
if (true) break;

case 10:
//if
this.state = 17;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))]).equals((Object)("DISPATCH"))) { 
this.state = 12;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))]).equals((Object)("DELIVERED"))) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 12:
//C
this.state = 17;
 //BA.debugLineNum = 291;BA.debugLine="GET_ROUTE";
_get_route();
 if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 293;BA.debugLine="Log(98)";
anywheresoftware.b4a.keywords.Common.LogImpl("75505041",BA.NumberToString(98),0);
 //BA.debugLineNum = 294;BA.debugLine="Msgbox2Async(\"The picklist you scan :\" & CRLF";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The picklist you scan :"+anywheresoftware.b4a.keywords.Common.CRLF+" Picklist Name : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PicklistName"))))])),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 297;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 299;BA.debugLine="Msgbox2Async(\"The picklist you scan :\" & CRLF";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The picklist you scan :"+anywheresoftware.b4a.keywords.Common.CRLF+" Picklist Name : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PicklistName"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" is on :"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PickListStatus"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Tablet : TABLET "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PreparingTabletid"))))])),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 304;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 17:
//C
this.state = 28;
;
 if (true) break;
if (true) break;

case 18:
//C
this.state = 21;
;
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 308;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 309;BA.debugLine="Msgbox2Async(\"The Picklist ID you type/scan is";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 21:
//C
this.state = 24;
;
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 312;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 313;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("75505061","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 314;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 315;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = -1;
;
 //BA.debugLineNum = 317;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 318;BA.debugLine="End Sub";
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
