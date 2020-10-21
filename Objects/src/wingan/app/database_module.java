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

public class database_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static database_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.database_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (database_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.database_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.database_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (database_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (database_module) Resume **");
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
		return database_module.class;
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
            BA.LogInfo("** Activity (database_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (database_module) Pause event (activity is not paused). **");
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
            database_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (database_module) Resume **");
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
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollview1 = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_producttbl = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_warehouse = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_principal = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_expiration = null;
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
public ResumableSub_Activity_Create(wingan.app.database_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.database_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"database\")";
parent.mostCurrent._activity.LoadLayout("database",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 42;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 43;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 44;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 45;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 46;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 47;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 49;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 50;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 51;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 53;BA.debugLine="ScrollView1.Panel.LoadLayout(\"database_scrollview";
parent.mostCurrent._scrollview1.getPanel().LoadLayout("database_scrollview",mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="ScrollView1.Panel.Height = 92%y";
parent.mostCurrent._scrollview1.getPanel().setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (92),mostCurrent.activityBA));
 //BA.debugLineNum = 56;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 57;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 60;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 61;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 63;BA.debugLine="LOG_PRINCIPAL";
_log_principal();
 //BA.debugLineNum = 64;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 65;BA.debugLine="LOG_PRODUCTTBL";
_log_producttbl();
 //BA.debugLineNum = 66;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 67;BA.debugLine="LOG_WAREHOUSE";
_log_warehouse();
 //BA.debugLineNum = 68;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 69;BA.debugLine="LOG_EXPIRATION";
_log_expiration();
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 433;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 434;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 435;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 437;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 99;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 100;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 101;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 92;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 93;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 94;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 95;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return null;
}
public static void  _button_update_expiration_click() throws Exception{
ResumableSub_BUTTON_UPDATE_EXPIRATION_Click rsub = new ResumableSub_BUTTON_UPDATE_EXPIRATION_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_EXPIRATION_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_EXPIRATION_Click(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 426;BA.debugLine="Msgbox2Async(\"Are you sure to update your local e";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure to update your local expiration data?"),BA.ObjectToCharSequence("Sync Expiration Data"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 427;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 428;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 429;BA.debugLine="DOWNLOAD_RECEIVING_EXPIRATION";
_download_receiving_expiration();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 431;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static void  _button_update_principal_click() throws Exception{
ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click rsub = new ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 417;BA.debugLine="Msgbox2Async(\"Are you sure you want to update pri";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update principal data?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 5 seconds"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 418;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 419;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 420;BA.debugLine="UPDATE_PRINCIPAL";
_update_principal();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 424;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_update_producttbl_click() throws Exception{
ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click rsub = new ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 398;BA.debugLine="Msgbox2Async(\"Are you sure you want to update pro";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update product data?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 1-2 minutes"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 399;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 400;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 401;BA.debugLine="UPDATE_PRODUCTTBL";
_update_producttbl();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 406;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_update_warehouse_click() throws Exception{
ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click rsub = new ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 408;BA.debugLine="Msgbox2Async(\"Are you sure you want to update war";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update warehouse data?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 5 seconds"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 409;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 410;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 411;BA.debugLine="UPDATE_WAREHOUSE";
_update_warehouse();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 415;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 110;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 111;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 112;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 113;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 114;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 115;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 105;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 106;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 107;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,database_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 108;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 109;BA.debugLine="End Sub";
return null;
}
public static void  _download_audit_expiration() throws Exception{
ResumableSub_DOWNLOAD_AUDIT_EXPIRATION rsub = new ResumableSub_DOWNLOAD_AUDIT_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DOWNLOAD_AUDIT_EXPIRATION extends BA.ResumableSub {
public ResumableSub_DOWNLOAD_AUDIT_EXPIRATION(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 320;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 321;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_expi";
_cmd = _createcommand("select_expiration_audit",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 322;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 15;
return;
case 15:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 323;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 324;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 325;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 326;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("780936967",BA.NumberToString(2),0);
 //BA.debugLineNum = 327;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 328;BA.debugLine="For Each row() As Object In res.Rows";
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
 //BA.debugLineNum = 329;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_e";
parent._connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("document_ref_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("time_registered"))))])+"','AUDIT')");
 //BA.debugLineNum = 333;BA.debugLine="LABEL_MSGBOX2.Text = \"Downloading Expiration :";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Downloading Expiration : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 335;BA.debugLine="jr.Release";
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
 //BA.debugLineNum = 338;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 339;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 19;
return;
case 19:
//C
this.state = 14;
;
 //BA.debugLineNum = 340;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 341;BA.debugLine="Msgbox2Async(\"EXPIRATION TABLE IS NOT UPDATED.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("EXPIRATION TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 342;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 345;BA.debugLine="End Sub";
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
public ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 272;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 273;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 274;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_expi";
_cmd = _createcommand("select_expiration_receiving",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 275;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 276;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 277;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 278;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 4;
;
 //BA.debugLineNum = 279;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 280;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 281;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 282;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM product_exp";
parent._connection.ExecNonQuery("DELETE FROM product_expiration_ref_table");
 //BA.debugLineNum = 283;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 4;
;
 //BA.debugLineNum = 284;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 285;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 20;
return;
case 20:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 286;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("780871439",BA.NumberToString(2),0);
 //BA.debugLineNum = 287;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 288;BA.debugLine="For Each row() As Object In res.Rows";
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
 //BA.debugLineNum = 289;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_e";
parent._connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("dr_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("time_registered"))))])+"','RECEIVING')");
 //BA.debugLineNum = 293;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 22;
;
 //BA.debugLineNum = 294;BA.debugLine="LABEL_MSGBOX2.Text = \"Downloading Expiration :";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Downloading Expiration : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 296;BA.debugLine="DOWNLOAD_AUDIT_EXPIRATION";
_download_audit_expiration();
 //BA.debugLineNum = 297;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 13;
;
 //BA.debugLineNum = 298;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_u";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Expiration Table'");
 //BA.debugLineNum = 299;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 13;
;
 //BA.debugLineNum = 300;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_u";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Expiration Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 301;BA.debugLine="LABEL_MSGBOX2.Text = \"Expiration Downloaded Suc";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Expiration Downloaded Successfully"));
 //BA.debugLineNum = 302;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 26;
return;
case 26:
//C
this.state = 13;
;
 //BA.debugLineNum = 303;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 304;BA.debugLine="LOG_EXPIRATION";
_log_expiration();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 306;BA.debugLine="ToastMessageShow(\"No expiration data for this P";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No expiration data for this Principal"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 307;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 27;
return;
case 27:
//C
this.state = 13;
;
 //BA.debugLineNum = 308;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
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
 //BA.debugLineNum = 311;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 312;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 28;
return;
case 28:
//C
this.state = 16;
;
 //BA.debugLineNum = 313;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 314;BA.debugLine="Msgbox2Async(\"EXPIRATION TABLE IS NOT UPDATED.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("EXPIRATION TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 315;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
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
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 28;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private LABEL_LAST_PRODUCTTBL As Label";
mostCurrent._label_last_producttbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private LABEL_LAST_WAREHOUSE As Label";
mostCurrent._label_last_warehouse = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private LABEL_LAST_PRINCIPAL As Label";
mostCurrent._label_last_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private LABEL_LAST_EXPIRATION As Label";
mostCurrent._label_last_expiration = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static void  _log_expiration() throws Exception{
ResumableSub_LOG_EXPIRATION rsub = new ResumableSub_LOG_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_EXPIRATION extends BA.ResumableSub {
public ResumableSub_LOG_EXPIRATION(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 385;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Expiration Table'")));
 //BA.debugLineNum = 386;BA.debugLine="If cursor5.RowCount > 0 Then";
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
 //BA.debugLineNum = 387;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
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
 //BA.debugLineNum = 388;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 389;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 390;BA.debugLine="LABEL_LAST_EXPIRATION.Text = \"Last Update: \" & c";
parent.mostCurrent._label_last_expiration.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor5.GetString("date_time_updated")+" - "+parent._cursor5.GetString("updated_by")));
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
 //BA.debugLineNum = 395;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_principal() throws Exception{
ResumableSub_LOG_PRINCIPAL rsub = new ResumableSub_LOG_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_LOG_PRINCIPAL(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 373;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Principal Table'")));
 //BA.debugLineNum = 374;BA.debugLine="If cursor3.RowCount > 0 Then";
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
 //BA.debugLineNum = 375;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
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
 //BA.debugLineNum = 376;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 377;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 378;BA.debugLine="LABEL_LAST_PRINCIPAL.Text = \"Last Update: \" & c";
parent.mostCurrent._label_last_principal.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor3.GetString("date_time_updated")+" - "+parent._cursor3.GetString("updated_by")));
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
 //BA.debugLineNum = 383;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_producttbl() throws Exception{
ResumableSub_LOG_PRODUCTTBL rsub = new ResumableSub_LOG_PRODUCTTBL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_PRODUCTTBL extends BA.ResumableSub {
public ResumableSub_LOG_PRODUCTTBL(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 349;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Product Table'")));
 //BA.debugLineNum = 350;BA.debugLine="If cursor1.RowCount > 0 Then";
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
 //BA.debugLineNum = 351;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor1.getRowCount()-1);
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
 //BA.debugLineNum = 352;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 353;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 354;BA.debugLine="LABEL_LAST_PRODUCTTBL.Text = \"Last Update: \" &";
parent.mostCurrent._label_last_producttbl.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor1.GetString("date_time_updated")+" - "+parent._cursor1.GetString("updated_by")));
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
 //BA.debugLineNum = 359;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_warehouse() throws Exception{
ResumableSub_LOG_WAREHOUSE rsub = new ResumableSub_LOG_WAREHOUSE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_WAREHOUSE extends BA.ResumableSub {
public ResumableSub_LOG_WAREHOUSE(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
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
 //BA.debugLineNum = 361;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Warehouse Table'")));
 //BA.debugLineNum = 362;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 363;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
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
 //BA.debugLineNum = 364;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 365;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 366;BA.debugLine="LABEL_LAST_WAREHOUSE.Text = \"Last Update: \" & c";
parent.mostCurrent._label_last_warehouse.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor2.GetString("date_time_updated")+" - "+parent._cursor2.GetString("updated_by")));
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
 //BA.debugLineNum = 371;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 80;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 81;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 82;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 83;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 84;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 85;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 86;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 87;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 88;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 89;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static void  _update_principal() throws Exception{
ResumableSub_UPDATE_PRINCIPAL rsub = new ResumableSub_UPDATE_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_UPDATE_PRINCIPAL(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
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
 //BA.debugLineNum = 227;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 228;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 229;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_prin";
_cmd = _createcommand("select_principal",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 230;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 231;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 232;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 233;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 234;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 235;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 236;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM principal_t";
parent._connection.ExecNonQuery("DELETE FROM principal_table");
 //BA.debugLineNum = 237;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 238;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 239;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 241;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("780805903",BA.NumberToString(2),0);
 //BA.debugLineNum = 242;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group15 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index15 = 0;
groupLen15 = group15.getSize();
this.state = 14;
if (true) break;

case 14:
//C
this.state = 7;
if (index15 < groupLen15) {
this.state = 6;
_row = (Object[])(group15.Get(index15));}
if (true) break;

case 15:
//C
this.state = 14;
index15++;
if (true) break;

case 6:
//C
this.state = 15;
 //BA.debugLineNum = 243;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO principal_";
parent._connection.ExecNonQuery("INSERT INTO principal_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_acronym"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("mai_acronym"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_address"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("contact_person"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_tin"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_logo"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("trade_discount"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("office_phone_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("fax_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("mobile_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("active_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("modified_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("edit_number"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("user_info"))))])+"')");
 //BA.debugLineNum = 249;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 //BA.debugLineNum = 250;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Principal : \" &";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Principal : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 253;BA.debugLine="LABEL_MSGBOX2.Text = \"Data Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data Updated Successfully"));
 //BA.debugLineNum = 254;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 17;
return;
case 17:
//C
this.state = 10;
;
 //BA.debugLineNum = 255;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 256;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 10;
;
 //BA.debugLineNum = 257;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_up";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Principal Table'");
 //BA.debugLineNum = 258;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 10;
;
 //BA.debugLineNum = 259;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_up";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Principal Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 260;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 10;
;
 //BA.debugLineNum = 261;BA.debugLine="LOG_PRINCIPAL";
_log_principal();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 263;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 264;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 21;
return;
case 21:
//C
this.state = 10;
;
 //BA.debugLineNum = 265;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 266;BA.debugLine="Msgbox2Async(\"PRINCIPAL DATABASE IS NOT UPDATED.";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("PRINCIPAL DATABASE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 267;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 269;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _update_producttbl() throws Exception{
ResumableSub_UPDATE_PRODUCTTBL rsub = new ResumableSub_UPDATE_PRODUCTTBL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_PRODUCTTBL extends BA.ResumableSub {
public ResumableSub_UPDATE_PRODUCTTBL(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
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
 //BA.debugLineNum = 118;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 119;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 120;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_prod";
_cmd = _createcommand("select_product_table",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 121;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 122;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 123;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 124;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 125;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 126;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 127;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 128;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM product_tab";
parent._connection.ExecNonQuery("DELETE FROM product_table");
 //BA.debugLineNum = 129;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 4;
;
 //BA.debugLineNum = 130;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 131;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 14;
return;
case 14:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 132;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("780674831",BA.NumberToString(2),0);
 //BA.debugLineNum = 133;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group16 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index16 = 0;
groupLen16 = group16.getSize();
this.state = 15;
if (true) break;

case 15:
//C
this.state = 7;
if (index16 < groupLen16) {
this.state = 6;
_row = (Object[])(group16.Get(index16));}
if (true) break;

case 16:
//C
this.state = 15;
index16++;
if (true) break;

case 6:
//C
this.state = 16;
 //BA.debugLineNum = 134;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_ta";
parent._connection.ExecNonQuery("INSERT INTO product_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_category"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("brand_img"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_brand"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_desc_img"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_desc"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("expiration_date"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("case_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("box_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("bag_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("pack_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("weight"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit_weight"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("metric_tons"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("flag_deleted"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prod_status"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("promo_product"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("life_span_year"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("life_span_month"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("default_expiration_date_reading"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("inner_piece"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("category_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("datetime_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_audit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("length_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("height_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("width_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("effective_price_date"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_RCS_CHAIN"))))])+"')");
 //BA.debugLineNum = 162;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 17;
return;
case 17:
//C
this.state = 16;
;
 //BA.debugLineNum = 163;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Product : \" & ro";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Product : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_desc"))))])));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 165;BA.debugLine="LABEL_MSGBOX2.Text = \"Data Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data Updated Successfully"));
 //BA.debugLineNum = 166;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 18;
return;
case 18:
//C
this.state = 10;
;
 //BA.debugLineNum = 167;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 168;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 10;
;
 //BA.debugLineNum = 169;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_up";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Product Table'");
 //BA.debugLineNum = 170;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 10;
;
 //BA.debugLineNum = 171;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_up";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Product Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 172;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 10;
;
 //BA.debugLineNum = 173;BA.debugLine="LOG_PRODUCTTBL";
_log_producttbl();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 175;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 176;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 22;
return;
case 22:
//C
this.state = 10;
;
 //BA.debugLineNum = 177;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 178;BA.debugLine="Msgbox2Async(\"PRODUCT TABLE IS NOT UPDATED.\" & C";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("PRODUCT TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 179;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 181;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _update_warehouse() throws Exception{
ResumableSub_UPDATE_WAREHOUSE rsub = new ResumableSub_UPDATE_WAREHOUSE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_WAREHOUSE extends BA.ResumableSub {
public ResumableSub_UPDATE_WAREHOUSE(wingan.app.database_module parent) {
this.parent = parent;
}
wingan.app.database_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
int _gr = 0;
Object[] _row = null;
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
 //BA.debugLineNum = 184;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 185;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 186;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_ware";
_cmd = _createcommand("select_warehouse",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 187;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 188;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 189;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 190;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 191;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 192;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 193;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM warehouse_t";
parent._connection.ExecNonQuery("DELETE FROM warehouse_table");
 //BA.debugLineNum = 194;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 195;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 196;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 198;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("780740367",BA.NumberToString(2),0);
 //BA.debugLineNum = 200;BA.debugLine="Dim gr As Int = 0";
_gr = (int) (0);
 //BA.debugLineNum = 201;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group16 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index16 = 0;
groupLen16 = group16.getSize();
this.state = 14;
if (true) break;

case 14:
//C
this.state = 7;
if (index16 < groupLen16) {
this.state = 6;
_row = (Object[])(group16.Get(index16));}
if (true) break;

case 15:
//C
this.state = 14;
index16++;
if (true) break;

case 6:
//C
this.state = 15;
 //BA.debugLineNum = 202;BA.debugLine="gr = gr + 1";
_gr = (int) (_gr+1);
 //BA.debugLineNum = 203;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO warehouse_";
parent._connection.ExecNonQuery("INSERT INTO warehouse_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("warehouse_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("warehouse_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("assigned_modules"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("modified_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("edit_number"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("user_info"))))])+"')");
 //BA.debugLineNum = 206;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Warehouse : \" &";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Warehouse : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("warehouse_name"))))])));
 //BA.debugLineNum = 207;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 209;BA.debugLine="LABEL_MSGBOX2.Text = \"Data Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data Updated Successfully"));
 //BA.debugLineNum = 210;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 17;
return;
case 17:
//C
this.state = 10;
;
 //BA.debugLineNum = 211;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 212;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_up";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Warehouse Table'");
 //BA.debugLineNum = 213;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 10;
;
 //BA.debugLineNum = 214;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_up";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Warehouse Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 215;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 10;
;
 //BA.debugLineNum = 216;BA.debugLine="LOG_WAREHOUSE";
_log_warehouse();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 218;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 219;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 20;
return;
case 20:
//C
this.state = 10;
;
 //BA.debugLineNum = 220;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 221;BA.debugLine="Msgbox2Async(\"WAREHOUSE DATABASE IS NOT UPDATED.";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("WAREHOUSE DATABASE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 222;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 224;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
}
