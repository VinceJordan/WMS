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

public class monthly_inventory_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static monthly_inventory_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.monthly_inventory_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (monthly_inventory_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.monthly_inventory_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.monthly_inventory_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (monthly_inventory_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (monthly_inventory_module) Resume **");
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
		return monthly_inventory_module.class;
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
            BA.LogInfo("** Activity (monthly_inventory_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (monthly_inventory_module) Pause event (activity is not paused). **");
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
            monthly_inventory_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (monthly_inventory_module) Resume **");
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
public static String _transaction_id = "";
public static String _principal_acronym = "";
public static String _principal_id = "";
public static String _principal_name = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _plusbitmap = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xtable _b4xtable1 = null;
public wingan.app.b4xtableselections _xselections = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_new = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_invdate = null;
public wingan.app.b4xcombobox _cmb_principal = null;
public wingan.app.b4xcombobox _cmb_warehouse = null;
public wingan.app.b4xcombobox _cmb_area = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xdatetemplate _datetemplate = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_new_header = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_create = null;
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
public ResumableSub_Activity_Create(wingan.app.monthly_inventory_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.monthly_inventory_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 68;BA.debugLine="Activity.LoadLayout(\"monthly\")";
parent.mostCurrent._activity.LoadLayout("monthly",mostCurrent.activityBA);
 //BA.debugLineNum = 70;BA.debugLine="plusBitmap = LoadBitmap(File.DirAssets, \"add.png\"";
parent._plusbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"add.png");
 //BA.debugLineNum = 73;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 74;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 75;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 76;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 77;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 78;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 79;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 81;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 82;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 83;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 85;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 86;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 89;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 90;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 91;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 93;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 94;BA.debugLine="Dialog.Initialize (Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 95;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 96;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 97;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 98;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 99;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 100;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 101;BA.debugLine="Dialog.BodyTextColor = Colors.Black";
parent.mostCurrent._dialog._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 103;BA.debugLine="DateTemplate.Initialize";
parent.mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 104;BA.debugLine="DateTemplate.MinYear = 2016";
parent.mostCurrent._datetemplate._minyear /*int*/  = (int) (2016);
 //BA.debugLineNum = 105;BA.debugLine="DateTemplate.MaxYear = 2030";
parent.mostCurrent._datetemplate._maxyear /*int*/  = (int) (2030);
 //BA.debugLineNum = 106;BA.debugLine="DateTemplate.lblMonth.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 107;BA.debugLine="DateTemplate.lblYear.TextColor = Colors.Black";
parent.mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 108;BA.debugLine="DateTemplate.btnMonthLeft.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 109;BA.debugLine="DateTemplate.btnMonthRight.Color = Colors.RGB(82,";
parent.mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 110;BA.debugLine="DateTemplate.btnYearLeft.Color = Colors.RGB(82,16";
parent.mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 111;BA.debugLine="DateTemplate.btnYearRight.Color = Colors.RGB(82,1";
parent.mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)));
 //BA.debugLineNum = 112;BA.debugLine="DateTemplate.DaysInMonthColor = Colors.Black";
parent.mostCurrent._datetemplate._daysinmonthcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 114;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 115;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 117;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 118;BA.debugLine="LOAD_INVDATE_HEADER";
_load_invdate_header();
 //BA.debugLineNum = 119;BA.debugLine="LOAD_INVDATE";
_load_invdate();
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
 //BA.debugLineNum = 121;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 122;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("create"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 123;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 124;BA.debugLine="UpdateIcon(\"create\", plusBitmap)";
_updateicon("create",_plusbitmap);
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 516;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 517;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 518;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 520;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.monthly_inventory_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.monthly_inventory_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 494;BA.debugLine="If Item.Title = \"create\" Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((_item.getTitle()).equals("create")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 495;BA.debugLine="PANEL_BG_NEW.BringToFront";
parent.mostCurrent._panel_bg_new.BringToFront();
 //BA.debugLineNum = 496;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 497;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 498;BA.debugLine="POPULATE_PRINCIPAL";
_populate_principal();
 //BA.debugLineNum = 499;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 500;BA.debugLine="POPULATE_WAREHOUSE";
_populate_warehouse();
 //BA.debugLineNum = 501;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 6;
;
 //BA.debugLineNum = 502;BA.debugLine="POPULATE_AREA";
_populate_area();
 //BA.debugLineNum = 503;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 10;
return;
case 10:
//C
this.state = 6;
;
 //BA.debugLineNum = 504;BA.debugLine="LABEL_LOAD_INVDATE.Text = DateTime.Date(DateTime";
parent.mostCurrent._label_load_invdate.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
 //BA.debugLineNum = 505;BA.debugLine="Sleep(300)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (300));
this.state = 11;
return;
case 11:
//C
this.state = 6;
;
 //BA.debugLineNum = 506;BA.debugLine="OpenSpinner(CMB_PRINCIPAL.cmbBox)";
_openspinner(parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 507;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 6;
;
 //BA.debugLineNum = 508;BA.debugLine="CMB_PRINCIPAL.SelectedIndex = -1";
parent.mostCurrent._cmb_principal._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 509;BA.debugLine="LABEL_NEW_HEADER.Text = \"Create New Transaction\"";
parent.mostCurrent._label_new_header.setText(BA.ObjectToCharSequence("Create New Transaction"));
 //BA.debugLineNum = 510;BA.debugLine="BUTTON_CREATE.Text = \" Create\"";
parent.mostCurrent._button_create.setText(BA.ObjectToCharSequence(" Create"));
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 514;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 475;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 476;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 477;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 478;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 479;BA.debugLine="End Sub";
return "";
}
public static void  _b4xtable1_cellclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_B4XTable1_CellClicked rsub = new ResumableSub_B4XTable1_CellClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_B4XTable1_CellClicked extends BA.ResumableSub {
public ResumableSub_B4XTable1_CellClicked(wingan.app.monthly_inventory_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.monthly_inventory_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
int _result = 0;
String _query = "";
int _i = 0;
int step13;
int limit13;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 204;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 206;BA.debugLine="Dim RowData As Map = B4XTable1.GetRow(RowId)";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._b4xtable1._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 207;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 209;BA.debugLine="Log(RowData.Get(\"Inventory Date\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783230730",BA.ObjectToString(_rowdata.Get((Object)("Inventory Date"))),0);
 //BA.debugLineNum = 210;BA.debugLine="Log(RowData.Get(\"Principal\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783230731",BA.ObjectToString(_rowdata.Get((Object)("Principal"))),0);
 //BA.debugLineNum = 211;BA.debugLine="Log(RowData.Get(\"Warehouse\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783230732",BA.ObjectToString(_rowdata.Get((Object)("Warehouse"))),0);
 //BA.debugLineNum = 212;BA.debugLine="Log(RowData.Get(\"Area\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783230733",BA.ObjectToString(_rowdata.Get((Object)("Area"))),0);
 //BA.debugLineNum = 214;BA.debugLine="Msgbox2Async(\"Inventory Date : \" & RowData.Get(\"I";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Inventory Date : "+BA.ObjectToString(_rowdata.Get((Object)("Inventory Date")))+anywheresoftware.b4a.keywords.Common.CRLF+"Principal : "+BA.ObjectToString(_rowdata.Get((Object)("Principal")))+anywheresoftware.b4a.keywords.Common.CRLF+"Warehouse : "+BA.ObjectToString(_rowdata.Get((Object)("Warehouse")))+anywheresoftware.b4a.keywords.Common.CRLF+"Area : "+BA.ObjectToString(_rowdata.Get((Object)("Area")))+anywheresoftware.b4a.keywords.Common.CRLF),BA.ObjectToCharSequence("Option"),"Continue","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 219;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 220;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 9;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 221;BA.debugLine="Dim query As String = \"SELECT * FROM inventory_r";
_query = "SELECT * FROM inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?";
 //BA.debugLineNum = 222;BA.debugLine="cursor1 = connection.ExecQuery2(query,Array As S";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{BA.ObjectToString(_rowdata.Get((Object)("Inventory Date"))),BA.ObjectToString(_rowdata.Get((Object)("Warehouse"))),BA.ObjectToString(_rowdata.Get((Object)("Area"))),BA.ObjectToString(_rowdata.Get((Object)("Principal")))})));
 //BA.debugLineNum = 223;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step13 = 1;
limit13 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 14;
if (true) break;

case 14:
//C
this.state = 7;
if ((step13 > 0 && _i <= limit13) || (step13 < 0 && _i >= limit13)) this.state = 6;
if (true) break;

case 15:
//C
this.state = 14;
_i = ((int)(0 + _i + step13)) ;
if (true) break;

case 6:
//C
this.state = 15;
 //BA.debugLineNum = 224;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 //BA.debugLineNum = 225;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 226;BA.debugLine="transaction_id = cursor1.GetString(\"transaction";
parent._transaction_id = parent._cursor1.GetString("transaction_id");
 //BA.debugLineNum = 227;BA.debugLine="principal_id = cursor1.GetString(\"principal_id\"";
parent._principal_id = parent._cursor1.GetString("principal_id");
 //BA.debugLineNum = 228;BA.debugLine="principal_name = cursor1.GetString(\"principal_n";
parent._principal_name = parent._cursor1.GetString("principal_name");
 if (true) break;
if (true) break;

case 7:
//C
this.state = 12;
;
 //BA.debugLineNum = 230;BA.debugLine="B4XTable1.Refresh";
parent.mostCurrent._b4xtable1._refresh /*String*/ ();
 //BA.debugLineNum = 231;BA.debugLine="StartActivity(MONTLHY_INVENTORY2_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._montlhy_inventory2_module.getObject()));
 //BA.debugLineNum = 232;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\"";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 233;BA.debugLine="MONTLHY_INVENTORY2_MODULE.cmb_trigger = 1";
parent.mostCurrent._montlhy_inventory2_module._cmb_trigger /*int*/  = (int) (1);
 if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 237;BA.debugLine="B4XTable1.Refresh";
parent.mostCurrent._b4xtable1._refresh /*String*/ ();
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static void  _b4xtable1_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_B4XTable1_CellLongClicked rsub = new ResumableSub_B4XTable1_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_B4XTable1_CellLongClicked extends BA.ResumableSub {
public ResumableSub_B4XTable1_CellLongClicked(wingan.app.monthly_inventory_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.monthly_inventory_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
int _result = 0;
String _query = "";
int _i = 0;
String _insert_query = "";
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
 //BA.debugLineNum = 245;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 247;BA.debugLine="Dim RowData As Map = B4XTable1.GetRow(RowId)";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._b4xtable1._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 248;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 250;BA.debugLine="Log(RowData.Get(\"Inventory Date\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783296266",BA.ObjectToString(_rowdata.Get((Object)("Inventory Date"))),0);
 //BA.debugLineNum = 251;BA.debugLine="Log(RowData.Get(\"Principal\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783296267",BA.ObjectToString(_rowdata.Get((Object)("Principal"))),0);
 //BA.debugLineNum = 252;BA.debugLine="Log(RowData.Get(\"Warehouse\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783296268",BA.ObjectToString(_rowdata.Get((Object)("Warehouse"))),0);
 //BA.debugLineNum = 253;BA.debugLine="Log(RowData.Get(\"Area\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("783296269",BA.ObjectToString(_rowdata.Get((Object)("Area"))),0);
 //BA.debugLineNum = 255;BA.debugLine="Msgbox2Async(\"Inventory Date : \" & RowData.Get(\"I";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Inventory Date : "+BA.ObjectToString(_rowdata.Get((Object)("Inventory Date")))+anywheresoftware.b4a.keywords.Common.CRLF+"Principal : "+BA.ObjectToString(_rowdata.Get((Object)("Principal")))+anywheresoftware.b4a.keywords.Common.CRLF+"Warehouse : "+BA.ObjectToString(_rowdata.Get((Object)("Warehouse")))+anywheresoftware.b4a.keywords.Common.CRLF+"Area : "+BA.ObjectToString(_rowdata.Get((Object)("Area")))+anywheresoftware.b4a.keywords.Common.CRLF),BA.ObjectToCharSequence("Option"),"Edit","","Delete",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 260;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 17;
return;
case 17:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 261;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 16;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 9;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 262;BA.debugLine="POPULATE_PRINCIPAL";
_populate_principal();
 //BA.debugLineNum = 263;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 18;
return;
case 18:
//C
this.state = 4;
;
 //BA.debugLineNum = 264;BA.debugLine="CMB_PRINCIPAL.SelectedIndex = 0";
parent.mostCurrent._cmb_principal._setselectedindex /*int*/ ((int) (0));
 //BA.debugLineNum = 265;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 19;
return;
case 19:
//C
this.state = 4;
;
 //BA.debugLineNum = 266;BA.debugLine="POPULATE_WAREHOUSE";
_populate_warehouse();
 //BA.debugLineNum = 267;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 20;
return;
case 20:
//C
this.state = 4;
;
 //BA.debugLineNum = 268;BA.debugLine="CMB_WAREHOUSE.SelectedIndex = 0";
parent.mostCurrent._cmb_warehouse._setselectedindex /*int*/ ((int) (0));
 //BA.debugLineNum = 269;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 21;
return;
case 21:
//C
this.state = 4;
;
 //BA.debugLineNum = 270;BA.debugLine="POPULATE_AREA";
_populate_area();
 //BA.debugLineNum = 271;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 22;
return;
case 22:
//C
this.state = 4;
;
 //BA.debugLineNum = 272;BA.debugLine="CMB_AREA.SelectedIndex = 0";
parent.mostCurrent._cmb_area._setselectedindex /*int*/ ((int) (0));
 //BA.debugLineNum = 273;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 23;
return;
case 23:
//C
this.state = 4;
;
 //BA.debugLineNum = 275;BA.debugLine="Dim query As String = \"SELECT * FROM inventory_r";
_query = "SELECT * FROM inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?";
 //BA.debugLineNum = 276;BA.debugLine="cursor6 = connection.ExecQuery2(query,Array As S";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{BA.ObjectToString(_rowdata.Get((Object)("Inventory Date"))),BA.ObjectToString(_rowdata.Get((Object)("Warehouse"))),BA.ObjectToString(_rowdata.Get((Object)("Area"))),BA.ObjectToString(_rowdata.Get((Object)("Principal")))})));
 //BA.debugLineNum = 277;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step25 = 1;
limit25 = (int) (parent._cursor6.getRowCount()-1);
_i = (int) (0) ;
this.state = 24;
if (true) break;

case 24:
//C
this.state = 7;
if ((step25 > 0 && _i <= limit25) || (step25 < 0 && _i >= limit25)) this.state = 6;
if (true) break;

case 25:
//C
this.state = 24;
_i = ((int)(0 + _i + step25)) ;
if (true) break;

case 6:
//C
this.state = 25;
 //BA.debugLineNum = 278;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 279;BA.debugLine="transaction_id = cursor6.GetString(\"transaction";
parent._transaction_id = parent._cursor6.GetString("transaction_id");
 //BA.debugLineNum = 280;BA.debugLine="Log(CMB_PRINCIPAL.cmbBox.IndexOf(cursor6.GetStr";
anywheresoftware.b4a.keywords.Common.LogImpl("783296296",BA.NumberToString(parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("principal_name"))),0);
 //BA.debugLineNum = 281;BA.debugLine="Log(CMB_WAREHOUSE.cmbBox.IndexOf(cursor6.GetStr";
anywheresoftware.b4a.keywords.Common.LogImpl("783296297",BA.NumberToString(parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("warehouse"))),0);
 //BA.debugLineNum = 282;BA.debugLine="Log(CMB_AREA.cmbBox.IndexOf(cursor6.GetString(\"";
anywheresoftware.b4a.keywords.Common.LogImpl("783296298",BA.NumberToString(parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("area"))),0);
 //BA.debugLineNum = 283;BA.debugLine="CMB_PRINCIPAL.SelectedIndex = CMB_PRINCIPAL.cmb";
parent.mostCurrent._cmb_principal._setselectedindex /*int*/ (parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("principal_name")));
 //BA.debugLineNum = 284;BA.debugLine="CMB_WAREHOUSE.SelectedIndex = CMB_WAREHOUSE.cmb";
parent.mostCurrent._cmb_warehouse._setselectedindex /*int*/ (parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("warehouse")));
 //BA.debugLineNum = 285;BA.debugLine="CMB_AREA.SelectedIndex = CMB_AREA.cmbBox.IndexO";
parent.mostCurrent._cmb_area._setselectedindex /*int*/ (parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor6.GetString("area")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 16;
;
 //BA.debugLineNum = 287;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 26;
return;
case 26:
//C
this.state = 16;
;
 //BA.debugLineNum = 288;BA.debugLine="LABEL_LOAD_INVDATE.Text = RowData.Get(\"Inventory";
parent.mostCurrent._label_load_invdate.setText(BA.ObjectToCharSequence(_rowdata.Get((Object)("Inventory Date"))));
 //BA.debugLineNum = 289;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 290;BA.debugLine="PANEL_BG_NEW.BringToFront";
parent.mostCurrent._panel_bg_new.BringToFront();
 //BA.debugLineNum = 291;BA.debugLine="LABEL_NEW_HEADER.Text = \"Editing Transaction\"";
parent.mostCurrent._label_new_header.setText(BA.ObjectToCharSequence("Editing Transaction"));
 //BA.debugLineNum = 292;BA.debugLine="BUTTON_CREATE.Text = \" Edit\"";
parent.mostCurrent._button_create.setText(BA.ObjectToCharSequence(" Edit"));
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 295;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this transaction? All data in this transaction will be lost."),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 296;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 10;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 297;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 10:
//if
this.state = 13;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 298;BA.debugLine="Dim insert_query As String = \"INSERT INTO inven";
_insert_query = "INSERT INTO inventory_ref_table_trail SELECT transaction_id,principal_name,principal_id,inventory_date,warehouse,area,user_info,tab_id,date_registered,time_registered,transaction_status,edit_count,? as 'edit_by','DELETED' as 'edit_type' from inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?  ";
 //BA.debugLineNum = 299;BA.debugLine="connection.ExecNonQuery2(insert_query,Array As";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,BA.ObjectToString(_rowdata.Get((Object)("Inventory Date"))),BA.ObjectToString(_rowdata.Get((Object)("Warehouse"))),BA.ObjectToString(_rowdata.Get((Object)("Area"))),BA.ObjectToString(_rowdata.Get((Object)("Principal")))}));
 //BA.debugLineNum = 300;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 13;
;
 //BA.debugLineNum = 301;BA.debugLine="Dim query As String = \"DELETE from inventory_re";
_query = "DELETE from inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?";
 //BA.debugLineNum = 302;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{BA.ObjectToString(_rowdata.Get((Object)("Inventory Date"))),BA.ObjectToString(_rowdata.Get((Object)("Warehouse"))),BA.ObjectToString(_rowdata.Get((Object)("Area"))),BA.ObjectToString(_rowdata.Get((Object)("Principal")))}));
 //BA.debugLineNum = 303;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 304;BA.debugLine="LOAD_INVDATE";
_load_invdate();
 //BA.debugLineNum = 305;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _b4xtable1_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 180;BA.debugLine="Sub B4XTable1_DataUpdated";
 //BA.debugLineNum = 181;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 182;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)]),(Object)(mostCurrent._namecolumn[(int) (5)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 183;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 184;BA.debugLine="For i = 0 To B4XTable1.VisibleRowIds.Size";
{
final int step4 = 1;
final int limit4 = mostCurrent._b4xtable1._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 185;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 186;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 187;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 189;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 190;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 191;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 194;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 195;BA.debugLine="B4XTable1.Refresh";
mostCurrent._b4xtable1._refresh /*String*/ ();
 //BA.debugLineNum = 196;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 470;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 471;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 472;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 473;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 474;BA.debugLine="End Sub";
return null;
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 363;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 364;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 365;BA.debugLine="End Sub";
return "";
}
public static void  _button_create_click() throws Exception{
ResumableSub_BUTTON_CREATE_Click rsub = new ResumableSub_BUTTON_CREATE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CREATE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CREATE_Click(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
int _result = 0;
int _i = 0;
String _insert_query = "";
String _query = "";
int step7;
int limit7;
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
 //BA.debugLineNum = 395;BA.debugLine="If BUTTON_CREATE.Text = \" Edit\" Then";
if (true) break;

case 1:
//if
this.state = 36;
if ((parent.mostCurrent._button_create.getText()).equals(" Edit")) { 
this.state = 3;
}else {
this.state = 19;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 396;BA.debugLine="Msgbox2Async(\"Are you sure you want to update th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update this transaction?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 397;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 37;
return;
case 37:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 398;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 4:
//if
this.state = 17;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 399;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM i";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_ref_table WHERE inventory_date ='"+parent.mostCurrent._label_load_invdate.getText()+"' and principal_name ='"+parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and  area ='"+parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and  warehouse ='"+parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 402;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 7:
//if
this.state = 16;
if (parent._cursor1.getRowCount()>0) { 
this.state = 9;
}else {
this.state = 15;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 403;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 10:
//for
this.state = 13;
step7 = 1;
limit7 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 38;
if (true) break;

case 38:
//C
this.state = 13;
if ((step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7)) this.state = 12;
if (true) break;

case 39:
//C
this.state = 38;
_i = ((int)(0 + _i + step7)) ;
if (true) break;

case 12:
//C
this.state = 39;
 if (true) break;
if (true) break;

case 13:
//C
this.state = 16;
;
 //BA.debugLineNum = 405;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 40;
return;
case 40:
//C
this.state = 16;
;
 //BA.debugLineNum = 406;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 407;BA.debugLine="Msgbox2Async(\"The system does not allowed to p";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The system does not allowed to process the same area , warehouse and principal on the same inventory date. To continue, you can search the existing transaction"),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 409;BA.debugLine="Dim insert_query As String = \"INSERT INTO inve";
_insert_query = "INSERT INTO inventory_ref_table_trail SELECT transaction_id,principal_name,principal_id,inventory_date,warehouse,area,user_info,tab_id,date_registered,time_registered,transaction_status,edit_count,'EDITED' as 'edit_type',? as 'edit_by' from inventory_ref_table WHERE transaction_id = ?";
 //BA.debugLineNum = 410;BA.debugLine="connection.ExecNonQuery2(insert_query,Array As";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,parent._transaction_id}));
 //BA.debugLineNum = 411;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 41;
return;
case 41:
//C
this.state = 16;
;
 //BA.debugLineNum = 412;BA.debugLine="GET_PRINCIPALID";
_get_principalid();
 //BA.debugLineNum = 413;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 16;
;
 //BA.debugLineNum = 414;BA.debugLine="Dim query As String = \"UPDATE inventory_ref_ta";
_query = "UPDATE inventory_ref_table SET principal_name = ?, principal_id = ?, inventory_date = ?, warehouse = ?, area = ?, user_info = ?, edit_count = edit_count + 1, date_updated = ?, time_updated = ?, transaction_status = ? WHERE transaction_id = ?";
 //BA.debugLineNum = 415;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._principal_id,parent.mostCurrent._label_load_invdate.getText(),parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"EDITED",parent._transaction_id}));
 //BA.debugLineNum = 418;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 16;
;
 //BA.debugLineNum = 419;BA.debugLine="ToastMessageShow(\"Transaction Updated\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 420;BA.debugLine="LOAD_INVDATE";
_load_invdate();
 //BA.debugLineNum = 421;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 44;
return;
case 44:
//C
this.state = 16;
;
 //BA.debugLineNum = 422;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = 17;
;
 if (true) break;

case 17:
//C
this.state = 36;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 426;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM in";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_ref_table WHERE inventory_date ='"+parent.mostCurrent._label_load_invdate.getText()+"' and principal_name ='"+parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and  area ='"+parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and  warehouse ='"+parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 429;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 20:
//if
this.state = 35;
if (parent._cursor1.getRowCount()>0) { 
this.state = 22;
}else {
this.state = 28;
}if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 430;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 23:
//for
this.state = 26;
step30 = 1;
limit30 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 45;
if (true) break;

case 45:
//C
this.state = 26;
if ((step30 > 0 && _i <= limit30) || (step30 < 0 && _i >= limit30)) this.state = 25;
if (true) break;

case 46:
//C
this.state = 45;
_i = ((int)(0 + _i + step30)) ;
if (true) break;

case 25:
//C
this.state = 46;
 if (true) break;
if (true) break;

case 26:
//C
this.state = 35;
;
 //BA.debugLineNum = 432;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 35;
;
 //BA.debugLineNum = 433;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 434;BA.debugLine="Msgbox2Async(\"The system does not allowed to pr";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The system does not allowed to process the same area , warehouse and principal on the same inventory date. To continue, you can search the existing transaction"),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 436;BA.debugLine="Msgbox2Async(\"Are you sure you want to create n";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to create new transaction?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 437;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 48;
return;
case 48:
//C
this.state = 29;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 438;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 29:
//if
this.state = 34;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 31;
}else {
this.state = 33;
}if (true) break;

case 31:
//C
this.state = 34;
 //BA.debugLineNum = 439;BA.debugLine="GET_PRINCIPALID";
_get_principalid();
 //BA.debugLineNum = 440;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 49;
return;
case 49:
//C
this.state = 34;
;
 //BA.debugLineNum = 441;BA.debugLine="GET_TRANSACTIONID";
_get_transactionid();
 //BA.debugLineNum = 442;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 50;
return;
case 50:
//C
this.state = 34;
;
 //BA.debugLineNum = 443;BA.debugLine="Dim query As String = \"INSERT INTO inventory_r";
_query = "INSERT INTO inventory_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 444;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._transaction_id,parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._principal_id,parent.mostCurrent._label_load_invdate.getText(),parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"0","-","-","SAVED"}));
 //BA.debugLineNum = 447;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 51;
return;
case 51:
//C
this.state = 34;
;
 //BA.debugLineNum = 448;BA.debugLine="ToastMessageShow(\"Transaction Added\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Added"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 449;BA.debugLine="LOAD_INVDATE";
_load_invdate();
 //BA.debugLineNum = 450;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 52;
return;
case 52:
//C
this.state = 34;
;
 //BA.debugLineNum = 451;BA.debugLine="PANEL_BG_NEW.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_new.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 33:
//C
this.state = 34;
 if (true) break;

case 34:
//C
this.state = 35;
;
 if (true) break;

case 35:
//C
this.state = 36;
;
 if (true) break;

case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 457;BA.debugLine="End Sub";
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
public ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged(wingan.app.monthly_inventory_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.monthly_inventory_module parent;
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
 //BA.debugLineNum = 347;BA.debugLine="POPULATE_WAREHOUSE";
_populate_warehouse();
 //BA.debugLineNum = 348;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 349;BA.debugLine="OpenSpinner(CMB_WAREHOUSE.cmbBox)";
_openspinner(parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 350;BA.debugLine="End Sub";
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
public ResumableSub_CMB_WAREHOUSE_SelectedIndexChanged(wingan.app.monthly_inventory_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.monthly_inventory_module parent;
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
 //BA.debugLineNum = 352;BA.debugLine="POPULATE_AREA";
_populate_area();
 //BA.debugLineNum = 353;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 354;BA.debugLine="OpenSpinner(CMB_AREA.cmbBox)";
_openspinner(parent.mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 355;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_principalid() throws Exception{
ResumableSub_GET_PRINCIPALID rsub = new ResumableSub_GET_PRINCIPALID(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_PRINCIPALID extends BA.ResumableSub {
public ResumableSub_GET_PRINCIPALID(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
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
 //BA.debugLineNum = 367;BA.debugLine="If CMB_PRINCIPAL.cmbBox.SelectedIndex <> CMB_PRIN";
if (true) break;

case 1:
//if
this.state = 10;
if (parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()!=parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("ALL")) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 368;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM principal_table WHERE principal_name ='"+parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 369;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
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
 //BA.debugLineNum = 370;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 371;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 373;BA.debugLine="principal_acronym = cursor3.GetString(\"principa";
parent._principal_acronym = parent._cursor3.GetString("principal_acronym");
 //BA.debugLineNum = 374;BA.debugLine="principal_id = cursor3.GetString(\"principal_id\"";
parent._principal_id = parent._cursor3.GetString("principal_id");
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
 //BA.debugLineNum = 378;BA.debugLine="principal_acronym = \"ALL\"";
parent._principal_acronym = "ALL";
 //BA.debugLineNum = 379;BA.debugLine="principal_id = \"ALL\"";
parent._principal_id = "ALL";
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 381;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_transactionid() throws Exception{
ResumableSub_GET_TRANSACTIONID rsub = new ResumableSub_GET_TRANSACTIONID(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_TRANSACTIONID extends BA.ResumableSub {
public ResumableSub_GET_TRANSACTIONID(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
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
 //BA.debugLineNum = 383;BA.debugLine="Dim CExpDate As String = DateTime.GetYear(DateTim";
_cexpdate = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 384;BA.debugLine="Dim Cxmo As String = DateTime.GetMonth(DateTime.N";
_cxmo = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 385;BA.debugLine="Dim Cxday As String = DateTime.GetDayOfMonth(Date";
_cxday = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 386;BA.debugLine="Dim Cxtime As String = DateTime.GetHour(DateTime.";
_cxtime = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 387;BA.debugLine="Dim Cxmin As String = DateTime.GetMinute(DateTime";
_cxmin = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 388;BA.debugLine="Dim Cxsecs As String = DateTime.GetSecond(DateTim";
_cxsecs = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetSecond(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 389;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 390;BA.debugLine="transaction_id = principal_acronym&CExpDate&Cxmo&";
parent._transaction_id = parent._principal_acronym+_cexpdate+_cxmo+_cxday+_cxtime+_cxmin+_cxsecs+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 391;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 392;BA.debugLine="ToastMessageShow(transaction_id, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(parent._transaction_id),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 393;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 484;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 485;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 486;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 487;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 488;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 491;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 492;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 32;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 33;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 34;BA.debugLine="Private NameColumn(6) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (6)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 36;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 37;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private B4XTable1 As B4XTable";
mostCurrent._b4xtable1 = new wingan.app.b4xtable();
 //BA.debugLineNum = 40;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 43;BA.debugLine="Private PANEL_BG_NEW As Panel";
mostCurrent._panel_bg_new = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private LABEL_LOAD_INVDATE As Label";
mostCurrent._label_load_invdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private CMB_PRINCIPAL As B4XComboBox";
mostCurrent._cmb_principal = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 46;BA.debugLine="Private CMB_WAREHOUSE As B4XComboBox";
mostCurrent._cmb_warehouse = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 47;BA.debugLine="Private CMB_AREA As B4XComboBox";
mostCurrent._cmb_area = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 48;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 49;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private DateTemplate As B4XDateTemplate";
mostCurrent._datetemplate = new wingan.app.b4xdatetemplate();
 //BA.debugLineNum = 51;BA.debugLine="Private LABEL_NEW_HEADER As Label";
mostCurrent._label_new_header = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private BUTTON_CREATE As Button";
mostCurrent._button_create = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static void  _label_load_invdate_click() throws Exception{
ResumableSub_LABEL_LOAD_INVDATE_Click rsub = new ResumableSub_LABEL_LOAD_INVDATE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_INVDATE_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_INVDATE_Click(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
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
 //BA.debugLineNum = 357;BA.debugLine="Dialog.Title = \"Select Date\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Select Date");
 //BA.debugLineNum = 358;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)(""),(Object)("CANCEL")));
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 359;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 360;BA.debugLine="LABEL_LOAD_INVDATE.Text = DateTime.Date(DateTemp";
parent.mostCurrent._label_load_invdate.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.DateTime.Date(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 362;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static void  _load_invdate() throws Exception{
ResumableSub_LOAD_INVDATE rsub = new ResumableSub_LOAD_INVDATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_INVDATE extends BA.ResumableSub {
public ResumableSub_LOAD_INVDATE(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
Object[] _row = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 157;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 1;
;
 //BA.debugLineNum = 158;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 159;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 160;BA.debugLine="Dim rs As ResultSet = connection.ExecQuery(\"SELEC";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_ref_table GROUP BY transaction_id ORDER by inventory_date DESC")));
 //BA.debugLineNum = 161;BA.debugLine="Do While rs.NextRow";
if (true) break;

case 1:
//do while
this.state = 4;
while (_rs.NextRow()) {
this.state = 3;
if (true) break;
}
if (true) break;

case 3:
//C
this.state = 1;
 //BA.debugLineNum = 162;BA.debugLine="Dim row(6) As Object";
_row = new Object[(int) (6)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 163;BA.debugLine="row(0) = rs.GetString(\"inventory_date\")";
_row[(int) (0)] = (Object)(_rs.GetString("inventory_date"));
 //BA.debugLineNum = 164;BA.debugLine="row(1) = rs.GetString(\"principal_name\")";
_row[(int) (1)] = (Object)(_rs.GetString("principal_name"));
 //BA.debugLineNum = 165;BA.debugLine="row(2) = rs.GetString(\"warehouse\")";
_row[(int) (2)] = (Object)(_rs.GetString("warehouse"));
 //BA.debugLineNum = 168;BA.debugLine="row(3) = rs.GetString(\"area\")";
_row[(int) (3)] = (Object)(_rs.GetString("area"));
 //BA.debugLineNum = 169;BA.debugLine="row(4) = rs.GetString(\"user_info\")";
_row[(int) (4)] = (Object)(_rs.GetString("user_info"));
 //BA.debugLineNum = 170;BA.debugLine="row(5) = rs.GetString(\"transaction_status\")";
_row[(int) (5)] = (Object)(_rs.GetString("transaction_status"));
 //BA.debugLineNum = 171;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 173;BA.debugLine="rs.Close";
_rs.Close();
 //BA.debugLineNum = 174;BA.debugLine="B4XTable1.SetData(Data)";
parent.mostCurrent._b4xtable1._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 175;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 5:
//if
this.state = 8;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 7;
}if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 176;BA.debugLine="XSelections.Initialize(B4XTable1)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._b4xtable1);
 //BA.debugLineNum = 177;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_P";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_invdate_header() throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub LOAD_INVDATE_HEADER";
 //BA.debugLineNum = 147;BA.debugLine="NameColumn(0)=B4XTable1.AddColumn(\"Inventory Date";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._b4xtable1._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Inventory Date",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 148;BA.debugLine="NameColumn(1)=B4XTable1.AddColumn(\"Principal\", B4";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._b4xtable1._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Principal",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 149;BA.debugLine="NameColumn(2)=B4XTable1.AddColumn(\"Warehouse\", B4";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._b4xtable1._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Warehouse",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 150;BA.debugLine="NameColumn(3)=B4XTable1.AddColumn(\"Area\", B4XTabl";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._b4xtable1._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Area",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 151;BA.debugLine="NameColumn(4)=B4XTable1.AddColumn(\"User\", B4XTabl";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._b4xtable1._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("User",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 152;BA.debugLine="NameColumn(5)=B4XTable1.AddColumn(\"Status\", B4XTa";
mostCurrent._namecolumn[(int) (5)] = mostCurrent._b4xtable1._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Status",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 154;BA.debugLine="B4XTable1.NumberOfFrozenColumns = 1";
mostCurrent._b4xtable1._numberoffrozencolumns /*int*/  = (int) (1);
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 140;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 141;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 142;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 143;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 135;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 136;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 137;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 138;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_new_click() throws Exception{
 //BA.debugLineNum = 312;BA.debugLine="Sub PANEL_BG_NEW_Click";
 //BA.debugLineNum = 313;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 314;BA.debugLine="End Sub";
return "";
}
public static String  _populate_area() throws Exception{
 //BA.debugLineNum = 338;BA.debugLine="Sub POPULATE_AREA";
 //BA.debugLineNum = 339;BA.debugLine="CMB_AREA.cmbBox.Clear";
mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 340;BA.debugLine="CMB_AREA.cmbBox.DropdownTextColor = Colors.Black";
mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 341;BA.debugLine="CMB_AREA.cmbBox.TextColor = Colors.White";
mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 342;BA.debugLine="CMB_AREA.cmbBox.Add(\"WAREHOUSE 1\")";
mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WAREHOUSE 1");
 //BA.debugLineNum = 343;BA.debugLine="CMB_AREA.cmbBox.Add(\"WAREHOUSE 2\")";
mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WAREHOUSE 2");
 //BA.debugLineNum = 344;BA.debugLine="CMB_AREA.cmbBox.Add(\"WAREHOUSE 3\")";
mostCurrent._cmb_area._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("WAREHOUSE 3");
 //BA.debugLineNum = 345;BA.debugLine="End Sub";
return "";
}
public static void  _populate_principal() throws Exception{
ResumableSub_POPULATE_PRINCIPAL rsub = new ResumableSub_POPULATE_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_POPULATE_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_POPULATE_PRINCIPAL(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
int _i = 0;
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
 //BA.debugLineNum = 316;BA.debugLine="CMB_PRINCIPAL.cmbBox.Clear";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 317;BA.debugLine="CMB_PRINCIPAL.cmbBox.DropdownTextColor = Colors.B";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 318;BA.debugLine="CMB_PRINCIPAL.cmbBox.TextColor = Colors.White";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 319;BA.debugLine="CMB_PRINCIPAL.cmbBox.Add(\"ALL\")";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("ALL");
 //BA.debugLineNum = 320;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT principal_";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table ORDER BY principal_name ASC")));
 //BA.debugLineNum = 321;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step6 = 1;
limit6 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step6)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 322;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 323;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 324;BA.debugLine="CMB_PRINCIPAL.cmbBox.Add(cursor1.GetString(\"prin";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor1.GetString("principal_name"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 326;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _populate_warehouse() throws Exception{
ResumableSub_POPULATE_WAREHOUSE rsub = new ResumableSub_POPULATE_WAREHOUSE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_POPULATE_WAREHOUSE extends BA.ResumableSub {
public ResumableSub_POPULATE_WAREHOUSE(wingan.app.monthly_inventory_module parent) {
this.parent = parent;
}
wingan.app.monthly_inventory_module parent;
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
 //BA.debugLineNum = 328;BA.debugLine="CMB_WAREHOUSE.cmbBox.Clear";
parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 329;BA.debugLine="CMB_WAREHOUSE.cmbBox.DropdownTextColor = Colors.B";
parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 330;BA.debugLine="CMB_WAREHOUSE.cmbBox.TextColor = Colors.White";
parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 331;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT warehouse_";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT warehouse_name FROM warehouse_table ORDER BY warehouse_name ASC")));
 //BA.debugLineNum = 332;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step5 = 1;
limit5 = (int) (parent._cursor2.getRowCount()-1);
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
 //BA.debugLineNum = 333;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 334;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 335;BA.debugLine="CMB_WAREHOUSE.cmbBox.Add(cursor2.GetString(\"ware";
parent.mostCurrent._cmb_warehouse._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor2.GetString("warehouse_name"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 337;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
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
 //BA.debugLineNum = 20;BA.debugLine="Public transaction_id As String";
_transaction_id = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim principal_acronym As String";
_principal_acronym = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim principal_name As String";
_principal_name = "";
 //BA.debugLineNum = 25;BA.debugLine="Private plusBitmap As Bitmap";
_plusbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 459;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 460;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 461;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 462;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 463;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 464;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 465;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 466;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 467;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 468;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 469;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 480;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 481;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 482;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 483;BA.debugLine="End Sub";
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
